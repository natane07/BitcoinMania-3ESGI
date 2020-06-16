package org.bitcoin.utils;

import org.bitcoin.app.App;

/**
 * Classe d'exception
 * @author Bendavid Natane
 */

public class BitcoinmaniaException extends  Exception {

    /**
     * Code erreur
     */
    private int codeError;

    /**
     * Message d'erreur
     */
    private String msgError;

    /**
     * Constructeur de la classe
     * @param codeError Code d'erreur
     * @param msgError Message d'erreur
     */
    public BitcoinmaniaException(final int codeError, final String msgError) {
        super();
        this.codeError = codeError;
        this.msgError = msgError;
        App.logger.error(codeError, msgError, null);
    }

    /**
     * Constructeur de la classe
     * @param codeError Code d'erreur
     * @param msgError Message d'erreur
     * @param exception Exception
     */
    public BitcoinmaniaException(final int codeError, final String msgError, final Exception exception) {
        super(exception);
        this.codeError = codeError;
        this.msgError = msgError;
        App.logger.error(codeError, msgError, exception);
    }

    /**
     * Récupération du code d'erreur
     * @return le code d'erreur
     */
    public int getCodeError() {
        return this.codeError;
    }

    /**
     * Récupération du message d'erreur
     * @return le message d'erreur
     */
    public String getMsgError() {
        return this.msgError;
    }
}
