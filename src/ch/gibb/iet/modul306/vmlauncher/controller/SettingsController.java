package ch.gibb.iet.modul306.vmlauncher.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.model.SettingsModel;
import ch.gibb.iet.modul306.vmlauncher.view.SettingsView;
import javafx.stage.Stage;

public class SettingsController extends AbstractController {
	private static final Logger LOGGER = LogManager.getLogger(SettingsController.class);

	private SettingsModel settingsModul;
	private SettingsView settingsView;

	public SettingsController(Stage mainStage, BootController bootController) throws FileNotFoundException {
		super(mainStage, bootController);

		settingsModul = new SettingsModel();
		settingsView = new SettingsView(mainStage, this);
	}

	@Override
	public void loadView() {
		settingsView.display();

		try {
			settingsView.setProperties(settingsModul.getAllProperties());
		} catch (URISyntaxException | IOException e) {
			settingsView.setPropertiesNotFound();
			LOGGER.error(e.getLocalizedMessage());
		}
	}
}