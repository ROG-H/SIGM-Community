<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.datosInteresado" select="'Datos del interesado'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	
	<xsl:variable name="lang.nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre y Apellidos'"/>

	<xsl:variable name="lang.asunto" select="'Expediente'"/>
	<xsl:variable name="lang.numexp" select="'N�mero de expediente'"/>

	<xsl:variable name="lang.idSolicitud" select="'Id Licencia'"/>
	<xsl:variable name="lang.codigoPeticion" select="'C�digo Licencia'"/>
	<xsl:variable name="lang.tipoPeticion" select="'Tipo Licencia'"/>
	<xsl:variable name="lang.anio" select="'Correspondientes al a�o'"/>
	<xsl:variable name="lang.fechaInicio" select="'Fecha Inicio'"/>
	<xsl:variable name="lang.fechaFin" select="'Fecha Fin'"/>	

	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	<xsl:variable name="lang.expone" select="'Expone'"/>		
	
		
	<xsl:template match="/">
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosInteresado"/></b>	
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
			</label>
			<br/>
		</div>
		<br/>
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosSolicitud"/></b>	
			</label>
			<br/>
		</div>
		<br/>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numExpediente">
			<div class="col">
				<label class="gr" style="position: relative; width:200px;">
					<xsl:value-of select="$lang.numexp"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:450px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numExpediente"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Id_Solicitud">
			<div class="col">
				<label class="gr" style="position: relative; width:200px;">
					<xsl:value-of select="$lang.idSolicitud"/>:	
				</label>
				<label class="gr" style="position: relative; width:450px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Id_Solicitud"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Tipo_Peticion">
			<div class="col">
				<label class="gr" style="position: relative; width:200px;">
					<xsl:value-of select="$lang.tipoPeticion"/>:
				</label>
				<label class="gr" style="position: relative; width:450px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Tipo_Peticion"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Fecha_Inicio">
			<div class="col">
				<label class="gr" style="position: relative; width:200px;">
					<xsl:value-of select="$lang.fechaInicio"/>:
				</label>
				<label class="gr" style="position: relative; width:450px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Fecha_Inicio"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Fecha_Fin">
			<div class="col">
				<label class="gr" style="position: relative; width:200px;">
					<xsl:value-of select="$lang.fechaFin"/>:
				</label>
				<label class="gr" style="position: relative; width:450px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Fecha_Fin"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/expone">
			<div class="col">
				<label class="gr" style="position: relative; width:200px;">
					<xsl:value-of select="$lang.expone"/>:	
				</label>
				<label class="gr" style="position: relative; width:450px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/expone"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/solicita">
			<div class="col">
				<label class="gr" style="position: relative; width:200px;">
					<xsl:value-of select="$lang.solicita"/>:	
				</label>
				<label class="gr" style="position: relative; width:450px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/solicita"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<br/>
		<div style="color: grey; text-align:justify">
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
				Los datos personales, identificativos y de contacto, aportados mediante esta comunicaci�n se entienden facilitados voluntariamente, y ser�n incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del �mbito de las competencias de esta Administraci�n P�blica as� como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telem�ticos y enviarle invitaciones para eventos y felicitaciones en fechas se�aladas. Entenderemos que presta su consentimiento t�cito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podr� ejercer sus derechos de acceso, rectificaci�n, cancelaci�n y oposici�n ante el Responsable del Fichero, la Diputaci�n Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - Espa�a, siempre acreditando conforme a Derecho su identidad en la comunicaci�n. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigi�ndose a la direcci�n citada ut supra o bien al correo electr�nico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.	
			</label>
		</div>
	</xsl:template>
</xsl:stylesheet>