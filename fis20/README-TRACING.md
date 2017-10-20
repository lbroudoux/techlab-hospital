## Getting distributed tracing with Open Tracing / Jaeger

Open Tracing support has been recently added into Apache Camel (2.9+) and thus can be used with Fuse Integration Services 2.0 applications to produce fragments contributing to the aggregation of a global trace allowing to get insights on a distributed business transactions.

For a better introduction on why tracing matters in distributed microservices architecture, I suggest having a look at [OpenTracing.io](http://opentracing.io/documentation/) introduction.

Here's a sample of a trace using Jaeger (an Open Tracing implementation) for the `Patient Admission` process supported by our techlab-hospital use-case :

![trace-details](https://raw.githubusercontent.com/lbroudoux/techlab-hospital/master/assets/tracing-details.png)


### Installation of Jaeger

```
oc new-project jaeger-infra
oc process -f https://raw.githubusercontent.com/jaegertracing/jaeger-openshift/master/all-in-one/jaeger-all-in-one-template.yml | oc create -f -
```

### Modification of Fuse components



#### Modification of modules pom.xml

The Maven `pom.xml`project descriptor of each module has to be modified in order to retrieve the correct interface and implementation dependencies. First, you may want to introduce a custom property to track and ease the version management of `Jaeger` like in the snippet below:

```
<properties>
  ...
  <jaeger.version>0.19.0</jaeger.version>
</properties>
```

Then, you need to add 2 dependencies: the `camel-opentracing-starter` that will bring into the project the correct annotation for activation and the Open Tracing interface java library ; the `jaeger-core` will bring Jaeger implementation of Open Tracing depending on requested version.

```
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

```
<dependency>
  <groupId>org.apache.camel</groupId>
  <artifactId>camel-core</artifactId>
  <version>2.19.0</version>
</dependency>
```

#### Adding a Jaeger configuration for Open Tracing

Then you need to add a new configuration class to your Fuse module. This configuration will be responsible for retrieving by some mean the URL of a Jaeger endpoint for sending trace fragments and to create a new tracer that will be reused later to mark each trace with the identifier of this Fuse module.

In the snippet below loaceted into `fis20/modules/api-gateway/src/main/java/com/github/lbroudoux/techlab/ApplicationCamelConfig.java` java class, we'll use an environment variable to get the endpoint URL and we'll mark each trace has coming from `api-gateway` component:

```
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

Final modification, we'll need to instrumentate our Fuse/Camel application with this new `org.apache.camel.opentracing.starter.CamelOpenTracing` annotation. For example, in this `fis20/modules/api-gateway/src/main/java/com/github/lbroudoux/techlab/Application.java` java class, we've added the following class annotation:

```
...
@SpringBootApplication
@CamelOpenTracing
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends RouteBuilder {
  ...
}
```
