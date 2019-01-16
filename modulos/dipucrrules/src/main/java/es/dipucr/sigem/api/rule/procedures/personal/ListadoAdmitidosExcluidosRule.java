package es.dipucr.sigem.api.rule.procedures.personal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.Exception;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class ListadoAdmitidosExcluidosRule extends DipucrAutoGeneraDocIniTramiteRule {

	private static final Logger LOGGER = Logger.getLogger(ListadoAdmitidosExcluidosRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		LOGGER.info("INICIO - " + this.getClass().getName());
		try {

			IClientContext cct = rulectx.getClientContext();

			plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

			if (StringUtils.isNotEmpty(plantilla)) {
				tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
			}
			refTablas = "%TABLA1%";
			refTablas = "%TABLA2%";

		} catch (ISPACException e) {
			LOGGER.error("Error al generar el documento. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar el documento. " + e.getMessage(), e);
		}
		LOGGER.info("FIN - " + this.getClass().getName());
		return true;
	}


	public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI,
			String numexp) {
		try {
			if (refTabla.equals("%TABLA1%")) {
				String sQuery = "select exped.NUMEXP, NIFCIFTITULAR, IDENTIDADTITULAR, PUESTOTRABAJO from spac_expedientes as exped, PERSONAL_PUESTO_TRABAJO as puesto WHERE exped.NUMEXP IN "
						+ "(SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE='"+rulectx.getNumExp()+"' AND RELACION='Solicitud Convocatoria') and puesto.numexp = exped.numexp and puesto.admitido = 'SI' "
						+ "ORDER BY puestotrabajo, exped.NIFCIFTITULAR  ASC";
				
				ResultSet expRel = rulectx.getClientContext().getConnection().executeQuery(sQuery).getResultSet();
				
				if(expRel==null)
	          	{
	          		throw new ISPACInfo("No existe ninguna dato a mostrar");
	          	}

		    	
				int numFilas =  numFilas(expRel);
				
				expRel = rulectx.getClientContext().getConnection().executeQuery(sQuery).getResultSet();
				
				XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, 4);
				double[] distribucionColumnas = {10,10,40, 40};
				LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);	
				LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, "Num. Exp.");
				LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, "CIF");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, "Titular");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, "Puesto");

				
            	int i = 1;
            	while (expRel.next()){
					String numexpSolicitud = "";
					if(expRel.getString(ExpedientesUtil.NUMEXP)!=null){
						numexpSolicitud = expRel.getString(ExpedientesUtil.NUMEXP);
					}
					String cif = "";
					if(expRel.getString(ExpedientesUtil.NIFCIFTITULAR)!=null){
						cif = expRel.getString(ExpedientesUtil.NIFCIFTITULAR);
					}
					String titular = "";
					if(expRel.getString(ExpedientesUtil.IDENTIDADTITULAR)!=null){
						titular = expRel.getString(ExpedientesUtil.IDENTIDADTITULAR);
					}
					String puestotrabajo = "";
					if(expRel.getString("PUESTOTRABAJO")!=null){
						puestotrabajo = expRel.getString("PUESTOTRABAJO");
					}
					
					 i++;
                     LibreOfficeUtil.setTextoCelda(tabla, 1, i, numexpSolicitud);
                     LibreOfficeUtil.setTextoCelda(tabla, 2, i, cif);
                     LibreOfficeUtil.setTextoCelda(tabla, 3, i, titular);
                     LibreOfficeUtil.setTextoCelda(tabla, 4, i, puestotrabajo);
				}
			}
			if (refTabla.equals("%TABLA2%")) {
				String sQuery = "select exped.NUMEXP, NIFCIFTITULAR, IDENTIDADTITULAR, PUESTOTRABAJO, MOTIVO_RECHAZO from spac_expedientes as exped, PERSONAL_PUESTO_TRABAJO as puesto WHERE exped.NUMEXP IN "
						+ "(SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE='"+rulectx.getNumExp()+"' AND RELACION='Solicitud Convocatoria') and puesto.numexp = exped.numexp and puesto.admitido = 'NO' "
						+ "ORDER BY puestotrabajo, exped.NIFCIFTITULAR  ASC";
				
				ResultSet expRel = rulectx.getClientContext().getConnection().executeQuery(sQuery).getResultSet();
				
				if(expRel==null)
	          	{
	          		throw new ISPACInfo("No existe ninguna dato a mostrar");
	          	}

		    	
				int numFilas =  numFilas(expRel);
				
				expRel = rulectx.getClientContext().getConnection().executeQuery(sQuery).getResultSet();
				
				XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, 5);
				double[] distribucionColumnas = {10,10,40, 10, 30};
				LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);	
				LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, "Num. Exp.");
				LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, "CIF");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, "Titular");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, "Puesto");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, "Motivo del rechazo");

				
            	int i = 1;
            	while (expRel.next()){
					String numexpSolicitud = "";
					if(expRel.getString(ExpedientesUtil.NUMEXP)!=null){
						numexpSolicitud = expRel.getString(ExpedientesUtil.NUMEXP);
					}
					String cif = "";
					if(expRel.getString(ExpedientesUtil.NIFCIFTITULAR)!=null){
						cif = expRel.getString(ExpedientesUtil.NIFCIFTITULAR);
					}
					String titular = "";
					if(expRel.getString(ExpedientesUtil.IDENTIDADTITULAR)!=null){
						titular = expRel.getString(ExpedientesUtil.IDENTIDADTITULAR);
					}
					String puestotrabajo = "";
					if(expRel.getString("PUESTOTRABAJO")!=null){
						puestotrabajo = expRel.getString("PUESTOTRABAJO");
					}
					String motivoRechazo = "";
					if(expRel.getString("MOTIVO_RECHAZO")!=null){
						motivoRechazo = expRel.getString("MOTIVO_RECHAZO");
					}
					
					 i++;
                     LibreOfficeUtil.setTextoCelda(tabla, 1, i, numexpSolicitud);
                     LibreOfficeUtil.setTextoCelda(tabla, 2, i, cif);
                     LibreOfficeUtil.setTextoCelda(tabla, 3, i, titular);
                     LibreOfficeUtil.setTextoCelda(tabla, 4, i, puestotrabajo);
                     LibreOfficeUtil.setTextoCelda(tabla, 5, i, motivoRechazo);
				}
			}
		} catch (ISPACRuleException e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		} catch (SQLException e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		}

	}
	
	private int numFilas(ResultSet personal) throws ISPACRuleException {
		int numFilas = 0;
		try {
			while (personal.next()){
				numFilas++;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException(e);
		}
		return numFilas;
	}
}