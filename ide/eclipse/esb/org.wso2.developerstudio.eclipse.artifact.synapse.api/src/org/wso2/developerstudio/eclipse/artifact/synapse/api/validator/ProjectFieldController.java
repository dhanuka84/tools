/*
 * Copyright (c) 2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.developerstudio.eclipse.artifact.synapse.api.validator;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.wso2.developerstudio.eclipse.artifact.synapse.api.util.ArtifactConstants;
import org.wso2.developerstudio.eclipse.platform.core.exception.FieldValidationException;
import org.wso2.developerstudio.eclipse.platform.core.model.AbstractFieldController;
import org.wso2.developerstudio.eclipse.platform.core.project.model.ProjectDataModel;
import org.wso2.developerstudio.eclipse.platform.ui.validator.CommonFieldValidator;

/**
 * The controller class for API artifact wizard specific fields.
 */
public class ProjectFieldController extends AbstractFieldController {

	@Override
	public void validate(String modelProperty, Object value, ProjectDataModel model)
			throws FieldValidationException {
		if (modelProperty.equals(ArtifactConstants.ID_API_NAME)) {
			CommonFieldValidator.validateArtifactName(value);
		} else if (modelProperty.equals(ArtifactConstants.ID_SAVE_LOCATION)) {
			IResource resource = (IResource)value;
			if(resource==null || !resource.exists())	
				throw new FieldValidationException(ArtifactConstants.ERRMSG_SAVE_LOCATION);
		} else if (modelProperty.equals(ArtifactConstants.ID_API_CONTEXT)) {
			if (value == null || value.equals("")) {
				throw new FieldValidationException(ArtifactConstants.ERRMSG_API_CONTEXT);
			}
		} else if (modelProperty.equals(ArtifactConstants.ID_API_PORT)) {
			if (value != null && value.toString().length()>0) {
				try{
					int port = Integer.parseInt(value.toString());
					if(port<=0){
						throw new Exception();
					}
				} catch (Exception e) {
					throw new FieldValidationException(ArtifactConstants.ERRMSG_API_PORT);
				}
			}
		}
	}
	
	public List<String> getUpdateFields(String modelProperty, ProjectDataModel model) {
		List<String> updateFields = super.getUpdateFields(modelProperty, model);
		if (modelProperty.equals(ArtifactConstants.ID_CREATE_PRJ)) {
			updateFields.add(ArtifactConstants.ID_SAVE_LOCATION);
		} 
		return updateFields;
	}
	
	public boolean isReadOnlyField(String modelProperty, ProjectDataModel model) {
		boolean readOnlyField = super.isReadOnlyField(modelProperty, model);
		if (modelProperty.equals(ArtifactConstants.ID_SAVE_LOCATION)) {
			readOnlyField = true;
		}
	    return readOnlyField;
	}	

}