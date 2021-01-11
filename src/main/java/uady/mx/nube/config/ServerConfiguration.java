package uady.mx.nube.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfiguration {

  @Value("${sample.rabbitmq.exchange}")
  String exchange;
  @Value("${sample.rabbitmq.routingkey}")
  String routingkey;
  @Value("${sample.rabbitmq.queue}")
  String queueName;
  
  @Bean
  TopicExchange exchange() {
    return new TopicExchange(exchange);
  }

  @Bean
  Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(routingkey);
  }

  @Bean
  Queue queue() {
    return new Queue(queueName, false);
  }

}
