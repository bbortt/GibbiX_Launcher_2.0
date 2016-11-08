package ch.gibb.iet.modul306.vmlauncher.model.objects;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class XMLSessions {
	@XmlElement
	public List<Session> sessions;

	public XMLSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public class Session {

	}
}