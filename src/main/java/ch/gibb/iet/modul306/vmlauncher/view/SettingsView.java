package ch.gibb.iet.modul306.vmlauncher.view;

import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

import ch.gibb.iet.modul306.vmlauncher.controller.SettingsController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class SettingsView extends AbstractView<SettingsController> {
	private static final Logger LOGGER = LogManager.getLogger(SettingsView.class);

	private HashMap<String, Object> properties;
	private boolean propertiesNotFound = false;

	public void setProperties(HashMap<String, Object> properties) {
		if (properties == null) {
			setPropertiesNotFound();
			return;
		}

		this.properties = properties;
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

		mainStage.setTitle(DISPLAY_NAME);

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
						information.setContentText("Please restart application to affect changes!");
						information.show();

						controller.loadView(mainStage);

						evt.preventDefault();
					}
				}, false);

		bindClickEventToClass("home_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				LOGGER.info("Chaning to boot-modul");
				controller.getBootController().loadView(mainStage);
			}
		});

		bindFooterLinks();
	}

	private void showPropertiesNotFound() {
		LOGGER.info("Inform user that no properties were found");

		// <div class="col s12 m4"><div class="icon-block"><!-- Left filler
		// --></div></div>
		String leftFiller = "<div class='col s12 m4'><div class='icon-block'><!-- Left filler --></div></div>";
		addHTMLToElementWithId(getContentElementId(), leftFiller);

		StringBuilder htmlBuilder = new StringBuilder();

		// <div class="col s12 m4">
		htmlBuilder.append("<div class='col s12 m4'>");
		// <p class="light">[SOME TEXT]</p>
		htmlBuilder.append(
				"<p class='light'>We were not able to find any configurations. Please check your classpath!</p>");
		// </div>
		htmlBuilder.append("</div>");

		addHTMLToElementWithId(getContentElementId(), htmlBuilder.toString());

		// <div class="col s12 m4"><div class="icon-block"><!-- Right filler
		// --></div></div>
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

		// <div class="switch col s12">
		elementBuilder.append("<div class='switch col s12'>");
		// <p>[PROPERTY_KEY]</p>
		elementBuilder.append("<p>" + name + "</p>");
		// <label>
		elementBuilder.append("<label>");
		// Off
		elementBuilder.append("False");

		if (value == true) {
			// <input type="checkbox">
			elementBuilder.append("<input id='" + name + "' type='checkbox' checked='true'>");
		} else {
			// <input type="checkbox">
			elementBuilder.append("<input id='" + name + "' type='checkbox'>");
		}

		// <span class="lever"></span>
		elementBuilder.append("<span class='lever'></span>");
		// On
		elementBuilder.append("True");
		// </label>
		elementBuilder.append("</label>");
		// </div>
		elementBuilder.append("</div>");

		return elementBuilder.toString();
	}

	private String createStringHTMLElement(String name, String value) {
		StringBuilder elementBuilder = new StringBuilder();

		// <div class="input-field col s12">
		elementBuilder.append("<div class='input-field col s12'>");
		// <p>[PROPERTY_KEY]</p>
		elementBuilder.append("<p>" + name + "</p>");

		if (value != null && value != "") {
			// <input id="[PROPERTY_KEY]" type="text" class="validate">
			elementBuilder.append("<input id='" + name + "' type='text' class='validate' value='" + value + "'>");
		} else {
			// <input placeholder="[PROPERTY-VALUE]" id="[PROPERTY_KEY]"
			// type="text" class="validate">
			elementBuilder
					.append("<input placeholder='" + name + "-value' id='" + name + "' type='text' class='validate'>");
		}

		// </div>
		elementBuilder.append("</div>");

		return elementBuilder.toString();
	}

	private void saveChanges() {
		updateObjectProperties();

		try {
			controller.saveProperties(properties);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());

			Alert error = new Alert(AlertType.ERROR);
			error.setTitle(e.getClass().toString());
			error.setHeaderText("Could not save new properties");
			error.setContentText(e.getLocalizedMessage());
			error.show();
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