
package ch.gibb.iet.modul306.vmlauncher.view;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

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

public class CreateSessionsView extends AbstractView<SessionController> {
	private static final Logger LOGGER = LogManager.getLogger(CreateSessionsView.class);

	private XMLMachine[] givenMachines;

	public void setXMLMachines(XMLMachine[] machines) {
		this.givenMachines = machines;
	}

	public CreateSessionsView(Stage mainStage, SessionController controller) {
		super(mainStage, controller);
	}

	@Override
	protected void loadScene() {
		this.webView = new WebView();

		mainStage.setTitle(DISPLAY_NAME);

		if (mainStage.getScene() != null) {
			mainStage.setScene(new Scene(super.loadPage("create_session_view.html"), mainStage.getScene().getWidth(),
					mainStage.getScene().getHeight()));
		} else {
			mainStage.setScene(new Scene(super.loadPage("create_session_view.html")));
		}

		mainStage.show();
	}

	@Override
	protected void viewLoadedCallback() throws Exception {
		addNewMachineSelect();

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

		((EventTarget) webView.getEngine().getDocument().getElementById("cancel_session_button"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						LOGGER.info("Cancel creating new session, changing to default modul-view");

						controller.loadView(mainStage);

						evt.preventDefault();
					}
				}, false);

		((EventTarget) webView.getEngine().getDocument().getElementById("create_session_button"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						try {
							controller.createSession(createNewSessionFromGUIInputs());

							controller.loadView(mainStage);

							Alert information = new Alert(AlertType.INFORMATION);
							information.setTitle("Success");
							information.setContentText("New session successfully created!");
							information.show();
						} catch (JAXBException | IOException e) {
							LOGGER.error(e.getLocalizedMessage());

							Alert error = new Alert(AlertType.ERROR);
							error.setTitle(e.getClass().toString());
							error.setContentText(e.getLocalizedMessage());
							error.show();
						}

						evt.preventDefault();
					}
				}, false);

		super.bindFooterLinks();
	}

	// TODO: The select ist not displayd??
	private void addNewMachineSelect() {
		StringBuilder builder = new StringBuilder();

		// <div class="input-field col s12">
		builder.append("<div class='input-field col s12'>");
		// <select class="machine_select_element" id="session_machines_select">
		builder.append("<select class='machine_select_element' id='session_machines_select'>");
		// <option value="" disabled selected>Choose virtual machines</option>
		builder.append("<option value='default' disabled selected>Choose virtual machines</option>");
		// <option value="3">Option 3</option>
		Arrays.asList(givenMachines).forEach(machine -> {
			builder.append(createMachineOptionHTMLElement(machine));
		});
		// </select>
		builder.append("</select>");
		// </div>
		builder.append("</div>");

		addHTMLToElementWithId("root_content_element", builder.toString());
	}

	private String createMachineOptionHTMLElement(XMLMachine machine) {
		LOGGER.debug("Creating select option for machine " + machine.name);

		StringBuilder optionBuilder = new StringBuilder();

		// <option value="3">Option 3</option>
		optionBuilder.append("<option value='" + machine.id + "'>" + machine.name + "</option>");

		return optionBuilder.toString();
	}

	private Session createNewSessionFromGUIInputs() {
		// TODO Auto-generated method stub
		return null;
	}
}