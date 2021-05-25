[TOC]

## 1 Typora快捷键

- ctr + k	页内引用
- ">" 引用

## 2 面试题

## 3 算法

#### 3.1 时间复杂度

算法中的**基本操作的执行次数，为算法的时间复杂度**

时间复杂度和空间复杂度一般都使用大O的渐进表示法进行表示，大O的渐进表示法规则如下：

1、所有常数都用常数1表示。
2、只保留最高阶项。
3、如果最高阶项存在且不是1，则去除与这个项的系数，得到的结果就是大O阶

**在刷题时看到题目要求时间复杂度为O(1)，并不是要求函数内部不能含有循环，而是要求循环的次数为常数次。**

#### 3.2 空间复杂度

空间复杂度算的是变量的个数。

注：递归算法的空间复杂度通常是递归的深度（即递归多少层）

#### 3.3 重点算法

## 4 WEB开发

#### 4.1 Servlet

4.1.1 常见面试题

Servlet的生命周期

```java
// 1.加载和实例化
// 2.初始化
// 3.请求处理
// 4.服务终止
```

1.servletContext生命周期跟web容器生命周期是保持一致的

#### 4.2 Mybatis

4.2.1 原理

**spring 处理Mapper**

> - 当代码中需要使用到一个mapper时候，首先去spring容器中寻找，调用MapperFactoryBean中的getObject方法，获取，然后保存到容器。
>
>   ```java
>     public T getObject() throws Exception {
>       return getSqlSession().getMapper(this.mapperInterface);
>     }
>   ```
>
> - 根据上边代码可以看出，getSqlSession()会获取一个SqlSession
>
> - 
>
>   **因此：一个MapperFactoryBean对象拥有一个sqlSession对象。类型是org.mybatis.spring.SqlSessionTemplate**
>
>   **重要：spring整合mybatis使用的sqlSession类型是org.mybatis.spring.SqlSessionTemplate**



**SqlSessionFactory**

> ```java
> //1. 唯一必要属性：
> 	DataSource	可以是任意的DataSource对象
> //2. 常用属性：
>     configLocation	指定mybatis自身配置文件路径
> 	mapperLocations	 MyBatis 的映射器 XML 配置文件的位置
> //3.         
>         
> ```
>
> 

4.2.2 配置的重点问题

1、如果引用的时mybatis-spring-boot-starter包，MyBatis-Spring-Boot-Starter会做以下操作：

- 自动检测到一个存在的数据源

- 创建并注册一个SqlSessionFactory对象，并将这个数据源传入SqlSessionFactory对象

- 将创建并注册SqlSessionTemplate的实例从SqlSessionFactory中获取的

- Auto-scan your mappers, link them to the `SqlSessionTemplate` and register them to Spring context so they can be injected into your beans

  > ```shell
  > 注意：以上有个前提，是你的Mapper接口跟你的Mapper.xml文件在同一个目录下，如果接口文件和xml是分开放置，比如xml文件在resource目录下，则需要手动配置SqlSessionFactory，并设定好xml扫描路径，此时yml文件中的mybatis.mapper-locations将不会起作用
  > ```

- 手动注入SqlSessionFactoryBean示例

  ```java
      @Bean
      public SqlSessionFactoryBean sqlSessionFactory () throws IOException {
          SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
          sqlSessionFactory.setDataSource(dataSource);
          /**
           * 如果自定义了SqlSessionFactoryBean，那么配置文件中的mybatis:mapper-locations将不会起作用，需要在此set
           */
          sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
          return sqlSessionFactory;
      }
  ```

- mapper.xml文件中jdbcType中必须是大写，无语

  ```xml
  <resultMap id="BasicResultMap" type="cn.qz.mybatis.entity.UserInfoEntity">
      <id column="id" jdbcType="INTEGER" property="id" />
      <result column="name"  jdbcType="VARCHAR" property="name" />
  ```

  

#### 4.3 事务

作用范围：事务隔离级别是对不同事务之间来说。

脏读：

> 读了未提交的数据，事务A读到了事务B

可重复读：

> 只能读取事务开启前的数据版本

commit 动作实质：

> 更新修改数据提交数据库服务器，写入日志，仅此而已。

#### 4.4 Filter

通常实现Filter接口来实现一些对Http请求或者返回过程的一些增强处理。

作用时机：

![image-20210513111821112](Java特性.assets/image-20210513111821112.png)

![image-20210513113125016](Java特性.assets/image-20210513113125016.png)

#### 4.5 Intercepter

作用范围：

>  当你提交对Action(默认是.action结尾的url)的请求时，ServletDispatcher会根据你的请求，去调度并执行相应的Action。在Action执行之前，调用被Interceptor截取，Interceptor在Action执行前后执行。

preHandle():

