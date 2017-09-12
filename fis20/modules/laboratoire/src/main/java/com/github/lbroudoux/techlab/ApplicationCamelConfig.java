package com.github.lbroudoux.techlab;

import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.uber.jaeger.metrics.Metrics;
import com.uber.jaeger.metrics.NullStatsReporter;
import com.uber.jaeger.metrics.StatsFactoryImpl;
import com.uber.jaeger.reporters.RemoteReporter;
import com.uber.jaeger.samplers.ProbabilisticSampler;
import com.uber.jaeger.senders.Sender;
import com.uber.jaeger.senders.UdpSender;

import io.opentracing.NoopTracerFactory;
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
            return new com.uber.jaeger.Tracer.Builder("laboratoire",
                    new RemoteReporter(sender, 100, 50,
                            new Metrics(new StatsFactoryImpl(new NullStatsReporter()))),
                    new ProbabilisticSampler(1.0))
                    .build();
        }
        log.info("Using Noop tracer");
        return NoopTracerFactory.create();
    }
}
