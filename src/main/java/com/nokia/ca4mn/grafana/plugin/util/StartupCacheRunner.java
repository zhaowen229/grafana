package com.nokia.ca4mn.grafana.plugin.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.nokia.ca4mn.grafana.plugin.pojo.Targets;
import com.nokia.ca4mn.grafana.plugin.service.GrafanaService;

@Component
public class StartupCacheRunner implements CommandLineRunner {

    private static Logger logger = LogManager.getLogger(StartupCacheRunner.class);

    @Autowired
    private GrafanaService grafanaService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private Targets targets;

    @Override
    @SuppressWarnings("unchecked")
    public void run(String... args) throws Exception {
        logger.info("program cache start...");
        Cache cache = cacheManager.getCache("queryHive");
        /*
         * 原默认获取cache的方式由于每次启动遍历数据库字段，导致启动太慢，所以，改用“尽量”从文件中读取所有字段
         */
        File file = new File(FileToMapUtils.filePath);
        // 由于表结构不会变，所以：文件若存在，则不执行，节约启动时间
        if (!file.exists()) {
            grafanaService.listAllTables();
            List<String> allTables = cache.get("listAllTables", new ArrayList<String>().getClass());
            for (String table : allTables) {
                targets.setTarget(table);
                grafanaService.describeTable(targets);
                grafanaService.describeTableForFilter(targets);
            }
            Map<String, List<String>> map = (Map<String, List<String>>) cache.getNativeCache();
            FileToMapUtils.mapToFile(map, file, "");
        }
        FileToMapUtils.fileToMap().forEach((k, v) -> {
            cache.put(k, v);
        });
        logger.info("program cache end.");
        // Object o = cache.getNativeCache();
    }
}
