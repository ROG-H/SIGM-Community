<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="java.util.Locale"%>
<%@page import="ieci.tecdoc.sgm.core.services.idioma.Idioma"%>
<%@page import="ieci.tecdoc.sgm.consulta_telematico.utils.Defs"%>
<%@page import="ieci.tecdoc.sgm.core.services.telematico.ConstantesGestionTelematicoCiudadano"%>

<%
String rutaEstilos = (String)session.getAttribute("PARAMETRO_RUTA_ESTILOS");
if (rutaEstilos == null) rutaEstilos = "";
String rutaImagenes = (String)session.getAttribute("PARAMETRO_RUTA_IMAGENES");
if (rutaImagenes == null) rutaImagenes = "";

String entidadId = (String)request.getParameter("ENTIDAD_ID");
if (entidadId == null || entidadId.equals(""))
	entidadId = (String)request.getSession().getAttribute("ENTIDAD_ID");
session.setAttribute(ConstantesGestionTelematicoCiudadano.PARAMETRO_ID_ENTIDAD, entidadId);
%>

	<div id="cabecera">
		<h1>
			<img src="img/minetur.jpg" alt="GOBIERNO DE ESPAÑA. MINISTERIO DE INDUSTRIA, ENERGÍA Y TURISMO " />
		<!-- [eCenpri-Manu Ticket#267] + ALSIGM3 Logotipo de la entidad sale feas en registro presencial y tramitador -->
			<!-- <img src="<%=request.getContextPath()%>/resourceServlet/logos/logo.gif" alt="SIgem" /> -->
			<img src="<%=request.getContextPath()%>/resourceServlet/logos/logo.gif" alt="SIGM" style="width: auto; max-width: 200px; height: 59px; max-height: 200px;" />
		<!-- [eCenpri-Manu Ticket#267] + ALSIGM3 Logotipo de la entidad sale feas en registro presencial y tramitador -->
		</h1>
		<h3>&nbsp;</h3>
		<p class="salir"><a href="<html:rewrite page="/desconectar.do"/>"><bean:message key="mensaje.salir"/></a></p>
	</div>

	<div id="usuario">
		<div id="barra_usuario">
			<h3><bean:message key="mensaje.usuario"/> <%=(String)session.getAttribute(Defs.NOMBRE_USUARIO)%></h3>
			<p class="ayuda">

				<%
					Locale locale = (Locale)request.getSession().getAttribute("org.apache.struts.action.LOCALE");
					String strIdioma = locale.getLanguage() + "_" + locale.getCountry();
				%>

				<logic:present name="<%=Defs.IDIOMAS_DISPONIBLES%>">
					<bean:define id="idiomasDesplegable" type="java.util.ArrayList" name="<%=Defs.IDIOMAS_DISPONIBLES%>" />

					<span class="idioma">
						<select id="selIdioma">
						<%
						for(int indIdioma = 	0; indIdioma<idiomasDesplegable.size(); indIdioma++){
							Idioma objIdioma = (Idioma)idiomasDesplegable.get(indIdioma);
						%>
							<option value="<%=objIdioma.getCodigo()%>" <%=(objIdioma.getCodigo().equals(strIdioma) ? "selected" : "")%>><%=objIdioma.getDescripcion()%></option>
						<%}%>
						</select>

						<a href="javascript:cambiarIdioma();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
						<iframe src="blank.html" id="recargarIdioma" title='<bean:message key="mensaje.cambiarIdioma"/>' style="top: 0px; left: 0px; visibility:hidden; position:absolute">
						</iframe>
					</span>
				</logic:present>

				<a href="#">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
			</p>
		</div>
	</div>

