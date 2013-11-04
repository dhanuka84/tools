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

package org.wso2.developerstudio.appfactory.ui.views;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.wso2.developerstudio.appfactory.core.model.AppDBinfo;
import org.wso2.developerstudio.appfactory.core.model.AppUserInfo;
import org.wso2.developerstudio.appfactory.core.model.AppVersionInfo;
import org.wso2.developerstudio.appfactory.core.model.ApplicationInfo;
import org.wso2.developerstudio.appfactory.ui.Activator;
import org.wso2.developerstudio.appfactory.ui.utils.Messages;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;

public class AppfactoryApplicationDetailsView extends ViewPart {

	public static final String ID = Messages.AppfactoryApplicationDetailsView_0;
	private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);
	private static final String APPINFO_TAB_ITEM_NAME = Messages.AppfactoryApplicationDetailsView_1;
	private static final String CURRENT_STATUS_TAB_ITEM_NAME = Messages.AppfactoryApplicationDetailsView_2;
	private static final String TEAM_TAB_ITEM_NAME = Messages.AppfactoryApplicationDetailsView_3;
	private static final String DATA_SOURCES_TAB_ITEM_NAME = Messages.AppfactoryApplicationDetailsView_4;
	private static final String DEFAULT_VALUE = Messages.AppfactoryApplicationDetailsView_5;

	private Table currentStatusTable;
	private Table teamDetailsTable;
	private Table dataSourcesTable;
	private Composite appTypeComposite;
	private Composite repoTypeComposite;
	private Composite appOwnerComposite;
	private Composite descriptionComposite;
	private Composite databaseInfoComposite;
	private Composite databaseUsersComposite;
	private Composite databaseTemplatesComposite;
	private TabFolder tabFolder;
	private TabItem appInfoTabItem;
	private TabItem currentStatusTabItem;
	private TabItem teamTabItem;
	private TabItem dataSourcesTabItem;

	public AppfactoryApplicationDetailsView() {
		AppfactoryApplicationListView.setAppDetailView(this);
	}

	public static Label createLabel(Composite parent, int style, String text, Object layoutData,
			Color backColor, Font font) {
		Label lbl = new Label(parent, style);
		if (font != null) {
			lbl.setFont(font);
		}
		if (backColor != null) {
			lbl.setBackground(backColor);
		}
		lbl.setText(text);
		if (layoutData != null) {
			// lbl.setLayoutData(layoutData);
		}
		return lbl;
	}

	private GridData getGridData() {
		GridData gd = new GridData();
		gd.minimumWidth = 50;
		gd.widthHint = GridData.FILL_HORIZONTAL;
		gd.grabExcessHorizontalSpace = true;
		return gd;
	}

	public void createPartControl(Composite parent) {
		tabFolder = new TabFolder(parent, SWT.BORDER);
		tabFolder.setBackground(tabFolder.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		createTabPages();

		/*
		 * tabFolder.addSelectionListener(new SelectionListener() {
		 * 
		 * public void widgetDefaultSelected(SelectionEvent arg0) {
		 * updatePageContent(); }
		 * 
		 * public void widgetSelected(SelectionEvent arg0) {
		 * updatePageContent(); }
		 * 
		 * });
		 */
	}

	/**
	 * @param composite
	 */
	private void createCompositeLabel(Composite composite, String label) {
		createLabel(composite, SWT.NONE, label, new GridData(), composite.getBackground(),
				new Font(null, Messages.AppfactoryApplicationDetailsView_6, 11, SWT.BOLD));
	}

	public void updateView(ApplicationInfo applicationInfo) {
		// Updating the Application info
		updateAppInfo(applicationInfo);

		// Updating the version info table
		updateBuildStatus(applicationInfo);

		// Updating the team details
		updateTeamDetails(applicationInfo);

		// Updating the data sources details
		updateDataSources(applicationInfo);
	}

	private void removeChildControls(Composite composite) {
		Control[] children = composite.getChildren();

		for (Control control : children) {
			control.dispose();
		}
	}

	@Override
	public void setFocus() {

		// Testing only
		// updateView();
	}

	/**
	 * Create tabs
	 */
	private void createTabPages() {
		appInfoTabItem = new TabItem(tabFolder, SWT.NULL);
		appInfoTabItem.setText(APPINFO_TAB_ITEM_NAME);

		currentStatusTabItem = new TabItem(tabFolder, SWT.NULL);
		currentStatusTabItem.setText(CURRENT_STATUS_TAB_ITEM_NAME);

		teamTabItem = new TabItem(tabFolder, SWT.NULL);
		teamTabItem.setText(TEAM_TAB_ITEM_NAME);

		dataSourcesTabItem = new TabItem(tabFolder, SWT.NULL);
		dataSourcesTabItem.setText(DATA_SOURCES_TAB_ITEM_NAME);

		createAppInfoPage();
		createCurrentStatusPage();
		createTeamDetailsPage();
		createDbInfoPage();
		//createDataSourcesPage();
	}

	/**
	 * Update page content on based on the selected tab
	 */
	private void updatePageContent() {
		int selectionIndex = tabFolder.getSelectionIndex();
		TabItem item = tabFolder.getItem(selectionIndex);

		if (item == appInfoTabItem) {
			// App Info tab
		} else if (item == currentStatusTabItem) {
			// Current Status tab
		} else if (item == teamTabItem) {
			// Team tab
		}
	}

	/**
	 * Create application information tab page
	 */
	private void createAppInfoPage() {
		ScrolledComposite scroller = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		scroller.setBackground(tabFolder.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		// scroller.setExpandVertical(true);
		scroller.setExpandHorizontal(true);
		Composite composite = new Composite(scroller, SWT.NULL);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		composite.setBackground(tabFolder.getBackground());
		scroller.setContent(composite);
		scroller.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		String[] names = new String[] { Messages.AppfactoryApplicationDetailsView_7, Messages.AppfactoryApplicationDetailsView_8, Messages.AppfactoryApplicationDetailsView_9,
				Messages.AppfactoryApplicationDetailsView_10, Messages.AppfactoryApplicationDetailsView_11, Messages.AppfactoryApplicationDetailsView_12 };

		createLabel(composite, SWT.NONE, Messages.AppfactoryApplicationDetailsView_13, new GridData(), scroller.getBackground(), new Font(
				null, Messages.AppfactoryApplicationDetailsView_14, 15, SWT.BOLD));

		createCompositeLabel(composite, names[0]);
		appTypeComposite = new Composite(composite, SWT.NONE);
		appTypeComposite.setBackground(tabFolder.getBackground());
		GridLayout appTypeGridLayout = new GridLayout(1, false);
		appTypeGridLayout.marginWidth = 20;
		appTypeComposite.setLayout(appTypeGridLayout);

		createLabel(composite, SWT.NONE, Messages.AppfactoryApplicationDetailsView_15, new GridData(), scroller.getBackground(), new Font(
				null, Messages.AppfactoryApplicationDetailsView_16, 15, SWT.BOLD));

		createCompositeLabel(composite, names[1]);
		repoTypeComposite = new Composite(composite, SWT.NONE);
		repoTypeComposite.setBackground(tabFolder.getBackground());
		GridLayout repoTypeGridLayout = new GridLayout(1, false);
		repoTypeGridLayout.marginWidth = 20;
		repoTypeComposite.setLayout(repoTypeGridLayout);

		createLabel(composite, SWT.NONE, Messages.AppfactoryApplicationDetailsView_17, new GridData(), scroller.getBackground(), new Font(
				null, Messages.AppfactoryApplicationDetailsView_18, 15, SWT.BOLD));

		createCompositeLabel(composite, names[2]);
		appOwnerComposite = new Composite(composite, SWT.NONE);
		appOwnerComposite.setBackground(tabFolder.getBackground());
		GridLayout appOwnerGridLayout = new GridLayout(1, false);
		appOwnerGridLayout.marginWidth = 20;
		appOwnerComposite.setLayout(appOwnerGridLayout);

		createLabel(composite, SWT.NONE, Messages.AppfactoryApplicationDetailsView_19, new GridData(), scroller.getBackground(), new Font(
				null, Messages.AppfactoryApplicationDetailsView_20, 15, SWT.BOLD));

		createCompositeLabel(composite, names[3]);
		descriptionComposite = new Composite(composite, SWT.NONE);
		descriptionComposite.setBackground(tabFolder.getBackground());
		GridLayout descriptionGridLayout = new GridLayout(1, false);
		descriptionGridLayout.marginWidth = 20;
		descriptionComposite.setLayout(descriptionGridLayout);

		createLabel(composite, SWT.NONE, Messages.AppfactoryApplicationDetailsView_21, new GridData(), scroller.getBackground(), new Font(
				null, Messages.AppfactoryApplicationDetailsView_22, 15, SWT.BOLD));

		appInfoTabItem.setControl(scroller);
		composite.pack();
		composite.layout();
	}
	
	private void createDbInfoPage() {
		ScrolledComposite scroller = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		scroller.setBackground(tabFolder.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		// scroller.setExpandVertical(true);
		scroller.setExpandHorizontal(true);
		Composite composite = new Composite(scroller, SWT.NULL);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		composite.setBackground(tabFolder.getBackground());
		scroller.setContent(composite);
		scroller.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		String[] names = new String[] { Messages.AppfactoryApplicationDetailsView_23, Messages.AppfactoryApplicationDetailsView_24, Messages.AppfactoryApplicationDetailsView_25};

		createLabel(composite, SWT.NONE, Messages.AppfactoryApplicationDetailsView_26, new GridData(), scroller.getBackground(), new Font(
				null, Messages.AppfactoryApplicationDetailsView_27, 15, SWT.BOLD));

		createCompositeLabel(composite, names[0]);
		databaseInfoComposite = new Composite(composite, SWT.NONE);
		databaseInfoComposite.setBackground(tabFolder.getBackground());
		GridLayout appTypeGridLayout = new GridLayout(1, false);
		appTypeGridLayout.marginWidth = 20;
		databaseInfoComposite.setLayout(appTypeGridLayout);

		createLabel(composite, SWT.NONE, Messages.AppfactoryApplicationDetailsView_28, new GridData(), scroller.getBackground(), new Font(
				null, Messages.AppfactoryApplicationDetailsView_29, 15, SWT.BOLD));

		createCompositeLabel(composite, names[1]);
		databaseUsersComposite = new Composite(composite, SWT.NONE);
		databaseUsersComposite.setBackground(tabFolder.getBackground());
		GridLayout repoTypeGridLayout = new GridLayout(1, false);
		repoTypeGridLayout.marginWidth = 20;
		databaseUsersComposite.setLayout(repoTypeGridLayout);

		createLabel(composite, SWT.NONE, Messages.AppfactoryApplicationDetailsView_30, new GridData(), scroller.getBackground(), new Font(
				null, Messages.AppfactoryApplicationDetailsView_31, 15, SWT.BOLD));

		createCompositeLabel(composite, names[2]);
		databaseTemplatesComposite = new Composite(composite, SWT.NONE);
		databaseTemplatesComposite.setBackground(tabFolder.getBackground());
		GridLayout appOwnerGridLayout = new GridLayout(1, false);
		appOwnerGridLayout.marginWidth = 20;
		databaseTemplatesComposite.setLayout(appOwnerGridLayout);

		createLabel(composite, SWT.NONE, Messages.AppfactoryApplicationDetailsView_32, new GridData(), scroller.getBackground(), new Font(
				null, Messages.AppfactoryApplicationDetailsView_33, 15, SWT.BOLD));
		composite.pack();
		composite.layout();
	}
	
	
	
	

	/**
	 * Create current status tab page
	 */
	private void createCurrentStatusPage() {
		ScrolledComposite scroller = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		scroller.setBackground(tabFolder.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		scroller.setExpandVertical(true);
		scroller.setExpandHorizontal(true);
		Composite composite = new Composite(scroller, SWT.NULL);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		scroller.setContent(composite);
		scroller.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		currentStatusTable = new Table(composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		currentStatusTable.setLinesVisible(true);
		currentStatusTable.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		currentStatusTable.setLayoutData(data);

		String[] titles = { Messages.AppfactoryApplicationDetailsView_34, Messages.AppfactoryApplicationDetailsView_35, Messages.AppfactoryApplicationDetailsView_36 };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(currentStatusTable, SWT.BOLD);
			column.setText(titles[i]);
			column.pack();
		}

		currentStatusTabItem.setControl(scroller);
		// composite.pack();
	}

	/**
	 * Create team details tab page
	 */
	private void createTeamDetailsPage() {
		ScrolledComposite scroller = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		scroller.setBackground(tabFolder.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		scroller.setExpandVertical(true);
		scroller.setExpandHorizontal(true);
		Composite composite = new Composite(scroller, SWT.NULL);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		composite.setBackground(tabFolder.getBackground());
		scroller.setContent(composite);
		scroller.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		teamDetailsTable = new Table(composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		teamDetailsTable.setLinesVisible(true);
		teamDetailsTable.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		teamDetailsTable.setLayoutData(data);

		String[] titles = { Messages.AppfactoryApplicationDetailsView_37, Messages.AppfactoryApplicationDetailsView_38, Messages.AppfactoryApplicationDetailsView_39 };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(teamDetailsTable, SWT.BOLD);
			column.setText(titles[i]);
			column.pack();
		}

		teamTabItem.setControl(scroller);
	}

	/**
	 * Create data sources tab page
	 */
	private void createDataSourcesPage() {
		ScrolledComposite scroller = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		scroller.setBackground(tabFolder.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		scroller.setExpandVertical(true);
		scroller.setExpandHorizontal(true);
		Composite composite = new Composite(scroller, SWT.NULL);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		composite.setBackground(tabFolder.getBackground());
		scroller.setContent(composite);
		scroller.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		dataSourcesTable = new Table(composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		dataSourcesTable.setLinesVisible(true);
		dataSourcesTable.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		dataSourcesTable.setLayoutData(data);

		String[] titles = { Messages.AppfactoryApplicationDetailsView_40,Messages.AppfactoryApplicationDetailsView_41, Messages.AppfactoryApplicationDetailsView_42, Messages.AppfactoryApplicationDetailsView_43 };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(dataSourcesTable, SWT.BOLD);
			column.setText(titles[i]);
			column.pack();
		}

		dataSourcesTabItem.setControl(scroller);

	}

	/**
	 * Display application info of the selected application
	 * 
	 * @param applicationInfo
	 */
	private void updateAppInfo(ApplicationInfo applicationInfo) {
		removeChildControls(appTypeComposite);
		if (applicationInfo.getType() != null && !applicationInfo.getType().equals(Messages.AppfactoryApplicationDetailsView_44)) {
			createLabel(appTypeComposite, SWT.NONE, applicationInfo.getType(), getGridData(),
					tabFolder.getBackground(), null);
		} else {
			createLabel(appTypeComposite, SWT.NONE, DEFAULT_VALUE, getGridData(),
					tabFolder.getBackground(), null);
		}
		appTypeComposite.pack();
		appTypeComposite.layout();

		removeChildControls(repoTypeComposite);
		if (applicationInfo.getRepositoryType() != null
				&& !applicationInfo.getRepositoryType().equals(Messages.AppfactoryApplicationDetailsView_45)) {
			createLabel(repoTypeComposite, SWT.NONE, applicationInfo.getRepositoryType(),
					getGridData(), tabFolder.getBackground(), null);
		} else {
			createLabel(repoTypeComposite, SWT.NONE, DEFAULT_VALUE, getGridData(),
					tabFolder.getBackground(), null);
		}
		repoTypeComposite.pack();
		repoTypeComposite.layout();
		removeChildControls(appOwnerComposite);
		if (applicationInfo.getApplicationOwner() != null
				&& !applicationInfo.getApplicationOwner().equals(Messages.AppfactoryApplicationDetailsView_46)) {
			createLabel(appOwnerComposite, SWT.NONE, applicationInfo.getApplicationOwner(),
					getGridData(), tabFolder.getBackground(), null);
		} else {
			createLabel(appOwnerComposite, SWT.NONE, DEFAULT_VALUE, getGridData(),
					tabFolder.getBackground(), null);
		}
		appOwnerComposite.pack();
		appOwnerComposite.layout();
		removeChildControls(descriptionComposite);
		if (applicationInfo.getDescription() != null
				&& !applicationInfo.getDescription().equals(Messages.AppfactoryApplicationDetailsView_47)) {
			createLabel(descriptionComposite, SWT.NONE, applicationInfo.getDescription(),
					getGridData(), tabFolder.getBackground(), null);
		} else {
			createLabel(descriptionComposite, SWT.NONE, DEFAULT_VALUE, getGridData(),
					tabFolder.getBackground(), null);
		}
		descriptionComposite.pack();
		descriptionComposite.layout();
	}

	/**
	 * Display current status of the selected application
	 * 
	 * @param applicationInfo
	 */
	private void updateBuildStatus(ApplicationInfo applicationInfo) {
		// Remove existing
		currentStatusTable.removeAll();

		// Add new
		List<AppVersionInfo> version = applicationInfo.getappVersionList();
		for (AppVersionInfo appVersionInfo : version) {
			TableItem item = new TableItem(currentStatusTable, SWT.NONE);
			item.setText(0, appVersionInfo.getVersion());
			item.setText(1, appVersionInfo.getLastBuildResult());
			item.setText(2, appVersionInfo.getRepoURL());
		}

		// Pack the new one to table
		for (int i = 0; i < currentStatusTable.getColumnCount(); i++) {
			currentStatusTable.getColumn(i).pack();
		}
	}

	/**
	 * Display team details of the selected application
	 * 
	 * @param applicationInfo
	 */
	private void updateTeamDetails(ApplicationInfo applicationInfo) {
		// Remove existing
		teamDetailsTable.removeAll();

		// Add new
		List<AppUserInfo> appUsers = applicationInfo.getApplicationDevelopers();
		for (AppUserInfo appUser : appUsers) {
			TableItem item = new TableItem(teamDetailsTable, SWT.NONE);
			item.setText(0, appUser.getUserDisplayName());
			item.setText(1, appUser.getUserName());
			item.setText(2, appUser.getRoles());
		}

		// Pack the new one to table
		for (int i = 0; i < teamDetailsTable.getColumnCount(); i++) {
			teamDetailsTable.getColumn(i).pack();
		}
	}

	/**
	 * Display data sources information of the selected application.
	 * 
	 * @param applicationInfo
	 */
	private void updateDataSources(ApplicationInfo applicationInfo) {
		
		List<AppDBinfo> dataSources = applicationInfo.getDatabases();
		AppDBinfo appDBinfo = dataSources.get(0);
		removeChildControls(databaseInfoComposite);
		List<Map<String, String>> dbs = appDBinfo.getDbs();
	  
		if (dbs!= null) {
			  for (Map<String, String> map : dbs) {
				  createLabel(databaseInfoComposite, SWT.NONE, map.get(Messages.AppfactoryApplicationDetailsView_48), getGridData(),
							tabFolder.getBackground(), null);
				}

		} else {
			createLabel(databaseInfoComposite, SWT.NONE, DEFAULT_VALUE, getGridData(),
					tabFolder.getBackground(), null);
		}
		databaseInfoComposite.pack();
		databaseInfoComposite.layout();

		removeChildControls(databaseUsersComposite);
		List<String> usr = appDBinfo.getUsr();
		
		if (usr!=null) {
			for (String name : usr) {
				createLabel(databaseUsersComposite, SWT.NONE, name,
						getGridData(), tabFolder.getBackground(), null);
			}
			
		} else {
			createLabel(databaseUsersComposite, SWT.NONE, DEFAULT_VALUE, getGridData(),
					tabFolder.getBackground(), null);
		}
		databaseUsersComposite.pack();
		databaseUsersComposite.layout();
	 
		removeChildControls(databaseTemplatesComposite);
		List<String> temp = appDBinfo.getTemplates();
		
		if (temp!=null) {
			for (String name : temp) {
				createLabel(databaseTemplatesComposite, SWT.NONE, name,
						getGridData(), tabFolder.getBackground(), null);
			}
			
		} else {
			createLabel(databaseTemplatesComposite, SWT.NONE, DEFAULT_VALUE, getGridData(),
					tabFolder.getBackground(), null);
		}
		databaseTemplatesComposite.pack();
		databaseTemplatesComposite.layout();
 
		/*// Remove existing
		dataSourcesTable.removeAll();

		// Add new
		List<AppDBinfo> dataSources = applicationInfo.getDatabases();
		for (AppDBinfo db : dataSources) {
			TableItem item = new TableItem(dataSourcesTable, SWT.NONE);
			
			//item.setText(0, db.getDbs());
			item.setText(1, db.getUsr());
			item.setText(2, db.getTemplates());
		}

		// Pack the new one to table
		for (int i = 0; i < dataSourcesTable.getColumnCount(); i++) {
			dataSourcesTable.getColumn(i).pack();
		}*/
	}

}
