
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

框架通过maven引入，加载到自己的工程中即可。同时，框架也提供了一个示例工程`auth-sample`可以直接启动。启动完成后台工程就创建好了。
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
需要把`auth-starter-security-web`和`auth-starter-rabc`都引进，前者有安全和登录相关的，后台有基于RABC的权限实现

```xml
<dependency>
   <groupId>com.kewen.framework.auth</groupId>
   <artifactId>auth-starter-security-web</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
   <groupId>com.kewen.framework.auth</groupId>
   <artifactId>auth-starter-rabc</artifactId>
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
│    │    ├─AnnotationAuthHandler   权限相关主要抽象接口，需要子类实现
│    │    └─AuthDataService         权限服务层服务，可以直接在Service层调用处理权限
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

# 7. 示例功能

本文以会议室、会议室预约功能举例说明

我们需要的功能有创建会议室、删除会议室、查看会议室可编辑列表、编辑会议室、查看会议室可预约列表、预约会议室
我们将这些功能点进行分类，其中

- 创建会议室、删除会议室因为是数据有无的概念，应当由菜单权限控制，这是比编辑数据更高的权限要求
- 查看可编辑列表、编辑会议室是对会议室的基本信息的操作，其权限应当分为同一功能(meeting_room)下的一组操作类型(edit_info)
- 查看会议室可预约列表、预约会议室对应会议室的使用，其权限应当分为同一功能(meeting_room)下的另一组操作类型(appointment)

需要特别注意的是，编辑会议室可以编辑会议室的预约权限，这是合理的。

## 7.1. 创建会议室

创建会议室主要使用@AuthMenu注解校验是否拥有创建会议室的权限，由菜单API权限控制。
创建会议室完成后需要添加会议室的管理权限。生活中一般有两种方式来控制，
a. 一种方式是创建的人直接有权限处理，不需要每一个都分配管理人员，毕竟每条数据都分配一个权限会很麻烦，有的数据本身也不需要这么多维度的权限。
b. 另一种方式是每个会议室都会分配管理的人，因为会议室这种属于不经常变动的资产，需要有人来管理它。

我们这里使用第二种方案，即创建会议室时为会议室分配管理人员。

除此之外，也可以添加会议室预约的权限。这里就直接创建一个会议室，先不允许预约

```java
/**
 * 创建会议室
 * @param entity  需要实现接口 IdDataAuthEdit<ID> 
 * @return
 */
@PostMapping("/add")
@Transactional(rollbackFor = Exception.class)
@AuthMenu(name = "创建会议室")
@AuthDataAuthEdit(businessFunction = "meeting_room",operate = "edit_info",before = false)
public Result add(@RequestBody MeetingRoomAddReq entity){
   meetingRoomService.save(entity);
   return Result.success();
}
/**
 * 创建会议室,第二种写法，不依赖于注解
 * @param entity  需要实现接口 IdDataAuthEdit<ID> 
 * @return
 */
