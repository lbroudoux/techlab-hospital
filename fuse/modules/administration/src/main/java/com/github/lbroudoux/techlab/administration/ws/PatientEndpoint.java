package com.github.lbroudoux.techlab.administration.ws;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PatientEndpoint {

    public java.lang.String getAllPatients();

    public java.lang.String admitPatient(
        com.github.lbroudoux.techlab.administration.ws.Patient patient);
}
