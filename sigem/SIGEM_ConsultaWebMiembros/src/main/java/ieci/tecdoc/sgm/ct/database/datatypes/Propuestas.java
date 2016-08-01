package ieci.tecdoc.sgm.ct.database.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class Propuestas extends ArrayList implements Serializable {
	
	 /**
    * Constructor de clase
    */

	public Propuestas() {
		//expedientes = new ArrayList();
	}

	/**
	 * Devuelve el n�mero de expedientes de la colecci�n.
	 * @return int N�mero de expedientes de la colecci�n.
	 */
	public int count() {
		//return expedientes.size();
		return size();
	}

	/**
	 * Devuelve el expediente de la posici�n indicada dentro de la colecci�n
	 * @param index Posici�n del expediente a recuperar.
	 * @return Expediente asociado a registro.
	 */
	public Object get(int index) {
		//return (Expediente) expedientes.get(index);
		return (Propuesta)super.get(index);
	}

	/**
	 * A�ade un nuevo expediente a la colecci�n.
	 * @param expediente Nuevo expediente a a�adir.
	 */
	public void add(Expediente expediente) {
		//expedientes.add(expediente);
		super.add(expediente);
	}

}

