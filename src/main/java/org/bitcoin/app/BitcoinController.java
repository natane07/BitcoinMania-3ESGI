package org.bitcoin.app;
import javafx.scene.layout.BorderPane;
import org.bitcoin.utils.Error;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.bitcoin.api.ApiBitcoin;
import org.bitcoin.excel.Import;
import org.bitcoin.excel.Line;
import org.bitcoin.excel.Recording;
import org.bitcoin.utils.BitcoinmaniaException;
import org.bitcoin.utils.Modal;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
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

    @FXML
    private LineChart<String, Number> lineChartGraphiqueExcel;

    @FXML
    private CategoryAxis xNumberAxisExcel;

    @FXML
    private NumberAxis yNumberAxisExcel;

    @FXML
    private TextField seuilExcel;

    @FXML
    private TextField nbMinute;

    private XYChart.Series lineSeuilExcel;

    private XYChart.Series seriesExcel;

    private String urlFileExcel;

    public String getUrlFileExcel() {
        return urlFileExcel;
    }

    public void setUrlFileExcel(String urlFileExcel) {
        this.urlFileExcel = urlFileExcel;
    }

    private ArrayList<Line> linesExcel;

    private ArrayList<Line> newTabMoyenne = new ArrayList<Line>();

    // Onglet mon compte
    @FXML
    private PasswordField newPwd;

    @FXML
    private PasswordField confirmPwd;

    @FXML
    private ComboBox comboBoxMoneyUser;

    @FXML
    private Label nameUser;

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
        final String moneyUser = App.user.getMoney().isEmpty() ? "EUR" : App.user.getMoney();
        setComboBoxMoneyValue(moneyUser);
        setComboBoxMoneyValueUser(moneyUser);
        final String money = ApiBitcoin.getSymbolvalue(moneyUser);
        final Double mostMarketPlaceValue = ApiBitcoin.getlastPriceBitcoinvalue(moneyUser);
        setCourBitcoinValue(mostMarketPlaceValue, money);
        nameUser.setText(App.user.getLastname() + " " + App.user.getName());
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

    private void setComboBoxMoneyValue(final String moneyString) {
        var money = ApiBitcoin.getAllExchange();
        System.out.println(money);
        ObservableList<String> list = FXCollections.observableArrayList(money);
        comboBoxMoney.setValue(moneyString);
        comboBoxMoney.setItems(list);
    }

    private void setComboBoxMoneyValueUser(final String moneyString) {
        var money = ApiBitcoin.getAllExchange();
        ObservableList<String> list = FXCollections.observableArrayList(money);
        comboBoxMoneyUser.setValue(moneyString);
        comboBoxMoneyUser.setItems(list);
    }

    private void setCourBitcoinValue(Double value, String money) {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(money);
        String utf8EncodedString = StandardCharsets.UTF_8.decode(buffer).toString();
        courBitcoin.setText("Cours du bitcoin: " + value + utf8EncodedString);
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
        lineSeuilHaut.setName("Seuil haut");
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

    @FXML
    public void importFile(ActionEvent actionEvent) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText("Chargement du fichier...");
        a.show();

        FileChooser file = new FileChooser();
        file.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier xlsx", "*.xlsx"));
        File f = file.showOpenDialog(null);
        if (f == null) {
            Modal.showModalError("Erreur lors du chargement du fichier");
            return;
        }

        urlFile.setText("Nom du fichier : " + f.getName());
        urlFileExcel = f.getAbsolutePath();
        Import importFile = new Import();
        try {
            linesExcel = importFile.getTab(urlFileExcel, -1);
            App.logger.debug(linesExcel.toString());
            generateChartLineExcel(linesExcel);

        } catch (IOException e) {
            Modal.showModalError(e.getMessage());
            return;
        }
        a.close();
    }

    @FXML
    public void addSeuilExcel(ActionEvent actionEvent) {
        App.logger.debug("addSeuilExcel");
        if(newTabMoyenne.isEmpty()) {
            Modal.showModalError("Aucun fichier importer");
            return;
        }

        if(!isNumeric(seuilExcel.getText())) {
            Modal.showModalError("Le seuil doit etre de type numérique");
            return;
        }

        Double seuilExcelNumeric = Double.parseDouble(seuilExcel.getText());
        if (seuilExcelNumeric < 0) {
            Modal.showModalError("Le seuil doit etre supérieur à 0");
            return;
        }

        lineChartGraphiqueExcel.getData().removeAll(lineSeuilExcel);

        lineSeuilExcel = new XYChart.Series();
        lineSeuilExcel.setName("Seuil");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateStringStart = format.format(newTabMoyenne.get(0).getDate());
        String dateStringEnd = format.format(newTabMoyenne.get(newTabMoyenne.size() - 1).getDate());
        lineSeuilExcel.getData().add(new XYChart.Data(dateStringStart, seuilExcelNumeric));
        lineSeuilExcel.getData().add(new XYChart.Data(dateStringEnd , seuilExcelNumeric));

        lineChartGraphiqueExcel.getData().add(lineSeuilExcel);

        Import importFile = new Import();
        Modal.showModalInfo("Le seuil a été dépassé " + importFile.get_nb_threshold_passed(newTabMoyenne, seuilExcelNumeric)
                                + " sur le graphe"
                                + "\n Le seuil a été dépassé " + importFile.get_nb_threshold_passed(linesExcel, seuilExcelNumeric)
                                + " dans tous le fichier");
    }

    @FXML
    public void removeSeuilExcel(ActionEvent actionEvent) {
        lineChartGraphiqueExcel.getData().removeAll(lineSeuilExcel);
    }

    @FXML
    public void fastChangeExcel(ActionEvent actionEvent) {
        App.logger.debug("lowfastChangeExcel");
        if(newTabMoyenne.isEmpty()) {
            Modal.showModalError("Aucun fichier importer");
            return;
        }

        if(!isNumeric(seuilExcel.getText())) {
            Modal.showModalError("Le seuil doit etre de type numérique");
            return;
        }

        if(!isNumeric(nbMinute.getText())) {
            Modal.showModalError("Le nombre de minute doit etre de type numérique");
            return;
        }

        Double seuilExcelNumeric = Double.parseDouble(seuilExcel.getText());
        if (seuilExcelNumeric < 0) {
            Modal.showModalError("Le seuil doit etre supérieur à 0");
            return;
        }

        Long nbMinuteNumeric = Long.parseLong(nbMinute.getText());
        if (nbMinuteNumeric < 0) {
            Modal.showModalError("Le seuil doit etre supérieur à 0");
            return;
        }

        Import importFile = new Import();
        ArrayList<Recording> recordingByFile = importFile.get_fast_changes_minute(linesExcel, seuilExcelNumeric, nbMinuteNumeric);
        String message = "";
        for (Recording value : recordingByFile) {
            message += value.toString() + "\n";
        }
        Modal.showModalInfoExtend(message, "Hausse : (" + recordingByFile.size() + ")");
    }
    @FXML
    public void lowChangeExcel(ActionEvent actionEvent) {
        App.logger.debug("lowChangeExcel");
        if(newTabMoyenne.isEmpty()) {
            Modal.showModalError("Aucun fichier importer");
            return;
        }

        if(!isNumeric(seuilExcel.getText())) {
            Modal.showModalError("Le seuil doit etre de type numérique");
            return;
        }

        if(!isNumeric(nbMinute.getText())) {
            Modal.showModalError("Le nombre de minute doit etre de type numérique");
            return;
        }

        Double seuilExcelNumeric = Double.parseDouble(seuilExcel.getText());
        if (seuilExcelNumeric < 0) {
            Modal.showModalError("Le seuil doit etre supérieur à 0");
            return;
        }

        Long nbMinuteNumeric = Long.parseLong(nbMinute.getText());
        if (nbMinuteNumeric < 0) {
            Modal.showModalError("Le seuil doit etre supérieur à 0");
            return;
        }

        Import importFile = new Import();
        ArrayList<Recording> recordingByFile = importFile.get_low_changes_minute(linesExcel, seuilExcelNumeric, nbMinuteNumeric);
        String message = "";
        for (Recording value : recordingByFile) {
            message += value.toStringBaisse() + "\n";
        }
        Modal.showModalInfoExtend(message, "Baisse : (" + recordingByFile.size() + ")");
    }

    private void generateChartLineExcel(ArrayList<Line> linesExcel) {
        App.logger.debug("Generate Chart Line Excel");
        lineChartGraphiqueExcel.getData().removeAll(seriesExcel);
        seriesExcel = new XYChart.Series();
        xNumberAxisExcel = new CategoryAxis();
        yNumberAxisExcel = new NumberAxis();

        ArrayList<Line> newTabMoyenneDay = createLineTabDay(linesExcel);
        if (newTabMoyenneDay.size() >= 42) {
            newTabMoyenne = createLineTabMonth(linesExcel);
            seriesExcel.setName("Cotation Excel par mois");
        } else {
            newTabMoyenne = newTabMoyenneDay;
            seriesExcel.setName("Cotation Excel par jour");
        }

        App.logger.debug(newTabMoyenneDay.toString());
        App.logger.debug(newTabMoyenne.toString());

        for (Line value : newTabMoyenne) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = format.format(value.getDate());
            seriesExcel.getData().add(new XYChart.Data(dateString, value.getCurrent()));

        }
        lineChartGraphiqueExcel.getData().add(seriesExcel);
        removeSeuilExcel(null);
    }

    private ArrayList<Line> createLineTabDay(ArrayList<Line> linesExcel) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateCompare = format.format(linesExcel.get(0).getDate());
        ArrayList<Line> lineTransform = new ArrayList<Line>();
        double sum = 0;
        int nbLine = 0;
        for (Line value : linesExcel) {
            String dateString = format.format(value.getDate());
            if (dateString.equals(dateCompare)) {
                sum += value.getCurrent();
                nbLine += 1;
            } else {
                Date date = null;
                try {
                    date = format.parse(dateCompare);
                } catch (ParseException e) {
                    Modal.showModalError(e.getMessage());
                }
                Line line = new Line(date, sum / nbLine);
                lineTransform.add(line);
                sum = value.getCurrent();
                nbLine = 1;
                dateCompare = format.format(value.getDate());
            }
        }
        return lineTransform;
    }

    private ArrayList<Line> createLineTabMonth(ArrayList<Line> linesExcel) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(linesExcel.get(0).getDate());
        calendar.add(Calendar.MONTH, 1);
        String dateCompare = format.format(calendar.getTime());
        ArrayList<Line> lineTransform = new ArrayList<Line>();
        double sum = 0;
        int nbLine = 0;
        for (Line value : linesExcel) {
            String dateString = format.format(value.getDate());
            if (dateString.equals(dateCompare)) {
                // Recupere l'ancienne date
                calendar.setTime(value.getDate());
                calendar.add(Calendar.MONTH, -1);
                Date lastdate = calendar.getTime();
                Line line = new Line(lastdate, sum / nbLine);
                lineTransform.add(line);
                // Mise a jour des valeurs
                calendar.setTime(value.getDate());
                calendar.add(Calendar.MONTH, 1);
                dateCompare = format.format(calendar.getTime());
                sum = value.getCurrent();
                nbLine = 1;
            } else {
                sum += value.getCurrent();
                nbLine += 1;
            }
        }
        // Ajout du dernier mois
        Line lastLine = linesExcel.get(linesExcel.size() - 1);
        Line line = new Line(lastLine.getDate(), sum / nbLine);
        lineTransform.add(line);
        return lineTransform;
    }
        }
    }
}
