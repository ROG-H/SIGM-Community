package ieci.tdw.ispac.ispaclib.tageval;

import ieci.tdw.ispac.api.ISPACEntities;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACNullObject;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContextParams;
import ieci.tdw.ispac.api.rule.RuleProperties;
import ieci.tdw.ispac.ispaclib.XMLUtil.XMLDocUtil;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.cat.CTEntityDAO;
import ieci.tdw.ispac.ispaclib.dao.cat.CTRuleDAO;
import ieci.tdw.ispac.ispaclib.dao.entity.EntityFactoryDAO;
import ieci.tdw.ispac.ispaclib.dao.entity.MultivalueTable;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.db.DbResultSet;
import ieci.tdw.ispac.ispaclib.db.InternalDataType;
import ieci.tdw.ispac.ispaclib.entity.def.EntityDef;
import ieci.tdw.ispac.ispaclib.entity.def.EntityField;
import ieci.tdw.ispac.ispaclib.templates.TemplateDocumentInfo;
import ieci.tdw.ispac.ispaclib.templates.TemplateGraphicInfo;
import ieci.tdw.ispac.ispaclib.util.FileTemplateManager;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tdw.ispac.ispaclib.utils.XPathUtil;
import ieci.tdw.ispac.ispaclib.utils.XmlTag;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;



public class TagTranslator implements ITagTranslator
{
	private static final Logger logger = Logger.getLogger(TagTranslator.class);
	
	private final ClientContext mcontext;
	private final IRuleContextParams mrulectx;

	public TagTranslator(ClientContext context,IRuleContextParams rulectx)
	{
		mcontext=context;
		mrulectx=rulectx;
	}

	public List translateTags(List tags)
		throws ISPACException
	{
		List translatedtags = new ArrayList();
		String tagsXml="";

		Iterator it=tags.iterator();
		while (it.hasNext()){
			tagsXml =tagsXml + (String)it.next();
		}

		//String xml=XmlTag.getXmlInstruction("UTF-8")+ XmlTag.newTag("ispactags",tagsXml);
		String xml=XmlTag.getXmlInstruction("ISO-8859-15")+ XmlTag.newTag("ispactags",tagsXml);
		
		Document docxml= null;
		try{
			docxml=XMLDocUtil.newDocument(xml);
		}catch(ISPACException e){
			throw new ISPACException("exception.documents.templates.tags.malformed");
		}
		NodeList nodelist=XPathUtil.getNodeList(docxml,"/ispactags/ispactag");

		for (int i=0;i<nodelist.getLength();i++)
		{
			translatedtags.add(translateNode(nodelist.item(i)));
		}

		return translatedtags;
	}

	/**
	 * Traduce un ispactag a un texto
	 *
	 */
	public String translateStringTag(String tag) throws ISPACException
	{
		ArrayList toTranslate = new ArrayList();
		toTranslate.add(tag);
		List ret = translateTags(toTranslate);
		Object result = ret.get(0);
		if (result == null)
			return "";
		return (String)result;
	}

	/**
	 * Eval�a un ispactag a un boolean
	 */
	public boolean translateBooleanTag(String tag) throws ISPACException
	{
//	    ArrayList toTranslate = new ArrayList();
//		toTranslate.add(tag);
//		Object ret = translateTags(toTranslate).get(0);
//		if (!(ret instanceof Boolean))
//			throw new ISPACException("La expresi�n no eval�a a un valor l�gico.\n\tExpresi�n: "+tag);
//		return ((Boolean)ret).booleanValue();
		
		boolean ret = true;
        
        ArrayList toTranslate = new ArrayList();
        toTranslate.add(tag);
        List list = translateTags(toTranslate);
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            if (!(object instanceof Boolean)) {
                throw new ISPACException("La expresi�n no eval�a a un valor l�gico.\n\tExpresi�n: "+tag);
            }
            ret = ret & ((Boolean)object).booleanValue();
        }
        
