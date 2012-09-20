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
package de.dim.bfr.jpa.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Query;

import de.dim.bfr.BfrFactory;
import de.dim.bfr.DoubleKennzahlen;
import de.dim.bfr.Einheiten;
import de.dim.bfr.GeschModelList;
import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Literatur;
import de.dim.bfr.LiteraturListe;
import de.dim.bfr.Messwerte;
import de.dim.bfr.ParameterCovCor;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellKatalog;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingung;
import de.dim.bfr.VersuchsBedingungList;
import de.dim.bfr.jpa.entities.JPADoubleKennzahlen;
import de.dim.bfr.jpa.entities.JPAEinheiten;
import de.dim.bfr.jpa.entities.JPAGeschaetzteModelle;
import de.dim.bfr.jpa.entities.JPAGeschaetzteParameter;
import de.dim.bfr.jpa.entities.JPALiteratur;
import de.dim.bfr.jpa.entities.JPAMesswerte;
import de.dim.bfr.jpa.entities.JPAModell;
import de.dim.bfr.jpa.entities.JPAModellParameter;
import de.dim.bfr.jpa.entities.JPAParameterCovCor;
import de.dim.bfr.jpa.entities.JPAVersuchsbedingungen;
import de.dim.bfr.jpa.internal.BFRJPAActivator;
import de.dim.bfr.jpa.service.mapping.MapDoubleKennzahlen;
import de.dim.bfr.jpa.service.mapping.MapEinheiten;
import de.dim.bfr.jpa.service.mapping.MapGeschModell;
import de.dim.bfr.jpa.service.mapping.MapGeschModellParameter;
import de.dim.bfr.jpa.service.mapping.MapLiteratur;
import de.dim.bfr.jpa.service.mapping.MapMesswerte;
import de.dim.bfr.jpa.service.mapping.MapStatistikModel;
import de.dim.bfr.jpa.service.mapping.MapStatistikModellParameter;
import de.dim.bfr.jpa.service.mapping.MapVersuchsbedingung;
import de.dim.bfr.ui.services.BFRUIService;

/**
 * @author sebastian doerl
 * 
 */
public class PersistenceManager {

	private static final PersistenceManager instance = new PersistenceManager();
	private EntityTransaction tx;
	private Map<Connection, EntityManager> activeConnections = new HashMap<Connection, EntityManager>();

	public PersistenceManager() {

	}

	/**
	 * Returns an instance of {@link LiteraturListe} containing all entries of
	 * the Table <tt>Literatur</tt>
	 * 
	 * @return an instance of {@link LiteraturListe}
	 */
	public LiteraturListe getLiteraturList() {
		EntityManager em = getNewEntityManager();
		MapLiteratur map = new MapLiteratur();
		LiteraturListe literaturListe = BfrFactory.eINSTANCE
				.createLiteraturListe();
		Query q = em.createQuery("select l from JPALiteratur l",
				JPALiteratur.class);
		@SuppressWarnings("unchecked")
		final List<JPALiteratur> jpaLiteraturList = q.getResultList();
		for (JPALiteratur l : jpaLiteraturList) {
			Literatur literatur = map.jpaToEMF(l);
			literaturListe.getLiteratur().add(literatur);
		}
		em.close();
		return literaturListe;
	}

	/**
	 * @return a new instance of the {@link EntityManager}
	 */
	private EntityManager getNewEntityManager() {
		BFRJPAActivator def = BFRJPAActivator.getDefault();
		EntityManagerFactory emf = def.getEntityManagerFactory();
		return emf == null ? null : emf.createEntityManager();
	}

	/**
	 * Get an instance of {@link Literatur} by specifying an Id
	 * 
	 * @param id
	 *            the id of the {@link Literatur} object
	 * @return an instance of {@link Literatur}
	 */
	public JPALiteratur getLiteraturById(int id) {
		EntityManager em = getNewEntityManager();
		final JPALiteratur jpaLiteratur = em.find(JPALiteratur.class, id);
		em.close();
		return jpaLiteratur;
	}

	/**
	 * @param lit
	 *            the instance of {@link Literatur} to save to the database
	 * @return <code>true</code> if the save was successful, otherwise
	 *         <code>false</code>
	 */
	public boolean saveLiteratur(Literatur lit) {
		EntityManager em = getNewEntityManager();
		MapLiteratur map = new MapLiteratur();
		JPALiteratur jpaLiteratur = null;
		tx = em.getTransaction();
		tx.begin();
		if (lit.getId() == 0) {
			jpaLiteratur = map.createJPALiteraturFromLiteratur(lit);
			em.persist(jpaLiteratur);
		} else {
			jpaLiteratur = em.find(JPALiteratur.class, lit.getId());
			em.merge(map.emfToJPA(lit, jpaLiteratur));
		}
		tx.commit();
		/*
		Integer newID = (lit.getId() == 0) ? getIDAfterInsert(jpaLiteratur.getId(), "Literatur") : jpaLiteratur.getId();
		lit.setId(newID);
		*/
		lit.setId(jpaLiteratur.getId());
		em.close();
		return true;
	}
	/*
	private Integer getIDAfterInsert(Integer id, String tablename) { // old Statup. jetzt neu! wegen der Trigger!
		Integer result = id;
	    try {
	    	Statement stmt = instance.getJDBCConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    	ResultSet rs = stmt.executeQuery("SELECT \"Tabelle\", \"TabellenID\" FROM \"ChangeLog\" WHERE \"ID\" = " + id);
	    	if (rs != null && rs.first()) {
		    	if (tablename.equals(rs.getString("Tabelle"))) {
		    		result = rs.getInt("TabellenID");
		    	}
		    	rs.close();
	    	}
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
		return result;
	}
*/
	/**
	 * @param lit
	 *            the instance of {@link Literatur} to delete from database
	 * @return <code>true</code> if the deletion was successful, otherwise
	 *         <code>false</code>
	 */
	public boolean deleteLiteratur(Literatur lit) {
		EntityManager em = getNewEntityManager();
		JPALiteratur jpaLiteratur = em.find(JPALiteratur.class, lit.getId());
		if (!(jpaLiteratur.getId() == 0)) {
			tx = em.getTransaction();
			tx.begin();
			em.remove(jpaLiteratur);
			tx.commit();
		}
		em.close();
		return true;
	}

