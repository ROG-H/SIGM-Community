<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'CAT-Formulario de Contrataci�n administrativa mediante el procedimiento negociado'"/>
	<xsl:variable name="lang.datosContratante" select="'CAT-Datos del Contratante'"/>
	<xsl:variable name="lang.datosContratado" select="'CAT-Datos del Contratado'"/>
	<xsl:variable name="lang.docIdentidad" select="'CAT-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'CAT-Nombre'"/>
	<xsl:variable name="lang.relacion" select="'CAT-Relaci�n'"/>
	<xsl:variable name="lang.domicilio" select="'CAT-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'CAT-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'CAT-Provincia'"/>
	<xsl:variable name="lang.cp" select="'CAT-C�digo postal'"/>
	<xsl:variable name="lang.datosContrato" select="'CAT-Datos del Contrato'"/>
	<xsl:variable name="lang.tipoContrato" select="'CAT-Tipo de Contrato'"/>
	
	<xsl:variable name="lang.TColaboracion" select="'CAT-Colaboraci�n entre el sector p�blico y el sector privado'" />
	<xsl:variable name="lang.TConcesion" select="'CAT-Concesi�n de obras p�blicas'" />
	<xsl:variable name="lang.TGestion" select="'CAT-Gesti�n de servicios p�blicos'" />
	<xsl:variable name="lang.TObras" select="'CAT-Obras'" />
	<xsl:variable name="lang.TServicio" select="'CAT-Servicio'" />
	<xsl:variable name="lang.TSuministros" select="'CAT-Suministros'" />
	
	<xsl:variable name="lang.formaAdjudicacion" select="'CAT-Forma de Adjudicaci�n'"/>
	<xsl:variable name="lang.TConcurso" select="'CAT-Concurso'"/>
	<xsl:variable name="lang.TSubasta" select="'CAT-Subasta'"/>
	
	<xsl:variable name="lang.presupuestoMax" select="'CAT-Presupuesto m�ximo'"/>
	<xsl:variable name="lang.precioAdjudicacion" select="'CAT-Precio de la Adjudicaci�n'"/>
	<xsl:variable name="lang.aplicacion" select="'CAT-Aplicaci�n presupuestaria'"/>
	<xsl:variable name="lang.plazo" select="'CAT-Plazo de Ejecuci�n'"/>
	<xsl:variable name="lang.unidades" select="'CAT-Unidades de Plazo'"/>
	<xsl:variable name="lang.garantiaProv" select="'CAT-Garant�a Provisional'"/>
	<xsl:variable name="lang.garantiaDef" select="'CAT-Garant�a Definitiva'"/>
	<xsl:variable name="lang.clasificacion" select="'CAT-Clasificaci�n'"/>
	<xsl:variable name="lang.fechaFinEjecucion" select="'CAT-Fecha final de Ejecuci�n'"/>
	<xsl:variable name="lang.fechaFinGarantia" select="'CAT-Fecha final de Garant�a'"/>
	
	<xsl:variable name="lang.TDiasNaturales" select="'CAT-D�as naturales'"/>
	<xsl:variable name="lang.TDiasLaborales" select="'CAT-D�as Laborales'"/>
	<xsl:variable name="lang.TMeses" select="'CAT-Meses'"/>
	<xsl:variable name="lang.TAnos" select="'CAT-A�os'"/>
	
	<xsl:variable name="lang.anexar" select="'CAT-Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'CAT-1.- Para adjuntar un fichero (*.pdf), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'CAT-2.- Seleccione el fichero que desea anexar al contrato'"/>
	
	<xsl:variable name="lang.escritura" select="'CAT-Escritura o documento de constituci�n'"/>
	<xsl:variable name="lang.obrar" select="'CAT-Acreditaci�n de la capacidad de obrar'"/>
	<xsl:variable name="lang.estatutos" select="'CAT-Estatutos'"/>
	<xsl:variable name="lang.solvencia" select="'CAT-Acreditaci�n de solvencia'"/>
	<xsl:variable name="lang.noConcurrencia" select="'CAT-Prueba de la no concurrencia de una prohibici�n de contratar'"/>
	<xsl:variable name="lang.seguro" select="'CAT-Declaraciones apropiadas de entidades financieras o, en su caso, justificante de la existencia de un seguro de indemnizaci�n por riesgos profesionales'"/>
	<xsl:variable name="lang.cuentas" select="'CAT-Las cuentas anuales presentadas en el Registro Mercantil o en el Registro oficial que corresponda'"/>
	<xsl:variable name="lang.volumen" select="'CAT-Declaraci�n sobre el volumen global de negocios'"/>
	<xsl:variable name="lang.5anos" select="'CAT-Relaci�n de las obras ejecutadas en el curso de los cinco �ltimos a�os'"/>
	<xsl:variable name="lang.unidadesTecnicas" select="'CAT-Declaraci�n indicando los t�cnicos o las unidades t�cnicas de los que �sta disponga para la ejecuci�n de las obras'"/>
	<xsl:variable name="lang.titulos" select="'CAT-T�tulos acad�micos y profesionales del emepresario y de los directivos de la empresa y, en particular, del responsable o responsables de las obras'"/>
	<xsl:variable name="lang.medidas" select="'CAT-Indicaci�n de las medidas de gesti�n medioambientales'"/>
	<xsl:variable name="lang.plantilla" select="'CAT-Declaraci�n sobre la plantilla media anual de la empresa y la importancia de su personal directivo durante los tres �ltimos a�os, acompa�ada de la documentaci�n justificada correspondiente'"/>
	<xsl:variable name="lang.material" select="'CAT-Declaraci�n indicando la maquinaria, material y equipo t�cnico del que se dispondr� para la ejecuci�n de las obras'"/>
	
	<xsl:variable name="lang.envio" select="'CAT-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'CAT-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'CAT-D.E.U.'"/>
	<xsl:variable name="lang.telefono" select="'CAT-Tel�fono'"/>
	<xsl:variable name="lang.email" select="'CAT-Email'"/>	
	<xsl:variable name="lang.importante" select="'CAT-IMPORTANTE'"/>	
	<xsl:variable name="lang.nota" select="' CAT-S�lo podr�n solicitar el contrato con el sector p�blico aquellas personas naturales o jur�dicas, espa�olas o extranjeras, que tengan plena capacidad de obrar, no est�n incursas en una prohibici�n de contratar, y acrediten su solvencia econ�mica, financiera y t�cnica o profesional o, en los casos en que as� lo exija esta Ley, se encuentren debidamente clasificadas.'"/>	
	<xsl:variable name="lang.unidades" select="'CAT-Unidades Plazo'"/>
	
	<xsl:variable name="lang.TResponsable" select="'CAT-Responsable del contrato'"/>
	<xsl:variable name="lang.TContratante" select="'CAT-Contratante'"/>
	<xsl:variable name="lang.TLicitador" select="'CAT-Licitador'"/>
	<xsl:variable name="lang.TAdjudicatario" select="'CAT-Adjudicatario'"/>
	
</xsl:stylesheet>