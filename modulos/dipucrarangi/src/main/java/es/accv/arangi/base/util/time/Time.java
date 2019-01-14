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
package es.accv.arangi.base.util.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

import org.apache.log4j.Logger;

import es.accv.arangi.base.util.time.util.NtpMessage;

/**
 * Clase para obtener la fecha y hora de un servidor SNTP
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class Time {

	/*
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(Time.class);
	
	/**
	 * Servidor SNTP por defecto: servidor del Real Instituto y Observatorio
	 * de la Armada (<i>hora.roa.es</i>)
	 */
	public static String URL_DEFAULT_SNTP_SERVER = "hora.roa.es";
	
	
	/**
	 * M�todo que llama al servidor SNTP por defecto (servidor del Real 
	 * Instituto y Observatorio de la Armada) para obtener la fecha y hora 
	 * actuales.
	 * 
	 * @return Fecha y hora actuales
	 * @throws IOException Ha ocurrido un error obteniendo el resultado del
	 * 	servidor
	 */
	public static Date getTime () throws IOException {
		return getTime (URL_DEFAULT_SNTP_SERVER);
	}
	
	/**
	 * M�todo que llama al servidor SNTP ubicado en la direcci�n pasada como 
	 * par�metro para obtener la fecha y hora actuales.
	 * 
	 * @param sntpName Direcci�n del servidor de NTP
	 * @return Fecha y hora actuales
	 * @throws IOException Ha ocurrido un error obteniendo el resultado del
	 * 	servidor
	 */
	public static Date getTime (String sntpName) throws IOException {
		logger.debug ("[Time.getTime]::Entrada::" + sntpName);
		
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			// Send request
			InetAddress address = InetAddress.getByName(sntpName);
			byte[] buf = new NtpMessage().toByteArray();
			DatagramPacket packet =
				new DatagramPacket(buf, buf.length, address, 123);
			
			// Set the transmit timestamp *just* before sending the packet
			// ToDo: Does this actually improve performance or not?
			NtpMessage.encodeTimestamp(packet.getData(), 40,
				(System.currentTimeMillis()/1000.0) + 2208988800.0);
			
			socket.send(packet);
			
			
			// Get response
			logger.debug ("[Time.getTime]::NTP request enviado, esperando respuesta...\n");
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			
			// Immediately record the incoming timestamp
			double destinationTimestamp =
				(System.currentTimeMillis()/1000.0) + 2208988800.0;
			
			return NtpMessage.timestampToDate(destinationTimestamp);
			
		} catch (IOException e) {
			logger.info("[Time.getTime]::Error de conexi�n con el servidor que se encuentra en " + sntpName, e);
			throw e;
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
	}
	
}
