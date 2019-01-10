<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<script type="text/javascript"> 
//<!--

	//	Confirma la salida de la aplicaci�n
	function exit() {
		jConfirm('<bean:message key="catalog.exit.msg"/>', '<bean:message key="common.confirm"/>', '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>',function(resultado) {
			if(resultado){
				window.location.href = '<c:url value="/exit.do"/>';
			}
		});
	}
	        
//-->
</script>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr style="width:100%;">
	    <td width="100%">
		<div id="cabecera_int_left">
			<!-- [eCenpri-Manu Ticket#267] + ALSIGM3 Logotipo de la entidad sale feas en registro presencial y tramitador -->
			<!-- <h1><bean:message key="main.company"/></h1>-->
			<img id="img_cab" src="/SIGEM_CatalogoProcedimientosWeb/resourceServlet/logos/logo.gif"/>

			<!-- [eCenpri-Manu Ticket#267] + ALSIGM3 Logotipo de la entidad sale feas en registro presencial y tramitador -->
		</div>
		<div id="cabecera_int_right">
			<h3><bean:message key="main.productName"/></h3>		
			<p class="salir"><a onclick="javascript:exit();return false;" href='#'><bean:message key="menu.exit"/></a></p>
		</div>
	    </td>
	</tr>
</table>
