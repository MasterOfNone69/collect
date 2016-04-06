package org.openforis.collect.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/**
 * 
 * @author S. Ricci
 *
 */
public class Files {
	
	private static final String PATH_SEPARATOR_PATTERN = "[\\\\|/]";

	public static File writeToTempFile(String text, String tempFilePrefix, String tempFileSuffix) throws IOException {
		File file = File.createTempFile(tempFilePrefix, tempFileSuffix);
		Writer writer = null;
		try {
			writer = new FileWriter(file);
			IOUtils.write(text.getBytes(), writer, "UTF-8");			
		} finally {
			IOUtils.closeQuietly(writer);
		}
		return file;
	}
	
	public static File createTempDirectory() throws IOException {
		File temp = File.createTempFile("temp", Long.toString(System.nanoTime()));
		if (! temp.delete()) {
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}
		if (! temp.mkdir()) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}

		return (temp);
	}

	public static File getOrCreateFolder(File parentDestinationFolder, String path) {
		String[] entryParts = path.split(PATH_SEPARATOR_PATTERN);
		File folder = parentDestinationFolder;
		for (int i = 0; i < entryParts.length; i++) {
			String part = entryParts[i];
			folder = new File(folder, part);
		}
		if (! folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}

	public static String extractFileName(String entryName) {
		String name = FilenameUtils.getName(entryName);
		return name;
	}

	public static List<String> listFileNamesInFolder(File parentFolder) {
		return listFileNamesInFolder(parentFolder, null);
	}
	
	public static List<String> listFileNamesInFolder(File parentFolder, String folderPath) {
		List<File> files = listFilesInFolder(parentFolder, folderPath);
		List<String> result = extractNames(files);
		return result;
	}
	
	private static List<String> extractNames(List<File> files) {
		List<String> result = new ArrayList<String>(files.size());
		for (File file : files) {
			result.add(file.getName());
		}
		Collections.sort(result);
		return result;
	}

	public static List<File> listFilesInFolder(File parentFolder) {
		return listFilesInFolder(parentFolder, null);
	}
	
	public static List<File> listFilesInFolder(File parentFolder, String folderPath) {
		List<File> result = new ArrayList<File>();
		File folder = folderPath == null ? parentFolder : 
			Files.getOrCreateFolder(parentFolder, folderPath);
		File[] files = folder.listFiles();
		for (File file : files) {
			result.add(file);
		}
		return result;
	}
	
	public static void eraseFileContent(File file) throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.write("");
			writer.flush();
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
	
}
