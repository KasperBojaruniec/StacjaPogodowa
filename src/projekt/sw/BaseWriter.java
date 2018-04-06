/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt.sw;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Mikolaj Musidlowski
 */
public class BaseWriter {
    
    private static final String FILENAME = "C:\\Users\\Mikolaj Musidlowski\\Documents\\NetBeansProjects\\Projekt.sw\\baza.txt";

    public static void zapisz(ArrayList<Pomiar> baza) throws IOException {
        FileWriter fw = new FileWriter(FILENAME);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        for (int i = 0; i < baza.size(); i++) {
            String temp = baza.get(i).getTemp().toString();
            String hum = baza.get(i).getHum().toString();
            String year = baza.get(i).getYear().toString();
            String month = baza.get(i).getMonth().toString();
            String day = baza.get(i).getDay().toString();
            String hour = baza.get(i).getHour().toString();
            String minute = baza.get(i).getMinute().toString();
            String second = baza.get(i).getSecond().toString();
            String line = temp + "," + hum + "," + year + "," + month + "," + day + "," + hour + "," + minute + "," + second;
            bw.write(line);
            bw.newLine();
        }
        bw.close();
    }
}
