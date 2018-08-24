package controller;

import java.util.Set;

import controller.railwayactions.MoveAction;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.CanvasModel;
import model.renderables.Renderable;
import model.renderables.Track;
import model.renderables.TrackGroup;
import view.canvas.RailwayCanvas;

public class TrackController {

	private static CanvasModel canvasModel;
	private static RailwayCanvas canvasView;
	
	public static void setCavasModelAndView(CanvasModel canvasModel, RailwayCanvas canvasView) {
		TrackController.canvasModel = canvasModel;	
		TrackController.canvasView = canvasView;
	}
	
	public static class TrackMousePressHandler implements EventHandler<MouseEvent> {
		private void initTrackPrevAndDragPos(Track t, double scenex, double sceney) {
			t.setPrevX(t.getX());
			t.setPrevY(t.getY());
			
			t.setDragX(0);
			t.setDragY(0);
		}
		
		public void handle(MouseEvent e) {
			System.out.println("Track Mouse Press Handler Active");
			
			//get track by image view
			ImageView iv = (ImageView)e.getSource();
			Track t = canvasModel.getTrack(iv);
			
			//if track isn't found just return
			if(t == null) return;
			
			TrackGroup group = canvasModel.getGroup(t);
			//if t doesn't belong to a group
			if(group == null) {
				//if already selected do nothing
				for(Renderable r : canvasModel.getSelectedRenderables())
					if(r == t) return;
				
				initTrackPrevAndDragPos(t, e.getSceneX(), e.getSceneY());
				
				//if multiple select key isn't pressed then clear selection model
				if(!e.isControlDown())
					canvasModel.clearSelectedRenderables();
				
				canvasModel.addSelectedRenderable(t);
			}else { 
				for(Track track : group) 
					initTrackPrevAndDragPos(track, e.getSceneX(), e.getSceneY());
				
				//if multiple select key isn't pressed then clear selection model
				if(!e.isControlDown())
					canvasModel.clearSelectedRenderables();
				
				canvasModel.addSelectedRenderable(group);
			}
			
		}
	}
	
	public static class TrackMouseReleaseHandler implements EventHandler<MouseEvent>{
		private void releaseTrack(Track t) {
			//snap to grid area (int because integer division)
			int newx = (((int)t.getX() + (RailwayCanvas.TRACK_SIZE / 2)) / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
			int newy = (((int)t.getY() + (RailwayCanvas.TRACK_SIZE / 2)) / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
			
			if(newx == t.getX() && t.getY() == newy) return;
			
			if(!canvasModel.isEmptySpace(newx, newy)) {
				t.setX(t.getPrevX());
				t.setY(t.getPrevY());
				return;
			}

			t.setX(newx);
			t.setY(newy);
			
			t.setPrevX(t.getX());
			t.setPrevY(t.getY());
			
			t.setDragX(0);
			t.setDragY(0);
		}
		
		public void handle(MouseEvent e) {
			System.out.println("Track Mouse Release Handler Active");
			
			ImageView iv = (ImageView) e.getSource();
			Track track = canvasModel.getTrack(iv);
			
			int tx = (int)(track.getX() - track.getPrevX());
			int ty = (int)(track.getY() - track.getPrevY());
			
			Set<Renderable> renderables = canvasModel.getSelectedRenderables();
			
			//release each selected track in an appropriate position
			for(Renderable r : renderables) {
				if(r instanceof Track) {
					releaseTrack((Track) r);
				}else if (r instanceof TrackGroup) {
					for(Track t : (TrackGroup) r) {
						releaseTrack(t);
					}
				}
			}
			
			//if translation isn't (0, 0) then acknowledge move action
			if(tx != 0 && ty != 0) {
				canvasModel.pushUndoStack(new MoveAction(renderables, -tx, -ty));
				if(canvasView == null) System.out.println("null");canvasView.assureSize(canvasModel.getSelectedRenderables());
			}
		}
	}
	
	public static class TrackMouseDragHandler implements EventHandler<MouseEvent>{
		//set the tracks drag pos based on x and y of mouse
		private void setTrackDragPos(Track t, double scenex, double sceney) {
			
			//if not been dragged before then set initial values
			if(t.getDragX() == 0D) t.setDragX(scenex);
			if(t.getDragY() == 0D) t.setDragY(sceney);
			
			double xx = t.getX() + (scenex - t.getDragX());
			double yy = t.getY() + (sceney - t.getDragY());
			
			if(xx > 0) {
				t.setX(xx);
				t.setDragX(scenex);
			}
			if(yy > 0) {
				t.setY(yy);
				t.setDragY(sceney);
			}
		}
		
		public void handle(MouseEvent e) {
			System.out.println("Track Mouse Drag Handler Active");
			
			if(!e.isPrimaryButtonDown()) return;
			
			//set each selected renderbales x and y drag values based on mouse pos
			Set<Renderable> renderables = canvasModel.getSelectedRenderables();
			for(Renderable r : renderables) {
				if(r instanceof Track) {
					setTrackDragPos((Track) r, e.getSceneX(), e.getSceneY());
				}else if(r instanceof TrackGroup) {
					for(Track t : (TrackGroup) r)
						setTrackDragPos(t, e.getSceneX(), e.getSceneY());
				}
			}
		}
	}
}
