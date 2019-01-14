package es.dipucr.sigem.api.rule.common.utils;

public class DIR3IneUtils {

	public static final String INITIAL_CODE_CHAR = "L"; //Letra inicial en las tablas scr_prov y scr_cities del campo "code"
	
	public static String getCodDir3Provincia(String codProvincia){
		return codProvincia.replace(INITIAL_CODE_CHAR, "");
	}
	
	/**
	 * Viene un c�digo de 9 d�gitos, el del ayuntamiento, y nos quedamos con los 4 �ltimos, el municipio f�sico
	 * Ejemplo: L01139014 -> 9014 
	 * @param codMunicipioAyto
	 * @return
	 */
	public static String getCodDir3Municipio(String codMunicipioAyto){
		return codMunicipioAyto.substring(5);
	}
}
