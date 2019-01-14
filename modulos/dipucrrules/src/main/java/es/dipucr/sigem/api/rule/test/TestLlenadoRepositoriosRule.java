package es.dipucr.sigem.api.rule.test;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.extra.ExtraConfiguration;
import es.ieci.tecdoc.isicres.admin.estructura.dao.Volume;
import es.ieci.tecdoc.isicres.admin.estructura.dao.VolumeList;
import es.ieci.tecdoc.isicres.admin.estructura.dao.VolumeLists;
import es.ieci.tecdoc.isicres.admin.estructura.dao.impl.VolumesImpl;

public class TestLlenadoRepositoriosRule implements IRule 
{
	
	public final static String LIST_TRAMSEGURA_INITIAL_NAME = "ListaTramitadorSegura";
	
	/** Logger de la clase. */
	private static final Logger LOGGER = Logger.getLogger(TestLlenadoRepositoriosRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		try{
			String idEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
			
			//Porcentaje m�ximo de los vol�menes
			double MAX_PERCENT = 0.0;
			ExtraConfiguration extraConf = ExtraConfiguration.getInstance(rulectx.getClientContext());
			try{
				String sPercent = extraConf.get(ExtraConfiguration.ALERTAS_VOLUMENES.OCCUPANCY_PERCENT);
				MAX_PERCENT = Double.valueOf(sPercent);
			}
			catch(Exception ex){
				throw new ISPACException("La variable '" + ExtraConfiguration.ALERTAS_VOLUMENES.OCCUPANCY_PERCENT
						+ "' no existe en el fichero extra.properties de la entidad o no tiene un formato n�merico");
			}
			
			//Recuperamos las listas de vol�menes
			//* ListaRegistro01, ListaTramitador01, ListaArchivo01, ListaTramitadorSegura01
			VolumeLists volumneListsAPI = new VolumeLists();
			volumneListsAPI.load(idEntidad);
			
			for (int i = 0; i < volumneListsAPI.count(); i++){
				VolumeList volList = volumneListsAPI.getVolumeList(i);
				
				if (!volList.getName().contains(LIST_TRAMSEGURA_INITIAL_NAME)){
					// Obtener los vol�menes de la lista de vol�menes
					VolumesImpl volumesAPI = new VolumesImpl();
					volumesAPI.loadByVolumeList(volList.getId(), "");
					
					//Recuperamos �nicamente el �ltimo volumen de cada lista, que es el volumen activo
					Volume volume = volumesAPI.getVolume(volumesAPI.count() - 1);

					double dActSize = Double.valueOf(volume.getActSize());
					double dMaxSize = Double.valueOf(volume.getMaxSize());
					double dPercent = dActSize/dMaxSize * 100; 
					if (dPercent > MAX_PERCENT){
						rulectx.setInfoMessage("Volumen " + volume.getName() + " superado l�mite ocupaci�n: " + dPercent + "%");
					}
					
					DecimalFormat df = new DecimalFormat("#.00");
					LOGGER.warn("Nombre volumen: " + volume.getName());
					LOGGER.warn("Tama�o actual: " + volume.getActSize());
					LOGGER.warn("Tama�o m�ximo: " + volume.getMaxSize());
					LOGGER.warn("Porcentaje ocupaci�n: " + df.format(dPercent) + "%");
				}
			}
		}
		catch (Exception e) {
			String error = "Error test llenado repositorios: " + e.getMessage();
			LOGGER.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return new Boolean(true);
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
