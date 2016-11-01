package ch.gibb.iet.modul306.vmlauncher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.gibb.iet.modul306.vmlauncher.controller.StartController;

/**
 * Spring configuration. Provides beans.
 * 
 * @author timon.borter
 * @version 0.1
 */
@Configuration
public class BeanConfig {
	@Bean
	public StartController StartController() {
		return new StartController();
	}
}
