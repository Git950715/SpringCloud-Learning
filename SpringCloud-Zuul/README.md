# SpringCloud-Zuul
## eureka-zuul

在分布式系统中必定要有一个网关，有了网关就可以做好多好多的事，限流、负载均衡、权限验证等等等。。。。当然在SpringCloud中也有一个模块组件Zuul。Zuul的主要功能是路由转发和过滤器，下面就开始介绍是怎么实现的。

该事例需要用到之前项目中的eureka-server、eureka-producer1、eureka-producer2。

1.最初的一步都是要搭建一个maven项目，然后引入jar。
    
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
    </dependency>
    注册中心服务肯定是要引入的，再者就是引入zuul的jar。
    
2.修改启动类，开始zuul。

    @SpringBootApplication
    @EnableZuulProxy
    @EnableEurekaClient
    @EnableDiscoveryClient
    public class EurekaZuulApplication {
        public static void main(String[] args) {
            SpringApplication.run( EurekaZuulApplication.class, args );
        }
    }
    @EnableZuulProxy：开启zuul。
    
3.下面就是修改配置文件application.properties了。
    
    server.port=8775
    spring.application.name=eurka-zuul
    eureka.instance.hostname=localhost
    eureka.client.serviceUrl.defaultZone=http://localhost:8760/eureka/
    #zuul.routes.api.path=/api/**
    #zuul.routes.api.url=http://127.0.0.1:8763/api
    zuul.routes.api-a.path=/api-a/**
    zuul.routes.api-a.serviceId=eurka-producer
    
    第一种代理方式是指定代理到的地址是什么就是#注释掉的代码
    第二种代理方式是通过eureka注册中心使用zuul默认集成的ribbon实现代理与负载均衡。
    
4.其实这个时候就已经实现了转发，但是还有一个功能就是过滤。

    @Component
    public class MyFilter extends ZuulFilter{
      private static Logger log = LoggerFactory.getLogger(MyFilter.class);
      /**
       * 过滤逻辑编写，这里验证是否有token传入
       */
      @Override
      public Object run() throws ZuulException {
        log.info("MyFilter进来了！！！！！");
        RequestContext ctx = RequestContext.getCurrentContext();
            HttpServletRequest request = ctx.getRequest();
            log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
            Object accessToken = request.getParameter("token");
            if(accessToken == null) {
                log.error("token is empty");
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(401);
                try {
                    ctx.getResponse().getWriter().write("token is empty");
                }catch (Exception e){}
                return null;
            }
            log.info("ok");
        return null;
      }
      /**
       * 这里可以写逻辑判断，是否要过滤，本文true,永远过滤。
       */
      @Override
      public boolean shouldFilter() {
        return true;
      }
      /**
       * 过滤的顺序,值越小越先
       */
      @Override
      public int filterOrder() {
        return 0;
      }
      /**
       * 返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
       *	pre：路由之前
         *	routing：路由之时
       *	post： 路由之后
       *	error：发送错误调用
       */
      @Override
      public String filterType() {
        return "pre";
      }
    }
    @Component：将类注入到Spring容器中。
    
到这里就已经集成成功了，分别启动eureka-server、eureka-produce1、eureka-produce2、eureka-zuul

第一种方式不推荐，实际项目中用的很少，但是这样配置的话你就需要启动eureka-server、eureka-produce1、eureka-zuul。这样你请求http://localhost:8775/api/hello?name=hdc&token=123 可以看到回复的就是：hello:hdc,this is producer1。

采用第二种配置方式推荐，更符合实际项目需求，请求http://localhost:8775/api-a/api/hello?name=hdc&token=123 可以看到hello:hdc,this is producer1。和hello:hdc,this is producer2。交替出现，说明实现了转发与负载均衡。

再看我们之前实现的过滤器：当请求http://localhost:8775/api-a/api/hello?name=hdc 可以看到返回token is empty，可见过滤器也生效了。
