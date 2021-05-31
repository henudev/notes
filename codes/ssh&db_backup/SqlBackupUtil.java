package com.h3c.bigdata.zhgx.function.report.task;

import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据库备份工具
 */
@Slf4j
public class SqlBackupUtil {

    /**
     * mysql数据库备份
     * @param hostIP 数据库地址
     * @param hostPort 数据库端口
     * @param userName 用户名
     * @param password 密码
     * @param databaseName 数据库名称
     * @return 文件名称
     */
    public static String mysqlBackupDatabaseTool(String hostIP, String hostPort, String userName, String password, String databaseName, DbSSHDomain dbSSHDomain) throws Exception {
        if(!StringUtil.isNotEmpty(dbSSHDomain.getSavePath())){
            throw new Exception("file path is null");
        }
        // 创建文件保存的路径 不管存不存在都创建一下
        File saveFile = new File(dbSSHDomain.getSavePath());

        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
            saveFile.setWritable(true);
        }

        String fileName = "dbbackup"+new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new Date())+ databaseName.replaceAll(" ", "@") + ".sql.bak";

        String COMMAND = "find / -name \"*mysqldump\"";
        List<String> sshResult = new ArrayList<>();
        if(dbSSHDomain.getEnabled()){
            int ret = 0;
            try {
                ret = ShellUtils.shellExecute(dbSSHDomain.getHostIP(), 22, dbSSHDomain.getUserName(), dbSSHDomain.getPassword(), COMMAND, sshResult);
            } catch (Exception e) {
                log.error("服务器连接异常"+ e.getMessage(),e);
                throw new Exception("服务器连接异常");
            }
            if (ret == 0) {
                log.info("====sshResult:"+sshResult.toString());
            }
        }
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            if(dbSSHDomain.getEnabled()&& (sshResult.size() > 0 )){
                stringBuilder.append(sshResult.get(0));
            }else{
                stringBuilder.append("mysqldump");
            }
            stringBuilder.append(" --opt")
                    .append(" -h").append(hostIP)
                    .append(" -p").append(hostPort)
                    .append(" --user=").append(userName)
                    .append(" --password=").append(password)
                    .append(" --result-file=").append(dbSSHDomain.getSavePath() + fileName)
                    .append(" --default-character-set=utf8 ")
                    .append(databaseName);

            log.info("====执行命令:" + stringBuilder.toString());
            if(dbSSHDomain.getEnabled()){
                String saveDir = "mkdir -p " + dbSSHDomain.getSavePath();
                try {
                    ShellUtils.shellExecute(dbSSHDomain.getHostIP(), 22, dbSSHDomain.getUserName(), dbSSHDomain.getPassword(), saveDir, sshResult);
                } catch (Exception e) {
                    log.error("服务器连接异常"+ e.getMessage(),e);
                    throw new Exception("服务器连接异常"+e.getMessage());
                }

                int ret = 0;
                try {
                    ret = ShellUtils.shellExecute(dbSSHDomain.getHostIP(), 22, dbSSHDomain.getUserName(), dbSSHDomain.getPassword(), stringBuilder.toString(), sshResult);
                } catch (Exception e) {
                    log.error("服务器连接异常"+ e.getMessage(),e);
                    throw new Exception("服务器连接异常"+e.getMessage());
                }
                if (ret == 0) {
                    log.info("====sshResult:"+sshResult.toString());
                }

                return fileName;

            } else{
                printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(dbSSHDomain.getSavePath() + fileName), "utf8"));
                Process process = Runtime.getRuntime().exec(stringBuilder.toString());
                InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
                bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    printWriter.println(line);
                }
                printWriter.flush();

                if (process.waitFor() == 0) {//
                    return fileName;
                }
            }
        } catch (Exception e) {
            log.error("数据库备份执行异常"+e.getMessage(),e);
            throw new Exception("数据库备份执行异常，"+e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                log.warn(e.getMessage(),e);
            }
        }
        return "";
    }
}
