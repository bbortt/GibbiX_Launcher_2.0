package ch.gibb.iet.modul306.vmlauncher.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;

import ch.gibb.iet.modul306.vmlauncher.model.SettingsModel;
import ch.gibb.iet.modul306.vmlauncher.view.DesignView;
import javafx.stage.Stage;

public class DesignController extends AbstractController {
	@Autowired
	private SettingsModel settingsModul;

	@Autowired
	private DesignView designView;

	@Override
	public void loadView(Stage mainStage) {
		designView.display(mainStage, this);
	}

	public void saveProperty(String name, String value) throws JAXBException, IOException, URISyntaxException {
		settingsModul.setProperty(name, value);
		settingsModul.savePropertiesChanged();
	}
}