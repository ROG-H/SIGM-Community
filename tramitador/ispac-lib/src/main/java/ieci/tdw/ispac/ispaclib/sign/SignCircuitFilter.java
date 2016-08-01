package ieci.tdw.ispac.ispaclib.sign;

public class SignCircuitFilter {

	//TODO [FIRMA]Se iran a�adiendo mas parametros que serviran para saber sobre que elementos se aplica el filtro

	int pcdId;
	int respUID;
	//Identificador del portafirmas
	String idSistema;
	//Indica si el circuito pertenece al portafirmas por defecto
	boolean defaultPortafirmas=false;

	int taskPcdId; //INICIO [eCenpri-Felipe #592]


	public int getPcdId() {
		return pcdId;
	}
	public void setPcdId(int pcdId) {
		this.pcdId = pcdId;
	}
	public int getRespUID() {
		return respUID;
	}
	public void setRespUID(int respUID) {
		this.respUID = respUID;
	}
	public String getIdSistema() {
		return idSistema;
	}
	public void setIdSistema(String idSistema) {
		this.idSistema = idSistema;
	}
	public boolean isDefaultPortafirmas() {
		return defaultPortafirmas;
	}
	public void setDefaultPortafirmas(boolean defaultPortafirmas) {
		this.defaultPortafirmas = defaultPortafirmas;
	}

	//INICIO [eCenpri-Felipe #592]
	public int getTaskPcdId() {
		return taskPcdId;
	}
	public void setTaskPcdId(int taskPcdId) {
		this.taskPcdId = taskPcdId;
	}
	//FIN [eCenpri-Felipe #592]
}
