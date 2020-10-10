package com.grafana.plugin.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hive.service.cli.HiveSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.grafana.plugin.dao.GrafanaDao;
import com.grafana.plugin.pojo.Data;
import com.grafana.plugin.pojo.Response;
import com.grafana.plugin.pojo.Targets;

@Service
@CacheConfig(cacheNames = "queryHive")
public class GrafanaServiceImpl implements GrafanaService {

    private static final Logger logger = LoggerFactory.getLogger(GrafanaServiceImpl.class);

    @Autowired
    private GrafanaDao grafanaDao;

    @Autowired
    private Response response;

    @Override
    @Cacheable(key = "#root.methodName")
    public List<String> listAllTables() throws SQLException {
        return grafanaDao.listAllTables();
    }

    @Override
    @Cacheable(key = "#targets.target")
    public List<String> describeTable(Targets targets) throws SQLException {
        List<String> res = null;
        if (targets.getTarget() != null && !"".equals(targets.getTarget())) {
            try {
                res = grafanaDao.describeTable(targets);

            } catch (HiveSQLException e) {
                logger.warn("describe table: table is unexist");
            }
        } else {
            logger.warn("describe table: table name is null");
        }
        return res;
    }

    @Override
    @Cacheable(key = "#targets.target+'_filter'")
    public List<String> describeTableForFilter(Targets targets) throws SQLException {
        List<String> res = null;
        if (targets.getTarget() != null && !"".equals(targets.getTarget())) {
            try {
                res = grafanaDao.describeTableForFilter(targets);
            } catch (HiveSQLException e) {
                logger.warn("describe table: table is unexist");
            }
        } else {
            logger.warn("describe table ForFilter: table name is null");
        }
        return res;
    }

    @Override
    public List<Response> selectFromTable(Data data) throws SQLException {
        Map<String, String> fieldsMap = getFieldsMap(data);
        /*
         * 1）先add到子list中，再add到大list中，但是程序运行慢
         */
        List<Response> responseJsons = new ArrayList<Response>();
        try {
            List<List<Double>> datapoints = grafanaDao.selectFromTable(fieldsMap);
            // 目前假设有且只有一行条件输入，所add一条
            response.setTarget(fieldsMap.get("columnName"));
            response.setDatapoints(datapoints);
            responseJsons.add(response);
        } catch (Exception e) {
            logger.warn("select all: table is unexist");
        } finally {
            logger.info("==END==");
        }
        return responseJsons;
    }

    private Map<String, String> getFieldsMap(Data requestData) {
        Map<String, String> map = new HashMap<String, String>();
        // Long.doubleValue()//测试时候小心ISOTime时差需要按照加8h来看
        Long fromTimestamp = requestData.getRange().getFrom().getTime();
        Long toTimestamp = requestData.getRange().getTo().getTime();

        String betweenTime = " starttime between " + fromTimestamp + " and " + toTimestamp + " ";

        // 目前假设有且只有一行条件输入，所以get(0)
        String columnName = requestData.getTargets().get(0).getField();
        String tableName = requestData.getTargets().get(0).getTable();
        String filterStr = requestData.getTargets().get(0).getFilter();
        filterStr = filterStr.isEmpty() ? "" : " and " + filterStr;
        String aggregate = requestData.getTargets().get(0).getAggregate();

        if (aggregate != null && !aggregate.equals("")) {
            filterStr += " group by starttime ";
            columnName = aggregate + "(" + columnName + ")";
        }

        map.put("betweenTime", betweenTime);
        map.put("columnName", columnName);
        map.put("tableName", tableName);
        map.put("filterStr", filterStr);
        return map;
    }

}