@PostMapping("/add2")
@AuthMenu(name = "创建会议室")
@Transactional(rollbackFor = Exception.class)
public Result add2(@RequestBody MeetingRoomAddReq entity){
   meetingRoomService.save(entity);
   //引入 com.kewen.framework.auth.core.annotation.AuthDataService;
   authDataService.editDataAuths("meeting_room",entity.getDataId(),"edit_info",entity.getAuthObject());
   return Result.success();
}
```

**启动的时候会自动添加接口** `/meetingRoom;/meetingRoom/add;/meetingRoom/deleteById`
数据库查看`sys_menu_api`可以看到以下新增的
|id|path|name|parent_id|auth_type|
|---|---|---|---|---|
|1820708571775561728	|/meetingRoom	|MeetingRoomMpController|	0	|	1|
|1820708571775561729	|/meetingRoom/add	|MeetingRoomMpController>创建会议室|	1820708571775561728	|	2|
|1820708571775561730	|/meetingRoom/deleteById	|MeetingRoomMpController>删除会议室|	1820708571775561728	|	2|

然后需要我们在数据库找到对应接口权限的地方添加权限映射，由于有上下级关系，我们只需要在`/meetingRoom`对应的数据**id=1820708571775561728**配置权限即可，在此class下都有权限访问

```sql
INSERT into sys_auth_menu(api_id,authority,description) VALUES (1820708571775561728,'ROLE_1','ROLE_超级管理员');
```

上述SQL把超级管理员角色加了进去，超级管理员就有权限访问了，其他角色我们也可以按照此添加即可，也可以在页面上添加。

然后启动执行创建会议室，就可以把数据添加进去了。

`/meetingRoom/add`或`/meetingRoom/add2`

```json
{
  "name": "1103大会议室",
  "userCount": 32,
  "place": "大门入口右转1103号大会议室",
  "remark": "全景落地窗，尊享位置",
  "isVideo": 1,
  "isProjector": 1,
  "isPhone": 1,
  "authObject": {
    "roles": [
      {
        "id": 1,
        "name": "超级管理员"
      },
      {
        "id": 4,
        "name": "会议室管理员"
      }
    ]
  }
}
```

## 7.2. 删除会议室

删除会议室同创建一致，注意菜单权限保持相同即可，（与创建会议均用父级class的权限就可以解决）
//todo 删除会议室需要删除相关权限，是不是考虑单独做一个删除注解比较好

`/meetingRoom/delete`

## 7.3. 编辑会议室

编辑会议室需要有会议室的管理权限，这里定义角色为 **会议室管理角色**

编辑会议室首先要校验`@AuthDataOperation`，判定是否有权限，然后再编辑会议室相关的信息及预约权限

```java
/**
 * 编辑会议室
 * @param entity
 * @return
 */
@PostMapping("/updateById")
@Transactional(rollbackFor = Exception.class)
@AuthDataOperation(businessFunction = "meeting_room",operate = "edit_info")
@AuthDataAuthEdit(businessFunction = "meeting_room",operate = "appointment")
public Result updateById(@RequestBody MeetingRoomUpdateReq entity){
   meetingRoomService.updateById(entity);
   return Result.success();
}
/**
 * 编辑会议室方式2，
 * 不依赖@AuthDataAuthEdit
 * @param entity
 * @return
 */
@PostMapping("/updateById")
@Transactional(rollbackFor = Exception.class)
@AuthDataOperation(businessFunction = "meeting_room",operate = "edit_info")
public Result updateById(@RequestBody MeetingRoomUpdateReq entity){
   meetingRoomService.updateById(entity);
   authDataService.editDataAuths("meeting_room",entity.getDataId(),"appointment",entity.getAuthObject());
   return Result.success();
}
```

编辑会议室除了编辑基本信息以外，还要编辑可预约权限，这里也可以使用两种方式进行
需要注意的是校验是校验数据的`edit_info`操作，而插入的是预约权限`appointment`

```json
{
  "id":1,
  "name": "1103大会议室",
  "userCount": 32,
  "place": "大门入口右转1103号大会议室",
  "remark": "全景落地窗，尊享位置 编辑之后",
  "isVideo": 1,
  "isProjector": 1,
  "isPhone": 1,
  "authObject": {
    "roles": [
      {
        "id": 4,
        "name": "会议室管理员"
      }
    ]
  }
}
```

预约权限改成了只有会议室管理员可以编辑，因此后续超级管理员调用预约的时候的时候就会报没有权限。
至于会议室编辑的权限，应当单独拎出来一个接口用创建会议室的权限来执行，这样权限才能统一

## 7.4. 会议室预约

```java
   /**
    * 预约会议室
    * @param req 会议室ID，其实还有其他的入参，比如时间段等，这里暂时不管
    * @return
    */
   @PostMapping(value = "/appointmentMeetingRoom")
   @AuthDataOperation(businessFunction = "meeting_room",operate = "appointment")
   public Result appointmentMeetingRoom(@RequestBody @Validated MeetingRoomAppointmentReq req) {
      String time = req.getTime();
      //预约会议室的逻辑
      log.info("预约成功{}", time);
      return Result.success(time);
   }
