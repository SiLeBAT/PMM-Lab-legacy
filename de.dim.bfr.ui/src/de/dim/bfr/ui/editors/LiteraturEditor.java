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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/editors/LiteraturEditor.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
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

import de.dim.bfr.BfrPackage.Literals;
import de.dim.bfr.FreigabeTyp;
import de.dim.bfr.Literatur;
import de.dim.bfr.LiteraturTyp;
import de.dim.bfr.provider.BfrItemProviderAdapterFactory;
import de.dim.bfr.ui.databinding.DIMDatabindingHelper;
import de.dim.bfr.ui.databinding.MessageManagerSupport;
import de.dim.bfr.ui.databinding.converter.String2IntegerConverter;
import de.dim.bfr.ui.databinding.converter.ToStringConverter;
import de.dim.bfr.ui.internal.BFRUIActivator;
import de.dim.bfr.ui.message.Messages;
import de.dim.bfr.ui.services.BFRUIService;

/**
 * Editor to handle literature model editing
 * @author Mark Hoffmann
 * @since 13.11.2011
 */
public class LiteraturEditor extends AbstractEMFEditor {

	public static final String ID = "de.dim.bfr.ui.editors.LiteraturEditor"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private BFRUIService service;
	private Text textAuthor;
	private Text textTitel;
	private Text abstractText;
	private Spinner spinnerJahr;
	
	private Literatur literature;
	private Text textJournal;
	private Text textVolume;
	private Text textIssue;
	private Text textPaper;
	private Text textPage;
	private Text textWebsite;
	private Text textComment;
	private CCombo comboLiteratureType;
	private CCombo comboPermissionType;
	private ComboViewer permissionViewer;
	private ComboViewer literatureViewer;

