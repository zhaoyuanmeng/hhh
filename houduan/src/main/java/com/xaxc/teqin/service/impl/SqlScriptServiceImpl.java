package com.xaxc.teqin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class SqlScriptServiceImpl {

    private final static String[] disAllowedOperations = {"delete ", "drop ", "create ", "truncate ", "update ", "alter "};
    @Resource
    DataSource dataSource;



    /**
     * 执行脚本
     *
     * @param path
     */
    public void executeSqlScript(String path) {
        log.info("执行脚本文件：" + path);
        validatorFile(path);
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new FileSystemResource(path));
        //设置当出错是是否继续，默认是false即不继续
        resourceDatabasePopulator.setContinueOnError(false);
        //设置是否忽略删除错误，默认是false即不忽略
        resourceDatabasePopulator.setIgnoreFailedDrops(false);
        resourceDatabasePopulator.execute(dataSource);
    }

    public void executeViewScript(String view) {
        ClassPathResource resource = new ClassPathResource(view);
        log.info("执行脚本文件：" + resource.getPath());
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(resource);
        //设置当出错是是否继续，默认是false即不继续
        resourceDatabasePopulator.setContinueOnError(false);
        //设置是否忽略删除错误，默认是false即不忽略
        resourceDatabasePopulator.setIgnoreFailedDrops(false);
        resourceDatabasePopulator.execute(dataSource);
    }

    /**
     * 备份表
     *
     * @param tableName
     */
    public void backupTable(String tableName) {
        String newTable = tableName + "_import_bak";
        try {
            dataSource.getConnection().createStatement().execute("CREATE TABLE " + newTable + "  AS TABLE " + tableName + ";");
            log.info("backupTable " + tableName + " to " + newTable);
        } catch (SQLException e) {
            log.error("backupTable " + tableName + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void restoreTable(String tableName) {
        String bakTable = tableName + "_import_bak";
        try {
            dataSource.getConnection().createStatement().execute("CREATE TABLE " + tableName + "  AS TABLE " + bakTable + ";");
            dataSource.getConnection().createStatement().execute("DROP TABLE IF EXISTS " + bakTable + ";");
            log.info("restoreTable " + bakTable + " to " + tableName);
        } catch (SQLException e) {
            log.error("restoreTable " + tableName + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 清空表
     *
     * +
     * @param tableName
     */
    public void truncateTable(String tableName, boolean bakFlag) {
        String bakTableName = tableName + (bakFlag ? "_import_bak" : "");
        try {
            dataSource.getConnection().createStatement().execute("TRUNCATE TABLE " + bakTableName + ";");
            log.info("truncate Table " + bakTableName);
        } catch (Exception e) {
            log.error("truncate Table " + bakTableName + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除表
     *
     * @param tableName
     */
    public void dropTable(String tableName, boolean bakFlag) {
        String bakTableName = tableName + (bakFlag ? "_import_bak" : "");
        try {
            dataSource.getConnection().createStatement().execute("DROP TABLE IF EXISTS " + bakTableName + ";");
            log.info("dropTable table " + bakTableName);
        } catch (Exception e) {
            log.error("dropTable " + bakTableName + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public boolean validator(String sql) {
        String lowerSql = sql.trim().toLowerCase();
        for (String op : disAllowedOperations) {
            if (lowerSql.startsWith(op)) {
                return false;
            }
        }
        return true;
    }

    public String operationValidator(String sql) {
        String lowerSql = sql.trim().toLowerCase();
        for (String op : disAllowedOperations) {
            if (lowerSql.startsWith(op) || lowerSql.contains(op)) {
                return op;
            }
        }
        return null;
    }

    public boolean validatorFile(String sqlFile) {
        boolean result = true;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(sqlFile));
            String line;
            int lineNum = 0;
            while ((line = bufferedReader.readLine()) != null) {
                lineNum++;
                String op = operationValidator(line);
                if (StringUtils.hasText(op)) {
                    throw new RuntimeException("Found [" + op + "] statement at line " + lineNum + ": " + line.trim());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }
}
