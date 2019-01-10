package ieci.tdw.ispac.ispactx.tx;

import ieci.tdw.ispac.api.IBPMAPI;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.EventManager;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.api.rule.RuleProperties;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.tx.TXHitoDAO;
import ieci.tdw.ispac.ispaclib.dao.tx.TXTramiteDAO;
import ieci.tdw.ispac.ispaclib.notices.Notices;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispactx.ITXAction;
import ieci.tdw.ispac.ispactx.TXConstants;
import ieci.tdw.ispac.ispactx.TXTransactionDataContainer;

import java.util.Date;
import java.util.Map;

import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * Acci�n para delegar un tr�mite.
 */
public class TXDelegateTask implements ITXAction {
	
	/** Identificador del tr�mite instanciado. */
	private final int mnIdTask;
	
	/** Identificador del responsable. */
	private final String mIdResp;
	
	/**Nombre del responsable*/
	private String mNameResp;
	/**
	 * Par�metros para el contexto de las reglas.
	 */
	private final Map mparams;

	/**
	 * Constructor
	 * @param nIdTaskI dentificador del tr�mite instanciado.
	 * @param IdResp Identificador del responsable.
	 * @param nameResp Nombre del responsable
	 */
	public TXDelegateTask(int nIdTask,String IdResp, String nameResp) {
	
		this(nIdTask, IdResp, nameResp,null);
	}
	
	/**
	 * Constructor.
	 * @param nIdTask Identificador del tr�mite instanciado.
	 * @param IdResp Identificador del responsable.
	 */
	public TXDelegateTask(int nIdTask,String IdResp) {
		this(nIdTask, IdResp, null,null);
	}

	/**
	 * Constructor.
	 * @param nIdTask Identificador del tr�mite instanciado.
	 * @param IdResp Identificador del responsable.
	 * @param params Par�metros para el contexto de las reglas.
	 */
	public TXDelegateTask(int nIdTask,String IdResp, Map params) {
		this(nIdTask, IdResp, null, params);
	}
	
	/**
	 * Constructor
	 * @param nIdTask Identificador del tr�mite instanciado.
	 * @param IdResp Identificador del responsable.
	 * @param nameResp Nombre del responsable
	 * @param params Par�metros para el contexto de las reglas.
	 */
	public TXDelegateTask(int nIdTask,String IdResp,String nameResp, Map params) {
		mnIdTask=nIdTask;
		mIdResp=IdResp;
		mNameResp=nameResp;
		mparams=params;
	}
	
