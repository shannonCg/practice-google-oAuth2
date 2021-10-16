package com.syntrontech.oAuth2;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/*
 * 在spring boot中,此為serve啟動的主程式,會執行在此package下的所有程式(可以看成一個package內為一個專案)
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@PropertySource(value="${spring.config.location:classpath:application.yml}")
public class Application {

	public static void main(String[] args) {
		new SpringApplicationBuilder()
		.sources(Application.class)
		.run(args);
	}

}
