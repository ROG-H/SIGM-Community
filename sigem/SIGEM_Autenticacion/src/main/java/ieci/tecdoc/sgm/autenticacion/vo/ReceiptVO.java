/**
 * 
 */
package ieci.tecdoc.sgm.autenticacion.vo;

import java.util.Date;

/**
 * @author IECISA
 * 
 * Almacena el contenido de un documento y el CSV asociado a dicho documento.
 *
 */
public class ReceiptVO {

	/**
	 * C�digo Seguro de Verificaci�n del justificante
	 */
	private String csv;
	
	/**
	 * Array de bytes con el contenido del documento
	 */
	private byte[] content;
	
	/**
	 * Fecha de firma del documento
	 */
	private Date fechaFirma;

	/**
	 * @return el csv
	 */
	public String getCsv() {
		return csv;
	}

	/**
	 * @param csv el csv a fijar
	 */
	public void setCsv(String csv) {
		this.csv = csv;
	}

	/**
	 * @return el content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content el content a fijar
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @return la fecha de firma
	 */
	public Date getFechaFirma() {
		return fechaFirma;
	}

	/**
	 * @param fechaFirma la fecha de firma
	 */
	public void setFechaFirma(Date fechaFirma) {
		this.fechaFirma = fechaFirma;
	}
}