	/**
	 * Returns an instance of {@link StatistikModellKatalog} which contains all
	 * entries of the corresponding database table
	 * 
	 * @return an instance of {@link StatistikModellKatalog}
	 */
	public StatistikModellKatalog getStatistikModellkatalog() {
		EntityManager em = getNewEntityManager();
		MapStatistikModel map = new MapStatistikModel();
		StatistikModell modell;
		StatistikModellKatalog modellKatalog = BfrFactory.eINSTANCE
				.createStatistikModellKatalog();
		Query q = em.createQuery("select m from JPAModell m", JPAModell.class);
		@SuppressWarnings("unchecked")
		List<JPAModell> modellListe = q.getResultList();
		for (JPAModell jpaModellkatalog : modellListe) {
			modell = map.jpaToEMF(jpaModellkatalog);
			modellKatalog.getModelle().add(modell);
		}
		em.close();
		return modellKatalog;
	}

	/**
	 * @param model
	 *            the instance of {@link StatistikModell} to save to database
	 * @return <code>true</code> if save was successful, otherwise
	 *         <code>false</code>
	 */
	public boolean saveStatisticModell(StatistikModell model) {
		EntityManager em = getNewEntityManager();
		MapStatistikModel map = new MapStatistikModel();
		JPAModell jpaModel = null;
		tx = em.getTransaction();
		tx.begin();
		if (model.getId() == 0) {
			jpaModel = map.createJPAModellFromEMFObj(model);
			em.persist(jpaModel);
		} else {
			jpaModel = em.find(JPAModell.class, model.getId());
			em.merge(map.emfToJPA(model, jpaModel));
		}
		HashMap<Literatur, JPALiteratur> literaturMap = saveModelLiteratur(
				jpaModel, model);
		HashMap<StatistikModellParameter, JPAModellParameter> paramMap = saveModelParams(
				jpaModel, model);
		tx.commit();
		/*
		Integer newID = (model.getId() == 0) ? getIDAfterInsert(jpaModel.getId(), "Modellkatalog") : jpaModel.getId();
		model.setId(newID);
		*/
		model.setId(jpaModel.getId());
		for (Map.Entry<StatistikModellParameter, JPAModellParameter> e : paramMap.entrySet()) {
			/*
			newID = (e.getKey().getId() == 0) ? getIDAfterInsert(e.getValue().getId(), "ModellkatalogParameter") : e.getValue().getId();
			e.getKey().setId(newID);
			*/
			e.getKey().setId(e.getValue().getId());
		}
		for (Map.Entry<Literatur, JPALiteratur> e : literaturMap.entrySet()) {
			/*
			newID = (e.getKey().getId() == 0) ? getIDAfterInsert(e.getValue().getId(), "Literatur") : e.getValue().getId();
			e.getKey().setId(newID);
			*/
			e.getKey().setId(e.getValue().getId());
		}
		em.close();
		return true;
	}

	/**
	 * Converts {@link Literatur} objects to {@link JPALiteratur} objects and
	 * sets them to a {@link JPAModell}. The corresponding literature objects
	 * are returned as a {@link HashMap} for post-transaction purposes.
	 * 
	 * @param jpaModel
	 *            the {@link JPAModell} object to set the literature to
	 * @param model
	 *            the {@link StatistikModell} to get the literature from
	 * @return a {@link HashMap}<{@link Literatur}, {@link JPALiteratur}>
	 */
	private HashMap<Literatur, JPALiteratur> saveModelLiteratur(
			JPAModell jpaModel, StatistikModell model) {
		HashMap<Literatur, JPALiteratur> literaturMap = new HashMap<Literatur, JPALiteratur>();
		List<JPALiteratur> jpaLiteraturToUnset = new ArrayList<JPALiteratur>();
		if (jpaModel.getLiteratur() != null)
			jpaLiteraturToUnset.addAll(jpaModel.getLiteratur());
		for (Literatur e : model.getLiteratur()) {
			JPALiteratur jpaLiteratur = takeFromLiteraturList(e.getId(),
					jpaLiteraturToUnset);
			if (jpaLiteratur == null)
				jpaLiteratur = getLiteraturById(e.getId());
			literaturMap.put(e, jpaLiteratur);
			jpaModel.addLiteratur(jpaLiteratur);
		}
		for (JPALiteratur e : jpaLiteraturToUnset) {
			jpaModel.removeLiteratur(e);
		}
		return literaturMap;
	}

	/**
	 * Auxiliary method which returns an instance of {@link JPALiteratur} that
	 * has an corresponding {@link Literatur} instance.
	 * 
	 * @param id
	 *            of an {@link Literatur} instance
	 * @param jpaLiteraturToUnset
	 * @return an instance of {@link JPALiteratur}, if there is a corresponding
	 *         {@link Literatur} instance, otherwise <code>null</code>
	 */
	private JPALiteratur takeFromLiteraturList(int id,
			List<JPALiteratur> jpaLiteraturToUnset) {
		for (JPALiteratur jpaLiteratur : jpaLiteraturToUnset) {
			if (jpaLiteratur.getId().equals(id)) {
				jpaLiteraturToUnset.remove(jpaLiteratur);
				return jpaLiteratur;
			}
		}
		return null;
	}

