## Installation

### Pre-requisites

It is assumed you have downloaded and installed a distribution of JBoss Fuse 6.3.x for Apache Karaf using the documentation present on [Red Hat JBoss Fuse site](https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html/installation_on_apache_karaf/). This is straightforward as it usually downloading and unpacking an archive.

You just have to start Fuse in a local terminal for having access to the OSGi console once startup is finished. Fuse comes with a bundled JBoss A-MQ server that will be used.

### Installing modules

Installation of modules in the Apache Karaf based Fuse instance can be done in 2 steps: building modules on your machine and then deploying OSGi bundles corresponding to modules into Fuse.

#### 1. Building on your machine

All modules are located within `/fuse/modules` sub-directory. They have been initialized using Fuse 6.3 Maven archetypes as explained into the [Developing and Deploying Applications](https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html/developing_and_deploying_applications/develop-createroute) guide.

Each module can be simply build and stored into local Maven repository using this single command:

    mvn install

#### 2. Deploying modules

The deployment of modules should be done in a specific order so that dependencies may be resolved successfully. In order to deploy modules, you should have to issue some commands onto the terminal console where you have started the Fuse instance.

We'll start deploying the `event-bus` module but before that we'll need the optional `camel-websocket` feature that is not installed in default Fuse profile.

  osgi> features:install camel-websocket
  osgi> install -s mvn:com.github.lbroudoux.techlab.fuse/event-bus/1.0.0-SNAPSHOT

Then, we have to deploy the `laboratoire` module that need the `camel-jetty`feature present in Fuse instance.

  osgi> features:install camel-jetty
  osgi> install -s mvn:com.github.lbroudoux.techlab.fuse/laboratoire/1.0.0-SNAPSHOT

We are now able to deploy the `imagerie` module that way

  osgi> install -s mvn:com.github.lbroudoux.techlab.fuse/imagerie/1.0.0-SNAPSHOT

And finally, the other `uds-chirabdo`, `uds-chirortho`, `administration`, `api-gateway` modules that depends onto the `camel-http4` feature:

  osgi> features:install camel-http4
  osgi> install -s mvn:com.github.lbroudoux.techlab.fuse/uds-chirabdo/1.0.0-SNAPSHOT
  osgi> install -s mvn:com.github.lbroudoux.techlab.fuse/uds-chirortho/1.0.0-SNAPSHOT
  osgi> install -s mvn:com.github.lbroudoux.techlab.fuse/administration/1.0.0-SNAPSHOT
  osgi> install -s mvn:com.github.lbroudoux.techlab.fuse/api-gateway/1.0.0-SNAPSHOT

### Demonstrating

Once all modules have been deployed onto your Fuse instance, you can easily demonstrate the patient admission/registration process using the provided console.

Just open the `/fuse/console/index.html` file within a browser. When running locally, IP of Fuse instance and ports for requesting/listening to REST and WebSocket endpoints should be configured. This can be easily done using the inputs into header bar of the console.

Obviously, you can also edit the HTML source ;-)
