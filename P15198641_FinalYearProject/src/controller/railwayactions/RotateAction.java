package controller.railwayactions;

import java.util.Set;

import model.CanvasModel;
import model.renderables.Renderable;
import model.renderables.Track;
import model.renderables.TrackGroup;
import view.canvas.RailwayCanvas;

public class RotateAction implements RailwayAction{

	private Set<Renderable> renderables;
	private int rotation;
	
	public RotateAction(Set<Renderable> renderables, int rotation) {
		this.renderables = renderables;
		this.rotation = rotation;
	}
	
	public RailwayAction doAction(RailwayCanvas railwayCanvas, CanvasModel canvasModel) {
		//get lowest x and y value, and max x and y 
		int x = Integer.MAX_VALUE;
		int y = Integer.MAX_VALUE;
		int w = 0;
		int h = 0;			
		for(Renderable r : renderables) {
			if(r instanceof Track) {
				Track t = (Track) r;
				if(t.getX() < x) x = (int) t.getX();
				if(t.getY() < y) y = (int) t.getY();
				if(t.getX() + RailwayCanvas.TRACK_SIZE > w) w = (int) t.getX() + RailwayCanvas.TRACK_SIZE;
				if(t.getY() + RailwayCanvas.TRACK_SIZE > h) h = (int) t.getY() + RailwayCanvas.TRACK_SIZE;
			}else if(r instanceof TrackGroup) {
				for(Track t : (TrackGroup) r) {
					if(t.getX() < x) x = (int) t.getX();
					if(t.getY() < y) y = (int) t.getY();
					if(t.getX() + RailwayCanvas.TRACK_SIZE > w) w = (int) t.getX() + RailwayCanvas.TRACK_SIZE;
					if(t.getY() + RailwayCanvas.TRACK_SIZE > h) h = (int) t.getY() + RailwayCanvas.TRACK_SIZE;
				}
			}
		}
		
		//rotate and translate renderables
		for(Renderable r : renderables) {
			if(r instanceof Track) {
				Track t = (Track) r;
				double xx = t.getX();
				double yy = t.getY();
				t.setX(h - yy + RailwayCanvas.TRACK_SIZE);
				t.setY(xx);
				t.rotate(rotation);
			}else if(r instanceof TrackGroup) {
				for(Track t : (TrackGroup) r) {
					double xx = t.getX();
					double yy = t.getY();
					t.setX(h - yy);
					t.setY(xx);
					t.rotate(rotation);
				}
			}
		}
	
		return new RotateAction(renderables, -rotation);
	}

}
