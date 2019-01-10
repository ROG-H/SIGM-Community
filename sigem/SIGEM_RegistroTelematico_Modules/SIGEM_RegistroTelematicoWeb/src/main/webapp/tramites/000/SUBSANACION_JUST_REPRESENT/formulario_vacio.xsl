<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'SUBSANACI�N DE DEFECTOS Y TRAMITACI�N DE INCIDENTES EN UN EXPEDIENTE INICIADO'"/>

	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Datos de la persona f�sica o del representante de la persona jur�dica'"/>
	<xsl:variable name="lang.datosInteresado" select="'Datos del solicitante o del representado'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.datosDeclarativos" select="'Declaraciones Preceptivas que formula el solicitante'"/>

	<xsl:variable name="lang.representante" select="'En representaci�n de:'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>

	<xsl:variable name="lang.anio" select="'A�o de inicio del expediente:*'"/>

	<xsl:variable name="lang.selecSol" select="'Seleccione el expediente que desea subsanar, justificar o modificar.'"/>
	<xsl:variable name="lang.asunto" select="'Asunto'"/>
	<xsl:variable name="lang.numexp" select="'N�mero de Registro de Entrada en Diputaci�n de la solicitud'"/>
	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>	

	<xsl:variable name="lang.declaro" select="'Asimismo, declaro bajo mi Responsabilidad (obligatorio marcar con una x cada cuadro para poder seguir el tr�mite)'"/>

	<xsl:variable name="lang.declaro1" select="'Que la entidad solicitante no se encuentra incursa en ninguna de las circunstancias de prohibici�n para la obtenci�n de la condici�n de beneficiario de ayuda o subvenci�n, as� como que se encuentra al corriente en el cumplimiento de sus obligaciones fiscales con la Diputaci�n Provincial de Ciudad Real.'"/>
	<xsl:variable name="lang.declaro2" select="'Que esta entidad se encuentra al corriente en el cumplimiento de sus obligaciones tributarias y frente a la Seguridad Social, y no es deudora por resoluci�n de procedencia de reintegro de subvenciones.'"/>
	<xsl:variable name="lang.declaro3" select="'Que la persona o entidad solicitante autoriza expresamente a la Diputaci�n de Ciudad Real para consultar las expresadas circunstancias ante las entidades se�aladas.'"/>	

	<xsl:variable name="lang.anexar" select="'Ficheros que se Anexan'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Debe anexar la documentaci�n en cada caso seg�n lo dispuesto en la convocatoria.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.-  Para adjuntar los ficheros, pulse el bot�n examinar. Seleccione el fichero que desee anexar a la solicitud. Recuerde que debe anexar copia de la Memoria o proyecto al que se refiere la presente solicitud y de cualquier otro que considere conveniente. Si el documento est� en soporte papel, debe escanearlo con car�cter previo.'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>
	<xsl:variable name="lang.documentoTipo_DOC_ODT" select="'Archivo ODT/DOC'"/>
	<xsl:variable name="lang.documentoTipoPDF" select="'Archivo PDF'"/>
	<xsl:variable name="lang.documentoTipoJPEG" select="'Archivo JPG'"/>

	<xsl:variable name="lang.mensajePopUps" select="'Si est� viendo este mensaje debe &lt;b&gt;deshabilitar&lt;/b&gt; el bloqueador de elementos emergentes de su navegador para &lt;b&gt;permitir los PopUps en Diputaci�n de Ciudad Real&lt;/b&gt;.'"/>
	<xsl:variable name="lang.mensajeAlert" select="'Debe deshabilitar el bloqueador de elementos emergentes de su navegador para permitir los PopUps en Diputaci�n de Ciudad Real y poder comprobar sus representados antes de continuar.'"/>

	<xsl:variable name="lang.mensajeRepresentante" select="'No tiene ning�n representado dado de alta en Comparece para este NIF/NIE o bien su solicitud de alta de representaci�n est� siendo tramitada en el departamento correspondiente.&lt;br&gt;Si no ha realizado dicha solicitud cierre el navegador, acceda a Comparece y rellene el formulario &lt;b&gt;Solicitud de alta, modificaci�n y baja de datos de usuarios en Comparece para AYUNTAMIENTOS, ASOCIACIONES Y EMPRESAS&lt;/b&gt;.'"/>

	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(4);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');
			validar[2] = new Array('expone','<xsl:value-of select="$lang.expone"/>');
			validar[3] = new Array('solicita','<xsl:value-of select="$lang.solicita"/>');


			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(10);

			especificos[0] = new Array('ayuntamiento','ayuntamiento');
			especificos[1] = new Array('asunto','asunto');
			especificos[2] = new Array('numExpediente','numExpediente');
			especificos[3] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[4] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[5] = new Array('expone','expone');
			especificos[6] = new Array('solicita','solicita');
			especificos[7] = new Array('emailSolicitante','emailSolicitante');
			especificos[8] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[9] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');



			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				if(document.getElementById('asociaciones_div').style.display == ''){
					alert('<xsl:value-of select="$lang.mensajeAlert"/>');
					return false;
				}
				if(document.getElementById('numExpediente').value == ''){
					alert('Debe seleccionar el expediente que va a subsanar, justificar o modificar');
					document.getElementById('expedientes').focus();
					return false;
				}
				if(document.getElementById('numExpediente').value == '-------'){
					alert('Debe seleccionar el expediente que va a subsanar, justificar o modificar');
					document.getElementById('expedientes').focus();
					return false;
				}
				
				return true;
			}
			function getDatosObligado(nif){
				window.open('tramites/000/SUBSANACION_JUST_REPRESENT/buscaObligado.jsp?valor='+nif+';000','','width=3,height=3');
			}
			
			function abrirDocumento(){ 
				v=window.open("documentos/MODELO.odt"); 
			} 
			function getDatosRepre(cifRepre){
				document.getElementById('anio').value='----';
				document.getElementById('expedientes').value='-------';
				document.getElementById('cif').value = cifRepre;
			}

			function getAsuntoNumexp(numExpediente){
				window.open('tramites/000/SUBSANACION_JUST_REPRESENT/buscaAsuntoNumExp.jsp?valor='+numExpediente+';000','','width=700,height=300,resizable=1');
			}
			function getDatosObligadoEntidad(nif){
				window.open('tramites/000/SUBSANACION_JUST_REPRESENT/buscaObligadoEntidad.jsp?valor='+nif+';000','','width=3,height=3');
			}
			function getDatos(anio){
				var dni = document.getElementById('cif').value;
				if(dni=="-------" || dni==""){<!-- Compruebo que el cif no sea vacio -->
					dni = document.getElementById('documentoIdentidad').value;
				}
				window.open('tramites/000/SUBSANACION_JUST_REPRESENT/dameExpedientes.jsp?valor='+dni+';'+anio.value+';000','','width=3,height=3,left=10000,top=10000');
			}

			function getRepresentados(){
				window.open('tramites/000/SUBSANACION_JUST_REPRESENT/dameRepresentantes.jsp?valor='+document.getElementById('documentoIdentidad').value+';000','','width=3,height=3,left=10000,top=10000');
			}


		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>

   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.id_nif"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px;</xsl:attribute>
					<xsl:attribute name="name">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="id">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Documento_Identificacion/Numero"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.id_nombre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>

		</div>

   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
   		</div>

   		<div class="cuadro" style="">

			<div id="asociaciones_div" class="col">
				<img>
					<xsl:attribute name="src">img/close.png</xsl:attribute>
					<xsl:attribute name="id">aviso_img</xsl:attribute>
				</img>
				<label>
					<xsl:attribute name="style">color:#006699; width:100%; line-height:15px; padding-left:20px;</xsl:attribute>
					<xsl:attribute name="name">asociaciones_lbl</xsl:attribute>
					<xsl:attribute name="id">asociaciones_lbl</xsl:attribute>
				</label>
			</div>

			<div id="mensajeRepresente_div" class="col" style="display:none">
				<img>
					<xsl:attribute name="src">img/close.png</xsl:attribute>
					<xsl:attribute name="id">aviso_img</xsl:attribute>
				</img>

				<label>
					<xsl:attribute name="style">color:#006699; width:100%; line-height:15px; padding-left:20px;</xsl:attribute>
					<xsl:attribute name="name">mensajeRepresente_lbl</xsl:attribute>
					<xsl:attribute name="id">mensajeRepresente_lbl</xsl:attribute>

				</label>
			</div>

			<div id="ayuntamiento_div" class="col" style="display:none">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.representante"/>*
				</label>
				<select onchange="getDatosRepre(this.value)">
					<xsl:attribute name="style">width:400px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">ayuntamiento</xsl:attribute>
					<xsl:attribute name="id">ayuntamiento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ayuntamiento"/></xsl:attribute>
					<option>
						<xsl:attribute name="value">-------</xsl:attribute>----------------------------------------------------
					</option>
				</select>
			</div>
			<div id="remitente_div" class="col" style="display:none">
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

		</div>
		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.anio"/>:
				</label>
				<select>
					<xsl:attribute name="style">color:#006699;</xsl:attribute>
					<xsl:attribute name="name">anio</xsl:attribute>
					<xsl:attribute name="id">anio</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/anio"/></xsl:attribute>
					<xsl:attribute name="onchange">getDatos(anio)</xsl:attribute>
					<options>
						<option value='----'>----</option>
					</options>
				</select>
				<script>
					var anio = 2009;
					var f = new Date();
					var anioAc = f.getFullYear();
					for (var i=1; (anio+i)&lt;=anioAc; i++){
						document.getElementById('anio').options[i] = new Option (anio+i,anio+i);
					}
				</script>
				<br/>
			</div>

	<!-- MQE Cargamos los expedientes sobre los que puede subsanar-->
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%;</xsl:attribute>
					<xsl:value-of select="$lang.selecSol"/>:
				</label>
			</div>

			<select class="gr">
				<xsl:attribute name="style">border:none; width:650px;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">expedientes</xsl:attribute>
				<xsl:attribute name="id">expedientes</xsl:attribute>
				<xsl:attribute name="onblur"> document.getElementById('asunto').value = this.options[this.selectedIndex].text; document.getElementById('numExpediente').value = this.value;</xsl:attribute>
					<option>
						<xsl:attribute name="value">-------</xsl:attribute>----------------------------------------------------
					</option>
			</select>
		</div> 
	<!-- MQE fin modificaciones -->


   		<div class="cuadro" style="">
			<div class="col" style="display:none;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.numexp"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:200px;</xsl:attribute>
					<xsl:attribute name="name">numExpediente</xsl:attribute>
					<xsl:attribute name="id">numExpediente</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/numExpediente"/></xsl:attribute>
				</input>
				<img onclick="getAsuntoNumexp(document.getElementById('numExpediente').value);" src="img/search-mg.gif"/>
			</div>

			<br/>

   			<div class="col" style="display:none;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.asunto"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">asunto</xsl:attribute>
					<xsl:attribute name="id">asunto</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/asunto"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>

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
					<xsl:value-of select="Datos_Registro/datos_especificos/expone"/>
				</textarea>
			</div>
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
					<xsl:value-of select="Datos_Registro/datos_especificos/solicita"/>
				</textarea>
			</div>

			<br/>

			</div>
			<div class="submenu">
   				<h1><xsl:value-of select="$lang.datosDeclarativos"/></h1>
   			</div>
   			<div class="cuadro" style="">	
				<input type="checkbox" id="declaro1" checked="Yes" style="border:none;color:#006699;position: relative; width:20px;" DISABLED="Yes"/>
				<xsl:value-of select="$lang.declaro1"/>

				<br/>

				<input type="checkbox" id="declaro2" checked="Yes" style="border:none;color:#006699;position: relative; width:20px;" DISABLED="Yes"/>
				<xsl:value-of select="$lang.declaro2"/>
				<br/>

				<input type="checkbox" id="declaro3" checked="Yes" style="border:none;color:#006699;position: relative; width:20px;" DISABLED="Yes"/>
				<xsl:value-of select="$lang.declaro3"/>
				<br/>
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
					<xsl:attribute name="name">CR_SUBJUSREPRE_D1</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUSREPRE_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUSREPRE_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUSREPRE_D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip,rar</xsl:attribute>
				</input>
				
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo_DOC_ODT"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_SUBJUSREPRE_D2</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUSREPRE_D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUSREPRE_D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUSREPRE_D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">odt,doc</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoPDF"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_SUBJUSREPRE_D3</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUSREPRE_D3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUSREPRE_D3_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUSREPRE_D3_Tipo</xsl:attribute>
					<xsl:attribute name="value">PDF</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoJPEG"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_SUBJUSREPRE_D4</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUSREPRE_D4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUSREPRE_D4_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUSREPRE_D4_Tipo</xsl:attribute>
					<xsl:attribute name="value">JPG</xsl:attribute>
				</input>
			</div>
   		</div>
   		<br/>
		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN" />
   		<br/>
		<xsl:call-template name="TEXTO_COMPARECE_COMUN" />
   		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name">emailSolicitante</xsl:attribute>
			<xsl:attribute name="id">emailSolicitante</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
		<script language="Javascript">
			document.getElementById('asociaciones_lbl').innerHTML = '<xsl:value-of select="$lang.mensajePopUps"/>';
			document.getElementById('mensajeRepresente_lbl').innerHTML = '<xsl:value-of select="$lang.mensajeRepresentante"/>';
			getRepresentados();
		</script>
	</xsl:template>
</xsl:stylesheet>