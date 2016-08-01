package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GenerateActaComiMiguelturraRule extends GenerateActaBasePropuestaRule {
	protected static final Logger logger = Logger.getLogger(GenerateActaComiMiguelturraRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		STR_prefijo                 = "Borrador de Acta de Comisi�n";
		STR_nombreTramite           = STR_prefijo + "";
		STR_nombrePie               = STR_prefijo + " - Pie";
		
		String organo = SecretariaUtil.getOrgano(rulectx);
		logger.warn("organo "+organo);
		
		if(organo.equals("MESA")){
			STR_prefijo = "Borrador de Acta de Mesa";
			STR_nombreCabecera 			= STR_prefijo + " - Cabecera";
			STR_propuestaBorrador       = STR_prefijo + " - Propuesta - Borrador";
			STR_urgencias				= "Borrador de Acta de Junta - Urgencias - Comi";
			STR_nombreRuegos            = "";
			STR_nombreTramite           = STR_prefijo + "";
			STR_nombrePie               = STR_prefijo + " - Pie";
			
		}
		else{
			try {
				String area = SecretariaUtil.getAreaSesion(rulectx,rulectx.getNumExp()); 
				String sesion = SecretariaUtil.getSesion(rulectx);
				logger.warn("area "+area);
				logger.warn("sesion "+sesion);
				////logger.warn("sesion "+sesion);
				////logger.warn("area "+area);
				//urbanismo
				if(area.equals("0030")){
					if(sesion.equals("ORD")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Urba - Ordinaria";
					}
					if(sesion.equals("EXTR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Urba - Extra";
					}
					if(sesion.equals("EXUR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Urba -  ExtraUrg";
					}
					if(sesion.equals("CONS")){
						
					}
				}
				/*if(area.equals("Personal")){
					if(sesion.equals("ORD")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Personal - Ordinaria";
					}
					if(sesion.equals("EXTR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Personal - Extra";
					}
					if(sesion.equals("EXUR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Personal -  ExtraUrg";
					}
					if(sesion.equals("CONS")){
						
					}
				}
				/**
				 * [Teresa Ticket#201# Cambiar nombre de la COMI de bienestar social ]
				 * **/
				//"Servicios a la Ciudadan�a"
				if(area.equals("0040")){
					if(sesion.equals("ORD")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - BSocial - Ordinaria";
					}
					if(sesion.equals("EXTR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - BSocial - Extra";
					}
					if(sesion.equals("EXUR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - BSocial -  ExtraUrg";
					}
					if(sesion.equals("CONS")){
						
					}
				}
				//"Acci�n Social"
				if(area.equals("0060")){
					if(sesion.equals("ORD")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Cultura - Ordinaria";
					}
					if(sesion.equals("EXTR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Cultura - Extra";
					}
					if(sesion.equals("EXUR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Cultura - ExtraUrg";
					}
					if(sesion.equals("CONS")){
						
					}
				}
				//"Hacienda"
				if(area.equals("0050")){
					if(sesion.equals("ORD")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Haci - Ordinaria";
					}
					if(sesion.equals("EXTR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Haci - Extra";
					}
					if(sesion.equals("EXUR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Haci - ExtraUrg";
					}
					if(sesion.equals("CONS")){
						
					}
				}
				//"Asuntos de Pleno"
				if(area.equals("0020")){
					if(sesion.equals("ORD")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Pleno - Ordinaria";
					}
					if(sesion.equals("EXTR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Pleno - Extra";
					}
					if(sesion.equals("EXUR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Pleno - ExtraUrg";
					}
					if(sesion.equals("CONS")){
						
					}
				}
				if(sesion.equals("ORD")){

					STR_propuestaBorrador       = STR_prefijo + " - Propuesta - Borrador";
					STR_urgencias				= "Borrador de Acta de Junta - Urgencias - Comi";
					STR_nombreRuegos            = "Borrador de Acta de Comisi�n - Ruegos y preguntas - Comi";
				}
				if(sesion.equals("EXTR")){
			
					STR_propuestaBorrador       = STR_prefijo + " - Propuesta - Borrador";
					STR_urgencias				= "";
					STR_nombreRuegos            = "";
				}
				if(sesion.equals("EXUR")){

					STR_propuestaBorrador       = STR_prefijo + " - Propuesta - RatificacionUrgencia";
					STR_urgencias				= "";
					STR_nombreRuegos            = "";
				}
				if(sesion.equals("CONS")){
					
				}
			} catch (ISPACException e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		
		return true;
	}
}