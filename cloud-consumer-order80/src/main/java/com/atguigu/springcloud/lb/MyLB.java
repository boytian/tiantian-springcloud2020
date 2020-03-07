package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: TianTian
 * @Date: 2020/3/7 19:55
 */
@Component
public class MyLB implements LoadBalancer {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public final int getAndIncrement(){
        int current;
        int next;
        do{
            current=this.atomicInteger.get();
            /*2147483647:整型最大值*/
            next= current >=2147483647 ? 0:current+1;
        }while (!this.atomicInteger.compareAndSet(current,next));
        System.out.println("******第几次访问next"+next);
        return next;
    }

    //负载均衡算法：第几次请求%服务器总数量=实际访问。服务每次启动从1开始
    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        int index= getAndIncrement() % serviceInstances.size();

        return serviceInstances.get(index);
    }
}
