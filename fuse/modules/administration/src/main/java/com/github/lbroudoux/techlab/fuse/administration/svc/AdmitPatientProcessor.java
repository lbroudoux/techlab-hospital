package com.github.lbroudoux.techlab.fuse.administration.svc;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.github.lbroudoux.techlab.administration.ws.Patient;
/**
 * 
 * @author laurent
 */
public class AdmitPatientProcessor implements Processor{

	private PatientService service;
	
	public void setPatientService(PatientService service) {
		this.service = service;
	}
	
	@Override
    public void process(Exchange exchange) throws Exception {
		Patient patient = exchange.getIn().getBody(Patient.class);
		
		PatientInfo info = service.getPatientInfo(patient.getFirstname(), patient.getLastname(), patient.getId());
		
		String output = info.getId();
		exchange.getOut().setBody(output);
		//exchange.getIn().setBody(output);
    }
}
