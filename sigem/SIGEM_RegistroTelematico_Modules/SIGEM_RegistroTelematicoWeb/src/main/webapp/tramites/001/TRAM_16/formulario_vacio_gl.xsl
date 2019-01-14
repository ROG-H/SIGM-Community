<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'GAL-Formulario de Contrataci�n administrativa mediante el procedimiento negociado'"/>
	<xsl:variable name="lang.datosContratante" select="'GAL-Datos del Contratante'"/>
	<xsl:variable name="lang.datosContratado" select="'GAL-Datos del Contratado'"/>
	<xsl:variable name="lang.docIdentidad" select="'GAL-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'GAL-Nombre'"/>
	<xsl:variable name="lang.relacion" select="'GAL-Relaci�n'"/>
	<xsl:variable name="lang.domicilio" select="'GAL-Domicilio a efectos de notificaci�n'"/>
	<xsl:variable name="lang.localidad" select="'GAL-Localidad'"/>
	<xsl:variable name="lang.provincia" select="'GAL-Provincia'"/>
	<xsl:variable name="lang.cp" select="'GAL-C�digo postal'"/>
	<xsl:variable name="lang.datosContrato" select="'GAL-Datos del Contrato'"/>
	<xsl:variable name="lang.tipoContrato" select="'GAL-Tipo de Contrato'"/>
	
	<xsl:variable name="lang.TColaboracion" select="'GAL-Colaboraci�n entre el sector p�blico y el sector privado'" />
	<xsl:variable name="lang.TConcesion" select="'GAL-Concesi�n de obras p�blicas'" />
	<xsl:variable name="lang.TGestion" select="'GAL-Gesti�n de servicios p�blicos'" />
	<xsl:variable name="lang.TObras" select="'GAL-Obras'" />
	<xsl:variable name="lang.TServicio" select="'GAL-Servicio'" />
	<xsl:variable name="lang.TSuministros" select="'GAL-Suministros'" />
	
	<xsl:variable name="lang.formaAdjudicacion" select="'GAL-Forma de Adjudicaci�n'"/>
	<xsl:variable name="lang.TConcurso" select="'GAL-Concurso'"/>
	<xsl:variable name="lang.TSubasta" select="'GAL-Subasta'"/>
	
	<xsl:variable name="lang.presupuestoMax" select="'GAL-Presupuesto m�ximo'"/>
	<xsl:variable name="lang.precioAdjudicacion" select="'GAL-Precio de la Adjudicaci�n'"/>
	<xsl:variable name="lang.aplicacion" select="'GAL-Aplicaci�n presupuestaria'"/>
	<xsl:variable name="lang.plazo" select="'GAL-Plazo de Ejecuci�n'"/>
	<xsl:variable name="lang.unidades" select="'GAL-Unidades de Plazo'"/>
	<xsl:variable name="lang.garantiaProv" select="'GAL-Garant�a Provisional'"/>
	<xsl:variable name="lang.garantiaDef" select="'GAL-Garant�a Definitiva'"/>
	<xsl:variable name="lang.clasificacion" select="'GAL-Clasificaci�n'"/>
	<xsl:variable name="lang.fechaFinEjecucion" select="'GAL-Fecha final de Ejecuci�n'"/>
	<xsl:variable name="lang.fechaFinGarantia" select="'GAL-Fecha final de Garant�a'"/>
	
	<xsl:variable name="lang.TDiasNaturales" select="'GAL-D�as naturales'"/>
	<xsl:variable name="lang.TDiasLaborales" select="'GAL-D�as Laborales'"/>
	<xsl:variable name="lang.TMeses" select="'GAL-Meses'"/>
	<xsl:variable name="lang.TAnos" select="'GAL-A�os'"/>
	
	<xsl:variable name="lang.anexar" select="'GAL-Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'GAL-1.- Para adjuntar un fichero (*.pdf), pulse el bot�n examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'GAL-2.- Seleccione el fichero que desea anexar al contrato'"/>
	
	<xsl:variable name="lang.escritura" select="'GAL-Escritura o documento de constituci�n'"/>
	<xsl:variable name="lang.obrar" select="'GAL-Acreditaci�n de la capacidad de obrar'"/>
	<xsl:variable name="lang.estatutos" select="'GAL-Estatutos'"/>
	<xsl:variable name="lang.solvencia" select="'GAL-Acreditaci�n de solvencia'"/>
	<xsl:variable name="lang.noConcurrencia" select="'GAL-Prueba de la no concurrencia de una prohibici�n de contratar'"/>
	<xsl:variable name="lang.seguro" select="'GAL-Declaraciones apropiadas de entidades financieras o, en su caso, justificante de la existencia de un seguro de indemnizaci�n por riesgos profesionales'"/>
	<xsl:variable name="lang.cuentas" select="'GAL-Las cuentas anuales presentadas en el Registro Mercantil o en el Registro oficial que corresponda'"/>
	<xsl:variable name="lang.volumen" select="'GAL-Declaraci�n sobre el volumen global de negocios'"/>
	<xsl:variable name="lang.5anos" select="'GAL-Relaci�n de las obras ejecutadas en el curso de los cinco �ltimos a�os'"/>
	<xsl:variable name="lang.unidadesTecnicas" select="'GAL-Declaraci�n indicando los t�cnicos o las unidades t�cnicas de los que �sta disponga para la ejecuci�n de las obras'"/>
	<xsl:variable name="lang.titulos" select="'GAL-T�tulos acad�micos y profesionales del emepresario y de los directivos de la empresa y, en particular, del responsable o responsables de las obras'"/>
	<xsl:variable name="lang.medidas" select="'GAL-Indicaci�n de las medidas de gesti�n medioambientales'"/>
	<xsl:variable name="lang.plantilla" select="'GAL-Declaraci�n sobre la plantilla media anual de la empresa y la importancia de su personal directivo durante los tres �ltimos a�os, acompa�ada de la documentaci�n justificada correspondiente'"/>
	<xsl:variable name="lang.material" select="'GAL-Declaraci�n indicando la maquinaria, material y equipo t�cnico del que se dispondr� para la ejecuci�n de las obras'"/>
	
	<xsl:variable name="lang.envio" select="'GAL-Env�o de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'GAL-Solicito el env�o de notificaciones por medios telem�ticos'"/>
	<xsl:variable name="lang.deu" select="'GAL-D.E.U.'"/>
	<xsl:variable name="lang.telefono" select="'GAL-Tel�fono'"/>
	<xsl:variable name="lang.email" select="'GAL-Email'"/>	
	<xsl:variable name="lang.importante" select="'GAL-IMPORTANTE'"/>	
	<xsl:variable name="lang.nota" select="' GAL-S�lo podr�n solicitar el contrato con el sector p�blico aquellas personas naturales o jur�dicas, espa�olas o extranjeras, que tengan plena capacidad de obrar, no est�n incursas en una prohibici�n de contratar, y acrediten su solvencia econ�mica, financiera y t�cnica o profesional o, en los casos en que as� lo exija esta Ley, se encuentren debidamente clasificadas.'"/>	
	<xsl:variable name="lang.unidades" select="'GAL-Unidades Plazo'"/>
	
	<xsl:variable name="lang.TResponsable" select="'GAL-Responsable del contrato'"/>
	<xsl:variable name="lang.TContratante" select="'GAL-Contratante'"/>
	<xsl:variable name="lang.TLicitador" select="'GAL-Licitador'"/>
	<xsl:variable name="lang.TAdjudicatario" select="'GAL-Adjudicatario'"/>
	
</xsl:stylesheet>