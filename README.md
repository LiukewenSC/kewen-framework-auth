
# 1. 项目简介

**kewen-framework-auth** 是一个基于注解实现的权限控制和验证的Java后端多模块的框架，通过引入对应的模块功能快速引入一整套权限系统开发业务功能，避免了业务上重新再设计权限体系。

在普通的项目中，权限往往是项目的基础功能，业务的运行离不开权限的配合使用，因此每个系统都去搭建自己的一套权限体系，而且每个业务中都需要维护一套权限的系列表，业务中的各个地方还需要在服务中进行一系列的校验。这样做的结果就是花了很多时间来解决权限的问题，而且每个业务自行维护的表无法进行公共的抽象，将出现了很多的相同逻辑的冗余代码。

目前大部分的权限体系其实都是基于RABC思想（用户-部门-角色-岗位）来设计，通过对业务与用户组织信息建立对应的映射关系来实现权限控制，其实有非常大的共有特点，而且由于权限本身对于一个项目来说的话就是公共的部分，因此本项目从常用的RABC权限体系入手，通过设计来实现统一的权限管理。

本框架主要是用`Java+Maven+Spring+SpringMVC+SpringBoot+SpringSecurity+MybatisPlus+Mysql`来搭建的，主要基于Spring5和Springboot2搭建。

# 2. 核心概念

## 2.1. 权限说明

**菜单权限**：访问某个菜单需要的权限。框架中涉及到有前端访问路由的菜单路由权限和请求后端接口的菜单API请求权限。通过对菜单的控制完成粗粒度的鉴权，达到可见/不可见的效果。
**数据权限**：某条数据对应的权限。对于每一条数据，其都应该对应有一个数据权限来对其控制，有对应权限的就应该可以执行对应的操作，没有权限的就应该在查询列表中不可见，同时对应请求也应当不能执行。
**数据权限-操作类型** 数据的权限应该还需要进一步的划分，一条数据对应不同的操作应该有不同的权限。如：会议室可以编辑，也可以预约，这两个操作的基础数据都是会议室，但是编辑应当由管理员来执行，而预约应当大部分人都可以执行的，它肯定不应该只能管理员可以预约。

## 2.2. 核心注解

主要涉及到4个核心注解，1个菜单注解+3个数据注解，分别对应不同的功能实现

`@AuthMenu`：菜单权限注解，判断登录人是否能访问注解对应的API接口。
`@AuthDataRange`： 数据范围查询注解，加上此注解会在查询数据列表时自动筛选登录人可见的数据，不可见的数据不展示。
`@AuthDataOperation`：数据操作注解，加上此注解会在请求时校验是否对单条数据有操作权限，避免通过接口进行越权攻击，一般业务配合`@AuthDataRange`使用。
`@AuthDataAuthEdit`： 数据编辑注解，加上此注解会直接把请求中的权限编辑到权限表中，此注解依赖菜单的权限校验。

# 3. 快速开始

框架通过maven引入，加载到自己的工程中即可。同时，框架也提供了一个示例工程`kewen-auth-sample`可以直接启动。启动完成后台工程就创建好了。
此外，框架还搭配了我的另外一个前端模板项目`kewen-vue-admin`，可以fork到自己工程中然后启动。
启动后前后端均可以直接登录访问，默认账号密码**admin/123456**

## 3.1. 启动示例工程

框架自带了一个启动示例工程，已经做好了模块引入和基本配置，我们只需要做少量的配置即可启动

- 初始化数据库
- 添加配置
- 启动

**1.初始化数据库**：框架默认给了MySQL的初始化脚本，并且添加了默认的一些数据供使用，方便一键启动项目并查看。
脚本的路径在框架目录下的`./script/sql/auth.sql`下

**2.配置**：配置需要自行配置基本的数据库配置

   ```properties
   server.port=8081
   spring.datasource.url=jdbc:mysql://liukewensc.mysql.rds.aliyuncs.com:3306/kewen_framework_auth_template
   spring.datasource.username=open_framework
   spring.datasource.password=framework123456_
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```

