/**
 * Copyright 2012 Agencia de Tecnolog�a y Certificaci�n Electr�nica
 */
package es.accv.arangi.base.signature.util;


/**
 * A partir de la versi�n 1.4.7 se agrupan todas las opciones que se pueden pasar
 * a una firma XAdES attached en este objeto.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� Manuel Guti�rrez N��ez</a>
 */
public class XAdESAttachedSignatureOptions extends XAdESDetachedSignatureOptions {
	
	private XAdESAttachedNodeToSign nodeToSign;
	
	public XAdESAttachedSignatureOptions() {
		super();
	}
	
	public XAdESAttachedSignatureOptions(String digitalSignatureAlgorithm,
			String tsaHashingAlgorithm,
			XAdESDataObjectFormat dof,
			String[] claimedRoles,
			ArangiXAdESPolicyIdentifier policyIdentifier,
			ArangiXAdESProductionPlace productionPlace,
			XAdESAttachedNodeToSign nodeToSign) {
		super(digitalSignatureAlgorithm, tsaHashingAlgorithm, dof, claimedRoles, policyIdentifier, productionPlace);
		this.nodeToSign = nodeToSign;
	}

	public XAdESAttachedNodeToSign getNodeToSign() {
		return nodeToSign;
	}

	public void setNodeToSign(XAdESAttachedNodeToSign nodeToSign) {
		this.nodeToSign = nodeToSign;
	}
	
	@Override
	public String toString() {
		return 	super.toString() +
				"XAdESAttachedNodeToSign=" + nodeToSign;

	}
}
