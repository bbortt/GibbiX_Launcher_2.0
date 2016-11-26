package ch.gibb.iet.modul306.vmlauncher.controller;

import org.springframework.beans.factory.annotation.Autowired;

import ch.gibb.iet.modul306.vmlauncher.model.MachineModel;
import javafx.stage.Stage;

public abstract class AbstractController {
	@Autowired
	protected BootController bootController;

	public BootController getBootController() {
		return bootController;
	}

	@Autowired
	protected MachineModel machineModel;

	public String getGibbiXRootPath() {
		return machineModel.getGibbiXRootPath();
	}

	public abstract void loadView(Stage mainStage);
}