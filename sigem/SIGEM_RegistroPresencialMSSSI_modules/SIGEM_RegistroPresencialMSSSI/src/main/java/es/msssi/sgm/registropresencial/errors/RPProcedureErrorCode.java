/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versi�n 1.1 o �en cuanto sean aprobadas por laComisi�n Europea� versiones posteriores de la EUPL (la �Licencia�); 
* Solo podr� usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
* V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.errors;

import core.error.ErrorCode;

/**
 * Clase que recoge los c�digos de error referentes a operaciones con los procedimientos.
 * 
 * @author eacuna
 */
public class RPProcedureErrorCode
		extends ErrorCode {
	/** Constante de c�digo RPB003: Error en la obtenci�n de los procedimientos. */
	public static final RPProcedureErrorCode GET_BOOKS_ERROR = new RPProcedureErrorCode("RPP003",
			"GET BOOKS ERROR");
	/** Constante de c�digo RPB004: Error en la obtenci�n del procedimiento. */
	public static final RPProcedureErrorCode GET_BOOK_ERROR = new RPProcedureErrorCode("RPP004",
			"GET BOOK ERROR");
	/** Constante de c�digo RPB008: Error al obtener los datos de sesi�n del procedimiento. */
	public static final RPProcedureErrorCode GET_SESSION_BOOK_ERROR = new RPProcedureErrorCode(
			"RPB005", "GET SESSION BOOK ERROR");
	
	/**
	 * Constructor con c�digo de error y mensaje.
	 * 
	 * @param code
	 *            C�digo interno del error.
	 * @param message
	 *            Mensaje descriptivo del error.
	 */
	protected RPProcedureErrorCode(String code, String message) {
		super(code, message);
	}
}