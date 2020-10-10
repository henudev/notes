package com.h3c.bigdata.zhgx.common.utils;

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

    private static final String OUTPUTDIR = "C:\\git\\h3c\\gesp_server\\src\\main\\java";
    private static final String AUTHOR = "qin";
    private static final String[] TABLES = {"zq_company","zq_company_annual","zq_company_annual_asset","zq_company_annual_investment","zq_company_annual_out_guarantee",
            "zq_company_annual_shareholder","zq_company_annual_social_security","zq_company_annual_stockright_change","zq_company_annual_webinfo",
            "zq_company_branch","zq_company_cancel_info","zq_company_category","zq_company_category_code","zq_company_change_info","zq_company_contactinfo",
            "zq_company_equity_pledged","zq_company_investment","zq_company_jud_assist_list","zq_company_judrisk_assist","zq_company_keyperson",
            "zq_company_liquidation","zq_company_mortgage","zq_company_mortgage_change","zq_company_mortgage_debt_secured","zq_company_mortgage_pawn",
            "zq_company_mortgage_people","zq_company_shareholder","zq_credit_ad_lic","zq_credit_ad_lic_icb","zq_credit_ad_penalty","zq_credit_ad_penalty_cbrc",
            "zq_credit_ad_penalty_circ","zq_credit_ad_penalty_csrc","zq_credit_ad_penalty_icb","zq_credit_bad_abnlist","zq_credit_bad_abnlist_icb",
            "zq_credit_bad_env_punish","zq_credit_bad_executed","zq_credit_bad_executed_icb","zq_credit_bad_owing_tax","zq_credit_bad_records",
            "zq_credit_bad_taxpunish","zq_credit_executed","zq_credit_focus_spotinsp","zq_credit_focus_spotinsp_icb","zq_credit_good","zq_enqa_dp_code",
            "zq_enqa_product_lic","zq_enqa_product_lic_list","zq_enqa_qc","zq_enqa_qc_list","zq_enqa_special_lic","zq_enqa_special_lic_list",
            "zq_environmental_protection","zq_ip_app","zq_ip_book","zq_ip_copyright_software","zq_ip_copyright_software_list","zq_ip_copyright_works","zq_ip_copyright_works_list","zq_ip_patent","zq_ip_patent_applicant_list","zq_ip_patent_ipc_code","zq_ip_patent_status_code","zq_ip_trademark","zq_ip_trademark_detail","zq_ip_trademark_detail_status_code","zq_ip_website_reg","zq_ip_website_reg_list","zq_ip_wechat","zq_judrisk_actioncause_code","zq_judrisk_all_actioncause_code","zq_judrisk_bankruptcy_notice","zq_judrisk_bankruptcy_notice_list","zq_judrisk_casetype_code","zq_judrisk_court_annc_list","zq_judrisk_court_announcement","zq_judrisk_court_code","zq_judrisk_court_session","zq_judrisk_court_session_list","zq_judrisk_judicial_auction","zq_judrisk_lawsuit","zq_judrisk_lawsuit_list","zq_judrisk_lawsuit_relevant_list","zq_person_name"};
    private static final DbType DBTYPE = DbType.MYSQL;
    private static final String DRIVERNAME = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String URL = "jdbc:mysql://10.90.3.59:3306/gesp?useUnicode=true&serverTimezone=GMT&useSSL=false&characterEncoding=utf8";
    private static final String PACKAGEPARENT = "com.h3c.bigdata.zhgx.function.zqxy";


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
