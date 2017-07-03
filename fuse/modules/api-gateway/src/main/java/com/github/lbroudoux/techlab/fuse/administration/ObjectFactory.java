
package com.github.lbroudoux.techlab.fuse.administration;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.github.lbroudoux.techlab.fuse.administration package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetAllPatients_QNAME = new QName("http://ws.administration.techlab.lbroudoux.github.com/", "getAllPatients");
    private final static QName _AdmitPatientResponse_QNAME = new QName("http://ws.administration.techlab.lbroudoux.github.com/", "admitPatientResponse");
    private final static QName _AdmitPatient_QNAME = new QName("http://ws.administration.techlab.lbroudoux.github.com/", "admitPatient");
    private final static QName _GetAllPatientsResponse_QNAME = new QName("http://ws.administration.techlab.lbroudoux.github.com/", "getAllPatientsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.github.lbroudoux.techlab.fuse.administration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AdmitPatient }
     * 
     */
    public AdmitPatient createAdmitPatient() {
        return new AdmitPatient();
    }

    /**
     * Create an instance of {@link GetAllPatientsResponse }
     * 
     */
    public GetAllPatientsResponse createGetAllPatientsResponse() {
        return new GetAllPatientsResponse();
    }

    /**
     * Create an instance of {@link AdmitPatientResponse }
     * 
     */
    public AdmitPatientResponse createAdmitPatientResponse() {
        return new AdmitPatientResponse();
    }

    /**
     * Create an instance of {@link GetAllPatients }
     * 
     */
    public GetAllPatients createGetAllPatients() {
        return new GetAllPatients();
    }

    /**
     * Create an instance of {@link Patient }
     * 
     */
    public Patient createPatient() {
        return new Patient();
    }

    /**
     * Create an instance of {@link AdmitPatient.Arg0 }
     * 
     */
    public AdmitPatient.Arg0 createAdmitPatientArg0() {
        return new AdmitPatient.Arg0();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllPatients }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.administration.techlab.lbroudoux.github.com/", name = "getAllPatients")
    public JAXBElement<GetAllPatients> createGetAllPatients(GetAllPatients value) {
        return new JAXBElement<GetAllPatients>(_GetAllPatients_QNAME, GetAllPatients.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdmitPatientResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.administration.techlab.lbroudoux.github.com/", name = "admitPatientResponse")
    public JAXBElement<AdmitPatientResponse> createAdmitPatientResponse(AdmitPatientResponse value) {
        return new JAXBElement<AdmitPatientResponse>(_AdmitPatientResponse_QNAME, AdmitPatientResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdmitPatient }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.administration.techlab.lbroudoux.github.com/", name = "admitPatient")
    public JAXBElement<AdmitPatient> createAdmitPatient(AdmitPatient value) {
        return new JAXBElement<AdmitPatient>(_AdmitPatient_QNAME, AdmitPatient.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllPatientsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.administration.techlab.lbroudoux.github.com/", name = "getAllPatientsResponse")
    public JAXBElement<GetAllPatientsResponse> createGetAllPatientsResponse(GetAllPatientsResponse value) {
        return new JAXBElement<GetAllPatientsResponse>(_GetAllPatientsResponse_QNAME, GetAllPatientsResponse.class, null, value);
    }

}
