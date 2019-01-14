/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto sean aprobadas por laComisi�n Europea- versiones posteriores de la EUPL (la �Licencia�); 
 * Solo podr� usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
 * V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.businessobject;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Order;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.ieci.tecdoc.common.exception.AttributesException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrTypeadm;
import com.ieci.tecdoc.common.utils.EntityByLanguage;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.Validator;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.msssi.sgm.registropresencial.beans.QueryCompactSearchOrg;
import es.msssi.sgm.registropresencial.beans.SearchUnitsBean;
import es.msssi.sgm.registropresencial.beans.UnitsResultsBean;
import es.msssi.sgm.registropresencial.beans.ibatis.DirOrgs;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.UnidadTramitadoraDAO;
import es.msssi.sgm.registropresencial.daos.UnitsDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase q implementa IGenericBo que contiene los m�todos relacionados con las
 * unidades.
 * 
 * @author cmorenog
 * 
 */
public class UnitsBo implements IGenericBo {
	
	private static final Logger LOG = Logger.getLogger(UnitsBo.class.getName());
	
	private static ApplicationContext appContext;
	private static UnidadTramitadoraDAO unidadTramitadoraDAO;
	private static UnitsDAO unitsDAO;

	public UnitsBo() {
		if (appContext == null) {
			appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();
		}
		if (unidadTramitadoraDAO == null) {
			unidadTramitadoraDAO = (UnidadTramitadoraDAO) appContext.getBean("unidadTramitadoraDAO");
		}
		if (unitsDAO == null) {
			unitsDAO = (UnitsDAO) appContext.getBean("unitsDAO");
		}
	}

