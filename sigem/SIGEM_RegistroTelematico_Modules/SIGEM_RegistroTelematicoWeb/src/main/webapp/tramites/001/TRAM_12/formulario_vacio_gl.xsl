<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'GAL-Formulario de solicitud de Licencia de vado'"/>
	<xsl:variable name="lang.datosSolicitante" select="'GAL-Datos del Solicitante'"/>
	<xsl:variable name="lang.docIdentidad" select="'GAL-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'GAL-Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'GAL-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'GAL-Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'GAL-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'GAL-Provincia'"/>
	<xsl:variable name="lang.cp" select="'GAL-C�digo postal'"/>
	<xsl:variable name="lang.datosSolicitud" select="'GAL-Datos de la Solicitud'"/>
	<xsl:variable name="lang.ubicacion" select="'GAL-Ubicaci�n'"/>
	<xsl:variable name="lang.tipoVado" select="'GAL-Tipo de vado'"/>
	<xsl:variable name="lang.TLaboral" select="'GAL-Laboral'"/>
	<xsl:variable name="lang.TPermanente" select="'GAL-Permanente'"/>
	<xsl:variable name="lang.actividad" select="'GAL-Actividad o uso del local'"/>
	<xsl:variable name="lang.numero" select="'GAL-N�mero de plazas para veh�culos'"/>
	<xsl:variable name="lang.rebaje" select="'GAL-Rebaje'"/>
	<xsl:variable name="lang.TAceraAncha" select="'GAL-Acera ancha'"/>
	<xsl:variable name="lang.TAceraEstrecha" select="'GAL-Acera estrecha'"/>
	<xsl:variable name="lang.TMinusvalidos" select="'GAL-Minusv�lidos'"/>
	<xsl:variable name="lang.anexar" select="'GAL-Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'GAL-1.- Para adjuntar un fichero (*.pdf), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'GAL-2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.licenciaVado" select="'GAL-Solicitud de licencia de vado'"/>
	<xsl:variable name="lang.licenciaObras" select="'GAL-Licencias de obras y apertura de los inmuebles a los que se accede'"/>
	<xsl:variable name="lang.planoSituacion" select="'GAL-Plano de situaci�n a escala 1/200'"/>
	<xsl:variable name="lang.planoPlanta" select="'GAL-Plano de planta a escala 1/50'"/>
	<xsl:variable name="lang.justificante" select="'GAL-Justificante de reintegro de los derechos'"/>
	<xsl:variable name="lang.fotocopiaDni" select="'GAL-Fotocopia del DNI'"/>
	
	<xsl:variable name="lang.envio" select="'GAL-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'GAL-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'GAL-D.E.U.'"/>
	<xsl:variable name="lang.telefono" select="'GAL-Tel�fono'"/>
	<xsl:variable name="lang.required" select="' GAL-Campos obligatorios'"/>
	<xsl:variable name="lang.documentoPDF" select="'GAL-Documento PDF'"/>
</xsl:stylesheet>