<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'Formulario General de Solicitud de Certificados de Personal'"/>
	<xsl:variable name="lang.datosSolicitante" select="'Datos del Solicitante'"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.tipoCert" select="'Tipo de Certificado'"/>
	<xsl:variable name="lang.email" select="'Correo electr�nico'"/>
	<xsl:variable name="lang.telefono" select="'Tel�fono m�vil'"/>
	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'(M�ximo 7 mbs total)'"/>
	<xsl:variable name="lang.documento1" select="'Documento ZIP'"/>
	<xsl:variable name="lang.envio" select="'Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'D.E.U.'"/>
	<xsl:variable name="lang.AvisoMail" select="'Recuerde que si no est� dado de alta en Comparece, debe rellenar el campo de correo electr�nico. Si no rellena este campo, pasados unos d�as deber� personarse en las dependencias del Servicio de Personal para recoger su certificado.'"/>
	<xsl:variable name="lang.Comparece" select="'QUEREMOS SER M�S �GILES Y CONTESTARLE CON RAPIDEZ'"/>
	<xsl:variable name="lang.Comparece1" select="'Portal de Notificaciones Telem�ticas de la Diputaci�n de Ciudad Real COMPARECE'"/>
	<xsl:variable name="lang.Comparece2" select="'Si lo desea, puede ayudarnos d�ndose de alta en el'"/>
	<xsl:variable name="lang.Comparece3" select="'R�pido, gratuito, sin papel.'"/>
	
	<xsl:variable name="lang.info_legal" select="'Los datos personales, identificativos y de contacto, aportados mediante esta comunicaci�n se entienden facilitados voluntariamente, y ser�n incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del �mbito de las competencias de esta Administraci�n P�blica as� como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telem�ticos y enviarle invitaciones para eventos y felicitaciones en fechas se�aladas. Entenderemos que presta su consentimiento t�cito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podr� ejercer sus derechos de acceso, rectificaci�n, cancelaci�n y oposici�n ante el Responsable del Fichero, la Diputaci�n Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - Espa�a, siempre acreditando conforme a Derecho su identidad en la comunicaci�n. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigi�ndose a la direcci�n citada ut supra o bien al correo electr�nico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.'"/>
	
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			var validar = new Array(3);
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.docIdentidad"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.nombre"/>');
			validar[2] = new Array('tipoCert','<xsl:value-of select="$lang.tipoCert"/>');

			var validar_NO = new Array(1);
			validar_NO[0] = new Array('BOP1D1','<xsl:value-of select="$lang.documento1"/>');
			
			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			var especificos = new Array(8);
			especificos[0] = new Array('tipoCert','Tipo_Cert');			
			especificos[1] = new Array('emailSolicitante','Email_Solicitante');
			especificos[2] = new Array('telefono','Telefono');
			especificos[3] = new Array('observaciones','Observaciones');
			especificos[4] = new Array('documentoIdentidad','NIF');
			especificos[5] = new Array('nombreSolicitante','Nombre');

			especificos[6] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[7] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');
			
			var validarNumero = new Array(0);
			
			function verificacionesEspecificas() {
				return true;
			}

		</script>
		
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitante"/></h1>
   		</div>
   		<div class="cuadro" style="">	
   		
			<xsl:if test="Datos_Registro/datos_especificos/NIF">
				<div class="col">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.docIdentidad"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">position: relative; width:350px;</xsl:attribute>
						<xsl:attribute name="name">documentoIdentidad</xsl:attribute>
						<xsl:attribute name="id">documentoIdentidad</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/NIF"/></xsl:attribute>
						<xsl:attribute name="disabled"></xsl:attribute>
					</input>
				</div>
				<div class="col" style="height: 35px;">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.nombre"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
						<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
						<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Nombre"/></xsl:attribute>
					</input>
				</div>
			</xsl:if>
			<xsl:if test="not(Datos_Registro/datos_especificos/NIF)">
				<div class="col">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.docIdentidad"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">position: relative; width:350px;</xsl:attribute>
						<xsl:attribute name="name">documentoIdentidad</xsl:attribute>
						<xsl:attribute name="id">documentoIdentidad</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Documento_Identificacion/Numero"/></xsl:attribute>
						<xsl:attribute name="disabled"></xsl:attribute>
					</input>
				</div>
				<div class="col" style="height: 35px;">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.nombre"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
						<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
						<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
						<xsl:attribute name="disabled"></xsl:attribute>
					</input>
				</div>
			</xsl:if>

			<!-- Cargamos los tipos de certificados mediante servicio web -->
   		<xsl:variable name="fileTiposCert" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.certPersonal.XmlCertificadosPersonal.getDatosTiposCert()"/>
			<xsl:variable name="b" select="document($fileTiposCert)"/>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.tipoCert"/>:*	
				</label>
				<select>
					<xsl:attribute name="style">position:relative;width:450px;color:#006699;font-size:13px;</xsl:attribute>
					<xsl:attribute name="name">tipoCert</xsl:attribute>
					<xsl:attribute name="id">tipoCert</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Tipo_Cert"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
			</div>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.telefono"/>:	
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">telefono</xsl:attribute>
					<xsl:attribute name="id">telefono</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Telefono"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.email"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">emailSolicitante</xsl:attribute>
					<xsl:attribute name="id">emailSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Correo_Electronico"/></xsl:attribute>
				</input>
				<label>
					<xsl:attribute name="style">font-style:italic; color:red; </xsl:attribute>				
					(*) <xsl:value-of select="$lang.AvisoMail"/>
				</label>
			</div>
			<div class="col" style="height: 66px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.observaciones"/>:
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">observaciones</xsl:attribute>
					<xsl:attribute name="id">observaciones</xsl:attribute>
					<xsl:attribute name="rows">3</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/Observaciones"/>
				</textarea>
			</div>
		</div>
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN" />
   		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/><br/>
		   			<xsl:value-of select="$lang.anexarInfo2"/><br/>
			   			<xsl:value-of select="$lang.anexarInfo3"/><br/>
			</label>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento1"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">BOP1D1</xsl:attribute>
					<xsl:attribute name="id">BOP1D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">BOP1D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">BOP1D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>			
   		</div>
		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN" />
   		<br/>
		<xsl:call-template name="TEXTO_COMPARECE_COMUN" />
   		<br/>
   		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
   		<br/>

   		<input class="gr" type="hidden" id="solicitarEnvio" onclick="activarDEU();" style="border:0px; width:20px;" />
   		<input class="gr" type="hidden" id="direccionElectronicaUnica" />
	</xsl:template>
</xsl:stylesheet>
