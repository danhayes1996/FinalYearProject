package controller.railwayactions;

import java.util.HashSet;
import java.util.Set;

import model.CanvasModel;
import model.renderables.Renderable;
import model.renderables.Track;
import model.renderables.TrackGroup;
import view.canvas.RailwayCanvas;

public class UngroupAction implements RailwayAction {

	private TrackGroup group;
	
	public UngroupAction(TrackGroup group) {
		this.group = group;
	}

	public RailwayAction doAction(RailwayCanvas railwayCanvas, CanvasModel canvasModel) {
		Set<Renderable> rs = new HashSet<>();
		canvasModel.removeRenderable(group);
		
		for(Track t : group) { 
			canvasModel.addRenderable(t);
			rs.add(t);	//so they can be regrouped if redo is pressed
		}
		
		return new GroupAction(rs);
	}

}
