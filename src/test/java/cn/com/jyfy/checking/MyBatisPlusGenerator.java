package cn.com.jyfy.checking;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.sql.SQLException;


/**
 * Created by Miracle on 2019/1/26.
 */
public class MyBatisPlusGenerator {
    public static void main(String[] args) throws SQLException {

        //1. 全局配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true) // 是否支持AR模式
                .setAuthor("Miracle") // 作者
                .setOutputDir("E:\\Projects\\all") // 生成路径
                .setFileOverride(true)  // 文件覆盖
                .setIdType(IdType.INPUT) // 主键策略
                .setServiceName("%sService")  // 设置生成的service接口的名字的首字母是否为I
                .setMapperName("%sMapper")
                .setEntityName("%sDO")
                // IEmployeeService
                .setBaseResultMap(true)//生成基本的resultMap
                .setBaseColumnList(true);//生成基本的SQL片段

        //2. 数据源配置
        DataSourceConfig dsConfig  = new DataSourceConfig();
        dsConfig.setDbType(DbType.ORACLE)  // 设置数据库类型
                .setDriverName("oracle.jdbc.driver.OracleDriver")
                .setUrl("jdbc:oracle:thin:@172.20.0.54:1521/orcl" )
                .setUsername("KAOHE")
                .setPassword("KAOHE");

        //3. 策略配置globalConfiguration中
        StrategyConfig stConfig = new StrategyConfig();
        stConfig.setCapitalMode(true)//全局大写命名
                // 指定表名 字段名是否使用下划线
                .setNaming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                .setTablePrefix("CS_")
                .setInclude("CS_MENU_ELEMENT");  // 生成的表


        //4. 包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent("cn.com.jyfy.checking")
                .setMapper("mapper")//dao
                .setService("service")//servcie
                .setEntity("entity")
                .setXml("mapper.mapper")//mapper.xml
                .setController("controller")//controller
        ;

        //5. 整合配置
        AutoGenerator  ag = new AutoGenerator();
        ag.setGlobalConfig(config)
                .setDataSource(dsConfig)
                .setStrategy(stConfig)
                .setPackageInfo(pkConfig);

        //6. 执行
        ag.execute();
    }


}
