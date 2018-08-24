package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.CanvasModel;
import view.canvas.RailwayTrackPane;

public class TrackPaneController {

	private RailwayTrackPane trackPane;
	private CanvasModel canvasModel;
	
	public TrackPaneController(RailwayTrackPane trackPane, CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
		this.trackPane = trackPane;
		
		assignHandlers();
	}
	
	private void assignHandlers() {
		trackPane.setMousePointerBtnAction(new MousePointerBtnHandler());
		trackPane.setStraightBtnAction(new StraightBtnHandler());
		trackPane.setCurveBtnAction(new CurveBtnHandler());
		trackPane.setStationBtnAction(new StationBtnHandler());
		trackPane.setCrossBtnAction(new CrossBtnHandler());
	}
	
	private class MousePointerBtnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			System.out.println("Mouse Pointer Button Handler Active");
			trackPane.setPointerBtnSelected();
			canvasModel.setSelectedTrackPaneType(RailwayTrackPane.TRACKPANE_MOUSE_POINTER_ID);
		}
	}
	
	private class StraightBtnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			System.out.println("Straight Button Handler Active");
			trackPane.setStraightBtnSelected();
			canvasModel.setSelectedTrackPaneType(RailwayTrackPane.TRACKPANE_STRAIGHT_ID);
		}
	}
	
	private class CurveBtnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			System.out.println("Curve Button Handler Active");
			trackPane.setCurveBtnSelected();
			canvasModel.setSelectedTrackPaneType(RailwayTrackPane.TRACKPANE_CURVE_ID);
		}
	}
	
	private class StationBtnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			System.out.println("Station Button Handler Active");
			trackPane.setStationBtnSelected();
			canvasModel.setSelectedTrackPaneType(RailwayTrackPane.TRACKPANE_STATION_ID);
		}
	}
	
	private class CrossBtnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			System.out.println("Cross Button Handler Active");
			trackPane.setCrossBtnSelected();
			canvasModel.setSelectedTrackPaneType(RailwayTrackPane.TRACKPANE_CROSS_ID);
		}
	}
}
