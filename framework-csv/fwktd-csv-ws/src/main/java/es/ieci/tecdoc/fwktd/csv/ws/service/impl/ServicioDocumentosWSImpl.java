package es.ieci.tecdoc.fwktd.csv.ws.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ieci.tecdoc.fwktd.csv.ws.delegate.DocumentosDelegate;
import es.ieci.tecdoc.fwktd.csv.ws.service.DocumentoCSV;
import es.ieci.tecdoc.fwktd.csv.ws.service.InfoDocumentoCSV;
import es.ieci.tecdoc.fwktd.csv.ws.service.InfoDocumentoCSVForm;
import es.ieci.tecdoc.fwktd.csv.ws.service.ServicioDocumentosPortType;

/**
 * Implementaci�n del servicio web de gesti�n de CSV.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public class ServicioDocumentosWSImpl implements ServicioDocumentosPortType {

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ServicioDocumentosWSImpl.class);

	/**
	 * Delegate para la gesti�n de documentos.
	 */
	private DocumentosDelegate documentosDelegate = null;

	/**
	 * Constructor.
	 */
	public ServicioDocumentosWSImpl() {
		super();
	}

	public DocumentosDelegate getDocumentosDelegate() {
		return documentosDelegate;
	}

	public void setDocumentosDelegate(DocumentosDelegate documentosDelegate) {
		this.documentosDelegate = documentosDelegate;
	}

	/**
	 * Genera el CSV para un documento.
	 *
	 * @param infoDocumentoForm
	 *            Informaci�n del documento.
	 * @return Informaci�n del documento
	 */
	public InfoDocumentoCSV generarCSV(InfoDocumentoCSVForm infoDocumentoForm) {

		logger.info("Llamada a generarCSV: infoDocumentoForm=[{}]",
				infoDocumentoForm);

		return getDocumentosDelegate().generarCSV(infoDocumentoForm);
	}

	/**
	 * Obtiene la informaci�n almacenada del documento.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @return Informaci�n del documento.
	 */
	public InfoDocumentoCSV getInfoDocumento(String id) {

		logger.info("Llamada a getInfoDocumento: id=[{}]", id);

		return getDocumentosDelegate().getInfoDocumento(id);
	}

	/**
	 * Obtiene la informaci�n almacenada del documento.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @return Informaci�n del documento.
	 */
	public InfoDocumentoCSV getInfoDocumentoByCSV(String csv) {

		logger.info("Llamada a getInfoDocumentoByCSV: csv=[{}]", csv);

		return getDocumentosDelegate().getInfoDocumentoByCSV(csv);
	}
	
	/**
	 * Obtiene la informaci�n almacenada del documento.
	 *
	 * @param nombreDoc
	 *            Nombre del documento.
	 * @return Informaci�n del documento.
	 */
	public InfoDocumentoCSV getInfoDocumentoByNombre(String nombreDoc) {

		logger.info("Llamada a getInfoDocumentoByCSV: nombre=[{}]", nombreDoc);

		return getDocumentosDelegate().getInfoDocumentoByNombre(nombreDoc);
	}

	/**
	 * Obtiene la informaci�n almacenada del documento junto con el contenido.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @return Informaci�n del documento con el contenido.
	 */
	public DocumentoCSV getDocumento(String id) {

		logger.info("Llamada a getDocumento: id=[{}]", id);

		return getDocumentosDelegate().getDocumento(id);
	}

	/**
	 * Obtiene la informaci�n almacenada del documento junto con el contenido.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @return Informaci�n del documento con el contenido.
	 */
	public DocumentoCSV getDocumentoByCSV(String csv) {

		logger.info("Llamada a getDocumentoByCSV: csv=[{}]", csv);

		return getDocumentosDelegate().getDocumentoByCSV(csv);
	}

	/**
	 * Guarda la informaci�n de un documento.
	 *
	 * @param infoDocumento
	 *            Informaci�n del documento.
	 * @return Informaci�n del documento.
	 */
	public InfoDocumentoCSV saveInfoDocumento(InfoDocumentoCSV infoDocumento) {

		logger.info("Llamada a saveInfoDocumento: infoDocumento=[{}]",
				infoDocumento);

		return getDocumentosDelegate()
				.saveInfoDocumento(infoDocumento);
	}

	/**
	 * Actualiza la informaci�n almacenada del documento.
	 *
	 * @param infoDocumento
	 *            Informaci�n del documento.
	 * @return Informaci�n del documento.
	 */
	public InfoDocumentoCSV updateInfoDocumento(InfoDocumentoCSV infoDocumento) {

		logger.info("Llamada a updateInfoDocumento: infoDocumento=[{}]",
				infoDocumento);

		return getDocumentosDelegate()
				.updateInfoDocumento(infoDocumento);
	}

	/**
	 * Elimina la informaci�n almacenada del documento.
	 *
	 * @param id
	 *            Identificador del documento.
	 */
	public void deleteInfoDocumento(String id) {

		logger.info("Llamada a deleteInfoDocumento: id=[{}]", id);

		getDocumentosDelegate().deleteInfoDocumento(id);
	}

	/**
	 * Comprueba si el contenido del documento se puede descargar de la
	 * aplicaci�n externa.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @return true si el contenido del documento existe, false en caso
	 *         contrario.
	 */
	public boolean existeContenidoDocumento(String id) {

		logger.info("Llamada a existeContenidoDocumento: id=[{}]", id);

		return getDocumentosDelegate().existeContenidoDocumento(id);
	}

	/**
	 * Obtiene el contenido del documento.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @return Contenido del documento.
	 */
	public byte[] getContenidoDocumento(String id) {

		logger.info("Llamada a getContenidoDocumento: id=[{}]", id);

		return getDocumentosDelegate().getContenidoDocumento(id);
	}

	/**
	 * Revoca la disponibilidad del documento.
	 *
	 * @param csv
	 *            CSV del documento.
	 */
	public void revocarDocumento(String csv) {

		logger.info("Llamada a revocarDocumento: csv=[{}]", csv);

		getDocumentosDelegate().revocarDocumento(csv);
	}

}
