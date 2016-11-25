package ch.gibb.iet.modul306.vmlauncher.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.stage.Stage;

public abstract class AbstractController {
	@Autowired
	protected BootController bootController;

	public BootController getBootController() {
		return bootController;
	}

	public abstract void loadView(Stage mainStage);
}