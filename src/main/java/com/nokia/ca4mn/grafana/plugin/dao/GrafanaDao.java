package com.nokia.ca4mn.grafana.plugin.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.nokia.ca4mn.grafana.plugin.pojo.Targets;

@Component
public interface GrafanaDao {

    public List<String> listAllTables() throws SQLException;

    public List<String> describeTable(Targets targets) throws SQLException;

    public List<String> describeTableForFilter(Targets targets) throws SQLException;

    public List<List<Double>> selectFromTable(Map<String, String> fieldsMap) throws SQLException;

}
