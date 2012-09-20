/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/**
 * Project: de.dim.bfr.ui
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/editors/EstimatedStatisticModelEditor.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.menus.IMenuService;

import de.dim.bfr.BfrPackage;
import de.dim.bfr.BfrPackage.Literals;
import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.LevelTyp;
import de.dim.bfr.ParameterCovCor;
import de.dim.bfr.ParameterRoleType;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellKatalog;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingungList;
import de.dim.bfr.provider.BfrItemProviderAdapterFactory;
import de.dim.bfr.ui.databinding.DIMControlDecorationSupport;
import de.dim.bfr.ui.databinding.DIMDatabindingHelper;
import de.dim.bfr.ui.databinding.MessageManagerSupport;
import de.dim.bfr.ui.databinding.ObservableListSizeObservableValue;
import de.dim.bfr.ui.databinding.converter.BooleanNotNullConverter;
import de.dim.bfr.ui.databinding.converter.String2IntegerConverter;
import de.dim.bfr.ui.databinding.validators.ActiveNotEmptyValidator;
import de.dim.bfr.ui.dialogs.CovCorParameterEditDialog;
import de.dim.bfr.ui.dialogs.EstimatedParameterEditDialog;
import de.dim.bfr.ui.dialogs.LiteratureSelectionDialog;
import de.dim.bfr.ui.internal.BFRUIActivator;
import de.dim.bfr.ui.message.Messages;
import de.dim.bfr.ui.services.BFRUIService;

/**
 * Editor to handle statistic model editing 
 * @author Mark Hoffmann
 * @since 13.11.2011
 */
public class EstimatedStatisticModelEditor extends AbstractEMFEditor {

	public static final String ID = "de.dim.bfr.ui.editors.EstimatedStatisticModelEditor"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private GeschaetztStatistikModell statisticModel;
	private BFRUIService service;

	private TableViewer parameterViewer;
	private TableViewer covCorParameterViewer;
	private TableViewer literatureViewer;
	private ComboViewer conditionsViewer;
	private ComboViewer modelViewer;
	private ComboViewer responseViewer;
	private Text textRSquare;
	private Text textScore;
	private Text textComment;
	private Text textRSS;
	private Button deleteLiteratureButton;
//	private Button manualButton;
	private Button deleteParameterButton;
	private Button editParameterButton;
	private Button deleteCovCorButton;
	private Button editCovCorButton;
	private Button addParameterButton;
	private Button addCovCorButton;
	private Button selectLiteratureButton;
	private IObservableList parameterInputOL;
	private IObservableList literatureInputOL;
	private IObservable modelLevelOV;
	private IObservableValue primaryModelOV;
	private IChangeListener literatureChangeListener;
	private IChangeListener modelLevelChangeListener;
	private IChangeListener primaryModelChangeListener;

	public EstimatedStatisticModelEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent the parent composite
	 */
	@Override
	public void createPartControl(Composite parent) {
		ScrolledForm scrolledForm = formToolkit.createScrolledForm(parent);
		Form form = scrolledForm.getForm();
		form.setText(Messages.EstimatedStatisticModelEditor_0);
		formToolkit.decorateFormHeading(form);
		form.getBody().setLayout(new GridLayout(1, false));
		ToolBarManager toolbarManager = (ToolBarManager) form.getToolBarManager();
		toolbarManager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		IMenuService menuService = (IMenuService) getSite().getService(IMenuService.class);
		menuService.populateContributionManager(toolbarManager, "toolbar:" + ID + ".toolbar"); //$NON-NLS-1$ //$NON-NLS-2$
		toolbarManager.update(true);
		
		// configure message manager
		IMessageManager manager = form.getMessageManager();
		manager.setDecorationPosition(SWT.TOP | SWT.LEFT);
		MessageManagerSupport.create(new ManagedForm(formToolkit, scrolledForm), dbc);
		
		// create the general section
		createGeneralSection(scrolledForm);
		// create the parameter section
		createParameterSection(scrolledForm);
		// create the cov cor parameter section
		createCovCorParameterSection(scrolledForm);
		
		// the data binding
		initDataBindings();
	}

