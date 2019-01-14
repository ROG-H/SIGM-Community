/**
 * Copyright 2012 Agencia de Tecnolog�a y Certificaci�n Electr�nica
 */
package es.accv.arangi.base.signature.util;

/**
 * Al m�todo attached se le indicar� cu�l es el nodo a firmar
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� Manuel Guti�rrez N��ez</a>
 */
public class XAdESAttachedNodeToSignObject implements XAdESAttachedNodeToSign {
	
	String idToSign;
	
	public XAdESAttachedNodeToSignObject(String idToSign) {
		super();
		this.idToSign = idToSign;
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.util.XAdESAttachedNodeToSign#getIdToSign()
	 */
	public String getIdToSign() {
		return idToSign;
	}

	public void setIdToSign(String idToSign) {
		this.idToSign = idToSign;
	}

}
