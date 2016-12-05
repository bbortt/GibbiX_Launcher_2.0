package ch.gibb.iet.modul306.vmlauncher.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import ch.gibb.iet.modul306.vmlauncher.model.SettingsModel;
import ch.gibb.iet.modul306.vmlauncher.view.DesignView;
import javafx.stage.Stage;

public class DesignController extends AbstractController {
	private SettingsModel settingsModul;

	private DesignView designView;

	public DesignController(Stage mainStage, BootController bootController) {
		super(mainStage, bootController);

		designView = new DesignView();
	}

	@Override
	public void loadView() {
		designView.display(mainStage, this);
	}

	public void saveProperty(String name, String value) throws JAXBException, IOException, URISyntaxException {
		settingsModul.setProperty(name, value);
		settingsModul.savePropertiesChanged();
	}
}