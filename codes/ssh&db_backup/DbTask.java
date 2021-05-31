package com.h3c.bigdata.zhgx.function.report.task;

import com.h3c.bigdata.zhgx.function.report.dao.TemplateCollectEntityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component("dbTask")
@Slf4j
public class DbTask {

    @Resource
    private TemplateCollectEntityMapper templateCollectEntityMapper;

    @Autowired
    private DbSSHDomain dbSSHDomain;

    @Value("${database.ip}")
    private String hostIP;

    @Value("${database.port}")
    private String hostPort;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static String select_backup_dbs_sql = "SELECT source_name_en from template_source";

    private String databaseName;
    /**
     * 初始化查询需要备份的库.
     */
    @PostConstruct
    private void init(){
        List<Map<String, Object>> dbs = templateCollectEntityMapper.getMapsBySql(select_backup_dbs_sql);
        StringBuilder sb = new StringBuilder();
        dbs.stream().forEach(item -> {
            sb.append(item.get("source_name_en")).append(" ");
        });
        databaseName = sb.toString();
    }

    @Scheduled(cron ="${scheduled.cron}")
    public void mysqlBackup() throws Exception {
        SqlBackupUtil.mysqlBackupDatabaseTool(hostIP,hostPort,userName,password,databaseName,dbSSHDomain);
    }
}
