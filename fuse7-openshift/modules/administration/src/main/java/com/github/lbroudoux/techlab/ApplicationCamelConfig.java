package com.github.lbroudoux.techlab;

import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jaegertracing.metrics.Metrics;
import io.jaegertracing.metrics.InMemoryMetricsFactory;
import io.jaegertracing.reporters.RemoteReporter;
import io.jaegertracing.samplers.ProbabilisticSampler;
import io.jaegertracing.senders.Sender;
import io.jaegertracing.senders.UdpSender;

import io.opentracing.noop.NoopTracerFactory;
import io.opentracing.Tracer;

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
            
            RemoteReporter reporter = new RemoteReporter.Builder()
            		.withSender(sender)
            		.withFlushInterval(100)
            		.withMaxQueueSize(50)
            		.withMetrics(new Metrics(new InMemoryMetricsFactory()))
            		.build();
            
            return new io.jaegertracing.Tracer.Builder("administration")
            		.withReporter(reporter)
            		.withSampler(new ProbabilisticSampler(1.0))
            		.build();
            		
            /*
            return new com.uber.jaeger.Tracer.Builder("order-service",
                    new RemoteReporter(sender, 100, 50,
                            new Metrics(new StatsFactoryImpl(new NullStatsReporter()))),
                    new ProbabilisticSampler(1.0))
                    .build();
             */
        }
        log.info("Using Noop tracer");
        return NoopTracerFactory.create();
    }
}