	public void setFocus() {
		// Set the focus
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.editors.AbstractEMFEditor#internalDoSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected boolean internalDoSave(IProgressMonitor monitor) {
		if (!isValid()) {
			MessageDialog.openError(getSite().getShell(), Messages.EstimatedStatisticModelEditor_3, Messages.EstimatedStatisticModelEditor_4);
			return false;
		}
		return service.saveEstimatedStatisticModel(statisticModel);
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.editors.AbstractEMFEditor#internalDoSaveAs()
	 */
	@Override
	protected boolean internalDoSaveAs() {
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.editors.AbstractEMFEditor#getAdapterFactories()
	 */
	@Override
	protected List<AdapterFactory> getAdapterFactories() {
		List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new BfrItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return factories;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.editors.AbstractEMFEditor#doInit(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	protected void doInit(IEditorSite site, IEditorInput input) {
		setTitleImage(input.getImageDescriptor().createImage());
		setTitleToolTip(input.getToolTipText());
		statisticModel = (GeschaetztStatistikModell) getModel();
		service = BFRUIActivator.getBFRService();
	}

	/**
	 * Creates the general section
	 * @param scrolledForm the scrolled form
	 */
	private void createGeneralSection(ScrolledForm scrolledForm) {
		Section generalSection = formToolkit.createSection(scrolledForm.getBody(), Section.TITLE_BAR);
		generalSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		generalSection.setText(Messages.EstimatedStatisticModelEditor_5);
		formToolkit.paintBordersFor(generalSection);
		
		Composite generalComposite = formToolkit.createComposite(generalSection, SWT.NONE);
		generalSection.setClient(generalComposite);
		GridLayout gl_generalComposite = new GridLayout(3, false);
		gl_generalComposite.horizontalSpacing = 7;
		generalComposite.setLayout(gl_generalComposite);
		formToolkit.paintBordersFor(generalComposite);
		
		Label labelName = formToolkit.createLabel(generalComposite, Messages.EstimatedStatisticModelEditor_6, SWT.NONE);
		GridData gd_labelName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelName.widthHint = 100;
		labelName.setLayoutData(gd_labelName);
		
		CCombo comboModel = new CCombo(generalComposite, SWT.READ_ONLY);
		comboModel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboModel.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(comboModel);
		formToolkit.paintBordersFor(comboModel);
		modelViewer = new ComboViewer(comboModel);
		ObservableListContentProvider modelContentProvider = new ObservableListContentProvider();
		modelViewer.setContentProvider(modelContentProvider);
		IObservableSet inputList = modelContentProvider.getKnownElements();
		IObservableMap modelLabelMap = EMFEditProperties.value(getEditingDomain(), Literals.STATISTIK_MODELL__NAME).observeDetail(inputList);
		modelViewer.setLabelProvider(new ObservableMapLabelProvider(modelLabelMap));
		StatistikModellKatalog allStatisticModels = service.getAllStatisticModels();
		IObservableList allModelsInput = EMFEditProperties.list(getEditingDomain(), Literals.STATISTIK_MODELL_KATALOG__MODELLE).observe(allStatisticModels);
		modelViewer.setInput(allModelsInput);
		
		Label labelConditions = formToolkit.createLabel(generalComposite, Messages.EstimatedStatisticModelEditor_7, SWT.NONE);
		GridData gd_labelConditions = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelConditions.widthHint = 100;
		labelConditions.setLayoutData(gd_labelConditions);
		
		CCombo comboConditions = new CCombo(generalComposite, SWT.READ_ONLY);
		comboConditions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboConditions.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(comboConditions);
		formToolkit.paintBordersFor(comboConditions);
		conditionsViewer = new ComboViewer(comboConditions);
		ObservableListContentProvider conditionsContentProvider = new ObservableListContentProvider();
		conditionsViewer.setContentProvider(conditionsContentProvider);
		IObservableSet conditionsInputList = conditionsContentProvider.getKnownElements();
		IObservableMap conditionLabelMap = EMFEditProperties.value(getEditingDomain(), Literals.VERSUCHS_BEDINGUNG__ID_CB).observeDetail(conditionsInputList);
		conditionsViewer.setLabelProvider(new ObservableMapLabelProvider(conditionLabelMap));
		VersuchsBedingungList allConditions = service.getAllVersuchbedingungen();
		IObservableList allConditionsInput = EMFEditProperties.list(getEditingDomain(), Literals.VERSUCHS_BEDINGUNG_LIST__BEDINGUNGEN).observe(allConditions);
		conditionsViewer.setInput(allConditionsInput);
//		
//		Label label = formToolkit.createLabel(generalComposite, "", SWT.NONE);
//		manualButton = formToolkit.createButton(generalComposite, "Manuel", SWT.CHECK);
//		statisticModel.setManuellEingetragen(true);
//		manualButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label labelRSquare = formToolkit.createLabel(generalComposite, Messages.EstimatedStatisticModelEditor_10, SWT.NONE);
		GridData gd_labelRSquare = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelRSquare.widthHint = 100;
		labelRSquare.setLayoutData(gd_labelRSquare);
		
		textRSquare = formToolkit.createText(generalComposite, "", SWT.NONE); //$NON-NLS-1$
		textRSquare.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label labelRSS = formToolkit.createLabel(generalComposite, Messages.EstimatedStatisticModelEditor_12, SWT.NONE);
		GridData gd_labelRSS = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelRSS.widthHint = 100;
		labelRSS.setLayoutData(gd_labelRSS);

		textRSS = formToolkit.createText(generalComposite, "", SWT.NONE); //$NON-NLS-1$
		textRSS.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label labelScore = formToolkit.createLabel(generalComposite, Messages.EstimatedStatisticModelEditor_14, SWT.NONE);
		GridData gd_labelScore = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelScore.widthHint = 100;
		labelScore.setLayoutData(gd_labelScore);
		
		textScore = formToolkit.createText(generalComposite, "", SWT.NONE); //$NON-NLS-1$
		textScore.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label responseName = formToolkit.createLabel(generalComposite, Messages.EstimatedStatisticModelEditor_8, SWT.NONE);
		GridData gd_responseName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_responseName.widthHint = 100;
		responseName.setLayoutData(gd_responseName);
		
		CCombo comboResponse = new CCombo(generalComposite, SWT.READ_ONLY);
		comboResponse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboResponse.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(comboResponse);
		formToolkit.paintBordersFor(comboResponse);
		responseViewer = new ComboViewer(comboResponse);
		ObservableListContentProvider responseContentProvider = new ObservableListContentProvider();
		responseViewer.setContentProvider(responseContentProvider);
		IObservableSet responseList = responseContentProvider.getKnownElements();
		IObservableMap responseLabelMap = EMFEditProperties.value(getEditingDomain(), Literals.STATISTIK_MODELL_PARAMETER__NAME).observeDetail(responseList);
		responseViewer.setLabelProvider(new ObservableMapLabelProvider(responseLabelMap));
		IObservableList allResponsesInput = EMFEditProperties.list(getEditingDomain(), FeaturePath.fromList(Literals.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL, Literals.STATISTIK_MODELL__PARAMETER)).observe(statisticModel);
		responseViewer.setInput(allResponsesInput);
		
		Label labelLiterature = formToolkit.createLabel(generalComposite, Messages.EstimatedStatisticModelEditor_16, SWT.NONE);
		GridData gd_labelLiterature = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 3);
		gd_labelLiterature.widthHint = 100;
		labelLiterature.setLayoutData(gd_labelLiterature);
		
		Table literatureTable = formToolkit.createTable(generalComposite, SWT.FULL_SELECTION | SWT.MULTI);
		literatureTable.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(literatureTable);
		formToolkit.paintBordersFor(literatureTable);
		GridData gd_tableLiterature = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3);
		gd_tableLiterature.heightHint = 75;
		literatureTable.setLayoutData(gd_tableLiterature);
		literatureViewer = new TableViewer(literatureTable);
		TableViewerColumn titelVC = new TableViewerColumn(literatureViewer, SWT.NONE);
		TableColumn titleCol = titelVC.getColumn();
		titleCol.setWidth(250);
		titleCol.setText(Messages.EstimatedStatisticModelEditor_17);
		
		TableViewerColumn authorVC = new TableViewerColumn(literatureViewer, SWT.NONE);
		TableColumn authorCol = authorVC.getColumn();
		authorCol.setWidth(250);
		authorCol.setText(Messages.EstimatedStatisticModelEditor_18);
		
		selectLiteratureButton = formToolkit.createButton(generalComposite, Messages.EstimatedStatisticModelEditor_19, SWT.PUSH);
		GridData gd_selectButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_selectButton.widthHint = 100;
		selectLiteratureButton.setLayoutData(gd_selectButton);
		selectLiteratureButton.addSelectionListener(new SelectionAdapter() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				LiteratureSelectionDialog dialog = new LiteratureSelectionDialog(getSite().getShell(), statisticModel, getEditingDomain());
				dialog.open();
			}
		});
		deleteLiteratureButton = formToolkit.createButton(generalComposite, Messages.EstimatedStatisticModelEditor_20, SWT.PUSH);
		GridData gd_deleteButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_deleteButton.widthHint = 100;
		deleteLiteratureButton.setLayoutData(gd_deleteButton);
		deleteLiteratureButton.addSelectionListener(new SelectionAdapter() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object[] objects = ((IStructuredSelection)literatureViewer.getSelection()).toArray();
				List<Object> objectList = Arrays.asList(objects);
				Command removeCommand = RemoveCommand.create(getEditingDomain(), statisticModel, BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__LITERATUR, objectList);
				getEditingDomain().getCommandStack().execute(removeCommand);
			}
		});
		formToolkit.createLabel(generalComposite, ""); //$NON-NLS-1$
		
		Label labelComment = formToolkit.createLabel(generalComposite, Messages.EstimatedStatisticModelEditor_22, SWT.NONE);
		GridData gd_labelComment = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelComment.widthHint = 100;
		labelComment.setLayoutData(gd_labelComment);
		
		textComment = formToolkit.createText(generalComposite, "", SWT.WRAP | SWT.V_SCROLL | SWT.MULTI); //$NON-NLS-1$
		GridData gd_textComment = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_textComment.heightHint = 50;
		textComment.setLayoutData(gd_textComment);
	}
	
	/**
	 * Creates the cov cor parameter section
	 * @param scrolledForm the scrolled form
	 */
	private void createCovCorParameterSection(ScrolledForm scrolledForm) {
		Section covCorParameterSection = formToolkit.createSection(scrolledForm.getBody(), Section.TITLE_BAR);
		covCorParameterSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		covCorParameterSection.setText(Messages.EstimatedStatisticModelEditor_24);
		formToolkit.paintBordersFor(covCorParameterSection);
		
		Composite covCorParameterComposite = formToolkit.createComposite(covCorParameterSection, SWT.NONE);
		covCorParameterSection.setClient(covCorParameterComposite);
		GridLayout gl_generalComposite = new GridLayout(3, false);
		gl_generalComposite.horizontalSpacing = 7;
		covCorParameterComposite.setLayout(gl_generalComposite);
		formToolkit.paintBordersFor(covCorParameterComposite);
		
		Label labelParameter = formToolkit.createLabel(covCorParameterComposite, "", SWT.NONE); //$NON-NLS-1$
		GridData gd_labelName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 4);
		gd_labelName.widthHint = 100;
		labelParameter.setLayoutData(gd_labelName);
		
		Table covCorParameterTable = new Table(covCorParameterComposite, SWT.FULL_SELECTION | SWT.MULTI);
		covCorParameterTable.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(covCorParameterTable);
		formToolkit.paintBordersFor(covCorParameterTable);
		GridData gd_tableParameter = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 4);
		gd_tableParameter.heightHint = 75;
		covCorParameterTable.setLayoutData(gd_tableParameter);
		covCorParameterViewer = new TableViewer(covCorParameterTable);
		covCorParameterTable.setLinesVisible(true);
		covCorParameterTable.setHeaderVisible(true);
		TableViewerColumn param1VC = new TableViewerColumn(covCorParameterViewer, SWT.NONE);
		param1VC.getColumn().setWidth(200);
		param1VC.getColumn().setText(Messages.EstimatedStatisticModelEditor_26);
		param1VC.getColumn().setResizable(true);
		TableViewerColumn param2VC = new TableViewerColumn(covCorParameterViewer, SWT.NONE);
		param2VC.getColumn().setWidth(200);
		param2VC.getColumn().setText(Messages.EstimatedStatisticModelEditor_27);
		param2VC.getColumn().setResizable(true);
		TableViewerColumn valueVC = new TableViewerColumn(covCorParameterViewer, SWT.NONE);
		valueVC.getColumn().setWidth(125);
		valueVC.getColumn().setText(Messages.EstimatedStatisticModelEditor_28);
		valueVC.getColumn().setResizable(true);
		TableViewerColumn covCorVC = new TableViewerColumn(covCorParameterViewer, SWT.NONE);
		covCorVC.getColumn().setWidth(50);
		covCorVC.getColumn().setText(Messages.EstimatedStatisticModelEditor_1);
		covCorVC.getColumn().setResizable(true);
		
		addCovCorButton = formToolkit.createButton(covCorParameterComposite, Messages.EstimatedStatisticModelEditor_29, SWT.PUSH);
		GridData gd_addButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_addButton.widthHint = 100;
		addCovCorButton.setLayoutData(gd_addButton);
		addCovCorButton.addSelectionListener(new SelectionAdapter() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				CovCorParameterEditDialog dialog = new CovCorParameterEditDialog(getSite().getShell(), statisticModel, getEditingDomain());
				dialog.open();
			}
		});
		editCovCorButton = formToolkit.createButton(covCorParameterComposite, Messages.EstimatedStatisticModelEditor_30, SWT.PUSH);
		GridData gd_editButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_editButton.widthHint = 100;
		editCovCorButton.setLayoutData(gd_editButton);
		editCovCorButton.addSelectionListener(new SelectionAdapter() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object selection = ((IStructuredSelection)covCorParameterViewer.getSelection()).getFirstElement();
				ParameterCovCor parameter = (ParameterCovCor) selection;
				CovCorParameterEditDialog dialog = new CovCorParameterEditDialog(getSite().getShell(), statisticModel, parameter, getEditingDomain());
				dialog.open();
			}
		});
		deleteCovCorButton = formToolkit.createButton(covCorParameterComposite, Messages.EstimatedStatisticModelEditor_31, SWT.PUSH);
		GridData gd_deleteButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_deleteButton.widthHint = 100;
		deleteCovCorButton.setLayoutData(gd_deleteButton);
		deleteCovCorButton.addSelectionListener(new SelectionAdapter() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object[] objects = ((IStructuredSelection)covCorParameterViewer.getSelection()).toArray();
				List<Object> objectList = Arrays.asList(objects);
				Command removeCommand = RemoveCommand.create(getEditingDomain(), statisticModel, BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR, objectList);
				getEditingDomain().getCommandStack().execute(removeCommand);
			}
		});
	}

	/**
	 * Creates the parameter section
	 * @param scrolledForm the scrolle form
	 */
	private void createParameterSection(ScrolledForm scrolledForm) {
		Section parameterSection = formToolkit.createSection(scrolledForm.getBody(), Section.TITLE_BAR);
		parameterSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		parameterSection.setText(Messages.EstimatedStatisticModelEditor_32);
		formToolkit.paintBordersFor(parameterSection);
		
		Composite parameterComposite = formToolkit.createComposite(parameterSection, SWT.NONE);
		parameterSection.setClient(parameterComposite);
		GridLayout gl_generalComposite = new GridLayout(3, false);
		gl_generalComposite.horizontalSpacing = 7;
		parameterComposite.setLayout(gl_generalComposite);
		formToolkit.paintBordersFor(parameterComposite);
		
		Label labelParameter = formToolkit.createLabel(parameterComposite, "", SWT.NONE); //$NON-NLS-1$
		GridData gd_labelName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 4);
		gd_labelName.widthHint = 100;
		labelParameter.setLayoutData(gd_labelName);
		
		Table parameterTable = new Table(parameterComposite, SWT.FULL_SELECTION | SWT.MULTI);
		parameterTable.setLinesVisible(true);
		parameterTable.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(parameterTable);
		formToolkit.paintBordersFor(parameterTable);
		GridData gd_tableParameter = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 4);
		gd_tableParameter.heightHint = 75;
		parameterTable.setLayoutData(gd_tableParameter);
		parameterViewer = new TableViewer(parameterTable);
		
		addParameterButton = formToolkit.createButton(parameterComposite, Messages.EstimatedStatisticModelEditor_34, SWT.PUSH);
		GridData gd_addButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_addButton.widthHint = 100;
		addParameterButton.setLayoutData(gd_addButton);
		addParameterButton.addSelectionListener(new SelectionAdapter() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				EstimatedParameterEditDialog dialog = new EstimatedParameterEditDialog(getSite().getShell(), statisticModel, getEditingDomain());
				dialog.open();
			}
		});
		editParameterButton = formToolkit.createButton(parameterComposite, Messages.EstimatedStatisticModelEditor_35, SWT.PUSH);
		GridData gd_editButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_editButton.widthHint = 100;
		editParameterButton.setLayoutData(gd_editButton);
		editParameterButton.addSelectionListener(new SelectionAdapter() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object selection = ((IStructuredSelection)parameterViewer.getSelection()).getFirstElement();
				GeschModellParameter parameter = (GeschModellParameter) selection;
				EstimatedParameterEditDialog dialog = new EstimatedParameterEditDialog(getSite().getShell(), statisticModel, parameter, getEditingDomain());
				dialog.open();
			}
		});
		deleteParameterButton = formToolkit.createButton(parameterComposite, Messages.EstimatedStatisticModelEditor_36, SWT.PUSH);
		GridData gd_deleteButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_deleteButton.widthHint = 100;
		deleteParameterButton.setLayoutData(gd_deleteButton);
		deleteParameterButton.addSelectionListener(new SelectionAdapter() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object[] objects = ((IStructuredSelection)parameterViewer.getSelection()).toArray();
				List<Object> objectList = Arrays.asList(objects);
				Command removeCommand = RemoveCommand.create(getEditingDomain(), statisticModel, BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__PARAMETER, objectList);
				getEditingDomain().getCommandStack().execute(removeCommand);
			}
		});
	}

	/**
	 * Initializes the data binding
	 */
	private void initDataBindings() {
		// 
		final IObservableValue manualModelOV = EMFEditProperties.value(getEditingDomain(), Literals.GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN).observe(statisticModel);
		
		primaryModelOV = EMFEditProperties.value(getEditingDomain(), FeaturePath.fromList(Literals.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL, Literals.STATISTIK_MODELL__LEVEL)).observe(statisticModel);
				
		IObservableValue modelSelectionOV = ViewersObservables.observeSingleSelection(modelViewer);
		IObservableValue modelModelOV = EMFEditProperties.value(getEditingDomain(), Literals.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL).observe(statisticModel);
		dbc.bindValue(modelSelectionOV, modelModelOV, new UpdateValueStrategy().setAfterGetValidator(new ActiveNotEmptyValidator(Messages.EstimatedStatisticModelEditor_37, manualModelOV)), new UpdateValueStrategy().setAfterGetValidator(new ActiveNotEmptyValidator(Messages.EstimatedStatisticModelEditor_38, manualModelOV)));
		bindTitleName(modelSelectionOV, new UpdateValueStrategy().setConverter(new IConverter() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
			 */
			@Override
			public Object getToType() {
				return String.class;
			}
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
			 */
			@Override
			public Object getFromType() {
				return StatistikModell.class;
			}
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
			 */
			@Override
			public Object convert(Object fromObject) {
				if (fromObject == null) {
					return Messages.EstimatedStatisticModelEditor_39;
				}
				return ((StatistikModell)fromObject).getName();
			}
		}));
		
