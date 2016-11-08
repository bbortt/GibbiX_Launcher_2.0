package ch.gibb.iet.modul306.vmlauncher.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.controller.BootController;
import ch.gibb.iet.modul306.vmlauncher.controller.SessionController;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLSessions;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLSessions.Session;

public class SessionModel extends XMLModel<SessionController> {
	private static final Logger LOGGER = LogManager.getLogger(BootController.class);

	private File localFile;
	private List<Session> sessions;

	public Session getSession(int index) {
		return sessions.get(index);
	}

	public void addSession(Session session) {
		sessions.add(session);
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	public void removeSessionByIndex(int index) {
		sessions.remove(index);
	}

	public Session[] getAllSessions() {
		return sessions.toArray(new Session[sessions.size()]);
	}

	public SessionModel(SessionController controller, Object object) throws JAXBException, FileNotFoundException {
		super(controller, object);
		readAllSessions();
	}

	private void readAllSessions() throws JAXBException, FileNotFoundException {
		if (localFile == null) {
			localFile = new File("session_config.xml");
		}

		if (localFile.exists()) {
			LOGGER.info("Loading local session configuratin.");
			sessions = ((XMLSessions) xmlReader.unmarshal(localFile)).sessions;
		} else {
			throw new FileNotFoundException("Could not find any session config at " + localFile.getAbsolutePath());
		}
	}

	public void writeAllSessions() throws JAXBException, IOException {
		if (localFile == null) {
			localFile = new File("session_config.xml");
		}

		if (!localFile.exists()) {
			LOGGER.debug("Local file does not yet exist. Creating..");
			localFile.createNewFile();
		}

		LOGGER.info("Saving session configuration.");
		xmlWriter.marshal(new XMLSessions(sessions), localFile);
	}
}