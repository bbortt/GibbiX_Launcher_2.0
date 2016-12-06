
package ch.gibb.iet.modul306.vmlauncher.view;

import java.util.Arrays;
import java.util.InputMismatchException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLSelectElement;

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

	private int selectIdCounter = 1;

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

		mainStage.setTitle(displayName);

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
		addFirstMachineSelect();

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

		((EventTarget) webView.getEngine().getDocument().getElementById("add_machine_button")).addEventListener("click",
				new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						selectIdCounter++;
						addNewMachineSelect();
					}
				}, false);

		((EventTarget) webView.getEngine().getDocument().getElementById("cancel_session_button"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						LOGGER.info("Cancel creating new session, changing to default modul-view");

						controller.loadView();

						evt.preventDefault();
					}
				}, false);

		((EventTarget) webView.getEngine().getDocument().getElementById("create_session_button"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						try {
							controller.createSession(createNewSessionFromGUIInputs());

							Alert information = new Alert(AlertType.INFORMATION);
							information.setTitle("Success");
							information.setContentText("New session successfully created!");
							information.show();
						} catch (Exception e) {
							LOGGER.error(e.getLocalizedMessage());

							Alert error = new Alert(AlertType.ERROR);
							error.setTitle(e.getClass().toString());
							error.setHeaderText("Failed to create new session.");
							error.setContentText(e.getLocalizedMessage());
							error.show();
						}

						evt.preventDefault();
					}
				}, false);

		super.bindFooterLinks();
	}

	private void addFirstMachineSelect() {
		addNewMachineSelect();
	}

	private void addNewMachineSelect() {
		StringBuilder builder = new StringBuilder();

		// <div class="input-field col s12">
		builder.append("<div class='input-field col s12'>");
		// <select id="session_machines_select">
		builder.append("<select id='machine_select_element_" + selectIdCounter + "'>");
		// <option value="" disabled selected>Choose virtual machines</option>
		builder.append("<option value='default' disabled selected>Add machine</option>");

		// Virtual machines
		// <option value="3">Option 3</option>
		Arrays.asList(givenMachines).forEach(machine -> {
			builder.append(createMachineOptionHTMLElement(machine));
		});

		// </select>
		builder.append("</select>");
		// <label>Add machine</label>
		builder.append("<label>Add machine</label>");
		// </div>
		builder.append("</div>");

		addHTMLToElementWithId("root_content_element", builder.toString());

		initalizeMaterialSelect();
	}

	private String createMachineOptionHTMLElement(XMLMachine machine) {
		LOGGER.debug("Creating select option for machine " + machine.name);

		StringBuilder optionBuilder = new StringBuilder();

		// <option value="3">Option 3</option>
		optionBuilder.append("<option value='" + machine.id + "'>" + machine.name + "</option>");

		return optionBuilder.toString();
	}

	private void initalizeMaterialSelect() {
		webView.getEngine().executeScript(
				"$(document).ready( function() { setTimeout( function() { $('select').material_select(); }, 50); });");
	}

	private Session createNewSessionFromGUIInputs() throws InputMismatchException, IndexOutOfBoundsException {
		validateInputs();

		Session newSession = new Session();
		newSession.id = controller.countExistingSessions() + 1;
		newSession.name = ((HTMLInputElement) webView.getEngine().getDocument().getElementById("session_name_input"))
				.getValue();

		for (int i = 0; i < selectIdCounter; i++) {
			int selectedIndex = ((HTMLSelectElement) webView.getEngine().getDocument()
					.getElementById("machine_select_element_" + String.valueOf(selectIdCounter))).getSelectedIndex();

			if (selectedIndex != 0 && selectedIndex - 1 <= givenMachines.length) {
				LOGGER.debug("Adding machine " + givenMachines[selectedIndex - 1].name + " to the session");
				newSession.addVirtualMachine(givenMachines[selectedIndex - 1]);
			} else {
				throw new IndexOutOfBoundsException("Please select an existing machine!");
			}
		}

		LOGGER.info("Creating new session " + newSession.name + " with " + newSession.getAllMachines().length
				+ " machines");

		return newSession;
	}

	private void validateInputs() throws InputMismatchException {
		// Check session name
		HTMLInputElement nameInputElement = (HTMLInputElement) webView.getEngine().getDocument()
				.getElementById("session_name_input");
		if (nameInputElement == null || nameInputElement.getValue() == null || nameInputElement.getValue() == "") {
			throw new InputMismatchException("Session name is not set correctly!");
		}
	}
}