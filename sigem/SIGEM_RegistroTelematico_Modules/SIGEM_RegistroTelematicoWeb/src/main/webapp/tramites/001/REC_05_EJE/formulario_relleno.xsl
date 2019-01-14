<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.datosIdentificativos" select="'Datos identificativos'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.pres_nif" select="'NIF Presentador'"/>
	<xsl:variable name="lang.pres_nombre" select="'Nombre Presentador'"/>
	<xsl:variable name="lang.repres_nif" select="'NIF/CIF Representante'"/>
	<xsl:variable name="lang.repres_nombre" select="'Nombre Representante'"/>
	<xsl:variable name="lang.repres_movil" select="'N�mero de tel�fono m�vil Representante'"/>
	<xsl:variable name="lang.repres_d_email" select="'Direcci�n de correo electr�nico Representante'"/>
	<xsl:variable name="lang.nif" select="'NIF/CIF Contribuyente'"/>
	<xsl:variable name="lang.nombre" select="'Nombre Contribuyente'"/>
	<xsl:variable name="lang.direccion" select="'Direcci�n'"/>
	<xsl:variable name="lang.calle" select="'Calle'"/>
	<xsl:variable name="lang.numero" select="'Numero'"/>
	<xsl:variable name="lang.escalera" select="'Escalera'"/>
	<xsl:variable name="lang.planta_puerta" select="'Planta/Puerta'"/>
	<xsl:variable name="lang.c_postal" select="'C�digo Postal'"/>
	<xsl:variable name="lang.movil" select="'N�mero de tel�fono m�vil'"/>
	<xsl:variable name="lang.d_email" select="'Direcci�n de correo electr�nico'"/>
	
	<xsl:variable name="lang.motivo" select="'Motivo'"/>
	<xsl:variable name="lang.otro_motivo" select="'Otros motivos'"/>
	<xsl:variable name="lang.municipio" select="'Nombre del municipio'"/>
	<xsl:variable name="lang.liquidacion" select="'N� de liquidaci�n'"/>
	<xsl:variable name="lang.referencia" select="'Referencia catastral'"/>
	<xsl:variable name="lang.situacion" select="'Situaci�n del inmueble'"/>
	<xsl:variable name="lang.years" select="'A�os'"/>
	
	<xsl:variable name="lang.anexos" select="'Anexos'"/>
	
	<xsl:template match="/">
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosIdentificativos"/></b>	
			</label>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.pres_nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.pres_nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
			</label>
			<br/>
		</div>
		<br/>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.repres_nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nif"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.repres_nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nombre"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.repres_movil"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_movil"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.repres_d_email"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_d_email"/>
			</label>
			<br/>
		</div>
		<br/>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nif"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.direccion"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/calle"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numero"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/escalera"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/planta_puerta"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/c_postal"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.movil"/>:
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/movil"/>
			</label>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.d_email"/>:
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/d_email"/>
			</label>
		</div>
		<br/>
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosSolicitud"/></b>	
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.motivo"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_motivo"/>
			</label>
		</div>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/motivo='MOTIVO_5'">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.otro_motivo"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/otro_motivo"/>
				</label>
			</div>
		</xsl:if>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.years"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/years"/>
			</label>
		</div>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">1</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">2</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">3</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">4</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">5</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">6</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">7</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">8</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">9</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">10</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">11</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">12</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">13</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">14</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">15</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">16</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">17</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">18</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">19</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">20</xsl:with-param></xsl:call-template>
		<br/>
		<br/>
	</xsl:template>
	
	<xsl:template name="FIELDS">
	    <xsl:param name="row_id" />
	    <xsl:variable name="param_mun">mun_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_liq">liq_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref1">ref1_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref2">ref2_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref3">ref3_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref4">ref4_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_sit">sit_<xsl:value-of select="$row_id"/></xsl:variable>
		
		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_liq]='')">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.municipio"/>:<br/>
					<xsl:value-of select="$lang.liquidacion"/>:<br/>
					<xsl:value-of select="$lang.referencia"/>:<br/>
					<xsl:value-of select="$lang.situacion"/>:
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_mun]"/><br/>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_liq]"/><br/>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref1]"/> .
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref2]"/> .
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref3]"/> .
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref4]"/><br/>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_sit]"/>
					<br/>
				</label>
			</div>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>
