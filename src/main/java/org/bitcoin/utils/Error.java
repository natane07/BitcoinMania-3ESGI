package org.bitcoin.utils;

import javafx.scene.control.Alert;

/**
 * Classe d'erreurs
 * @author Natane Bendavid
 */
public class Error {
    // Other
    public static final int ERROR_INTERNAL_CODE = 199;
    public static final String ERROR_INTERNAL_MSG = "Internal error";

    // USER
    public static final int ERROR_USER_REGISTER_CODE = 200;
    public static final String ERROR_USER_REGISTER_MSG = "Erreur lors de la création de l'utilisateur";
    public static final int ERROR_CHECK_LOGIN_CODE = 201;
    public static final String ERROR_CHECK_LOGIN_MSG = "Login ou mot de passe incorrecte";
    public static final int ERROR_CHECK_USER_DATA_CODE = 202;
    public static final String ERROR_CHECK_USER_DATA_MSG = "Les données de l'utilsateur sont manquantes";
    public static final int ERROR_CHECK_USER_MAIL_CODE = 203;
    public static final String ERROR_CHECK_USER_MAIL_MSG = "L'adresse mail est incorrecte";
    public static final int ERROR_CHECK_USER_MAIL_EXIST_CODE = 204;
    public static final String ERROR_CHECK_USER_MAIL_EXIST_MSG = "L'adresse mail existe déja";
    public static final int ERROR_CHECK_USER_LOGIN_EXIST_CODE = 205;
    public static final String ERROR_CHECK_USER_LOGIN_EXIST_MSG = "Le login existe déja";
    public static final int ERROR_USER_LOGIN_CODE = 206;
    public static final String ERROR_USER_LOGIN_MSG = "Erreur lors de la connexion";
    public static final int ERROR_USER_SAVE_CHANGE_CODE = 207;
    public static final String ERROR_USER_SAVE_CHANGE_MSG = "Les nouveaux mots de passe ne correspondent pas ensemble";
    // Logger
    public static final int ERROR_LOGGER_DIR_FILES_LIST = 500;
    public static final int ERROR_LOGGER_DIR_CONTAINS_BAD_FILE = 501;
    public static final int ERROR_LOGGER_PARSE_LOGS_FILE_DATE = 502;
    public static final int ERROR_LOGGER_REMOVE_FILE = 503;

    // Export file
    public static final int ERROR_EXPORT_FILE_CSV_CODE = 600;
    public static final String ERROR_EXPORT_FILE_CSV_MSG = "Erreur lors de l'écriture du fichier CSV";
    public static final int ERROR_EXPORT_FILE_SQL_CODE = 601;
    public static final String ERROR_EXPORT_FILE_SQL_MSG = "Erreur lors de l'écriture du fichier SQL";
    public static final int ERROR_EXPORT_FILE_SQL_DATA_CODE = 603;
    public static final String ERROR_EXPORT_FILE_SQL_DATA_MSG = "Les données de la table SQL pour l'export sont manquantes";

}

