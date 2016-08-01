package ieci.tecdoc.sgm.core.services.consulta;

import java.util.ArrayList;
import java.util.List;

public class Propuestas {

	private List propuestas;
	
	public Propuestas(){
		propuestas = new ArrayList();
	}
	
	public int count() {
		return propuestas.size();
	}

	/**
	 * Devuelve el expediente de la posici�n indicada dentro de la colecci�n
	 * @param index Posici�n del expediente a recuperar.
	 * @return Expediente asociado a registro.
	 */
	public Propuesta get(int index) {
		return (Propuesta)propuestas.get(index);
	}

	/**
	 * A�ade un nuevo expediente a la colecci�n.
	 * @param expediente Nuevo expediente a a�adir.
	 */
	public void add(Propuesta propuesta) {
		propuestas.add(propuesta);
	}

	public List getPropuestas() {
		return propuestas;
	}

	public void setPropuestas(List propuestas) {
		this.propuestas = propuestas;
	}

}