	/**
	 * Ejecuta la acci�n.
	 * @param cs Contexto de cliente.
	 * @param dtc Contenedor de los datos de la transacci�n.
	 * @param itx Transacci�n.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void run(ClientContext cs, TXTransactionDataContainer dtc,
			ITXTransaction itx) throws ISPACException {
		
		// Informaci�n del tr�mite
		TXTramiteDAO task = dtc.getTask(mnIdTask);
		
		String bpmTaskId = task.getString("ID_TRAMITE_BPM");
		if (StringUtils.isNotBlank(bpmTaskId)) {

			// Obtener el API de BPM
			IBPMAPI bpmAPI = dtc.getBPMAPI();
			
			// Delegar el tr�mite en el BPM
			bpmAPI.delegateTask(bpmTaskId, mIdResp);
		}

	    // Actualizar el responsable del tr�mite y su nombre
		task.set("ID_RESP", mIdResp);
		

		if(StringUtils.isBlank(mNameResp)){
			IResponsible resp = cs.getAPI().getRespManagerAPI().getResp(mIdResp);
			mNameResp=resp.getName();
		}
		
		task.set("RESP", mNameResp);

		// Se a�ade una descripci�n al hito (en este caso a qui�n se delega)
		String desc = new StringBuffer("Delegado a '")
			.append(mNameResp)
			.append("' UID[").append(mIdResp).append("]")
			.toString();

		// Se construye el contexto de ejecuci�n de scripts.
	    EventManager eventmgr = new EventManager(cs, mparams);
		eventmgr.getRuleContextBuilder().addContext(task);
		eventmgr.getRuleContextBuilder().addContext(
		        RuleProperties.RCTX_RESPDELEGATEID, 
		        mIdResp);
		eventmgr.getRuleContextBuilder().addContext(
		        RuleProperties.RCTX_RESPDELEGATENAME,
		        mNameResp);
		eventmgr.getRuleContextBuilder().setItem(task);

		// Ejecutar evento
		eventmgr.processSystemEvents(EventsDefines.EVENT_EXEC_DELEGATE);

		// Ejecutar evento al delegar tr�mite.
		eventmgr.processEvents(EventsDefines.EVENT_OBJ_TASK,
		        			   task.getInt("ID_TRAMITE"),
							   EventsDefines.EVENT_EXEC_DELEGATE);

		// Marcar hito
		TXHitoDAO hito=dtc.newMilestone(task.getInt("ID_EXP"), 
						 task.getInt("ID_FASE_PCD"),
						 task.getInt("ID_TRAMITE"), 
						 TXConstants.MILESTONE_EXPED_DELEGATED, 
						 desc);
		hito.set("INFO", composeInfo());		

		//[dipucr-Felipe #823]
		String numexp = task.getString("NUMEXP");
		IItem itemExpediente = ExpedientesUtil.getExpediente(cs, numexp);
		String asunto = itemExpediente.getString("ASUNTO");
		
		//Se crea un aviso electr�nico indicando que el tramite ha sido delegado
		Notices notices = new Notices(cs);
		String nombreTramite = task.getString("NOMBRE");
		
		String avisoElectronico = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?taskId=" + mnIdTask +"\" class=\"displayLink\"> Nuevo Tr�mite delegado: " +  nombreTramite + "</a><br/>Asunto: " + asunto;//[dipucr-Felipe #823]
		if (avisoElectronico.length() > AvisosUtil._MAXLENGTH_MESSAGE){
			avisoElectronico = avisoElectronico.substring(0, (AvisosUtil._MAXLENGTH_MESSAGE - 5));
			avisoElectronico = avisoElectronico + "...";
		}
		
//		String avisoElectronico = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?taskId=" + mnIdTask +"\" class=\"displayLink\"> Nuevo Tr�mite: " +  nombreTramite + " desde " + mNameResp +"</a>";
		//notices.generateDelegateObjectNotice(task.getInt("ID_EXP"), task.getInt("ID_FASE_EXP"), task.getKeyInt(), cs.getStateContext().getNumexp(), "notice.delegateTask", mIdResp,Notices.TIPO_AVISO_TRAMITE_DELEGADO);
		
		notices.generateDelegateObjectNotice(task.getInt("ID_EXP"), task.getInt("ID_FASE_EXP"), task.getKeyInt(), cs.getStateContext().getNumexp(), avisoElectronico, mIdResp,Notices.TIPO_AVISO_TRAMITE_DELEGADO);
	}

	

	

	private String composeInfo(){
		return new StringBuffer()
			.append("<?xml version='1.0' encoding='ISO-8859-1'?>")
			.append("<infoaux><id_tramite>")
			.append(mnIdTask)
			.append("</id_tramite></infoaux>")
			.toString();
	}	

	
	/**
	 * Bloquea el objeto de la acci�n.
	 * @param cs Contexto de cliente.
	 * @param dtc Contenedor de los datos de la transacci�n.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void lock(ClientContext cs, TXTransactionDataContainer dtc)
			throws ISPACException {
		
		TXTramiteDAO task = dtc.getTask(mnIdTask);
		dtc.getLockManager().lockProcess(task.getInt("ID_EXP"));
		dtc.getLockManager().lockTask(mnIdTask);
	}

	/**
	 * Obtiene el resultado de la acci�n.
	 * @param nameResult Nombre del resultado.
	 * @return Resultado de la acci�n.
	 */
	public Object getResult(String nameResult) {
		return null;
	}
	
}