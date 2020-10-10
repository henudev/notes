
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @Description: Mybatis Plus 代码生成类
 * @Author: fly
 * @Date: 2020/7/4 15:17
 */
public class MybatisPlusGenerator {

    private static final String OUTPUTDIR = "C:\\git\\gesp_server\\src\\main\\java";
    private static final String AUTHOR = "q";
    private static final String[] TABLES = {"zq_person_name"};
    private static final DbType DBTYPE = DbType.MYSQL;
    private static final String DRIVERNAME = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String URL = "jdbc:mysql://localhost:3306/gesp?useUnicode=true&serverTimezone=GMT&useSSL=false&characterEncoding=utf8";
    private static final String PACKAGEPARENT = "com.zqxy";


    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(OUTPUTDIR);//输出文件路径
        gc.setFileOverride(true);
        gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columnList
        gc.setAuthor(AUTHOR);

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setEntityName("%sEntity");
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DBTYPE);
        dsc.setDriverName(DRIVERNAME);
        dsc.setUsername(USERNAME);
        dsc.setPassword(PASSWORD);
        dsc.setUrl(URL);
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(TABLES);// 需要生成的表
        strategy.setSuperServiceClass(null);
        strategy.setSuperServiceImplClass(null);
        strategy.setSuperMapperClass(null);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PACKAGEPARENT);
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("dao");
        pc.setEntity("entity");
        pc.setXml("xml");
        mpg.setPackageInfo(pc);

        // 执行生成
        mpg.execute();

    }
}
