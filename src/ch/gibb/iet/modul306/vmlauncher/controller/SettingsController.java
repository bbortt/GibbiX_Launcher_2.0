package ch.gibb.iet.modul306.vmlauncher.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.model.SettingsModel;
import ch.gibb.iet.modul306.vmlauncher.view.SettingsView;
import javafx.stage.Stage;

public class SettingsController extends AbstractController {
	private static final Logger LOGGER = LogManager.getLogger(SettingsController.class);

	private SettingsModel settingsModul;
	private SettingsView settingsView;

	public SettingsController(Stage mainStage, BootController bootController) {
		super(mainStage, bootController);

		settingsModul = new SettingsModel();
		settingsView = new SettingsView();
	}

	@Override
	public void loadView() {
		settingsView.display(mainStage, this);

		try {
			settingsView.setProperties(settingsModul.readRuntimeConfiguration().getAllProperties());
		} catch (URISyntaxException | IOException e) {
			settingsView.setPropertiesNotFound();
			LOGGER.error(e.getLocalizedMessage());
		}
	}

	public void saveProperties(TreeMap<String, Object> properties)
			throws JAXBException, IOException, URISyntaxException {
		settingsModul.overrideProperties(properties);
	}
}