package com.nokia.ca4mn.grafana.plugin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileToMapUtils {
	private static Logger logger = LoggerFactory.getLogger(StartupCacheRunner.class);

	private static String newLine = System.getProperty("line.separator");
	private static String separ = "=";
	// static String basePath = System.getProperty("user.dir");
	public static String filePath = "./src/main/resources/DBFieldsMap.cfg";// 文件相对路径

	/**
	 * 将map写入到file文件中。默认map（String A,String A')file中以A=A'来表示，map中每个键值对显示一行
	 * 
	 * @throws IOException
	 */
	public static File mapToFile(Map<String, List<String>> map, File file, String separ) throws IOException {
		StringBuffer buffer = new StringBuffer();
		FileWriter writer = new FileWriter(file, false);
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			buffer.append(key + "=" + value).append(newLine);
		}
		writer.write(buffer.toString());
		writer.close();
		return file;

	}

	/**
	 * 将文件转换成map存储
	 * 
	 * @return
	 */
	public static Map<String, List<String>> fileToMap() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		File file = new File(filePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				// logger.info("line " + line + ": " + tempString);
				if (!tempString.startsWith("#")) {
					String[] strArray = tempString.split(separ);
					map.put(strArray[0],
							Arrays.asList(strArray[1].replace("[", "").replace("]", "").replace(" ", "").split(",")));
				}
				// line++;
			}
			reader.close();
		} catch (IOException e) {
			logger.info("file to map error");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					logger.info("file to map error");
				}
			}
		}
		return map;
	}
}
