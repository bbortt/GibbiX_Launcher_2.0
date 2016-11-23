package ch.gibb.iet.modul306.vmlauncher.view;

import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.controller.AbstractController;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public abstract class AbstractView<C extends AbstractController> {
	private static final Logger LOGGER = Logger.getLogger(AbstractView.class);
	protected static final String DISPLAY_NAME = "VMLauncher 2.0";

	private WebView webView;

	protected C controller;

	public AbstractView(Stage mainStage, C controller) {
		this.controller = controller;

		this.webView = new WebView();

		mainStage.setTitle(DISPLAY_NAME);
		mainStage.setScene(new Scene(loadMainPage()));
		mainStage.show();
	}

	protected abstract WebView loadMainPage();

	protected WebView loadPage(String pageName) {
		String notWorkingFile = "file:///" + getClass().getClassLoader().getResource("pages/" + pageName).getPath();
		LOGGER.debug("Using file at " + notWorkingFile);

		webView.getEngine().load(notWorkingFile);

		return webView;
	}

	protected abstract void bindButtonListener();
}