	/**
	 * Converts {@link StatistikModellParameter}s to {@link JPAModellParameter}s
	 * and sets them to a {@link JPAModell}. The corresponding Parameters are
	 * returned as a {@link HashMap} for post-transaction purposes.
	 * 
	 * @param jpaModel
	 *            the {@link JPAModell} object to set the Parameters to
	 * @param model
	 *            the the {@link StatistikModell} to get the Parameters from
	 * @return a {@link HashMap}<{@link StatistikModellParameter},
	 *         {@link JPAModellParameter}>
	 */
	private HashMap<StatistikModellParameter, JPAModellParameter> saveModelParams(
			JPAModell jpaModel, StatistikModell model) {
		MapStatistikModellParameter map = new MapStatistikModellParameter();
		List<JPAModellParameter> jpaParamsToDelete = new ArrayList<JPAModellParameter>();
		HashMap<StatistikModellParameter, JPAModellParameter> paramMap = new HashMap<StatistikModellParameter, JPAModellParameter>();
		List<JPAModellParameter> paramList = new ArrayList<JPAModellParameter>();
		jpaParamsToDelete.addAll(jpaModel.getParameters());
		for (StatistikModellParameter e : model.getParameter()) {
			JPAModellParameter jpaParam = takeFromParamList(e.getId(),
					jpaParamsToDelete);
			if (jpaParam == null)
				jpaParam = new JPAModellParameter();
			jpaParam = map.emfToJPA(e, jpaParam);
			paramMap.put(e, jpaParam);
			jpaParam.setModell(jpaModel);
			paramList.add(jpaParam);
		}
		for (JPAModellParameter param : jpaParamsToDelete) {
			jpaModel.getParameters().remove(param);
		}
		jpaModel.setParameters(paramList);
		return paramMap;
	}

	/**
	 * Auxiliary method which returns an instance of {@link JPAModellParameter}
	 * that has a corresponding {@link StatistikModellParameter} instance
	 * 
	 * @param id
	 *            of an {@link StatistikModellParameter} instance
	 * @param jpaParamsToDelete
	 * @return an instance of {@link JPAModellParameter}, if there is a
	 *         corresponding {@link StatistikModellParameter} instance,
	 *         otherwise <code>null</code>
	 */
	private JPAModellParameter takeFromParamList(int id,
			List<JPAModellParameter> jpaParamsToDelete) {

		for (JPAModellParameter param : jpaParamsToDelete) {
			if (param.getId().equals(id)) {
				jpaParamsToDelete.remove(param);
				return param;
			}
		}
		return null;
	}

	/**
	 * @param parameter
	 *            the {@link StatistikModellParameter} to delete from database
	 * @return <code>true</code> if deletion was successful, otherwise
	 *         <code>false</code>
	 */
	public boolean deleteStatistikModelParameter(
			StatistikModellParameter parameter) {
		EntityManager em = getNewEntityManager();
		JPAModellParameter jpaParameter = em.find(JPAModellParameter.class,
				parameter.getId());

		if (!(parameter.getId() == 0)) {
			tx = em.getTransaction();
			tx.begin();
			em.remove(jpaParameter);
			tx.commit();
		}
		em.close();
		return true;
	}

	/**
	 * @param model
	 *            the instance of {@link StatistikModell} to delete from
	 *            database
	 * @return <code>true</code> if deletion was successful, otherwise
	 *         <code>false</code>
	 */
	public boolean deleteStatistikModel(StatistikModell model) {
		EntityManager em = getNewEntityManager();
		JPAModell jpaModell = em.find(JPAModell.class, model.getId());
		if (!(model.getId() == 0)) {
			tx = em.getTransaction();
			tx.begin();
			em.remove(jpaModell);
			tx.commit();
		}
		em.close();
		return true;
	}

	/**
	 * Returns an instance of {@link GeschModelList} containing all entries of
	 * the table <tt>GeschaetzteModelle</tt>
	 * 
	 * @return {@link GeschModelList}
	 */
	public GeschModelList getGeschModellList() {
		EntityManager em = getNewEntityManager();
		MapGeschModell map = new MapGeschModell();
		MapGeschModellParameter mapGeschParam = new MapGeschModellParameter();
		GeschaetztStatistikModell model;
		GeschModelList modelList = BfrFactory.eINSTANCE.createGeschModelList();
		Query q = em.createQuery("select m from JPAGeschaetzteModelle m");
		@SuppressWarnings("unchecked")
		List<JPAGeschaetzteModelle> jpaModelList = q.getResultList();
		for (JPAGeschaetzteModelle jpaModel : jpaModelList) {
			model = map.jpaToEMF(jpaModel);
			for (JPAParameterCovCor e : jpaModel
					.getGeschaetzteParameterCovCors()) {
				ParameterCovCor pcc = copyEntityPropertiesToParamCovCor(e);
				pcc.setParameter1(mapGeschParam.jpaToEMF(e.getParam1(), model));
				pcc.setParameter2(mapGeschParam.jpaToEMF(e.getParam2(), model));
				model.getParameterCovCor().add(pcc);
			}
			modelList.getModels().add(model);
		}
		em.close();
		return modelList;
	}

