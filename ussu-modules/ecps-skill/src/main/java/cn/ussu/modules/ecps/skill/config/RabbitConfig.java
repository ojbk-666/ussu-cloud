package cn.ussu.modules.ecps.skill.config;

import cn.ussu.modules.ecps.common.constants.ConstantsEcpsRabbit;
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
     * 秒杀订单的交换机
     */
    @Bean
    public Exchange createSkillOrderExchange() {
        return ExchangeBuilder.directExchange(ConstantsEcpsRabbit.EXCHANGE_SKILL_ORDER).durable(true).build();
    }

    /**
     * 秒杀订单队列
     */
    @Bean
    public Queue createSkillOrderQueue() {
        return QueueBuilder.durable(ConstantsEcpsRabbit.QUEUE_SKILL_ORDER).build();
    }

    /**
     * 秒杀订单绑定
     */
    @Bean
    public Binding createSkillOrderBinding() {
        Binding binding = new Binding(ConstantsEcpsRabbit.QUEUE_SKILL_ORDER, Binding.DestinationType.QUEUE,
                ConstantsEcpsRabbit.EXCHANGE_SKILL_ORDER, ConstantsEcpsRabbit.ROUTING_KEY_SKILL_ORDER, null);
        return binding;
    }

}
