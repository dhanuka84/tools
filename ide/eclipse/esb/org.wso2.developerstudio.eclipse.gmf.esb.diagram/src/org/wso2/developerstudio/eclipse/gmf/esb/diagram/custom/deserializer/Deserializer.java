/*
 * Copyright 2012 WSO2, Inc. (http://wso2.com)
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

package org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.deserializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.synapse.config.Entry;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.config.SynapseConfigurationBuilder;
import org.apache.synapse.core.axis2.ProxyService;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.Template;
import org.apache.synapse.mediators.base.SequenceMediator;
import org.apache.synapse.mediators.template.TemplateMediator;
import org.apache.synapse.rest.API;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbDiagram;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbElement;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbNode;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbServer;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.Activator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.parts.EsbServerEditPart;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.part.EsbDiagramEditor;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.wso2.developerstudio.eclipse.gmf.esb.ArtifactType;
import org.apache.synapse.config.xml.ProxyServiceFactory;
import org.apache.synapse.config.xml.rest.APIFactory;
import org.apache.synapse.config.xml.SequenceMediatorFactory;
import org.apache.synapse.config.xml.EntryFactory;
import org.apache.synapse.config.xml.endpoints.EndpointFactory;
import org.apache.synapse.task.TaskDescription;
import org.apache.synapse.task.TaskDescriptionFactory;

/**
 * Synapse model de-serialize base class
 */
public class Deserializer {
	/**
	 * Singleton instance.
	 */
	private static Deserializer singleton;
	
	/**
	 * DeveloperStudio logger
	 * */
	private static IDeveloperStudioLog log=Logger.getLog(Activator.PLUGIN_ID);
	
	private Deserializer(){
		
	}
	
	/**
	 * @return singleton instance.
	 */
	public static Deserializer getInstance() {
		if (null == singleton) {
			singleton = new Deserializer();
		}
		return singleton;
	}
	