	/**
	 * @param geschModel
	 *            the instance of {@link GeschaetztStatistikModell} to save to
	 *            database
	 * @return <code>true</code> if save was successful, otherwise
	 *         <code>false</code>
	 */
	public boolean saveGeschaetztStatistikModell(
			GeschaetztStatistikModell geschModel) {
		StatistikModell model = geschModel.getStatistikModel();
		EntityManager em = getNewEntityManager();
		MapGeschModell map = new MapGeschModell();
		JPAGeschaetzteModelle jpaGeschModel = null;
		tx = em.getTransaction();
		tx.begin();
		if (geschModel.getId() == 0) {
			jpaGeschModel = map.createJPAEstimatedModelFromEMFObj(geschModel);
			em.persist(jpaGeschModel);
			setCondition(jpaGeschModel, geschModel);
			// avoid a new condition
			em.merge(jpaGeschModel);
		} else {
			jpaGeschModel = em.find(JPAGeschaetzteModelle.class,
					geschModel.getId());
			jpaGeschModel = map.emfToJPA(geschModel, jpaGeschModel);
			setCondition(jpaGeschModel, geschModel);
			em.merge(jpaGeschModel);
		}
		JPAModell test = getJPAModelById(model.getId(), em);
		jpaGeschModel.setModell(test);
		if (geschModel.getResponse() != null)
			for (JPAModellParameter p : jpaGeschModel.getModell().getParameters())
				if (p.getId().equals(geschModel.getResponse().getId()))
					jpaGeschModel.setResponseParameter(p);
		HashMap<Literatur, JPALiteratur> literaturMap = saveGeschModelLiteratur(
				jpaGeschModel, geschModel);
		HashMap<GeschModellParameter, JPAGeschaetzteParameter> geschParamMap = saveGeschModelParams(
				jpaGeschModel, geschModel, em);
		HashMap<ParameterCovCor, JPAParameterCovCor> pccMap = saveParamCovCor(
				jpaGeschModel, geschModel, geschParamMap);
		tx.commit();
		Integer newID;
		for (Map.Entry<GeschModellParameter, JPAGeschaetzteParameter> e : geschParamMap.entrySet()) {
			/*
			newID = (e.getKey().getId() == 0) ? getIDAfterInsert(e.getValue().getId(), "GeschaetzteParameter") : e.getValue().getId();
			e.getKey().setId(newID);
			*/
			e.getKey().setId(e.getValue().getId());
		}
		for (Map.Entry<ParameterCovCor, JPAParameterCovCor> e : pccMap.entrySet()) {
			/*
			newID = (e.getKey().getId() == 0) ? getIDAfterInsert(e.getValue().getId(), "GeschaetzteParameterCovCor") : e.getValue().getId();
			e.getKey().setId(newID);
			*/
			e.getKey().setId(e.getValue().getId());
		}
		for (Map.Entry<Literatur, JPALiteratur> e : literaturMap.entrySet()) {
			/*
			newID = (e.getKey().getId() == 0) ? getIDAfterInsert(e.getValue().getId(), "Literatur") : e.getValue().getId();
			e.getKey().setId(newID);
			*/
			e.getKey().setId(e.getValue().getId());
		}
		/*
		newID = (geschModel.getId() == 0) ? getIDAfterInsert(jpaGeschModel.getId(), "GeschaetzteModelle") : jpaGeschModel.getId();
		geschModel.setId(newID);
		*/
		geschModel.setId(jpaGeschModel.getId());
		em.close();
		return true;
	}

	/**
	 * Set bidirectional relationship between {@link JPAGeschaetzteModelle} and {@link JPAVersuchsbedingungen}
	 * @param jpaGeschModel the {@link JPAGeschaetzteModelle} object
	 * @param geschModel the {@link JPAVersuchsbedingungen} object
	 */
	private void setCondition(JPAGeschaetzteModelle jpaGeschModel, GeschaetztStatistikModell geschModel) {
		if (geschModel.getBedingung() != null) {
			int id = geschModel.getBedingung().getId();
			JPAVersuchsbedingungen jpaVersuchsbedingungById = getJPAVersuchsbedingungById(id);
			jpaGeschModel
					.setVersuchsbedingung(jpaVersuchsbedingungById);
			jpaVersuchsbedingungById.getGeschaetzteModelle().add(jpaGeschModel);
		}
	}

	/**
	 * Converts {@link Literatur} objects to {@link JPALiteratur} objects and
	 * sets them to a {@link JPAGeschaetzteModelle}. The corresponding
	 * literature objects are returned as a {@link HashMap} for post-transaction
	 * purposes.
	 * 
	 * @param jpaModel
	 *            the {@link JPAGeschaetzteModelle} object to set the literature
	 *            to
	 * @param model
	 *            the {@link GeschaetztStatistikModell} to get the literature
	 *            from
	 * @return a {@link HashMap}<{@link Literatur}, {@link JPALiteratur}>
	 */
	private HashMap<Literatur, JPALiteratur> saveGeschModelLiteratur(
			JPAGeschaetzteModelle jpaGeschModel,
			GeschaetztStatistikModell geschModel) {
		HashMap<Literatur, JPALiteratur> literaturMap = new HashMap<Literatur, JPALiteratur>();
		List<JPALiteratur> jpaLiteraturToUnset = new ArrayList<JPALiteratur>();
		if (jpaGeschModel.getLiteratur() != null)
			jpaLiteraturToUnset.addAll(jpaGeschModel.getLiteratur());
		for (Literatur e : geschModel.getLiteratur()) {
			JPALiteratur jpaLiteratur = takeFromLiteraturList(e.getId(),
					jpaLiteraturToUnset);
			if (jpaLiteratur == null)
				jpaLiteratur = getLiteraturById(e.getId());
			literaturMap.put(e, jpaLiteratur);
			jpaGeschModel.addLiteratur(jpaLiteratur);
		}
		for (JPALiteratur e : jpaLiteraturToUnset) {
			jpaGeschModel.removeLiteratur(e);
		}
		return literaturMap;
	}

