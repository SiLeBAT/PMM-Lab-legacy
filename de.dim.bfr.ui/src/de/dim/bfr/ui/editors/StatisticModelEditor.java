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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/editors/StatisticModelEditor.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
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
import de.dim.bfr.KlasseTyp;
import de.dim.bfr.LevelTyp;
import de.dim.bfr.ParameterRoleType;
import de.dim.bfr.SoftwareType;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.provider.BfrItemProviderAdapterFactory;
import de.dim.bfr.ui.databinding.DIMControlDecorationSupport;
import de.dim.bfr.ui.databinding.DIMDatabindingHelper;
import de.dim.bfr.ui.databinding.MessageManagerSupport;
import de.dim.bfr.ui.databinding.ObservableListSizeObservableValue;
import de.dim.bfr.ui.databinding.converter.BooleanNotNullConverter;
import de.dim.bfr.ui.dialogs.LiteratureSelectionDialog;
import de.dim.bfr.ui.dialogs.ParameterEditDialog;
import de.dim.bfr.ui.internal.BFRUIActivator;
import de.dim.bfr.ui.message.Messages;
import de.dim.bfr.ui.services.BFRUIService;

/**
 * Editor to handle statistic model editing
 * @author Mark Hoffmann
 * @since 13.11.2011
 */
public class StatisticModelEditor extends AbstractEMFEditor {

	public static final String ID = "de.dim.bfr.ui.editors.StatisticModelEditor"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private StatistikModell statisticModel;
	private BFRUIService service;

	private Text textName;
	private Text textDescription;
	private Text textAuthor;
	private DateTime dateTime;
	private Text textNotation;
	private Text textType;
	private Text textFormula;
	private Text textComment;
	private ComboViewer levelViewer;
	private ComboViewer classViewer;
	private ComboViewer softwareViewer;
	private TableViewer literatureViewer;
	private TableViewer parameterViewer;
	private Button deleteModelButton;
	private Button deleteLiteratureButton;
	private Button editModelButton;
	private Binding parameterBinding;
	private IObservableList literatureInputOL;
	private IObservableList parameterInputOL;
	private IObservableValue modelLevelOV;
	private IChangeListener modelLevelChangeListener;

