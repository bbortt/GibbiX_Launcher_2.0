package ch.gibb.iet.modul306.vmlauncher.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class StartController extends AbstractController {
	private static final Logger LOGGER = LogManager.getLogger(StartController.class);

	@Value("${application.modules.backup}")
	private boolean enableBackupModel;
	private static BackupController backupModul;

	@Value("${application.modules.design}")
	private boolean enableDesignModel;
	private static DesignController designModul;

	@Value("${application.modules.launcher}")
	private boolean enableLauncherModel;
	private static LauncherController launcherModul;

	@Value("${application.modules.session}")
	private boolean enableSessionModel;
	private static SessionController sessionModul;

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
			backupModul = new BackupController();
			LOGGER.info("Enabled modul " + backupModul.getClass().getSimpleName());
		}

		if (enableDesignModel) {
			designModul = new DesignController();
			LOGGER.info("Enabled modul " + designModul.getClass().getSimpleName());
		}

		if (enableLauncherModel) {
			launcherModul = new LauncherController();
			LOGGER.info("Enabled modul " + launcherModul.getClass().getSimpleName());
		}

		if (enableSessionModel) {
			sessionModul = new SessionController();
			LOGGER.info("Enabled modul " + sessionModul.getClass().getSimpleName());
		}
	}
}