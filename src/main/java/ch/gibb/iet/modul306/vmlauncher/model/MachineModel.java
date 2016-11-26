package ch.gibb.iet.modul306.vmlauncher.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;

@Component
public class MachineModel {
	private static final Logger LOGGER = LogManager.getLogger(MachineModel.class);

	@Value("${gibbix.path.default}")
	private String GIBBIX_PATH;
	@Value("${gibbix.machines.path.default}")
	private String WORK_DIRECTORY;
	@Value("${gibbix.machines.file.suffix}")
	private String VM_FILE_SUFFIX;

	public String getGibbiXRootPath() {
		return GIBBIX_PATH + WORK_DIRECTORY;
	}

	public XMLMachine[] getAllMachinesInWorkDirectory() {
		File workDir = new File(GIBBIX_PATH + WORK_DIRECTORY);
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
			if (subFile.isFile() && subFile.getName().contains(VM_FILE_SUFFIX)) {
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