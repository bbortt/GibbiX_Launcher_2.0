package ch.gibb.iet.modul306.vmlauncher.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("unused")
public class BootController extends AbstractController {
	private static final Logger LOGGER = LogManager.getLogger(BootController.class);

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

	public BootController() {
		super();
	}

	public void startApplication(String[] args) {
		printStartUpInfo();
		loadModules();
	}

	private void printStartUpInfo() {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Application starting at "
				+ new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(System.currentTimeMillis())));

		LOGGER.info("Group Id: " + bootConfiguration.getGroupId());
		LOGGER.info("Artifact Id: " + bootConfiguration.getArtifactId());
		LOGGER.info("Build name: " + bootConfiguration.getBuildName());
		LOGGER.info("Build version: " + bootConfiguration.getBuildVersion());
	}

	private void loadModules() {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Loading modules..");

		if (enableBackupModel) {
			LOGGER.debug("Enable Backup modul..");
			backupModul = new BackupController();
		}

		if (enableDesignModel) {
			LOGGER.debug("Enable Design modul..");
			designModul = new DesignController();
		}

		if (enableLauncherModel) {
			LOGGER.debug("Enable Machine Configuration modul..");
			launcherModul = new LauncherController();
		}

		if (enableSessionModel) {
			LOGGER.debug("Enable Session Administration modul..");
			sessionModul = new SessionController();
		}
	}
}