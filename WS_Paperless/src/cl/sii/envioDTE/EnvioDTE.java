//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.14 at 05:32:40 PM CLT 
//


package cl.sii.envioDTE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="SetDTE">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Caratula">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="RutEmisor">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.sii.cl/SiiDte}RUTType">
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="RutEnvia" type="{http://www.sii.cl/SiiDte}RUTType"/>
 *                             &lt;element name="RutReceptor" type="{http://www.sii.cl/SiiDte}RUTType"/>
 *                             &lt;element name="FchResol" type="{http://www.sii.cl/SiiDte}FechaType"/>
 *                             &lt;element name="NroResol" type="{http://www.sii.cl/SiiDte}NroResolType"/>
 *                             &lt;element name="TmstFirmaEnv" type="{http://www.sii.cl/SiiDte}FechaHoraType"/>
 *                             &lt;element name="SubTotDTE" maxOccurs="20">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="TpoDTE" type="{http://www.sii.cl/SiiDte}DOCType"/>
 *                                       &lt;element name="NroDTE" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" fixed="1.0" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element ref="{http://www.sii.cl/SiiDte}DTE" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" fixed="1.0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "setDTE",
    "signature"
})
@XmlRootElement(name = "EnvioDTE")
public class EnvioDTE {

    @XmlElement(name = "SetDTE", required = true)
    protected EnvioDTE.SetDTE setDTE;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected SignatureType signature;
    @XmlAttribute(required = true)
    protected BigDecimal version;

    /**
     * Gets the value of the setDTE property.
     * 
     * @return
     *     possible object is
     *     {@link EnvioDTE.SetDTE }
     *     
     */
    public EnvioDTE.SetDTE getSetDTE() {
        return setDTE;
    }

    /**
     * Sets the value of the setDTE property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnvioDTE.SetDTE }
     *     
     */
    public void setSetDTE(EnvioDTE.SetDTE value) {
        this.setDTE = value;
    }

    /**
     * Firma Digital sobre SetDTE
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Firma Digital sobre SetDTE
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVersion() {
        if (version == null) {
            return new BigDecimal("1.0");
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVersion(BigDecimal value) {
        this.version = value;
    }


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
     *         &lt;element name="Caratula">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="RutEmisor">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.sii.cl/SiiDte}RUTType">
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="RutEnvia" type="{http://www.sii.cl/SiiDte}RUTType"/>
     *                   &lt;element name="RutReceptor" type="{http://www.sii.cl/SiiDte}RUTType"/>
     *                   &lt;element name="FchResol" type="{http://www.sii.cl/SiiDte}FechaType"/>
     *                   &lt;element name="NroResol" type="{http://www.sii.cl/SiiDte}NroResolType"/>
     *                   &lt;element name="TmstFirmaEnv" type="{http://www.sii.cl/SiiDte}FechaHoraType"/>
     *                   &lt;element name="SubTotDTE" maxOccurs="20">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="TpoDTE" type="{http://www.sii.cl/SiiDte}DOCType"/>
     *                             &lt;element name="NroDTE" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" fixed="1.0" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element ref="{http://www.sii.cl/SiiDte}DTE" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "caratula",
        "dte"
    })
    public static class SetDTE {

        @XmlElement(name = "Caratula", required = true)
        protected EnvioDTE.SetDTE.Caratula caratula;
        @XmlElement(name = "DTE", required = true)
        protected List<DTEDefType> dte;
        @XmlAttribute(name = "ID", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected String id;

        /**
         * Gets the value of the caratula property.
         * 
         * @return
         *     possible object is
         *     {@link EnvioDTE.SetDTE.Caratula }
         *     
         */
        public EnvioDTE.SetDTE.Caratula getCaratula() {
            return caratula;
        }

        /**
         * Sets the value of the caratula property.
         * 
         * @param value
         *     allowed object is
         *     {@link EnvioDTE.SetDTE.Caratula }
         *     
         */
        public void setCaratula(EnvioDTE.SetDTE.Caratula value) {
            this.caratula = value;
        }

