package org.bitcoin.export;

import org.bitcoin.utils.BitcoinmaniaException;
import org.bitcoin.utils.Error;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;


public class SQL {
    private String fileContent;

    public String getFileContent() {
        return fileContent;
    }

    public SQL() {
        fileContent = "";
    }

    /**
     * Add to fileContent a CREATE TABLE querry
     * @param nameOfTable Name of the table to create
     * @param dateColumn Name of the first column
     * @param quotaColumn Name of the second column
     */
    public void createTable(String nameOfTable, String dateColumn, String quotaColumn) {
        if (nameOfTable == null || dateColumn == null || quotaColumn == null) {
            System.err.println("Parameter can't be null");
            return;
        }
        this.fileContent += "CREATE TABLE " + nameOfTable + "(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " + dateColumn + " DATE, " + quotaColumn + " FLOAT);\n";
    }

    /**
     * Add to the fileContent variable INSERT querries
     * @param nameOfTable Name of the table to insert in
     * @param firstField Name of the first column
     * @param secondField Name of the second column
     * @param data Array of maps with data, ex : [{"date" : "19/09/2019", "quota" : 190.4}, {...}]
     */
    public void insertTable(String nameOfTable, String firstField, String secondField, ArrayList<Map<String, Object>> data) {
        if(nameOfTable == null ||firstField == null || secondField == null){
            System.err.println("Parameter can't be null");
            return;
        }
        for (Map<String, Object> i : data) {
            this.fileContent += "INSERT INTO " + nameOfTable + "(" + firstField + ", " + secondField + ") VALUES('" + i.get("date") + "', " + i.get("quota").toString() + ");\n";
        }
    }

    /**
     * Write a sql file with fileContent variable as the content
     * @param path Path where to write the file
     */
    public void writeSqlFile(String path) throws BitcoinmaniaException {
        try {
            Files.write(Paths.get(path), fileContent.getBytes());
        } catch (IOException error) {
            throw new BitcoinmaniaException(Error.ERROR_EXPORT_FILE_SQL_CODE,
                    Error.ERROR_EXPORT_FILE_SQL_MSG,
                    error);
        }
    }
}
