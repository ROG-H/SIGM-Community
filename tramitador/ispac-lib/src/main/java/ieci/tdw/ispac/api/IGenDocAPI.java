package ieci.tdw.ispac.api;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.common.constants.NotifyStatesConstants;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public interface IGenDocAPI {
	/**
	 * Devuelve la lista de tipos de documento que se pueden
	 * generar a partir de una fase de tramitaci�n
	 *
	 * @param stageId Identificador de la fase de un procedimiento
	 * @return lista de procedimientos
	 */
	public abstract IItemCollection getDocTypesFromStage(int stageId)
	throws ISPACException;

	/**
	 * Obtiene un tipo de documento.
	 *
	 * @param typeId Identificador del tipo de documento
	 * @return el objeto CTTpDocDAO con los datos del tipo de documento
	 */
	public IItem getDocumentType(int typeId)
	throws ISPACException;
	
	/**
	 * Obtiene un tipo de documento a partir del c�digo.
	 *
	 * @param typeCode C�digo del tipo de documento
	 * @return el objeto CTTpDocDAO con los datos del tipo de documento
	 */
	public IItem getDocumentType(String typeCode)
	throws ISPACException;

	/**
	 * Devuelve la lista de tipos de documento que se pueden
	 * generar a partir de una tarea del cat�logo.
	 *
	 * @param taskCtlId Identificador de la tarea en el cat�logo
	 * @return lista de tipos de documentos
	 */
	public IItemCollection getDocTypesFromTaskCTL(int taskCtlId)
	throws ISPACException;

	/**
	 * Devuelve la lista de tipos de documento que se pueden
	 * generar a partir de una tarea del procedimiento.
	 *
	 * @param taskPcdId Identificador de la tarea en el procedimiento
	 * @return lista de tipos de documentos
	 */
	public IItemCollection getDocTypesFromTaskPCD(int taskPcdId)
	throws ISPACException;

	/**
	 * calcula la lista de plantillas de un tipo de documento
	 * generables desde el contexto de una fase
	 *
	 * @param docType Identificador del tipo de documento
	 * @param stageId Identificador de la fase
	 * @return lista de procedimientos
	 */
	public IItemCollection getTemplatesInStage(int docType, int stageId)
	throws ISPACException;

	/**
	 * calcula la lista de plantillas de un tipo de documento
	 * generables desde el contexto de un array de fases
	 *
	 * @param docType Identificador del tipo de documento
	 * @param stagesId array de identificador de fases
	 * @return lista de procedimientos
	 */
	public IItemCollection getTemplatesInStages(int docType, int []stagesId)
	throws ISPACException;

	/**
	 * calcula la lista de plantillas de un tipo de documento
	 * generables desde el contexto de un tr�mite
	 *
	 * @param docType Identificador del tipo de documento
	 * @param taskId Identificador del tr�mite
	 * @return lista de procedimientos
	 */
	public IItemCollection getTemplatesInTask(int docType, int taskId)
	throws ISPACException;

	/**
	 * Calcula la lista de plantillas de un tipo de documento
	 * generables desde el contexto de un array de tr�mites
	 *
	 * @param docType Identificador del tipo de documento
	 * @param tasksId array de identificadores de tr�mites
	 * @return lista de procedimientos
	 */
	public IItemCollection getTemplatesInTasks(int docType, int []tasksId)
	throws ISPACException;
	
	/**
	 * Obtiene las plantillas de un tipo de documento en un tr�mite del procedimiento.
	 * @param docType Identificador del tipo de documento.
	 * @param nIdTaskPCD Identificador del tr�mite en el procedimiento.
	 * @return Colecci�n de plantillas.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public IItemCollection getTemplatesInPTask(int docType, int nIdTaskPCD)
			throws ISPACException;
	
	/**
	 * 
	 * @param stageId Identificador de la fase instanciada
	 * @param docType Identificador del tipo de documentos
	 * @param entityId Identificador de la entidad
	 * @param regId Identificador del registro de la entidad
	 * @return
	 * @throws ISPACException
	 */
	public IItem createStageDocument(int stageId, int docType, int entityId, int regId) throws ISPACException;
	
	/**
	 * Crea un registro de la entidad de documentos asoci�ndolo a una fase
	 * @param stageId Identificador de la fase instanciada
	 * @param docType Identificador del tipo de documento
	 * @return Registro de la entidad creado
	 * @throws ISPACException
	 */
	public IItem createStageDocument(int stageId,int docType)
	throws ISPACException;
	
	/**
	 * Crea un registro de la entidad de documentos asoci�ndolo a una actividad
	 * @param activityId Identificador de la actividad instanciada
	 * @param taskId Identificador del tr�mite instanciado
	 * @param taskPcdId Identificador del tr�mite en el procedimiento
	 * @param docType Identificador del tipo de documento
	 * @param idEntidad Identificador de la entidad
	 * @param regId Identificador del registro de la entidad
	 * @return Registro de la entidad creado
	 * @throws ISPACException
	 */
	public IItem createActivityDocument(int activityId, int taskId, int taskPcdId, int docType, int idEntid, int regId)
	throws ISPACException;
	
	
	/**
	 * Crea un registro de la entidad de documentos asoci�ndolo a una actividad
	 * @param activityId Identificador de la actividad instanciada
	 * @param taskId Identificador del tr�mite instanciado
	 * @param taskPcdId Identificador del tr�mite en el procedimiento
	 * @param docType Identificador del tipo de documento
	 * @return Registro de la entidad creado
	 * @throws ISPACException
	 */
	public IItem createActivityDocument(int activityId, int taskId, int taskPcdId, int docType)
	throws ISPACException;

	/**
	 * 
	 * @param taskId Identificador del tr�mite instanciado
	 * @param docType Identificador del tipo de documento
	 * @param entityId Identificador de la entidad
	 * @param regId Identificador de registro de la entidad con el que estamos trabajando
	 * @return
	 * @throws ISPACException
	 */
	public IItem createTaskDocument(int taskId, int docType, int entityId, int regId)
	throws ISPACException;
	
	/**
	 * Crea un registro de la entidad de documentos asoci�ndolo a un tr�mite
	 * @param taskId Identificador del tr�mite instanciado
	 * @param docType Identificador del tipo de documento
	 * @return Registro de la entidad creado
	 * @throws ISPACException
	 */
	public IItem createTaskDocument(int taskId,int docType)
	throws ISPACException;

	/**
	 * Crea un registro de la entidad de documentos y lo asociamos a la tramitaci�n agrupada
	 * @param batchTaskId Identificador de la tramitaci�n agrupada
	 * @param taskId Identificador del tr�mtie instanciado
	 * @param docType Identificador del tipo de documento
	 * @param idTramite Identificador del �ltimo tr�mite creado
	 * @param idTemplate Identificador de la plantilla
	 * @return Registro de la entidad creado
	 * @throws ISPACException
	 */
	public IItem createBatchTaskDocument(int batchTaskId, int taskId, int docType, int idTramite, int idTemplate)
	throws ISPACException;

	/**
	 * 
	 * @param connectorSession Conexi�n con el conector documental
	 * @param stageId Identificador de la fase instanciada
	 * @param docId Identificador del documento
	 * @param templateId Identificador de la plantilla
	 * @param entityId Identificador de la entidad
	 * @param regId Identificador del registro de la entidad
	 * @return Registro de la entidad de documentos
	 * @throws ISPACException
	 */
	public IItem attachStageTemplate(Object connectorSession, int stageId, int docId,int templateId, int entityId, int regId)
	throws ISPACException;
	/**
	 * Adjunta un documento a una fase gener�ndolo partiendo de una plantilla
	 * @param connectorSession Conexi�n con el conector documental
	 * @param stageId Identificador de la fase instanciada
	 * @param docId Identificador del documento
	 * @param templateId Identificador de la plantilla
	 * @return Registro de la entidad de documentos
	 * @throws ISPACException
	 */
	public IItem attachStageTemplate(Object connectorSession, int stageId,int docId,int templateId)
	throws ISPACException;

	/**
	 * Adjunto un documento a una fase a partir de una plantilla descargada.
	 * @param connectorSession Conexi�n con el conector documental 
	 * @param stageId Identificador de la fase instanciada
	 * @param taskId Identificador del tr�mtie instanciado
	 * @param taskPcdId Identificador del tr�mtie en el procedimiento
	 * @param docId Identificador del documento
	 * @param templateId Identificador de la plantilla
	 * @param sFileTemplate Ruta de la plantilla
	 * @param entity Identificador de la entidad
	 * @param key Identificador del registro de la entidad
	 * @return Registro de la entidad de documentos
	 * @throws ISPACException
	 */	public IItem attachStageTemplate(Object connectorSession, int stageId,int docId,int templateId,String sFileTemplate,int entity,int key)
	throws ISPACException;
	
	/**
	 * Adjunto un documento a una actividad a partir de una plantilla descargada.
	 * @param connectorSession Conexi�n con el conector documental 
	 * @param activityId Identificador de la actividad instanciada
	 * @param taskId Identificador del tr�mtie instanciado
	 * @param taskPcdId Identificador del tr�mtie en el procedimiento
	 * @param docId Identificador del registro de la entidad documentos
	 * @param templateId Identificador de la plantilla
	 * @param sFileTemplate Ruta de la plantilla
	 * @param entity Identificador de la entidad
	 * @param key Identificador del registro de la entidad
	 * @return Registro de la entidad de documentos
	 * @throws ISPACException
	 */
	public IItem attachActivityTemplate(Object connectorSession, int activityId, int taskId, int taskPcdId, int docId, int templateId, String sFileTemplate, int entity, int key)
	throws ISPACException;
	
	/**
	 * 
	 * @param connectorSession Conexi�n con el conector documental
	 * @param taskId Identificador de la fase instanciada
	 * @param docId Identificador del documento
	 * @param templateId Identificador de la plantilla
	 * @param entityId Identificador de la entidad
	 * @param regId Identificador del registro de la entidad
	 * @return Registro de la entidad de documentos
	 * @throws ISPACException
	 */
	public IItem attachTaskTemplate(Object connectorSession, int taskId, int docId,int templateId, int entityId, int regId)
	throws ISPACException;
	
	/**
	 * Adjunta un documento a un tr�mite gener�ndolo partiendo de una plantilla
	 * @param connectorSession Conexi�n con el conector documental
	 * @param taskId Identificador del tr�mite instanciado
	 * @param docId Identificador del registro de la entidad documentos
	 * @param templateId Identificador de la plantilla
	 * @return Registro de la entidad de documentos 
	 * @throws ISPACException
	 */
	public IItem attachTaskTemplate(Object connectorSession, int taskId, int docId,int templateId)
	throws ISPACException;

	
	/**
	 * Adjunto un documento a un tr�mite a partir de una plantilla descargada.
	 * @param connectorSession Conexi�n con el conector documental 
	 * @param taskId Identificador del tr�mtie instanciado
	 * @param docId Identificador del registro de la entidad documentos
	 * @param templateId Identificador de la plantilla
	 * @param sFileTemplate Ruta de la plantilla
	 * @param entity Identificador de la entidad
	 * @param key Identificador del registro de la entidad
	 * @return Registro de la entidad de documentos
	 * @throws ISPACException
	 */
	public IItem attachTaskTemplate(Object connectorSession, int taskId, int docId,int templateId,String sFileTemplate,int entity,int key)
	throws ISPACException;

	/**
	 * Adjunto un documento a un tr�mite a partir de una plantilla descargada.
	 * @param connectorSession Conexi�n con el conector documental 
	 * @param taskId Identificador del tr�mtie instanciado
	 * @param docId Identificador del registro de la entidad documentos
	 * @param templateId Identificador de la plantilla
	 * @param sFileTemplate Ruta de la plantilla
	 * @return Registro de la entidad de documentos
	 * @throws ISPACException
	 */
	public IItem attachTaskTemplate(Object connectorSession, int taskId, int docId,int templateId,String sFileTemplate)
	throws ISPACException;
	
	/**
	 * Anexa un fichero a una fase.
	 * @param connectorSession Conexi�n con el conector documental
	 * @param stageId Identificador de la fase instanciada
	 * @param docId Identificador del registro de la entidad documentos
	 * @param in Representaci�n del fichero a anexar
	 * @param length Tama�o del fichero
	 * @param sMimeType Tipo mime del fichero
	 * @param sName Nombre del fichero
	 * @return Registro de la entidad documentos
	 * @throws ISPACException
	 */
	public IItem attachStageInputStream(Object connectorSession, int stageId,int docId,InputStream in,int length,String sMimeType,String sName)
	throws ISPACException;


	/**
	 *  Anexa un fichero a una fase incluyendo propiedades para la metainformaci�n del documento
	 * @param connectorSession Conexi�n con el conector documental
	 * @param obj objeto a pasar al instanciar al gestor documental dependiente 
	 * @param stageId Identificador de la fase instanciada
	 * @param docId Identificador del registro de la entidad documentos
	 * @param in Representaci�n del fichero a anexar
	 * @param length Tama�o del fichero
	 * @param mimeType Tipo mime del fichero
	 * @param sName Nombre del fichero
	 * @param properties Propiedades a asociar al documento
	 * @return Registro de la entidad documentos
	 * @throws ISPACException
	 */
	public IItem attachStageInputStream(Object connectorSession, Object obj, int stageId, int docId, InputStream in, int length, String mimeType, String sName, String properties)
	throws ISPACException;

	/**
	 * Anexa un fichero a un tr�mite
	 * @param connectorSession Conex�n con el conector documental
	 * @param taskId Identificador del tr�mite instanciado
	 * @param docId Identificador del registro de la entidad documentos
	 * @param in Representaci�n del fichero a anexar
	 * @param length Tama�o del fichero
	 * @param sMimeType Tipo mime del fichero
	 * @param sName Nombre del fichero
	 * @return Registro de la entidad documentos
	 * @throws ISPACException
	 */
	public IItem attachTaskInputStream(Object connectorSession, int taskId,int docId,InputStream in,int length,String sMimeType,String sName)
	throws ISPACException;

	
	
	/**
	 * Anexa un fichero a un tr�mite incluyendo propiedades para la metainformaci�n del documento
	 * @param connectorSession Conex�n con el conector documental
	 * @param obj objeto a pasar al instanciar al gestor documental dependiente 
	 * @param taskId Identificador del tr�mite instanciado
	 * @param docId Identificador del registro de la entidad documentos
	 * @param in Representaci�n del fichero a anexar
	 * @param length Tama�o del fichero
	 * @param sMimeType Tipo mime del fichero
	 * @param sName Nombre del fichero
	 * @param properties Propiedades a asociar al documento
	 * @return Registro de la entidad documentos
	 * @throws ISPACException
	 */
	public IItem attachTaskInputStream(Object connectorSession, Object obj, int taskId, int docId, InputStream in, int length, String sMimeType, String sName, String properties)
	throws ISPACException;	
	
	/**
	 * Adjunta un documento a una fase a partir de una plantilla descargada
	 * @param stageId Identificador de la fase instanciada
	 * @param docType Identificador del tipo de documento
	 * @param templateId Identificador de la plantilla
	 * @param sFileTemplate Ruta de la plantilla
	 * @param entity Identificador de la entidad
	 * @param key Identificador del registro de la entidad
	 * @return Identificador del documento creado
	 * @throws ISPACException
	 */
	public String createStageDocument(int stageId,int docType,int templateId,String sFileTemplate,int entity,int key)
	throws ISPACException;
	
	/**
	 * Adjunta un documento a una actividad a partir de una plantilla descargada
	 * @param activityId Identificador de la actividad instanciada
	 * @param taskId Identificador del tr�mite instanciado
	 * @param taskPcdId Identificador del tr�mite en el procedimiento
	 * @param docType Identificador del tipo de documento
	 * @param templateId Identificador de la plantilla
	 * @param sFileTemplate Ruta de la plantilla
	 * @param entity Identificador de la entidad
	 * @param key Identificador de un registro de la entidad
	 * @return Identificador del documento creado
	 * @throws ISPACException
	 */
	public String createActivityDocument(int activityId, int taskId, int taskPcdId, int docType,int templateId,String sFileTemplate,int entity,int key)
	throws ISPACException;


	/**
	 * Adjunta un documento a un tr�mite a partir de una plantilla descargada
	 * @param taskId Identificador del tr�mite instanciado
	 * @param docType Identificador del tipo de documento
	 * @param templateId Identificador de la plantilla
	 * @param sFileTemplate Ruta de la plantilla
	 * @param entity Identificador de la entidad
	 * @param key Identificador de un registro de la entidad
	 * @return Identificador del documento creado
	 * @throws ISPACException
	 */
	public String createTaskDocument(int taskId,int docType,int templateId,String sFileTemplate,int entity,int key)
	throws ISPACException;
	/**
	 * Adjunta un documento a un tr�mite a partir de una plantilla descargada
	 * @param taskId Identificador del tr�mite instanciado
	 * @param docType Identificador del tipo de documento
	 * @param templateId Identificador de la plantilla
	 * @param sFileTemplate Ruta de la plantilla
	 * @return Identificador del documento creado
	 * @throws ISPACException
	 */
	public String createTaskDocument(int taskId,int docType,int templateId,String sFileTemplate)
	throws ISPACException;

	/**
	 * Obtiene un fichero
	 * @param connectorSession Conexi�n con el conector documental
	 * @param sDocRef Identificador del documento en el gestor documental
	 * @param out variable de retorno del fichero solicitado
	 * @throws ISPACException
	 */
	public void getDocument(Object connectorSession, String sDocRef, OutputStream out)
	throws ISPACException;

	/**
	 * Actualiza un documento.
	 * @param connectorSession Conexi�n con el conector documental
	 * @param sDocRef Identificador del documento en el gestor documental
	 * @param in Representaci�n del fichero a actualizar
	 * @param length Tama�o del fichero
	 * @param sMimetype Tipo mime del fichero
	 * @throws ISPACException
	 */
	public void setDocument(Object connectorSession, int docId, String sDocRef, InputStream in,int length,String sMimetype)
	throws ISPACException;

	/**
	 * Obtiene el mimetype de un documento almacenado en el gestor documental
	 * @param connectorSession Conexi�n con el conector documental
	 * @param sDocRef Identificador del documento en el gestor documental
	 * @return mimetype de un documento
	 * @throws ISPACException
	 */
	public String getMimeType(Object connectorSession, String sDocRef)
	throws ISPACException;

	/**
	 * Obtiene el tama�o de un documento almacenado en el gestor documental
	 * @param connectorSession Conexi�n con el conector documental
	 * @param sDocRef Identificador del documento en el gestor documental
	 * @return tama�o de un documento
	 * @throws ISPACException
	 */
	public int getDocumentSize(Object connectorSession, String sDocRef)
	throws ISPACException;

	/**
	 * Comprueba la existencia de un documento en el gestor documental
	 * @param connectorSession Conexi�n con el conector documental
	 * @param sDocRef Identificador del documento en el gestor documental
	 * @return true si existe el documento, false en caso contrario
	 * @throws ISPACException
	 */
	public boolean existsDocument(Object connectorSession, String sDocRef)
	throws ISPACException;

	/**
	 * Elimina un documento del gestor documental
	 * @param connectorSession Conexi�n con el conector documental
	 * @param sDocRef Identificador del documento en el gestor documental
	 * @throws ISPACException
	 */
	public void deleteDocument(Object connectorSession, String sDocRef)
	throws ISPACException;

	/**
	 * Vuelca una plantilla en un fichero del directorio temporal
	 * @param templateId Identificador de la plantilla
	 * @return Ruta completa del fichero generado
	 * @throws ISPACException
	 */
	public String getTemporaryTemplate(int templateId)
	throws ISPACException;
	
	/**
	 * @param connectorSession Conexi�n con el conector documental
	 * @param obj objeto a pasar al instanciar al gestor documental dependiente
	 * @param in Representaci�n del documento a almacenar
	 * @param length tama�o del documento
	 * @param properties propiedades a establecer en la metainformaci�n del docuemnto
	 * @return Identificador del documento en el gestor documental
	 * @throws ISPACException
	 */
	public String newDocument(Object connectorSession, Object obj, InputStream in, int length, String properties)	
	throws ISPACException;

	/**
	 * @param connectorSession Conexi�n con el conector documental
	 * @param sDocRef Identificador del documento en el gestor documental
	 * @return XML con los valores de todas las propiedades del documento
	 * @throws ISPACException
	 */
	public String getDocumentProperties(Object connectorSession, String sDocRef) 
	throws ISPACException;
	
	/**
	 * Obtiene el valor de una propiedad de la metainformaci�n del documento.
	 * @param connectorSession Conexi�n con el conector documental
	 * @param sDocRef Identificador del documento en el gestor documental
	 * @param name Nombre de la propiedad
	 * @return Valor de la propiedad
	 * @throws ISPACException
	 */
	public String getDocumentProperty(Object connectorSession, String sDocRef, String name)
	throws ISPACException;

	/**
	 * Establace el valor de una propiedad en la metainformaci�n del documento.
	 * @param connectorSession Conexi�n con el conector documental
	 * @param sDocRef Identificador del documento en el gestor documental
	 * @param name nombre de la propiedad
	 * @param value valor de la propiedad
	 * @throws ISPACException
	 */
	public void setDocumentProperty(Object connectorSession, String sDocRef, String name, String value)
	throws ISPACException;
	
	/**
	 * Obtiene la informaci�n de un repositorio.
	 * @param repId Identificador del repositorio.
	 * @return XML con la informaci�n del repositorio.
	 * @throws ISPACException si ocurre alg�n error.
	 */

	public String getRepositoryInfo(Object connectorSession, String repId) 
	throws ISPACException;
	
	/**
	 * Inicia una conexi�n con el gestor documental manti�ndose abierta 
	 * para consecutivas llamadas al mismo.
	 * @return Conexi�n con el gestor documental
	 * @throws ISPACException
	 */
	public Object createConnectorSession()
	throws ISPACException;

	/**
	 * Cierra la conexi�n abierta con el gestor documental.
	 * @param connectorSession Conexi�n con el conector documental
	 * @throws ISPACException
	 */
	public void closeConnectorSession(Object connectorSession)
	throws ISPACException;

	
	
	/**
	 * Genera un documento a partir de una plantilla y lo asocia a una actividad.
	 * @param connectorSession Conexi�n con el conector documental
	 * @param activityId Identificador de la actividad instanciada
	 * @param taskId Identificador del tr�mite instanciado
	 * @param taskPcdId Identificador del tr�mite en el procedimiento 
	 * @param documentId Identificador del documento
	 * @param templateId Identificador de la plantilla 
	 * @param entityId Identificador de la entidad
	 * @param regId Identificador del registro de la entidad
	 * @return IItem con los datos del documento
	 * @throws ISPACException
	 */
	public abstract IItem attachActivityTemplate(Object connectorSession, int activityId, int taskId, int taskPcdId, int documentId, int templateId, int entityId, int regId)
	throws ISPACException;
	
	/**
	 * Genera un documento a partir de una plantilla y lo asocia a una actividad.
	 * @param connectorSession Conexi�n con el conector documental
	 * @param activityId Identificador de la actividad instanciada
	 * @param taskId Identificador del tr�mite instanciado
	 * @param taskPcdId Identificador del tr�mite en el procedimiento 
	 * @param documentId Identificador del documento
	 * @param templateId Identificador de la plantilla 
	 * @return IItem con los datos del documento
	 * @throws ISPACException
	 */
	public abstract IItem attachActivityTemplate(Object connectorSession, int activityId, int taskId, int taskPcdId, int documentId, int templateId)
	throws ISPACException;

	/**
	 * Inicia la notificaci�n de un documento.
	 * @param processId Identificador del proceso.
	 * @param stagePcdId Identificador de la fase en el procedimiento.
	 * @param taskPcdId Identificador del tr�mite en el procedimiento.
	 * @param documentId Identificador del documento.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void initNotification(int processId, int stagePcdId, int taskPcdId, 
			int documentId) throws ISPACException;

	/**
	 * Inicia la notificaci�n de un documento.
	 * @param document Informaci�n del documento
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void initNotification(IItem document) throws ISPACException;

	/**
	 * Actualiza el estado de la notificaci�n de un documento.
	 * @param documentId Identificador del documento.
	 * @param status Estado de notificaci�n ({@link NotifyStatesConstants}).
	 * @param date Fecha de notificaci�n.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void updateNotificationStatus(int documentId, String status, 
			Date date) throws ISPACException;

	/**
	 * [Dipucr-Manu Ticket #478] + ALSIGM3 Nueva opci�n Repositorio Com�n
	 * 
	 * Anexa un fichero a una fase reemplazando previamente los tags.
	 * @param connectorSession Conexi�n con el conector documental
	 * @param stageId Identificador de la fase instanciada
	 * @param docId Identificador del registro de la entidad documentos
	 * @param in Representaci�n del fichero a anexar
	 * @param length Tama�o del fichero
	 * @param sMimeType Tipo mime del fichero
	 * @param sName Nombre del fichero
	 * @return Registro de la entidad documentos
	 * @throws ISPACException
	 */
	public IItem attachStageInputStreamReplaceVars(Object connectorSession, int stageId,int docId,InputStream in,int length,String sMimeType,String sName)
	throws ISPACException;


	/**
	 * [Dipucr-Manu Ticket #478] + ALSIGM3 Nueva opci�n Repositorio Com�n
	 * 
	 *  Anexa un fichero a una fase incluyendo propiedades para la metainformaci�n del documento reemplazando previamente los tags.
	 * @param connectorSession Conexi�n con el conector documental
	 * @param obj objeto a pasar al instanciar al gestor documental dependiente 
	 * @param stageId Identificador de la fase instanciada
	 * @param docId Identificador del registro de la entidad documentos
	 * @param in Representaci�n del fichero a anexar
	 * @param length Tama�o del fichero
	 * @param mimeType Tipo mime del fichero
	 * @param sName Nombre del fichero
	 * @param properties Propiedades a asociar al documento
	 * @return Registro de la entidad documentos
	 * @throws ISPACException
	 */
	public IItem attachStageInputStreamReplaceVars(Object connectorSession, Object obj, int stageId, int docId, InputStream in, int length, String mimeType, String sName, String properties)
	throws ISPACException;

	/**
	 * [Dipucr-Manu Ticket #478] + ALSIGM3 Nueva opci�n Repositorio Com�n
	 * 
	 * Anexa un fichero a un tr�mite reemplazando previamente los tags.
	 * @param connectorSession Conex�n con el conector documental
	 * @param taskId Identificador del tr�mite instanciado
	 * @param docId Identificador del registro de la entidad documentos
	 * @param in Representaci�n del fichero a anexar
	 * @param length Tama�o del fichero
	 * @param sMimeType Tipo mime del fichero
	 * @param sName Nombre del fichero
	 * @return Registro de la entidad documentos
	 * @throws ISPACException
	 */
	public IItem attachTaskInputStreamReplaceVars(Object connectorSession, int taskId,int docId,InputStream in,int length,String sMimeType,String sName)
	throws ISPACException;

	
	
	/**
	 * [Dipucr-Manu Ticket #478] + ALSIGM3 Nueva opci�n Repositorio Com�n
	 * 
	 * Anexa un fichero a un tr�mite incluyendo propiedades para la metainformaci�n del documento reemplazando previamente los tags.
	 * @param connectorSession Conex�n con el conector documental
	 * @param obj objeto a pasar al instanciar al gestor documental dependiente 
	 * @param taskId Identificador del tr�mite instanciado
	 * @param docId Identificador del registro de la entidad documentos
	 * @param in Representaci�n del fichero a anexar
	 * @param length Tama�o del fichero
	 * @param sMimeType Tipo mime del fichero
	 * @param sName Nombre del fichero
	 * @param properties Propiedades a asociar al documento
	 * @return Registro de la entidad documentos
	 * @throws ISPACException
	 */
	public IItem attachTaskInputStreamReplaceVars(Object connectorSession, Object obj, int taskId, int docId, InputStream in, int length, String sMimeType, String sName, String properties)
	throws ISPACException;	

}