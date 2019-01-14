package es.dipucr.contratacion.rule;

import java.util.Vector;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class CargarDatosSobresElectronicoMenor extends CargaDocumentacionPresentar{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		sobre1 = "Administrativo";
		sobre1TipoDoc = "1 - Documentaci�n administrativa";
		
		
		
		sobre2 = "SOBRE B";
		sobre2TipoDoc = "2 - Oferta t�cnica o evaluable mediante juicio de valor";
		sobre2Docs = new Vector<String>();sobre1Docs = new Vector<String>();
		sobre1Docs.add("Econ�mico");
		
			
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
