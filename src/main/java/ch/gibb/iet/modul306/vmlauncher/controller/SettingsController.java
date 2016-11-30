package ch.gibb.iet.modul306.vmlauncher.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.gibb.iet.modul306.vmlauncher.model.SettingsModel;
import ch.gibb.iet.modul306.vmlauncher.view.SettingsView;
import javafx.stage.Stage;

@Component
public class SettingsController extends AbstractController {
	private static final Logger LOGGER = LogManager.getLogger(SettingsController.class);

	@Autowired
	private SettingsModel settingsModul;

	@Override
	public void loadView(Stage mainStage) {
		SettingsView view = new SettingsView(mainStage, this);

		try {
			view.setProperties(settingsModul.readRuntimeConfiguration().getAllProperties());
		} catch (URISyntaxException | IOException e) {
			view.setPropertiesNotFound();
			LOGGER.error(e.getLocalizedMessage());
		}
	}

	public void saveProperties(TreeMap<String, Object> properties)
			throws JAXBException, IOException, URISyntaxException {
		settingsModul.overrideProperties(properties);
	}
}