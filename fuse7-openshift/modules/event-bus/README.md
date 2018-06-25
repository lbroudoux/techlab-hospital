# Spring-Boot Camel XML QuickStart

This example demonstrates how to configure Camel routes in Spring Boot via
a Spring XML configuration file.

The application utilizes the Spring [`@ImportResource`](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/annotation/ImportResource.html) annotation to load a Camel Context definition via a [camel-context.xml](src/main/resources/spring/camel-context.xml) file on the classpath.

**IMPORTANT**: This quickstart can run in 2 modes: standalone on your machine and on an OpenShift Cluster 

## Building

The example can be built with

    mvn clean install

### Running the Quickstart standalone on your machine

You can also run this quickstart as a standalone project directly:

Obtain the project and enter the project's directory
Build the project:

```
$ mvn clean package
$ mvn spring-boot:run 
```

### Running the Quickstart on OpenShift Cluster

The following steps assume you already have a Kubernetes / Openshift environment installed and relative tools like `oc`.
If you have a single-node OpenShift cluster, such as `Minishift`, you can also deploy your quickstart there. 
A single-node OpenShift cluster provides you with access to a cloud environment that is similar to a production environment.

**IMPORTANT**: You need to run this example on Container Development Kit 3.3 or OpenShift 3.7.
Both of these products have suitable Fuse images pre-installed. 
If you run it in an environment where those images are not preinstalled follow the steps described below.

+ Log in and create your project / namespace:
```
$ oc login -u developer -p developer
$ oc new-project MY_PROJECT_NAME
```

+ Build and deploy the project to the Kubernetes / OpenShift cluster:
```
$ mvn clean -DskipTests fabric8:deploy -Popenshift
```

### Running the Quickstart on OpenShift Cluster without preinstalled images

Following steps assume you already have a Kubernates / Openshift environment installed and relative tools like `oc`.
If you have a single-node OpenShift cluster, such as `Minishift`, you can also deploy your quickstart there. 
A single-node OpenShift cluster provides you with access to a cloud environment that is similar to a production environment.

+ Log in and create your project / namespace:
```
$ oc login -u developer -p developer
$ oc new-project MY_PROJECT_NAME
```

+ Import base images in your newly created project (MY_PROJECT_NAME):
```
$ oc import-image fis-java-openshift:2.0 --from=registry.access.redhat.com/jboss-fuse-6/fis-java-openshift:2.0 --confirm
```

+ Build and deploy the project to the OpenShift cluster:
```
$ mvn clean -DskipTests fabric8:deploy -Popenshift -Dfabric8.generator.fromMode=istag -Dfabric8.generator.from=MY_PROJECT_NAME/fis-java-openshift:2.0
```
