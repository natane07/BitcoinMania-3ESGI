package org.bitcoin.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bitcoin.app.App;

public class Import {

    /**
     *
     * @param filename Nom du fichier
     * @param max_values Nombre ligne maximum extraits
     * @return ArrayList<Line>
     * @throws IOException
     */
    public ArrayList<Line> getTab(String filename, int max_values) throws IOException {
        ArrayList<String> Headers= new ArrayList<String>();
        ArrayList<Line> Tab = new ArrayList<Line>();
        File excelFile = new File(filename);
        FileInputStream fis = new FileInputStream(excelFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIt = sheet.iterator();
        int i = 0;

        while (rowIt.hasNext()) {
            i++;
            Row row = rowIt.next();
            Date d = new Date();
            double current = 0.0;
            Iterator<Cell> headers = row.cellIterator();
            Iterator<Cell> cellIterator = row.cellIterator();

            //Collecter & Sauter les entêtes
            if(row.getRowNum() == 0) {
                while(headers.hasNext()) {
                    Cell header = headers.next();
                    if (header != null || header.getCellType() != Cell.CELL_TYPE_BLANK) {
                        Headers.add(header.getStringCellValue());
                    }
                }
                continue;
            }
            //Valider les entêtes
            if(!validateHeaders(Headers)) {
                return new ArrayList<>();
            }
            //S'arrêter après un nombre max d'enregistrements
            if (i == max_values) {
                break;
            }
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        d = cell.getDateCellValue();
                    } else {
                        current = cell.getNumericCellValue();
                    }
                }
            }
            Tab.add(new Line(d, current));
        }
        return Tab;
    }




    //Déterminer le nombre de fois que le seuil est dépassé
    /**
     * déterminer une valeur seuil et déterminer le nombre de fois que la cotation dépasse
     * cette valeur seuil,
     * @param lines tableau
     * @param threshold valeur seuil
     * @return
     */
    public int get_nb_threshold_passed(ArrayList<Line> lines, double threshold) {
        int max = 0;
        for (int i = 0; i < lines.size(); i++) {
            if(lines.get(i).getCurrent() > threshold) {
                max += 1;
            }
        }
        return max;
    }


    //Extrait d'une ArrayList
    public ArrayList<Line> get_sub_array(ArrayList<Line> lines, int start, int end) {
        ArrayList<Line> result = new ArrayList<Line>();
        if(start > 0 && end < lines.size()) {
            for (int i = start; i < end; i++) {
                result.add(lines.get(i));
            }
        }
        return result;
    }

    /**
     * Récupération dans une arraylist des hausse et des baisses rapides
     * @param lines ArrayList<Line>
     * @param threshold valeur Seuil
     * @param date_limit l'écart entre les deux dates considéré comme rapide
     * @return
     */
    public ArrayList<Recording> get_fast_changes (ArrayList<Line> lines, double threshold, long date_limit) {
        double gap;
        long date_diff;
        ArrayList<Recording> Rcd = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = i + 1; j < lines.size(); j++) {
                gap = lines.get(i).getCurrent() - lines.get(j).getCurrent();
                date_diff = (lines.get(j).getDate().getTime() - lines.get(i).getDate().getTime()) / 1000;
                if ((gap > threshold || gap < -threshold) && date_diff <= date_limit) {
                    Rcd.add(new Recording(lines.get(i), lines.get(j), gap, date_diff));
                }
            }
        }
        return Rcd;
    }

    /**
     * Récupération dans une arraylist des hausse rapides
     * @param lines ArrayList<Line>
     * @param threshold valeur Seuil
     * @param date_limit l'écart entre les deux dates considéré comme rapide Minute
     * @return
     */
    public ArrayList<Recording> get_fast_changes_minute (ArrayList<Line> lines, double threshold, long date_limit) {
        double gap;
        long date_diff;
        ArrayList<Recording> Rcd = new ArrayList<>();
        int date_limit_minute = 1;
        if (date_limit > 5) {
            date_limit_minute = (int) (date_limit / 5);
        }
        for (int i = 0; i < lines.size() - date_limit_minute; i++) {
            gap = lines.get(i).getCurrent() - lines.get(i + date_limit_minute).getCurrent();
            date_diff = (lines.get(i + date_limit_minute).getDate().getTime() - lines.get(i).getDate().getTime()) / 1000;

            if ((gap < -threshold) && date_diff <= date_limit * 60) {
                Rcd.add(new Recording(lines.get(i), lines.get(i + date_limit_minute), gap, date_diff));
            }
        }
        return Rcd;
    }

    /**
     * Récupération dans une arraylist des baisses rapides
     * @param lines ArrayList<Line>
     * @param threshold valeur Seuil
     * @param date_limit l'écart entre les deux dates considéré comme rapide Minute
     * @return
     */
    public ArrayList<Recording> get_low_changes_minute (ArrayList<Line> lines, double threshold, long date_limit) {
        double gap;
        long date_diff;
        ArrayList<Recording> Rcd = new ArrayList<>();
        int date_limit_minute = 1;
        if (date_limit > 5) {
            date_limit_minute = (int) (date_limit / 5);
        }
        for (int i = date_limit_minute; i < lines.size(); i++) {
            gap = lines.get(i).getCurrent() - lines.get(i - date_limit_minute).getCurrent();
            date_diff = (lines.get(i - date_limit_minute).getDate().getTime() - lines.get(i).getDate().getTime()) / 1000;

            if ((gap > threshold) && date_diff <= date_limit * 60) {
                Rcd.add(new Recording(lines.get(i), lines.get(i - date_limit_minute), gap, date_diff));
            }
        }
        return Rcd;
    }


    public static boolean validateHeaders(ArrayList<String> Headers) {
        if (Headers.size() != 2) {
            System.out.println("Nombre invalide d'entêtes");
            return false;
        }
        if (!Headers.get(0).toLowerCase().equals("date")) {
            System.out.println("Entête date introuvable");
            return false;
        }
        if (!Headers.get(1).toLowerCase().equals("cours")) {
            System.out.println("Entête cours introuvable");
            return false;
        }
        return true;
    }
}

