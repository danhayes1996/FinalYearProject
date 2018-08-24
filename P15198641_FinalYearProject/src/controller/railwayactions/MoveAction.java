package controller.railwayactions;

import java.util.Set;

import model.CanvasModel;
import model.renderables.Renderable;
import model.renderables.Track;
import model.renderables.TrackGroup;
import view.canvas.RailwayCanvas;

public class MoveAction implements RailwayAction{

	private Set<Renderable> renderables;
	
	private int tx, ty;
	
	public MoveAction(Set<Renderable> rs) {
		renderables = rs;
	}
	
	public MoveAction(Set<Renderable> rs, int translationX, int translationY) {
		renderables = rs;
		tx = translationX;
		ty = translationY;
	}
	
	public RailwayAction doAction(RailwayCanvas railwayCanvas, CanvasModel canvasModel) {
		//move each track to their previous position
		for(Renderable r : renderables) {
			if(r instanceof Track) {
				moveTrack((Track) r);
			}else if(r instanceof TrackGroup) {
				for(Track t : (TrackGroup) r)
					moveTrack(t);
			}
		}
		//return new MoveAction(renderables);
		return new MoveAction(renderables, -tx, -ty);
	}
	
	//translates track to previous position
	private void moveTrack(Track t) {
		int newx = (((int)(t.getX() + tx) + (RailwayCanvas.TRACK_SIZE / 2)) / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
		int newy = (((int)(t.getY() + ty) + (RailwayCanvas.TRACK_SIZE / 2)) / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
		t.setX(newx);
		t.setY(newy);
	}
}
