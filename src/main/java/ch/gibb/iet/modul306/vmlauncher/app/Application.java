package ch.gibb.iet.modul306.vmlauncher.app;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ch.gibb.iet.modul306.vmlauncher.config.BeanConfig;
import ch.gibb.iet.modul306.vmlauncher.controller.BootController;

public class Application {
	private static final Logger LOGGER = LogManager.getLogger(Application.class);

	private static ApplicationContext context;

	public static void main(String[] args) {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Starting application..");
		LOGGER.info("--------------------------------------------");

		context = new AnnotationConfigApplicationContext(BeanConfig.class);
		BootController main = context.getBean(BootController.class);
		main.startApplication(args);

		LOGGER.info("--------------------------------------------");
		LOGGER.info("Application started successfully..");
		LOGGER.info("--------------------------------------------");
	}
}