	/**
	 * Converts {@link GeschModellParameter}s to {@link JPAGeschaetzteParameter}
	 * s and sets them to a {@link JPAGeschaetzteModelle}. The corresponding
	 * Parameters are returned as a {@link HashMap} for post-transaction
	 * purposes.
	 * 
	 * @param jpaModel
	 *            the {@link JPAGeschaetzteModelle} object to set the Parameters
	 *            to
	 * @param model
	 *            the the {@link GeschaetztStatistikModell} to get the
	 *            Parameters from
	 * @return a {@link HashMap}<{@link GeschaetztStatistikModell},
	 *         {@link JPAGeschaetzteParameter}>
	 */
	private HashMap<GeschModellParameter, JPAGeschaetzteParameter> saveGeschModelParams(
			JPAGeschaetzteModelle jpaGeschModel,
			GeschaetztStatistikModell geschModel, EntityManager em) {
		MapGeschModellParameter map = new MapGeschModellParameter();
		HashMap<GeschModellParameter, JPAGeschaetzteParameter> geschParamMap = new HashMap<GeschModellParameter, JPAGeschaetzteParameter>();
		List<JPAGeschaetzteParameter> paramsToDelete = new ArrayList<JPAGeschaetzteParameter>();
		List<JPAGeschaetzteParameter> paramsToSet = new ArrayList<JPAGeschaetzteParameter>();
		Collection<JPAModellParameter> jpaModelParams = jpaGeschModel.getModell().getParameters();
		paramsToDelete.addAll(jpaGeschModel.getGeschaetzteParameter());
		for (GeschModellParameter e : geschModel.getParameter()) {
			JPAGeschaetzteParameter jpaGeschParam = takeFromGeschParamList(
					e.getId(), paramsToDelete);
			if (jpaGeschParam == null)
				jpaGeschParam = new JPAGeschaetzteParameter();
			jpaGeschParam = map.emfToJPA(e, jpaGeschParam);
			if (jpaGeschParam.getId() != null) {
				for (JPAModellParameter p : jpaModelParams) {
					if (p.getId().equals(jpaGeschParam.getId()))
						jpaGeschParam.setModellParameter(p);
				}
			} else {
				jpaGeschParam.setModellParameter(getJPAModellParameterById(e.getModelParameter().getId(), em));
			}
			geschParamMap.put(e, jpaGeschParam);
			jpaGeschParam.setGeschaetztesModell(jpaGeschModel);
			if (jpaGeschModel.getGeschaetzteParameter() != null
					&& !jpaGeschModel.getGeschaetzteParameter().contains(
							jpaGeschParam))
				jpaGeschModel.getGeschaetzteParameter().add(jpaGeschParam);
			paramsToSet.add(jpaGeschParam);
		}

		for (JPAGeschaetzteParameter param : paramsToDelete) {
			for (ParameterCovCor covCor : geschModel.getParameterCovCor()) {
				if (covCor.getParameter1().getId() == param.getId()
						|| covCor.getParameter2().getId() == param.getId()) {
					geschModel.getParameterCovCor().remove(covCor);
				}
			}
		}

		if (jpaGeschModel.getGeschaetzteParameter() == null) {
			jpaGeschModel.setGeschaetzteParameter(paramsToSet);
		}

		return geschParamMap;
	}

	/**
	 * Converts {@link ParameterCovCor}s to {@link JPAParameterCovCor}s and sets
	 * them to a {@link JPAGeschaetzteModelle}. The corresponding Parameters are
	 * returned as a {@link HashMap} for post-transaction purposes.
	 * 
	 * @param geschParamMap
	 * 
	 * @param jpaModel
	 *            the {@link JPAGeschaetzteModelle} object to set the Parameters
	 *            to
	 * @param model
	 *            the the {@link GeschaetztStatistikModell} to get the
	 *            Parameters from
	 * @return a {@link HashMap}<{@link ParameterCovCor},
	 *         {@link JPAParameterCovCor}>
	 */
	private HashMap<ParameterCovCor, JPAParameterCovCor> saveParamCovCor(
			JPAGeschaetzteModelle jpaGeschModel,
			GeschaetztStatistikModell geschModel,
			HashMap<GeschModellParameter, JPAGeschaetzteParameter> geschParamMap) {
		HashMap<ParameterCovCor, JPAParameterCovCor> pccMap = new HashMap<ParameterCovCor, JPAParameterCovCor>();
		List<JPAParameterCovCor> pCCDeleteList = new ArrayList<JPAParameterCovCor>();
		List<JPAParameterCovCor> pccList = new ArrayList<JPAParameterCovCor>();
		pCCDeleteList.addAll(jpaGeschModel.getGeschaetzteParameterCovCors());
		// get estimated params to set param1 and param2 for each paramcovcor
		
		for (ParameterCovCor e : geschModel.getParameterCovCor()) {
			JPAParameterCovCor jpaPCC = takeFromPCCDeleteList(e.getId(),
					pCCDeleteList);
			if (jpaPCC == null)
				jpaPCC = new JPAParameterCovCor();
			jpaPCC = copyParamCovCorPropertiesToEntity(e, jpaPCC);

			if (geschParamMap.get(e.getParameter1()) != null) //in case of editing
				jpaPCC.setParam1(geschParamMap.get(e.getParameter1()));
			if (geschParamMap.get(e.getParameter2()) != null) //in case of editing
				jpaPCC.setParam2(geschParamMap.get(e.getParameter2()));
			pccList.add(jpaPCC);
			pccMap.put(e, jpaPCC);
			jpaPCC.setGeschaetztesModell(jpaGeschModel);
			if (jpaGeschModel.getGeschaetzteParameterCovCors() != null
					&& !jpaGeschModel.getGeschaetzteParameterCovCors()
							.contains(jpaPCC))
				jpaGeschModel.getGeschaetzteParameterCovCors().add(jpaPCC);
		}
		/*
		 * Don't use values of the HashMap here! Eclipselink relies on
		 * <code>clone</code> to create Collections. So it is necessary to use a
		 * Collection that implements Clonable.
		 */
		if (jpaGeschModel.getGeschaetzteParameterCovCors() == null)
			jpaGeschModel.setGeschaetzteParameterCovCors(pccList);
		for (JPAParameterCovCor pcc : pCCDeleteList) {
			jpaGeschModel.getGeschaetzteParameterCovCors().remove(pcc);
		}
		return pccMap;
	}

	/**
	 * Auxiliary method which returns an instance of {@link JPAParameterCovCor}
	 * that has a corresponding {@link ParameterCovCor} instance
	 * 
	 * @param id
	 *            of an {@link ParameterCovCor} instance
	 * @param jpaParamsToDelete
	 * @return an instance of {@link JPAParameterCovCor}, if there is a
	 *         corresponding {@link ParameterCovCor} instance, otherwise
	 *         <code>null</code>
	 */
	private JPAParameterCovCor takeFromPCCDeleteList(int id,
			List<JPAParameterCovCor> pccToDelete) {
		for (JPAParameterCovCor param : pccToDelete) {
			if (param.getId().equals(id)) {
				pccToDelete.remove(param);
				return param;
			}
		}
		return null;
	}

