package org.unibl.etf.mdp.czsmdpclient.gui;


import java.util.logging.Level;

import org.unibl.etf.mdp.logger.MyLogger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			MyLogger.setup();
			Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
			Scene scene = new Scene(root,815,700);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			MyLogger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

}
