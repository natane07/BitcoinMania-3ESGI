package org.bitcoin.app;

import org.bitcoin.api.ApiBitcoin;
import org.bitcoin.model.User;
import org.bitcoin.utils.BitcoinmaniaException;

/**
 * Controller pour l'interface
 * @author Bendavid Natane
 */
public class Controller {

    public void pressButton(javafx.event.ActionEvent actionEvent) {
        System.out.println("Hello word !");
        ApiBitcoin.test();
        try {
            User.test();
        } catch (BitcoinmaniaException exception) {
            App.logger.error(exception.getCodeError(), exception.getMessage(), exception);
        }
    }
}
