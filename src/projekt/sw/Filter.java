/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt.sw;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Mikolaj Musidlowski
 */
public class Filter extends Thread {

    private ProjektSw gui;
    private ArrayList<Pomiar> pomiary = new ArrayList<>();
    private Pomiar srednia;

    public Filter(ProjektSw gui) {
        this.gui = gui;
    }

    public void usunMin() {
        int index = 0;
        Pomiar min = pomiary.get(0);
        for (int i = 0; i < pomiary.size(); i++) {
            if (min.getTemp() > pomiary.get(i).getTemp()) {
                min = pomiary.get(i);
                index = i;
            }
        }
        pomiary.remove(index);
    }

    public void usunMax() {
        int index = 0;
        Pomiar max = pomiary.get(0);
        for (int i = 0; i < pomiary.size(); i++) {
            if (max.getTemp() < pomiary.get(i).getTemp()) {
                max = pomiary.get(i);
                index = i;
            }
        }
        pomiary.remove(index);
    }

    public Pomiar usrednij() {
        int sredniaHum = 0;
        int sredniaTemp = 0;
        for (int i = 0; i < pomiary.size(); i++) {
            sredniaHum += pomiary.get(i).getHum();
            sredniaTemp += pomiary.get(i).getTemp();
        }
        sredniaHum = sredniaHum / pomiary.size();
        sredniaTemp = sredniaTemp / pomiary.size();
        return new Pomiar(sredniaTemp, sredniaHum);
    }

    @Override
    public void run() {
        while (Boolean.TRUE) {
            for (int i = 0; i < 18; i++) {
                try {
                    pomiary.add(URLConnectionReader.getPomiar());
                } catch (Exception ex) {
                    Logger.getLogger(Filter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            usunMax();
            usunMin();
            srednia = usrednij();
            srednia.setTime();
            gui.baza.add(srednia);
            gui.updateWykres();

            try {
                sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Filter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
