package ieci.tdw.ispac.ispaclib.common.constants;


/**
 * 
 * @author Manu
 *
 * El estado con valor 3 se correspond�a con la firma con reparo pero esa opci�n se elimin�
 * 
 * public static final int FIRMADO_REPARO = 3; 
 */

public class SignCircuitStates {
	public static final int SIN_INICIAR = 0;
	public static final int PENDIENTE = 1;
	public static final int FINALIZADO = 2;
		
	//[MQE Ticket #31] A�adimos la opcion de Rechazar firma 
	public static final int FIRMADO_REPARO = 3;
	public static final int RECHAZADO = 4;
	//[MQE Ticket #31] Fin Rechazar firma
}
