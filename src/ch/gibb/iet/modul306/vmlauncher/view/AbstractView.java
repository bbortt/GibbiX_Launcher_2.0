package ch.gibb.iet.modul306.vmlauncher.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

	protected String displayName;
	protected String themeName;

	protected Stage mainStage;

	protected WebView webView;
	protected C controller;

	public AbstractView(Stage mainStage, C controller) {
		this.mainStage = mainStage;
		this.controller = controller;

		try {
			TreeMap<String, Object> applicationSettings = controller.getBootController().getApplicationSettings()
					.getAllProperties();

			displayName = applicationSettings.get("application.display.name").toString();
			themeName = applicationSettings.get("application.display.themes.current").toString();
		} catch (URISyntaxException | IOException e) {
			LOGGER.error(e.getLocalizedMessage());
		}
	}

	public void display() {
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
					LOGGER.warn("Page is still loading. Worker state is \"" + newValue + "\"!");
				} else {
					LOGGER.info("Loading finished: State is \"" + newValue + "\".");

					try {
						viewLoadedCallback();
					} catch (Exception e) {
						waitForGUIAndBindButtons();
					}

					webView.getEngine().getLoadWorker().stateProperty().removeListener(this);
				}
			}
		});
	}

	protected abstract void loadScene();

	protected WebView loadPage(String pageName) {
		String newDisplayFile = "file:///" + new File("resources/sites/" + pageName).getAbsolutePath();
		LOGGER.debug("Using html file at " + newDisplayFile);

		webView.getEngine().load(newDisplayFile);

		return webView;
	}

	protected void loadAndApplyTheme() {
		// Create and append a new css tag (dynamically)
		Document doc = webView.getEngine().getDocument();
		Element styleNode = doc.createElement("link");
		styleNode.setAttribute("href", "../themes/" + themeName + ".css");
		styleNode.setAttribute("type", "text/css");
		styleNode.setAttribute("rel", "stylesheet");
		styleNode.setAttribute("media", "screen, projection");
		doc.getDocumentElement().getElementsByTagName("head").item(0).appendChild(styleNode);
	}

	protected abstract void viewLoadedCallback() throws Exception;

	protected void warnModulNotEnabled(String modulName) {
		Alert warning = new Alert(AlertType.WARNING);
		warning.setTitle("Modul not enabled");
		warning.setContentText(modulName + " is currently not enabled. Please check your settings!");
		warning.show();
	}

	protected void bindClickEventToLinkClass(String elementClazz, EventListener listener) {
		LOGGER.debug("Binding click listener to links of class " + elementClazz);

		NodeList classElements = webView.getEngine().getDocument().getElementsByTagName("A");
		for (int i = 0; i < classElements.getLength(); i++) {
			NamedNodeMap attributesList = classElements.item(i).getAttributes();
			for (int j = 0; j < attributesList.getLength(); j++) {
				if (attributesList.item(j).getNodeName() != null && attributesList.item(j).getNodeName().equals("class")
						&& attributesList.item(j).getNodeValue().contains(elementClazz)) {
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

								Alert error = new Alert(AlertType.ERROR);
								error.setTitle(e.getClass().toString());
								error.setContentText(e.getLocalizedMessage());
								error.show();
							}
						}

						evt.preventDefault();
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

								Alert error = new Alert(AlertType.ERROR);
								error.setTitle(e.getClass().toString());
								error.setContentText(e.getLocalizedMessage());
								error.show();
							}
						}

						evt.preventDefault();
					}
				}, false);
	}

	protected void addHTMLToElementWithId(String id, String html) {
		StringBuilder scriptBuilder = new StringBuilder();

		// JQery opening tag
		scriptBuilder.append("$( \"#" + id + "\" ).append( \"");

		// Content to append
		scriptBuilder.append(html);

		// JQuery closing tag
		scriptBuilder.append("\" )");

		LOGGER.debug("Appending content to element " + id + ": " + html);

		webView.getEngine().executeScript(scriptBuilder.toString());
	}
}