<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'EUS-Formulario de Contrataci�n administrativa mediante el procedimiento negociado'"/>
	<xsl:variable name="lang.datosContratante" select="'EUS-Datos del Contratante'"/>
	<xsl:variable name="lang.datosContratado" select="'EUS-Datos del Contratado'"/>
	<xsl:variable name="lang.docIdentidad" select="'EUS-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'EUS-Nombre'"/>
	<xsl:variable name="lang.relacion" select="'EUS-Relaci�n'"/>
	<xsl:variable name="lang.domicilio" select="'EUS-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'EUS-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'EUS-Provincia'"/>
	<xsl:variable name="lang.cp" select="'EUS-C�digo postal'"/>
	<xsl:variable name="lang.datosContrato" select="'EUS-Datos del Contrato'"/>
	<xsl:variable name="lang.tipoContrato" select="'EUS-Tipo de Contrato'"/>
	
	<xsl:variable name="lang.TColaboracion" select="'EUS-Colaboraci�n entre el sector p�blico y el sector privado'" />
	<xsl:variable name="lang.TConcesion" select="'EUS-Concesi�n de obras p�blicas'" />
	<xsl:variable name="lang.TGestion" select="'EUS-Gesti�n de servicios p�blicos'" />
	<xsl:variable name="lang.TObras" select="'EUS-Obras'" />
	<xsl:variable name="lang.TServicio" select="'EUS-Servicio'" />
	<xsl:variable name="lang.TSuministros" select="'EUS-Suministros'" />
	
	<xsl:variable name="lang.formaAdjudicacion" select="'EUS-Forma de Adjudicaci�n'"/>
	<xsl:variable name="lang.TConcurso" select="'EUS-Concurso'"/>
	<xsl:variable name="lang.TSubasta" select="'EUS-Subasta'"/>
	
	<xsl:variable name="lang.presupuestoMax" select="'EUS-Presupuesto m�ximo'"/>
	<xsl:variable name="lang.precioAdjudicacion" select="'EUS-Precio de la Adjudicaci�n'"/>
	<xsl:variable name="lang.aplicacion" select="'EUS-Aplicaci�n presupuestaria'"/>
	<xsl:variable name="lang.plazo" select="'EUS-Plazo de Ejecuci�n'"/>
	<xsl:variable name="lang.unidades" select="'EUS-Unidades de Plazo'"/>
	<xsl:variable name="lang.garantiaProv" select="'EUS-Garant�a Provisional'"/>
	<xsl:variable name="lang.garantiaDef" select="'EUS-Garant�a Definitiva'"/>
	<xsl:variable name="lang.clasificacion" select="'EUS-Clasificaci�n'"/>
	<xsl:variable name="lang.fechaFinEjecucion" select="'EUS-Fecha final de Ejecuci�n'"/>
	<xsl:variable name="lang.fechaFinGarantia" select="'EUS-Fecha final de Garant�a'"/>
	
	<xsl:variable name="lang.TDiasNaturales" select="'EUS-D�as naturales'"/>
	<xsl:variable name="lang.TDiasLaborales" select="'EUS-D�as Laborales'"/>
	<xsl:variable name="lang.TMeses" select="'EUS-Meses'"/>
	<xsl:variable name="lang.TAnos" select="'EUS-A�os'"/>
	
	<xsl:variable name="lang.anexar" select="'EUS-Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'EUS-1.- Para adjuntar un fichero (*.pdf), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'EUS-2.- Seleccione el fichero que desea anexar al contrato'"/>
	
	<xsl:variable name="lang.escritura" select="'EUS-Escritura o documento de constituci�n'"/>
	<xsl:variable name="lang.obrar" select="'EUS-Acreditaci�n de la capacidad de obrar'"/>
	<xsl:variable name="lang.estatutos" select="'EUS-Estatutos'"/>
	<xsl:variable name="lang.solvencia" select="'EUS-Acreditaci�n de solvencia'"/>
	<xsl:variable name="lang.noConcurrencia" select="'EUS-Prueba de la no concurrencia de una prohibici�n de contratar'"/>
	<xsl:variable name="lang.seguro" select="'EUS-Declaraciones apropiadas de entidades financieras o, en su caso, justificante de la existencia de un seguro de indemnizaci�n por riesgos profesionales'"/>
	<xsl:variable name="lang.cuentas" select="'EUS-Las cuentas anuales presentadas en el Registro Mercantil o en el Registro oficial que corresponda'"/>
	<xsl:variable name="lang.volumen" select="'EUS-Declaraci�n sobre el volumen global de negocios'"/>
	<xsl:variable name="lang.5anos" select="'EUS-Relaci�n de las obras ejecutadas en el curso de los cinco �ltimos a�os'"/>
	<xsl:variable name="lang.unidadesTecnicas" select="'EUS-Declaraci�n indicando los t�cnicos o las unidades t�cnicas de los que �sta disponga para la ejecuci�n de las obras'"/>
	<xsl:variable name="lang.titulos" select="'EUS-T�tulos acad�micos y profesionales del emepresario y de los directivos de la empresa y, en particular, del responsable o responsables de las obras'"/>
	<xsl:variable name="lang.medidas" select="'EUS-Indicaci�n de las medidas de gesti�n medioambientales'"/>
	<xsl:variable name="lang.plantilla" select="'EUS-Declaraci�n sobre la plantilla media anual de la empresa y la importancia de su personal directivo durante los tres �ltimos a�os, acompa�ada de la documentaci�n justificada correspondiente'"/>
	<xsl:variable name="lang.material" select="'EUS-Declaraci�n indicando la maquinaria, material y equipo t�cnico del que se dispondr� para la ejecuci�n de las obras'"/>
	
	<xsl:variable name="lang.envio" select="'EUS-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'EUS-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'EUS-D.E.U.'"/>
	<xsl:variable name="lang.telefono" select="'EUS-Tel�fono'"/>
	<xsl:variable name="lang.email" select="'EUS-Email'"/>	
	<xsl:variable name="lang.importante" select="'EUS-IMPORTANTE'"/>	
	<xsl:variable name="lang.nota" select="' EUS-S�lo podr�n solicitar el contrato con el sector p�blico aquellas personas naturales o jur�dicas, espa�olas o extranjeras, que tengan plena capacidad de obrar, no est�n incursas en una prohibici�n de contratar, y acrediten su solvencia econ�mica, financiera y t�cnica o profesional o, en los casos en que as� lo exija esta Ley, se encuentren debidamente clasificadas.'"/>	
	<xsl:variable name="lang.unidades" select="'EUS-Unidades Plazo'"/>
	
	
	<xsl:variable name="lang.TResponsable" select="'EUS-Responsable del contrato'"/>
	<xsl:variable name="lang.TContratante" select="'EUS-Contratante'"/>
	<xsl:variable name="lang.TLicitador" select="'EUS-Licitador'"/>
	<xsl:variable name="lang.TAdjudicatario" select="'EUS-Adjudicatario'"/>
	
</xsl:stylesheet>