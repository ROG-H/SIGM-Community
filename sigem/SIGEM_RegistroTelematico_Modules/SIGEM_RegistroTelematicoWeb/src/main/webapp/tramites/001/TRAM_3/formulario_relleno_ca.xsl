<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_relleno.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.docIdentidad" select="'CAT-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'CAT-Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'CAT-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'CAT-Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'CAT-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'CAT-Provincia'"/>
	<xsl:variable name="lang.cp" select="'CAT-C�digo postal'"/>
	<xsl:variable name="lang.tipoSubvencion" select="'CAT-Tipo de Subvenci�n'"/>
	<xsl:variable name="lang.resumenProyecto" select="'CAT-Resumen del Proyecto'"/>
	<xsl:variable name="lang.telefono" select="'CAT-Tel�fono'"/>
	<xsl:variable name="lang.asunto" select="'CAT-Asunto'"/>
	<xsl:variable name="lang.orgDestino" select="'CAT-�rgano destino'"/>
	<xsl:variable name="lang.idioma" select="'CAT-Idioma de Presentaci�n'"/>
</xsl:stylesheet>