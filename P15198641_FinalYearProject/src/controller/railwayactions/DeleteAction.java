package controller.railwayactions;

import java.util.HashSet;
import java.util.Set;

import model.CanvasModel;
import model.renderables.Renderable;
import model.renderables.Track;
import view.canvas.RailwayCanvas;

public class DeleteAction implements RailwayAction{

	private Set<Renderable> renderables;
	
	public DeleteAction(Track t) {
		renderables = new HashSet<Renderable>();
		renderables.add(t);
	}
	
	public DeleteAction(Set<Renderable> rs) {
		renderables = rs;
	}
	
	public RailwayAction doAction(RailwayCanvas railwayCanvas, CanvasModel canvasModel) {
		//delete selected renderables
		for(Renderable r : renderables) {		
			railwayCanvas.removeRenderable(r);
			canvasModel.removeRenderable(r);
		}
		return new CreateAction(renderables);
	}

}
