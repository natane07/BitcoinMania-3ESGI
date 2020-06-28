package org.bitcoin.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import org.bitcoin.utils.BitcoinmaniaException;
import org.bitcoin.utils.Error;

import java.io.IOException;

/**
 * Controller pour l'interface User
 * @author Bendavid Natane
 */
public class LoginController {

    @FXML
    private Button registerBtn;
    @FXML
    private TextField lastNameText;
    @FXML
    private TextField nameText;
    @FXML
    private TextField loginText;
    @FXML
    private TextField mailText;
    @FXML
    private PasswordField pwdText;

    @FXML
    public void registerUser(ActionEvent actionEvent) {
        App.user.setName(nameText.getText());
        App.user.setLastname(lastNameText.getText());
        App.user.setMail(mailText.getText());
        App.user.setLogin(loginText.getText());
        App.user.setPwd(pwdText.getText());
        boolean isRegister = false;
        try {
            isRegister = App.user.register();
        } catch (BitcoinmaniaException exception) {
            Error.showModalError(exception.getMsgError());
            return;
        }

        if(isRegister) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("L'utilisateur à bien été enregistrer");
            a.show();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/bitcoin.fxml"));
                App.stage.setScene(new Scene(root));
                App.stage.show();
                return;
            } catch (Exception exception) {
                Error.showModalError(exception.getMessage());
                return;
            }
        }
        Error.showModalError(Error.ERROR_USER_REGISTER_MSG);
    }
}
