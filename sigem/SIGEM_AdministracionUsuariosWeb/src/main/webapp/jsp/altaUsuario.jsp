<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="es" xml:lang="es">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="author" content="IECISA" />
<title>sigem</title>
<link href="<html:rewrite page="/css/estilos.css"/>" rel="stylesheet" type="text/css" />
<html:javascript formName="/crearUsuario" method="validateCrearUsuario" dynamicJavascript="true" staticJavascript="false"/>
<script src="<html:rewrite page="/js/validator-rules.js"/>"></script>
</head>
<body>
<script language="javascript">
function guardar(formulario){
	var url = '<html:rewrite page="/crearUsuario.do"/>';
    if(validateCrearUsuario(formulario)){
        	if(confirm("<bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.message3"/>")){
				formulario.action = url;        	
				//formulario.submit();
				return true;
			} else {
			 return false;
			}
	} else {
	 return false;
	}
}

function salir(){
	var url = '<html:rewrite page="/buscarUsuarios.do"/>';
	var formulario = document.getElementById("formulario");
    formulario.action = url;
	formulario.submit();
	return;
}

</script>
<div id="contenedora">
  <!-- Inicio Cabecera -->
  
<!-- [Manu #814] INICIO - SIGEM Administraci�n - Poner nombre entidad en la que estamos en el Cat�logo de Procedimientos. -->

<%@page import="ieci.tecdoc.sgm.core.admin.web.AutenticacionAdministracion" %>
<%@page import="ieci.tecdoc.sgm.core.services.LocalizadorServicios" %>

<% 
	String entidad = AutenticacionAdministracion.obtenerDatosEntidad(request).getIdEntidad();
	String descEntidad = LocalizadorServicios.getServicioEntidades().obtenerEntidad(entidad).getNombreCorto();
%>

<!-- [Manu #814] FIN- SIGEM Administraci�n - Poner nombre entidad en la que estamos en el Cat�logo de Procedimientos. -->
  
  <div id="cabecera">
    <div id="logo">
    	<img src="img/minetur.jpg" alt="GOBIERNO DE ESPA�A. MINISTERIO DE INDUSTRIA, ENERG�A Y TURISMO " />
		<img src="img/logo.gif" alt="AL SIGM" />
    </div>
    <div class="salir"><img src="img/exit.gif" alt="salir" width="26" height="20" class="icono" /><span class="titular"><a href="<html:rewrite page="/desconectar.do"/>"><bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.exit"/></a></span> </div>
  </div>
  <div class="usuario">
    <div class="usuarioleft">
      <p><bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.title"/></p>
    </div>
    <div class="usuarioright">
	    <!-- [Manu #814] INICIO - SIGEM Administraci�n - Poner nombre entidad en la que estamos en el Cat�logo de Procedimientos. -->
		  <p> Entidad: <%=descEntidad%></p>
		<!-- [Manu #814] FIN - SIGEM Administraci�n - Poner nombre entidad en la que estamos en el Cat�logo de Procedimientos. -->
    </div>
  </div>
  <div id="migas">
  <img src="img/flecha_migas.gif" width="13" height="9" class="margen"/>
  <bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.create.title"/>
  </div>
  <!-- Fin Cabecera -->
  <!-- Inicio Contenido -->
  <div id="contenido">
    <div class="cuerpo">
      <div class="cuerporight">
        <div class="cuerpomid">
                    <div class="cuadro">
                    <table width="100%">
						<html:form action="/crearUsuario.do" styleId="formulario">                    
                    	<tr>
	                    	<td width="10%">&nbsp;
	                    	</td>
	                    	<td width="20%" style="text-align:right;">
	                    	&nbsp;
	                    	</td>
	                    	<td width="20%" style="text-align:left;">
							<p class="textoRojo"><html:errors/></p>
	                    	</td>
	                    	<td width="50%">&nbsp;
	                    	</td>                    	
                    	</tr>
                    	<tr><td width="10%">&nbsp;</td>
	                    	<td width="20%" style="text-align:right;">
	                    	<bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.usuario.usuario"/>
	                    	</td>
	                    	<td width="20%" style="text-align:left;">
	                    	<html:text property="user" maxlength="15"/>
	                    	</td><td width="50%">&nbsp;</td>                    	
                    	</tr>
                    	<tr><td width="10%">&nbsp;</td>
	                    	<td width="20%" style="text-align:right;">
	                    	<bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.usuario.pass"/>
	                    	</td>
	                    	<td width="20%" style="text-align:left;">
	                    	<html:password property="password" maxlength="15"/>
	                    	</td><td width="50%">&nbsp;</td>                    	
                    	</tr>
                    	<tr><td width="10%">&nbsp;</td>
	                    	<td width="20%" style="text-align:right;">
	                    	<bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.usuario.pass2"/>
	                    	</td>
	                    	<td width="20%" style="text-align:left;">
	                    	<html:password property="password2" maxlength="15"/>
	                    	</td><td width="50%">&nbsp;</td>                    	
                    	</tr>                    	
                    	<tr>
	                    	<td width="10%">&nbsp;
	                    	</td>
	                    	<td width="20%" style="text-align:right;">
	                    	<bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.usuario.id"/>
	                    	</td>
	                    	<td width="20%" style="text-align:left;">
	                    	<html:text property="id" maxlength="9"/>
	                    	</td>
	                    	<td width="50%">&nbsp;
	                    	</td>                    	
                    	</tr>                    	
                    	<tr><td width="10%">&nbsp;</td>
	                    	<td width="20%" style="text-align:right;">
	                    	<bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.usuario.nombre"/>
	                    	</td>
	                    	<td width="20%" style="text-align:left;">
	                    	<html:text property="name" maxlength="50"/>
	                    	</td><td width="50%">&nbsp;</td>                    	
                    	</tr>                    	
                    	<tr><td width="10%">&nbsp;</td>
	                    	<td width="20%" style="text-align:right;">
	                    	<bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.usuario.apellidos"/>
	                    	</td>
	                    	<td width="20%" style="text-align:left;">
	                    	<html:text property="lastname" maxlength="50"/>
	                    	</td><td width="50%">&nbsp;</td>                    	
                    	</tr>                    	                    	
                    	<tr><td width="10%">&nbsp;</td>
	                    	<td width="20%" style="text-align:right;">
	                    	<bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.usuario.email"/>
	                    	</td>
	                    	<td width="20%" style="text-align:left;">
	                    	<html:text property="email" maxlength="60"/>
	                    	</td><td width="50%">&nbsp;</td>                    	
                    	</tr>                    	                    	
                    	<tr><td width="10%">&nbsp;</td>
	                    	<td width="20%" style="text-align:right;">
	                    	&nbsp;
	                    	</td>
	                    	<td width="20%" style="text-align:left;">
               				<html:submit property="Enviar" styleId="boton" onclick="if(!guardar(this.form)) return false;"><bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.save"/></html:submit>
               				<html:button property="Enviar" styleId="boton" onclick="salir()"><bean:message key="ieci.tecdoc.sgm.autenticacion.admin.struts.exit"/></html:button>
	                    	</td><td width="50%">&nbsp;</td>                    	
                    	</tr>                    	                    	
						</html:form>							                    		
                    </table>
                    </div>
      </div>
    </div>
    <div class="cuerpobt">
      <div class="cuerporightbt">
        <div class="cuerpomidbt"></div>
      </div>
    </div>
  </div>
  <!-- Fin Contenido -->
  <div id="pie"></div>
</div>
</body>
</html>