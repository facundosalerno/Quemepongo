package Persistencia;

import clima.OpenWeather;
import clima.TemperaturaOpenWeather;
import domain.atuendo.Atuendo;

import domain.atuendo.Estado;
import domain.guardarropas.Guardarropas;
import domain.guardarropas.GuardarropasPremium;
import domain.prenda.*;
import domain.usuario.TipoDeUsuario;
import domain.usuario.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAtuendos extends AbstractPersistenceTest implements WithGlobalEntityManager{
    private Usuario facundo;
    private Guardarropas guardarropasInvierno;

    private Prenda anteojos;
    private Prenda zapatosFormales;
    private Prenda pantalonParaSalida;
    private Prenda pantalonFormal;
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

    TemperaturaOpenWeather nuevoClima;
    OpenWeather nuevoMeteorologo;

    @Before
    public void init(){
        Color rojo = new Color(255, 0, 0);
        Color verde = new Color(0, 255, 0);
        Color azul = new Color(0, 0, 255);
        Color blanco = new Color(255, 255, 255);
        Color negro = new Color(255,255,255);

        zapatosFormales = armarUnaPrenda("zapatosFormales",TipoDePrenda.ZAPATO, Material.CUERO, negro, null, Trama.LISA);

        pantalonParaSalida = armarUnaPrenda("pantalonParaSalida",TipoDePrenda.PANTALON, Material.JEAN, blanco, null, Trama.GASTADO);
        pantalonFormal = armarUnaPrenda("Pantalon Formal",TipoDePrenda.PANTALON, Material.POLIESTER, negro, null, Trama.LISA);  //TODO: Para tests guardarropas

        anteojos = armarUnaPrenda("anteojos",TipoDePrenda.ANTEOJOS, Material.PLASTICO, verde, rojo, Trama.LISA);

        busoFormal = armarUnaPrenda("busoFormal",TipoDePrenda.BUSO, Material.ALGODON, azul, null, Trama.LISA);   //TODO: Para tests guardarropas
        busoInformal = armarUnaPrenda("busoInformal",TipoDePrenda.BUSO, Material.ALGODON, azul, verde, Trama.RAYADA);
        sweaterFormal = armarUnaPrenda("sweaterFormal",TipoDePrenda.SWEATER,Material.LINO, azul, null, Trama.LISA);

        camperaParaSalida = armarUnaPrenda("camperaParaSalida",TipoDePrenda.CAMPERA, Material.JEAN, verde, null, Trama.GASTADO);
        camperaMichelin = armarUnaPrenda("camperaMichelin",TipoDePrenda.CAMPERA, Material.PLUMA, azul, null, Trama.LISA);

        camisaSalida = armarUnaPrenda("camisaSalida",TipoDePrenda.CAMISA, Material.JEAN, azul, blanco, Trama.ESCOCESA);
        camisaFormalAzul = armarUnaPrenda("Camisa Formal Azul",TipoDePrenda.CAMISA, Material.SEDA, azul, null, Trama.LISA);  //TODO: Para tests guardarropas
        camisaFormalBlanca = armarUnaPrenda("camisaFormalBlanca",TipoDePrenda.CAMISA, Material.LINO, blanco, null, Trama.LISA);

        remeraCanchera = armarUnaPrenda("Remera canchera",TipoDePrenda.REMERA, Material.ALGODON, azul, rojo, Trama.RAYADA);  //TODO: Para tests guardarropas
        remeraDeDia = armarUnaPrenda("remeraDeDia",TipoDePrenda.REMERA, Material.ALGODON, azul, null, Trama.LISA);

        nuevoClima =	mock(TemperaturaOpenWeather.class);


        nuevoMeteorologo = mock (OpenWeather.class);
        when (nuevoMeteorologo.obtenerClima()).thenReturn(nuevoClima);

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
    public void sePersisteAtuendoAceptadoPorUsuario(){
        when(nuevoClima.getTemperature()).thenReturn(10.0);

        facundo = new Usuario("Facundo Salerno",null, TipoDeUsuario.PREMIUM);

        guardarropasInvierno = new GuardarropasPremium("guardarropas de invierno", new ArrayList<Prenda>(Arrays.asList( camisaFormalBlanca,remeraDeDia, camisaSalida, sweaterFormal, camperaMichelin)), new ArrayList<Prenda>(Arrays.asList(pantalonParaSalida)), new ArrayList<Prenda>(Arrays.asList(zapatosFormales)), new ArrayList<Prenda>(Arrays.asList(anteojos)));

        facundo.agregarGuardarropas(guardarropasInvierno);

        entityManager().persist(facundo);

        Atuendo outfitRandomDeLunes= facundo.obtenerSugerenciasDeTodosSusGuardarropas(nuevoMeteorologo).get(0);


        facundo.aceptarSugerencia(outfitRandomDeLunes);

        Atuendo outfitRecuperado = entityManager()
                .createQuery("from Usuario where id LIKE :IdUsuario", Usuario.class)
                .setParameter("IdUsuario", facundo.getId())
                .getResultList()
                .get(0).getAtuendosAceptados().get(0);

        assertEquals(outfitRecuperado,
                outfitRandomDeLunes);

        assertEquals(outfitRecuperado.getEstado(), Estado.ACEPTADO);
    }

    @Test
    public void sePersisteAtuendoRechazadoPorUsuario(){
        when(nuevoClima.getTemperature()).thenReturn(10.0);

        facundo = new Usuario("Facundo Salerno",null, TipoDeUsuario.PREMIUM);

        guardarropasInvierno = new GuardarropasPremium("guardarropas de invierno", new ArrayList<Prenda>(Arrays.asList( camisaFormalBlanca,remeraDeDia, camisaSalida, sweaterFormal, camperaMichelin)), new ArrayList<Prenda>(Arrays.asList(pantalonParaSalida)), new ArrayList<Prenda>(Arrays.asList(zapatosFormales)), new ArrayList<Prenda>(Arrays.asList(anteojos)));

        facundo.agregarGuardarropas(guardarropasInvierno);

        entityManager().persist(facundo);

        Atuendo outfitRandomDeLunes= facundo.obtenerSugerenciasDeTodosSusGuardarropas(nuevoMeteorologo).get(0);


        facundo.rechazarSugerencia(outfitRandomDeLunes);

        Atuendo outfitRecuperado = entityManager()
                .createQuery("from Usuario where id LIKE :IdUsuario", Usuario.class)
                .setParameter("IdUsuario", facundo.getId())
                .getResultList()
                .get(0).getAtuendosRechazados().get(0);

        assertEquals(outfitRecuperado,
                outfitRandomDeLunes);

        assertEquals(outfitRecuperado.getEstado(), Estado.RECHAZADO);
    }

    @Test
    public void sePersisteAtuendoCalificadoPorUsuario(){
        when(nuevoClima.getTemperature()).thenReturn(10.0);

        facundo = new Usuario("Facundo Salerno",null, TipoDeUsuario.PREMIUM);

        guardarropasInvierno = new GuardarropasPremium("guardarropas de invierno", new ArrayList<Prenda>(Arrays.asList( camisaFormalBlanca,remeraDeDia, camisaSalida, sweaterFormal, camperaMichelin)), new ArrayList<Prenda>(Arrays.asList(pantalonParaSalida)), new ArrayList<Prenda>(Arrays.asList(zapatosFormales)), new ArrayList<Prenda>(Arrays.asList(anteojos)));

        facundo.agregarGuardarropas(guardarropasInvierno);

        entityManager().persist(facundo);

        Atuendo outfitRandomDeLunes= facundo.obtenerSugerenciasDeTodosSusGuardarropas(nuevoMeteorologo).get(0);

        facundo.aceptarSugerencia(outfitRandomDeLunes);

        facundo.calificarSugerencia(outfitRandomDeLunes,5);

        Atuendo outfitRecuperado = entityManager()
                .createQuery("from Usuario where id LIKE :IdUsuario", Usuario.class)
                .setParameter("IdUsuario", facundo.getId())
                .getResultList()
                .get(0).getAtuendosCalificados().get(0);

        assertEquals(outfitRecuperado,
                outfitRandomDeLunes);

        assertEquals(outfitRecuperado.getEstado(), Estado.CALIFICADO);
        assertEquals(outfitRecuperado.getCalificacion(), 5);
    }

}
