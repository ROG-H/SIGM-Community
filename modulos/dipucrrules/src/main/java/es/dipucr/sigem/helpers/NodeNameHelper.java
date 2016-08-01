package es.dipucr.sigem.helpers;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.ispaclib.dao.procedure.PNodoDAO;

import java.util.Iterator;

/**
 * Utilidad para componer el nombre de los nodos.
 *
 */
public class NodeNameHelper {

	/**
	 * Obtiene el nombre del nodo. Si el nodo es de sincronizaci�n, lo compone 
	 * a partir del identificador del mismo.
	 * @param node Informaci�n del nodo.
	 * @return Nombre del nodo.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public static String getNodeName(String language, IItem node) throws ISPACException {
		
		String nodeName = null;
		
		if (node != null) {
			int nodeType = node.getInt("TIPO");
			if (nodeType == PNodoDAO.NODE_OBJ_SYNCNODE) {
				nodeName = getSyncNodeName(language, node.getInt("ID"));
			} else {
				nodeName = node.getString("NOMBRE"); 
			}
		}
		
		return nodeName;
	}
	
	/**
	 * Compone el nombre del nodo de sincronizaci�n.
	 * @param nodeId Identificador del nodo de sincronizaci�n.
	 * @return Nombre del nodo de sincronizaci�n.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public static String getSyncNodeName(String language, int nodeId) throws ISPACException {
		
		return Messages.getString("procedure.tree.syncnode", 
				new Object[] { String.valueOf(nodeId) });
	}

	/**
	 * Compone el nombre de los nodos de sincronizaci�n de la lista.
	 * @param nodes Lista de nodos.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public static void fillNodeNames(String language, IItemCollection nodes) 
			throws ISPACException {

		if (nodes != null) {
			Iterator it = nodes.iterator();
			IItem node;
			while (it.hasNext()) {
				node = (IItem) it.next();
				if (node.getInt("TIPO") == PNodoDAO.NODE_OBJ_SYNCNODE) {
					node.set("NOMBRE", getSyncNodeName(language, node.getInt("ID")));
				}
			}
		}
	}
	
}
