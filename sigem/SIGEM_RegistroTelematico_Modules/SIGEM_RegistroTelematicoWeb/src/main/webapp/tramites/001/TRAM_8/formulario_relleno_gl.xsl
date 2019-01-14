<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_relleno.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.datosInteresado" select="'GAL-Datos del Interesado Principal'"/>
	<xsl:variable name="lang.datosParticipante" select="'GAL-Datos del Participante'"/>
	<xsl:variable name="lang.datosSolicitud" select="'GAL-Datos de la Solicitud'"/>
	<xsl:variable name="lang.nifCif" select="'GAL-NIF/CIF'"/>
	<xsl:variable name="lang.identidad" select="'GAL-Nombre'"/>
	<xsl:variable name="lang.relacion" select="'GAL-Relaci�n'"/>
	<xsl:variable name="lang.TComprador" select="'GAL-Comprador'"/>
	<xsl:variable name="lang.TVendedor" select="'GAL-Vendedor'"/>
	<xsl:variable name="lang.TTitular" select="'GAL-Titular'"/>
	<xsl:variable name="lang.domicilio" select="'GAL-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'GAL-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'GAL-Provincia'"/>
	<xsl:variable name="lang.cp" select="'GAL-C�digo postal'"/>
	<xsl:variable name="lang.telefono" select="'GAL-Tel�fono'"/>
	<xsl:variable name="lang.denominacion" select="'GAL-Denominaci�n'"/>
	<xsl:variable name="lang.datosSolicitud" select="'GAL-Datos de la Solicitud'"/>
	<xsl:variable name="lang.actividad" select="'GAL-Actividad'"/>
	<xsl:variable name="lang.emplazamiento" select="'GAL-Emplazamiento'"/>
	<xsl:variable name="lang.fechaLicencia" select="'GAL-Fecha Licencia'"/>
	<xsl:variable name="lang.fechaAprobacion" select="'GAL-Fecha Aprobaci�n'"/>
	<xsl:variable name="lang.fechaFormato" select="'GAL-Formato v�lido [DD/MM/AAAA]'" /> 
	<xsl:variable name="lang.tasas" select="'GAL-Tasas'" /> 
	<xsl:variable name="lang.euros" select="'&#8364;'" /> 
	<xsl:variable name="lang.organoSujeto_1" select="'GAL-�rgano/s o unidad/es municipal/es responsable/s del procedimiento'"/>
	<xsl:variable name="lang.organoResponsable" select="'GAL-�rgano Responsable'"/>
	<xsl:variable name="lang.gerenciasSubgerenciasCatastro" select="'GAL-Las Gerencias y Subgerencias del Catastro'"/>
	<xsl:variable name="lang.areaUrbanismo" select="'GAL-�rea de Urbanismo'"/>	
	
	<xsl:variable name="lang.instanciaCambioTitular" select="'GAL-Instancia/solictud de cambio de titular, firmada por ambos (actual titular y adquiriente)'"/>
	<xsl:variable name="lang.fotocopiaLicencia" select="'GAL-Fotocopia de la licencia'"/>
	<xsl:variable name="lang.fotocopiaIAE" select="'GAL-Justificante/fotocopia de alta en I.A.E.'"/>
	<xsl:variable name="lang.contratoArrendamiento" select="'GAL-Contrato de Arrendamiento'"/>

	<xsl:variable name="lang.envio" select="'GAL-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'GAL-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'GAL-D.E.U.'"/>
	<xsl:variable name="lang.email" select="'GAL-Correo electr�nico'"/>
</xsl:stylesheet>