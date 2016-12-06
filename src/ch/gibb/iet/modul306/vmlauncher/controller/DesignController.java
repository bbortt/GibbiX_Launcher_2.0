package ch.gibb.iet.modul306.vmlauncher.controller;

import ch.gibb.iet.modul306.vmlauncher.view.DesignView;
import javafx.stage.Stage;

public class DesignController extends AbstractController {
	private DesignView designView;

	public DesignController(Stage mainStage, BootController bootController) {
		super(mainStage, bootController);

		designView = new DesignView(mainStage, this);
	}

	@Override
	public void loadView() {
		designView.display();
	}
}