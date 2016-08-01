package es.ieci.tecdoc.fwktd.csv.api.manager.impl;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import es.ieci.tecdoc.fwktd.core.model.Entity;
import es.ieci.tecdoc.fwktd.csv.api.manager.AplicacionManager;
import es.ieci.tecdoc.fwktd.csv.api.manager.DocumentoManager;
import es.ieci.tecdoc.fwktd.csv.api.vo.DocumentoVO;
import es.ieci.tecdoc.fwktd.csv.api.xml.connectionConfig.ConfiguracionConexion;
import es.ieci.tecdoc.fwktd.server.manager.BaseManager;
import es.ieci.tecdoc.fwktd.test.db.annotation.DatasetLocation;

@DatasetLocation("data/dataset.xml")
@ContextConfiguration({
	"/beans/fwktd-csv-api-applicationContext.xml",
	"/beans/fwktd-csv-test-beans.xml" })
public class DocumentoManagerImplTest extends BaseManagerTest {

	protected static final String ID_APLICACION_EXISTENTE = "20000001";

	protected static final String ID_DOCUMENTO_EXISTENTE = "10000001";
	protected static final String ID_DOCUMENTO_NO_EXISTENTE = "99999999";

	protected static final String CSV_DOCUMENTO_EXISTENTE = "00001";

	@Autowired
	private AplicacionManager fwktd_sir_aplicacionManager;

	@Autowired
	private DocumentoManager fwktd_sir_documentoManager;

	public AplicacionManager getAplicacionManager() {
		return fwktd_sir_aplicacionManager;
	}

