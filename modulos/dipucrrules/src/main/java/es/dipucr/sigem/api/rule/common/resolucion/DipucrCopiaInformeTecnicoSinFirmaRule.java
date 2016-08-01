package es.dipucr.sigem.api.rule.common.resolucion;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.File;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

/**
 * [eCenpri-Felipe #978]
 * @author Felipe
 * @since 28.10.2013
 * Para los procedimientos integrales.
 * Recuperar el documento de Informe T�cnico sin firmar, para copiar su contenido en la propuesta
 * 
 */
public class DipucrCopiaInformeTecnicoSinFirmaRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrCopiaInformeTecnicoSinFirmaRule.class);
	
	private static final String NOMBREDOC_INFORME_TECNICO = "Informe T�cnico";
	private static final String DESC_INFORME_TECNICO = "Informe T�cnico";
	

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			logger.info("INICIO - " + this.getClass().getName());
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        //----------------------------------------------------------------------------------------------
			
			String numexp = rulectx.getNumExp();
			
			IItemCollection collection = DocumentosUtil.
					getDocumentsByDescripcion(numexp, rulectx, DESC_INFORME_TECNICO);

			//Si hay alg�n informe t�cnico, lo copiamos al tr�mite actual
			if (collection.next()){
				
				//Recuperamos el fichero del primer tr�mite
				IItem itemDocInformeTecnico1 = (IItem) collection.iterator().next();
				String strInfopag = itemDocInformeTecnico1.getString("INFOPAG");
				File fileInfTecnico = DocumentosUtil.getFile(cct, strInfopag, null, null);
				
				//Copiamos el documento de informe t�cnico sin firma al tr�mite actual
				int tpdoc = DocumentosUtil.getTipoDoc(cct, NOMBREDOC_INFORME_TECNICO, DocumentosUtil.BUSQUEDA_EXACTA, false);
				DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, itemDocInformeTecnico1.getString("DESCRIPCION"), 
						fileInfTecnico, itemDocInformeTecnico1.getString("EXTENSION"));
				
				//Borramos el fichero
    			if (null != fileInfTecnico && fileInfTecnico.exists()) fileInfTecnico.delete();
			}
			
		}
		catch(Exception e){
			String descError = "Error al copiar el documento de informe t�cnico sin firma, " + e.getMessage();
			logger.error(descError, e);
			throw new ISPACRuleException(descError, e);
		}
		logger.info("FIN - " + this.getClass().getName());
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}
