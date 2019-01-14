<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_relleno.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.datosContratante" select="'CAT-Datos del Contratante'"/>
	<xsl:variable name="lang.datosContratado" select="'CAT-Datos del Contratado'"/>
	<xsl:variable name="lang.docIdentidad" select="'CAT-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'CAT-Nombre'"/>
	<xsl:variable name="lang.relacion" select="'CAT-Relaci�n'"/>
	<xsl:variable name="lang.domicilio" select="'CAT-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'CAT-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'CAT-Provincia'"/>
	<xsl:variable name="lang.cp" select="'CAT-C�digo postal'"/>
	<xsl:variable name="lang.datosContrato" select="'CAT-Datos del Contrato'"/>
	<xsl:variable name="lang.tipoContrato" select="'CAT-Tipo de Contrato'"/>
	
	<xsl:variable name="lang.formaAdjudicacion" select="'CAT-Forma de Adjudicaci�n'"/>
	
	<xsl:variable name="lang.presupuestoMax" select="'CAT-Presupuesto m�ximo'"/>
	<xsl:variable name="lang.precioAdjudicacion" select="'CAT-Precio de la Adjudicaci�n'"/>
	<xsl:variable name="lang.aplicacion" select="'CAT-Aplicaci�n presupuestaria'"/>
	<xsl:variable name="lang.plazo" select="'CAT-Plazo de Ejecuci�n'"/>
	<xsl:variable name="lang.unidades" select="'CAT-Unidades de Plazo'"/>
	<xsl:variable name="lang.garantiaProv" select="'CAT-Garant�a Provisional'"/>
	<xsl:variable name="lang.garantiaDef" select="'CAT-Garant�a Definitiva'"/>
	<xsl:variable name="lang.clasificacion" select="'CAT-Clasificaci�n'"/>
	<xsl:variable name="lang.fechaFinEjecucion" select="'CAT-Fecha final de Ejecuci�n'"/>
	<xsl:variable name="lang.fechaFinGarantia" select="'CAT-Fecha final de Garant�a'"/>
	
	<xsl:variable name="lang.telefono" select="'CAT-Tel�fono'"/>
	<xsl:variable name="lang.email" select="'CAT-Correo electr�nico'"/>	
	<xsl:variable name="lang.unidades" select="'CAT-Unidades de Plazo'"/>
</xsl:stylesheet>