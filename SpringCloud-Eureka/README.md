# SpringCloud-Eureka
## Eureka-Server（服务注册中心）
SpringCloud核心之一，用于服务注册与发现，详细介绍可自行Google。
1.首先需要创建一个maven项目，构建完成后我们需要引入jar包。
    
    <parent>
  	  <groupId>springcloud.parent</groupId>
      <artifactId>sc-p</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </parent>
    需要引入父级jar包，其中配置分别对应父级pom中的配置。
    
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    再者需要引入eureka-server包，这个不需要设置version，因为父级包中已经定义了版本。
2.jar包引入就完成了，用过SpringBoot的小伙伴就知道了需要启动项目需要一个启动类。
    
    @SpringBootApplication
    @EnableEurekaServer
    public class EurekaServerApplication {
        public static void main(String[] args) {
            SpringApplication.run( EurekaServerApplication.class, args );
        }
     }
     @SpringBootApplication：启动SpringBoot项目的基础注解。
     @EnableEurekaServer：开启服务中心注解。
3.启动类配置完成后就差配置文件application.yml了。

    server:
      port: 8760
    eureka:
      instance:
        hostname: localhost
      client:
        registerWithEureka: false
        fetchRegistry: false
        serviceUrl:
        defaultZone: http://localhost:8760/eureka/
    spring:
      application:
        name: eurka-server
    server.port:项目启动端口。
    eureka.instance.hostname:
    eureka.client.registerWithEureka:表示是否将自己注册到Eureka Server，默认为true。
    eureka.client.fetchRegistry:表示是否从Eureka Server获取注册信息，默认为true。
    eureka.client.serviceUrl.defaultZone:设置与Eureka Server交互的地址，查询服务和注册服务都需要依赖这个地址。默认是                                                                http://localhost:8760/eureka ；多个地址可使用 , 分隔。
    spring.application.name:注册之后展示在注册中心的名称，可用这个名称访问到注册的服务（在服务端注册的服务必须配置！！！） 
   这样单机版本的服务注册中心就完成啦，启动项目，调用http://localhost:8760 就可以看到服务啦。
   ![](https://github.com/Git950715/SpringCloudConfig/blob/master/images/eureka-server.png)
