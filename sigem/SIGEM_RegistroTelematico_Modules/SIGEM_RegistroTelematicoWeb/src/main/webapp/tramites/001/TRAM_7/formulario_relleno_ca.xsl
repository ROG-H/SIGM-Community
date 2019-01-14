<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_relleno.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'CAT-Formulario de Expediente Sancionador'"/>
	<xsl:variable name="lang.docIdentidad" select="'CAT-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'CAT-Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'CAT-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'CAT-Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'CAT-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'CAT-Provincia'"/>
	<xsl:variable name="lang.cp" select="'CAT-C�digo postal'"/>
	<xsl:variable name="lang.telefono" select="'CAT-Tel�fono'"/>
	<xsl:variable name="lang.envio" select="'CAT-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'CAT-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'CAT-D.E.U.'"/>
	<xsl:variable name="lang.datosDenunciante" select="'CAT-Datos del Denunciante'"/>
	<xsl:variable name="lang.datosDenunciado" select="'CAT-Datos del Denunciado'"/>
	<xsl:variable name="lang.datosDenunciados" select="'CAT-Hechos Denunciados'"/>
	<xsl:variable name="lang.domicilioDenunciado" select="'CAT-Domicilio'"/>
	<xsl:variable name="lang.datosSolicitud" select="'CAT-Datos de la Solicitud'"/>
	
</xsl:stylesheet>