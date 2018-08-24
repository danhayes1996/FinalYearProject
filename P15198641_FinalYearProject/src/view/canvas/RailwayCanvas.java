package view.canvas;

import java.util.Collection;

import controller.TrackController;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import model.renderables.Renderable;
import model.renderables.Track;
import model.renderables.TrackGroup;

public class RailwayCanvas extends Pane {

	//how big the grid blocks are.
	public static final int TRACK_SIZE = 32;
	
	//the box used to select multiple tracks
	private Rectangle selectBox;
	
	public RailwayCanvas(){
		//grid is on by default
		this.getStyleClass().add("railway-canvas-grid-on");
	}
	
	//clear the canvas and add back the selectBox
	public void clear() {
		this.getChildren().clear();
		this.getChildren().add(selectBox); //re-add the select box
	}
	
	//add a track to the canvas
	public void addTrack(Track t) {
		t.assureImageIsSet(); //assure the image is set with position and rotation
		ImageView iv = t.getTrackImage();
		
		//add handlers if they haven't been set yet
		if(iv.getOnMousePressed() == null) {
			iv.setOnMousePressed(new TrackController.TrackMousePressHandler());
			iv.setOnMouseDragged(new TrackController.TrackMouseDragHandler());
			iv.setOnMouseReleased(new TrackController.TrackMouseReleaseHandler());
		}
		t.setTrackImage(iv);
		//add track image to canvas
		this.getChildren().add(t.getTrackImage());
	}
	
	//add renderables to the canvas
	public void addRenderables(Collection<Renderable> rs) {
		for(Renderable r : rs) {
			if(r instanceof Track) addTrack((Track) r);
			else if(r instanceof TrackGroup) {
				for(Track track : (TrackGroup) r)
					addTrack(track);
			}
		}
	}
	
	//remove a renderable from the canvas
	public void removeRenderable(Renderable r) {
		if(r instanceof Track)
			this.getChildren().remove(((Track) r).getTrackImage());
		else if (r instanceof TrackGroup) {
			for(Track track : (TrackGroup) r)
				this.getChildren().remove(track.getTrackImage());
		}
	}

	//add select box reference to the canvas
	public void setSelectBox(Rectangle selectBox) {
		this.selectBox = selectBox;
		this.getChildren().add(selectBox); //add select box to the canvas
	}
	
	//make sure the selected renderables aren't past the with and height of canvas,
	//if they are then make width and/or height bigger
	public void assureSize(Collection<Renderable> renderables) {
		//get track position with the largest x and y value.
		double maxW = 0, maxH = 0;
		for(Renderable r : renderables) {
			if(r instanceof Track) {
				Track t = (Track) r;
				if(t.getX() + TRACK_SIZE > maxW) maxW = t.getX() + TRACK_SIZE;
				if(t.getY() + TRACK_SIZE > maxH) maxH = t.getY() + TRACK_SIZE;
			}else if(r instanceof TrackGroup) {
				for(Track t : (TrackGroup) r) {
					if(t.getX() + TRACK_SIZE > maxW) maxW = t.getX() + TRACK_SIZE;
					if(t.getY() + TRACK_SIZE > maxH) maxH = t.getY() + TRACK_SIZE;
				}
			}
		}
		
		//set width and height with extra padding
		if(maxW + TRACK_SIZE >= this.getWidth()) this.setMinWidth(maxW + (3 * TRACK_SIZE));
		if(maxH + TRACK_SIZE >= this.getHeight()) this.setMinHeight(maxH + (3 * TRACK_SIZE));
	}
	
	//event handler functions
	public void setMousePressHandler(EventHandler<MouseEvent> e) {
		this.setOnMousePressed(e);
	}

	public void setMouseReleaseHandler(EventHandler<MouseEvent> e) {
		this.setOnMouseReleased(e);
	}

	public void setMouseDragHandler(EventHandler<MouseEvent> e) {
		this.setOnMouseDragged(e);
	}
	
	public void setMouseMoveHandler(EventHandler<MouseEvent> e) {
		this.setOnMouseMoved(e);
	}
}
