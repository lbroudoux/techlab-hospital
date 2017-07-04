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

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends RouteBuilder {

	Processor admitPatientComposer = new AdmitPatientComposer();
	Processor getAllPatientsProcessor = new GetAllPatientsComposer();
	
    // must have a main method spring-boot can run
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void configure() throws Exception {
    	// Configure CXF Endpoint
        CxfEndpoint cxfEndpoint = new CxfEndpoint();
        cxfEndpoint.setAddress("http://administration:80/cxf/patient");
        cxfEndpoint.setWsdlURL("administration.wsdl");
        cxfEndpoint.setCamelContext(getContext());
        cxfEndpoint.setServiceClass(com.github.lbroudoux.techlab.administration.PatientEndpoint.class);

		/*
		 * <camel:restConfiguration bindingMode="auto" component="jetty"
		      contextPath="/api" enableCORS="true" port="8080">
		      <camel:endpointProperty key="host" value="0.0.0.0"/>
		      <camel:endpointProperty key="cors" value="true"/>
		      <camel:endpointProperty key="api.title" value="Healthcare demo clinical API"/>
		      <camel:endpointProperty key="api.version" value="1.0.0"/>
		      <camel:corsHeaders key="Access-Control-Allow-Origin" value="*"/>
		      <camel:corsHeaders key="Access-Control-Allow-Methods" value="GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH"/>
		      <camel:corsHeaders key="Access-Control-Allow-Headers" value="Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"/>
		    </camel:restConfiguration>
		    <camel:rest path="/hospital">
		      <camel:post uri="/admission/{firstname}/{lastname}/{patientid}">
		        <camel:to uri="direct:admission"/>
		      </camel:post>
		      <camel:get uri="/patients/all">
		        <camel:to uri="direct:allpatients"/>
		      </camel:get>
		    </camel:rest>
		 */
        
		restConfiguration("jetty")
			.bindingMode(RestBindingMode.auto)
			.contextPath("/api")
			.enableCORS(true)
			.port(8181)
			.endpointProperty("host","0.0.0.0")
			.endpointProperty("cors", "true")
			.endpointProperty("api.title", "Healthcare demo clinical API")
			.endpointProperty("api.version", "1.0.0")
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
			.process(admitPatientComposer)
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
			.process(getAllPatientsProcessor)
			.setHeader("defaultOperationName", this.constant("getAllPatients"))
			.to(cxfEndpoint)
			.setHeader("Content-Type", this.constant("text/plain;charset=UTF-8"))
			.removeHeader("ResponseContext")
			.log("body: ${body}");
    }
}
