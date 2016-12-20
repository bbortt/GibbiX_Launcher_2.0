package ch.gibb.iet.modul306.vmlauncher.view;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLSelectElement;

import ch.gibb.iet.modul306.vmlauncher.controller.DesignController;
import ch.gibb.iet.modul306.vmlauncher.view.dialogues.ErrorDialogWithStacktrace;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class DesignView extends AbstractView<DesignController> {
	private static final Logger LOGGER = LogManager.getLogger(DesignView.class);

	String[] themes;

	private static String getSelectElementId() {
		return "theme_select_element";
	}

	public DesignView(Stage mainStage, DesignController controller) {
		super(mainStage, controller);

		try {
			themes = controller.getBootController().getApplicationSettings().getAllPossibleThemes();
		} catch (URISyntaxException | IOException e) {
			LOGGER.warn(e.getLocalizedMessage());

			new ErrorDialogWithStacktrace().appendStracktrace(e).showAndWait();
		}
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
		loadAndApplyTheme();

		fillThemeOptions();

		bindClickEventToLinkClass("home_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				LOGGER.info("Chaning to boot-modul");
				controller.getBootController().loadView();
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
				controller.getBootController().getSettingsModul().loadView();

				evt.preventDefault();
			}
		});

		((EventTarget) webView.getEngine().getDocument().getElementById("save_theme_button")).addEventListener("click",
				new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						int selectedIndex = ((HTMLSelectElement) webView.getEngine().getDocument()
								.getElementById(getSelectElementId())).getSelectedIndex();

						LOGGER.debug("Changing theme to " + themes[selectedIndex]);

						try {
							controller.getBootController().getApplicationSettings()
									.setProperty("application.display.themes.current", themes[selectedIndex]);
							controller.getBootController().getApplicationSettings().savePropertiesChanged();

							Alert information = new Alert(AlertType.INFORMATION);
							information.setTitle("Success");
							information.setHeaderText("Changes successfully saved.");
							information.setContentText("Please restart application to affect changes!");
							information.show();
						} catch (Exception e) {
							LOGGER.error(e.getLocalizedMessage());

							new ErrorDialogWithStacktrace().appendStracktrace(e).showAndWait();
						}

						controller.loadView();

						evt.preventDefault();
					}
				}, false);

		bindFooterLinks();
	}

	private void fillThemeOptions() {
		StringBuilder optionsBuilder = new StringBuilder();

		Arrays.asList(themes).forEach(theme -> {
			if (theme.equals(themeName)) {
				optionsBuilder.append("<option class='content-text' value='" + Arrays.asList(themes).indexOf(theme)
						+ "' selected>" + theme + "</option>");
			} else {
				optionsBuilder.append("<option class='content-text' value='" + Arrays.asList(themes).indexOf(theme)
						+ "'>" + theme + "</option>");
			}
		});

		addHTMLToElementWithId(getSelectElementId(), optionsBuilder.toString());
	}
}