package com.h3c.bigdata.zhgx.function.report.task;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sshinfo")
@Data
public class DbSSHDomain {

    private Boolean enabled;

    private String hostIP;

    private String userName;

    private String password;

    private String savePath;

}
