
package ch.gibb.iet.modul306.vmlauncher.view;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

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
		addFirstMachineSelect();

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

	private void addFirstMachineSelect() {
		addNewMachineSelect(selectIdCounter);
		addMachineSelectedListener();
	}

	private void addNewMachineSelect(int id) {
		StringBuilder builder = new StringBuilder();

		// <div class="input-field col s12">
		builder.append("<div class='input-field col s12'>");
		// <select class="machine_select_element" id="session_machines_select">
		builder.append("<select id='machine_select_element_" + id + "'>");
		// <option value="" disabled selected>Choose virtual machines</option>
		builder.append("<option value='default' disabled selected>Add machine</option>");
		// <option value="3">Option 3</option>
		Arrays.asList(givenMachines).forEach(machine -> {
			builder.append(createMachineOptionHTMLElement(machine));
		});
		// </select>
		builder.append("</select>");
		// <label>Materialize Select</label>
		builder.append("<label>Add machine</label>");
		// </div>
		builder.append("</div>");

		addHTMLToElementWithId("root_content_element", builder.toString());
	}

	private String createMachineOptionHTMLElement(XMLMachine machine) {
		LOGGER.debug("Creating select option for machine " + machine.name);

		StringBuilder optionBuilder = new StringBuilder();

		// <option class="machine_option_element" value="3">Option 3</option>
		optionBuilder.append(
				"<option class='machine_option_element' value='" + machine.id + "'>" + machine.name + "</option>");

		return optionBuilder.toString();
	}

	private void addMachineSelectedListener() {
		bindClickEventToClass("machine_option_element", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				LOGGER.debug("Event catched.");

				// Might not further be usefull if input-cast success'
				XMLMachine selectedMachine = Arrays.asList(givenMachines).stream()
						.filter(machine -> evt.getTarget().toString().contains(String.valueOf(machine.id))).findFirst()
						.get();

				LOGGER.debug("Selected machine was " + selectedMachine.name);

				// webView.getEngine().getDocument().getElementById("machine_select_element_"
				// + selectIdCounter)
				// .setAttribute("name", selectedMachine.name);

				selectIdCounter++;
				addNewMachineSelect(selectIdCounter);
				addMachineSelectedListener();
			}
		});
	}

	private Session createNewSessionFromGUIInputs() {
		// TODO: Validate inputs!!

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
				throw new IndexOutOfBoundsException("Index " + selectedIndex + " does not exist!");
			}
		}

		LOGGER.info("Creating new session " + newSession.name + " with " + newSession.getAllMachines().length
				+ " machines");

		return newSession;
	}
}