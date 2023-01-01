package cc.ussu.modules.ecps.order.listener;

import cc.ussu.modules.ecps.common.constants.ConstantsEcpsRabbit;
import cc.ussu.modules.ecps.order.entity.EbOrder;
import cc.ussu.modules.ecps.order.service.IEbOrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = ConstantsEcpsRabbit.QUEUE_CLOSE_ORDER)
public class CloseOrderListener {

    @Autowired
    private IEbOrderService orderService;

    @RabbitHandler
    public void closeOrderListener(Message message, EbOrder order, Channel channel) throws IOException {
        // 一旦监听到消息，就表示消息已经进入到该队列中，从死信队列过来的数据。
        // 订单过了时间，不清楚用户是否已经支付成功的。
        if (orderService.closeOrder(order)) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } else {
            // 记录相关的日志，写定时器扫描日志表，补偿相关数据。发送预警的信息。
            // 业务处理失败的，true把消息重新放回到队列中
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

}
