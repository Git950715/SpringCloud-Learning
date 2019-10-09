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
## Eureka-Server1、Eureka-Server2（集群化服务注册中心)
   为了实现服务的高可用，注册中心也必须使用集群化部署，注册中心挂掉一台机后也能继续提供服务注册服务。
   步骤1和2都和上面相同，唯一不同点在于配置。
   
   3.eureka-server1的配置：
            
        server.port=8761
        eureka.instance.hostname=peer1
        eureka.client.serviceUrl.defaultZone=http://peer2:8762/eureka/
        spring.application.name=eurka-server
        区别点在于配置defaultZone时注册到同级服务上
      
   eureka-server2的配置：
   
        server.port=8762
        eureka.instance.hostname=peer2
        eureka.client.serviceUrl.defaultZone=http://peer1:8761/eureka/
        spring.application.name=eurka-server
        同样的defaultZone注册到另一台服务上
        
   同样的如果你有更多台服务的时候，只需要在defaultZone后面再用逗号隔开就好了
   
        eureka.instance.hostname=peer1
        eureka.client.serviceUrl.defaultZone=http://peer2:8762/eureka/,http://peer3:8763/eureka/
        
        eureka.instance.hostname=peer2
        eureka.client.serviceUrl.defaultZone=http://peer1:8761/eureka/,http://peer3:8763/eureka/
        ......
   
   当然你在启动服务的时候还需要将peer1，和peer2配置到路由中，不然解析不了peer1和peer2。linux系统通过vim /etc/hosts ，windows电脑，在c:/windows/systems/drivers/etc/hosts。
        
        127.0.0.1 peer1
        127.0.0.1 peer2
   
   其实你也可以只建一个项目，使用不同的配置文件就可以了，这样启动项目的时候指定不同的配置文件也是可以的，但是本人在windows上没有打包，方便测试就建了两个项目。
   
   分别启动项目之后分别访问，http://localhost:8761/ 和 http://localhost:8762/ 你就可以看到
   ![](https://github.com/Git950715/SpringCloudConfig/blob/master/images/eureka-server1.png)
   ![](https://github.com/Git950715/SpringCloudConfig/blob/master/images/eureka-server2.png)
   服务分别注册到了互相的服务上，如果你启动一个客户端注册到服务中心的时候，你分别刷新地址，可以看到另一台服务也注册了相同的客户端。
   
   集群配置完成后引用的时候还是正常引用：eureka.client.serviceUrl.defaultZone=http://peer1:8761/eureka/, 即使你停掉了server1, server2还是依旧能提供服务。
