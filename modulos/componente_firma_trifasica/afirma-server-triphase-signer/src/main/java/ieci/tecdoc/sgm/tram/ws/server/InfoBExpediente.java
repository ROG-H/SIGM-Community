/**
 * InfoBExpediente.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class InfoBExpediente  implements java.io.Serializable {
    private java.lang.String datosIdentificativos;

    private java.lang.String id;

    private java.lang.String numExp;

    public InfoBExpediente() {
    }

    public InfoBExpediente(
           java.lang.String datosIdentificativos,
           java.lang.String id,
           java.lang.String numExp) {
           this.datosIdentificativos = datosIdentificativos;
           this.id = id;
           this.numExp = numExp;
    }


    /**
     * Gets the datosIdentificativos value for this InfoBExpediente.
     * 
     * @return datosIdentificativos
     */
    public java.lang.String getDatosIdentificativos() {
        return datosIdentificativos;
    }


    /**
     * Sets the datosIdentificativos value for this InfoBExpediente.
     * 
     * @param datosIdentificativos
     */
    public void setDatosIdentificativos(java.lang.String datosIdentificativos) {
        this.datosIdentificativos = datosIdentificativos;
    }


    /**
     * Gets the id value for this InfoBExpediente.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this InfoBExpediente.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the numExp value for this InfoBExpediente.
     * 
     * @return numExp
     */
    public java.lang.String getNumExp() {
        return numExp;
    }


    /**
     * Sets the numExp value for this InfoBExpediente.
     * 
     * @param numExp
     */
    public void setNumExp(java.lang.String numExp) {
        this.numExp = numExp;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InfoBExpediente)) return false;
        InfoBExpediente other = (InfoBExpediente) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.datosIdentificativos==null && other.getDatosIdentificativos()==null) || 
             (this.datosIdentificativos!=null &&
              this.datosIdentificativos.equals(other.getDatosIdentificativos()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.numExp==null && other.getNumExp()==null) || 
             (this.numExp!=null &&
              this.numExp.equals(other.getNumExp())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDatosIdentificativos() != null) {
            _hashCode += getDatosIdentificativos().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getNumExp() != null) {
            _hashCode += getNumExp().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoBExpediente.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoBExpediente"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosIdentificativos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "datosIdentificativos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numExp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "numExp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