	public LiteraturEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent the parent composite
	 */
	@Override
	public void createPartControl(Composite parent) {
		ScrolledForm scrolledForm = formToolkit.createScrolledForm(parent);
		Form form = scrolledForm.getForm();
		form.setText(Messages.LiteraturEditor_0);
		formToolkit.decorateFormHeading(form);
		scrolledForm.getBody().setLayout(new GridLayout(1, false));
		ToolBarManager toolbarManager = (ToolBarManager) form.getToolBarManager();
		toolbarManager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		IMenuService menuService = (IMenuService) getSite().getService(IMenuService.class);
		menuService.populateContributionManager(toolbarManager, "toolbar:" + ID + ".toolbar"); //$NON-NLS-1$ //$NON-NLS-2$
		toolbarManager.update(true);
		
		// configure message manager
		IMessageManager manager = form.getMessageManager();
		manager.setDecorationPosition(SWT.TOP | SWT.LEFT);
		MessageManagerSupport.create(new ManagedForm(formToolkit, scrolledForm), dbc);
		
		createGeneralsection(scrolledForm);
		createInfoSection(scrolledForm);
		createMiscSection(scrolledForm);
		
		initDataBindings();
		
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
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
	
	/**
	 * Creates the section with general information
	 * @param scrolledForm the scrolled form
	 */
	private void createGeneralsection(ScrolledForm scrolledForm) {
		Section generalSection = formToolkit.createSection(scrolledForm.getBody(), Section.TITLE_BAR);
		generalSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		generalSection.setText(Messages.LiteraturEditor_3);
		formToolkit.paintBordersFor(generalSection);
		
		Composite generalComposite = formToolkit.createComposite(generalSection, SWT.NONE);
		generalSection.setClient(generalComposite);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 7;
		generalComposite.setLayout(layout);
		formToolkit.paintBordersFor(generalComposite);
		
		Label labelAutor = formToolkit.createLabel(generalComposite, Messages.LiteraturEditor_4, SWT.NONE);
		GridData gd_labelAutor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelAutor.widthHint = 100;
		labelAutor.setLayoutData(gd_labelAutor);
		
		textAuthor = formToolkit.createText(generalComposite, "", SWT.NONE); //$NON-NLS-1$
		textAuthor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelTitel = formToolkit.createLabel(generalComposite, Messages.LiteraturEditor_6, SWT.NONE);
		GridData gd_labelTitel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelTitel.widthHint = 100;
		labelTitel.setLayoutData(gd_labelTitel);
		
		textTitel = formToolkit.createText(generalComposite, "", SWT.NONE); //$NON-NLS-1$
		textTitel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelJahr = formToolkit.createLabel(generalComposite, Messages.LiteraturEditor_8, SWT.NONE);
		GridData gd_labelJahr = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelJahr.widthHint = 100;
		labelJahr.setLayoutData(gd_labelJahr);
		
		spinnerJahr = new Spinner(generalComposite, SWT.NONE);
		GridData gd_spinnerJahr = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_spinnerJahr.widthHint = 100;
		spinnerJahr.setLayoutData(gd_spinnerJahr);
		spinnerJahr.setMinimum(0);
		spinnerJahr.setMaximum(3000);
		spinnerJahr.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.paintBordersFor(spinnerJahr);
		
		Label labelAbstract = formToolkit.createLabel(generalComposite, Messages.LiteraturEditor_9, SWT.NONE);
		GridData gd_labelAbstract = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelAbstract.widthHint = 100;
		labelAbstract.setLayoutData(gd_labelAbstract);
		formToolkit.adapt(labelAbstract, true, true);
		
		abstractText = formToolkit.createText(generalComposite, "", SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI); //$NON-NLS-1$
		GridData gd_abstractText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_abstractText.heightHint = 75;
		abstractText.setLayoutData(gd_abstractText);
		abstractText.setText(""); //$NON-NLS-1$
	}

	/**
	 * Creates the info section
	 * @param scrolledForm the scrolled form
	 */
	private void createInfoSection(ScrolledForm scrolledForm) {
		Section infoSection = formToolkit.createSection(scrolledForm.getBody(), Section.TITLE_BAR);
		GridData gd_infoSection = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		infoSection.setLayoutData(gd_infoSection);
		infoSection.setText(Messages.LiteraturEditor_12);
		formToolkit.paintBordersFor(infoSection);
		
		Composite infoComposite = formToolkit.createComposite(infoSection, SWT.NONE);
		infoSection.setClient(infoComposite);
		formToolkit.paintBordersFor(infoComposite);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 7;
		infoComposite.setLayout(layout);
		
		Label labelPage = formToolkit.createLabel(infoComposite, Messages.LiteraturEditor_13, SWT.NONE);
		GridData gd_labelPage = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelPage.widthHint = 100;
		labelPage.setLayoutData(gd_labelPage);
		
		textPage = formToolkit.createText(infoComposite, "", SWT.NONE); //$NON-NLS-1$
		GridData gd_textPage = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textPage.widthHint = 100;
		textPage.setLayoutData(gd_textPage);
		
		Label labelJournal = new Label(infoComposite, SWT.NONE);
		GridData gd_labelJournal = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelJournal.widthHint = 100;
		labelJournal.setLayoutData(gd_labelJournal);
		formToolkit.adapt(labelJournal, true, true);
		labelJournal.setText(Messages.LiteraturEditor_15);
		
		textJournal = formToolkit.createText(infoComposite, "", SWT.NONE); //$NON-NLS-1$
		textJournal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelVolume = formToolkit.createLabel(infoComposite, Messages.LiteraturEditor_17, SWT.NONE);
		GridData gd_labelVolume = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelVolume.widthHint = 100;
		labelVolume.setLayoutData(gd_labelVolume);
		
		textVolume = formToolkit.createText(infoComposite, "", SWT.NONE); //$NON-NLS-1$
		textVolume.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelIssue = formToolkit.createLabel(infoComposite, Messages.LiteraturEditor_19, SWT.NONE);
		GridData gd_labelIssue = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelIssue.widthHint = 100;
		labelIssue.setLayoutData(gd_labelIssue);
		
		textIssue = formToolkit.createText(infoComposite, "", SWT.NONE); //$NON-NLS-1$
		textIssue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelPaper = formToolkit.createLabel(infoComposite, Messages.LiteraturEditor_21, SWT.NONE);
		GridData gd_labelPaper = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelPaper.widthHint = 100;
		labelPaper.setLayoutData(gd_labelPaper);
		
		textPaper = formToolkit.createText(infoComposite, "", SWT.NONE); //$NON-NLS-1$
		textPaper.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelWebsite = formToolkit.createLabel(infoComposite, Messages.LiteraturEditor_23, SWT.NONE);
		GridData gd_labelWebsite = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelWebsite.widthHint = 100;
		labelWebsite.setLayoutData(gd_labelWebsite);
		
		textWebsite = formToolkit.createText(infoComposite, "", SWT.NONE); //$NON-NLS-1$
		textWebsite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

	/**
	 * Creates the miscellaneous section
	 * @param scrolledForm the scrolled form
	 */
	private void createMiscSection(ScrolledForm scrolledForm) {
		Section miscSection = formToolkit.createSection(scrolledForm.getBody(), Section.TITLE_BAR);
		GridData gd_miscSection = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_miscSection.heightHint = 100;
		miscSection.setLayoutData(gd_miscSection);
		miscSection.setText(Messages.LiteraturEditor_25);
		formToolkit.paintBordersFor(miscSection);
		
		Composite miscComposite = formToolkit.createComposite(miscSection, SWT.NONE);
		miscSection.setClient(miscComposite);
		formToolkit.paintBordersFor(miscComposite);
		GridLayout layout = new GridLayout(2, false);
		miscComposite.setLayout(layout);
		
		Label labelLiteratureType = formToolkit.createLabel(miscComposite, Messages.LiteraturEditor_26, SWT.NONE);
		GridData gd_labelLiteratureType = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelLiteratureType.widthHint = 100;
		labelLiteratureType.setLayoutData(gd_labelLiteratureType);
	
		comboLiteratureType = new CCombo(miscComposite, SWT.READ_ONLY);
		comboLiteratureType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboLiteratureType.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(comboLiteratureType);
		formToolkit.paintBordersFor(comboLiteratureType);
		literatureViewer = new ComboViewer(comboLiteratureType);
		literatureViewer.setContentProvider(new ArrayContentProvider());
		literatureViewer.setLabelProvider(new LabelProvider());
		literatureViewer.setInput(LiteraturTyp.VALUES);
		
		Label labelPermissionType = formToolkit.createLabel(miscComposite, Messages.LiteraturEditor_27, SWT.NONE);
		GridData gd_labelPermissionType = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelPermissionType.widthHint = 100;
		labelPermissionType.setLayoutData(gd_labelPermissionType);
		
		comboPermissionType = new CCombo(miscComposite, SWT.READ_ONLY);
		comboPermissionType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboPermissionType.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		formToolkit.adapt(comboPermissionType);
		formToolkit.paintBordersFor(comboPermissionType);
		permissionViewer = new ComboViewer(comboPermissionType);
		permissionViewer.setContentProvider(new ArrayContentProvider());
		permissionViewer.setLabelProvider(new LabelProvider());
		permissionViewer.setInput(FreigabeTyp.VALUES);
		
		Label labelComment = formToolkit.createLabel(miscComposite, Messages.LiteraturEditor_28, SWT.NONE);
		GridData gd_labelComment = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelComment.widthHint = 100;
		labelComment.setLayoutData(gd_labelComment);
		
		textComment = formToolkit.createText(miscComposite, "", SWT.WRAP | SWT.V_SCROLL | SWT.PASSWORD | SWT.MULTI); //$NON-NLS-1$
		GridData gd_textComment = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textComment.heightHint = 50;
		textComment.setLayoutData(gd_textComment);
	}

	/**
	 * Initializes the data binding
	 */
	private void initDataBindings() {
		// binds the author, this field is required to be set 
		IObservableValue textAuthorObserveTextObserveWidget = SWTObservables.observeText(textAuthor, SWT.Modify);
		IObservableValue literaturErstAuthorObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__ERSTAUTOR);
		dbc.bindValue(textAuthorObserveTextObserveWidget, literaturErstAuthorObserveValue, DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.LiteraturEditor_30), DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.LiteraturEditor_31));
		bindTitleName(literaturErstAuthorObserveValue, new UpdateValueStrategy().setConverter(new IConverter() {
			
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
					return Messages.LiteraturEditor_32;
				}
				return fromObject;
			}
		}));
		
		// binds the title, this field is required to be set 
		IObservableValue textTitelObserveTextObserveWidget = SWTObservables.observeText(textTitel, SWT.Modify);
		IObservableValue literaturTitelObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__TITEL);
		dbc.bindValue(textTitelObserveTextObserveWidget, literaturTitelObserveValue, DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.LiteraturEditor_33), DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.LiteraturEditor_34));
		// binds the year field
		IValidator yearValidator = new IValidator() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
			 */
			@Override
			public IStatus validate(Object value) {
				if (value instanceof Integer) {
					Integer i = (Integer) value;
					if (i < 1700 || i > 2050) {
						return ValidationStatus.error(Messages.LiteraturEditor_35);
					}
				}
				return ValidationStatus.ok();
			}
		};
		IObservableValue spinnerJahrObserveSelectionObserveWidget = SWTObservables.observeSelection(spinnerJahr);
		IObservableValue literaturJahrObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__JAHR);
		dbc.bindValue(spinnerJahrObserveSelectionObserveWidget, literaturJahrObserveValue, new UpdateValueStrategy().setAfterGetValidator(yearValidator), new UpdateValueStrategy().setAfterGetValidator(yearValidator));
		// binds the abstract texts
		IObservableValue abstractTextObserveTextObserveWidget = SWTObservables.observeText(abstractText, SWT.Modify);
		IObservableValue literaturAbstractObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__LITERATUR_ABSTRACT);
		dbc.bindValue(abstractTextObserveTextObserveWidget, literaturAbstractObserveValue, null, null);
		// binds the page field
		IValidator numberValidator = new IValidator() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
			 */
			@Override
			public IStatus validate(Object value) {
				if (value == null || value.toString().length() == 0) {
					return ValidationStatus.ok();
				}
				if (value instanceof String) {
					try {
						Integer i = Integer.parseInt((String) value);
						return i > 0 ? ValidationStatus.ok() : ValidationStatus.error(Messages.LiteraturEditor_36);
					} catch (NumberFormatException e) {
						return ValidationStatus.error(Messages.LiteraturEditor_37);
					}
				} 
				if (value instanceof Integer) {
					Integer i = (Integer) value;
					return i > 0 ? ValidationStatus.ok() : ValidationStatus.error(Messages.LiteraturEditor_38);
				}
				return ValidationStatus.error(Messages.LiteraturEditor_39 + value.getClass().getName());
			}
		};
		IObservableValue textPageObserveTextObserveWidget = SWTObservables.observeText(textPage, SWT.Modify);
		IObservableValue literaturSeiteObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__SEITE);
		dbc.bindValue(textPageObserveTextObserveWidget, literaturSeiteObserveValue, new UpdateValueStrategy().setAfterGetValidator(numberValidator).setConverter(new String2IntegerConverter()), new UpdateValueStrategy().setAfterGetValidator(numberValidator).setConverter(new ToStringConverter()));
		//
		IObservableValue textJournalObserveTextObserveWidget = SWTObservables.observeText(textJournal, SWT.Modify);
		IObservableValue literaturJournalObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__JOURNAL);
		dbc.bindValue(textJournalObserveTextObserveWidget, literaturJournalObserveValue, null, null);
		//
		IObservableValue textVolumeObserveTextObserveWidget = SWTObservables.observeText(textVolume, SWT.Modify);
		IObservableValue literaturVolumeObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__VOLUME);
		dbc.bindValue(textVolumeObserveTextObserveWidget, literaturVolumeObserveValue, null, null);
		//
		IObservableValue textIssueObserveTextObserveWidget = SWTObservables.observeText(textIssue, SWT.Modify);
		IObservableValue literaturIssueObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__ISSUE);
		dbc.bindValue(textIssueObserveTextObserveWidget, literaturIssueObserveValue, null, null);
		//
		IObservableValue textPaperObserveTextObserveWidget = SWTObservables.observeText(textPaper, SWT.Modify);
		IObservableValue literaturPaperObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__PAPER);
		dbc.bindValue(textPaperObserveTextObserveWidget, literaturPaperObserveValue, null, null);
		//
		IObservableValue textWebsiteObserveTextObserveWidget = SWTObservables.observeText(textWebsite, SWT.Modify);
		IObservableValue literaturWebseiteObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__WEBSEITE);
		dbc.bindValue(textWebsiteObserveTextObserveWidget, literaturWebseiteObserveValue, null, null);
		//
		IObservableValue textCommentObserveTextObserveWidget = SWTObservables.observeText(textComment, SWT.Modify);
		IObservableValue literaturKommentarObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__KOMMENTAR);
		dbc.bindValue(textCommentObserveTextObserveWidget, literaturKommentarObserveValue, null, null);
		// binds the selection of the literature viewer
		IObservableValue comboLiteratureTypeObserveSelectionObserveWidget = ViewersObservables.observeSingleSelection(literatureViewer);
		IObservableValue literaturLiteraturTypObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__LITERATUR_TYP);
		dbc.bindValue(comboLiteratureTypeObserveSelectionObserveWidget, literaturLiteraturTypObserveValue, null, null);
		// binds the selection of the permission viewer
		IObservableValue comboPermissionTypeObserveSelectionObserveWidget = ViewersObservables.observeSingleSelection(permissionViewer);
		IObservableValue literaturFreigabeModusObserveValue = EMFEditObservables.observeValue(getEditingDomain(), literature, Literals.LITERATUR__FREIGABE_MODUS);
		dbc.bindValue(comboPermissionTypeObserveSelectionObserveWidget, literaturFreigabeModusObserveValue, null, null);
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.editors.AbstractEMFEditor#internalDoSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected boolean internalDoSave(IProgressMonitor monitor) {
		if (!isValid()) {
			MessageDialog.openInformation(getSite().getShell(), Messages.LiteraturEditor_40, Messages.LiteraturEditor_41);
			return false;
		}
		return service.saveLiterature(literature);
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
		setPartName(input.getName());
		setTitleToolTip(input.getToolTipText());
		literature = (Literatur) getModel();
		service = BFRUIActivator.getBFRService();
	}
}
