package com.example.demopugspring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import de.neuland.pug4j.PugConfiguration;
import de.neuland.pug4j.spring.template.SpringTemplateLoader;
import de.neuland.pug4j.spring.view.PugViewResolver;
import de.neuland.pug4j.template.TemplateLoader;

@Configuration
public class PugConfig {
	private static boolean ISWINDOWS = false;

	@Bean
	public TemplateLoader templateLoader() {
		// JmsTemplateLoader templateLoader = new JmsTemplateLoader();
		SpringTemplateLoader templateLoader = new SpringTemplateLoader();
		// templateLoader.setTemplateLoaderPath(Paths.get("./src/main/resources/templates/").toAbsolutePath().toString());
		templateLoader.setTemplateLoaderPath("classpath:/templates/");
		// templateLoader.setSearchFile(ISWINDOWS);
		templateLoader.setEncoding("UTF-8");
		templateLoader.setSuffix(".pug");
		return templateLoader;
	}

	@Bean
	public PugConfiguration pugConfiguration() {
		PugConfiguration configuration = new PugConfiguration();
		configuration.setCaching(false);
		configuration.setTemplateLoader(templateLoader());
		configuration.setPrettyPrint(true);
		return configuration;
	}

	@Bean
	public ViewResolver viewResolver() {
		PugViewResolver viewResolver = new PugViewResolver();
		viewResolver.setConfiguration(pugConfiguration());
		return viewResolver;
	}
}
