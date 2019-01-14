<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_relleno.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.datosContratante" select="'EUS-Datos del Contratante'"/>
	<xsl:variable name="lang.datosContratado" select="'EUS-Datos del Contratado'"/>
	<xsl:variable name="lang.docIdentidad" select="'EUS-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'EUS-Nombre'"/>
	<xsl:variable name="lang.relacion" select="'EUS-Relaci�n'"/>
	<xsl:variable name="lang.domicilio" select="'EUS-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'EUS-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'EUS-Provincia'"/>
	<xsl:variable name="lang.cp" select="'EUS-C�digo postal'"/>
	<xsl:variable name="lang.datosContrato" select="'EUS-Datos del Contrato'"/>
	<xsl:variable name="lang.tipoContrato" select="'EUS-Tipo de Contrato'"/>
	
	<xsl:variable name="lang.formaAdjudicacion" select="'EUS-Forma de Adjudicaci�n'"/>
	
	<xsl:variable name="lang.presupuestoMax" select="'EUS-Presupuesto m�ximo'"/>
	<xsl:variable name="lang.precioAdjudicacion" select="'EUS-Precio de la Adjudicaci�n'"/>
	<xsl:variable name="lang.aplicacion" select="'EUS-Aplicaci�n presupuestaria'"/>
	<xsl:variable name="lang.plazo" select="'EUS-Plazo de Ejecuci�n'"/>
	<xsl:variable name="lang.unidades" select="'EUS-Unidades de Plazo'"/>
	<xsl:variable name="lang.garantiaProv" select="'EUS-Garant�a Provisional'"/>
	<xsl:variable name="lang.garantiaDef" select="'EUS-Garant�a Definitiva'"/>
	<xsl:variable name="lang.clasificacion" select="'EUS-Clasificaci�n'"/>
	<xsl:variable name="lang.fechaFinEjecucion" select="'EUS-Fecha final de Ejecuci�n'"/>
	<xsl:variable name="lang.fechaFinGarantia" select="'EUS-Fecha final de Garant�a'"/>
	
	<xsl:variable name="lang.telefono" select="'EUS-Tel�fono'"/>
	<xsl:variable name="lang.email" select="'EUS-Correo electr�nico'"/>
	<xsl:variable name="lang.unidades" select="'EUS-Unidades de Plazo'"/>
</xsl:stylesheet>