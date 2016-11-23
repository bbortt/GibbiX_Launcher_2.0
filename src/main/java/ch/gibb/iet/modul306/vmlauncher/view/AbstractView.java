package ch.gibb.iet.modul306.vmlauncher.view;

import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.controller.AbstractController;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public abstract class AbstractView<C extends AbstractController> {
	private static final Logger LOGGER = Logger.getLogger(AbstractView.class);
	protected static final String DISPLAY_NAME = "VMLauncher 2.0";

	protected C controller;

	public AbstractView(Stage mainStage, C controller) {
		this.controller = controller;

		mainStage.setTitle(DISPLAY_NAME);
		mainStage.setScene(new Scene(loadMainPage()));
		mainStage.show();
	}

	protected abstract Browser loadMainPage();

	protected Browser loadPage(String pageName) {
		return new Browser(pageName);
	}

	protected abstract void bindButtonListener();

	protected class Browser extends Region {
		private final WebView WEB_VIEW = new WebView();
		private final WebEngine WEB_ENGINE = WEB_VIEW.getEngine();

		public Browser(String pageName) {
			String htmlFilePath = getClass().getClassLoader().getResource("pages/" + pageName).getPath();
			LOGGER.debug("Using html file at " + htmlFilePath);

			WEB_ENGINE.load(htmlFilePath);

			getChildren().add(WEB_VIEW);
		}

		@SuppressWarnings("unused")
		private Node createSpacer() {
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);
			return spacer;
		}

		@Override
		protected void layoutChildren() {
			double w = getWidth();
			double h = getHeight();
			layoutInArea(WEB_VIEW, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
		}

		@Override
		protected double computePrefWidth(double height) {
			return 750;
		}

		@Override
		protected double computePrefHeight(double width) {
			return 500;
		}
	}
}