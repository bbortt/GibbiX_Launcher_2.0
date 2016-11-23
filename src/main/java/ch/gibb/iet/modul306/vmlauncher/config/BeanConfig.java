package ch.gibb.iet.modul306.vmlauncher.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ch.gibb.iet.modul306.vmlauncher.controller.BackupController;
import ch.gibb.iet.modul306.vmlauncher.controller.DesignController;
import ch.gibb.iet.modul306.vmlauncher.controller.LauncherController;
import ch.gibb.iet.modul306.vmlauncher.controller.SessionController;

/**
 * Spring configuration. Provides beans.
 * 
 * @author timon.borter
 * @version 0.1
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "ch.gibb.iet.modul306.vmlauncher.*")
@PropertySource("classpath:application.properties")
public class BeanConfig {
	public LauncherController launcherModul() {
		return new LauncherController();
	}

	public BackupController backupModul() {
		return new BackupController();
	}

	public DesignController designModul() {
		return new DesignController();
	}

	public SessionController sessionModul() {
		return new SessionController();
	}
}