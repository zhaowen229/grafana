package com.nokia.ca4mn.grafana.plugin.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用 JdbcTemplate 操作 Hive
 */
@RestController
@RequestMapping("/hive2")
public class HiveJdbcTemplateController {

    private static final Logger logger = LogManager.getLogger(HiveJdbcTemplateController.class);

    @Autowired
    @Qualifier("hiveJdbcTemplate")
    private JdbcTemplate hiveJdbcTemplate;

    /**
     * 创建新表
     */
    @PostMapping("/table/create")
    public String createTable() {
        StringBuffer sql = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        // sql.append("weather_forecast");
        // sql.append("(id BIGINT, area STRING, air_temperature INT, starttime BIGINT,
        // remark STRING)");
        sql.append("sys_4g_pcmd_mme_proc_15min_test");
        sql.append("(starttime BIGINT,endtime BIGINT,mme STRING,cell4g STRING,"
                + "procedureid BIGINT,procedurecfc BIGINT,procedurecfcq BIGINT,procedurescfcq BIGINT,"
                + "vs_mme_dim_breakdown_pcmd BIGINT,vs_mme_dim_breakdown_highprio_pcmd BIGINT,"
                + "remark STRING, pktime STRING, pkdim STRING)");
        sql.append("ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' "); // 定义分隔符
        sql.append("STORED AS TEXTFILE"); // 作为文本存储

        logger.info("Running: " + sql);
        String result = "Create table successfully...";
        try {
            hiveJdbcTemplate.execute(sql.toString());
        } catch (DataAccessException dae) {
            result = "Create table encounter an error: " + dae.getMessage();
            logger.error(result);
        }
        return result;

    }

    /**
     * 将Hive服务器本地文档中的数据加载到Hive表中
     */
    @PostMapping("/table/load")
    public String loadIntoTable() {
        // String filepath = "/tmp/grafana-plugin-data.txt";
        String filepath = "/tmp/grafana-plugin-data-mme_proc_15min.txt";
        // String sql = "load data local inpath '" + filepath + "' into table
        // weather_forecast";
        String sql = "load data local inpath '" + filepath + "' into table sys_4g_pcmd_mme_proc_15min_test";
        String result = "Load data into table successfully...";
        try {
            hiveJdbcTemplate.execute(sql);
        } catch (DataAccessException dae) {
            result = "Load data into table encounter an error: " + dae.getMessage();
            logger.error(result);
        }
        return result;
    }

    /**
     * 向Hive表中添加数据
     */
    @PutMapping("/table/insert")
    public String insertIntoTable() {
        // String sql = "INSERT INTO TABLE
        // weather_forecast(id,area,air_temperature,starttime,remark)
        // VALUES(888,'shenyang',2,1544694796222,'2018-12-13 17:52:01')";
        String sql = "INSERT INTO TABLE  weather_forecast(id,area,air_temperature,starttime,remark) VALUES(7,'hebei',1,1544689761099,'2018-12-13 16:29:01')";
        String result = "Insert into table successfully...";
        try {
            hiveJdbcTemplate.execute(sql);
        } catch (DataAccessException dae) {
            result = "Insert into table encounter an error: " + dae.getMessage();
            logger.error(result);
        }
        return result;
    }

    /**
     * 删除表
     */
    @DeleteMapping("/table/delete")
    public String delete(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        String result = "Drop table successfully...";
        logger.info("Running: " + sql);
        try {
            hiveJdbcTemplate.execute(sql);
        } catch (DataAccessException dae) {
            result = "Drop table encounter an error: " + dae.getMessage();
            logger.error(result);
        }
        return result;
    }
}
