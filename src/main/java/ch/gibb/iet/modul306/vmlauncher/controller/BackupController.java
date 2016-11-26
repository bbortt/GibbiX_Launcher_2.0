package ch.gibb.iet.modul306.vmlauncher.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
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

	public void restoreMachine(XMLMachine machine, Stage mainStage) throws ZipException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(getGibbiXRootPath()));
		fileChooser.setTitle("Choose zip to restore");

		File source = fileChooser.showOpenDialog(mainStage);

		Arrays.asList(new File(machine.path).listFiles()).forEach(subFile -> {
			if (subFile.isDirectory()) {
				try {
					FileUtils.deleteDirectory(subFile);
				} catch (IOException e) {
					LOGGER.fatal(e.getLocalizedMessage());
				}
			} else {
				subFile.delete();
			}
		});

		new BackupModel(this).uncompressFile(source.getAbsolutePath(), machine.path, null);
	}
}