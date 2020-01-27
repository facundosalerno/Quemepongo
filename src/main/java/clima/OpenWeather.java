package clima;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import conexionesUrl.NetClientGet;

public class OpenWeather implements Meteorologo {
    private final String URI = "https://api.openweathermap.org/data/2.5/weather?q=Buenos%20Aires,%20AR&appid=a4eb7e978f43ec94e5cf3885b5dc3b2c";
    private String jsonData;

    public TemperaturaOpenWeather obtenerClima() {
        jsonData = NetClientGet.main(URI);    
      
        final Gson gson = new Gson();
		
		JsonParser parser = new JsonParser();
		JsonObject objetoParseado = (JsonObject)parser.parse(jsonData.toString());

		JsonElement Main = objetoParseado.get("main");

	    return  gson.fromJson(Main, TemperaturaOpenWeather.class);
    }



}
