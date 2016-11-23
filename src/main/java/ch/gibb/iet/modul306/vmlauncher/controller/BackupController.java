package ch.gibb.iet.modul306.vmlauncher.controller;

import ch.gibb.iet.modul306.vmlauncher.model.BackupModel;
import javafx.stage.Stage;

public class BackupController extends AbstractController {
	@SuppressWarnings("unused")
	private BackupModel backupModul;

	public BackupController() {
		super();

		backupModul = new BackupModel(this);
	}

	@Override
	public void loadView(Stage mainStage) {
		// TODO Auto-generated method stub

	}
}