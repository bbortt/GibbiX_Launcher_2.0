package ch.gibb.iet.modul306.vmlauncher.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.controller.BackupController;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;

public class BackupModel extends AbstractModel<BackupController> {
	private static final Logger LOGGER = LogManager.getLogger(BackupModel.class);
	private static final String SUFFIX = ".7zip";

	public BackupModel(BackupController controller) {
		super(controller);
	}

	public void backupMachine(XMLMachine machine, File backupDestination) throws FileNotFoundException, IOException {
		LOGGER.info("Creating backup for " + machine.name);

		compressFolder(machine.path, backupDestination.getAbsolutePath() + "\\" + machine.name + SUFFIX);
	}

	public void compressFolder(String sourceDir, String outputFile) throws IOException, FileNotFoundException {
		ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(outputFile));
		compressFolderRecursive(sourceDir, sourceDir, zipFile);
		zipFile.flush();
		zipFile.close();
	}

	private void compressFolderRecursive(String rootDir, String sourceDir, ZipOutputStream out)
			throws IOException, FileNotFoundException {
		for (File file : new File(sourceDir).listFiles()) {
			if (file.isDirectory()) {
				compressFolderRecursive(rootDir, sourceDir + file.getName() + File.separator, out);
			} else {
				ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + file.getName());
				out.putNextEntry(entry);

				// FileInputStream in = new FileInputStream(sourceDir +
				// file.getName());
				FileInputStream in = new FileInputStream(sourceDir + File.separator + file.getName());
				IOUtils.copy(in, out);
				in.close();
			}
		}
	}
}