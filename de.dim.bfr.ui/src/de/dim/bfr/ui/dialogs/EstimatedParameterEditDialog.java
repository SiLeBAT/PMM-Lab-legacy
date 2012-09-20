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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/dialogs/EstimatedParameterEditDialog.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.dialogs;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.dialog.TitleAreaDialogSupport;
import org.eclipse.jface.databinding.dialog.ValidationMessageProvider;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.dim.bfr.BfrFactory;
import de.dim.bfr.BfrPackage;
import de.dim.bfr.BfrPackage.Literals;
import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.ui.databinding.converter.Double2StringConverter;
import de.dim.bfr.ui.databinding.converter.String2DoubleConverter;
import de.dim.bfr.ui.databinding.validators.NotEmptyValidator;
import de.dim.bfr.ui.databinding.validators.ParameterValidator;
import de.dim.bfr.ui.message.Messages;

/**
 * Dialog for creating or modifying model parameters
 * @author Mark Hoffmann
 * @since 18.11.2011
 */
public class EstimatedParameterEditDialog extends TitleAreaDialog {

	private final DataBindingContext dbc = new EMFDataBindingContext();
	private final EditingDomain domain;
	private final GeschaetztStatistikModell statistikModell;
	private final GeschModellParameter parameter;
	private final boolean isNewModel;
	private String message = Messages.EstimatedParameterEditDialog_0;
	private Text textValue;
	private Text textSD;
	private Text textT;
	private Text textP;
	private Text textCIup;
	private Text textCIdown;
	private ComboViewer parameterViewer;

	/**
	 * Create the dialog.
	 * @param parentShell the under lying shell
	 * @param model the statistic model instance
	 * @param domain the editing domain
	 * @wbp.parser.constructor
	 */
	public EstimatedParameterEditDialog(Shell parentShell, GeschaetztStatistikModell model, EditingDomain domain) {
		this(parentShell, model, null, domain);
	}
	
	/**
	 * Create the dialog.
	 * @param parentShell the under lying shell
	 * @param model the statistic model instance
	 * @param parameter the model parameter to edit
	 * @param domain the editing domain
	 */
	public EstimatedParameterEditDialog(Shell parentShell, GeschaetztStatistikModell model, GeschModellParameter parameter, EditingDomain domain) {
		super(parentShell);
		this.domain = domain;
		Assert.isNotNull(model);
		this.statistikModell = model;
		if (parameter == null) {
			this.parameter = BfrFactory.eINSTANCE.createGeschModellParameter();
			isNewModel = true;
		} else {
			this.parameter = parameter;
			isNewModel = false;
		}
	}

