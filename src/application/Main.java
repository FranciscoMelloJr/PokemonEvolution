package application;
	
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Principal.fxml"));
			Scene scene = new Scene(root);
			primaryStage.getIcons().add(new Image("file:src/banner/icon.png"));
	        primaryStage.setTitle("What's that Pok�mon ??");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
