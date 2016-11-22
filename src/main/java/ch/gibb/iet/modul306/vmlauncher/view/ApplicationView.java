package ch.gibb.iet.modul306.vmlauncher.view;

import ch.gibb.iet.modul306.vmlauncher.controller.BootController;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

@SuppressWarnings({ "restriction" })
public class ApplicationView extends AbstractView<BootController> {
	public ApplicationView(Stage mainStage, BootController controller) {
		super(mainStage, controller);
	}

	@Override
	protected WebView loadMainPage() {
		return super.loadPage("index.html");
	}

	@Override
	protected void bindButtonListener() {
		// TODO Auto-generated method stub
	}
}