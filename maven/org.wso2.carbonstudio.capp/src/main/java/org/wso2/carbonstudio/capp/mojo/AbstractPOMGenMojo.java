/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbonstudio.capp.mojo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.wso2.carbonstudio.capp.model.Artifact;
import org.wso2.carbonstudio.capp.model.ArtifactDependency;
import org.wso2.carbonstudio.capp.utils.CAppUtils;
import org.wso2.carbonstudio.capp.utils.MavenUtils;

public abstract class AbstractPOMGenMojo extends AbstractMojo {

	public MavenProject project;

	public MavenProjectHelper projectHelper;

	public File outputLocation;

	public File artifactLocation;
	
	public File moduleProject;
	
	public String groupId;
	
	public String typeList;
	
	private MavenProject mavenModuleProject;

	
	public void execute() throws MojoExecutionException, MojoFailureException {
	
		List<Artifact> artifacts = retrieveArtifacts();
		
		processArtifacts(artifacts);

	}

	protected abstract String getArtifactType();
	
	private void processArtifacts(List<Artifact> artifacts)
			throws MojoExecutionException {
		for (Artifact artifact : artifacts) {
			if (artifact.getType().equalsIgnoreCase(getArtifactType())) {
				getLog().info("Creating maven project for artifact "+artifact.getName()+":"+artifact.getVersion()+"...");
				getLog().info("\tgenerating maven project...");

				File projectLocation = new File(getOutputLocation(), artifact.getName());
				projectLocation.mkdirs();
				
				
				MavenProject artifactMavenProject = createMavenProjectForSequenceArtifact(artifact, artifacts);
				
				try {
					getLog().info("\tcopying resources...");
					getMavenModuleProject().getModules().add(MavenUtils.getMavenModuleRelativePath(getModuleProject(),projectLocation));
					MavenUtils.saveMavenProject(artifactMavenProject, new File(projectLocation, "pom.xml"));
					MavenUtils.saveMavenProject(getMavenModuleProject(), getModuleProject());
					copyResources(artifactMavenProject, projectLocation, artifact);
				} catch (Exception e) {
					throw new MojoExecutionException(
							"Error creating maven project for artifact '"
									+ artifact.getName() + "'", e);
				}
			}
		}
	}

	protected abstract void copyResources(MavenProject project, File projectLocation, Artifact artifact)throws IOException;

	private MavenProject createMavenProjectForSequenceArtifact(Artifact artifact, List<Artifact> artifacts)
			throws MojoExecutionException {
		MavenProject artifactMavenProject = MavenUtils.createMavenProject(artifact, getGroupId(),getArtifactType());
		//Adding & configuring the plugin section
		addPlugins(artifactMavenProject, artifact);
		
		addMavenDependencies(artifactMavenProject, artifact, artifacts);
		
		return artifactMavenProject;
	}

	protected void addMavenDependencies(MavenProject artifactMavenProject,
			Artifact artifact, List<Artifact> artifacts)
			throws MojoExecutionException {
		List<ArtifactDependency> dependencies = artifact.getDependencies();
		for (ArtifactDependency dependency : dependencies) {
			String dGroupId=getGroupId();
			String dArtifactId = dependency.getName();
			String dVersion = dependency.getVersion();
			String scope = "capp";
			String type = getExtensionOfDependency(artifacts, dependency);
			addMavenDependency(artifactMavenProject, dGroupId, dArtifactId,
					dVersion, scope, type);
		}
	}

	protected void addMavenDependency(MavenProject artifactMavenProject,
			String dGroupId, String dArtifactId, String dVersion, String scope,
			String type) {
		Dependency mavenDependency = new Dependency();
		mavenDependency.setGroupId(dGroupId);
		mavenDependency.setArtifactId(dArtifactId);
		mavenDependency.setVersion(dVersion);
		mavenDependency.setScope(scope);
		if (type!=null) {
			mavenDependency.setType(type);
		}
		artifactMavenProject.getDependencies().add(mavenDependency);
	}

	protected String getExtensionOfDependency(List<Artifact> artifacts,
			ArtifactDependency dependency) {
		String type = null;
		String artifactType = null;
		for (Artifact existingArtifact : artifacts) {
			if (existingArtifact.getName().equals(dependency.getName()) && existingArtifact.getVersion().equals(dependency.getVersion())){
				artifactType=existingArtifact.getType();
			}
		}
		if (artifactType!=null && getTypeList().containsKey(artifactType)){
			type = getTypeList().get(artifactType);
		}
		return type;
	}

	protected abstract void addPlugins(MavenProject artifactMavenProject, Artifact artifact);

	private List<Artifact> retrieveArtifacts() {
		return CAppUtils.retrieveArtifacts(getArtifactLocation());
	}

	private MavenProject getMavenModuleProject() throws MojoExecutionException{
		if (mavenModuleProject==null) {
			try {
				if (!getModuleProject().exists()) {
					if (groupId==null){
						groupId=project.getGroupId();
					}
					mavenModuleProject = MavenUtils.createMavenProject(groupId,
							getProject().getArtifactId() + "_module", getProject()
									.getVersion(), "pom");
					MavenUtils.saveMavenProject(mavenModuleProject, getModuleProject());
				}
				mavenModuleProject=MavenUtils.getMavenProject(getModuleProject());
			} catch (Exception e) {
				throw new MojoExecutionException(
						"Error retrieving module parent project: "
								+ getModuleProject().toString(), e);
			}
		}
		return mavenModuleProject;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupId() throws MojoExecutionException {
		if (groupId==null){
			groupId=getMavenModuleProject().getGroupId();
		}
		return groupId;
	}

	public void setModuleProject(File moduleProject) {
		this.moduleProject = moduleProject;
	}

	public File getModuleProject() {
		if (!moduleProject.getName().equalsIgnoreCase("pom.xml")){
			moduleProject=new File(moduleProject,"pom.xml");
		}
		return moduleProject;
	}

	public void setArtifactLocation(File artifactLocation) {
		this.artifactLocation = artifactLocation;
	}

	public File getArtifactLocation() {
		return artifactLocation;
	}

	public void setOutputLocation(File outputLocation) {
		this.outputLocation = outputLocation;
	}

	public File getOutputLocation() {
		if (!outputLocation.exists()) {
			outputLocation.mkdirs();
		}
		return outputLocation;
	}
	public void setProject(MavenProject project) {
		this.project = project;
	}
	public MavenProject getProject() {
		return project;
	}

	public void setTypeList(String typeList) {
		this.typeList = typeList;
	}

	public Map<String,String> getTypeList() {
		Map<String,String> types=new HashMap<String, String>();
		if (typeList!=null) {
			String[] typeSet = typeList.split(",");
			for (String type : typeSet) {
				String[] typeData = type.split("=");
				types.put(typeData[0], typeData[1]);
			}
		}
		return types;
	}

	
}
