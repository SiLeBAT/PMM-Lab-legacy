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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/dialogs/CovCorParameterEditDialog.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.dialogs;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.dialog.TitleAreaDialogSupport;
import org.eclipse.jface.databinding.dialog.ValidationMessageProvider;
import org.eclipse.jface.databinding.swt.WidgetProperties;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.dim.bfr.BfrFactory;
import de.dim.bfr.BfrPackage;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.ParameterCovCor;
import de.dim.bfr.ui.databinding.converter.Double2StringConverter;
import de.dim.bfr.ui.databinding.converter.String2DoubleConverter;
import de.dim.bfr.ui.databinding.validators.DoubleValidator;
import de.dim.bfr.ui.databinding.validators.NotEmptyValidator;
import de.dim.bfr.ui.message.Messages;

/**
 * Dialog for creating or modifying model parameters
 * @author Mark Hoffmann
 * @since 18.11.2011
 */
public class CovCorParameterEditDialog extends TitleAreaDialog {

	private final EMFDataBindingContext dbc = new EMFDataBindingContext();
	private final EditingDomain domain;
	private final GeschaetztStatistikModell statistikModell;
	private final ParameterCovCor parameter;
	private final boolean isNewModel;
	private final String message =Messages.CovCorParameterEditDialog_0;
	private Text textValue;
	private Button checkCorrelation;
	private ComboViewer parameter1Viewer;
	private ComboViewer parameter2Viewer;

	/**
	 * Create the dialog.
	 * @param parentShell the under lying shell
	 * @param model the statistic model instance
	 * @param domain the editing domain
	 * @wbp.parser.constructor
	 */
	public CovCorParameterEditDialog(Shell parentShell, GeschaetztStatistikModell model, EditingDomain domain) {
		this(parentShell, model, null, domain);
	}
	
