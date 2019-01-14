<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'EUS-Formulario de Solicitud de Certificado Urban�stico'"/>
	<xsl:variable name="lang.datosSolicitante" select="'EUS-Datos del Solicitante'"/>
	<xsl:variable name="lang.docIdentidad" select="'EUS-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'EUS-Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'EUS-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'EUS-Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'EUS-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'EUS-Provincia'"/>
	<xsl:variable name="lang.cp" select="'EUS-C�digo postal'"/>
	<xsl:variable name="lang.telefono" select="'EUS-Tel�fono'"/>
	<xsl:variable name="lang.datosSolicitud" select="'EUS-Datos de la Solicitud'"/>
	<xsl:variable name="lang.anexar" select="'EUS-Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'EUS-1.- Para adjuntar un fichero (*.pdf), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'EUS-2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.envio" select="'EUS-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'EUS-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'EUS-D.E.U.'"/>
	<xsl:variable name="lang.documentoPDF" select="'EUS-Documento PDF'"/>
	<xsl:variable name="lang.documentoDOC" select="'EUS-Documento DOC'"/>
	
	<xsl:variable name="lang.representante" select="'EUS-Datos del Representante'"/>
	<xsl:variable name="lang.situacion" select="'EUS-Situacion de la Finca'"/>
	<xsl:variable name="lang.documento1" select="'EUS-Justificante de autoliquidacion de la tasa por emisi�n de certificados.'"/>
	<xsl:variable name="lang.documento2" select="'EUS-Plano de localizaci�n sobre cartograf�a oficial FIRMADO, en el que se refleje la posici�n exacta de la finca/parcela/edificio objeto de la solicitud.'"/>
	<xsl:variable name="lang.documento3" select="'EUS-Fotocopia del DNI del solicitante en el caso de personas f�sicas. En el caso de actuar en representaci�n, fotocopia de los documentos de identidad del representante y del solicitante, con autorizaci�n escrita de este �ltimo o copia de poder para representarlo. En el caso de personas jur�dicas, la persona que firma la solicitud adjuntar� documentaci�n acreditativa de su representaci�n como puede ser una fotocopia p�blica de la escritura de constituci�n de la sociedad, poder, etc.'"/>
	<xsl:variable name="lang.documento4" select="'EUS-Acreditaci�n de la condici�n de interesado (escrituras de propiedad,etc.).'"/>
	<xsl:variable name="lang.required" select="' EUS-Campos obligatorios'"/>
	
</xsl:stylesheet>