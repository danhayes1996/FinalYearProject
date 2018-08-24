package main;

import java.net.URL;

import controller.RailwayController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.RailwayModel;
import view.RailwayRootPane;

public class RailwayApplication extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Railway Model Planner");
		//window can be no smaller than (640, 480)
		stage.setMinWidth(640);
		stage.setMinHeight(480);

		//set the default width and height of window
		stage.setWidth(1000);
		stage.setHeight(700);
		
		//create view (the application layout)
		RailwayRootPane view = new RailwayRootPane();
		//create controller with view and model (stage is used for changing the window title and force the focus on popup windows)
		new RailwayController(stage, view, new RailwayModel());
		
		Scene scene = new Scene(view);
		//load style sheet
		URL url = this.getClass().getResource("/css/railway.css");
		if(url == null) {
			System.out.println("Resource \"railway.css\" not found.");
			System.exit(1);
		}else {
			scene.getStylesheets().add(url.toExternalForm());
		}
		
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
