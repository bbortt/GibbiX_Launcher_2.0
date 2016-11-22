package ch.gibb.iet.modul306.vmlauncher.model;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.controller.BackupController;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class BackupModel extends AbstractModel<BackupController> {
	private static final Logger LOGGER = LogManager.getLogger(BackupModel.class);
	private static final String SUFFIX = ".7zip";

	public BackupModel(BackupController controller) {
		super(controller);
	}

	public void backupMachine(XMLMachine machine, String backupDestination, String password) throws ZipException {
		LOGGER.info("Creating backup for " + machine.name);

		if (password == null) {
			compressFolder(machine.path, backupDestination + "\\" + machine.name + SUFFIX, null);
		} else {
			compressFolder(machine.path, backupDestination + "\\" + machine.name + SUFFIX, null);
		}
	}

	public void compressFolder(String sourceDir, String outputFile, String password) throws ZipException {
		ZipFile newZip = new ZipFile(outputFile);

		ZipParameters params = new ZipParameters();
		params.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		params.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);

		if (password != null) {
			params.setPassword(password);
		}

		newZip.addFolder(sourceDir, params);
	}

	public void uncompressFile(String sourceFile, String outputDir, String password) throws ZipException {
		ZipFile zip = new ZipFile(sourceFile);

		if (zip.isEncrypted() && password == null) {
			throw new IllegalArgumentException("Cannot open zip. Requires password!");
		} else if (zip.isEncrypted() && password != null) {
			zip.setPassword(password);
		}

		zip.extractAll(outputDir);
	}
}