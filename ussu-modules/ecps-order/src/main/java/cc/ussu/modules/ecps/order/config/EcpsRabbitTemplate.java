package cc.ussu.modules.ecps.order.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EcpsRabbitTemplate {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initEcpsRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                String body = StrUtil.str(message.getBody(), CharsetUtil.UTF_8);
            }
        });
    }

    /**
     * 发送消息
     *
     * @param exchange
     * @param routingKey
     * @param object
     * @param correlationData
     */
    public void convertAndSend(String exchange, String routingKey, Object object, CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(exchange, routingKey, object, correlationData);
    }

    /**
     * 发消息
     *
     * @param exchange
     * @param routingKey
     * @param object
     */
    public void convertAndSend(String exchange, String routingKey, Object object) {
        rabbitTemplate.convertAndSend(exchange, routingKey, object);
    }

}
