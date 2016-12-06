package ch.gibb.iet.modul306.vmlauncher.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.controller.SettingsController;

public class SettingsModel extends AbstractModel<SettingsController> {
	private static final Logger LOGGER = LogManager.getLogger(SettingsModel.class);

	private static final File SETTINGS_FILE = new File("conf/application.properties");

	private TreeMap<String, Object> properties;

	public Object getProperty(String name) {
		return this.properties.get(name);
	}

	public void setProperty(String name, Object value) {
		if (this.properties.get(name) != null) {
			this.properties.remove(name);
		}

		this.properties.put(name, value);
	}

	public TreeMap<String, Object> getAllProperties() {
		return this.properties;
	}

	public SettingsModel() throws FileNotFoundException {
		super();

		if (!SETTINGS_FILE.exists()) {
			throw new FileNotFoundException(
					"\"application.properties\" does not exist. Your installation might be corrupt!");
		}
	}

	public SettingsModel readRuntimeConfiguration() throws URISyntaxException, IOException {
		LOGGER.info("Reading local runtime configuration from " + SETTINGS_FILE.getAbsolutePath());

		BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE));

		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains("=") && !line.contains("#") && !line.equals("") && !line.contains("info")) {
				if (properties == null) {
					properties = new TreeMap<String, Object>();
				}

				String[] splittedValue = line.split("=");

				properties.put(splittedValue[0], splittedValue[1]);
			}
		}

		reader.close();

		return this;
	}

	public void savePropertiesChanged() throws JAXBException, IOException, URISyntaxException {
		if (properties == null || properties.size() == 0) {
			throw new IllegalArgumentException("No properties exist to save!");
		}

		LOGGER.info("Saving runtime configuration to " + SETTINGS_FILE.getAbsolutePath());

		BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE));
		StringBuilder propertyString = new StringBuilder();

		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains("#")) {
				propertyString.append(line + "\n");
			} else if (line.equals("")) {
				propertyString.append("\n");
			} else {
				final String tmpLine = line;

				try {
					Entry<String, Object> foundEntry = properties.entrySet().stream()
							.filter(entry -> entry.getKey().equals(tmpLine.split("=")[0])).findFirst().get();

					if (foundEntry != null) {
						LOGGER.debug("Updating " + foundEntry.getKey() + " to " + foundEntry.getValue());
						propertyString.append("\n" + foundEntry.getKey() + "=" + foundEntry.getValue());
					} else {
						propertyString.append(line);
					}
				} catch (NoSuchElementException e) {
					propertyString.append(line);
				}
			}
		}

		reader.close();

		FileWriter writer = new FileWriter(SETTINGS_FILE);
		writer.write(propertyString.toString());
		writer.close();
	}

	public void overrideProperties(TreeMap<String, Object> newProperties)
			throws JAXBException, IOException, URISyntaxException {
		if (newProperties == null || newProperties.size() == 0) {
			throw new IllegalArgumentException("No properties exist to save!");
		}

		this.properties = newProperties;

		savePropertiesChanged();

		// if (localFile == null) {
		// localFile = new
		// File(this.getClass().getClassLoader().getResource("application.properties").toURI());
		// }
		//
		// LOGGER.info("Saving runtime configuration to " +
		// localFile.getAbsolutePath());
		//
		// BufferedReader reader = new BufferedReader(new
		// FileReader(localFile));
		// StringBuilder propertyString = new StringBuilder();
		//
		// String line;
		// while ((line = reader.readLine()) != null) {
		// if (line.contains("#")) {
		// propertyString.append(line);
		// } else if (line.equals("")) {
		// propertyString.append("\n");
		// } else if (line.contains("=")) {
		// final String tmpLine = line;
		//
		// try {
		// Entry<String, Object> foundEntry = newProperties.entrySet().stream()
		// .filter(entry ->
		// entry.getKey().equals(tmpLine.split("=")[0])).findFirst().get();
		//
		// if (foundEntry != null) {
		// LOGGER.debug("Updating " + foundEntry.getKey() + " to " +
		// foundEntry.getValue());
		// propertyString.append("\n" + foundEntry.getKey() + "=" +
		// foundEntry.getValue());
		// } else {
		// propertyString.append(line);
		// }
		// } catch (NoSuchElementException e) {
		// propertyString.append(line);
		// }
		// }
		// }
		//
		// FileWriter writer = new FileWriter(localFile);
		// writer.write(propertyString.toString());
	}
}