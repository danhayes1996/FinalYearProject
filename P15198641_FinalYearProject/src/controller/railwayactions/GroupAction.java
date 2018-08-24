package controller.railwayactions;

import java.util.Set;

import model.CanvasModel;
import model.renderables.Renderable;
import model.renderables.Track;
import model.renderables.TrackGroup;
import view.canvas.RailwayCanvas;

public class GroupAction implements RailwayAction {

	private Set<Renderable> renderables;
	
	public GroupAction(Set<Renderable> rs) {
		renderables = rs;
	}
	
	public RailwayAction doAction(RailwayCanvas railwayCanvas, CanvasModel canvasModel) {
		TrackGroup group = new TrackGroup();
		
		//add all tracks (and groups) to new group
		for(Renderable r : renderables) {
			if(r instanceof Track) {
				group.add((Track) r);
				canvasModel.removeRenderable((Track) r);
			}else if(r instanceof TrackGroup) {
				for(Track t : (TrackGroup) r)
					group.add(t);
				canvasModel.removeRenderable(r);
			}
		}
		canvasModel.addRenderable(group);
		
		//make the new group the selected tracks
		canvasModel.clearSelectedRenderables();
		canvasModel.addSelectedRenderable(group);
		
		return new UngroupAction(group);
	}

}
