<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.datosInteresado" select="'Datos del interesado'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.anio" select="'A�o de inicio del expediente'"/>

	<xsl:variable name="lang.convocatoria" select="'Expediente Seleccionado'"/>
	<xsl:variable name="lang.convocatoriaObligatoria" select="'Convocatorias'"/>

	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>

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
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.anio"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/anio"/>
			</label>
			<br/>
		</div>
		<br/>
		<div class="col">
			<label class="gr" style="position: relative; width:200px;font-weight: bold;">
				<xsl:value-of select="$lang.convocatoria"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:450px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/asunto"/>	
			</label>
			<br/>
		</div>	

		<div class="col">
			<label class="gr" style="position: relative; width:640px;text-align:justify;">
				<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/expone">
					<div style="position: relative; width:640px;text-align:justify;">
						<label class="gr" style="position: relative; width:150px;font-weight: bold;">
							<xsl:value-of select="$lang.expone"/>:	
						</label>
						<label class="gr" style="position: relative; width:640px;">
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/expone"/>
						</label>
						<br/>
					</div>
				</xsl:if>
				<br/>
				<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/solicita">
					<div class="col">
						<label class="gr" style="position: relative; width:150px;font-weight: bold;">
							<xsl:value-of select="$lang.solicita"/>:	
						</label>
						<label class="gr" style="position: relative; width:640px;">
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/solicita"/>
						</label>
						<br/>
					</div>
				</xsl:if>				
				<br/>			
			</label>
			<br/>
		</div>
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_AUTOFIRMA_COMUN_RELLENO" />
	</xsl:template>
</xsl:stylesheet>