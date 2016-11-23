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
		mainStage.setScene(new Scene(super.loadPage("index.html")));

		mainStage.show();
	}

	@Override
	protected void bindButtonListener() throws InterruptedException {
		bindClickEventToClass("hosts_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				LOGGER.warn("Hosts modul does not exist yet!");
			}
		});

		bindClickEventToClass("settings_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				LOGGER.warn("Settings modul does not exist yet!");
			}
		});

		((EventTarget) webView.getEngine().getDocument().getElementById("machine_modul_link")).addEventListener("click",
				new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						if (!controller.isLauncherModulEnabled()) {
							LOGGER.warn("Launcher-modul is currently not enabled!");

							warnModulNotEnabled("Launcher-Modul");
							return;
						}

						LOGGER.info("Chaning to launcher-modul");
						controller.getLauncherModul().loadView(mainStage);
					}
				}, false);

		((EventTarget) webView.getEngine().getDocument().getElementById("backup_modul_link")).addEventListener("click",
				new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						if (!controller.isBackupModulEnabled()) {
							LOGGER.warn("Backup-modul is currently not enabled!");

							warnModulNotEnabled("Backup-modul");
							return;
						}

						LOGGER.info("Chaning to backup-modul");
						controller.getBackupModul().loadView(mainStage);
					}
				}, false);

		((EventTarget) webView.getEngine().getDocument().getElementById("session_modul_link")).addEventListener("click",
				new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						if (!controller.isSessionModulEnabled()) {
							LOGGER.warn("Session-modul is currently not enabled!");

							warnModulNotEnabled("Session-modul");
							return;
						}

						LOGGER.info("Chaning to session-modul");
						controller.getSessionModul().loadView(mainStage);
					}
				}, false);

		bindFooterLinks();
	}
}