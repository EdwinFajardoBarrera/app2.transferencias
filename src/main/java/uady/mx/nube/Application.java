package uady.mx.nube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import org.springframework.context.annotation.Bean;
// import org.springframework.amqp.core.Binding;
// import org.springframework.amqp.core.BindingBuilder;
// import org.springframework.amqp.core.Queue;
// import org.springframework.amqp.core.TopicExchange;
// import org.springframework.amqp.rabbit.connection.ConnectionFactory;
// import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
// import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
// import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class Application {

  // @Value("${sample.rabbitmq.exchange}")
  // String topicExchangeName;
  // @Value("${sample.rabbitmq.routingkey}")
  // String routingkey;
  // @Value("${sample.rabbitmq.queue}")
  // String queueName;

  // // @Bean
  // // public MessageConverter jsonMessageConverter() {
  // // return new Jackson2JsonMessageConverter();
  // // }

  // // @Bean
  // // public SimpleRabbitListenerContainerFactory jsaFactory(ConnectionFactory
  // // connectionFactory,
  // // SimpleRabbitListenerContainerFactoryConfigurer configurer) {
  // // SimpleRabbitListenerContainerFactory factory = new
  // // SimpleRabbitListenerContainerFactory();
  // // configurer.configure(factory, connectionFactory);
  // // factory.setMessageConverter(jsonMessageConverter());
  // // return factory;
  // // }

  // @Bean
  // Queue queue() {
  // return new Queue(queueName, false);
  // }

  // @Bean
  // TopicExchange exchange() {
  // return new TopicExchange(topicExchangeName);
  // }

  // @Bean
  // Binding binding(Queue queue, TopicExchange exchange) {
  // return BindingBuilder.bind(queue).to(exchange).with(routingkey);
  // }

  // @Bean
  // SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
  // MessageListenerAdapter listenerAdapter) {
  // SimpleMessageListenerContainer container = new
  // SimpleMessageListenerContainer();
  // container.setConnectionFactory(connectionFactory);
  // container.setQueueNames(queueName);
  // container.setMessageListener(listenerAdapter);
  // return container;
  // }

  // @Bean
  // MessageListenerAdapter listenerAdapter(Receiver receiver) {
  // return new MessageListenerAdapter(receiver, "receiveMessage");
  // }

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(Application.class, args);
    System.out.println("Servicio iniciado correctamente");
  }

}