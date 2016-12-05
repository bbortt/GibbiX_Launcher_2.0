package ch.gibb.iet.modul306.vmlauncher.view;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.xml.sax.SAXException;

import ch.gibb.iet.modul306.vmlauncher.controller.LauncherController;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MachineView extends AbstractView<LauncherController> {
	private static final Logger LOGGER = LogManager.getLogger(MachineView.class);

	private XMLMachine[] givenMachines;

	public void setXMLMachines(XMLMachine[] machines) {
		if (machines == null) {
			setMachinesNotFound();
			return;
		}

		this.givenMachines = machines;
	}

	private boolean machinesNotFound = false;

	public void setMachinesNotFound() {
		machinesNotFound = true;
	}

	private static String getContentElementId() {
		return "root_content_element";
	}

	public MachineView(Stage mainStage, LauncherController controller) {
		super(mainStage, controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void loadScene() {
		this.webView = new WebView();

		mainStage.setTitle(DISPLAY_NAME);

		if (mainStage.getScene() != null) {
			mainStage.setScene(new Scene(super.loadPage("machine_view.html"), mainStage.getScene().getWidth(),
					mainStage.getScene().getHeight()));
		} else {
			mainStage.setScene(new Scene(super.loadPage("machine_view.html")));
		}

		mainStage.show();
	}

	@Override
	protected void viewLoadedCallback() throws Exception {
		if (machinesNotFound) {
			showMachinesNotFount();
		} else {
			addMachinesToView();
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

		super.bindFooterLinks();
	}

	private void addMachinesToView() throws SAXException, IOException, ParserConfigurationException {
		LOGGER.info("Adding " + givenMachines.length + " machines to view");

		Arrays.asList(givenMachines).forEach(machine -> {
			addHTMLToElementWithId(getContentElementId(), createMachineHTMLElement(machine));
			addMachineLaunchClickListener(machine);
		});
	}

	private void addMachineLaunchClickListener(XMLMachine machine) {
		LOGGER.debug("Adding click listener to " + machine.name);

		((EventTarget) webView.getEngine().getDocument().getElementById(machine.id + "_" + machine.name))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						machine.launch();

						evt.preventDefault();
					}
				}, false);
	}

	private String createMachineHTMLElement(XMLMachine machine) {
		LOGGER.debug("Adding machine " + machine.name);

		StringBuilder htmlBuilder = new StringBuilder();

		// <div class="col s12 m4">
		htmlBuilder.append("<div class='col s12 m3 l2 height-limit'>");
		// <div class="icon-block">
		htmlBuilder.append("<div class='icon-block'>");
		// <a id="[ID]" href="[DETAIL_VIEW]" class="black-text">
		htmlBuilder.append("<a id='" + machine.id + "_" + machine.name + "' href='" + machine.path + "\\" + machine.file
				+ "' class='black-text'>");
		// <h2 class="center light-blue-text">
		htmlBuilder.append("<div class='center vm-icon'>");

		// TODO: What if machine not from gibbiX (external machines shall be
		// supported too!)
		// <img alt="Launch machine" src="../images/vm/[ICON]" />
		htmlBuilder
				.append("<img alt='Launch machine' src='../images/vm/" + machine.name + "-icon.png' class='vm-icon'/>");
		// </h2>
		htmlBuilder.append("</div>");
		// </a>
		htmlBuilder.append("</a>");
		// <h5 class="center">[NAME]</h5>
		htmlBuilder.append("<h5 class='center'>" + machine.name + "</h5>");

		// <p class="light">[SOME TEXT]</p>
		// TODO: Might add the path or some information here!
		// TODO: To get information refactor getData() from zimmermannj

		// </div>
		htmlBuilder.append("</div>");
		// </div>
		htmlBuilder.append("</div>");

		return htmlBuilder.toString();
	}

	private void showMachinesNotFount() throws SAXException, IOException, ParserConfigurationException {
		LOGGER.info("Inform user that no machine was found");

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
		// <img alt="Launch machine"
		// src="../images/ic_settings_black_24dp_2x.png"
		// />
		htmlBuilder.append("<img alt='Settings' src='../images/ic_settings_black_24dp_2x.png' />");
		// </h2>
		htmlBuilder.append("</h2>");
		// <h5 class="center">Settings</h5>
		htmlBuilder.append("<h5 class='center'>Settings</h5>");
		// </a>
		htmlBuilder.append("</a>");
		// <p class="light">[SOME TEXT]</p>
		htmlBuilder.append("<p class='light'>We were not able to find your GibbiX at the  default path on "
				+ controller.getBootController().getApplicationSettings().get("gibbix.path.default").toString()
				+ ". Please check your settings!</p>");
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
}