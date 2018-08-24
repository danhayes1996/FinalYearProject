package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import controller.RailwayController;
import javafx.scene.control.Alert.AlertType;

/*
 * TODO:
 *  - login session
 */
public class RailwayModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private AppSettingsModel settings;
	private CanvasModel canvas;
		
	public RailwayModel(){
		canvas = new CanvasModel();
		settings = readSettingsFile();
	}
	
	private AppSettingsModel readSettingsFile() {
		AppSettingsModel settings = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Settings.conf"));
			settings = (AppSettingsModel) ois.readObject();
			if(settings == null) 
			ois.close();
		} catch (FileNotFoundException e) {
			settings = new AppSettingsModel(); //if settings file doesn't exits then create a new one.
		} catch (IOException e) {
			RailwayController.createAlert(AlertType.ERROR, "Input/Output Error", null, "An IO Exception occured.\nA potential solution is to delete the Settings.conf file.");
		} catch (ClassNotFoundException e) {
			RailwayController.createAlert(AlertType.ERROR, "Fatal Program Error", null, "Parts of the application seem to be missing, try reinstalling the application.");
		}
		return settings;
	}
	
	//getters
	public AppSettingsModel getAppSettingsModel() {
		return settings;
	}
	
	public CanvasModel getCanvasModel() {
		return canvas;
	}

}
