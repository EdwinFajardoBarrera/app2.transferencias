package uady.mx.nube;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import uady.mx.nube.service.Receiver;

@SpringBootApplication
@EnableAsync
public class Application {

  @Value("${sample.rabbitmq.exchange}")
  String topicExchangeName;
  @Value("${sample.rabbitmq.routingkey}")
  String routingkey;
  @Value("${sample.rabbitmq.queue}")
  String queueName;

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(Receiver receiver) {
    MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "receiveMessage");
    // messageListenerAdapter.setMessageConverter(jsonMessageConverter());
    return messageListenerAdapter;
  }
  // @Bean
  // public MessageConverter jsonMessageConverter() {
  // return new Jackson2JsonMessageConverter();
  // }

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(Application.class, args);
    System.out.println("Servicio iniciado correctamente");
  }

}