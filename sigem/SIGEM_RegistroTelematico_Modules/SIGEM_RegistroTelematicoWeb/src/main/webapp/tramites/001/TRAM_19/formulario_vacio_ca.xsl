<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'Formulario de Solicitud con Cita Previa con T�cnicos Municipales'"/>
	<xsl:variable name="lang.datosSolicitante" select="'Datos del Solicitante'"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.cp" select="'C�digo postal'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Citaci�n'"/>
	<xsl:variable name="lang.descripcionObras" select="'Motivo de cita'"/>			
	<xsl:variable name="lang.organoAsignado" select="'Aviso de Notificaciones'"/>
	<xsl:variable name="lang.organoAlternativo" select="'Modo de aviso de la solicitud'"/>
	<xsl:variable name="lang.tl" select="'Telefono'"/>
	<xsl:variable name="lang.sms" select="'SMS'"/>
	<xsl:variable name="lang.email" select="'E_mail'"/>
	<xsl:variable name="lang.envio" select="'Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'D.E.U.'"/>
	<xsl:variable name="lang.telefono" select="'Tel�fono'"/>
</xsl:stylesheet>