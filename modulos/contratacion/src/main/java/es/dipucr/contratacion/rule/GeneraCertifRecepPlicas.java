package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.awt.FontWeight;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.table.XCell;
import com.sun.star.text.ParagraphVertAlign;
import com.sun.star.text.TableColumnSeparator;
import com.sun.star.text.VertOrientation;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GeneraCertifRecepPlicas extends DipucrAutoGeneraDocIniTramiteRule {

	private static final Logger logger = Logger.getLogger(GeneraCertifRecepPlicas.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - " + GeneraCertifRecepPlicas.class);

		IClientContext cct = rulectx.getClientContext();
		
		plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
		
		if(StringUtils.isNotEmpty(plantilla)){
			tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
		}
		refTablas = "%TABLA1%";

		logger.warn("FIN - " + GeneraCertifRecepPlicas.class);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void insertaTabla(IRuleContext rulectx, XComponent component,
			String refTabla, IEntitiesAPI entitiesAPI, String numexp)
	{
        String licitador = "";
//        String representante = "";
        Date fechaPresentacion = new Date();
        
        ArrayList<String> expedientesResolucion = new ArrayList<String>();
        
        try{
        	if (refTabla.equals("%TABLA1%")){
				 //Obtenemos los expedientes relacionados y aprobados, ordenados por ayuntamiento
		        IItemCollection exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"+numexp+"' AND RELACION='Plica' ORDER BY ID ASC");
		        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
		        String query = "";
		        while (exp_relacionadosIterator.hasNext()){
		        	String numexpHijo = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_HIJO");
		        	expedientesResolucion.add(numexpHijo);
		        	query += "'"+numexpHijo+"',";	        	
		        }
		        		
				if(query.length()>0){
					query = query.substring(0,query.length()-1);
			    }
				String sQuery = "WHERE NUMEXP IN ("+query+") ORDER BY NREG ASC";
				logger.warn("sQuery "+sQuery);
				IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, sQuery);
			   	Iterator<IItem> expedientesIterator = expedientesCollection.iterator();
			 
			   	int numFilas = expedientesCollection.toList().size();

			    //Busca la posici�n de la tabla y coloca el cursor ah�
				//Usaremos el localizador %TABLA1%
				XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, component);
			    XText xText = xTextDocument.getText();
		        XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, component);
		        XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
		        xSearchDescriptor.setSearchString(refTabla);
		        XInterface xSearchInterface = null;
		        XTextRange xSearchTextRange = null;
		        xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
		        if (xSearchInterface != null) 
		        {
		        	//Cadena encontrada, la borro antes de insertar la tabla
			        xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
			        xSearchTextRange.setString("");
			        
					//Inserta una tabla de 4 columnas y tantas filas
				    //como nuevas liquidaciones haya mas una de cabecera
					XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
					Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
					XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
					
					//A�adimos 3 filas m�s para las dos de la cabecera de la tabla y uno para la celda final
					xTable.initialize(numFilas + 1, 2);
					XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
					xText.insertTextContent(xSearchTextRange, xTextContent, false);
	
					colocaColumnas1(xTable);

					//Rellena la cabecera de la tabla				
					setHeaderCellText(xTable, "A1", "LICITADORES");	
					//setHeaderCellText(xTable, "B1", "REPRESENTANTE");				
					setHeaderCellText(xTable, "B1", "FECHA DE PRESENTACI�N");							
					
				   	int i = 0;
				   	while (expedientesIterator.hasNext()){
				   		i++;
				    	IItem expediente = (IItem) expedientesIterator.next();
				    	licitador = expediente.getString("IDENTIDADTITULAR");
				    	fechaPresentacion = expediente.getDate("FREG");
				    	
				    	logger.warn(licitador+ " -> "+fechaPresentacion);
				    	
//				    	IItemCollection plicaCol = entitiesAPI.getEntities("CONTRATACION_PLICA", expediente.getString("NUMEXP"));
//				    	Iterator<IItem> itPlica = plicaCol.iterator();
				    	
//				    	representante = "";
//				    	
//				    	if(itPlica.hasNext()){
//				    		IItem plica = (IItem)itPlica.next();
//				    		representante = plica.getString("REPRESENTANTE");
//				    	}
				    				    	
						setCellText(xTable, "A"+String.valueOf(i+1), licitador);
				    	//setCellText(xTable, "B"+String.valueOf(i+1), representante);
				    	String patron = "dd/MM/yyyy";
				        SimpleDateFormat formato = new SimpleDateFormat(patron);
				    	setCellText(xTable, "B"+String.valueOf(i+1), formato.format(fechaPresentacion));			
				 	}
		        }
			}
		}
        catch (ISPACException e) {
        	logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private void setHeaderCellText(XTextTable xTextTable, String CellName, String strText) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(CellName));

		//Propiedades		
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharHeight", new Float(8.0));	
		xTPS.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
		xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
		xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
		xTPS.setPropertyValue("ParaTopMargin", new Short((short)60));
		xTPS.setPropertyValue("ParaBottomMargin", new Short((short)60));
		XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
		xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));
		xCPS.setPropertyValue("BackColor", new Integer(0xC0C0C0));
		
		//Texto de la celda
		xCellText.setString(strText);
	}	

    private void setCellText(XTextTable xTextTable, String CellName, String strText) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);    	
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

		//Propiedades
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharHeight", new Float(8.0));	
		xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
		xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
		xTPS.setPropertyValue("ParaTopMargin", new Short((short)0));
		xTPS.setPropertyValue("ParaBottomMargin", new Short((short)0));
		XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
		xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

		//Texto de la celda
		xCellText.setString(strText);
	}
  
    private void colocaColumnas1(XTextTable xTextTable) throws ISPACRuleException{
    	
		XPropertySet xPS = ( XPropertySet ) UnoRuntime.queryInterface(XPropertySet.class, xTextTable);
		 
		// Get table Width and TableColumnRelativeSum properties values
		int iWidth;
		try {
			iWidth = ( Integer ) xPS.getPropertyValue( "Width" );
			
    		short sTableColumnRelativeSum = ( Short ) xPS.getPropertyValue( "TableColumnRelativeSum" );
    		 
    		// Get table column separators
    		Object xObj = xPS.getPropertyValue( "TableColumnSeparators" );
    		 
    		TableColumnSeparator[] xSeparators = ( TableColumnSeparator[] )UnoRuntime.queryInterface(
    		    TableColumnSeparator[].class, xObj );

    		
    		//Calculamos el tama�o que le queremos dar a la celda
    		//Se empieza colocando de la �ltima a la primera
    		double dRatio = ( double ) sTableColumnRelativeSum / ( double ) iWidth;
    		double dRelativeWidth = ( double ) 20000 * dRatio;
    		
    		// Last table column separator position
    		double dPosition = sTableColumnRelativeSum - dRelativeWidth;
    		 
    		// Set set new position for all column separators        
    		//N�mero de separadores
    		int i = xSeparators.length - 1;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		


    		// Do not forget to set TableColumnSeparators back! Otherwise, it doesn't work.
    		xPS.setPropertyValue( "TableColumnSeparators", xSeparators );	
		} catch (UnknownPropertyException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (WrappedTargetException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (PropertyVetoException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
	}
}

