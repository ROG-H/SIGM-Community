package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

/**
 * @author teresa
 * @date 18/11/2009
 * @prop�sito Genera el documento de Elaboraci�n del pliego para el expediente de Convocatoria de contrataci�n al iniciar
 *  el tr�mite de Elaboraci�n del Pliego de cl�usulas Econ�mico-Administrativas y le concatena los documentos de Prescripciones t�cnicas
 *  y Valoraci�n de la oferta.
 */
public class GenerateElaboracionPliegoRule implements IRule {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(GenerateElaboracionPliegoRule.class);
	
	protected String STR_DocValoracionOferta = "Valor oferta";
	protected String STR_DocElaboracionPliego = "Elab pliego";
	protected String STR_DocPrescripcionesTecnicas = "Pres tecnicas";
	protected String STR_TemplateElaboracionPliego = "Elaboraci�n del pliego";
	
	private OpenOfficeHelper ooHelper = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{

			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
	        //----------------------------------------------------------------------------------------------
			
			String nameDocValoracionOferta = "";
			String nameDocElaboracionPliego = "";
			String nameDocPrescripcionesTecnicas = "";
			File file = null;

			// Obtener el documento de "Valoraci�n de la oferta"
			
			//Obtenemos el nombre del documento de "Valoraci�n de la oferta" a partir de su c�digo
			nameDocValoracionOferta = CommonFunctions.getNombreTpDoc(rulectx, STR_DocValoracionOferta);
	        
			//Obtenemos el nombre del documento de "Elaboraci�n del pliego" a partir de su c�digo
	        nameDocElaboracionPliego = CommonFunctions.getNombreTpDoc(rulectx, STR_DocElaboracionPliego);
	        
			//Obtenemos el nombre del documento de "Prescripciones t�cnicas" a partir de su c�digo
	        nameDocPrescripcionesTecnicas = CommonFunctions.getNombreTpDoc(rulectx, STR_DocPrescripcionesTecnicas);
	        

	        //Generar documento de "Elaboraci�n del pliego" desde plantilla.
	        //Despu�s concatenar el file de "Valoraci�n de la oferta" y el de "Prescripciones t�cnicas"
	        
			//Generaci�n del documento a partir de la plantilla
			CommonFunctions.generarDocumento(rulectx, nameDocElaboracionPliego, STR_TemplateElaboracionPliego, "previo");
	        
			//Abro el documento con OppenOffice para concatenarle los otros dos documentos
			String strInfoPag = CommonFunctions.getInfoPag(rulectx, STR_TemplateElaboracionPliego+" - previo");
        	File file1 = CommonFunctions.getFile(rulectx, strInfoPag);
        	//String cnt = "uno:socket,host=localhost,port=8100;urp;StarOffice.NamingService";
        	//ooHelper = OpenOfficeHelper.getInstance(cnt);
        	ooHelper = OpenOfficeHelper.getInstance();
        	XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());

        	
        	//Es m�s pr�ctico meter el salto de p�gina en la plantilla
    		CommonFunctions.insertaSaltoDePagina(xComponent);
	        
	        //Obtenemos el documento de "Valoraci�n de la oferta" a partir de su nombre
    		String infoPagValoracionOferta = CommonFunctions.getInfoPagNombre(rulectx, nameDocValoracionOferta);
    		file = CommonFunctions.getFile(rulectx, infoPagValoracionOferta);
	        
	        //Concatenar el documento de "Valoraci�n de la oferta" al de "Elaboraci�n del pliego"
    		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
    		file.delete();
    		CommonFunctions.insertaSaltoDePagina(xComponent);
    		
	        //Obtenemos el documento de "Prescripciones t�cnicas" a partir de su nombre
    		String infoPagPrescripcionesTecnicas = CommonFunctions.getInfoPagNombre(rulectx, nameDocPrescripcionesTecnicas);
    		file = CommonFunctions.getFile(rulectx, infoPagPrescripcionesTecnicas);
	        
	        //Concatenar el documento de "Prescripciones t�cnicas" al de "Elaboraci�n del pliego"
    		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
    		file.delete();
    		//CommonFunctions.insertaSaltoDePagina(xComponent);
    	
    		//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
    		OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
    		file1.delete();

    		//Guarda el resultado en gestor documental
			String strQuery = "WHERE NOMBRE = '"+nameDocElaboracionPliego+"'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
	        Iterator it = collection.iterator();
	        int tpdoc = 0;
	        if (it.hasNext())
	        {
	        	IItem tpd = (IItem)it.next();
	        	tpdoc = tpd.getInt("ID");
	        }
    		IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
    		FileInputStream in = new FileInputStream(file);
    		int docId = newdoc.getInt("ID");
    		Object connectorSession = gendocAPI.createConnectorSession();
    		IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", nameDocElaboracionPliego);
    		entityDoc.set("EXTENSION", "doc");
    		entityDoc.store(cct);
    		file.delete();
    		
    		//Borra el documento previo del gestor documental
			strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'" +
					" AND DESCRIPCION = '"+STR_TemplateElaboracionPliego+" - previo'";
	        collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
    		
	        
        	return new Boolean(true);

    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido crear el documento de Elaboraci�n del pliego",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }


}