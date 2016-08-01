package es.dipucr.tablonEdictalUnico.objetos;

import java.util.GregorianCalendar;
import java.util.Vector;

import es.dipucr.tablonEdictalUnico.xml.Notificados;

public class InformacionTablonEdictal {
	private String numexp;
	/**
	 * Fecha de publicaci�n solicitada para los anuncios. Si la fecha se correspondiese con un domingo, 
	 * la publicaci�n se realizar� el lunes siguiente. Si no se incluye o es incorrecta se proceder� a 
	 * publicar en la fecha m�s temprana posible conforme al procedimiento de cierre y publicaci�n que rige la publicaci�n del BOE. 
	 * La fecha se especificar� en formato ISO 8601:2004 (aaaa-mm-dd).
	 * **/
	private GregorianCalendar fechaPublicacion;
	/**
	 * Direcci�n de correo electr�nico a efectos de comunicar las incidencias que se generen en el proceso de la informaci�n.
	 * **/
	private String email;
	/**
	 * Forma de publicaci�n. Es un dato obligatorio imprescindible para el tratamiento posterior y la forma de mostrar el anuncio. Puede tomar dos valores:
	 * E: Publicaci�n en extracto (cuando el anuncio no contiene el contenido del acto administrativo a notificar, sino �nicamente la identificaci�n del interesado 
	 * y del procedimiento)
	 * I: Publicaci�n �ntegra (cuando en el texto del anuncio se recoge completo el contenido del acto administrativo objeto de notificaci�n)
	 * **/
	private String formaPublicacion;
	/**
	 * Informa sobre si el anuncio contiene datos de car�cter personal. Puede tomar los siguientes valores:
	 * N: No incluye ning�n dato de car�cter personal.
	 * S: Incluye datos de car�cter personal.
	 * **/
	private String datosPersonales;
	/**
	 * Tipo de anuncio. Por ejemplo: �catastro�, �impuestos�, �tasas�, �subvenciones� con el objetivo de facilitar la recuperaci�n 
	 * posterior en base de datos. Contendr� tantos elementos �materia� como sean precisos para facilitar la b�squeda del anuncio. Clasificaci�n a determinar.
	 * **/
	private String tipoAnuncio;
	/**
	 * El valor ser� �S� si el anuncio debe publicarse conforme a lo dispuesto en el art�culo 112 de la Ley 58/2003 (Ley General Tributaria).
	 * **/
	private String lgt;
	/**
	 * Identificaci�n del procedimiento. Es un texto libre que permitir� construir de manera automatizada el t�tulo 
	 * del anuncio y diferenciar entre los emitidos en igual fecha por el mismo emisor
	 * **/
	private String procedimiento;
	/**
	 * Aunque el elemento es opcional, deber� incluirse aqu� la lista con los datos de los notificados si no es posible marcarlos 
	 * en el elemento contenido/texto que se describe en el punto siguiente. Contendr� tantos elementos �notificado� como notificados haya.
	 * **/
	private Notificados notificados;
	private Vector <String> parrafos;
	private GregorianCalendar fechaFirma;
	private String lugar;
	private String cargoNombre;
	public String getNumexp() {
		return numexp;
	}
	public void setNumexp(String numexp) {
		this.numexp = numexp;
	}
	public GregorianCalendar getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(GregorianCalendar fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFormaPublicacion() {
		return formaPublicacion;
	}
	public void setFormaPublicacion(String formaPublicacion) {
		this.formaPublicacion = formaPublicacion;
	}
	public String getDatosPersonales() {
		return datosPersonales;
	}
	public void setDatosPersonales(String datosPersonales) {
		this.datosPersonales = datosPersonales;
	}
	public String getTipoAnuncio() {
		return tipoAnuncio;
	}
	public void setTipoAnuncio(String tipoAnuncio) {
		this.tipoAnuncio = tipoAnuncio;
	}
	public String getLgt() {
		return lgt;
	}
	public void setLgt(String lgt) {
		this.lgt = lgt;
	}
	public String getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}
	public Notificados getNotificados() {
		return notificados;
	}
	public void setNotificados(Notificados notificados) {
		this.notificados = notificados;
	}
	public Vector <String> getParrafos() {
		return parrafos;
	}
	public void setParrafos(Vector <String> parrafos) {
		this.parrafos = parrafos;
	}
	public GregorianCalendar getFechaFirma() {
		return fechaFirma;
	}
	public void setFechaFirma(GregorianCalendar fechaFirma) {
		this.fechaFirma = fechaFirma;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public String getCargoNombre() {
		return cargoNombre;
	}
	public void setCargoNombre(String cargoNombre) {
		this.cargoNombre = cargoNombre;
	}
}
