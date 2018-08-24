package model;

import java.io.File;
import java.io.Serializable;

public class AppSettingsModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private File defaultPath;
	
	public AppSettingsModel() { }
	
	public void setDefaultPath(File defaultPath) {
		this.defaultPath = defaultPath;
	}
	
	public File getDefaultPath() {
		return defaultPath;
	}
	
}
