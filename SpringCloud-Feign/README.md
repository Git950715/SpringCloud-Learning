# SpringCloud-Feign

eureka-server注册中心构建后当然需要查看服务怎么注册到注册中心上面了，事例展示两个服务提供者和一个服务消费者，注册到服务中心后使用消费者Fegin转发调用服务提供者的例子。使用了之前构建好的eureka-server。

Feign是一个声明式的伪Http客户端，它使得写Http客户端变得更简单。使用Feign，只需要创建一个接口并注解。它具有可插拔的注解特性，可使用Feign 注解和JAX-RS注解。Feign支持可插拔的编码器和解码器。Feign默认集成了Ribbon，并和Eureka结合，默认实现了负载均衡的效果。

## eureka-producer1

1.构建一个maven项目，开始修改pom文件，引入jar包
    
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    引入申明注册到eureka服务的包。
  
2.创建SpringBoot启动文件。

    @SpringBootApplication
    @EnableEurekaClient
    public class EurekaProducer1Application {

        public static void main(String[] args) {
            SpringApplication.run( EurekaProducer1Application.class, args );
        }
    }
    @EnableEurekaClient：启用服务注册与发现
    
3.修改配置文件。

    server:
      port: 8763
    eureka:
      instance:
        hostname: localhost
      client:
        serviceUrl:
          defaultZone: http://localhost:8760/eureka/
    spring:
      application:
        name: eurka-producer

4.当然需要对外模拟提供服务还需要写一个接口。

    @RestController
    public class HelloController {
      @RequestMapping("/api/hello")
      public String Controller(String name){
        return "hello:"+name+",this is producer1。";
      }
    }
    熟悉SpringBoot的肯定在熟悉不过了，因为SpringCloud是在SpringBoot基础上的，所以可以这么写接口。

这样服务提供者就配置好了当然为了模拟Fegin自带的负载均衡，还需要建一个一模一样的项目eureka-producer2.

## eureka-producer2
  
  唯一不同于eureka-producer1的就在于
      
      server:
        port: 8764
      修改了端口才能跑起来。
      
## eureka-consumer
  
  1.构建Maven项目,修改pom文件
  
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
      </dependency>
	    <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-feign -->
	    <dependency>
	        <groupId>org.springframework.cloud</groupId>
	        <artifactId>spring-cloud-starter-feign</artifactId>
	        <version>1.4.3.RELEASE</version>
	    </dependency>
      引入服务注册包和feign包
      
  2.创建SpringBoot启动文件。
  
      @SpringBootApplication
      @EnableDiscoveryClient
      @EnableFeignClients
      public class EurekaConsumerApplication {
          public static void main(String[] args) {
              SpringApplication.run( EurekaConsumerApplication.class, args );
          }
      }
      @EnableFeignClients：申明开启Feign。
      
  3.修改配置文件application.properties。
  
      server.port=8765
      eureka.instance.hostname=localhost
      eureka.client.serviceUrl.defaultZone=http://localhost:8760/eureka/
      spring.application.name=eurka-consumer
      简单申明端口、注册服务地址、注册到的注册中心的名称
      
  4.接下来就是使用Feign去调用服务提供者了。
  
      @FeignClient(name="eurka-producer")
      public interface HelloRemote {
	        @RequestMapping(value="/api/hello")
	        public String hello(@RequestParam(value="name")String name);
      }
      定义一个feign接口，通过@FeignClient("服务名")，来指定调用哪个服务。@RequestMapping(value="/api/hello")指定接口方法调用服务的方法。
      
  5.消费者当然也需要对外提供一个测试接口来验证服务是否被提供。
  
      @RestController
      public class ConsumerController {
	        @Autowired
	        private HelloRemote helloRemote;
	        @RequestMapping("/api/consumer/hello")
	        public String Controller(String name){
		          return helloRemote.hello(name);
	        }
      }
      @Autowired申明调用类
      
  测试的时候，分别启动eureka-server、eureka-producer1、eureka-producer2、eureka-consumer。启动成功后访问http://127.0.0.1:8760/ 可以看到服务都注册成功了。
  ![](https://github.com/Git950715/SpringCloudConfig/blob/master/images/eureka-server-feign.png)
  
  注册成功后访问http://127.0.0.1:8765/api/consumer/hello?name=HDC 可以看到返回的是hello:HDC,this is producer1。再次访问返回的是hello:HDC,this is producer2。可见服务提供成功了，也实现了负载均衡，因为Fegin使用的是Ribbon，负载均衡采用的是轮询算法，不断请求，也可以看到这两个结果交替出现。
