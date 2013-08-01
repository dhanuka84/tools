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

package org.wso2.datamapper.core;

import java.util.ArrayList;
import java.util.List;

public class ConfigDataModel {
	private String outputElement;
	private String methodName;
	private List<String> argList;
	
	public ConfigDataModel(){
		argList = new ArrayList<String>();
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public List<String> getArgList() {
		return argList;
	}
	public void addArg(String arg) {
		argList.add(arg);
	}
	public String getOutputElement() {
		return outputElement;
	}
	public void setOutputElement(String outputElement) {
		this.outputElement = outputElement;
	}
}