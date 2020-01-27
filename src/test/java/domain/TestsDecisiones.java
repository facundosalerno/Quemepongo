package domain;

import clima.OpenWeather;
import clima.TemperaturaOpenWeather;
import domain.atuendo.Atuendo;
import domain.atuendo.Estado;
import domain.guardarropas.Guardarropas;
import domain.guardarropas.GuardarropasPremium;
import domain.prenda.*;
import domain.usuario.TipoDeUsuario;
import domain.usuario.Usuario;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestsDecisiones {
    private Usuario batman;

    private Guardarropas guardarropasBasicoBatman;
    private GuardarropasPremium guardarropasCasual;

    private Atuendo trajeBatman;

    private Prenda botaNegra;
    private Prenda armaduraNegra;
    private Prenda pantalonNegro;
    private Prenda antifazDeMurcielago;
    private Prenda zapatos;
    private Prenda remera;
    private Prenda camisa;
    private Prenda pantalon;
    private Prenda anteojos;
    private Prenda camperaMichelin;
    private Prenda capaNegra;

    TemperaturaOpenWeather temperaturaCiudadGotica;
    OpenWeather meteorologoCiudadGotica;

    @Before
    public void init(){
        Color negro = new Color(0, 0, 0);
        Color rojo = new Color(255, 0, 0);
        Color verde = new Color(0, 255, 0);
        Color azul = new Color(0, 0, 255);
        Color blanco = new Color(255, 255, 255);

        botaNegra = armarUnaPrenda("Bota Negra",TipoDePrenda.ZAPATO, Material.CUERO, negro, Trama.GASTADO);
        armaduraNegra = armarUnaPrenda("Armadura Negra",TipoDePrenda.REMERA, Material.ALGODON, negro, Trama.CUADROS);
        capaNegra = armarUnaPrenda("Capa Negra",TipoDePrenda.CAMPERA, Material.ALGODON, negro, Trama.CUADROS);
        pantalonNegro = armarUnaPrenda("Pantalon Negro",TipoDePrenda.PANTALON, Material.JEAN, negro, Trama.RAYADA);
        antifazDeMurcielago = armarUnaPrenda("Antifaz de murcielago",TipoDePrenda.ANTEOJOS, Material.PLASTICO, negro, Trama.LISA);

        trajeBatman = new Atuendo(Arrays.asList(armaduraNegra), pantalonNegro, botaNegra, antifazDeMurcielago);

        guardarropasBasicoBatman = new GuardarropasPremium("Guardarropas de verano", Arrays.asList(armaduraNegra,capaNegra), Arrays.asList(pantalonNegro), Arrays.asList(botaNegra), Arrays.asList(antifazDeMurcielago));

        zapatos = armarUnaPrenda("zapatos",TipoDePrenda.ZAPATO, Material.CUERO, rojo, Trama.GASTADO);
        remera = armarUnaPrenda("remera",TipoDePrenda.REMERA, Material.ALGODON, azul, Trama.CUADROS);
        pantalon = armarUnaPrenda("pantalon",TipoDePrenda.PANTALON, Material.JEAN, verde, Trama.RAYADA);
        anteojos= armarUnaPrenda("anteojos",TipoDePrenda.ANTEOJOS, Material.PLASTICO, verde, Trama.LISA);
        camisa = armarUnaPrenda("camisa",TipoDePrenda.CAMISA, Material.ALGODON, blanco, Trama.LISA);
        camperaMichelin = armarUnaPrenda("camperaMichelin",TipoDePrenda.CAMPERA, Material.PLUMA, azul, Trama.LISA);

        guardarropasCasual = new GuardarropasPremium("guardarropas casual", new ArrayList<Prenda>(Arrays.asList(camisa, remera,camperaMichelin)), new ArrayList<Prenda>(Arrays.asList(pantalon)), new ArrayList<Prenda>(Arrays.asList(zapatos)), new ArrayList<Prenda>(Arrays.asList(anteojos)));

        batman = new Usuario("Facundo Salerno",Arrays.asList(guardarropasBasicoBatman,guardarropasCasual), TipoDeUsuario.PREMIUM);


        temperaturaCiudadGotica  =	mock(TemperaturaOpenWeather.class);

        meteorologoCiudadGotica = mock (OpenWeather.class);
        when (meteorologoCiudadGotica.obtenerClima()).thenReturn(temperaturaCiudadGotica);
    }

    public Prenda armarUnaPrenda(String nombre, TipoDePrenda tipoDePrenda, Material material, Color colorPrimario, Trama trama){
        BorradorPrenda borradorPrenda = new BorradorPrenda();
        borradorPrenda.definirNombre(nombre);
        borradorPrenda.definirTipo(tipoDePrenda);
        borradorPrenda.definirMaterial(material);
        borradorPrenda.definirColorPrimario(colorPrimario);
        borradorPrenda.definirTrama(trama);
        return borradorPrenda.crearPrenda();
    }

    @Test
    public void verificarQueElAtuendoEsAceptado(){
        batman.aceptarSugerencia(trajeBatman);
        Assert.assertEquals(trajeBatman.getEstado(), Estado.ACEPTADO);
    }

    @Test
    public void verificarQueElAtuendoEsRechazado(){
        batman.rechazarSugerencia(trajeBatman);
        Assert.assertEquals(trajeBatman.getEstado(), Estado.RECHAZADO);
    }

    @Test
    public void verificarQueElAtuendoEsCalificado(){
        batman.aceptarSugerencia(trajeBatman);
        batman.calificarSugerencia(trajeBatman, 5);
        Assert.assertEquals(trajeBatman.getEstado(), Estado.CALIFICADO);
    }

    @Test
    public void verificarQueLaDecisionSePuedeDeshacer(){
        batman.aceptarSugerencia(trajeBatman);
        batman.deshacerUltimaDecision();
        Assert.assertEquals(trajeBatman.getEstado(), Estado.NUEVO);
    }


    @Test
    public void alCalificarTresAtuendosDichaCalificacionAfectaAFuturasDecisiones(){
        when(temperaturaCiudadGotica.getTemperature()).thenReturn(20.0);

        List<Atuendo> sugerenciasParaBatman = batman.obtenerSugerenciasDeTodosSusGuardarropas(meteorologoCiudadGotica);
        batman.aceptarSugerencia(sugerenciasParaBatman.get(0));
        batman.aceptarSugerencia(sugerenciasParaBatman.get(1));
        batman.aceptarSugerencia(sugerenciasParaBatman.get(2));

        batman.calificarSugerencia(sugerenciasParaBatman.get(0), 3 );
        batman.calificarSugerencia(sugerenciasParaBatman.get(1), 3 );
        batman.calificarSugerencia(sugerenciasParaBatman.get(2), 3 );

        //HABRIA QUE PEDIR QUE GENERE SUGERENCIAS DE NUEVO Y QUE ESTA VEZ DE QUE SOLO NECESITA UNA CAPA EN LUGAR DE 2

        when(temperaturaCiudadGotica.getTemperature()).thenReturn(20.0 + batman.getCoeficienteSensacionT());

        Assert.assertEquals(batman.obtenerSugerenciasDeTodosSusGuardarropas(meteorologoCiudadGotica).get(0).getPrendasSuperiores().size(), 1);

    }

}
