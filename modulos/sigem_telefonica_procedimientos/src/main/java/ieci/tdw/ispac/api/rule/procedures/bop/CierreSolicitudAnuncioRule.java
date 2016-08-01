package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class CierreSolicitudAnuncioRule implements IRule
{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(CierreSolicitudAnuncioRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException
    {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        IItemCollection collAnuncios = null;
        IItem itemAnuncio = null;
        IItem itemProceso = null;

        try
        {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        ITXTransaction txAPI = invesFlowAPI.getTransactionAPI();

	        // Se obtiene la fecha de publicaci�n
	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        logger.warn("Query: " + strQuery);
	        IItemCollection bops = entitiesAPI.queryEntities("BOP_PUBLICACION", strQuery);
	        Iterator it = bops.iterator();
        	IItem bop = null;
	        if(it.hasNext()) {
	        	bop = ((IItem)it.next());
	        }
			String fecha = bop.getString("FECHA");
			Date fechaPub = bop.getDate("FECHA"); 
			logger.warn("Fecha: " + fecha);

			//Se obtiene la lista de solicitudes de anuncio para dicho BOP
	        strQuery = "WHERE fecha_publicacion='" + df.format(fechaPub) + "'";
	        logger.warn("Query: " + strQuery);
	        collAnuncios = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
	        it = collAnuncios.iterator();
	        
	        //Recorremos la lista de anuncios publicados en el BOP actual
	        while (it.hasNext())
	        {
	        	itemAnuncio = (IItem)it.next();
        		String numexpToClose = itemAnuncio.getString("NUMEXP");
    			logger.warn("N�mero de expediente que se va a cerrar: " + numexpToClose);

        		//Obtenemos el proceso
    	        //[eCenpri-Felipe #893] Se estaba cogiendo el id del expediente en vez del id del proceso
    	        //Se perdi�n la sincronizaci�n, al ejecutar la instrucci�n closeProcess se produce un error
    	        itemProceso = invesFlowAPI.getProcess(numexpToClose);
    	        int idProc = itemProceso.getInt("ID");
    	        
    	        // Cerramos todos los tr�mites abiertos del expediente
    	        String strQuery3 = "WHERE NUMEXP='" + numexpToClose + "'";
		        IItemCollection collTramites = entitiesAPI.queryEntities("SPAC_TRAMITES", strQuery3);
		        Iterator it3 = collTramites.iterator();
	        	IItem itemTramite = null;
	        	//[eCenpri-Felipe #484] Si por error se ha creado m�s de un tr�mite, se cierran todos
//    		    if (it3.hasNext())
	        	while (it3.hasNext())
		        {
		        	itemTramite = ((IItem)it3.next());
		        	int idTram = itemTramite.getInt("ID");
	        		logger.warn("Cerrando el tr�mite con ID = " + idTram);
		        	txAPI.closeTask(idTram);
		        }
		        
		        IItemCollection fases = invesFlowAPI.getStagesProcess(idProc);
		        Iterator it4 = fases.iterator();
	        	IItem fase = null;
		        if (it4.hasNext()) 
		        {
		        	fase = ((IItem)it4.next());
		        	int idFase = fase.getInt("ID");
	        		logger.warn("Cerrando la fase con ID = " + idFase);
		        	txAPI.closeStage(idFase);
		        }
		        
        		logger.warn("Cerrando el proceso de solicitud de anuncio con ID = " + idProc);
        		txAPI.closeProcess(idProc);
	        }

	        return new Boolean(true);
        } 
        catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido cerrar la sesi�n",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException
	{
    }
}
