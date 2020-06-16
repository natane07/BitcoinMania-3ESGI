package org.bitcoin.model;

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

    public static void test() throws BitcoinmaniaException {
        // Interrogation de la base données
        final Database database = new Database(true);
        final Statement statement = database.getStatement();
        final String query = "SELECT * FROM `user`";
        App.logger.debug("Check initialized device exists: \"" + query + "\"");
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException exception) {
            database.close();
            throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                    "Error executing select \"" + query + "\"",
                    exception);
        }

        try {
            while (resultSet.next()) {
                final String login = resultSet.getString("login");
                final String password = resultSet.getString("pwd");
                final String name = resultSet.getString("name");
                System.out.println(login + " " + password + " " + name);
            }
        } catch (SQLException exception) {
            App.logger.debug("Error DB: \"" + query + "\"");
            throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                    "Error getting select result of \""+ query + "\"",
                    exception);
        } finally {
            database.close();
        }
    }
}
