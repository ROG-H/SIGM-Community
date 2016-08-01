<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'SOLICITUD DE TRABAJOS A LA IMPRENTA PROVINCIAL'"/>
	
	<xsl:variable name="lang.datosPresentador" select="'Datos de la persona que presenta la solicitud'"/>
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cargo" select="'Cargo'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Datos de la persona de contacto'"/>
	<xsl:variable name="lang.nif_repre" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre_repre" select="'Apellidos y Nombre o Denominaci�n Social'"/>
	<xsl:variable name="lang.domicilio_repre" select="'Domicilio'"/>
	<xsl:variable name="lang.ciudad_repre" select="'Localidad'"/>
	<xsl:variable name="lang.c_postal_repre" select="'C�digo Postal'"/>
	<xsl:variable name="lang.region_repre" select="'Regi�n / Pa�s'"/>
	<xsl:variable name="lang.movil_repre" select="'Tel�fono'"/>
	<xsl:variable name="lang.d_email_repre" select="'Correo electr�nico'"/>	

	<xsl:variable name="lang.datosInteresado" select="'Datos de la Asociaci�n solicitante'"/>
	<xsl:variable name="lang.representante" select="'Asociaci�n'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'C�digo postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Tel�fono'"/>
	<xsl:variable name="lang.email" select="'Correo electr�nico'"/>

	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.clase" select="'Clase y denominaci�n del trabajo'"/>
	<xsl:variable name="lang.tipo_trabajo" select="'Tipo de trabajo solicitado'"/>
	<xsl:variable name="lang.nota" select="'Una solicitud por cada tipo de trabajo.'"/>
	<xsl:variable name="lang.cantidad" select="'Cantidad'"/>
	<xsl:variable name="lang.formato" select="'Formato'"/>
	<xsl:variable name="lang.deposito_legal" select="'Dep�sito legal'"/>
	<xsl:variable name="lang.jccm" select="'(asignado por la JCCM, cuando proceda)'"/>

	<xsl:template match="/">
	<div class="cuadro" style="">
		<div class="col">
			<label class="gr" style="position: relative; width:100%; text-align:center; background-color:ORANGE; vertical-align:middle;">
				<font style="color:WHITE; font-weight: bold; font-size:14px;"><xsl:value-of select="$lang.titulo"/></font>
			</label>			
		</div>
	</div>
	<div class="submenu">
		<h1><xsl:value-of select="$lang.datosPresentador"/></h1>
	</div>
	<div class="cuadro" style="">
		<div class="col">
			<label class="gr" style="position: relative; width:200px; left:10px;">
				<b><xsl:value-of select="$lang.id_nif"/>:</b>	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; left:10px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
			</label>
			<br/>
		</div>		
		<div class="col">
			<label class="gr" style="position: relative; left:10px;">
				<b><xsl:value-of select="$lang.id_nombre"/>:</b>	
			</label>
			<label class="gr" style="position: relative; width:500px; left:10px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
			</label>
			<br/>
		</div>	
		<div class="col">
			<label class="gr" style="position: relative; left:10px;">
				<b><xsl:value-of select="$lang.cargo"/>:</b>	
			</label>
			<label class="gr" style="position: relative; width:500px; left:10px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cargo"/>
			</label>
			<br/>
		</div>
	</div>
	<div class="submenu">
		<h1><xsl:value-of select="$lang.datosRepresentante"/></h1>
	</div>
	<div class="cuadro" style="">
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nif_repre">
			<div class="col">
				<label class="gr" style="position: relative; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.nif_repre"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nif_repre"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre_repre">
			<div class="col">
				<label class="gr" style="position: relative; width:100%;font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.nombre_repre"/>:	
				</label>
				<label class="gr" style="position: relative; width:100%; left:20px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre_repre"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/domicilio_repre">
			<div class="col">
				<label class="gr" style="position: relative; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.domicilio_repre"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/domicilio_repre"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ciudad_repre">
			<div class="col">
				<label class="gr" style="position: relative; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.ciudad_repre"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ciudad_repre"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/c_postal_repre">
			<div class="col">
				<label class="gr" style="position: relative; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.c_postal_repre"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/c_postal_repre"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/region_repre">
			<div class="col">
				<label class="gr" style="position: relative; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.region_repre"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/region_repre"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/movil_repre">
			<div class="col">
				<label class="gr" style="position: relative; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.movil_repre"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/movil_repre"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/d_email_repre">
			<div class="col">
				<label class="gr" style="position: relative; width:150px; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.d_email_repre"/>:	
				</label>
				<label class="gr" style="position: relative; width:300px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/d_email_repre"/>
				</label>
				<br/>
			</div>
		</xsl:if>
	</div>
	<div class="submenu">
		<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
	</div>
	<div class="cuadro" style="">
		<div class="col">
			<label class="gr" style="position: relative; width:100%; font-weight: bold; left:10px">
				<xsl:value-of select="$lang.representante"/>:	
			</label>
			<label class="gr" style="position: relative; width:100%; left:20px">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/asociacion"/>
			</label>
			<br/>
		</div>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cif">
			<div class="col">
				<label class="gr" style="position: relative; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.cif"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cif"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/domicilioNotificacion">
			<div class="col">
				<label class="gr" style="position: relative; width:100%; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.direccion"/>:	
				</label>
				<label class="gr" style="position: relative; width:100%; left:20px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/domicilioNotificacion"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/localidad">
			<div class="col">
				<label class="gr" style="position: relative; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.localidad"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/localidad"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/provincia">
			<div class="col">
				<label class="gr" style="position: relative; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.provincia"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/provincia"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/codigoPostal">
			<div class="col">
				<label class="gr" style="position: relative; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.cp"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/codigoPostal"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/telefono">
			<div class="col">
				<label class="gr" style="position: relative; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.telefono"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/telefono"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/d_email_repre">
			<div class="col">
				<label class="gr" style="position: relative; width:150px; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.email"/>:	
				</label>
				<label class="gr" style="position: relative; width:300px; left:10px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/emailSolicitante"/>
				</label>
				<br/>
			</div>
		</xsl:if>
	</div>
	<div class="submenu">
		<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
	</div>
	<div class="cuadro" style="">
		<div class="col">
			<label class="gr" align="center">
				<xsl:attribute name="style">position: relative; width:100%; font-size:14px; color:BROWN; font-weight:bold; </xsl:attribute>
				<xsl:value-of select="$lang.clase"/>
			</label>
		</div>
		<div class="col">
			<table>
				<tr>					
					<td>
						<label class="gr" style="position: relative; font-weight: bold; font-size:11px; width:100%;">
							<xsl:value-of select="$lang.tipo_trabajo"/>:	
						</label>
						<br/>
						<label class="gr">
							<xsl:attribute name="style">position: relative; width:100%; font-size:10px; font-weight:bold; font-style:italic;</xsl:attribute>
							<xsl:value-of select="$lang.nota"/>
						</label>
					</td>
				</tr>
				<tr>
					<td>
						<label class="gr">
							<xsl:attribute name="style">position: relative; width:450px; font-size:11px; width:100%;</xsl:attribute>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipo_trabajo"/>	
						</label>
					</td>
				</tr>
			</table>
		</div>
		<div class="col">
			<table>
				<tr>					
					<td style="vertical-align:top;">
						<label class="gr">
							<xsl:attribute name="style">position: relative; width:100px; font-size:11px; font-weight: bold;</xsl:attribute>
							<xsl:value-of select="$lang.cantidad"/>:
						</label>
					</td>
					<td style="vertical-align:top;">
						<label class="gr">
							<xsl:attribute name="style">position: relative; width:100px; font-size:11px; font-weight: bold;</xsl:attribute>	
							<xsl:value-of select="$lang.formato"/>:
						</label>
					</td>
					<td style="vertical-align:top;">
						<label class="gr">
							<xsl:attribute name="style">position: relative; width:150px; font-size:11px; font-weight: bold;</xsl:attribute>	
							<xsl:value-of select="$lang.deposito_legal"/>:
						</label>
						<br/>
						<label class="gr">
							<xsl:attribute name="style">position: relative; width:150px; font-size:10px; font-weight: bold;</xsl:attribute>	
							<xsl:value-of select="$lang.jccm"/>
						</label>
					</td>
				</tr>
				<tr>
					<td>
						<label class="gr">
							<xsl:attribute name="style">position: relative; font-size:11px;</xsl:attribute>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cantidad"/>									</label>
					</td>
					<td>
						<label class="gr">
							<xsl:attribute name="style">position: relative; font-size:11px;</xsl:attribute>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/formato"/>									</label>
					</td>
					<td>
						<label class="gr">
							<xsl:attribute name="style">position: relative; font-size:11px;</xsl:attribute>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/jccm"/>	
						</label>
					</td>
				</tr>
			</table>
		</div>
	</div>
		<br/>
		<div style="color: grey; text-align:justify">
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
				Los datos personales, identificativos y de contacto, aportados mediante esta comunicaci�n se entienden facilitados voluntariamente, y ser�n incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del �mbito de las competencias de esta Administraci�n P�blica as� como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telem�ticos y enviarle invitaciones para eventos y felicitaciones en fechas se�aladas. Entenderemos que presta su consentimiento t�cito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podr� ejercer sus derechos de acceso, rectificaci�n, cancelaci�n y oposici�n ante el Responsable del Fichero, la Diputaci�n Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - Espa�a, siempre acreditando conforme a Derecho su identidad en la comunicaci�n. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigi�ndose a la direcci�n citada ut supra o bien al correo electr�nico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.	
			</label>
		</div>
	</xsl:template>
</xsl:stylesheet>