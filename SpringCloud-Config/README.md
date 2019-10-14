# SpringCloud-Config
在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。在Spring Cloud中，有分布式配置中心组件spring cloud config ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程Git仓库中。在spring cloud config 组件中，分两个角色，一是config server，二是config client。

## eureka-config-server
  本示例只展示放在Git上面。

  1.构建maven项目，修改pom文件。
  
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
      </dependency>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
      </dependency>
      引入config-server包
      
  2.修改SpringBoot启动类，增加@EnableConfigServer注解，开启ConfigServer
  
      @SpringBootApplication
      @EnableDiscoveryClient
      @EnableConfigServer
      public class EurekaConfigServerApplication {
          public static void main(String[] args) {
              SpringApplication.run(EurekaConfigServerApplication.class, args);
          }
      }
      
  3.当然不可缺少的是修改配置文件application.properties，引入Git配置。
  
      server.port=8770
      spring.application.name=eurka-config-server
      eureka.instance.hostname=localhost
      eureka.client.serviceUrl.defaultZone=http://localhost:8760/eureka/
      spring.cloud.config.server.git.uri=https://github.com/Git950715/SpringCloudConfig.git
      spring.cloud.config.server.git.searchPaths=config-server
      spring.cloud.config.server.git.skip-ssl-validation=true
      spring.cloud.config.label=master
      spring.cloud.config.server.git.username=你的Git账号
      spring.cloud.config.server.git.password=你的Git密码
      
      spring.cloud.config.server.git.uri：配置git仓库地址
      spring.cloud.config.server.git.searchPaths：配置仓库路径
      spring.cloud.config.server.git.skip-ssl-validation：跳过SSL的验证
      spring.cloud.config.label：配置仓库的分支
      spring.cloud.config.server.git.username：访问git仓库的用户名
      spring.cloud.config.server.git.password：访问git仓库的用户密码
  
      说下趟过的坑，仿照的示例里面Git地址是没有.git的，跳过SSL的验证也是没有的。
      按照示例父级版本是Finchley.RELEASE我起初是可以的，但是我换了一下版本Finchley.SR4之后就不行了，
      然后我开始觉得是版本的问题，我又换回了Finchley.RELEASE，发现又不可以了，我就很奇怪，
      最后搜索看看原因外加看看源码，发现在地址后面加上.git和skip-ssl-validation配置就可以了。
      
  这边配置就ok了，下面可以访问一下http://127.0.0.1:8770/master/eurka-config-client-dev.properties 发现配置文件的内容都输出了。
  
      访问链接说明
      仓库中的配置文件会被转换成web接口，访问可以参照以下的规则：
      /{application}/{profile}[/{label}]
      /{application}-{profile}.yml
      /{label}/{application}-{profile}.yml
      /{application}-{profile}.properties
      /{label}/{application}-{profile}.properties
      
## eureka-config-client
  配置中心有了，怎么访问到那些配置呢，客户端的代码来了！
  
  1.构建maven项目，引入客户端jar。
  
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
      </dependency>
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-config-client</artifactId>
      </dependency>
      
  2.正常配置启动类。
  
      @SpringBootApplication
      @EnableDiscoveryClient
      public class EurekaConfigClientApplication {
          public static void main(String[] args) {
              SpringApplication.run(EurekaConfigClientApplication.class, args);
          }

      }
      
  3.下面是重点：建立一个bootstrap.properties！！！！！！。
  
      eureka.instance.hostname=localhost
      eureka.client.serviceUrl.defaultZone=http://localhost:8760/eureka/
      spring.cloud.config.label=master
      spring.cloud.config.name=eurka-config-client
      spring.cloud.config.profile=dev
      spring.cloud.config.uri=http://localhost:8770/
      配置config的地址和你想要访问的文件。
      
  applicatio.properties:
      
      server.port=8772
      spring.application.name=eurka-config-client
      
  bootstrap.properties文件是要在applicatio.properties之前被加载。
  
  4.构建controller请求输出访问配置。
  
      @RestController
      public class TestController {
        @Value("${name}")
        private String name;
        @RequestMapping(value="/api/config/test")
        public String backConfig(){
          return name;
        }
      }
      
  访问http://127.0.0.1:8772/api/config/test 可以正常输出配置在Git的文件配置。
  
## 高可用的配置服务中心

  看到这里，你们可能会想到，配置中心就一台挂了怎么办，所以我们采用集群的方式保证高可用性。在SpringCloud中，集群的方式都可以注册到eureka中去，让eureka帮我们去分发请求。
  
  ### eureka-config-server2
  1.按照ureka-config-server配置，只需要改动一下application.properties配置文件中的端口server.port=8771
  
  ### eureka-config-client
  我们需要修改一下上面eureka-config-client的bootstrap.properties配置文件。
  
      eureka.instance.hostname=localhost
      eureka.client.serviceUrl.defaultZone=http://localhost:8760/eureka/
      spring.cloud.config.label=master
      spring.cloud.config.name=eurka-config-client
      spring.cloud.config.profile=dev
      #spring.cloud.config.uri=http://localhost:8770/
      spring.cloud.config.discovery.enabled=true
      spring.cloud.config.discovery.serviceId=eurka-config-server
      
  修改完成后，启动项目访问http://127.0.0.1:8772/api/config/test 发现可以输出信息，就算停掉一个config-server也是可以的。
