package ieci.tecdoc.sgm.tram.ws.client;

import ieci.tecdoc.sgm.core.services.dto.RetornoServicio;
import ieci.tecdoc.sgm.tram.ws.client.dto.Binario;
import ieci.tecdoc.sgm.tram.ws.client.dto.Booleano;
import ieci.tecdoc.sgm.tram.ws.client.dto.Cadena;
import ieci.tecdoc.sgm.tram.ws.client.dto.Entero;
import ieci.tecdoc.sgm.tram.ws.client.dto.DatosComunesExpediente;
import ieci.tecdoc.sgm.tram.ws.client.dto.DocElectronico;
import ieci.tecdoc.sgm.tram.ws.client.dto.DocFisico;
import ieci.tecdoc.sgm.tram.ws.client.dto.DocumentoExpediente;
import ieci.tecdoc.sgm.tram.ws.client.dto.Emplazamiento;
import ieci.tecdoc.sgm.tram.ws.client.dto.Expediente;
import ieci.tecdoc.sgm.tram.ws.client.dto.Firma;
import ieci.tecdoc.sgm.tram.ws.client.dto.InfoBExpediente;
import ieci.tecdoc.sgm.tram.ws.client.dto.InfoBProcedimiento;
import ieci.tecdoc.sgm.tram.ws.client.dto.InfoFichero;
import ieci.tecdoc.sgm.tram.ws.client.dto.InfoOcupacion;
import ieci.tecdoc.sgm.tram.ws.client.dto.Interesado;
import ieci.tecdoc.sgm.tram.ws.client.dto.InteresadoExpediente;
import ieci.tecdoc.sgm.tram.ws.client.dto.ListaIdentificadores;
import ieci.tecdoc.sgm.tram.ws.client.dto.ListaInfoBExpedientes;
import ieci.tecdoc.sgm.tram.ws.client.dto.ListaInfoBProcedimientos;
import ieci.tecdoc.sgm.tram.ws.client.dto.OrganoProductor;
import ieci.tecdoc.sgm.tram.ws.client.dto.Procedimiento;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.axis.AxisEngine;
import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.SerializerFactory;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

