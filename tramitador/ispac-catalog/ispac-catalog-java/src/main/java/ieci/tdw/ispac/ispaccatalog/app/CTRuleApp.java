package ieci.tdw.ispac.ispaccatalog.app;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.app.SimpleEntityApp;
import ieci.tdw.ispac.ispaclib.bean.ValidationError;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;

/**
 * EntityApp para la informaci�n de una regla del cat�logo de reglas.
 *
 */
public class CTRuleApp extends SimpleEntityApp {

	//private static final String[][] ruleTypes = {{"Acci�n","1"},{"C�lculo de responsabilidad", "2"}};
	
	//[dipucr-Felipe #665]
	private static final String ALLOW_DUPLICATED_CLASS_KEY = "DATOSREGLA";
	
    public CTRuleApp(ClientContext context)
    {
        super(context);
    }

	public void initiate() throws ISPACException
    {
		//Obtener la lista de tipos de reglas
		//valuesExtra.put("RULE_TYPES", getComboRuleTypes(false));
    }

	/**
	 * Valida la informaci�n del formulario.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public boolean validate() throws ISPACException {
		
		boolean ret = super.validate();
		
		if (ret) {
			
			IInvesflowAPI invesFlowAPI = mContext.getAPI();
			ICatalogAPI catalogAPI = invesFlowAPI.getCatalogAPI();
			
			// Bloqueo de la tabla
			catalogAPI.setCTEntitiesForUpdate(ICatalogAPI.ENTITY_CT_RULE, "");
			
			// Nombre �nico de regla
			String nombre = getString("NOMBRE");
			IItemCollection itemcol = catalogAPI.queryCTEntities(ICatalogAPI.ENTITY_CT_RULE, " WHERE NOMBRE = '" + DBUtil.replaceQuotes(nombre) + "' AND ID != " + getString("ID"));
			if (itemcol.next()) {
				
				addError(new ValidationError("property(NOMBRE)", "error.rule.nameDuplicated", new String[] {nombre}));
				return false;
			}
			
			// Clase �nica en las reglas
			String clase = getString("CLASE");
			itemcol = catalogAPI.queryCTEntities(ICatalogAPI.ENTITY_CT_RULE, " WHERE CLASE = '" + DBUtil.replaceQuotes(clase) + "' AND ID != " + getString("ID"));
			if (itemcol.next()) {
				
				if (!clase.toUpperCase().contains(ALLOW_DUPLICATED_CLASS_KEY)){//[dipucr-Felipe #665]
					addError(new ValidationError("property(CLASE)",	"error.rule.classDuplicated", new String[] {clase}));
					return false;
				}
			}
			
			return true;
		}
		else {
			return false;
		}
	}
	
	/*
	private List getComboRuleTypes(boolean noValue) {
		List aux = new ArrayList();
		if(noValue){
			aux.add(new LabelValueBean("", "-1"));
		}
		for(int i=0; i<ruleTypes.length;i++){
			aux.add(new LabelValueBean(ruleTypes[i][0], ruleTypes[i][1]));
		}
		return aux;
	}
	*/
	
}