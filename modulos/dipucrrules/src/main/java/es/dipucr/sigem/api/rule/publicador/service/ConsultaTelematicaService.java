package es.dipucr.sigem.api.rule.publicador.service;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.FicheroHito;
import ieci.tecdoc.sgm.core.services.consulta.FicherosHito;
import ieci.tecdoc.sgm.core.services.repositorio.ServicioRepositorioDocumentosTramitacion;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.publicador.dao.ExpedienteDAO;
import es.dipucr.sigem.api.rule.publicador.vo.ExpedienteVO;
import es.dipucr.sigem.api.rule.publicador.vo.InteresadoVO;

/**
 * Servicio de acceso a la informaci�n de los expedientes para la 
 * Consulta Telem�tica.
 *
 */
public class ConsultaTelematicaService {

	/** Logger de la clase. */
	protected static final Logger logger = 
		Logger.getLogger(ConsultaTelematicaService.class);

	/** Contexto de cliente. */
	private ClientContext context = null;
	
	
	/**
	 * Contructor.
	 * @param context Contexto de cliene.
	 */
	public ConsultaTelematicaService() {
		this.context = new ClientContext();
		this.context.setAPI(new InvesflowAPI(context));
	}

	/**
	 * Obtiene la informaci�n del expediente.
	 * @param numExp N�mero del expediente.
	 * @return Informaci�n del expediente.
	 * @throws ISPACException si ocurre alg�n error.
	 */
    public ExpedienteVO getExpediente(String numExp) throws ISPACException  {
		DbCnt cnt = null;
		
		try {
			cnt = context.getConnection();
			return ExpedienteDAO.getExpediente(cnt, numExp);
		} finally {
			context.releaseConnection(cnt);
		}
    }

	/**
	 * Obtiene el interesado principal del expediente.
	 * @param numExp N�mero del expediente.
	 * @return Informaci�n del expediente.
	 * @throws ISPACException si ocurre alg�n error.
	 */
    public InteresadoVO getInteresadoExpediente(String numExp) 
    		throws ISPACException  {
    	
		DbCnt cnt = null;
		
		try {
			cnt = context.getConnection();
			return ExpedienteDAO.getInteresadoExpediente(cnt, numExp);
		} finally {
			context.releaseConnection(cnt);
		}
    }

	/**
	 * Obtiene la lista de interesados del expediente.
	 * @param numExp N�mero del expediente.
	 * @return Lista de interesados del expediente.
	 * @throws ISPACException si ocurre alg�n error.
	 */
    public List getInteresados(String numExp) throws ISPACException  {
		DbCnt cnt = null;
		
		try {
			cnt = context.getConnection();
			return ExpedienteDAO.getInteresados(cnt, numExp);
		} finally {
			context.releaseConnection(cnt);
		}
    }

    /**
     * Obtiene los documentos creados en un tr�mite.
     * @param numExp N�mero del expediente.
     * @param idTramite Identificador del tr�mite.
     * @return Lista de documentos.
     * @throws ISPACException si ocurre alg�n error.
     */
    public IItemCollection getDocumentosTramite(String numExp, int idTramite) 
    		throws ISPACException {
    	
		// API de entidades
		IEntitiesAPI entitiesAPI = context.getAPI().getEntitiesAPI();
		
		// Documentos del tr�mite
		IItemCollection docs = entitiesAPI.getTaskDocuments(numExp, idTramite);
        
        return docs;
    }

    
    /**
     * Obtiene los documentos creados en una fase.
     * @param numExp N�mero del expediente.
     * @param idFase Identificador de la fase.
     * @return Lista de documentos.
     * @throws ISPACException si ocurre alg�n error.
     */
    public IItemCollection getDocumentosFase(String numExp, int idFase)
			throws ISPACException {

		// API de entidades
		IEntitiesAPI entitiesAPI = context.getAPI().getEntitiesAPI();

		// Documentos de la fase
		IItemCollection docs = entitiesAPI.getStageDocuments(numExp, idFase);

		return docs;
	}

