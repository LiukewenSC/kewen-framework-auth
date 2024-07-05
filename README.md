
# 介绍

本项目是基于kewen-framework的权限管理框架，并在原基础上做了优化，更易于理解，实现了权限的统一封装权限控制

# 模块功能说明

## `kewen-auth-core` 

本模块主要提供基础的权限校验功能，包括：

- 菜单访问权限校验 `@CheckMenuAccess`。
- 数据范围查询封装 `@DataRange`。
- 数据操作校验  `@CheckDataOperation`。
- 数据权限编辑 `@EditDataAuth`。

本模块仅做了权限流程的抽象，并未对其实现，需要用户自行实现，也可以引入`kewen-auth-impl`默认实现模块

# 使用

## 入门 快速搭建工程

1. 首先创建工程，Pom形式如下，
   - 需要指定<parent>标签管理依赖
   - dependencies中指定必要的依赖，这里直接以SpringSecurity引入

其实主要就是引入两个依赖 `kewen-auth-starter-security-web`和`kewen-auth-starter-rabc`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>

   <parent>
      <groupId>com.kewen.framework.auth</groupId>
      <artifactId>kewen-auth-parent</artifactId>
      <version>1.0-SNAPSHOT</version>
   </parent>

   <groupId>com.kewen.self</groupId>
   <artifactId>kewen.self.backend</artifactId>
   <version>1.0-SNAPSHOT</version>

   <properties>
      <maven.compiler.source>8</maven.compiler.source>
      <maven.compiler.target>8</maven.compiler.target>
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
   </properties>

   <dependencies>
      <dependency>
         <groupId>com.kewen.framework.auth</groupId>
         <artifactId>kewen-auth-starter-security-web</artifactId>
      </dependency>
      <dependency>
         <groupId>com.kewen.framework.auth</groupId>
         <artifactId>kewen-auth-starter-rabc</artifactId>
      </dependency>

      <dependency>
         <groupId>mysql</groupId>
         <artifactId>mysql-connector-java</artifactId>
      </dependency>
   </dependencies>
</project>
```

2. 配置 application.properties 文件

```properties
server.port=8081
spring.datasource.url=jdbc:mysql://liukewensc.mysql.rds.aliyuncs.com:3306/kewen_framework_auth_template
spring.datasource.username=open_framework
spring.datasource.password=framework123456_
spring.datasource.hikari.connection-test-query=SELECT 1 from dual
```

执行完以上两步骤就可以使用了，但是这里没有对异常返回的统一处理，建议再配置一个异常解析器

```java
/**
 * 可以继承 ResponseEntityExceptionHandler，继承了之后会多出来默认的异常解析处理
 */
@Slf4j
@RestControllerAdvice
//public class SampleRepopseAdvance extends ResponseEntityExceptionHandler {
public class SampleResponseAdvance {
    
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exception(Throwable e){
        log.error("全局异常拦截Throwable： "+e.getMessage(),e);
        return Result.fail(e.getMessage());
    }
}
```


## 使用说明

#### 接口实现

1. 使用`kewen-auth-core`模块需要用户自行实现`AnnotationAuthHandler`接口，完成

   - 数据范围权限的数据库字段定义
   - 是否有菜单访问权限 判定
   - 是否有某条数据的操作权限 判定
   - 编辑某条数据权限的 实现逻辑
    （本项目默认实现在`kewen-auth-impl`中，在starter中可以替换）

2. 用户登录逻辑自行控制，登录完成后每次请求的时候设置用户权限至 `UserAuthContextContainer`中
   `UserAuthContextContainer`也有默认实现ThreadLocal方式，也可以修改，如spring-security的SecurityContextHolder方式

#### 菜单访问权限校验 `@CheckMenuAccess`说明

菜单访问权限通过webmvc的 `HandlerInterceptor` 来实现，应用在需要校验的Controller方法上加入注解`@CheckMenuAccess`即可.
默认菜单会根据请求的url去对应后台配置的菜单链接
如：
```java
@Controller
@RequestMapping("/test")
public class UserController {
    /**
     * 测试菜单控制
     * @return
     */
    @CheckMenuAccess
    @GetMapping("/checkMenu")
    public String testCheckMenu() {
        //......
    }
}
```

#### 数据范围查询封装 `@DataRange`说明

数据范围查询封装，通过注解`@DataRange`来实现，在查询方法上加入注解`@DataRange`
就可以实现不用再关注权限相关东西，业务直接调用即可范围查询即可
如：
```java
@Controller
@RequestMapping("/test")
public class UserController {

    /**
     * 测试数据范围
     * @return
     */
    @DataRange(module = "testauth")
    @GetMapping("/dataRange")
    public String testDataRange() {
        //直接测试菜单的权限就知道了，
        List<TestauthAnnotationBusiness> list = testauthAnnotationBusinessMpService.list();
        System.out.println(list);
        Assert.isTrue(list.size()==1, "菜单列表为空");
        return "testDataRange";
    }
}
```

#### 数据操作校验  `@CheckDataOperation`说明

数据权限校验，通过注解`@CheckDataOperation`来实现，需要校验的方法上加入注解`@CheckDataOperation`，同时，请求参数需要实现`BusinessData`接口
如：
```java
@Controller
@RequestMapping("/test")
public class UserController {

    /**
     * 测试数据编辑
     * @return
     */
    @PostMapping("/dataEdit")
    @CheckDataOperation(module = "testedit")
    public String testDataEdit(@RequestBody EditBusinessData editBusinessData) {
        System.out.println("successEdit");
        return "testDataEdit";
    }
}
```

#### 数据权限编辑封装 `@EditDataAuth`说明

数据权限编辑封装，通过注解`@EditDataAuth`来实现，需要校验的方法上加入注解`@EditDataAuth`，同时，请求参数需要实现`AuthDataEditBusiness`接口
如：
```java
@Controller
@RequestMapping("/test")
public class UserController {
    /**
     * 测试数据权限编辑
     * @return
     */
    @PostMapping("/dataAuthEdit")
    @EditDataAuth(module = "testauth")
    public String testDataAuthEdit(@RequestBody EditAuthDataEditBusiness applicationBusiness) {

        return "testDataAuthEdit";
    }
 }
```

#### 扩展实现

定义了抽象的权限配置集合体，以及权限实体，用户可以统一使用此定义，方便快捷的对权限字符串和权限实体、权限集合体进行转换，从而转换成项目中可直观使用的权限集合体。
默认已经有`DefaultAuthObject`实现，和基本的`User、Dept、Role`实体
需要自定义实体的则 权限配置集合体继承`DefaultAuthObject`，权限实体则继承`AbstractIdNameAuthEntity`或实现`IAuthEntityProvider`

## `kewen-auth-impl`

默认实现模块，对`kewen-auth-core`的权限实现，有`Dept Role User`三个维度的默认实现，也可以通过继承替换添加更多的维度
当然也可以不使用此模块，完全对core模块自行实现

## `kewen-auth-starter`

默认的springboot启动器，引入即可以开箱即用，权限维度默认`kewen-auth-impl`的`User、Dept、Role`维度
修改见`AuthImplConfig`类，注入自己的`SysDataAuthComposite`和`SysMenuAuthComposite`

## `kewen-auth-sample`

启动示例

# 配置文件

```yml
kewen-framework:
   auth:
      data-range-database-field:
         table-name: sys_auth_data
         dataIdColumn: data_id
         authorityColumn: authority  
```

# 工程过程记录

- sys_menu用mybatis-plus自动生成的代码，改动过，meta 属性添加了typeHandler，就别删了
