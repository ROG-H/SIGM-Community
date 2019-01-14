<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="informacion_registro.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.docIdentidad" select="'GAL-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'GAL-Nombre'"/>
	<xsl:variable name="lang.email" select="'GAL-Correo electr�nico'"/>
	<xsl:variable name="lang.numRegistro" select="'GAL-N�mero de registro'"/>
	<xsl:variable name="lang.fechaPresentacion" select="'GAL-Fecha de presentaci�n'"/>
	<xsl:variable name="lang.fechaEfectiva" select="'GAL-Fecha efectiva'"/>
    
	<xsl:variable name="lang.cifProveedorOrigen" select="'GAL-CIF del proveedor o NIF del ciudadano'"/>
	<xsl:variable name="lang.razonSocialOrigen" select="'GAL-Raz�n social'"/>
	<xsl:variable name="lang.nifRepresentanteOrigen" select="'GAL-NIF del representante o apoderado del certificado digital'"/>
	<xsl:variable name="lang.cifEntidadDestino" select="'GAL-CIF de la entidad'"/>
	<xsl:variable name="lang.razonSocialDestino" select="'GAL-Raz�n social'"/>
	<xsl:variable name="lang.asuntoSolicitud" select="'GAL-Asunto'"/>
	<xsl:variable name="lang.numFacturaSolicitud" select="'GAL-N� Factura'"/>
	<xsl:variable name="lang.fechaFacturaSolicitud" select="'GAL-Fecha factura'"/>
	<xsl:variable name="lang.importe" select="'GA-Importe'"/>
    
    <xsl:variable name="lang.origenTitle" select="'GAL-ORIGEN'"/>
    <xsl:variable name="lang.destinoTitle" select="'GAL-DESTINO'"/>
    <xsl:variable name="lang.solicitudTitle" select="'GAL-SOLICITUD'"/>
</xsl:stylesheet>