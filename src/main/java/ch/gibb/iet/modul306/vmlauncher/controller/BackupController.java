package ch.gibb.iet.modul306.vmlauncher.controller;

import java.awt.Window;
import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import ch.gibb.iet.modul306.vmlauncher.model.BackupModel;
import ch.gibb.iet.modul306.vmlauncher.model.MachineModel;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;
import ch.gibb.iet.modul306.vmlauncher.view.BackupView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.lingala.zip4j.exception.ZipException;

public class BackupController extends AbstractController {
	private static final Logger LOGGER = Logger.getLogger(BackupController.class);

	@Autowired
	private MachineModel machineModel;

	public BackupController() {
		super();
	}

	@Override
	public void loadView(Stage mainStage) {
		BackupView view = new BackupView(mainStage, this);

		try {
			view.setXMLMachines(machineModel.getAllMachinesInWorkDirectory());
		} catch (IllegalArgumentException e) {
			view.setMachinesNotFound();
			LOGGER.error(e.getLocalizedMessage());
		}
	}

	public void backupMachine(XMLMachine machine, Stage mainStage) throws ZipException {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File(getGibbiXRootPath()));
		directoryChooser.setTitle("Choose backup destination");

		File destination = directoryChooser.showDialog(mainStage);

		new BackupModel(this).backupMachine(machine, destination, null);
	}
}