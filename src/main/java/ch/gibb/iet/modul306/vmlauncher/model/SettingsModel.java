package ch.gibb.iet.modul306.vmlauncher.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import ch.gibb.iet.modul306.vmlauncher.controller.SettingsController;

@Component
public class SettingsModel extends AbstractModel<SettingsController> {
	private static final Logger LOGGER = LogManager.getLogger(SettingsModel.class);

	private File localFile;
	private TreeMap<String, Object> properties;

	public Object getProperty(String name) {
		return properties.get(name);
	}

	public void setProperty(String name, Object value) {
		if (properties.get(name) != null) {
			properties.remove(name);
		}

		properties.put(name, value);
	}

	public TreeMap<String, Object> getAllProperties() {
		return properties;
	}

	public SettingsModel() {
		super();
	}

	@SuppressWarnings("resource")
	public SettingsModel readRuntimeConfiguration() throws URISyntaxException, IOException {
		localFile = new File(this.getClass().getClassLoader().getResource("application.properties").toURI());
		BufferedReader reader = new BufferedReader(new FileReader(localFile));

		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains("=") && !line.contains("#") && !line.equals("") && !line.contains("info")) {
				if (properties == null) {
					properties = new TreeMap<String, Object>();
				}

				LOGGER.debug(line);
				String[] splittedValue = line.split("=");

				properties.put(splittedValue[0], splittedValue[1]);
			}
		}

		return this;
	}

	@SuppressWarnings("resource")
	public void savePropertiesChanged() throws JAXBException, IOException, URISyntaxException {
		if (properties == null || properties.size() == 0) {
			throw new IllegalArgumentException("No properties exist to save!");
		}

		if (localFile == null) {
			localFile = new File(this.getClass().getClassLoader().getResource("application.properties").toURI());
		}

		LOGGER.info("Saving runtime configuration to " + localFile.getAbsolutePath());

		BufferedReader reader = new BufferedReader(new FileReader(localFile));
		StringBuilder propertyString = new StringBuilder();

		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains("#") || line.contains("info")) {
				propertyString.append(line);
			} else if (line.equals("")) {
				propertyString.append("\n");
			} else if (line.contains("=")) {
				final String tmpLine = line;

				try {
					Entry<String, Object> foundEntry = properties.entrySet().stream()
							.filter(entry -> entry.getKey().equals(tmpLine.split("=")[0])).findFirst().get();

					if (foundEntry != null) {
						propertyString.append("\n" + foundEntry.getKey() + "=" + foundEntry.getValue());
					}
				} catch (NullPointerException e) {
					LOGGER.error(e.getLocalizedMessage());
				}
			}
		}

		FileWriter writer = new FileWriter(localFile);
		writer.write(propertyString.toString());
	}

	@SuppressWarnings("resource")
	public void overrideProperties(TreeMap<String, Object> properties2)
			throws JAXBException, IOException, URISyntaxException {
		if (properties2 == null || properties2.size() == 0) {
			throw new IllegalArgumentException("No properties exist to save!");
		}

		if (localFile == null) {
			localFile = new File(this.getClass().getClassLoader().getResource("application.properties").toURI());
		}

		LOGGER.info("Saving runtime configuration to " + localFile.getAbsolutePath());

		BufferedReader reader = new BufferedReader(new FileReader(localFile));
		StringBuilder propertyString = new StringBuilder();

		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains("#") || line.contains("info")) {
				propertyString.append(line);
			} else if (line.equals("")) {
				propertyString.append("\n");
			} else if (line.contains("=")) {
				final String tmpLine = line;

				try {
					Entry<String, Object> foundEntry = properties2.entrySet().stream()
							.filter(entry -> entry.getKey().equals(tmpLine.split("=")[0])).findFirst().get();

					if (foundEntry != null) {
						LOGGER.debug("Updating " + foundEntry.getKey() + " to " + foundEntry.getValue());
						propertyString.append("\n" + foundEntry.getKey() + "=" + foundEntry.getValue());
					}
				} catch (NullPointerException e) {
					LOGGER.error(e.getLocalizedMessage());
				}
			}
		}

		FileWriter writer = new FileWriter(localFile);
		writer.write(propertyString.toString());
	}
}