//		dbc.bindValue(SWTObservables.observeSelection(manualButton), manualModelOV);
		
		IObservableValue addCovCorEnablementOV = SWTObservables.observeEnabled(addCovCorButton);
		dbc.bindValue(addCovCorEnablementOV, manualModelOV);
//		IObservableValue selectLiteratureEnablementOV = SWTObservables.observeEnabled(selectLiteratureButton);
//		dbc.bindValue(selectLiteratureEnablementOV, manualModelOV);
		IObservableValue addParameterEnablementOV = SWTObservables.observeEnabled(addParameterButton);
		dbc.bindValue(addParameterEnablementOV, manualModelOV);
		IObservableValue conditionsViewerEnablementOV = SWTObservables.observeEnabled(conditionsViewer.getCCombo());
		dbc.bindValue(conditionsViewerEnablementOV, manualModelOV);
		IObservableValue covCovViewerEnablementOV = SWTObservables.observeEnabled(covCorParameterViewer.getTable());
		dbc.bindValue(covCovViewerEnablementOV, manualModelOV);
//		IObservableValue literatureViewerEnablementOV = SWTObservables.observeEnabled(literatureViewer.getTable());
//		dbc.bindValue(literatureViewerEnablementOV, manualModelOV);
		IObservableValue modelViewerEnablementOV = SWTObservables.observeEnabled(modelViewer.getCCombo());
		dbc.bindValue(modelViewerEnablementOV, manualModelOV);
		IObservableValue parameterViewerEnablementOV = SWTObservables.observeEnabled(parameterViewer.getTable());
		dbc.bindValue(parameterViewerEnablementOV, manualModelOV);
		IObservableValue RSquareEnablementOV = SWTObservables.observeEnabled(textRSquare);
		dbc.bindValue(RSquareEnablementOV, manualModelOV);
		IObservableValue RSSEnablementOV = SWTObservables.observeEnabled(textRSS);
		dbc.bindValue(RSSEnablementOV, manualModelOV);
		// response only enabled for manual and secondary models
		final IObservableValue responseEnableModelOV = new ComputedValue() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.observable.value.ComputedValue#calculate()
			 */
			@Override
			protected Object calculate() {
				LevelTyp level = (LevelTyp) primaryModelOV.getValue();
				Boolean manual = (Boolean) manualModelOV.getValue();
				return !manual ? manual : (level == null || LevelTyp.PRIMARY.equals(level)) ? false : true;
			}
		};
		IObservableValue responseEnablementOV = SWTObservables.observeEnabled(responseViewer.getCCombo());
		dbc.bindValue(responseEnablementOV, responseEnableModelOV);
		//
		IObservableValue conditionSelectionOV = ViewersObservables.observeSingleSelection(conditionsViewer);
		IObservableValue conditionsModelOV = EMFEditProperties.value(getEditingDomain(), Literals.GESCHAETZT_STATISTIK_MODELL__BEDINGUNG).observe(statisticModel);
		dbc.bindValue(conditionSelectionOV, conditionsModelOV);
		// Number validator for min and max
		IValidator numberValidator = new IValidator() {

			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
			 */
			@Override
			public IStatus validate(Object value) {
				if (getBooleanValue(manualModelOV)) {
					return ValidationStatus.ok();
				}
				if (value == null || value.toString().length() == 0) {
					return ValidationStatus.error(Messages.EstimatedStatisticModelEditor_40);
				}
				if (value instanceof String) {
					try {
						Integer.parseInt(value.toString());
					} catch (Exception e) {
						return ValidationStatus.error(Messages.EstimatedStatisticModelEditor_41);
					}
				}
				return ValidationStatus.ok();
			}
		};
		// converter that handles empty values too
		// Number validator for min and max
		IValidator decimalNumberValidator = new IValidator() {

			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
			 */
			@Override
			public IStatus validate(Object value) {
				if (getBooleanValue(manualModelOV)) {
					return ValidationStatus.ok();
				}
				if (value == null || value.toString().length() == 0) {
					return ValidationStatus.error(Messages.EstimatedStatisticModelEditor_42);
				}
				if (value instanceof String) {
					try {
						Double.parseDouble(value.toString());
					} catch (Exception e) {
						return ValidationStatus.error(Messages.EstimatedStatisticModelEditor_43);
					}
				}
				return ValidationStatus.ok();
			}
		};
		// converter that handles empty values too
		IConverter decimalNumberConverter = new IConverter() {

			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
			 */
			@Override
			public Object getToType() {
				return Double.TYPE;
			}

			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
			 */
			@Override
			public Object getFromType() {
				return String.class;
			}

			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
			 */
			@Override
			public Object convert(Object fromObject) {
				if (fromObject == null || fromObject.toString().length() == 0) {
					return 0;
				}
				try {
					return Double.parseDouble(fromObject.toString());
				} catch (Exception e) {
					return 0;
				}
			}
		};
		//
		IObservableValue textRSquareTextOV = SWTObservables.observeText(textRSquare, SWT.Modify);
		IObservableValue modelRSquareOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.GESCHAETZT_STATISTIK_MODELL__RSQUARED);
		dbc.bindValue(textRSquareTextOV, modelRSquareOV,new UpdateValueStrategy().setAfterGetValidator(decimalNumberValidator).setConverter(decimalNumberConverter), null);
		//
		IObservableValue textRSSTextOV = SWTObservables.observeText(textRSS, SWT.Modify);
		IObservableValue modelRSSOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.GESCHAETZT_STATISTIK_MODELL__RSS);
		dbc.bindValue(textRSSTextOV, modelRSSOV,new UpdateValueStrategy().setAfterGetValidator(decimalNumberValidator).setConverter(decimalNumberConverter), null);
		
		//
		IObservableValue textScoreTextOV = SWTObservables.observeText(textScore, SWT.Modify);
		IObservableValue modelScoreOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.GESCHAETZT_STATISTIK_MODELL__SCORE);
		dbc.bindValue(textScoreTextOV, modelScoreOV, new UpdateValueStrategy().setAfterGetValidator(numberValidator).setConverter(new String2IntegerConverter()), null);
		
		IObservableValue responseSelectionOV = ViewersObservables.observeSingleSelection(responseViewer);
		IObservableValue responseModelOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.GESCHAETZT_STATISTIK_MODELL__RESPONSE);
		final Binding responseBinding = dbc.bindValue(responseSelectionOV, responseModelOV, new UpdateValueStrategy().setAfterConvertValidator(new IValidator() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
			 */
			@Override
			public IStatus validate(Object value) {
				Boolean enabled = (Boolean) responseEnableModelOV.getValue();
				return !enabled ? ValidationStatus.ok() : value == null ? ValidationStatus.error("Eine response muss ausgewählt sein") : ValidationStatus.ok() ;
			}
		}), null);
		DIMControlDecorationSupport.create(responseBinding, SWT.TOP | SWT.LEFT);
		primaryModelChangeListener = new IChangeListener() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.observable.IChangeListener#handleChange(org.eclipse.core.databinding.observable.ChangeEvent)
			 */
			@Override
			public void handleChange(ChangeEvent event) {
				responseBinding.updateTargetToModel();
			}
		};
		primaryModelOV.addChangeListener(primaryModelChangeListener);
		//
		IObservableValue textCommentObserveTextObserveWidget = SWTObservables.observeText(textComment, SWT.Modify);
		IObservableValue statisticModelKommentarObserveValue = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.GESCHAETZT_STATISTIK_MODELL__KOMMENTAR);
		dbc.bindValue(textCommentObserveTextObserveWidget, statisticModelKommentarObserveValue, null, null);
		IConverter null2BooleanConverter = new IConverter() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
			 */
			@Override
			public Object getToType() {
				return Boolean.TYPE;
			}
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
			 */
			@Override
			public Object getFromType() {
				return Object.class;
			}
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
			 */
			@Override
			public Object convert(Object fromObject) {
				return fromObject != null;
			}
		};
		dbc.bindValue(WidgetProperties.enabled().observe(addParameterButton), modelSelectionOV, DIMDatabindingHelper.createNeverUpdateValueStrategy(), new UpdateValueStrategy().setConverter(null2BooleanConverter));
		dbc.bindValue(SWTObservables.observeEnabled(deleteParameterButton), ViewersObservables.observeSingleSelection(parameterViewer), null, new UpdateValueStrategy().setConverter(new BooleanNotNullConverter()));
		dbc.bindValue(SWTObservables.observeEnabled(editParameterButton), ViewersObservables.observeSingleSelection(parameterViewer), null, new UpdateValueStrategy().setConverter(new BooleanNotNullConverter()));
		
		parameterInputOL = EMFEditProperties.list(getEditingDomain(), Literals.GESCHAETZT_STATISTIK_MODELL__PARAMETER).observe(statisticModel);
		ObservableListContentProvider parameterContentProvider = new ObservableListContentProvider();
		parameterViewer.setContentProvider(parameterContentProvider);
		IObservableMap[] parameterLabelMap = new IObservableMap[1];
		IObservableSet parameterSet = parameterContentProvider.getKnownElements();
		parameterLabelMap[0] = EMFEditProperties.value(getEditingDomain(), Literals.GESCH_MODELL_PARAMETER__NAME).observeDetail(parameterSet);
		parameterViewer.setLabelProvider(new ObservableMapLabelProvider(parameterLabelMap));
		parameterViewer.setInput(parameterInputOL);
		// get observable value for the model from the current statistic model selection 
		modelLevelOV = EMFEditProperties.value(getEditingDomain(), BfrPackage.Literals.STATISTIK_MODELL__LEVEL).observeDetail(modelSelectionOV);
		// get observable value from the current parameter list size
		IObservableValue listSizeOV = new ObservableListSizeObservableValue(parameterInputOL);
		// use the same condition like in the statistic model before
		final IObservableList parameterOL = EMFEditProperties.list(getEditingDomain(), FeaturePath.fromList(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL, BfrPackage.Literals.STATISTIK_MODELL__PARAMETER)).observe(statisticModel);
		final Binding bindingParameter = dbc.bindValue(listSizeOV, new WritableValue(), new UpdateValueStrategy().setAfterGetValidator(new IValidator() {

			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
			 */
			@Override
			public IStatus validate(Object value) {
				if (getBooleanValue(manualModelOV)) {
					return ValidationStatus.ok();
				}
				if (value != null && ((Integer)value) > 0) {
					// all parameters from the statistic model have to be set
					if (getEstParameterSize(ParameterRoleType.PARAMETER) != getParameterSize(ParameterRoleType.PARAMETER)) {
						return ValidationStatus.error(Messages.EstimatedStatisticModelEditor_47);
					} 
					return ValidationStatus.ok();
				} else {
					return ValidationStatus.error(Messages.EstimatedStatisticModelEditor_48);
				}
			}
			
			private int getEstParameterSize(ParameterRoleType role) {
				int i = 0;
				for (Object o : parameterInputOL) {
					GeschModellParameter p = (GeschModellParameter) o;
					if (p.getModelParameter().getRole().equals(role)) {
						i++;
					}
				}
				return i;
			}
			private int getParameterSize(ParameterRoleType role) {
				int i = 0;
				for (Object o : parameterOL) {
					StatistikModellParameter p = (StatistikModellParameter) o;
					if (p.getRole().equals(role)) {
						i++;
					}
				}
				return i;
			}
		}), DIMDatabindingHelper.createNeverUpdateValueStrategy());
		DIMControlDecorationSupport.create(bindingParameter, SWT.TOP | SWT.LEFT, parameterViewer.getTable());
		modelLevelChangeListener = new IChangeListener() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.observable.IChangeListener#handleChange(org.eclipse.core.databinding.observable.ChangeEvent)
			 */
			@Override
			public void handleChange(ChangeEvent event) {
				bindingParameter.validateTargetToModel();
			}
		};
		modelLevelOV.addChangeListener(modelLevelChangeListener);
		
		dbc.bindValue(SWTObservables.observeEnabled(deleteLiteratureButton), ViewersObservables.observeSingleSelection(literatureViewer), null, new UpdateValueStrategy().setConverter(new BooleanNotNullConverter()));
		
		literatureInputOL = EMFEditProperties.list(getEditingDomain(), Literals.GESCHAETZT_STATISTIK_MODELL__LITERATUR).observe(statisticModel);
		ObservableListContentProvider literatureContentProvider = new ObservableListContentProvider();
		literatureViewer.setContentProvider(literatureContentProvider);
		IObservableSet literatureSet = literatureContentProvider.getKnownElements();
		IObservableMap[] literatureLabelMap = new IObservableMap[2];
		literatureLabelMap[0] = EMFEditProperties.value(getEditingDomain(), Literals.LITERATUR__TITEL).observeDetail(literatureSet);
		literatureLabelMap[1] = EMFEditProperties.value(getEditingDomain(), Literals.LITERATUR__ERSTAUTOR).observeDetail(literatureSet);
		literatureViewer.setLabelProvider(new ObservableMapLabelProvider(literatureLabelMap));
		literatureViewer.setInput(literatureInputOL);
		final Binding bindingLiterature = dbc.bindValue(listSizeOV, new WritableValue(), new UpdateValueStrategy().setAfterGetValidator(new IValidator() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
			 */
			@Override
			public IStatus validate(Object value) {
				if (getBooleanValue(manualModelOV)) {
					return ValidationStatus.ok();
				}
				Boolean manual = (Boolean) manualModelOV.getValue();
				if (manual == null) {
					manual = Boolean.TRUE;
				}
				if (value != null && ((Integer)value) > 0 && literatureInputOL.size() == 0 && manual) {
					return ValidationStatus.error(Messages.EstimatedStatisticModelEditor_49);
				}
				return ValidationStatus.ok();
			}
		}), DIMDatabindingHelper.createNeverUpdateValueStrategy());
		DIMControlDecorationSupport.create(bindingLiterature, SWT.TOP | SWT.LEFT, literatureViewer.getTable());
		literatureChangeListener = new IChangeListener() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.observable.IChangeListener#handleChange(org.eclipse.core.databinding.observable.ChangeEvent)
			 */
			@Override
			public void handleChange(ChangeEvent event) {
				bindingLiterature.updateTargetToModel();
			}
		};
		literatureInputOL.addChangeListener(literatureChangeListener);
		
		dbc.bindValue(WidgetProperties.enabled().observe(addCovCorButton), modelSelectionOV, DIMDatabindingHelper.createNeverUpdateValueStrategy(), new UpdateValueStrategy().setConverter(null2BooleanConverter));
		dbc.bindValue(SWTObservables.observeEnabled(deleteCovCorButton), ViewersObservables.observeSingleSelection(covCorParameterViewer), null, new UpdateValueStrategy().setConverter(new BooleanNotNullConverter()));
		dbc.bindValue(SWTObservables.observeEnabled(editCovCorButton), ViewersObservables.observeSingleSelection(covCorParameterViewer), null, new UpdateValueStrategy().setConverter(new BooleanNotNullConverter()));
		
		IObservableList covCorInputOL = EMFEditProperties.list(getEditingDomain(), Literals.GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR).observe(statisticModel);
		ObservableListContentProvider covCorContentProvider = new ObservableListContentProvider();
		covCorParameterViewer.setContentProvider(covCorContentProvider);
		IObservableMap[] covCorLabelMap = new IObservableMap[4];
		IObservableSet covCorSet = covCorContentProvider.getKnownElements();
		covCorLabelMap[0] = EMFEditProperties.value(getEditingDomain(), FeaturePath.fromList(BfrPackage.Literals.PARAMETER_COV_COR__PARAMETER1, BfrPackage.Literals.GESCH_MODELL_PARAMETER__NAME)).observeDetail(covCorSet);
		covCorLabelMap[1] = EMFEditProperties.value(getEditingDomain(), FeaturePath.fromList(BfrPackage.Literals.PARAMETER_COV_COR__PARAMETER2, BfrPackage.Literals.GESCH_MODELL_PARAMETER__NAME)).observeDetail(covCorSet);
		covCorLabelMap[2] = EMFEditProperties.value(getEditingDomain(), BfrPackage.Literals.PARAMETER_COV_COR__VALUE).observeDetail(covCorSet);
		covCorLabelMap[3] = EMFEditProperties.value(getEditingDomain(), BfrPackage.Literals.PARAMETER_COV_COR__COR).observeDetail(covCorSet);
		covCorParameterViewer.setLabelProvider(new ObservableMapLabelProvider(covCorLabelMap));
		covCorParameterViewer.setInput(covCorInputOL);
	}
	
	private boolean getBooleanValue(IObservableValue activeOV) {
		Object active = activeOV.getValue();
		if (active != null && active instanceof Boolean) {
			// if active is false, no further validation is neccessary
			return Boolean.FALSE.equals(active);
		}
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.editors.AbstractEMFEditor#dispose()
	 */
	@Override
	public void dispose() {
		literatureInputOL.removeChangeListener(literatureChangeListener);
		modelLevelOV.removeChangeListener(modelLevelChangeListener);
		primaryModelOV.removeChangeListener(primaryModelChangeListener);
		super.dispose();
	}

}
