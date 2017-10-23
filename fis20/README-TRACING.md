## Getting distributed tracing with Open Tracing / Jaeger

Open Tracing support has been recently added into Apache Camel (2.19 +) and thus can be used with Fuse Integration Services 2.0 applications to produce fragments contributing to the aggregation of a global trace allowing to get insights on a distributed business transactions.

For a better introduction on why tracing matters in distributed microservices architecture, I suggest having a look at [OpenTracing.io](http://opentracing.io/documentation/) introduction.

Here's a sample of a trace using Jaeger (an Open Tracing implementation) for the `Patient Admission` process supported by our `techlab-hospital` use-case. You may see that JMS/AMQP hops are also included into the global trace so you'll be able to have a complete view of your asynchronous services orchestration:

![trace-details](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/tracing-details.png)

_Warning: Open Tracing support has been introduced since Apache Camel 2.19 that is not yet integrated into official Fuse Integration Services 2.0. The instructions here are mentioned to upgrade and adopt an existing FIS 2.O module._

All the modifications below have been done on the `fis20-jaeger-tracing` branch in this Git repository. Be sure to checkout this branch for getting everything.

### Installation of Jaeger

[Jaeger](http://jaeger.readthedocs.io/en/latest/) is an Open Tracing implementation originally developed within Uber and now open-sourced. It may be deployed as a bunch of components on Kubernetes or OpenShift but there's also an `all-in-one` for simple development environment.

If you want to quickly setup such an environment, you may want to use the corresponding OpenShift template into a separate `jaeger-infra` project like this:

```sh
oc new-project jaeger-infra
oc process -f https://raw.githubusercontent.com/jaegertracing/jaeger-openshift/master/all-in-one/jaeger-all-in-one-template.yml | oc create -f -
```

You should then have a `jaeger-agent` service running and being reachable using this service hostname: `jaeger-agent.jaeger-infra.svc.cluster.local`. If your cluster is in multi-tenant mode, you may rather use the route to access service later on.

### Modification of Fuse components

Some light modifications should occur within the different Fuse modules in order to activate tracing. Modifications should be done and adapted for each components within our use-case:
* Enhance Maven descriptor to pull new dependencies,
* Adding new configuration to connect to Jaeger remote agent,
* Instrument application through new Java annotation,
* Add configuration element to inject Jaeger remote agent address.

#### Modification of modules pom.xml

The Maven `pom.xml`project descriptor of each module has to be modified in order to retrieve the correct interface and implementation dependencies. First, you may want to introduce a custom property to track and ease the version management of `Jaeger` like in the snippet below:

```xml
<properties>
  ...
  <jaeger.version>0.19.0</jaeger.version>
</properties>
```

Then, you need to add 2 dependencies: the `camel-opentracing-starter` that will bring into the project the correct annotation for activation and the Open Tracing interface java library ; the `jaeger-core` will bring Jaeger implementation of Open Tracing depending on requested version.

```xml
<dependency>
  <groupId>org.apache.camel</groupId>
  <artifactId>camel-opentracing-starter</artifactId>
  <version>2.19.0</version>
</dependency>
<dependency>
  <groupId>com.uber.jaeger</groupId>
  <artifactId>jaeger-core</artifactId>
  <version>${jaeger.version}</version>
</dependency>
```
Finally, you may need to override some Camel libraries that may be pulled transitively by some other dependencies and that may not have been updated to version `2.19.0`. Adding them to pom.xml enforces their resolution to 2.19.0 version:

```xml
<dependency>
  <groupId>org.apache.camel</groupId>
  <artifactId>camel-core</artifactId>
  <version>2.19.0</version>
</dependency>
```

#### Adding a Jaeger configuration for Open Tracing

Then you need to add a new configuration class to your Fuse module. This configuration will be responsible for retrieving by some mean the URL of a Jaeger endpoint for sending trace fragments and to create a new tracer that will be reused later to mark each trace with the identifier of this Fuse module.

In the snippet below loaceted into `fis20/modules/api-gateway/src/main/java/com/github/lbroudoux/techlab/ApplicationCamelConfig.java` java class, we'll use an environment variable to get the endpoint URL and we'll mark each trace has coming from `api-gateway` component:

```java
@Configuration
public class ApplicationCamelConfig {

	private static final Logger log = Logger.getLogger(ApplicationCamelConfig.class.getName());

	@Bean
	public Tracer tracer() {
    	log.info("Initializing a Tracer");

    	String jaegerURL = System.getenv("JAEGER_SERVER_HOSTNAME");
        if (jaegerURL != null) {
        	log.info("Using Jaeger Tracer");
            Sender sender = new UdpSender(jaegerURL, 0, 0);
            return new com.uber.jaeger.Tracer.Builder("api-gateway",
                    new RemoteReporter(sender, 100, 50,
                            new Metrics(new StatsFactoryImpl(new NullStatsReporter()))),
                    new ProbabilisticSampler(1.0))
                    .build();
        }
        log.info("Using Noop tracer");
        return NoopTracerFactory.create();
    }
}
```

#### Instrumenting Fuse application

We'll need to instrumentate our Fuse/Camel application with this new `org.apache.camel.opentracing.starter.CamelOpenTracing` annotation. For example, in this `fis20/modules/api-gateway/src/main/java/com/github/lbroudoux/techlab/Application.java` java class, we've added the following class annotation:

```java
...
@SpringBootApplication
@CamelOpenTracing
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends RouteBuilder {
  ...
}
```

#### Adding configuration for Jaeger agent

Finally, we have to declare a new environment variable that will be used and injected into our Fuse container at startup to retrieve the Jaeger agent endpoint. For example, in this `fis20/modules/api-gateway/src/main/fabric8/deployment.yml` YAML file, we've added an `env` entry:

```yml
...
env:
  - name: JAEGER_SERVER_HOSTNAME
    value: jaeger-agent.jaeger-infra.svc.cluster.local
...
```

### Cool features of Camel / Open Tracing integration and Jaeger

Beside the basic trace viewing shown above, here's some other cool and helpful features.

First, Camel Open Tracing implementation also attach the log statements within the route. This allows you to get the log statements right into the trace viewer, log statements timestamps are also re-computed according the startup time of the transaction:

![trace-camel-log](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/tracing-camel-log.png)

Jaeger is also able to generate a dependency graph of your application components from the traces. Basically for the `techlab-hospital`, it is able to produce such a graph:

![trace-dependencies](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/tracing-dependencies.png)
