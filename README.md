# SpringCloud-Learning
个人学习SpringCloud过程笔记，也是第一次写这样的，如有错误的地方还请各位指导纠正，欢迎给我再Issues提问题。<br/>

本项目是借鉴两位大神的项目分别为[大神1](https://www.fangzhipeng.com/spring-cloud.html)和[大神2](http://www.ityouknow.com/spring-cloud.html)，自己整合了一下并且踩了一下里面的坑。

## 1.初始项目是建立一个所有SpringCloud模块都能引用的父级包 [SpringCloud-Parent](https://github.com/Git950715/SpringCloud-Learning/tree/master/SpringCloud-Parent)

## 2.服务注册中心 [SpringCloud-Eureka](https://github.com/Git950715/SpringCloud-Learning/tree/master/SpringCloud-Eureka)

## 3.注册服务中心，并使用Feign调用服务 [SpringCloud-Feign](https://github.com/Git950715/SpringCloud-Learning/tree/master/SpringCloud-Feign)

## 4.服务提供者宕机，消费者熔断保证服务可用（Hystrix） [SpringCloud-Hystrix](https://github.com/Git950715/SpringCloud-Learning/tree/master/SpringCloud-Hystrix)

## 5.网关实现路由转发和过滤器（Zuul） [SpringCloud-Zuul](https://github.com/Git950715/SpringCloud-Learning/tree/master/SpringCloud-Zuul)

## 6.文件配置中心，配置文件统一管理（Config） [SpringCloud-Config](https://github.com/Git950715/SpringCloud-Learning/tree/master/SpringCloud-Config)

## 7.配置文件修改，客户端统一更新配置（Bus）[SpringCloud-Bus](https://github.com/Git950715/SpringCloud-Learning/tree/master/SpringCloud-Bus)

## 8.SpringCloud官方推出的取代Zuul的新网关SpringCloud Gateway篇幅过长,这边就不一一叙述了，查看上面两位大神的专栏就可以看懂了。

## 9.写在最后，这是入门SpringCloud最初级的版本，里面的示例也是最简单的引用，想要在项目实践中引用明显是不够的，所以革命尚未成功，同志仍需努力。示例里面任然遗留了一些问题，所以等我解决完了再一一解决。更好的项目实践经历等公司用到，或者自己决定写一个小项目的时候在来赘述吧。
