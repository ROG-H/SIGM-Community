package es.dipucr.portafirmas.common;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.AssignJobToUser;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.AssignJobToUserResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.DeleteJobs;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.DeleteJobsResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.DeleteUsers;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.DeleteUsersResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedJobs;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedJobsResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedUsers;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedUsersResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.SeparateJobToUser;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.SeparateJobToUserResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateEnhancedJobs;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateEnhancedJobsResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateEnhancedUsers;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateEnhancedUsersResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.PfirmaException;



public class ServiciosAdministracion {
	/**
	 * Objeto proxy que har� de web service para realizar las llamadas
	 */
	private AdminServiceStub ws;
	
	protected static final Logger logger = Logger.getLogger(ServiciosAdministracion.class);	
	
	/**
	 * Constructor
	 * @throws AxisFault 
	 */
	public ServiciosAdministracion () throws AxisFault{
		ws = new AdminServiceStub();
	}

	/**
	 * Da de alta cargos en la aplicacion
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public InsertEnhancedJobsResponse altaCargosAplicacion (InsertEnhancedJobs cargosAlta) throws RemoteException, PfirmaException{
	
		return ws.insertEnhancedJobs(cargosAlta); 

	}
	
	/**
	 * Da de alta usuarios en la aplicacion
	 * @return InsertEnhancedUsersResponse N�mero de usuarios insertados.
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public InsertEnhancedUsersResponse altaUsuariosAplicacion (InsertEnhancedUsers usuariosAlta) throws RemoteException, PfirmaException{
		
		return ws.insertEnhancedUsers(usuariosAlta); 

	}
	
	/**
	 * updateEnhancedUsers: Modifica usuarios en la aplicaci�n. Si alguno de los usuarios no 
	 * existiera o no estuviera vigente dar� error.
	 * @return UpdateEnhancedUsersResponse N�mero de usuarios modificados
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public UpdateEnhancedUsersResponse modificarUsuarios (UpdateEnhancedUsers updateEnhancedUsers) throws RemoteException, PfirmaException{

		return ws.updateEnhancedUsers(updateEnhancedUsers);
	}
	
	/**
	 * Elimina usuarios de la aplicaci�n. Si alg�n usuario no existe dar� error. Si el usuario tiene relaciones que 
	 * no pueden eliminarse (peticiones, autorizaciones sobre otros usuarios...) el usuario se modificar� para que 
	 * quede como no vigente.
	 * @return DeleteUsersResponse
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public DeleteUsersResponse eliminarUsuarios (DeleteUsers deleteUsers) throws RemoteException, PfirmaException{
		
		return ws.deleteUsers(deleteUsers);
	}
	
	/**
	 * Crea nuevos cargos en la aplicaci�n. Si alguno de los cargos existiera y estuviera vigente dar�a error. 
	 * En caso de existir y no estar vigente se modificar�a para que lo estuviera. 
	 * @param insertEnhancedJobs
	 * @return InsertEnhancedJobsResponse N�mero de cargos insertados.
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public InsertEnhancedJobsResponse insertarNuevoCargo(InsertEnhancedJobs insertEnhancedJobs) throws RemoteException, PfirmaException{
		
		return ws.insertEnhancedJobs(insertEnhancedJobs);
	}
	
	/**
	 * Modifica cargos en la aplicaci�n. Si alguno de los cargos no existiera o no estuviera vigente dar� error.
	 * @param updateEnhancedJobs 
	 * @return UpdateEnhancedJobsResponse N�mero de cargos modificados
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public UpdateEnhancedJobsResponse modificarCargos(UpdateEnhancedJobs updateEnhancedJobs) throws RemoteException, PfirmaException{
		
		return ws.updateEnhancedJobs(updateEnhancedJobs);
	}
	
	/**
	 * Elimina cargos de la aplicaci�n. Si alg�n cargo no existe dar� error. Si el cargo tiene relaciones que no 
	 * pueden eliminarse (usuarios asociados, peticiones) el cargo se modificar� para que quede como no vigente.
	 * @param deleteJobs N�mero de cargos borrados.
	 * @return DeleteJobsResponse
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public DeleteJobsResponse eliminarCargos(DeleteJobs deleteJobs) throws RemoteException, PfirmaException{
		return ws.deleteJobs(deleteJobs);
	}
	
	/**
	 * Asigna un cargo a un usuario para un determinado periodo de tiempo. Si ya existiera la relaci�n usuariocargo, 
	 * se modificar�a el periodo de vigencia.
	 * @param assignJobToUser 
	 * @return AssignJobToUserResponse; Indica si se ha podido hacer la asociaci�n.
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public AssignJobToUserResponse asignaCargoUsuario(AssignJobToUser assignJobToUser) throws RemoteException, PfirmaException{
		return ws.assignJobToUser(assignJobToUser);
	}
	
	
	/**
	 * Elimina la relaci�n usuario-cargo.
	 * @param separateJobToUser 
	 * @return SeparateJobToUserResponse Indica si se ha eliminado la asociaci�n.
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public SeparateJobToUserResponse separarUsuarioCargo(SeparateJobToUser separateJobToUser) throws RemoteException, PfirmaException{
		return ws.separateJobToUser(separateJobToUser);
	}
	
}
