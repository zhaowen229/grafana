package com.nokia.ca4mn.grafana.plugin.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nokia.ca4mn.grafana.plugin.pojo.Data;
import com.nokia.ca4mn.grafana.plugin.pojo.Response;
import com.nokia.ca4mn.grafana.plugin.pojo.Targets;

@Service
public interface GrafanaService {
    public List<String> listAllTables() throws SQLException;

    public List<String> describeTable(Targets targets) throws SQLException;

    public List<String> describeTableForFilter(Targets targets) throws SQLException;

    public List<Response> selectFromTable(Data data) throws SQLException;
}
