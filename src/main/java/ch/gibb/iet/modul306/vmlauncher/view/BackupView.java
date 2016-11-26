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

import ch.gibb.iet.modul306.vmlauncher.controller.BackupController;
import ch.gibb.iet.modul306.vmlauncher.model.BackupModel;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.lingala.zip4j.exception.ZipException;

public class BackupView extends AbstractView<BackupController> {
	private static final Logger LOGGER = LogManager.getLogger(BackupModel.class);

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

	public BackupView(Stage mainStage, BackupController controller) {
		super(mainStage, controller);
	}

	@Override
	protected void loadScene() {
		this.webView = new WebView();

		mainStage.setTitle(DISPLAY_NAME);

		if (mainStage.getScene() != null) {
			mainStage.setScene(new Scene(super.loadPage("backup_view.html"), mainStage.getScene().getWidth(),
					mainStage.getScene().getHeight()));
		} else {
			mainStage.setScene(new Scene(super.loadPage("backup_view.html")));
		}

		mainStage.show();
	}

	@Override
	protected void viewLoadedCallback()
			throws InterruptedException, SAXException, IOException, ParserConfigurationException {
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

		if (machinesNotFound) {
			showMachinesNotFount();
		} else {
			addMachinesToView(givenMachines);
		}

		bindFooterLinks();
	}

	private void addMachinesToView(XMLMachine[] machines)
			throws SAXException, IOException, ParserConfigurationException {
		LOGGER.info("Adding " + machines.length + " machines to view");

		Arrays.asList(machines).forEach(machine -> {
			addHTMLToElementWithId(getContentElementId(), createMachineHTMLElement(machine));
			addClickListener(machine);
		});
	}

	private void addClickListener(XMLMachine machine) {
		LOGGER.debug("Adding click listener to " + machine.name);

		// Export
		((EventTarget) webView.getEngine().getDocument().getElementById(machine.id + "_" + machine.name + "_export"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						LOGGER.info("Starting backup for " + machine.name);
						try {
							controller.backupMachine(machine, mainStage);
						} catch (ZipException e) {
							LOGGER.error(e.getLocalizedMessage());
						}
					}
				}, false);

		// Import
		((EventTarget) webView.getEngine().getDocument().getElementById(machine.id + "_" + machine.name + "_import"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						LOGGER.info("Restoring " + machine.name);
						try {
							controller.restoreMachine(machine, mainStage);
						} catch (ZipException e) {
							LOGGER.error(e.getLocalizedMessage());
						}
					}
				}, false);
	}

	private String createMachineHTMLElement(XMLMachine machine) {
		LOGGER.debug("Adding machine " + machine.name);

		StringBuilder htmlBuilder = new StringBuilder();

		// <div class="col s12 m4">
		htmlBuilder.append("<div class='col s12 m4'>");
		// <div class="icon-block">
		htmlBuilder.append("<div class='icon-block'>");

		// Export
		// <h2 class="center light-blue-text">
		htmlBuilder.append("<h2 class='center light-blue-text'>");
		// <a id="[ID]" href="[PATH]" class="black-text">
		htmlBuilder.append("<a id='" + machine.id + "_" + machine.name + "_export' href='" + machine.path
				+ "' class='black-text'>");
		// <img alt="Backup machine" src="images/ic_backup_black_24dp_2x.png" />
		htmlBuilder.append("<img alt='Backup machine' src='images/ic_backup_black_24dp_2x.png' />");
		// </a>
		htmlBuilder.append("</a>");

		// Import
		// <a id="[ID]" href="[PATH]" class="black-text">
		htmlBuilder.append("<a id='" + machine.id + "_" + machine.name + "_import' href='" + machine.path
				+ "' class='black-text'>");
		// <img alt="Restore machine"
		// src="images/ic_cloud_download_black_24dp_2x.png" />
		htmlBuilder.append("<img alt='Restore machine' src='images/ic_cloud_download_black_24dp_2x.png' />");
		// </a>
		htmlBuilder.append("</a>");
		// </h2>
		htmlBuilder.append("</h2>");

		// <h5 class="center">[NAME]</h5>
		htmlBuilder.append("<h5 class='center'>" + machine.name + "</h5>");

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
}