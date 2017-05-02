/**
 * ListaInfoBExpedientes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class ListaInfoBExpedientes  extends ieci.tecdoc.sgm.core.services.dto.RetornoServicio  implements java.io.Serializable {
    private ieci.tecdoc.sgm.tram.ws.server.InfoBExpediente[] expedientes;

    public ListaInfoBExpedientes() {
    }

    public ListaInfoBExpedientes(
           java.lang.String errorCode,
           java.lang.String returnCode,
           ieci.tecdoc.sgm.tram.ws.server.InfoBExpediente[] expedientes) {
        super(
            errorCode,
            returnCode);
        this.expedientes = expedientes;
    }


    /**
     * Gets the expedientes value for this ListaInfoBExpedientes.
     * 
     * @return expedientes
     */
    public ieci.tecdoc.sgm.tram.ws.server.InfoBExpediente[] getExpedientes() {
        return expedientes;
    }


    /**
     * Sets the expedientes value for this ListaInfoBExpedientes.
     * 
     * @param expedientes
     */
    public void setExpedientes(ieci.tecdoc.sgm.tram.ws.server.InfoBExpediente[] expedientes) {
        this.expedientes = expedientes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListaInfoBExpedientes)) return false;
        ListaInfoBExpedientes other = (ListaInfoBExpedientes) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.expedientes==null && other.getExpedientes()==null) || 
             (this.expedientes!=null &&
              java.util.Arrays.equals(this.expedientes, other.getExpedientes())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getExpedientes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExpedientes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExpedientes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ListaInfoBExpedientes.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ListaInfoBExpedientes"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expedientes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "expedientes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoBExpediente"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item"));
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
