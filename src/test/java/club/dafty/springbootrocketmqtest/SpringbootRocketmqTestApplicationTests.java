package club.dafty.springbootrocketmqtest;

import club.dafty.springbootrocketmqtest.rocketmq.consumer.MyConsumer;
import club.dafty.springbootrocketmqtest.rocketmq.producer.MyProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRocketmqTestApplicationTests {
    @Autowired
    MyConsumer myConsumer;
    @Autowired
    MyProducer myProducer;
    @Test
    public void contextLoads() throws UnsupportedEncodingException {

        myConsumer.consumeMessage();
        myProducer.sendMessage();

    }

}