	/**
	 * Devuelve una lista de organismos.
	 * 
	 * @param useCaseConf
	 *            Usuario que realiza la petici�n.
	 * @param query
	 *            criterio de b�squeda.
	 * @param queryCompactSearchOrg
	 *            nivel de b�squeda y ordenaci�n sobre el que se realiza la
	 *            b�squeda r�pida.
	 * @return result Listado de organismos disponibles.
	 * 
	 * @throws AttributesException
	 *             si ha habido alg�n problema con los atributos.
	 * @throws SessionException
	 *             si ha habido alg�n problema con la sesi�n.
	 * @throws ValidationException
	 *             si ha habido alg�n problema con en la validaci�n.
	 */
	public List<ScrOrg> getScrOrg(UseCaseConf useCaseConf, String query, QueryCompactSearchOrg queryCompactSearchOrg) throws ValidationException, AttributesException, SessionException {
		LOG.trace("Entrando en UnitsBo.getScrOrg()");
		Validator.validate_String_NotNull_LengthMayorZero( useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);

		Transaction tran = null;
		try {
			Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
			tran = session.beginTransaction();

			// Recuperamos la sesi�n
			CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());

			Criteria criteriaResults = session.createCriteria(EntityByLanguage.getScrOrgLanguage(useCaseConf.getLocale().getLanguage()));
			String sql = null;
			if (query != null && !"".equals(query)) {
				sql = "unaccent(UPPER(code || ' '||name)) LIKE '%" + Utils.converterToCS(query) + "%'";

			}
			if (queryCompactSearchOrg != null) {
				if (sql != null) {
					sql += " AND ";
				}
				sql += queryCompactSearchOrg.getWhere();
			}
			
			if (StringUtils.isNotEmpty(sql)) {
				sql += " AND TYPE = 1";
			}
			
			criteriaResults.add(Expression.sql(sql));
			criteriaResults.add(Expression.eq("enabled", Integer.valueOf(1)));

			criteriaResults.addOrder(Order.asc("adminLevel"));
			criteriaResults.addOrder(Order.asc("hierarchicalLevel"));
			criteriaResults.addOrder(Order.asc("name"));
			@SuppressWarnings("unchecked")
			List<ScrOrg> list = criteriaResults.list();
			List<ScrOrg> result = completeOrg(useCaseConf, list);
			if (session.isOpen()) {
				HibernateUtil.commitTransaction(tran);
			}
			return result;
		} catch (SessionException sessionException) {
			HibernateUtil.rollbackTransaction(tran);
			LOG.error(ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", sessionException);
			HibernateUtil.rollbackTransaction(tran);
			throw sessionException;
		} catch (Exception exception) {
			LOG.error(ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", exception);
			HibernateUtil.rollbackTransaction(tran);
			throw new AttributesException( AttributesException.ERROR_CANNOT_FIND_VALIDATIONLISTFIELDS);
		} finally {
			HibernateUtil.closeSession(useCaseConf.getEntidadId());
		}
	}

	/**
	 * M�todo que construye la query y controla los permisos para la b�squeda de
	 * las unidades.
	 * 
	 * @param useCaseConf
	 *            configuraci�n de la aplicaci�n.
	 * @param searchUnitsBean
	 *            par�metros de b�squeda.
	 * @param firstRow
	 *            primera fila.
	 * @param pageSize
	 *            tama�o b�squeda.
	 * @return objeto con los datos necesarios para la b�squeda.
	 * @throws ValidationException
	 *             error en la validaci�n
	 * @throws SessionException
	 *             error de sesi�n
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public UnitsResultsBean searchUnits(UseCaseConf useCaseConf, SearchUnitsBean searchUnitsBean, int firstRow, int pageSize) throws AttributesException, SessionException {
		LOG.trace("Entrando en UnitsBo.searchUnits()");
		UnitsResultsBean result = null;

		Transaction tran = null;
		String finalWhere = null;
		try {
			Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
			tran = session.beginTransaction();
			result = new UnitsResultsBean();
			// Recuperamos la sesi�n
			CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
			finalWhere = getWhere(session, searchUnitsBean);
			// N�mero total de registros
			int unitsCount = 0;
			unitsCount = ((Integer) session.createQuery( "select count(*) from ScrOrg WHERE " + finalWhere).uniqueResult()).intValue();
			result.setTotalSize(unitsCount);
			if (unitsCount > 0) {
				Criteria criteriaResults = session.createCriteria(ScrOrg.class);
				criteriaResults.setFirstResult(firstRow);
				criteriaResults.setMaxResults(pageSize);
				criteriaResults.add(Expression.sql(finalWhere));
				criteriaResults.addOrder(Order.asc("hierarchicalLevel"));
				criteriaResults.addOrder(Order.asc("name"));
				List<ScrOrg> list = criteriaResults.list();
				List<ScrOrg> completeList = completeOrg(useCaseConf, list);
				result.setRows(completeList);
			}
			if (session.isOpen()) {
				HibernateUtil.commitTransaction(tran);
			}
		} catch (SessionException sessionException) {
			HibernateUtil.rollbackTransaction(tran);
			LOG.error(ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", sessionException);
			HibernateUtil.rollbackTransaction(tran);
			throw sessionException;
		} catch (Exception exception) {
			LOG.error(ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", exception);
			HibernateUtil.rollbackTransaction(tran);
			throw new AttributesException( AttributesException.ERROR_CANNOT_FIND_VALIDATIONLISTFIELDS);
		} finally {
			HibernateUtil.closeSession(useCaseConf.getEntidadId());
		}
		return result;
	}

	/**
	 * M�todo que forma el where de la b�squeda.
	 * 
	 * @param searchUnitsBean
	 *            elementos que formar�n el where.
	 * @return where el where de la consulta.
	 * */
	private String getWhere(Session session, SearchUnitsBean searchUnitsBean) {
		
		ArrayList<String> tiposUnidadEspecificos = new ArrayList<String>();
		tiposUnidadEspecificos.add("");
		tiposUnidadEspecificos.add("0");
		tiposUnidadEspecificos.add("E");
		tiposUnidadEspecificos.add("A");
		tiposUnidadEspecificos.add("L");
		tiposUnidadEspecificos.add("U");
		
		String result = " enabled = 1 ";
		try {
			if (searchUnitsBean != null) {
	//			if (searchUnitsBean.getSearchType() <= 4 || searchUnitsBean.getSearchType() == 9) {
				if (tiposUnidadEspecificos.contains(searchUnitsBean.getCodeTipoUnidad())){
					result += " AND TYPE = " + searchUnitsBean.getSearchType();
				} else {		
					String whereTiposUnidades = " code NOT IN ('" + StringUtils.join(tiposUnidadEspecificos, "','") + "')";
					Criteria criteriaTiposUnidades = session.createCriteria(ScrTypeadm.class);
					criteriaTiposUnidades.add(Expression.sql(whereTiposUnidades));
					
					List<ScrTypeadm> listaTiposUnidades = criteriaTiposUnidades.list();
					
					List<Integer> idsTiposUnidades = new ArrayList<Integer>();
					for(ScrTypeadm tipoUnidad : listaTiposUnidades){
						idsTiposUnidades.add(tipoUnidad.getId());
					}				
					
					result += " AND TYPE IN ('" + StringUtils.join(idsTiposUnidades, "','") + "')";
				}
				if (!"".equals(searchUnitsBean.getSearchText())) {
					result += " AND unaccent(UPPER(code || ' '||name)) LIKE '%" + Utils.converterToCS(searchUnitsBean.getSearchText()) + "%'";
				}
//				if (searchUnitsBean.getSearchType() == 3 && searchUnitsBean.getCcaaId() != null) {
				if ("A".equals(searchUnitsBean.getCodeTipoUnidad()) && searchUnitsBean.getCcaaId() != null) {
					result += " AND ID_CCAA = '" + searchUnitsBean.getCcaaId() + "'";
				}
//				if (searchUnitsBean.getSearchType() == 4 && searchUnitsBean.getProvId() != null) {
				if ("L".equals(searchUnitsBean.getCodeTipoUnidad())  && searchUnitsBean.getProvId() != null) {
					result += " AND ID_PROV = " + searchUnitsBean.getProvId() + "";
				}
			}
		} catch (HibernateException e) {
			LOG.error("ERROR al recuperar los tipos de unidades, no devolvemos ninguna. " + e.getMessage(), e);
			result = " 1 = 2 "; 
		}
		return result;
	}

	/**
	 * Devuelve el organismo con el c�digo de entrada.
	 * 
	 * @param useCaseConf
	 *            Usuario que realiza la petici�n.
	 * @param code
	 *            C�digo del organiso.
	 * @return result organismo.
	 * 
	 * @throws AttributesException
	 *             si ha habido alg�n problema con los atributos.
	 * @throws SessionException
	 *             si ha habido alg�n problema con la sesi�n.
	 * @throws ValidationException
	 *             si ha habido alg�n problema con en la validaci�n.
	 */
	public ScrOrg getOrg(UseCaseConf useCaseConf, String code) throws ValidationException, AttributesException, SessionException {
		LOG.trace("Entrando en UnitsBo.getScrOrg()");
		Validator.validate_String_NotNull_LengthMayorZero( useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);

		Transaction tran = null;
		try {
			Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
			tran = session.beginTransaction();

			// Recuperamos la sesi�n
			CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());

			Criteria criteriaResults = session.createCriteria(EntityByLanguage.getScrOrgLanguage(useCaseConf.getLocale().getLanguage()));
			if (code != null && !"".equals(code)) {
				criteriaResults.add(Expression.eq("code", code));
			}
			criteriaResults.add(Expression.eq("enabled", Integer.valueOf(1)));

			@SuppressWarnings("unchecked")
			List<ScrOrg> result = criteriaResults.list();
			ScrOrg organismo = null;
			if (result != null && result.size() > 0) {
				organismo = result.get(0);
			}
			HibernateUtil.commitTransaction(tran);
			return organismo;
		} catch (SessionException sessionException) {
			HibernateUtil.rollbackTransaction(tran);
			LOG.error(ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", sessionException);
			HibernateUtil.rollbackTransaction(tran);
			throw sessionException;
		} catch (Exception exception) {
			LOG.error(ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", exception);
			HibernateUtil.rollbackTransaction(tran);
			throw new AttributesException( AttributesException.ERROR_CANNOT_FIND_VALIDATIONLISTFIELDS);
		} finally {
			HibernateUtil.closeSession(useCaseConf.getEntidadId());
		}
	}

	/**
	 * Devuelve el organismo con el c�digo de entrada.
	 * 
	 * @param useCaseConf
	 *            Usuario que realiza la petici�n.
	 * @param id
	 *            id del organiso.
	 * @return result organismo.
	 * 
	 * @throws AttributesException
	 *             si ha habido alg�n problema con los atributos.
	 * @throws SessionException
	 *             si ha habido alg�n problema con la sesi�n.
	 * @throws ValidationException
	 *             si ha habido alg�n problema con en la validaci�n.
	 */
	public ScrOrg getOrgId(UseCaseConf useCaseConf, Integer id) throws ValidationException, AttributesException, SessionException {
		LOG.trace("Entrando en UnitsBo.getOrgId()");
		Validator.validate_String_NotNull_LengthMayorZero( useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);

		Transaction tran = null;
		try {
			Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
			tran = session.beginTransaction();

			// Recuperamos la sesi�n
			CacheFactory.getCacheInterface().getCacheEntry( useCaseConf.getSessionID());

			Criteria criteriaResults = session.createCriteria(EntityByLanguage.getScrOrgLanguage(useCaseConf.getLocale().getLanguage()));
			if (id != null) {
				criteriaResults.add(Expression.eq("id", id));
			}

			@SuppressWarnings("unchecked")
			List<ScrOrg> result = criteriaResults.list();
			ScrOrg organismo = null;
			if (result != null && result.size() > 0) {
				organismo = result.get(0);
			}
			HibernateUtil.commitTransaction(tran);
			return organismo;
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", sessionException);
			HibernateUtil.rollbackTransaction(tran);
			throw sessionException;
		} catch (Exception exception) {
			LOG.error(ErrorConstants.GET_ORGANISMS_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", exception);
			HibernateUtil.rollbackTransaction(tran);
			throw new AttributesException( AttributesException.ERROR_CANNOT_FIND_VALIDATIONLISTFIELDS);
		} finally {
			HibernateUtil.closeSession(useCaseConf.getEntidadId());
		}
	}

	private List<ScrOrg> completeOrg(UseCaseConf useCaseConf, List<ScrOrg> orgs) {
		List<ScrOrg> result = null;
		if (orgs != null) {
			result = new ArrayList<ScrOrg>();
			ScrOrg temp = null;
			for (ScrOrg org : orgs) {
				temp = org;
				temp.setIsTramUnit(unidadTramitadoraDAO.esUnidadTramitacion( useCaseConf, org.getId()));
				result.add(temp);
			}
		}
		return result;
	}

	/**
	 * Devuelve la direcci�n completa de un organismo.
	 * 
	 * @param idOrg
	 *            id del organismo.
	 * @return la direcci�n del organismo.
	 */

	public DirOrgs getAddressOrg(Integer idOrg) {
		LOG.trace("Entrando en UnitsBo.getAddressOrg()");
		DirOrgs addressOrg = null;
		addressOrg = unitsDAO.getAddressOrg(idOrg);
		return addressOrg;
	}
}
