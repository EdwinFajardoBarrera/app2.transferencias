package uady.mx.nube.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.ConnectionFactory;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
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

  private String incomingQueue = "DLQ_queue";
  private String dlqEx = "dead.letter.test";

  
  @Bean
  TopicExchange exchange() {
    return new TopicExchange(exchange);
  }

  @Bean
  DirectExchange dlqExhange(){
    return new DirectExchange(dlqEx);  
  }

  @Bean
  Binding bindingQueue(TopicExchange exchange) {
    return BindingBuilder.bind(queue()).to(exchange).with(routingkey);
  }

  @Bean
  Binding bindingDLQ(DirectExchange exchange) {
    return BindingBuilder.bind(dlqQueue()).to(exchange).with(incomingQueue);
  }
 

  @Bean
  Queue queue() {
    Map<String, Object> args = new HashMap<String, Object>();
    // The default exchange
    args.put("x-dead-letter-exchange", dlqEx);
    // Route to the incoming queue when the TTL occurs
    args.put("x-dead-letter-routing-key", incomingQueue);
    // TTL 5 seconds
    args.put("x-message-ttl", 5000);
    return new Queue(queueName, false, false , false, args);
  }
  
  @Bean
  Queue dlqQueue(){
    return new Queue(incomingQueue);
  }
}
