package ch.gibb.iet.modul306.vmlauncher.view;

import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.controller.BootController;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class ApplicationView extends AbstractView<BootController> {
	private static final Logger LOGGER = Logger.getLogger(ApplicationView.class);

	public ApplicationView(Stage mainStage, BootController controller) {
		super(mainStage, controller);

		LOGGER.info(this.getClass().getSimpleName() + " successfully displayed");
	}

	@Override
	protected WebView loadMainPage() {
		return loadPage("index.html");
	}

	@Override
	protected void bindButtonListener() {
		// TODO Auto-generated method stub
	}
}