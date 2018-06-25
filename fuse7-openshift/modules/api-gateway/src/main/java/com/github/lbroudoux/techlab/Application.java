/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.github.lbroudoux.techlab;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.opentracing.starter.CamelOpenTracing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@SpringBootApplication
@CamelOpenTracing
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends RouteBuilder {

	// Below processor are no more necessary as we are using annotations on Java classes.
	//Processor admitPatientComposer = new AdmitPatientComposer();
	//Processor getAllPatientsProcessor = new GetAllPatientsComposer();
	
    // must have a main method spring-boot can run
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void configure() throws Exception {
    	// Configure CXF Endpoint
        CxfEndpoint cxfEndpoint = new CxfEndpoint();
        cxfEndpoint.setAddress("http://administration:8080/services/cxf/patient");
        cxfEndpoint.setWsdlURL("administration.wsdl");
        cxfEndpoint.setCamelContext(getContext());
        cxfEndpoint.setServiceClass(com.github.lbroudoux.techlab.administration.PatientEndpoint.class);

		restConfiguration("servlet")
			.bindingMode(RestBindingMode.auto)
			.contextPath("/api")
			.enableCORS(true)
			.port(8080)
			.apiProperty("host","0.0.0.0")
			.apiProperty("cors", "true")
			.apiProperty("api.title", "Healthcare demo clinical API")
			.apiProperty("api.version", "1.0.0")
			.corsHeaderProperty("Access-Control-Allow-Origin", "*")
			.corsHeaderProperty("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH")
			.corsHeaderProperty("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
		
		rest("/hospital")
			.post("/admission/{firstname}/{lastname}/{patientid}").to("direct:admission")
			.get("/patients/all").to("direct:allpatients");
		
		from("direct:admission").routeId("doAdmission")
			.log("Admission request processing")
			.log("patient id: ${headers.patientid}")
			.log("patient firstname: ${headers.firstname}")
			.log("patient lastname: ${headers.lastname}")
			.process("admitPatientComposer")
			//.marshal(new JaxbDataFormat())
			.log("Composed SOAP body: ${body}")
			.setHeader("operationName", this.constant("admitPatient"))
			.to(cxfEndpoint)
//			.to("cxf://http://localhost:8181/cxf/administration/patient?dataFormat=MESSAGE&wsdlURL=administration.wsdl"
//					+ "&serviceName={http://ws.administration.techlab.lbroudoux.github.com/}PatientEndpointService"
//					+ "&portName={http://ws.administration.techlab.lbroudoux.github.com/}PatientEndpointPort")
			.log("body: ${body}");
		
		from("direct:allpatients").routeId("doAllPatients")
			.log("AllPatients request processing")
			.process("getAllPatientsProcessor")
			.setHeader("defaultOperationName", this.constant("getAllPatients"))
			.to(cxfEndpoint)
			.setHeader("Content-Type", this.constant("text/plain;charset=UTF-8"))
			.removeHeader("ResponseContext")
			.log("body: ${body}");
    }
}