	/**
	 * Returns an instance of {@link JPAModell} that corresponds to a certain Id
	 * 
	 * @param id
	 * @return the {@link JPAModell} instance
	 */
	public JPAModell getJPAModelById(int id) {
		EntityManager em = getNewEntityManager();
		JPAModell model = getJPAModelById(id, em);
		em.close();
		return model;
	}

	private JPAModell getJPAModelById(int id, EntityManager em) {
		return em.find(JPAModell.class, id);
	}

	/**
	 * Auxiliary method which returns an instance of
	 * {@link JPAGeschaetzteParameter} that has a corresponding
	 * {@link GeschModellParameter} instance
	 * 
	 * @param id
	 *            of an {@link GeschModellParameter} instance
	 * @param jpaParamsToDelete
	 * @return an instance of {@link JPAGeschaetzteParameter}, if there is a
	 *         corresponding {@link GeschModellParameter} instance, otherwise
	 *         <code>null</code>
	 */
	private JPAGeschaetzteParameter takeFromGeschParamList(int id,
			List<JPAGeschaetzteParameter> paramsToDelete) {
		for (JPAGeschaetzteParameter param : paramsToDelete) {
			if (param.getId().equals(id)) {
				paramsToDelete.remove(param);
				return param;
			}
		}
		return null;
	}

	/**
	 * @param model
	 *            the instance of {@link GeschaetztStatistikModell} to delete
	 *            from database
	 * @return <code>true</code> if deletion was successful, otherwise
	 *         <code>false</code>
	 */
	public boolean deleteGeschaetztStatistikModell(
			GeschaetztStatistikModell model) {
		EntityManager em = getNewEntityManager();
		JPAGeschaetzteModelle jpaModell = em.find(JPAGeschaetzteModelle.class,
				model.getId());
		if (!(jpaModell.getId() == 0)) {
			tx = em.getTransaction();
			tx.begin();
			em.remove(jpaModell);
			tx.commit();
		}
		em.close();
		return true;
	}

	/**
	 * Returns a {@link List} containing all entries of the database table
	 * <tt>GeschaetzteParameter</tt>
	 * 
	 * @return {@link List}<{@link GeschModellParameter}>
	 */
	public List<GeschModellParameter> getAllGeschModellParameter() {
		EntityManager em = getNewEntityManager();
		MapGeschModellParameter map = new MapGeschModellParameter();
		GeschModellParameter parameter = BfrFactory.eINSTANCE
				.createGeschModellParameter();
		List<GeschModellParameter> paramList = new ArrayList<GeschModellParameter>();
		Query q = em.createQuery("select p from JPAModellParameter p",
				JPAModellParameter.class);
		@SuppressWarnings("unchecked")
		List<JPAGeschaetzteParameter> jpaParamList = q.getResultList();
		for (JPAGeschaetzteParameter jpaParam : jpaParamList) {
			parameter = map.jpaToEMF(jpaParam, null);
			paramList.add(parameter);
		}
		em.close();
		return paramList;
	}

	/**
	 * @param param
	 *            the instance of {@link GeschModellParameter} to delete from
	 *            database
	 * @return <code>true</code> if deletion was successful, otherwise
	 *         <code>false</code>
	 */
	public boolean deleteGeschModellParameter(GeschModellParameter param) {
		EntityManager em = getNewEntityManager();
		JPAGeschaetzteParameter jpaParam = em.find(
				JPAGeschaetzteParameter.class, param.getId());
		if (!(jpaParam.getId() == 0)) {
			tx = em.getTransaction();
			tx.begin();
			em.remove(jpaParam);
			tx.commit();
		}
		em.close();
		return true;
	}

	/**
	 * Returns an instance of {@link VersuchsBedingungList} containing all
	 * entries of the database table <tt>VersuchsBedingungen</tt>
	 * 
	 * @return {@link VersuchsBedingungList}
	 */
	public VersuchsBedingungList getAllVersuchsbedingungen() {
		VersuchsBedingung condition;
		VersuchsBedingungList conditionList = BfrFactory.eINSTANCE
				.createVersuchsBedingungList();
		EntityManager em = getNewEntityManager();
		MapVersuchsbedingung map = new MapVersuchsbedingung();
		Query q = em.createQuery("select v from JPAVersuchsbedingungen v");
		@SuppressWarnings("unchecked")
		List<JPAVersuchsbedingungen> jpaConditionList = q.getResultList();
		for (JPAVersuchsbedingungen jpaCondition : jpaConditionList) {
			condition = map.jpaToEMF(jpaCondition);
			conditionList.getBedingungen().add(condition);
		}
		em.close();
		return conditionList;
	}

	/**
	 * @param condition
	 *            the instance of {@link VersuchsBedingungList} to save to
	 *            database
	 * @return <code>true</code> if save was successful, otherwise
	 *         <code>false</code>
	 */
	public boolean saveVersuchsbedinung(VersuchsBedingung condition) {
		EntityManager em = getNewEntityManager();
		MapVersuchsbedingung map = new MapVersuchsbedingung();
		JPAVersuchsbedingungen jpaCondition = null;
		tx = em.getTransaction();
		tx.begin();
		if (condition.getId() == 0) {
			jpaCondition = map.createJPAVersuchsbedingungFromEMFObj(condition);
			em.persist(jpaCondition);
		} else {
			jpaCondition = em.find(JPAVersuchsbedingungen.class,
					condition.getId());
			em.merge(map.emfToJPA(condition, jpaCondition));
		}
		tx.commit();
		/*
		Integer newID = (condition.getId() == 0) ? getIDAfterInsert(jpaCondition.getId(), "Versuchsbedingungen") : jpaCondition.getId();
		condition.setId(newID);
		*/
		condition.setId(jpaCondition.getId());
		em.close();
		return true;
	}

