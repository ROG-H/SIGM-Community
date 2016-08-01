package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.session.LockDAO;
import ieci.tdw.ispac.ispaclib.dao.session.SessionDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.session.SessionMgr;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.lock.LockManager;

/**
 * Clase para desbloquear los tr�mites en casos de firmas automatizada
 * @author Felipe
 * @since 15.10.14 #1115
 */
public class BloqueosFirmaUtil {

	public static String MENSAJE_BLOQUEO_FIRMA = "mensajeBloqueoFirma.jsp";
	public static String SPAC_VAR_CLOSE_SESSION = "BLOQUEO_FIRMA_CERRAR_SESION";
	
	/**
	 * Desbloquea un tr�mite
	 * @param cct
	 * @param idTramite
	 * @throws ISPACException
	 */
	public static void desbloquearTramite(ClientContext cct, int idTramite) throws ISPACException{
		
		LockManager lockMgr = new LockManager(cct);
		
		if (estaBloqueadoTramite(cct, idTramite)){
			
			lockMgr.unlockTask(idTramite);
		}
	}
	
	/**
	 * Cierra la session del usuario y elimina los bloqueos que pudiera tener este
	 * @param cct
	 * @param idTramite
	 * @throws ISPACException
	 */
	public static void cerrarSesionYEliminarBloqueos(ClientContext cct, int idTramite) throws ISPACException{
		
		//S�lo borramos la sesi�n del usuario si existe y est� activada la opci�n
		String sCerrarSesion = ConfigurationMgr.getVarGlobal(cct, SPAC_VAR_CLOSE_SESSION);
		
		if (!StringUtils.isEmpty(sCerrarSesion) && sCerrarSesion.equals("SI")){
			
			//Obtenemos los bloqueos que pudiera tener el tr�mite
			DbCnt cnt = cct.getConnection();
			String sQuery = "WHERE TP_OBJ = " + LockManager.LOCKTYPE_TASK
					 + " AND ID_OBJ = " + idTramite;
			CollectionDAO collectionLocks = new CollectionDAO(LockDAO.class);
			collectionLocks.query(cnt, sQuery);
			
			if (collectionLocks.next()){
			
				//Obtenemos el id de la sesi�n
				LockDAO lock = (LockDAO) collectionLocks.value();
				String idSession = lock.getString("ID");
				
				//Obtenemos el objeto de sesi�n para conseguir el nombre de usuario
				SessionMgr sessMgr = new SessionMgr();
				SessionDAO session = new SessionDAO(cnt);
				session.load(cnt, "WHERE ID = '" + idSession + "'");
				String sUsuario = session.getString("USUARIO");
	
				//Eliminamos la sesi�n del usuario
				sessMgr.deleteSession(idSession);
				
				//Mandamos un aviso al usuario, explic�ndole porque se le expuls� de SIGEM
				MensajesSesionUtil.crearMensajeUsuario(cct, MENSAJE_BLOQUEO_FIRMA, sUsuario);
			}
		}
	}
	
	/**
	 * Comprueba si un cierto tr�mite est� bloqueado
	 * @param cct
	 * @param idTramite
	 * @return
	 * @throws ISPACException
	 */
	public static boolean estaBloqueadoTramite(ClientContext cct, int idTramite) throws ISPACException{
		
		return estaBloqueado(cct, LockManager.LOCKTYPE_TASK, idTramite);
	}
	
	/**
	 * Comprueba si un cierto objeto est� bloqueado
	 * @param cct
	 * @param tpObj
	 * @param idObj
	 * @return
	 * @throws ISPACException
	 */
	private static boolean estaBloqueado(ClientContext cct, int tpObj, int idObj) throws ISPACException{
		
		LockManager lockMgr = new LockManager(cct);
		return (lockMgr.isLock(tpObj, idObj) == LockManager.LOCKED_CURSESSION);
	}
}
