package es.dipucr.sigem.api.rule.publicador;

import ieci.tdw.ispac.ispaclib.utils.MapUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacpublicador.business.action.DefaultAction;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.util.Iterator;
import java.util.Map;

/**
 * Acci�n base para el publicador de SIGEM.
 *
 */
public abstract class SigemBaseAction extends DefaultAction {

	/**
	 * Constructor.
	 */
	public SigemBaseAction() {
		super();
	}
	
	/**
	 * Obtiene la informaci�n de la entidad.
	 * @return Informaci�n de la entidad.
	 */
	public Entidad getEntidad() {
		return EntidadHelper.getEntidad();
	}
	
	/**
	 * Sustituye los par�metros en una cadena de texto.
	 * @param str Cadena de texto.
	 * @param params Mapa con los par�metros.
	 * @return Cadena final.
	 */
	public String substituteParams(String str, Map params) {
		
		if (StringUtils.isBlank(str) || MapUtils.isEmpty(params)) {
			return str;
		}
		
		Iterator it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			str = StringUtils.replace(str, "${" + String.valueOf(entry.getKey()) + "}", String.valueOf(entry.getValue()));
		}
		
		return str;
	}
}
