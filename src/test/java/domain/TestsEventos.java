package domain;

import clima.AccuWeather;
import clima.Meteorologo;
import clima.TemperaturaAccuWeather;
import domain.atuendo.Atuendo;
import domain.evento.Evento;
import domain.evento.FrecuenciaEvento;
import domain.guardarropas.GuardarropasPremium;
import domain.prenda.*;
import domain.usuario.TipoDeUsuario;
import domain.usuario.Usuario;
import exceptions.TodaviaNoEstaCercaElEvento;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestsEventos {
    private Usuario panchoPepeGil;
    private Prenda zapatos;
    private Prenda remera;
    private Prenda pantalon;
    private Prenda anteojos;
    private Prenda buso;
    private Prenda campera;

    @Before
    public void init(){
        Color rojo = new Color(255, 0, 0);
        Color verde = new Color(0, 255, 0);
        Color azul = new Color(0, 0, 255);

        zapatos = armarUnaPrenda("Zapatos",TipoDePrenda.ZAPATO, Material.CUERO, rojo, azul, Trama.GASTADO);
        remera = armarUnaPrenda("Remera",TipoDePrenda.REMERA, Material.ALGODON, azul, rojo, Trama.CUADROS);
        pantalon = armarUnaPrenda("Pantalon",TipoDePrenda.PANTALON, Material.JEAN, verde, rojo, Trama.RAYADA);
        anteojos= armarUnaPrenda("Anteojos",TipoDePrenda.ANTEOJOS, Material.PLASTICO, verde, rojo, Trama.LISA);
        buso = armarUnaPrenda("Buso",TipoDePrenda.BUSO, Material.ALGODON, azul, verde, Trama.LISA);
        campera = armarUnaPrenda("Campera",TipoDePrenda.CAMPERA, Material.JEAN, verde, azul, Trama.GASTADO);

        panchoPepeGil = new Usuario("Facundo Salerno",Arrays.asList(new GuardarropasPremium("guardarropas casual", Arrays.asList(remera, buso, campera), Arrays.asList(pantalon), Arrays.asList(zapatos), Arrays.asList(anteojos))), TipoDeUsuario.PREMIUM);

    }

    public Prenda armarUnaPrenda(String nombre, TipoDePrenda tipoDePrenda, Material material, Color colorPrimario,Color colorSecundario, Trama trama){
        BorradorPrenda borradorPrenda = new BorradorPrenda();
        borradorPrenda.definirNombre(nombre);
        borradorPrenda.definirTipo(tipoDePrenda);
        borradorPrenda.definirMaterial(material);
        borradorPrenda.definirColorPrimario(colorPrimario);
        borradorPrenda.definirColorSecundario(colorSecundario);
        borradorPrenda.definirTrama(trama);
        return borradorPrenda.crearPrenda();
    }

    /* Testea que las sugerencias solo se realizan cuando el evento esta proximo */
    @Test(expected = TodaviaNoEstaCercaElEvento.class)
    public void verificarQueSoloSeHacenSugerenciasAlEstarProximoElEvento(){
        LocalDateTime fechaCumpleWilly = LocalDateTime.of(2021,06,20,20,30);
        panchoPepeGil.crearEvento("Cumpleaños de juan", fechaCumpleWilly, FrecuenciaEvento.ANUAL,"Casa de Juan");

        TemperaturaAccuWeather temperatura = mock(TemperaturaAccuWeather.class);
        when(temperatura.getTemperature()).thenReturn(20.0);

        AccuWeather meteorologo = mock(AccuWeather.class);
        when(meteorologo.obtenerClima()).thenReturn(temperatura);
        panchoPepeGil.getEventos().get(0).generarSugerencias(meteorologo);
    }

    /* Testea que si el evento esta proximo, las sugerencia se realizan */
    @Test
    public void verificarQueSeHacenSugerencias(){
        LocalDateTime fechaCumpleWilly = LocalDateTime.now();
        panchoPepeGil.crearEvento("Cumpleaños de juan", fechaCumpleWilly, FrecuenciaEvento.ANUAL,"Casa de Juan");

        TemperaturaAccuWeather temperatura = mock(TemperaturaAccuWeather.class);
        when(temperatura.getTemperature()).thenReturn(25.0);

        AccuWeather meteorologo = mock(AccuWeather.class);
        when(meteorologo.obtenerClima()).thenReturn(temperatura);

        panchoPepeGil.getEventos().get(0).generarSugerencias(meteorologo);
        Assert.assertTrue(panchoPepeGil.getEventos().get(0).existenSugerencias());
    }


}
