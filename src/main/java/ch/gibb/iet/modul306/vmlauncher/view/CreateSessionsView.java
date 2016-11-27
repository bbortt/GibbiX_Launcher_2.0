package ch.gibb.iet.modul306.vmlauncher.view;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.controller.SessionController;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class CreateSessionsView extends AbstractView<SessionController> {
	private static final Logger LOGGER = LogManager.getLogger(CreateSessionsView.class);

	private XMLMachine[] givenMachines;
	private SessionView sessionView;

	public void setXMLMachines(XMLMachine[] machines) {
		this.givenMachines = machines;
	}

	public CreateSessionsView(Stage mainStage, SessionController controller) {
		super(mainStage, controller);
	}

	@Override
	protected void loadScene() {
		this.webView = new WebView();

		mainStage.setTitle(DISPLAY_NAME);

		if (mainStage.getScene() != null) {
			mainStage.setScene(new Scene(super.loadPage("create_session_view.html"), mainStage.getScene().getWidth(),
					mainStage.getScene().getHeight()));
		} else {
			mainStage.setScene(new Scene(super.loadPage("create_session_view.html")));
		}

		mainStage.show();
	}

	@Override
	protected void viewLoadedCallback() throws Exception {
		// TODO Auto-generated method stub

	}

	public void setSuper(SessionView superView) {
		this.sessionView = superView;
	}
}