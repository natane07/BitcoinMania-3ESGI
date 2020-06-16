package org.bitcoin.app;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bitcoin.utils.Logger;

import java.io.IOException;

/**
 * Point d'entré de l'application
 * @author Bendavid Natane
 */

public class App extends Application {

    /**
     * Logger
     */
    public static final Logger logger = new Logger();

    @Override
    public void start(Stage stage) throws IOException {
        Parent root =  FXMLLoader.load(getClass().getResource("/sample.fxml"));
        stage.setTitle("Hello word !");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        App.logger.error(101, "Start Application Error", null);
        App.logger.info("Start application Info");
        App.logger.debug("Start application Debug");
        App.logger.warn(1001, "Start application Warning", null);
        launch();
    }

}