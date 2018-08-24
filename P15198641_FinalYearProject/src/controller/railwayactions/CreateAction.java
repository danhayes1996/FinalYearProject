package controller.railwayactions;

import java.util.HashSet;
import java.util.Set;

import model.CanvasModel;
import model.renderables.Renderable;
import model.renderables.Track;
import view.canvas.RailwayCanvas;

public class CreateAction implements RailwayAction{

	private Set<Renderable> tracks;
	
	public CreateAction(Track t) {
		tracks = new HashSet<Renderable>();
		tracks.add(t);
	}
	
	public CreateAction(Set<Renderable> tracks) {
		this.tracks = new HashSet<>(tracks);
	}
	
	public RailwayAction doAction(RailwayCanvas railwayCanvas, CanvasModel canvasModel) {
		canvasModel.clearSelectedRenderables();
		canvasModel.addRenderables(tracks);
		canvasModel.addSelectedRenderables(tracks);
		railwayCanvas.addRenderables(tracks);
		return new DeleteAction(tracks);
	}

}
