<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />

<xsl:include href="../templates_comunes.xsl" /><xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Formulario de solicitud de beneficio fiscal en el Impuesto sobre Bienes Inmuebles (IBI)'"/>

	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

	<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre" select="'Apellidos y Nombre o Denominaci�n Social'"/>
	<xsl:variable name="lang.calle" select="'Calle'"/>
	<xsl:variable name="lang.c_postal" select="'C�digo Postal'"/>
	
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'C�digo postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Tel�fono'"/>
	<xsl:variable name="lang.email" select="'Correo electr�nico'"/>
	<xsl:variable name="lang.representacion" select="'Rellene los siguientes datos si act�a como representante'"/>			
	<xsl:variable name="lang.nif_repr" select="'NIF/CIF de la persona o entidad a quien representa'"/>			
	<xsl:variable name="lang.nombre_repr" select="'Nombre del representado'"/>			
	
	<xsl:variable name="lang.expone1" select="'EXPONE'"/>
	<xsl:variable name="lang.expone2" select="', que considerando que tiene derecho al disfrute del beneficio fiscal en el IBI que se indica (seleccionar de las opciones que figuran aquella a la que crea tiene derecho), en el inmueble que se identificar� a continuaci�n:'"/>
	<xsl:variable name="lang.motivo1" select="'1 - Vivienda de Protecci�n Oficial (IBI-URBANA)'"/>
	<xsl:variable name="lang.motivo2" select="'2 - Empresas de Construcci�n y Promoci�n Inmobiliaria (IBI-URBANA)'"/>
	<xsl:variable name="lang.motivo3" select="'3 - Sociedades Cooperativas (IBI-RUSTICA)'"/>
	<xsl:variable name="lang.motivo4" select="'4 - Familia numerosa (IBI-URBANA)'"/>
	<xsl:variable name="lang.motivo5" select="'5 - Otras distintas de las enunciadas'"/>
	<xsl:variable name="lang.notas_motivos1" select="'Opcion 1: Se deber� aportar copia de la c�dula de calificaci�n definitiva y de las escrituras de compra venta.'"/>
	<xsl:variable name="lang.notas_motivos2" select="'Opcion 2: Se deber� aportar copia de la licencia de obra, certificado de inicio de obras, y en el caso de que �stas hubieren finalizado certificaci�n final de obra.'"/>
	<xsl:variable name="lang.notas_motivos3" select="'Opcion 3: Se deber� aportar certificado de inscripci�n en el registro de cooperativas.'"/>
	<xsl:variable name="lang.notas_motivos4" select="'Opcion 4: Se deber� aportar copia del libro de familia numerosa y certificado de empadronamiento.'"/>
	<xsl:variable name="lang.notas_motivos5" select="'Opcion 5: Indicar cual:'"/>
	<xsl:variable name="lang.liquidaciones" select="'LIQUIDACI�N E INMUEBLE OBJETO DE LA SOLICITUD*'"/>
	<xsl:variable name="lang.municipio" select="'Nombre del municipio'"/>
	<xsl:variable name="lang.liquidacion" select="'N� de liquidaci�n'"/>
	<xsl:variable name="lang.referencia" select="'Referencia catastral'"/>
	<xsl:variable name="lang.situacion" select="'Situaci�n del inmueble'"/>
	<xsl:variable name="lang.solicita1" select="'SOLICITA'"/>
	<xsl:variable name="lang.solicita2" select="', que previa las comprobaciones oportunas, procedan a conceder el beneficio fiscal solicitado para los a�os:'"/>
	<xsl:variable name="lang.solicita3" select="'As� mismo, '"/>
    <xsl:variable name="lang.solicita4" select="' que previa las comprobaciones oportunas, procedan a efectuar la devoluci�n que proceda a la '"/>
	<xsl:variable name="lang.ccc" select="'siguiente entidad y cuenta corriente :'"/>
    <xsl:variable name="lang.cuantia" select="'Cuant�a de la devoluci�n que se solicita:'"/>
	<xsl:variable name="lang.cuenta" select="'C�digo de Cuenta Corriente:'"/>
	<xsl:variable name="lang.notas" select="'NOTAS INFORMATIVAS:'"/>
	<xsl:variable name="lang.notas1" select="'1.- El plazo m�ximo de resoluci�n de su solicitud es de 6 meses, siendo los efectos del silencio administrativo NEGATIVOS.'"/>
	<xsl:variable name="lang.notas2" select="'2.- Es necesario consignar obligatoriamente los campos de n� de liquidaci�n y referencia catastral con sus 20 d�gitos.'"/>
	<xsl:variable name="lang.notas3" select="'3.- En el caso de que la solicitud venga referida a liquidaciones a nombre de otro obligado tributario, deber� acreditarse la representaci�n legal o voluntaria (ver modelo en la p�gina) del mismo.'"/>
	
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.zip), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento" select="'Conjunto de anexos de la solicitud'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>

		
	<xsl:template match="/">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(14);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');

			validar[2] = new Array('nif', '<xsl:value-of select="$lang.nif"/>');
			validar[3] = new Array('nombre','<xsl:value-of select="$lang.nombre"/>');
			validar[4] = new Array('calle','<xsl:value-of select="$lang.calle"/>');
			validar[5] = new Array('c_postal','<xsl:value-of select="$lang.c_postal"/>');

			validar[6] = new Array('motivo','Motivo');
			
			validar[7] = new Array('mun_1','<xsl:value-of select="$lang.municipio"/>');
			validar[8] = new Array('liq_1','<xsl:value-of select="$lang.liquidacion"/>');
			validar[9] = new Array('ref1_1','<xsl:value-of select="$lang.referencia"/>');
			validar[10] = new Array('ref1_1','<xsl:value-of select="$lang.referencia"/>');
			validar[11] = new Array('ref1_1','<xsl:value-of select="$lang.referencia"/>');
			validar[12] = new Array('ref1_1','<xsl:value-of select="$lang.referencia"/>');
			validar[13] = new Array('sit_1','<xsl:value-of select="$lang.situacion"/>');

			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(161);
			
			especificos[0] = new Array('nif','nif');
			especificos[1] = new Array('nombre','nombre');
			especificos[2] = new Array('calle','calle');
			especificos[3] = new Array('numero','numero');
			especificos[4] = new Array('escalera','escalera');
			especificos[5] = new Array('planta_puerta','planta_puerta');
			especificos[6] = new Array('c_postal','c_postal');
			especificos[7] = new Array('movil','movil');
			especificos[8] = new Array('d_email','d_email');
			especificos[9] = new Array('repres_nif','repres_nif');
			especificos[10] = new Array('repres_nombre','repres_nombre');
			especificos[11] = new Array('repres_movil','repres_movil');
			especificos[12] = new Array('repres_d_email','repres_d_email');
			
			especificos[13] = new Array('mun_1','mun_1');
			especificos[14] = new Array('liq_1','liq_1');
			especificos[15] = new Array('ref1_1','ref1_1');
			especificos[16] = new Array('ref2_1','ref2_1');
			especificos[17] = new Array('ref3_1','ref3_1');
			especificos[18] = new Array('ref4_1','ref4_1');
			especificos[19] = new Array('sit_1','sit_1');
			especificos[20] = new Array('mun_2','mun_2');
			especificos[21] = new Array('liq_2','liq_2');
			especificos[22] = new Array('ref1_2','ref1_2');
			especificos[23] = new Array('ref2_2','ref2_2');
			especificos[24] = new Array('ref3_2','ref3_2');
			especificos[25] = new Array('ref4_2','ref4_2');
			especificos[26] = new Array('sit_2','sit_2');
			especificos[27] = new Array('mun_3','mun_3');
			especificos[28] = new Array('liq_3','liq_3');
			especificos[29] = new Array('ref1_3','ref1_3');
			especificos[30] = new Array('ref2_3','ref2_3');
			especificos[31] = new Array('ref3_3','ref3_3');
			especificos[32] = new Array('ref4_3','ref4_3');
			especificos[33] = new Array('sit_3','sit_3');
			especificos[34] = new Array('mun_4','mun_4');
			especificos[35] = new Array('liq_4','liq_4');
			especificos[36] = new Array('ref1_4','ref1_4');
			especificos[37] = new Array('ref2_4','ref2_4');
			especificos[38] = new Array('ref3_4','ref3_4');
			especificos[39] = new Array('ref4_4','ref4_4');
			especificos[40] = new Array('sit_4','sit_4');
			especificos[41] = new Array('mun_5','mun_5');
			especificos[42] = new Array('liq_5','liq_5');
			especificos[43] = new Array('ref1_5','ref1_5');
			especificos[44] = new Array('ref2_5','ref2_5');
			especificos[45] = new Array('ref3_5','ref3_5');
			especificos[46] = new Array('ref4_5','ref4_5');
			especificos[47] = new Array('sit_5','sit_5');
			especificos[48] = new Array('mun_6','mun_6');
			especificos[49] = new Array('liq_6','liq_6');
			especificos[50] = new Array('ref1_6','ref1_6');
			especificos[51] = new Array('ref2_6','ref2_6');
			especificos[52] = new Array('ref3_6','ref3_6');
			especificos[53] = new Array('ref4_6','ref4_6');
			especificos[54] = new Array('sit_6','sit_6');
			especificos[55] = new Array('mun_7','mun_7');
			especificos[56] = new Array('liq_7','liq_7');
			especificos[57] = new Array('ref1_7','ref1_7');
			especificos[58] = new Array('ref2_7','ref2_7');
			especificos[59] = new Array('ref3_7','ref3_7');
			especificos[60] = new Array('ref4_7','ref4_7');
			especificos[61] = new Array('sit_7','sit_7');
			especificos[62] = new Array('mun_8','mun_8');
			especificos[63] = new Array('liq_8','liq_8');
			especificos[64] = new Array('ref1_8','ref1_8');
			especificos[65] = new Array('ref2_8','ref2_8');
			especificos[66] = new Array('ref3_8','ref3_8');
			especificos[67] = new Array('ref4_8','ref4_8');
			especificos[68] = new Array('sit_8','sit_8');
			especificos[69] = new Array('mun_9','mun_9');
			especificos[70] = new Array('liq_9','liq_9');
			especificos[71] = new Array('ref1_9','ref1_9');
			especificos[72] = new Array('ref2_9','ref2_9');
			especificos[73] = new Array('ref3_9','ref3_9');
			especificos[74] = new Array('ref4_9','ref4_9');
			especificos[75] = new Array('sit_9','sit_9');
			especificos[76] = new Array('mun_10','mun_10');
			especificos[77] = new Array('liq_10','liq_10');
			especificos[78] = new Array('ref1_10','ref1_10');
			especificos[79] = new Array('ref2_10','ref2_10');
			especificos[80] = new Array('ref3_10','ref3_10');
			especificos[81] = new Array('ref4_10','ref4_10');
			especificos[82] = new Array('sit_10','sit_10');
			especificos[83] = new Array('mun_11','mun_11');
			especificos[84] = new Array('liq_11','liq_11');
			especificos[85] = new Array('ref1_11','ref1_11');
			especificos[86] = new Array('ref2_11','ref2_11');
			especificos[87] = new Array('ref3_11','ref3_11');
			especificos[88] = new Array('ref4_11','ref4_11');
			especificos[89] = new Array('sit_11','sit_11');
			especificos[90] = new Array('mun_12','mun_12');
			especificos[91] = new Array('liq_12','liq_12');
			especificos[92] = new Array('ref1_12','ref1_12');
			especificos[93] = new Array('ref2_12','ref2_12');
			especificos[94] = new Array('ref3_12','ref3_12');
			especificos[95] = new Array('ref4_12','ref4_12');
			especificos[96] = new Array('sit_12','sit_12');
			especificos[97] = new Array('mun_13','mun_13');
			especificos[98] = new Array('liq_13','liq_13');
			especificos[99] = new Array('ref1_13','ref1_13');
			especificos[100] = new Array('ref2_13','ref2_13');
			especificos[101] = new Array('ref3_13','ref3_13');
			especificos[102] = new Array('ref4_13','ref4_13');
			especificos[103] = new Array('sit_13','sit_13');
			especificos[104] = new Array('mun_14','mun_14');
			especificos[105] = new Array('liq_14','liq_14');
			especificos[106] = new Array('ref1_14','ref1_14');
			especificos[107] = new Array('ref2_14','ref2_14');
			especificos[108] = new Array('ref3_14','ref3_14');
			especificos[109] = new Array('ref4_14','ref4_14');
			especificos[110] = new Array('sit_14','sit_14');
			especificos[111] = new Array('mun_15','mun_15');
			especificos[112] = new Array('liq_15','liq_15');
			especificos[113] = new Array('ref1_15','ref1_15');
			especificos[114] = new Array('ref2_15','ref2_15');
			especificos[115] = new Array('ref3_15','ref3_15');
			especificos[116] = new Array('ref4_15','ref4_15');
			especificos[117] = new Array('sit_15','sit_15');
			especificos[118] = new Array('mun_16','mun_16');
			especificos[119] = new Array('liq_16','liq_16');
			especificos[120] = new Array('ref1_16','ref1_16');
			especificos[121] = new Array('ref2_16','ref2_16');
			especificos[122] = new Array('ref3_16','ref3_16');
			especificos[123] = new Array('ref4_16','ref4_16');
			especificos[124] = new Array('sit_16','sit_16');
			especificos[125] = new Array('mun_17','mun_17');
			especificos[126] = new Array('liq_17','liq_17');
			especificos[127] = new Array('ref1_17','ref1_17');
			especificos[128] = new Array('ref2_17','ref2_17');
			especificos[129] = new Array('ref3_17','ref3_17');
			especificos[130] = new Array('ref4_17','ref4_17');
			especificos[131] = new Array('sit_17','sit_17');
			especificos[132] = new Array('mun_18','mun_18');
			especificos[133] = new Array('liq_18','liq_18');
			especificos[134] = new Array('ref1_18','ref1_18');
			especificos[135] = new Array('ref2_18','ref2_18');
			especificos[136] = new Array('ref3_18','ref3_18');
			especificos[137] = new Array('ref4_18','ref4_18');
			especificos[138] = new Array('sit_18','sit_18');
			especificos[139] = new Array('mun_19','mun_19');
			especificos[140] = new Array('liq_19','liq_19');
			especificos[141] = new Array('ref1_19','ref1_19');
			especificos[142] = new Array('ref2_19','ref2_19');
			especificos[143] = new Array('ref3_19','ref3_19');
			especificos[144] = new Array('ref4_19','ref4_19');
			especificos[145] = new Array('sit_19','sit_19');
			especificos[146] = new Array('mun_20','mun_20');
			especificos[147] = new Array('liq_20','liq_20');
			especificos[148] = new Array('ref1_20','ref1_20');
			especificos[149] = new Array('ref2_20','ref2_20');
			especificos[150] = new Array('ref3_20','ref3_20');
			especificos[151] = new Array('ref4_20','ref4_20');
			especificos[152] = new Array('sit_20','sit_20');
			
			especificos[153] = new Array('motivo','motivo');
			especificos[154] = new Array('otro_motivo','otro_motivo');
			especificos[155] = new Array('years','years');
			especificos[156] = new Array('cuantia','cuantia');
			especificos[157] = new Array('ccc1','ccc1');
			especificos[158] = new Array('ccc2','ccc2');
			especificos[159] = new Array('ccc3','ccc3');
			especificos[160] = new Array('ccc4','ccc4');
			
			<xsl:call-template name="ADD_ROW" />
			
			<xsl:call-template name="VALIDACIONES" />
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			
			function verificacionesEspecificas() {
				var f = document.forms[0];
				return validarCuenta(f);
			}
		</script>
		
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
		<xsl:call-template name="DATOS_SOLICITANTE" />
		<xsl:call-template name="DATOS_OBLIGADO" />
		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="">	
	   		<b><xsl:value-of select="$lang.expone1"/></b><xsl:value-of select="$lang.expone2"/><br/>
			<select class="gr">
				<xsl:attribute name="style">margin-top:10px; width:400px;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">motivo</xsl:attribute>
				<xsl:attribute name="id">motivo</xsl:attribute>
				<option selected="selected" value=""></option>
				<option value="MOTIVO_1"><xsl:value-of select="$lang.motivo1"/></option>
				<option value="MOTIVO_2"><xsl:value-of select="$lang.motivo2"/></option>
				<option value="MOTIVO_3"><xsl:value-of select="$lang.motivo3"/></option>
				<option value="MOTIVO_4"><xsl:value-of select="$lang.motivo4"/></option>
				<option value="MOTIVO_5"><xsl:value-of select="$lang.motivo5"/></option>
			</select>
			<br/>
			<div style="margin-left:10px;margin-top:10px;">
				<i><xsl:value-of select="$lang.notas_motivos1"/></i><br/>
				<i><xsl:value-of select="$lang.notas_motivos2"/></i><br/>
				<i><xsl:value-of select="$lang.notas_motivos3"/></i><br/>
				<i><xsl:value-of select="$lang.notas_motivos4"/></i><br/>
				<i><xsl:value-of select="$lang.notas_motivos5"/></i>
			</div>
			<textarea>
				<xsl:attribute name="style">margin-left:75px;margin-top:5px;width:400px;font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">otro_motivo</xsl:attribute>
				<xsl:attribute name="id">otro_motivo</xsl:attribute>
				<xsl:attribute name="rows">3</xsl:attribute>
				<xsl:value-of select="Datos_Registro/datos_especificos/otro_motivo"/>
			</textarea>

			<div style="margin-top:30px; width:100%; text-align:center;color:#006699;"><xsl:value-of select="$lang.liquidaciones"/></div>
			<table style="font-size:10px; width:95%; table-layout: fixed;" cellspacing="0" border="1">
			    <tr>
					<td style="width:140px;background-color:#dee1e8"><xsl:value-of select="$lang.municipio"/></td>
					<td style="width:115px;background-color:#dee1e8"><xsl:value-of select="$lang.liquidacion"/></td>
					<td style="width:170px;background-color:#dee1e8"><xsl:value-of select="$lang.referencia"/></td>
					<td style="width:150px;background-color:#dee1e8"><xsl:value-of select="$lang.situacion"/></td>
				</tr>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">1</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">2</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">3</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">4</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">5</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">6</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">7</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">8</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">9</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">10</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">11</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">12</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">13</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">14</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">15</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">16</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">17</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">18</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">19</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">20</xsl:with-param></xsl:call-template>
			</table>
            <div style="margin:5px 0 20px 10px;">
                <a id="addRow_link" style="cursor:pointer" onclick="addRow()">[A�adir fila]</a><br/>
            </div>

			<xsl:call-template name="SOLICITA1" />
	   		<br/>
			
			<xsl:call-template name="SOLICITA3" />
	   		<br/>
			
			<div style="margin-bottom:10px;margin-top:30px;"><i><xsl:value-of select="$lang.notas"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas1"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas2"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas3"/></i></div>
		</div>
   		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<label class="gr">
			   		<xsl:attribute name="style">width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/><br/>
		   			<xsl:value-of select="$lang.anexarInfo2"/><br/>
			</label>
			<br/>
   			<div class="col">
	   			<label class="gr">
					<xsl:attribute name="style">width:250px;</xsl:attribute>
					<xsl:value-of select="$lang.documento"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">width:400px; </xsl:attribute>
					<xsl:attribute name="name">REC-05-D1</xsl:attribute>
					<xsl:attribute name="id">REC-05-D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">REC-05-D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">REC-05-D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>
   		</div>
   		<br/>
		<input type="hidden"><xsl:attribute name="id">telefono</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">domicilioNotificacion</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">localidad</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">provincia</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">codigoPostal</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">emailSolicitante</xsl:attribute></input>
   		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
	</xsl:template>
	
	<xsl:template name="FIELDS">
	    <xsl:param name="row_id" />
	    <xsl:variable name="param_mun">mun_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_liq">liq_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_ref1">ref1_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_ref2">ref2_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_ref3">ref3_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_ref4">ref4_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_sit">sit_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="row_style">
	    <xsl:choose>
            <xsl:when test="$row_id&gt;'5'">
   		        display:none;
            </xsl:when>
        </xsl:choose>
	    </xsl:variable>
	    <tr id="row_{$row_id}" style="{$row_style}">
	        <td>
		        <select class="gr">
			        <xsl:attribute name="style">border:none;color:#006699;width:140px;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_mun"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_mun"/></xsl:attribute>
			        <xsl:call-template name="OPTIONS_MUNICIPIOS" />
		        </select>
	        </td>
	        <td>
		        <input type="text">
			        <xsl:attribute name="style">border:none;color:#006699;width:110px;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_liq"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_liq"/></xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_liq]"/></xsl:attribute>
		        </input>
	        </td>
	        <td>
		        <input type="text">
			        <xsl:attribute name="style">border:none;margin:0;width:90px;color:#006699;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_ref1"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_ref1"/></xsl:attribute>
			        <xsl:attribute name="size">14</xsl:attribute>
			        <xsl:attribute name="maxlength">14</xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref1]"/></xsl:attribute>
		        </input>�
		        <input type="text">
			        <xsl:attribute name="style">border:none;margin:0;width:32px;color:#006699;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_ref2"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_ref2"/></xsl:attribute>
			        <xsl:attribute name="size">4</xsl:attribute>
			        <xsl:attribute name="maxlength">4</xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref2]"/></xsl:attribute>
		        </input>�
		        <input type="text">
			        <xsl:attribute name="style">border:none;margin:0;width:12px;color:#006699;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_ref3"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_ref3"/></xsl:attribute>
			        <xsl:attribute name="size">1</xsl:attribute>
			        <xsl:attribute name="maxlength">1</xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref3]"/></xsl:attribute>
		        </input>�
		        <input type="text">
			        <xsl:attribute name="style">border:none;margin:0;width:12px;color:#006699;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_ref4"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_ref4"/></xsl:attribute>
			        <xsl:attribute name="size">1</xsl:attribute>
			        <xsl:attribute name="maxlength">1</xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref4]"/></xsl:attribute>
		        </input>
	        </td>
	        <td>
		        <input type="text">
			        <xsl:attribute name="style">border:none;color:#006699;width:150px;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_sit"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_sit"/></xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_sit]"/></xsl:attribute>
		        </input>
	        </td>
	    </tr>
	</xsl:template>

</xsl:stylesheet>