配置完成直接启动`AuthWebSample`类即可。

示例项目中包含了基本的注解使用，可以登录后调用相关接口验证。也可以配合`kewen-vue-admin`工程启动登录之后在页面上验证查看。
这里建议把前端工程启动起来验证，可以查看一个完整的流程，只不过前端的启动需要nodejs相关的前端知识。

## 3.2. 引入到自己的工程

框架自带了一套基于RABC(User-Dept-Role)的权限实现，同时配置了安全模块，可以直接引入依赖即可完成，下面说一下快速启动的步骤

- 初始化数据库
- 引入依赖
- 添加配置
- 添加可选配置
- 启动

**引入依赖**：引入相关的依赖
需要把`kewen-auth-starter-security-web`和`kewen-auth-starter-rabc`都引进，前者有安全和登录相关的，后台有基于RABC的权限实现

```xml
<dependency>
   <groupId>com.kewen.framework.auth</groupId>
   <artifactId>kewen-auth-starter-security-web</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
   <groupId>com.kewen.framework.auth</groupId>
   <artifactId>kewen-auth-starter-rabc</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
   <groupId>mysql</groupId>
   <artifactId>mysql-connector-java</artifactId>
</dependency>
```

**初始化数据库**：和利用sample模块启动一致，找到工程目录`./script/sql/auth.sql`的脚本执行即可

**配置**：同样，自行配置基本的数据库配置

```properties
server.port=8081
spring.datasource.url=jdbc:mysql://liukewensc.mysql.rds.aliyuncs.com:3306/kewen_framework_auth_template
spring.datasource.username=open_framework
spring.datasource.password=framework123456_
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## 3.3. 应用可选配置

然后应用中有部分可选配置，可以参考`kewne-auth-sample`启动示例模块选择使用
`ExceptionAdviceHandler`主要用于返回异常，方便在报错时可以准确处理异常
`SampleResponseBodyResultResolver`，认证成功返回的结构处理，可以匹配成和自己项目中一致的统一返回，不配置则使用默认的

另外，示例项目还包含了代码生成相关的类，可以忽略不管，新的表需要一键生成MybatisPlus代码时也可以是使用，后续来完善这部分功能

## 3.4. 启动

完成了以上配置，启动普通的springboot项目即可。

快速开始的内容中包含了完全实现了功能的最小配置，引入即可使用。一个自带的权限管理功能引入完成。

# 4. 项目结构

```txt
kewen-framework-auth
├─auth-core                         核心模块
│    ├─annotation                   注解相关包
│    │    ├─data                    数据相关注解
│    │    │    ├─@AuthDataAuthEdit  数据权限编辑
│    │    │    ├─@AuthDataOperation 数据权限操作校验
│    │    │    └─@AuthDataRange     数据范围权限
│    │    ├─menu                    菜单相关包
│    │    │    └─@AuthMenu          菜单校验注解
│    │    └─AnnotationAuthHandler   权限相关主要抽象接口，需要子类实现
│    ├─model                        权限模型包
│    │    ├─BaseAuth                基础权限，数据库对应的模型结构
│    │    ├─IAuthEntity             权限实体接口，与应用层面相关的抽象
│    │    └─IAuthObject             权限结构体集合抽象，实现应该完成一组权限的集合的定义
│    └─extension
│         ├─AbstractAuthObject      抽象的权限结构体集合，带标记的权限实体接口的组合，内部实现带标记前缀权限实体扫描
│         ├─AbstractIdNameFlagAuthEntity  id+name结构的权限实体实现，内部实现获取和填充属性的方法
│         └─IFlagAuthEntity         带标记的权限实体， 比如 ROLE_1  USER_1 这种结构标记
│
├─auth-rabc                         RABC权限实现模块
│    ├─RabcAnnotationAuthHandler    RABC权限相关实现接口，完成注解的实际功能逻辑
│    ├─model                        RABC相关的基础模型
│    ├─mp                           RABC持久层逻辑代码，mybatisplus代码生成器相关服务
│    ├─utils                        树形工具和对象转换拷贝工具
│    ├─composite                    拆分服务，根据数据、菜单、登录人维度各自完成对应的服务
│    └─controller                   RABC用户、部门、角色相关的业务逻辑，方便维护
|
├─auth-starter-rabc                 RABC快速配置模块，以SpringBoot方式配置
│    ├─config                       配置
│    │    ├─AuthRabcConfig          默认的对象配置，配置auth-rabc模块的相关的Bean
│    │    └─AuthRabcScanConfig      RABC配置扫描，配置相关Bean
│    └─init
│         └─InitMenuAuthCommandLineRunner 菜单初始化配置，启动时默认将@AuthMenu对应的api接口保存至数据库，后续可以直接配置权限
|
├─auth-starter-security-web         框架安全相关的配置，同时包括登录
│    ├─annotation
│    │    └─SecurityIgnore          忽略登录校验路径
│    ├─before  
│    │    └─BeforeSecurityFilter    封装的在进入SpringSecurity过滤器链之前的逻辑
│    ├─config                       安全配置
│    ├─configurer                   以SpringSecurity风格的配置类
│    │    └─JsonLoginAuthenticationFilterConfigurer
│    ├─filter                       过滤器
│    │    ├─AuthUserContextFilter   用户上下文相关过滤器
│    │    ├─JsonLoginFilter         登录过滤器
│    │    └─TokenSessionRequestFilter  token相关过滤器
│    ├─model          
│    │    └─SecurityUser            登录用户
│    ├─response
│    │    └─ResponseBodyResultResolver 成功登录之后的转换结构，方便统一返回
│    └─service
│         ├─SecurityUserDetailsService 用户登录相关需要实现的类
│         └─RabcSecurityUserDetailsService RABC方式默认实现的类
|
├─auth-sample
│    ├─config                       配置类
│    ├─controller                   列表功能，对相关的注解有测试代码
│    ├─mp                           测试表自动生成的测试代码
│    ├─response                     统一异常处理、登录成功返回转化等
│    └─AuthWebSample    启动类
└─script(文件夹)
     └─sql                          RABC初始化数据库脚本文件
