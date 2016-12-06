package ch.gibb.iet.modul306.vmlauncher.model;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.controller.BootController;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;

public class MachineModel {
	private static final Logger LOGGER = LogManager.getLogger(MachineModel.class);

	private String gibbiXPath;
	private String workDirectory;
	private String vmFileSuffix;

	public MachineModel(BootController controller) {
		try {
			gibbiXPath = controller.getApplicationSettings().getProperty("gibbix.path.default").toString();
			workDirectory = controller.getApplicationSettings().getProperty("gibbix.machines.path.default").toString();
			vmFileSuffix = controller.getApplicationSettings().getProperty("gibbix.machines.file.suffix").toString();
		} catch (URISyntaxException | IOException e) {
			LOGGER.error(e.getLocalizedMessage());
		}
	}

	public String getGibbiXRootPath() {
		return gibbiXPath + workDirectory;
	}

	public XMLMachine[] getAllMachinesInWorkDirectory() {
		File workDir = new File(gibbiXPath + workDirectory);
		if (!workDir.exists() || !workDir.isDirectory()) {
			throw new IllegalArgumentException("Work directory at " + workDir + " does not exist!");
		}

		XMLMachine[] foundMachines = searchMachinesRecursive(workDir);
		if (foundMachines == null) {
			return null;
		}

		// Add id's (all time different)
		int idCounter = 1;
		for (XMLMachine xmlMachine : foundMachines) {
			xmlMachine.id = idCounter;
			idCounter++;
		}

		return foundMachines;
	}

	private XMLMachine[] searchMachinesRecursive(File currentDir) {
		if (!currentDir.exists() || !currentDir.isDirectory()) {
			return null;
		}

		for (File subFile : currentDir.listFiles()) {
			if (subFile.isFile() && subFile.getName().contains(vmFileSuffix)
					&& !subFile.getName().contains(vmFileSuffix + "f")) {
				LOGGER.debug("Got machine at " + subFile.getAbsolutePath());

				XMLMachine newMachine = new XMLMachine();
				newMachine.name = subFile.getName().substring(0, subFile.getName().lastIndexOf("."));
				newMachine.path = subFile.getAbsolutePath().substring(0, subFile.getAbsolutePath().lastIndexOf("\\"));
				newMachine.file = subFile.getName();

				return new XMLMachine[] { newMachine };
			}
		}

		List<XMLMachine> collected = new ArrayList<>();
		Arrays.asList(currentDir.listFiles()).stream().filter(subFile -> subFile.isDirectory())
				.forEach(subDirectory -> {
					XMLMachine[] fromSubs = searchMachinesRecursive(subDirectory);
					if (fromSubs != null) {
						Arrays.asList(fromSubs).forEach(machine -> {
							collected.add(machine);
						});
					}
				});

		if (collected.size() == 0) {
			return null;
		}

		return collected.toArray(new XMLMachine[collected.size()]);
	}
}