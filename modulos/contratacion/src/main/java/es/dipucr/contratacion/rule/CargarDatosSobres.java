package es.dipucr.contratacion.rule;

import java.util.Vector;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class CargarDatosSobres extends CargaDocumentacionPresentar{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		sobre1 = "SOBRE A";
		sobre1TipoDoc = "1 - Documentaci�n administrativa";
		sobre1Docs = new Vector<String>();
		sobre1Docs.add("Los que acrediten la personalidad juridica del empresario: DNI o fotocopia autorizada del mismo, o documento que le sustituya reglamentariamente si se trata de extranjeros. En el caso de actuar en representaci�n, acompa�ar�n el Poder que as� lo acredite y la Escritura de Constituci�n o modificaci�n de la Sociedad, inscrita en el Registro Mercantil");
		sobre1Docs.add("En el caso de concurrir  a la licitaci�n varias empresas constituyendo una uni�n temporal, cada una de ellas deber� acreditar su personalidad y capacidad");
		sobre1Docs.add("Documento de clasificaci�n empresarial en el caso que se exija y si no se exige, deber�n presentar documentos que acrediten requisitos de solvencia econ�mica y financiera y solvencia t�cnica");
		sobre1Docs.add("Para las empresas extranjeras declaraci�n de someterse a la jurisdicci�n de los Juzgados y tribunales espa�oles de cualquier orden");
		sobre1Docs.add("Declaraci�n jurada o declaraci�n expresa respolsable de no hallare incursos en ninguna de las prohibiciones de contratar conforme al art. 60 TRLCSP");
		sobre1Docs.add("Declaraci�n responsable de no existencia de empresas vinculadas con el oferente, o en su caso relaci�n de empresas vinculadas con el mismo");
		sobre1Docs.add("Relaci�n de equipo, maquinaria, personas facultativo/titulado y medios auxiliares que la empresa se compromete a poner a disposici�n");
		sobre1Docs.add("Programa de construcci�n de las obras que asegure la ejecuci�n en los plazos");
		sobre1Docs.add("Voluntario el Certificado expedido por el Registro de Licitadores de Castilla La  Mancha acompa�ado de una Declaraci�n expresa responsable relativa a la no alteraci�n de los datos que constan en el referido Registro");
		sobre1Docs.add("Voluntario el Certificado expedido por el Registro Oficial de Licitadores y Empresas Clasificadas del Estado acompa�ado de una Declaraci�n expresa responsable relativa a la no alteraci�n de los datos que constan en el referido Registro");
		sobre1Docs.add("Declaraci�n de documentos confidenciales para el caso de que el licitador considere que tienen car�cter confidencial determinados documentos incorporados a su oferta");
	
		
		sobre2 = "SOBRE B";
		sobre2TipoDoc = "2 - Oferta t�cnica o evaluable mediante juicio de valor";
		sobre2Docs = new Vector<String>();
			
		sobre3 = "SOBRE C";
		sobre3TipoDoc = "3 - Oferta econ�mica o evaluable mediante f�rmulas";
		sobre3Docs = new Vector<String>();
		sobre3Docs.add("Proposici�n econ�mica");
		
		calle = "C/Toledo, n�17";
		localidad = "Ciudad Real";
		provincia = "Ciudad Real";
		lugar = "Diputaci�n Provincial de Ciudad Real";
		cp = "13071";
		
		return true;
	}
}
