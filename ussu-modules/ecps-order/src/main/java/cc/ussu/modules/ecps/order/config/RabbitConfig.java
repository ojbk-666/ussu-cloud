package cc.ussu.modules.ecps.order.config;

import cc.ussu.modules.ecps.common.constants.ConstantsEcpsRabbit;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbit配置及初始化
 */
@Configuration
public class RabbitConfig {

    /**
     * 消息转换模板
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 订单支付状态的路由
     */
    @Bean
    public Exchange createOrderStatusExchange() {
        return ExchangeBuilder.directExchange(ConstantsEcpsRabbit.EXCHANGE_ORDER_DEAD).durable(true).build();
    }

    /**
     * 订单支付状态得死信队列
     */
    @Bean
    public Queue createOrderStatusQueue() {
        return QueueBuilder.durable(ConstantsEcpsRabbit.QUEUE_ORDER_DEAD)
                .deadLetterExchange(ConstantsEcpsRabbit.EXCHANGE_CLOSE_ORDER)
                .deadLetterRoutingKey(ConstantsEcpsRabbit.ROUTING_KEY_CLOSE_ORDER)
                .ttl(1000 * 10)
                .build();
    }

    /**
     * 订单支付状态 绑定
     */
    @Bean
    public Binding createOrderStatusBinding() {
        Binding binding = new Binding(ConstantsEcpsRabbit.QUEUE_ORDER_DEAD, Binding.DestinationType.QUEUE,
                ConstantsEcpsRabbit.EXCHANGE_ORDER_DEAD, ConstantsEcpsRabbit.ROUTING_KEY_ORDER_DEAD, null);
        return binding;
    }

    /**
     * 关单路由
     */
    @Bean
    public Exchange createCloseOrderExchange() {
        return ExchangeBuilder.directExchange(ConstantsEcpsRabbit.EXCHANGE_CLOSE_ORDER)
                .durable(true)
                .build();
    }

    /**
     * 关单队列
     */
    @Bean
    public Queue createCloseOrderQueue() {
        return QueueBuilder.durable(ConstantsEcpsRabbit.QUEUE_CLOSE_ORDER).build();
    }

    /**
     * 关单绑定
     */
    @Bean
    public Binding createCloseOrderBinding() {
        Binding binding = new Binding(ConstantsEcpsRabbit.QUEUE_CLOSE_ORDER, Binding.DestinationType.QUEUE,
                ConstantsEcpsRabbit.EXCHANGE_CLOSE_ORDER, ConstantsEcpsRabbit.ROUTING_KEY_CLOSE_ORDER, null);
        return binding;
    }

}
