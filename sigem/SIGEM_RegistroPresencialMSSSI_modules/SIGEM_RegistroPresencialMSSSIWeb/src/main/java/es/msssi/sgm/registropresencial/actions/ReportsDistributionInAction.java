/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versi�n 1.1 o �en cuanto sean aprobadas por laComisi�n Europea� versiones posteriores de la EUPL (la �Licencia�); 
* Solo podr� usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
* V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.actions;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Logger;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

import com.ieci.tecdoc.common.invesicres.ScrOrg;

import es.msssi.sgm.registropresencial.beans.DistributionResultsBean;
import es.msssi.sgm.registropresencial.beans.SearchDistributionBean;
import es.msssi.sgm.registropresencial.businessobject.ReportsDistributionBo;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase que genera los informes relacionados con registros de entrada de la
 * aplicaci�n.
 * 
 * @author jortizs
 */
public class ReportsDistributionInAction extends GenericActions implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ReportsDistributionInAction.class.getName());

    /** Bean con los criterios del buscador. */
    private SearchDistributionBean searchDistributionBean = new SearchDistributionBean();
    /** Clase con la l�gica de negocio. */
    private ReportsDistributionBo reportsBo;
    /**
     * objeto que contiene el fichero.
     */
    private StreamedContent file;
    private ScrOrg selectedDestOrg;
    private ScrOrg selectedOriginOrg;
    
    /**
     * Constructor.
     */
    public ReportsDistributionInAction() {
	init();
	searchDistributionBean
	    	.setType(1);
	reportsBo = new ReportsDistributionBo();
    }

    /**
     * Construye los informes de la aplicaci�n correspondientes a relaciones,
     * por origen y destino, de distribuciones de registros seg�n una fecha
     * marcada.
     */
    public void buildRelationsReport() {
	LOG.trace("Entrando en ReportsDistributionAction.buildRelationsReport()");
	try {
	    init();
	    DistributionResultsBean distributionList =
		reportsBo.getRegistersForRelationReports(searchDistributionBean);
	    LOG.info("Obtenidos " +
		distributionList.getTotalSize() + " registros.");
	    if (distributionList == null ||
		distributionList.getTotalSize() <= 0) {
		FacesContext.getCurrentInstance().addMessage(
		    null,
		    new FacesMessage(
			FacesMessage.SEVERITY_WARN, "Generaci�n de informe ",
			"No hay registros que cumplan con los requisitos de b�squeda"));
	    }
	    else {
		// Se construye y devuelve el informe
		file = reportsBo.buildJasperReport(distributionList);
	    }
	}
	catch (Exception exception) {
	    LOG.error(
		"Error al generar el informe: ", exception);
	    Utils.redirectToErrorPage(
		null, null, exception);
	}
    }

    /**
     * Devuelve el searchDistributionBean.
     * 
     * @return searchDistributionBean el objeto searchDistributionBean.
     */
    public SearchDistributionBean getSearchDistributionBean() {
	return searchDistributionBean;
    }

    /**
     * Recibe el objeto searchDistributionBean.
     * 
     * @param searchDistributionBean
     *            el objeto searchDistributionBean.
     */
    public void setSearchDistributionBean(
	SearchDistributionBean searchDistributionBean) {
	this.searchDistributionBean = searchDistributionBean;
    }
    
    /**
     * M�todo que actualiza el campo origen con el valor seleccionado en la
     * b�squeda avanzada de organismos.
     */
    public void updateOrigin() {
	ScrOrg unidad = null;
	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(
	    "UNITSDIALOG") != null) {
	    unidad =
		(ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		    .get(
			"UNITSDIALOG");
	    getSearchDistributionBean().setFld7(
		unidad);
	    selectedOriginOrg = unidad;
	}
    }

    /**
     * M�todo que actualiza el campo destino con el valor seleccionado en la
     * b�squeda avanzada de organismos.
     */
    public void updateDestination() {
	ScrOrg unidad = null;
	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(
	    "UNITSDIALOG") != null) {
	    unidad =
		(ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		    .get(
			"UNITSDIALOG");
	    getSearchDistributionBean().setFld8(
		unidad);
	    selectedDestOrg = unidad;
	}
    }
    
    
    /**
     * Devuelve el file.
     * 
     * @return file el objeto file.
     */
    public StreamedContent getFile() {
        return file;
    }
    
    /**
     * M�todo resetea los mensajes.
     */
    public void reset(){
	
    }
    
    /**
     * Metodo que se lanza al seleccionar un organismo en el destino.
     * @param event
     * 		evento select.
     */
    public void onItemSelectDestinationOrg (SelectEvent event){
	selectedDestOrg = (ScrOrg) event.getObject();
    }
    
    /**
     * Metodo que se lanza al cambiar un organismo en el destino.
     * @param event
     * 		evento change.
     */
    public void onChangeDestinationOrg (AjaxBehaviorEvent event){
	if (((AutoComplete) event.getSource()).getItemValue() == null){
	    selectedDestOrg = null;
	}else {
	    selectedDestOrg = (ScrOrg)((AutoComplete) event.getSource()).getItemValue();
	}
    }
    
    /**
     * Metodo que se lanza al seleccionar un organismo en el origen.
     * @param event
     * 		evento select.
     */
    public void onItemSelectOriginOrg (SelectEvent event){
	selectedOriginOrg = (ScrOrg) event.getObject();
    }
    
    /**
     * Metodo que se lanza al cambiar un organismo en el origen.
     * @param event
     * 		evento change.
     */
    public void onChangeOriginOrg (AjaxBehaviorEvent event){
	if (((AutoComplete) event.getSource()).getItemValue() == null){
	    selectedOriginOrg = null;
	}else {
	    selectedOriginOrg = (ScrOrg)((AutoComplete) event.getSource()).getItemValue();
	}
    }
    
    /**
     * Obtiene el valor del par�metro selectedDestOrg.
     * 
     * @return selectedDestOrg valor del campo a obtener.
     */
    public ScrOrg getSelectedDestOrg() {
        return selectedDestOrg;
    }
    /**
     * Guarda el valor del par�metro selectedDestOrg.
     * 
     * @param selectedDestOrg
     *            valor del campo a guardar.
     */
    public void setSelectedDestOrg(ScrOrg selectedDestOrg) {
        this.selectedDestOrg = selectedDestOrg;
    }

    /**
     * Obtiene el valor del par�metro selectedOriginOrg.
     * 
     * @return selectedOriginOrg valor del campo a obtener.
     */
    public ScrOrg getSelectedOriginOrg() {
        return selectedOriginOrg;
    }
    /**
     * Guarda el valor del par�metro selectedOriginOrg.
     * 
     * @param selectedOriginOrg
     *            valor del campo a guardar.
     */
    public void setSelectedOriginOrg(ScrOrg selectedOriginOrg) {
        this.selectedOriginOrg = selectedOriginOrg;
    }
}