<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'EUS-Formulario de Solicitud de Reclamaci�n, Quejas y Sugerencias'"/>
	<xsl:variable name="lang.datosSolicitante" select="'EUS-Datos del Solicitante'"/>
	<xsl:variable name="lang.docIdentidad" select="'EUS-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'EUS-Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'EUS-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'EUS-Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'EUS-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'EUS-Provincia'"/>
	<xsl:variable name="lang.cp" select="'EUS-C�digo postal'"/>
	<xsl:variable name="lang.datosSolicitud" select="'EUS-Datos de la Solicitud'"/>
	<xsl:variable name="lang.tipoSubvencion" select="'EUS-Tipo de Subvenci�n'"/>
	<xsl:variable name="lang.TSinvestigaciones" select="'EUS-Investigaciones'"/>
	<xsl:variable name="lang.TSinnovacionTecnologica" select="'EUS-Innovaci�n Tecnol�gica'"/>
	<xsl:variable name="lang.TSactividadPromocional" select="'EUS-Actidad Promocional'"/>
	<xsl:variable name="lang.TSobrasMenores" select="'EUS-Obras Menores'"/>
	<xsl:variable name="lang.resumenProyecto" select="'EUS-Resumen del Proyecto'"/>
	<xsl:variable name="lang.organoAsignado" select="'EUS-�rgano al que se dirige: Servicio de Tramitaci�n de Licencias'"/>
	<xsl:variable name="lang.organoAlternativo" select="'EUS-�rgano alternativo'"/>
	<xsl:variable name="lang.servRelacionesCiudadano" select="'EUS-Servicio de Relaciones con el Ciudadano'"/>
	<xsl:variable name="lang.servSecretaria" select="'EUS-Servicio de Secretar�a'"/>
	<xsl:variable name="lang.anexar" select="'EUS-Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'EUS-1.- Para adjuntar un fichero (*.pdf), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'EUS-2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento1" select="'EUS-Documento'"/>
	<xsl:variable name="lang.envio" select="'EUS-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'EUS-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'EUS-D.E.U.'"/>
	<xsl:variable name="lang.telefono" select="'EUS-Tel�fono'"/>
</xsl:stylesheet>