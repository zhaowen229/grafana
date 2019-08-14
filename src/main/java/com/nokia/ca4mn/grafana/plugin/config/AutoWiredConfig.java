package com.nokia.ca4mn.grafana.plugin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nokia.ca4mn.grafana.plugin.pojo.Response;
import com.nokia.ca4mn.grafana.plugin.pojo.Targets;

/**
 * RestTemplate配置 这是一种JavaConfig的容器配置，用于spring容器的bean收集与注册，并通过参数传递的方式实现依赖注入。
 * "@Configuration"注解标注的配置类，都是spring容器配置类，springboot通过"@EnableAutoConfiguration"
 * 注解将所有标注了"@Configuration"注解的配置类，"一股脑儿"全部注入spring容器中。
 */
@Configuration
public class AutoWiredConfig {

	@Bean
	public Response response() {
		return new Response();
	}

	@Bean
	public Targets targets() {
		return new Targets();
	}

}
