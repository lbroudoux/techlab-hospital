## Installation

### Pre-requisites

It is assumed that you have some kind of OpenShift cluster instance running and available. This instance can take several forms depending on your environment and needs :
* Full blown OpenShift cluster at your site, see how to [Install OpenShift at your site](https://docs.openshift.com/container-platform/3.5/install_config/index.html),
* Red Hat Container Development Kit on your laptop, see how to [Get Started with CDK](http://developers.redhat.com/products/cdk/get-started/),
* Lightweight Minishift on your laptop, see [Minishift project page](https://github.com/minishift/minishift).

You should also have the `oc` client line interface tool installed on your machine. Pick the corresponding OpenShift version from [this page](https://github.com/openshift/origin/releases).

Once your OpenShift instance is up and running, ensure you've got the Red Hat Fuse Integration Services images installed onto OpenShift. You can check this going to the catalogue view of OpenShift web console. If not present, you can run the following command for installing missing image streams and templates :

    oc create -f https://raw.githubusercontent.com/openshift/openshift-ansible/master/roles/openshift_examples/files/examples/v1.3/xpaas-streams/fis-image-streams.json -n openshift
    oc create -f https://raw.githubusercontent.com/openshift/openshift-ansible/master/roles/openshift_examples/files/examples/v1.3/xpaas-streams/jboss-image-streams.json -n openshift
    oc create -f https://raw.githubusercontent.com/openshift/openshift-ansible/master/roles/openshift_examples/files/examples/v1.3/xpaas-templates/amq62-basic.json -n openshift

You need now to deploy a JBoss A-MQ instance in order to later deploy the project's modules. In order to isolate stuffs, create a new OpenShift project and then add a `JBoss A-MQ 6.2 basic` application, letting all the default values _expecting for username and password that both should be set to `admin`_.

### Installing modules

Installation of modules can be done in 2 fashions: building modules on your machine and sending result Docker images to OpenShift cluster OR letting OpenShift retrieve, compile sources and build Docker images for you (this is called the Source-to-image) process within OpenShift terminology.

#### 1. Building on your machine

All modules are located within `/fis10/modules` sub-directory. They have been initialized using Fuse Integration Services Maven archetypes as explained onto [Red Hat xPaaS site](https://access.redhat.com/documentation/en/red-hat-xpaas/0/single/red-hat-xpaas-fuse-integration-services-image).

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

All modules located within `/fis10/modules` sub-directory contain OpenShift templates at root of module. For each template, the resource file is `quickstart-template.json`.

You can simply deploy each module on your OpenShift cluster instance using the usual `oc` CLI:

    oc new-app -f quickstart-template.json

You can check current builds by listing all the running pods:

    oc get pods

When running Source-to-image, build time can be very long because of all the dependencies download within Source-to-image builder images. In this case, we recommend forking/cloning this repository and adapting the Maven `/configuration/settings.xml` file present into each module sub-directory. Just add a new mirror section and point out to your local artifacts repository (Nexus or whatever).

### Demonstrating

Once all modules have been deployed onto your OpenShift instance, you can easily demonstrate the patient admission/registration process using the provided console.

Just open the `/fis10/console/index.html` file within a browser. When running locally, IP of OpenShift instance and ports for requesting/listening to REST and WebSocket endpoints should be configured. This can be easily done using the inputs into header bar of the console.

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
