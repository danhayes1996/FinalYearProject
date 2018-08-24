package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import controller.railwayactions.CreateAction;
import controller.railwayactions.DeleteAction;
import controller.railwayactions.GroupAction;
import controller.railwayactions.RailwayAction;
import controller.railwayactions.RotateAction;
import controller.railwayactions.UngroupAction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.AppSettingsModel;
import model.CanvasModel;
import model.RailwayModel;
import model.renderables.Renderable;
import model.renderables.Track;
import model.renderables.TrackGroup;
import view.RailwayRootPane;
import view.canvas.RailwayCanvas;
import view.canvas.RailwayTrackPane;
import view.menubar.RailwayMenuBar;
import view.toolbar.RailwayToolBar;

public class RailwayController {

	//stage required to lock focus on FileChooser's while they are open
	private Stage stage;
	
	private RailwayModel model;
	private AppSettingsModel appSettings;
	private CanvasModel canvasModel;
	
	private RailwayRootPane view;
	private RailwayMenuBar menuBar;
	private RailwayToolBar toolBar;
	private RailwayTrackPane trackPane;
	private RailwayCanvas canvasView;
	
	public RailwayController(Stage stage, RailwayRootPane view, RailwayModel model){
		this.stage = stage;
		this.view = view;
		this.model = model;
		
		appSettings = this.model.getAppSettingsModel();
		canvasModel = this.model.getCanvasModel();
		
		menuBar = this.view.getMenuBar();
		toolBar = this.view.getToolBar();
		trackPane = this.view.getTrackPane();
		canvasView = this.view.getCanvas();
	
		//controls events for track pieces
		TrackController.setCavasModelAndView(canvasModel, canvasView);

		//assign event handlers
		this.assignHandlers();
		
		//canvasView and canvasModel share selection box reference (view draws while model updates box)
		canvasView.setSelectBox(canvasModel.getSelectBox());
	}
	
	//setup the handlers for each of the menu items, buttons, etc.
	private void assignHandlers(){
		stage.setOnCloseRequest(new CloseHandler());
		
		menuBar.setNewCanvasHandler(new CreateNewCanvasHandler());
		menuBar.setOpenHandler(new OpenFileHandler());
		menuBar.setSaveHandler(new SaveHandler());
		menuBar.setSaveAsHandler(new SaveAsHandler());
		menuBar.setExitHandler(new ExitHandler());
		
		menuBar.setUndoHandler(new UndoHandler());
		menuBar.setRedoHandler(new RedoHandler());
		menuBar.setCopyHandler(new CopyHandler());
		menuBar.setCutHandler(new CutHandler());
		menuBar.setPasteHandler(new PasteHandler());
		
		menuBar.setSelectAllHandler(new SelectAllHandler());
		menuBar.setDeleteHandler(new DeleteHandler());
		menuBar.setRotateHandler(new RotateHandler());
		menuBar.setGroupHandler(new GroupHandler());
		menuBar.setUngroupHandler(new UngroupHandler());
		
		menuBar.setAboutHandler(new AboutHandler());
		
		toolBar.setNewHandler(new CreateNewCanvasHandler());
		toolBar.setOpenHandler(new OpenFileHandler());
		toolBar.setSaveHandler(new SaveHandler());
		toolBar.setUndoHandler(new UndoHandler());
		toolBar.setRedoHandler(new RedoHandler());
		toolBar.setCopyHandler(new CopyHandler());
		toolBar.setCutHandler(new CutHandler());
		toolBar.setPasteHandler(new PasteHandler());
		toolBar.setDeleteHandler(new DeleteHandler());
		toolBar.setRotateHandler(new RotateHandler());
		toolBar.setGroupHandler(new GroupHandler());
		toolBar.setUngroupHandler(new UngroupHandler());
		toolBar.setGridHandler(new GridHandler());
		
		//setup track pane event handlers
		new TrackPaneController(trackPane, canvasModel);
		
		canvasView.setMousePressHandler(new CanvasMousePressHandler());
		canvasView.setMouseReleaseHandler(new CanvasMouseReleaseHandler());
		canvasView.setMouseDragHandler(new CanvasMouseDragHandler());
		//gets the mouse x and y to be used for pasting in an appropriate location
		canvasView.setOnMouseMoved(event -> { canvasModel.setMouseX(event.getX()); canvasModel.setMouseY(event.getY()); });
	}
	
