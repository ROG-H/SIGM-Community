//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.18 at 02:19:57 PM CET 
//


package es.dipucr.jaxb.juntaconsultiva.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}usuario"/>
 *         &lt;element ref="{}RegistrosEnviados"/>
 *         &lt;element ref="{}tipoAdmin"/>
 *         &lt;element ref="{}tipoAdminLocal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "usuario",
    "registrosEnviados",
    "tipoAdmin",
    "tipoAdminLocal"
})
@XmlRootElement(name = "cabecera")
public class Cabecera {

    @XmlElement(required = true)
    protected Usuario usuario;
    @XmlElement(name = "RegistrosEnviados", required = true)
    protected String registrosEnviados;
    @XmlElement(required = true)
    protected String tipoAdmin;
    @XmlElement(required = true)
    protected String tipoAdminLocal;

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link Usuario }
     *     
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link Usuario }
     *     
     */
    public void setUsuario(Usuario value) {
        this.usuario = value;
    }

    /**
     * Gets the value of the registrosEnviados property.
     * 
     * @return
     *     possible object is
     *     {@link RegistrosEnviados }
     *     
     */
    public String getRegistrosEnviados() {
        return registrosEnviados;
    }

    /**
     * Sets the value of the registrosEnviados property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistrosEnviados }
     *     
     */
    public void setRegistrosEnviados(String value) {
        this.registrosEnviados = value;
    }

    /**
     * Gets the value of the tipoAdmin property.
     * 
     * @return
     *     possible object is
     *     {@link TipoAdmin }
     *     
     */
    public String getTipoAdmin() {
        return tipoAdmin;
    }

    /**
     * Sets the value of the tipoAdmin property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoAdmin }
     *     
     */
    public void setTipoAdmin(String value) {
        this.tipoAdmin = value;
    }

    /**
     * Gets the value of the tipoAdminLocal property.
     * 
     * @return
     *     possible object is
     *     {@link TipoAdminLocal }
     *     
     */
    public String getTipoAdminLocal() {
        return tipoAdminLocal;
    }

    /**
     * Sets the value of the tipoAdminLocal property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoAdminLocal }
     *     
     */
    public void setTipoAdminLocal(String value) {
        this.tipoAdminLocal = value;
    }

}