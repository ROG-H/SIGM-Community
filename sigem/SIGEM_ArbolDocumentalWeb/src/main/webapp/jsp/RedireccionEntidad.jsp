<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>


<%
String rutaEstilos = (String)session.getAttribute("PARAMETRO_RUTA_ESTILOS");
if (rutaEstilos == null) rutaEstilos = "";
String rutaImagenes = (String)session.getAttribute("PARAMETRO_RUTA_IMAGENES");
if (rutaImagenes == null) rutaImagenes = "";
%>

<html:html locale="true">
<head>
	<%
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%> 
		<base href="<%= basePath %>" />
		<link rel="stylesheet" href="css/<%=rutaEstilos%>estilos.css" type="text/css" />
	<%
		String url_parcial = (String)request.getSession().getServletContext().getAttribute("redirAutenticacion");
		String dir = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + url_parcial;
	%>
    <META HTTP-EQUIV="Refresh" CONTENT="0;URL=<%=dir%>">
</head>

<body>
<p><bean:message key="cargando"/></p>
</body>

</html:html>