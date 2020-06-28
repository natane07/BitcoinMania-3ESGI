package org.bitcoin.app;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.bitcoin.model.User;
import org.bitcoin.utils.Error;
import org.bitcoin.utils.Logger;
import org.bitcoin.utils.Modal;

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

    /**
     * User
     */
    public static final User user = new User();

    /**
     * Application stage
     */
    public static Stage stage = new Stage();

    @Override
    public void start(Stage stage) {
        App.stage = stage;
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            App.stage.setWidth(600);
            App.stage.setHeight(450);
            App.stage.setMinWidth(600);
            App.stage.setMinHeight(450);
            App.stage.setMaxWidth(600);
            App.stage.setMaxHeight(450);
            App.stage.setTitle("BitcoinMania");
            App.stage.getIcons().add(new Image("/Bitcoin.png"));
            App.stage.setScene(new Scene(root));
            App.stage.show();
        } catch (IOException exception) {
            Modal.showModalError(exception.getMessage());
        }
    }

    public static void main(String[] args) {
        App.logger.error(101, "Start Application Error", null);
        App.logger.info("Start application Info");
        App.logger.debug("Start application Debug");
        App.logger.warn(1001, "Start application Warning", null);
        launch();
    }

}