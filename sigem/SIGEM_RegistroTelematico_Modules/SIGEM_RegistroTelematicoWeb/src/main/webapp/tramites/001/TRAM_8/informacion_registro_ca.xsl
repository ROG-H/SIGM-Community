<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="informacion_registro.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.datosInteresado" select="'CAT-Datos del Interesado Principal'"/>
	<xsl:variable name="lang.datosParticipante" select="'CAT-Datos del Participante'"/>
	<xsl:variable name="lang.datosSolicitud" select="'CAT-Datos de la Solicitud'"/>
	<xsl:variable name="lang.nifCif" select="'CAT-NIF/CIF'"/>
	<xsl:variable name="lang.identidad" select="'CAT-Nombre'"/>
	<xsl:variable name="lang.relacion" select="'CAT-Relaci�n'"/>
	<xsl:variable name="lang.TComprador" select="'CAT-Comprador'"/>
	<xsl:variable name="lang.TVendedor" select="'CAT-Vendedor'"/>
	<xsl:variable name="lang.TTitular" select="'CAT-Titular'"/>
	<xsl:variable name="lang.domicilio" select="'CAT-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'CAT-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'CAT-Provincia'"/>
	<xsl:variable name="lang.cp" select="'CAT-C�digo postal'"/>
	<xsl:variable name="lang.telefono" select="'CAT-Tel�fono'"/>
	<xsl:variable name="lang.denominacion" select="'CAT-Denominaci�n'"/>
	<xsl:variable name="lang.datosSolicitud" select="'CAT-Datos de la Solicitud'"/>
	<xsl:variable name="lang.actividad" select="'CAT-Actividad'"/>
	<xsl:variable name="lang.emplazamiento" select="'CAT-Emplazamiento'"/>
	<xsl:variable name="lang.fechaLicencia" select="'CAT-Fecha Licencia'"/>
	<xsl:variable name="lang.fechaAprobacion" select="'CAT-Fecha Aprobaci�n'"/>
	<xsl:variable name="lang.fechaFormato" select="'CAT-Formato v�lido [DD/MM/AAAA]'" /> 
	<xsl:variable name="lang.tasas" select="'CAT-Tasas'" /> 
	<xsl:variable name="lang.euros" select="'&#8364;'" /> 
	<xsl:variable name="lang.organoSujeto_1" select="'CAT-�rgano/s o unidad/es municipal/es responsable/s del procedimiento'"/>
	<xsl:variable name="lang.organoResponsable" select="'CAT-�rgano Responsable'"/>
	<xsl:variable name="lang.gerenciasSubgerenciasCatastro" select="'CAT-Las Gerencias y Subgerencias del Catastro'"/>
	<xsl:variable name="lang.areaUrbanismo" select="'CAT-�rea de Urbanismo'"/>	
	
	<xsl:variable name="lang.instanciaCambioTitular" select="'CAT-Instancia/solictud de cambio de titular, firmada por ambos (actual titular y adquiriente)'"/>
	<xsl:variable name="lang.fotocopiaLicencia" select="'CAT-Fotocopia de la licencia'"/>
	<xsl:variable name="lang.fotocopiaIAE" select="'CAT-Justificante/fotocopia de alta en I.A.E.'"/>
	<xsl:variable name="lang.contratoArrendamiento" select="'CAT-Contrato de Arrendamiento'"/>

	<xsl:variable name="lang.envio" select="'CAT-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'CAT-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'CAT-D.E.U.'"/>
	<xsl:variable name="lang.email" select="'CAT-Email'"/>
	<xsl:variable name="lang.numRegistro" select="'CAT-N�mero de registro'"/>
	<xsl:variable name="lang.fechaPresentacion" select="'CAT-Fecha de presentaci�n'"/>
	<xsl:variable name="lang.fechaEfectiva" select="'CAT-Fecha efectiva'"/>
	<xsl:variable name="lang.asunto" select="'CAT-Asunto'"/>
	<xsl:variable name="lang.destino" select="'CAT-Destino'"/>
</xsl:stylesheet>