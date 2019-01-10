package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * 
 * @author teresa
 * @date 30/03/2010
 * @prop�sito Validar el campo Transici�n de un expediente al iniciar un tr�mite
 */
public class ValidateStatusTaskPlaneamientoRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(ValidateStatusTaskPlaneamientoRule.class);

    private static String strEntidad = "PLAN_POM";
    private static String strIniciado = "_I";
    private static String strCerrado = "_C";
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        
        try{

            //Obtenemos la �ltima transicion ejecutada
            IItem exp = null;
            String transicion = null;
            
            try{
                exp = getExpediente(rulectx);
                transicion = exp.getString("TRANSICION");
                
            } catch (Exception e){
                LOGGER.info("No se mostraba mensaje alguno. Error al obtener la informaci�n del expediente. " + e.getMessage(), e);

                rulectx.setInfoMessage("Error al obtener la informaci�n del expediente");
                return false;
            }
            
            //Comprobamos que el tr�mite actual no se encuentre en estado iniciado (sino cerrado)
            if (isTramiteIniciado(transicion)) {
                rulectx.setInfoMessage("No se puede iniciar el tr�mite ya que existe otro en proceso");
                return false;
                
            } else {
                //Comprobamos si la transici�n inicio-fin a ejecutar existe en la tabla de transiciones
                String inicio = getSegundoTramite(transicion);
                String fin = null;
                
                int idCtTramite = rulectx.getClientContext().getAPI().getTask(rulectx.getTaskId()).getInt("ID_CTTRAMITE");
                String codTram = getCodTram(idCtTramite, rulectx);
                
                if (StringUtils.isNotEmpty(codTram)){
                    fin = tramiteMultiple (codTram, transicion, rulectx);
                    
                    if (fin == null){
                        fin = codTram;
                    }
                    
                    try{
                        getTransicion(inicio, fin, rulectx);
                        
                    } catch (ISPACInfo exception){
                        LOGGER.info("No se mostraba mensaje alguno. No se puede iniciar el tr�maite desde el estado actual. " + exception.getMessage(), exception);

                        rulectx.setInfoMessage("No se puede iniciar el tr�mite desde el estado actual");                        
                        return false;
                    }
                } else {
                    rulectx.setInfoMessage("El tr�mite a iniciar no tiene definido un c�digo de tr�mite");
                    return false;
                }
                
            }
        } catch(ISPACException e){
            throw new ISPACRuleException(e);
        }
        
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            //----------------------------------------------------------------------------------------------
            
            //Obtenemos la �ltima transicion ejecutada
            IItem exp = null;
            String transicion = null;
            
            try{
                exp = getExpediente(rulectx);
                transicion = exp.getString("TRANSICION");
                
            } catch (Exception e){
                LOGGER.info("No se mostraba mensaje alguno. Error al obtener la informaci�n del expediente. " + e.getMessage(), e);
                throw new ISPACInfo("Error al obtener la informaci�n del expediente");
            }
            
            //Comprobamos que el tr�mite actual no se encuentre en estado iniciado (sino cerrado)
            if (isTramiteIniciado(transicion)){
                throw new ISPACInfo("No se puede iniciar el tr�mite ya que existe otro en proceso");
                
            } else {
                //Comprobamos si la transici�n inicio-fin a ejecutar existe en la tabla de transiciones
                String inicio = getSegundoTramite(transicion);
                String fin = null;
                
                int idCtTramite = rulectx.getClientContext().getAPI().getTask(rulectx.getTaskId()).getInt("ID_CTTRAMITE");
                String codTram = getCodTram(idCtTramite, rulectx);
                
                if (StringUtils.isNotEmpty(codTram)){
                    
                    fin = tramiteMultiple (codTram, transicion, rulectx);
                    
                    if (fin == null){
                        fin = codTram;
                    }
                    
                    IItem itTransicion = null;
                    
                    try{
                        itTransicion = getTransicion(inicio, fin, rulectx);
                        //Incrementar el n�mero de ejecuciones de esa transici�n
                        int ejecuciones = itTransicion.getInt("EJECUCIONES");
                        itTransicion.set("EJECUCIONES", ejecuciones+1);
                        itTransicion.store(cct);
                        
                        //Actualizar la transici�n anterior y la actual
                        exp.set("TRANSICION_ANTERIOR", transicion);
                        exp.set("TRANSICION", inicio+"-"+fin+strIniciado);
                        exp.store(cct);
                        
                    } catch (ISPACInfo exception){
                        LOGGER.info("No se mostraba mensaje alguno. No se puede iniciar el tr�mite desde el estado actual. " + exception.getMessage(), exception);

                        throw new ISPACInfo("No se puede iniciar el tr�mite desde el estado actual");
                    }
                    
                } else {
                    throw new ISPACInfo("El tr�mite a iniciar no tiene definido un c�digo de tr�mite");
                }
            }
            
        } catch(ISPACException e) {
            throw new ISPACRuleException(e);
        }
        return null;
        
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
    
    private String tramiteMultiple (String codTram, String transicion, IRuleContext rulectx) throws ISPACException{
        //Si el tramite codTram es m�ltiple (Tr�mite de Audiencia) hay que tener en cuenta desde que estado (transicion)
        //se est� iniciando la actual transici�n
        String inicio = getSegundoTramite(transicion);
        String fin = null;
        
        // Tr�mite de Providencia
        if ("PLA010".equals(codTram)){
            //if (transicion.substring(6, 11).equals("SAN08")){
            if ("PLA000".equals(inicio)){
                fin = "PLA010";
            } else if ("PLA030".equals(inicio) || "PLA042".equals(inicio)){
                fin = "PLA050";
            } else if ("PLA190".equals(inicio)){
                fin = "PLA200";
            }
        // Tr�mite de Publicaci�n    
        } else if ("SAN19".equals(codTram)){
            if ("PLA050".equals(inicio)){
                fin = "PLA061";
            } else if ("PLA130".equals(inicio)){
                fin = "PLA141";
            } else if ("PLA242".equals(inicio)){/////////////////
                fin = "PLA141"; //� fin = "PLA250";
            } else if ("PLA200".equals(inicio)){
                fin = "PLA141";
                int numTransiciones = getNumEjecuciones(inicio, fin, rulectx);
                
                fin = "PLA150";
                numTransiciones = numTransiciones + getNumEjecuciones(inicio, fin, rulectx);

                if (numTransiciones > 0){
                    throw new ISPACInfo("Esta transici�n �nicamente se puede realizar una vez");
                } else{
                    fin = "PLA141";
                }
                
            }
        // Tr�mite de Alegaciones    
        } else if ("PLA062".equals(codTram)){
            if ("PLA061".equals(inicio)){
                fin = "PLA062";
            } else if ("PLA141".equals(inicio)){
                fin = "PLA142";
            }
        // Tr�mite de Remisi�n a Consejer�a de Medio Ambiente    
        } else if ("PLA080".equals(codTram)){
            if ("PLA070".equals(inicio)){
                fin = "PLA080";
            } else if ("PLA160".equals(inicio)){
                fin = "PLA170";
            }
        // Tr�mite de Consultas sectoriales
        } else if ("PLA150".equals(codTram) && "PLA200".equals(inicio)){
            fin = "PLA150";
            int numTransiciones = getNumEjecuciones(inicio, fin, rulectx);
            
            fin = "PLA141";
            numTransiciones = numTransiciones + getNumEjecuciones(inicio, fin, rulectx);

            if (numTransiciones > 0){
                throw new ISPACInfo("Esta transici�n �nicamente se puede realizar una vez");
            } else{
                fin = "PLA150";
            }
        }
        return fin;
    }
    
    
    /**
     * 
     * @param transicion
     * @return Devuelve el c�digo del primer tr�mite de la transici�n
     */
    protected static String getPrimerTramite(String transicion){
        
        String primerTramite = null;
        
        int posGuion = transicion.indexOf('-');
            
        primerTramite = transicion.substring(0, posGuion);        
        return primerTramite;
    }
    
    /**
     * 
     * @param transicion
     * @return Devuelve el c�digo del segundo tr�mite de la transici�n
     */
    protected static String getSegundoTramite(String transicion){
        
        String segundoTramite = null;
        
        int posGuion = transicion.indexOf('-');
        int posGuionBajo = transicion.indexOf('_');
            
        segundoTramite = transicion.substring(posGuion+1, posGuionBajo);        
        
        return segundoTramite;
    }

    /**
     * 
     * @param transicion
     * @return Devuelve true si el tr�mite actual est� iniciado o false si est� cerrado
     * @throws ISPACInfo 
     */
    protected static boolean isTramiteIniciado(String transicion) throws ISPACInfo{
        
        if (transicion.endsWith(strIniciado)){
            return true;
        } else if (transicion.endsWith(strCerrado)){
            return false;
        } else{
            throw new ISPACInfo("Error al comprobar el estado del tr�mite actual");
        }

    }

    /**
     * 
     * @param inicio, fin, entitiesAPI
     * @return Devuelve la transici�n del expediente desde inicio hasta fin
     * @throws ISPACInfo 
     */
    protected static IItem getTransicion(String inicio, String fin, IRuleContext rulectx) throws ISPACException{
        
        //----------------------------------------------------------------------------------------------
        IClientContext cct =  rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
        
        String numExp = rulectx.getNumExp();
        
        IItem itTransicion = null;
        
        String strQuery = "WHERE NUMEXP='" + numExp + "' AND INICIO='" + inicio + "' AND FIN='" + fin + "'";
        
        IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
        Iterator<?> itTransiciones = collTransiciones.iterator();
        
        if (!itTransiciones.hasNext()){
            throw new ISPACInfo("Error al comprobar el estado actual");
        } else{
            itTransicion = (IItem)itTransiciones.next();
        }
        
        return itTransicion;
    }
    

    /**
     * 
     * @param inicio, fin, entitiesAPI
     * @return Devuelve el n�mero de veces que se ha ejecutado la transici�n del expediente desde inicio hasta fin
     * @throws ISPACInfo 
     */
    protected static int getNumEjecuciones(String inicio, String fin, IRuleContext rulectx) throws ISPACException{
        
        //----------------------------------------------------------------------------------------------
        IClientContext cct =  rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
        
        String numExp = rulectx.getNumExp();
        
        int numEjecuciones = -1;
        
        String strQuery = "WHERE NUMEXP='" + numExp + "' AND INICIO='" + inicio + "' AND FIN='" + fin + "'";
        
        IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
        Iterator<?> itTransiciones = collTransiciones.iterator();
        
        if (!itTransiciones.hasNext()){
            throw new ISPACInfo("Error al comprobar el estado actual");
        } else{
            IItem itTransicion = (IItem)itTransiciones.next();
            numEjecuciones = itTransicion.getInt("EJECUCIONES");
        }
        
        return numEjecuciones;
    }
    
    /**
     * 
     * @param idCtTramite, entitiesAPI
     * @return Devuelve el c�digo de tr�mite correspondiente al identificador idCtTramite
     * @throws ISPACInfo 
     */
    protected static String getCodTram(int idCtTramite, IRuleContext rulectx) throws ISPACException{
        
        //----------------------------------------------------------------------------------------------
        IClientContext cct =  rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
        
        String codTram = null;
        
        IItem itTram = null;
        String strQuery = "WHERE ID='" + idCtTramite + "'";
        IItemCollection collTrams = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
        Iterator<?> itTrams = collTrams.iterator();
        
        if (itTrams.hasNext()){
            itTram = (IItem)itTrams.next();
            codTram = itTram.getString("COD_TRAM");
        
        } else {
            throw new ISPACInfo("Error al obtener el c�digo de tr�mite");
        }
        
        return codTram;
        
    }
    
    /**
     * 
     * @param numExp, entitiesAPI
     * @return Devuelve el expediente con n�mero de expediente numExp de la entidad strEntidad
     * @throws ISPACInfo 
     */
    protected static IItem getExpediente(IRuleContext rulectx) throws ISPACException{
        
        //----------------------------------------------------------------------------------------------
        IClientContext cct =  rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
        
        String numExp = rulectx.getNumExp();
        
        IItem exp = null;
        
        IItemCollection collExps = entitiesAPI.getEntities(strEntidad, numExp);
        Iterator<?> itExps = collExps.iterator();
        
        if (itExps.hasNext()) {
            exp = (IItem)itExps.next();
            
        } else {
            throw new ISPACInfo("Error al obtener la informaci�n del expediente");
        }
        
        return exp;
    }
    
}
