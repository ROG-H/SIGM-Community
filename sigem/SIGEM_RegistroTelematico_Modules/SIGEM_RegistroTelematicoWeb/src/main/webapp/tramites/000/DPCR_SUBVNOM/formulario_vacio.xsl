<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'SOLICITUD SUBVENCI�N NOMINATIVA ENTIDADES LOCALES'"/>

	<xsl:variable name="lang.datosInteresado" select="'Datos del interesado'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.datosDeclarativos" select="'Declaraciones Perceptivas que formule el solicitante'"/>

	<xsl:variable name="lang.ayuntamiento" select="'Nombre del Ayuntamiento'"/>	
	<xsl:variable name="lang.nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.representante" select="'En representaci�n del Ayuntamiento de'"/>
	<xsl:variable name="lang.cif" select="'con CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'C�digo postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Tel�fono'"/>
	<xsl:variable name="lang.email" select="'Correo electr�nico'"/>

	<xsl:variable name="lang.asunto" select="'Asunto'"/>
	<xsl:variable name="lang.finalidad" select="'Finalidad (Obra, Suministro o Actividad)'"/>	
	<xsl:variable name="lang.presupuesto" select="'Presupuesto Total Estimado'"/>
	<xsl:variable name="lang.subvencion" select="'Subvenci�n que se solicita'"/>
	<xsl:variable name="lang.fecha" select="'Fecha prevista de realizaci�n actividad / suministro / terminaci�n de las obras'"/>
	
	
	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	<xsl:variable name="lang.declaro" select="'Asimismo, declaro bajo mi Responsabilidad (obligatorio marcar con una x cada cuadro para poder seguir el tr�mite)'"/>
	
	<xsl:variable name="lang.declaro1" select="'Que la entidad solicitante no se encuentra incursa en ninguna de las circunstancias de prohibici�n para la obtenci�n de la condici�n de beneficiario de ayuda o subvenci�n, as� como que se encuentra al corriente en el cumplimiento de sus obligaciones fiscales con la Diputaci�n Provincial de Ciudad Real.'"/>
	<xsl:variable name="lang.declaro2" select="'Que esta entidad se encuentra al corriente en el cumplimiento de sus obligaciones tributarias y frente a la Seguridad Social, y no es deudora por resoluci�n de procedencia de reintegro de subvenciones.'"/>	

	<xsl:variable name="lang.anexar" select="'Ficheros que se Anexan'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Debe anexar la Memoria Descriptiva y Valorada del proyecto, programa, servicio o actividad al que se refiere la presente solicitud.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.-  Para adjuntar los ficheros, pulse el bot�n examinar. Seleccione el fichero que desee anexar a la solicitud. Recuerde que debe anexar copia de la Memoria o proyecto al que se refiere la presente solicitud y de cualquier otro que considere conveniente. Si el documento est� en soporte papel, debe escanearlo con car�cter previo.'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>
	<xsl:variable name="lang.documentoTipo_DOC_ODT" select="'Archivo ODT/DOC'"/>
	<xsl:variable name="lang.documentoTipoPDF" select="'Archivo PDF'"/>
	<xsl:variable name="lang.documentoTipoJPEG" select="'Archivo JPEG'"/>
	
		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(13);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.nombre"/>');
			validar[2] = new Array('domicilioNotificacion','<xsl:value-of select="$lang.direccion"/>');
			validar[3] = new Array('localidad','<xsl:value-of select="$lang.localidad"/>');
			validar[4] = new Array('provincia','<xsl:value-of select="$lang.provincia"/>');
			validar[5] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>');

			validar[6] = new Array('asunto','<xsl:value-of select="$lang.asunto"/>');
			validar[7] = new Array('finalidad', '<xsl:value-of select="$lang.finalidad"/>');
			validar[8] = new Array('presupuesto','<xsl:value-of select="$lang.presupuesto"/>');
			validar[9] = new Array('subvencion','<xsl:value-of select="$lang.subvencion"/>');
			validar[10] = new Array('fecha','<xsl:value-of select="$lang.fecha"/>');
			
			validar[11] = new Array('expone','<xsl:value-of select="$lang.expone"/>');
			validar[12] = new Array('solicita','<xsl:value-of select="$lang.solicita"/>');


			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(9);
			
			especificos[0] = new Array('ayuntamiento','ayuntamiento');
			especificos[1] = new Array('domicilioNotificacion','domicilioNotificacion');
			especificos[2] = new Array('localidad','localidad');
			especificos[3] = new Array('provincia','provincia');
			especificos[4] = new Array('codigoPostal','codigoPostal');
			especificos[5] = new Array('telefono','telefono');
			
			
			especificos[6] = new Array('asunto','asunto');
			especificos[7] = new Array('finalidad','finalidad');
			especificos[8] = new Array('presupuesto','presupuesto');
			especificos[9] = new Array('subvencion','subvencion');
			especificos[10] = new Array('fecha','fecha');
						
			especificos[11] = new Array('expone','expone');
			especificos[12] = new Array('solicita','solicita');
			
			
			especificos[13] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[14] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[15] = new Array('emailSolicitante','emailSolicitante');
			
			especificos[16] = new Array('cif','cif');
			
			especificos[17] = new Array('declaro1','declaro1');
			especificos[18] = new Array('declaro2','declaro2');
			
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				return true;
			}
			
			function abrirDocumento(){ 
				v=window.open("documentos/MODELO.odt"); 
			} 
			function getDatosAyto(valueAyto){
				window.open('tramites/001/ayuntamiento.jsp?valor='+valueAyto+';001','','width=3,height=3,left=10000,top=10000');
			}
		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
   		</div>
   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nif"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:attribute name="name">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="id">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Documento_Identificacion/Numero"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_VLDTBL_MUNICIPIOS','000')"/>
			
			
			<!-- <xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.api.registroTelematico.XmlAyuntamiento.getAyuntamientos()"/> -->
			<xsl:variable name="b" select="document($fileAyuntam)"/>
		
			
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.representante"/>:*
				</label>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select onchange="getDatosAyto(this.value)">
					<xsl:attribute name="style">width:400px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">ayuntamiento</xsl:attribute>
					<xsl:attribute name="id">ayuntamiento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ayuntamiento"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>	
			</div>
			<!-- <br/><br/><br/>hola mundo<br/><br/>
   		<input id='hola'/>
    		<button onclick='abreVentana()'>Buscar</button>
    		<input id='adios' />
			        		
	    <br/><br/><br/>hola mundo<br/><br/>
	     -->
	    
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cif"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">cif</xsl:attribute>
					<xsl:attribute name="id">cif</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/cif"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col" style="height: 35px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.direccion"/>:*	
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">domicilioNotificacion</xsl:attribute>
					<xsl:attribute name="id">domicilioNotificacion</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/Domicilio_Notificacion"/>
				</textarea>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.localidad"/>:*	
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">localidad</xsl:attribute>
					<xsl:attribute name="id">localidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Localidad"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.provincia"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">provincia</xsl:attribute>
					<xsl:attribute name="id">provincia</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Provincia"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cp"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">codigoPostal</xsl:attribute>
					<xsl:attribute name="id">codigoPostal</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Codigo_Postal"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.telefono"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">telefono</xsl:attribute>
					<xsl:attribute name="id">telefono</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Telefono"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.email"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">emailSolicitante</xsl:attribute>
					<xsl:attribute name="id">emailSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Correo_Electronico"/></xsl:attribute>
				</input>
			</div>
		</div>
		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="">	
   			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.asunto"/>:*
				</label>
				<label type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">asunto</xsl:attribute>
					<xsl:attribute name="id">asunto</xsl:attribute>
					Solicitud de Subvenci�n Nominativa para Entidades Locales
				</label>
				
			</div>	
			
			<br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.finalidad"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">finalidad</xsl:attribute>
					<xsl:attribute name="id">finalidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/finalidad"/></xsl:attribute>
				</input>
			</div>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.presupuesto"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">presupuesto</xsl:attribute>
					<xsl:attribute name="id">presupuesto</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/presupuesto"/></xsl:attribute>
				</input>
			</div>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.subvencion"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">subvencion</xsl:attribute>
					<xsl:attribute name="id">subvencion</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/subvencion"/></xsl:attribute>
				</input>
			</div>	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.fecha"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">fecha</xsl:attribute>
					<xsl:attribute name="id">fecha</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/fecha"/></xsl:attribute>
				</input>
			</div>
			<br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.expone"/>:*
				</label>
				<textarea type="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">expone</xsl:attribute>
					<xsl:attribute name="id">expone</xsl:attribute>
					<xsl:attribute name="rows">7</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/expone"/>Que ante la carencia de recursos econ�micos suficientes para costear la inversi�n antecitada.
				</textarea>
			</div>
			<br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.solicita"/>:*	
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">solicita</xsl:attribute>
					<xsl:attribute name="id">solicita</xsl:attribute>
					<xsl:attribute name="rows">7</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/solicita"/>Que la Diputaci�n Povincial de Ciudad Real conceda a esta Entidad Local la subvenci�n indicada o, en su defecto, la mayor cuant�a que sea posible.
				</textarea>
			</div>
			
			</div>
			<div class="submenu">
   				<h1><xsl:value-of select="$lang.datosDeclarativos"/></h1>
   			</div>
   			<div class="cuadro" style="">	
				<input type="checkbox" id="declaro1" checked="Yes" style="border:none;color:#006699;position: relative; width:20px;"/>
				<label for="declaro1" style=""><xsl:value-of select="$lang.declaro1"/></label>
				
				<br/>
				
				<input type="checkbox" id="declaro2" checked="Yes" style="border:none;color:#006699;position: relative; width:20px;"/>
				<xsl:value-of select="$lang.declaro2"/>
				<br/>
			</div>
		
   			<br/>
			<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   			</div>
   			<div class="cuadro" style="">
				<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/>
		   			<!-- <a style="text-decoration:underline; color:red" href="javascript:abrirDocumento();">Modelo orientativo de acuerdo</a><br/>  -->
		   			<br/>
		   			<xsl:value-of select="$lang.anexarInfo2"/><br/>
				</label>
			<br/><br/>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_ASES_02_D1</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_02_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_ASES_02_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_02_D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
				
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo_DOC_ODT"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_ASES_01_D2</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_ASES_01_D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">odt,doc</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoPDF"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_ASES_01_D3</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_ASES_01_D3_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D3_Tipo</xsl:attribute>
					<xsl:attribute name="value">PDF</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoJPEG"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_ASES_01_D4</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_ASES_01_D4_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D4_Tipo</xsl:attribute>
					<xsl:attribute name="value">JPEG</xsl:attribute>
				</input>
			</div>
			<br/>
			<div style="color: grey; text-align:justify">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
					Los datos personales, identificativos y de contacto, aportados mediante esta comunicaci�n se entienden facilitados voluntariamente, y ser�n incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del �mbito de las competencias de esta Administraci�n P�blica as� como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telem�ticos y enviarle invitaciones para eventos y felicitaciones en fechas se�aladas. Entenderemos que presta su consentimiento t�cito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podr� ejercer sus derechos de acceso, rectificaci�n, cancelaci�n y oposici�n ante el Responsable del Fichero, la Diputaci�n Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - Espa�a, siempre acreditando conforme a Derecho su identidad en la comunicaci�n. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigi�ndose a la direcci�n citada ut supra o bien al correo electr�nico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.	
				</label>
			</div>
   		</div>
   		<br/>
   		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
	</xsl:template>
</xsl:stylesheet>
