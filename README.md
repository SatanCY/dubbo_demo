### Dubbo学习笔记

1. 软件架构的演变：
   * 单体架构：全部功能都集中在一个项目内
     * 优点：架构简单，开发成本低、周期短，适合小型项目
     * 缺点：不适合大型项目开发，技术栈受限，只能使用一种语言开发，不易扩展，想要扩展只能通过扩展集群点，成本高
   * 垂直架构：按照业务进行切割，形成小的单体项目
     * 优点：技术栈可扩展
     * 缺点：功能集中在一个项目中，不利于开发、扩展、维护，系统扩张只能通过集群的方式，项目之间功能冗余、数据冗余、耦合性强
   * SOA架构：面向服务的架构，根据需求通过网络对松散耦合的粗粒度应用组件(服务)进行分布式部署、组合和使用。一个服务通常以独立的形式存在于操作系统进程中。
     * 优点：重复功能或模块抽取为服务，提高开发效率。可重用性高。可维护性高。
     * 缺点：各系统之间业务不同，很难确认功能或模块是重复的。抽取服务的粒度大。系统和服务之间耦合度高。
   * 微服务架构：

### Dubbo

是一款高性能的Java RPC框架。

#### RPC

全称为remote procedure call，即**远程过程调用**。多个服务器通过网络来表达调用的语义和传达调用的数据。

#### 三大核心能力

1. 面向接口的远程调用
2. 智能容错和**负载均衡**
3. 服务自动注册和发现

| 节点      | 角色名称                               |
| --------- | -------------------------------------- |
| Provider  | 暴露服务的服务提供方                   |
| Consumer  | 调用远程服务的服务消费方               |
| Registry  | 服务注册与发现的注册中心               |
| Monitor   | 统计服务的调用次数和调用时间的监控中心 |
| Container | 服务运行容器                           |

### Zookeeper

是一个树型的目录服务，支持变更推送，适合作为Dubbo 服务的注册中心

流程说明：

- 服务提供者(Provider)启动时: 向 `/dubbo/com.foo.BarService/providers` 目录下写入自己的 URL 地址
- 服务消费者(Consumer)启动时: 订阅 `/dubbo/com.foo.BarService/providers` 目录下的提供者 URL 地址。并向 `/dubbo/com.foo.BarService/consumers` 目录下写入自己的 URL 地址
- 监控中心(Monitor)启动时: 订阅 `/dubbo/com.foo.BarService` 目录下的所有提供者和消费者 URL 地址

### 思考

1. 入门案例中是将HelloService接口从服务提供者工程(dubbodemo_provider)复制到服务消费者工程(dubbodemo_consumer)中，后期可以单独创建一个maven工程，将此接口创建在这个maven工程中。
2. Dubbo是如何做到远程调用的？Dubbo底层是基于代理技术为HelloService接口创建代理对象，远程调用是通过此代理对象完成的。Dubbo实现网络传输底层是基于**Netty框架**完成的。
3. Zookeeper如果单点故障怎么办？Zookeeper其实是支持集群模式的，可以配置Zookeeper集群来达到Zookeeper服务的高可用，防止出现单点故障。

---

#### Dubbo的相关配置

1. 包扫描：

   ```xml
   <dubbo:annotation package="com.cy.service" />
   ```

2. 协议：

   ```xml
   <dubbo:protocol name="dubbo" port="20880"/> // 默认为20880
   ```

---

### *负载均衡

将请求分摊到多个操作单元上进行执行，从而共同完成工作任务。在集群负载均衡时，Dubbo 提供了多种均衡策略（包括随机、轮询、最少活跃调用数、一致性Hash），缺省为random随机调用。

配置负载均衡策略，既可以在服务提供者一方配置，也可以在服务消费者一方配置

```java
@Controller
@RequestMapping("/demo")
public class HelloController {
    //在服务消费者一方配置负载均衡策略
    @Reference(check = false,loadbalance = "random")
    private HelloService helloService;

    @RequestMapping("/hello")
    @ResponseBody
    public String getName(String name){
        //远程调用
        String result = helloService.sayHello(name);
        System.out.println(result);
        return result;
    }
}


//在服务提供者一方配置负载均衡
@Service(loadbalance = "random")
public class HelloServiceImpl implements HelloService {
    public String sayHello(String name) {
        return "hello " + name;
    }
}
```

