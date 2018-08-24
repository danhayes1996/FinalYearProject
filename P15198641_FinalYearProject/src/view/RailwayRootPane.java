package view;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.canvas.RailwayCanvas;
import view.canvas.RailwayTrackPane;
import view.menubar.RailwayMenuBar;
import view.toolbar.RailwayToolBar;

public class RailwayRootPane extends VBox {

	//the different sections of the applications layout.
	private RailwayMenuBar mb;
	private RailwayToolBar tb;
	private RailwayTrackPane tp;
	private RailwayCanvas c;

	public RailwayRootPane() {
		//create each of the sections.
		mb = new RailwayMenuBar();
		tb = new RailwayToolBar();
		tp = new RailwayTrackPane();
		c = new RailwayCanvas();
		
		//bind with and height of canvas to the the windows with and height.
		c.prefWidthProperty().bind(this.widthProperty());
		c.prefHeightProperty().bind(this.heightProperty());
		
		//create scroll pane for the canvas (creates a horizontal and vertical scroll bar for the canvas)
		ScrollPane sp = new ScrollPane(c);
		sp.setHbarPolicy(ScrollBarPolicy.ALWAYS); //always have scroll horizontal bar
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS); //always have scroll vertical bar
		sp.setStyle("-fx-focus-color: transparent;"); //stop highlighting when focused
		
		//add all the sections to the root pane
		this.getChildren().addAll(mb, tb, new HBox(tp, sp));
	}

	//getters for the sections of the application (used by the controllers)
	public RailwayMenuBar getMenuBar(){
		return mb;
	}
	
	public RailwayToolBar getToolBar(){
		return tb;
	}

	public RailwayTrackPane getTrackPane() {
		return tp;
	}
	
	public RailwayCanvas getCanvas() {
		return c;
	}
}
