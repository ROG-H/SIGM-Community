//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.27 at 02:03:34 PM CEST 
//


package es.dipucr.tablonEdictalUnico.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoContenido complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoContenido">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}texto"/>
 *         &lt;element ref="{}pieFirma"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoContenido", propOrder = {
    "texto",
    "pieFirma"
})
public class TipoContenido {

    @XmlElement(required = true)
    protected Texto texto;
    @XmlElement(required = true)
    protected PieFirma pieFirma;

    /**
     * Gets the value of the texto property.
     * 
     * @return
     *     possible object is
     *     {@link Texto }
     *     
     */
    public Texto getTexto() {
        return texto;
    }

    /**
     * Sets the value of the texto property.
     * 
     * @param value
     *     allowed object is
     *     {@link Texto }
     *     
     */
    public void setTexto(Texto value) {
        this.texto = value;
    }

    /**
     * Gets the value of the pieFirma property.
     * 
     * @return
     *     possible object is
     *     {@link PieFirma }
     *     
     */
    public PieFirma getPieFirma() {
        return pieFirma;
    }

    /**
     * Sets the value of the pieFirma property.
     * 
     * @param value
     *     allowed object is
     *     {@link PieFirma }
     *     
     */
    public void setPieFirma(PieFirma value) {
        this.pieFirma = value;
    }

}