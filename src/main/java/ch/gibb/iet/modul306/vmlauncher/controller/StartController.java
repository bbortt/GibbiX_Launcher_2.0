package ch.gibb.iet.modul306.vmlauncher.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import ch.gibb.iet.modul306.vmlauncher.model.BackupModel;
import ch.gibb.iet.modul306.vmlauncher.model.DesignModel;
import ch.gibb.iet.modul306.vmlauncher.model.LauncherModel;
import ch.gibb.iet.modul306.vmlauncher.model.SessionModel;

public class StartController extends AbstractController {
	private static final Logger LOGGER = LogManager.getLogger(StartController.class);

	@Value("${application.modules.backup}")
	private boolean enableBackupModel;
	private static BackupModel backupModel;

	@Value("${application.modules.design}")
	private boolean enableDesignModel;
	private static DesignModel designModel;

	@Value("${application.modules.launcher}")
	private boolean enableLauncherModel;
	private static LauncherModel launcherModel;

	@Value("${application.modules.session}")
	private boolean enableSessionModel;
	private static SessionModel sessionModel;

	@Autowired
	private ConfigurationController bootConfiguration;

	public StartController() {
		super();
	}

	public void startApplication(String[] args) {
		printVersionInfo();
		loadModules();
	}

	private void printVersionInfo() {
		LOGGER.info("Group Id: " + bootConfiguration.getGroupId());
		LOGGER.info("Artifact Id: " + bootConfiguration.getArtifactId());
		LOGGER.info("Build name: " + bootConfiguration.getBuildName());
		LOGGER.info("Build version: " + bootConfiguration.getBuildVersion());
	}

	private void loadModules() {
		if (enableBackupModel) {
			backupModel = new BackupModel();
			LOGGER.info("Enabled modul " + backupModel.getClass().getSimpleName());
		}

		if (enableDesignModel) {
			designModel = new DesignModel();
			LOGGER.info("Enabled modul " + designModel.getClass().getSimpleName());
		}

		if (enableLauncherModel) {
			launcherModel = new LauncherModel();
			LOGGER.info("Enabled modul " + launcherModel.getClass().getSimpleName());
		}

		if (enableSessionModel) {
			sessionModel = new SessionModel();
			LOGGER.info("Enabled modul " + sessionModel.getClass().getSimpleName());
		}
	}
}