<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'Solicitud de Ocupaci�n del Dominio P�blico'"/>
	<xsl:variable name="lang.datosSolicitante" select="'Datos del Solicitante'"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.email" select="'Correo electr�nico'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.cp" select="'C�digo postal'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Expone: Que est� interesado en ocupar temporal el dominio p�blico, sito en:'"/>
	<xsl:variable name="lang.preferenciaa" select="'1� preferencia Calle'"/>
	<xsl:variable name="lang.preferenciab" select="'2� preferencia Calle'"/>
	<xsl:variable name="lang.preferenciac" select="'3� preferencia Calle'"/>
	<xsl:variable name="lang.metros" select="'Extensi�n a ocupar'"/>
	<xsl:variable name="lang.periodo" select="'Periodo'"/>	
	<xsl:variable name="lang.otros" select="'Otros'"/>
	<xsl:variable name="lang.ocupacion" select="'La ocupaci�n se realizara: (marque seg�n proceda)'"/>
	<xsl:variable name="lang.ventaambulante" select="'Venta ambulante'"/>
	<xsl:variable name="lang.indicaractividad" select="'Actividad de la venta ambulante'"/>
	<xsl:variable name="lang.atraccionferia" select="'Atracci�n de feria'"/>
	<xsl:variable name="lang.otrosa" select="'Otros'"/>
	<xsl:variable name="lang.indicarotrosa" select="'Indicar'"/>
	<xsl:variable name="lang.organoAsignado" select="'�rgano al que se dirige: Servicio de Tramitaci�n de Licencias'"/>
	<xsl:variable name="lang.organoAlternativo" select="'�rgano alternativo'"/>
	<xsl:variable name="lang.servRelacionesCiudadano" select="'Servicio de Relaciones con el Ciudadano'"/>
	<xsl:variable name="lang.servSecretaria" select="'Servicio de Secretar�a'"/>
	<xsl:variable name="lang.anexar" select="'Anexar ficheros: (si es actividad empresarial o comercial)'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.pdf, *.doc,...), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento1" select="'Nif del solicitante'"/>
	<xsl:variable name="lang.documento2" select="'Alta en el I.A.E'"/>
	<xsl:variable name="lang.documento3" select="'Alta en la Seguridad Social'"/>
	<xsl:variable name="lang.documento4" select="'Carn� de Manipulador de alimentos (en caso de venta ambulante y/o vetna de productos de alimentos'"/>
	<xsl:variable name="lang.envio" select="'Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'D.E.U.'"/>
	<xsl:variable name="lang.telefono" select="'Tel�fono'"/>

</xsl:stylesheet>