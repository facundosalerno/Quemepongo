package domain;

import clima.AccuWeather;
import clima.TemperaturaAccuWeather;
import domain.atuendo.Atuendo;
import domain.prenda.*;
import exceptions.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestsValidacionAtuendo {

    private Prenda zapatos;
    private Prenda remera;
    private Prenda buso;
    private Prenda campera;
    private Prenda pantalon;
    private Prenda anteojos;

    @Before
    public void init() {
        //Instanciaciones previas a los TEST
        Color rojo = new Color(255, 0, 0);
        Color verde = new Color(0, 255, 0);
        Color azul = new Color(0, 0, 255);

        zapatos = armarUnaPrenda("zapatos",TipoDePrenda.ZAPATO, Material.CUERO, rojo, azul, Trama.GASTADO);
        remera = armarUnaPrenda("remera",TipoDePrenda.REMERA, Material.ALGODON, azul, rojo, Trama.CUADROS);
        pantalon = armarUnaPrenda("pantalon",TipoDePrenda.PANTALON, Material.JEAN, verde, rojo, Trama.RAYADA);
        anteojos = armarUnaPrenda("anteojos",TipoDePrenda.ANTEOJOS, Material.PLASTICO, verde, rojo, Trama.LISA);

        buso = armarUnaPrenda("buso",TipoDePrenda.BUSO, Material.ALGODON, azul, verde, Trama.LISA);
        campera = armarUnaPrenda("campera",TipoDePrenda.CAMPERA, Material.JEAN, verde, null, Trama.GASTADO);


    }

    public Prenda armarUnaPrenda(String nombre,TipoDePrenda tipoDePrenda, Material material, Color colorPrimario, Color colorSecundario, Trama trama) {
        BorradorPrenda borradorPrenda = new BorradorPrenda();
        borradorPrenda.definirNombre(nombre);
        borradorPrenda.definirTipo(tipoDePrenda);
        borradorPrenda.definirMaterial(material);
        borradorPrenda.definirColorPrimario(colorPrimario);
        if(colorSecundario != null) {

            borradorPrenda.definirColorSecundario(colorSecundario);
        }
        borradorPrenda.definirTrama(trama);
        return borradorPrenda.crearPrenda();
    }


    //Test para verificar que podemos crear un atuendo correctamente
    @Test
    public void crearAtuendoValido() {
        try {
            new Atuendo(Arrays.asList(remera), pantalon, zapatos, anteojos);
        } catch (AtuendoInvalidoException exc) {
            Assert.fail();
        }
    }

    //Test para verificar que no deberia crearse un atuendo invalido
    @Test(expected = AtuendoInvalidoException.class)
    public void crearAtuendoInvalido() {
        new Atuendo(Arrays.asList(pantalon), pantalon, zapatos, anteojos);
    }

}
