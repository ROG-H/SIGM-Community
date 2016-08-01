
/*
 * Created on 10-feb-2005
 *
 */
package ieci.tdw.ispac.ispacweb.api.impl;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacweb.api.IActions;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IScheme;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.IWorklist;
import ieci.tdw.ispac.ispacweb.api.ManagerState;

import java.util.Map;

/**
 * @author juanin
 *
 * API de tramitaci�n que se apoya sobre el framework de ISPAC
 */
public class ManagerAPI implements IManagerAPI
{
    ClientContext mcct;

	public ManagerAPI(ClientContext cct)
	{
		this.mcct = cct;
	}

	/**
	 * Obtiene el esquema de tramitaci�n de un expediente.
	 * @return IScheme el esquema
	 */
	public IScheme getSchemeAPI()
	{
		return new Scheme(mcct);
	}

	/**
	 * Obtiene las posibles acciones a realizar en la tramitaci�n de un expediente.
	 * @return Actions Acciones a realizar
	 */
	public IActions getActionsAPI()
	{
		return new Actions(mcct);
	}

	/**
	 * Obtiene la lista de trabajo dependiendo del estado de tramitaci�n.
	 * @return IScheme el esquema
	 */
	public IWorklist getWorklistAPI()
	{
		return new Worklist(mcct);
	}

	/**
	 * Transiciona a un nuevo estado de tramitaci�n
	 * 
	 * @param stateticket ticket del estado actual
	 * @param newstateID identificador del nuevo estado
	 * @param params par�metros necesarios para la creaci�n del estado
	 * @return IState estado actual de tramitaci�n
	 * @throws ISPACException
	 */
	public IState enterState(String stateticket, 
							 int newstateID, 
							 Map params) throws ISPACException {
		
		StateFactory statefact = new StateFactory();

		IState currentState = statefact.createState(stateticket);
		IState newState = statefact.createState(newstateID, params, mcct);
		
		//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el �rea de trabajo por a�o de inicio de expediente.
		if(newState.getState() != ManagerState.PROCESSESLIST){
			newState.setAnio(currentState.getAnio());
		}
		
		// Comprobar si el estado de tramitaci�n ha cambiado
		if (currentState.equals(newState)) {
			
			// Para fase, tr�mite y actividad:
			// comprobar si dentro del mismo estado la entidad seleccionada ha cambiado
			// para desbloquear la entidad anteriormente seleccionada y bloquear la entidad
			// actualmente seleccionada siempre que se tenga el bloqueo sobre el estado actual
			newState.checkNewEntity(currentState, mcct);
		}
		else {		
			// Salir del estado anterior de tramitaci�n (liberar bloqueos)
			currentState.exit(mcct);
			
			// Entrar en el nuevo estado de tramitaci�n
			newState.enter(mcct);
		}
		
		// Establecer el contexto del estado actual de tramitaci�n
		// en el contexto del cliente
		mcct.setStateContext(newState.getStateContext());
		
		return newState;
	}

	/**
	 * Obtiene el estado en el que se encuentra la tramitaci�n.
	 * @param stateticket ticket del estado
	 * @return IState
	 * @throws ISPACException
	 */
	public IState currentState(String stateticket) throws ISPACException
	{
		StateFactory statefact = new StateFactory();
		IState currentState = statefact.createState(stateticket);
		
		// Establecer el contexto del estado actual de tramitaci�n
		// en el contexto del cliente
		mcct.setStateContext(currentState.getStateContext());
		
		return currentState;
	}

	/**
	 * Transiciona a un nuevo estado de tramitaci�n.
	 * @param stateticket ticket del estado actual
	 * @param newstateID identificador del nuevo estado
	 * @param params par�metros necesarios para la creaci�n del estado
	 * @return IState
	 * @throws ISPACException
	 */
	/*
	public IState enterState(IState currentState, int newstateID, Map params) throws ISPACException
	{
		StateFactory statefact = new StateFactory();

		IState newState = statefact.createState(newstateID, params, mcct);
		if (currentState.equals(newState))
			return newState;

		currentState.exit(mcct);
		newState.enter(mcct);
		return newState;
	}*/

	/**
	 * Crea un nuevo estado de tramitaci�n pero no entra en el mismo ya que no llama al m�todo
	 * IState.enter()
	 * @param stateticket ticket del estado actual
	 * @param newstateID identificador del nuevo estado
	 * @param params par�metros necesarios para la creaci�n del estado
	 * @return IState
	 * @throws ISPACException
	 */
	public IState newState(int newstateID, Map params) throws ISPACException
	{
		StateFactory statefact = new StateFactory();
		IState newState = statefact.createState(newstateID, params, mcct);
		return newState;
	}
}
