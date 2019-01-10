package ieci.tdw.ispac.api;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcedure;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.api.item.ITask;

import java.io.Serializable;

/**
 * Interface que proporciona la funcionalidad b�sica del framework.
 * A partir de �ste se obtienen la mayor�a de los interfaces
 * que proporcionan el resto de la funcionalidad e informaci�n del framework.
 *
 * @see ieci.tdw.ispac.api.item.IItem
 * @see ieci.tdw.ispac.api.item.IItemCollection
 */
public interface IInvesflowAPI extends Serializable
{
	/**
	 * Obtiene la definici�n de un procedimiento seg�n su identificador.
	 *
	 * @param nIdProcedure identificador del procedimiento
	 * @return IProcedure IItem espec�fico para el procedimiento
	 * @throws ISPACException
	 * @see IProcedure
	 */
	public IProcedure getProcedure(int nIdProcedure) throws ISPACException;

	/**
	 * Obtiene la definici�n de la fase en el procedimiento.
	 *
	 * @param nIdStagePCD identificador de la fase en el procedimiento.
	 * @return IItem
	 * @throws ISPACException
	 */
	public IItem getProcedureStage(int nIdStagePCD) throws ISPACException;

	/**
	 * Obtiene la informaci�n del nodo en el procedimiento.
	 *
	 * @param nIdNodePCD identificador del nodo en el procedimiento.
	 * @return IItem
	 * @throws ISPACException
	 */
	public IItem getProcedureNode(int nIdNodePCD) throws ISPACException;

	/**
	 * Obtiene la definici�n del tr�mite en el procedimiento.
	 *
	 * @param nIdTaskPCD identificador del tr�mite en el procedimiento
	 * @return IItem
	 * @throws ISPACException
	 */
	public IItem getProcedureTaskPCD(int nIdTaskPCD) throws ISPACException;

	/**
	 * Obtiene la definici�n del tr�mite en el procedimiento.
	 *
	 * @param nIdTaskCTL identificador del tr�mite en el cat�logo
	 * @return IItem
	 * @throws ISPACException
	 */
	public IItem getProcedureTaskCTL(int nIdTaskPCD) throws ISPACException;
	/**
	 * Obtiene la colecci�n de procedimientos definidos.
	 *
	 * @param query SQL para restringir el resultado
	 * @return IItem
	 * @throws ISPACException
	 */
	public IItemCollection getProcedures(String query) throws ISPACException;
	
	
	/**
	 * Obtiene la lista de procesos para un determinado procedimiento
	 * @param idProcedimiento Identificador del Procedimiento
	 * @return Colecci�n de Procesos
	 * @throws ISPACException
	 */
	public IItemCollection getProcessesByProcedure(int idProcedimiento) throws ISPACException;

	/**
	 * Obtiene un proceso seg�n su identificador.
	 * Un proceso es una instancia activa de un procedimiento.
	 *
	 * @param nIdProc identificador del proceso.
	 * @return IProcess IItem espec�fico para el proceso
	 * @throws ISPACException
	 * @see IProcess
	 */
	public IProcess getProcess(int nIdProc) throws ISPACException;

	/**
	 * Busca un proceso dado su n�mero de expediente. Si existe un
	 * m�s de proceso con el mismo n�mero de expediente se devuelve el m�s reciente.
	 *
	 * @param numExp n�mero de expediente.
	 * @return IProcess IItem espec�fico para el proceso
	 * @throws ISPACException
	 * @see IProcess
	 */
	public IProcess getProcess(String numExp) throws ISPACException;

	
	/**
	 * Obtiene un subproceso instanciado segun su identificador.
	 *  
	 * @param nIdSubProc identificador del subproceso
	 * @return IItem espec�fico para el subproceso
	 * @throws ISPACException
	 */
	public IProcess getSubProcess(int nIdSubProc)throws ISPACException;
	
	/**
	 * Obtiene todos los subprocesos asociados a un expediente
	 * @param numExp n�mero de expediente
	 * @return Colecci�n de subprocesos de un expediente
	 * @throws ISPACException
	 */
	public IItemCollection getSubProcess(String numExp) throws ISPACException;	
	