```

请求数据：

```json
{
  "id":1,
  "time":"2024-11-10"
}
```

现在，超级管理员已经没有办法预约会议室了，因为没有权限、（至于超级管理员应当有最大权限这回事则应该在角色层级里面处理，有超管角色就应当有其余所有角色）

我们在数据库的`sys_auth_data`中插入一条功能为会议室**meeting_room**ID为**1**的操作为**appointment**的数据，角色把超级管理员**ROLE_1**加进去

```sql
insert into sys_auth_data(business_function,data_id,operate,authority,description) 
VALUES ('meeting_room',1,'appointment','ROLE_1','ROLE_超级管理员');
```

再次访问接口，成功的预约上了。

以上就是一个完整的功能示例演示。主要需要注意的就是各接口需要的功能即操作类型不能搞混了。

**菜单权限有最大权限，增减数据类的应当由菜单权限控制，对单条数据操作的应当菜单权限创建时指定数据对应的管理权限。**

**对于只单一控制数据的比如日程列表，因为数据本身就之归属于某个人或一组人，因此菜单权限保证数据的增删改查，数据权限就仅剩下数据对应的范围。**

# 8. 高级配置扩展

框架实现了基于RABC的权限体系，同时也预留了很多扩展点，这其实也是一个框架应该具有的基本要求。
这里将从基础到复杂进行配置说明

## 8.1. 安全登录相关

框架引入了SpringSecurity安全框架，大部分安全功能都是在SpringSecurity上进行扩展

### 8.1.1. 登录相关的参数配置

登录重写了SpringSecurity的表单登录逻辑，因为前后端分离项目本身就是使用json交互的，所以改成了json交互，
可以自定义登录的API请求地址、username参数、password参数、token头参数、当前登录人接口、最大session数量、是否可以挤下线

```yml
kewen-framework:
  security:
    login:
      login-url: /login                 #登录地址
      current-user-url: /currentUser    #当前用户接口地址
      maximum-sessions: 1               #最大session数量
      max-sessions-prevents-login: true # 是否不允许挤下线
      username-parameter: username      # username参数
      password-parameter: password      # password参数
      token-parameter: Authorization    # token请求头参数
