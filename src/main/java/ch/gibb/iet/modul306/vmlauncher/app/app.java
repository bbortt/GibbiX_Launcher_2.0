package ch.gibb.iet.modul306.vmlauncher.app;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ch.gibb.iet.modul306.vmlauncher.config.BeanConfig;
import ch.gibb.iet.modul306.vmlauncher.controller.BootController;
import javafx.application.Application;
import javafx.stage.Stage;

@SuppressWarnings({ "restriction" })
public class app extends Application {
	private static final Logger LOGGER = Logger.getLogger(app.class);

	private static ApplicationContext context;

	public static void main(String[] args) {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Starting application..");
		LOGGER.info("--------------------------------------------");

		context = new AnnotationConfigApplicationContext(BeanConfig.class);

		launch(args);

		LOGGER.info("--------------------------------------------");
		LOGGER.info("Application exited.");
		LOGGER.info("--------------------------------------------");
	}

	@Override
	public void start(Stage mainStage) {
		context.getBean(BootController.class).startApplication(mainStage);
	}
}