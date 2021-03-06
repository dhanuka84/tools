/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbonstudio.eclipse.greg.base.ui.editor.pages;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbonstudio.eclipse.greg.base.Activator;
import org.wso2.carbonstudio.eclipse.greg.base.core.Registry;
import org.wso2.carbonstudio.eclipse.greg.base.editor.input.ResourceEditorInput;
import org.wso2.carbonstudio.eclipse.greg.base.interfaces.IRegistryFormEditorPage;
import org.wso2.carbonstudio.eclipse.greg.base.managers.RemoteContentManager;
import org.wso2.carbonstudio.eclipse.greg.base.model.RegistryResourceNode;
import org.wso2.carbonstudio.eclipse.greg.base.ui.editor.RegistryResourceEditor;
import org.wso2.carbonstudio.eclipse.greg.core.exception.InvalidRegistryURLException;
import org.wso2.carbonstudio.eclipse.greg.core.exception.UnknownRegistryException;
import org.wso2.carbonstudio.eclipse.greg.core.interfaces.IGARImportDependency;
import org.wso2.carbonstudio.eclipse.greg.core.utils.GARUtils;
import org.wso2.carbonstudio.eclipse.logging.core.ICarbonStudioLog;
import org.wso2.carbonstudio.eclipse.logging.core.Logger;
import org.wso2.carbonstudio.eclipse.platform.core.MediaManager;
import org.wso2.carbonstudio.eclipse.platform.core.mediatype.PlatformMediaTypeConstants;