```

以上是基于yml配置的默认的值，不修改默认为以上的地址。

### 8.1.2. 认证成功处理器

在认证成功后，默认会提供一个成功返回的处理器`DefaultSecurityAuthenticationSuccessHandler`，将成功认证的数据经过转换写入输出流中。
我们这里可以自定义一个成功返回解析器，也可以自定义成功处理器。一般只需要定义解析器就可以了。

1. 自定义成功返回解析器
   需要实现`ResponseBodyResultResolver`，覆写`resolver`方法，要求返回最终写流的数据格式，一般这里用来匹配后端统一定义的返回格式，否则默认返回用户信息和默认退出信息不太符合统一返回格式的要求。
   可以参照`SampleResponseBodyResultResolver`来进行配置

   ```java
   /**
    * 认证成功用户转化类，处理用户的额外信息
    * @author kewen
    * @since 2024-07-04
    */
   public interface ResponseBodyResultResolver {
      /**
       * 处理返回格式
       * @param request  请求
       * @param response 响应，注意不要开关流
       * @param data 准备返回的数据，可以为空
       * @return 准备写流的数据
       */
      Object resolver(HttpServletRequest request, HttpServletResponse response, @Nullable Object data);
   }
   ```

2. 自定义成功返回处理器
   如果自定义返回处理器，则需要定义登录成功的返回和退出登录成功的返回

   ```java
   /**
    * 认证成功处理器 
    * @author kewen
    * @since 2024-07-04
    */
   public interface SecurityAuthenticationSuccessHandler extends AuthenticationSuccessHandler , LogoutSuccessHandler {
      /**
       * 认证成功的数据处理，要求写流返回
       */
      @Override
      void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException;
      /**
       * 退出登录成功的数据处理，要求写流返回
       */
      @Override
      void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException;
   }
   ```

### 8.1.3. 认证失败、授权失败处理器

认证失败的处理相对而言就简单了，框架封装了SpringMVC的异常解析器，只需要按照SpringMVC的异常处理`@ControllerAdvice` + `@ExceptionHandler` 就可以
比如配置一下异常增强，

```java
@RestControllerAdvice
public class ExceptionAdviceHandler {
   @ExceptionHandler(AccessDeniedException.class)
   public Result accessDeniedException(AccessDeniedException t){
      logger.error("访问异常：{}", t.getMessage(), t);
      return Result.failed(401, t.getMessage());
   }
   @ResponseStatus(HttpStatus.FORBIDDEN)
   @ExceptionHandler(AuthenticationException.class)
   public Result authenticationException(AuthenticationException t){
      logger.error("认证异常：{}", t.getMessage(), t);
      return Result.failed(403, t.getMessage());
   }
   @ExceptionHandler(InsufficientAuthenticationException.class)
   public Result insufficientAuthenticationException(InsufficientAuthenticationException t){
      logger.error("授权异常：{}", t.getMessage(), t);
      return Result.failed(401, t.getMessage());
   }
}
```

也可以参考 **auth-sample**下的`ExceptionAdviceHandler`

## 8.2. 注解权限相关

注解定义了主体的逻辑，除了既定的配置外，不支持扩展，能扩展的主要是在它的处理器中以及处理器的实现逻辑

### 8.2.1. 权限处理器`AnnotationAuthHandler`

`AnnotationAuthHandler`是权限校验和配置的实现类，它主要包含四个注解对应的校验方法。
框架默认在auth-rabc中对其有一个RABC框架的实现，如果不需要的话也可以自己定义实现
但是这里需要注意，如果自定义了`AnnotationAuthHandler`那么后续`auth-rabc`和`auth-starter-rabc`就不再应该引入了，因为`auth-rabc`的目的就是实现默认的`AnnotationAuthHandler`

实现默认的需要实现4个方法，如下：

```java
/**
 *  权限处理器，对于注解涉及到的权限需要的实现都从这里完成
 * <E> 权限对象泛型
 * @author kewen
 * @since 2023-04-10
 */
public interface AnnotationAuthHandler<ID> {

    /**
     * 是否有菜单访问权限
     *  对应菜单 范围权限 @AuthCheckMenuAccess
     * @param auths
     * @param path
     * @return
     */
    boolean hasMenuAccessAuth(Collection<BaseAuth> auths, String path) ;

    /**
     * 数据权限的数据库、表字段
     *  对应范围查询 @AuthDataRange
     * @return
     */
    AuthDataTable getAuthDataTable();

    /**
     * 是否有某条数据的操作权限
     *  对应操作范围权限 @AuthCheckDataOperation
     * @param auths 用户权限
     * @param businessFunction 模块
     * @param operate 操作
     * @param dataId 业务id，如 1L 1011L等业务主键ID
     * @return 是否有权限
     */
    boolean hasDataOperateAuths(Collection<BaseAuth> auths, String businessFunction, String operate, ID dataId);


    /**
     * 编辑某条 数据权限
     * 但是这里要注意了，不应该编辑此接口本身的权限，否则就会出现自己把自己编辑没，或者把不应该有的人加入（其实就是属于越权了，本应该是上级做的事）
     *  对应编辑数据权限 @AuthEditDataAuth
     * @param dataId 数据ID
     * @param businessFunction 模块
     * @param operate 操作
     * @param auths 权限结构
     */
    void editDataAuths(ID dataId, String businessFunction, String operate, Collection<BaseAuth> auths);

