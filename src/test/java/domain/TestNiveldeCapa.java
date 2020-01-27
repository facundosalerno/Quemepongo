package domain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import clima.AccuWeather;
import clima.TemperaturaAccuWeather;
import domain.guardarropas.Guardarropas;
import domain.prenda.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import domain.capaPrenda.NivelDeCapa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestNiveldeCapa {

	private Prenda prendaZapatos;
	private Prenda prendaRemera;
	private Prenda prendaBuso;
	private Prenda prendaCampera;
	private Prenda prendaPantalon;
	private Prenda prendaAnteojos;

	private List<Prenda> parteSuperiorInvierno= new ArrayList<>();

	@Before
	public void init() {
		//Instanciaciones previas a los TEST
		Color rojo = new Color(255, 0, 0);
		Color verde = new Color(0, 255, 0);
		Color azul = new Color(0, 0, 255);

		prendaZapatos = armarUnaPrenda("zapatos",TipoDePrenda.ZAPATO, Material.CUERO, rojo, azul, Trama.GASTADO);
		prendaRemera = armarUnaPrenda("remera",TipoDePrenda.REMERA, Material.ALGODON, azul, rojo, Trama.CUADROS);
		prendaPantalon = armarUnaPrenda("pantalon",TipoDePrenda.PANTALON, Material.JEAN, verde, rojo, Trama.RAYADA);
		prendaAnteojos = armarUnaPrenda("anteojos",TipoDePrenda.ANTEOJOS, Material.PLASTICO, verde, rojo, Trama.LISA);
		prendaBuso = armarUnaPrenda("buso",TipoDePrenda.BUSO, Material.ALGODON, azul, verde, Trama.LISA);
		prendaCampera = armarUnaPrenda("campera",TipoDePrenda.CAMPERA, Material.JEAN, verde, azul, Trama.GASTADO);

	}

	public Prenda armarUnaPrenda(String nombre, TipoDePrenda tipoDePrenda, Material material, Color colorPrimario, Color colorSecundario, Trama trama) {
		BorradorPrenda borradorPrenda = new BorradorPrenda();
		borradorPrenda.definirNombre(nombre);
		borradorPrenda.definirTipo(tipoDePrenda);
		borradorPrenda.definirMaterial(material);
		borradorPrenda.definirColorPrimario(colorPrimario);
		if (colorSecundario != null) {

			borradorPrenda.definirColorSecundario(colorSecundario);
		}
		borradorPrenda.definirTrama(trama);
		return borradorPrenda.crearPrenda();
	}

	@Test
	public  void capasMalOrdenadasSeFiltran(){
		parteSuperiorInvierno = Arrays.asList(prendaBuso, prendaRemera, prendaCampera);
		Assert.assertTrue(!Guardarropas.estaOrdenada(parteSuperiorInvierno));
	}

	@Test
	public void capasEstanOrdenadas(){
		parteSuperiorInvierno = Arrays.asList(prendaRemera, prendaBuso, prendaCampera);
		Assert.assertTrue(Guardarropas.estaOrdenada(parteSuperiorInvierno));

	}


	@Test
	public void testCapaAbrigaBien(){
		TemperaturaAccuWeather temperaturaImpostora = mock(TemperaturaAccuWeather.class);
		when(temperaturaImpostora.getTemperature()).thenReturn(21.0);

		parteSuperiorInvierno = Arrays.asList(prendaRemera, prendaBuso, prendaCampera);
		Assert.assertTrue(Guardarropas.abrigaBien(parteSuperiorInvierno,temperaturaImpostora));
	}
}
