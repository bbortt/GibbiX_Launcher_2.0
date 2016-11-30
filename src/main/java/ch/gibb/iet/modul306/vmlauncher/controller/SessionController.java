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

	@Autowired
	private SessionView sessionView;

	@Override
	public void loadView(Stage mainStage) {
		sessionView.display(mainStage, this);

		try {
			sessionModel.initalize(this);
			sessionView.setSessions(sessionModel.getAllSessions());
		} catch (NullPointerException | FileNotFoundException | JAXBException e) {
			sessionView.setSessionsNotFound();
			LOGGER.error(e.getLocalizedMessage());
		}

		try {
			sessionView.setXMLMachines(machineModel.getAllMachinesInWorkDirectory());
		} catch (IllegalArgumentException e) {
			sessionView.setMachinesNotFound();
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

	public void deleteSession(Session session, Stage mainStage) throws JAXBException, IOException {
		sessionModel.removeSession(session);
		sessionModel.saveSessionChanges();
		loadView(mainStage);
	}
}