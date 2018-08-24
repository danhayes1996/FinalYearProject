package view.menubar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/* TODO:
 *  - gets and sets for disabling/enabling links (bindings?)
 *  - add action listeners from controller
 */

public class RailwayMenuBar extends MenuBar{
	
	//put Menus into separate class with hashMap to add event handlers
	private MenuItem iNew, iOpen, iSave, iSaveAs, iExit; 			//FILE MENU'S ITEMS
	private MenuItem iUndo, iRedo, iCopy, iCut, iPaste, iSelectAll, 
	                 iDelete, iRotate, iGroup, iUngroup;      		//EDIT MENU'S ITEMS
	private MenuItem iAbout;						 				//HELP MENU'S ITEMS
	
	
	public RailwayMenuBar(){
		//FILE MENU
		Menu mFile = new Menu("_File");
		iNew = new MenuItem("_New"); // underscore before a letter means that alt + letter will trigger the menu items event
		//set a key combination to trigger the menu item event (CTRL + N)
		iNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		
		//Ellipsis indicates that a new window will open if clicked
		iOpen = new MenuItem("_Open...");
		iOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		
		iSave = new MenuItem("_Save");
		iSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		
		iSaveAs = new MenuItem("Save _As...");
		iSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN));
		
		iExit = new MenuItem("E_xit");
		iExit.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));

		//add all menu items to file menu, separators used to give each menu some structure
		mFile.getItems().addAll(iNew, iOpen, new SeparatorMenuItem(),
								iSave, iSaveAs, new SeparatorMenuItem(),
								iExit);

		//EDIT MENU
		Menu mEdit = new Menu("_Edit");

		iUndo = new MenuItem("_Undo");
		iUndo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
		
		iRedo = new MenuItem("_Redo");
		iRedo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
		
		iCopy = new MenuItem("_Copy");
		iCopy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
		
		iCut = new MenuItem("Cu_t");
		iCut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
		
		iPaste = new MenuItem("_Paste");
		iPaste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
		
		iSelectAll = new MenuItem("Select _All");
		iSelectAll.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
		
		iDelete = new MenuItem("_Delete");
		iDelete.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
		
		iRotate = new MenuItem("R_otate");
		iRotate.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
		
		iGroup = new MenuItem("_Group");
		iGroup.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN));
		
		iUngroup = new MenuItem("U_ngroup");
		iUngroup.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN));

		mEdit.getItems().addAll(iUndo, iRedo, new SeparatorMenuItem(),
								iCopy, iCut, iPaste,  new SeparatorMenuItem(),
								iSelectAll, iDelete, iRotate, iGroup, iUngroup);


		//HELP BAR
		Menu mHelp = new Menu("_Help");

		iAbout = new MenuItem("_About");
		
		mHelp.getItems().add(iAbout);
		iAbout.setAccelerator(new KeyCodeCombination(KeyCode.F1));

		//add all menus to this ("this" extends menu bar)
		this.getMenus().addAll(mFile, mEdit, mHelp);
	}
	
	//set menu item handler functions
	public void setNewCanvasHandler(EventHandler<ActionEvent> e){
		iNew.setOnAction(e);
	}
	
	public void setOpenHandler(EventHandler<ActionEvent> e){
		iOpen.setOnAction(e);
	}

	public void setSaveHandler(EventHandler<ActionEvent> e){
		iSave.setOnAction(e);
	}

	public void setSaveAsHandler(EventHandler<ActionEvent> e){
		iSaveAs.setOnAction(e);
	}

	public void setExitHandler(EventHandler<ActionEvent> e){
		iExit.setOnAction(e);
	}

	public void setUndoHandler(EventHandler<ActionEvent> e){
		iUndo.setOnAction(e);
	}

	public void setRedoHandler(EventHandler<ActionEvent> e){
		iRedo.setOnAction(e);
	}

	public void setCopyHandler(EventHandler<ActionEvent> e){
		iCopy.setOnAction(e);
	}

	public void setCutHandler(EventHandler<ActionEvent> e){
		iCut.setOnAction(e);
	}

	public void setPasteHandler(EventHandler<ActionEvent> e){
		iPaste.setOnAction(e);
	}

	public void setSelectAllHandler(EventHandler<ActionEvent> e){
		iSelectAll.setOnAction(e);
	}

	public void setDeleteHandler(EventHandler<ActionEvent> e){
		iDelete.setOnAction(e);
	}

	public void setRotateHandler(EventHandler<ActionEvent> e){
		iRotate.setOnAction(e);
	}

	public void setGroupHandler(EventHandler<ActionEvent> e){
		iGroup.setOnAction(e);
	}

	public void setUngroupHandler(EventHandler<ActionEvent> e){
		iUngroup.setOnAction(e);
	}

	public void setAboutHandler(EventHandler<ActionEvent> e){
		iAbout.setOnAction(e);
	}
}