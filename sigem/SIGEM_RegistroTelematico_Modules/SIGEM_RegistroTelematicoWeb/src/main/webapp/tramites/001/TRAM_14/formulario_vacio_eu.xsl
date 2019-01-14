<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	
	<xsl:variable name="lang.titulo" select="'EUS-Formulario de Solicitud de Tarjeta de Estacionamiento para Minusv�lidos.'"/>
	<xsl:variable name="lang.datosSolicitante" select="'EUS-Datos del Solicitante'"/>
	<xsl:variable name="lang.datosRepresentante" select="'EUS-Conductor Autorizado'"/>
	<xsl:variable name="lang.docIdentidad" select="'EUS-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'EUS-Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'EUS-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'EUS-Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'EUS-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'EUS-Provincia'"/>
	<xsl:variable name="lang.cp" select="'EUS-C�digo postal'"/>
	<xsl:variable name="lang.telefono" select="'EUS-Tel�fono'"/>
	<xsl:variable name="lang.anexar" select="'EUS-Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'EUS-1.- Para adjuntar un fichero (*.pdf), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'EUS-2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.envio" select="'EUS-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'EUS-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'EUS-D.E.U.'"/>

	<xsl:variable name="lang.documentoPDF" select="'EUS-Documento PDF'"/>
	<xsl:variable name="lang.documentoJPG" select="'EUS-Documento Imagen'"/>
	<xsl:variable name="lang.documento1" select="'EUS-Una solicitud debidamente cumplimentada, en impreso normalizado que ser� facilitado en el departamento correspondiente (servicios sociales, circulaci�n, veh�culos y transportes, etc.) del ayuntamiento.'"/>
	<xsl:variable name="lang.documento2" select="'EUS-DNI del solicitante'"/>
	<xsl:variable name="lang.documento3" select="'EUS-Certificado de minusval�a expedido por el equipo de valoraci�n de la Xunta de Galicia, o por los equipos autorizados en otras CC.AA.'"/>
	<xsl:variable name="lang.documento4" select="'EUS-Certificado de empadronamiento.'"/>
	<xsl:variable name="lang.documento5" select="'EUS-Fotograf�a tama�o carnet.'"/>
	<xsl:variable name="lang.documento6" select="'EUS-Permiso de conducir bien del minusv�lido, en el caso de que sea �ste el conductor del veh�culo o bien de la persona que lo transporte habitualmente.'"/>
	<xsl:variable name="lang.documento7" select="'EUS-DNI de la persona que lo represente(seg�n caso).'"/>
	<xsl:variable name="lang.documento11" select="'EUS-Declaraci�n jurada del conductor habitual del veh�culo alegando dicha condici�n, en el supuesto de que no sea el propio minusv�lido. '"/>
	<xsl:variable name="lang.documento8" select="'EUS-Permiso de circulaci�n del veh�culo.'"/>
	<xsl:variable name="lang.documento9" select="'EUS-Justificante de pago del �ltimo recibo del impuesto municipal sobre veh�culos de tracci�n mec�nica.'"/>
	<xsl:variable name="lang.documento10" select="'EUS-En los casos de invalidez temporal: informe m�dico que acredite su problema de movilidad, su evoluci�n y pron�stico, as� como la necesidad de utilizar silla de ruedas, muletas, bastones o cualquier otra ayuda t�cnica para minusv�lidos.'"/>
	<xsl:variable name="lang.tipo" select="'EUS-Tipo de Tarjeta Solicitada'"/>
	<xsl:variable name="lang.tipo1" select="'EUS-Tarjeta de Estacionamiento'"/>
	<xsl:variable name="lang.tipo2" select="'EUS-Tarjeta de Accesibilidad'"/>
	<xsl:variable name="lang.solicita" select="'EUS-Solicita'"/>
	<xsl:variable name="lang.nota" select="'EUS-*NOTA: A rellenar en el caso de ser conductor autorizado (distinto del solicitante)'"/>	
	<xsl:variable name="lang.required" select="' EUS-Campos obligatorios'"/>
	<xsl:variable name="lang.datosSolicitud" select="'EUS-Datos de la Solicitud'"/>
</xsl:stylesheet>