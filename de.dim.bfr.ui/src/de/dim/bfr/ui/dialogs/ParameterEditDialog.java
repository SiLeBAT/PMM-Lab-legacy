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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/dialogs/ParameterEditDialog.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.MultiValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.dialog.TitleAreaDialogSupport;
import org.eclipse.jface.databinding.dialog.ValidationMessageProvider;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.dim.bfr.BfrFactory;
import de.dim.bfr.BfrPackage;
import de.dim.bfr.BfrPackage.Literals;
import de.dim.bfr.ParameterRoleType;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.ui.databinding.DIMDatabindingHelper;
import de.dim.bfr.ui.databinding.converter.Double2StringConverter;
import de.dim.bfr.ui.databinding.converter.String2DoubleConverter;
import de.dim.bfr.ui.databinding.validators.DoubleValidator;
import de.dim.bfr.ui.databinding.validators.RoleValidator;
import de.dim.bfr.ui.message.Messages;

/**
 * Dialog for creating or modifying model parameters
 * @author Mark Hoffmann
 * @since 18.11.2011
 */
public class ParameterEditDialog extends TitleAreaDialog {

	private final DataBindingContext dbc = new EMFDataBindingContext();
	private final EditingDomain domain;
	private final StatistikModell statistikModell;
	private final StatistikModellParameter parameter;
	private final boolean isNewModel;
	private String message =Messages.ParameterEditDialog_0;
	private Text textName;
	private Text textMin;
	private Text textMax;
	private Text textDescription;
	private Button checkInteger;
	private Combo comboRole;
	private ComboViewer roleViewer;

	/**
	 * Create the dialog.
	 * @param parentShell the under lying shell
	 * @param model the statistic model instance
	 * @param domain the editing domain
	 * @wbp.parser.constructor
	 */
	public ParameterEditDialog(Shell parentShell, StatistikModell model, EditingDomain domain) {
		this(parentShell, model, null, domain);
	}
	
