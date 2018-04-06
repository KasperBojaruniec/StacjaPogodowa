package projekt.sw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import static thorwin.math.Math.polyfit;
import static thorwin.math.Math.polynomial;

public class ProjektSw extends Application {

    XYChart.Series temperatura, wilgotnosc, trendPoints, trendLine;
    final int MAX_NUM = 10;
    int temp;
    int hum;
    ArrayList<Pomiar> baza = new ArrayList<>();
    ArrayList<Pomiar> baza_trend = new ArrayList<>();
    Filter filter;

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        BaseReader.wczytaj(baza);

        final NumberAxis xTemp = new NumberAxis(0, 60, 2);
        xTemp.setMinorTickCount(0);
        final NumberAxis yTemp = new NumberAxis();
        final AreaChart<Number, Number> wykresTemp = new AreaChart<>(xTemp, yTemp);
        wykresTemp.setTitle("Zmiany temperatury w ostatniej minucie");
        temperatura = new XYChart.Series();
        temperatura.setName("Temperatura w ℃");
        wykresTemp.getData().add(temperatura);

        final NumberAxis xHum = new NumberAxis(0, 60, 2);
        final NumberAxis yHum = new NumberAxis();
        final AreaChart<Number, Number> wykresHum = new AreaChart<>(xHum, yHum);
        wykresHum.setTitle("Zmiany wilgotności w ostatniej minucie");
        wilgotnosc = new XYChart.Series();
        wilgotnosc.setName("Wilgotność w %");
        wykresHum.getData().add(wilgotnosc);

        final NumberAxis xTrend = new NumberAxis();
        final NumberAxis yTrend = new NumberAxis();
        trendPoints = new XYChart.Series<Number, Number>();
        trendLine = new XYChart.Series<Number, Number>();
        final LineChart<Number, Number> wykresTrend = new LineChart<>(xTrend, yTrend);
        wykresTrend.getData().add(trendLine);
        wykresTrend.getData().add(trendPoints);
        wykresTrend.getXAxis().setTickLabelsVisible(false);
        wykresTrend.getXAxis().setTickMarkVisible(false);

        wykresTrend.setLegendVisible(false);
        wykresTrend.setPrefWidth(800);
        wykresTrend.setTitle("Linia trendu dla wybranych danych");

       
        for (int i=0; i < 30; i++) {

            temperatura.getData().add(new XYChart.Data(i * 2, baza.get(baza.size()-1-i).getTemp()));
            wilgotnosc.getData().add(new XYChart.Data(i * 2, baza.get(baza.size()-1-i).getHum()));
        }

        ChoiceBox wybor = new ChoiceBox();
        wybor.setItems(FXCollections.observableArrayList(
                "Dziś", "Tydzień", "Miesiąc",
                new Separator(), "Wyczyść")
        );

        wybor.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                udpateWykresTrendu(newValue.intValue());
            }
        });

        Label label = new Label("    Wybierz okres, dla którego chcesz wykreślić linię trendu temperatury");

        HBox hbox1 = new HBox();
        hbox1.getChildren().addAll(wykresTemp, wykresHum);
        HBox hbox2 = new HBox();
        hbox2.getChildren().addAll(wybor, label);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(hbox1, 0, 0);
        grid.add(hbox2, 0, 1);
        grid.add(wykresTrend, 0, 2);

        Scene scene = new Scene(grid, 1000, 900);

        primaryStage.setTitle("Projekt SK");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((event) -> {
            try {
                BaseWriter.zapisz(baza);
            } catch (IOException ex) {
                Logger.getLogger(ProjektSw.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(0);
        });
        String css = ProjektSw.class.getResource("/Wykres.css").toExternalForm();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
        primaryStage.show();
        filter = new Filter(this);
        filter.start();
    }

    public ArrayList<Pomiar> getPomiary(int wybor) {

        ArrayList<Pomiar> pomiary = new ArrayList<>();
        Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();

        switch (wybor) {
            case 0:
                for (int i = 0; i < baza.size(); i++) {
                    if (baza.get(i).getDay() == day && baza.get(i).getMonth() == month && baza.get(i).getYear() == year) {
                        pomiary.add(baza.get(i));
                    }
                }
                break;
            case 1:
                for (int i = 0; i < baza.size(); i++) {
                    if (baza.get(i).getDay() >= day - 7 && baza.get(i).getMonth() == month && baza.get(i).getYear() == year) {
                        pomiary.add(baza.get(i));
                    }
                }
                break;
            case 2:
                for (int i = 0; i < baza.size(); i++) {
                    if (baza.get(i).getMonth() == month) {
                        pomiary.add(baza.get(i));
                    }
                }
        }

        return pomiary;
    }

    public void udpateWykresTrendu(int wybor) {

        if (wybor == 4) {
            trendPoints.getData().clear();
            trendLine.getData().clear();
        } else {
            baza_trend.clear();
            baza_trend = getPomiary(wybor);
            trendPoints.getData().clear();
            trendLine.getData().clear();

            ArrayList<Double> listX = new ArrayList<>();
            ArrayList<Double> listY = new ArrayList<>();

            for (int i = 0; i < baza_trend.size(); i++) {
                Double xd;
                xd = new Double(i);
                listX.add(xd);
                Double xd1;
                xd1 = new Double(baza_trend.get(i).getTemp());
                listY.add(xd1);
            }

            double[] targetX = new double[listX.size()];
            double[] targetY = new double[listY.size()];
            for (int i = 0; i < targetX.length; i++) {
                targetX[i] = listX.get(i);
                targetY[i] = listY.get(i);
            }

            for (int i = 0; i < targetY.length; i++) {
                trendPoints.getData().add(new XYChart.Data<>(targetX[i], targetY[i]));
            }

            double[] coefficients = polyfit(targetX, targetY, 2);

            for (double x = 0; x <= targetX.length; x += 0.05) {
                double y = polynomial(x, coefficients);
                trendLine.getData().add(new XYChart.Data<>(x, y));
            }
            trendPoints.getData().clear();
        }

    }

    public void updateWykres() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<Data<Number,Number>> listTemp = FXCollections.observableArrayList();
                ObservableList<Data<Number,Number>> listHum = FXCollections.observableArrayList();
                
                for (int i = 0; i < 30; i++) {
                    
                    listTemp.add(new XYChart.Data(i*2, baza.get(baza.size()-1-i).getTemp()));
                    listHum.add(new XYChart.Data(i*2, baza.get(baza.size()-1-i).getHum()));
                }
                temperatura.getData().clear();
                temperatura.setData(listTemp);
                wilgotnosc.getData().clear();
                wilgotnosc.setData(listHum);
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
