# SpringCloud-Hystrix

# Eureka-Hystrix

从上一篇的例子来看，如果produce1或者produce2其中的一个服务挂了，或者两个服务都挂了的话，那么consumer访问不到服务就只能报错了。如果只有produce2一个挂了，那么访问的时候还是报错，等注册中心发现这个注册服务不可用后，将它从可用服务移除后，也就访问不到了，但是在这个过程还是能访问到的，所以还是会有问题。

为了解决服务提供者宕机引起消费者服务也不可用的问题，引入Hystrix（断路器），想了解Hystrix是个啥，或者想了解原理的，可自行Google。

我们还是借用SpringCloud-Feign的例子保留其中的eureka-producer1和eureka-producer2，修改eureka-consumer。

1.eureka-consumer的pom文件增加jar。

    <dependency>
	    <groupId>org.springframework.cloud</groupId>
	    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
	  </dependency>
    
2.配置文件application.properties增加配置。

    feign.hystrix.enabled=true
    因为feign的熔断默认是关闭的，我们这边需要打开。
    
3.增加熔断默认实现类

    @Component
    public class HelloRemoteHystrix implements HelloRemote {
	    @Override
	    public String hello(String name) {
		    return "hello:"+name+",this is default。";
	    }
    }
    熔断就是服务不可用后默认的输出，这边业务处理的话可以直接抛出异常，或者将错误存储起来后面修复数据。
 
4.接口实现类增加fallback=HelloRemoteHystrix.class。

    @FeignClient(name="eurka-producer",fallback=HelloRemoteHystrix.class)
    public interface HelloRemote {
	      @RequestMapping(value="/api/hello")
	      public String hello(@RequestParam(value = "name")String name);
    }
    
 这样就修改完成了，消费者中集成了Hystrix。
 
 分别启动eureka-server、eureka-produce1、eureka-produce2、修改后的eureka-consumer(eureka-hystrix)。启动成功后访问http://127.0.0.1:8766/api/consumer/hello?name=HDC 如果服务都正常的话肯定是hello:HDC,this is producer1。和hello:HDC,this is producer2。交替展示。这个时候我们将produce1服务停掉并且立即去访问consumer的接口，可以看见返回了hello:HDC,this is default。和hello:HDC,this is producer2。我们再将produce2服务停掉，那么请求接口看见只返回了hello:HDC,this is default。证明熔断产生了作用。
