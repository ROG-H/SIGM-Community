package es.ieci.tecdoc.fwktd.csv.web.helper;

import java.util.Date;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import es.ieci.tecdoc.fwktd.csv.core.vo.InfoDocumentoCSV;
import es.ieci.tecdoc.fwktd.csv.web.vo.InfoDocumentoVO;

/**
 * Test de la clase de utilidad para la adaptaci�n de objetos con la informaci�n
 * del documento desde el fwktd-csv.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public class DocumentoAdapterHelperTest {

	@Test
	public void testGetInfoDocumentoVO() {

		InfoDocumentoCSV infoDocumentoCSV = null;
		InfoDocumentoVO infoDocumentoVO = DocumentoAdapterHelper
				.getInfoDocumentoVO(infoDocumentoCSV, Locale.getDefault());
		Assert.assertNull("El objeto deber�a ser nulo", infoDocumentoVO);

		infoDocumentoCSV = new InfoDocumentoCSV();
		infoDocumentoCSV.setId("1");
		infoDocumentoCSV.setNombre("documento.txt");
		infoDocumentoCSV.setTipoMime("text/plain");
		infoDocumentoCSV.setFechaCreacion(new Date());
		infoDocumentoCSV.setFechaCaducidad(new Date());
		infoDocumentoCSV.setCodigoAplicacion("COD_APP");
		infoDocumentoCSV.setNombreAplicacion("Aplicaci�n");
		infoDocumentoCSV.setDisponible(true);
		infoDocumentoCSV.setCsv("CSV000");
		infoDocumentoCSV.setFechaCSV(new Date());
		
		/**
		 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
		 */
		infoDocumentoCSV.setNumeroRegistro("123456789");
		infoDocumentoCSV.setFechaRegistro(new Date());
		infoDocumentoCSV.setOrigenRegistro("ORIGEN");
		infoDocumentoCSV.setDestinoRegistro("DESTINO");
		/**
		 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
		 */

		infoDocumentoVO = DocumentoAdapterHelper.getInfoDocumentoVO(
				infoDocumentoCSV, Locale.getDefault());
		assertEquals(infoDocumentoCSV, infoDocumentoVO);
	}

	protected void assertEquals(InfoDocumentoCSV infoDocumentoCSV,
			InfoDocumentoVO infoDocumentoVO) {

		Assert.assertEquals(infoDocumentoCSV.getId(),
				infoDocumentoVO.getId());
		Assert.assertEquals(infoDocumentoCSV.getNombre(),
				infoDocumentoVO.getNombre());
		Assert.assertEquals(infoDocumentoCSV.getTipoMime(),
				infoDocumentoVO.getTipoMime());
		Assert.assertEquals(infoDocumentoCSV.getFechaCreacion().toString(),
				infoDocumentoVO.getFechaCreacion().toString());
		Assert.assertEquals(infoDocumentoCSV.getFechaCaducidad().toString(),
				infoDocumentoVO.getFechaCaducidad().toString());
		Assert.assertEquals(infoDocumentoCSV.getCodigoAplicacion(),
				infoDocumentoVO.getCodigoAplicacion());
		Assert.assertEquals(infoDocumentoCSV.getNombreAplicacion(),
				infoDocumentoVO.getNombreAplicacion());
		Assert.assertEquals(infoDocumentoCSV.isDisponible(),
				infoDocumentoVO.isDisponible());
		Assert.assertEquals(infoDocumentoCSV.getCsv(), infoDocumentoVO.getCsv());
		Assert.assertEquals(infoDocumentoCSV.getFechaCSV().toString(),
				infoDocumentoVO.getFechaCSV().toString());
	}
}
