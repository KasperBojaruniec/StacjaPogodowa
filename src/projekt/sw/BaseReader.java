/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt.sw;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Mikolaj Musidlowski
 */
public class BaseReader {
    
    private static final String FILENAME = "C:\\Users\\Mikolaj Musidlowski\\Documents\\NetBeansProjects\\Projekt.sw\\baza.txt";
    
     public static void wczytaj(ArrayList<Pomiar> baza) throws FileNotFoundException {
        Scanner sc=new Scanner(new FileReader(FILENAME));
        String line="";
        while (sc.hasNextLine()){
            line = sc.nextLine();
            String[] split=line.split(",");
            Pomiar temp = new Pomiar(Integer.parseInt(split[0]),Integer.parseInt(split[1]));
            temp.setYear(Integer.parseInt(split[2]));
            temp.setMonth(Integer.parseInt(split[3]));
            temp.setDay(Integer.parseInt(split[4]));
            temp.setHour(Integer.parseInt(split[5]));
            temp.setMinute(Integer.parseInt(split[6]));
            temp.setSecond(Integer.parseInt(split[7]));
            baza.add(temp);
        }
    }
    
}
