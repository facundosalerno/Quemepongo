package conexionesUrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetClientGet {

    // http://dataservice.accuweather.com/currentconditions/v1/7894?apikey=lC8MHsdofqPxsn14yuX4GfNJYZ990AMr&language=es-es&details=false
    public static String main(String uri) {

        try {

            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            String salida = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                salida += output;
            }

            conn.disconnect();

            return  salida;

        } catch (MalformedURLException e) {

            e.printStackTrace();
            return  "No se pudo obtener la informacion";
        } catch (IOException e) {

            e.printStackTrace();
            return  "No se pudo obtener la informacion";
        }

    }
}
