<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_relleno.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.docIdentidad" select="'Dni'"/>
	<xsl:variable name="lang.nombre" select="'Nome'"/>
	<xsl:variable name="lang.domicilio" select="'Direcci�n para notificaci�n'"/>
	<xsl:variable name="lang.email" select="'E-mail'"/>
	<xsl:variable name="lang.localidad" select="'Local'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.cp" select="'C�digo postal'"/>
	<xsl:variable name="lang.telefono" select="'Tel�fono'"/>
	<xsl:variable name="lang.beneficioFiscal" select="'Beneficio Fiscal'"/>
	<xsl:variable name="lang.impuesto" select="'Imposto'"/>
	<xsl:variable name="lang.hechos" select="'Feitos'"/>
</xsl:stylesheet>