package ieci.tecdoc.sgm.catalogo_tramites.util;

import org.apache.log4j.Logger;

import ieci.tecdoc.sgm.catalogo_tramites.exception.CatalogoTramitesCodigosError;
import ieci.tecdoc.sgm.catalogo_tramites.exception.CatalogoTramitesExcepcion;
import ieci.tecdoc.sgm.catalogo_tramites.util.ConectorAutenticacion;
import ieci.tecdoc.sgm.catalogo_tramites.util.ConectoresAutenticacion;
import ieci.tecdoc.sgm.catalogo_tramites.util.database.ConectorAutenticacionDatos;
import ieci.tecdoc.sgm.catalogo_tramites.util.Conectores;
import ieci.tecdoc.sgm.catalogo_tramites.util.ConectorManager;

/**
 * Clase para la gesti�n de conectores de autenticaci�n. Proporciona una fachada 
 * compuesta por m�todos est�ticos con los operaciones necesarias para la 
 * administraci�n de conectores de autenticaci�n.
 * 
 * @author IECISA
 *
 */
public abstract class ConectorAutenticacionManager {

	private static final Logger logger = Logger.getLogger(ConectorAutenticacionManager.class);
			
	/**
	 * A�ade una nueva entrada con la informaci�n del conector de autenticaci�n
	 * en la base de datos.
	 * 
	 * @param conectorAutenticacion Informaci�n sobre el conector de autenticaci�n.
	 * @throws AutenticacionExcepcion En caso de producirse alg�n error.
	 */
   public static void add(ConectorAutenticacion conectorAutenticacion, String entidad) throws CatalogoTramitesExcepcion {
	   ConectorAutenticacionDatos data = new ConectorAutenticacionDatos(conectorAutenticacion); 
      try {
    	  data.insert(entidad);
      } catch (CatalogoTramitesExcepcion exc) {
    	  logger.error("Error al insertar un conector de autenticacion [add][CatalogoTramitesExcepcion]", exc.fillInStackTrace());
    	  throw exc;
      } catch (Exception e) {
    	  logger.error("Error al insertar un conector de autenticacion [add][Excepcion]", e.fillInStackTrace());
    	  throw new CatalogoTramitesExcepcion(CatalogoTramitesCodigosError.EC_ADD_AUTENTICATION_HOOK);
      }
   }

   /**
	 * Modifica una entrada con la informaci�n del conector de autenticaci�n
	 * en la base de datos.
	 * 
	 * @param conectorAutenticacion Informaci�n actualizada sobre el conector
	 * 	de autenticaci�n a modificar.
	 * @param oldConectorId Valor del conector al que estaba asociado el conector de autenticaci�n
	 * @throws AutenticacionExcepcion En caso de producirse alg�n error.
	 */
  public static void update(ConectorAutenticacion conectorAutenticacion, String oldConectorId, String entidad) throws CatalogoTramitesExcepcion {
	   ConectorAutenticacionDatos data = new ConectorAutenticacionDatos(conectorAutenticacion); 
     try {
    	 data.update(oldConectorId, entidad);
     } catch (CatalogoTramitesExcepcion exc) {
    	 logger.error("Error al actualizar un conector de autenticacion [update][CatalogoTramitesExcepcion]", exc.fillInStackTrace());
    	 throw exc;
     } catch (Exception e) {
    	 logger.error("Error al actualizar un conector de autenticacion [update][Excepcion]", e.fillInStackTrace());
    	 throw new CatalogoTramitesExcepcion(CatalogoTramitesCodigosError.EC_UPDATE_AUTENTICATION_HOOK);
     }
  }
   
   /**
    * Elimina un determinado conector de autenticaci�n de la base de datos.
    * 
    * @param tramiteId Identificador del tr�mite.
    * @param conectorId Identificador del conector. 
    * @throws AutenticacionExcepcion En caso de producirse alg�n error.
    */
   public static void delete(String tramiteId, String conectorId, String entidad) throws CatalogoTramitesExcepcion {
	   ConectorAutenticacionDatos conectorAutenticacion = new ConectorAutenticacionDatos();
      try {
    	  conectorAutenticacion.setTramiteId(tramiteId);
    	  conectorAutenticacion.setConectorId(conectorId);
    	  conectorAutenticacion.delete(entidad);
      } catch (CatalogoTramitesExcepcion exc) {
    	  logger.error("Error al eliminar un conector de autenticacion [delete][CatalogoTramitesExcepcion]", exc.fillInStackTrace());
    	  throw exc;
      } catch (Exception e) {
    	  logger.error("Error al eliminar un conector de autenticacion [delete][Excepcion]", e.fillInStackTrace());
    	  throw new CatalogoTramitesExcepcion(CatalogoTramitesCodigosError.EC_DELETE_AUTENTICATION_HOOK);
      }
   }
   
