/**
 * Copyright 2012 Agencia de Tecnolog�a y Certificaci�n Electr�nica
 */
package es.accv.arangi.base.signature.util;

import java.net.URI;

import es.accv.arangi.base.signature.XAdESSignature;

/**
 * Clase que representa un objeto Object identifier seg�n el punto 7.1.2 del est�ndar.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� Manuel Guti�rrez N��ez</a>
 *
 */
public class ObjectIdentifier extends es.mityc.firmaJava.libreria.xades.elementos.xades.ObjectIdentifier {
	
	public ObjectIdentifier(String description, URI uri) {
		super(XAdESSignature.DEFAULT_XADES_SCHEMA, uri, description);
	}
	
}