> 调用时间：Controller方法处理之前
>
> 执行顺序：链式Intercepter情况下，Intercepter按照声明的顺序一个接一个执行
>
> 若返回false，则中断执行，注意：不会进入afterCompletion

postHandle():

> 调用前提：preHandle返回true
>
> 调用时间：Controller方法处理完之后，DispatcherServlet进行视图的渲染之前，也就是说在这个方法中你可以对ModelAndView进行操作
>
> 执行顺序：链式Intercepter情况下，Intercepter按照声明的顺序倒着执行。
>
> 备注：postHandle虽然post打头，但post、get方法都能处理

afterCompletion():

> 调用前提：preHandle返回true
>
> 调用时间：DispatcherServlet进行视图的渲染之后
>
> 多用于清理资源

如果注入的时候引用到Service，因为spring Bean加载是在springcontext 之后，而拦截器加载的时间点在springcontext之前,因此自动注入的话，service是null，解决方案也很简单，我们在注册拦截器之前，先将Interceptor 手动进行注入。注意：在registry.addInterceptor()注册的是getMyInterceptor() 实例。

```java
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Bean
    public MyInterceptor getMyInterceptor(){
        System.out.println("注入了MyInterceptor");
        return new MyInterceptor();
    }
  
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/**");
    }
}
```

```java
// 过滤器用@Order注解控制执行顺序，通过@Order控制过滤器的级别，值越小级别越高越先执行。
 @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor2()).addPathPatterns("/**").order(2);
        registry.addInterceptor(new MyInterceptor1()).addPathPatterns("/**").order(1);
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**").order(3);
    }
```

**两者区别**

> - 过滤器 不能够使用 Spring 容器资源，只能在 Servlet 容器（e.g. tomcat）启动时调用一次，而 拦截器 是 Spring 提供的组件，由 Spring 来管理，因此它能使用 Spring 里的任何资源、对象，例如 Service 对象、数据源、事务管理等等，通过 IoC 注入到 拦截器 中即可。相比较而言，拦截器 要更灵活一些。
> - 过滤器的实现基于回调函数。而拦截器（代理模式）的实现基于反射
> - 
>

#### 4.6  分布式锁

单机情况下，多个线程间同时访问某个共享资源，通过synchronize/Lock进行对共享资源加锁，如果在分布式系统方式下，共享资源的竞争则是在不同进程间进行，因此必须引入，分布式锁。

实现方式：

- 基于数据库乐观锁，乐观锁不用借助数据库的锁机制，增加字段版本标识version来控制

  > 注意事项：
  >
  > - 这种锁没有失效时间，一旦释放锁失败，锁记录会一直存在表中，其他线程无法获取锁，可以通过定时任务清理
  > - 这种锁依赖数据库，建议设置备份库，避免单点
  > - 这种锁是非阻塞的，因为插入数据失败后会直接报错，想要获取锁就要再次操作，
  >
  > 

- 悲观锁

- 

## 5 核心知识卷一

#### 5.1 常用命令

生成doc文档

javadoc -d tartgetDir package1  package2

#### 5.2 修饰符

public

> - 公用属性，其他类都可以调用

private

> - 私有属性，是最严格的，只有当前类可以调用，继承的子类也不可访问

protected

> - 解决private子类不能访问的缺陷，子类可以访问，相同包内也可以访问
> - 一个类的方法操作另一个类的对象，我们就说这个类依赖另一个类
> - 

final

> - 此变量只能被赋值一次
> - 命名字母全大写

#### 5.3 集合

**HashSet**

特性：

- 不保证元素顺序
- 允许为null
- 线程不安全
- 不允许有重复元素
- 底层实现是HashMap,因此也是线程不安全

构造函数

```java
//构造一个新的空集合; 背景HashMap实例具有默认初始容量（16）和负载因子（0.75）
    public HashSet() {
        map = new HashMap<>();
    }
```

**HashTable**

特性：

- 线程安全,实现方法中基本都添加了synchronized关键字来确保线程同步。
- 因此性能不及HashMap
- 不允许null作key

**HashMap**

特性：

- 线程不安全

- 在多线程环境下若使用HashMap需要使用Collections.synchronizedMap()方法来获取一个线程安全的集合

  ```java
  /**
  Collections.synchronizedMap()实现原理是Collections定义了一个SynchronizedMap的内部类，这个类实现了Map接口，在调用方法时使用synchronized来保证线程同步,当然了实际上操作的还是我们传入的HashMap实例，简单的说就是Collections.synchronizedMap()方法帮我们在操作HashMap时自动添加了synchronized来实现线程同步，类似的其它Collections.synchronizedXX方法也是类似原理
  */
   Map m = Collections.synchronizedMap(new HashMap(...)); 
  ```

