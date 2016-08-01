package es.ieci.tecdoc.fwktd.csv.web.helper;

import java.util.Locale;

import es.ieci.tecdoc.fwktd.csv.core.vo.InfoDocumentoCSV;
import es.ieci.tecdoc.fwktd.csv.web.vo.InfoDocumentoVO;

/**
 * Utilidad para la adaptaci�n de objetos con la informaci�n del documento desde
 * el fwktd-csv.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public class DocumentoAdapterHelper {

	/**
	 * Adapta la informaci�n del documento de fwktd-csv al VO de la aplicaci�n.
	 *
	 * @param infoDocumentoCSV
	 *            Informaci�n del documento de fwktd-csv
	 * @return Informaci�n del documento.
	 */
	public static InfoDocumentoVO getInfoDocumentoVO(
			InfoDocumentoCSV infoDocumentoCSV, Locale locale) {

		InfoDocumentoVO infoDocumentoVO = null;

		if (infoDocumentoCSV != null) {
			infoDocumentoVO = new InfoDocumentoVO();
			infoDocumentoVO.setId(infoDocumentoCSV.getId());
			infoDocumentoVO.setNombre(infoDocumentoCSV.getNombre());
			infoDocumentoVO.setDescripcion(infoDocumentoCSV.getDescripcion(locale));
			infoDocumentoVO.setTipoMime(infoDocumentoCSV.getTipoMime());
			infoDocumentoVO.setFechaCreacion(infoDocumentoCSV.getFechaCreacion());
			infoDocumentoVO.setFechaCaducidad(infoDocumentoCSV.getFechaCaducidad());
			infoDocumentoVO.setCodigoAplicacion(infoDocumentoCSV.getCodigoAplicacion());
			infoDocumentoVO.setNombreAplicacion(infoDocumentoCSV.getNombreAplicacion());
			infoDocumentoVO.setDisponible(infoDocumentoCSV.isDisponible());
			infoDocumentoVO.setCsv(infoDocumentoCSV.getCsv());
			infoDocumentoVO.setFechaCSV(infoDocumentoCSV.getFechaCSV());
			
			/**
			 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
			 */
			infoDocumentoVO.setNumeroRegistro(infoDocumentoCSV.getNumeroRegistro());
			infoDocumentoVO.setFechaRegistro(infoDocumentoCSV.getFechaRegistro());
			infoDocumentoVO.setOrigenRegistro(infoDocumentoCSV.getOrigenRegistro());
			infoDocumentoVO.setDestinoRegistro(infoDocumentoCSV.getDestinoRegistro());
			/**
			 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
			 */
		}

		return infoDocumentoVO;
	}
}
