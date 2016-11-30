package ch.gibb.iet.modul306.vmlauncher.view;

import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLSelectElement;

import ch.gibb.iet.modul306.vmlauncher.controller.DesignController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebView;

@Component
public class DesignView extends AbstractView<DesignController> {
	private static final Logger LOGGER = LogManager.getLogger(DesignView.class);

	private static String[] themes = new String[] { "Starter-Theme", "Default Material-Design" };

	private static String getSelectElementId() {
		return "theme_select_element";
	}

	@Override
	protected void loadScene() {
		this.webView = new WebView();

		mainStage.setTitle(displayName);

		if (mainStage.getScene() != null) {
			mainStage.setScene(new Scene(super.loadPage("theme_view.html"), mainStage.getScene().getWidth(),
					mainStage.getScene().getHeight()));
		} else {
			mainStage.setScene(new Scene(super.loadPage("theme_view.html")));
		}

		mainStage.show();
	}

	@Override
	protected void viewLoadedCallback() throws Exception {
		fillThemeOptions();

		bindClickEventToLinkClass("home_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				LOGGER.info("Chaning to boot-modul");
				controller.getBootController().loadView(mainStage);
			}
		});

		bindClickEventToLinkClass("settings_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				if (controller.getBootController().getSettingsModul() == null) {
					LOGGER.warn("Settings-modul ist currently not enabled!");

					warnModulNotEnabled("Settings-modul");
					return;
				}

				LOGGER.info("Changing to settings-modul");
				controller.getBootController().getSettingsModul().loadView(mainStage);

				evt.preventDefault();
			}
		});

		((EventTarget) webView.getEngine().getDocument().getElementById("save_theme_button")).addEventListener("click",
				new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						int selectedIndex = ((HTMLSelectElement) webView.getEngine().getDocument()
								.getElementById(getSelectElementId())).getSelectedIndex();

						LOGGER.debug("Changing theme to " + themes[selectedIndex].toLowerCase());

						try {
							controller.saveProperty("info.application.display.theme",
									themes[selectedIndex].toLowerCase());

							Alert information = new Alert(AlertType.INFORMATION);
							information.setTitle("Succeed");
							information.setContentText(
									"Succesfully changed theme. Please restart the application to affect changes!");
							information.show();
						} catch (Exception e) {
							e.printStackTrace();
							LOGGER.error(e.getLocalizedMessage());

							Alert error = new Alert(AlertType.ERROR);
							error.setTitle(e.getClass().toString());
							error.setHeaderText("Could not change theme!");
							error.setContentText(e.getLocalizedMessage());
							error.show();
						}

						controller.loadView(mainStage);

						evt.preventDefault();
					}
				}, false);

		bindFooterLinks();
	}

	private void fillThemeOptions() {
		StringBuilder optionsBuilder = new StringBuilder();

		Arrays.asList(themes).forEach(theme -> {
			if (theme.toLowerCase().equals(themeName)) {
				optionsBuilder.append(
						"<option value='" + Arrays.asList(themes).indexOf(theme) + "' selected>" + theme + "</option>");
			} else {
				optionsBuilder
						.append("<option value='" + Arrays.asList(themes).indexOf(theme) + "'>" + theme + "</option>");
			}
		});

		addHTMLToElementWithId(getSelectElementId(), optionsBuilder.toString());
	}
}