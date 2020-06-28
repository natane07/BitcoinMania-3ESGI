package org.bitcoin.utils;

import javafx.scene.control.Alert;

/**
 * Affichage des modal JAVAFX
 * @author Bendavid Natane
 */
public class Modal {

    public static void showModalError(final String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.show();
    }

    public static void showModalInfo(final String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.show();
    }

}
