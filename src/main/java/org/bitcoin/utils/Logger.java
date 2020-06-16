package org.bitcoin.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Gestionnaire des logs
 * @author Bendavid Natane
 */
public class Logger {
    /**
     * Niveaux de log
     */
    public enum LogLevel {
        /**
         * Niveau de debug
         */
        Debug,

        /**
         * Niveau d'information
         */
        Info,

        /**
         * Niveau d'avertissement
         */
        Warn,

        /**
         * Niveau d'erreur
         */
        Error
    }

    /**
     * Format de date du nom des fichiers de log
     */
    private final String LOGS_FILE_NAME_DATE_FORMAT = "yyyyMMdd";

    /**
     * Format d'heure et de date des logs
     */
    private final String LOGS_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * Nombre de jours de conservation des fichiers de log
     */
    private final int NB_DAYS_KEEP_LOG_FILES = 60;

    /**
     * Dossier d'enregistrement des fichiers de log
     */
    private String logsDirPath = "";

    /**
     * Format de date du nom des fichiers de log
     */
    private String logsFileNamePrefix = "BitcoinMania_";

    /**
     * Logs en debug ou non
     */
    private boolean debug = false;

    /**
     * Logs en debug console ou non
     */
    private boolean debugOnConsole = false;

    /**
     * Indicateur d'initialisation du logger
     */
    private boolean sInit = false;

    /**
     * Chemin du dossier des fichiers de logs
     */
    private String logsdirPath = "C:/Users/MOI/dev/logs";

    /**
     * Initialise le logger devant être appelé au lancement de l'application
     */
    public Logger() {
        // Configuration du module de log
        try {
            logsDirPath = this.logsdirPath;
            debug = true;
            debugOnConsole = true;
        }
        catch (Exception exception) {
            System.err.print("Error initializing Logger: " + exception.getMessage());
            exception.printStackTrace();
            return;
        }

        sInit = true;
    }

    /**
     * Log un message de debug en mode debug
     * @param message Message du log
     */
    public void debug(final String message) {
        if (debug) {
            log(LogLevel.Debug, 0, message, null);
        }
    }

    /**
     * Log un message d'information
     * @param message Message du log
     */
    public void info(final String message) {
        log(LogLevel.Info, 0, message, null);
    }

    /**
     * Log un message d'avertissement
     * @param errorCode Code d'erreur
     * @param message Message du log
     * @param exception Exception du log, peut être null
     */
    public void warn(final int errorCode, final String message, final Exception exception) {
        log(LogLevel.Warn, errorCode, message, exception);
    }

    /**
     * Log un message d'erreur
     * @param errorCode Code d'erreur
     * @param message Message du log
     * @param exception Exception du log, peut être null
     */
    public void error(final int errorCode, final String message, final Exception exception) {
        log(LogLevel.Error, errorCode, message, exception);
    }


    /**
     * Log un message
     * @param logLevel Niveau de log
     * @param errorCode Code d'erreur, 0 si non renseigné
     * @param message Message du log
     * @param exception Exception du log, peut être null
     */
    private void log(final LogLevel logLevel, final int errorCode, final String message, final Exception exception) {
        // Si le logger n'est pas initialisé, on quitte la fonction
        if (!sInit) {
            return;
        }

        // Informations sur la ligne de code de déclenchement du log
        final StackTraceElement[] stackTraceElementArray = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = null;
        if (stackTraceElementArray.length >= 4) {
            stackTraceElement = stackTraceElementArray[3];
        }

        final String formattedMessage = formatMessage(logLevel, stackTraceElement, message, errorCode, exception);

        if (debugOnConsole) {
            switch (logLevel) {
                case Debug:
                    System.out.print(formattedMessage);
                    break;

                case Info:
                    System.out.print(formattedMessage);
                    break;

                case Warn:
                    System.err.print(formattedMessage);
                    break;

                case Error:
                    System.err.print(formattedMessage);
                    break;
            }
            if (exception != null) {
                exception.printStackTrace();
            }
        }

        // Enregistrement du log dans un fichier
        try {
            final String currentDate = new SimpleDateFormat(LOGS_FILE_NAME_DATE_FORMAT, Locale.FRANCE)
                    .format(Calendar.getInstance().getTime());
            final String logsFilePath = getLogFilePath(currentDate);
            final File logsFile = new File(logsFilePath);
            final FileOutputStream logsFileOutputStream = new FileOutputStream(logsFile, true);
            final OutputStreamWriter logsOutputStreamWriter = new OutputStreamWriter(logsFileOutputStream);
            logsOutputStreamWriter.append(formattedMessage);
            logsOutputStreamWriter.close();
            logsFileOutputStream.close();
        } catch (IOException fileException) {
            if (debug) {
                System.err.print("Exception on logging a message: " + fileException.getMessage());
                fileException.printStackTrace();
            }
        }
    }

    /**
     * Formate un message de log
     * @param logLevel Niveau de log
     * @param stackTraceElement Élément de trace de la pile qui a déclenché le log, peut être null
     * @param message Message du log
     * @param errorCode Code d'erreur, 0 si non renseigné
     * @param exception Exception du log, peut être null
     * @return Un message de log formaté
     */
    private String formatMessage(final LogLevel logLevel, final StackTraceElement stackTraceElement,
                                 final String message, final int errorCode, final Exception exception) {
        // Date
        final SimpleDateFormat dateFormat = new SimpleDateFormat(LOGS_DATE_TIME_FORMAT, Locale.FRANCE);
        String messageFormatted = dateFormat.format(Calendar.getInstance().getTime());

        // Log level
        messageFormatted += ((logLevel != null) ? (" #" + logLevel.toString() + "#") : " #Undefined#");

        // Élément de trace de la pile
        if (stackTraceElement != null) {
            messageFormatted += " {";
            final String fileName = stackTraceElement.getFileName();
            if (fileName != null && !fileName.isEmpty()) {
                messageFormatted += fileName.replace(".java", "") + ":";
            }
            final String methodName = stackTraceElement.getMethodName();
            if (methodName != null && !methodName.isEmpty()) {
                messageFormatted += methodName + ":";
            }
            messageFormatted += stackTraceElement.getLineNumber() + "}";
        }

        // Code d'erreur
        if (errorCode != 0) {
            messageFormatted += " [0x"+ String.format("%08X", errorCode) + "]";
        }

        // Message
        if (message != null && !message.isEmpty()) {
            messageFormatted += " " + message;
        }

        // Exception
        if (exception != null) {
            String exceptionStackTrace = Arrays.toString(exception.getStackTrace());
            int exceptionLength = exceptionStackTrace.length();
            if (exceptionLength > 1000) {
                exceptionLength = 1000;
            }
            messageFormatted += " EXCEPTION FOUND: CLASS: " + exception.getClass().getName() +
                    " MESSAGE: " + exception.getLocalizedMessage() +
                    " STACKTRACE: " + exceptionStackTrace.substring(0, exceptionLength);
        }
        return messageFormatted + '\n';
    }

    /**
     * Récupère le chemin d'un fichier de logs
     * @param date Date du fichier de logs au format yyyyMMdd
     * @return Le chemin du fichier de logs de la date
     */
    private String getLogFilePath(final String date) {
        return logsDirPath + File.separator + logsFileNamePrefix + date + ".log";
    }
}