package org.bitcoin.app;

import com.sun.glass.ui.CommonDialogs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.apache.commons.codec.binary.StringUtils;
import org.bitcoin.api.ApiBitcoin;
import org.bitcoin.utils.Modal;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller pour l'interface Bitcoin
 * @author Bendavid Natane
 */
public class BitcoinController implements Initializable {

    private String minDateGraphique;

    private String maxDateGraphique;

    // Onglet graphique
    @FXML
    private LineChart<Number, Number> lineChartGraphique;
    @FXML
    private CategoryAxis categoriAxisTest;
    @FXML
    private CategoryAxis xNumberAxis;
    @FXML
    private NumberAxis yNumberAxis;
    @FXML
    private TextField seuilBas;
    @FXML
    private TextField seuilHaut;
    @FXML
    private Label courBitcoin;
    @FXML
    private ComboBox comboBoxMoney;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;

    private XYChart.Series series;

    private XYChart.Series lineSeuilBas;

    private XYChart.Series lineSeuilHaut;

    // Onglet import fichier excel

    @FXML
    private Label urlFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.logger.debug("initialize BitcoinController");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        maxDateGraphique = format.format(calendar.getTime());
        calendar.add(Calendar.MONTH, -1);
        minDateGraphique = format.format(calendar.getTime());
        generateChartLine(minDateGraphique, maxDateGraphique);
        App.logger.debug("API OK");
        setComboBoxMoneyValue();
        String money = ApiBitcoin.getSymbolvalue("EUR");
        Double mostMarketPlaceValue = ApiBitcoin.getlastPriceBitcoinvalue("EUR");
        setCourBitcoinValue(mostMarketPlaceValue, money);
    }

    public void generateChartLine(String dateDebut, String dateFin) {
        App.logger.debug("Generate Chart Line");
        var apiMoneyDollard = ApiBitcoin.currentPriceMarketBitcoin(dateDebut, dateFin);
        lineChartGraphique.getData().removeAll(series);
        series = new XYChart.Series();
        xNumberAxis = new CategoryAxis();
        yNumberAxis = new NumberAxis(ApiBitcoin.getMinPriceBitcoin(), ApiBitcoin.getMaxPriceBitcoin(), 1000);
        series.setName("Cotation");
        for (String value:apiMoneyDollard) {
            series.getData().add(new XYChart.Data(value, ApiBitcoin.getPriceBitcoinWithDate(value)));
        }
        lineChartGraphique.getData().add(series);
    }

    public void modifdateGraph(ActionEvent actionEvent) {
        System.out.println(dateFin.getValue());
        if(dateDebut.getValue() ==  null || dateFin.getValue() == null) {
            Modal.showModalError("Les dates ne sont pas renseigné");
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date dateDebutFormat;
        Date dateFinFormat;
        try {
            dateDebutFormat = format.parse(dateDebut.getValue().toString());
            dateFinFormat = format.parse(dateFin.getValue().toString());
        } catch (Exception exception) {
            Modal.showModalError("Les dates ne sont pas renseigné");
            return;
        }

        if (dateDebutFormat.compareTo(dateFinFormat) <= 0) {
            App.logger.debug(dateDebut.getValue().toString() + " " + dateFin.getValue().toString());
            generateChartLine(dateDebut.getValue().toString(), dateFin.getValue().toString());
            minDateGraphique = dateDebut.getValue().toString();
            maxDateGraphique = dateFin.getValue().toString();
            // supprime les seuils
            lineChartGraphique.getData().removeAll(lineSeuilBas, lineSeuilHaut);
        } else {
          Modal.showModalError("La date de début est supérieur à la date de fin");
        }
    }

    public void changeMoney(ActionEvent actionEvent) {
        String money = ApiBitcoin.getSymbolvalue(comboBoxMoney.getValue().toString());
        Double mostMarketPlaceValue = ApiBitcoin.getlastPriceBitcoinvalue(comboBoxMoney.getValue().toString());
        App.logger.debug("Change Money " + money + " " + mostMarketPlaceValue);
        setCourBitcoinValue(mostMarketPlaceValue, money);
    }

    private void setComboBoxMoneyValue() {
        var money = ApiBitcoin.getAllExchange();
        System.out.println(money);
        ObservableList<String> list = FXCollections.observableArrayList(money);
        comboBoxMoney.setValue("EUR");
        comboBoxMoney.setItems(list);
    }
    private void setCourBitcoinValue(Double value, String money) {
        courBitcoin.setText("Cours du bitcoin: " + value + money);
    }

    public void removeSeuil(ActionEvent actionEvent) {
        lineChartGraphique.getData().removeAll(lineSeuilBas, lineSeuilHaut);
    }

    public void addSeuil(ActionEvent actionEvent) {
        App.logger.debug("addSeuil");
        if(!isNumeric(seuilBas.getText()) || !isNumeric(seuilHaut.getText())) {
            Modal.showModalError("Le seuil doit etre de type numérique");
            return;
        }
        Double seuilBasNumeric = Double.parseDouble(seuilBas.getText());
        Double seuilHautNumeric = Double.parseDouble(seuilHaut.getText());

        if (seuilBasNumeric < 0 || seuilHautNumeric < 0) {
            App.logger.debug("Le seuil doit etre supérieur à 0");
            Modal.showModalError("Le seuil doit etre supérieur à 0");
            return;
        }

        if (seuilBasNumeric >= seuilHautNumeric) {
            App.logger.debug("Le seuil haut doit etre supérieur au seuil bas");
            Modal.showModalError("Le seuil haut doit etre supérieur au seuil bas");
            return;
        }
        lineChartGraphique.getData().removeAll(lineSeuilBas, lineSeuilHaut);
        var apiMoneyDollard = ApiBitcoin.currentPriceMarketBitcoin(minDateGraphique, maxDateGraphique);

        lineSeuilBas = new XYChart.Series();
        lineSeuilBas.setName("Seuil bas");
        lineSeuilBas.getData().add(new XYChart.Data(apiMoneyDollard.get(0), seuilBasNumeric));
        lineSeuilBas.getData().add(new XYChart.Data(apiMoneyDollard.get(apiMoneyDollard.size() - 1), seuilBasNumeric));

        lineSeuilHaut = new XYChart.Series();
        lineSeuilHaut.setName("Seuil bas");
        lineSeuilHaut.getData().add(new XYChart.Data(apiMoneyDollard.get(0), seuilHautNumeric));
        lineSeuilHaut.getData().add(new XYChart.Data(apiMoneyDollard.get(apiMoneyDollard.size() - 1), seuilHautNumeric));

        lineChartGraphique.getData().addAll(lineSeuilBas, lineSeuilHaut);
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public void importFile(ActionEvent actionEvent) {
        FileChooser file = new FileChooser();
        file.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier xlsx", "*.xlsx"));
        File f = file.showOpenDialog(null);
        if(f != null) {
            urlFile.setText("Chemin du fichier : " + f.getAbsolutePath());
        }
    }
}
