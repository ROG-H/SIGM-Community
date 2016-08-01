<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:include href="templates_comunes_rellenos.xsl" />	
	<xsl:template match="/">
		<xsl:call-template name="DATOS_SOLICITANTE" />
		<xsl:call-template name="DATOS_BENEFICIARIO" />
		<xsl:call-template name="DATOS_FAMILIA" />
		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
			<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
				<xsl:call-template name="DATOS_PROPUESTA_1" />
			</xsl:if>
		</xsl:if>
		<!--<xsl:call-template name="DATOS_FAMILIA2" />-->
		<br/>
		<div style="color: grey; text-align:justify">
			<!--label class="gr">
				<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
				Los datos personales, identificativos y de contacto, aportados mediante esta comunicaci�n se entienden facilitados voluntariamente, y ser�n incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del �mbito de las competencias de esta Administraci�n P�blica as� como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telem�ticos y enviarle invitaciones para eventos y felicitaciones en fechas se�aladas. Entenderemos que presta su consentimiento t�cito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podr� ejercer sus derechos de acceso, rectificaci�n, cancelaci�n y oposici�n ante el Responsable del Fichero, la Diputaci�n Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - Espa�a, siempre acreditando conforme a Derecho su identidad en la comunicaci�n. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigi�ndose a la direcci�n citada ut supra o bien al correo electr�nico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.	
			</label-->

			<label class="gr">
				<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
				<br/>
				AVISO LEGAL:
				<br/><br/>
				En cumplimiento de la Ley Org�nica 15/1999, de 13 de diciembre, de Protecci�n de Datos de Car�cter Personal, le informamos que sus datos ser�n incorporados a un fichero con datos de car�cter personal cuya finalidad es la gesti�n de las solicitudes realizadas por ciudadanos/as a la Diputaci�n Provincial de Ciudad Real. Le informamos as� mismo que los datos podr�n ser comunicados a otras Administraciones P�blicas en el �mbito de competencias semejantes o materias comunes, en cumplimiento de la legislaci�n aplicable. El/a interesado/a declara estar informado/a de las condiciones detalladas en la presente cl�usula, se compromete a mantener actualizados sus datos y, en cualquier caso, podr� ejercitar gratuitamente los derechos de acceso, rectificaci�n, cancelaci�n y oposici�n (siempre de acuerdo con los supuestos contemplados por la legislaci�n vigente) dirigi�ndose a la Diputaci�n Provincial de Ciudad Real en C/ Toledo, 17. 13071-Ciudad Real- Espa�a, siempre acreditando conforme a Derecho su identidad en la comunicaci�n. 
			</label>
		</div>


		<div class="col">
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:600px;</xsl:attribute>
				<b>Documentaci�n que se aporta: </b>
			</label>
			<label class="gr" style="position: relative; width:600px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/documentos"/>				
			</label>
		</div>
	</xsl:template>
</xsl:stylesheet>
