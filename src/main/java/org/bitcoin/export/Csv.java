package org.bitcoin.export;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.bitcoin.api.ApiBitcoin;
import org.bitcoin.utils.BitcoinmaniaException;
import org.bitcoin.utils.Error;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class Csv {

    /**
     * Ecrire un fihier Csv
     * @param pathFile Chemin ou ecrire le fichier
     * @param data Les données a écrire dans le fichier, Ex: [{"date" : "2020-07-07", "quota" : 9180.40}, {...}]
     */
    public void whriteCsvFile (final String pathFile, ArrayList<String> data) throws BitcoinmaniaException {
        try {
            // create a writer
            Writer writer = Files.newBufferedWriter(Paths.get(pathFile + "/BitcoinMania.csv"));
            // write CSV file
            CSVPrinter printer = CSVFormat.DEFAULT.withHeader("ID", "Date", "Quota").print(writer);

            int ctr = 1;
            for (String value : data) {
                printer.printRecord(ctr, value, ApiBitcoin.getPriceBitcoinWithDate(value));
                ctr++;
            }
            // flush the stream
            printer.flush();
            // close the writer
            writer.close();
        } catch (Exception error) {
            throw new BitcoinmaniaException(Error.ERROR_EXPORT_FILE_CSV_CODE,
                    Error.ERROR_EXPORT_FILE_CSV_MSG,
                    error);
        }
    }
}