	/**
	 * @param condition
	 *            the instance of {@link VersuchsBedingungList} to delete from
	 *            database
	 * @return <code>true</code> if deletion was successful, otherwise
	 *         <code>false</code>
	 */
	public boolean deleteVersuchsbedingung(VersuchsBedingung condition) {
		EntityManager em = getNewEntityManager();
		JPAVersuchsbedingungen jpaCondition = em.find(
				JPAVersuchsbedingungen.class, condition.getId());
		if (!(jpaCondition.getId() == 0)) {
			tx = em.getTransaction();
			tx.begin();
			em.remove(jpaCondition);
			tx.commit();
		}
		em.close();
		return true;
	}

	/**
	 * Returns a {@link List} containing all entries of the database table
	 * <tt>DoubleKennzahlen</tt>
	 * 
	 * @return {@link List}<{@link DoubleKennzahlen}>
	 */
	public List<DoubleKennzahlen> getAllDoubleKennzahlen() {
		EntityManager em = getNewEntityManager();
		MapDoubleKennzahlen map = new MapDoubleKennzahlen();
		DoubleKennzahlen values = BfrFactory.eINSTANCE.createDoubleKennzahlen();
		List<DoubleKennzahlen> valuesList = new ArrayList<DoubleKennzahlen>();
		Query q = em.createQuery("select k from JPADoubleKennzahlen k",
				JPADoubleKennzahlen.class);
		@SuppressWarnings("unchecked")
		List<JPADoubleKennzahlen> jpaValList = q.getResultList();
		for (JPADoubleKennzahlen jpaVal : jpaValList) {
			values = map.jpaToEMF(jpaVal);
			valuesList.add(values);
		}
		return valuesList;
	}


	/**
	 * Returns a {@link List} containing all entries of the database table
	 * <tt>Einheiten</tt>
	 * 
	 * @return {@link List}<{@link Einheiten}>
	 */
	public List<Einheiten> getAllEinheiten() {
		EntityManager em = getNewEntityManager();
		MapEinheiten map = new MapEinheiten();
		Einheiten unit = BfrFactory.eINSTANCE.createEinheiten();
		List<Einheiten> units = new ArrayList<Einheiten>();
		Query q = em.createQuery("select u from JPAEinheiten u",
				JPAEinheiten.class);
		@SuppressWarnings("unchecked")
		List<JPAEinheiten> jpaUnits = q.getResultList();
		for (JPAEinheiten jpaUnit : jpaUnits) {
			unit = map.jpaToEMF(jpaUnit);
			units.add(unit);
		}
		return units;
	}

	/**
	 * Returns an instance of {@link StatistikModellParameter} corresponding to
	 * a certain Id
	 * 
	 * @param id
	 * @return instance of {@link StatistikModellParameter}
	 */
	public StatistikModellParameter getStatistikModellParameterById(Integer id) {
		StatistikModellKatalog katalog = BFRJPAActivator.getDefault()
				.getUIService().getAllStatisticModels();
		for (StatistikModell model : katalog.getModelle()) {
			for (StatistikModellParameter param : model.getParameter())
				if (param.getId() == id)
					return param;
		}
		return null;
	}

	private JPAModellParameter getJPAModellParameterById(Integer id,
			EntityManager em) {
		JPAModellParameter param = em.find(JPAModellParameter.class, id);
		return param;
	}

	public StatistikModellParameter getParameterByModelParameterId(
			Integer modelId, Integer paramId) {
		List<StatistikModellParameter> params = BFRJPAActivator.getDefault()
				.getUIService().getStatisticModellById(modelId).getParameter();
		for (StatistikModellParameter param : params)
			if (param.getId() == paramId)
				return param;
		return null;
	}

	/**
	 * Returns an instance of {@link GeschModellParameter} corresponding to a
	 * certain {@link GeschaetztStatistikModell}
	 * 
	 * @param model
	 *            the instance of {@link GeschaetztStatistikModell}
	 * @return the corresponding {@link GeschModellParameter}
	 */
	public Collection<? extends GeschModellParameter> getGeschModellParameterByModelFromDB(
			GeschaetztStatistikModell model) {
		EntityManager em = getNewEntityManager();
		MapGeschModellParameter map = new MapGeschModellParameter();
		JPAGeschaetzteModelle jpaGesModels = em.find(
				JPAGeschaetzteModelle.class, model.getId());
		ArrayList<GeschModellParameter> params = new ArrayList<GeschModellParameter>();
		for (JPAGeschaetzteParameter p : jpaGesModels.getGeschaetzteParameter()) {

			GeschModellParameter param = BfrFactory.eINSTANCE
					.createGeschModellParameter();
			param = map.jpaToEMF(p, model);
			params.add(param);
		}

		return params;
	}

	private JPAParameterCovCor copyParamCovCorPropertiesToEntity(
			ParameterCovCor param, JPAParameterCovCor jpaParam) {
		jpaParam.setCor(param.isCor());
		jpaParam.setWert(param.getValue());
		return jpaParam;
	}

	private ParameterCovCor copyEntityPropertiesToParamCovCor(
			JPAParameterCovCor jpaParam) {
		if (jpaParam == null)
			return null;
		ParameterCovCor param = BfrFactory.eINSTANCE.createParameterCovCor();
		param.setCor(jpaParam.isCor());
		param.setValue(jpaParam.getWert());
		param.setId(jpaParam.getId());
		return param;
	}

	/**
	 * Returns a {@link Collection} of {@link JPAMesswerte} corresponding to a
	 * specific instance of {@link JPAVersuchsbedingungen}
	
	 * @param condition
	 *            the instance of {@link JPAVersuchsbedingungen}
	 * @return {@link Collection}<{@link JPAMesswerte}>
	 */
	public Collection<JPAMesswerte> getJPAMesswerteByVersuchsbedingung(
			JPAVersuchsbedingungen condition) {
		if (condition == null)
			return null;
		Collection<JPAMesswerte> jpaValues = condition.getMesswerte();
		return jpaValues;
	}

