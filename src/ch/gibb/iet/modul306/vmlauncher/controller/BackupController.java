package ch.gibb.iet.modul306.vmlauncher.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.model.BackupModel;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;
import ch.gibb.iet.modul306.vmlauncher.view.BackupView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.lingala.zip4j.exception.ZipException;

public class BackupController extends AbstractController {
	private static final Logger LOGGER = Logger.getLogger(BackupController.class);

	private BackupModel backupModel;
	BackupView backupView;

	public BackupController(Stage mainStage, BootController bootController) {
		super(mainStage, bootController);

		backupModel = new BackupModel(this);
		backupView = new BackupView(mainStage, this);
	}

	@Override
	public void loadView() {
		backupView.display();

		try {
			backupView.setXMLMachines(getBootController().getMachineModel().getAllMachinesInWorkDirectory());
		} catch (IllegalArgumentException e) {
			LOGGER.warn(e.getLocalizedMessage());
			backupView.setMachinesNotFound();
		}
	}

	public void backupMachine(XMLMachine machine, Stage mainStage) throws ZipException, URISyntaxException {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(
				new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
		directoryChooser.setTitle("Choose backup destination");

		File destination = directoryChooser.showDialog(mainStage);

		if (backupModel == null) {
			backupModel = new BackupModel(this);
		}

		Alert information = new Alert(AlertType.INFORMATION);
		information.setTitle("Backup");
		information.setContentText(
				"Backup runs in asynchronous task. You can still continue to browse your other machines. You will be informed if the task completed.");
		information.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					backupModel.backupMachine(machine, destination, null);
				} catch (ZipException e) {
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Error");
					error.setHeaderText("An error occured while backing up " + machine.name + ".");
					error.setContentText(e.getLocalizedMessage());
					error.show();

					LOGGER.fatal(e.getLocalizedMessage());
				}

				LOGGER.info("Successfully backed up " + machine.name);

				// TODO: Fix "this is not an fx-thread"
				Alert done = new Alert(AlertType.INFORMATION);
				done.setTitle("Finished backup");
				done.setContentText("Machine " + machine.name + " successfully backed up.");
				done.show();
			}
		}).start();

		Alert done = new Alert(AlertType.INFORMATION);
		done.setTitle("Finished backup");
		done.setContentText("Machine " + machine.name + " successfully backed up.");
		done.show();
	}

	public void restoreMachine(XMLMachine machine, Stage mainStage) throws ZipException, URISyntaxException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(
				new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
		fileChooser.setTitle("Choose zip to restore");

		File source = fileChooser.showOpenDialog(mainStage);

		if (backupModel == null) {
			backupModel = new BackupModel(this);
		}

		Alert information = new Alert(AlertType.INFORMATION);
		information.setTitle("Backup");
		information.setContentText(
				"Restore runs in asynchronous task. You can still continue to browse your other machines. You will be informed if the task completed.");
		information.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				Arrays.asList(new File(machine.path).listFiles()).forEach(subFile -> {
					if (subFile.isDirectory()) {
						try {
							FileUtils.deleteDirectory(subFile);
						} catch (IOException e) {
							Alert error = new Alert(AlertType.ERROR);
							error.setTitle("Error");
							error.setHeaderText("An error occured while restoring up " + machine.name + ".");
							error.setContentText(e.getLocalizedMessage());
							error.show();

							LOGGER.fatal(e.getLocalizedMessage());
						}
					} else {
						subFile.delete();
					}
				});

				try {
					backupModel.uncompressFile(source.getAbsolutePath(), machine.path, null);
				} catch (ZipException e) {
					LOGGER.fatal(e.getLocalizedMessage());
				}

				// TODO: Fix "this is not an fx-thread"
				Alert done = new Alert(AlertType.INFORMATION);
				done.setTitle("Finished backup");
				done.setContentText("Machine " + machine.name + " successfully backed up.");
				done.show();
			}
		}).start();
	}
}