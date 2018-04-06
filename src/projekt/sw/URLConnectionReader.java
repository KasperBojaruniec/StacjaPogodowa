package projekt.sw;

import java.net.*;
import java.io.*;

public class URLConnectionReader {

    public static int temp;
    public static int hum;

    public static Pomiar getPomiar() throws Exception {
        URL arduino = new URL("http://192.168.43.209/dht11");
        URLConnection yc = arduino.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine = in.readLine();
        in.close();
        String[] parts = inputLine.split(",");
        temp = Integer.parseInt(parts[0]);
        hum = Integer.parseInt(parts[1]);
        Pomiar pomiar = new Pomiar(temp, hum);
        pomiar.setTime();
        return pomiar;
    }
}
