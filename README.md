# techlab-hospital

This demo helps demonstrate how [Red Hat JBoss Fuse solution](https://developers.redhat.com/products/fuse/overview/) can be used on combination of [OpenShift](https://www.openshift.org) to provides Integration as a Microservices platform.

The business scenario can be introduced that way: an hospital is a very complex organization involving many different units working together; from transversal services units like laboratory, radiology to administration holding central view of patient going through dedicated Health-care units focused on specific health issues.

The Information System part of each units involved may have evolved over time in heterogeneous ways. More over, each unit Information System may have been designed (or most probably refactored ;-)) to present the qualities of a Microservices Based Architecture:
* Units are autonomous and consistent parts,
* Units collaborates through promises called services,
* Units are resilients and fault-tolerant.

As a consequence each unit has selected the best technology for the job and there's some kind of redundancy of data between units. However complex business process should be executed and tracked using this distributed and heterogeneous elements.

In this demonstration, we'll focus on the patient admission process: when patient comes into hospital for surgery (for example), he or she is facing a sole unit that is the Administration. This unit is then in charge of publishing an event saying 'Administrative admission of patient done in surgery HCU X' ; this event should then being processed by the target unit whom in turn has to notify transversal services of the patient incoming.

## Architecture overview

The demonstration is built onto 4 kinds of modules as shown in picture below.

![overview](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/architecture.png)  

From right to left on the following picture, we've got:
* Hospital transversal modules (`laboratoire` and `imagerie`), responsible for dealing with patient admission once patient has been received into a Health-care Unit. They are notified throught dedicated channel that are REST services and Messaging Queue,
* Health-care Unit modules (`uds-chirabdo` and `uds-chirortho`), responsible for processing admission event: receiving patient and then in turn notifying transversal services. They should listen a Messaging Topic for admission events,
* Administration modules (`administration`) that holds patients database and admission details. Admission is trigerred through a SOAP service, realizes administrative admission and the publish an event but does not care on who is consuming events,
* Technical modules (`api-gateway` and `event-bus`) that are in charge of exposing easy-to-use REST interface to administration services and collecting/aggregating/re-dispatching business events from all the other modules using WebSocket (think about `event-bus` as a stub for implementing stuffs like BAM or BPM tracking).

### Console overview

For testing/demonstration purpose, we introduce a web console allowing you to register an admission for an incoming patient, checking list of admitted patients and receiving business events to control that admission process is executing well.

![overview](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/console-screenshot.png)  

Register a patient involves the admission form on the left. Following/tracking business events is done through the panel on the right where events appears as process flows.

## Installation

### Pre-requisites

It is assumed that you have some kind of OpenShift cluster instance running and available. This instance can take several forms depending on your environment and needs :
* Full blown OpenShift cluster at your site, see how to [Install OpenShift at your site](https://docs.openshift.com/container-platform/3.3/install_config/index.html),
* Red Hat Container Development Kit on your laptop, see how to [Get Started with CDK](http://developers.redhat.com/products/cdk/get-started/),
* Lightweight Minishift on your laptop, see [Minishift project page](https://github.com/minishift/minishift).

You should also have the `oc` client line interface tool installed on your machine. Pick the corresponding OpenShift version from [this page](https://github.com/openshift/origin/releases).

Once your OpenShift instance is up and running, ensure you've got the Red Hat Fuse Integration Services images installed onto OpenShift. You ca check this going to the catalogue view of OpenShift web console. If not present, you can run the following command for installing missing image streams and templates :

    oc create -f https://raw.githubusercontent.com/openshift/openshift-ansible/master/roles/openshift_examples/files/examples/v1.3/xpaas-streams/fis-image-streams.json -n openshift
    oc create -f https://raw.githubusercontent.com/openshift/openshift-ansible/master/roles/openshift_examples/files/examples/v1.3/xpaas-templates/amq62-basic.json -n openshift

You need now to deploy a JBoss A-MQ instance in order to later deploy the project's modules. In order to isolate stuffs, create a new OpenShift project and then add a `JBoss A-MQ 6.2 basic` application, letting all the default values _expecting for username and password that both should be set to `admin`_.

### Installing modules

Installation of modules can be done in 2 fashions: building modules on your machine and sending result Docker images to OpenShift cluster OR letting OpenShift retrieve, compile sources and build Docker images for you (this is called the Source-to-image) process within OpenShift terminology.

#### 1. Building on your machine

All modules are located within `/modules` sub-directory. They have been initialized using Fuse Integration Services Maven archetypes as explained onto [Red Hat xPaaS site](https://access.redhat.com/documentation/en/red-hat-xpaas/0/single/red-hat-xpaas-fuse-integration-services-image).

Before building and deploying module, you should check your local environment is correctly configured for using Docker daemon embedded into CDK or Minishift.

For CDK, run the following command from the CDK directory:

    eval "$(vagrant service-manager env docker)"

For Minishift, run the following command:

    eval $(minishift docker-env)

Then, modules can be simply build and deployed using this single command:

    mvn -Pf8-local-deploy

You can check current deployment by listing all the running pods:

    oc get pods


#### 2. OpenShift Source-to-image

All modules located within `/modules` sub-directory contain OpenShift templates at root of module. For each template, the resource file is `quickstart-template.json`.

You can simply deploy each module on your OpenShift cluster instance using the usual `oc` CLI:

    oc new-app -f quickstart-template.json

You can check current builds by listing all the running pods:

    oc get pods

When running Source-to-image, build time can be very long because of all the dependencies download within Source-to-image builder images. In this case, we recommend forking/cloning this repository and adapting the Maven `/configuration/settings.xml` file present into each module sub-directory. Just add a new mirror section and point out to your local artifacts repository (Nexus or whatever).

### Demonstrating

Once all modules have been deployed onto your OpenShift instance, you can easily demonstrate the patient admission/registration process using the provided console.

Just open the `/console/index.html` file within a browser. When running locally, IP of OpenShift instance and ports for requesting/listening to REST and WebSocket endpoints should be configured. This can be easily done using the inputs into header bar of the console.

To get all the informations, we could use these command within Minishift:

    minishift service list
    |------------------|-------------------|-----------------------------|
    |    NAMSEPACE     |       NAME        |             URL             |
    |------------------|-------------------|-----------------------------|
    | default          | docker-registry   | http://192.168.99.100:32246 |
    | default          | kubernetes        | No node port                |
    | techlab-hospital | administration    | http://192.168.99.100:32276 |
    | techlab-hospital | api-gateway       | http://192.168.99.100:31547 |
    | techlab-hospital | broker-amq-amqp   | No node port                |
    | techlab-hospital | broker-amq-mqtt   | No node port                |
    | techlab-hospital | broker-amq-stomp  | No node port                |
    | techlab-hospital | broker-amq-tcp    | No node port                |
    | techlab-hospital | event-bus         | http://192.168.99.100:32707 |
    | techlab-hospital | laboratoire       | http://192.168.99.100:31832 |
    |------------------|-------------------|-----------------------------|

and select the ports for `api-gateway` and `event-bus` pods.

Obviously, you can also edit the HTML source ;-)
