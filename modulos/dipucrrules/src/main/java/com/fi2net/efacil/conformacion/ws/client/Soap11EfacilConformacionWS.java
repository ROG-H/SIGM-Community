/*
 * Plan Avanza Local Soluciones � e-f�cil � Copyright � 2011 � Ministerio de Industria, Turismo y 
 * Comercio.
 *
 * Este programa es software libre, por ello est� permitido redistribuirlo y/o modificarlo bajo los 
 * t�rminos de la GNU General Public License, en su versi�n 3, publicada por la Free Software 
 * Foundation.
 *
 * Junto con este programa debe haber recibido una copia de la GNU General Public License, en 
 * caso contrario puede consultarla en <http://www.gnu.org/licenses/>.
 *
 * El presente programa posee las siguientes cl�usulas particulares de licencia GPL v3:
 *
 *  - Queda restringido su uso a Administraciones P�blicas, en el �mbito de sus 
 * competencias legalmente establecidas y con la finalidad de utilidad p�blica e inter�s 
 * social;
 *  - La distribuci�n y el uso del software tienen car�cter gratuito;
 *  - Ni el software ni ninguno de los M�dulos que lo componen ser�n usados ni 
 * sublicenciados, bajo ninguna circunstancia, con fines o intereses comerciales, bien sea 
 * directa o indirectamente, por parte de la Administraci�n P�blica, sus funcionarios, 
 * empleados o cualquier otra persona u organizaci�n dentro o fuera de la 
 * administraci�n.
 *
 */

/**
 * Soap11EfacilConformacionWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.fi2net.efacil.conformacion.ws.client;

public interface Soap11EfacilConformacionWS extends javax.xml.rpc.Service {
    public java.lang.String getSoap11HttpEndpointAddress();

    public com.fi2net.efacil.conformacion.ws.client.IEFacilConformacionFacturasService getSoap11HttpEndpoint() throws javax.xml.rpc.ServiceException;

    public com.fi2net.efacil.conformacion.ws.client.IEFacilConformacionFacturasService getSoap11HttpEndpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
