package ieci.tdw.ispac.ispaclib.app;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.Properties;
import ieci.tdw.ispac.api.item.Property;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.entity.EntityDAO;
import ieci.tdw.ispac.ispaclib.dao.entity.EntityFactoryDAO;
import ieci.tdw.ispac.ispaclib.dao.entity.IEntityDef;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.entity.def.EntityDef;
import ieci.tdw.ispac.ispaclib.entity.def.EntityField;
import ieci.tdw.ispac.ispaclib.item.CompositeItem;
import ieci.tdw.ispac.ispaclib.item.GenericItem;
import ieci.tdw.ispac.ispaclib.util.PrefixBuilder;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GenericSecondaryEntityApp extends SecondaryEntityApp {

	private static final long serialVersionUID = 8785811211339148577L;

	public GenericSecondaryEntityApp(ClientContext context) {
		super(context);
	}

	public void store() throws ISPACException {
		
		if (getParameters() != null) {
			
			// Entidades declaradas en los par�metros del formulario
			List entities = getParameters().getEntities();
			
			Iterator it = entities.iterator();
			while (it.hasNext()) {
				
				// Procesar s�lo las entidades agregadas (composici�n)
				EntityParameterDef entity = (EntityParameterDef) it.next();
				if (entity.getType().equals(EntityAppConstants.ENTITY_TYPE_COMPOSITE)) {
					
					EntityRelationDef relation = entity.getRelation();
					if (relation.getSecondaryField() != null) {
						
						String fldSecondary = relation.getSecondaryField();
						
						// Tabla secundaria para usarla como prefijo
						String secondaryTableName = entity.getTable();
						if (StringUtils.isNotBlank(secondaryTableName)) {

							// Establecer en la entidad agregada (en el campo secondary-field)
							// el valor que la relaciona con la entidad principal (la clave o el campo primary-field)
							if (StringUtils.isEmpty(mitem.getString(secondaryTableName + ":" + fldSecondary))) {
								
								Object value = null;
								if (StringUtils.equalsIgnoreCase(relation.getType(), EntityAppConstants.RELATION_TYPE_PRIMARY_KEY)) {
									value = mitem.get(mPrefixMainEntity + ":" + mKeyMainEntity);
								}
								else if (StringUtils.equalsIgnoreCase(relation.getType(), EntityAppConstants.RELATION_TYPE_FIELD)) {
									value = mitem.get(mPrefixMainEntity + ":" + relation.getPrimaryField());
								}
								
								mitem.set(secondaryTableName + ":" + fldSecondary, value);
							}
						}
					}
				}
			}
		}
		
		super.store();
	}

	/**
	 * Obtener registros y recursos de las entidades secundarias
	 * declaradas en los par�metros del formulario.
	 */
	protected void getSecondariesEntities() throws ISPACException {
		
		if (getParameters() != null) {
			
			String appLanguage = mContext.getAppLanguage();
			
			// Entidades declaradas en los par�metros del formulario
			List entities = getParameters().getEntities();
			
			Iterator it = entities.iterator();
			while (it.hasNext()) {
				
				EntityParameterDef entity = (EntityParameterDef) it.next();
				if (entity.getType().equals(EntityAppConstants.ENTITY_TYPE_COMPOSITE)) {
					
					boolean entityReadonly = entity.isReadonly();
					boolean entityNoDelete = entity.isNoDelete();
					
					// Las entidades de s�lo lectura no ser�n borradas
					// al borrar el registro de la entidad principal
					if (entityReadonly) {
						entityNoDelete = true;
					}
					
					// Definici�n de la entidad agregada (composici�n)
					IEntityDef compositeEntityDef = getEntity(entity, !entityReadonly, !entityNoDelete);
					if (compositeEntityDef != null) {
					
						// Obtener los recursos de la entidad agregada necesarios para la vista
						getEntityResources(compositeEntityDef.getId(), compositeEntityDef.getName(), appLanguage);
						
						// Relaci�n entre la entidad principal y la secundaria
						// necesaria para obtener el listado de entidades a mostrar en un formulario de lista
						joinListSecondaryEntities.put(compositeEntityDef.getName(), joinSecondaryEntity);
					}
					else {
						// No se ha indicado <table> en el par�metro
						throw new ISPACInfo("exception.entities.form.paramTable.composite", 
											new String[] {mAppName, 
														  getLabel(mainEntityName + ":" + mainEntityName), 
														  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
					}
				}
				// Si no se recibe el path es que la entidad no se va a presentar en el formulario
				// luego es necesario cargar la informaci�n para la vista
				else if ((entity.getType().equals(EntityAppConstants.ENTITY_TYPE_VALIDATION)) &&
						 (getPath() != null)) {
					
					// Entidad de validaci�n
					if (getEntity(entity, false, false) == null) {
						
						// No se ha indicado <table> en el par�metro
						throw new ISPACInfo("exception.entities.form.paramTable.validation", 
											new String[] {mAppName, 
														  getLabel(mainEntityName + ":" + mainEntityName), 
														  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
					}
					else {
						String key = mainEntityName;
						if (StringUtils.isNotEmpty(entity.getPrimaryTable())) {
							key = entity.getPrimaryTable();
						}
						key += PrefixBuilder.PREFIX_ISPAC + entity.getRelation().getPrimaryField();
						
						// Relaci�n entre la entidad y la tabla de validaci�n
						// necesaria para obtener el listado de entidades a mostrar en un formulario de lista
						joinListSecondaryEntities.put(key, joinSecondaryEntity);
					}
				}
				else if ((entity.getType().equals(EntityAppConstants.ENTITY_TYPE_MULTIPLE_RELATION)) &&
						 (getPath() != null)) {
					
					// Definici�n de la entidad de relaci�n m�ltiple
					IEntityDef relationEntityDef = getEntityMultipleRelation(entity, !entity.isReadonly());
					if (relationEntityDef != null) {
					
						// Obtener los recursos de la entidad de la relaci�n necesarios para la vista
						getEntityResources(relationEntityDef.getId(), relationEntityDef.getName(), appLanguage);
					}
					else {
						// No se ha indicado <table> en el par�metro
						throw new ISPACInfo("exception.entities.form.paramTable.multiple", 
											new String[] {mAppName, 
														  getLabel(mainEntityName + ":" + mainEntityName), 
														  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
					}
				}
				else if (getPath() != null) {
					
					// Tipo de entidad no v�lido
					throw new ISPACInfo("exception.entities.form.param.entityType", 
										new String[] {mAppName, 
													  getLabel(mainEntityName + ":" + mainEntityName), 
													  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
				}
			}
		}
	}
	
	/**
	 * Obtener los datos de la definici�n de par�metro de entidad de relaci�n m�ltiple.
	 * 
	 * @param entity Definici�n de par�metro de entidad.
	 * @param storeItem Indicador de si el item se guarda o no.
	 * @return definici�n de la entidad de relaci�n m�ltiple.
	 * 
	 * @throws ISPACException Si se produce alg�n error.
	 */
	protected IEntityDef getEntityMultipleRelation(EntityParameterDef entity, 
											   	   boolean storeItem) throws ISPACException {
		
		IEntitiesAPI entitiesAPI = mContext.getAPI().getEntitiesAPI();
		
		// Tabla de la relaci�n m�ltiple para usarla como prefijo
		String relationTableName = entity.getTable();
		if (StringUtils.isBlank(relationTableName)) {
			return null;
		}
				
		// Definici�n de la entidad de la relaci�n m�ltiple
		IEntityDef relationEntityDef = (IEntityDef) entitiesAPI.getCatalogEntity(relationTableName);
		if (relationEntityDef == null) {
		
			// No existe una entidad con la tabla BD declarada
			throw new ISPACInfo("exception.entities.form.paramTable.noExist", 
								new String[] {mAppName, 
											  getLabel(mainEntityName + ":" + mainEntityName), 
											  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
		}
		
		// Obtener el registro de la relaci�n
		IItemCollection itemCol = entitiesAPI.getEntities(SpacEntities.SPAC_ENTIDADES_RELACION_MULTIPLE, 
														  msExpedient, 
														  "ENTITY_ID = " + relationEntityDef.getId());
		
		if (itemCol.next()) {
			
			IItem multipleRelationEntity = itemCol.value();
			
			// Obtener el registro de la entidad relacionada
			IItem item = entitiesAPI.getEntity(multipleRelationEntity.getInt("ENTITY_ID"), 
											   multipleRelationEntity.getInt("KEY_ID"));
			
			// A�adir el IItem al Composite
			( (CompositeItem) mitem).addItem(item, relationTableName + ":", storeItem, false);	
		}
		
		return relationEntityDef;
	}

	/**
	 * Obtener los datos de la definici�n de par�metro de entidad.
	 * 
	 * @param entity Definici�n de par�metro de entidad.
	 * @param storeItem Indicador de si el item se guarda o no.
	 * @param deleteItem Indicador de si el item se borra o no.
	 * @return definici�n de la entidad secundaria.
	 * 
	 * @throws ISPACException Si se produce alg�n error.
	 */
	private IEntityDef getEntity(EntityParameterDef entity, 
								 boolean storeItem,
								 boolean deleteItem) throws ISPACException {
		
		IItem item = null;
		String fldPrimary = null;
		String fldSecondary = null;
		Object valuePrimary = null;
		String sqlQuery = null;
		String relationType = null;
		
		IEntitiesAPI entitiesAPI = mContext.getAPI().getEntitiesAPI();
		
		// Tabla secundaria para usarla como prefijo
		String secondaryTableName = entity.getTable();
		if (StringUtils.isBlank(secondaryTableName)) {
			return null;
		}
				
		// Definici�n de la entidad secundaria
		IEntityDef secondaryEntityDef = (IEntityDef) entitiesAPI.getCatalogEntity(secondaryTableName);
		if (secondaryEntityDef == null) {
			
			// No existe una entidad con la tabla BD declarada
			throw new ISPACInfo("exception.entities.form.paramTable.noExist", 
								new String[] {mAppName, 
											  getLabel(mainEntityName + ":" + mainEntityName), 
											  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
		}
		
		EntityDef entityDefinicion = null;
		
		// Si la entidad se va a guardar
		// obtener los campos obligatorios para la entidad secundaria
		if (storeItem) {
			entityDefinicion = getRequiredData(secondaryEntityDef);
		}
		else {
			// Si la entidad no se va a guardar
			// obtener la definici�n de la entidad secundaria
			String xmlDefinicion = secondaryEntityDef.getDefinition();
			if (!StringUtils.isEmpty(xmlDefinicion)) {
				
				// Parsear la definici�n de la entidad secundaria
				entityDefinicion = EntityDef.parseEntityDef(xmlDefinicion);
			}
		}
		
		// Obtener la definici�n de los campos para la entidad secundaria
		setFieldDefs(secondaryEntityDef.getName(), entityDefinicion);
		
		EntityRelationDef relation = entity.getRelation();
		relationType = relation.getType();
		
		String itemPrefix = mPrefixMainEntity;
		
		// Relaci�n con otra tabla del composite item que no sea la tabla principal
		String compositeTable = entity.getPrimaryTable();
		if (StringUtils.isNotBlank(compositeTable)) {
			itemPrefix = compositeTable;
		}
		
		//Obtenemos los datos que compondran la consulta a aplicar para obtener la entidad secundaria,
		//segun el tipo de relacion definida entre la entidad principal y la secundaria
		if (StringUtils.equalsIgnoreCase(relationType, EntityAppConstants.RELATION_TYPE_FIELD)) {
			
			//La relacion depende de 2 campos indicados para las 2 entidades
			fldPrimary = relation.getPrimaryField();
			fldSecondary = relation.getSecondaryField();
		}
		else if (StringUtils.equalsIgnoreCase(relationType, EntityAppConstants.RELATION_TYPE_NUMEXP)) {
			
			// La relacion esta establecida por el campo que contiene el n� de expediente en las 2 entidades
			fldPrimary = mainEntityDef.getKeyNumExp();
			fldSecondary = secondaryEntityDef.getKeyNumExp();
		}
		else if (StringUtils.equalsIgnoreCase(relationType, EntityAppConstants.RELATION_TYPE_PRIMARY_KEY)) {
						
			// La relacion esta establecida entre el campo que contiene la clave primaria de la entidad principal
			// y otro campo indicado de la entidad secundaria
			fldPrimary = mainEntityDef.getKeyField();
			fldSecondary = relation.getSecondaryField();
		}
		else if (StringUtils.equalsIgnoreCase(relationType, EntityAppConstants.RELATION_TYPE_QUERY)) {
			
			sqlQuery = relation.getQuery();
			if (StringUtils.isEmpty(sqlQuery)) {
				// No se ha indicado <query> en el par�metro
				throw new ISPACInfo("exception.entities.form.param.sqlQuery", 
									new String[] {mAppName, 
												  getLabel(mainEntityName + ":" + mainEntityName), 
												  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
			}
			
			// Se realizan las sustituciones correspondientes en la consulta
			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_PRIMARY_ENTITY, mainEntityDef.getName());
			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_PRIMARY_FIELD_NUMEXP, mainEntityDef.getKeyNumExp());
			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_PRIMARY_FIELD_PK, mainEntityDef.getKeyField());

			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_SECONDARY_ENTITY, secondaryEntityDef.getName());
			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_SECONDARY_FIELD_NUMEXP, secondaryEntityDef.getKeyNumExp());
			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_SECONDARY_FIELD_PK, secondaryEntityDef.getKeyField());

			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_NUMEXP, msExpedient);
		}
		else {
			// Tipo de relaci�n no v�lido
			throw new ISPACInfo("exception.entities.form.param.relationType", 
								new String[] {mAppName, 
											  getLabel(mainEntityName + ":" + mainEntityName), 
											  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
		}
		
		if (sqlQuery == null) {
			
			if (StringUtils.isEmpty(fldPrimary)) {
				// No se ha indicado <primary-field> en el par�metro
				throw new ISPACInfo("exception.entities.form.param.primaryFld", 
									new String[] {mAppName, 
												  getLabel(mainEntityName + ":" + mainEntityName), 
												  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
			}
			
			if (StringUtils.isEmpty(fldSecondary)) {
				// No se ha indicado <secondary-field> en el par�metro
				throw new ISPACInfo("exception.entities.form.param.secondaryFld", 
									new String[] {mAppName, 
												  getLabel(mainEntityName + ":" + mainEntityName), 
												  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
			}
			
			// Relaci�n entre la entidad primaria y secundaria
			// necesaria para obtener el listado de entidades a mostrar en un formulario de lista
			joinSecondaryEntity = itemPrefix + PrefixBuilder.PREFIX_DB + fldPrimary 
								+ " = " 
								+ secondaryEntityDef.getName() + PrefixBuilder.PREFIX_DB + fldSecondary;

			try {
				//[Ticket671#Teresa#INICIO]Cambio para que muestro el valor de la tabla de validaci�n que le corresponda
				// compruebo si fldPrimary no sea ese campo de otra entidad
				//x ejemplo CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION
				String [] vCampoOtraEntidad = fldPrimary.split(":");
				if(vCampoOtraEntidad.length>1){
					itemPrefix = vCampoOtraEntidad[0];
					fldPrimary = vCampoOtraEntidad[1];
				}
				//[Ticket671#Teresa#FIN]
				
				///valuePrimary = mitem.getString(itemPrefix + ":" + fldPrimary);
				valuePrimary = mitem.get(itemPrefix + ":" + fldPrimary);
				
			}
			catch (ISPACException ie) {
			}
			
			if (
				    (valuePrimary != null && (valuePrimary instanceof Object[] && ArrayUtils.isEmpty((Object[])valuePrimary)))
				 || (valuePrimary == null && (mitem.getProperty(itemPrefix + ":" + fldPrimary)).isMultivalue())
			   ){
				item = composeMultivalueIItem(new String[0]);
				secondaryTableName = fldPrimary + "_" + secondaryTableName;
				((CompositeItem) mitem).addItem(item, secondaryTableName + ":", storeItem, deleteItem);

				return secondaryEntityDef;
			}
			
			if (valuePrimary == null){
				
				if (!entity.getType().equals(EntityAppConstants.ENTITY_TYPE_VALIDATION)) {
					
					// Generar una entidad vac�a sin valor de registro
					item = entitiesAPI.getEntity(secondaryEntityDef.getId());
					((CompositeItem) mitem).addItem(item, secondaryTableName + ":", storeItem, deleteItem);
				}
								
				return secondaryEntityDef;
			}
			
			// Comprobar si se trata de un campo num�rico o alfanum�rico.
			String fieldValue = DBUtil.getFieldValue(entitiesAPI.getEntityFieldProperty(secondaryEntityDef.getName(), fldSecondary), valuePrimary); 
			
			if (!StringUtils.isEmpty(fieldValue)){
				sqlQuery = "WHERE " + fldSecondary + " IN (" + fieldValue + ")";
			}
		}
		else {
			int indexWhere = sqlQuery.toUpperCase().indexOf("WHERE");
			if (indexWhere != -1) {
			
				// Relaci�n entre la entidad primaria y secundaria
				// necesaria para obtener el listado de entidades a mostrar en un formulario de lista
				joinSecondaryEntity = sqlQuery.substring(indexWhere + 6);
			}
		}
		
		boolean hayResultados = false;
		IItemCollection itemcol = null;
		if (!StringUtils.isEmpty(sqlQuery)){
			// Obtener el registro de la entidad secundaria
			itemcol = entitiesAPI.queryEntities(secondaryEntityDef.getId(), sqlQuery);
			hayResultados = itemcol.next();
		}
		
		if (hayResultados == false) {
			if (!entity.getType().equals(EntityAppConstants.ENTITY_TYPE_VALIDATION)) {
			
				// Generar una entidad vac�a con valor de registro
				item = entitiesAPI.createEntity(secondaryEntityDef.getId());
	            String fieldNumExp = secondaryEntityDef.getKeyNumExp();
	            if (StringUtils.isNotEmpty(fieldNumExp)) {
	            	item.set(fieldNumExp, msExpedient); 
				}
			}else if (valuePrimary instanceof Object[]){
				item = composeMultivalueIItem(new String[((Object[])valuePrimary).length]);
			}
		}
		else {
			if (valuePrimary instanceof Object[]){
				Map map = itemcol.toMapStringKey("VALOR");
				int length = ((Object[])valuePrimary).length;
				String[] substitutes = new String[length];
				for (int i = 0; i < length; i++) {
					IItem itemSubstitute = (IItem)map.get(((Object[])valuePrimary)[i]);
					if (itemSubstitute != null)
						substitutes[i] = itemSubstitute.getString("SUSTITUTO");
					else
						substitutes[i] = "";
				}
				item = composeMultivalueIItem(substitutes);

			}else{
				item = itemcol.value();
			}
		}
		
		// A�adir un nuevo IItem al Composite, si es una tabla de validacion, al nombre de la tabla le concatenamos el nombre del campo,
		// para diferenciarlo si existe otro campo que se valide contra la misma tabla
		if (entity.getType().equals(EntityAppConstants.ENTITY_TYPE_VALIDATION)) {
			secondaryTableName = fldPrimary + "_" + secondaryTableName;
		}
		
		if (item != null) {
			
			((CompositeItem) mitem).addItem(item, secondaryTableName + ":", storeItem, deleteItem);
		}
		
		return secondaryEntityDef;
	}
	
	private IItem composeMultivalueIItem(String[] substitutes) throws ISPACException{
		GenericItem multivalueItem;
		Properties propSet = new Properties();
		propSet.add(new Property(0,"SUSTITUTO",Types.VARCHAR, true) );
		multivalueItem = new GenericItem(propSet, "");
		multivalueItem.set("SUSTITUTO", substitutes);
		return multivalueItem;
	}
	
	
	public Map clone(IItem newExpedient, 
					 Map entityFields) throws ISPACException {
		return clone(newExpedient, entityFields, null);
	}

	public Map clone(IItem newExpedient, 
					 Map entityFields,
					 Map noCloneSecondaryCtEntityIds) throws ISPACException {
		
		Map clonedSecondaryCTEntityIds = new HashMap();
				
		CompositeItem compositeItem = (CompositeItem) mitem;
		
        DbCnt cnt = mContext.getConnection();
        try {
		
        	IItem newMainEntity = null;
        	List excludedFields = null;
        	List entityFieldsToClone = null;
        	String fieldNumExp = null;
        	String fieldPKey = null;
        	
        	// Si la entidad principal es el expediente no se clona
        	// porque ya se ha creado al crear el proceso
        	if (mMainEntityId == SpacEntities.SPAC_EXPEDIENTES) {
        		
        		newMainEntity = newExpedient;
        	}
        	else {
        		// Entidad principal a clonar
				IItem sourceEntity = compositeItem.getItemWithPrefix(mPrefixMainEntity);
				
				// Crear el nuevo registro de la entidad
				// en el que se copiar�n los datos de los campos a clonar
		    	EntityDAO entityDAO = EntityFactoryDAO.getInstance().newEntityDAO(cnt, mainEntityDef);
		    	entityDAO.createNew(cnt);
		    	newMainEntity = entityDAO;
		    	
		    	excludedFields = new ArrayList();
		    	
	    		fieldNumExp = mainEntityDef.getKeyNumExp();
	    		fieldPKey = mainEntityDef.getKeyField();
	    		
	    		// La clave primaria no se clona
	    		excludedFields.add(fieldPKey);
	    		
	    		// Establecer el n�mero del expediente clonado
	    		if (StringUtils.isNotEmpty(fieldNumExp)) {
	    			
	    			newMainEntity.set(fieldNumExp, newExpedient.getString("NUMEXP"));
	    			// El n�mero de expediente no se clona
	    			excludedFields.add(fieldNumExp);
	    		}
	    		
	    		// Clonar la entidad
	    		entityFieldsToClone = getEntityFieldsToClone(mainEntityDef, entityFields);    		
	    		cloneEntityData(sourceEntity, newMainEntity, excludedFields, entityFieldsToClone);
        	}
    		
    		// Entidades secundarias a clonar
    		if (getParameters() != null) {
    			
    			// Entidades declaradas en los par�metros
    			List entities = getParameters().getEntities();
    			
    			Iterator it = entities.iterator();
    			while (it.hasNext()) {
    				
    				EntityParameterDef entity = (EntityParameterDef) it.next();
    				
    				// Entidades agregadas (composici�n) que no sean de s�lo lectura
    				if ((entity.getType().equals(EntityAppConstants.ENTITY_TYPE_COMPOSITE)) &&
    					(!entity.isReadonly())) {
    					
    					EntityRelationDef relation = entity.getRelation();
    						
    					// Entidad secundaria relacionada con la entidad principal
    						String secondaryTableName = entity.getTable();
    						if (StringUtils.isNotBlank(secondaryTableName)) {

    						// Definici�n de la entidad secundaria
    						IEntityDef secondaryEntityDef = EntityFactoryDAO.getInstance().getCatalogEntityDAO(cnt, secondaryTableName);
    						
    						// Comprobar que la entidad puede ser clonada
    						if ((noCloneSecondaryCtEntityIds == null) ||
    							(!noCloneSecondaryCtEntityIds.containsKey(new Integer(secondaryEntityDef.getId())))) {
    						
    							fieldNumExp = secondaryEntityDef.getKeyNumExp();
    							fieldPKey = secondaryEntityDef.getKeyField();
	    					
    							String fldSecondary = null;
	    					
    							// Tipo de relaci�n entre las entidades
    							if (relation.getType().equals(EntityAppConstants.RELATION_TYPE_NUMEXP)) {
    								fldSecondary = fieldNumExp;
    							}
    							else if (!relation.getType().equals(EntityAppConstants.RELATION_TYPE_QUERY)) {
    								fldSecondary = relation.getSecondaryField();
    							}
    							
    							// Entidad agregada a clonar
    							IItem sourceEntity = null;
    							
    							// Comprobar si existe el registro para la entidad secundaria
    							if (fldSecondary != null) {
    								
        							// Cuando la relaci�n es por campo en la entidad secundaria
    								// comprobar que el campo tenga valor
    							if (StringUtils.isNotEmpty(mitem.getString(secondaryTableName + ":" + fldSecondary))) {
    								
    								// Entidad agregada a clonar
	    								sourceEntity = compositeItem.getItemWithPrefix(secondaryTableName);
    								}
    							}
								else {
    								// Entidad agregada a clonar
    								sourceEntity = compositeItem.getItemWithPrefix(secondaryTableName);
								}
    							
    							// Clonar cuando la entidad agregada exista en el expediente a clonar
    							if (sourceEntity != null) {
	    								
    								// Comprobar si la entidad secundaria ya existe en el nuevo expediente
    								// IItem newSecondaryEntity = EntityFactoryDAO.getInstance().getEntity(cnt, secondaryEntityDef.getId(), newExpedient.getString("NUMEXP"));
    								IItem newSecondaryEntity = getSecondaryEntity(newMainEntity, entity, secondaryEntityDef);
    								if (newSecondaryEntity == null) {
    								
    								// Crear el nuevo registro de la entidad
    								// en el que se copiar�n los datos de los campos a clonar
    								EntityDAO entityDAO = EntityFactoryDAO.getInstance().newEntityDAO(cnt, secondaryTableName);
    						    	entityDAO.createNew(cnt);
	    						    	newSecondaryEntity = entityDAO;
    								}
    						    	
    						    	excludedFields = new ArrayList();
    						    	
    					    		// La clave primaria no se clona
    					    		excludedFields.add(fieldPKey);
    					    		
    					    		// Establecer el n�mero del expediente clonado
    					    		if (StringUtils.isNotEmpty(fieldNumExp)) {
    					    			
    					    			newSecondaryEntity.set(fieldNumExp, newExpedient.getString("NUMEXP"));
    					    			// El n�mero de expediente no se clona
    					    			excludedFields.add(fieldNumExp);
    					    		}
    					    		
    					    		if (secondaryEntityDef.getId() == SpacEntities.SPAC_EXPEDIENTES) {
    					    			
    					    			excludedFields.add("IDPROCESO");
    					    			excludedFields.add("REFERENCIA_INTERNA");
    					    		}
    					    		
    					    		// Cuando las entidades no se relacionan por n�mero de expediente
    					    		// establecer la relaci�n entre la entidad principal ya clonada y la secundaria a clonar
    					    		if ((!relation.getType().equals(EntityAppConstants.RELATION_TYPE_NUMEXP)) &&
    					    			(fldSecondary != null)) {
    					    			
	    								Object value = null;
	    								if (StringUtils.equalsIgnoreCase(relation.getType(), EntityAppConstants.RELATION_TYPE_PRIMARY_KEY)) {
	    									value = newMainEntity.get(mKeyMainEntity);
	    								}
	    								else if (StringUtils.equalsIgnoreCase(relation.getType(), EntityAppConstants.RELATION_TYPE_FIELD)) {
	    									value = newMainEntity.get(relation.getPrimaryField());
	    								}
	    								newSecondaryEntity.set(fldSecondary, value);
	    								// El campo relacionado no se clona
	    								excludedFields.add(fldSecondary);
    					    		}
    					    		
    								// Clonar la entidad secundaria
    								entityFieldsToClone = getEntityFieldsToClone(secondaryEntityDef, entityFields);
    					    		cloneEntityData(sourceEntity, newSecondaryEntity, excludedFields, entityFieldsToClone);
    					    		
    					    		clonedSecondaryCTEntityIds.put(new Integer(secondaryEntityDef.getId()), null);
    							}
    						}
    					}
    				}
    			}
    		}
    	}
        catch (ISPACException ie) {
            throw new ISPACException("exception.entities.clone", "("+ mPrefixMainEntity + ")");
        }
        finally {
        	mContext.releaseConnection(cnt);
        }
        
        return clonedSecondaryCTEntityIds;
	}
	
	private void cloneEntityData(IItem sourceEntity, IItem newEntity, List excludedFields, List fieldsToClone) throws ISPACException {
		
		Iterator iterator = sourceEntity.getProperties().iterator();
		while (iterator.hasNext()) {
			
			Property property = (Property) iterator.next();
			String propertyName = property.getName();
			
			if ((excludedFields != null) && 
				(excludedFields.contains(propertyName))) {
				continue;
			}
			
			if (((fieldsToClone != null) &&(!fieldsToClone.contains(propertyName)))
					|| fieldsToClone==null) {
				continue;
			}
			
			Object value = sourceEntity.get(propertyName);
			newEntity.set(propertyName, value);
		}
		
		newEntity.store(mContext);
	}
	
	private List getEntityFieldsToClone(IEntityDef entityDef, 
										Map entityFields) throws ISPACException {
		
		String[] idFieldsToClone = null;
		if (entityFields != null) {						
			idFieldsToClone = (String[]) entityFields.get(String.valueOf(entityDef.getId()));
		}
		
		EntityField entityField;
		List entityFieldsToClone = new ArrayList();
			
		// Obtener los campos de la definici�n de la entidad
		EntityDef entityDefinition = EntityDef.parseEntityDef(entityDef.getDefinition());
			
		if (idFieldsToClone != null) {
		
			for (int i = 0; i < idFieldsToClone.length; i++) {
			
				entityField = entityDefinition.findField(Integer.parseInt(idFieldsToClone[i]));
				if (entityField != null) {
					
					entityFieldsToClone.add(entityField.getPhysicalName().toUpperCase());
				}
			}
		}
		else {
			List fields = entityDefinition.getFields();
			
			Iterator it = fields.iterator();
			while (it.hasNext()) {
				
				entityField = (EntityField) it.next();
				entityFieldsToClone.add(entityField.getPhysicalName().toUpperCase());
			}
		}
		
		return entityFieldsToClone;
	}

	/**
	 * Obtener la entidad secundaria.
	 * 
	 * @param mainEntity
	 * @param entity Definici�n de par�metro de entidad.
	 * @param secondaryEntityDef Definici�n de la entidad secundaria.
	 * @return entidad secundaria.
	 * 
	 * @throws ISPACException Si se produce alg�n error.
	 */
	private IItem getSecondaryEntity(IItem mainEntity,
									 EntityParameterDef entity,
									 IEntityDef secondaryEntityDef) throws ISPACException {
		
		String fldPrimary = null;
		String fldSecondary = null;
		Object valuePrimary = null;
		String sqlQuery = null;
		String relationType = null;
		
		IEntitiesAPI entitiesAPI = mContext.getAPI().getEntitiesAPI();
						
		// Definici�n de la entidad secundaria
		if (secondaryEntityDef == null) {
			
			// No existe una entidad con la tabla BD declarada
			throw new ISPACInfo("exception.entities.form.paramTable.noExist", 
								new String[] {mAppName, 
											  getLabel(mainEntityName + ":" + mainEntityName), 
											  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
		}
		
		EntityRelationDef relation = entity.getRelation();
		relationType = relation.getType();
		
		//Obtenemos los datos que compondran la consulta a aplicar para obtener la entidad secundaria,
		//segun el tipo de relacion definida entre la entidad principal y la secundaria
		if (StringUtils.equalsIgnoreCase(relationType, EntityAppConstants.RELATION_TYPE_FIELD)) {
			
			//La relacion depende de 2 campos indicados para las 2 entidades
			fldPrimary = relation.getPrimaryField();
			fldSecondary = relation.getSecondaryField();
		}
		else if (StringUtils.equalsIgnoreCase(relationType, EntityAppConstants.RELATION_TYPE_NUMEXP)) {
			
			// La relacion esta establecida por el campo que contiene el n� de expediente en las 2 entidades
			fldPrimary = mainEntityDef.getKeyNumExp();
			fldSecondary = secondaryEntityDef.getKeyNumExp();
		}
		else if (StringUtils.equalsIgnoreCase(relationType, EntityAppConstants.RELATION_TYPE_PRIMARY_KEY)) {
						
			// La relacion esta establecida entre el campo que contiene la clave primaria de la entidad principal
			// y otro campo indicado de la entidad secundaria
			fldPrimary = mainEntityDef.getKeyField();
			fldSecondary = relation.getSecondaryField();
		}
		else if (StringUtils.equalsIgnoreCase(relationType, EntityAppConstants.RELATION_TYPE_QUERY)) {
			
			sqlQuery = relation.getQuery();
			if (StringUtils.isEmpty(sqlQuery)) {
				// No se ha indicado <query> en el par�metro
				throw new ISPACInfo("exception.entities.form.param.sqlQuery", 
									new String[] {mAppName, 
												  getLabel(mainEntityName + ":" + mainEntityName), 
												  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
			}
			
			// Se realizan las sustituciones correspondientes en la consulta
			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_PRIMARY_ENTITY, mainEntityDef.getName());
			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_PRIMARY_FIELD_NUMEXP, mainEntityDef.getKeyNumExp());
			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_PRIMARY_FIELD_PK, mainEntityDef.getKeyField());

			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_SECONDARY_ENTITY, secondaryEntityDef.getName());
			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_SECONDARY_FIELD_NUMEXP, secondaryEntityDef.getKeyNumExp());
			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_SECONDARY_FIELD_PK, secondaryEntityDef.getKeyField());

			sqlQuery = StringUtils.replace(sqlQuery, EntityAppConstants.REF_NUMEXP, mainEntity.getString("NUMEXP"));
		}
		else {
			// Tipo de relaci�n no v�lido
			throw new ISPACInfo("exception.entities.form.param.relationType", 
								new String[] {mAppName, 
											  getLabel(mainEntityName + ":" + mainEntityName), 
											  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
		}
		
		if (sqlQuery == null) {
			
			if (StringUtils.isEmpty(fldPrimary)) {
				// No se ha indicado <primary-field> en el par�metro
				throw new ISPACInfo("exception.entities.form.param.primaryFld", 
									new String[] {mAppName, 
												  getLabel(mainEntityName + ":" + mainEntityName), 
												  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
			}
			
			if (StringUtils.isEmpty(fldSecondary)) {
				// No se ha indicado <secondary-field> en el par�metro
				throw new ISPACInfo("exception.entities.form.param.secondaryFld", 
									new String[] {mAppName, 
												  getLabel(mainEntityName + ":" + mainEntityName), 
												  StringUtils.escapeHtml(ParametersDef.toXmlEntityParameterDef(entity))});
			}

			try {
				///valuePrimary = mitem.getString(itemPrefix + ":" + fldPrimary);
				valuePrimary = mainEntity.get(fldPrimary);
			}
			catch (ISPACException ie) {
			}
			
			// Comprobar si se trata de un campo num�rico o alfanum�rico.
			String fieldValue = DBUtil.getFieldValue(entitiesAPI.getEntityFieldProperty(secondaryEntityDef.getName(), fldSecondary), valuePrimary); 
			
			if (!StringUtils.isEmpty(fieldValue)){
				sqlQuery = "WHERE " + fldSecondary + " IN (" + fieldValue + ")";
			}
		}
		
		if (!StringUtils.isEmpty(sqlQuery)) {
			
			// Obtener el registro de la entidad secundaria
			IItemCollection itemcol = entitiesAPI.queryEntities(secondaryEntityDef.getId(), sqlQuery);
			if (itemcol.next()) {
				
				return itemcol.value();
			}
		}
		
		return null;
	}
	
}