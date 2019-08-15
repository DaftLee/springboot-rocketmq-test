package club.dafty.springbootrocketmqtest.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author leechengchao@foxmail.com
 * @version 1.0
 * @date 2019/8/14 17:46
 */
@Component
public class MyConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MyConsumer.class);
    @Value("${apache.rocketmq.consumer.PushConsumer}")
    private String pushConsumerGroup;

    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    public void consumeMessage() throws UnsupportedEncodingException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(pushConsumerGroup);
        //指定NameServer地址，多个地址以 ; 隔开
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);

        logger.error("消费者启动..........");

        try {
            ////订阅MyTopic下Tag为MyTag的消息
            defaultMQPushConsumer.subscribe("MyTopic","MyTag");
            //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
            //如果非第一次启动，那么按照上次消费的位置继续消费
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

            defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

                    try {
                        for (MessageExt messageExt : list){

                            String messagebody = new String(messageExt.getBody(),"utf-8");
                            logger.error("--消费消息："+messagebody+" || 消息id"+messageExt.getMsgId());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            defaultMQPushConsumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
