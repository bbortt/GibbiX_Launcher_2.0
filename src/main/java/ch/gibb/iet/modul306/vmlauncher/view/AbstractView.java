package ch.gibb.iet.modul306.vmlauncher.view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import ch.gibb.iet.modul306.vmlauncher.app.app;
import ch.gibb.iet.modul306.vmlauncher.controller.AbstractController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public abstract class AbstractView<C extends AbstractController> {
	private static final Logger LOGGER = Logger.getLogger(AbstractView.class);
	protected static final String DISPLAY_NAME = "VMLauncher 2.0";

	protected Stage mainStage;

	protected WebView webView;
	protected C controller;

	public AbstractView(Stage mainStage, C controller) {
		this.mainStage = mainStage;
		this.controller = controller;

		loadScene();

		waitForGUIAndBindButtons();

		LOGGER.info(this.getClass().getSimpleName() + " loaded at " + app.getCurrentSystemTime());
	}

	private void waitForGUIAndBindButtons() {
		// LoadWorker calls listener if page is fully loaded
		// Bind buttons to fully loaded page --> NullPointerException otherwise
		// Removes itself after one use
		webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
			@Override
			public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue,
					Worker.State newValue) {
				if (newValue != Worker.State.SUCCEEDED) {
					LOGGER.fatal("Could not load page to display. Exiting application!");
					System.exit(1);
				}

				try {
					viewLoadedCallback();
				} catch (Exception e) {
					waitForGUIAndBindButtons();
				}

				webView.getEngine().getLoadWorker().stateProperty().removeListener(this);
			}
		});
	}

	protected abstract void loadScene();

	protected WebView loadPage(String pageName) {
		String notWorkingFile = "file:///" + getClass().getClassLoader().getResource("contents/" + pageName).getPath();
		LOGGER.debug("Using file at " + notWorkingFile);

		webView.getEngine().load(notWorkingFile);

		return webView;
	}

	protected abstract void viewLoadedCallback() throws Exception;

	protected void warnModulNotEnabled(String modulName) {
		Alert warning = new Alert(AlertType.WARNING);
		warning.setTitle("Modul not enabled");
		warning.setContentText(modulName + " is currently not enabled. Please check your settings!");
		warning.show();
	}

	protected void bindClickEventToClass(String clazz, EventListener listener) {
		LOGGER.debug("Binding click listener to items of class " + clazz);

		NodeList classElements = webView.getEngine().getDocument().getElementsByTagName("A");
		for (int i = 0; i < classElements.getLength(); i++) {
			NamedNodeMap attributesList = classElements.item(i).getAttributes();
			for (int j = 0; j < attributesList.getLength(); j++) {
				if (attributesList.item(j).getNodeName() != null && attributesList.item(j).getNodeName().equals("class")
						&& attributesList.item(j).getNodeValue().contains(clazz)) {
					((EventTarget) classElements.item(i)).addEventListener("click", listener, false);
				}
			}
		}
	}

	protected void bindFooterLinks() {
		LOGGER.debug("Binding footer links");

		((EventTarget) webView.getEngine().getDocument().getElementById("project_github_link"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						LOGGER.debug("Asking user to open https://github.com/bbortt/GibbiX_Launcher_2.0");

						Alert confirmOpening = new Alert(AlertType.CONFIRMATION);
						confirmOpening.setTitle("Please confirm");
						confirmOpening.setHeaderText("Opening materialize in your browser.");
						confirmOpening.setContentText(
								"Are you sure you want to open the external link to \"https://github.com/bbortt/GibbiX_Launcher_2.0\"?");

						if (confirmOpening.showAndWait().get() == ButtonType.OK) {
							try {
								LOGGER.info(
										"Opening https://github.com/bbortt/GibbiX_Launcher_2.0 in external browser");
								Desktop.getDesktop().browse(new URI("https://github.com/bbortt/GibbiX_Launcher_2.0"));
							} catch (IOException | URISyntaxException e) {
								LOGGER.error(e.getLocalizedMessage());
							}
						}
					}
				}, false);

		((EventTarget) webView.getEngine().getDocument().getElementById("materialize_footer_link"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						LOGGER.debug("Asking user to open http://materializecss.com");

						Alert confirmOpening = new Alert(AlertType.CONFIRMATION);
						confirmOpening.setTitle("Please confirm");
						confirmOpening.setHeaderText("Opening materialize in your browser.");
						confirmOpening.setContentText(
								"Are you sure you want to open the external link to \"http://materializecss.com\"?");

						if (confirmOpening.showAndWait().get() == ButtonType.OK) {
							try {
								LOGGER.info("Opening http://materializecss.com in external browser");
								Desktop.getDesktop().browse(new URI("http://materializecss.com"));
							} catch (IOException | URISyntaxException e) {
								LOGGER.error(e.getLocalizedMessage());
							}
						}
					}
				}, false);
	}
}