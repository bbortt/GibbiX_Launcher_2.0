package ch.gibb.iet.modul306.vmlauncher.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import ch.gibb.iet.modul306.vmlauncher.controller.SessionController;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLSessions;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLSessions.Session;

@Component
public class SessionModel extends XMLModel<SessionController> {
	private static final Logger LOGGER = LogManager.getLogger(SessionModel.class);

	private File localFile;
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

		if (sessions.size() == 0) {
			sessions = null;
		}
	}

	public void removeSessionByIndex(int index) {
		sessions.remove(index);

		if (sessions.size() == 0) {
			sessions = null;
		}
	}

	public Session[] getAllSessions() {
		return sessions.toArray(new Session[sessions.size()]);
	}

	public SessionModel() {
		super();
	}

	// public SessionModel(SessionController controller) throws
	// FileNotFoundException, JAXBException {
	// super(controller);
	// localFile=new File("session_config.xml");
	// readAllLocalSessions(localFile);
	// }

	public SessionModel initalize(SessionController controller) throws JAXBException, FileNotFoundException {
		super.initalize(controller, XMLSessions.class);
		localFile = new File("session_config.xml");
		readAllLocalSessions(localFile);
		return this;
	}

	public SessionModel readSessionsFromFile(File sessionFile) throws FileNotFoundException, JAXBException {
		this.localFile = sessionFile;
		readAllLocalSessions(sessionFile);
		return this;
	}

	private void readAllLocalSessions(File sessionFile) throws JAXBException, FileNotFoundException {
		if (localFile == null) {
			localFile = sessionFile;
		}

		if (localFile.exists()) {
			LOGGER.debug("Loading local session configuration from " + sessionFile.getAbsolutePath());
			sessions = ((XMLSessions) xmlReader.unmarshal(localFile)).sessions;
		} else {
			throw new FileNotFoundException(
					"Could not find any session config at " + sessionFile.getAbsolutePath() + ".");
		}
	}

	public void saveSessionChanges() throws JAXBException, IOException {
		if (sessions == null || sessions.size() == 0) {
			throw new IllegalArgumentException("No sessions exist to save!");
		}

		if (localFile == null) {
			localFile = new File("session_config.xml");
		}

		if (!localFile.exists()) {
			LOGGER.debug("Local file does not exist yet. Creating..");
			localFile.createNewFile();
		}

		LOGGER.info("Saving session configuration to " + localFile.getAbsolutePath());
		xmlWriter.marshal(new XMLSessions(sessions), localFile);
	}
}