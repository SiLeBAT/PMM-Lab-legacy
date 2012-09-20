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
 * $Id: GeschaetztStatistikModellItemProvider.java 651 2012-01-24 09:59:12Z sdoerl $
 */
package de.dim.bfr.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
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

import de.dim.bfr.BfrFactory;
import de.dim.bfr.BfrPackage;
import de.dim.bfr.GeschaetztStatistikModell;

/**
 * This is the item provider adapter for a {@link de.dim.bfr.GeschaetztStatistikModell} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class GeschaetztStatistikModellItemProvider
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
	public GeschaetztStatistikModellItemProvider(AdapterFactory adapterFactory) {
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

			addStatistikModelPropertyDescriptor(object);
			addManuellEingetragenPropertyDescriptor(object);
			addRSquaredPropertyDescriptor(object);
			addRssPropertyDescriptor(object);
			addScorePropertyDescriptor(object);
			addKommentarPropertyDescriptor(object);
			addIdPropertyDescriptor(object);
			addBedingungPropertyDescriptor(object);
			addLiteraturPropertyDescriptor(object);
			addParameterCovCorPropertyDescriptor(object);
			addResponsePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Statistik Model feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStatistikModelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GeschaetztStatistikModell_statistikModel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GeschaetztStatistikModell_statistikModel_feature", "_UI_GeschaetztStatistikModell_type"),
				 BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Manuell Eingetragen feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addManuellEingetragenPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GeschaetztStatistikModell_manuellEingetragen_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GeschaetztStatistikModell_manuellEingetragen_feature", "_UI_GeschaetztStatistikModell_type"),
				 BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the RSquared feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRSquaredPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GeschaetztStatistikModell_rSquared_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GeschaetztStatistikModell_rSquared_feature", "_UI_GeschaetztStatistikModell_type"),
				 BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__RSQUARED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Rss feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRssPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GeschaetztStatistikModell_rss_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GeschaetztStatistikModell_rss_feature", "_UI_GeschaetztStatistikModell_type"),
				 BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__RSS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Score feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addScorePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GeschaetztStatistikModell_score_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GeschaetztStatistikModell_score_feature", "_UI_GeschaetztStatistikModell_type"),
				 BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__SCORE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Kommentar feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addKommentarPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GeschaetztStatistikModell_kommentar_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GeschaetztStatistikModell_kommentar_feature", "_UI_GeschaetztStatistikModell_type"),
				 BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__KOMMENTAR,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
				 getString("_UI_GeschaetztStatistikModell_id_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GeschaetztStatistikModell_id_feature", "_UI_GeschaetztStatistikModell_type"),
				 BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__ID,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Bedingung feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBedingungPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GeschaetztStatistikModell_bedingung_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GeschaetztStatistikModell_bedingung_feature", "_UI_GeschaetztStatistikModell_type"),
				 BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__BEDINGUNG,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Literatur feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLiteraturPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GeschaetztStatistikModell_literatur_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GeschaetztStatistikModell_literatur_feature", "_UI_GeschaetztStatistikModell_type"),
				 BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__LITERATUR,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Parameter Cov Cor feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addParameterCovCorPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GeschaetztStatistikModell_parameterCovCor_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GeschaetztStatistikModell_parameterCovCor_feature", "_UI_GeschaetztStatistikModell_type"),
				 BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Response feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addResponsePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GeschaetztStatistikModell_response_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GeschaetztStatistikModell_response_feature", "_UI_GeschaetztStatistikModell_type"),
				 BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__RESPONSE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__PARAMETER);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		GeschaetztStatistikModell geschaetztStatistikModell = (GeschaetztStatistikModell)object;
		return getString("_UI_GeschaetztStatistikModell_type") + " " + geschaetztStatistikModell.getId();
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

		switch (notification.getFeatureID(GeschaetztStatistikModell.class)) {
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN:
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSQUARED:
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSS:
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__SCORE:
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__KOMMENTAR:
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__ID:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
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

		newChildDescriptors.add
			(createChildParameter
				(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__PARAMETER,
				 BfrFactory.eINSTANCE.createGeschModellParameter()));
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
