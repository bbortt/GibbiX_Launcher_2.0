package ch.gibb.iet.modul306.vmlauncher.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;
import ch.gibb.iet.modul306.vmlauncher.view.MachineView;
import javafx.stage.Stage;

@Component
public class LauncherController extends AbstractController {
	private static final Logger LOGGER = LogManager.getLogger(LauncherController.class);

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

	public Map<String, Map<String, String>> virtualMachines;

	// public LauncherController() {
	// // TODO: Eingabe eines Paths, falls Default Pfad nicht vorhanden!
	// String path = "D:/1_work";
	// File f = new File(path);
	// if (!(f.exists() && f.isDirectory())) {
	// path = "C:/Users/vmadmin/workspace/1_work";
	// }
	//
	// this.VM_PATH = path;
	// // this.virtualMachines = getData();
	// }

	private Map<String, Map<String, String>> getData() {
		try {
			Map<String, Map<String, String>> vmMap = new HashMap<String, Map<String, String>>();
			File[] machines = getVirtualMachineDirectories();

			for (File a : machines) {
				if (a.isDirectory()) {
					vmMap.put(a.getName(), getVirtualMachine(a));
				}
			}

			return vmMap;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("resource")
	private Map<String, String> getVirtualMachine(File direcory) throws IOException {

		String lineInfo;
		Map<String, String> map = new HashMap<String, String>();

		BufferedReader br = new BufferedReader(new FileReader(
				GIBBIX_PATH + WORK_DIRECTORY + "/" + direcory.getName() + "/" + direcory.getName() + ".vmx"));

		while ((lineInfo = br.readLine()) != null) {

			if (!(lineInfo.contains("#")) && !(lineInfo.isEmpty())) {
				String[] val = lineInfo.split(" = ");
				String[] val2 = val[1].split("\"");
				if (val2.length == 2) {
					map.put(val[0], val2[1]);
				}
			}
		}
		map.put("VM_PATH", GIBBIX_PATH + WORK_DIRECTORY + "/" + direcory.getName() + "/" + direcory.getName() + ".vmx");
		return map;
	}

	private File[] getVirtualMachineDirectories() {
		return new File(GIBBIX_PATH + WORK_DIRECTORY).listFiles();
	}

	public void startVirtualMachine(Map<String, String> map) throws IOException {
		if (!map.isEmpty()) {
			String pathToVMPlayer = "C:/Program Files (x86)/VMware/VMware Player/vmplayer.exe";
			File f1 = new File(pathToVMPlayer);

			if (!(f1.exists() && f1.canExecute())) {
				File[] files = new File("/vmplayer.exe").listFiles();
				if (files != null && files.length > 0) {
					pathToVMPlayer = files[0].getPath();
				} else {
					pathToVMPlayer = "";
				}
			}

			if (new File(pathToVMPlayer).canExecute()) {
				File f2 = new File(map.get("VM_PATH"));

				if (f2.exists() && f2.canExecute()) {
					new ProcessBuilder().command(pathToVMPlayer, map.get("VM_PATH")).start();
				}
			}
		}
	}

	@Override
	public void loadView(Stage mainStage) {
		MachineView view = new MachineView(mainStage, this);

		try {
			view.setXMLMachines(getAllMachinesInWorkDirectory());
		} catch (IllegalArgumentException e) {
			view.setMachinesNotFound();
			LOGGER.error(e.getLocalizedMessage());
		}
	}
}