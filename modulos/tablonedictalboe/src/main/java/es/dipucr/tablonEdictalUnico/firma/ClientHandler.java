package es.dipucr.tablonEdictalUnico.firma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.SOAPPart;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecSignature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
/**
 * Clase encargada de securizar los mensajes SOAP de petici�n realizados desde
 * un cliente.
 *
 * @author SEPAOT
 *
 */
public class ClientHandler extends BasicHandler {
	
	public static final Logger logger = Logger.getLogger(ClientHandler.class);
 
    private static final long serialVersionUID = 1L;
 
    // Opciones de seguridad
 
    // Localizaci�n del keystore con certificado y clave privada de usuario
    private String keystoreLocation = null;
 
    // Tipo de keystore
    private String keystoreType = null;
 
    // Clave del keystore
    private String keystorePassword = null;
 
    // Alias del certificado usado para firmar el tag soapBody de la petici�n y
    // que ser� alojado en el token BinarySecurityToken
    private String keystoreCertAlias = null;
 
    // Password del certificado usado para firmar el tag soapBody de la petici�n
    // y que ser� alojado en el token BinarySecurityToken
    private String keystoreCertPassword = null;
 
    /**
     * Constructor que inicializa el atributo securityOption
     *
     * @param securityOption
     *            opci�n de seguridad.
     * @throws Exception
     */
    public ClientHandler(Properties config) {
        if (config == null) {
            logger.error("Fichero de configuracion de propiedades nulo");
            System.exit(-1);
        }
        try {
 
            keystoreLocation = config.getProperty("security.keystore.location");
            keystoreType = config.getProperty("security.keystore.type");
            keystorePassword = config.getProperty("security.keystore.password");
            keystoreCertAlias = config.getProperty("security.keystore.cert.alias");
            keystoreCertPassword = config.getProperty("security.keystore.cert.password");
 
        } catch (Exception e) {
        	logger.error("Error leyendo el fichero de configuraci�n de securizaci�n.", e);
            System.exit(-1);
        }
    }
 
    public void invoke(MessageContext msgContext) throws AxisFault {
        SOAPMessage secMsg = null;
        try {
 
            ((SOAPPart) msgContext.getRequestMessage().getSOAPPart()).setCurrentMessage(this.createBinarySecurityToken(msgContext),
                    SOAPPart.FORM_SOAPENVELOPE);
 
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            System.exit(-1);
        }
    }
 
    /**
     * Securiza, mediante el tag BinarySecurityToken y firma una petici�n SOAP
     * no securizada.
     *
     * @param soapEnvelopeRequest
     *            Documento xml que representa la petici�n SOAP sin securizar.
     * @return Un mensaje SOAP que contiene la petici�n SOAP de entrada
     *         securizada mediante el tag BinarySecurityToken.
     */
    private SOAPEnvelope createBinarySecurityToken(MessageContext msgContext) {
        ByteArrayOutputStream baos;
        Crypto crypto;
        Document secSOAPReqDoc,soapEnvelopeRequest;
        DOMSource source;
        Element element;
        StreamResult streamResult;
        String secSOAPReq;
        WSSecSignature wsSecSignature;
        WSSecHeader wsSecHeader;
        SOAPMessage msg;
 
        try {
 
            // Obtenci�n del documento XML que representa la petici�n SOAP
            msg = msgContext.getCurrentMessage();
            soapEnvelopeRequest = ((org.apache.axis.message.SOAPEnvelope) msg.getSOAPPart().getEnvelope()).getAsDocument();
 
            // Inserci�n del tag wsse:Security y BinarySecurityToken
            wsSecHeader = new WSSecHeader(null, false);
            wsSecSignature = new WSSecSignature();
            crypto = CryptoFactory.getInstance("org.apache.ws.security.components.crypto.Merlin", this.initializateCryptoProperties());
            // Indicaci�n para que inserte el tag BinarySecurityToken
            wsSecSignature.setKeyIdentifierType(WSConstants.BST_DIRECT_REFERENCE);
            // wsSecSignature.setKeyIdentifierType(WSConstants.ISSUER_SERIAL);
            wsSecSignature.setUserInfo(this.keystoreCertAlias, this.keystoreCertPassword);
            wsSecHeader.insertSecurityHeader(soapEnvelopeRequest);
            wsSecSignature.prepare(soapEnvelopeRequest, crypto, wsSecHeader);
            // Modificaci�n y firma de la petici�n
            secSOAPReqDoc = wsSecSignature.build(soapEnvelopeRequest, crypto, wsSecHeader);
            element = secSOAPReqDoc.getDocumentElement();
            // Transformaci�n del elemento DOM a String
            source = new DOMSource(element);
            baos = new ByteArrayOutputStream();
            streamResult = new StreamResult(baos);
            TransformerFactory.newInstance().newTransformer().transform(source, streamResult);
            secSOAPReq = new String(baos.toByteArray());
 
            // Creaci�n de un nuevo mensaje SOAP a partir del mensaje SOAP
            // securizado formado
            Message axisMessage = getAxisMessage(secSOAPReq,msgContext);
            return axisMessage.getSOAPEnvelope();
 
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            System.exit(-1);
            return null;
 
        }
    }
 
    /**
     * Establece el conjunto de propiedades con el que ser� inicializado el
     * gestor criptogr�fico de WSS4J.
     *
     * @return Devuelve el conjunto de propiedades con el que ser� inicializado
     *         el gestor criptogr�fico de WSS4J.
     */
    private Properties initializateCryptoProperties() {
        Properties res = new Properties();
        res.setProperty("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
        res.setProperty("org.apache.ws.security.crypto.merlin.keystore.type", this.keystoreType);
        res.setProperty("org.apache.ws.security.crypto.merlin.keystore.password", this.keystorePassword);
        res.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias", this.keystoreCertAlias);
        res.setProperty("org.apache.ws.security.crypto.merlin.alias.password", this.keystoreCertPassword);
        res.setProperty("org.apache.ws.security.crypto.merlin.file", this.keystoreLocation);
        return res;
    }
 
    /**
     * Creates and returns an Axis message from a SOAP envelope string.
     *
     * @param unsignedEnvelope
     *            a string containing a SOAP envelope
     * @return <code>Message</code> the Axis message
     */
    private Message getAxisMessage(String unsignedEnvelope, MessageContext msgContext) {
        InputStream inStream = new ByteArrayInputStream(unsignedEnvelope.getBytes());
        Message axisMessage = new Message(inStream);
        axisMessage.setMessageContext(msgContext);
        return axisMessage;
    }
}