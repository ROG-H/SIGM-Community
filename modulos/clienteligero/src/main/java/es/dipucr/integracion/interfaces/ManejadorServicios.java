package es.dipucr.integracion.interfaces;

import org.w3c.dom.Element;



/**
 * Define aquellos m�todos comunes que todos las clases van a tener  que utilizar para el procesamiento del servicio Web.
 * Principalmente se encarga de asignar correctamente los namespaces correctos, crear el elemento que viajar� en el nodo
 * <DatosEspecificos> y obtener los datos relevantes de este elemento.
 */
public interface ManejadorServicios {

	
	/**
	 * @return texto correspondiente al namespace usado en la petici�n. 
	 * Este m�todo solo tiene sentido cuando se usa el cliente del Recubrimiento ya que el cliente
	 * de las librer�as SCSP asigna los namespaces. Es decir, para el recubrimiento se usar� este:
	 * 
	 * http://intermediacion.redsara.es/scsp/esquemas/ws/peticion
	 */
	public String getNameSpaceRequest();
	
	
	/**
	 * @return texto correspondiente al namespace usado en las respuestas dadas por los servicios.
	 * Lo normal es que retorne:
	 * Servicios V2: "http://www.map.es/scsp/esquemas/V2/respuesta";
	 * Servicios V3: "http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta";
	 * 
	 * Si alg�n servicio usa otros namespaces, sobreescribiendo este m�todo en su clase podremos
	 * adaptarnos. Se usa para asignar los namespaces para luego buscar elementos por Xpath. 
	 * Hay varias maneras de obtener los datos: recorriendo los nodos, mediante Xpath, etc..
	 */
	public String getNameSpaceResponse();
	
	
	/**
	 * @return texto correspondiente al namespace usado por el servicio para los datos espec�ficos.
	 * Lo normal es que retorne:
	 * Servicios V2: "http://www.map.es/scsp/esquemas/datosespecificos";
	 * Servicios V3: "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos";
	 */
	public String getNameSpaceDatosEspecificos();
	
	
	/**
	 * @return texto correspondiente al namespace usado en el nodo ConfirmacionPeticion 
	 * de los servicos as�ncronos.
	 * 
	 */
	public String getNameSpaceConfirmationRequest();

	
	/**
	 * @return texto correspondiente al namespace usado para una petici�n de este tipo.
	 * 
	 * Este m�todo solo tiene sentido cuando se usa el cliente del Recubrimiento ya que el cliente
	 * de las librer�as SCSP asigna los namespaces
	 */
	public String getNameSpaceSolicitudRespuesta();
	
	
	/**
	 * Devuele el objeto que contiene el nodo correspondiente al esquema de datos-especificos.xsd de cada servicio.
	 * Para los servicios que no usen este nodo en su esquema de peticion.xsd se devolver� null. 
	 * Para el resto se formar� la estructura personalizada acorde a su esquema espec�fico.
	 * 
	 * @return Objeto de tipo org.w3c.dom.Element que contiene la estructura  de los datos espec�ficos.
	 * @throws Exception
	 */
	public Element createDatosEspecificos() throws Exception;

	
	
	/**
	 * @param datosEspecificos Objeto de tipo org.w3c.dom.Element que contiene la estructura de los datos espec�ficos.
	 * @return Texto formateado con la informaci�n de los datos especif�ficos que se quiere mostrar por pantalla.
	 * @throws Exception
	 */
	public String getDatosEspecificosOutput(Element datosEspecificos) throws Exception;
	
	


	
}
