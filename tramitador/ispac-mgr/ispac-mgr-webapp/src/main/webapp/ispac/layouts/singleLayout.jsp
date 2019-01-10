<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-menu.tld" prefix="menu" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<?xml version="1.0" encoding="iso-8859-1"?>
<html:html>
  	<head>
	    <title><bean:message key="head.title" /></title>
	    
	    <link rel="shortcut icon" href='<ispac:rewrite href="img/favicon.ico"/>' type="image/x-icon"/>
	  	<link rel="icon" href='<ispac:rewrite href="img/favicon.ico"/>' type="image/x-icon"/>
	  	
	    <link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/styles.css"/>'/>
	    <link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/menus.css"/>'/>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/nicetabs.css"/>'/>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos.css"/>'/>
		
	<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab.css"/>'/>
	
	<!--[if lte IE 5]>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie5.css"/>'/>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab_ie5.css"/>'/>
	<![endif]-->	

	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie6.css"/>'>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab_ie6.css"/>'/>
	<![endif]-->	

	<!--[if gte IE 7]>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie7.css"/>'>
		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_cab_ie7.css"/>'/>
	<![endif]-->
		
	    <script type="text/javascript" src='<ispac:rewrite href="../scripts/menus.js"/>'></script>
	    <script type="text/javascript" src='<ispac:rewrite href="../scripts/sorttable.js"/>'></script>
	    <script type="text/javascript" src='<ispac:rewrite href="../scripts/utilities.js"/>'></script>
	    <ispac:javascriptLanguage/>
	    <script type="text/javascript" src='<ispac:rewrite href="../scripts/numberFormat.js"/>'> </script>
		<script type="text/javascript" src='<ispac:rewrite href="../scripts/utils.js"/>'> </script>
		
		<!-- [dipucr-Felipe-Alberto #510] -->
		<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-1.12.2.min.js"/>'></script>
		<script type="text/javascript">
			var jQ1_12_2 = $.noConflict(true);
		</script>
		<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-1.3.2.min.js"/>'></script>

  		<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-ui-1.7.2.custom.min.js"/>'></script>
 		<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery.alerts.js"/>'></script>
 		
		<!-- [dipucr-Felipe-Alberto #510] -->
		<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery.quicksearch.js"/>'></script>
		
	    <script>
	    	window.name="ParentWindow";
	    </script>
	</head>
  	<body>
  	<%--ojo no mover el ispac:message ha de ser hijo directo del body para lanzar en jalert al recargar sino IE peta por un bug del propio 
  	navegador--%>
		<ispac:message/>
  		<ispac:keepalive />
		<table border="0" width="100%" cellpadding="0" cellspacing="0">
	    	<tr>
	        	<td colspan="2" width="100%">
					<tiles:insert attribute="header" ignore="true"/>
				</td>
	      	</tr>
	      	<tr valign="top">
			
				<td  class="td_desplegado" height="440px" valign="top"  id="td_menu">
					<tiles:insert attribute="menu" ignore="true"/>
				</td>
				<td height="440px">
	          		<table border="0" width="100%" cellpadding="0" cellspacing="0">
	            		<tr valign="top">
	              			<td width="100%" height="100%">
	               				<div id="body" style="visibility:visible;z-index:1;">
									<tiles:insert attribute="body" ignore="true"/>
								</div>
							</td>
	            		</tr>
	          		</table>
	        	</td>
	      	</tr>
    	</table>
		<ispac:layer id="waitInProgress" msgKey="msg.layer.downloadDocument" showCloseLink="true" styleClassMsg="messageShowLayer" />    	
    	<ispac:layer id="waitOperationInProgress" msgKey="msg.layer.operationInProgress" styleClassMsg="messageShowLayer" />

		<ispac:layer/>
    	<ispac:frame/>
    	<ispac:layer id="configLayer"/>
    	<ispac:frame id="configFrame"/>
    	
  	</body>
</html:html>