	/**
	 * Create the dialog.
	 * @param parentShell the under lying shell
	 * @param model the statistic model instance
	 * @param parameter the model parameter to edit
	 * @param domain the editing domain
	 */
	public CovCorParameterEditDialog(Shell parentShell, GeschaetztStatistikModell model, ParameterCovCor parameter, EditingDomain domain) {
		super(parentShell);
		this.domain = domain;
		Assert.isNotNull(model);
		this.statistikModell = model;
		if (parameter == null) {
			this.parameter = BfrFactory.eINSTANCE.createParameterCovCor();
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
		setTitle(Messages.CovCorParameterEditDialog_1);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		GridLayout gl_container = new GridLayout(2, false);
		gl_container.horizontalSpacing = 7;
		container.setLayout(gl_container);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label labelParameter1 = new Label(container, SWT.NONE);
		GridData gd_labelParameter1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelParameter1.widthHint = 100;
		labelParameter1.setLayoutData(gd_labelParameter1);
		labelParameter1.setText(Messages.CovCorParameterEditDialog_2);
		
		parameter1Viewer = new ComboViewer(container, SWT.READ_ONLY);
		parameter1Viewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelParameter2 = new Label(container, SWT.NONE);
		GridData gd_labelParameter2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelParameter2.widthHint = 100;
		labelParameter2.setLayoutData(gd_labelParameter2);
		labelParameter2.setText(Messages.CovCorParameterEditDialog_3);
		
		parameter2Viewer = new ComboViewer(container, SWT.READ_ONLY);
		parameter2Viewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelCor = new Label(container, SWT.NONE);
		GridData gd_labelCor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelCor.widthHint = 100;
		labelCor.setLayoutData(gd_labelCor);
		labelCor.setText(""); //$NON-NLS-1$
		
		checkCorrelation = new Button(container, SWT.CHECK);
		checkCorrelation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		checkCorrelation.setText(Messages.CovCorParameterEditDialog_5);
		
		Label labelValue = new Label(container, SWT.NONE);
		GridData gd_labelValue = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelValue.widthHint = 100;
		labelValue.setLayoutData(gd_labelValue);
		labelValue.setText(Messages.CovCorParameterEditDialog_6);
		
		textValue = new Text(container, SWT.BORDER);
		textValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
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
		return new Point(450, 350);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		// add the parameter, it is a new created one
		if (isNewModel) {
			Command addCommand = AddCommand.create(domain, statistikModell, BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR, parameter);
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
		IObservableList parameterCovCorOL = EMFEditProperties.list(domain, BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__PARAMETER).observe(statistikModell);
		// bind model combo 
		ObservableListContentProvider olcp1 = new ObservableListContentProvider();
		parameter1Viewer.setContentProvider(olcp1);
		IObservableSet knownElements1 = olcp1.getKnownElements();
		IObservableMap labelMap1 = EMFEditProperties.value(domain, BfrPackage.Literals.GESCH_MODELL_PARAMETER__NAME).observeDetail(knownElements1);
		parameter1Viewer.setLabelProvider(new ObservableMapLabelProvider(labelMap1));
		parameter1Viewer.setInput(parameterCovCorOL);
		ObservableListContentProvider olcp2 = new ObservableListContentProvider();
		parameter2Viewer.setContentProvider(olcp2);
		IObservableSet knownElements2 = olcp2.getKnownElements();
		IObservableMap labelMap2 = EMFEditProperties.value(domain, BfrPackage.Literals.GESCH_MODELL_PARAMETER__NAME).observeDetail(knownElements2);
		parameter2Viewer.setLabelProvider(new ObservableMapLabelProvider(labelMap2));
		parameter2Viewer.setInput(parameterCovCorOL);
		
		IObservableValue parameter1SelectionOV = ViewerProperties.singleSelection().observe(parameter1Viewer);
		IObservableValue parameter2SelectionOV = ViewerProperties.singleSelection().observe(parameter2Viewer);
		IObservableValue corSelectionOV = WidgetProperties.selection().observe(checkCorrelation);
		IObservableValue textValueOV = WidgetProperties.text(SWT.Modify).observe(textValue);
		
		
		IObservableValue param1ModelOV = EMFEditProperties.value(domain, BfrPackage.Literals.PARAMETER_COV_COR__PARAMETER1).observe(parameter);
		IObservableValue param2ModelOV = EMFEditProperties.value(domain, BfrPackage.Literals.PARAMETER_COV_COR__PARAMETER2).observe(parameter);
		IObservableValue valueModelOV = EMFEditProperties.value(domain, BfrPackage.Literals.PARAMETER_COV_COR__VALUE).observe(parameter);
		IObservableValue corModelOV = EMFEditProperties.value(domain, BfrPackage.Literals.PARAMETER_COV_COR__COR).observe(parameter);
		// double converter
		IConverter s2dConverter = new String2DoubleConverter();
		IConverter d2sConverter = new Double2StringConverter();
		// observe value
		UpdateValueStrategy t2mValueUVS = new UpdateValueStrategy().setAfterGetValidator(new DoubleValidator()).setConverter(s2dConverter);
		UpdateValueStrategy m2tValueUVS = new UpdateValueStrategy().setAfterGetValidator(new DoubleValidator()).setConverter(d2sConverter);
		dbc.bindValue(textValueOV, valueModelOV, t2mValueUVS, m2tValueUVS);
		IValidator parameter1Validator = new NotEmptyValidator(Messages.CovCorParameterEditDialog_7);
		IValidator parameter2Validator = new NotEmptyValidator(Messages.CovCorParameterEditDialog_8);
		// bind parameter 1
		dbc.bindValue(parameter1SelectionOV, param1ModelOV, new UpdateValueStrategy().setAfterGetValidator(parameter1Validator), new UpdateValueStrategy().setAfterGetValidator(parameter1Validator));
		dbc.bindValue(parameter2SelectionOV, param2ModelOV, new UpdateValueStrategy().setAfterGetValidator(parameter2Validator), new UpdateValueStrategy().setAfterGetValidator(parameter2Validator));
		dbc.bindValue(corSelectionOV, corModelOV);
		// dummy binding to force initial dialog change
		dbc.bindValue(new WritableValue("dummy", String.class), new WritableValue()); //$NON-NLS-1$
	}
	
}
