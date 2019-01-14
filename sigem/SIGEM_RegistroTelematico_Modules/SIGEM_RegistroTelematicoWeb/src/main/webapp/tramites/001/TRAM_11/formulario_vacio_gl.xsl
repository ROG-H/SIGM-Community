<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'GAL-Formulario de Solicitud de Certificado Urban�stico'"/>
	<xsl:variable name="lang.datosSolicitante" select="'GAL-Datos del Solicitante'"/>
	<xsl:variable name="lang.docIdentidad" select="'GAL-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'GAL-Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'GAL-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'GAL-Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'GAL-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'GAL-Provincia'"/>
	<xsl:variable name="lang.cp" select="'GAL-C�digo postal'"/>
	<xsl:variable name="lang.telefono" select="'GAL-Tel�fono'"/>
	<xsl:variable name="lang.datosSolicitud" select="'GAL-Datos de la Solicitud'"/>
	<xsl:variable name="lang.anexar" select="'GAL-Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'GAL-1.- Para adjuntar un fichero (*.pdf), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'GAL-2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.envio" select="'Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'GAL-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'GAL-D.E.U.'"/>
	<xsl:variable name="lang.documentoPDF" select="'GAL-Documento PDF'"/>
	<xsl:variable name="lang.documentoDOC" select="'GAL-Documento DOC'"/>
	
	<xsl:variable name="lang.representante" select="'GAL-Datos del Representante'"/>
	<xsl:variable name="lang.situacion" select="'GAL-Situacion de la Finca'"/>
	<xsl:variable name="lang.documento1" select="'GAL-Justificante de autoliquidacion de la tasa por emisi�n de certificados.'"/>
	<xsl:variable name="lang.documento2" select="'GAL-Plano de localizaci�n sobre cartograf�a oficial FIRMADO, en el que se refleje la posici�n exacta de la finca/parcela/edificio objeto de la solicitud.'"/>
	<xsl:variable name="lang.documento3" select="'GAL-Fotocopia del DNI del solicitante en el caso de personas f�sicas. En el caso de actuar en representaci�n, fotocopia de los documentos de identidad del representante y del solicitante, con autorizaci�n escrita de este �ltimo o copia de poder para representarlo. En el caso de personas jur�dicas, la persona que firma la solicitud adjuntar� documentaci�n acreditativa de su representaci�n como puede ser una fotocopia p�blica de la escritura de constituci�n de la sociedad, poder, etc.'"/>
	<xsl:variable name="lang.documento4" select="'GAL-Acreditaci�n de la condici�n de interesado (escrituras de propiedad,etc.).'"/>
	<xsl:variable name="lang.documento5" select="'GAL_Plano de situaci�n de la parcela a escala 1:1000.'"/>
	<xsl:variable name="lang.documento6" select="'GAL-Documentaci�n gr�fica de la edificaci�n (planta y secciones transversales).'"/>
	<xsl:variable name="lang.required" select="' GAL-Campos obligatorios'"/>
</xsl:stylesheet>