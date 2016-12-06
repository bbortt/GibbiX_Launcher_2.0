package ch.gibb.iet.modul306.vmlauncher.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;

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

	public LauncherController getLauncherModul() {
		return launcherController;
	}

	public MachineModel getMachineModel() {
		if (this.machineModel == null) {
			this.machineModel = new MachineModel(this);
		}

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

	public SettingsModel getApplicationSettings() throws URISyntaxException, IOException {
		if (settingsModel == null) {
			settingsModel = new SettingsModel().readRuntimeConfiguration();
		}

		return this.settingsModel;
	}

	public Stage getMainStage() {
		return this.mainStage;
	}

	@Override
	public BootController getBootController() {
		return this;
	}

	public BootController(Stage mainStage) {
		super(mainStage);
	}

	@Deprecated
	public BootController(Stage mainStage, BootController self) {
		super(mainStage, self);
	}

	public void startPortableAppManager() throws IOException, URISyntaxException {
		File pstartFile = new File(getApplicationSettings().getProperty("gibbix.path.default").toString()
				+ getApplicationSettings().getProperty("application.external.apps.path").toString());

		if (!pstartFile.exists()) {
			throw new FileNotFoundException(
					"Either your GibbiX-Path or PStart.exe-Installation are incorrect. Please check your settings.");
		}

		Desktop.getDesktop().open(pstartFile);
	}

	public void startApplication() {
		try {
			readLocalSettings();
		} catch (URISyntaxException | IOException e) {
			LOGGER.fatal(e.getLocalizedMessage());
			System.exit(1);
		}

		loadModules();
		loadView();

		LOGGER.info("--------------------------------------------");
		LOGGER.info("Startup finished at " + app.getCurrentSystemTime() + ". Application ready to use.");
		LOGGER.info("Good luck @ GIBB :)");
		LOGGER.info("(c) 2016 by Team VMLauncher 2.0");
		LOGGER.info("--------------------------------------------");
	}

	private void readLocalSettings() throws URISyntaxException, IOException {
		TreeMap<String, Object> settings = getApplicationSettings().getAllProperties();

		isLauncherModulEnabled = Boolean.valueOf(settings.get("application.modules.launcher").toString());
		isBackupModulEnabled = Boolean.valueOf(settings.get("application.modules.backup").toString());
		isDesignModulEnabled = Boolean.valueOf(settings.get("application.modules.design").toString());
		isSessionModulEnabled = Boolean.valueOf(settings.get("application.modules.session").toString());
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

		try {
			if (isSessionModulEnabled) {
				LOGGER.debug("Session-modul is enabled. Creating");
				sessionController = new SessionController(mainStage, this);
			}
		} catch (JAXBException e) {
			LOGGER.fatal(e.getLocalizedMessage());
		}

		try {
			LOGGER.debug("Creating settings-modul..");
			this.settingsController = new SettingsController(mainStage, this);
		} catch (FileNotFoundException e) {
			LOGGER.fatal(e.getLocalizedMessage());
		}
	}

	public void loadView() {
		LOGGER.info("--------------------------------------------");
		LOGGER.info("Loading view..");

		new ApplicationView(mainStage, this).display();
	}
}