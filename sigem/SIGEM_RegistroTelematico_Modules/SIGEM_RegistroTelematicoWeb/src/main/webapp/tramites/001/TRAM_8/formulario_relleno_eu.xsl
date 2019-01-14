<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_relleno.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.datosInteresado" select="'EUS-Datos del Interesado Principal'"/>
	<xsl:variable name="lang.datosParticipante" select="'EUS-Datos del Participante'"/>
	<xsl:variable name="lang.datosSolicitud" select="'EUS-Datos de la Solicitud'"/>
	<xsl:variable name="lang.nifCif" select="'EUS-NIF/CIF'"/>
	<xsl:variable name="lang.identidad" select="'EUS-Nombre'"/>
	<xsl:variable name="lang.relacion" select="'EUS-Relaci�n'"/>
	<xsl:variable name="lang.TComprador" select="'EUS-Comprador'"/>
	<xsl:variable name="lang.TVendedor" select="'EUS-Vendedor'"/>
	<xsl:variable name="lang.TTitular" select="'EUS-Titular'"/>
	<xsl:variable name="lang.domicilio" select="'EUS-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'EUS-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'EUS-Provincia'"/>
	<xsl:variable name="lang.cp" select="'EUS-C�digo postal'"/>
	<xsl:variable name="lang.telefono" select="'EUS-Tel�fono'"/>
	<xsl:variable name="lang.denominacion" select="'EUS-Denominaci�n'"/>
	<xsl:variable name="lang.datosSolicitud" select="'EUS-Datos de la Solicitud'"/>
	<xsl:variable name="lang.actividad" select="'EUS-Actividad'"/>
	<xsl:variable name="lang.emplazamiento" select="'EUS-Emplazamiento'"/>
	<xsl:variable name="lang.fechaLicencia" select="'EUS-Fecha Licencia'"/>
	<xsl:variable name="lang.fechaAprobacion" select="'EUS-Fecha Aprobaci�n'"/>
	<xsl:variable name="lang.fechaFormato" select="'EUS-Formato v�lido [DD/MM/AAAA]'" /> 
	<xsl:variable name="lang.tasas" select="'EUS-Tasas'" /> 
	<xsl:variable name="lang.euros" select="'&#8364;'" /> 
	<xsl:variable name="lang.organoSujeto_1" select="'EUS-�rgano/s o unidad/es municipal/es responsable/s del procedimiento'"/>
	<xsl:variable name="lang.organoResponsable" select="'EUS-�rgano Responsable'"/>
	<xsl:variable name="lang.gerenciasSubgerenciasCatastro" select="'EUS-Las Gerencias y Subgerencias del Catastro'"/>
	<xsl:variable name="lang.areaUrbanismo" select="'EUS-�rea de Urbanismo'"/>	
	
	<xsl:variable name="lang.instanciaCambioTitular" select="'EUS-Instancia/solictud de cambio de titular, firmada por ambos (actual titular y adquiriente)'"/>
	<xsl:variable name="lang.fotocopiaLicencia" select="'EUS-Fotocopia de la licencia'"/>
	<xsl:variable name="lang.fotocopiaIAE" select="'EUS-Justificante/fotocopia de alta en I.A.E.'"/>
	<xsl:variable name="lang.contratoArrendamiento" select="'EUS-Contrato de Arrendamiento'"/>

	<xsl:variable name="lang.envio" select="'EUS-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'EUS-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'EUS-D.E.U.'"/>
	<xsl:variable name="lang.email" select="'EUS-Correo electr�nico'"/>
</xsl:stylesheet>