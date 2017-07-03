package com.github.lbroudoux.techlab.fuse;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * 
 * @author laurent
 */
public class GetAllPatientsComposer implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setBody(new Object[0]);
	}
}
