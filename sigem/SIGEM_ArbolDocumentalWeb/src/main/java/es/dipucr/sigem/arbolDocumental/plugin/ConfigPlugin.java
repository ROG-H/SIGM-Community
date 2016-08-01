package es.dipucr.sigem.arbolDocumental.plugin;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;

import es.dipucr.sigem.arbolDocumental.utils.Defs;


public class ConfigPlugin extends BasePlugin
{

  private static Logger logger          = Logger.getLogger(ConfigPlugin.class);

  /*
   * Atributos por defecto del plugin.
   */
  //private ModuleConfig  m_config        = null;
  private ActionServlet m_servlet       = null;

  /*
   * Atributos espec�ficos de la aplicaci�n
   */
  private String m_redirAutenticacion = null;

  /*
   * M�todos
   */
  public String getRedirAutenticacion() {
    return m_redirAutenticacion;
  }

  public void setRedirAutenticacion(String redirAutenticacion) {
    m_redirAutenticacion = redirAutenticacion;
  } 
  

/*
   * (non-Javadoc)
   *
   * @see org.apache.struts.action.PlugIn#destroy()
   */
  public void destroy()
  {
    m_servlet.getServletContext().removeAttribute(Defs.PLUGIN_REDIRAUTENTICACION);
    
    m_servlet = null;
    //m_config = null;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet,
   *      org.apache.struts.config.ModuleConfig)
   */
  public void init(ActionServlet servlet, ModuleConfig config)
        throws ServletException
  {

    //this.m_config = config;
    this.m_servlet = servlet;

    //cogemos la configuracion de la externalizacion
    populate();

    m_servlet.getServletContext().setAttribute(Defs.PLUGIN_REDIRAUTENTICACION, this.m_redirAutenticacion);
    
    if (logger.isDebugEnabled())
    {
      logger.debug("Defs.PLUGIN_REDIRAUTENTICACION: " + this.m_redirAutenticacion);
    }
  }

  protected void populate()
  {
	  ConfigLoader configloader = new ConfigLoader();
	  this.m_redirAutenticacion=configloader.getRedirAutenticacionValue();
  }
}