# techlab-hospital

This demo helps demonstrate how [Red Hat JBoss Fuse solution](https://developers.redhat.com/products/fuse/overview/) can be used on combination of [OpenShift](https://www.openshift.org) to provide Integration as a Microservices platform.

This repository provides implementations of the same business scenario using:
* Fuse Integration Services 1.0 (FIS 1.0) that packages each module into a Hawt-app Docker container,
* Fuse Integration Services 2.0 (FIS 2.0) that packages each module into a Spring-boot Docker container,
* Fuse 6.3.0 _traditionnal_ that packages each module into a OSGi bundle for using in a standard Apache Karaf platform.
Thus, you'll be able to compare changes in building, deploying and monitoring applications using these 3 ways.

The business scenario can be introduced that way: an hospital is a very complex organization involving many different units working together; from transversal services units like laboratory, radiology to administration holding central view of patient going through dedicated Health-care units focused on specific health issues.

The Information System part of each units involved may have evolved over time in heterogeneous ways. More over, each unit Information System may have been designed (or most probably refactored ;-)) to present the qualities of a Microservices Based Architecture:
* Units are autonomous and consistent parts,
* Units collaborates through promises called services,
* Units are resilients and fault-tolerant.

As a consequence each unit has selected the best technology for the job and there's some kind of redundancy of data between units. However complex business process should be executed and tracked using this distributed and heterogeneous elements.

In this demonstration, we'll focus on the patient admission process: when patient comes into hospital for surgery (for example), he or she is facing a sole unit that is the Administration. This unit is then in charge of publishing an event saying 'Administrative admission of patient done in surgery HCU X' ; this event should then being processed by the target unit whom in turn has to notify transversal services of the patient incoming.

_Note: this demonstration is a derivative work from Christina Lin's healthcare demo on OpenShift (see https://github.com/weimeilin79/healthcaredemos2i). My aim was to simplify Christina's demo but also to diversify the range of demonstrated technologies using Apache Camel._

*28-Feb-2017 update* We've complete this use case with the addition of API Management capabilities on top of demonstration. If your interested, just check the [documentation](https://github.com/lbroudoux/techlab-hospital/blob/master/README-API-MANAGEMENT.md).

## Architecture overview

The demonstration is built onto 4 kinds of modules as shown in picture below.

![overview](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/architecture.png)  

From right to left on the following picture, we've got:
* Hospital transversal modules (`laboratoire` and `imagerie`), responsible for dealing with patient admission once patient has been received into a Health-care Unit. They are notified throught dedicated channel that are REST services and Messaging Queue,
* Health-care Unit modules (`uds-chirabdo` and `uds-chirortho`), responsible for processing admission event: receiving patient and then in turn notifying transversal services. They should listen a Messaging Topic for admission events,
* Administration modules (`administration`) that holds patients database and admission details. Admission is trigerred through a SOAP service, realizes administrative admission and the publish an event but does not care on who is consuming events,
* Technical modules (`api-gateway` and `event-bus`) that are in charge of exposing easy-to-use REST interface to administration services and collecting/aggregating/re-dispatching business events from all the other modules using WebSocket (think about `event-bus` as a stub for implementing stuffs like BAM or BPM tracking).

### Console overview

For testing/demonstration purpose, we introduce a web console allowing you to register an admission for an incoming patient, checking list of admitted patients and receiving business events to control that admission process is executing well.

![overview](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/console-screenshot.png)  

Register a patient involves the admission form on the left. Following/tracking business events is done through the panel on the right where events appears as process flows.

## Installing and using

For pre-requisites, installation and deployment process, you should have a look at the README of the different implementation methods:
* [README](https://github.com/lbroudoux/techlab-hospital/blob/master/fis10/README.md) for Fuse Integration Services 1.0 implementation,
* [README](https://github.com/lbroudoux/techlab-hospital/blob/master/fis20/README.md) for Fuse Integration Services 2.0 implementation,
* [README](https://github.com/lbroudoux/techlab-hospital/blob/master/fuse/README.md) for _traditionnal_ Fuse implementation.
