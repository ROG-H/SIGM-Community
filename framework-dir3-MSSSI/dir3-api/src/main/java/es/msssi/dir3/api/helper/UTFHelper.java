/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versi�n 1.1 o �en cuanto sean aprobadas por laComisi�n Europea� versiones posteriores de la EUPL (la �Licencia�); 
 * Solo podr� usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
 * V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.dir3.api.helper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Clase de utilidad para la transformaci�n de objetos con la informaci�n de
 * unidades org�nicas.
 * 
 * @author cmorenog
 * 
 */
public class UTFHelper {
	
	public static final Logger LOGGER = Logger.getLogger(UTFHelper.class);
	
	public static String parseUTF8ToISO885916(String cadena){
		if(StringUtils.isNotEmpty(cadena)){

			cadena = cadena.replaceAll("�", "'");
			cadena = cadena.replaceAll("�", "'");
			cadena = cadena.replaceAll("�", "'");

			cadena = cadena.replaceAll("�", "\"");
			cadena = cadena.replaceAll("�", "\"");

			cadena = cadena.replaceAll("�", "\"");
			cadena = cadena.replaceAll("�", "\"");

			cadena = cadena.replaceAll("�", "-");

			cadena = cadena.replaceAll("�", ".");

			cadena = cadena.replaceAll("�", "�");
		}
		return cadena;
	}
}