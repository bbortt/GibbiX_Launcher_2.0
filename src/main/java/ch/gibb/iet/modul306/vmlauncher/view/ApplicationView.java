package ch.gibb.iet.modul306.vmlauncher.view;

import org.apache.log4j.Logger;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import ch.gibb.iet.modul306.vmlauncher.controller.BootController;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class ApplicationView extends AbstractView<BootController> {
	private static final Logger LOGGER = Logger.getLogger(ApplicationView.class);

	public ApplicationView(Stage mainStage, BootController controller) {
		super(mainStage, controller);
	}

	@Override
	protected void loadScene() {
		this.webView = new WebView();

		mainStage.setTitle(DISPLAY_NAME);

		if (mainStage.getScene() != null) {
			mainStage.setScene(new Scene(super.loadPage("index.html"), mainStage.getScene().getWidth(),
					mainStage.getScene().getHeight()));
		} else {
			mainStage.setScene(new Scene(super.loadPage("index.html")));
		}

		mainStage.show();
	}

	@Override
	protected void viewLoadedCallback() throws InterruptedException {
		bindClickEventToClass("hosts_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				LOGGER.warn("Hosts modul does not exist yet!");
			}
		});

		bindClickEventToClass("settings_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				if (controller.getSettingsModul() == null) {
					LOGGER.warn("Settings-modul ist currently not enabled!");

					warnModulNotEnabled("Settings-modul");
					return;
				}

				LOGGER.info("Changing to settings-modul");
				controller.getSettingsModul().loadView(mainStage);

				evt.preventDefault();
			}
		});

		((EventTarget) webView.getEngine().getDocument().getElementById("machine_modul_link")).addEventListener("click",
				new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						if (controller.getLauncherModul() == null) {
							LOGGER.warn("Launcher-modul is currently not enabled!");

							warnModulNotEnabled("Launcher-modul");
							return;
						}

						LOGGER.info("Changing to launcher-modul");
						controller.getLauncherModul().loadView(mainStage);

						evt.preventDefault();
					}
				}, false);

		((EventTarget) webView.getEngine().getDocument().getElementById("backup_modul_link")).addEventListener("click",
				new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						if (controller.getBackupModul() == null) {
							LOGGER.warn("Backup-modul is currently not enabled!");

							warnModulNotEnabled("Backup-modul");
							return;
						}

						LOGGER.info("Changing to backup-modul");
						controller.getBackupModul().loadView(mainStage);

						evt.preventDefault();
					}
				}, false);

		((EventTarget) webView.getEngine().getDocument().getElementById("session_modul_link")).addEventListener("click",
				new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						if (controller.getSessionModul() == null) {
							LOGGER.warn("Session-modul is currently not enabled!");

							warnModulNotEnabled("Session-modul");
							return;
						}

						LOGGER.info("Changing to session-modul");
						controller.getSessionModul().loadView(mainStage);

						evt.preventDefault();
					}
				}, false);

		bindFooterLinks();
	}
}