```

# 5. 技术选型

框架：本框架主要使用了`Spring+SpringMVC+SpringBoot+SpringSecurity+MybatisPlus` 相关框架完成基础框架的搭建
构建工具：采用Maven作为构建工具，。
数据库：采用Mysql作为后端数据库，但是框架本身是支持其他类型的数据库的，因此也可以自行选择数据库（需要修改初始化的sql脚本，可能还需要修改自定义Mapper中的sql）

# 6. 注解使用

前面我们说道，框架主要由4个注解实现，
`@AuthMenu`：菜单权限注解，判断登录人是否能访问注解对应的API接口。
`@AuthDataRange`： 数据范围查询注解，加上此注解会在查询数据列表时自动筛选登录人可见的数据，不可见的数据不展示。
`@AuthDataOperation`：数据操作注解，加上此注解会在请求时校验是否对单条数据有操作权限，避免通过接口进行越权攻击，一般业务配合@AuthDataRange使用。
`@AuthDataAuthEdit`： 数据编辑注解，加上此注解会直接把请求中的权限编辑到权限表中，此注解依赖菜单的权限校验。

## 6.1. `@AuthMenu`

注解如下：

```java
public @interface AuthMenu {

    /**
     * 名字
     * API菜单的名称，在数据库存储API菜单的时候作为描述使用，方便在数据库层面定位到对应功能
     * @return
     */
    String name();
}
```

**参数：**

- name()：API菜单的名称，在数据库存储API菜单的时候作为描述使用，方便在数据库层面定位到对应功能

**使用方法**：

1. 在Controller对应的方法上添加`@AuthMenu`即可，如：

   ```java

   @RestController
   @RequestMapping("/test")
   public class TestAuthAnnotationController {
      /**
       * 测试菜单控制
       */
      @AuthMenu(name = "测试菜单控制")
      @GetMapping("/checkMenu")
      public Result testCheckMenu() {
         return Result.success("成功通过注解AuthMenu完成控制");
      }
   }
   ```

   此时`/test/checkMenu`路径就会校验菜单权限。
   这里需要说明的是及时类上没有注解，也会默认生成虚拟API菜单，虚拟菜单的说明接下。

2. 在Controller类上使用
   在类上使用表示类中所有的带有@ReqeustMapping的都会加入API菜单校验，但是权限不一定都一样。加入数据库的时候会以类的路径为基础生成一个虚拟API菜单，作为内部方法的父级，可以自由的选择是以父类为权限基准还是子类，如：

   ```java
   @RestController
   @RequestMapping("/testAnnoClassMenuController")
   @AuthMenu(name = "测试只在Controller上加@AuthMenu权限注解")
   public class TestAuthMenuClassController {
      @GetMapping("/hello")
      public String hello(String name) {
         return "hello " + name;
      }
      @GetMapping("/hello2")
      public String hello2() {
         return "hello2 ";
      }
   }
   ```

   此时`/testAnnoClassMenuController/hello`、`/testAnnoClassMenuController/hello2`均会加入API菜单校验

## 6.2. `@AuthDataRange`

注解如下：

```java
/**
 * @author kewen
 * @since 2022-11-23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthDataRange {
    /**
     * 业务功能
     * @return
     */
    String businessFunction() ;
    /**
     * 表别名，多表联查时用于拼接权限 如： t.id
     */
    String tableAlias() default "";
    /**
     * 业务主键column名 用于拼接 t.id
     */
    String dataIdColumn() default "id";
    /**
     * 默认统一的
     * @return 返回操作类型
     */
    String operate() default "unified";
    /**
     * 条件匹配方式 in/exists
     * 关联原则 小表驱动大表
     * 默认通过in的方式，当权限表中数据大时应该采用exists方式
     * //todo 暂未实现
     */
    MatchMethod matchMethod() default MatchMethod.IN;
}
```

**参数：**

- businessFunction()：必填，业务功能，用于区分权限的所属，唯一标识一套数据属于哪一个功能
- tableAlias()： 可选，表别名，用于多表联查时的别名，
- dataIdColumn()： 主表的ID字段名，默认id
- operate()：操作，对同一业务功能的细化操作，如 会议室编辑、预约
- matchMethod()： 暂未实现

基于当前权限范围查询注解，在查询数据的时候关联数据的权限表以及用户权限体系，检查用户是否拥有数据配置的权限
在mapper拦截 ，用于数据权限列表查询
例如 拼接语句：

```sql
select * from ${business_table}
where ${business_table}.{business_id} in
    ( select data_id from sys_auth_data
    where business_function=#{business_function} and operate=#{operate} and authority in ( #{用户权限} )
    )
