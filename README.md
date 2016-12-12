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

The demonstration is built onto 4 kinds of modules. From right to left on the following picture, we've got:
* Hospital transversal modules (`laboratoire` and `imagerie`), responsible for dealing with patient admission once patient has been received into a Health-care Unit. They are notified throught dedicated channel that are REST services and Messaging Queue,
* Health-care Unit modules (`uds-chirabdo` and `uds-chirortho`), responsible for processing admission event: receiving patient and then in turn notifying transversal services. They should listen a Messaging Topic for admission events,
* Administration modules (`administration`) that holds patients database and admission details. It realizes administrative admission and the publish an event but does not care on who is consuming events,
*  

![overview](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/architecture.png)  

### Console overview

![overview](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/console-screenshot.png)  

## Installation

### Pre-requisites

It is assumed that you have some kind of OpenShift cluster instance running and available. This instance can take several forms depending on your environment and needs :
* Full blown OpenShift cluster at your site, see how to [Install OpenShift at your site](https://docs.openshift.com/container-platform/3.3/install_config/index.html),
* Red Hat Container Development Kit on your laptop, see how to [Get Started with CDK](http://developers.redhat.com/products/cdk/get-started/),
* Lightweight Minishift on your laptop, see [Minishift project page](https://github.com/minishift/minishift).

You should also have the `oc` client line interface tool installed on your machine. Pick the corresponding OpenShift version from [this page](https://github.com/openshift/origin/releases).

### Installing modules

Installation of modules can be done in 2 fashions: building modules on your machine and sending result Docker images to OpenShitf cluster OR letting OpenShift retrieve, compile sources and build Docker images for you (this is called the Source-to-image) process within OpenShift terminology.

#### 1. Building on your machine

    mvn -Pf8-local-deploy

#### 2. OpenShift Source-to-image

### Demonstrating