	public StatisticModelEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent the parent composite
	 */
	@Override
	public void createPartControl(Composite parent) {
		ScrolledForm scrolledForm = formToolkit.createScrolledForm(parent);
		Form form = scrolledForm.getForm();
		form.setText(Messages.StatisticModelEditor_0);
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
		// create the formula section
		createFormulaSection(scrolledForm);
		// create the parameter section 
		createParameterSection(scrolledForm);
		
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
			MessageDialog.openError(getSite().getShell(), Messages.StatisticModelEditor_3, Messages.StatisticModelEditor_4);
			return false;
		}
		return service.saveStatisticModel(statisticModel);
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
		statisticModel = (StatistikModell) getModel();
		service = BFRUIActivator.getBFRService();
		statisticModel.setEingabedatum(new Date());
	}

	/**
	 * Creates the general section
	 * @param scrolledForm the scrolled form
	 */
	private void createGeneralSection(ScrolledForm scrolledForm) {
		Section generalSection = formToolkit.createSection(scrolledForm.getBody(), Section.TITLE_BAR);
		generalSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		generalSection.setText(Messages.StatisticModelEditor_5);
		formToolkit.paintBordersFor(generalSection);
		
		Composite generalComposite = formToolkit.createComposite(generalSection, SWT.NONE);
		generalSection.setClient(generalComposite);
		GridLayout gl_generalComposite = new GridLayout(3, false);
		gl_generalComposite.horizontalSpacing = 7;
		generalComposite.setLayout(gl_generalComposite);
		formToolkit.paintBordersFor(generalComposite);
		
		Label labelName = formToolkit.createLabel(generalComposite, Messages.StatisticModelEditor_6, SWT.NONE);
		GridData gd_labelName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelName.widthHint = 100;
		labelName.setLayoutData(gd_labelName);
		
		textName = formToolkit.createText(generalComposite, "", SWT.NONE); //$NON-NLS-1$
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label labelDescription = formToolkit.createLabel(generalComposite, Messages.StatisticModelEditor_8, SWT.NONE);
		GridData gd_labelDescription = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelDescription.widthHint = 100;
		labelDescription.setLayoutData(gd_labelDescription);
		
		textDescription = formToolkit.createText(generalComposite, "", SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI); //$NON-NLS-1$
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		gd_textDescription.heightHint = 75;
		textDescription.setLayoutData(gd_textDescription);
		
		Label labelAuthor = formToolkit.createLabel(generalComposite, Messages.StatisticModelEditor_10, SWT.NONE);
		GridData gd_labelAuthor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelAuthor.widthHint = 100;
		labelAuthor.setLayoutData(gd_labelAuthor);
		
		textAuthor = formToolkit.createText(generalComposite, "", SWT.NONE); //$NON-NLS-1$
		textAuthor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label labelDate = formToolkit.createLabel(generalComposite, Messages.StatisticModelEditor_12, SWT.NONE);
		GridData gd_labelDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelDate.widthHint = 100;
		labelDate.setLayoutData(gd_labelDate);
		
		dateTime = new DateTime(generalComposite, SWT.DATE);
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		dateTime.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.paintBordersFor(dateTime);
		
		Label labelLiterature = formToolkit.createLabel(generalComposite, Messages.StatisticModelEditor_13, SWT.NONE);
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
		titleCol.setText(Messages.StatisticModelEditor_14);
		
		TableViewerColumn authorVC = new TableViewerColumn(literatureViewer, SWT.NONE);
		TableColumn authorCol = authorVC.getColumn();
		authorCol.setWidth(250);
		authorCol.setText(Messages.StatisticModelEditor_15);
		
		Button selectButton = formToolkit.createButton(generalComposite, Messages.StatisticModelEditor_16, SWT.PUSH);
		GridData gd_selectButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_selectButton.widthHint = 100;
		selectButton.setLayoutData(gd_selectButton);
		selectButton.addSelectionListener(new SelectionAdapter() {
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
		deleteLiteratureButton = formToolkit.createButton(generalComposite, Messages.StatisticModelEditor_17, SWT.PUSH);
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
				Command removeCommand = RemoveCommand.create(getEditingDomain(), statisticModel, BfrPackage.Literals.STATISTIK_MODELL__LITERATUR, objectList);
				getEditingDomain().getCommandStack().execute(removeCommand);
			}
		});
	}

	/**
	 * Creates the formula section
	 * @param scrolledForm the scrolled form
	 */
	private void createFormulaSection(ScrolledForm scrolledForm) {
		Section formulaSection = formToolkit.createSection(scrolledForm.getBody(), Section.TITLE_BAR);
		GridData gd_formulaSection = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_formulaSection.heightHint = 100;
		formulaSection.setLayoutData(gd_formulaSection);
		formulaSection.setText(Messages.StatisticModelEditor_18);
		formToolkit.paintBordersFor(formulaSection);
		
		Composite formulaComposite = formToolkit.createComposite(formulaSection, SWT.NONE);
		formulaSection.setClient(formulaComposite);
		formToolkit.paintBordersFor(formulaComposite);
		GridLayout gl_formulaComposite = new GridLayout(2, false);
		gl_formulaComposite.horizontalSpacing = 7;
		formulaComposite.setLayout(gl_formulaComposite);
		
		Label labelNotation = formToolkit.createLabel(formulaComposite, Messages.StatisticModelEditor_19, SWT.NONE);
		GridData gd_labelNotation = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelNotation.widthHint = 100;
		labelNotation.setLayoutData(gd_labelNotation);
		
		textNotation = formToolkit.createText(formulaComposite, "", SWT.NONE); //$NON-NLS-1$
		textNotation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelType = formToolkit.createLabel(formulaComposite, Messages.StatisticModelEditor_21, SWT.NONE);
		GridData gd_labelType = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelType.widthHint = 100;
		labelType.setLayoutData(gd_labelType);
		
		textType = formToolkit.createText(formulaComposite, "", SWT.NONE); //$NON-NLS-1$
		textType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelLevel = formToolkit.createLabel(formulaComposite, Messages.StatisticModelEditor_23, SWT.NONE);
		GridData gd_labelLevel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelLevel.widthHint = 100;
		labelLevel.setLayoutData(gd_labelLevel);
		
		CCombo comboLevel = new CCombo(formulaComposite, SWT.READ_ONLY);
		comboLevel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboLevel.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(comboLevel);
		formToolkit.paintBordersFor(comboLevel);
		levelViewer = new ComboViewer(comboLevel);
		levelViewer.setContentProvider(new ArrayContentProvider());
		levelViewer.setLabelProvider(new LabelProvider());
		levelViewer.setInput(LevelTyp.VALUES);
		
		Label labelClass = formToolkit.createLabel(formulaComposite, Messages.StatisticModelEditor_24, SWT.NONE);
		GridData gd_labelClass = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelClass.widthHint = 100;
		labelClass.setLayoutData(gd_labelClass);
		
		CCombo comboClass = new CCombo(formulaComposite, SWT.READ_ONLY);
		comboClass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboClass.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(comboClass);
		formToolkit.paintBordersFor(comboClass);
		classViewer = new ComboViewer(comboClass);
		classViewer.setContentProvider(new ArrayContentProvider());
		classViewer.setLabelProvider(new LabelProvider());
		classViewer.setInput(KlasseTyp.VALUES);
		
		Label labelSoftware = formToolkit.createLabel(formulaComposite, Messages.StatisticModelEditor_25, SWT.NONE);
		GridData gd_labelSoftware = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelSoftware.widthHint = 100;
		labelSoftware.setLayoutData(gd_labelSoftware);
		
		CCombo comboSoftware = new CCombo(formulaComposite, SWT.READ_ONLY);
		comboSoftware.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboSoftware.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(comboSoftware);
		formToolkit.paintBordersFor(comboSoftware);
		softwareViewer = new ComboViewer(comboSoftware);
		softwareViewer.setContentProvider(new ArrayContentProvider());
		softwareViewer.setLabelProvider(new LabelProvider());
		softwareViewer.setInput(SoftwareType.VALUES);
		
		Label labelFormula = formToolkit.createLabel(formulaComposite, Messages.StatisticModelEditor_26, SWT.NONE);
		GridData gd_labelFormula = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelFormula.widthHint = 100;
		labelFormula.setLayoutData(gd_labelFormula);
		
		textFormula = formToolkit.createText(formulaComposite, "", SWT.WRAP | SWT.V_SCROLL | SWT.MULTI); //$NON-NLS-1$
		GridData gd_textFormula = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textFormula.heightHint = 50;
		textFormula.setLayoutData(gd_textFormula);
		
		Label labelComment = formToolkit.createLabel(formulaComposite, Messages.StatisticModelEditor_28, SWT.NONE);
		GridData gd_labelComment = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelComment.widthHint = 100;
		labelComment.setLayoutData(gd_labelComment);
		
		textComment = formToolkit.createText(formulaComposite, "", SWT.WRAP | SWT.V_SCROLL | SWT.MULTI); //$NON-NLS-1$
		GridData gd_textComment = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textComment.heightHint = 50;
		textComment.setLayoutData(gd_textComment);
	}

	/**
	 * Creates the parameter section
	 * @param scrolledForm the scrolle form
	 */
	private void createParameterSection(ScrolledForm scrolledForm) {
		Section parameterSection = formToolkit.createSection(scrolledForm.getBody(), Section.TITLE_BAR);
		parameterSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		parameterSection.setText(Messages.StatisticModelEditor_30);
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
		
		Table parameterTable = formToolkit.createTable(parameterComposite, SWT.FULL_SELECTION | SWT.MULTI);
		parameterTable.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(parameterTable);
		formToolkit.paintBordersFor(parameterTable);
		GridData gd_tableParameter = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 4);
		gd_tableParameter.heightHint = 75;
		parameterTable.setLayoutData(gd_tableParameter);
		parameterViewer = new TableViewer(parameterTable);
		TableViewerColumn nameVC = new TableViewerColumn(parameterViewer, SWT.NONE);
		nameVC.getColumn().setMoveable(false);
		nameVC.getColumn().setResizable(false);
		nameVC.getColumn().setWidth(200);
		nameVC.getColumn().setText(Messages.StatisticModelEditor_32);
		TableViewerColumn roleVC = new TableViewerColumn(parameterViewer, SWT.NONE);
		roleVC.getColumn().setMoveable(false);
		roleVC.getColumn().setResizable(false);
		roleVC.getColumn().setWidth(200);
		roleVC.getColumn().setText(Messages.StatisticModelEditor_33);
		
		Button addButton = formToolkit.createButton(parameterComposite, Messages.StatisticModelEditor_34, SWT.PUSH);
		GridData gd_addButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_addButton.widthHint = 100;
		addButton.setLayoutData(gd_addButton);
		addButton.addSelectionListener(new SelectionAdapter() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				ParameterEditDialog dialog = new ParameterEditDialog(getSite().getShell(), statisticModel, getEditingDomain());
				dialog.open();
			}
		});
		editModelButton = formToolkit.createButton(parameterComposite, Messages.StatisticModelEditor_35, SWT.PUSH);
		GridData gd_editButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_editButton.widthHint = 100;
		editModelButton.setLayoutData(gd_editButton);
		editModelButton.addSelectionListener(new SelectionAdapter() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object selection = ((IStructuredSelection)parameterViewer.getSelection()).getFirstElement();
				StatistikModellParameter parameter = (StatistikModellParameter) selection;
				ParameterEditDialog dialog = new ParameterEditDialog(getSite().getShell(), statisticModel, parameter, getEditingDomain());
				dialog.open();
				parameterBinding.validateTargetToModel();
			}
		});
		deleteModelButton = formToolkit.createButton(parameterComposite, Messages.StatisticModelEditor_36, SWT.PUSH);
		GridData gd_deleteButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_deleteButton.widthHint = 100;
		deleteModelButton.setLayoutData(gd_deleteButton);
		deleteModelButton.addSelectionListener(new SelectionAdapter() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object[] objects = ((IStructuredSelection)parameterViewer.getSelection()).toArray();
				List<Object> objectList = Arrays.asList(objects);
				if (MessageDialog.openQuestion(getSite().getShell(), Messages.StatisticModelEditor_37, Messages.StatisticModelEditor_38)) {
					Command removeCommand = RemoveCommand.create(getEditingDomain(), statisticModel, BfrPackage.Literals.STATISTIK_MODELL__PARAMETER, objectList);
					getEditingDomain().getCommandStack().execute(removeCommand);
				}
			}
		});
	}

	/**
	 * Initializes the data binding
	 */
	private void initDataBindings() {
		//
		IObservableValue textNameObserveTextObserveWidget = SWTObservables.observeText(textName, SWT.Modify);
		IObservableValue statisticModelNameObserveValue = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.STATISTIK_MODELL__NAME);
		dbc.bindValue(textNameObserveTextObserveWidget, statisticModelNameObserveValue, DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.StatisticModelEditor_39), DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.StatisticModelEditor_40));
		bindTitleName(statisticModelNameObserveValue, new UpdateValueStrategy().setConverter(new IConverter() {
			
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
				return String.class;
			}
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
			 */
			@Override
			public Object convert(Object fromObject) {
				if (fromObject == null || fromObject.toString().length() == 0 || fromObject.toString().length() == 1) {
					return Messages.StatisticModelEditor_41;
				}
				return fromObject;
			}
		}));
		//
		IObservableValue textDescriptionObserveTextObserveWidget = SWTObservables.observeText(textDescription, SWT.Modify);
		IObservableValue modelBeschreibungOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.STATISTIK_MODELL__BESCHREIBUNG);
		dbc.bindValue(textDescriptionObserveTextObserveWidget, modelBeschreibungOV, DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.StatisticModelEditor_42), DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.StatisticModelEditor_43));
		//
		IObservableValue textAuthorObserveTextObserveWidget = SWTObservables.observeText(textAuthor, SWT.Modify);
		IObservableValue modelBenutzerOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.STATISTIK_MODELL__BENUTZER);
		dbc.bindValue(textAuthorObserveTextObserveWidget, modelBenutzerOV, null, null);
		//
		IObservableValue dateTimeObserveSelectionObserveWidget = SWTObservables.observeSelection(dateTime);
		IObservableValue modelEingabedatumOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.STATISTIK_MODELL__EINGABEDATUM);
		dbc.bindValue(dateTimeObserveSelectionObserveWidget, modelEingabedatumOV, null, null);
		//
		IObservableValue textNotationObserveTextObserveWidget = SWTObservables.observeText(textNotation, SWT.Modify);
		IObservableValue modelNotationOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.STATISTIK_MODELL__NOTATION);
		dbc.bindValue(textNotationObserveTextObserveWidget, modelNotationOV, DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.StatisticModelEditor_44), DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.StatisticModelEditor_45));
		//
		IObservableValue textTypeObserveTextObserveWidget = SWTObservables.observeText(textType, SWT.Modify);
		IObservableValue modelTypOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.STATISTIK_MODELL__TYP);
		dbc.bindValue(textTypeObserveTextObserveWidget, modelTypOV, null, null);
		//
		IObservableValue comboLevelSelectionOV = ViewersObservables.observeSingleSelection(levelViewer);
		ViewerFilter classFilter = new ClassFilter(comboLevelSelectionOV);
		classViewer.addFilter(classFilter);
		
		modelLevelOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.STATISTIK_MODELL__LEVEL);
		dbc.bindValue(comboLevelSelectionOV, modelLevelOV, DIMDatabindingHelper.createLevelUpdateStrategy(), DIMDatabindingHelper.createLevelUpdateStrategy());
		//
		IObservableValue comboClassObserveSelectionObserveWidget = ViewersObservables.observeSingleSelection(classViewer);
		IObservableValue modelKlasseOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.STATISTIK_MODELL__KLASSE);
		dbc.bindValue(comboClassObserveSelectionObserveWidget, modelKlasseOV, null, null);
		//
		IObservableValue textFormulaObserveTextObserveWidget = SWTObservables.observeText(textFormula, SWT.Modify);
		IObservableValue modelFormelOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.STATISTIK_MODELL__FORMEL);
		dbc.bindValue(textFormulaObserveTextObserveWidget, modelFormelOV, null, null);
		//
		IObservableValue comboSoftwareObserveSelectionObserveWidget = ViewersObservables.observeSingleSelection(softwareViewer);
		IObservableValue modelSoftwareOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.STATISTIK_MODELL__SOFTWARE);
		dbc.bindValue(comboSoftwareObserveSelectionObserveWidget, modelSoftwareOV, null, null);
		//
		IObservableValue textCommentObserveTextObserveWidget = SWTObservables.observeText(textComment, SWT.Modify);
		IObservableValue modelKommentarOV = EMFEditObservables.observeValue(getEditingDomain(), statisticModel, Literals.STATISTIK_MODELL__KOMMENTAR);
		dbc.bindValue(textCommentObserveTextObserveWidget, modelKommentarOV, null, null);
		
		dbc.bindValue(SWTObservables.observeEnabled(deleteLiteratureButton), ViewersObservables.observeSingleSelection(literatureViewer), null, new UpdateValueStrategy().setConverter(new BooleanNotNullConverter()));
		dbc.bindValue(SWTObservables.observeEnabled(deleteModelButton), ViewersObservables.observeSingleSelection(parameterViewer), null, new UpdateValueStrategy().setConverter(new BooleanNotNullConverter()));
		dbc.bindValue(SWTObservables.observeEnabled(editModelButton), ViewersObservables.observeSingleSelection(parameterViewer), null, new UpdateValueStrategy().setConverter(new BooleanNotNullConverter()));
		
		literatureInputOL = EMFEditProperties.list(getEditingDomain(), Literals.STATISTIK_MODELL__LITERATUR).observe(statisticModel);
		ObservableListContentProvider literatureContentProvider = new ObservableListContentProvider();
		literatureViewer.setContentProvider(literatureContentProvider);
		IObservableSet literatureSet = literatureContentProvider.getKnownElements();
		IObservableMap[] literatureLabelMap = new IObservableMap[2];
		literatureLabelMap[0] = EMFEditProperties.value(getEditingDomain(), Literals.LITERATUR__TITEL).observeDetail(literatureSet);
		literatureLabelMap[1] = EMFEditProperties.value(getEditingDomain(), Literals.LITERATUR__ERSTAUTOR).observeDetail(literatureSet);
		literatureViewer.setLabelProvider(new ObservableMapLabelProvider(literatureLabelMap));
		literatureViewer.setInput(literatureInputOL);
		
		parameterInputOL = EMFEditProperties.list(getEditingDomain(), Literals.STATISTIK_MODELL__PARAMETER).observe(statisticModel);
		ObservableListContentProvider parameterContentProvider = new ObservableListContentProvider();
		parameterViewer.setContentProvider(parameterContentProvider);
		IObservableMap[] parameterLabelMap = new IObservableMap[4];
		IObservableSet parameterSet = parameterContentProvider.getKnownElements();
		parameterLabelMap[0] = EMFEditProperties.value(getEditingDomain(), Literals.STATISTIK_MODELL_PARAMETER__NAME).observeDetail(parameterSet);
		parameterLabelMap[1] = EMFEditProperties.value(getEditingDomain(), Literals.STATISTIK_MODELL_PARAMETER__ROLE).observeDetail(parameterSet);
		parameterLabelMap[2] = EMFEditProperties.value(getEditingDomain(), Literals.STATISTIK_MODELL_PARAMETER__MIN).observeDetail(parameterSet);
		parameterLabelMap[3] = EMFEditProperties.value(getEditingDomain(), Literals.STATISTIK_MODELL_PARAMETER__MAX).observeDetail(parameterSet);
		parameterViewer.setLabelProvider(new ObservableMapLabelProvider(parameterLabelMap));
		parameterViewer.setInput(parameterInputOL);
		IObservableValue listSizeOV = new ObservableListSizeObservableValue(parameterInputOL);
		parameterBinding = dbc.bindValue(listSizeOV, new WritableValue(), new UpdateValueStrategy().setAfterGetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				if (value != null && ((Integer)value) > 0) {
					// one dependent variable is neccessary
					if (getParameterSize(ParameterRoleType.DEPENDENT) != 1) {
						return ValidationStatus.error(Messages.StatisticModelEditor_46);
					}
					// at exact one independent variable for primary models
					if (LevelTyp.PRIMARY.equals(modelLevelOV.getValue())) {
						if (getParameterSize(ParameterRoleType.INDEPENDENT) != 1) {
							return ValidationStatus.error(Messages.StatisticModelEditor_47);
						} 
					// at least one independent variable for secondary models
					} else {
						if (getParameterSize(ParameterRoleType.INDEPENDENT) < 1) {
							return ValidationStatus.error(Messages.StatisticModelEditor_48);
						} 
					}
					// at least one parameter has to be set
					if (getParameterSize(ParameterRoleType.PARAMETER) < 1) {
						return ValidationStatus.error(Messages.StatisticModelEditor_49);
					} 
					return ValidationStatus.ok();
				} else {
					return ValidationStatus.error(Messages.StatisticModelEditor_50);
				}
			}
			
			private int getParameterSize(ParameterRoleType role) {
				int i = 0;
				for (Object o : parameterInputOL) {
					StatistikModellParameter p = (StatistikModellParameter) o;
					if (p.getRole().equals(role)) {
						i++;
					}
				}
				return i;
			}
		}), DIMDatabindingHelper.createNeverUpdateValueStrategy());
		DIMControlDecorationSupport.create(parameterBinding, SWT.TOP | SWT.LEFT, parameterViewer.getTable());
		modelLevelChangeListener = new IChangeListener() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.observable.IChangeListener#handleChange(org.eclipse.core.databinding.observable.ChangeEvent)
			 */
			@Override
			public void handleChange(ChangeEvent event) {
				parameterBinding.validateTargetToModel();
			}
		};
		modelLevelOV.addChangeListener(modelLevelChangeListener);
		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.editors.AbstractEMFEditor#dispose()
	 */
	@Override
	public void dispose() {
		modelLevelOV.removeChangeListener(modelLevelChangeListener);
		super.dispose();
	}
}
