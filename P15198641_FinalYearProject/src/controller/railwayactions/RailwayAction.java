package controller.railwayactions;

import model.CanvasModel;
import view.canvas.RailwayCanvas;

public abstract interface RailwayAction {

	//return the opposite action for this action
	public abstract RailwayAction doAction(RailwayCanvas railwayCanvas, CanvasModel canvasModel);
	
}
