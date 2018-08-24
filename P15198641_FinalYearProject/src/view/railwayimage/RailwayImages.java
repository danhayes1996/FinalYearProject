package view.railwayimage;

import javafx.scene.image.Image;
import view.canvas.RailwayTrackPane;

public final class RailwayImages {

	//pointer
	public static final Image MOUSE_POINTER;
	
	//track images
	public static final Image TRACK_STRAIGHT_IMAGE;
	public static final Image TRACK_CURVE_IMAGE; 
	public static final Image TRACK_CROSS_IMAGE;
	public static final Image TRACK_STATION_IMAGE;
	
	//static initialise images
	static {
		//CHANGES MADE TO ALLOW IMAGES TO BE EXPORTED WITH SOURCE FILES SO THE EXECTUABLE CAN ACCESS THE IMAGES.
		TRACK_STRAIGHT_IMAGE = new Image(RailwayImages.class.getResourceAsStream("/img/straight.png"));
		
		TRACK_CURVE_IMAGE = new Image(RailwayImages.class.getResourceAsStream("/img/curve.png"));
		TRACK_CROSS_IMAGE = new Image(RailwayImages.class.getResourceAsStream("/img/cross.png"));
		TRACK_STATION_IMAGE = new Image(RailwayImages.class.getResourceAsStream("/img/station.png"));
		MOUSE_POINTER = new Image(RailwayImages.class.getResourceAsStream("/img/pointer.png"));
		/*
		Image tmp = null;
		try{
			tmp = new Image(new FileInputStream("res/img/straight.png"));
		}catch(IOException e){
			RailwayController.createAlert(AlertType.ERROR, "Error loading images", "", "An error occured while attempting to load straight.png");
			System.exit(1);
		}
		TRACK_STRAIGHT_IMAGE = tmp;
		
		tmp = null;
		try{
			tmp = new Image(new FileInputStream("res/img/curve.png"));
		}catch(IOException e){
			RailwayController.createAlert(AlertType.ERROR, "Error loading images", "", "An error occured while attempting to load curve.png");
			System.exit(1);
		}
		TRACK_CURVE_IMAGE = tmp;
		
		tmp = null;
		try{
			tmp = new Image(new FileInputStream("res/img/cross.png"));
		}catch(IOException e){
			RailwayController.createAlert(AlertType.ERROR, "Error loading images", "", "An error occured while attempting to load cross.png");
			System.exit(1);
		}
		TRACK_CROSS_IMAGE = tmp;
		
		tmp = null;
		try{
			tmp = new Image(new FileInputStream("res/img/station.png"));
		}catch(IOException e){
			RailwayController.createAlert(AlertType.ERROR, "Error loading images", "", "An error occured while attempting to load station.png");
			System.exit(1);
		}
		TRACK_STATION_IMAGE = tmp;
		
		tmp = null;
		try{
			tmp = new Image(new FileInputStream("res/img/pointer.png"));
		}catch(IOException e){
			RailwayController.createAlert(AlertType.ERROR, "Error loading images", "", "An error occured while attempting to load pointer.png");
			System.exit(1);
		}
		MOUSE_POINTER = tmp;
		*/
	}
	
	//get the image from a given type
	public static final Image getImageFromID(int id) {
		switch(id) {
			case RailwayTrackPane.TRACKPANE_STRAIGHT_ID : return TRACK_STRAIGHT_IMAGE;
			case RailwayTrackPane.TRACKPANE_CURVE_ID : return TRACK_CURVE_IMAGE;
			case RailwayTrackPane.TRACKPANE_CROSS_ID : return TRACK_CROSS_IMAGE;
			case RailwayTrackPane.TRACKPANE_STATION_ID : return TRACK_STATION_IMAGE;
		}
		return null;
	}
}
