package ch.gibb.iet.modul306.vmlauncher.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.app.app;
import ch.gibb.iet.modul306.vmlauncher.model.MachineModel;
import ch.gibb.iet.modul306.vmlauncher.model.SettingsModel;
import ch.gibb.iet.modul306.vmlauncher.view.ApplicationView;
import javafx.stage.Stage;

public class BootController extends AbstractController {
	private static final Logger LOGGER = Logger.getLogger(BootController.class);

	private boolean isLauncherModulEnabled;
	private LauncherController launcherController;
	private MachineModel machineModel;

	private boolean isBackupModulEnabled;
	private BackupController backupController;

	private boolean isDesignModulEnabled;
	private DesignController designController;

	private boolean isSessionModulEnabled;
	private SessionController sessionController;

	private SettingsController settingsController;
	private SettingsModel settingsModel;

	private Stage mainStage;

	public LauncherController getLauncherModul() {
		return launcherController;
	}

	public MachineModel getMachineModel() {
		return this.machineModel;
	}

	public BackupController getBackupModul() {
		return this.backupController;
	}

	public DesignController getDesignModul() {
		return this.designController;
	}

	public SessionController getSessionModul() {
		return this.sessionController;
	}

	public SettingsController getSettingsModul() {
		return this.settingsController;
	}

	public TreeMap<String, Object> getApplicationSettings() {
		if (settingsModel == null) {
			settingsModel = new SettingsModel();
		}

		return this.settingsModel.getAllProperties();
	}

	public Stage getMainStage() {
		return this.mainStage;
	}

	public BootController(Stage mainStage) {
		super(mainStage);
	}

	@Deprecated
	public BootController(Stage mainStage, BootController self) {
		super(mainStage, self);
	}

	public void startPortableAppManager() throws IOException {
		File pstartFile = new File(getApplicationSettings().get("gibbix.path.default").toString()
				+ getApplicationSettings().get("application.external.apps.path").toString());

		if (!pstartFile.exists()) {
			throw new FileNotFoundException(
					"Either your GibbiX-Path or PStart.exe-Installation are incorrect. Please check your settings.");
		}

		Desktop.getDesktop().open(pstartFile);
	}

	public void startApplication() {
		loadModules();
		loadView();

		machineModel = new MachineModel();

		LOGGER.info("--------------------------------------------");
		LOGGER.info("Startup finished at " + app.getCurrentSystemTime() + ". Application ready to use.");
		LOGGER.info("Good luck @ GIBB :)");
		LOGGER.info("(c) 2016 by Team VMLauncher 2.0");
		LOGGER.info("--------------------------------------------");
	}

	private void loadModules() {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Enabling modules");

		if (isLauncherModulEnabled) {
			LOGGER.debug("Machine-modul is enabled. Creating..");
			launcherController = new LauncherController(mainStage, this);
		}

		if (isBackupModulEnabled) {
			LOGGER.debug("Backup-modul is enabled. Creating..");
			backupController = new BackupController(mainStage, this);
		}

		if (isDesignModulEnabled) {
			LOGGER.debug("Design-modul is enabled. Creating..");
			designController = new DesignController(mainStage, this);
		}

		if (isSessionModulEnabled) {
			LOGGER.debug("Session-modul is enabled. Creating");
			sessionController = new SessionController(mainStage, this);
		}

		LOGGER.debug("Creating settings-modul..");
		this.settingsController = new SettingsController(mainStage, this);
	}

	public void loadView() {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Loading view..");

		new ApplicationView().display();
	}
}