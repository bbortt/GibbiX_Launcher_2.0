package ch.gibb.iet.modul306.vmlauncher.model.objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class XMLMachine {
	@XmlAttribute(name = "Name", required = true)
	public String name;

	@XmlElement(name = "Installation_path", required = true)
	public String path;

	@XmlElement(name = "Config_file", required = true)
	public String file;

	@XmlElement(name = "Is_default", required = true)
	public boolean isDefault;
}