public class TramitacionWebServiceSoapBindingStub extends Stub implements
		TramitacionWebService {

	private Vector cachedSerClasses = new Vector();

	private Vector cachedSerQNames = new Vector();

	private Vector cachedSerFactories = new Vector();

	private Vector cachedDeserFactories = new Vector();

	static OperationDesc[] _operations;

	static {
		_operations = new OperationDesc[21];
		_initOperationDesc1();
		_initOperationDesc2();
		_initOperationDesc3();
	}

	private static void _initOperationDesc1() {
		OperationDesc oper;
		ParameterDesc param;

		oper = new OperationDesc();
		oper.setName("getProcedimientosPorTipo");

		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "tipoProc"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"int"), int.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "nombre"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ListaInfoBProcedimientos"));
		oper.setReturnClass(ListaInfoBProcedimientos.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"getProcedimientosPorTipoReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[0] = oper;

		oper = new OperationDesc();
		oper.setName("getProcedimientos");

		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idProcs"),
				ParameterDesc.IN, 
				new QName("http://server.ws.tram.sgm.tecdoc.ieci",
						"ArrayOfString"), String[].class, false, false);
		oper.addParameter(param);

		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ListaInfoBProcedimientos"));
		oper.setReturnClass(ListaInfoBProcedimientos.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"getProcedimientosReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[1] = oper;

		oper = new OperationDesc();
		oper.setName("getProcedimiento");
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idProc"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"Procedimiento"));
		oper.setReturnClass(Procedimiento.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"getProcedimientoReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[2] = oper;

		oper = new OperationDesc();
		oper.setName("getFichero");
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "guid"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"Binario"));
		oper.setReturnClass(Binario.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"getFicheroReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[3] = oper;

		oper = new OperationDesc();
		oper.setName("getInfoFichero");
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "guid"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"InfoFichero"));
		oper.setReturnClass(InfoFichero.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"getInfoFicheroReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[4] = oper;

		oper = new OperationDesc();
		oper.setName("getInfoOcupacion");
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"InfoOcupacion"));
		oper.setReturnClass(InfoOcupacion.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"getInfoOcupacionReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[5] = oper;

		oper = new OperationDesc();
		oper.setName("eliminaFicheros");
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "guids"),
				ParameterDesc.IN, new QName("http://server.ws.tram.sgm.tecdoc.ieci",
						"ArrayOfString"), String[].class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"RetornoServicio"));
		oper.setReturnClass(RetornoServicio.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"eliminaFicherosReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[6] = oper;

		oper = new OperationDesc();
		oper.setName("getIdsExpedientes");
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idProc"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "fechaIni"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"dateTime"), Date.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "fechaFin"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"dateTime"), Date.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "tipoOrd"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ListaIdentificadores"));
		oper.setReturnClass(ListaIdentificadores.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"getIdsExpedientesReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[7] = oper;

		oper = new OperationDesc();
		oper.setName("getExpedientes");
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idExps"),
				ParameterDesc.IN, new QName("http://server.ws.tram.sgm.tecdoc.ieci",
						"ArrayOfString"), String[].class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ListaInfoBExpedientes"));
		oper.setReturnClass(ListaInfoBExpedientes.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"getExpedientesReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[8] = oper;

		oper = new OperationDesc();
		oper.setName("getExpediente");
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idExp"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"Expediente"));
		oper.setReturnClass(Expediente.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"getExpedienteReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[9] = oper;

	}

	private static void _initOperationDesc2() {
		OperationDesc oper;
		ParameterDesc param;
		oper = new OperationDesc();
		oper.setName("iniciarExpediente");
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "datosComunes"),
				ParameterDesc.IN, new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci",
						"DatosComunesExpediente"),
				DatosComunesExpediente.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "datosEspecificos"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "documentos"),
				ParameterDesc.IN, new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci",
						"ArrayOfDocumentoExpedientes"), DocumentoExpediente[].class,
				false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"Booleano"));
		oper.setReturnClass(Booleano.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"iniciarExpedienteReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[10] = oper;

		oper = new OperationDesc();
		oper.setName("anexarDocsExpediente");
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "numExp"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "numReg"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "fechaReg"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"datetime"), Date.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "documentos"),
				ParameterDesc.IN, new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci",
						"ArrayOfDocumentoExpedientes"), DocumentoExpediente[].class,
				false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"Booleano"));
		oper.setReturnClass(Booleano.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"iniciarExpedienteReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[11] = oper;

		oper = new OperationDesc();
		oper.setName("archivarExpedientes");
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "idExps"),
				ParameterDesc.IN, new QName("http://server.ws.tram.sgm.tecdoc.ieci",
						"ArrayOfString"), String[].class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"RetornoServicio"));
		oper.setReturnClass(RetornoServicio.class);
		oper.setReturnQName(new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"archivarExpedientesReturn"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[12] = oper;

		
		
        oper = new org.apache.axis.description.OperationDesc();
		oper.setName("crearExpediente");
		param = new ParameterDesc(
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN,
				new QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci", "datosComunes"),
				ParameterDesc.IN,
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci",
						"DatosComunesExpediente"),
				DatosComunesExpediente.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci",
						"datosEspecificos"),
				ParameterDesc.IN,
				new QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci", "documentos"),
				ParameterDesc.IN,
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci",
						"ArrayOfDocumentoExpedientes"), DocumentoExpediente[].class,
				false, false);
		oper.addParameter(param);
		param = new ParameterDesc(
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci", "initSystem"),
				ParameterDesc.IN,
				new QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "Cadena"));
		oper.setReturnClass(Cadena.class);
		oper.setReturnQName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci",
				"crearExpedienteReturn"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[13] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("cambiarEstadoAdministrativo");
		param = new ParameterDesc(
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN,
				new QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci", "numExp"),
				ParameterDesc.IN,
				new QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci", "estadoAdm"),
				ParameterDesc.IN,
				new QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "Booleano"));
		oper.setReturnClass(Booleano.class);
		oper.setReturnQName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci",
				"cambiarEstadoAdministrativo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		oper
				.addFault(new org.apache.axis.description.FaultDesc(
						new QName(
								"http://server.ws.tram.sgm.tecdoc.ieci",
								"fault"),
						"ieci.tecdoc.sgm.core.services.tramitacion.TramitacionException",
						new QName(
								"http://tramitacion.services.core.sgm.tecdoc.ieci",
								"TramitacionException"), true));
		_operations[14] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("moverExpedienteAFase");
		param = new ParameterDesc(
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci", "idEntidad"),
				ParameterDesc.IN,
				new QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci", "numExp"),
				ParameterDesc.IN,
				new QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(
				new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci",
						"idFaseCatalogo"),
				ParameterDesc.IN,
				new QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "Booleano"));
		oper.setReturnClass(Booleano.class);
		oper
				.setReturnQName(new QName(
						"http://server.ws.tram.sgm.tecdoc.ieci",
						"moverExpedienteAFase"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		oper
				.addFault(new org.apache.axis.description.FaultDesc(
						new QName(
								"http://server.ws.tram.sgm.tecdoc.ieci",
								"fault"),
						"ieci.tecdoc.sgm.core.services.tramitacion.TramitacionException",
						new QName(
								"http://tramitacion.services.core.sgm.tecdoc.ieci",
								"TramitacionException"), true));
		_operations[15] = oper;		
		

        oper = new org.apache.axis.description.OperationDesc();
		oper.setName("busquedaAvanzada");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "idEntidad"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "groupName"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci",
						"searchFormName"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "searchXML"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "domain"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "int"), int.class,
				false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci", "Cadena"));
		oper.setReturnClass(Cadena.class);
		oper
				.setReturnQName(new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci",
						"busquedaAvanzada"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[16] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("establecerDatosRegistroEntidad");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "idEntidad"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci",
						"nombreEntidad"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "numExp"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci",
						"xmlDatosEspecificos"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci", "Entero"));
		oper.setReturnClass(Entero.class);
		oper.setReturnQName(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci",
				"establecerDatosRegistroEntidad"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[17] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("obtenerRegistroEntidad");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "idEntidad"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci",
						"nombreEntidad"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "numExp"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci",
						"idRegistro"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "int"), int.class,
				false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci", "Cadena"));
		oper.setReturnClass(Cadena.class);
		oper.setReturnQName(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci",
				"obtenerRegistroEntidad"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[18] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("obtenerRegistrosEntidad");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "idEntidad"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci",
						"nombreEntidad"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "numExp"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci", "Cadena"));
		oper.setReturnClass(Cadena.class);
		oper.setReturnQName(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci",
				"obtenerRegistrosEntidad"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[19] = oper;

		
		
	}

	private static void _initOperationDesc3() {
		OperationDesc oper;
		ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("recibirDocumentoFirmado");
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "idEntidad"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci",
						"numExp"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(
				new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci", "idDocumento"),
				org.apache.axis.description.ParameterDesc.IN,
				new javax.xml.namespace.QName(
						"http://www.w3.org/2001/XMLSchema", "string"),
				java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci", "Cadena"));
		oper.setReturnClass(Cadena.class);
		oper.setReturnQName(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci",
				"recibirDocumentoFirmado"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[20] = oper;



	}

	public TramitacionWebServiceSoapBindingStub() throws AxisFault {
		this(null);
	}

	public TramitacionWebServiceSoapBindingStub(java.net.URL endpointURL,
			javax.xml.rpc.Service service) throws AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public TramitacionWebServiceSoapBindingStub(javax.xml.rpc.Service service)
			throws AxisFault {
		if (service == null) {
			super.service = new Service();
		} else {
			super.service = service;
		}
		((Service) super.service).setTypeMappingVersion("1.2");
		Class cls;
		QName qName;
		QName qName2;
		Class beansf = BeanSerializerFactory.class;
		Class beandf = BeanDeserializerFactory.class;

		qName = new QName("http://dto.services.core.sgm.tecdoc.ieci",
				"RetornoServicio");
		cachedSerQNames.add(qName);
		cls = ieci.tecdoc.sgm.core.services.dto.RetornoServicio.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ArrayOfString");
		cachedSerQNames.add(qName);
		cls = String[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://www.w3.org/2001/XMLSchema", "string");
		qName2 = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ArrayOfDocElectronicos");
		cachedSerQNames.add(qName);
		cls = DocElectronico[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"DocElectronico");
		qName2 = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ArrayOfDocFisicos");
		cachedSerQNames.add(qName);
		cls = DocFisico[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "DocFisico");
		qName2 = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ArrayOfEmplazamientos");
		cachedSerQNames.add(qName);
		cls = Emplazamiento[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"Emplazamiento");
		qName2 = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ArrayOfFirmas");
		cachedSerQNames.add(qName);
		cls = Firma[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "Firma");
		qName2 = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ArrayOfInfoBExpedientes");
		cachedSerQNames.add(qName);
		cls = InfoBExpediente[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"InfoBExpediente");
		qName2 = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ArrayOfInfoBProcedimientos");
		cachedSerQNames.add(qName);
		cls = InfoBProcedimiento[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"InfoBProcedimiento");
		qName2 = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ArrayOfInteresadoExpedientes");
		cachedSerQNames.add(qName);
		cls = InteresadoExpediente[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"InteresadoExpediente");
		qName2 = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ArrayOfInteresados");
		cachedSerQNames.add(qName);
		cls = Interesado[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "Interesado");
		qName2 = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ArrayOfOrganosProductores");
		cachedSerQNames.add(qName);
		cls = OrganoProductor[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"OrganoProductor");
		qName2 = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "item");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "Binario");
		cachedSerQNames.add(qName);
		cls = Binario.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "Booleano");
		cachedSerQNames.add(qName);
		cls = Booleano.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"DatosComunesExpediente");
		cachedSerQNames.add(qName);
		cls = DatosComunesExpediente.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"DocElectronico");
		cachedSerQNames.add(qName);
		cls = DocElectronico.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "DocFisico");
		cachedSerQNames.add(qName);
		cls = DocFisico.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"DocumentoExpediente");
		cachedSerQNames.add(qName);
		cls = DocumentoExpediente.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"Emplazamiento");
		cachedSerQNames.add(qName);
		cls = Emplazamiento.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "Expediente");
		cachedSerQNames.add(qName);
		cls = Expediente.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "Firma");
		cachedSerQNames.add(qName);
		cls = Firma.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"InfoBExpediente");
		cachedSerQNames.add(qName);
		cls = InfoBExpediente.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"InfoBProcedimiento");
		cachedSerQNames.add(qName);
		cls = InfoBProcedimiento.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"InfoFichero");
		cachedSerQNames.add(qName);
		cls = InfoFichero.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"InfoOcupacion");
		cachedSerQNames.add(qName);
		cls = InfoOcupacion.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "Interesado");
		cachedSerQNames.add(qName);
		cls = Interesado.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"InteresadoExpediente");
		cachedSerQNames.add(qName);
		cls = InteresadoExpediente.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ListaIdentificadores");
		cachedSerQNames.add(qName);
		cls = ListaIdentificadores.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ListaInfoBExpedientes");
		cachedSerQNames.add(qName);
		cls = ListaInfoBExpedientes.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"ListaInfoBProcedimientos");
		cachedSerQNames.add(qName);
		cls = ListaInfoBProcedimientos.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"OrganoProductor");
		cachedSerQNames.add(qName);
		cls = OrganoProductor.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"Procedimiento");
		cachedSerQNames.add(qName);
		cls = Procedimiento.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://server.ws.tram.sgm.tecdoc.ieci",
				"RetornoServicio");
		cachedSerQNames.add(qName);
		cls = RetornoServicio.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

	}

	protected Call createCall() throws RemoteException {
		try {
			Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			Enumeration keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			// All the type mapping information is registered
			// when the first call is made.
			// The type mapping information is actually registered in
			// the TypeMappingRegistry of the service, which
			// is the reason why registration is only needed for the first call.
			synchronized (this) {
				if (firstCall()) {
					// must set encoding style before registering serializers
					_call.setEncodingStyle(null);
					for (int i = 0; i < cachedSerFactories.size(); ++i) {
						Class cls = (Class) cachedSerClasses.get(i);
						QName qName = (QName) cachedSerQNames.get(i);
						Object x = cachedSerFactories.get(i);
						if (x instanceof Class) {
							Class sf = (Class) cachedSerFactories.get(i);
							Class df = (Class) cachedDeserFactories.get(i);
							_call
									.registerTypeMapping(cls, qName, sf, df,
											false);
						} else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							SerializerFactory sf = (SerializerFactory) cachedSerFactories
									.get(i);
							DeserializerFactory df = (DeserializerFactory) cachedDeserFactories
									.get(i);
							_call
									.registerTypeMapping(cls, qName, sf, df,
											false);
						}
					}
				}
			}
			return _call;
		} catch (Throwable _t) {
			throw new AxisFault("Failure trying to get the Call object", _t);
		}
	}

	public ListaInfoBProcedimientos getProcedimientosPorTipo(String idEntidad, int tipoProc,
			String nombre) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci",
				"getProcedimientosPorTipo"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] {idEntidad,  new Integer(tipoProc),
					nombre });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (ListaInfoBProcedimientos) _resp;
				} catch (Exception _exception) {
					return (ListaInfoBProcedimientos) JavaUtils.convert(_resp,
							ListaInfoBProcedimientos.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public ListaInfoBProcedimientos getProcedimientos(String idEntidad, String[] idProcs)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "getProcedimientos"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] {idEntidad,  idProcs});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (ListaInfoBProcedimientos) _resp;
				} catch (Exception _exception) {
					return (ListaInfoBProcedimientos) JavaUtils.convert(_resp,
							ListaInfoBProcedimientos.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public Procedimiento getProcedimiento(String idEntidad, String idProc)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[2]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "getProcedimiento"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] {idEntidad, idProc });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (Procedimiento) _resp;
				} catch (Exception _exception) {
					return (Procedimiento) JavaUtils.convert(_resp,
							Procedimiento.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public Binario getFichero(String idEntidad, String guid) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[3]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "getFichero"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] {idEntidad, guid });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (Binario) _resp;
				} catch (Exception _exception) {
					return (Binario) JavaUtils.convert(_resp, Binario.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public InfoFichero getInfoFichero(String idEntidad, String guid)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[4]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "getInfoFichero"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] {idEntidad, guid });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (InfoFichero) _resp;
				} catch (Exception _exception) {
					return (InfoFichero) JavaUtils.convert(_resp,
							InfoFichero.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public InfoOcupacion getInfoOcupacion(String idEntidad) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[5]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "getInfoOcupacion"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { idEntidad });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (InfoOcupacion) _resp;
				} catch (Exception _exception) {
					return (InfoOcupacion) JavaUtils.convert(_resp,
							InfoOcupacion.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public RetornoServicio eliminaFicheros(String idEntidad, String[] guids)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[6]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "eliminaFicheros"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { idEntidad, guids });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (RetornoServicio) _resp;
				} catch (Exception _exception) {
					return (RetornoServicio) JavaUtils.convert(_resp,
							RetornoServicio.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public ListaIdentificadores getIdsExpedientes(String idEntidad, String idProc, Date fechaIni,
			Date fechaFin, int tipoOrd) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[7]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "getIdsExpedientes"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { idEntidad, idProc, fechaIni,
					fechaFin, new Integer(tipoOrd) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (ListaIdentificadores) _resp;
				} catch (Exception _exception) {
					return (ListaIdentificadores) JavaUtils.convert(_resp,
							ListaIdentificadores.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public ListaInfoBExpedientes getExpedientes(String idEntidad, String[] idExps)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[8]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "getExpedientes"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { idEntidad, idExps });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (ListaInfoBExpedientes) _resp;
				} catch (Exception _exception) {
					return (ListaInfoBExpedientes) JavaUtils.convert(_resp,
							ListaInfoBExpedientes.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public Expediente getExpediente(String idEntidad, String idExp)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[9]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "getExpediente"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { idEntidad, idExp });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (Expediente) _resp;
				} catch (Exception _exception) {
					return (Expediente) JavaUtils.convert(_resp,
							Expediente.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public Booleano iniciarExpediente(String idEntidad, DatosComunesExpediente datosComunes,
			String datosEspecificos, DocumentoExpediente[] documentos)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[10]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "iniciarExpediente"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { idEntidad, datosComunes,
					datosEspecificos, documentos });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (Booleano) _resp;
				} catch (Exception _exception) {
					return (Booleano) JavaUtils.convert(_resp, Booleano.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public Booleano anexarDocsExpediente(String idEntidad, String numExp, String numReg, 
				Date fechaReg, DocumentoExpediente[] documentos) 
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[11]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "anexarDocsExpediente"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { idEntidad, numExp, numReg, fechaReg,
					documentos });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (Booleano) _resp;
				} catch (Exception _exception) {
					return (Booleano) JavaUtils.convert(_resp, Booleano.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public RetornoServicio archivarExpedientes(String idEntidad, String[] idExps) 
			throws RemoteException {
		
		Call _call = createCall();
		_call.setOperation(_operations[12]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName(
				"http://server.ws.tram.sgm.tecdoc.ieci", "archivarExpedientes"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { idEntidad, idExps });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (RetornoServicio) _resp;
				} catch (Exception _exception) {
					return (RetornoServicio) JavaUtils.convert(_resp,
							RetornoServicio.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

    public Cadena crearExpediente(String idEntidad, DatosComunesExpediente datosComunes, String datosEspecificos, DocumentoExpediente[] documentos, String initSystem) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "crearExpediente"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, datosComunes, datosEspecificos, documentos, initSystem});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Cadena) _resp;
            } catch (java.lang.Exception _exception) {
                return (Cadena) org.apache.axis.utils.JavaUtils.convert(_resp, Cadena.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public Booleano cambiarEstadoAdministrativo(String idEntidad, String numExp, String estadoAdm) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "cambiarEstadoAdministrativo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, numExp, estadoAdm});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Booleano) _resp;
            } catch (java.lang.Exception _exception) {
                return (Booleano) org.apache.axis.utils.JavaUtils.convert(_resp, Booleano.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public Booleano moverExpedienteAFase(String idEntidad, String numExp, java.lang.String idFaseCatalogo) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "moverExpedienteAFase"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idEntidad, numExp, idFaseCatalogo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Booleano) _resp;
            } catch (java.lang.Exception _exception) {
                return (Booleano) org.apache.axis.utils.JavaUtils.convert(_resp, Booleano.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }
	
    
    public Cadena busquedaAvanzada(java.lang.String idEntidad,
			java.lang.String groupName, java.lang.String searchFormName,
			java.lang.String searchXML, int domain)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[16]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
				Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
				Boolean.FALSE);
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call
				.setOperationName(new javax.xml.namespace.QName(
						"http://server.ws.services.ispac.tdw.ieci",
						"busquedaAvanzada"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					idEntidad, groupName, searchFormName, searchXML,
					new java.lang.Integer(domain) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (Cadena) _resp;
				} catch (java.lang.Exception _exception) {
					return (Cadena) org.apache.axis.utils.JavaUtils.convert(
							_resp, Cadena.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public Entero establecerDatosRegistroEntidad(java.lang.String idEntidad,
			java.lang.String nombreEntidad, java.lang.String numExp,
			java.lang.String xmlDatosEspecificos)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[17]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
				Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
				Boolean.FALSE);
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci",
				"establecerDatosRegistroEntidad"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					idEntidad, nombreEntidad, numExp, xmlDatosEspecificos });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (Entero) _resp;
				} catch (java.lang.Exception _exception) {
					return (Entero) org.apache.axis.utils.JavaUtils.convert(
							_resp, Entero.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public Cadena obtenerRegistroEntidad(java.lang.String idEntidad,
			java.lang.String nombreEntidad, java.lang.String numExp,
			int idRegistro) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[18]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
				Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
				Boolean.FALSE);
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci",
				"obtenerRegistroEntidad"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					idEntidad, nombreEntidad, numExp,
					new java.lang.Integer(idRegistro) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (Cadena) _resp;
				} catch (java.lang.Exception _exception) {
					return (Cadena) org.apache.axis.utils.JavaUtils.convert(
							_resp, Cadena.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public Cadena obtenerRegistrosEntidad(java.lang.String idEntidad,
			java.lang.String nombreEntidad, java.lang.String numExp)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[19]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
				Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
				Boolean.FALSE);
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci",
				"obtenerRegistrosEntidad"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					idEntidad, nombreEntidad, numExp });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (Cadena) _resp;
				} catch (java.lang.Exception _exception) {
					return (Cadena) org.apache.axis.utils.JavaUtils.convert(
							_resp, Cadena.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public Cadena recibirDocumentoFirmado(java.lang.String idEntidad,
			java.lang.String numExp, java.lang.String idDocumento)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[20]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
				Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
				Boolean.FALSE);
		_call
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName(
				"http://server.ws.services.ispac.tdw.ieci",
				"recibirDocumentoFirmado"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			java.lang.Object _resp = _call.invoke(new java.lang.Object[] {
					idEntidad, numExp, idDocumento });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (Cadena) _resp;
				} catch (java.lang.Exception _exception) {
					return (Cadena) org.apache.axis.utils.JavaUtils.convert(
							_resp, Cadena.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}
	
	//[DipuCR-Agust�n #781]
	public Binario getFicheroTemp(String idEntidad, String guid) {
		// TODO Auto-generated method stub
		return null;
	}
    
	
}