	/**
	 * Create the dialog.
	 * @param parentShell the under lying shell
	 * @param model the statistic model instance
	 * @param parameter the model parameter to edit
	 * @param domain the editing domain
	 */
	public ParameterEditDialog(Shell parentShell, StatistikModell model, StatistikModellParameter parameter, EditingDomain domain) {
		super(parentShell);
		this.domain = domain;
		Assert.isNotNull(model);
		
		this.statistikModell = model;
		if (parameter == null) {
			this.parameter = BfrFactory.eINSTANCE.createStatistikModellParameter();
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
			message = Messages.ParameterEditDialog_1;
		} 
		setTitle(Messages.ParameterEditDialog_2);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		GridLayout gl_container = new GridLayout(2, false);
		gl_container.horizontalSpacing = 7;
		container.setLayout(gl_container);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label labelName = new Label(container, SWT.NONE);
		GridData gd_labelName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelName.widthHint = 100;
		labelName.setLayoutData(gd_labelName);
		labelName.setText(Messages.ParameterEditDialog_3);
		
		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelRole = new Label(container, SWT.NONE);
		GridData gd_labelRole = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelRole.widthHint = 100;
		labelRole.setLayoutData(gd_labelRole);
		labelRole.setText(Messages.ParameterEditDialog_4);
		
		comboRole = new Combo(container, SWT.READ_ONLY);
		comboRole.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		roleViewer = new ComboViewer(comboRole);
		roleViewer.setLabelProvider(new LabelProvider());
		roleViewer.setContentProvider(new ArrayContentProvider());
		roleViewer.setInput(ParameterRoleType.VALUES);
		
		Label labelInteger = new Label(container, SWT.NONE);
		GridData gd_labelInteger = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelInteger.widthHint = 100;
		labelInteger.setLayoutData(gd_labelInteger);
		labelInteger.setText(""); //$NON-NLS-1$
		
		checkInteger = new Button(container, SWT.CHECK);
		checkInteger.setText(Messages.ParameterEditDialog_6);
		checkInteger.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelMin = new Label(container, SWT.NONE);
		GridData gd_labelMin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelMin.widthHint = 100;
		labelMin.setLayoutData(gd_labelMin);
		labelMin.setText(Messages.ParameterEditDialog_7);
		
		textMin = new Text(container, SWT.BORDER);
		textMin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelMax = new Label(container, SWT.NONE);
		GridData gd_labelMax = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_labelMax.widthHint = 100;
		labelMax.setLayoutData(gd_labelMax);
		labelMax.setText(Messages.ParameterEditDialog_8);
		
		textMax = new Text(container, SWT.BORDER);
		textMax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelDescription = new Label(container, SWT.NONE);
		GridData gd_labelDescription = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_labelDescription.widthHint = 100;
		labelDescription.setLayoutData(gd_labelDescription);
		labelDescription.setText(Messages.ParameterEditDialog_9);
		
		textDescription = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_textDescription.heightHint = 50;
		textDescription.setLayoutData(gd_textDescription);

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
		return new Point(500, 400);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		// add the parameter, it is a new created one
		if (isNewModel) {
			Command addCommand = AddCommand.create(domain, statistikModell, BfrPackage.Literals.STATISTIK_MODELL__PARAMETER, parameter);
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
		// Number validator for min and max
		// bind the parameter name
		IObservableValue textNameObserveTextObserveWidget = SWTObservables.observeText(textName, SWT.Modify);
		IObservableValue parameterNameObserveValue = EMFObservables.observeValue(parameter, Literals.STATISTIK_MODELL_PARAMETER__NAME);
		dbc.bindValue(textNameObserveTextObserveWidget, parameterNameObserveValue, DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.ParameterEditDialog_10), DIMDatabindingHelper.createNotEmptyUpdateStrategy(Messages.ParameterEditDialog_11));
		// bind the role selection
		IObservableValue roleSelectionOV = ViewersObservables.observeSingleSelection(roleViewer);
		IObservableValue roleModelOV = EMFEditObservables.observeValue(domain, parameter, BfrPackage.Literals.STATISTIK_MODELL_PARAMETER__ROLE);
		dbc.bindValue(roleSelectionOV, roleModelOV, new UpdateValueStrategy().setAfterGetValidator(new RoleValidator()), new UpdateValueStrategy().setAfterGetValidator(new RoleValidator()));
		// bind integer check box
		IObservableValue checkIntegerOV = SWTObservables.observeSelection(checkInteger);
		IObservableValue integerModelOV = EMFEditObservables.observeValue(domain, parameter, BfrPackage.Literals.STATISTIK_MODELL_PARAMETER__INTEGER);
		dbc.bindValue(checkIntegerOV, integerModelOV);
		// bind min and max  with its cross validation constraint: min <= max
		IObservableValue textMinObserveTextObserveWidget = SWTObservables.observeText(textMin, SWT.Modify);
		IObservableValue textMaxObserveTextObserveWidget = SWTObservables.observeText(textMax, SWT.Modify);
		// create caching values
		final IObservableValue middleMin = new WritableValue(null, Double.TYPE);
		final IObservableValue middleMax = new WritableValue(null, Double.TYPE);
		// 1. bind text widget to caching values
		dbc.bindValue(textMinObserveTextObserveWidget, middleMin, new UpdateValueStrategy().setAfterGetValidator(new DoubleValidator()).setConverter(new String2DoubleConverter()), new UpdateValueStrategy().setConverter(new Double2StringConverter()));
		dbc.bindValue(textMaxObserveTextObserveWidget, middleMax, new UpdateValueStrategy().setAfterGetValidator(new DoubleValidator()).setConverter(new String2DoubleConverter()), new UpdateValueStrategy().setConverter(new Double2StringConverter()));
		// 2. create multi validator
		MultiValidator minMaxValidator = new MultiValidator() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.validation.MultiValidator#validate()
			 */
			@Override
			protected IStatus validate() {
				Double min = (Double)middleMin.getValue();
				Double max = (Double)middleMax.getValue();
				if (min != null && max != null) {
					return min <= max ? ValidationStatus.ok() : ValidationStatus.error(Messages.ParameterEditDialog_12);
				}
				return ValidationStatus.ok();
			}
		};
		dbc.addValidationStatusProvider(minMaxValidator);
		
		// 3. Validated observables validatedMin and validatedMax do not change value until the validator passes. 
		IObservableValue validatedMin = minMaxValidator.observeValidatedValue(middleMin);
		IObservableValue validatedMax = minMaxValidator.observeValidatedValue(middleMax);
		// 4. Bind to the validated value, to the model
		IObservableValue parameterMinObserveValue = EMFObservables.observeValue(parameter, Literals.STATISTIK_MODELL_PARAMETER__MIN);
		IObservableValue parameterMaxObserveValue = EMFObservables.observeValue(parameter, Literals.STATISTIK_MODELL_PARAMETER__MAX);
		dbc.bindValue(validatedMin, parameterMinObserveValue, null, null);
		dbc.bindValue(validatedMax, parameterMaxObserveValue, null, null);
		// bind description
		IObservableValue textDescriptionObserveTextObserveWidget = SWTObservables.observeText(textDescription, SWT.Modify);
		IObservableValue parameterBeschreibungObserveValue = EMFObservables.observeValue(parameter, Literals.STATISTIK_MODELL_PARAMETER__BESCHREIBUNG);
		dbc.bindValue(textDescriptionObserveTextObserveWidget, parameterBeschreibungObserveValue, null, null);
	}
	
}
