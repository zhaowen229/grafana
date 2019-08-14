package com.nokia.ca4mn.grafana.plugin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.nokia.ca4mn.grafana.plugin.pojo.Targets;

@Repository
public class GrafanaDaoImpl implements GrafanaDao {
    private static final Logger logger = LoggerFactory.getLogger(GrafanaDaoImpl.class);

    @Autowired
    @Qualifier("hiveJdbcTemplate")
    private JdbcTemplate hiveJdbcTemplate;

    @Override
    public List<String> listAllTables() throws SQLException {
        String sql = "show tables";
        logger.info("Running: " + sql);
        return hiveJdbcTemplate.queryForList(sql, String.class);
    }

    /**
     * @param targets.getTarget()
     *            is tableName
     */
    @Override
    public List<String> describeTable(Targets targets) throws SQLException {
        String sql = "describe " + targets.getTarget();
        logger.info("Running: " + sql);
        List<String> aList = hiveJdbcTemplate.query(sql, new ResultSetExtractor<List<String>>() {
            @Override
            public List<String> extractData(ResultSet res) throws SQLException, DataAccessException {
                List<String> list = new ArrayList<String>();
                // one line by one line
                while (res != null && res.next()) {
                    if ("bigint".equals(res.getString(2)) || "int".equals(res.getString(2))) {// col_name//data_type
                        list.add(res.getString(1).toString());
                    }
                }
                return list;
            }
        });
        return aList;
    }

    @Override
    public List<String> describeTableForFilter(Targets targets) throws SQLException {
        String sql = "describe " + targets.getTarget();
        logger.info("Running: " + sql);
        List<String> aList = hiveJdbcTemplate.query(sql, new ResultSetExtractor<List<String>>() {
            @Override
            public List<String> extractData(ResultSet res) throws SQLException, DataAccessException {
                List<String> list = new ArrayList<String>();
                // one line by one line
                int a = 0;
                int b = 0;
                int cal = 0;
                Boolean flagA = true;
                Boolean flagB = true;
                while (res != null && res.next()) {
                    // list.subList(list., toIndex)

                    if (flagA && res.getString(1).equals("endtime")) {
                        a = cal + 1;
                        flagA = false;
                    }
                    if (flagB && res.getString(1).startsWith("vs_")) {
                        b = cal;
                        flagB = false;
                    }
                    cal += 1;
                    list.add(res.getString(1).toString());
                }

                return list.subList(a, b);
            }
        });
        return aList;
    }

    @Override
    public List<List<Double>> selectFromTable(Map<String, String> fieldsMap) throws SQLException {
        String sql = "select " + fieldsMap.get("columnName") + ",starttime from " + fieldsMap.get("tableName")
                + " where " + fieldsMap.get("betweenTime") + fieldsMap.get("filterStr");

        // String sql3 = "select " + fieldsMap.get("columnName") + ",starttime from
        // (select " + fieldsMap.get("columnName")
        // + ",starttime,row_number() over (distribute by starttime sort by " +
        // fieldsMap.get("columnName")
        // + ") as rn from " + fieldsMap.get("tableName") + " where " +
        // fieldsMap.get("betweenTime")
        // + fieldsMap.get("filterStr") + ") t where t.rn=1";

        logger.info("Running: " + sql.replace("  ", " "));
        List<List<Double>> aList = hiveJdbcTemplate.query(sql, new ResultSetExtractor<List<List<Double>>>() {
            @Override
            public List<List<Double>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<List<Double>> datapoints = new ArrayList<List<Double>>();
                int count = rs.getMetaData().getColumnCount();
                // one line by one line
                while (rs.next()) {
                    List<Double> childList = new ArrayList<Double>();
                    for (int i = 1; i <= count; i++) {
                        childList.add(rs.getDouble(i));
                    }
                    datapoints.add(childList);
                }
                return datapoints;
            }
        });
        return aList;

        /*
         * 2）或者先拼接，再转换,麻烦，但是程序运行快
         *
         * //pojo:startTime(Long) value(getDouble)//return list<POJO> List<String> list
         * = new ArrayList<String>(); int count = res.getMetaData().getColumnCount();
         * String str = null; while (res.next()) { str = ""; for (int i = 1; i < count;
         * i++) { //getDouble有什么用么？？？？不用getString？ str += "[" + res.getDouble(i) + ",";
         * } str += res.getDouble(count)+"]"; logger.info(str);
         * 
         * list.add(str);
         */
    }
}
