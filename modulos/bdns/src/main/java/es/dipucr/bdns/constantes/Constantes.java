package es.dipucr.bdns.constantes;

public class Constantes {
	public static final String NIFIGAE = "S2826015F";
	public static String nombreIGAE = "IGAE";
	
	public static final String DIR3Cenpri = "U03500057";
	public static String nombreCenpri = "SERVICIO INFORM�TICO";
	
	public static final String DIR3Dipu = "U03500001";
	public static final String TIPOMOVIMIENTO_ALTA = "ALTA";
	public static final Object TIPOMOVIMIENTO_MODIFICACION = "MODIFICACION";
	public static final Object TIPOMOVIMIENTO_BAJA = "BAJA";
	public static String nombreDipu = "UNIVERSIDAD P�BLICA DE NAVARRA";


	/**
	 * Permite al requirente el env�o de la informaci�n relativa a la normativa que rige la 
	 * gesti�n de la subvenci�n o de cualquier otra ayuda p�blica.
	 * **/
	public static String codConvocatoria = "BDNSCONVOC";
	
	/**
	 * Permite al requirente el env�o de informaci�n relativa a los datos personales de cada uno de los terceros 
	 * sobre los que est� obligado a facilitar alg�n tipo de informaci�n (beneficiario de una subvenci�n, sancionado, 
	 * inhabilitado o part�cipe de un proyecto).
	 * **/
	public static String codDatosPersonales = "BDNSDATPER";
	
	/**
	 * Permite al requirente el env�o de la informaci�n relativa a concesiones (subvenci�n, pr�stamo, aval�), 
	 * pagos a los beneficiarios y proyectos (ejecuci�n material de la actividad con detalle sobre los compromisos 
	 * asumidos en el tiempo por sus ejecutores). Se han unificado estos tres conceptos en un mismo servicio web, 
	 * pero en una misma petici�n (llamada al servicio) se admitir� �nicamente informaci�n, de concesiones, de pagos o bien de proyectos.
	 * **/
	public static String codConcesiones = "BDNSCONCPAGPRY";
	
	/**
	 * Permite al requirente el env�o de informaci�n relativa a devoluciones voluntarias a iniciativa del beneficiario, 
	 * y reintegros procedentes de subvenci�n. Se han unificado estos dos conceptos en un mismo servicio web, pero en una misma 
	 * petici�n (llamada al servicio) se admitir� �nicamente informaci�n, o bien de devoluciones o de reintegros.
	 * **/
	public static String codDevolucionesVol = "BDNSDEVOLREINT";
	
	/**
	 * Permite al requirente el env�o de informaci�n relativa a resoluciones firmes del procedimiento sancionador 
	 * (sanciones pecuniarias impuestas en aplicaci�n de la LGS) as� como las inhabilitaciones. Se han unificado estos 
	 * dos conceptos en un mismo servicio web, pero en una misma petici�n (llamada al servicio) se admitir� �nicamente 
	 * informaci�n, o bien de sanciones o de inhabilitaciones.
	 * **/
	public static String codProcSancionador = "BDNSSANCINH";
		
}
