package org.bitcoin.model;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.bitcoin.app.App;
import org.bitcoin.utils.BitcoinmaniaException;
import org.bitcoin.utils.Database;
import org.bitcoin.utils.Error;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Model BDD user
 * @author Bendavid Natane
 */

public class User {

    private String name = "";
    private String lastname = "";
    private String login = "";
    private String pwd = "";
    private String mail = "";

    public boolean register() throws BitcoinmaniaException {
        // Verification des données de l'utilisateur
        if(!checkValidatorRegister()) {
            throw new BitcoinmaniaException(Error.ERROR_CHECK_USER_DATA_CODE, Error.ERROR_CHECK_USER_DATA_MSG, null);
        }
        if(!checkValidatorMail()) {
            throw new BitcoinmaniaException(Error.ERROR_CHECK_USER_MAIL_CODE, Error.ERROR_CHECK_USER_MAIL_MSG, null);
        }
        if (checkMailExist()) {
            throw new BitcoinmaniaException(Error.ERROR_CHECK_USER_MAIL_EXIST_CODE, Error.ERROR_CHECK_USER_MAIL_EXIST_MSG, null);
        }
        if (checkLoginExist()) {
            throw new BitcoinmaniaException(Error.ERROR_CHECK_USER_LOGIN_EXIST_CODE, Error.ERROR_CHECK_USER_LOGIN_EXIST_MSG, null);
        }

        // Insertion de la ligne
        final Database database = new Database(true);
        final Statement statement = database.getStatement();
        final String sha256HexPwd = DigestUtils.sha256Hex(pwd);
        final String query = "INSERT INTO `user` (`name`, lastname, login, mail, pwd) VALUES ('" + name + "','" + lastname + "','" + login + "','" + mail
        + "','" + sha256HexPwd +"')";
        int result = 0;
        try {
            App.logger.debug("Insert new line on database: \"" + query + "\"");
            result = statement.executeUpdate(query);
        } catch (SQLException exception) {
            throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                    "Error getting select result of \""+ query + "\": ",
                    exception);
        } finally {
            database.close();
        }

        // Vérification du résultat
        if (result < 1) {
            return false;
        }
        return true;
    }

    /**
     * Logger l'utilisateur
     * @return true si l'utilisateur est logger
     * @throws BitcoinmaniaException
     */
    public boolean login() throws BitcoinmaniaException {
        // Verification
        if(!checkValidatorLogin()) {
            throw new BitcoinmaniaException(Error.ERROR_CHECK_LOGIN_CODE, Error.ERROR_CHECK_LOGIN_MSG, null);
        }

        // Interrogation de la base données
        final Database database = new Database(true);
        final Statement statement = database.getStatement();
        final String sha256HexPwd = DigestUtils.sha256Hex(pwd);
        final String query = "SELECT * FROM `user` WHERE login = '" + login + "' AND pwd = '" + sha256HexPwd + "'";
        App.logger.debug("Query Login User : \"" + query + "\"");
        ResultSet resultSet = null;

        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException exception) {
            database.close();
            throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                    "Error executing select \"" + query + "\"",
                    exception);
        }

        // Récupération du résultat
        try {
            if (resultSet != null && resultSet.next()) {
                login = resultSet.getString("login");
                pwd = resultSet.getString("pwd");
                name = resultSet.getString("name");
                lastname = resultSet.getString("lastname");
                mail = resultSet.getString("mail");
                App.logger.info("User login with:" + login);
                return true;
            }
        } catch (SQLException exception) {
            throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                    "Error getting select result of \""+ query + "\": ",
                    exception);
        } finally {
            database.close();
        }
        return false;
    }

    private boolean checkLoginExist() throws BitcoinmaniaException {
        // Interrogation de la base données
        final Database database = new Database(true);
        final Statement statement = database.getStatement();
        final String sha256HexPwd = DigestUtils.sha256Hex(pwd);
        final String query = "SELECT COUNT(*) AS numberLine FROM `user` WHERE login = '" + login + "'";
        App.logger.debug("Query Login User : \"" + query + "\"");
        ResultSet resultSet = null;

        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException exception) {
            database.close();
            throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                    "Error executing select \"" + query + "\"",
                    exception);
        }

        // Récupération du résultat
        try {
            if (resultSet != null && resultSet.next() && resultSet.getInt("numberLine") > 0) {
                return true;
            }
        } catch (SQLException exception) {
            throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                    "Error getting select result of \""+ query + "\": ",
                    exception);
        } finally {
            database.close();
        }
        return false;
    }

    private boolean checkMailExist() throws BitcoinmaniaException {
        // Interrogation de la base données
        final Database database = new Database(true);
        final Statement statement = database.getStatement();
        final String sha256HexPwd = DigestUtils.sha256Hex(pwd);
        final String query = "SELECT COUNT(*) AS numberLine FROM `user` WHERE mail = '" + mail + "'";
        App.logger.debug("Query Login User : \"" + query + "\"");
        ResultSet resultSet = null;

        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException exception) {
            database.close();
            throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                    "Error executing select \"" + query + "\"",
                    exception);
        }

        // Récupération du résultat
        try {
            if (resultSet != null && resultSet.next() && resultSet.getInt("numberLine") > 0) {
                return true;
            }
        } catch (SQLException exception) {
            throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                    "Error getting select result of \""+ query + "\": ",
                    exception);
        } finally {
            database.close();
        }
        return false;
    }

    private boolean checkValidatorLogin() {
        if (login.isBlank() || pwd.isBlank()) {
            return false;
        }
        return true;
    }

    private boolean checkValidatorRegister() {
        if (login.isBlank() || pwd.isBlank() || name.isBlank() || lastname.isBlank() || mail.isBlank()) {
            return false;
        }
        return true;
    }

    private boolean checkValidatorMail() {
        if (mail.isBlank()) {
            return false;
        }
        return EmailValidator.getInstance().isValid(mail);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

}
