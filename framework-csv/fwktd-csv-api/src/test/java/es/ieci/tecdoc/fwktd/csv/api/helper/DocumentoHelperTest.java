package es.ieci.tecdoc.fwktd.csv.api.helper;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import es.ieci.tecdoc.fwktd.csv.api.manager.AplicacionManager;
import es.ieci.tecdoc.fwktd.csv.api.vo.AplicacionVO;
import es.ieci.tecdoc.fwktd.csv.api.vo.DocumentoVO;
import es.ieci.tecdoc.fwktd.csv.core.vo.DocumentoCSV;
import es.ieci.tecdoc.fwktd.csv.core.vo.InfoDocumentoCSV;
import es.ieci.tecdoc.fwktd.test.db.AbstractDbUnitTransactionalJUnit4SpringContextTests;
import es.ieci.tecdoc.fwktd.test.db.annotation.DatasetLocation;

/**
 * @author Iecisa
 * @version $Revision$
 *
 */
@DatasetLocation("data/dataset.xml")
@ContextConfiguration({
	"/beans/fwktd-csv-api-applicationContext.xml",
	"/beans/fwktd-csv-test-beans.xml" })
public class DocumentoHelperTest extends AbstractDbUnitTransactionalJUnit4SpringContextTests {

	@Autowired
	private AplicacionManager fwktd_sir_aplicacionManager;

	public AplicacionManager getAplicacionManager() {
		return fwktd_sir_aplicacionManager;
	}

	@Test
	public void testGetInfoDocumentoCSV() {

		DocumentoVO documentoVO = createDocumentoVO();

		InfoDocumentoCSV infoDocumentoCSV = DocumentoHelper
				.getInfoDocumentoCSV(documentoVO);

		assertEquals(documentoVO, infoDocumentoCSV);
	}

	@Test
	public void testGetDocumentoCSV() {

		DocumentoVO documentoVO = createDocumentoVO();

		DocumentoCSV documentoCSV = DocumentoHelper
				.getDocumentoCSV(documentoVO);

		assertEquals(documentoVO, documentoCSV);
	}

	@Test
	public void testGetDocumentoVO() {

		InfoDocumentoCSV infoDocumentoCSV = createInfoDocumentoCSV();

		DocumentoVO documentoVO = DocumentoHelper.getDocumentoVO(infoDocumentoCSV, getAplicacionManager(), true);

		assertEquals(documentoVO, infoDocumentoCSV);
	}

	protected DocumentoVO createDocumentoVO() {

		DocumentoVO documentoVO = new DocumentoVO();
		documentoVO.setId("1");
		documentoVO.setNombre("Documento.txt");
		documentoVO.setDescripcion("<?xml version=\"1.0\" encoding=\"UTF-8\"?><labels><label locale=\"default\"><![CDATA[[default] Descripci�n del documento]]></label><label locale=\"ca\"><![CDATA[[ca] Descripci�n del documento]]></label><label locale=\"es\"><![CDATA[[es] Descripci�n del documento]]></label><label locale=\"eu\"><![CDATA[[eu] Descripci�n del documento]]></label><label locale=\"gl\"><![CDATA[[gl] Descripci�n del documento]]></label></labels>");
		documentoVO.setTipoMIME("text/plain");
		documentoVO.setFechaCreacion(new Date());
		documentoVO.setFechaCaducidad(new Date());
		documentoVO.setDisponible(true);
		documentoVO.setCsv("CSV000");
		documentoVO.setFechaCSV(new Date());

		AplicacionVO aplicacionVO = new AplicacionVO();
		aplicacionVO.setCodigo("APP1");
		aplicacionVO.setNombre("Aplicaci�n 1");
		documentoVO.setAplicacion(aplicacionVO);
		
		/**
		 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
		 */
		documentoVO.setNumeroRegistro("123456789");
		documentoVO.setFechaRegistro(new Date());
		documentoVO.setOrigenRegistro("ORIGEN");
		documentoVO.setDestinoRegistro("DESTINO");
		/**
		 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
		 */

		return documentoVO;
	}

	protected InfoDocumentoCSV createInfoDocumentoCSV() {

		InfoDocumentoCSV infoDocumentoCSV = new InfoDocumentoCSV();
		infoDocumentoCSV.setId("1");
		infoDocumentoCSV.setNombre("Documento.txt");
		infoDocumentoCSV.setTipoMime("text/plain");
		infoDocumentoCSV.setFechaCreacion(new Date());
		infoDocumentoCSV.setFechaCaducidad(new Date());
		infoDocumentoCSV.setDisponible(true);
		infoDocumentoCSV.setCsv("CSV000");
		infoDocumentoCSV.setFechaCSV(new Date());
		infoDocumentoCSV.setCodigoAplicacion("APP1");
		infoDocumentoCSV.setNombreAplicacion("Aplicaci�n 1");

		infoDocumentoCSV.addDescripcion(null, "[default] Descripci�n del documento");
		infoDocumentoCSV.addDescripcion(new Locale("ca"), "[ca] Descripci�n del documento");
		infoDocumentoCSV.addDescripcion(new Locale("es"), "[es] Descripci�n del documento");
		infoDocumentoCSV.addDescripcion(new Locale("eu"), "[eu] Descripci�n del documento");
		infoDocumentoCSV.addDescripcion(new Locale("gl"), "[gl] Descripci�n del documento");
		
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

		return infoDocumentoCSV;
	}

	protected void assertEquals(DocumentoVO documentoVO, InfoDocumentoCSV infoDocumentoCSV) {

		Assert.assertNotNull(documentoVO);
		Assert.assertNotNull(infoDocumentoCSV);

		Assert.assertEquals(documentoVO.getId(), infoDocumentoCSV.getId());
		Assert.assertEquals(documentoVO.getNombre(), infoDocumentoCSV.getNombre());
		Assert.assertEquals(documentoVO.getTipoMIME(), infoDocumentoCSV.getTipoMime());
		Assert.assertEquals(documentoVO.getFechaCreacion().toString(), infoDocumentoCSV.getFechaCreacion().toString());
		Assert.assertEquals(documentoVO.getFechaCaducidad().toString(), infoDocumentoCSV.getFechaCaducidad().toString());
		Assert.assertEquals(documentoVO.getAplicacion().getCodigo(), infoDocumentoCSV.getCodigoAplicacion());
		Assert.assertEquals(documentoVO.getAplicacion().getNombre(), infoDocumentoCSV.getNombreAplicacion());
		Assert.assertTrue((documentoVO.isDisponible() && infoDocumentoCSV.isDisponible())
				|| (!documentoVO.isDisponible() && !infoDocumentoCSV.isDisponible()));
		Assert.assertEquals(documentoVO.getCsv(), infoDocumentoCSV.getCsv());
		Assert.assertEquals(documentoVO.getFechaCSV().toString(), infoDocumentoCSV.getFechaCSV().toString());

		// Descripciones
		Map<String, String> descripcionesVO = DocumentoHelper.getDescripcionesMap(documentoVO.getDescripcion());
		Assert.assertEquals(descripcionesVO.size(), infoDocumentoCSV.getDescripciones().size());
		for (String key : descripcionesVO.keySet()) {
			Assert.assertEquals(descripcionesVO.get(key), infoDocumentoCSV.getDescripciones().get(key));
		}
	}
}