- 如果HashMap用Null作key ，则存储在table数组得第一个节点上，hash值为0

- 1.7版本采用头插法，1.8以后采用数组+链表+红黑树（bucket长度超过8就转化成红黑树，如果缩减至小于6之后，则重新退化成链表，达到性能均衡）

- （**JDK1.7**）HashMap在并发执行put操作时，多线程会导致HashMap的Entry链表形成环形数据结构，一旦形成环行数据结构，Entry的next节点永不为空，就会产生死循环获取Entry。

- HashMap不能保证随着时间的推移Map中的元素次序是不变的，如果想考虑顺序存储可以使用LinkedHashMap

**ConcurrentHashMap**

特性

- 

#### 5.4 多线程





## 6 设计模式

#### 6.1 工厂模式

通俗来讲，就是把需求抽象成一个接口，按照不同的业务需求来实现具体的实现类，然后根据业务类型返回需要的执行对象。Spring的BeanFactory就是标准的工厂模式。

```java
//思考：工厂模式和代理模式的区别
```

#### 6.2 代理模式

通俗来讲，就是为其他对象提供一种代理以控制这个对象的访问。

## 7 JVM

#### 7.1 内存空间理解

7.1.0 **JVM对象初始化过程**

- 首先是类加载器 class loader加载class文件
- 加载完成后，交给JVM执行引擎，这个过程中JVM会用一块空间来进行存储程序执行期间数据和相关信息，此为运行时数据区，也就是我们说的JVM内存。
- 运行时数据区分为：
  - java 栈 (VM stack) 【局部变量】
  - 堆 (heap)    【对象，数组】
  - 本地方法栈【C++,go等其他非java实现，也就是**native方法**】
  - 程序计数器 【保存下一条指令的所在存储单元的地址，**每个线程私有**，】
  - 方法区  【类中创建的变量如果声明为引用类型，则放在方法区，对象则在堆中，简单说方法区用来存储类型的元数据信息,在方法区中，存储了每个类的信息（包括类的名称、方法信息、字段信息）、静态变量、常量以及编译器编译后的代码等,当然除了这些之外，还有一项信息是常量池，用来存储编译期间生成的字面量和符号引用】

7.11 堆内存

通俗理解：保存对象的真正数据，都是每一个对象的属性内容
	

7.12栈内存

通俗理解：保存的是一块堆内存的空间地址（类似于地址寄存器）

![image-20210111154513930](Java特性.assets/image-20210111154513930.png)

*栈--主要存放引用和基本数据类型。*

*堆--用来存放 new 出来的对象实例。*

7.13 浅拷贝和深拷贝

浅拷贝：copy对象的引用

深拷贝：创建了一个新的对象，并且复制其内的成员变量

#### 7.2 内存工具使用

7.2.1 Jmap

```shell
jmap -J-d64 -heap pid
```

#### 7.3 类加载过程(重要)

在编译生成class文件时，编译器会产生两个方法加于class文件中，一个是类的初始化方法clinit(加载过程中调用，也叫类初始化阶段), 另一个是实例的初始化方法init（实例化过程中调用）。

![image-20210510171106959](Java特性.assets/image-20210510171106959.png)

7.3.1 clinit方法

指的是，类构造器，主要作用是在类加载过程中初始化阶段进行，执行静态变量初始化和静态块的执行

```java
//注意事项
1、如果类中没有静态变量和静态块，那么此方法不会生成
2、执行clinit方法时，必须先执行父类clinit方法
3、clinit方法只执行一次
4、static变量的赋值操作和静态代码块的合并顺序由源文件中出现的顺序决定
```

7.3.2 init方法

## 8 Spring Cloud

#### 8.1 核心组件

#### 8.2 工程搭建

```java
com.qz     
├── qz-ui              // 前端框架 [80]
├── qz-gateway         // 网关模块 [8080]
├── qz-auth            // 认证中心 [9200]
├── qz-api             // 接口模块
│       └── qz-api-system                          // 系统接口
├── qz-common          // 通用模块
│       └── qz-common-core                         // 核心模块
│       └── qz-common-datascope                    // 权限范围
│       └── qz-common-datasource                   // 多数据源
│       └── qz-common-log                          // 日志记录
│       └── qz-common-redis                        // 缓存服务
│       └── qz-common-security                     // 安全模块
│       └── qz-common-swagger                      // 系统接口
├── qz-modules         // 业务模块
│       └── qz-system                              // 系统模块 [9201]
│       └── qz-gen                                 // 代码生成 [9202]
│       └── qz-job                                 // 定时任务 [9203]
│       └── qz-file                                // 文件服务 [9300]
├── qz-visual          // 图形化管理模块
│       └── qz-visual-monitor                      // 监控中心 [9100]
├──pom.xml                // 公共依赖
```

