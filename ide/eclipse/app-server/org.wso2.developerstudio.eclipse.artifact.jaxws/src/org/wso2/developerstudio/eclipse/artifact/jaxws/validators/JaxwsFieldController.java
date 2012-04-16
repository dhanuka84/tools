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

package org.wso2.developerstudio.eclipse.artifact.jaxws.validators;

import java.io.File;

import org.wso2.developerstudio.eclipse.platform.core.exception.FieldValidationException;
import org.wso2.developerstudio.eclipse.platform.core.model.AbstractFieldController;
import org.wso2.developerstudio.eclipse.platform.core.project.model.ProjectDataModel;
import org.wso2.developerstudio.eclipse.platform.ui.validator.CommonFieldValidator;

public class JaxwsFieldController extends AbstractFieldController {

	
	public void validate(String modelProperty, Object value,
			ProjectDataModel model) throws FieldValidationException {
		if (modelProperty.equals("project.name")) {
			CommonFieldValidator.validateProjectField(value);
		} else if (modelProperty.equals("import.file")) {
			String fileType ="WSDL";
			if (value == null) {
				throw new FieldValidationException("Specified " + fileType +" file location is invalid");
			}
			File importFile = (File) value;
			if (!importFile.exists()) {
				throw new FieldValidationException("Specified " + fileType +" file doesn't exist");
			}
		} else if (modelProperty.equals("source.package") ) {
			if (value != null && !value.toString().isEmpty()) {
				CommonFieldValidator.validateJavaPackageNameField(value);
			}
			
		}  else if (modelProperty.equals("runtime") ) {
			if (value == null) {
				throw new FieldValidationException("Specified CXF-Runtime location is invalid");
			}
			File importFile = (File) value;
			if (importFile.exists()) {
				String os = System.getProperty("os.name").toLowerCase();
				File cxfBin = new File(importFile,"bin");
				File wsdl2java  = (os.indexOf("win") >= 0)? new File(cxfBin,"wsdl2java.bat"): new File(cxfBin,"wsdl2java");
				if(!wsdl2java.exists()){
					throw new FieldValidationException("Cannot find CXF wsdl2java executable");
				}
			} else{
				throw new FieldValidationException("Specified CXF-Runtime location doesn't exist");
			}
		} 
	}

}