    /**
     * 获取数据
     */
    Collection<BaseAuth> getDataAuths(ID dataId, String businessFunction, String operate);
}
```

因此，如果用RABC权限体系的话一般不建议实现此接口，如有自定义的可以修改`RabcAnnotationAuthHandler`内部的一些逻辑

### 8.2.2. `RabcAnnotationAuthHandler`内部的扩展

内部扩展主要是在`SysAuthMenuComposite`和`SysAuthDataComposite`两个。

- `SysAuthMenuComposite`承载了菜单相关的逻辑，默认实现是基于内存的菜单管理关系，这里可以扩展将其修改为基于redis的等。后续看情况单独抽离一个存储容器让其可以自定义
- `SysAuthDataComposite` 主要是数据权限验证相关的处理，默认是`SysAuthDataCompositeImpl`直接查库，有自定义需求的可以修改这里。

## 8.3. RABC默认权限结构体的扩展

RABC默认目前只实现了基于部门-用户-角色的三个维度的权限，且没有完成上下级的相关（后续拟加入上下级和复杂组合的权限实现）
如果目前不满足也可以自行扩展基础类

比如，我们新增加一个岗位维度的

- 1. 新建一个岗位类`Position`继承`AbstractIdNameFlagAuthEntity`，实现基本的基于id、name的权限体

 ```java
 public class Position extends AbstractIdNameFlagAuthEntity{
    public Position(Long id , String name) {
       this.id=id;
       this.name=name;
    }
 }
 ```

- 2. 新建一个权限集合体继承`SimpleAuthObject`，重写方法`addAnotherBashAuth`和`setAnotherBaseAuth`，使之可以添加相关的功能

 ```java
 public class PositionSimpleAuthObject extends SimpleAuthObject {
    /**
     * 添加其他权限对象，若子类继承可以扩展这里，也可以覆写listBaseAuth()
     * @param baseAuths
     */
    public void addAnotherBashAuth(Collection<BaseAuth>  baseAuths){

    }
    /**
     * 设置其他权限对象，若子类需要的话可以扩展这里，也可以覆写 setBaseAuth()
     * @param abstractAuthEntity
     */
    public void setAnotherBaseAuth(IFlagAuthEntity abstractAuthEntity){

    }
 }
 ```

- 3. 实现`SysUserComposite`或继承`SysUserCompositeImpl`，重写通过用户加载权限的方法`loadByUsername()`，要替换`UserAuthObject`中的`authObject`字段
     这部分可以参考`SysUserCompositeImpl`来实现

```java
    @Override
    public UserAuthObject loadByUsername(String username) {

        SysUser user = userMpService.getOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
                        .select()
        );
        if (user == null){
            return null;
        }

        UserAuthObject userAuthObject = new UserAuthObject();
        userAuthObject.setSysUser(user);
        SysUserCredential credential = credentialMpService.getOne(
                new LambdaQueryWrapper<SysUserCredential>().eq(SysUserCredential::getUserId, user.getId())
                        .select()
        );
        if (credential == null){
            return userAuthObject;
        }
        userAuthObject.setSysUserCredential(credential);
        
        //这里改成查询得到PositionSimpleAuthObject
        SimpleAuthObject authObject = unionCompositeMapper.getUserAuthObject(user.getId());

        userAuthObject.setAuthObject(authObject);

        return userAuthObject;
    }
```

- 4. 编辑权限入参的实体加入`PositionSimpleAuthObject`以配置权限体

 ```java
 @Data
 public class EditIdDataAuthEdit implements IdDataAuthEdit<Long> {
    PositionSimpleAuthObject authObject;
    private Long id;
    @Override
    public Long getDataId() {
       return id;
    }
    @Override
    public IAuthObject getAuthObject() {
       return authObject;
    }
 }
 ```

