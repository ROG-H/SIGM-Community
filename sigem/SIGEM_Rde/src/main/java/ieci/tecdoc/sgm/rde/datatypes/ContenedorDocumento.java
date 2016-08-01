package ieci.tecdoc.sgm.rde.datatypes;

import java.util.Date;


/**
 * Interfaz de comportamiento de un documento.
 * 
 * @author IECISA
 *
 */
public interface ContenedorDocumento 
{

	/**
	 * Devuelve el identificador �nico de documento.
	 * @return String Identificador �nico de documento.
	 */
   public abstract String getGuid();
   
   /**
    * Devuelve el contenido del documento.
    * @return Blob Contenido del documento.
    */   
   public abstract byte[] getContent();
   
   /**
    * Devuelve el hash o resumen asociado a un documento.
    * @return String Hash o resumen asociado a un documento.
    */   
   public abstract String getHash();
	
   /**
    * Devuelve la extensi�n del documento
    * @return String Extensi�n del documento.
    */   
   public abstract String getExtension();
   
   /**
    * Devuelve el sello de tiempo del instante en el que
    * el documento fue almacenado
    * @return Timestamp Timestamp de almacenamiento del documento.
    */   
   public abstract Date getTimestamp();
   
   /**
    * Devuelve el tama�o del documento
    * @return int Tama�o del documento.
    */   
   public abstract int getContentSize();
   
   /**
    * Establece identificador �nico de documento.
    * @param guid Identificador �nico de documento.
    */	
   public abstract void setGuid(String guid);
   
   /**
    * Establece el contenido del documento.
    * @param content Contenido del documento.
    */   
   public abstract void setContent(byte[] content);
   
   /**
    * Establece el hash o resumen del documento.
    * @param hash Hash o resumen del documento.
    */   
   public abstract void setHash(String hash);

   /**
    * Establece la extensi�n del documento.
    * @param extension Extensi�n del documento.
    */   
   public abstract void setExtension(String extension);
   
   /**
    * Establece el sello de tiempo del instante en el que
    * el documento fue almacenado
    * @param timestamp Timestamp de almacenamiento del documento.
    */   
   public abstract void setTimestamp(Date timestamp);
   
   /**
    * Establece el tama�o del documento
    * @param contentSize Tama�o del documento
    */   
   public abstract void setContentSize(int contentSize);
   
   /**
    * Recoge los valores de la instancia en una cadena xml
    * @param header Si se incluye la cabecera
    * @return los datos en formato xml
    */
   public abstract String toXML(boolean header);

   /**
    * Devuelve los valores de la instancia en una cadena de caracteres.
    */
   public abstract String toString();
   
   //[Tere # 1014] INICIO
   public String getFileRegistroPresencial();
   
   public void setFileRegistroPresencial(String fileRegistroPresencial);
//   [Tere # 1014] fin

}