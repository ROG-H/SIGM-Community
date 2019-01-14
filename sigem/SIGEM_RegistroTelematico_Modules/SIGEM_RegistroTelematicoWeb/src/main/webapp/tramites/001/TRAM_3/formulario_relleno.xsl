<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.cp" select="'C�digo postal'"/>
	<xsl:variable name="lang.tipoSubvencion" select="'Tipo de Subvenci�n'"/>
	<xsl:variable name="lang.resumenProyecto" select="'Resumen del Proyecto'"/>
	<xsl:variable name="lang.telefono" select="'Tel�fono'"/>
	
	<xsl:variable name="lang.asunto" select="'Asunto'"/>
	<xsl:variable name="lang.orgDestino" select="'�rgano Destino'"/>
	<xsl:variable name="lang.idioma" select="'Idioma de Presentaci�n'"/>
	<xsl:template match="/">

			<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.docIdentidad"/>:	
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
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Telefono">
			<div class="col" style="height: 35px;">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.telefono"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Telefono"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Domicilio_Notificacion">
			<div class="col" style="height: 35px;">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.domicilio"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Domicilio_Notificacion"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.email"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Correo_Electronico"/>
			</label>
			<br/>
		</div>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Localidad">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.localidad"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Localidad"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Provincia">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.provincia"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Provincia"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Codigo_Postal">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.cp"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Codigo_Postal"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		
		
		
			<div class="col">
				<label class="gr"
					style="position: relative; width:150px;">
					<xsl:value-of select="$lang.asunto" />
					:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
					<xsl:value-of
						select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Asunto/Descripcion" />
				</label>
				<br />
			</div>
			<div class="col">
				<label class="gr"
					style="position: relative; width:150px;">
					<xsl:value-of select="$lang.orgDestino" />
					:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
					<xsl:value-of
						select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/descr_organo" />
				</label>
				<br />
			</div>
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Idioma">
			<div class="col">
				<label class="gr"
					style="position: relative; width:150px;">
					<xsl:value-of select="$lang.idioma" />
					:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
					<xsl:value-of
						select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Idioma" />
				</label>
				<br />
			</div>
			</xsl:if>
		
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tipo_subvencion">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.tipoSubvencion"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tipo_subvencion"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/resumen_proyecto">
			<div class="col" style="height: 35px;">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.resumenProyecto"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/resumen_proyecto"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<br/>
	</xsl:template>
</xsl:stylesheet>