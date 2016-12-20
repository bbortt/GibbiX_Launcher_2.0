package ch.gibb.iet.modul306.vmlauncher.view;

import java.util.TreeMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

import ch.gibb.iet.modul306.vmlauncher.controller.SettingsController;
import ch.gibb.iet.modul306.vmlauncher.view.dialogues.ErrorDialogWithStacktrace;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class SettingsView extends AbstractView<SettingsController> {
	private static final Logger LOGGER = LogManager.getLogger(SettingsView.class);

	private TreeMap<String, Object> properties;
	private boolean propertiesNotFound = false;

	public void setProperties(TreeMap<String, Object> treeMap) {
		if (treeMap == null) {
			setPropertiesNotFound();
			return;
		}

		this.properties = treeMap;
	}

	public void setPropertiesNotFound() {
		propertiesNotFound = true;
	}

	private static String getContentElementId() {
		return "root_content_element";
	}

	public SettingsView(Stage mainStage, SettingsController controller) {
		super(mainStage, controller);
	}

	@Override
	protected void loadScene() {
		this.webView = new WebView();

		mainStage.setTitle(displayName);

		if (mainStage.getScene() != null) {
			mainStage.setScene(new Scene(super.loadPage("settings_view.html"), mainStage.getScene().getWidth(),
					mainStage.getScene().getHeight()));
		} else {
			mainStage.setScene(new Scene(super.loadPage("settings_view.html")));
		}

		mainStage.show();
	}

	@Override
	protected void viewLoadedCallback() throws Exception {
		loadAndApplyTheme();

		if (propertiesNotFound) {
			showPropertiesNotFound();
		} else {
			addPropertiesToView();
		}

		((EventTarget) webView.getEngine().getDocument().getElementById("save_settings_button"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						saveChanges();

						Alert information = new Alert(AlertType.INFORMATION);
						information.setTitle("Success");
						information.setHeaderText("Changes successfully saved.");
						information.setContentText("Please restart application to affect changes!");
						information.show();

						controller.loadView();

						evt.preventDefault();
					}
				}, false);

		bindClickEventToLinkClass("home_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				LOGGER.info("Chaning to boot-modul");
				controller.getBootController().loadView();
			}
		});

		bindClickEventToLinkClass("theme_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				if (controller.getBootController().getDesignModul() == null) {
					LOGGER.warn("Design-modul ist currently not enabled!");

					warnModulNotEnabled("Design-modul");
					return;
				}

				LOGGER.info("Chaning to design-modul");
				controller.getBootController().getDesignModul().loadView();

				evt.preventDefault();
			}
		});

		bindFooterLinks();
	}

	private void showPropertiesNotFound() {
		LOGGER.info("Inform user that no properties were found");

		String leftFiller = "<div class='col s12 m4'><div class='icon-block'><!-- Left filler --></div></div>";
		addHTMLToElementWithId(getContentElementId(), leftFiller);

		StringBuilder htmlBuilder = new StringBuilder();

		htmlBuilder.append("<div class='col s12 m4'>");
		htmlBuilder.append(
				"<p class='light content-text'>We were not able to find any configurations. Please check your classpath!</p>");
		htmlBuilder.append("</div>");

		addHTMLToElementWithId(getContentElementId(), htmlBuilder.toString());

		String rightFiller = "<div class='col s12 m4'><div class='icon-block'><!-- Right filler --></div></div>";
		addHTMLToElementWithId(getContentElementId(), rightFiller);
	}

	private void addPropertiesToView() {
		LOGGER.info("Adding " + properties.size() + " properties to view");

		properties.entrySet().forEach(entrySet -> {
			if (entrySet.getValue().toString().contains("true") || entrySet.getValue().toString().contains("false")) {
				LOGGER.debug("Handling boolean property");

				addHTMLToElementWithId(getContentElementId(),
						createBooleanHTMLElement(entrySet.getKey(), Boolean.valueOf(entrySet.getValue().toString())));
			} else if (entrySet.getValue().getClass().equals(String.class)) {
				LOGGER.debug("Handling String property");

				addHTMLToElementWithId(getContentElementId(),
						createStringHTMLElement(entrySet.getKey(), entrySet.getValue().toString()));
			} else {
				LOGGER.error("Cannot handle property of class " + entrySet.getValue().getClass() + ". Skipping..");
			}
		});
	}

	private String createBooleanHTMLElement(String name, boolean value) {
		StringBuilder elementBuilder = new StringBuilder();

		elementBuilder.append("<div class='switch col s12'>");
		elementBuilder.append("<p class='content-text'>" + name + "</p>");
		elementBuilder.append("<label class='content-text'>");
		// Off
		elementBuilder.append("False");

		if (value == true) {
			elementBuilder.append("<input id='" + name + "' type='checkbox' checked='true'>");
		} else {
			elementBuilder.append("<input id='" + name + "' type='checkbox'>");
		}

		elementBuilder.append("<span class='lever'></span>");

		// On
		elementBuilder.append("True");
		elementBuilder.append("</label>");
		elementBuilder.append("</div>");

		return elementBuilder.toString();
	}

	private String createStringHTMLElement(String name, String value) {
		StringBuilder elementBuilder = new StringBuilder();

		elementBuilder.append("<div class='input-field col s12'>");
		elementBuilder.append("<p class='content-text'>" + name + "</p>");

		if (value != null && value != "") {
			elementBuilder.append(
					"<input id='" + name + "' type='text' class='validate content-text' value='" + value + "'>");
		} else {
			elementBuilder.append("<input placeholder='" + name + "-value' id='" + name
					+ "' type='text' class='validate content-text'>");
		}

		elementBuilder.append("</div>");

		return elementBuilder.toString();
	}

	private void saveChanges() {
		updateObjectProperties();

		try {
			controller.getBootController().getApplicationSettings().overrideProperties(properties);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());

			new ErrorDialogWithStacktrace().appendStracktrace(e).showAndWait();
		}
	}

	private void updateObjectProperties() {
		properties.entrySet().forEach(entry -> {
			HTMLInputElement inputElement = (HTMLInputElement) webView.getEngine().getDocument()
					.getElementById(entry.getKey());

			if (inputElement.getType().equals("checkbox")) {
				LOGGER.debug("Handling checkbox element");

				properties.put(entry.getKey(), inputElement.getChecked());
			} else if (inputElement.getType().equals("text")) {
				LOGGER.debug("Handling text element");

				properties.put(entry.getKey(), inputElement.getValue());
			} else {
				LOGGER.error("Cannot handle element of type " + inputElement.getType());
			}
		});
	}
}