package club.dafty.springbootrocketmqtest.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;

/**
 * @author leechengchao@foxmail.com
 * @version 1.0
 * @date 2019/8/14 17:31
 */
public class MyProducer {
    @Value("${apache.rocketmq.producer.producerGroup}")
    private String producerGroup;

    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    public void sendMessage() throws UnsupportedEncodingException {
        DefaultMQProducer mqProducer = new DefaultMQProducer(producerGroup);
        mqProducer.setNamesrvAddr(namesrvAddr);

        try {
            mqProducer.start();
            for (int i = 0; i < 100; i++) {
                String str = "我是生产消息"+i;
                String string = new String(str.getBytes(),"utf-8");
                Message message = new Message("MyTopic","MyTag","Mykey_",string.getBytes());
                SendResult sendResult = mqProducer.send(message);
                System.out.println("++发送消息："+str+" || 消息id："+sendResult.getMsgId()+" || "+"消息结果:"+sendResult.getSendStatus());
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } finally {
            mqProducer.shutdown();
        }

    }
}
