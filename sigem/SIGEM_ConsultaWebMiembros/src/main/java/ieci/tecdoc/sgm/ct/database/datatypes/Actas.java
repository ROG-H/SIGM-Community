package ieci.tecdoc.sgm.ct.database.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class Actas extends ArrayList implements Serializable {
	
	 /**
    * Constructor de clase
    */

	public Actas() {
	}

	/**
	 * Devuelve el n�mero de expedientes de la colecci�n.
	 * @return int N�mero de expedientes de la colecci�n.
	 */
	public int count() {
		return size();
	}

	/**
	 * Devuelve el expediente de la posici�n indicada dentro de la colecci�n
	 * @param index Posici�n del expediente a recuperar.
	 * @return Expediente asociado a registro.
	 */
	public Object get(int index) {
		return (Acta)super.get(index);
	}
}

