/**
 * LICENCIA LGPL:
 * 
 * Esta librer�a es Software Libre; Usted puede redistribuirla y/o modificarla
 * bajo los t�rminos de la GNU Lesser General Public License (LGPL) tal y como 
 * ha sido publicada por la Free Software Foundation; o bien la versi�n 2.1 de 
 * la Licencia, o (a su elecci�n) cualquier versi�n posterior.
 * 
 * Esta librer�a se distribuye con la esperanza de que sea �til, pero SIN 
 * NINGUNA GARANT�A; tampoco las impl�citas garant�as de MERCANTILIDAD o 
 * ADECUACI�N A UN PROP�SITO PARTICULAR. Consulte la GNU Lesser General Public 
 * License (LGPL) para m�s detalles
 * 
 * Usted debe recibir una copia de la GNU Lesser General Public License (LGPL) 
 * junto con esta librer�a; si no es as�, escriba a la Free Software Foundation 
 * Inc. 51 Franklin Street, 5� Piso, Boston, MA 02110-1301, USA o consulte
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 Agencia de Tecnolog�a y Certificaci�n Electr�nica
 */
package es.accv.arangi.base.certificate.validation.service;

import java.util.Date;
import java.util.Map;

import es.accv.arangi.base.certificate.validation.OCSPResponse;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Resultado de la validaci�n a trav�s del servicio de validaci�n de 
 * certificados.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� Manuel Guti�rrez N��ez</a>
 */
public class CertificateValidationServiceResult {

	private int result;
	
	private Map<String, Object> fields;
	
	private Date revocationDate;
	
	private int revocationReason;
	
	private OCSPResponse ocspResponse; 

	public CertificateValidationServiceResult() {
		super();
	}
	
	public CertificateValidationServiceResult(int result,
			Map<String, Object> fields) {
		super();
		this.result = result;
		this.fields = fields;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public Map<String, Object> getFields() {
		return fields;
	}

	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}

	public Date getRevocationDate() {
		return revocationDate;
	}

	public void setRevocationDate(Date revocationDate) {
		this.revocationDate = revocationDate;
	}

	public int getRevocationReason() {
		return revocationReason;
	}

	public void setRevocationReason(int revocationReason) {
		this.revocationReason = revocationReason;
	}

	public OCSPResponse getOcspResponse() {
		return ocspResponse;
	}

	public void setOcspResponse(OCSPResponse ocspResponse) {
		this.ocspResponse = ocspResponse;
	}

	public boolean isValid() {
		return getResult() == ValidationResult.RESULT_VALID;
	}
}
