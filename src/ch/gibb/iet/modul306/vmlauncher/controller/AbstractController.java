package ch.gibb.iet.modul306.vmlauncher.controller;

import javafx.stage.Stage;

public abstract class AbstractController {
	protected Stage mainStage;
	protected BootController bootController;

	public Stage getMainStage() {
		return this.mainStage;
	}

	public BootController getBootController() {
		return this.bootController;
	}

	public AbstractController(Stage mainStage) {
		this.mainStage = mainStage;
	}

	public AbstractController(Stage mainStage, BootController bootController) {
		this.mainStage = mainStage;
		this.bootController = bootController;
	}

	public abstract void loadView();
}