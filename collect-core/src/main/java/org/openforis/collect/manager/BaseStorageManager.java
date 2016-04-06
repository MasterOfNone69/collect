package org.openforis.collect.manager;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openforis.collect.model.Configuration;
import org.openforis.collect.model.Configuration.ConfigurationItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author S. Ricci
 *
 */
public abstract class BaseStorageManager implements Serializable {

	private static Log LOG = LogFactory.getLog(BaseStorageManager.class);
	
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_BASE_DATA_SUBDIR = "data";
	private static final String DEFAULT_BASE_TEMP_SUBDIR = "temp";
	private static final String DEFAULT_BASE_WEBAPPS_SUBDIR = "webapps";

	private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
	private static final String CATALINA_BASE = "catalina.base";

	@Autowired
	protected transient ConfigurationManager configurationManager;

	/**
	 * Directory in which files will be stored
	 */
	protected File storageDirectory;
	
	/**
	 * Default path in which files will be stored.
	 * If not specified, java temp folder or catalina base temp folder
	 * will be used as storage directory.
	 */
	private String defaultRootStoragePath;
	
	/**
	 * Default subfolder used together with the default storage path to determine the default storage folder
	 */
	private String defaultSubFolder;
	
	public BaseStorageManager() {
		this(null, null);
	}
	
	public BaseStorageManager(String defaultSubFolder) {
		this(null, defaultSubFolder);
	}
	
	public BaseStorageManager(String defaultRootStoragePath, String defaultSubFolder) {
		this.defaultRootStoragePath = defaultRootStoragePath;
		this.defaultSubFolder = defaultSubFolder;
	}
	
	protected static File getTempFolder() {
		return getReadableSysPropLocation(JAVA_IO_TMPDIR, null);
	}
	
	protected static File getCatalinaBaseDataFolder() {
		return getReadableSysPropLocation(CATALINA_BASE, DEFAULT_BASE_DATA_SUBDIR);
	}
	
	protected static File getCatalinaBaseTempFolder() {
		return getReadableSysPropLocation(CATALINA_BASE, DEFAULT_BASE_TEMP_SUBDIR);
	}

	protected static File getCatalinaBaseWebappsFolder() {
		return getReadableSysPropLocation(CATALINA_BASE, DEFAULT_BASE_WEBAPPS_SUBDIR);
	}
	
	protected static String getCatalinaBaseWebappsFolderPath() {
		return getSysPropPath(CATALINA_BASE, DEFAULT_BASE_WEBAPPS_SUBDIR);
	}
	
	protected static String getSysPropPath(String sysProp, String subDir) {
		String base = System.getProperty(sysProp);
		if ( base == null ) {
			return null;
		}
		String path = base;
		if ( subDir != null ) {
			path = path + File.separator + subDir;
		}
		return path;
	}
	
	protected static File getReadableSysPropLocation(String sysProp, String subDir) {
		String path = getSysPropPath(sysProp, subDir);
		if (path == null) {
			return null;
		} else {
			return getLocationIfAccessible(path);
		}
	}

	protected static File getLocationIfAccessible(String path) {
		File result = new File(path);
		if ( (result.exists() || result.mkdirs()) && result.canWrite() ) {
			return result;
		} else {
			return null;
		}
	}
	
	protected void initStorageDirectory(ConfigurationItem configurationItem) {
		initStorageDirectory(configurationItem, true);
	}
	
	protected boolean initStorageDirectory(ConfigurationItem configurationItem, boolean createIfNotExists) {
		Configuration configuration = configurationManager.getConfiguration();
		
		String customStoragePath = configuration.get(configurationItem);
		
		if ( StringUtils.isBlank(customStoragePath) ) {
			storageDirectory = getDefaultStorageDirectory();
		} else {
			storageDirectory = new File(customStoragePath);
		}
		boolean result = storageDirectory.exists();
		if (! result) {
			if (createIfNotExists) {
				result = storageDirectory.mkdirs();
			}
		}
		
		if (LOG.isInfoEnabled() ) {
			if (result) {
				LOG.info(String.format("Using %s directory: %s", configurationItem.getLabel(), storageDirectory.getAbsolutePath()));
			} else {
				LOG.info(String.format("%s directory %s does not exist or it's not accessible", configurationItem.getLabel(), storageDirectory.getAbsolutePath()));
			}
		}
		return result;
	}

	protected File getDefaultStorageRootDirectory() {
		if ( defaultRootStoragePath == null ) {
			File rootDir = getCatalinaBaseDataFolder();
			if ( rootDir == null ) {
				rootDir = getTempFolder();
			}
			return rootDir;
		} else {
			return new File(defaultRootStoragePath );
		}
	}
	
	public String getDefaultRootStoragePath() {
		return defaultRootStoragePath;
	}
	
	public void setDefaultRootStoragePath(String defaultStoragePath) {
		this.defaultRootStoragePath = defaultStoragePath;
	}
	
	public File getDefaultStorageDirectory() {
		File rootDir = getDefaultStorageRootDirectory();
		if ( rootDir == null ) {
			return null;
		} else if ( defaultSubFolder == null ) {
			return rootDir;
		} else {
			return new File(rootDir, defaultSubFolder);
		}
	}
	
	protected void setDefaultSubFolder(String defaultSubFolder) {
		this.defaultSubFolder = defaultSubFolder;
	}
}
