package ch.gibb.iet.modul306.vmlauncher.app;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ch.gibb.iet.modul306.vmlauncher.config.BeanConfig;
import ch.gibb.iet.modul306.vmlauncher.controller.BootController;
import javafx.application.Application;
import javafx.stage.Stage;

public class app extends Application {
	private static final Logger LOGGER = Logger.getLogger(app.class);

	private static ApplicationContext context;

	public static void main(String[] args) {
		// BasicConfigurator.configure();

		LOGGER.info("--------------------------------------------");
		LOGGER.info("Starting application..");
		LOGGER.info("--------------------------------------------");

		context = new AnnotationConfigApplicationContext(BeanConfig.class);
		launch(args);
	}

	@Override
	public void start(Stage mainStage) {
		context.getBean(BootController.class).startApplication(mainStage);
	}

	@Override
	public void stop() {
		printGoodBye();

		try {
			super.stop();
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		}

		System.exit(0);
	}

	private void printGoodBye() {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Application exited.");
		LOGGER.info("--------------------------------------------");
	}

	public static String getCurrentSystemTime() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(System.currentTimeMillis()));
	}
}