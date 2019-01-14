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
package es.accv.arangi.base.certificate.validation;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;

/**
 * Clase que registra una traza de los pasos que se dan durante el proceso de validaci�n de
 * un certificado. Para ver la traza usar el m�todo {@link #toString() toString}. <br><br>
 * Ejemplo de traza tal y como se obtiene con el m�todo (puede comprobarse como se realiza 
 * la validaci�n del certificado y su emisor en la misma CRL):<br><br>
 * 
 * ---->CERTIFICATE: 1.3.6.1.4.1.16533.30.1=EMPLEADO,SURNAME=EMPLEADO,GIVENNAME=DEMO,SERIALNUMBER=08967425R,1.3.6.1.4.1.4710.1.3.2=Q2863006I,T=Personal,ST=Madrid,C=ES,O=Consejo General de la Abogac�a Espa�ola / CGAE / 2000,OU=Infarmatica,CN=NOMBRE EMPLEADO EMPLEADO DEMO - NIF 08967429R,E=demo.empleado@cgae.redabogacia.org<br>
 * ----** Get CRL from http://www.acabogacia.org/crl/ACAcorporativos.crl<br>
 * ----** CRL returns response 0: valid certificate<br>
 * -------->CERTIFICATE: C=ES,L=Madrid,E=ac@acabogacia.org,O=Consejo General de la Abogacia NIF:Q-2863006I,OU=Autoridad de Certificacion de la Abogacia,CN=ACA - Certificados Corporativos<br>
 * --------** Get CRL from http://www.acabogacia.org/crl/acabogacia.crl<br>
 * --------** CRL returns response 0: valid certificate<br>
 * ------------>CERTIFICATE: C=ES,O=Consejo General de la Abogacia NIF:Q-2863006I,CN=Autoridad de Certificacion de la Abogacia<br>
 * ------------** Certificate is self signed, end of chain validation <br>
 * --------** Validation response0: valid certificate<br>
 * ----** Validation response0: valid certificate
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class ValidatingTrace {

	/*
	 * Class' logger
	 */
	Logger logger = Logger.getLogger(ValidatingTrace.class);
	
	/*
	 * Object to store trace
	 */
	Hashtable<X509Certificate, List<String>> trace;
	
	/*
	 * guarda los certificados ordenados
	 */
	List<X509Certificate> lCertificates;
	
	/**
	 * Constructor por defecto
	 */
	public ValidatingTrace () {
		this.trace = new Hashtable<X509Certificate, List<String>> ();
		this.lCertificates = new ArrayList<X509Certificate>();
	}
	
	/**
	 * A�ade una nueva l�nea a la traza.
	 * 
	 * @param certificate Certificado que origina la validaci�n
	 * @param message Mensaje a a�adir a la traza
	 */
	public void add (X509Certificate certificate, String message) {
		
		logger.debug ("[ValidatingTrace.add] :: Enter");
		
		//-- If certificate is null -> don't add row
		if (certificate == null) {
			return;
		}
		
		//-- Add row
		if (trace.containsKey(certificate)) {
			((List<String>)trace.get(certificate)).add(message);
		} else {
			List<String> lMessages = new ArrayList<String>();
			lMessages.add(message);
			trace.put(certificate, lMessages);
			lCertificates.add(certificate);
		}
		
	}
	
	/**
	 * Devuelve una vista de la traza como XML)
	 */
	public String toString() {
		
		logger.debug ("[ValidatingTrace.toString] :: Enter");
		
		//-- Result
		StringBuffer sb = new StringBuffer ("");
		
		//-- iterate
		for (Iterator<X509Certificate> iterator = lCertificates.iterator(); iterator.hasNext();) {
			X509Certificate x509Certificate = iterator.next();
			Certificate certificate;
			try {
				certificate = new Certificate (x509Certificate);
			} catch (NormalizeCertificateException e) {
				logger.info("[ValidatingTrace.toString]::El certificado no puede ser normalizado::" + x509Certificate, e);
				return sb.toString();
			}
			List<String> lMessages = trace.get(certificate.toX509Certificate());
			
			sb.append ("<certificate>");
			sb.append ("<common_name>" + certificate.getCommonName() + "</common_name>");
			sb.append ("<issuer>" + certificate.getIssuerCommonName() + "</issuer>");
			
			for (Iterator<String> iterator2 = lMessages.iterator(); iterator2.hasNext();) {
				sb.append ("<message>" + iterator2.next() + "</message>");
			}
			
			sb.append ("</certificate>");
		}
		
		return sb.toString();
	}

	/**
	 * Devuelve una vista de la traza como HTML
	 */
	public String toHTML() {
		
		logger.debug ("[ValidatingTrace.toHTML] :: Enter");
		
		//-- Result
		StringBuffer sb = new StringBuffer ("");
		
		//-- iterate
		for (Iterator<X509Certificate> iterator = lCertificates.iterator(); iterator.hasNext();) {
			X509Certificate x509Certificate = iterator.next();
			Certificate certificate;
			try {
				certificate = new Certificate (x509Certificate);
			} catch (NormalizeCertificateException e) {
				logger.info("[ValidatingTrace.toString]::El certificado no puede ser normalizado::" + x509Certificate, e);
				return sb.toString();
			}
			List<String> lMessages = trace.get(certificate.toX509Certificate());
			
			sb.append ("<div class='trace_certificate'>");
			sb.append ("<p>" + certificate.getCommonName() + "</p>");
			sb.append ("<p>" + certificate.getIssuerCommonName() + "</p>");

			sb.append ("<div class='trace_messages'>");
			for (Iterator<String> iterator2 = lMessages.iterator(); iterator2.hasNext();) {
				sb.append ("<p>" + iterator2.next() + "</p>");
			}
			sb.append ("</div>");
			
			sb.append ("</div>");
		}
		
		return sb.toString();
	}
		

}
