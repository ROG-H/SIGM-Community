/**
 * @author Alvaro Ugarte G�mez
 *
 * Fecha de Creaci�n: 02-jun-2004
 */

package ieci.tecdoc.sgm.rde.database;

import ieci.tecdoc.sgm.base.dbex.DbUtil;
import ieci.tecdoc.sgm.rde.datatypes.ContenedorDocumentoImpl;

/**
 * Gestiona el acceso (inserciones, modificaciones, etc.) a la tabla de
 * documentos.
 *
 */
public class ContenedorDocumentoTabla extends ContenedorDocumentoImpl {
   private static final String TABLE_NAME = "SGMRDEDOCUMENTOS";
   private static final String CN_GUID = "GUID";
   private static final String CN_CONTENT = "CONTENIDO";
   private static final String CN_HASH = "FILEHASH";
   private static final String CN_EXTENSION = "EXTENSION";
   private static final String CN_TIMESTAMP = "FECHA";
   //[Ticket 1014 Teresa]
   private static final String CN_FILEREGISTROPRES = "IDFILEREGISTROPRES";
   private static final String ALL_COLUMN_NAMES = CN_GUID + ","
           + CN_CONTENT + ","
           + CN_HASH + ","
           + CN_EXTENSION + ","
           + CN_TIMESTAMP+ ","
         //[Ticket 1014 Teresa]
            + CN_FILEREGISTROPRES;

   /**
    * Constructor de la clase ContenedorDocumentoTabla
    */
   public ContenedorDocumentoTabla() {
   }


   /**
    * Devuelve el nombre de la tabla
    * @return String Nombre de la tabla
    */
   public String getTableName() {

      return TABLE_NAME;
   }


   /**
    * Devuelve los nombres de las columnas
    * @return String Nombres de las columnas
    */
   public String getAllColumnNames() {

      return ALL_COLUMN_NAMES;
   }

   /**
    * Devuelve el nombre de la columna guid
    * @return String Nombre de la columna guid
    */
   public String getGuidColumnName(){
     return CN_GUID;
   }


   /**
    * Devuelve el nombre de la columna content
    * @return String Nombre de la columna content
    */
   public String getContentColumnName(){
     return CN_CONTENT;
   }


   /**
    * Devuelve el nombre de la columna hash
    * @return String Nombre de la columna hash
    */
   public String getHashColumnName(){
     return CN_HASH;
   }


   /**
    * Devuelve el nombre de la columna extensi�n
    * @return String Nombre de la columna extensi�n
    */
   public String getExtensionColumnName(){
     return CN_EXTENSION;
   }


   /**
    * Devuelve el nombre de la columna timestamp
    * @return String Nombre de la columna timestamp
    */
   public String getTimestampColumnName(){
     return CN_TIMESTAMP;
   }


   /**
    * Devuelve la clausula de consulta por guid
    * @param guid Valor del campo guid
    * @return String Clausula de consulta por guid
    */
   public String getByGuidQual(String guid) {
      String qual;

      qual = "WHERE " + CN_GUID + " = '" + DbUtil.replaceQuotes(guid) + "'";

      return qual;
   }
   
    //[Ticket 1014 Teresa INICIO]
   /**
    * Devuelve el nombre completo de las columnas mapeadas de la tabla
    * para la fecha efectiva y el estado.
    *
    * @return Las columnas mencionadas.
    */
   public String getUpdateContenidoIdfileregistropres() {
      String val = CN_CONTENT + "," 
      			+ CN_FILEREGISTROPRES;
      return val;
   }

   /**
    * Devuelve el nombre de la columna file
    * @return String Nombre de la columna file
    */
	public static String getCnFileregistropres() {
		return CN_FILEREGISTROPRES;
	}
	//[Ticket 1014 Teresa FIN]

}