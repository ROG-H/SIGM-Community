/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto sean aprobadas por laComisi�n Europea- versiones posteriores de la EUPL (la �Licencia�); 
 * Solo podr� usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
 * V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.actions;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.component.tabview.Tab;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.LazyDataModel;

import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.isicres.context.ISicresBeansProvider;

import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.ieci.tecdoc.isicres.api.business.keys.ConstantKeys;
import es.ieci.tecdoc.isicres.api.business.manager.UnidadAdministrativaManager;
import es.ieci.tecdoc.isicres.api.business.manager.impl.UnidadAdministrativaManagerImpl;
import es.ieci.tecdoc.isicres.api.business.vo.CriterioBusquedaTipoUnidadAdministrativaVO;
import es.ieci.tecdoc.isicres.api.business.vo.TipoUnidadAdministrativaVO;
import es.msssi.sgm.registropresencial.beans.SearchUnitsBean;
import es.msssi.sgm.registropresencial.beans.ibatis.DirOrgs;
import es.msssi.sgm.registropresencial.businessobject.SearchUnitsBo;
import es.msssi.sgm.registropresencial.businessobject.UnitsBo;

/**
 * Action buscar un organismo en el buscador avanzado.
 * 
 * @author cmorenog
 */
public class SearchUnitsAction extends GenericActions {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(SearchUnitsAction.class);
	
	private String textSearch;
	private ScrOrg selectedUnit;
	private String ccaaSearch;
	private String provSearch;
	
	/** Implementaci�n de lazydatamodel para gestionar el datatable. */
	private LazyDataModel<ScrOrg> searchUnitsBo;
	private ScrOrg selectOrg;
	private DirOrgs addressOrg;

	/**
	 * Post.
	 */
	@PostConstruct
	public void init() {
		searchUnitsBo = new SearchUnitsBo();
	}

	/**
	 * Constructor.
	 */
	public SearchUnitsAction() {
		init();
	}

