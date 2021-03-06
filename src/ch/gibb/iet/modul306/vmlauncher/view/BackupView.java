package ch.gibb.iet.modul306.vmlauncher.view;

import java.io.IOException;
import java.net.URISyntaxException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.lingala.zip4j.exception.ZipException;

public class BackupView extends AbstractView<BackupController> {
	private static final Logger LOGGER = LogManager.getLogger(BackupModel.class);

	private XMLMachine[] givenMachines;
	private boolean machinesNotFound = false;

	public void setXMLMachines(XMLMachine[] machines) {
		if (machines == null) {

			setMachinesNotFound();
			return;
		}

		this.givenMachines = machines;
	}

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

		mainStage.setTitle(displayName);

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
		loadAndApplyTheme();

		if (machinesNotFound) {
			try {
				showMachinesNotFount();
			} catch (URISyntaxException e) {
				LOGGER.fatal(e.getLocalizedMessage());
			}
		} else {
			addMachinesToView(givenMachines);
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

		bindFooterLinks();
	}

	private void addMachinesToView(XMLMachine[] machines)
			throws SAXException, IOException, ParserConfigurationException {
		LOGGER.info("Adding " + machines.length + " machines to view");

		Arrays.asList(machines).forEach(machine -> {
			addHTMLToElementWithId(getContentElementId(), createMachineHTMLElement(machine));
			addMachineClickListener(machine);
		});
	}

	private void addMachineClickListener(XMLMachine machine) {
		LOGGER.debug("Adding click listener to " + machine.name);

		// Export
		((EventTarget) webView.getEngine().getDocument().getElementById(machine.id + "_" + machine.name + "_export"))
				.addEventListener("click", new EventListener() {
					@Override
					public void handleEvent(Event evt) {
						LOGGER.info("Starting backup for " + machine.name);
						try {
							controller.backupMachine(machine, mainStage);
						} catch (ZipException | URISyntaxException e) {
							LOGGER.error(e.getLocalizedMessage());

							Alert error = new Alert(AlertType.ERROR);
							error.setTitle(e.getClass().toString());
							error.setHeaderText("Error while backing up machine " + machine.name);
							error.setContentText(e.getLocalizedMessage());
							error.show();
						}

						evt.preventDefault();
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
						} catch (ZipException | URISyntaxException e) {
							LOGGER.error(e.getLocalizedMessage());

							Alert error = new Alert(AlertType.ERROR);
							error.setTitle(e.getClass().toString());
							error.setHeaderText("Error while restoring " + machine.name);
							error.setContentText(e.getLocalizedMessage());
							error.show();
						}

						evt.preventDefault();
					}
				}, false);
	}

	private String createMachineHTMLElement(XMLMachine machine) {
		LOGGER.debug("Adding machine " + machine.name);

		StringBuilder htmlBuilder = new StringBuilder();

		htmlBuilder.append("<div class='col s12 m3 l2'>");
		htmlBuilder.append("<div class='icon-block'>");

		// Export
		htmlBuilder.append("<h2 class='center content-text'>");
		htmlBuilder.append("<a id='" + machine.id + "_" + machine.name + "_export' href='" + machine.path
				+ "' class='content-link'>");
		htmlBuilder.append("<img alt='Backup machine' src='../images/ic_backup_black_24dp_2x.png' />");
		htmlBuilder.append("</a>");

		// Import
		htmlBuilder.append("<a id='" + machine.id + "_" + machine.name + "_import' href='" + machine.path
				+ "' class='content-link'>");
		htmlBuilder.append("<img alt='Restore machine' src='../images/ic_cloud_download_black_24dp_2x.png' />");
		htmlBuilder.append("</a>");
		htmlBuilder.append("</h2>");

		htmlBuilder.append("<h5 class='center content-header'>" + machine.name + "</h5>");

		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");

		return htmlBuilder.toString();
	}

	private void showMachinesNotFount()
			throws SAXException, IOException, ParserConfigurationException, URISyntaxException {
		LOGGER.info("Inform user that no machine was found");

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
}