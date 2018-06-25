/*
 * Copyright 2016 Red Hat, Inc.
 * <p>
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package com.github.lbroudoux.techlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.opentracing.starter.CamelOpenTracing;
import org.apache.qpid.jms.JmsConnectionFactory;

@SpringBootApplication
@CamelOpenTracing
// load regular Spring XML file from the classpath that contains the Camel XML DSL
@ImportResource({"classpath:spring/amq.xml", "classpath:spring/camel-context.xml"})
public class Application {

    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(name = "amqp-component")
    AMQPComponent amqpComponent(AMQPConfiguration config) {
        JmsConnectionFactory qpid = new JmsConnectionFactory(config.getUsername(), config.getPassword(), "amqp://"+ config.getHost() + ":" + config.getPort());
        qpid.setTopicPrefix("topic://");

        PooledConnectionFactory factory = new PooledConnectionFactory();
        factory.setConnectionFactory(qpid);

        AMQPComponent component = new AMQPComponent(factory);
        component.setMaxConcurrentConsumers(config.getMaxConcurrentConsumers());
        return component;
    }
}