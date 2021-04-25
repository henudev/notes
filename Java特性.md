[TOC]

## 面试题

### 汇总

#### 集合

1. hashmap的结构，默认扩容因子0.75时，，达到12个元素后什么情况下不会引发扩容？最多能存储多少个元素才引发扩容？
2. 还是hashmap，和currenthashmap区别，二者结构，原理，hashmap能不能存null，为什么可以？
3. threadlocal原理，应用之类的；
4. hashMap hashTable区别
5. hashMap 1.7 和 1.8实现区别

#### Mybatis

## Java特性

4. 

### 封装

Java中的封装是将数据（变量）和作用于数据（方法）的代码作为一个单元一起包装的机制。在封装中，类的变量将从其他类隐藏，并且只能通过其当前类的方法访问。因此，它也被称为数据隐藏。

> 好好理解，好好思考

思考问题：Java 属性为什么要设置为private而不用public

### 多态



### 继承

## 算法

### 时间复杂度

算法中的**基本操作的执行次数，为算法的时间复杂度**

时间复杂度和空间复杂度一般都使用大O的渐进表示法进行表示，大O的渐进表示法规则如下：

1、所有常数都用常数1表示。
2、只保留最高阶项。
3、如果最高阶项存在且不是1，则去除与这个项的系数，得到的结果就是大O阶

**在刷题时看到题目要求时间复杂度为O(1)，并不是要求函数内部不能含有循环，而是要求循环的次数为常数次。**

### 空间复杂度

空间复杂度算的是变量的个数。

注：递归算法的空间复杂度通常是递归的深度（即递归多少层）

### 重点算法示例

## JavaWeb

### servlet

1.servletContext生命周期跟web容器生命周期是保持一致的

## 核心知识卷一

#### 基础语法

##### 修饰符

public：公用属性，其他类都可以调用

private：私有属性，是最严格的，只有当前类可以调用，继承的子类也不可访问

protected：解决private子类不能访问的缺陷，子类可以访问，相同包内也可以访问

1. 一个类的方法操作另一个类的对象，我们就说这个类依赖另一个类



##### 集合

###### HashSet

特性：

- 不保证元素顺序
- 允许为null
- 线程不安全
- 不允许有重复元素
- 底层实现是HashTable

构造函数

```java
//构造一个新的空集合; 背景HashMap实例具有默认初始容量（16）和负载因子（0.75）
    public HashSet() {
        map = new HashMap<>();
    }
```

###### HashTable

特性：

- 线程安全,实现方法中基本都添加了synchronized关键字来确保线程同步。
- 因此性能不及HashMap
- 不允许null作key

###### HashMap

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

- 

## Mybatis

### 配置的坑

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

- 

### 执行日志打印

## 设计模式

### 工厂模式

通俗来讲，就是把需求抽象成一个接口，按照不同的业务需求来实现具体的实现类，然后根据业务类型返回需要的执行对象。Spring的BeanFactory就是标准的工厂模式。

```
思考：工厂模式和代理模式的区别

```

### 代理模式

通俗来讲，就是为其他对象提供一种代理以控制这个对象的访问。

## JVM

### 内存空间理解

#### 堆内存

通俗理解：保存对象的真正数据，都是每一个对象的属性内容
	

#### 栈内存

通俗理解：保存的是一块堆内存的空间地址（类似于地址寄存器）

![image-20210111154513930](Java特性.assets/image-20210111154513930.png)

#### 浅拷贝和深拷贝

浅拷贝：copy对象的引用

深拷贝：创建了一个新的对象，并且复制其内的成员变量

