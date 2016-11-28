package ch.gibb.iet.modul306.vmlauncher.view;

import java.util.Arrays;
import java.util.NoSuchElementException;

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

		mainStage.setTitle(DISPLAY_NAME);

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
		if (machinesNotFound) {
			showMachinesNotFount();
		} else if (sessionsNotFound) {
			showSessionsNotFound();
		} else {
			addSessionsToView();
		}

		bindClickEventToClass("home_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				LOGGER.info("Chaning to boot-modul");
				controller.getBootController().loadView(mainStage);
			}
		});

		bindClickEventToClass("settings_menu_link", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				LOGGER.warn("Settings modul does not exist yet!");
			}
		});

		((EventTarget) webView.getEngine().getDocument().getElementById("create_session_button"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						CreateSessionsView view = new CreateSessionsView(mainStage, controller);
						view.setXMLMachines(givenMachines);

						evt.preventDefault();
					}
				}, false);

		bindFooterLinks();
	}

	private void showMachinesNotFount() {
		LOGGER.info("Inform user that no machine was found");

		// Disable "create" button
		webView.getEngine().getDocument().getElementById("create_session_button").setAttribute("disabled", null);

		// <div class="col s12 m4"><div class="icon-block"><!-- Left filler
		// --></div></div>
		String leftFiller = "<div class='col s12 m4'><div class='icon-block'><!-- Left filler --></div></div>";
		addHTMLToElementWithId(getContentElementId(), leftFiller);

		StringBuilder htmlBuilder = new StringBuilder();

		// <div class="col s12 m4">
		htmlBuilder.append("<div class='col s12 m4'>");
		// <div class="icon-block">
		htmlBuilder.append("<div class='icon-block'>");
		// <a class="settings_menu_link black-text" href="settings_modul">
		htmlBuilder.append("<a class='settings_menu_link black-text' href='settings_modul'>");
		// <h2 class="center light-blue-text">
		htmlBuilder.append("<h2 class='center light-blue-text'>");
		// TODO: Download google-material "settings"-icon
		// <img alt="Launch machine" src="images/ic_settings_black_24dp_2x.png"
		// />
		htmlBuilder.append("<img alt='Settings' src='images/ic_settings_black_24dp_2x.png' />");
		// </h2>
		htmlBuilder.append("</h2>");
		// <h5 class="center">Settings</h5>
		htmlBuilder.append("<h5 class='center'>Settings</h5>");
		// </a>
		htmlBuilder.append("</a>");
		// <p class="light">[SOME TEXT]</p>
		htmlBuilder.append("<p class='light'>We were not able to find your GibbiX at the  default path on "
				+ controller.getGibbiXRootPath() + ". Please check your settings!</p>");
		// </div>
		htmlBuilder.append("</div>");
		// </div>
		htmlBuilder.append("</div>");

		addHTMLToElementWithId(getContentElementId(), htmlBuilder.toString());

		// <div class="col s12 m4"><div class="icon-block"><!-- Right filler
		// --></div></div>
		String rightFiller = "<div class='col s12 m4'><div class='icon-block'><!-- Right filler --></div></div>";
		addHTMLToElementWithId(getContentElementId(), rightFiller);
	}

	private void showSessionsNotFound() {
		LOGGER.info("Inform user that no sessions were found");

		// <div class="col s12 m4"><div class="icon-block"><!-- Left filler
		// --></div></div>
		String leftFiller = "<div class='col s12 m4'><div class='icon-block'><!-- Left filler --></div></div>";
		addHTMLToElementWithId(getContentElementId(), leftFiller);

		StringBuilder htmlBuilder = new StringBuilder();

		// <div class="col s12 m4">
		htmlBuilder.append("<div class='col s12 m4'>");
		// <p class="light">[SOME TEXT]</p>
		htmlBuilder.append(
				"<p class='light'>We were not able to find any session. Create your first session by clicking the button below.</p>");
		// </div>
		htmlBuilder.append("</div>");

		addHTMLToElementWithId(getContentElementId(), htmlBuilder.toString());

		// <div class="col s12 m4"><div class="icon-block"><!-- Right filler
		// --></div></div>
		String rightFiller = "<div class='col s12 m4'><div class='icon-block'><!-- Right filler --></div></div>";
		addHTMLToElementWithId(getContentElementId(), rightFiller);
	}

	private void addSessionsToView() {
		LOGGER.info("Adding " + givenSessions.length + " sessions to view");

		Arrays.asList(givenSessions).forEach(session -> {
			addHTMLToElementWithId(getContentElementId(), createSessionHTMLElement(session));
			addSessionLaunchClickListener(session);
		});
	}

	private void addSessionLaunchClickListener(Session session) {
		LOGGER.debug("Adding click listener to " + session.name);

		((EventTarget) webView.getEngine().getDocument().getElementById(String.valueOf(session.id)))
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
	}

	private String createSessionHTMLElement(Session session) {
		LOGGER.debug("Adding session " + session.name);

		StringBuilder htmlBuilder = new StringBuilder();

		// <div class="col s12 m4">
		htmlBuilder.append("<div class='col s12 m4'>");
		// <div class="icon-block">
		htmlBuilder.append("<div class='icon-block'>");
		// <a id="[ID]" href="[DETAIL_VIEW]" class="black-text">
		htmlBuilder.append("<a id='" + session.id + "' href='" + session.name + "' class='black-text'>");
		// <h2 class="center light-blue-text">
		htmlBuilder.append("<h2 class='center light-blue-text'>");
		// <img alt="Launch machine" src="images/ic_launch_black_24dp_2x.png" />
		htmlBuilder.append("<img alt='Launch session' src='images/ic_launch_black_24dp_2x.png' />");
		// </h2>
		htmlBuilder.append("</h2>");
		// </a>
		htmlBuilder.append("</a>");
		// <h5 class="center">[NAME]</h5>
		htmlBuilder.append("<h5 class='center'>" + session.name + "</h5>");

		// <p class="light">[SOME TEXT]</p>
		// TODO: Might add the path or some information here!
		// TODO: To get information refactor getData() from zimmermannj

		// </div>
		htmlBuilder.append("</div>");
		// </div>
		htmlBuilder.append("</div>");

		return htmlBuilder.toString();
	}
}