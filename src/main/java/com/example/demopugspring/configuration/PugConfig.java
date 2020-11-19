package com.example.demopugspring.configuration;

import java.nio.file.Paths;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import de.neuland.pug4j.PugConfiguration;
import de.neuland.pug4j.spring.template.SpringTemplateLoader;
import de.neuland.pug4j.spring.view.PugViewResolver;
import de.neuland.pug4j.template.TemplateLoader;

@Configuration
public class PugConfig {
	private static final String UTF_8_ENCODING = "UTF-8";
	private static final String PUG_SUFFIX = ".pug";
	private static boolean ISWINDOWS = SystemUtils.IS_OS_WINDOWS;

	@Bean
	public TemplateLoader templateLoader() {

		if (ISWINDOWS) {
			JmsTemplateLoader templateLoaderWindows = new JmsTemplateLoader();
			templateLoaderWindows.setTemplateLoaderPath(Paths.get("./src/main/resources/templates/").toAbsolutePath().toString());
			templateLoaderWindows.setSearchFile(ISWINDOWS);
			templateLoaderWindows.setEncoding(UTF_8_ENCODING);
			templateLoaderWindows.setSuffix(PUG_SUFFIX);
			return templateLoaderWindows;
		} else {
			SpringTemplateLoader templateLoader = new SpringTemplateLoader();
			templateLoader.setTemplateLoaderPath("classpath:/templates/");
			templateLoader.setEncoding(UTF_8_ENCODING);
			templateLoader.setSuffix(PUG_SUFFIX);
			return templateLoader;
		}
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
