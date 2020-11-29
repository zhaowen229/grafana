package com.grafana.plugin.util;

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

import com.grafana.plugin.pojo.Targets;
import com.grafana.plugin.service.GrafanaService;

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

		File file = new File(FileToMapUtils.filePath);
		// 第一次 启动时文件还未创建 此时 执行查询方法 对数据进行缓存，并把结果存到文件中
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

		// 第二次 重启程序时，文件已经被创建，并保存了查询结果, 在程序启动时执行循环把文件中的数据放到缓存中，
		// 这样在页面上进行查询操作，执行具体方法时，不需要再重新去表中去查，而是直接取缓存中的数据
		FileToMapUtils.fileToMap().forEach((k, v) -> {
			cache.put(k, v);
		});
		logger.info("program cache end.");
		// Object o = cache.getNativeCache();
	}
}
