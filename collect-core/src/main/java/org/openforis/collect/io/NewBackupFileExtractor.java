package org.openforis.collect.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.openforis.collect.utils.Files;
import org.openforis.collect.utils.ZipFiles;
import org.openforis.concurrency.ProgressListener;

/**
 * 
 * @author S. Ricci
 *
 */
public class NewBackupFileExtractor implements Closeable {

	public static final String RECORD_FILE_DIRECTORY_NAME = "upload";
	
	//input variables
	private File file;
	
	//temporary variables
	private transient File tempUncompressedFolder;
	private transient ZipFile zipFile;

	public NewBackupFileExtractor(File file) throws ZipException, IOException {
		this.file = file;
	}

	public void init() throws IOException {
		init(null);
	}
	
	public void init(ProgressListener progressListener) throws IOException {
		tempUncompressedFolder = Files.createTempDirectory();
		zipFile = new ZipFile(file);
		ZipFiles.extract(zipFile, tempUncompressedFolder, progressListener);
	}
	
	public File extractInfoFile() {
		return extract(SurveyBackupJob.INFO_FILE_NAME);
	}
	
	public SurveyBackupInfo extractInfo() {
		try {
			File infoFile = extractInfoFile();
			SurveyBackupInfo info = SurveyBackupInfo.parse(new FileInputStream(infoFile));
			return info;
		} catch (Exception e) {
			throw new RuntimeException("Error extracting info file from archive", e);
		}
	}
	
	public File extractIdmlFile() {
		return extract(SurveyBackupJob.SURVEY_XML_ENTRY_NAME);
	}
	
	public File extract(String entryName) {
		return extract(entryName, true);
	}
	
	public File extract(String entryName, boolean required) {
		File folder = ZipFiles.getOrCreateEntryFolder(tempUncompressedFolder, entryName);
		String fileName = Files.extractFileName(entryName);
		File result = new File(folder, fileName);
		return result.exists() ? result: null;
	}

	public List<String> listSpeciesEntryNames() {
		List<String> entries = Files.listFileNamesInFolder(tempUncompressedFolder, SurveyBackupJob.SPECIES_FOLDER);
		return entries;
	}
	
	public List<File> extractFilesInPath(String folder) throws IOException {
		List<File> result = new ArrayList<File>();
		List<String> entryNames = Files.listFileNamesInFolder(tempUncompressedFolder, folder);
		for (String name : entryNames) {
			File tempFile = extract(name);
			result.add(tempFile);
		}
		return result;
	}
	
	public InputStream findEntryInputStream(String entryName) throws IOException {
		File file = extract(entryName, false);
		if (file == null) {
			return null;
		} else {
			return new FileInputStream(file);
		}
	}

	public boolean containsEntry(String name) {
		File file = extract(name, false);
		return file != null;
	}
	
	public boolean containsEntriesInPath(String path) {
		List<String> fileNames = listFilesInFolder(path);
		return ! fileNames.isEmpty();
	}
	
	public List<String> listFilesInFolder(String path) {
		return Files.listFileNamesInFolder(tempUncompressedFolder, path);
	}
	
	public List<String> getEntryNames() {
		List<String> result = new ArrayList<String>();
		Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
		while ( zipEntries.hasMoreElements() ) {
			ZipEntry zipEntry = zipEntries.nextElement();
			String name = zipEntry.getName();
			result.add(name);
		}
		return result;
	}
	
	public boolean isIncludingRecordFiles() {
		File recordFilesDir = new File(tempUncompressedFolder, RECORD_FILE_DIRECTORY_NAME);
		return recordFilesDir.exists() && recordFilesDir.isDirectory();
	}

	public boolean isOldFormat() {
		return ! containsEntry(SurveyBackupJob.INFO_FILE_NAME);
	}
	
	public int size() {
		return zipFile.size();
	}
	
	@Override
	public void close() throws IOException {
		IOUtils.closeQuietly(zipFile);
		FileUtils.deleteQuietly(tempUncompressedFolder);
	}

}