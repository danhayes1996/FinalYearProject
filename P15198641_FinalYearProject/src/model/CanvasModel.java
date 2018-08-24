package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import controller.railwayactions.RailwayAction;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.renderables.Renderable;
import model.renderables.Track;
import model.renderables.TrackGroup;
import view.canvas.RailwayTrackPane;

public class CanvasModel {

	private Stack<RailwayAction> undoStack, redoStack;
	
	//stores last known mouse x and y (for pasting in appropriate location)
	private double mouseX, mouseY;
	
	//stores the renderables on the canvas.
	private ArrayList<Renderable> canvasRenderables;
	
	//stores the if of the button pressed in the track pane (so when the canvas is pressed, it knows which track to put down)
	private int selectedType;
	
	//the box that allows you to select multiple tracks.
	private Rectangle selectBox;
	private boolean selectBoxEnabled; //stores if the select box can be used or bit
	
	//stores the renderables that have been selected
	private Set<Renderable> selectedRenderables;
	
	//stores the renderables that have been copied or cut
	private Set<Renderable> clipboard;
	
	//stores the currently open file (null if new canvas)
	private File currentFile;
	
	public CanvasModel() {
		undoStack = new Stack<>();
		redoStack = new Stack<>();
		
		mouseX = mouseY = 0D;
		
		canvasRenderables = new ArrayList<>();
		selectedType = RailwayTrackPane.TRACKPANE_MOUSE_POINTER_ID;
		
		selectedRenderables = new HashSet<>();

		selectBox = new Rectangle();
		selectBox.setStroke(Color.BLUE); //select box outline colour
		selectBox.setFill(Color.rgb(0, 0, 255, 0.5)); //select box fill colour
		
		clipboard = new HashSet<>();
	}

	//clear both stacks
	public void clearUndoAndRedoStacks() {
		undoStack.clear();
		redoStack.clear();
	}

	/**
	 * pop undo stack and return the result
	 * @return returns the top most RailwayAction in the stack, if empty return null.
	 */
	public RailwayAction popUndoStack() {
		return undoStack.isEmpty() ? null : undoStack.pop();
	}
	
	/**
	 * push RailwayAction to the undo stack
	 * @param action the RailwayAction to be pushed
	 */
	public void pushUndoStack(RailwayAction action) {
		undoStack.push(action);
	}
	
	public boolean isUndoStackEmpty() {
		return undoStack.isEmpty();
	}
	
	/**
	 * pop redo stack and return the result
	 * @return returns the top most RailwayAction in the stack, if empty return null.
	 */
	public RailwayAction popRedoStack() {
		return redoStack.isEmpty() ? null : redoStack.pop();
	}
	
	/**
	 * push RailwayAction to the redo stack
	 * @param action the RailwayAction to be pushed
	 */
	public void pushRedoStack(RailwayAction action) {
		redoStack.push(action);
	}
	
	public boolean isRedoStackEmpty() {
		return redoStack.isEmpty();
	}
	
	public void setMouseX(double x) {
		mouseX = x;
	}
	
	public double getMouseX() {
		return mouseX;
	}
	
	public void setMouseY(double y) {
		mouseY = y;
	}
	
	public double getMouseY() {
		return mouseY;
	}
	
	/**
	 * set the selected button in the track pane. Use the IDs from RailwayTrackPane.
	 * @param type the id to be selected.
	 */
	public void setSelectedTrackPaneType(int type) {
		selectedType = type;
	}
	
	public int getSelectedTrackPaneType() {
		return selectedType;
	}
	
	public void addRenderable(Renderable r) {
		canvasRenderables.add(r);
	}
	
	public void addRenderables(Set<Renderable> tracks) {
		canvasRenderables.addAll(tracks);
	}

	public boolean removeRenderable(Renderable r) {
		return canvasRenderables.remove(r);
	}

	public void setRenderables(ArrayList<Renderable> renderables) {
		this.canvasRenderables = renderables;
	}
	
	public ArrayList<Renderable> getRenderables(){
		return canvasRenderables;
	}
	
	public void clearRenderables() {
		canvasRenderables.clear();
	}
	
	//check if the space at x and y is empty (this uses grid coordinates, eg. multiples of RailwayCanvas.TRACK_SIZE)
	public boolean isEmptySpace(int x, int y) {
		for(Renderable r : canvasRenderables) {
			if(r instanceof Track) {
				if((int)((Track) r).getX() == x && (int) ((Track) r).getY() == y) return false;
			}else if(r instanceof TrackGroup) {
				for(Track t : (TrackGroup) r)
					if((int)t.getX() == x && (int)t.getY() == y) return false;
			}
		}
		return true;
	}
	
	public void addSelectedRenderable(Renderable r) {
		selectedRenderables.add(r);
		r.isSelected(true);
	}
	
	public void addSelectedRenderables(Collection<Renderable> rs) {
		for(Renderable r : rs) addSelectedRenderable(r);
	}

	public void clearSelectedRenderables() {
		for(Renderable r : selectedRenderables) r.isSelected(false);
		selectedRenderables.clear();
	}
	
	public Set<Renderable> getSelectedRenderables() {
		return selectedRenderables;
	}
	
	
	public void setSelectBoxXY(double x, double y) {
		selectBox.setX(x);
		selectBox.setY(y);
	}
	
	public void setSelectBoxWH(int w, int h) {
		selectBox.setWidth(w);
		selectBox.setHeight(h);
	}
	
	//reset the select box back to (0, 0) with a size of (0, 0)
	public void resetSelectBox() {
		selectBox.setX(0);
		selectBox.setY(0);
		selectBox.setTranslateX(0);
		selectBox.setTranslateY(0);
		selectBox.setWidth(0);
		selectBox.setHeight(0);
	}
	
	public Rectangle getSelectBox() {
		return selectBox;
	}
	
	public boolean isSelectBoxEnabled() {
		return selectBoxEnabled;
	}
	
	public void enableSelectBox(boolean b) {
		selectBoxEnabled = b;
	}
	
	public void clearClipboard() {
		clipboard.clear();
	}
	
	public Set<Renderable> getClipboard() {
		return clipboard;
	}
	
	public boolean addToClipboard(Renderable r) {
		return clipboard.add(r);
	}
	
	public void addToClipboard(Set<Renderable> rs) {
		for(Renderable r : rs) 
			addToClipboard(r);
	}
	
	public File getCurrentFile() {
		return currentFile;
	}
	
	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
	}

	//gets the track that holds the image view specified
	public Track getTrack(ImageView iv) {
		for(Renderable r : canvasRenderables) {
			if(r instanceof Track) {
				if(((Track) r).getTrackImage().equals(iv)) return (Track) r;
			} else if(r instanceof TrackGroup) {
				TrackGroup group = (TrackGroup) r;
				for(Track track : group)
					if(track.getTrackImage().equals(iv)) return track;
			}
		}
		return null;
	}

	//find the group that track belongs to
	public TrackGroup getGroup(Track t) {
		for(Renderable r : canvasRenderables) {
			if(r instanceof TrackGroup) {
				for(Track track : (TrackGroup) r)
					if(track.equals(t)) return (TrackGroup) r;
			}
		}
		return null;
	}
}
