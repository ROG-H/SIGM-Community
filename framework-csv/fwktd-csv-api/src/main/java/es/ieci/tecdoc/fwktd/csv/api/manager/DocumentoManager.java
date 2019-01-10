package es.ieci.tecdoc.fwktd.csv.api.manager;

import es.ieci.tecdoc.fwktd.csv.api.vo.DocumentoVO;
import es.ieci.tecdoc.fwktd.csv.api.xml.connectionConfig.ConfiguracionConexion;
import es.ieci.tecdoc.fwktd.server.manager.BaseManager;

/**
 * Interfaz para los managers de documentos.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public interface DocumentoManager extends BaseManager<DocumentoVO, String> {

	/**
	 * Obtiene la informaci�n del documento a partir del CSV.
	 *
	 * @param csv
	 *            C�digo Seguro de Verificaci�n del documento.
	 * @return Informaci�n del documento.
	 */
	public DocumentoVO getDocumentoByCSV(String csv);
	
	/**
	 * Obtiene la informaci�n del documento a partir del nombre.
	 *
	 * @param nombreDoc
	 *            Nombre del documento.
	 * @return Informaci�n del documento.
	 */
	public DocumentoVO getDocumentoByNombre(String nombreDoc);

	/**
	 * Elimina un documento a partir del CSV
	 *
	 * @param csv
	 *            C�digo Seguro de Verificaci�n del documento.
	 */
	public void deleteDocumentoByCSV(String csv);

	/**
	 * Obtiene el la configuraci�n de conexi�n a la aplicaci�n externa asociada
	 * al documento.
	 *
	 * @param documento
	 *            Informaci�n del documento.
	 * @return Conector con la aplicaci�n externa.
	 */
	public ConfiguracionConexion getConfiguracionConexion(DocumentoVO documento);

}
