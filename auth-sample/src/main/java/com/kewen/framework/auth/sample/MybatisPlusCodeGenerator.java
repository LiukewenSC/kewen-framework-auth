package com.kewen.framework.auth.sample;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author kewen
 * @descrpition
 * @since 2022-12-05 10:55
 */
public class MybatisPlusCodeGenerator {
    /**
     * @descrpition 配置类，代码生成的时候需要配置的信息
     * @author kewen
     * @since 2022-12-05 11:05
     */
    public static class Config {

        /**
         * 工程绝对路径地址，指定到src上面一层;
         * 为空则表示用代码生成器在的路径
         */
        public static final String ABSOLUTE_PATH="D:\\Projects\\kewen-framework-auth\\auth-sample";

        /**
         * 基础包名
         */
        public static final String BASE_PACKAGE = "com.kewen.framework.auth.sample";
        /**
         * 模块名
         */
        public static final String MODULE_NAME = null;
        /**
         * 是否开启swagger
         */
        public static final boolean ENABLE_SWAGGER = false;
        //public static final List<String> IGNORE_TABLE_PREFIX = Arrays.asList("bak_","sys_log");
        public static final List<String> IGNORE_TABLE_PREFIX = null;
        public static final List<String> CONTAINS_TABLE_PREFIX = Arrays.asList("meeting_room");
        //public static final List<String> CONTAINS_TABLE_PREFIX =null;

        /**
         * 作者
         */
        public static final String AUTHOR = "kewen";

    }

    public static void main(String[] args) throws SQLException {


        //工程路径
        String projectPath ;
        if (StringUtils.isNotBlank(Config.ABSOLUTE_PATH)){
            projectPath = Config.ABSOLUTE_PATH;
        } else {
            //获取路径
            // /D:/MyProjects/chinaunicom-authorities/target/classes/com/chinaunicom/
            String path = MybatisPlusCodeGenerator.class.getResource("").getPath();
            projectPath = path.substring(0, path.indexOf("/target/classes"));
        }


        //数据源配置
        DataSourceConfig.Builder dataSourceConfigBuilder = getDateSourceConfigBuilder();

        List<String> tableNames = getTableNames(dataSourceConfigBuilder);

        //创建配置
        FastAutoGenerator autoGenerator = createAutoGenerator(dataSourceConfigBuilder, projectPath,tableNames);
        //生成文件
        autoGenerator.execute();
    }

