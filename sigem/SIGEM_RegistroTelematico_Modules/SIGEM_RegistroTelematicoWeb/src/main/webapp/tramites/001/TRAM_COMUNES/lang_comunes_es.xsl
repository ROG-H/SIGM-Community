<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	
	<xsl:variable name="lang.datosSolicitante" select="'Datos del Solicitante'"/>		
	<xsl:variable name="lang.datosInteresado" select="'Datos del Interesado'"/>
	<xsl:variable name="lang.datosRepresentante" select="'Datos del Representante (opcional)'"/>
	<xsl:variable name="lang.interesado" select="'Interesado'"/>
	<xsl:variable name="lang.representante" select="'Representante (opcional)'"/>
	<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre" select="'Apellidos y Nombre o Denominaci�n Social'"/>
	<xsl:variable name="lang.calle" select="'Domicilio'"/>
	<xsl:variable name="lang.direccion" select="'Direcci�n'"/>
	<xsl:variable name="lang.c_postal" select="'C�digo Postal'"/>
	<xsl:variable name="lang.ciudad" select="'Ciudad'"/>
	<xsl:variable name="lang.region" select="'Regi�n / Pa�s'"/>
	<xsl:variable name="lang.movil" select="'N�mero de tel�fono m�vil'"/>
	<xsl:variable name="lang.d_email" select="'Direcci�n de correo electr�nico'"/>

	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'3.- Cuando se act�e por representaci�n es obligatorio anexar el documento que acredite la representaci�n (escritura notarial, apoderamiento apud acta ...)'"/>

	<xsl:variable name="lang.documento1" select="'Documento ODT/DOC'"/>
	<xsl:variable name="lang.documento2" select="'Documento PDF'"/>
	<xsl:variable name="lang.documento3" select="'Documento JPEG'"/>
	<xsl:variable name="lang.documento4" select="'Documento ZIP'"/>

	<xsl:variable name="lang.info_legal" select="'Los datos personales, identificativos y de contacto, aportados mediante esta comunicaci�n se entienden facilitados voluntariamente, y ser�n incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del �mbito de las competencias de esta Administraci�n P�blica as� como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telem�ticos y enviarle invitaciones para eventos y felicitaciones en fechas se�aladas. Entenderemos que presta su consentimiento t�cito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podr� ejercer sus derechos de acceso, rectificaci�n, cancelaci�n y oposici�n ante el Responsable del Fichero, la Diputaci�n Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - Espa�a, siempre acreditando conforme a Derecho su identidad en la comunicaci�n. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigi�ndose a la direcci�n citada ut supra o bien al correo electr�nico lopd@dipucr.es o bien por tel�fono al n�mero gratuito 900 714 080.'"/>
	
	<!-- Rellenar estos par�metros en el formulario vac�o 
		   Se ponen aqu� para que no provoque error el template -->
	<xsl:variable name="doc.docodt" select="'NOMBRE_D1'"/>
	<xsl:variable name="doc.pdf" select="'NOMBRE_D2'"/>
	<xsl:variable name="doc.jpg" select="'NOMBRE_D3'"/>
	<xsl:variable name="doc.zip" select="'NOMBRE_D4'"/>
	
	<!-- Informaci�n del registro -->
	<xsl:variable name="lang.numRegistro" select="'N�mero de registro'"/>
	<xsl:variable name="lang.fechaPresentacion" select="'Fecha de presentaci�n'"/>
	<xsl:variable name="lang.fechaEfectiva" select="'Fecha efectiva'"/>
	<xsl:variable name="lang.asunto" select="'Asunto'"/>

</xsl:stylesheet>
