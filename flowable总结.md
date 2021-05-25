## Flowable简单总结

安装的坑

目前集成的最新版本Flowable 6.6.0

- DATEEXECUTED字段在数据库中为timestemp类型,当使用的数据库驱动包版本较新时(这里使用的是8.0.23)返回的类型为LocalDateTime,强转为string导致报错. 修改驱动包版本到8.0.19,服务正常启动

- slf4j必须加上

  ```xml
          <dependency>
              <groupId>org.slf4j</groupId>
              <artifactId>slf4j-api</artifactId>
              <version>1.7.21</version>
          </dependency>
          <dependency>
              <groupId>org.slf4j</groupId>
              <artifactId>slf4j-log4j12</artifactId>
              <version>1.7.21</version>
          </dependency>
  ```

  ```properties
  #log4j.properties
  log4j.rootLogger=DEBUG, CA
  log4j.appender.CA=org.apache.log4j.ConsoleAppender
  log4j.appender.CA.layout=org.apache.log4j.PatternLayout
  log4j.appender.CA.layout.ConversionPattern= %d{hh:mm:ss,SSS} [%t] %-5p %c %x - %m%n
  ```

### 官方war

**flowable-rest** ： rest接口

**flowable-ui** ：

- Flowable IDM：对所有的Flowable UI应用提供权限管理
- Flowable Modeler：让用户制作model，表单，决策表，和应用定义
- Flowable Task：启动流程引擎的运行任务，编辑，完成，查询工作流实例中的任务
- Flowable Admin：允许管理员权限的用户查询BPMN，DMN，表单，提供一些选项来修改工作流中的任务，依赖Flowable Task和Flowable REST App

> 默认密码：admin / test

### 申请通过后自动执行

serviceTask 这个逻辑可以做任何事情

```xml
<serviceTask id="externalSystemCall" name="Enter holidays in external system"
    flowable:class="org.flowable.CallExternalSystemDelegate"/>
```

实现*org.flowable.engine.delegate.JavaDelegate*接口

```java
package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class CallExternalSystemDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) {
        System.out.println("Calling the external system for employee "
            + execution.getVariable("employee"));
    }
}
```

### activiti历史记录级别

- none

  不记录历史流程，性能好，流程结束后不可读取

- activiti

  归档流程实例和活动实例，流程变量不同步

- audit

  默认值，在activiti基础上同步变量，保存表单属性

- full

  性能较差，记录所有实例和变量细节变化，最完整的历史记录，如果需要日后跟踪详细可以使用此级别（一般不建议开启）