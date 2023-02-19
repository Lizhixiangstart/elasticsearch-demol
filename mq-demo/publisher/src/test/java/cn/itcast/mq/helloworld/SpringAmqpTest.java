package cn.itcast.mq.helloworld;/**
 * @ClassName SpringAmqpTest
 * @Description TODO
 * @Author LiZhixiang
 * @Date 2023/2/18 14:07
 * @Version 1.0
 */

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 @Description:
 @Auth:Lzx
 @Create:2023-022023/2/1814-07
 @Version:1.0
 */
@SpringBootTest
@Slf4j
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue(){
        //队列名称
        String queueName = "simple.queue";
        //消息
        String message = "hello,spring amqp";
        //发送消息
        rabbitTemplate.convertAndSend(queueName,message);
        log.info("消息发送成功");
    }


    @DisplayName("模拟publisher发布多条消息")
    @Test
    public void testWorkQueue() throws InterruptedException {
       for (int i=1;i<=50;i++){
           //队列名称
           String queueName = "simple.queue";
           //消息
           String message = "hello,message__";
           rabbitTemplate.convertAndSend(queueName,message+i);
           Thread.sleep(20);
       }
    }

    @Test
    public void testFanoutExchange(){
        //队列名称
        String exchangeName = "itcast.fanout";
        //消息
        String message = "hello,everyone";
        rabbitTemplate.convertAndSend(exchangeName,"",message);
    }

    @DisplayName("测试DirectExchange")
    @Test
    public void testSendDirectExchange(){
        //交换机名称
        String exchangeName = "itcast.direct";
        //消息
        String message = "红色警报！日本乱排核废水，导致海洋生物变异，惊险哥斯拉！";
        //发送消息
        rabbitTemplate.convertAndSend(exchangeName,"green",message);
    }

    @DisplayName("测试TopicExchange")
    @Test
    public void testTopicExchange(){
        //交换机名称
        String exchangeName = "itcast.topic";
        //消息
        String message = "喜报！孙悟空大战哥斯拉，胜!";
        //发送消息
        rabbitTemplate.convertAndSend(exchangeName,"china.news",message);
    }

    @Test
    public void testSendObjectQueue() {
        // 准备消息
        Map<String,Object> msg = new HashMap<>();
        msg.put("name", "Jack");
        msg.put("age", 21);
        // 发送消息
        rabbitTemplate.convertAndSend("object.queue", msg);
    }
}
