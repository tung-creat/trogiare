package com.trogiare.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabitmq.exchange.name}")
    private String EXCHANGE_NAME;
    @Value("${rabitmq.queue.name}")
    private String QUEUE_NAME;
    @Value("${rabitmq.routing.key}")
    private String ROUTING_KEY;


    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }
//    @Bean
//    public Queue queueJson(){
//        return new Queue(QUEUE_JSON_NAME);
//    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(directExchange())
                .with(ROUTING_KEY);
    }
//    @Bean
//    public Binding jsonBinding(){
//        return BindingBuilder
//                .bind(queueJson())
//                .to(topicExchange())
//                .with(ROUNTING_JSON_KEY);
//    }
////    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//    @Bean
//    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(messageConverter());
//        return rabbitTemplate;
//    }
}
