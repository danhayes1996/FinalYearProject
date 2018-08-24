package view.canvas;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import view.railwayimage.RailwayImages;

public class RailwayTrackPane extends FlowPane {

	//ids used to define which button has been pressed.
	public static final int TRACKPANE_NONE_ID = -1,
							TRACKPANE_MOUSE_POINTER_ID = 0, 
							TRACKPANE_STRAIGHT_ID = 1, 
							TRACKPANE_CURVE_ID = 2,
							TRACKPANE_CROSS_ID = 3,
							TRACKPANE_STATION_ID = 4,
							TRACKPANE_Y_ID = 5;
	
	private ToggleButton tbPointer, tbStraight, tbCurve90, tbStation, tbCross;
	
	public RailwayTrackPane(){
		//padding, border, vgap
		this.setPadding(new Insets(5));
		this.setVgap(5); //vertical space between each button
		this.setPrefWrapLength(70); //when to wrap
		
		//only one button in a toggle group can be active at any one time.
		ToggleGroup tg = new ToggleGroup();

		//initalize with no text, instead use the mouse pointer image
		tbPointer = new ToggleButton(null, new ImageView(RailwayImages.MOUSE_POINTER));
		tbPointer.setToggleGroup(tg);
		tbPointer.setMinWidth(52); //set manually because it wouldn't fill track pane fully...
		tbPointer.setSelected(true); //this button is on by default
		
		tbStraight = new ToggleButton("", new ImageView(RailwayImages.TRACK_STRAIGHT_IMAGE)); 
		tbStraight.setToggleGroup(tg);

		tbCurve90 = new ToggleButton("", new ImageView(RailwayImages.TRACK_CURVE_IMAGE));
		tbCurve90.setToggleGroup(tg);

		tbStation = new ToggleButton("", new ImageView(RailwayImages.TRACK_STATION_IMAGE));
		tbStation.setToggleGroup(tg);
		
		tbCross = new ToggleButton("", new ImageView(RailwayImages.TRACK_CROSS_IMAGE));
		tbCross.setToggleGroup(tg);
		
		//add all buttons to this ("this" is a FlowPane)
		this.getChildren().addAll(tbPointer, tbStraight, tbCurve90, tbStation, tbCross);
	}
	
	//setters to determine which button is active.
	public void setPointerBtnSelected() {
		tbPointer.setSelected(true);
	}
	
	public void setStraightBtnSelected() {
		tbStraight.setSelected(true);
	}
	
	public void setCurveBtnSelected() {
		tbCurve90.setSelected(true);
	}
	
	public void setStationBtnSelected() {
		tbStation.setSelected(true);
	}
	
	public void setCrossBtnSelected() {
		tbCross.setSelected(true);
	}

	//event handler functions
	public void setMousePointerBtnAction(EventHandler<ActionEvent> e) {
		tbPointer.setOnAction(e);
	}
	
	public void setStraightBtnAction(EventHandler<ActionEvent> e) {
		tbStraight.setOnAction(e);
	}
	
	public void setCurveBtnAction(EventHandler<ActionEvent> e) {
		tbCurve90.setOnAction(e);
	}
	
	public void setStationBtnAction(EventHandler<ActionEvent> e) {
		tbStation.setOnAction(e);
	}
	
	public void setCrossBtnAction(EventHandler<ActionEvent> e) {
		tbCross.setOnAction(e);
	}
}