	//template for creating pop-up windows
	public static void createAlert(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	//handles what happens when close button is pressed.
	private class CloseHandler implements EventHandler<WindowEvent>{
		public void handle(WindowEvent e) {
			//create a save alert popup window (prompt the user to save current file)
			Alert saveAlert = new Alert(AlertType.NONE);
			saveAlert.setTitle("Save?");
			
			File filePath = canvasModel.getCurrentFile();
			//determine what text should be in the save alert
			if(filePath == null) { //if its a new canvas with no name
				saveAlert.setContentText("Do you want to save changes to Untitled?");				
			}else {
				saveAlert.setContentText("Do you want to save changes to " + filePath.getName() + "?");				
			}
			
			//buttons to be in the save alert
			ButtonType btnYes = new ButtonType("Yes");
			ButtonType btnNo = new ButtonType("No");
			ButtonType btnCancel = new ButtonType("Cancel");
			saveAlert.getButtonTypes().setAll(btnYes, btnNo, btnCancel); //add buttons to save alert
			
			//show save alert and wait for user to click a button
			Optional<ButtonType> result = saveAlert.showAndWait();
			if(result.get() == btnYes) { //user wants to save
				new SaveHandler().handle(null); //trigger save handler
			}else if(result.get() == btnNo){
				//if this method wasnt invoked by the x button (if exitHandler() is triggered)
				if(e == null) System.exit(1);
			}else if(result.get() == btnCancel) {
				if(e != null) e.consume();
			}
			
			//save settings config
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Settings.conf"));
				oos.writeObject(appSettings);
				oos.flush();
				oos.close();
			} catch (FileNotFoundException e1) {
			} catch (IOException e1) {
			}
		}
	}
	
	//handles creating a new canvas
	private class CreateNewCanvasHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//set up a save prompt
			Alert saveAlert = new Alert(AlertType.NONE);
			saveAlert.setTitle("Save?");
			
			File filePath = canvasModel.getCurrentFile();
			if(filePath == null) {
				saveAlert.setContentText("Do you want to save changes to Untitled?");				
			}else {
				saveAlert.setContentText("Do you want to save changes to " + filePath.getName() + "?");				
			}
			
			ButtonType btnYes = new ButtonType("Yes");
			ButtonType btnNo = new ButtonType("No");
			ButtonType btnCancel = new ButtonType("Cancel");
			saveAlert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
			
			Optional<ButtonType> result = saveAlert.showAndWait();
			if(result.get() == btnYes) {
				new SaveHandler().handle(null); //save before creating new canvas
			}else if(result.get() == btnCancel) {
				return; //cancel creating new canvas
			}