   /**
    * Elimina todos los conetores de autenticaci�n asociados a un tr�mite.
    * 
    * @param tramiteId Identificador de tr�mite.
    * @throws AutenticacionExcepcion En caso de producirse alg�n error.
    */
   public static void delete(String tramiteId, String entidad) throws CatalogoTramitesExcepcion {
	   ConectorAutenticacionDatos conectorAutenticacion = new ConectorAutenticacionDatos();
      try {
    	  conectorAutenticacion.setTramiteId(tramiteId);
    	  conectorAutenticacion.delete(entidad);
      } catch (CatalogoTramitesExcepcion exc) {
    	  logger.error("Error al eliminar los conectores de autenticacion de un tramite [delete][CatalogoTramitesExcepcion]", exc.fillInStackTrace());
    	  throw exc;
      } catch (Exception e) {
    	  logger.error("Error al eliminar los conectores de autenticacion de un tramite [delete][Excepcion]", e.fillInStackTrace());
    	  throw new CatalogoTramitesExcepcion(CatalogoTramitesCodigosError.EC_DELETE_AUTENTICATION_HOOK);
      }
   }
   

   /**
    * M�todo que obtiene la informaci�n asociada a un determinado conector de autenticaci�n.
    * @param tramiteId Identificador de tr�mite al que est� asociado el conector de autenticaci�n.
    * @param conectorId Identificador de conector al que est� asociado el conector de autenticaci�n.
    * @return SessionInfo Con la informaci�n de la sesi�n.
    * @throws AutenticacionExcepcion En caso de producirse alg�n error.
    */
   public static ConectorAutenticacion get(String tramiteId, String conectorId, String entidad) throws CatalogoTramitesExcepcion {
	   ConectorAutenticacionDatos conectorAutenticacion = new ConectorAutenticacionDatos();
      try {
    	  conectorAutenticacion.load(tramiteId, conectorId, entidad);
      } catch (CatalogoTramitesExcepcion exc) {
    	  logger.error("Error al obtener conector de autenticacion [delete][CatalogoTramitesExcepcion]", exc.fillInStackTrace());
    	  throw exc;
      } catch (Exception e) {
    	  logger.error("Error al obtener conector de autenticacion [delete][Excepcion]", e.fillInStackTrace());
    	  throw new CatalogoTramitesExcepcion(CatalogoTramitesCodigosError.EC_GET_AUTENTICATION_HOOK);
      }
      return conectorAutenticacion;
   }


   /**
    * M�todo que obtiene los conectores de autenticaci�n asociados a un tr�mite
    * @param tramiteId Identificador de tr�mite.
    * @return ConectoresAutenticacion listado de conectores de autenticaci�n.
    * @throws AutenticacionExcepcion En caso de producirse alg�n error.
    */
   public static ConectoresAutenticacion getAuthHooks(String tramiteId, String entidad) throws CatalogoTramitesExcepcion
   {
	   ConectoresAutenticacion conectoresAutenticacion = new ConectoresAutenticacion();
	   try {
		   conectoresAutenticacion = ConectorAutenticacionDatos.getAuthHooks(tramiteId, entidad);
	   } catch (CatalogoTramitesExcepcion exc) {
		   logger.error("Error al obtener los conector de autenticacion de un tramite [getAuthHooks][CatalogoTramitesExcepcion]", exc.fillInStackTrace());
		   throw exc;
	   } catch (Exception e) {
		   logger.error("Error al obtener los conector de autenticacion de un tramite [getAuthHooks][Excepcion]", e.fillInStackTrace());
		   throw new CatalogoTramitesExcepcion(CatalogoTramitesCodigosError.EC_GET_AUTENTICATION_HOOKS);
	   }
      
	   return conectoresAutenticacion;
   }

   
   /**
    * M�todo que obtiene los conectores de autenticaci�n asociados a un tr�mite
    * @param tramiteId Identificador de tr�mite.
    * @return ConectoresAutenticacion listado de conectores de autenticaci�n.
    * @throws AutenticacionExcepcion En caso de producirse alg�n error.
    */
   public static Conectores getHooksForProcedure(String tramiteId, String entidad) throws CatalogoTramitesExcepcion
   {
	   Conectores conectores = new Conectores();
	   try {
		   ConectoresAutenticacion conectoresAutenticacion = new ConectoresAutenticacion();
		   conectoresAutenticacion = ConectorAutenticacionDatos.getAuthHooks(tramiteId, entidad);
		   String[] hookIds = new String[conectoresAutenticacion.count()];
		   for(int i=0; i<conectoresAutenticacion.count(); i++)
			   hookIds[i]= conectoresAutenticacion.get(i).getConectorId();
		   
		   conectores = ConectorManager.getMultipleHooks(hookIds, entidad);
	   } catch (CatalogoTramitesExcepcion exc) {
		   logger.error("Error al obtener los conector de un tramite [getHooksForProcedure][CatalogoTramitesExcepcion]"+" - TRAMITE:"+tramiteId+" ENTIDAD: "+entidad+" - "+exc.getMessage(), exc);
		   throw exc;
	   } catch (Exception e) {
		   logger.error("Error al obtener los conector de un tramite [getHooksForProcedure][Excepcion]"+" - TRAMITE:"+tramiteId+" ENTIDAD: "+entidad+" - "+e.getMessage(), e);
		   throw new CatalogoTramitesExcepcion(CatalogoTramitesCodigosError.EC_GET_AUTENTICATION_HOOKS);
	   }
      
	   return conectores;
   }

}