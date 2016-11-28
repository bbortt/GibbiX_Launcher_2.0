package ch.gibb.iet.modul306.vmlauncher.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.gibb.iet.modul306.vmlauncher.model.SessionModel;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLSessions.Session;
import ch.gibb.iet.modul306.vmlauncher.view.SessionView;
import javafx.stage.Stage;

@Component
public class SessionController extends AbstractController {
	private static final Logger LOGGER = LogManager.getLogger(SessionController.class);

	@Autowired
	private SessionModel sessionModel;

	@Override
	public void loadView(Stage mainStage) {
		SessionView view = new SessionView(mainStage, this);

		try {
			sessionModel.initalize(this);
			view.setSessions(sessionModel.getAllSessions());
		} catch (NullPointerException | FileNotFoundException | JAXBException e) {
			view.setSessionsNotFound();
			LOGGER.error(e.getLocalizedMessage());
		}

		try {
			view.setXMLMachines(machineModel.getAllMachinesInWorkDirectory());
		} catch (IllegalArgumentException e) {
			view.setMachinesNotFound();
			LOGGER.error(e.getLocalizedMessage());
		}
	}

	public int countExistingSessions() {
		return sessionModel.getAllSessions().length;
	}

	public void createSession(Session session) throws JAXBException, IOException {
		sessionModel.addSession(session);
		sessionModel.saveSessionChanges();
	}
}