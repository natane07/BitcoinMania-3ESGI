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
import org.bitcoin.utils.Modal;

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
    private PasswordField pwdConnexionText;
    @FXML
    private TextField loginConnexionText;

    @FXML
    public void registerUser(ActionEvent actionEvent) {
        App.user.setName(nameText.getText());
        App.user.setLastname(lastNameText.getText());
        App.user.setMail(mailText.getText());
        App.user.setLogin(loginText.getText());
        App.user.setPwd(pwdText.getText());
        App.user.setMoney("");

        boolean isRegister = false;
        try {
            isRegister = App.user.register();
        } catch (BitcoinmaniaException exception) {
            Modal.showModalError(exception.getMsgError());
            return;
        }

        if(isRegister) {
            Modal.showModalInfo("L'utilisateur à bien été enregistrer");
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/bitcoin.fxml"));
                App.stage.setScene(new Scene(root));
                App.stage.show();
                return;
            } catch (Exception exception) {
                Modal.showModalError(exception.getMessage());
                return;
            }
        }
        Modal.showModalError(Error.ERROR_USER_REGISTER_MSG);
    }

    public void loginUser(ActionEvent actionEvent) {
        App.user.setLogin(loginConnexionText.getText());
        App.user.setPwd(pwdConnexionText.getText());
        boolean isLogin = false;
        try {
            isLogin = App.user.login();
        } catch (BitcoinmaniaException exception) {
            Modal.showModalError(exception.getMsgError());
            return;
        }

        if(isLogin) {
            Modal.showModalInfo("L'utilisateur à bien été connecté");
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/bitcoin.fxml"));
                App.stage.setScene(new Scene(root));
                App.stage.show();
                return;
            } catch (Exception exception) {
                Modal.showModalError(exception.getMessage());
                return;
            }
        }
        Modal.showModalError(Error.ERROR_USER_LOGIN_MSG);
    }
}
