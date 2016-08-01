package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import com.sun.star.lang.XComponent;

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

public class GenerateAnuncioConvocatoriaRule implements IRule {

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

	    	
	        //Actualiza el campo estado de la entidad
	        //de modo que permita mostrar los enlaces para crear la Solicitud BOP
	        String numexp = rulectx.getNumExp();
	        IItemCollection coll = entitiesAPI.getEntities("SUBV_CONVOCATORIA", numexp);
	        Iterator it = coll.iterator();
	        if (it.hasNext())
	        {
		        IItem entidad = (IItem)it.next();
		        entidad.set("ESTADO", "Inicio");
		        entidad.store(cct);
	        }
	        
	        //Generaci�n del documento de anuncio a partir de plnatilla
	        //---------------------------------------------------------
	        
	        //Obtenci�n de la informaci�n de la convocatoria
	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        coll = entitiesAPI.queryEntities("SUBV_CONVOCATORIA", strQuery);
	        it = coll.iterator();
        	IItem conv = null;
	        if(it.hasNext()) {
	        	conv = ((IItem)it.next());
	        }
			String strTitulo = conv.getString("TITULO");
			String strContenido = conv.getString("CONTENIDO");
			strTitulo = strTitulo.toUpperCase();
			strContenido = strContenido.replaceAll("\r\n", "\r"); //Evita saltos de l�nea duplicados
			cct.setSsVariable("TITULO", strTitulo);
			cct.setSsVariable("CONTENIDO", strContenido);
	        
	        //Generaci�n del comienzo del documento
			String strNombreDoc = CommonFunctions.getNombreTpDoc(rulectx, "Subv-AnunCab");
	        CommonFunctions.generarDocumento(rulectx, strNombreDoc, strNombreDoc, null);
        	String strInfoPag = CommonFunctions.getInfoPag(rulectx, strNombreDoc);
        	File file1 = CommonFunctions.getFile(rulectx, strInfoPag);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
			
    		//Generaci�n de las bases
    		File file = null;
    		String descr = "";
			strNombreDoc = CommonFunctions.getNombreTpDoc(rulectx, "Subv-AnunBases");
			strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "' ORDER BY NUMERO ASC";
	        IItemCollection bases = entitiesAPI.queryEntities("SUBV_BASES", strQuery);
	        it = bases.iterator();
	        while (it.hasNext())
	        {
	        	IItem norma = (IItem)it.next();
	        	int nNumero = norma.getInt("NUMERO");
	        	String strNumeroNorma = String.valueOf(nNumero);
	        	String strExtractoNorma = norma.getString("EXTRACTO");
	        	String strContenidoNorma = norma.getString("CONTENIDO");
	        	strContenidoNorma = strContenidoNorma.replaceAll("\r\n", "\r");
	        	cct.setSsVariable("NUMERO_NORMA", strNumeroNorma);
	        	cct.setSsVariable("EXTRACTO_NORMA", strExtractoNorma);
	        	cct.setSsVariable("CONTENIDO_NORMA", strContenidoNorma);

	        	CommonFunctions.generarDocumento(rulectx, strNombreDoc, strNombreDoc, strNumeroNorma);
	        	
	        	cct.deleteSsVariable("NUMERO_NORMA");
	        	cct.deleteSsVariable("EXTRACTO_NORMA");
	        	cct.deleteSsVariable("CONTENIDO_NORMA");

	        	descr = strNombreDoc + " - " + strNumeroNorma;
            	strInfoPag = CommonFunctions.getInfoPag(rulectx, descr);
        		file = CommonFunctions.getFile(rulectx, strInfoPag);
        		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
        		file.delete();
	        }

    		//Generaci�n del pie del documento
			strNombreDoc = CommonFunctions.getNombreTpDoc(rulectx, "Subv-AnunPie");
			CommonFunctions.generarDocumento(rulectx, strNombreDoc, strNombreDoc, null);
        	strInfoPag = CommonFunctions.getInfoPag(rulectx, strNombreDoc);
    		file = CommonFunctions.getFile(rulectx, strInfoPag);
    		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
    		file.delete();
    		
    		//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
    		OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
    		file1.delete();
    		
    		//Guarda el resultado en gestor documental
			strNombreDoc = CommonFunctions.getNombreTpDoc(rulectx, "Subv-Anuncio");
			strQuery = "WHERE NOMBRE = '"+strNombreDoc+"'";
	        IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
	        it = collection.iterator();
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
    		IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", strNombreDoc);
    		entityDoc.set("EXTENSION", "doc");
    		entityDoc.store(cct);
    		file.delete();
    		
    		//Borra los documentos intermedios del gestor documental
			strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'" +
				" AND (DESCRIPCION LIKE '" + CommonFunctions.getNombreTpDoc(rulectx, "Subv-AnunCab") + "%'" +
				" OR DESCRIPCION LIKE '" + CommonFunctions.getNombreTpDoc(rulectx, "Subv-AnunBases") + "%'" + 
				" OR DESCRIPCION LIKE '" + CommonFunctions.getNombreTpDoc(rulectx, "Subv-AnunPie") + "%')" ;				
	        collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
    		
	        cct.deleteSsVariable("TITULO");
	        cct.deleteSsVariable("CONTENIDO");
			
			
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido crear el anuncio de convocatoria",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}