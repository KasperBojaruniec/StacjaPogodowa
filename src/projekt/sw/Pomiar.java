package projekt.sw;

import java.util.Calendar;
import java.util.Date;

public class Pomiar {
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;
    private Integer second;
    private Integer temp;
    private Integer hum;

    public Pomiar(Integer temp, Integer hum){
        this.temp = temp;
        this.hum = hum;
    }

    public void setTime(){
        Date date = new Date();
        this.year = date.getYear() + 1900;
        this.month = date.getMonth() + 1;
        this.day = date.getDate();
        this.hour = date.getHours();
        this.minute = date.getMinutes();
        this.second = date.getSeconds();

    }

    public void wypisz(){
        System.out.println("Rok: " +this.year);
        System.out.println("Miesiąc: " +this.month);
        System.out.println("Dzień: "+this.day);
        System.out.println("Godzina: "+this.hour);
        System.out.println("Minuta: "+this.minute);
        System.out.println("Sekunda: "+this.second);
        System.out.println("Temperatura: "+this.temp);
        System.out.println("Wilgotnosc: "+this.hum);
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getHum() {
        return hum;
    }

    public void setHum(Integer hum) {
        this.hum = hum;
    }
}

