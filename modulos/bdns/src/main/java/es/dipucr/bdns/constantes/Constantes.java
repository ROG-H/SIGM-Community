package es.dipucr.bdns.constantes;

public class Constantes {
	
	public static final String DIR3_ESPA�A = "ES";
	
	public static final String NIF_IGAE = "S2826015F";
	public static final String NOMBRE_IGAE = "IGAE";
	
	public static final String DIR3_CENPRI = "LA0006599";
	public static final String NOMBRE_CENPRI = "CENTRO PROVINCIAL DE INFORM�TICA";
	
	public static final String DIR3_DIPUCR = "L02000013";
	public static final String NOMBRE_DIPUCR = "DIPUTACI�N PROVINCIAL DE CIUDAD REAL";
	public static final String TIPOMOVIMIENTO_ALTA = "ALTA";
	public static final Object TIPOMOVIMIENTO_MODIFICACION = "MODIFICACION";
	public static final Object TIPOMOVIMIENTO_BAJA = "BAJA";
	public static final String NUTS_PROV_CIUDADREAL = "ES422";
	
	public interface TIPO_TERCERO{
		/** PERSONAS F�SICAS QUE NO DESARROLLAN ACTIVIDAD ECON�MICA **/
		public static final String FISICAS_NO_ECO = "FSA";
		/** PERSONAS JUR�DICAS QUE NO DESARROLLAN ACTIVIDAD ECON�MICA **/
		public static final String JURIDICAS_NO_ECO = "JSA";
		/** PYME Y PERSONAS F�SICAS QUE DESARROLLAN ACTIVIDAD ECON�MICA **/
		public static final String PYME_FIS_SI_ECO = "PFA";
		/** GRAN EMPRESA **/
		public static final String GRAN_EMPRESA = "GRA";
	}
	
	/** C�digo en la columna NUM_ACTO de SPAC_DT_DOCUMENTOS para identificar el documento resoluci�n de una subvenci�n **/
	public static final String COD_DTDOC_NUMACTO_BDNS = "BDNS";

	/**
	 * Permite al requirente el env�o de la informaci�n relativa a la normativa que rige la 
	 * gesti�n de la subvenci�n o de cualquier otra ayuda p�blica.
	 * **/
	public static String COD_APP_CONVOCATORIAS = "BDNSCONVOC";
	
	/**
	 * Permite al requirente el env�o de informaci�n relativa a los datos personales de cada uno de los terceros 
	 * sobre los que est� obligado a facilitar alg�n tipo de informaci�n (beneficiario de una subvenci�n, sancionado, 
	 * inhabilitado o part�cipe de un proyecto).
	 * **/
	public static String COD_APP_DATOSPERSONALES = "BDNSDATPER";
	
	/**
	 * Permite al requirente el env�o de la informaci�n relativa a concesiones (subvenci�n, pr�stamo, aval�), 
	 * pagos a los beneficiarios y proyectos (ejecuci�n material de la actividad con detalle sobre los compromisos 
	 * asumidos en el tiempo por sus ejecutores). Se han unificado estos tres conceptos en un mismo servicio web, 
	 * pero en una misma petici�n (llamada al servicio) se admitir� �nicamente informaci�n, de concesiones, de pagos o bien de proyectos.
	 * **/
	public static String COD_APP_CONCESIONES = "BDNSCONCPAGPRY";
	
	/**
	 * Permite al requirente el env�o de informaci�n relativa a devoluciones voluntarias a iniciativa del beneficiario, 
	 * y reintegros procedentes de subvenci�n. Se han unificado estos dos conceptos en un mismo servicio web, pero en una misma 
	 * petici�n (llamada al servicio) se admitir� �nicamente informaci�n, o bien de devoluciones o de reintegros.
	 * **/
	public static String COD_APP_DEVOLUCIONES = "BDNSDEVOLREINT";
	
	/**
	 * Permite al requirente el env�o de informaci�n relativa a resoluciones firmes del procedimiento sancionador 
	 * (sanciones pecuniarias impuestas en aplicaci�n de la LGS) as� como las inhabilitaciones. Se han unificado estos 
	 * dos conceptos en un mismo servicio web, pero en una misma petici�n (llamada al servicio) se admitir� �nicamente 
	 * informaci�n, o bien de sanciones o de inhabilitaciones.
	 * **/
	public static String COD_APP_CODSANCIONADOR = "BDNSSANCINH";
		
}
