package cn.itcast.mq.config;/**
 * @ClassName FanoutConfig
 * @Description TODO
 * @Author LiZhixiang
 * @Date 2023/2/18 16:52
 * @Version 1.0
 */

import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 @Description:声明交换机和队列
 @Auth:Lzx
 @Create:2023-022023/2/1816-52
 @Version:1.0
 */
@Configuration
public class FanoutConfig {
    /**
     * 声明交换机
     * @return Fanout类型交换机
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return  new FanoutExchange("itcast.fanout");
    }

    /**
     * 第一个队列
     * @return
     */
    @Bean
    public Queue fanoutQueue1(){
        return new Queue("fanout.queue1");
    }

    /**
     * 绑定交换机和队列1
     * @param fanoutExchange
     * @param fanoutQueue1
     * @return
     */
    @Bean
    public Binding bindingQueue1(FanoutExchange fanoutExchange,Queue fanoutQueue1){
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    /**
     * 声明第二个队列
     * @return
     */
    @Bean
    public Queue fanoutQueue2(){
        return new Queue("fanout.queue2");
    }

    /**
     * 绑定交换机和第队列2
     * @param fanoutExchange
     * @param fanoutQueue2
     * @return
     */
    @Bean
    public Binding bindingQueue2(FanoutExchange fanoutExchange,Queue fanoutQueue2){
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}

