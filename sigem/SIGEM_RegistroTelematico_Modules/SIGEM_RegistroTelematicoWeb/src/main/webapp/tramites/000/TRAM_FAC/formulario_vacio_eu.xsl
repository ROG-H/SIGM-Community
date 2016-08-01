<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.docIdentidad" select="'EUS-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'EUS-Nombre'"/>
	<xsl:variable name="lang.cifProveedorOrigen" select="'EUS-CIF del proveedor o NIF del ciudadano'"/>
	<xsl:variable name="lang.razonSocialOrigen" select="'EUS-Raz�n social'"/>
	<xsl:variable name="lang.nifRepresentanteOrigen" select="'EUS-NIF del representante o apoderado del certificado digital'"/>
	<xsl:variable name="lang.cifEntidadDestino" select="'EUS-CIF de la entidad'"/>
	<xsl:variable name="lang.razonSocialDestino" select="'EUS-Raz�n social'"/>
	<xsl:variable name="lang.asuntoSolicitud" select="'EUS-Asunto'"/>
	<xsl:variable name="lang.numFacturaSolicitud" select="'EUS-N� Factura'"/>
	<xsl:variable name="lang.fechaFacturaSolicitud" select="'EUS-Fecha factura'"/>
	<xsl:variable name="lang.importe" select="'EUS-Importe'"/>
    
    <xsl:variable name="lang.origenTitle" select="'EUS-ORIGEN'"/>
    <xsl:variable name="lang.destinoTitle" select="'EUS-DESTINO'"/>
    <xsl:variable name="lang.solicitudTitle" select="'EUS-SOLICITUD'"/>
	<xsl:variable name="lang.datosFacturaTitle" select="'EUS-DATOS FACTURA'"/>
</xsl:stylesheet>