	/**
	 * M�todo que inicializa el contenido de las pesta�as cuando se cambia de
	 * una a otra.
	 * 
	 * @param event
	 *            el evento de cambiar de pesta�a.
	 */
	public void onTabChange(TabChangeEvent event) {
		
		LOG.trace("Entrando en SearchUnitsAction.onTabChange()");
		
		searchUnitsBo = new SearchUnitsBo();
		
		Tab tab = event.getTab();
		
//		int idTab = Integer.valueOf(tab.getId().replaceAll("tab", "")).intValue();
		String sCodeTab = tab.getId().replaceAll("tab", "");
		
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
		
		Locale locale = new Locale(ConstantKeys.LOCALE_LENGUAGE_DEFAULT, ConstantKeys.LOCALE_COUNTRY_DEFAULT);
		CriterioBusquedaTipoUnidadAdministrativaVO criterio = new CriterioBusquedaTipoUnidadAdministrativaVO();
		criterio.setLimit(Long.valueOf(Integer.MAX_VALUE));
		criterio.setOffset(0L);
		
		UnidadAdministrativaManager unidadAdministrativaManager = (UnidadAdministrativaManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("unidadAdministrativaManager");		
		TipoUnidadAdministrativaVO tipoUnidad = unidadAdministrativaManager.getTipoUnidadesAdminByCode(sCodeTab, locale, criterio);
		
		int idTab = 0;
		
		if(StringUtils.isNumeric(tipoUnidad.getId())){
			idTab = Integer.parseInt(tipoUnidad.getId());
		}
		
		SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
		searchUnitsBean.setSearchType(idTab);
		searchUnitsBean.setCodeTipoUnidad(sCodeTab);
		
		textSearch = null;
		ccaaSearch = null;
		provSearch = null;
		
		((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
	}

	/**
	 * M�todo que abre el di�logo de b�squeda avanzada de organismos.
	 */
	public void chooseUnits() {
		
		LOG.trace("Entrando en SearchUnitsAction.chooseUnits()");
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", true);
		
		// options.put("width", 700);
		RequestContext.getCurrentInstance().openDialog("dialog/selectUnitsDialog", options, null);
	}

	/**
	 * M�todo que realiza una b�squeda de organismos propios de la entidad.
	 */
	public void searchPropios() {
		
		LOG.trace("Entrando en SearchUnitsAction.searchPropios()");
		
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
		
		Locale locale = new Locale(ConstantKeys.LOCALE_LENGUAGE_DEFAULT, ConstantKeys.LOCALE_COUNTRY_DEFAULT);
		CriterioBusquedaTipoUnidadAdministrativaVO criterio = new CriterioBusquedaTipoUnidadAdministrativaVO();
		criterio.setLimit(Long.valueOf(Integer.MAX_VALUE));
		criterio.setOffset(0L);
		
		UnidadAdministrativaManager unidadAdministrativaManager = (UnidadAdministrativaManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("unidadAdministrativaManager");		
		TipoUnidadAdministrativaVO tipoUnidad = unidadAdministrativaManager.getTipoUnidadesAdminByCode("0", locale, criterio);
		
		int codTpUd = 0;
		
		if(StringUtils.isNumeric(tipoUnidad.getId())){
			codTpUd = Integer.parseInt(tipoUnidad.getId());
		}
		
		SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
		searchUnitsBean.setSearchText(textSearch);
		searchUnitsBean.setSearchType(codTpUd);
		searchUnitsBean.setCodeTipoUnidad("0");
//		searchUnitsBean.setSearchType(1);
		
		((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
	}

	/**
	 * M�todo que realiza una b�squeda de organismos estatales.
	 */
	public void searchState() {
		
		LOG.trace("Entrando en SearchUnitsAction.searchState()");
		
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
		
		Locale locale = new Locale(ConstantKeys.LOCALE_LENGUAGE_DEFAULT, ConstantKeys.LOCALE_COUNTRY_DEFAULT);
		CriterioBusquedaTipoUnidadAdministrativaVO criterio = new CriterioBusquedaTipoUnidadAdministrativaVO();
		criterio.setLimit(Long.valueOf(Integer.MAX_VALUE));
		criterio.setOffset(0L);
		
		UnidadAdministrativaManager unidadAdministrativaManager = (UnidadAdministrativaManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("unidadAdministrativaManager");		
		TipoUnidadAdministrativaVO tipoUnidad = unidadAdministrativaManager.getTipoUnidadesAdminByCode("E", locale, criterio);
		
		int codTpUd = 0;
		
		if(StringUtils.isNumeric(tipoUnidad.getId())){
			codTpUd = Integer.parseInt(tipoUnidad.getId());
		}
		
		SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
		searchUnitsBean.setSearchText(textSearch);
		searchUnitsBean.setSearchType(codTpUd);
		searchUnitsBean.setCodeTipoUnidad("E");
//		searchUnitsBean.setSearchType(2);
		
		((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
	}

	/**
	 * M�todo que realiza una b�squeda de organismos auton�micos.
	 */
	public void searchAutonomic() {
		
		LOG.trace("Entrando en SearchUnitsAction.searchAutonomic()");
		
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
		
		Locale locale = new Locale(ConstantKeys.LOCALE_LENGUAGE_DEFAULT, ConstantKeys.LOCALE_COUNTRY_DEFAULT);
		CriterioBusquedaTipoUnidadAdministrativaVO criterio = new CriterioBusquedaTipoUnidadAdministrativaVO();
		criterio.setLimit(Long.valueOf(Integer.MAX_VALUE));
		criterio.setOffset(0L);
		
		UnidadAdministrativaManager unidadAdministrativaManager = (UnidadAdministrativaManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("unidadAdministrativaManager");		
		TipoUnidadAdministrativaVO tipoUnidad = unidadAdministrativaManager.getTipoUnidadesAdminByCode("A", locale, criterio);
		
		int codTpUd = 0;
		
		if(StringUtils.isNumeric(tipoUnidad.getId())){
			codTpUd = Integer.parseInt(tipoUnidad.getId());
		}
		
		SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
		
		if (StringUtils.isNotEmpty(ccaaSearch)) {
			if(1 == ccaaSearch.length()){
				searchUnitsBean.setCcaaId("0" + ccaaSearch);	
			} else {
				searchUnitsBean.setCcaaId(ccaaSearch);
			}
		}
		
		searchUnitsBean.setSearchText(textSearch);
		searchUnitsBean.setSearchType(codTpUd);
		searchUnitsBean.setCodeTipoUnidad("A");
//		searchUnitsBean.setSearchType(3);
		
		((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
	}

	/**
	 * M�todo que realiza una b�squeda de organismos locales.
	 */
	public void searchLocal() {
		
		LOG.trace("Entrando en SearchUnitsAction.searchLocal()");
		
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
		
		Locale locale = new Locale(ConstantKeys.LOCALE_LENGUAGE_DEFAULT, ConstantKeys.LOCALE_COUNTRY_DEFAULT);
		CriterioBusquedaTipoUnidadAdministrativaVO criterio = new CriterioBusquedaTipoUnidadAdministrativaVO();
		criterio.setLimit(Long.valueOf(Integer.MAX_VALUE));
		criterio.setOffset(0L);
		
		UnidadAdministrativaManager unidadAdministrativaManager = (UnidadAdministrativaManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("unidadAdministrativaManager");		
		TipoUnidadAdministrativaVO tipoUnidad = unidadAdministrativaManager.getTipoUnidadesAdminByCode("L", locale, criterio);
		
		int codTpUd = 0;
		
		if(StringUtils.isNumeric(tipoUnidad.getId())){
			codTpUd = Integer.parseInt(tipoUnidad.getId());
		}
		
		SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
		
		if (provSearch != null && !"".equals(provSearch)) {
			searchUnitsBean.setProvId(Integer.valueOf(provSearch));
		}
		
		searchUnitsBean.setSearchText(textSearch);
		searchUnitsBean.setSearchType(codTpUd);
		searchUnitsBean.setCodeTipoUnidad("L");
//		searchUnitsBean.setSearchType(4);
		
		((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
	}

	/**
	 * M�todo que realiza una b�squeda de organismos varios.
	 */
	public void searchOther() {
		
		LOG.trace("Entrando en SearchUnitsAction.searchOther()");
		
		SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
		searchUnitsBean.setSearchText(textSearch);
		searchUnitsBean.setSearchType(Integer.MIN_VALUE);
		searchUnitsBean.setCodeTipoUnidad("ASDF");
//		searchUnitsBean.setSearchType(5);
		
		((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
	}
	
	/**
	 * M�todo que realiza una b�squeda de Universidades
	 */
	public void searchUniversidad(){
		
		LOG.trace("Entrando en SearchUnitsAction.searchUniversidad()");
		
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
		
		Locale locale = new Locale(ConstantKeys.LOCALE_LENGUAGE_DEFAULT, ConstantKeys.LOCALE_COUNTRY_DEFAULT);
		CriterioBusquedaTipoUnidadAdministrativaVO criterio = new CriterioBusquedaTipoUnidadAdministrativaVO();
		criterio.setLimit(Long.valueOf(Integer.MAX_VALUE));
		criterio.setOffset(0L);
		
		UnidadAdministrativaManager unidadAdministrativaManager = (UnidadAdministrativaManagerImpl) ISicresBeansProvider.getInstance().getGenericBean("unidadAdministrativaManager");		
		TipoUnidadAdministrativaVO tipoUnidad = unidadAdministrativaManager.getTipoUnidadesAdminByCode("U", locale, criterio);
		
		int codTpUd = 0;
		
		if(StringUtils.isNumeric(tipoUnidad.getId())){
			codTpUd = Integer.parseInt(tipoUnidad.getId());
		}
		
		SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
		searchUnitsBean.setSearchText(textSearch);
		searchUnitsBean.setSearchType(codTpUd);
		searchUnitsBean.setCodeTipoUnidad("U");
		
		((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
	}

	/**
	 * M�todo que realiza una b�squeda de laboratorios.
	 */
	public void searchLab() {
		
		LOG.trace("Entrando en SearchUnitsAction.searchLab()");
		
		SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
		searchUnitsBean.setSearchText(textSearch);
		searchUnitsBean.setSearchType(9);
		
		((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
	}

	/**
	 * M�todo que oculta el di�logo de b�squeda avanzada de organismos.
	 */
	public void hiddenDialog() {
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("UNITSDIALOG");
		RequestContext.getCurrentInstance().closeDialog("selectUnitsDialog");
	}

	/**
	 * M�todo que selecciona el organismo seleccionado en la b�squeda avanzada y
	 * lo incluye como atributo en la sesi�n para ser recogido por el formulario
	 * que lo ha llamado.
	 */
	public void selectUnitFromDialog() {
		
		LOG.trace("Entrando en SearchUnitsAction.selectUnitFromDialog()");
		
		if (selectedUnit != null) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("UNITSDIALOG", selectedUnit);
			RequestContext.getCurrentInstance().closeDialog("selectUnitsDialog");
			
		} else {
			RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Debe seleccionar una unidad"));
		}
	}

	public void viewDirOrg() {
		
		if (selectOrg != null) {
			UnitsBo unitsBo = new UnitsBo();
			addressOrg = unitsBo.getAddressOrg(selectOrg.getId());
		}
	}

	/**
	 * Obtiene el valor del par�metro textSearch.
	 * 
	 * @return textSearch valor del campo a obtener.
	 */
	public String getTextSearch() {
		return textSearch;
	}

	/**
	 * Guarda el valor del par�metro textSearch.
	 * 
	 * @param textSearch
	 *            valor del campo a guardar.
	 */
	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}

	/**
	 * Obtiene el valor del par�metro selectedUnit.
	 * 
	 * @return selectedUnit valor del campo a obtener.
	 */
	public ScrOrg getSelectedUnit() {
		return selectedUnit;
	}

	/**
	 * Guarda el valor del par�metro selectedUnit.
	 * 
	 * @param selectedUnit
	 *            valor del campo a guardar.
	 */
	public void setSelectedUnit(ScrOrg selectedUnit) {
		this.selectedUnit = selectedUnit;
	}

	/**
	 * Obtiene el valor del par�metro searchUnitsBo.
	 * 
	 * @return searchUnitsBo valor del campo a obtener.
	 */
	public LazyDataModel<ScrOrg> getSearchUnitsBo() {
		return searchUnitsBo;
	}

	/**
	 * Guarda el valor del par�metro searchUnitsBo.
	 * 
	 * @param searchUnitsBo
	 *            valor del campo a guardar.
	 */
	public void setSearchUnitsBo(LazyDataModel<ScrOrg> searchUnitsBo) {
		this.searchUnitsBo = searchUnitsBo;
	}

	/**
	 * Obtiene el valor del par�metro ccaaSearch.
	 * 
	 * @return ccaaSearch valor del campo a obtener.
	 */
	public String getCcaaSearch() {
		return ccaaSearch;
	}

	/**
	 * Guarda el valor del par�metro ccaaSearch.
	 * 
	 * @param ccaaSearch
	 *            valor del campo a guardar.
	 */
	public void setCcaaSearch(String ccaaSearch) {
		this.ccaaSearch = ccaaSearch;
	}

	/**
	 * Obtiene el valor del par�metro provSearch.
	 * 
	 * @return provSearch valor del campo a obtener.
	 */
	public String getProvSearch() {
		return provSearch;
	}

	/**
	 * Guarda el valor del par�metro provSearch.
	 * 
	 * @param provSearch
	 *            valor del campo a guardar.
	 */
	public void setProvSearch(String provSearch) {
		this.provSearch = provSearch;
	}

	/**
	 * Obtiene el valor del par�metro selectOrg.
	 * 
	 * @return selectOrg valor del campo a obtener.
	 */
	public ScrOrg getSelectOrg() {
		return selectOrg;
	}

	/**
	 * Guarda el valor del par�metro selectOrg.
	 * 
	 * @param selectOrg
	 *            valor del campo a guardar.
	 */
	public void setSelectOrg(ScrOrg selectOrg) {
		this.selectOrg = selectOrg;
	}

	/**
	 * Obtiene el valor del par�metro addressOrg.
	 * 
	 * @return addressOrg valor del campo a obtener.
	 */
	public DirOrgs getAddressOrg() {
		return addressOrg;
	}

	/**
	 * Guarda el valor del par�metro addressOrg.
	 * 
	 * @param addressOrg
	 *            valor del campo a guardar.
	 */
	public void setAddressOrg(DirOrgs addressOrg) {
		this.addressOrg = addressOrg;
	}

}