        /**
         * Documento Tributario Electronico Gets the value of the dte property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the dte property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDTE().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DTEDefType }
         * 
         * 
         */
        public List<DTEDefType> getDTE() {
            if (dte == null) {
                dte = new ArrayList<DTEDefType>();
            }
            return this.dte;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setID(String value) {
            this.id = value;
        }


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
         *         &lt;element name="RutEmisor">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.sii.cl/SiiDte}RUTType">
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="RutEnvia" type="{http://www.sii.cl/SiiDte}RUTType"/>
         *         &lt;element name="RutReceptor" type="{http://www.sii.cl/SiiDte}RUTType"/>
         *         &lt;element name="FchResol" type="{http://www.sii.cl/SiiDte}FechaType"/>
         *         &lt;element name="NroResol" type="{http://www.sii.cl/SiiDte}NroResolType"/>
         *         &lt;element name="TmstFirmaEnv" type="{http://www.sii.cl/SiiDte}FechaHoraType"/>
         *         &lt;element name="SubTotDTE" maxOccurs="20">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="TpoDTE" type="{http://www.sii.cl/SiiDte}DOCType"/>
         *                   &lt;element name="NroDTE" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" fixed="1.0" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "rutEmisor",
            "rutEnvia",
            "rutReceptor",
            "fchResol",
            "nroResol",
            "tmstFirmaEnv",
            "subTotDTE"
        })
        public static class Caratula {

            @XmlElement(name = "RutEmisor", required = true)
            protected String rutEmisor;
            @XmlElement(name = "RutEnvia", required = true)
            protected String rutEnvia;
            @XmlElement(name = "RutReceptor", required = true)
            protected String rutReceptor;
            @XmlElement(name = "FchResol", required = true)
            protected XMLGregorianCalendar fchResol;
            @XmlElement(name = "NroResol", required = true)
            protected BigInteger nroResol;
            @XmlElement(name = "TmstFirmaEnv", required = true)
            protected XMLGregorianCalendar tmstFirmaEnv;
            @XmlElement(name = "SubTotDTE", required = true)
            protected List<EnvioDTE.SetDTE.Caratula.SubTotDTE> subTotDTE;
            @XmlAttribute(required = true)
            protected BigDecimal version;

            /**
             * Gets the value of the rutEmisor property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRutEmisor() {
                return rutEmisor;
            }

            /**
             * Sets the value of the rutEmisor property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRutEmisor(String value) {
                this.rutEmisor = value;
            }

            /**
             * Gets the value of the rutEnvia property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRutEnvia() {
                return rutEnvia;
            }

            /**
             * Sets the value of the rutEnvia property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRutEnvia(String value) {
                this.rutEnvia = value;
            }

            /**
             * Gets the value of the rutReceptor property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRutReceptor() {
                return rutReceptor;
            }

            /**
             * Sets the value of the rutReceptor property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRutReceptor(String value) {
                this.rutReceptor = value;
            }

            /**
             * Gets the value of the fchResol property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getFchResol() {
                return fchResol;
            }

            /**
             * Sets the value of the fchResol property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setFchResol(XMLGregorianCalendar value) {
                this.fchResol = value;
            }

            /**
             * Gets the value of the nroResol property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getNroResol() {
                return nroResol;
            }

            /**
             * Sets the value of the nroResol property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setNroResol(BigInteger value) {
                this.nroResol = value;
            }

            /**
             * Gets the value of the tmstFirmaEnv property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getTmstFirmaEnv() {
                return tmstFirmaEnv;
            }

            /**
             * Sets the value of the tmstFirmaEnv property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setTmstFirmaEnv(XMLGregorianCalendar value) {
                this.tmstFirmaEnv = value;
            }

            /**
             * Gets the value of the subTotDTE property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the subTotDTE property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getSubTotDTE().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link EnvioDTE.SetDTE.Caratula.SubTotDTE }
             * 
             * 
             */
            public List<EnvioDTE.SetDTE.Caratula.SubTotDTE> getSubTotDTE() {
                if (subTotDTE == null) {
                    subTotDTE = new ArrayList<EnvioDTE.SetDTE.Caratula.SubTotDTE>();
                }
                return this.subTotDTE;
            }

            /**
             * Gets the value of the version property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getVersion() {
                if (version == null) {
                    return new BigDecimal("1.0");
                } else {
                    return version;
                }
            }

            /**
             * Sets the value of the version property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setVersion(BigDecimal value) {
                this.version = value;
            }


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
             *         &lt;element name="TpoDTE" type="{http://www.sii.cl/SiiDte}DOCType"/>
             *         &lt;element name="NroDTE" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
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
                "tpoDTE",
                "nroDTE"
            })
            public static class SubTotDTE {

                @XmlElement(name = "TpoDTE", required = true)
                protected BigInteger tpoDTE;
                @XmlElement(name = "NroDTE", required = true)
                @XmlSchemaType(name = "positiveInteger")
                protected BigInteger nroDTE;

                /**
                 * Gets the value of the tpoDTE property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getTpoDTE() {
                    return tpoDTE;
                }

                /**
                 * Sets the value of the tpoDTE property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setTpoDTE(BigInteger value) {
                    this.tpoDTE = value;
                }

                /**
                 * Gets the value of the nroDTE property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getNroDTE() {
                    return nroDTE;
                }

                /**
                 * Sets the value of the nroDTE property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setNroDTE(BigInteger value) {
                    this.nroDTE = value;
                }

            }

        }

    }

}
