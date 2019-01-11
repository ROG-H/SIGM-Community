/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versi�n 1.1 o �en cuanto sean aprobadas por laComisi�n Europea� versiones posteriores de la EUPL (la �Licencia�); 
* Solo podr� usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
* V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.dao;

import org.apache.log4j.Logger;

import es.msssi.sgm.registropresencial.beans.AuditBean;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;

/**
 * Clase que realiza las operaciones de auditoria a base de datos.
 * 
 * @author cmorenog
 */
public class AuditDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(AuditDAO.class.getName());


    /**
     * Devuelve un audit.
     * 
    * @param audit
     *           audit.
     * @return la direcci�n del organismo.
     */
    public void insertAudit(AuditBean audit) {
	LOG.trace("Entrando en AuditDAO.insertAudit()");

	getSqlMapClientTemplate().insert("Audit.insertAudit",
		audit);
	getSqlMapClientTemplate().insert("Audit.insertAuditString",
		audit);
	LOG.trace("Saliendo en AuditDAO.insertAudit()");
    }
}