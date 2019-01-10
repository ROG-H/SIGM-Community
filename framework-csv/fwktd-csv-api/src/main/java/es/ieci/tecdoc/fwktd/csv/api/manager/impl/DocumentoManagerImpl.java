package es.ieci.tecdoc.fwktd.csv.api.manager.impl;

import org.springframework.util.Assert;

import es.ieci.tecdoc.fwktd.csv.api.dao.DocumentoDao;
import es.ieci.tecdoc.fwktd.csv.api.manager.DocumentoManager;
import es.ieci.tecdoc.fwktd.csv.api.vo.AplicacionVO;
import es.ieci.tecdoc.fwktd.csv.api.vo.DocumentoVO;
import es.ieci.tecdoc.fwktd.csv.api.xml.connectionConfig.ConfiguracionConexion;
import es.ieci.tecdoc.fwktd.csv.core.exception.CSVException;
import es.ieci.tecdoc.fwktd.server.dao.BaseDao;
import es.ieci.tecdoc.fwktd.server.manager.impl.BaseManagerImpl;

/**
 * Implementaci�n del manager de documentos.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public class DocumentoManagerImpl extends BaseManagerImpl<DocumentoVO, String>
		implements DocumentoManager {

	/**
	 * Constructor.
	 *
	 * @param aGenericDao
	 */
	public DocumentoManagerImpl(BaseDao<DocumentoVO, String> aGenericDao) {
		super(aGenericDao);
	}

	/**
	 * {@inheritDoc}
	 * @see es.ieci.tecdoc.fwktd.csv.api.manager.DocumentoManager#getDocumentoByCSV(java.lang.String)
	 */
	public DocumentoVO getDocumentoByCSV(String csv) {

		logger.info("Obteniendo el documento a partir del CSV [{}]", csv);

		Assert.hasText(csv, "'csv' must not be empty");

		return ((DocumentoDao)getDao()).getDocumentoByCSV(csv);
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.ieci.tecdoc.fwktd.csv.api.manager.DocumentoManager#getDocumentoByNombre(java.lang.String)
	 */
	public DocumentoVO getDocumentoByNombre(String nombredoc) {

		logger.info("Obteniendo el documento a partir del nombre [{}]", nombredoc);

		Assert.hasText(nombredoc, "'nombredoc' must not be empty");

		return ((DocumentoDao)getDao()).getDocumentoByNombre(nombredoc);
	}

	/**
	 * {@inheritDoc}
	 * @see es.ieci.tecdoc.fwktd.csv.api.manager.DocumentoManager#deleteDocumentoByCSV(java.lang.String)
	 */
	public void deleteDocumentoByCSV(String csv) {

		logger.info("Eliminando el documento a partir del CSV [{}]", csv);

		Assert.hasText(csv, "'csv' must not be empty");

		DocumentoVO documento = ((DocumentoDao)getDao()).getDocumentoByCSV(csv);
		if (documento != null) {
			delete(documento.getId());
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.ieci.tecdoc.fwktd.server.manager.impl.BaseManagerImpl#delete(java.io.Serializable)
	 */
	public void delete(String id) {

		logger.info("Eliminando el documento [{}]", id);

		Assert.hasText(id, "'id' must not be empty");

		// Eliminar el documento
		((DocumentoDao)getDao()).delete(id);
	}

//	public void deleteAll(List<DocumentoVO> documentos) {
//
//		logger.info("Eliminando los documentos: {}", documentos);
//
//		if (documentos != null) {
//			for (DocumentoVO documento : documentos) {
//				delete(documento.getId());
//			}
//		}
//	}

    /**
     * {@inheritDoc}
     *
     * @see es.ieci.tecdoc.fwktd.csv.api.manager.DocumentoManager#getConfiguracionConexion(es.ieci.tecdoc.fwktd.csv.api.vo.DocumentoVO)
     */
    public ConfiguracionConexion getConfiguracionConexion(DocumentoVO documento) {

    	logger.info("Obteniendo el objeto ConfiguracionConexion a partir del documento: [{}]", documento);

    	Assert.notNull(documento, "'documento' must not be null");

		// Informaci�n de la aplicaci�n externa
		AplicacionVO aplicacion = documento.getAplicacion();

        try {

    		// Parsear la informaci�n de conexi�n de la aplicaci�n
        	return ConfiguracionConexion.parseText(aplicacion.getInfoConexion());

        } catch (Throwable t) {
            logger.error("Error al parsear el XML de configuraci�n de la conexi�n con la aplicaci�n [" + aplicacion.getCodigo() + "]", t);
			throw new CSVException(
					"error.csv.application.unmarshalConnectionConfig",
					new String[] { aplicacion.getCodigo() },
					"Error al parsear el XML de configuraci�n de la conexi�n con la aplicaci�n: "
							+ aplicacion.getCodigo());
        }
    }
}