			//create new canvas
			canvasModel.setCurrentFile(null);
			canvasModel.clearRenderables();
			canvasView.clear();
			stage.setTitle("Railway Model Planner - New Unititled Canvas");
		}
	}

	//handles opening an existing file
	private class OpenFileHandler implements EventHandler<ActionEvent>{		
		public void handle(ActionEvent e) {
			//set up a save prompt
			Alert saveAlert = new Alert(AlertType.NONE);
			saveAlert.setTitle("Save?");
			
			File filePath = canvasModel.getCurrentFile();
			if(filePath == null) {
				saveAlert.setContentText("Do you want to save changes to Untitled?");				
			}else {
				saveAlert.setContentText("Do you want to save changes to " + filePath.getName() + "?");				
			}
			
			ButtonType btnYes = new ButtonType("Yes");
			ButtonType btnNo = new ButtonType("No");
			ButtonType btnCancel = new ButtonType("Cancel");
			saveAlert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
			
			Optional<ButtonType> result = saveAlert.showAndWait();
			if(result.get() == btnYes) {
				new SaveHandler().handle(null); //save before loading
			}else if(result.get() == btnCancel) {
				return; //cancel loading
			}
			
			//open file chooser to select file to load
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Railway Model File");
			
			fileChooser.setInitialDirectory(appSettings.getDefaultPath()); //default path to open file chooser from.
			
			//what files should be shown (only .rmf files)
			FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Railway Model Files (*.rmf)", "*.rmf");
			fileChooser.getExtensionFilters().add(fileExtensions);
			
			//show the filechooser and wait for the user to select a file
			File file = fileChooser.showOpenDialog(stage);
			if(file == null) return; //if no file selected then dont open anything
			
			//if the file selected is the same file that is currently open then do nothing
			if(canvasModel.getCurrentFile() != null && file.equals(canvasModel.getCurrentFile()))
				return;
			
			
			//load the file
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getAbsolutePath()));
				@SuppressWarnings("unchecked")
				ArrayList<Renderable> renderables = (ArrayList<Renderable>) ois.readObject();
				
				//set canvas model renderables to the ones stored in the dile
				canvasModel.setRenderables(renderables);
				
				//put the renderables on the RailwayCanvas
				canvasView.clear();
				canvasView.addRenderables(renderables);
				canvasView.assureSize(renderables);

				//setup default path based on the location the user chose. (it remembers the last place u opened)
				appSettings.setDefaultPath(file.getParentFile());
				canvasModel.setCurrentFile(file); //set the current file to this one
				canvasModel.clearUndoAndRedoStacks(); 
				
				//change the title of the application to include the new files name
				stage.setTitle("Railway Model Planner - " + file.getName());
				
				ois.close();
			} catch (Exception e1) {
				createAlert(AlertType.ERROR, "Error", null, "An error occurred while loading. ");
				return;
			}
		}
	}
	
	//handles saving to an pre-existing file
	private class SaveHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//if the current file isn't a new canvas then save as normal
			if(canvasModel.getCurrentFile() != null) {
				stage.setTitle("Railway Model Planner - " + canvasModel.getCurrentFile().getName());
				
				ObjectOutputStream oos = null;
				try{
					//save the data to the current file.
					oos = new ObjectOutputStream(new FileOutputStream(canvasModel.getCurrentFile().getAbsolutePath()));
					oos.writeObject(canvasModel.getRenderables());
					oos.flush();
				}catch (Exception e1){
					e1.printStackTrace();
					createAlert(AlertType.ERROR, "Error", null, "An error occurred while saving. Please Try Again Later.");
					return;
				}finally{
					try{
						oos.close();
					}catch (Exception e1){
						e1.printStackTrace();
						createAlert(AlertType.ERROR, "Error", null, "An error occurred while saving. Please Try Again Later.");
						return;
					}
				}
			}else { //if the current file is new then do save as action instead.
				new SaveAsHandler().handle(e);
			}
		}
	}
	
	//handles saving to a new file
	private class SaveAsHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//open a file chooser for the user to select a location and filename to save to.
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Railway Model File");
			fileChooser.setInitialDirectory(appSettings.getDefaultPath());
			
			//can only be saved as a .rmf file
			FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Railway Model Files (*.rmf)", "*.rmf");
			fileChooser.getExtensionFilters().add(fileExtensions);
		
			//show the filechooser and wait for the user to select a location and filename
			File file = fileChooser.showSaveDialog(stage);
			if(file == null) return; //if no filename was chose then dont save.
			
			appSettings.setDefaultPath(file.getParentFile());
			canvasModel.setCurrentFile(file);
			
			stage.setTitle("Railway Model Planner - " + file.getName());
			
			ObjectOutputStream oos = null;
			try{
				//save to file.
				oos = new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath()));
				oos.writeObject(canvasModel.getRenderables());
				oos.flush();
			}catch (Exception e1){
				e1.printStackTrace();
				createAlert(AlertType.ERROR, "Error", null, "An error occurred while saving. Please Try Again Later.");
				return;
			}finally{
				try{
					oos.close();
				}catch (Exception e1){
					e1.printStackTrace();
					createAlert(AlertType.ERROR, "Error", null, "An error occurred while saving. Please Try Again Later.");
					return;
				}
			}
		}
	}
	
	//handles exiting from anything other than the close button
	private class ExitHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			new CloseHandler().handle(null);
		}
	}
	
	//handles undoing previous actions
	private class UndoHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			RailwayAction action = canvasModel.popUndoStack();
			if(action == null) return; //if the undo stack is empty do nothing
			
			//undo the action (returns the opposite of the action just done)
			RailwayAction newAction = action.doAction(canvasView, canvasModel);
			//add the new action to the redo stack
			canvasModel.pushRedoStack(newAction);
		}
	}
	
	//handles redoing previously undone actions
	private class RedoHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			RailwayAction action = canvasModel.popRedoStack();
			if(action == null) return; //if redo stack is empty do nothing
			
			//do the action (returns the opposite of that action)
			RailwayAction newAction = action.doAction(canvasView, canvasModel);
			//add the new action to the undo stack
			canvasModel.pushUndoStack(newAction);
		}
	}
	
	//handles copying track pieces to the clipboard
	private class CopyHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//get all the renderables that have been selected.
			Set<Renderable> renderables = canvasModel.getSelectedRenderables();
			//add the renderables to the clipboard
			if(!renderables.isEmpty()) {
				canvasModel.clearClipboard();
				canvasModel.addToClipboard(renderables);
			}
		}
	}
	
	//handles cutting track pieces to the clipboard
	private class CutHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//get all the renderables that have been selected.
			Set<Renderable> renderables = canvasModel.getSelectedRenderables();
			if(!renderables.isEmpty()) {
				//add to clipboard
				canvasModel.clearClipboard();
				canvasModel.addToClipboard(renderables);
				
				//delete the selected renderables
				for(Renderable r : renderables) {		
					canvasView.removeRenderable(r);
					canvasModel.removeRenderable(r);
				}
				//add the ability to undo the cutting of the selected renderables.
				canvasModel.pushUndoStack(new CreateAction(renderables));
			}
		}
	}
	
	//handles pasting track pieces from the clipboard
	private class PasteHandler implements EventHandler<ActionEvent>{
		//clone the track (so multiple pastes can be done, javafx doesnt allow duplicate tracks on the canvas)
		private Track cloneTrack(Track t, double mousex, double mousey, double lowestTX, double lowestTY) {
			Track tClone = new Track((Track) t);
			
			//set x and y of clone based on mouse x and y
			int tx = ((int)(mousex + tClone.getX() - lowestTX) / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
			int ty = ((int)(mousey + tClone.getY() - lowestTY) / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
			tClone.setX(tx);
			tClone.setY(ty);
			
			//add the track to the RailwayCanvas, and the CanvasModel
			canvasView.addTrack(tClone);
			canvasModel.addRenderable(tClone);
			
			//select the clone 
			canvasModel.addSelectedRenderable(tClone);
			
			return tClone;
		}
		
		public void handle(ActionEvent e) {
			System.out.println("Paste Handler Active");
			//get the renderables from the clipboard (renderables that have been copied or cut)
			Set<Renderable> renderables = canvasModel.getClipboard();
			if(!renderables.isEmpty()) {
				canvasModel.clearSelectedRenderables();
				
				//get mouse x and y
				double mx = canvasModel.getMouseX();
				double my = canvasModel.getMouseY();

				//get track coords closest to top left.
				double lowestx = renderables.stream().mapToDouble(t -> t.getX()).min().getAsDouble();
				double lowesty = renderables.stream().mapToDouble(t -> t.getY()).min().getAsDouble();
				
				//clone renderables to allows for multiple pastes of clipboard
				Set<Renderable> clones = new HashSet<>(); 
				for(Renderable r : renderables) {
					if(r instanceof Track) {
						clones.add(cloneTrack((Track) r, mx, my, lowestx, lowesty));
					} else if (r instanceof TrackGroup) {
						for(Track t : (TrackGroup) r) 
							clones.add(cloneTrack((Track) t, mx, my, lowestx, lowesty));
					}
				}
				//make sure canvas isnt too small
				canvasView.assureSize(clones);
				//push action to be able to delete pasted items with undo button
				canvasModel.pushUndoStack(new DeleteAction(clones));
			}
		}
	}
	
	//handles selecting all the track pieces on the canvas
	private class SelectAllHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//select all tracks in the canvas
			for(Renderable r : canvasModel.getRenderables())
				canvasModel.addSelectedRenderable(r);
		}
	}
	
	//handles deleting the selected track pieces
	private class DeleteHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//delete renderables using delete action, then push CreateAction to undo stack
			canvasModel.pushUndoStack(new DeleteAction(canvasModel.getSelectedRenderables()).doAction(canvasView, canvasModel));
			//clear selected renderables (as they dont exist anymore)
			canvasModel.clearSelectedRenderables();
		}
	}
	
	//handles rotating the selected track pieces
	private class RotateHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//get all selected renderables
			Set<Renderable> renderables = canvasModel.getSelectedRenderables();
			//rotate 90 degrees then store the opposite rotation in the undo stack.
			canvasModel.pushUndoStack(new RotateAction(renderables, 90).doAction(canvasView, canvasModel));
			//make sure canvas isnt too small
			canvasView.assureSize(renderables);
		}
	}
	
	//TODO: handles grouping the selected track pieces
	private class GroupHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//group selected renderables, then push UngroupAction to undo stack.
			canvasModel.pushUndoStack(new GroupAction(canvasModel.getSelectedRenderables()).doAction(canvasView, canvasModel));
		}
	}
	
	//TODO: handles ungrouping the selected track pieces
	private class UngroupHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//gets the first group it can find then ungroups only it.
			Set<Renderable> renderables = canvasModel.getSelectedRenderables();
			for(Renderable r : renderables) {
				if(r instanceof TrackGroup) {
					//ungroup TrackGroup, then push GroupAction to undo stack
					canvasModel.pushUndoStack(new UngroupAction((TrackGroup) r).doAction(canvasView, canvasModel));
					break;
				}
			}
		}
	}
	
	//handles the grids state (turning it on and off)
	private class GridHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//remove and get current style for grid
			String gridClass = canvasView.getStyleClass().remove(0);
			
			//apply opposite grid style of the previous grid style
			if(gridClass.equals("railway-canvas-grid-on")) 
				canvasView.getStyleClass().add("railway-canvas-grid-off");
			else 
				canvasView.getStyleClass().add("railway-canvas-grid-on");
		}
	}
	
	//TODO: handles about
	private class AboutHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			//create popup window showing information
			createAlert(AlertType.INFORMATION, "About", null, "This application was developed as part of my final year project.");
		}
	}
	
	//handles when canvas is clicked on
	private class CanvasMousePressHandler implements EventHandler<MouseEvent>{
		public void handle(MouseEvent e) {
			//if the user is not using the mouse pointer (selected anything but the mouse pointer in track pane)
			if(canvasModel.getSelectedTrackPaneType() == RailwayTrackPane.TRACKPANE_MOUSE_POINTER_ID) {
				//get the position of the track to be placed clamped to the grid
				int trackx = ((int)e.getX() / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
				int tracky = ((int)e.getY() / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
				
				//see if mouse position is on a track or not
				if(canvasModel.isEmptySpace(trackx, tracky)) {
					canvasModel.enableSelectBox(true);
					canvasModel.clearSelectedRenderables();
					canvasModel.setSelectBoxXY(e.getX(), e.getY());
				} else canvasModel.enableSelectBox(false);
			}
		}
	}
	
	//handles when canvas is clicked on and dragged
	private class CanvasMouseDragHandler implements EventHandler<MouseEvent>{
		public void handle(MouseEvent e) {
			//change select box dimensions based on mouse pos
			if(canvasModel.getSelectedTrackPaneType() == RailwayTrackPane.TRACKPANE_MOUSE_POINTER_ID
					&& canvasModel.isSelectBoxEnabled()) {
				Rectangle selectBox = canvasModel.getSelectBox();
				
				//get translation between current mouse x and y and the select box x and y
				double dx = e.getX() - selectBox.getX();
				double dy = e.getY() - selectBox.getY();
				
				//if the width is less than the x coord (select box growing backwards)
				if(dx < 0) {
					selectBox.setTranslateX(dx);
					selectBox.setWidth(-dx);
				}else selectBox.setWidth(dx);
				
				//if the height is less than the y coord (select box growing backwards)
				if(dy < 0) {
					selectBox.setTranslateY(dy);
					selectBox.setHeight(-dy);
				}else selectBox.setHeight(dy);
			}
		}
	}
		
	//handles when canvas is released
	private class CanvasMouseReleaseHandler implements EventHandler<MouseEvent>{
		public void handle(MouseEvent e) {
			if(canvasModel.getSelectedTrackPaneType() != RailwayTrackPane.TRACKPANE_MOUSE_POINTER_ID) {
				//stop highlighting selected render
				canvasModel.clearSelectedRenderables();
				
				//get grid x and y pos from mouse x and y
				int xpos = ((int)e.getX() / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
				int ypos = ((int)e.getY() / RailwayCanvas.TRACK_SIZE) * RailwayCanvas.TRACK_SIZE;
				
				//check if valid x and y pos for track placement
				if(canvasModel.isEmptySpace(xpos, ypos)){
					Track t = new Track(xpos, ypos, canvasModel.getSelectedTrackPaneType());
					//create track, then push DeleteAction to undostack.
					canvasModel.pushUndoStack(new CreateAction(t).doAction(canvasView, canvasModel));
					
					File file = canvasModel.getCurrentFile();
					if(file != null) stage.setTitle("Railway Model Planner - " + file.getName() + "*");
					else stage.setTitle("Railway Model Planner - Untitled*");
				}
				
				canvasView.assureSize(canvasModel.getSelectedRenderables());
			} else { //mouse box handler
				ArrayList<Renderable> renderables = canvasModel.getRenderables();
				Rectangle selectBox = canvasModel.getSelectBox();
				
				//get all renderables that are inside the select box
				for(Renderable r : renderables) {
					if(r instanceof Track) {
						Track t = (Track) r;
						if(selectBox.getBoundsInParent().intersects(t.getTrackImage().getBoundsInParent()))
							canvasModel.addSelectedRenderable(t);
					} else if(r instanceof TrackGroup) {
						for(Track t : (TrackGroup) r) {
							if(selectBox.getBoundsInParent().intersects(t.getTrackImage().getBoundsInParent())) {
								canvasModel.addSelectedRenderable(r);
								break;
							}
						}
					}
				}
				//get rid of select box
				canvasModel.resetSelectBox();
			}
		}
	}
}
