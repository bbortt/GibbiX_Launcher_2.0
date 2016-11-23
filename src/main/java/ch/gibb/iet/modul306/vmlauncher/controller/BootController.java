package ch.gibb.iet.modul306.vmlauncher.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import ch.gibb.iet.modul306.vmlauncher.app.app;
import ch.gibb.iet.modul306.vmlauncher.config.ApplicationData;
import ch.gibb.iet.modul306.vmlauncher.view.ApplicationView;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class BootController extends AbstractController {
	private static final Logger LOGGER = Logger.getLogger(BootController.class);

	@Value("${application.modules.launcher}")
	private boolean enableLauncherModel;
	private static LauncherController launcherModul;

	public boolean isLauncherModulEnabled() {
		return enableLauncherModel;
	}

	public LauncherController getLauncherModul() {
		return launcherModul;
	}

	@Value("${application.modules.backup}")
	private boolean enableBackupModel;
	private static BackupController backupModul;

	public boolean isBackupModulEnabled() {
		return enableBackupModel;
	}

	public BackupController getBackupModul() {
		return backupModul;
	}

	@Value("${application.modules.design}")
	private boolean enableDesignModel;
	private static DesignController designModul;

	public boolean isDesignModulEnabled() {
		return enableDesignModel;
	}

	public static DesignController getDesignModul() {
		return designModul;
	}

	@Value("${application.modules.session}")
	private boolean enableSessionModel;
	private static SessionController sessionModul;

	public boolean isSessionModulEnabled() {
		return enableSessionModel;
	}

	public SessionController getSessionModul() {
		return sessionModul;
	}

	@Autowired
	private ApplicationData applicationData;

	private ApplicationView view;

	public void startApplication(Stage mainStage) {
		printStartUpInfo();
		loadModules();
		loadView(mainStage);

		LOGGER.info("--------------------------------------------");
		LOGGER.info("Startup finished at " + app.getCurrentSystemTime() + ". Application ready to use.");
		LOGGER.info("Good luck @ GIBB :)");
		LOGGER.info("(c) 2016 by Team VMLauncher 2.0");
		LOGGER.info("--------------------------------------------");
	}

	private void printStartUpInfo() {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Application starting at " + app.getCurrentSystemTime());

		LOGGER.info("Group Id: " + applicationData.getGroupId());
		LOGGER.info("Artifact Id: " + applicationData.getArtifactId());
		LOGGER.info("Build name: " + applicationData.getBuildName());
		LOGGER.info("Build version: " + applicationData.getBuildVersion());
	}

	private void loadModules() {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Loading modules");

		if (enableLauncherModel) {
			LOGGER.debug("Enable Machine Configuration modul..");
			launcherModul = new LauncherController();
		}

		if (enableBackupModel) {
			LOGGER.debug("Enable Backup modul");
			backupModul = new BackupController();
		}

		if (enableDesignModel) {
			LOGGER.debug("Enable Design modul");
			designModul = new DesignController();
		}

		if (enableSessionModel) {
			LOGGER.debug("Enable Session Administration modul");
			sessionModul = new SessionController();
		}
	}

	@Override
	public void loadView(Stage mainStage) {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Loading view..");

		view = new ApplicationView(mainStage, this);
	}
}