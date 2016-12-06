package ch.gibb.iet.modul306.vmlauncher.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ch.gibb.iet.modul306.vmlauncher.controller.BootController;
import javafx.application.Application;
import javafx.stage.Stage;

public class app extends Application {
	private static final Logger LOGGER = Logger.getLogger(app.class);

	public static void main(String[] args) {
		try {
			Properties props = new Properties();
			props.load(new FileInputStream("conf/log4j.properties"));
			PropertyConfigurator.configure(props);
		} catch (IOException e) {
			LOGGER.warn(e.getLocalizedMessage());
			BasicConfigurator.configure();
		}

		LOGGER.info("--------------------------------------------");
		LOGGER.info("Application starting at " + getCurrentSystemTime());
		LOGGER.info("--------------------------------------------");

		launch(args);
	}

	@Override
	public void start(Stage mainStage) {
		new BootController(mainStage).startApplication();
	}

	@Override
	public void stop() {
		printGoodBye();

		try {
			super.stop();
		} catch (Exception e) {
			LOGGER.fatal(e.getLocalizedMessage());
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