	/**
	 * Create contents of the dialog.
	 * @param parent the parent composite of this area
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		if (isNewModel) {
			message = Messages.EstimatedParameterEditDialog_1;
		} 
		setTitle(Messages.EstimatedParameterEditDialog_2);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		GridLayout gl_container = new GridLayout(2, false);
		gl_container.horizontalSpacing = 7;
		container.setLayout(gl_container);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label labelParameter = new Label(container, SWT.NONE);
		GridData gd_labelParameter = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelParameter.widthHint = 100;
		labelParameter.setLayoutData(gd_labelParameter);
		labelParameter.setText(Messages.EstimatedParameterEditDialog_3);
		
		parameterViewer = new ComboViewer(container, SWT.READ_ONLY);
		parameterViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelValue = new Label(container, SWT.NONE);
		GridData gd_labelValue = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelValue.widthHint = 100;
		labelValue.setLayoutData(gd_labelValue);
		labelValue.setText(Messages.EstimatedParameterEditDialog_4);
		
		textValue = new Text(container, SWT.BORDER);
		textValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelSD = new Label(container, SWT.NONE);
		GridData gd_labelSD = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelSD.widthHint = 100;
		labelSD.setLayoutData(gd_labelSD);
		labelSD.setText(Messages.EstimatedParameterEditDialog_5);
		
		textSD = new Text(container, SWT.BORDER);
		textSD.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelT = new Label(container, SWT.NONE);
		GridData gd_labelT = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelT.widthHint = 100;
		labelT.setLayoutData(gd_labelT);
		labelT.setText(Messages.EstimatedParameterEditDialog_6);
		
		textT = new Text(container, SWT.BORDER);
		textT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelP = new Label(container, SWT.NONE);
		GridData gd_labelP = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelP.widthHint = 100;
		labelP.setLayoutData(gd_labelP);
		labelP.setText(Messages.EstimatedParameterEditDialog_7);
		
		textP = new Text(container, SWT.BORDER);
		textP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelCIup = new Label(container, SWT.NONE);
		GridData gd_labelCIup = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelCIup.widthHint = 130;
		labelCIup.setLayoutData(gd_labelCIup);
		labelCIup.setText(Messages.EstimatedParameterEditDialog_8);
		
		textCIup = new Text(container, SWT.BORDER);
		textCIup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelCIdown = new Label(container, SWT.NONE);
		GridData gd_labelCIdown = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelCIdown.widthHint = 130;
		labelCIdown.setLayoutData(gd_labelCIdown);
		labelCIdown.setText(Messages.EstimatedParameterEditDialog_9);
		
		textCIdown = new Text(container, SWT.BORDER);
		textCIdown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent the parent button bar area
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 400);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		// add the parameter, it is a new created one
		if (isNewModel) {
			Command addCommand = AddCommand.create(domain, statistikModell, BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__PARAMETER, parameter);
			domain.getCommandStack().execute(addCommand);
		}
		super.okPressed();
	}
	
	/**
	 * Initializes the data binding
	 */
	private void initDataBindings() {
		// Make errors visible as dialog message
		TitleAreaDialogSupport.create(this, dbc).setValidationMessageProvider(new ValidationMessageProvider() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.jface.databinding.dialog.ValidationMessageProvider#getMessage(org.eclipse.core.databinding.ValidationStatusProvider)
			 */
			@Override
			public String getMessage(ValidationStatusProvider statusProvider) {
				if (statusProvider == null) {
					return message;
				}
				return super.getMessage(statusProvider);
			}

			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.jface.databinding.dialog.ValidationMessageProvider#getMessageType(org.eclipse.core.databinding.ValidationStatusProvider)
			 */
			@Override
			public int getMessageType(ValidationStatusProvider statusProvider) {
				int type = super.getMessageType(statusProvider);
				if( getButton(IDialogConstants.OK_ID) != null ) {
					getButton(IDialogConstants.OK_ID).setEnabled(type != IMessageProvider.ERROR);
				}
				return type;
			}
		});
		// bind model combo 
		ObservableListContentProvider olcp = new ObservableListContentProvider();
		parameterViewer.setContentProvider(olcp);
		IObservableSet knownElements = olcp.getKnownElements();
		IObservableMap labelMap = EMFEditProperties.value(domain, BfrPackage.Literals.STATISTIK_MODELL_PARAMETER__NAME).observeDetail(knownElements);
		parameterViewer.setLabelProvider(new ObservableMapLabelProvider(labelMap));
		IObservableList existingEstParameterOL = EMFEditProperties.list(domain, BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__PARAMETER).observe(statistikModell);
		parameterViewer.addFilter(new RoleViewerFilter(existingEstParameterOL));
		IObservableList parameterOL = EMFEditProperties.list(domain, FeaturePath.fromList(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL, BfrPackage.Literals.STATISTIK_MODELL__PARAMETER)).observe(statistikModell);
		parameterViewer.setInput(parameterOL);
		IObservableValue parameterSelectionOV = ViewerProperties.singleSelection().observe(parameterViewer);
		IObservableValue modelParameterOV = EMFEditProperties.value(domain, BfrPackage.Literals.GESCH_MODELL_PARAMETER__MODEL_PARAMETER).observe(parameter);
		dbc.bindValue(parameterSelectionOV, modelParameterOV, new UpdateValueStrategy().setAfterGetValidator(new NotEmptyValidator(Messages.EstimatedParameterEditDialog_10)), new UpdateValueStrategy().setAfterGetValidator(new NotEmptyValidator(Messages.EstimatedParameterEditDialog_11)));
		final IObservableValue integerDetailOV = EMFEditProperties.value(domain, BfrPackage.Literals.STATISTIK_MODELL_PARAMETER__INTEGER).observeDetail(parameterSelectionOV);
		IObservableValue nameDetailOV = EMFEditProperties.value(domain, BfrPackage.Literals.STATISTIK_MODELL_PARAMETER__NAME).observeDetail(parameterSelectionOV);
		IObservableValue modelNameOV = EMFEditProperties.value(domain, BfrPackage.Literals.GESCH_MODELL_PARAMETER__NAME).observe(parameter);
		dbc.bindValue(nameDetailOV, modelNameOV);
		IObservableValue minDetailOV = EMFEditProperties.value(domain, BfrPackage.Literals.STATISTIK_MODELL_PARAMETER__MIN).observeDetail(parameterSelectionOV);
		IObservableValue maxDetailOV = EMFEditProperties.value(domain, BfrPackage.Literals.STATISTIK_MODELL_PARAMETER__MAX).observeDetail(parameterSelectionOV);
		// validators for this dialog
		IValidator valueValidator = new ParameterValidator(integerDetailOV, minDetailOV, maxDetailOV);
		// double converter
		IConverter s2dConverter = new String2DoubleConverter();
		IConverter d2sConverter = new Double2StringConverter();
		// observe value
		IObservableValue textValueObserveTextObserveWidget = SWTObservables.observeText(textValue, SWT.Modify);
		IObservableValue parameterWertObserveValue = EMFObservables.observeValue(parameter, Literals.GESCH_MODELL_PARAMETER__WERT);
		UpdateValueStrategy t2mValueUVS = new UpdateValueStrategy().setAfterGetValidator(valueValidator).setConverter(s2dConverter);
		UpdateValueStrategy m2tValueUVS = new UpdateValueStrategy().setAfterGetValidator(valueValidator).setConverter(d2sConverter);
		final Binding valueBinding = dbc.bindValue(textValueObserveTextObserveWidget, parameterWertObserveValue, t2mValueUVS, m2tValueUVS);
		// bind SD field
		UpdateValueStrategy t2mSDUVS = new UpdateValueStrategy().setConverter(s2dConverter);
		UpdateValueStrategy m2tSDUVS = new UpdateValueStrategy().setConverter(d2sConverter);
		IObservableValue textSDObserveTextObserveWidget = SWTObservables.observeText(textSD, SWT.Modify);
		IObservableValue parameterSdObserveValue = EMFObservables.observeValue(parameter, Literals.GESCH_MODELL_PARAMETER__SD);
		dbc.bindValue(textSDObserveTextObserveWidget, parameterSdObserveValue, t2mSDUVS, m2tSDUVS);
		// bind T field
		IObservableValue textTObserveTextObserveWidget = SWTObservables.observeText(textT, SWT.Modify);
		IObservableValue parameterTObserveValue = EMFObservables.observeValue(parameter, Literals.GESCH_MODELL_PARAMETER__T);
		UpdateValueStrategy t2mTUVS = new UpdateValueStrategy().setConverter(s2dConverter);
		UpdateValueStrategy m2tTUVS = new UpdateValueStrategy().setConverter(d2sConverter);
		dbc.bindValue(textTObserveTextObserveWidget, parameterTObserveValue, t2mTUVS, m2tTUVS);
		// bind P field
		IObservableValue textPObserveTextObserveWidget = SWTObservables.observeText(textP, SWT.Modify);
		IObservableValue parameterPObserveValue = EMFObservables.observeValue(parameter, Literals.GESCH_MODELL_PARAMETER__P);
		UpdateValueStrategy t2mPUVS = new UpdateValueStrategy().setConverter(s2dConverter);
		UpdateValueStrategy m2tPUVS = new UpdateValueStrategy().setConverter(d2sConverter);
		dbc.bindValue(textPObserveTextObserveWidget, parameterPObserveValue, t2mPUVS, m2tPUVS);
		// bind confidence interval up field
		IObservableValue textCIupTextOV = SWTObservables.observeText(textCIup, SWT.Modify);
		IObservableValue modelCIupOV = EMFObservables.observeValue(parameter, Literals.GESCH_MODELL_PARAMETER__KI_OBEN);
		UpdateValueStrategy t2mCIUUVS = new UpdateValueStrategy().setConverter(s2dConverter);
		UpdateValueStrategy m2tCIUUVS = new UpdateValueStrategy().setConverter(d2sConverter);
		dbc.bindValue(textCIupTextOV, modelCIupOV, t2mCIUUVS, m2tCIUUVS);
		// bind confidence interval down field
		IObservableValue textCIdownTextOV = SWTObservables.observeText(textCIdown, SWT.Modify);
		IObservableValue modelCIdownOV = EMFObservables.observeValue(parameter, Literals.GESCH_MODELL_PARAMETER__KI_UNTEN);
		UpdateValueStrategy t2mCIDUVS = new UpdateValueStrategy().setConverter(s2dConverter);
		UpdateValueStrategy m2tCIDUVS = new UpdateValueStrategy().setConverter(d2sConverter);
		dbc.bindValue(textCIdownTextOV, modelCIdownOV, t2mCIDUVS, m2tCIDUVS);
		// revalidate if the selection changes and a new parameter is selected
		maxDetailOV.addChangeListener(new IChangeListener() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.observable.IChangeListener#handleChange(org.eclipse.core.databinding.observable.ChangeEvent)
			 */
			@Override
			public void handleChange(ChangeEvent event) {
				valueBinding.validateTargetToModel();
			}
		});
	}
	
}
