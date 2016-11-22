package ch.gibb.iet.modul306.vmlauncher.model.objects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Sessions")
public class XMLSessions {
	@XmlElement(name = "Session", required = true)
	public List<Session> sessions;

	public XMLSessions() {
		// Used by JAXBContext
	}

	public XMLSessions(List<Session> sessions) {
		this.sessions = (ArrayList<Session>) sessions;
	}

	public static class Session {
		@XmlAttribute(name = "Id", required = true)
		public int id;

		@XmlElement(name = "Name", required = true)
		public String name;

		@XmlElement(name = "Machine", required = true)
		private List<XMLMachine> virtualMachines;

		public XMLMachine getVirtualMachine(int index) {
			return virtualMachines.get(index);
		}

		public void addVirtualMachine(XMLMachine virtualMachine) {
			virtualMachines.add(virtualMachine);
		}

		public void removeVirtualMachine(XMLMachine virtualMachine) {
			virtualMachines.remove(virtualMachine);
		}

		public void removeVirtualMachineByIndex(int index) {
			virtualMachines.remove(index);
		}

		public XMLMachine[] getAllMachines() {
			return virtualMachines.toArray(new XMLMachine[virtualMachines.size()]);
		}
	}
}