	public DocumentoManager getDocumentoManager() {
		return fwktd_sir_documentoManager;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BaseManager<Entity, String> getManager() {
		return (BaseManager) getDocumentoManager();
	}

	public String getIdExistente() {
		return ID_DOCUMENTO_EXISTENTE;
	}

	public String getIdNoExistente() {
		return ID_DOCUMENTO_NO_EXISTENTE;
	}

	public String getIdBorrar() {
		return ID_DOCUMENTO_EXISTENTE;
	}

	@Test
	public void testGet() {
		DocumentoVO documento = getDocumentoManager().get(getIdExistente());
		assertDocumento(documento);
	}

	@Test
	public void testGetDocumentoByCSV() {
		DocumentoVO documento = getDocumentoManager().getDocumentoByCSV(CSV_DOCUMENTO_EXISTENTE);
		assertDocumento(documento);
	}

	@Test
	public void testGetConfiguracionConexion() {
		DocumentoVO documento = getDocumentoManager().get(ID_DOCUMENTO_EXISTENTE);
		ConfiguracionConexion config = getDocumentoManager().getConfiguracionConexion(documento);
		Assert.assertNotNull("No se ha encontrado la configuraci�n de conexi�n", config);
	}

	@Test
	public void testSave() {

		DocumentoVO documento = createDocumentoVO();

		DocumentoVO documentoCreado = (DocumentoVO) getDocumentoManager().save(documento);
		assertEquals(documento, documentoCreado);
	}

	@Test
	public void testUpdate() {

		DocumentoVO documento = getDocumentoManager().get(getIdExistente());
		documento.setDisponible(false);

		DocumentoVO documentoModificado = getDocumentoManager().update(documento);
		assertEquals(documento, documentoModificado);
	}

	@Test
	public void testDeleteDocumentoByCSV() {
		int count = getDocumentoManager().count();
		getDocumentoManager().deleteDocumentoByCSV(CSV_DOCUMENTO_EXISTENTE);
		Assert.assertEquals(getDocumentoManager().count(), count-1);
	}

	private void assertDocumento(DocumentoVO documento) {

		Assert.assertNotNull("No se ha obtenido el documento", documento);
		Assert.assertEquals(getIdExistente(), documento.getId());
		Assert.assertEquals("Documento_1.pdf", documento.getNombre());
		Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?><labels><label locale='default'>[default] Descripci�n del documento 1</label><label locale='ca'>[ca] Descripci�n del documento 1</label><label locale='es'>[es] Descripci�n del documento 1</label><label locale='eu'>[eu] Descripci�n del documento 1</label><label locale='gl'>[gl] Descripci�n del documento 1</label></labels>", documento.getDescripcion());
		Assert.assertEquals("application/pdf", documento.getTipoMIME());
		Assert.assertEquals("Sat Aug 06 12:07:31 CEST 2011", String.valueOf(documento.getFechaCreacion()));
		Assert.assertEquals("Thu Aug 06 12:07:31 CEST 2020", String.valueOf(documento.getFechaCaducidad()));
		Assert.assertEquals("00001", documento.getCsv());
		Assert.assertEquals("Sat Aug 06 12:07:31 CEST 2011", String.valueOf(documento.getFechaCSV()));
		Assert.assertTrue(documento.isDisponible());
		Assert.assertNotNull("No se ha obtenido la aplicaci�n del documento", documento.getAplicacion());
		Assert.assertEquals("20000001", documento.getAplicacion().getId());
		Assert.assertEquals("APP1", documento.getAplicacion().getCodigo());
		Assert.assertEquals("Aplicaci�n 1", documento.getAplicacion().getNombre());
		Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?><connection-config><connector>MOCK</connector></connection-config>", documento.getAplicacion().getInfoConexion());
	}

	private void assertEquals(DocumentoVO documento, DocumentoVO documento2) {

		Assert.assertNotNull("El documento 1 es nulo", documento);
		Assert.assertNotNull("El documento 2 es nulo", documento2);

		Assert.assertEquals(documento.getId(), documento2.getId());
		Assert.assertEquals(documento.getNombre(), documento2.getNombre());
		Assert.assertEquals(documento.getDescripcion(), documento2.getDescripcion());
		Assert.assertEquals(documento.getTipoMIME(), documento2.getTipoMIME());
		Assert.assertEquals(String.valueOf(documento.getFechaCreacion()), String.valueOf(documento2.getFechaCreacion()));
		Assert.assertEquals(String.valueOf(documento.getFechaCaducidad()), String.valueOf(documento2.getFechaCaducidad()));
		Assert.assertEquals(documento.getCsv(), documento2.getCsv());
		Assert.assertEquals(String.valueOf(documento.getFechaCSV()), String.valueOf(documento2.getFechaCSV()));
		Assert.assertEquals(documento.isDisponible(), documento2.isDisponible());

		Assert.assertNotNull("El documento 1 no tiene aplicaci�n asociada", documento.getAplicacion());
		Assert.assertNotNull("El documento 2 no tiene aplicaci�n asociada", documento2.getAplicacion());

		Assert.assertEquals(documento.getAplicacion().getId(), documento2.getAplicacion().getId());
		Assert.assertEquals(documento.getAplicacion().getCodigo(), documento2.getAplicacion().getCodigo());
		Assert.assertEquals(documento.getAplicacion().getNombre(), documento2.getAplicacion().getNombre());
		Assert.assertEquals(documento.getAplicacion().getInfoConexion(), documento2.getAplicacion().getInfoConexion());
	}

	private DocumentoVO createDocumentoVO() {

		DocumentoVO documento = new DocumentoVO();
		documento.setNombre("Documento.txt");
		documento.setDescripcion("Descripci�n del documento");
		documento.setTipoMIME("text/plain");
		documento.setFechaCreacion(new Date());
		documento.setFechaCaducidad(new Date());
		documento.setCsv("CSV_X");
		documento.setFechaCSV(new Date());
		documento.setDisponible(true);
		documento.setAplicacion(getAplicacionManager().get(ID_APLICACION_EXISTENTE));
		
		/**
		 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
		 */
		documento.setNumeroRegistro("123456789");
		documento.setFechaRegistro(new Date());
		documento.setOrigenRegistro("ORIGEN");
		documento.setDestinoRegistro("DESTINO");
		/**
		 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
		 */


		return documento;
	}
}
