/**
 * Emplazamiento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class Emplazamiento  implements java.io.Serializable {
    private java.lang.String comunidad;

    private java.lang.String concejo;

    private java.lang.String localizacion;

    private java.lang.String pais;

    private java.lang.String poblacion;

    public Emplazamiento() {
    }

    public Emplazamiento(
           java.lang.String comunidad,
           java.lang.String concejo,
           java.lang.String localizacion,
           java.lang.String pais,
           java.lang.String poblacion) {
           this.comunidad = comunidad;
           this.concejo = concejo;
           this.localizacion = localizacion;
           this.pais = pais;
           this.poblacion = poblacion;
    }


    /**
     * Gets the comunidad value for this Emplazamiento.
     * 
     * @return comunidad
     */
    public java.lang.String getComunidad() {
        return comunidad;
    }


    /**
     * Sets the comunidad value for this Emplazamiento.
     * 
     * @param comunidad
     */
    public void setComunidad(java.lang.String comunidad) {
        this.comunidad = comunidad;
    }


    /**
     * Gets the concejo value for this Emplazamiento.
     * 
     * @return concejo
     */
    public java.lang.String getConcejo() {
        return concejo;
    }


    /**
     * Sets the concejo value for this Emplazamiento.
     * 
     * @param concejo
     */
    public void setConcejo(java.lang.String concejo) {
        this.concejo = concejo;
    }


    /**
     * Gets the localizacion value for this Emplazamiento.
     * 
     * @return localizacion
     */
    public java.lang.String getLocalizacion() {
        return localizacion;
    }


    /**
     * Sets the localizacion value for this Emplazamiento.
     * 
     * @param localizacion
     */
    public void setLocalizacion(java.lang.String localizacion) {
        this.localizacion = localizacion;
    }


    /**
     * Gets the pais value for this Emplazamiento.
     * 
     * @return pais
     */
    public java.lang.String getPais() {
        return pais;
    }


    /**
     * Sets the pais value for this Emplazamiento.
     * 
     * @param pais
     */
    public void setPais(java.lang.String pais) {
        this.pais = pais;
    }


    /**
     * Gets the poblacion value for this Emplazamiento.
     * 
     * @return poblacion
     */
    public java.lang.String getPoblacion() {
        return poblacion;
    }


    /**
     * Sets the poblacion value for this Emplazamiento.
     * 
     * @param poblacion
     */
    public void setPoblacion(java.lang.String poblacion) {
        this.poblacion = poblacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Emplazamiento)) return false;
        Emplazamiento other = (Emplazamiento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.comunidad==null && other.getComunidad()==null) || 
             (this.comunidad!=null &&
              this.comunidad.equals(other.getComunidad()))) &&
            ((this.concejo==null && other.getConcejo()==null) || 
             (this.concejo!=null &&
              this.concejo.equals(other.getConcejo()))) &&
            ((this.localizacion==null && other.getLocalizacion()==null) || 
             (this.localizacion!=null &&
              this.localizacion.equals(other.getLocalizacion()))) &&
            ((this.pais==null && other.getPais()==null) || 
             (this.pais!=null &&
              this.pais.equals(other.getPais()))) &&
            ((this.poblacion==null && other.getPoblacion()==null) || 
             (this.poblacion!=null &&
              this.poblacion.equals(other.getPoblacion())));
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
        if (getComunidad() != null) {
            _hashCode += getComunidad().hashCode();
        }
        if (getConcejo() != null) {
            _hashCode += getConcejo().hashCode();
        }
        if (getLocalizacion() != null) {
            _hashCode += getLocalizacion().hashCode();
        }
        if (getPais() != null) {
            _hashCode += getPais().hashCode();
        }
        if (getPoblacion() != null) {
            _hashCode += getPoblacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Emplazamiento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Emplazamiento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comunidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "comunidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("concejo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "concejo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "localizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pais");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "pais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("poblacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "poblacion"));
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
