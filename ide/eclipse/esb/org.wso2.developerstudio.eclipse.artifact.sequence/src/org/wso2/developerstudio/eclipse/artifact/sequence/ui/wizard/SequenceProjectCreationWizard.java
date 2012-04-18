/*
 * Copyright (c) 2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.artifact.sequence.ui.wizard;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Repository;
import org.apache.maven.model.RepositoryPolicy;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.wso2.developerstudio.eclipse.platform.core.registry.util.RegistryResourceInfo;
import org.wso2.developerstudio.eclipse.platform.core.registry.util.RegistryResourceInfoDoc;
import org.wso2.developerstudio.eclipse.platform.core.registry.util.RegistryResourceUtils;
import static org.wso2.developerstudio.eclipse.platform.core.registry.util.Constants.*;
import org.wso2.developerstudio.eclipse.artifact.sequence.Activator;
import org.wso2.developerstudio.eclipse.artifact.sequence.model.SequenceModel;
import org.wso2.developerstudio.eclipse.artifact.sequence.utils.SequenceImageUtils;
import org.wso2.developerstudio.eclipse.esb.project.artifact.ESBArtifact;
import org.wso2.developerstudio.eclipse.esb.project.artifact.ESBProjectArtifact;
import org.wso2.developerstudio.eclipse.general.project.artifact.GeneralProjectArtifact;
import org.wso2.developerstudio.eclipse.general.project.artifact.RegistryArtifact;
import org.wso2.developerstudio.eclipse.general.project.artifact.bean.RegistryElement;
import org.wso2.developerstudio.eclipse.general.project.artifact.bean.RegistryItem;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.maven.util.MavenUtils;
import org.wso2.developerstudio.eclipse.platform.core.templates.ArtifactTemplate;
import org.wso2.developerstudio.eclipse.platform.core.templates.ArtifactTemplateHandler;
import org.wso2.developerstudio.eclipse.platform.ui.wizard.AbstractWSO2ProjectCreationWizard;
import org.wso2.developerstudio.eclipse.utils.file.FileUtils;

public class SequenceProjectCreationWizard extends AbstractWSO2ProjectCreationWizard {
	
	private static IDeveloperStudioLog log=Logger.getLog(Activator.PLUGIN_ID);
	
	private SequenceModel seqModel;
	private static final String SEQ_WIZARD_WINDOW_TITLE = "Create New Sequence";
	private ESBProjectArtifact esbProjectArtifact;
	private List<File> fileLst = new ArrayList<File>();
	private IProject project;

	public SequenceProjectCreationWizard() {
		this.seqModel = new SequenceModel();
		setModel(this.seqModel);
		setWindowTitle(SEQ_WIZARD_WINDOW_TITLE);
		setDefaultPageImageDescriptor(SequenceImageUtils.getInstance().getImageDescriptor("seq-wizard.png"));
	}
	
	protected boolean isRequireProjectLocationSection() {
		return false;
	}
	
	protected boolean isRequiredWorkingSet() {
	  return false;
	}
	
	public boolean performFinish() {
		try {
			seqModel = (SequenceModel)getModel();
			project = seqModel.getSequenceSaveLocation().getProject();
			if(seqModel.isSaveAsDynamic()){
				createDynamicSequenceArtifact(project,seqModel);
			} else{
				if(!createSequenceArtifact(project,seqModel)){
					return false;
				}
			}
			
			project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			
			if(fileLst.size()>0){
				openEditor(fileLst.get(0));
			}
			
		} catch (CoreException e) {
			log.error("CoreException has occurred", e);
		} catch (Exception e) {
			log.error("An unexpected error has occurred", e);
		}

		return true;
	}
	
	public void updatePom() throws Exception{
		File mavenProjectPomLocation = project.getFile("pom.xml").getLocation().toFile();
		MavenProject mavenProject = MavenUtils.getMavenProject(mavenProjectPomLocation);

		List<Plugin> plugins = mavenProject.getBuild().getPlugins();
		
		for(Plugin plg:plugins){
			if(plg.getId().equals("wso2-esb-sequence-plugin")){
				return ;
			}
		}
		
		Plugin plugin = MavenUtils.createPluginEntry(mavenProject, "org.wso2.maven", "wso2-esb-sequence-plugin", "1.0.5", true);
		
		PluginExecution pluginExecution = new PluginExecution();
		pluginExecution.addGoal("pom-gen");
		pluginExecution.setPhase("process-resources");
		pluginExecution.setId("sequence");
		
		Xpp3Dom configurationNode = MavenUtils.createMainConfigurationNode();
		Xpp3Dom artifactLocationNode = MavenUtils.createXpp3Node(configurationNode, "artifactLocation");
		artifactLocationNode.setValue(".");
		Xpp3Dom typeListNode = MavenUtils.createXpp3Node(configurationNode, "typeList");
		typeListNode.setValue("${artifact.types}");
		pluginExecution.setConfiguration(configurationNode);
		
		plugin.addExecution(pluginExecution);
		Repository repo = new Repository();
		repo.setUrl("http://maven.wso2.org/nexus/content/groups/wso2-public/");
		repo.setId("wso2-nexus");
		
		RepositoryPolicy releasePolicy=new RepositoryPolicy();
		releasePolicy.setEnabled(true);
		releasePolicy.setUpdatePolicy("daily");
		releasePolicy.setChecksumPolicy("ignore");
		
		repo.setReleases(releasePolicy);
		
		if (!mavenProject.getRepositories().contains(repo)) {
	        mavenProject.getModel().addRepository(repo);
	        mavenProject.getModel().addPluginRepository(repo);
        }
		
		MavenUtils.saveMavenProject(mavenProject, mavenProjectPomLocation);
	}

	private boolean createSequenceArtifact(IProject prj,SequenceModel sequenceModel) throws Exception {
        boolean isNewArtifact =true;
		IContainer location = project.getFolder("src" + File.separator + "main"
				+ File.separator + "synapse-config" + File.separator
				+ "sequences");

		// Adding the metadata about the sequence to the metadata store.
		esbProjectArtifact = new ESBProjectArtifact();
		esbProjectArtifact.fromFile(project.getFile("artifact.xml")
				.getLocation().toFile());

		if (getModel().getSelectedOption().equals("import.sequence")) {
			IFile sequence = location.getFile(new Path(getModel().getImportFile().getName()));
			if(sequence.exists()){
				if(!MessageDialog.openQuestion(getShell(), "WARNING", "Do you like to override exsiting project in the workspace")){
					return false;	
				}
				isNewArtifact = false;
			} 	
			copyImportFile(location,isNewArtifact);
		} else {
			// Map<String,List<String>> filters=new HashMap<String,List<String>>
			// ();
			// DeveloperStudioProviderUtils.addFilter(filters,
			// CSProviderConstants.FILTER_MEDIA_TYPE,
			// ESBMediaTypeConstants.MEDIA_TYPE_SEQUENCE);
			ArtifactTemplate selectedTemplate = ArtifactTemplateHandler
					.getArtifactTemplates("org.wso2.developerstudio.eclipse.esb.sequence");
			String templateContent = FileUtils
					.getContentAsString(selectedTemplate
							.getTemplateDataStream());
			String content = createSequenceTemplate(templateContent);
			File destFile = new File(location.getLocation().toFile(),
					sequenceModel.getSequenceName() + ".xml");
			FileUtils.createFile(destFile, content);
			fileLst.add(destFile);
			ESBArtifact artifact = new ESBArtifact();
			artifact.setName(sequenceModel.getSequenceName());
			artifact.setVersion("1.0.0");
			artifact.setType("synapse/sequence");
			artifact.setServerRole("EnterpriseServiceBus");
			artifact.setFile(FileUtils.getRelativePath(project.getLocation()
					.toFile(), new File(location.getLocation().toFile(),
					sequenceModel.getSequenceName() + ".xml")));
			esbProjectArtifact.addESBArtifact(artifact);
		}

		File pomfile = project.getFile("pom.xml").getLocation().toFile();
		getModel().getMavenInfo().setPackageName("synapse/sequence");
		if (!pomfile.exists()) {
			createPOM(pomfile);
		}

		updatePom();
		esbProjectArtifact.toFile();
		return true;
	}
	
	private void createDynamicSequenceArtifact(IContainer location,SequenceModel sequenceModel) throws Exception{
		String registryPath = sequenceModel.getDynamicSeqRegistryPath()
				.replaceAll("^conf:", "/_system/config")
				.replaceAll("^gov:", "/_system/governance")
				.replaceAll("^local:", "/_system/local");
		RegistryResourceInfoDoc regResInfoDoc = new RegistryResourceInfoDoc();

		ArtifactTemplate selectedTemplate = ArtifactTemplateHandler
				.getArtifactTemplates("org.wso2.developerstudio.eclipse.esb.sequence");
		String templateContent = FileUtils.getContentAsString(selectedTemplate
				.getTemplateDataStream());
		String content = createSequenceTemplate(templateContent);
		File destFile = new File(location.getLocation().toFile(),
				sequenceModel.getSequenceName() + ".xml");
		FileUtils.createFile(destFile, content);
		fileLst.add(destFile);
		RegistryResourceUtils.createMetaDataForFolder(registryPath, location
				.getLocation().toFile());
		RegistryResourceUtils.addRegistryResourceInfo(destFile, regResInfoDoc,
				project.getLocation().toFile(), registryPath);
		
		
		GeneralProjectArtifact generalProjectArtifact=new GeneralProjectArtifact();
		generalProjectArtifact.fromFile(project.getFile("artifact.xml").getLocation().toFile());
		
		RegistryArtifact artifact=new RegistryArtifact();
		artifact.setName(sequenceModel.getSequenceName());
		artifact.setVersion("1.0.0");
		artifact.setType("registry/resource");
		artifact.setServerRole("EnterpriseServiceBus");
		List<RegistryResourceInfo> registryResources = regResInfoDoc.getRegistryResources();
		for (RegistryResourceInfo registryResourceInfo : registryResources) {
			RegistryElement item = null;
			if (registryResourceInfo.getType() == REGISTRY_RESOURCE) {
				item = new RegistryItem();
				((RegistryItem) item).setFile(registryResourceInfo.getResourceBaseRelativePath());
			} 
			item.setPath(registryResourceInfo.getDeployPath().replaceAll("/$",""));
			artifact.addRegistryElement(item);
        }
		generalProjectArtifact.addArtifact(artifact);
		generalProjectArtifact.toFile();
		addGeneralProjectPlugin(project);
	}
	
	private void addGeneralProjectPlugin(IProject project) throws Exception{
		MavenProject mavenProject;
		
		File mavenProjectPomLocation = project.getFile("pom.xml").getLocation().toFile();
		if(!mavenProjectPomLocation.exists()){
			mavenProject = MavenUtils.createMavenProject("org.wso2.carbon." + project.getName(), project.getName(), "1.0.0","pom");
		} else {
			mavenProject = MavenUtils.getMavenProject(mavenProjectPomLocation);
		}
		
		List<Plugin> plugins = mavenProject.getBuild().getPlugins();
		
		for(Plugin plg:plugins){
			if(plg.getArtifactId().equals("wso2-general-project-plugin")){
				return ;
			}
		}
		
		mavenProject = MavenUtils.getMavenProject(mavenProjectPomLocation);
		Plugin plugin = MavenUtils.createPluginEntry(mavenProject, "org.wso2.maven", "wso2-general-project-plugin", "1.0.5", true);
		
		PluginExecution pluginExecution;
		
		pluginExecution = new PluginExecution();
		pluginExecution.addGoal("pom-gen");
		pluginExecution.setPhase("process-resources");
		pluginExecution.setId("registry");
		plugin.addExecution(pluginExecution);
		
		Xpp3Dom configurationNode = MavenUtils.createMainConfigurationNode();
		Xpp3Dom artifactLocationNode = MavenUtils.createXpp3Node(configurationNode, "artifactLocation");
		artifactLocationNode.setValue(".");
		Xpp3Dom typeListNode = MavenUtils.createXpp3Node(configurationNode, "typeList");
		typeListNode.setValue("${artifact.types}");
		pluginExecution.setConfiguration(configurationNode);
		
		Repository repo = new Repository();
		repo.setUrl("http://dist.wso2.org/maven2");
		repo.setId("wso2-maven2-repository-1");
		
		Repository repo1 = new Repository();
		repo1.setUrl("http://maven.wso2.org/nexus/content/groups/wso2-public/");
		repo1.setId("wso2-nexus-maven2-repository-1");
		
		if (!mavenProject.getRepositories().contains(repo)) {
	        mavenProject.getModel().addRepository(repo);
	        mavenProject.getModel().addPluginRepository(repo);
        }

		if (!mavenProject.getRepositories().contains(repo1)) {
	        mavenProject.getModel().addRepository(repo1);
	        mavenProject.getModel().addPluginRepository(repo1);
        }
		
		MavenUtils.saveMavenProject(mavenProject, mavenProjectPomLocation);
	}

	public void copyImportFile(IContainer importLocation,boolean isNewAritfact) throws IOException {
		File importFile = getModel().getImportFile();
		File destFile = null;
		List<OMElement> selectedSeqList = ((SequenceModel)getModel()).getSelectedSeqList();
		if(selectedSeqList != null && selectedSeqList.size() >0 ){
			for (OMElement element : selectedSeqList) {
				String name = element.getAttributeValue(new QName("name"));
				destFile = new File(importLocation.getLocation().toFile(), name + ".xml");
				FileUtils.createFile(destFile, element.toString());
				fileLst.add(destFile);
				if(isNewAritfact){
				ESBArtifact artifact=new ESBArtifact();
				artifact.setName(name);
				artifact.setVersion("1.0.0");
				artifact.setType("synapse/sequence");
				artifact.setServerRole("EnterpriseServiceBus");
				artifact.setFile(FileUtils.getRelativePath(importLocation.getProject().getLocation().toFile(), new File(importLocation.getLocation().toFile(),name+".xml")));
				esbProjectArtifact.addESBArtifact(artifact);
				}
			} 
			
		}else{
			destFile = new File(importLocation.getLocation().toFile(), importFile.getName());
			FileUtils.copy(importFile, destFile);
			fileLst.add(destFile);
			String name = importFile.getName().replaceAll(".xml$","");
			if(isNewAritfact){
			ESBArtifact artifact=new ESBArtifact();
			artifact.setName(name);
			artifact.setVersion("1.0.0");
			artifact.setType("synapse/sequence");
			artifact.setServerRole("EnterpriseServiceBus");
			artifact.setFile(FileUtils.getRelativePath(importLocation.getProject().getLocation().toFile(), new File(importLocation.getLocation().toFile(),name+".xml")));
			esbProjectArtifact.addESBArtifact(artifact);
			}
		}
		
		
	}
	
	public String createSequenceTemplate(String templateContent) throws IOException{
//		String defaultNS = ESBPreferenceData.getDefaultNamesapce();
//		if(defaultNS.equals("") || defaultNS == null){
//			defaultNS = SynapseConstants.NS_1_4;
//		}
		String content = "";
		if(!seqModel.getSelectedEP().equals("")){
			String contentWithoutClosingTag = templateContent.substring(0, templateContent.length()-2);
			contentWithoutClosingTag = contentWithoutClosingTag.concat(seqModel.getSelectedEP());
			content = MessageFormat.format(contentWithoutClosingTag,seqModel.getSequenceName(),seqModel.getOnErrorSequence());
		}else{
			content = MessageFormat.format(templateContent,seqModel.getSequenceName(),seqModel.getOnErrorSequence());
		}
        return content;
	}

	
	public IResource getCreatedResource() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void openEditor(File file){
		try {
			IFile dbsFile  = ResourcesPlugin
			.getWorkspace()
			.getRoot()
			.getFileForLocation(
					Path.fromOSString(file.getAbsolutePath()));
			IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),dbsFile);
		} catch (Exception e) { /* ignore */}
	}

}