        return ret;		
	}

	/**
	 * @param node
	 * @return
	 */
	private Object translateNode(Node nodetag)
		throws ISPACException
	{
		 String rulename=XPathUtil.get(nodetag,"@rule");
		 if (rulename!=null)
		 	return executeRuleName(rulename,nodetag);

		 String rule=XPathUtil.get(nodetag,"@ruleid");
		 if (rule!=null)
		 	return executeRuleId(rule,nodetag);

		 String sessionvar=XPathUtil.get(nodetag,"@sessionvar");
		 if (sessionvar!=null)
		 	return getSessionVar(sessionvar,nodetag);

		 String entity=XPathUtil.get(nodetag,"@entity");
		 if (entity!=null)
		 	return getEntity(entity.toUpperCase(),nodetag);

		 String entityid=XPathUtil.get(nodetag,"@entityid");
		 if (entityid!=null)
		 	return getEntityId(entityid,nodetag);

		 String template=XPathUtil.get(nodetag,"@template");
		 if (template!=null)
		 	return getTemplate(template,nodetag);

		 // [Dipucr-Manu Ticket #478] + ALSIGM3 Nueva opci�n Repositorio Com�n
		 String imagen=XPathUtil.get(nodetag,"@imagen");
		 if (imagen!=null)
		 	return getImagen(imagen,nodetag);

//TODO [recursos]	
//		 String resource=XPathUtil.get(nodetag,"@resource");
//		 if (resource!=null)
//		 	return getResource(resource,nodetag);

		 return "";
	}
	
	// [Dipucr-Manu Ticket #478] - INICIO - ALSIGM3 Nueva opci�n Repositorio Com�n
	private TemplateGraphicInfo getImagen(String imagen,Node nodetag)throws ISPACException{
		String sHeight = XPathUtil.get(nodetag,"@alto");
		String centrado = XPathUtil.get(nodetag,"@centrado");
		boolean bCentrado = (!StringUtils.isEmpty(centrado) && "true".equals(centrado));
		
		int iWidth = 0;
		int iHeight = 0;
		int imageHeight = 0;
		int imageWidth = 0;
		
		if(StringUtils.isNotEmpty(sHeight) && StringUtils.isNumeric(sHeight)){
			iHeight = Integer.parseInt(sHeight);
		}
		else{
			// Altura por defecto - 2'9 cms
			iHeight = 2900;
		}
		
		String ruta = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + MultiEntityContextHolder.getEntity(), "/SIGEM_TramitacionWeb") + imagen;
	
		// Cargamos la imagen para ver su altura y anchura
		try {
			File file = new File(ruta);
			BufferedImage bi = ImageIO.read(file);			
			imageHeight = bi.getHeight();
			imageWidth = bi.getWidth();
		} catch (IOException e) {
			logger.error("Error al cargar la imagen " + ruta , e);
		}
		
		// Calculamos la relaci�n de la altura
		double relacionAltura = ((double) imageHeight)/ ((double) iHeight);
		
		// Aplicamos esta misma relaci�n al ancho de la imagen
		iWidth = (int) ((double) imageWidth / relacionAltura);
		
		// Si el ancho de imagen resultante es mayor a 5cm lo dejamos a 5cms y recalculamos la altura
		if (iWidth > 5000) {
			double relacionAnchura = (double) iWidth / 5000;
			iHeight =  (int) (iHeight/ relacionAnchura);
			iWidth = 5000;
		}
		
		TemplateGraphicInfo graphic = new TemplateGraphicInfo(ruta, false, iWidth, iHeight);		
		graphic.setCentered(bCentrado);
	
		return graphic;
	}
	// [Dipucr-Manu Ticket #478] - FIN - ALSIGM3 Nueva opci�n Repositorio Com�n

	private Object executeRuleName(String rulename,Node nodetag)throws ISPACException{
		DbCnt cnt = null;
		CTRuleDAO ruledao=null;
		try{
			cnt=mcontext.getConnection();
			ruledao = (CTRuleDAO) ObjectDAO.getByName(cnt, CTRuleDAO.class, rulename);
		}catch (ISPACException e){
			throw new ISPACException("exception.documents.templates.tags", new Object[]{getTagAsString(nodetag), e.getMessage()}, true);
		}finally{
			mcontext.releaseConnection(cnt);
		}
		if (ruledao==null){
			throw new ISPACException("exception.documents.templates.tags.ruleNotFound", new Object[]{getTagAsString(nodetag), rulename},true);
		}
		
		try{
			return executeRule(ruledao.getKeyInt(),nodetag);
		}catch(ISPACRuleException e){
			logger.error("Error al ejecutar la regla", e);
			throw new ISPACException("exception.documents.templates.tags", new Object[]{getTagAsString(nodetag), e.getMessage()}, true);
		}
	}

	private Object executeRuleId(String rule,Node nodetag)
	throws ISPACException
	{
		int ruleid=0;
		try
		{
			ruleid=Integer.parseInt(rule);
		} catch (NumberFormatException e)
		{
			throw new ISPACException("exception.documents.templates.tags.malformedRuleId", new Object[]{getTagAsString(nodetag), rule}, true);
		}
		try{
			return executeRule( ruleid,nodetag);
		}catch(ISPACNullObject e){
			logger.error("Error al ejecutar la regla", e);
			throw new ISPACException("exception.documents.templates.tags.ruleIdNotFound", new Object[]{getTagAsString(nodetag), rule}, true);
		}
	}

	private Object executeRule(int ruleid,Node nodetag)
	throws ISPACException
	{
		ITXTransaction tx=mcontext.getAPI().getTransactionAPI();
		Map params=getRuleParams(nodetag);

		return tx.executeContextRule(ruleid,mrulectx.getRuleProcId(),mrulectx.getRuleStageId(),mrulectx.getRuleTaskId(),params);
	}

	private Map getRuleParams(Node nodetag)
	throws ISPACException
	{
	    HashMap parameters=new HashMap(mrulectx.getRuleParameters());
	    NodeList nodelist=XPathUtil.getNodeList(nodetag,"@*");

	    for (int i=0;i<nodelist.getLength();i++)
	    {
	        Node param=nodelist.item(i);
	        parameters.put(param.getNodeName(),param.getNodeValue());
	    }

		return parameters;
	}

	private String getSessionVar(String sessionvar,Node nodetag)throws ISPACException{
		try{
			return mcontext.getSsVariable(sessionvar);
		}catch (ISPACException e){
			logger.error("Error al obtener la variable de sesi�n", e);
			throw new ISPACException("exception.documents.templates.tags", new Object[]{getTagAsString(nodetag), e.getMessage()}, true);
		}
	}

	private String getEntity(String entity,Node nodetag)throws ISPACException{
		String property=XPathUtil.get(nodetag,"@property");
		String dateformat=XPathUtil.get(nodetag,"@dateformat");
		String multivalueFieldSeparator=XPathUtil.get(nodetag,"@multivaluefieldseparator");
		String sqlquery=XPathUtil.get(nodetag,"@sqlquery");
		String order = XPathUtil.get(nodetag,"@order");
		String sort = XPathUtil.get(nodetag,"@sort");
		String documentScope = XPathUtil.get(nodetag, "@documentscope");

		DbCnt cnt = null;
		try{
			cnt=mcontext.getConnection();
			CTEntityDAO ctentity=EntityFactoryDAO.getInstance().getCatalogEntityDAO(cnt,entity);
			if (ctentity == null){
				throw new ISPACException("exception.documents.templates.tags.entityNotFound", new Object[]{getTagAsString(nodetag), entity},true);
			}
			return processEntityTag(cnt,ctentity,property.toUpperCase(),sqlquery,order,sort,dateformat,multivalueFieldSeparator, documentScope);
		}catch (Exception e){
			logger.error("Error al obtener la entidad - Entidad. "+entity+" - "+e.getMessage(), e);
			if (e instanceof ISPACException &&  e.getMessage().equals("exception.documents.templates.tags.entityNotFound")){
				throw (ISPACException)e;
			}
			throw new ISPACException("exception.documents.templates.tags", new Object[]{getTagAsString(nodetag), e.getMessage()}, true);
		}finally{
			mcontext.releaseConnection(cnt);
		}
	}

	private String getEntityId(String entityid,Node nodetag)
		throws ISPACException
	{
		String property=XPathUtil.get(nodetag,"@property");
		String dateformat=XPathUtil.get(nodetag,"@dateformat");
		String multivalueFieldSeparator=XPathUtil.get(nodetag,"@multivaluefieldseparator");
		String sqlquery=XPathUtil.get(nodetag,"@sqlquery");
		String order = XPathUtil.get(nodetag,"@order");
		String sort = XPathUtil.get(nodetag,"@sort");
		String documentScope = XPathUtil.get(nodetag, "@documentscope");

		int entid=0;
		try{
		    entid=Integer.parseInt(entityid);
		} catch (NumberFormatException e){
			throw new ISPACException("exception.documents.templates.tags.malformedEntityId", new Object[]{getTagAsString(nodetag), entityid}, true);
		}


		DbCnt cnt = null;
		try{
			cnt=mcontext.getConnection();
			CTEntityDAO ctentity=EntityFactoryDAO.getInstance().getCatalogEntityDAO(cnt,entid);
			return processEntityTag(cnt,ctentity,property.toUpperCase(),sqlquery,order,sort,dateformat, multivalueFieldSeparator,documentScope);
		}catch (ISPACNullObject e){
			logger.error("Error al obtener la entidad", e);
			throw new ISPACException("exception.documents.templates.tags.entityIdNotFound", new Object[]{getTagAsString(nodetag), entityid}, true);
		}catch (Exception e){
			logger.error("Error al obtener la entidad", e);
			throw new ISPACException("exception.documents.templates.tags", new Object[]{getTagAsString(nodetag), e.getMessage()}, true);
		}finally{
			mcontext.releaseConnection(cnt);
		}
	}

	private Object getTemplate(String templateCode, Node nodetag) throws ISPACException {
		
		TemplateDocumentInfo documentInfo = null;
		DbCnt cnt = null;
		
		try{
			cnt = mcontext.getConnection();

			// Indica si se quiere retornar el contenido del documento como texto plano
			String propertyAsTest = XPathUtil.get(nodetag, "@asText");
			
			// Indica si se quiere retonar solo el contenido de un marcador
			String propertyMark = XPathUtil.get(nodetag, "@bookmark");
			
			// Obtener la informaci�n de la plantilla
			IItem template = mcontext.getAPI().getTemplateAPI().getTemplateByCode(templateCode);
			if (template != null) {
				
				// Cachear la plantilla
				FileTemplateManager templateManager = (FileTemplateManager) FileTemplateManager.getInstance();
				String templateURL = "file:///" + templateManager.getTemplatePath(cnt, template.getKeyInt());
				
				documentInfo = new TemplateDocumentInfo(templateURL);
				
				if (StringUtils.isNotEmpty(propertyMark)){
					documentInfo.setBookmark(propertyMark);
				}
				if (StringUtils.equalsIgnoreCase(propertyAsTest, "true")){
					documentInfo.setAsText(true);
				}
			}

		} catch (Exception e) {
			logger.error("Error al obtener la plantilla", e);
			throw new ISPACException("exception.documents.templates.tags", 
					new Object[]{ getTagAsString(nodetag), e.getMessage() }, true);
		} finally {
			mcontext.releaseConnection(cnt);
		}

		return documentInfo;
	}

	private String getTagAsString(Node nodetag) throws ISPACException{
		String tag = XMLDocUtil.toString(nodetag);
		tag = StringUtils.substring(tag, tag.indexOf("<ispactag"));
		tag = tag.replaceAll("[\\\n|\\\r]", "");
		return StringUtils.escapeXml(tag);
	}

	private String processEntityTag(DbCnt cnt,CTEntityDAO ctentity, String property,String sqlquery, String order, String sort, String dateformat, String multivalueFieldSeparator, String documentScope)
	throws ISPACException
	{
    	Map ruleparams = mrulectx.getRuleParameters();

    	String sqlkey = null;

    	if ( ctentity.getKeyInt() == ISPACEntities.DT_ID_DOCUMENTOS && !StringUtils.equalsIgnoreCase(documentScope, "all"))
	    {
    		sqlkey = ctentity.getString("CAMPO_PK")
			+ " = "
			+ (String) ruleparams.get(RuleProperties.RCTX_DOCUMENTID);
	    }
	    else
	    {
	    	String entity =(String) ruleparams.get(RuleProperties.RCTX_ENTITYID);

	    	// El tag est� asociado a la entidad relacionada
		    if (ctentity.getKeyInt() == Integer.parseInt(entity))
		    {
		    	sqlkey = ctentity.getString("CAMPO_PK")
				+ " = "
				+ (String) ruleparams.get(RuleProperties.RCTX_REGENTITYID);
		    }
	    }

    	if (sqlkey != null)
    	{
    		if (sqlquery == null)
    			sqlquery = sqlkey;
    		else
    			sqlquery += " AND (" + sqlkey + ")";
    	}

		CollectionDAO entities = EntityFactoryDAO.getInstance().getEntities(cnt,ctentity,mrulectx.getRuleNumexp(),sqlquery, order,sort);
		if (!entities.next())
			return "";
		ObjectDAO objdao=entities.value();
		
		EntityDef entityDef = EntityDef.parseEntityDef(ctentity.getDefinition());
		EntityField entityField = entityDef.getField(property);
		if ((entityField!= null) && entityField.isMultivalue()) {
			DbResultSet dbrs = null;
			String stmt = null; 
			StringBuffer buffer = new StringBuffer();
			try
			{
					stmt = "SELECT " + MultivalueTable.FIELD_VALUE + 
					      " FROM "   + MultivalueTable.getInstance().composeMultivalueTableName(ctentity.getName(), entityField.getType().getJdbcType()) + 
					      " WHERE "  + MultivalueTable.FIELD_FIELD + " = '" + DBUtil.replaceQuotes(entityField.getPhysicalName()) + "' " +
			      		  " AND "    + MultivalueTable.FIELD_REG + " = " + objdao.getKeyInt() ;
					dbrs = cnt.executeQuery(stmt);
					while(dbrs.getResultSet().next()){
						
						if (StringUtils.isBlank(dateformat)){
							
							InternalDataType dataType = entityField.getType();
							if (InternalDataType.SHORT_INTEGER.equals(dataType) ||
									InternalDataType.LONG_INTEGER.equals(dataType)) {
				    			
				    			// Entero con separador de miles
				    			BigInteger numeric = new BigInteger(dbrs.getResultSet().getString(MultivalueTable.FIELD_VALUE));
				    			buffer.append(getNumberFormat(entityField).format(numeric.longValue()));
								
				    		} else if (InternalDataType.SHORT_DECIMAL.equals(dataType) ||
				    				 InternalDataType.LONG_DECIMAL.equals(dataType)) {
				    			
				    			// Decimal con separador de miles y decimales
				    			BigDecimal numeric = new BigDecimal(dbrs.getResultSet().getDouble(MultivalueTable.FIELD_VALUE));
				    			buffer.append(getNumberFormat(entityField).format(numeric.doubleValue()));
								
				    		} else {
				    			buffer.append(dbrs.getResultSet().getString(MultivalueTable.FIELD_VALUE));
				    		}
							
						}else{
							buffer.append(TypeConverter.toString(dbrs.getResultSet().getDate(MultivalueTable.FIELD_VALUE),dateformat));
						}
						
						if (StringUtils.isEmpty(multivalueFieldSeparator)){
							buffer.append("\n");
						}else{
							buffer.append(StringUtils.unescapeJava(multivalueFieldSeparator));
						}
					}
			} catch (SQLException e) {
				logger.error("Error al obtener los valores del campo [" + property + "] - "+e.getMessage(), e);
				throw new ISPACException("Error al obtener los valores del campo [" + property + "] - "+e.getMessage(), e);
			}
	        finally
	        {
	            if (dbrs != null)
	            	dbrs.close();
	        }		
			return buffer.toString();
		}
		
		return getStringValue(objdao, entityField, property, dateformat);		
	}
	
	protected String getStringValue(ObjectDAO objdao, EntityField entityField, String property, String dateformat) throws ISPACException {
		
		
		if(!StringUtils.isEmpty(objdao.getString(property))){ //[dipucr-Manu #143]
			if (StringUtils.isNotBlank(dateformat)) {
				return TypeConverter.toString(objdao.getDate(property),dateformat);
			} 
	
			InternalDataType dataType = entityField.getType();
			if (dataType != null) {
				
				if (InternalDataType.SHORT_INTEGER.equals(dataType) ||
						InternalDataType.LONG_INTEGER.equals(dataType)) {
	    			
	    			// Entero con separador de miles
	    			BigInteger numeric = new BigInteger(objdao.getString(property));
					return getNumberFormat(entityField).format(numeric.longValue());
					
	    		} else if (InternalDataType.SHORT_DECIMAL.equals(dataType) ||
	    				 InternalDataType.LONG_DECIMAL.equals(dataType)) {
	    			
	    			// Decimal con separador de miles y decimales
	    			BigDecimal numeric = new BigDecimal(objdao.getDouble(property));
					return getNumberFormat(entityField).format(numeric.doubleValue());
	    		}
			}
		
			return objdao.getString(property);
		}
		return "";
	}
	
	private NumberFormat getNumberFormat(EntityField entityField) {
		
		String pattern = "#,###.############"; 
			
		if ((entityField != null) && (entityField.getPrecision() > 0)) {
			pattern = "#,###." + StringUtils.leftPad("", entityField.getPrecision(), "0");
		}
		
		//INICIO [dipucr-Felipe #106]
		Locale locale = mcontext.getLocale();
		if (null == locale){
			locale = Locale.getDefault();
		}
		//FIN [dipucr-Felipe #106]
		
		return new DecimalFormat(pattern, new DecimalFormatSymbols(locale));
		
	}
}
