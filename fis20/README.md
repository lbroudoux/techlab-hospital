## Installation

*20-Oct-2017 update* This implementation has been completed with the support of distributed tracing through Open Tracing / Jaeger additions. If you're interested, just check the [documentation](https://github.com/lbroudoux/techlab-hospital/blob/master/fis20/README-TRACING.md).

*23-Aug-2017 update* We've complete this implementation with the addition of Application metrics monitoring using Hawkular OpenShift Agent. If you're interested, just check the [documentation](https://github.com/lbroudoux/techlab-hospital/blob/master/fis20/README-HOSA.md).

### Pre-requisites

It is assumed that you have some kind of OpenShift cluster instance running and available. This instance can take several forms depending on your environment and needs :
* Full blown OpenShift cluster at your site, see how to [Install OpenShift at your site](https://docs.openshift.com/container-platform/3.5/install_config/index.html),
* Red Hat Container Development Kit on your laptop, see how to [Get Started with CDK](http://developers.redhat.com/products/cdk/get-started/),
* Lightweight Minishift on your laptop, see [Minishift project page](https://github.com/minishift/minishift).

You should also have the `oc` client line interface tool installed on your machine. Pick the corresponding OpenShift version from [this page](https://github.com/openshift/origin/releases).

Once your OpenShift instance is up and running, ensure you've got the Red Hat Fuse Integration Services images installed onto OpenShift. You can check this going to the catalogue view of OpenShift web console. If not present, you can run the following command for installing missing image streams and templates :

    oc create -f https://raw.githubusercontent.com/openshift/openshift-ansible/master/roles/openshift_examples/files/examples/v1.5/xpaas-streams/fis-image-streams.json -n openshift
    oc create -f https://raw.githubusercontent.com/openshift/openshift-ansible/master/roles/openshift_examples/files/examples/v1.5/xpaas-streams/jboss-image-streams.json -n openshift
    oc create -f https://raw.githubusercontent.com/openshift/openshift-ansible/master/roles/openshift_examples/files/examples/v1.5/xpaas-templates/amq62-basic.json -n openshift

You need now to deploy a JBoss A-MQ instance in order to later deploy the project's modules. In order to isolate stuffs, create a new OpenShift project and then add a `JBoss A-MQ 6.2 basic` application, letting all the default values _expecting for username and password that both should be set to `admin`_.

### Installing modules

Installation of modules can be done in 2 fashions: building modules on your machine and sending result Docker images to OpenShift cluster OR letting OpenShift retrieve, compile sources and build Docker images for you (this is called the Source-to-image) process within OpenShift terminology.

#### 1. Building on your machine

All modules are located within `/fis20/modules` sub-directory. They have been initialized using Fuse Integration Services 2.0 Maven archetypes as explained onto [Red Hat JBoss Fuse site](https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html/fuse_integration_services_2.0_for_openshift/).

Before building and deploying module, you should check your local environment is correctly configured for using Docker daemon embedded into CDK or Minishift.

For CDK, run the following command from the CDK directory:

    eval "$(vagrant service-manager env docker)"

For Minishift, run the following command:

    eval $(minishift docker-env)

Then, modules can be simply build and deployed using this single command:

    mvn fabric8:deploy

You can check current deployment by listing all the running pods:

    oc get pods

#### 2. OpenShift Source-to-image

TODO

### Demonstrating

Once all modules have been deployed onto your OpenShift instance, you can easily demonstrate the patient admission/registration process using the provided console.

Just open the `/fis20/console/index.html` file within a browser. When running locally, IP of OpenShift instance and ports for requesting/listening to REST and WebSocket endpoints should be configured. This can be easily done using the inputs into header bar of the console.

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
