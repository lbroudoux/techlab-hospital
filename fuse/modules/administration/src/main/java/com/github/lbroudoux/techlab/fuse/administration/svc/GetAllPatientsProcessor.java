package com.github.lbroudoux.techlab.fuse.administration.svc;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * 
 * @author laurent
 */
public class GetAllPatientsProcessor implements Processor {

	private PatientService service;
	
	public void setPatientService(PatientService service) {
		this.service = service;
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {
		String output = service.getAllPatientsInTxt();
		exchange.getOut().setBody(output);
	}
}
