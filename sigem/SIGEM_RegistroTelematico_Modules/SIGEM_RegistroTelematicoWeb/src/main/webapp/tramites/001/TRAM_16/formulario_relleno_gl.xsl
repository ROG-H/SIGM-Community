<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_relleno.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.datosContratante" select="'GAL-Datos del Contratante'"/>
	<xsl:variable name="lang.datosContratado" select="'GAL-Datos del Contratado'"/>
	<xsl:variable name="lang.docIdentidad" select="'GAL-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'GAL-Nombre'"/>
	<xsl:variable name="lang.relacion" select="'GAL-Relaci�n'"/>
	<xsl:variable name="lang.domicilio" select="'GAL-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'GAL-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'GAL-Provincia'"/>
	<xsl:variable name="lang.cp" select="'GAL-C�digo postal'"/>
	<xsl:variable name="lang.datosContrato" select="'GAL-Datos del Contrato'"/>
	<xsl:variable name="lang.tipoContrato" select="'GAL-Tipo de Contrato'"/>
	
	<xsl:variable name="lang.formaAdjudicacion" select="'GAL-Forma de Adjudicaci�n'"/>
	
	<xsl:variable name="lang.presupuestoMax" select="'GAL-Presupuesto m�ximo'"/>
	<xsl:variable name="lang.precioAdjudicacion" select="'GAL-Precio de la Adjudicaci�n'"/>
	<xsl:variable name="lang.aplicacion" select="'GAL-Aplicaci�n presupuestaria'"/>
	<xsl:variable name="lang.plazo" select="'GAL-Plazo de Ejecuci�n'"/>
	<xsl:variable name="lang.unidades" select="'GAL-Unidades de Plazo'"/>
	<xsl:variable name="lang.garantiaProv" select="'GAL-Garant�a Provisional'"/>
	<xsl:variable name="lang.garantiaDef" select="'GAL-Garant�a Definitiva'"/>
	<xsl:variable name="lang.clasificacion" select="'GAL-Clasificaci�n'"/>
	<xsl:variable name="lang.fechaFinEjecucion" select="'GAL-Fecha final de Ejecuci�n'"/>
	<xsl:variable name="lang.fechaFinGarantia" select="'GAL-Fecha final de Garant�a'"/>
	
	<xsl:variable name="lang.telefono" select="'GAL-Tel�fono'"/>
	<xsl:variable name="lang.email" select="'GAL-Correo electr�nico'"/>
	<xsl:variable name="lang.unidades" select="'GAL-Unidades de Plazo'"/>
</xsl:stylesheet>