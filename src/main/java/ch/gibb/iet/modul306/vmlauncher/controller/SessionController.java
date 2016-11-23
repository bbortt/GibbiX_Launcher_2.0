package ch.gibb.iet.modul306.vmlauncher.controller;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.model.SessionModel;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLSessions;
import javafx.stage.Stage;

public class SessionController extends AbstractController {
	private static final Logger LOGGER = LogManager.getLogger(SessionController.class);

	@SuppressWarnings("unused")
	private SessionModel sessionModul;

	public SessionController() {
		super();

		try {
			sessionModul = new SessionModel(this, XMLSessions.class);
		} catch (FileNotFoundException | JAXBException e) {
			LOGGER.error(e.getLocalizedMessage());
		}
	}

	@Override
	public void loadView(Stage mainStage) {
		// TODO Auto-generated method stub

	}
}