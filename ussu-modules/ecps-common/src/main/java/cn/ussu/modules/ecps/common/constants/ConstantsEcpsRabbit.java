package cn.ussu.modules.ecps.common.constants;

public interface ConstantsEcpsRabbit {

    String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    String X_MESSAGE_TTL = "x-message-ttl";

    String EXCHANGE_ORDER_DEAD = "ecps-order-exchange";
    String QUEUE_ORDER_DEAD = "ecps-order-queue";
    String ROUTING_KEY_ORDER_DEAD = "ecps-order-routing-key";

    String EXCHANGE_CLOSE_ORDER = "ecps-close-order-exchange";
    String QUEUE_CLOSE_ORDER = "ecps-close-order-queue";
    String ROUTING_KEY_CLOSE_ORDER = "ecps-close-order-routing-key";

}
