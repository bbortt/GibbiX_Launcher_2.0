package ch.gibb.iet.modul306.vmlauncher.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ch.gibb.iet.modul306.vmlauncher.controller.BootController;
import ch.gibb.iet.modul306.vmlauncher.controller.ConfigurationController;

/**
 * Spring configuration. Provides beans.
 * 
 * @author timon.borter
 * @version 0.1
 */
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class BeanConfig {
	@Bean
	public BootController bootController() {
		return new BootController();
	}

	@Bean
	public ConfigurationController bootConfiguration() {
		return new ConfigurationController();
	}
}