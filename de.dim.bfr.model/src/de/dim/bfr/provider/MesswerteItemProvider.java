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
 * $Id: MesswerteItemProvider.java 651 2012-01-24 09:59:12Z sdoerl $
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
import de.dim.bfr.Messwerte;

/**
 * This is the item provider adapter for a {@link de.dim.bfr.Messwerte} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MesswerteItemProvider
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
	public MesswerteItemProvider(AdapterFactory adapterFactory) {
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

			addGeprueftPropertyDescriptor(object);
			addIdPropertyDescriptor(object);
			addKommentarPropertyDescriptor(object);
			addZeitEinheitPropertyDescriptor(object);
			addSonstigesPropertyDescriptor(object);
			addVersuchsbedingungenPropertyDescriptor(object);
			addKonzEinheitPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Geprueft feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGeprueftPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Messwerte_geprueft_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Messwerte_geprueft_feature", "_UI_Messwerte_type"),
				 BfrPackage.Literals.MESSWERTE__GEPRUEFT,
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
				 getString("_UI_Messwerte_id_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Messwerte_id_feature", "_UI_Messwerte_type"),
				 BfrPackage.Literals.MESSWERTE__ID,
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
				 getString("_UI_Messwerte_kommentar_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Messwerte_kommentar_feature", "_UI_Messwerte_type"),
				 BfrPackage.Literals.MESSWERTE__KOMMENTAR,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Zeit Einheit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addZeitEinheitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Messwerte_zeitEinheit_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Messwerte_zeitEinheit_feature", "_UI_Messwerte_type"),
				 BfrPackage.Literals.MESSWERTE__ZEIT_EINHEIT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Sonstiges feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSonstigesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Messwerte_sonstiges_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Messwerte_sonstiges_feature", "_UI_Messwerte_type"),
				 BfrPackage.Literals.MESSWERTE__SONSTIGES,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Versuchsbedingungen feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVersuchsbedingungenPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Messwerte_versuchsbedingungen_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Messwerte_versuchsbedingungen_feature", "_UI_Messwerte_type"),
				 BfrPackage.Literals.MESSWERTE__VERSUCHSBEDINGUNGEN,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Konz Einheit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addKonzEinheitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Messwerte_konzEinheit_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Messwerte_konzEinheit_feature", "_UI_Messwerte_type"),
				 BfrPackage.Literals.MESSWERTE__KONZ_EINHEIT,
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
			childrenFeatures.add(BfrPackage.Literals.MESSWERTE__AW);
			childrenFeatures.add(BfrPackage.Literals.MESSWERTE__CO2);
			childrenFeatures.add(BfrPackage.Literals.MESSWERTE__DRUCK);
			childrenFeatures.add(BfrPackage.Literals.MESSWERTE__TEMPERATUR);
			childrenFeatures.add(BfrPackage.Literals.MESSWERTE__PH);
			childrenFeatures.add(BfrPackage.Literals.MESSWERTE__KONZENTRATION);
			childrenFeatures.add(BfrPackage.Literals.MESSWERTE__ZEIT);
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
		Messwerte messwerte = (Messwerte)object;
		return getString("_UI_Messwerte_type") + " " + messwerte.getId();
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

		switch (notification.getFeatureID(Messwerte.class)) {
			case BfrPackage.MESSWERTE__GEPRUEFT:
			case BfrPackage.MESSWERTE__ID:
			case BfrPackage.MESSWERTE__KOMMENTAR:
			case BfrPackage.MESSWERTE__ZEIT_EINHEIT:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case BfrPackage.MESSWERTE__AW:
			case BfrPackage.MESSWERTE__CO2:
			case BfrPackage.MESSWERTE__DRUCK:
			case BfrPackage.MESSWERTE__TEMPERATUR:
			case BfrPackage.MESSWERTE__PH:
			case BfrPackage.MESSWERTE__KONZENTRATION:
			case BfrPackage.MESSWERTE__ZEIT:
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
				(BfrPackage.Literals.MESSWERTE__AW,
				 BfrFactory.eINSTANCE.createDoubleKennzahlen()));

		newChildDescriptors.add
			(createChildParameter
				(BfrPackage.Literals.MESSWERTE__CO2,
				 BfrFactory.eINSTANCE.createDoubleKennzahlen()));

		newChildDescriptors.add
			(createChildParameter
				(BfrPackage.Literals.MESSWERTE__DRUCK,
				 BfrFactory.eINSTANCE.createDoubleKennzahlen()));

		newChildDescriptors.add
			(createChildParameter
				(BfrPackage.Literals.MESSWERTE__TEMPERATUR,
				 BfrFactory.eINSTANCE.createDoubleKennzahlen()));

		newChildDescriptors.add
			(createChildParameter
				(BfrPackage.Literals.MESSWERTE__PH,
				 BfrFactory.eINSTANCE.createDoubleKennzahlen()));

		newChildDescriptors.add
			(createChildParameter
				(BfrPackage.Literals.MESSWERTE__KONZENTRATION,
				 BfrFactory.eINSTANCE.createDoubleKennzahlen()));

		newChildDescriptors.add
			(createChildParameter
				(BfrPackage.Literals.MESSWERTE__ZEIT,
				 BfrFactory.eINSTANCE.createDoubleKennzahlen()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == BfrPackage.Literals.MESSWERTE__AW ||
			childFeature == BfrPackage.Literals.MESSWERTE__CO2 ||
			childFeature == BfrPackage.Literals.MESSWERTE__DRUCK ||
			childFeature == BfrPackage.Literals.MESSWERTE__TEMPERATUR ||
			childFeature == BfrPackage.Literals.MESSWERTE__PH ||
			childFeature == BfrPackage.Literals.MESSWERTE__KONZENTRATION ||
			childFeature == BfrPackage.Literals.MESSWERTE__ZEIT;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
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
