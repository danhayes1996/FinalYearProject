package view.toolbar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;

/* TODO:
 *  - gets and sets for enable/disable buttons
 *  - Tooltips for each button
 *  - add action listener from controller
 *  - pass controller to constructor
 *  - add graphics for buttons
 */
public class RailwayToolBar extends ToolBar{

	private Button bNew, bSave, bOpen, bUndo, bRedo, bCopy, bCut, bPaste, bDelete, bRotate, bGroup, bUngroup;
	private ToggleButton bGrid;
	
	public RailwayToolBar(){
		//create each button in the toolbar
		bNew = new Button("New");
		//tooltips are pieces of text that show up if you hover over a button
		bNew.setTooltip(new Tooltip("Create a New Canvas")); 

		bSave = new Button("Save");
		bSave.setTooltip(new Tooltip("Save"));

		bOpen = new Button("Open");
		bOpen.setTooltip(new Tooltip("Open"));

		bUndo = new Button("Undo");
		bUndo.setTooltip(new Tooltip("Undo"));

		bRedo = new Button("Redo");
		bRedo.setTooltip(new Tooltip("Redo"));

		bCopy = new Button("Copy");
		bCopy.setTooltip(new Tooltip("Copy"));

		bCut = new Button("Cut");
		bCut.setTooltip(new Tooltip("Cut"));

		bPaste = new Button("Paste");
		bPaste.setTooltip(new Tooltip("Paste"));

		bDelete = new Button("Delete");
		bDelete.setTooltip(new Tooltip("Delete"));

		bRotate = new Button("Rotate");
		bRotate.setTooltip(new Tooltip("Rotate"));

		bGroup = new Button("Group");
		bGroup.setTooltip(new Tooltip("Group"));

		bUngroup = new Button("Ungroup");
		bUngroup.setTooltip(new Tooltip("Ungroup"));

		bGrid = new ToggleButton("Grid");
		bGrid.setTooltip(new Tooltip("Toggle Grid View"));
		bGrid.setSelected(true);//by default this button is on

		//add all buttons to this ("this" is a toolbar)
		this.getItems().addAll(bNew, bSave, bOpen, bUndo, bRedo, bCopy, bCut, bPaste, bDelete, bRotate, bGroup, bUngroup, bGrid);
	}
	
	//set button handler functions
	public void setNewHandler(EventHandler<ActionEvent> e){
		bNew.setOnAction(e);
	}
	
	public void setSaveHandler(EventHandler<ActionEvent> e){
		bSave.setOnAction(e);
	}
	
	public void setOpenHandler(EventHandler<ActionEvent> e){
		bOpen.setOnAction(e);
	}
	
	public void setUndoHandler(EventHandler<ActionEvent> e){
		bUndo.setOnAction(e);
	}
	
	public void setRedoHandler(EventHandler<ActionEvent> e){
		bRedo.setOnAction(e);
	}
	
	public void setCopyHandler(EventHandler<ActionEvent> e){
		bCopy.setOnAction(e);
	}
	
	public void setCutHandler(EventHandler<ActionEvent> e){
		bCut.setOnAction(e);
	}
	
	public void setPasteHandler(EventHandler<ActionEvent> e){
		bPaste.setOnAction(e);
	}
	
	public void setDeleteHandler(EventHandler<ActionEvent> e){
		bDelete.setOnAction(e);
	}
	
	public void setRotateHandler(EventHandler<ActionEvent> e){
		bRotate.setOnAction(e);
	}
	
	public void setGroupHandler(EventHandler<ActionEvent> e){
		bGroup.setOnAction(e);
	}
	
	public void setUngroupHandler(EventHandler<ActionEvent> e){
		bUngroup.setOnAction(e);
	}
	
	public void setGridHandler(EventHandler<ActionEvent> e) {
		bGrid.setOnAction(e);
	}
}