	/**
	 * Obtiene la definici�n de una fase activa o instanciada en el expediente,
	 * seg�n su identificador.
	 *
	 * @param nIdStage identificador de la fase instanciada en el expediente
	 * @return IStage
	 * @throws ISPACException
	 * @see IStage
	 */
	public IStage getStage(int nIdStage) throws ISPACException;

	
	/**
	 * Obtiene una colecci�n de definiciones de fases activas o instanciadas
	 * seg�n una cadena de identificadores de fases activas.
	 *
	 * @param stages cadena de identificadores de fase separados por comas.
	 * @return IItemCollection, colecci�n de objetos TXFaseDAO
	 * @throws ISPACException
	 * @see ieci.tdw.ispac.ispaclib.dao.tx.TXFaseDAO
	 */
	public IItemCollection getStages(String stages) throws ISPACException;

	/**
	 * Obtiene la definici�n de un tr�mite activo o instanciado en el expediente,
	 * seg�n su identificador.
	 *
	 * @param nIdTask identificador del tr�mite instanciado en el expediente.
	 * @return ITask
	 * @throws ISPACException
	 * @see ITask
	 */
	public ITask getTask(int nIdTask) throws ISPACException;
	
	/**
	 * Obtiene la definici�n de un tr�mite activo o instanciado en el expediente,
	 * seg�n su identificador de subproceso.
	 *
	 * @param nIdSubProcess identificador del subproceso en el tr�mite instanciado en el expediente.
	 * @return ITask
	 * @throws ISPACException
	 * @see ITask
	 */
	public ITask getTaskBySubProcess(int nIdSubProcess) throws ISPACException;

	/**
	 * Obtiene una colecci�n de definiciones de tr�mites activos o instanciados
	 * seg�n una cadena de identificadores de tr�mites activos. Los identificadores iran separados por comas.
	 *
	 * @param tasks cadena de identificadores de tr�mites separados por comas.
	 * @return IItemCollection, colecci�n de objetos TXTramiteDAO
	 * @throws ISPACException
	 * @see ieci.tdw.ispac.ispaclib.dao.tx.TXTramiteDAO
	 */
	public IItemCollection getTasks(String tasks) throws ISPACException;

	/**
	 * Obtiene la colecci�n de hitos alcanzados en el expediente indicado mediante
	 * el identificador del proceso.
	 *
	 * @param nIdProc identificador de un proceso
	 * @return IItemCollection, colecci�n de objetos TXHitoDAO
	 * @throws ISPACException
	 * @see ieci.tdw.ispac.ispaclib.dao.tx.TXHitoDAO
	 */
	public IItemCollection getMilestones(int nIdProc) throws ISPACException;

	/**
	 * @param processId identificador de proceso
	 * @param stagePcdId identificador de fase en el procedmiento
	 * @param taskPcdId identificador de tr�mite en el procedimiento
	 * @param taskId identificador de tr�mite instanciado
	 * @param milestone hito
	 * @return Colecci�n de hitos
	 * @throws ISPACException
	 */
	public IItemCollection getMilestones(int processId, int stagePcdId,int taskPcdId, int taskId, int milestone) throws ISPACException;
	/**
	 * Obtiene la colecci�n de hitos alcanzadosseg�n
	 * el n�mero de expediente de un proceso.
	 *
	 * @param numexp n�mero de expediente.
	 * @return IItemCollection, colecci�n de objetos TXHitoDAO
	 * @throws ISPACException
	 * @see ieci.tdw.ispac.ispaclib.dao.tx.TXHitoDAO
	 */
	public IItemCollection getMilestones(String numexp) throws ISPACException;

	/**
	 * Cuenta el n�mero de expedientes enviados a la papelera.
	 * Solamente el supervisor(consulta o total) podr� visualizar la lista de 
	 * procesos enviados a la papelera
	 * @return
	 * @throws ISPACException
	 */
	public int countExpedientsSentToTrash()throws ISPACException;
	
