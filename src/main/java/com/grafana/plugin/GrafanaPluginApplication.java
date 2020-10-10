package com.grafana.plugin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
// @ComponentScan(basePackages = "com.grafana.plugin.*")
public class GrafanaPluginApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrafanaPluginApplication.class, args);
	}
}