	/**
	 * Update graphical model
	 * @param source
	 * @param graphicalEditor
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateDesign(String source, EsbDiagramEditor graphicalEditor) throws Exception {
		EsbDeserializerRegistry.getInstance().init(graphicalEditor);
		Diagram diagram = graphicalEditor.getDiagram();
		EsbDiagram esbDiagram = (EsbDiagram) diagram.getElement();
		EsbServer esbServer = esbDiagram.getServer();
		CompoundCommand resultCommand = new CompoundCommand();

		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(esbServer);
		// cleaning old diagram
		// TODO: should be replaced by better approach
		for (EsbElement child : esbServer.getChildren()) {
			RemoveCommand removeCmd = new RemoveCommand(domain, esbServer,
					EsbPackage.Literals.ESB_SERVER__CHILDREN, child);
			resultCommand.append(removeCmd);
		}

		if (resultCommand.canExecute()) {
			domain.getCommandStack().execute(resultCommand);
		}

	//	AddCommand addCmd = null;

		Map<String, Object> artifacts = getArtifacts(source);
		for (Map.Entry<String, Object> artifact : artifacts.entrySet()) {
			@SuppressWarnings("rawtypes")
			IEsbNodeDeserializer deserializer = EsbDeserializerRegistry.getInstance()
					.getDeserializer(artifact.getValue());
			AbstractEsbNodeDeserializer.refreshEditPartMap();
			EditPart editpart = AbstractEsbNodeDeserializer.getEditpart(esbServer);
			Object object = ((EsbServerEditPart)editpart).getChildren().get(0);
			if (deserializer != null) {
				EsbNode node = deserializer.createNode((IGraphicalEditPart)object,artifact.getValue());
				if (node!=null) {
				/*	addCmd = new AddCommand(domain, esbServer,
							EsbPackage.Literals.ESB_SERVER__CHILDREN, node);
					if (addCmd.canExecute()) {
						domain.getCommandStack().execute(addCmd);
						
						AbstractEsbNodeDeserializer.refreshEditPartMap();
						GraphicalEditPart editpart = (GraphicalEditPart) AbstractEsbNodeDeserializer
								.getEditpart(node);
						GraphicalEditPart parent = ((GraphicalEditPart) editpart.getParent());
						parent.setLayoutConstraint(editpart, editpart.getFigure(),
								new org.eclipse.draw2d.geometry.Rectangle(0, 0, -1, -1));
					} else {
						log.warn("Cannot execute EMF command : " + addCmd.toString());
					}*/
					
				} else{
					log.warn("Ignoring null output from deserializer for " + artifact.getValue().getClass());
				}
			}
		}
		if(artifacts.size()>0){
			AbstractEsbNodeDeserializer.connectMediatorFlows();
		}

	}
	
	
	private ArtifactType getArtifactType(String source) throws Exception{
		ArtifactType artifactType = null;
		OMElement element = AXIOMUtil.stringToOM(source);
		String localName = element.getLocalName();
		if("definitions".equals(localName)){
			artifactType=ArtifactType.SYNAPSE_CONFIG;
		} else if("proxy".equals(localName)){
			artifactType=ArtifactType.PROXY;
		} else if("sequence".equals(localName)){
			artifactType=ArtifactType.SEQUENCE;
		} else if("localEntry".equals(localName)){
			artifactType=ArtifactType.LOCAL_ENTRY;
		} else if("task".equals(localName)){
			artifactType=ArtifactType.TASK;
		} else if("api".equals(localName)){
			artifactType=ArtifactType.API;
		} else if("template".equals(localName)){
			artifactType=ArtifactType.TEMPLATE;
		} 
		return artifactType;
	}
	
	private Map<String,Object> getArtifacts(String source) throws Exception{
		Map<String,Object> artifacts =new LinkedHashMap<String, Object>();
		
		ArtifactType artifactType = getArtifactType(source);
		OMElement element = AXIOMUtil.stringToOM(source);
		try{
		switch (artifactType) {
		case SYNAPSE_CONFIG:
			File tempfile = File.createTempFile("file", ".tmp");
			BufferedWriter outfile = new BufferedWriter(new FileWriter(tempfile));
			outfile.write(source);
			outfile.close();
			SynapseConfiguration synapseCofig = SynapseConfigurationBuilder.getConfiguration(
					tempfile.getAbsolutePath(), null);
			Collection<ProxyService> proxyServices = synapseCofig.getProxyServices();
			for (ProxyService proxy : proxyServices) {
				artifacts.put(proxy.getName(), proxy);
			}
			Collection<API> apis = synapseCofig.getAPIs();
			for (API api : apis) {
				artifacts.put(api.getName(), api);
			}
			Map<String, SequenceMediator> definedSequences = synapseCofig.getDefinedSequences();
			if(definedSequences.size()>0)
				artifacts.putAll(definedSequences);
			Map<String, Endpoint> definedEndpoints = synapseCofig.getDefinedEndpoints();
			if(definedEndpoints.size()>0)
				artifacts.putAll(definedEndpoints);
			Map<String, Entry> definedEntries = synapseCofig.getDefinedEntries();
			if(definedEntries.size()>0)
				artifacts.putAll(definedEntries);
			Map<String, TemplateMediator> sequenceTemplates = synapseCofig.getSequenceTemplates();
			if(sequenceTemplates.size()>0)
				artifacts.putAll(sequenceTemplates);
			Map<String, Template> endpointTemplates = synapseCofig.getEndpointTemplates();
			if(endpointTemplates.size()>0)
				artifacts.putAll(endpointTemplates);
			break;
		case PROXY:
			ProxyService proxy = ProxyServiceFactory.createProxy(element, null);
			artifacts.put(proxy.getName(), proxy);
			break;
		case SEQUENCE:
			SequenceMediatorFactory sequenceMediatorFactory = new SequenceMediatorFactory();
			SequenceMediator sequence = (SequenceMediator) sequenceMediatorFactory.createSpecificMediator(element, null);
			artifacts.put(sequence.getName(), sequence);
			break;
		case API:
			API api = APIFactory.createAPI(element);
			artifacts.put(api.getName(), api);
			break;
		case ENDPOINT:
			Endpoint endpoint = EndpointFactory.getEndpointFromElement(element, false, null);
			artifacts.put(endpoint.getName(), endpoint);
			break;
		case LOCAL_ENTRY:
			Entry entry = EntryFactory.createEntry(element, null);
			artifacts.put(entry.getKey(), entry);
			break;
		case TASK:
			TaskDescription task = TaskDescriptionFactory.createTaskDescription(element, OMAbstractFactory.getOMFactory()
					.createOMNamespace("http://ws.apache.org/ns/synapse", ""));
			artifacts.put(task.getName(), task);
			break;
		case TEMPLATE:
			//TODO : implement for templates
			break;
		default:
			break;
		}
		}catch (org.apache.synapse.SynapseException exception) {
			
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Error occuerd during buidling the esb design view.",
					exception.getCause().toString());
		}
		
		return artifacts;
	}

}