    private static FastAutoGenerator createAutoGenerator(DataSourceConfig.Builder dataSourceConfigBuilder, String projectPath,List<String> tableNames) {
        return FastAutoGenerator.create(dataSourceConfigBuilder)
                .globalConfig(builder -> {
                    builder.author(Config.AUTHOR)
                            .disableOpenDir()
                            .commentDate(() -> LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
                            .outputDir(projectPath + "/src/main/java")
                            .dateType(DateType.TIME_PACK)
                    ;
                    if (Config.ENABLE_SWAGGER){
                        builder.enableSwagger();
                    }
                }).packageConfig(builder -> {
                    //包配置
                    builder.parent(Config.BASE_PACKAGE+".mp")
                            //.moduleName(Config.MODULE_NAME)
                            .controller("controller")
                            .service("service")
                            .serviceImpl("service.impl")
                            //.mapper("mapper")
                            .entity("entity")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/mapper/mp" ))
                    //.other("output")
                    ;

                }).strategyConfig(builder -> {
                    builder.enableCapitalMode()//开启全局大写命名
                            //.likeTable()模糊表匹配
                            .addInclude()//添加表匹配，指定要生成的数据表名，不写默认选定数据库所有表
                            //.disableSqlFilter()禁用sql过滤:默认(不使用该方法）true
                            //.enableSchema()启用schema:默认false
                            .addInclude(tableNames)
                            .entityBuilder() //实体策略配置
                                //.disableSerialVersionUID()禁用生成SerialVersionUID：默认true
                                .fileOverride()
                                .enableChainModel()//开启链式模型
                                .enableActiveRecord() //开启 ActiveRecord 模式
                                .enableLombok()//开启lombok
                                //.enableRemoveIsPrefix()//开启 Boolean 类型字段移除 is 前缀
                                .enableTableFieldAnnotation()//开启生成实体时生成字段注解
                                //.addTableFills()添加表字段填充
                                .naming(NamingStrategy.underline_to_camel)//数据表映射实体命名策略：默认下划线转驼峰underline_to_camel
                                .columnNaming(NamingStrategy.underline_to_camel)//表字段映射实体属性命名规则：默认null，不指定按照naming执行
                                .idType(IdType.AUTO)//添加全局主键类型
                                //.formatFileName("%s")//格式化实体名称，%s取消首字母I
                                .build()
                            .mapperBuilder()//mapper文件策略
                                .fileOverride()
                                .enableMapperAnnotation()//开启mapper注解
                                .enableBaseResultMap()//启用xml文件中的BaseResultMap 生成
                                .enableBaseColumnList()//启用xml文件中的BaseColumnList
                                //.cache(缓存类.class)设置缓存实现类
                                .formatMapperFileName("%sMpMapper")//格式化Dao类名称
                                .formatXmlFileName("%sMpMapper")//格式化xml文件名称
                                .build()
                            .serviceBuilder()//service文件策略
                                .fileOverride()
                                .formatServiceFileName("%sMpService")//格式化 service 接口文件名称
                                .formatServiceImplFileName("%sMpServiceImpl")//格式化 service 接口文件名称
                                .build()
                            .controllerBuilder()//控制层策略
                                .fileOverride()
                                //.enableHyphenStyle()开启驼峰转连字符，默认：false
                                .enableRestStyle()//开启生成@RestController
                                .formatFileName("%sMpController")//格式化文件名称
                    ;

                })
                .templateConfig(builder -> {
                    //模板配置
                    builder.controller("/templates/controller.java.vm")
                            .service("/templates/service.java.vm")
                            .serviceImpl("/templates/serviceimpl.java.vm")
                            .entity("/templates/entity.java.vm");

                })
                .templateEngine(new VelocityTemplateEngine())
                /*.injectionConfig(builder -> {
                    builder.customMap(Collections.singletonMap("enableSwagger",Config.ENABLE_SWAGGER));
                })*/
                ;
    }

    /**
     * 获取数据库连接配置
     * @return
     */
    private static DataSourceConfig.Builder getDateSourceConfigBuilder() {
        try {
            File file = ResourceUtils.getFile("classpath:application.properties");
            if (file.exists()){
                InputStream stream = new BufferedInputStream(new FileInputStream(file));
                //InputStream stream = new FileInputStream(classpath + "/" + APPLICATION_CONFIG_FILE + ".properties");
                Properties pro = new Properties();
                pro.load(stream);
                //pro.load(new FileInputStream("classpath:" + APPLICATION_CONFIG_FILE + ".properties"));
                String url = pro.getProperty("spring.datasource.url").trim();
                String username = pro.getProperty("spring.datasource.username").trim();
                String password = pro.getProperty("spring.datasource.password").trim();
                return new DataSourceConfig.Builder(url,username,password);
            } else {
                //此处使用yml的方式解析，这里不弄了，在kewen-framework中有
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        throw new RuntimeException("解析配置失败");
    }


    /**
     * 获取Schema下所有表
     *
     * @param dataSourceConfigBuilder
     * @return
     * @throws SQLException
     */
    private static List<String> getTableNames(DataSourceConfig.Builder dataSourceConfigBuilder) throws SQLException {
        DataSourceConfig dataSourceConfig = dataSourceConfigBuilder.build();
        String url = dataSourceConfig.getUrl();
        //jdbc:p6spy:mysql://liukewensc.mysql.rds.aliyuncs.com:3306/uucs
        //jdbc:mysql://119.6.253.231:16102/emergency?useUnicode=true&useSSL=false&serverTimezone=Hongkong&characterEncoding=UTF-8
        url = url.substring(url.indexOf("://")+3);
        String tableSchema;
        if (url.contains("?")){
            tableSchema = url.substring(url.indexOf("/")+1,url.indexOf("?"));
        } else {
            tableSchema = url.substring(url.indexOf("/")+1);
        }
        //String tableSchema = Config.SCHEMA;
        List<String> tableNames = new ArrayList<>();
        Connection connect = dataSourceConfig.getConn();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("select table_name from information_schema.tables where table_schema='" + tableSchema + "'");
        while (rs.next()) {
            tableNames.add(rs.getString("TABLE_NAME"));
        }
        //匹配需要保留的
        if (!CollectionUtils.isEmpty(Config.CONTAINS_TABLE_PREFIX)){
            tableNames.removeIf(tableName -> Config.CONTAINS_TABLE_PREFIX.stream().noneMatch(tableName::startsWith));
        }
        //排除在排除列表的
        if (!CollectionUtils.isEmpty(Config.IGNORE_TABLE_PREFIX)){
            tableNames.removeIf(tableName -> Config.IGNORE_TABLE_PREFIX.stream().anyMatch(tableName::startsWith));
        }
        return tableNames;
    }


}
