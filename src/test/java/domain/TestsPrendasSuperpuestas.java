package domain;

import clima.OpenWeather;
import clima.TemperaturaOpenWeather;
import com.google.common.collect.*;
import domain.atuendo.Atuendo;
import domain.guardarropas.Guardarropas;
import domain.guardarropas.GuardarropasPremium;
import domain.prenda.*;
import org.eclipse.swt.internal.C;
import org.junit.*;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TestsPrendasSuperpuestas {
    private Atuendo atuendo;

    private Prenda zapatosFormales;   //TODO: Para tests guardarropas
    private Prenda zapatosSalida;

    private Prenda pantalonParaSalida;
    private Prenda pantalonFormal;   //TODO: Para tests guardarropas

    private Prenda anteojos;

    private Prenda camperaParaSalida;
    private Prenda camperaMichelin;
    private Prenda busoInformal;
    private Prenda busoFormal;
    private Prenda sweaterFormal;
    private Prenda remeraCanchera;
    private Prenda remeraDeDia;
    private Prenda camisaFormalBlanca;
    private Prenda camisaFormalAzul;
    private Prenda camisaSalida;

    private Guardarropas guardarropasInvierno;

    TemperaturaOpenWeather nuevoClima;
    OpenWeather nuevoMeteorologo;

    @Before
    public void init() {
        //Instanciaciones previas a los TEST
        Color rojo = new Color(255, 0, 0);
        Color verde = new Color(0, 255, 0);
        Color azul = new Color(0, 0, 255);
        Color blanco = new Color(255, 255, 255);
        Color negro = new Color(0, 0, 0);

        //zapatosSalida = armarUnaPrenda(TipoDePrenda.ZAPATO, Material.GAMUZA, azul, null, Trama.GASTADO);  //TODO: Para tests guardarropas
        zapatosFormales = armarUnaPrenda("zapatosFormales",TipoDePrenda.ZAPATO, Material.CUERO, negro, null, Trama.LISA);

        pantalonParaSalida = armarUnaPrenda("pantalonParaSalida",TipoDePrenda.PANTALON, Material.JEAN, blanco, null, Trama.GASTADO);
        //pantalonFormal = armarUnaPrenda(TipoDePrenda.PANTALON, Material.POLIESTER, negro, null, Trama.LISA);  //TODO: Para tests guardarropas

        anteojos = armarUnaPrenda("anteojos",TipoDePrenda.ANTEOJOS, Material.PLASTICO, verde, rojo, Trama.LISA);


        busoFormal = armarUnaPrenda("busoFormal",TipoDePrenda.BUSO, Material.ALGODON, azul, null, Trama.LISA);   //TODO: Para tests guardarropas
        busoInformal = armarUnaPrenda("busoInformal",TipoDePrenda.BUSO, Material.ALGODON, azul, verde, Trama.RAYADA);
        sweaterFormal = armarUnaPrenda("sweaterFormal",TipoDePrenda.SWEATER,Material.LINO, azul, null, Trama.LISA);

        camperaParaSalida = armarUnaPrenda("camperaParaSalida",TipoDePrenda.CAMPERA, Material.JEAN, verde, null, Trama.GASTADO);
        camperaMichelin = armarUnaPrenda("camperaMichelin",TipoDePrenda.CAMPERA, Material.PLUMA, azul, null, Trama.LISA);

        camisaSalida = armarUnaPrenda("camisaSalida",TipoDePrenda.CAMISA, Material.JEAN, azul, blanco, Trama.ESCOCESA);
        //camisaFormalAzul = armarUnaPrenda(TipoDePrenda.CAMISA, Material.SEDA, azul, null, Trama.LISA);  //TODO: Para tests guardarropas
        camisaFormalBlanca = armarUnaPrenda("camisaFormalBlanca",TipoDePrenda.CAMISA, Material.LINO, blanco, null, Trama.LISA);

        //remeraCanchera = armarUnaPrenda(TipoDePrenda.REMERA, Material.ALGODON, azul, rojo, Trama.RAYADA);  //TODO: Para tests guardarropas
        remeraDeDia = armarUnaPrenda("remeraDeDia",TipoDePrenda.REMERA, Material.ALGODON, azul, null, Trama.LISA);

        guardarropasInvierno = new GuardarropasPremium("guardarropas de invierno", new ArrayList<Prenda>(Arrays.asList( camisaFormalBlanca,remeraDeDia, camisaSalida, sweaterFormal, busoInformal, camperaParaSalida, camperaMichelin)), new ArrayList<Prenda>(Arrays.asList(pantalonParaSalida)), new ArrayList<Prenda>(Arrays.asList(zapatosFormales)), new ArrayList<Prenda>(Arrays.asList(anteojos)));

        nuevoClima =	mock(TemperaturaOpenWeather.class);


        nuevoMeteorologo = mock (OpenWeather.class);
        when (nuevoMeteorologo.obtenerClima()).thenReturn(nuevoClima);

        // Assert.assertEquals(guardarropasInvierno.sugerirAtuendo(temperaturaCiudadGotica), Arrays.asList());

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

    /*@Test(expected = AtuendoInvalidoException.class)
    public void verificarQueNoSeCreaAtuendoConPrendasSuperpuestasDeDistintaCategoria(){
        new Atuendo(Arrays.asList(camisa, pantalon), pantalon, zapatos, anteojos);
    }*/

    //TODO: Test para verificar que siempre se crean capas con prendas de igual categoria

    //TODO:Test para verificar que se dispara una excepcion si no hay capas creadas debido a que no se satisface la temperatura

    @Test
    public void generarAtuendoTiraExcepcionSiNohayCapasQueSatisfaganLaTemperatura() {

    }

    //TODO: Test para verificar que las capas generadas esten ordenadas

    //TODO: Test para verificar que todas las capas son de distinto nivel

    @Test
    public void seGeneranAtuendosEsperadosConUnaTemperaturaDe10Grados(){
        when(nuevoClima.getTemperature()).thenReturn(10.0);

        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).size() == 12);

        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(remeraDeDia,busoInformal,camperaParaSalida)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(remeraDeDia,sweaterFormal,camperaParaSalida)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(remeraDeDia,busoInformal,camperaMichelin)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(remeraDeDia,sweaterFormal,camperaMichelin)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaSalida,busoInformal,camperaParaSalida)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaSalida,sweaterFormal,camperaParaSalida)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaFormalBlanca,sweaterFormal,camperaParaSalida)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaFormalBlanca,busoInformal,camperaMichelin)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaFormalBlanca,sweaterFormal,camperaMichelin)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaFormalBlanca,busoInformal,camperaParaSalida)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaSalida,sweaterFormal,camperaMichelin)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaSalida,busoInformal,camperaMichelin)),pantalonParaSalida,zapatosFormales,anteojos))));
    }

    @Test
    public void seGeneranAtuendosEsperadosConUnaTemperaturaDe20Grados(){
        when(nuevoClima.getTemperature()).thenReturn(20.0);

        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).size() == 12);
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(remeraDeDia,camperaParaSalida)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(remeraDeDia,camperaMichelin)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(remeraDeDia,busoInformal)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(remeraDeDia,sweaterFormal)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaSalida,camperaParaSalida)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaSalida,sweaterFormal)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaFormalBlanca,camperaParaSalida)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaFormalBlanca,camperaMichelin)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaFormalBlanca,sweaterFormal)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaFormalBlanca,busoInformal)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaSalida,camperaMichelin)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaSalida,busoInformal)),pantalonParaSalida,zapatosFormales,anteojos))));
    }

    @Test
    public void seGeneranAtuendosEsperadosConUnaTemperaturaDe25Grados(){    //TODO: revisar lo que pasa con la temperatura de 30 grados
        when(nuevoClima.getTemperature()).thenReturn(25.0);

        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).size() == 3);

        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(remeraDeDia)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaFormalBlanca)),pantalonParaSalida,zapatosFormales,anteojos))));
        Assert.assertTrue(guardarropasInvierno.sugerirAtuendo(nuevoMeteorologo).stream().anyMatch(sugerencia->sugerencia.equals(new Atuendo((Arrays.asList(camisaSalida)),pantalonParaSalida,zapatosFormales,anteojos))));
    }
}
