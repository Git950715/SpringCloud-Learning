# SpringCloud-Bus
上一章说到客户端可以通过配置中心获取到配置，如果配置直接在Git上面修改了呢，我们测试发现配置中心已经可以获取到修改后的配置，但是客户端还是没有获取到配置信息。所以就引入了Bus。Spring Cloud Bus 将分布式的节点用轻量的消息代理连接起来。它可以用于广播配置文件的更改或者服务之间的通讯，也可以用于监控。本文要讲述的是用Spring Cloud Bus实现通知微服务架构的配置文件的更改。因为需要用到RabbitMQ，这个需要自行下载安装。

## eureka-config-client-bus
  1.我们获取到上一章的eureka-config-client代码，对其进行修改。首先是pom引入bus包。
      
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-bus-amqp</artifactId>
    </dependency>
  
  2.修改配置文件application.properties，增加RabbitMQ配置。
    
    ## rabmitMQ地址 端口 账号 密码
    spring.rabbitmq.host=localhost
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=guest
    spring.rabbitmq.password=guest
    ## 开启bus
    spring.cloud.bus.enabled=true 
    ## 开启消息跟踪
    spring.cloud.bus.trace.enabled=true
    ## 开启bus-refresh
    management.endpoints.web.exposure.include=bus-refresh
    
    增加完配置后修改server.port=8773
  
## eureka-config-client-bus2
  按照上面的配置再修改一个，只需要改变一下端口号server.port=8774
  
全部修改完成之后，依次启动eureka-server，eureka-config-server，eureka-config-client-bus，eureka-config-client-bus2

修改配置后查看http://127.0.0.1:8770/eurka-config-client-dev.properties 发现返回的信息已经修改了。

但是依次访问http://127.0.0.1:8773/api/config/test 和 http://127.0.0.1:8774/api/config/test 配置还是以前的。

现在Bus需要上场来起作用了，用POST的方式访问http://localhost:8773/actuator/bus-refresh 发现  eureka-config-client-bus和eureka-config-client-bus2两个项目都去重新拉取了配置，这时再访问http://127.0.0.1:8773/api/config/test 和 http://127.0.0.1:8774/api/config/test 配置已经被修改了。

## 写在最后，遗留的看法与问题。
  1.执行了http://localhost:8773/actuator/bus-refresh 观看eureka-config-client-bus的控制台日志输出，发现Started application in 7.918 seconds (JVM running for 1890.912)有这个日志打出，这是重启的，如果他是重启的话，我觉得它就有待改进的空间，因为重启的话，我直接每个服务重启下不就配置重新读取了吗？但是我试了下，在这个过程中服务还是可以访问的，所以这可能是热加载吧，具体原因还有待研究。
  
  2.另外，/actuator/bus-refresh接口可以指定服务，即使用”destination”参数，比如 "/actuator/bus-refresh?destination=customers:**" 即刷新服务名为customers的所有服务。这个指定服务刷新我验证是是没有的,两个服务还是一起刷新了，可能是我验证的方式有问题，http://127.0.0.1:8773/actuator/bus-refresh?destination=customers:8773 我执行的是这个。
  
  3.还有一个就是Bus本身存在的问题了，使用bus-refresh后的服务，在注册中心都失效了，虽然注册中心还是能够显示，但是点击就会发现，地址404，也就说明没有注册上去，我起初是觉得版本有问题，我就升级了版本，直接升到最新版本Finchley.SR4，发现还是这样。但是说是这个版本Dalston.SR1解决了，但是Finchley版本还是没有。
  