    public FicherosHito getFicherosTramite(String guidHito, String numExp, 
    		int idTramite) throws ISPACException, SigemException {
    	
        // Documentos del tr�mite
        IItemCollection docs = getDocumentosTramite(numExp, idTramite);
        
        return getFicheros(guidHito, docs);
    }

    public FicherosHito getFicherosFase(String guidHito, String numExp, 
    		int idFase) throws ISPACException, SigemException {
    	
        // Documentos de la fase
        IItemCollection docs = getDocumentosFase(numExp, idFase);
        
        return getFicheros(guidHito, docs);

    }
    
    /**
     * Obtiene la informaci�n de los ficheros del hito para CT. 
     * @param guidHito GUID del hito en CT.
     * @param numExp N�mero de expediente.
     * @param idTramite Identificador del tr�mite.
     * @return Ficheros del hito
     * @throws ISPACException si ocurre alg�n error en tramitaci�n.
     * @throws SigemException si ocurre alg�n error en CT.
     */
    protected FicherosHito getFicheros(String guidHito, IItemCollection docs) throws ISPACException, SigemException {
    	
    	// Ficheros asociados al hito
        FicherosHito ficheros = new FicherosHito();
        
        // Documentos
        if ((docs != null) && docs.next()) {
        	
			// Llamada al API de RDE
			ServicioRepositorioDocumentosTramitacion rde = 
				LocalizadorServicios.getServicioRepositorioDocumentosTramitacion();

			IItem doc;
			FicheroHito fichero;
			String guid;
			String guidRDE;
			
			do {
				doc = docs.value();
				
				// GUID del fichero en el tramitador
				guid = doc.getString("SPAC_DT_DOCUMENTOS:INFOPAG");
				if (StringUtils.isNotBlank(guid)) {
					
					// Almacenar el fichero en RDE
					guidRDE = rde.storeDocument(null, getDocContent(guid), 
							getDocExt(guid), EntidadHelper.getEntidad());
	
					// Informaci�n del fichero
					fichero = new FicheroHito();
					fichero.setGuid(guidRDE);
					fichero.setGuidHito(guidHito);
					fichero.setTitulo(doc.getString("SPAC_DT_DOCUMENTOS:NOMBRE"));
					
					// A�adir el fichero a la lista
					ficheros.add(fichero);
				}
			} while (docs.next());
        }
        
        return ficheros;
    }

    public void deleteFicherosHito(FicherosHito ficheros) throws SigemException {
    	if (ficheros != null) {
    		ServicioRepositorioDocumentosTramitacion rde = 
    			LocalizadorServicios.getServicioRepositorioDocumentosTramitacion();
    		
    		FicheroHito fichero = null;
    		for (int i = 0; i < ficheros.count(); i++) {
				try {
					fichero = ficheros.get(i);
					rde.deleteDocument(null, fichero.getGuid(), EntidadHelper.getEntidad());
				} catch (Exception e) {
					logger.warn(
							"No se ha podido eliminar el fichero en RDE: " 
							+ fichero.getGuid(), e);
				}
			}
    	}
    }
    
    private String getDocExt(String guid) throws ISPACException {

		String ext = null;

		IGenDocAPI genDocAPI = context.getAPI().getGenDocAPI();
		Object connectorSession = null;

		try {
			connectorSession = genDocAPI.createConnectorSession();
			String mimeType = genDocAPI.getMimeType(connectorSession, guid);
			if (StringUtils.isNotBlank(mimeType)) {
				ext = MimetypeMapping.getExtension(mimeType);
			}
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}		        	

		return ext;
	}


	private byte[] getDocContent(String guid) throws ISPACException {

		IGenDocAPI genDocAPI = context.getAPI().getGenDocAPI();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Object connectorSession = null;

		try {
			connectorSession = genDocAPI.createConnectorSession();
			genDocAPI.getDocument(connectorSession, guid, out);
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}		        	

		return out.toByteArray();
	}
}
