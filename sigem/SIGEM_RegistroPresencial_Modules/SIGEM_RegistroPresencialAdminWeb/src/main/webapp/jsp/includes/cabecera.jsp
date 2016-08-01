<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="ieci.tecdoc.sgm.core.web.locale.LocaleFilterHelper"%>

<!-- [Manu #814] INICIO - SIGEM Administraci�n - Poner nombre entidad en la que estamos en el Cat�logo de Procedimientos. -->

<%@page import="ieci.tecdoc.sgm.core.admin.web.AutenticacionAdministracion" %>
<%@page import="ieci.tecdoc.sgm.core.services.LocalizadorServicios" %>

<% 

	String entidad = AutenticacionAdministracion.obtenerDatosEntidad(request).getIdEntidad();
	String descEntidad = LocalizadorServicios.getServicioEntidades().obtenerEntidad(entidad).getNombreCorto();
%>

<!-- [Manu #814] FIN- SIGEM Administraci�n - Poner nombre entidad en la que estamos en el Cat�logo de Procedimientos. -->

<script>
	function abreAyuda( url ) {
	
		var features = "directories=0,top=50px,left=50px,height=520px,width=930px,location=0,menubar=0";
		features +="resizable=0,scrollbars=0,status=no,titlebar=no,toolbar=no";

		window.open(url, "ventanaAyuda", features);
	}
</script>

<div id="cabecera">
	<div id="logo">
		<img src="<html:rewrite page="/img/minetur.jpg"/>" alt="GOBIERNO DE ESPA�A. MINISTERIO DE INDUSTRIA, ENERG͍A Y TURISMO " />
		<img src="<html:rewrite page="/img/logo.gif"/>" alt="sigem" />
	</div>
	<div class="salir">
		<img src="<html:rewrite page="/img/exit.gif"/>" alt="salir" width="26" height="20" class="icono" />
		<span class="titular"><a href='<html:rewrite page="/logout.do" />'><bean:message key="ieci.tecdoc.sgm.rpadmin.logout"/></a></span>
	</div>
</div>

<div class="usuario">
    <div class="usuarioleft">
    	<p> <bean:message key="ieci.tecdoc.sgm.rpadmin.titulo"/> </p>
    </div>
    <div class="usuarioright">
 		<table>
	   		<tr>
				<td>
				   	<!-- [Manu #814] INICIO - SIGEM Administraci�n - Poner nombre entidad en la que estamos en el Cat�logo de Procedimientos. -->
				   	<p> Entidad: <%=descEntidad%>&nbsp;-&nbsp; <bean:write name="datosSesion" property="usuario"/>	</p>
				<!-- [Manu #814] FIN - SIGEM Administraci�n - Poner nombre entidad en la que estamos en el Cat�logo de Procedimientos. -->
				</td>	
				<td>
					<p>
					  <% String ayuda = "/ayuda/" + LocaleFilterHelper.getCurrentLocale(request).getLanguage() + "/index.html";%>
				      	<img src="<html:rewrite page="/img/help.gif"/>" border="0" alt="sigem" onclick="abreAyuda('<html:rewrite page="<%=ayuda%>" />')" style="cursor:hand"/>
				     </p>
				</td>
			</tr>
		</table>
    </div>
</div><br>