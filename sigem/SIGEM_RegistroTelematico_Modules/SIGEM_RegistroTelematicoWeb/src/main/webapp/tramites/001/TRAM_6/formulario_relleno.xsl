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
	<xsl:variable name="lang.telefono" select="'Tel�fono'"/>
	
	<xsl:variable name="lang.situacion" select="'Situaci�n'"/>
		<xsl:variable name="lang.contenido" select="'Contenido'"/>
		<xsl:variable name="lang.duracion" select="'Duraci�n'"/>
	   <xsl:variable name="lang.fechas" select="'Fechas que se solicitan: Fecha de inicio y fecha fin de ocupaci�n'"/>
		<xsl:variable name="lang.metros" select="'Extensi�n a ocupar'"/>
		<xsl:variable name="lang.periodo" select="'Periodo'"/>	
		<xsl:variable name="lang.otros" select="'Otros'"/>

	<xsl:variable name="lang.ocupacion" select="'La ocupaci�n se realizara por:'"/>
		<xsl:variable name="lang.materiales" select="'Materiales de construcci�n (gruas, andamios,...)'"/>
		<xsl:variable name="lang.materialesVarios" select="'Barracas, casetas de venta, atracciones,...'"/>
		<xsl:variable name="lang.instalaciones" select="'Instalaci�n de rejas de pisos, lucernarios o accesos de personas o art�culos a sotanos o a semisotanos'"/>
		<xsl:variable name="lang.conducciones" select="'Conducciones de energia el�ctrica, agua, gas o an�logos'"/>	
		<xsl:variable name="lang.analogos" select="'Mesas, sillas y an�logos con finalidad lucrativa'"/>
 		<xsl:variable name="lang.instalacionesVarias" select="'Instalaci�n de quioscos de prensa, golosinas y an�logos'"/>
		<xsl:variable name="lang.puestos" select="'Puestos de mercadillo'"/>
		<xsl:variable name="lang.anuncios" select="'Anuncios'"/>
		<xsl:variable name="lang.otrosa" select="'Otros'"/>
		<xsl:variable name="lang.indicarotrosa" select="'Indicar'"/>
	
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
			<div class="col" >
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
		
		
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Situacion">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.situacion"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Situacion"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Contenido">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.contenido"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Contenido"/>
				</label>
				<br/>
			</div>
		</xsl:if>		
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Duracion">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.duracion"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Duracion"/>
				</label>
				<br/>
			</div>
		</xsl:if>		
		
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.ocupacion"/>:	
				</label>
				
				<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Materiales = 'Si'" >
				<br />
						<label class="gr"
							style="position: relative; width:150px;">
							<xsl:value-of select="$lang.materiales" />	
						</label>
						<br />
				</xsl:if>
				<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Materiales_Varios = 'Si'" >
						<br />
						<label class="gr"
							style="position: relative; width:150px;">
							<xsl:value-of select="$lang.materialesVarios" />	
						</label>
						<br />
				</xsl:if>
				<br/>
			</div>
		
		<br/>
	</xsl:template>
</xsl:stylesheet>