package core.error;

public class ErrorCode {
    
    /** Constante de c�digo RPER001: Error . */
    public static final String RPER001 = "Error";

    public static final String RPER002 = "Error en la marcar el formulario como s�lo lectura.";
    public static final String SEARCH_DESTINATION_REG_INTERCHANGE_ERROR_MESSAGE = RPER002;

    public static final String RPER003 = "Error en el env�o del asiento registral.";
    public static final String SEND_REG_INTERCHANGE_ERROR_MESSAGE = RPER003;


    public static final String RPER004 = "Error consultando el hist�rico de entrada del asiento registral.";
    public static final String GET_HIST_INPUT_REG_INTERCHANGE_ERROR_MESSAGE = RPER004;

    public static final String RPER005 = "Error consultando el hist�rico de salida del asiento registral.";
    public static final String GET_HIST_OUTPUT_REG_INTERCHANGE_ERROR_MESSAGE = RPER005;

    public static final String RPER006 = "Error recuperando la bandeja de entrada del asiento registral.";
    public static final String GET_INBOX_REG_INTERCHANGE_ERROR_MESSAGE = RPER006;

    public static final String RPER007 = "Error recuperando la bandeja de salida del asiento registral.";
    public static final String GET_OUTBOX_REG_INTERCHANGE_ERROR_MESSAGE = RPER007;

    public static final String RPER008 = "Error aceptando registros del intercambio registral.";
    public static final String ACCEPT_INBOX_REG_INTERCHANGE_ERROR_MESSAGE = RPER008;

    public static final String RPER009 = "Error rechazando registros del intercambio registral.";
    public static final String REJECT_INBOX_REG_INTERCHANGE_ERROR_MESSAGE = RPER009;

    public static final String RPER010 = "Error recuperando un asiento registral.";
    public static final String GET_INPUT_REG_INTERCHANGE_ERROR_MESSAGE = RPER010;

    public static final String RPER011 = "Error reenviando un asiento registral.";
    public static final String FORWARD_INBOX_REG_INTERCHANGE_ERROR_MESSAGE = RPER011;

    public static final String RPER012 = "Error recuperando lista de entidades registrales.";
    public static final String GET_ENTITYLIST_REG_INTERCHANGE_ERROR_MESSAGE = RPER012;

    public static final String RPER013 = "Error recuperando lista de unidades tramitadoras.";
    public static final String GET_UNIDLIST_REG_INTERCHANGE_ERROR_MESSAGE = RPER013;

    public static final String RPER014 = "Error descargando documento.";
    public static final String DOWNLOADFILE_ERROR_MESSAGE = RPER014;

    public static final String RPER015 = "Error recuperando el n�mero de registros pendientes en la bandeja de entrada.";
    public static final String GET_PENDING_INBOX_REG_INTERCHANGE_ERROR_MESSAGE  = RPER015;

    public static final String RPER016 = "Error recuperando una unidad tramitadora.";
    public static final String GET_UNID_REG_INTERCHANGE_ERROR_MESSAGE = RPER016;
    
    
    private String code;
    private String message;
    
    public ErrorCode(){
        this.code = "";
        this.message = "";
    }

    public ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