```

拼接 where后面部分，where前半部分为业务定义的sql，本增强只是在后加上 and 的权限查询语句，避免业务中都需要主动关联权限表匹配，
实现逻辑解耦
where后有条件也不用担心，会自动加上and

**使用方法**：

在对应的Controller上的方法上添加注解@AuthDataRange，填上必填项businessFunction即可
业务中正常查询数据，数据库层面会自动添加上范围条件查询

```java
@RestController
@RequestMapping("/test")
public class TestAuthAnnotationController {
       /**
     * 测试数据范围
     * @return
     */
    @AuthDataRange(businessFunction = "testrange")
    @GetMapping("/dataRange")
    public Object testDataRange() {
        List<TestauthAnnotationBusiness> list = testauthAnnotationBusinessMpService.list();
        System.out.println(list);
        Assert.isTrue(list.size()==1, "菜单列表为空");
        return list;
    }
}
```

## 6.3. `@AuthDataOperation`

注解如下：

```java
/**
 * @descrpition 校验是否有编辑单条数据的权限
 * dataId需要关联获取 {@link IdDataEdit}
 * @author kewen
 * @since 2022-11-23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthDataOperation {
    /**
     * 业务功能
     * @return
     */
    String businessFunction() ;

    /**
     * 操作
     */
    String operate() default "unified";
}
```

**参数：**

- businessFunction()： 业务功能
- operate()： 操作

实现了此注解会根据传入的模块ID和操作以及业务ID自动判定是否有执行此操作的权限，数据库层面如下

```sql
select business_id from sys_auth_data
where business_function=#{business_function} and operate=#{operate} and data_id=#{dataId} 
   and authority in ( #{用户权限集合} )
limit 1
```

**使用方法**：

在方法上添加相应的注解即可，此处不一定是Controller，Service也是可以的，但是要保证能正常开启切面(如不能是内部调用)。

```java
/**
 * 测试数据编辑
 * @return
 */
@AuthDataOperation(businessFunction = "testedit")
public Result testDataEdit(@RequestBody EditIdDataEdit editBusinessData) {
   System.out.println("successEdit");
   return Result.success(editBusinessData);
}
```

## 6.4. `@AuthDataAuthEdit`

注解如下：

```java
public @interface AuthDataAuthEdit {

    /**
     * 模块ID
     * @return 模块ID
     */
    String businessFunction() ;

    /**
     * 操作
     * @return 返回操作类型
     */
    String operate() default "unified";
}
```

**参数：**

- businessFunction()：必填，业务功能，用于区分权限的所属，唯一标识一套数据属于哪一个功能
- operate()：操作，对同一业务功能的细化操作，如 会议室编辑、预约

执行修改业务权限逻辑，记得在这之前要先执行url权限验证,自行控制权限，这里只是封装编辑逻辑
加上注解直接就开始修改业务的权限了，用了这个注解就不用再写权限逻辑，只需要完成写权限的后续逻辑即可
注意**事务**的保证，此注解只是单纯的封装编辑逻辑，并未对事务进行处理，需要调用方保证

**使用方法**：

```java
/**
 * 测试数据权限编辑
 * @return
 */
@PostMapping("/dataAuthEdit")
@AuthDataAuthEdit(businessFunction = "testauthedit")
public Result<EditIdDataAuthEdit> testDataAuthEdit(@RequestBody EditIdDataAuthEdit applicationBusiness) {
   System.out.println("successdataAuthEdit");

   return Result.success(applicationBusiness);
}
```

# 7. 注解原理（待编写）

## 7.1. `@AuthMenu`

## 7.2. `@AuthDataRange`

## 7.3. `@AuthDataOperation`

## 7.4. `@AuthDataAuthEdit`

# 8. 配置指南

**配置说明**：解释如何配置权限框架以适应不同的应用环境。

**配置示例**：提供配置文件示例，帮助用户理解如何设置。

# 9. 权限验证

**验证流程**：阐述权限注解是如何被验证的，以及验证流程是怎样的。

**错误处理**：说明当权限验证失败时框架的响应方式。

# 10. 兼容性和依赖

**兼容性说明**：介绍框架支持的操作系统、数据库、应用程序服务器等。

**依赖列表**：列出框架的依赖库和框架。

# 11. 示例项目

**示例应用**：提供一个或多个示例项目，让用户能够看到权限框架在实际项目中的运用。

# 12. 获取帮助

**问题解答**：提供常见问题解答。

**社区支持**：介绍社区论坛或支持渠道。

# 13. 许可证和版权

**许可证**：明确项目使用的开源许可证。

**版权声明**：声明项目的版权信息。

# 14. 贡献指南

**贡献说明**：鼓励用户贡献代码或提出建议。

**贡献流程**：说明如何提交问题、提出建议或贡献代码。
