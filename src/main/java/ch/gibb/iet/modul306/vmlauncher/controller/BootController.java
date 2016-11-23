package ch.gibb.iet.modul306.vmlauncher.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.gibb.iet.modul306.vmlauncher.app.app;
import ch.gibb.iet.modul306.vmlauncher.config.ApplicationData;
import ch.gibb.iet.modul306.vmlauncher.view.ApplicationView;
import javafx.stage.Stage;

@Component
public class BootController extends AbstractController {
	private static final Logger LOGGER = Logger.getLogger(BootController.class);

	@Value("${application.modules.launcher}")
	private boolean isLauncherModulEnabled;
	@Autowired
	private static LauncherController launcherModul;

	public LauncherController getLauncherModul() {
		if (isLauncherModulEnabled && launcherModul != null) {
			return launcherModul;
		}
		return null;
	}

	@Value("${application.modules.backup}")
	private boolean isBackupModulEnabled;
	@Autowired
	private static BackupController backupModul;

	public BackupController getBackupModul() {
		if (isBackupModulEnabled && backupModul != null) {
			return backupModul;
		}
		return null;
	}

	@Value("${application.modules.design}")
	private boolean isDesignModulEnabled;
	@Autowired
	private static DesignController designModul;

	public DesignController getDesignModul() {
		if (isDesignModulEnabled && designModul != null) {
			return designModul;
		}
		return null;
	}

	@Value("${application.modules.session}")
	private boolean isSessionModulEnabled;
	@Autowired
	private static SessionController sessionModul;

	public SessionController getSessionModul() {
		if (isSessionModulEnabled && sessionModul != null) {
			return sessionModul;
		}
		return null;
	}

	@Autowired
	private ApplicationData applicationData;

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
		LOGGER.info("Enabling modules");

		if (isLauncherModulEnabled) {
			LOGGER.debug("Machine-modul is enabled");
		}

		if (isBackupModulEnabled) {
			LOGGER.debug("Backup-modul is enabled");
		}

		if (isDesignModulEnabled) {
			LOGGER.debug("Design-modul is enabled");
		}

		if (isSessionModulEnabled) {
			LOGGER.debug("Session-modul is enabled");
		}
	}

	@Override
	public void loadView(Stage mainStage) {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Loading view..");

		new ApplicationView(mainStage, this);
	}
}