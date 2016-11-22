package ch.gibb.iet.modul306.vmlauncher.view;

import java.io.File;

import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.controller.AbstractController;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

@SuppressWarnings({ "restriction" })
public abstract class AbstractView<C extends AbstractController> {
	private static final Logger LOGGER = Logger.getLogger(AbstractView.class);
	protected static final String DISPLAY_NAME = "VMLauncher 2.0";

	protected WebView webView;

	protected C controller;

	public AbstractView(Stage mainStage, C controller) {
		this.webView = new WebView();
		this.controller = controller;

		mainStage.setTitle(DISPLAY_NAME);
		mainStage.setScene(new Scene(loadMainPage()));
		mainStage.show();
	}

	protected abstract WebView loadMainPage();

	protected WebView loadPage(String pageName) {
		String htmlFilePath = getClass().getClassLoader().getResource("pages/" + pageName).getPath();

		LOGGER.debug("Using html file at " + htmlFilePath);

		webView.getEngine().load(htmlFilePath);

		return webView;
	}

	protected abstract void bindButtonListener();
}