	/**
	 * Obtiene la lista de expedientes enviados a la papelera
	 * @fechaInicio Fecha de creaci�n del expediente
	 * @fechaEliminacion Fecha de eliminaci�n del expediente
	 * @procedimiento Procedimiento al que pertenece el expediente
	 * @return
	 * @throws ISPACException
	 */
	public IItemCollection getExpedientsSentToTrash(String fechaInicio, String fechaEliminacion , int procedimiento)throws ISPACException;
	/**
	 * Devuelve cierto si el expediente se encuentra en la papelera y falso en caso contrario
	 * @param numexp N�mero de expediente
	 * @return
	 * @throws ISPACException
	 */
	public boolean isExpedientSentToTrash(String numexp)throws ISPACException;
	/**
	 * Obtiene el interface {@link IWorklistAPI}
	 * @return el interface IWorklistAPI
	 * @throws ISPACException
	 */
	public IWorklistAPI getWorkListAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link IEntitiesAPI}
	 * @returnel interface IEntitiesAPI
	 * @throws ISPACException
	 */
	public IEntitiesAPI getEntitiesAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link ITXTransaction}
	 * @return el interface ITXTransaction
	 * @throws ISPACException
	 */
	public ITXTransaction getTransactionAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link IRespManagerAPI}
	 * @return el interface IRespManagerAPI
	 * @throws ISPACException
	 */
	public IRespManagerAPI getRespManagerAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link IGenDocAPI}
	 * @return el interface IGenDocAPI
	 * @throws ISPACException
	 */
	public IGenDocAPI getGenDocAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link ISearchAPI}
	 * @return el interface ISearchAPI
	 * @throws ISPACException
	 */
	public ISearchAPI getSearchAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link IInboxAPI}
	 * @return el interface IInboxAPI
	 * @throws ISPACException
	 */
	public IInboxAPI getInboxAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link ISchedulerAPI}
	 * @return el interface ISchedulerAPI
	 * @throws ISPACException
	 */
	public ISchedulerAPI getSchedulerAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link IThirdPartyAPI}
	 * @return el interface IThirdPartyAPI
	 * @throws ISPACException
	 */
	public IThirdPartyAPI getThirdPartyAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link ICatalogAPI}
	 * @return el interface ICatalogAPI
	 * @throws ISPACException
	 */
	public ICatalogAPI getCatalogAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link ITemplateAPI}
	 * @return el interface ITemplateAPI
	 * @throws ISPACException
	 */
	public ITemplateAPI getTemplateAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link getProcedureAPI}
	 * @return el interface getProcedureAPI
	 * @throws ISPACException
	 */
    public IProcedureAPI getProcedureAPI();

	/**
	 * Obtiene el interface {@link ISecurityAPI}
	 * @return el interface ISecurityAPI
	 * @throws ISPACException
	 */
	public ISecurityAPI getSecurityAPI() throws ISPACException;
	
	/**
	 * Obtiene el interface {@link ISignAPI}
	 * @return el interface ISignAPI
	 * @throws ISPACException
	 */
	public ISignAPI getSignAPI() throws ISPACException;
	
	/**
	 * Obtiene una tarea agrupada
	 * @param idBatchTask
	 * @return
	 * @throws ISPACException
	 */
	public IItem getBatchTask(int nIdBatchTask) throws ISPACException;
	
	/**
	 * Obtiene el interface {@link ICustomAPI}
	 * @returnel interface ICustomAPI
	 * @throws ISPACException
	 */
	public ICustomAPI getCustomAPI() throws ISPACException;

	/**
	 * Obtiene el interface {@link IBPMAPI}}
	 * @return interface IBPMAPI
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public IBPMAPI getBPMAPI() throws ISPACException;

	/**
	 * @param idProcess Identificador del proceso
	 * @return Fases instanciadas para el proceso
	 * @throws ISPACException si ocurre alg�n error
	 */
	public IItemCollection getStagesProcess(int idProcess) throws ISPACException;
	
	/**
	 * Obtiene el API de generaci�n de informes.
	 * @return API de generaci�n de informes.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public IReportsAPI getReportsAPI() throws ISPACException;
	
	/**
	 * Obtiene el interface {@link IPublisherAPI}
	 * @return el interface IPublisherAPI
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public IPublisherAPI getPublisherAPI() throws ISPACException;

	
	/**
	 * Obtiene el interface {@link IRegisterAPI}
	 * @return el interface IRegisterAPI
	 * @throws ISPACException
	 */
	public IRegisterAPI getRegisterAPI()  throws ISPACException;
	
	/**
	 * [eCenpri-Manu #120] ALSIGM3 Crear opci�n de men� que devuelva el manual de usuario del procedimento.
	 * 
	 * Obtiene el interface {@link ITemplateAPI}
	 * @return el interface IManualUsuarioAPI
	 * @throws ISPACException
	 */
	public IManualUsuarioAPI getManualUsuarioAPI() throws ISPACException;
	
	/**
	 * [Dipucr-Manu Ticket #] ALSIGM3 Crear opci�n de men� que devuelva el manual de usuario del procedimento.
	 * 
	 * Obtiene el interface {@link ITemplateAPI}
	 * @return el interface IManualUsuarioAPI
	 * @throws ISPACException
	 */
	public IMensajeAPI getMensajeAPI() throws ISPACException;
	
}