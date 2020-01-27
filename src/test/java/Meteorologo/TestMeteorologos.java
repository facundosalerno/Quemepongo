package Meteorologo;

import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Test;

import clima.AccuWeather;
import clima.OpenWeather;
import clima.TemperaturaAccuWeather;
import clima.TemperaturaOpenWeather;



public class TestMeteorologos {

	/**verificar con postman las temperaturas antes del hacer el test*/
	@Test
	public void debeDevolverJSONEnUnObjetoAccuWeather() {
		
		AccuWeather nuevaConexion= new AccuWeather();
		TemperaturaAccuWeather nuevoClima= nuevaConexion.obtenerClima();

		Assert.assertEquals(24, nuevoClima.getTemperature(), 0.5);
	}
	
	@Test
	public void debeDevolverJSONEnUnObjetoOpenWeather() {
		
		OpenWeather nuevaConexion= new OpenWeather();
		TemperaturaOpenWeather nuevoClima= nuevaConexion.obtenerClima();

		Assert.assertEquals(26, nuevoClima.getTemperature(), 0.5);
		
	}
	
	@Test
	public  void mokitoJsonAccuWweather(){
		TemperaturaAccuWeather nuevoClima =	mock(TemperaturaAccuWeather.class);
		when(nuevoClima.getTemperature()).thenReturn(10.2);
		Assert.assertEquals(nuevoClima.getTemperature(), 10.2, 0.5);

	}

	@Test
	public  void mokitoJsonOpenWweather(){
		TemperaturaOpenWeather nuevoClima =	mock(TemperaturaOpenWeather.class);
		when(nuevoClima.getTemperature()).thenReturn(11.2);
		Assert.assertEquals(nuevoClima.getTemperature(), 11.2, 0.5);

	}
}
