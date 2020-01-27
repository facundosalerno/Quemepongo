package domain;

import clima.AccuWeather;
import clima.TemperaturaAccuWeather;
import domain.atuendo.Atuendo;
import domain.guardarropas.Guardarropas;
import domain.guardarropas.GuardarropasPremium;
import domain.prenda.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestsSugerencias {
    private Guardarropas guardarropasDeWilly;

    private Prenda musculosa;
    private Prenda remeraMangaLarga;
    private Prenda pantalon;
    private Prenda trajeDeBaño;
    private Prenda zapatos;
    private Prenda ojotas;
    private Prenda anteojos;

    @Before
    public void init(){
        Color rojo = new Color(255, 0, 0);
        Color verde = new Color(0, 255, 0);
        Color azul = new Color(0, 0, 255);

        musculosa = armarUnaPrenda("Musculosa",TipoDePrenda.REMERA, Material.ALGODON, rojo, azul, Trama.GASTADO);
        remeraMangaLarga = armarUnaPrenda("Remera manga larga",TipoDePrenda.REMERA, Material.ALGODON, azul, rojo, Trama.CUADROS);
        pantalon = armarUnaPrenda("Pantalon",TipoDePrenda.PANTALON, Material.JEAN, verde, rojo, Trama.RAYADA);
        trajeDeBaño = armarUnaPrenda("Traje de banio",TipoDePrenda.SHORT, Material.ALGODON, verde, rojo, Trama.LISA);
        zapatos = armarUnaPrenda("Zapatos",TipoDePrenda.ZAPATO, Material.CUERO, rojo, azul, Trama.GASTADO);
        ojotas = armarUnaPrenda("Ojotas",TipoDePrenda.OJOTAS, Material.GOMA, azul, rojo, Trama.CUADROS);
        anteojos= armarUnaPrenda("Anteojos",TipoDePrenda.ANTEOJOS, Material.PLASTICO, verde, rojo, Trama.LISA);

        guardarropasDeWilly = new GuardarropasPremium("guardarropas casual", Arrays.asList(musculosa), Arrays.asList(trajeDeBaño), Arrays.asList(ojotas), Arrays.asList(anteojos));

    }

    public Prenda armarUnaPrenda(String nombre, TipoDePrenda tipoDePrenda, Material material, Color colorPrimario, Color colorSecundario, Trama trama){
        BorradorPrenda borradorPrenda = new BorradorPrenda();
        borradorPrenda.definirTipo(tipoDePrenda);
        borradorPrenda.definirNombre(nombre);
        borradorPrenda.definirMaterial(material);
        borradorPrenda.definirColorPrimario(colorPrimario);
        borradorPrenda.definirColorSecundario(colorSecundario);
        borradorPrenda.definirTrama(trama);
        return borradorPrenda.crearPrenda();
    }

    @Test
    public void verificarSugerenciasAcordeAlVerano(){
        TemperaturaAccuWeather temperatura = mock(TemperaturaAccuWeather.class);
        when(temperatura.getTemperature()).thenReturn(31.0);
        AccuWeather meteorologo = mock(AccuWeather.class);
        when(meteorologo.obtenerClima()).thenReturn(temperatura);

        List<Atuendo> sugerencias = guardarropasDeWilly.sugerirAtuendo(meteorologo);
        Atuendo posibleAtuendoSugerido = new Atuendo(Arrays.asList(musculosa), trajeDeBaño, ojotas, anteojos);
        Assert.assertTrue("el atuendo " + posibleAtuendoSugerido + "  debe estar en " + sugerencias, sugerencias.stream().anyMatch(atuendo -> atuendo.equals(posibleAtuendoSugerido)));
    }

    @Test
    public void verificarSugerenciasAcordeAlInvierno(){
        TemperaturaAccuWeather temperatura = mock(TemperaturaAccuWeather.class);
        when(temperatura.getTemperature()).thenReturn(25.0);
        AccuWeather meteorologo = mock(AccuWeather.class);
        when(meteorologo.obtenerClima()).thenReturn(temperatura);

        guardarropasDeWilly.sugerirAtuendo(meteorologo);
    }

    @Test
    public void testRevalidacionAtuendo(){
        TemperaturaAccuWeather temperatura = mock(TemperaturaAccuWeather.class);
        when(temperatura.getTemperature()).thenReturn(25.0);
        AccuWeather meteorologo = mock(AccuWeather.class);
        when(meteorologo.obtenerClima()).thenReturn(temperatura);


    }
}
