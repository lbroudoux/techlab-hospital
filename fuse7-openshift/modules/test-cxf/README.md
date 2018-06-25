# Spring-Boot CXF JAXWS QuickStart

This example demonstrates how you can use Apache CXF with Spring Boot.

The quickstart uses Spring Boot to configure a little application that includes a CXF JAXWS endpoint.



### Building

The example can be built with

    mvn clean install

### Running the example in OpenShift

It is assumed that:
- OpenShift platform is already running, if not you can find details how to [Install OpenShift at your site](https://docs.openshift.com/container-platform/3.3/install_config/index.html).
- Your system is configured for Fabric8 Maven Workflow, if not you can find a [Get Started Guide](https://access.redhat.com/documentation/en/red-hat-jboss-middleware-for-openshift/3/single/red-hat-jboss-fuse-integration-services-20-for-openshift/)
- The Red Hat JDG xPaaS product should already be installed and running on your OpenShift installation, one simple way to run a JDG service is following the documentation of the JDG xPaaS image for OpenShift related to the `datagrid65-basic` template


TThen the following command will package your app and run it on OpenShift:

    mvn fabric8:deploy

To list all the running pods:

    oc get pods

Then find the name of the pod that runs this quickstart, and output the logs from the running pods with:

    oc logs <name of pod>

You can also use the OpenShift [web console](https://docs.openshift.com/container-platform/3.3/getting_started/developers_console.html#developers-console-video) to manage the running pods, and view logs and much more.

### Running via an S2I Application Template

Application templates allow you deploy applications to OpenShift by filling out a form in the OpenShift console that allows you to adjust deployment parameters.  This template uses an S2I source build so that it handle building and deploying the application for you.

First, import the Fuse image streams:

    oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/GA/fis-image-streams.json

Then create the quickstart template:

    oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/GA/quickstarts/spring-boot-cxf-jaxws-template.json

Now when you use "Add to Project" button in the OpenShift console, you should see a template for this quickstart. 

### Accessing the Endpoints

To access the endpoint you first need to create an OpenShift route for the service so that it can be exposed externally.  You can use the 'oc create route' command to create the route and the 'oc get routes' to get the host name for
the route that you created.  Example:


    $ oc create route edge example1 --service=spring-boot-cxf-jaxws
    route "example1" created
    
    $ oc get routes example1
    NAME       HOST/PORT                                PATH      SERVICES                PORT      TERMINATION
    example1   example1-myproject.example.com                     spring-boot-cxf-jaxws   http      edge

You can then use the host report to access the service:

    https://example1-myproject.example.com /service/hello?wsdl
    
... will now display the generated WSDL.


### Integration Testing

The example includes a [fabric8 arquillian](https://github.com/fabric8io/fabric8/tree/v2.2.170.redhat/components/fabric8-arquillian) OpenShift Integration Test. 
Once the container image has been built and deployed in OpenShift, the integration test can be run with:

    mvn test -Dtest=*KT

The test is disabled by default and has to be enabled using `-Dtest`. Open Source Community documentation at [Integration Testing](https://fabric8.io/guide/testing.html) and [Fabric8 Arquillian Extension](https://fabric8.io/guide/arquillian.html) provide more information on writing full fledged black box integration tests for OpenShift. 


