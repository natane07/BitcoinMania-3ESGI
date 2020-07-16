package org.bitcoin.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Affichage des modal JAVAFX
 * @author Bendavid Natane
 */
public class Modal {

    public static void showModalError(final String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }

    public static void showModalInfo(final String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }

    public static void showModalInfoExtend(final String msg, final String header) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        VBox dialogPaneContent = new VBox();

        Label label = new Label(header);
        String stackTrace = msg;
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);
        dialogPaneContent.getChildren().addAll(label, textArea);

        a.getDialogPane().setContent(dialogPaneContent);
        a.showAndWait();
    }

}
