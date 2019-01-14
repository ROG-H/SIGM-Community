/**
 * Copyright 2012 Agencia de Tecnolog�a y Certificaci�n Electr�nica
 */
package es.accv.arangi.base.signature.util;

import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SignaturePolicyIdentifier;

/**
 * P�litica impl�cita
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� Manuel Guti�rrez N��ez</a>
 */
public class ArangiXAdESPolicyImplied {

	private SignaturePolicyIdentifier spi;

	public ArangiXAdESPolicyImplied() {
		super();
		spi = new SignaturePolicyIdentifier(XAdESSchemas.XAdES_132);
		spi.setSignaturePolicyImplied();
	}


	
}
