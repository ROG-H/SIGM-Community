package es.dipucr.tablonEdictalUnico.commons;

public class Constantes {
	//Forma de publicaci�n. Es un dato obligatorio imprescindible para el tratamiento posterior y la forma de mostrar el anuncio. Puede tomar dos valores:
	public interface  FORMA_PUBLICACION{
		//Publicaci�n en extracto (cuando el anuncio no contiene el contenido del acto 
		//administrativo a notificar, sino �nicamente la identificaci�n del interesado y del procedimiento)
		public static final String EXTRACTO = "E";
		//Publicaci�n �ntegra (cuando en el texto del anuncio se recoge completo el contenido del acto administrativo objeto de notificaci�n)
		public static final String INTEGRA = "I";
	}
	
	public interface  DIR3_NIVEL1{
		public static final String ID = "LA9999999";
		public static final int NIVEL = 1;
		public static final String NAME = "ENTIDADES LOCALES";
	}
	public interface  DIR3_NIVEL2{
		public static final String ID = "L02000013";
		public static final int NIVEL = 2;
		public static final String NAME = "Diputaci�n Provincial de Ciudad Real";
	}
	
	//Informa sobre si el anuncio contiene datos de car�cter personal
	public interface DATOS_PERSONALES{
		public static final String NO = "N";
		public static final String SI = "S";
	}
	
	public interface PLURALIDAD{
		public static final String NO = "N";
		public static final String SI = "S";
	}
	
	public interface TIPO_PARRAFO{
		//parrafo: P�rrafo por defecto.
		public static final String PARRAFO = "parrafo";
		//titulo: P�rrafo centrado con un tipo de letra mayor que el del p�rrafo por defecto.
		public static final String TITULO = "titulo";
		//pieFirma: El elemento no tendr� contenido alguno. Representa la posici�n donde se incorpor� el texto del elemento pieFirma. De no incluirse, el pie de firma ir� al final del texto.
		public static final String PIEFIRMA = "pieFirma";
		//page-break: El elemento no tendr� contenido alguno. Fuerza un salto de p�gina a partir de este elemento.
		public static final String PAGE_BREAK = "page-break";
	}



	public static final String UTF8 = "UTF-8";



	public static final String BBDD_TRAMITADOR = "tramitador";


	//campos de la tabla TABLON_EDICTAL_BOE_DATOS
	public static final String TABLON_EDICTAL_BOE_DATOS_NUMEXP = "NUMEXP";
	public static final String TABLON_EDICTAL_BOE_DATOS_EMAIL_INCIDENCIAS = "EMAIL_INCIDENCIAS";
	public static final String TABLON_EDICTAL_BOE_DATOS_FORMA_PUBLICACION = "FORMA_PUBLICACION";
	public static final String TABLON_EDICTAL_BOE_DATOS_DATOSPERSONALES = "DATOSPERSONALES";
	public static final String TABLON_EDICTAL_BOE_DATOS_TIPOANUNCIO = "TIPOANUNCIO";
	public static final String TABLON_EDICTAL_BOE_DATOS_LGT = "LGT";
	public static final String TABLON_EDICTAL_BOE_DATOS_PROCEDIMIENTO = "PROCEDIMIENTO";
	public static final String TABLON_EDICTAL_BOE_DATOS_LUGAR = "LUGAR";
	public static final String TABLON_EDICTAL_BOE_DATOS_CARGONOMBRE = "CARGONOMBRE";
	public static final String TABLON_EDICTAL_BOE_DATOS_FECHAPUBLICACION = "FECHAPUBLICACION";
	public static final String TABLON_EDICTAL_BOE_DATOS_FECHAFIRMA = "FECHAFIRMA";
	public static final String TABLON_EDICTAL_BOE_DATOS_IDENTIFICADORBOE = "IDENTIFICADORBOE";
	public static final String TABLON_EDICTAL_BOE_DATOS_IDENTIFICADORANUNCIOBOE = "IDENTIFICADORANUNCIOBOE";
	public static final String TABLON_EDICTAL_BOE_DATOS_TRAIDOANUNCIOTEU = "TRAIDOANUNCIOTEU";
	public static final String TABLON_EDICTAL_BOE_DATOS_ID_PROC = "ID_PROC";
	public static final String TABLON_EDICTAL_BOE_DATOS_UID_DESTINATARIO = "UID_DESTINATARIO";



	public static final String AVISO_ID_AVISO = "NEXTVAL";

	
	//C�digo que indica la versi�n utilizada. Existir� compatibilidad de versiones.
	public static String version = "1.0.0";
}
