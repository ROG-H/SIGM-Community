<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'GAL-Formulario de Solicitud de Tarjeta de Estacionamiento para Minusv�lidos.'"/>
	<xsl:variable name="lang.datosSolicitante" select="'GAL-Datos del Solicitante'"/>
	<xsl:variable name="lang.datosRepresentante" select="'GAL-Conductor Autorizado'"/>
	<xsl:variable name="lang.docIdentidad" select="'GAL-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'GAL-Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'GAL-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'GAL-Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'GAL-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'GAL-Provincia'"/>
	<xsl:variable name="lang.cp" select="'GAL-C�digo postal'"/>
	<xsl:variable name="lang.telefono" select="'GAL-Tel�fono'"/>
	<xsl:variable name="lang.anexar" select="'GAL-Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'GAL-1.- Para adjuntar un fichero (*.pdf), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'GAL-2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.envio" select="'GAL-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'GAL-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'GAL-D.E.U.'"/>

	<xsl:variable name="lang.documentoPDF" select="'GAL-Documento PDF'"/>
	<xsl:variable name="lang.documentoJPG" select="'GAL-Documento Imagen'"/>
	<xsl:variable name="lang.documento1" select="'GAL-Una solicitud debidamente cumplimentada, en impreso normalizado que ser� facilitado en el departamento correspondiente (servicios sociales, circulaci�n, veh�culos y transportes, etc.) del ayuntamiento.'"/>
	<xsl:variable name="lang.documento2" select="'GAL-DNI del solicitante'"/>
	<xsl:variable name="lang.documento3" select="'GAL-Certificado de minusval�a expedido por el equipo de valoraci�n de la Xunta de Galicia, o por los equipos autorizados en otras CC.AA.'"/>
	<xsl:variable name="lang.documento4" select="'GAL-Certificado de empadronamiento.'"/>
	<xsl:variable name="lang.documento5" select="'GAL-Fotograf�a tama�o carnet.'"/>
	<xsl:variable name="lang.documento6" select="'GAL-Permiso de conducir bien del minusv�lido, en el caso de que sea �ste el conductor del veh�culo o bien de la persona que lo transporte habitualmente.'"/>
	<xsl:variable name="lang.documento7" select="'GAL-DNI de la persona que lo represente(seg�n caso).'"/>
	<xsl:variable name="lang.documento11" select="'GAL-Declaraci�n jurada del conductor habitual del veh�culo alegando dicha condici�n, en el supuesto de que no sea el propio minusv�lido. '"/>
	<xsl:variable name="lang.documento8" select="'GAL-Permiso de circulaci�n del veh�culo.'"/>
	<xsl:variable name="lang.documento9" select="'GAL-Justificante de pago del �ltimo recibo del impuesto municipal sobre veh�culos de tracci�n mec�nica.'"/>
	<xsl:variable name="lang.documento10" select="'GAL-En los casos de invalidez temporal: informe m�dico que acredite su problema de movilidad, su evoluci�n y pron�stico, as� como la necesidad de utilizar silla de ruedas, muletas, bastones o cualquier otra ayuda t�cnica para minusv�lidos.'"/>
	<xsl:variable name="lang.tipo" select="'GAL-Tipo de Tarjeta Solicitada'"/>
	<xsl:variable name="lang.tipo1" select="'GAL-Tarjeta de Estacionamiento'"/>
	<xsl:variable name="lang.tipo2" select="'GAL-Tarjeta de Accesibilidad'"/>
	<xsl:variable name="lang.solicita" select="'GAL-Solicita'"/>
	<xsl:variable name="lang.nota" select="'GAL-*NOTA: A rellenar en el caso de ser conductor autorizado (distinto del solicitante)'"/>
	<xsl:variable name="lang.required" select="' GAL-Campos obligatorios'"/>
	<xsl:variable name="lang.datosSolicitud" select="'GAL-Datos de la Solicitud'"/>
</xsl:stylesheet>