package com.github.lbroudoux.techlab.administration.ws;

import com.github.lbroudoux.techlab.administration.Patient;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PatientEndpoint {

	public String admitPatient(Patient in);
	
	public String getAllPatients();
}
