package com.github.lbroudoux.techlab;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.github.lbroudoux.techlab.administration.AdmitPatient;

@Component(value="admitPatientComposer")
public class AdmitPatientComposer implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		AdmitPatient admitPatient = new AdmitPatient();
		
		admitPatient.setArg0(new AdmitPatient.Arg0());
		admitPatient.getArg0().setId(exchange.getIn().getHeader("patientid", String.class));
		admitPatient.getArg0().setFirstname(exchange.getIn().getHeader("firstname", String.class));
		admitPatient.getArg0().setLastname(exchange.getIn().getHeader("lastname", String.class));
	
		exchange.getIn().setBody(admitPatient.getArg0());
	}
}
