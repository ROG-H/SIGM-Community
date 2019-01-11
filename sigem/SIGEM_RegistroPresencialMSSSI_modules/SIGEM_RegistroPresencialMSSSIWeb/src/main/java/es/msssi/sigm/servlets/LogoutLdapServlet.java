/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versi�n 1.1 o �en cuanto sean aprobadas por laComisi�n Europea� versiones posteriores de la EUPL (la �Licencia�); 
 * Solo podr� usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
 * V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sigm.servlets;

import ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Servlet de logout.
 * 
 */
public class LogoutLdapServlet extends HttpServlet {

    private static final long serialVersionUID = 7244072384073764702L;
    private static final Logger logger = Logger.getLogger(LogoutLdapServlet.class);
    private static String urlLogout = null;
    
    @Override
    public void init(final ServletConfig config) throws ServletException {
    	try {
        setUrlLogout(Boolean.parseBoolean((String) new InitialContext().lookup("java:comp/env/IS_LOGIN_CERT_ACTIVE"))
            ? "/login/cert.jsp" : config.getInitParameter("urlLogout"));
      } catch (NamingException e) {
        // poner a logincert cuando sea definitivamente cert
        setUrlLogout(config.getInitParameter("urlLogout"));
      }
    }
    

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	HttpSession session = request.getSession();
	try {
	    // Quitamos la 'llave' que introducimos con el usuario
	    // codificado, por si se entraba en el m�dulo de Registro
	    // Presencial
	    if (session.getAttribute("keySesionUsuarioRP") != null)
		session.removeAttribute("keySesionUsuarioRP");

	    session.removeAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_ENTIDAD);
	    session.removeAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_APLICACION);
	    session.removeAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_DOMINIO);
	    session.removeAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_USUARIO);
	    session.removeAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_PASSWORD);
	    session.removeAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_DATOS_SESION);
	    session.removeAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_USUARIO);
	    session.removeAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_KEY_SESION_USUARIO);
	    session.invalidate();
	    response.sendRedirect(request.getContextPath()+urlLogout);
	   
	    return;
	}
	catch (Exception e) {
	    logger.error("Se ha producido un error al deslogar.", e.fillInStackTrace());
	    request.setAttribute("ERROR", "ERROR_GENERIC");
	    RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
	    rd.include(request, response);
	    return;
	}
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doPost(request, response);
    }
    
    /**
     * Obtiene el valor del par�metro urlLogout.
     * 
     * @return urlLogout valor del campo a obtener.
     */
    public static String getUrlLogout() {
        return urlLogout;
    }
    /**
     * Guarda el valor del par�metro urlLogout.
     * 
     * @param urlLogout
     *            valor del campo a guardar.
     */
    public static void setUrlLogout(String urlLogout) {
        LogoutLdapServlet.urlLogout = urlLogout;
    }

}