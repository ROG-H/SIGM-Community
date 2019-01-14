<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'CAT-Formulario de Solicitud de Certificado Urban�stico'"/>
	<xsl:variable name="lang.datosSolicitante" select="'CAT-Datos del Solicitante'"/>
	<xsl:variable name="lang.docIdentidad" select="'CAT-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'CAT-Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'CAT-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'CAT-Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'CAT-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'CAT-Provincia'"/>
	<xsl:variable name="lang.cp" select="'CAT-C�digo postal'"/>
	<xsl:variable name="lang.telefono" select="'CAT-Tel�fono'"/>
	<xsl:variable name="lang.datosSolicitud" select="'CAT-Datos de la Solicitud'"/>
	<xsl:variable name="lang.anexar" select="'CAT-Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'CAT-1.- Para adjuntar un fichero (*.pdf), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'CAT-2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.envio" select="'CAT-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'CAT-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'CAT-D.E.U.'"/>
	<xsl:variable name="lang.documentoPDF" select="'CAT-Documento PDF'"/>
	<xsl:variable name="lang.documentoDOC" select="'CAT-Documento DOC'"/>
	
	<xsl:variable name="lang.representante" select="'CAT-Datos del Representante'"/>
	<xsl:variable name="lang.situacion" select="'CAT-Situacion de la Finca'"/>
	<xsl:variable name="lang.documento1" select="'CAT-Justificante de autoliquidacion de la tasa por emisi�n de certificados.'"/>
	<xsl:variable name="lang.documento2" select="'CAT-Plano de localizaci�n sobre cartograf�a oficial FIRMADO, en el que se refleje la posici�n exacta de la finca/parcela/edificio objeto de la solicitud.'"/>
	<xsl:variable name="lang.documento3" select="'CAT-Fotocopia del DNI del solicitante en el caso de personas f�sicas. En el caso de actuar en representaci�n, fotocopia de los documentos de identidad del representante y del solicitante, con autorizaci�n escrita de este �ltimo o copia de poder para representarlo. En el caso de personas jur�dicas, la persona que firma la solicitud adjuntar� documentaci�n acreditativa de su representaci�n como puede ser una fotocopia p�blica de la escritura de constituci�n de la sociedad, poder, etc.'"/>
	<xsl:variable name="lang.documento4" select="'CAT-Acreditaci�n de la condici�n de interesado (escrituras de propiedad,etc.).'"/>
	<xsl:variable name="lang.required" select="' CAT-Campos obligatorios'"/>
	
</xsl:stylesheet>