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
 * $Id: DoubleKennzahlenItemProvider.java 651 2012-01-24 09:59:12Z sdoerl $
 */
package de.dim.bfr.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import de.dim.bfr.BfrPackage;
import de.dim.bfr.DoubleKennzahlen;

/**
 * This is the item provider adapter for a {@link de.dim.bfr.DoubleKennzahlen} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class DoubleKennzahlenItemProvider
	extends ItemProviderAdapter
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DoubleKennzahlenItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addFunctionXPropertyDescriptor(object);
			addFunctionXGPropertyDescriptor(object);
			addFunctionZeitPropertyDescriptor(object);
			addFunctionZeitGPropertyDescriptor(object);
			addIdPropertyDescriptor(object);
			addLcl95PropertyDescriptor(object);
			addLcl95GPropertyDescriptor(object);
			addMaximumPropertyDescriptor(object);
			addMaximumGPropertyDescriptor(object);
			addMedianPropertyDescriptor(object);
			addMedianGPropertyDescriptor(object);
			addMinimumPropertyDescriptor(object);
			addMinimumGPropertyDescriptor(object);
			addStandardabweichungPropertyDescriptor(object);
			addStandardabweichungGPropertyDescriptor(object);
			addTimes10PowerOfPropertyDescriptor(object);
			addUcl95PropertyDescriptor(object);
			addUcl95GPropertyDescriptor(object);
			addUndefinedNDPropertyDescriptor(object);
			addVerteilungPropertyDescriptor(object);
			addVerteilungGPropertyDescriptor(object);
			addWertPropertyDescriptor(object);
			addWertGPropertyDescriptor(object);
			addWertTypPropertyDescriptor(object);
			addWiederholungenPropertyDescriptor(object);
			addWiederholungenGPropertyDescriptor(object);
			addXPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Function X feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFunctionXPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_functionX_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_functionX_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__FUNCTION_X,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Function XG feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFunctionXGPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_functionXG_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_functionXG_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__FUNCTION_XG,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Function Zeit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFunctionZeitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_functionZeit_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_functionZeit_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Function Zeit G feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFunctionZeitGPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_functionZeitG_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_functionZeitG_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT_G,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Id feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIdPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_id_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_id_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__ID,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Lcl95 feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLcl95PropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_lcl95_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_lcl95_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__LCL95,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Lcl95 G feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLcl95GPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_lcl95G_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_lcl95G_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__LCL95_G,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Maximum feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaximumPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_maximum_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_maximum_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__MAXIMUM,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Maximum G feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaximumGPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_maximumG_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_maximumG_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__MAXIMUM_G,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Median feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMedianPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_median_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_median_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__MEDIAN,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Median G feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMedianGPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_medianG_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_medianG_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__MEDIAN_G,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Minimum feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinimumPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_minimum_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_minimum_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__MINIMUM,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Minimum G feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinimumGPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_minimumG_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_minimumG_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__MINIMUM_G,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Standardabweichung feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStandardabweichungPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_standardabweichung_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_standardabweichung_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Standardabweichung G feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStandardabweichungGPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_standardabweichungG_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_standardabweichungG_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG_G,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Times10 Power Of feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTimes10PowerOfPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_times10PowerOf_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_times10PowerOf_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__TIMES10_POWER_OF,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ucl95 feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUcl95PropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_ucl95_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_ucl95_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__UCL95,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ucl95 G feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUcl95GPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_ucl95G_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_ucl95G_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__UCL95_G,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Undefined ND feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUndefinedNDPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_undefinedND_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_undefinedND_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__UNDEFINED_ND,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Verteilung feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVerteilungPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_verteilung_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_verteilung_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__VERTEILUNG,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Verteilung G feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVerteilungGPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_verteilungG_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_verteilungG_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__VERTEILUNG_G,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Wert feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWertPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_wert_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_wert_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__WERT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Wert G feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWertGPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_wertG_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_wertG_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__WERT_G,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Wert Typ feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWertTypPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_wertTyp_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_wertTyp_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__WERT_TYP,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Wiederholungen feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWiederholungenPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_wiederholungen_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_wiederholungen_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Wiederholungen G feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWiederholungenGPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_wiederholungenG_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_wiederholungenG_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN_G,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the X feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addXPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DoubleKennzahlen_x_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_DoubleKennzahlen_x_feature", "_UI_DoubleKennzahlen_type"),
				 BfrPackage.Literals.DOUBLE_KENNZAHLEN__X,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		DoubleKennzahlen doubleKennzahlen = (DoubleKennzahlen)object;
		return getString("_UI_DoubleKennzahlen_type") + " " + doubleKennzahlen.getId();
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(DoubleKennzahlen.class)) {
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_X:
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_XG:
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT:
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT_G:
			case BfrPackage.DOUBLE_KENNZAHLEN__ID:
			case BfrPackage.DOUBLE_KENNZAHLEN__LCL95:
			case BfrPackage.DOUBLE_KENNZAHLEN__LCL95_G:
			case BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM:
			case BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM_G:
			case BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN:
			case BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN_G:
			case BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM:
			case BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM_G:
			case BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG:
			case BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG_G:
			case BfrPackage.DOUBLE_KENNZAHLEN__TIMES10_POWER_OF:
			case BfrPackage.DOUBLE_KENNZAHLEN__UCL95:
			case BfrPackage.DOUBLE_KENNZAHLEN__UCL95_G:
			case BfrPackage.DOUBLE_KENNZAHLEN__UNDEFINED_ND:
			case BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG:
			case BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG_G:
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT:
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT_G:
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT_TYP:
			case BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN:
			case BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN_G:
			case BfrPackage.DOUBLE_KENNZAHLEN__X:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return BfrEditPlugin.INSTANCE;
	}

}
