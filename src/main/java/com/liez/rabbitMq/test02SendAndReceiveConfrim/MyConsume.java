package com.liez.rabbitMq.test02SendAndReceiveConfrim;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * liez
 *
 * @date 2022/3/11
 * 自定义的消费者
 */
@Slf4j
public class MyConsume extends DefaultConsumer {

    private Channel channel;
    public MyConsume(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        log.info("================consume message==================");
        log.info("consumerTag is === :{}", consumerTag);
        log.info("envelope is === :{}", envelope);
        log.info("properties is === :{}", properties);
        log.info("body is === :{}", new String(body));
        //确认消息的方法，回调成功以后再执行下一条，表示这条消息我已经处理完了，你可以给我下一条了。false表示不批量签收
        channel.basicAck(envelope.getDeliveryTag(), false);
        log.info("================consume message success==================");

    }
}
