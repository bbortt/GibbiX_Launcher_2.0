package ch.gibb.iet.modul306.vmlauncher.model.objects;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class XMLMachine {
	private static final Logger LOGGER = LogManager.getLogger(XMLMachine.class);

	@XmlAttribute(name = "id", required = false)
	public int id;

	@XmlAttribute(name = "Name", required = true)
	public String name;

	@XmlElement(name = "Installation_path", required = true)
	public String path;

	@XmlElement(name = "Config_file", required = true)
	public String file;

	@XmlElement(name = "Is_default", required = false)
	public boolean isDefault;

	public void launch() {
		String fullPath = path + "\\" + file;
		LOGGER.info("Launching machine located at " + fullPath);
		try {
			Desktop.getDesktop().open(new File(fullPath));
		} catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage());
		}
	}
}