package ch.gibb.iet.modul306.vmlauncher.view;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import ch.gibb.iet.modul306.vmlauncher.controller.SessionController;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLSessions.Session;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class SessionView extends AbstractView<SessionController> {
	private static final Logger LOGGER = LogManager.getLogger(SessionView.class);

	private Session[] givenSessions;
	private XMLMachine[] givenMachines;
	private boolean sessionsNotFound = false;
	private boolean machinesNotFound = false;

	public void setSessions(Session[] sessions) {
		if (sessions == null) {
			setSessionsNotFound();
			return;
		}

		this.givenSessions = sessions;
	}

	public void setXMLMachines(XMLMachine[] machines) {
		if (machines == null) {
			setMachinesNotFound();
			return;
		}

		this.givenMachines = machines;
	}

	public void setSessionsNotFound() {
		sessionsNotFound = true;
	}

	public void setMachinesNotFound() {
		machinesNotFound = true;
	}

	private static String getContentElementId() {
		return "root_content_element";
	}

	public SessionView(Stage mainStage, SessionController controller) {
		super(mainStage, controller);
	}

	@Override
	protected void loadScene() {
		this.webView = new WebView();

		mainStage.setTitle(displayName);

		if (mainStage.getScene() != null) {
			mainStage.setScene(new Scene(super.loadPage("session_view.html"), mainStage.getScene().getWidth(),
					mainStage.getScene().getHeight()));
		} else {
			mainStage.setScene(new Scene(super.loadPage("session_view.html")));
		}

		mainStage.show();
	}

	@Override
	protected void viewLoadedCallback() throws Exception {
		loadAndApplyTheme();

		if (machinesNotFound) {
			showMachinesNotFount();
		} else if (sessionsNotFound) {
			showSessionsNotFound();
		} else {
			addSessionsToView();
		}

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

		((EventTarget) webView.getEngine().getDocument().getElementById("create_session_button"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						CreateSessionsView createSessionsView = new CreateSessionsView(mainStage, controller);
						createSessionsView.display();
						createSessionsView.setXMLMachines(givenMachines);

						evt.preventDefault();
					}
				}, false);

		bindFooterLinks();
	}

	private void showMachinesNotFount() throws URISyntaxException, IOException {
		LOGGER.info("Inform user that no machine was found");

		// Disable "create" button
		webView.getEngine().getDocument().getElementById("create_session_button").setAttribute("disabled", null);

		String leftFiller = "<div class='col s12 m4'><div class='icon-block'><!-- Left filler --></div></div>";
		addHTMLToElementWithId(getContentElementId(), leftFiller);

		StringBuilder htmlBuilder = new StringBuilder();

		htmlBuilder.append("<div class='col s12 m4'>");
		htmlBuilder.append("<div class='icon-block'>");
		htmlBuilder.append("<a class='settings_menu_link content-link' href='settings_modul'>");
		htmlBuilder.append("<h2 class='center content-text'>");
		htmlBuilder.append("<img alt='Settings' src='../images/ic_settings_black_24dp_2x.png' />");
		htmlBuilder.append("</h2>");
		htmlBuilder.append("<h5 class='center content-header'>Settings</h5>");
		htmlBuilder.append("</a>");
		htmlBuilder.append("<p class='light content-text'>We were not able to find your GibbiX at the  default path on "
				+ controller.getBootController().getApplicationSettings().getProperty("gibbix.path.default").toString()
				+ ". Please check your settings!</p>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");

		addHTMLToElementWithId(getContentElementId(), htmlBuilder.toString());

		String rightFiller = "<div class='col s12 m4'><div class='icon-block'><!-- Right filler --></div></div>";
		addHTMLToElementWithId(getContentElementId(), rightFiller);
	}

	private void showSessionsNotFound() {
		LOGGER.info("Inform user that no sessions were found");

		String leftFiller = "<div class='col s12 m4'><div class='icon-block'><!-- Left filler --></div></div>";
		addHTMLToElementWithId(getContentElementId(), leftFiller);

		StringBuilder htmlBuilder = new StringBuilder();

		htmlBuilder.append("<div class='col s12 m4'>");
		htmlBuilder.append("<div class='icon-block'>");
		htmlBuilder.append("<a class='settings_menu_link content-link' href='settings_modul'>");
		htmlBuilder.append("<h2 class='center content-text'>");
		htmlBuilder.append("<img alt='Settings' src='../images/ic_settings_black_24dp_2x.png' />");
		htmlBuilder.append("</h2>");
		htmlBuilder.append("<h5 class='center content-header'>Settings</h5>");
		htmlBuilder.append("</a>");
		htmlBuilder.append(
				"<p class='light content-text'>We were not able to find any session. Create your first session by clicking the button below.</p>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");

		addHTMLToElementWithId(getContentElementId(), htmlBuilder.toString());

		String rightFiller = "<div class='col s12 m4'><div class='icon-block'><!-- Right filler --></div></div>";
		addHTMLToElementWithId(getContentElementId(), rightFiller);
	}

	private void addSessionsToView() {
		LOGGER.info("Adding " + givenSessions.length + " sessions to view");

		Arrays.asList(givenSessions).forEach(session -> {
			addHTMLToElementWithId(getContentElementId(), createSessionHTMLElement(session));
			addSessionManageClickListener(session);
		});
	}

	private void addSessionManageClickListener(Session session) {
		LOGGER.debug("Adding click listener to " + session.name);

		((EventTarget) webView.getEngine().getDocument().getElementById(String.valueOf("launch_" + session.id)))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						LOGGER.info("Launching session " + session.name);

						Arrays.asList(session.getAllMachines()).forEach(machine -> {
							try {
								Arrays.asList(givenMachines).stream()
										.filter(givenMachine -> (givenMachine.name.equals(machine.name))).findFirst()
										.get().launch();
							} catch (NoSuchElementException e) {
								Alert error = new Alert(AlertType.ERROR);
								error.setTitle(e.getClass().toString());
								error.setContentText("Machine " + machine.name + " not found. Skipping..");
								error.show();
							}
						});

						evt.preventDefault();
					}
				}, false);

		((EventTarget) webView.getEngine().getDocument().getElementById(String.valueOf("delete_" + session.id)))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						Alert confirm = new Alert(AlertType.CONFIRMATION);
						confirm.setTitle("Please confirm");
						confirm.setContentText("Are you sure you want to delete " + session.name + "?");
						Optional<ButtonType> result = confirm.showAndWait();

						if (result.get() == ButtonType.OK) {
							LOGGER.info("Deleting session " + session.name);

							try {
								controller.deleteSession(session, mainStage);
							} catch (Exception e) {
								Alert error = new Alert(AlertType.ERROR);
								error.setTitle(e.getClass().toString());
								error.setHeaderText("Session " + session.name + " could not be saved!");
								error.setContentText(e.getLocalizedMessage());
								error.show();
							}
						} else {
							LOGGER.debug("Deleting session " + session.name + " abort");
						}

						evt.preventDefault();
					}
				}, false);
	}

	private String createSessionHTMLElement(Session session) {
		LOGGER.debug("Adding session " + session.name);

		StringBuilder htmlBuilder = new StringBuilder();

		htmlBuilder.append("<div class='col s12 m4'>");
		htmlBuilder.append("<div class='icon-block'>");
		htmlBuilder.append("<h2 class='center content-text'>");

		// Launch
		htmlBuilder.append("<a id='launch_" + session.id + "' href='" + session.name + "' class='content-link'>");
		htmlBuilder.append("<img alt='Launch session' src='../images/ic_launch_black_24dp_2x.png' />");
		htmlBuilder.append("</a>");

		// Delete
		htmlBuilder.append("<a id='delete_" + session.id + "' href='" + session.name + "' class='content-link'>");
		htmlBuilder.append("<img alt='Delete session' src='../images/ic_delete_forever_black_24dp_2x.png' />");
		htmlBuilder.append("</a>");
		htmlBuilder.append("</h2>");
		htmlBuilder.append("<h5 class='center content-header'>" + session.name + "</h5>");

		// TODO: Might add the path or some information here!
		// TODO: To get information refactor getData() from zimmermannj

		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");

		return htmlBuilder.toString();
	}
}