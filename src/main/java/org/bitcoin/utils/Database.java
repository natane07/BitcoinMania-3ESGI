package org.bitcoin.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Gestion de l'accès à la base de données du serveur
 * @author Natane Bendavid
 */

public class Database {
    private static String URL = "jdbc:mysql://localhost:3306/bitcoinmania";
    private static String LOGIN = "root";
    private static String PASSWORD = "";

     /**
      * Connexion à la base données
      */
     private Connection connection;

     /**
      * Constructeur
      * @param autCommit Commit automatiquement
      */
     public Database(final boolean autCommit) throws BitcoinmaniaException {

         // Connexion à la base de données
         try {
             connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             connection.setAutoCommit(autCommit);
         } catch (SQLException exception) {
             throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                     "Error connecting database",
                     exception);
         }
     }

     /**
      * Récupère le statement sur la base de données
      * @return le statement sur la base de données
      * @throws BitcoinmaniaException En cas d'erreur de récupération du statement sur la base de données
      */
     public Statement getStatement() throws BitcoinmaniaException {
         Statement statement;
         try {
             statement = connection.createStatement();
         } catch (SQLException exception) {
             throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                     "Error creating Statement object",
                     exception);
         }
         return statement;
     }

     /**
      * Commit toutes les modifications depuis le dernier commit ou rollback
      * @throws BitcoinmaniaException En cas d'erreur de commit des modifications
      */
     public void commit() throws BitcoinmaniaException {
         try {
             connection.commit();
         } catch (SQLException exception) {
             throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                     "Error commiting modifications",
                     exception);
         }
     }

     /**
      * Rollback toutes les modifications depuis le dernier commit ou rollback
      * @throws BitcoinmaniaException En cas d'erreur de rollback des modifications
      */
     public void rollback() throws BitcoinmaniaException {
         try {
             connection.rollback();
         } catch (SQLException exception) {
             throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                     "Error rollbacking modifications",
                     exception);
         }
     }

     /**
      * Ferme la connexion à la base de données
      * @throws BitcoinmaniaException En cas d'erreur de fermeture de la connexion à la base de données
      */
     public void close() throws BitcoinmaniaException {
         try {
             connection.close();
         } catch (SQLException exception) {
             throw new BitcoinmaniaException(Error.ERROR_INTERNAL_CODE,
                     "Error closing database",
                     exception);
         }
     }
}
