package org.bitcoin.excel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Launcher {

    public static void main(String args[]) throws IOException {

        Import I = new Import();
        String f = "ressources\\Fichier Excel.xlsx";
        ArrayList<Line> lines = I.getTab(f, 10);

        System.out.println("Nombre de fois que le seuil est dépassé: " + I.get_nb_threshold_passed(lines, 5000.0));

        ArrayList<Recording> rcd = I.get_fast_changes(lines, 10000.0, 300);
        System.out.println("Il y a " + rcd.size() + " enregistrements");
        for (Recording r : rcd) {
            System.out.println(r);
        }

    }

}
