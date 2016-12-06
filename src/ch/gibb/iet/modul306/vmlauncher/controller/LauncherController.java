package ch.gibb.iet.modul306.vmlauncher.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.view.MachineView;
import javafx.stage.Stage;

public class LauncherController extends AbstractController {
	private static final Logger LOGGER = LogManager.getLogger(LauncherController.class);

	private MachineView machineView;

	public LauncherController(Stage mainStage, BootController bootController) {
		super(mainStage, bootController);

		machineView = new MachineView(mainStage, this);
	}

	@Override
	public void loadView() {
		machineView.display();

		try {
			machineView.setXMLMachines(getBootController().getMachineModel().getAllMachinesInWorkDirectory());
		} catch (IllegalArgumentException e) {
			machineView.setMachinesNotFound();
			LOGGER.error(e.getLocalizedMessage());
		}
	}
}