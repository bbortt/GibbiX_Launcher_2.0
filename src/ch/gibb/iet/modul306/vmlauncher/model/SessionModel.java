package ch.gibb.iet.modul306.vmlauncher.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.controller.SessionController;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLSessions;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLSessions.Session;

public class SessionModel extends XMLModel<SessionController> {
	private static final Logger LOGGER = LogManager.getLogger(SessionModel.class);

	private static final File SESSION_CONFIG = new File("conf/session_config.xml");
	private List<Session> sessions;

	public Session getSession(int index) {
		return sessions.get(index);
	}

	public void addSession(Session session) {
		if (sessions == null) {
			sessions = new ArrayList<>();
		}

		sessions.add(session);
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	public void removeSessionByIndex(int index) {
		sessions.remove(index);
	}

	public Session[] getAllSessions() {
		if (sessions == null) {
			sessions = new ArrayList<>();
		}

		return sessions.toArray(new Session[sessions.size()]);
	}

	public SessionModel(SessionController controller) throws JAXBException {
		super(controller);
		super.initialize(XMLSessions.class);

		updateSessions();
	}

	private void updateSessions() {
		try {
			readAllLocalSessions(SESSION_CONFIG);
		} catch (FileNotFoundException | JAXBException e) {
			LOGGER.warn(e.getLocalizedMessage());
		}
	}

	public SessionModel readSessionsFromFile(File sessionFile) throws FileNotFoundException, JAXBException {
		readAllLocalSessions(sessionFile);
		return this;
	}

	private void readAllLocalSessions(File sessionFile) throws JAXBException, FileNotFoundException {
		if (sessionFile.exists()) {
			LOGGER.debug("Loading local session configuration from " + sessionFile.getAbsolutePath());
			sessions = ((XMLSessions) xmlReader.unmarshal(sessionFile)).sessions;
		} else {
			throw new FileNotFoundException(
					"Could not find any session config at " + sessionFile.getAbsolutePath() + ".");
		}
	}

	public void saveChanges() throws JAXBException, IOException {
		saveChangesToFile(SESSION_CONFIG);
	}

	public void saveSessionsToFile(File sessionFile) throws JAXBException, IOException {
		saveChangesToFile(sessionFile);
	}

	private void saveChangesToFile(File sessionFile) throws JAXBException, IOException {
		if (sessions == null) {
			throw new IllegalArgumentException("No sessions exist to save!");
		}

		if (!sessionFile.exists()) {
			LOGGER.debug("Local file does not exist yet. Creating..");
			sessionFile.createNewFile();
		}

		LOGGER.info("Saving session configuration to " + sessionFile.getAbsolutePath());
		xmlWriter.marshal(new XMLSessions(sessions), sessionFile);

		updateSessions();
	}
}