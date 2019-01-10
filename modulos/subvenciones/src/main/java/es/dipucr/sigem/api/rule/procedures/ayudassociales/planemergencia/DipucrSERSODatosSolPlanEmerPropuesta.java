package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSODatosSolPlanEmerPropuesta implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSODatosSolPlanEmerPropuesta.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        String numexp = "";
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            
            StringBuilder listado = new StringBuilder(); 
            numexp = rulectx.getNumExp();
            List<String> expedientesResolucion = new ArrayList<String>();
            
            //Tenemos que devolver la siguiente cadena:
            /**    
                1.LOCALIDAD
                Beneficiario/a:                     NIF:
                Trabajador/a Social:
                N�mero miembros de la unidad familiar:
                N� cheques: 
                Importe cheques:                
             */
            int contadorBeneficiario = 1;
            String ciudad = "";
            String descripcionCiudad = "";
            int countCiudad = 0;
            String beneficiario = "";
            String nifBeneficiario = "";
            String trabajadorSocial = "";
            String nifTrabajadorSocial = "";
            String numMiembrosFamilia = "";
                        
            String tipoAyuda = "";
            String trimestre = "";
            
            int importeCheques = 0;
            
            int numCheques1 = 0;
            int semestreImpresos1 = 0;
            int importeCheques1 = 0;
            
            int numCheques2 = 0;
            int semestreImpresos2 = 0;
            int importeCheques2 = 0;
            
            int numCheques3 = 0;
            int semestreImpresos3 = 0;
            int importeCheques3 = 0;
            
            int numCheques4 = 0;
            int semestreImpresos4 = 0;
            int importeCheques4 = 0;
            
            int total = 0;
            
            expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS);
            
            //Ya tenemos los expedientes que vamos a decretar en esta iteraci�n
            if(!expedientesResolucion.isEmpty()){
                
                String strQuery = ConstantesString.WHERE + ExpedientesUtil.NUMEXP + " IN " + SubvencionesUtils.getWhereInFormat(expedientesResolucion);               
                IItemCollection expSolicitudesCiudadCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery);
                
                String strQuery2 = ConstantesString.WHERE + ConstantesSubvenciones.MunicipiosValidationTable.VALOR + " IN " + SubvencionesUtils.getWhereInFormat(expSolicitudesCiudadCol, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD) + ConstantesString.ORDER_BY + ConstantesSubvenciones.MunicipiosValidationTable.SUSTITUTO;                
                IItemCollection ciudadOrdenCol = entitiesAPI.queryEntities(ConstantesSubvenciones.MunicipiosValidationTable.NOMBRE_TABLA, strQuery2);
                Iterator<?> ciudadOrdenIt = ciudadOrdenCol.iterator();
                
                while(ciudadOrdenIt.hasNext()){
                    
                    IItemCollection expSolicitudesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery + " AND CIUDAD = '" +((IItem)ciudadOrdenIt.next()).getString("VALOR")+"' ORDER BY TIPOAYUDA, NOMBRESOLICITANTE, NOMBRE");
                    Iterator<?> expSolicitudesIt = expSolicitudesCol.iterator();
                    while (expSolicitudesIt.hasNext()){
                                                
                        beneficiario = "";
                        nifBeneficiario = "";
                        trimestre = "";            
                        importeCheques = 0;                    
                        numCheques1 = 0;
                        semestreImpresos1 = 0;
                        importeCheques1 = 0;                    
                        numCheques2 = 0;
                        semestreImpresos2 = 0;
                        importeCheques2 = 0;
                        numCheques3 = 0;
                        semestreImpresos3 = 0;
                        importeCheques3 = 0;
                        numCheques4 = 0;
                        semestreImpresos4 = 0;
                        importeCheques4 = 0;
                        
                        IItem expSolicitud = (IItem) expSolicitudesIt.next();
                        trimestre = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.TRIMESTRE);
                        
                        //Recuperamos el municipio si es distinto
                        if(!ciudad.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD))){
                            ciudad = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);                            
                            
                            tipoAyuda = "";
                            trabajadorSocial = "";

                            descripcionCiudad = SubvencionesUtils.getMunicipioByValor(cct, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD));
                            if(StringUtils.isNotEmpty(descripcionCiudad)){
                                countCiudad++;
                            }
                            
                            listado.append("\n");
                            listado.append(countCiudad+". " +descripcionCiudad+"\n");
                        }
                        if(!tipoAyuda.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA))){
                            trabajadorSocial = "";
                            tipoAyuda = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                        }
                        
                        if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                            if(!trabajadorSocial.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE))){
                                trabajadorSocial = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE);
                                nifTrabajadorSocial = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DOCUMENTOIDENTIDAD);
                                listado.append("\n");
                                listado.append("  Trabajador/a Social: " +trabajadorSocial+"\n");
                            }
                            listado.append("\n");
                            
                            beneficiario = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);
                            nifBeneficiario = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                            listado.append("\t" + contadorBeneficiario + " - Beneficiario/a: " +beneficiario+"\t\tNIF: " +nifBeneficiario+"\n");                    
                            contadorBeneficiario++;
                            IItemCollection numChequesCol = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP));
                            Iterator<?> numChequesIt = numChequesCol.iterator();
                            
                            if(numChequesIt.hasNext()){   
                                IItem numCheq = (IItem)numChequesIt.next(); 
                                
                                numCheques1 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1);
                                semestreImpresos1 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS);
                                importeCheques1 = (numCheques1 - semestreImpresos1) * 30;
                                
                                numCheques2 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2);
                                semestreImpresos2 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS);
                                importeCheques2 = (numCheques2 - semestreImpresos2) * 30;
                                
                                numCheques3 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3);
                                semestreImpresos3 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);
                                importeCheques3 = (numCheques3 - semestreImpresos3) * 30;
                                
                                numCheques4 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4);
                                semestreImpresos4 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);
                                importeCheques4 = (numCheques4 - semestreImpresos4) * 30;
                            }
                            
                            if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                                importeCheques = importeCheques1;
                            } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                                importeCheques = importeCheques2;
                            } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                                importeCheques = importeCheques3;     
                            } else{
                                importeCheques = importeCheques4;
                            }
                            
                            numMiembrosFamilia = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR);
                            listado.append("\tN�mero de miembros de la unidad familiar: " +numMiembrosFamilia+"\n");
                            if("SI".equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS))){
                                listado.append("\tMenor: S�\n");
                            }

                            listado.append("\tN� cheques: " +(importeCheques/30)+"\n");
                            listado.append("\tImporte Cheques: " + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeCheques)+" �\n");
                            total += importeCheques;                            
                        }
                        
                        SubvencionesUtils.insertaTabajadorSocialComoParticipante(cct, numexp, nifTrabajadorSocial, nifTrabajadorSocial);
                    }
                }            
            }
            listado.append("\n\tEl importe total de la presente propuesta es: " + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, total)+" �\n");
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return listado.toString();
            
        } catch(Exception e) {
            LOGGER.error("No se han podido recuperar los datos de las solicitudes. " + e.getMessage(),e);
            throw new ISPACRuleException("No se han podido recuperar los datos de las solicitudes. " + e.getMessage(),e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

}