public class ResourceEditorPage extends FormPage implements
		IResourceChangeListener, IRegistryFormEditorPage {
	private static ICarbonStudioLog log=Logger.getLog(Activator.PLUGIN_ID);

	private FormToolkit toolkit;
	private ScrolledForm form;
	private String selectedMethod;
	private RegistryResourceNode regResourcePathData;
	private Text nameText;
	private Combo mediaTypeText;
	private Text descText;
	private Text contentAreaText; 
	private Registry registry;

	private String currentResourceName;
	private String currentDescription;
	private String currentMediaType;
	private boolean createGovernanceArchive=false;
	
	private Shell containerShell;
	private RegistryResourceEditor editor;

	private String resourceName = "";
	private String filePath = "";
	private String description = "";
	private String mediaType = "";
	private boolean pageDirty;
	private String content="" ;

	private Composite sectionContainer;

	private String[] addResourceMethods = { "Upload Content From file",
	                                        "Create custom content",
	                                        "Import Content from URL" };
	private ResourceEditorInput editorInput;
	private String parentPath;

	private Combo methodCombo;
	private Label lbl;
	private Button openButton;

	private String resourceUrl;
	private Section section;

	private Text urlText;

	private Text filePathText;

	private Button btnGovernanceArchive;

	public ResourceEditorPage(RegistryResourceEditor editor, String id, String title) {
		super(editor, id, title);
		this.editor = editor;
		editorInput = (ResourceEditorInput) editor.getEditorInput();
		loadData();
	}

	public void resourceChanged(IResourceChangeEvent arg0) {

	}

	private void updateFormName() {
		String name = getResourceName().equalsIgnoreCase("") ? "<Resource>"
				: getResourceName();
		name = getParentPath().endsWith("/") ? name : "/" + name;
		name = getParentPath() + name;
		name = name.replaceAll(Pattern.quote("//"), "/");
		if (form != null)
			form.setText(name);

	}

	public String getParentPath() {
		if (editorInput.getParentResource() != null
				&& editorInput.getParentResource().getRegistryResourcePath() == null) {
			parentPath = "/";
		} else if (editorInput.getParentResource() == null) {
			parentPath = editorInput.getResource().getRegistryResourcePath()
					.substring(
							0,
							editorInput.getResource().getRegistryResourcePath()
									.length()
									- editorInput.getResource()
											.getLastSegmentInPath().length());
		} else
			parentPath = editorInput.getParentResource()
					.getRegistryResourcePath();
		return parentPath;
	}

	protected void createFormContent(IManagedForm managedForm) {
		registry = new Registry();
		toolkit = managedForm.getToolkit();
		form = managedForm.getForm();
		toolkit.decorateFormHeading(form.getForm());
		form.getForm();
		form.getBody().setLayout(new GridLayout());

		Composite columnComp = toolkit.createComposite(form.getBody());
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 75;
		columnComp.setLayout(layout);
		GridData gd = new GridData();
		columnComp.setLayoutData(gd);

		if (editorInput.getResource() == null) {
			lbl = toolkit.createLabel(columnComp, "Method");
			lbl.setLayoutData(new GridData());

			methodCombo = new Combo(columnComp, SWT.VERTICAL | SWT.DROP_DOWN
					| SWT.BORDER | SWT.READ_ONLY);
			toolkit.adapt(methodCombo);
			toolkit.paintBordersFor(columnComp);
			methodCombo.add(addResourceMethods[0], 0);

			for (int i = 1; i < addResourceMethods.length; i++) {
				methodCombo.add(addResourceMethods[i]);
			}
			if (methodCombo.getSelectionIndex() == -1) {
				methodCombo.select(0);
			}
			
			Composite sectionComp = toolkit.createComposite(form.getBody());
			GridLayout sLayout = new GridLayout();
			layout.numColumns = 2;
			sectionComp.setLayout(sLayout);
			GridData sectionGd = new GridData();
			sectionComp.setLayoutData(sectionGd);
			
			sectionContainer = toolkit.createComposite(sectionComp);
//			sectionContainer = toolkit.createComposite(columnComp);
			GridLayout sectionLayout = new GridLayout();
			sectionLayout.numColumns = 1;
			sectionContainer.setLayout(sectionLayout);
			sectionContainer.setLayoutData(new GridData(
					GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));

			section = toolkit.createSection(sectionContainer, SWT.NONE);
			section1Data("Upload Content From file", section);
			methodCombo.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					selectedMethod = methodCombo.getText();
					
					if (selectedMethod.equals("Upload Content From file")) {
						section.dispose();
						section = toolkit.createSection(sectionContainer, SWT.NONE);
						section1Data(selectedMethod, section);
					} else if (selectedMethod.equals("Import Content from URL")) {
						section.dispose();
						section = toolkit.createSection(sectionContainer, SWT.NONE);
						section2Data(selectedMethod, section);
					} else if (selectedMethod.equals("Create custom content")) {
						section.dispose();
						section = toolkit.createSection(sectionContainer, SWT.NONE);
						section3Data(selectedMethod, section);
					} else {

					}
					updateDirtyState();
				}
			});

		} else {
			fillResourceInfo(columnComp);
			updateFormName();
			refresh();
		}
	}

	public void loadData(Section section, Composite container) {
		section.setClient(container);
	}

	public FormColors setFormColour(){
		FormColors formColor = new FormColors( Display.getCurrent());
		formColor.createColor( FormColors.BORDER, 0, 0, 255); 
		formColor.setBackground( new Color( Display.getCurrent(), 255, 255, 255)); 
		return formColor;
	}
	
	
	public void section1Data(String method, Section section) {
		Button pathButton;
		Label fileLabel;

		section.redraw();
		section.setVisible(true);
		section.setText(method);
		Composite container = toolkit.createComposite(section);
		GridLayout updatelayout = new GridLayout();
		updatelayout.numColumns = 4;
		updatelayout.horizontalSpacing = 10;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		container.setLayoutData(gd);
		container.setLayout(updatelayout);

		containerShell = section.getShell();

		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileLabel = toolkit.createLabel(container, "File: ");
		fileLabel.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label fileRequired = toolkit.createLabel(container, "*",SWT.COLOR_DARK_RED);
		fileRequired.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		fileLabel.setLayoutData(gd);
		
		
		filePathText = toolkit.createText(container, "", SWT.BORDER|SWT.COLOR_WIDGET_BACKGROUND);
		filePathText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		gd = new GridData();
		gd.heightHint = 18;
		gd.widthHint = 350;
		filePathText.setLayoutData(gd);

		gd = new GridData();
		gd.heightHint = 27;
		gd.widthHint = 70;
		pathButton = toolkit.createButton(container, "Browse", SWT.PUSH);
		pathButton.setLayoutData(gd);

		pathButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handlePathBrowseButton(filePathText);
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label nameLabel = toolkit.createLabel(container, "Name:");
		nameLabel.setLayoutData(gd);

		Label reqNameLabel = toolkit.createLabel(container, "*", SWT.COLOR_DARK_RED);
		reqNameLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		
		nameText = toolkit.createText(container, "", SWT.BORDER);
		nameText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		gd = new GridData();
		gd.heightHint = 18;
		gd.widthHint = 250;
		nameText.setLayoutData(gd);
		
		openButton = toolkit.createButton(container, "Open", SWT.None);
		openButton.setVisible(false);
		openButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				RegistryResourceNode resource = editorInput.getResource();
				registry = resource.getConnectionInfo().getRegistry();
				File tempFile;
				tempFile = resource.getFile();
				IEditorPart editor = RemoteContentManager.openFile(tempFile);
				resource.setFileEditor(editor);
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				widgetSelected(arg0);
			}
		});
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				nameText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				setResourceName(nameText.getText());
				updateDirtyState();
			}
		});

		gd = new GridData(GridData.FILL_HORIZONTAL);
		createMediaTypeCombo(container);
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label hiddenLabel2 = toolkit.createLabel(container, "", SWT.NULL);
		hiddenLabel2.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label descriptionLabel = toolkit.createLabel(container, "", SWT.NULL);
		descriptionLabel.setText("Description: ");
		descriptionLabel.setLayoutData(gd);
		
		Label hidLab3 = toolkit.createLabel(container, "");

		gd = new GridData();
		gd.heightHint = 50;
		gd.widthHint = 350;
		descText = toolkit.createText(container, "", SWT.MULTI | SWT.BORDER
				| SWT.V_SCROLL | SWT.WRAP);
		descText.setLayoutData(gd);
		descText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent event) {
				setDescription(descText.getText());
				updateDirtyState();

			}
		});

		filePathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				filePathText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				Text name = (Text) event.widget;
				resourceName = name.getText();
				setFilePath(resourceName);
				
				nameText.setText(getfileName(getFilePath()));
				
				File file = new File(getFilePath());
				String mediaType;
				mediaType=MediaManager.getMediaType(file);
				if (mediaType == null) {
					mediaTypeText.setText("not specified");
					setMediaType(null);
				} else {
					mediaTypeText.setText(mediaType);
					setMediaType(mediaType);
				}
				updateDirtyState();
			}
		});

		toolkit.createLabel(container, "", SWT.NULL);
		section.setClient(container);

		updateFormName();
		refresh();
	}

	private void createMediaTypeCombo(Composite container) {
		GridData gd;
		toolkit.createLabel(container, "Media Type: ");

		toolkit.createLabel(container, "");
		
		ArrayList<String> mediaTypeskeySet = registry.getAllMediaTypes();
		mediaTypeText = new Combo(container, SWT.BORDER);
		loadMediaTypes(mediaTypeskeySet);

		gd = new GridData();
		gd.heightHint = 18;
		gd.widthHint = 250;
		mediaTypeText.setLayoutData(gd);
		mediaTypeText.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent event) {
				setMediaType(mediaTypeText.getText());
				updateDirtyState();
			}
		});
		
		gd = new GridData();
		gd.horizontalSpan=3;

		btnGovernanceArchive = toolkit.createButton(container, "Create governance archive", SWT.CHECK);
		btnGovernanceArchive.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				setCreateGovernanceArchive(btnGovernanceArchive.getSelection());
			}
		});
		btnGovernanceArchive.setSelection(false);
		btnGovernanceArchive.setVisible(false);
		btnGovernanceArchive.setLayoutData(gd);
	}
	
	public void section2Data(String method, Section section) {
		section.setVisible(true);
		section.setText(method);
		Composite container = toolkit.createComposite(section);
		GridLayout updatelayout = new GridLayout();
		updatelayout.numColumns = 3;
		updatelayout.horizontalSpacing = 10;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL);
		container.setLayoutData(gd);
		container.setLayout(updatelayout);

		containerShell = section.getShell();

		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label urlLabel = toolkit.createLabel(container, "URL: ");
		urlLabel.setLayoutData(gd);
		
		Label reqURLLabel = toolkit.createLabel(container, "*", SWT.COLOR_DARK_RED);
		reqURLLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));

		urlText = toolkit.createText(container, "", SWT.BORDER);
		urlText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		gd = new GridData();
		gd.heightHint = 18;
		gd.widthHint = 350;
		urlText.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label nameLabel = toolkit.createLabel(container, "Name: ");
		nameLabel.setLayoutData(gd);

		Label reqNameLabel = toolkit.createLabel(container, "*", SWT.COLOR_DARK_RED);
		reqNameLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		
		nameText = toolkit.createText(container, "", SWT.BORDER);
		nameText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		gd = new GridData();
		gd.heightHint = 18;
		gd.widthHint = 250;
		nameText.setLayoutData(gd);

		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				setResourceName(nameText.getText());
				nameText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				updateDirtyState();
			}
		});

		gd = new GridData(GridData.FILL_HORIZONTAL);
		createMediaTypeCombo(container);
		
		urlText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				Text name = (Text) event.widget;
				String url = name.getText();
				setResourceUrl(url);
				urlText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				try {
					if (validateUrl(url)) {
						fillOtherInfo(mediaTypeText, nameText);
					}
				} catch (URISyntaxException e) {
					log.error(e);
				} catch (IOException e) {
					log.error(e);
				}

			}
		});

		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label descriptionLabel = toolkit.createLabel(container, "", SWT.NULL);
		descriptionLabel.setText("Description: ");
		descriptionLabel.setLayoutData(gd);

		toolkit.createLabel(container, "");
		
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 50;
		descText = toolkit.createText(container, "", SWT.MULTI | SWT.BORDER
				| SWT.V_SCROLL | SWT.WRAP);
		descText.setLayoutData(gd);
		descText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent event) {
				setDescription(descText.getText());
				updateDirtyState();

			}
		});
		section.setClient(container);
		updateFormName();
		refresh();

	}

	public void section3Data(String method, Section section) {
		section.redraw();
		section.setVisible(true);
		section.setText(method);
		Composite container = toolkit.createComposite(section);
		GridLayout updatelayout = new GridLayout();
		updatelayout.numColumns = 3;
		updatelayout.horizontalSpacing = 10;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL);
		container.setLayoutData(gd);
		container.setLayout(updatelayout);

		containerShell = section.getShell();
		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label nameLable = toolkit.createLabel(container, "Name: ");
		nameLable.setLayoutData(gd);

		Label reqNameLabel = toolkit.createLabel(container, "*", SWT.COLOR_DARK_RED);
		reqNameLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		
		nameText = toolkit.createText(container, "", SWT.BORDER);
		nameText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		gd = new GridData();
		gd.heightHint = 18;
		gd.widthHint = 250;
		nameText.setLayoutData(gd);

		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				Text name = (Text) event.widget;
				String resName = name.getText();
				setResourceName(resName);
				nameText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		createMediaTypeCombo(container);
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label descriptionLabel = toolkit.createLabel(container, "", SWT.NULL);
		descriptionLabel.setText("Description: ");
		descriptionLabel.setLayoutData(gd);

		toolkit.createLabel(container, "");
		
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 50;
		descText = toolkit.createText(container, "", SWT.MULTI | SWT.BORDER
				| SWT.V_SCROLL | SWT.WRAP);
		descText.setLayoutData(gd);
		descText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent event) {
				setDescription(descText.getText());

				updateDirtyState();

			}
		});
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label contentLabel = toolkit.createLabel(container, "", SWT.NULL);
		contentLabel.setText("Content: ");
		contentLabel.setLayoutData(gd);

		toolkit.createLabel(container, "");
		
		gd = new GridData();
		gd.heightHint = 150;
		gd.widthHint = 500;
		contentAreaText = toolkit.createText(container, "", SWT.MULTI | SWT.BORDER
				| SWT.V_SCROLL | SWT.WRAP);
		contentAreaText.setLayoutData(gd);
		contentAreaText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent event) {
				setContent(contentAreaText.getText());
				updateDirtyState();

			}
		});
		
		section.setClient(container);
		updateFormName();
		refresh();

	}

	private void loadMediaTypes(ArrayList<String> mediaTypeskeySet) {
		mediaTypeText.removeAll();
		for (String keys : mediaTypeskeySet) {
			mediaTypeText.add(keys);
		}
	}

	private void createContent(String fileName, String mediaType)
			throws IOException {
		
		if(selectedMethod.equals("Create custom content")){
			String fileExt = registry.getMediaTypeFileExtension(mediaType);
	
			if (fileExt != null) {
				String name = fileName + "." + fileExt;
				File tempDir = File.createTempFile("test", "test");
				tempDir.delete();
				tempDir.mkdirs();
	
				File tempFile = File.createTempFile(fileName, "", tempDir);
				BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
				out.write(getContent());
				out.close();
				tempFile.deleteOnExit();
//				IEditorPart editor = RemoteContentManager.openFile(tempFile);
				editorInput.getParentResource().setFileEditor(editor);
				setFilePath(tempFile.getAbsolutePath());
				
			}
		}
	}

	private boolean validateUrl(String url) throws URISyntaxException,
			IOException {
//		url = url.toLowerCase();
		url = removeSpaces(url);
		URI pathUri = new URI(url);
		String p2 = pathUri.toURL().toString(); 
		URI uri2 = new URI(p2); 
		String p3 = uri2.toASCIIString(); 
		URL path = new URL(p3); 
//		URL path = pathUri.toURL();
		if (((HttpURLConnection) path.openConnection()).getResponseCode() > 0) {
			setResourceUrl(p3);
			return true;
			
		}
		return false;

	}
	
	private String removeSpaces(String url){
	    Pattern pattern = Pattern.compile("\\s+");
	    Matcher matcher = pattern.matcher(url);
	    boolean check = matcher.find();
	    String str = matcher.replaceAll("");
	    return str;
	}

	private void fillOtherInfo(Combo mediatypeText, Text nameText)
			throws URISyntaxException, IOException {
		String extention="";
		String mediaType="";
		URI pathUri = new URI(getResourceUrl());
		URL path = pathUri.toURL();
		URLConnection uc = path.openConnection();
		mediaType=MediaManager.getMediaType(path);
		mediaType = (mediaType==null? uc.getContentType():mediaType);
		
		String[] info = path.getPath().split("/");
		String name="";
		if(path.getQuery()!= null){
			name = info[info.length - 1] + "." + path.getQuery();
		}else{
			name = info[info.length - 1];
		}

		nameText.setText(name);
	
		if (mediaType == null) {
			mediatypeText.setText("not specified");
			setMediaType(null);
		} else {
			mediatypeText.setText(mediaType);
			setMediaType(mediaType);
		}
		File tempDir = File.createTempFile("test", "test");
		tempDir.delete();
		tempDir.mkdirs();

//		File tempFile = new File(tempDir, name);
		File tempFile = File.createTempFile("temp", name, tempDir);
		tempFile.deleteOnExit();
		if(!tempFile.exists()){
			tempFile.createNewFile();
		}
		InputStream is = path.openStream();
		OutputStream out = new FileOutputStream(tempFile);
		byte buf[] = new byte[1024];
		int len;
		while ((len = is.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		is.close();

		setFilePath(tempFile.getAbsolutePath());

	}

	private void refresh() {
		if (editorInput.getResource() != null) {
			if (lbl != null)
				lbl.setVisible(false);
			if (methodCombo != null)
				methodCombo.setVisible(false);
		}
	}

	private void loadData() {
		RegistryResourceNode resource = editorInput.getResource();
		if (resource != null) {
			setCurrentResourceName(resource.getLastSegmentInPath());
			setResourceName(getCurrentResourceName());
			try {
				setCurrentMediaType(resource.getConnectionInfo().getRegistry().get(
								resource.getRegistryResourcePath())
						.getMediaType());
				setMediaType(getCurrentMediaType());
				setCurrentDescription(resource.getConnectionInfo()
						.getRegistry().get(
								resource.getRegistryResourcePath())
						.getDescription());
				setDescription(getCurrentDescription());
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	private void updateDirtyState() {
		boolean dirtyState = getCurrentResourceName() == null
				|| getCurrentMediaType() == null
				|| getCurrentDescription() == null
				|| (!getCurrentResourceName().equals(getResourceName()))
				|| (!getCurrentMediaType().equals(getMediaType()))
				|| (!getCurrentDescription().equals(getDescription()));
		setPageDirty(dirtyState);

	}

	public void syncToCurrentValues() {
		setCurrentResourceName(getResourceName());
		setCurrentMediaType(getMediaType());
		setCurrentDescription(getDescription());
		updateDirtyState();
	}

	public String getfileName(String filePath) {
		String fileName;
		String[] filepathInfo = filePath.split(Pattern.quote(File.separator));
		fileName = filepathInfo[filepathInfo.length - 1];
		return fileName;
	}

	public String getFileExtension(String filePath) {
		String fileExt = null;
		String fileName = getfileName(filePath);
		String[] fileNameInfo = fileName.split("\\.");
		fileExt = fileNameInfo[fileNameInfo.length - 1];
		return fileExt;
	}

	public RegistryResourceNode getRegResourcePathData() {
		return regResourcePathData;
	}

	public void setRegResourcePathData(
			RegistryResourceNode regResourcePathData) {
		this.regResourcePathData = regResourcePathData;
	}

	public String getCurrentResourceName() {
		return currentResourceName;
	}

	public void setCurrentResourceName(String userGivenName) {
		this.currentResourceName = userGivenName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		boolean isWSDLMediaType = mediaType.equalsIgnoreCase(PlatformMediaTypeConstants.MEDIA_TYPE_WSDL);
		boolean wasWSDLMediaType = this.mediaType.equalsIgnoreCase(PlatformMediaTypeConstants.MEDIA_TYPE_WSDL);
		btnGovernanceArchive.setVisible(isWSDLMediaType);
		this.mediaType = mediaType;
		btnGovernanceArchive.setSelection(btnGovernanceArchive.getSelection() || (isWSDLMediaType && !wasWSDLMediaType));
		if (isWSDLMediaType){
			
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void handlePathBrowseButton(Text filePathText) {
		String fileName = getSavePath();
		if (fileName != null)
			filePathText.setText(fileName);
	}

	private String getSavePath() {
		String fileName = null;
		// FileDialog
		FileDialog fld = new FileDialog(containerShell, SWT.OPEN);
		boolean done = false;

		while (!done) {
			// Open the File Dialog
			fileName = fld.open();
			if (fileName == null) {
				// User has cancelled, so quit and return
				done = true;
			} else {
				// User has selected a file; see if it already exists
				File file = new File(fileName);
				if (file.exists()) {
					// If they click Yes, we're done and we drop out. If
					// they click No, we redisplay the File Dialog
					done = true;
				} else {
					// File does not exist, so drop out
					done = false;
				}
			}
		}
		return fileName;
	}

	public void validate() throws Exception {

		
		// file name, file path should be valid
		
		if (editorInput.getResource() == null) {
			if (getFilePath() != null) {
				if (getFilePath() == null || getFilePath().equals("")) {
					try {
						createContent(getResourceName(), getMediaType());
					} catch (IOException e1) {
						e1.printStackTrace();
						throw new Exception("File cannot be empty");
					}
//					
				} else {
					String filepath = getFilePath();
					File file = new File(filepath);
					if (!file.exists()) {
						file.createNewFile();
					}
				}
			}
			regResourcePathData = editorInput.getParentResource();
			boolean found = false;
			for (RegistryResourceNode child : regResourcePathData
					.getResourceNodeList()) {
				if (child.getCaption().equals(getCurrentResourceName())) {
					found = true;
					break;
				}
			}
			if (found)
				throw new Exception("The Resource name '"
						+ getCurrentResourceName()
						+ "' already exists in the given path");
		}
		if (getResourceName() == null || getResourceName().equals("")) {
			throw new Exception("Resource name cannot be empty");
		}
	}

	public void doFinish() throws InvalidRegistryURLException, UnknownRegistryException {
		regResourcePathData = editorInput.getResource();
		String mediaType = getMediaType();
		if (regResourcePathData == null) {
			if (mediaType.equalsIgnoreCase(PlatformMediaTypeConstants.MEDIA_TYPE_WSDL)){
				try {
					if (isCreateGovernanceArchive()){
						selectedMethod = methodCombo.getText();
						URL url = null;
						if (selectedMethod.equals("Upload Content From file")) {
							url=new File(getFilePath()).toURI().toURL();
						} else if (selectedMethod.equals("Import Content from URL")) {
							url=new URL(getResourceUrl());
						}
						if (url!=null) {
							IGARImportDependency garImportDependencyImpl = GARUtils.getGARDependencyModel(url);
							if (!garImportDependencyImpl.isExclude()){
								try {
									mediaType = PlatformMediaTypeConstants.MEDIA_TYPE_GAR;
									setFilePath(GARUtils.createGAR(garImportDependencyImpl).toString());
								} catch (Exception e) {
									throw new UnknownRegistryException("Error occured while creatiing the governance archive: "+e.getMessage(),e);
								}
							}
						}
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			regResourcePathData = editorInput.getParentResource();
			registry = regResourcePathData.getConnectionInfo().getRegistry();
			String selectedPath = regResourcePathData.getRegistryResourcePath();
			selectedPath = selectedPath.endsWith("/") ? selectedPath
					: selectedPath + "/";
			Resource resource = registry.newResource();
			resource.setDescription(getDescription());
			try {
				resource.setContentStream(getFileContent(getFilePath()));
				resource.setMediaType(mediaType);
				String resourceName = selectedPath + getResourceName();
				String newPath = registry.put(resourceName, resource);
				String[] pathInfo = newPath.split("/");
				if (newPath.equals("/")) {
					editorInput.getParentResource().setRegistryResourcePath(newPath);
				} else {
					String rootPath = "/";
					for (int i = 0; i < pathInfo.length - 1; i++) {
						rootPath += pathInfo[i] + "/";
					}
					editorInput.getParentResource().setRegistryResourcePath(rootPath);
				}
				editorInput.getParentResource().setIterativeRefresh(true);
				
				regResourcePathData.refreshChildren();
				regResourcePathData.getConnectionInfo().getRegUrlData()
						.refreshViewer(false);
				ArrayList<RegistryResourceNode> resourcePathList = regResourcePathData
						.getResourceNodeList();
				for (RegistryResourceNode registryResourcePathData : resourcePathList) {
					if (registryResourcePathData.getRegistryResourcePath().equals(
							resourceName)) {
						editorInput.setResource(registryResourcePathData);
						registryResourcePathData.getVersionContent(
								registryResourcePathData.getLatestVersion(),
								getFilePath());
					}

				}
			} catch (FileNotFoundException e) {
				throw new UnknownRegistryException("Error occured while trying to retrieve file content for the registry resource: "+e.getMessage(),e);
			} catch (RegistryException e) {
				throw new UnknownRegistryException("Error occured while trying to set registry content from file: "+e.getMessage(),e);
			}
		} else {
			registry = regResourcePathData.getConnectionInfo().getRegistry();
			Resource resource = registry.get(regResourcePathData
					.getRegistryResourcePath());
			resource.setMediaType(mediaType);
			resource.setDescription(getDescription());
			registry.put(regResourcePathData.getRegistryResourcePath(),
					resource);
			regResourcePathData.getVersionContent(regResourcePathData
					.getLatestVersion(), getFilePath());
		}

		syncToCurrentValues();
		refresh();
		if (filePathText!=null){
			filePathText.setEnabled(false);
		}
		if (urlText!=null){
			urlText.setEnabled(false);
		}
	}

	public InputStream getFileContent(String filePath)
			throws FileNotFoundException {
		File file = new File(filePath);
		InputStream inStr = new FileInputStream(file);
		return inStr;
	}

	public String getCurrentDescription() {
		return currentDescription;
	}

	public void setCurrentDescription(String currentDescription) {
		this.currentDescription = currentDescription;
	}

	public String getCurrentMediaType() {
		return currentMediaType;
	}

	public void setCurrentMediaType(String currentMediaType) {
		this.currentMediaType = currentMediaType;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
		updateFormName();
	}

	public void fillResourceInfo(Composite container) {
		GridData gd;
		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label nameLabel = toolkit.createLabel(container, "Name: ");
		nameLabel.setLayoutData(gd);
		
		Label name = toolkit.createLabel(container, getResourceName());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		name.setLayoutData(gd);
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label mediaTypeLabel = toolkit.createLabel(container, "Media Type: ");

		if (mediaTypeText == null) {

			ArrayList<String> mediaTypeskeySet = registry.getAllMediaTypes();
			
			mediaTypeText = new Combo(container, SWT.BORDER);
			loadMediaTypes(mediaTypeskeySet);

			gd = new GridData();
			gd.heightHint = 18;
			gd.widthHint = 250;
			mediaTypeText.setLayoutData(gd);
			mediaTypeText.setText(getMediaType());
//			mediaTypeText.setLayoutData(gd);
		}
		
		mediaTypeText.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent arg0) {
				setMediaType(mediaTypeText.getText());
				updateDirtyState();
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label descriptionLabel = toolkit.createLabel(container, "", SWT.NULL);
		descriptionLabel.setText("Description: ");
		descriptionLabel.setLayoutData(gd);

		if (descText == null) {
			gd = new GridData();
			gd.widthHint = 250;
			gd.heightHint = 50;
			descText = toolkit.createText(container, "", SWT.MULTI | SWT.BORDER
					| SWT.V_SCROLL | SWT.WRAP);
			descText.setLayoutData(gd);
		}
		descText.setText(getDescription());
		
		descText.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent arg0) {
				setDescription(descText.getText());
				updateDirtyState();
				
			}
		});
		
	}

	public void executeAction(int actionID, Object object) {
		switch (actionID) {
			case IRegistryFormEditorPage.ACTION_VIEW_INFORMATION:
				setFocusToEditor();
				break;
			}
	}

	public void setFocusToEditor(){
		this.setFocus();
	}
	
	public int getPageType() {
		return IRegistryFormEditorPage.PAGE_RESOURCE;
	}

	public void setPageDirty(boolean pageDirty) {
		this.pageDirty = pageDirty;
		editor.updateDirtyState();
	}

	public boolean isPageDirty() {
		return pageDirty;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreateGovernanceArchive(boolean createGovernanceArchive) {
		this.createGovernanceArchive = createGovernanceArchive;
	}

	public boolean isCreateGovernanceArchive() {
		return createGovernanceArchive;
	}
}