	public List<Messwerte> getMesswerteByVersuchsbedingungID(int id) {
		List<Messwerte> messwerte = new ArrayList<Messwerte>();
		MapMesswerte map = new MapMesswerte();
		
		EntityManager em = getNewEntityManager();
		@SuppressWarnings("unchecked")
		Collection<JPAMesswerte> jpa = em.createQuery("SELECT m from JPAMesswerte m where m.versuchsbedingungen.id = ?1").setParameter(1, id).getResultList();
		
//		Collection<JPAMesswerte> jpa = getJPAMesswerteByVersuchsbedingung(getJPAVersuchsbedingungById(id));
		if (jpa == null)
			return null;
		for (JPAMesswerte jpamw : jpa) {
			messwerte.add(map.jpaToEMF(jpamw));
		}
		return messwerte;
	}


	/**
	 * @return instance of {@link PersistenceManager}
	 */
	public static PersistenceManager getInstance() {
		return instance;
	}

	/**
	 * @return <code>true</code> if the {@link EntityManagerFactory} is not
	 *         <code>null</code>, otherwise <code>false</code>
	 */
	public boolean isReady() {
		return BFRJPAActivator.getDefault().getEntityManagerFactory() != null;
	}

	/**
	 * re-reads the database properties of the application
	 */
	public void refresh() {
		BFRJPAActivator.getDefault().refreshEntityManagerFactory();
	}

	/**
	 * @return {@link Connection}
	 */
	public Connection getJDBCConnection() {
		EntityManager em = getNewEntityManager();
		if (em == null)
			return null;
		em.setFlushMode(FlushModeType.AUTO);
		tx = em.getTransaction();
		tx.begin();
		Connection con = em.unwrap(java.sql.Connection.class);
		activeConnections.put(con, em);
		return con;
	}

	/**
	 * @param connection
	 *            the {@link Connection} to be closed
	 */
	public void closeJDBCConnection(Connection connection) {
		closeJDBCConnection(connection, true);
	}
	public void closeJDBCConnection(Connection connection, boolean doClose) {
		EntityManager em = activeConnections.get(connection);
		if (em != null && em.getTransaction() != null && em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
		if (doClose) {
			if (em != null) em.close();
			activeConnections.remove(connection);			
		}
	}

	/**
	 * Returns an instance of {@link GeschaetztStatistikModell} corresponding to
	 * a ceratain Id
	 * 
	 * @param id
	 *            the Id of the object
	 * @return instance of {@link GeschaetztStatistikModell}
	 */
	public GeschaetztStatistikModell getEstimatedModellById(int id) {
		EntityManager em = getNewEntityManager();
		MapGeschModell map = new MapGeschModell();
		final JPAGeschaetzteModelle jpa = em.find(JPAGeschaetzteModelle.class,
				id);
		GeschaetztStatistikModell model = BfrFactory.eINSTANCE
				.createGeschaetztStatistikModell();
		model = map.jpaToEMF(jpa);
		em.close();
		return model;
	}

	public List<GeschaetztStatistikModell> getSecondaryEstModelByPrimaryEstModel(
			GeschaetztStatistikModell primModel) {
		BFRUIService service = new BFRUIServiceImpl();
		MapGeschModell map = new MapGeschModell();
		List<GeschaetztStatistikModell> secModelsList = new ArrayList<GeschaetztStatistikModell>();
		EntityManager em = getNewEntityManager();
		if (primModel == null)
			return null;
		@SuppressWarnings("unused")
		JPAGeschaetzteModelle jpaPrim = em.find(JPAGeschaetzteModelle.class,
				primModel.getId());
		@SuppressWarnings("unchecked")
		List<JPAGeschaetzteModelle> jpaSecModelsList = em.createQuery("SELECT m FROM JPAGeschaetzteModelle m JOIN m.primaermodelle e WHERE e.id = ?1").setParameter(1, primModel.getId()).getResultList();
		jpaSecModelsList.size();
		for (JPAGeschaetzteModelle m : jpaSecModelsList) {
			if (service.getEstimatedStatisticModellById(m.getId()) == null)
				secModelsList.add(map.jpaToEMF(m));
			else
				secModelsList.add(service.getEstimatedStatisticModellById(m
						.getId()));
		}
		return secModelsList;
	}
	
	public List<String> getPrimaryEstModelNamesBySecondaryEstModel(GeschaetztStatistikModell secModel) {
		if (secModel == null)
			return null;
		BFRUIService service = new BFRUIServiceImpl();
		MapGeschModell map = new MapGeschModell();
		List<GeschaetztStatistikModell> primModelsList = new ArrayList<GeschaetztStatistikModell>();
		EntityManager em = getNewEntityManager();
		JPAGeschaetzteModelle jpaSec = em.find(JPAGeschaetzteModelle.class, secModel.getId());
		jpaSec.getPrimaermodelle().size();
		Collection<JPAGeschaetzteModelle> jpaPrimModelsList = new ArrayList<JPAGeschaetzteModelle>();
		jpaPrimModelsList = jpaSec.getPrimaermodelle();
		jpaPrimModelsList.size();
		for (JPAGeschaetzteModelle m : jpaPrimModelsList)
			if (service.getEstimatedStatisticModellById(m.getId()) == null)
				primModelsList.add(map.jpaToEMF(m));
			else
				primModelsList.add(service.getEstimatedStatisticModellById(m.getId()));
		List<String> namesList = new ArrayList<String>();
		for (GeschaetztStatistikModell m : primModelsList) {
			namesList.add(m.getStatistikModel().getName());
		}
		return namesList;
	}

	/**
	 * Returns an instance of {@link JPAVersuchsbedingungen} corresponding to a
	 * certain Id
	 * 
	 * @param id
	 * @return an instance of {@link JPAVersuchsbedingungen}
	 */
	public JPAVersuchsbedingungen getJPAVersuchsbedingungById(int id) {
		EntityManager em = getNewEntityManager();
//		JPAVersuchsbedingungen vb = em.find(JPAVersuchsbedingungen.class, id);
		JPAVersuchsbedingungen vb = (JPAVersuchsbedingungen) em.createQuery("SELECT v FROM JPAVersuchsbedingungen v WHERE v.id = ?1").setParameter(1, id).getResultList().get(0);
		em.close();
		return vb;
	}

}
