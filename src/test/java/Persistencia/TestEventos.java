package Persistencia;
import clima.OpenWeather;
import clima.TemperaturaOpenWeather;
import domain.evento.Evento;
import domain.evento.FrecuenciaEvento;
import domain.guardarropas.Guardarropas;
import domain.guardarropas.GuardarropasLimitado;
import domain.guardarropas.GuardarropasPremium;
import domain.prenda.*;
import domain.usuario.TipoDeUsuario;
import domain.usuario.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestEventos extends AbstractPersistenceTest implements WithGlobalEntityManager {
    private Usuario facundo;

    private GuardarropasPremium guardarropasCasual;
    private GuardarropasLimitado guardarropasLimitado;
    private GuardarropasPremium guardarropasCopado;
    private Prenda zapatos;
    private Prenda remera;
    private Prenda camisa;
    private Prenda pantalon;
    private Prenda anteojos;

    LocalDateTime fechaCumpleWilly;
    LocalDateTime fechaCumpleSuegra;
    LocalDateTime fechaUltimaEntrega;

    TemperaturaOpenWeather nuevoClima;
    OpenWeather nuevoMeteorologo;

    @Before
    public void init(){
        Color rojo = new Color(255, 0, 0);
        Color verde = new Color(0, 255, 0);
        Color azul = new Color(0, 0, 255);
        Color blanco = new Color(255, 255, 255);

        zapatos = armarUnaPrenda("zapatos",TipoDePrenda.ZAPATO, Material.CUERO, rojo, azul, Trama.GASTADO);
        remera = armarUnaPrenda("remera",TipoDePrenda.REMERA, Material.ALGODON, azul, rojo, Trama.CUADROS);
        pantalon = armarUnaPrenda("pantalon",TipoDePrenda.PANTALON, Material.JEAN, verde, rojo, Trama.RAYADA);
        anteojos= armarUnaPrenda("anteojos",TipoDePrenda.ANTEOJOS, Material.PLASTICO, verde, rojo, Trama.LISA);
        camisa = armarUnaPrenda("camisa",TipoDePrenda.CAMISA, Material.ALGODON, blanco, rojo, Trama.LISA);



        /**OJO CON FECHA ACTUAL PARA LA GENERACION DE SUGERENCIAS EN EL EVENTO*/
        fechaCumpleWilly = LocalDateTime.of(2019,12,02,20,30);
        fechaCumpleSuegra = LocalDateTime.of(2019,12,12,20,30);
        fechaUltimaEntrega = LocalDateTime.of(2019,11,29,20,30);



        nuevoClima =	mock(TemperaturaOpenWeather.class);


        nuevoMeteorologo = mock (OpenWeather.class);
        when (nuevoMeteorologo.obtenerClima()).thenReturn(nuevoClima);

    }

    public Prenda armarUnaPrenda(String nombre,TipoDePrenda tipoDePrenda, Material material, Color colorPrimario, Color colorSecundario, Trama trama){
        BorradorPrenda borradorPrenda = new BorradorPrenda();
        borradorPrenda.definirNombre(nombre);
        borradorPrenda.definirTipo(tipoDePrenda);
        borradorPrenda.definirMaterial(material);
        borradorPrenda.definirColorPrimario(colorPrimario);
        borradorPrenda.definirColorSecundario(colorSecundario);
        borradorPrenda.definirTrama(trama);
        return borradorPrenda.crearPrenda();
    }


    @Test
    public void sePersisteUnEventoYSeRecupera(){
        facundo = new Usuario("Facundo Salerno",new ArrayList<Guardarropas>(Arrays.asList(new GuardarropasPremium("guardarropas casual", Arrays.asList(remera, camisa), Arrays.asList(pantalon), Arrays.asList(zapatos), Arrays.asList(anteojos)))), TipoDeUsuario.PREMIUM);

        facundo.crearEvento("Cumpleaños de juan", fechaCumpleWilly, FrecuenciaEvento.ANUAL,"Casa de Juan");

        entityManager().persist(facundo);

        assertEquals(entityManager()
                        .createQuery("from Usuario where id LIKE :IdUsuario", Usuario.class)
                        .setParameter("IdUsuario", facundo.getId())
                        .getResultList()
                        .get(0).getEventos().get(0).getNombre(),
                "Cumpleaños de juan");
    }

    @Test
    public void seRecuperanSugerenciasDeEvento() throws Exception {

        when(nuevoClima.getTemperature()).thenReturn(25.0);

        facundo = new Usuario("Facundo Salerno",new ArrayList<Guardarropas>(Arrays.asList(new GuardarropasPremium("guardarropas casual", Arrays.asList(remera, camisa), Arrays.asList(pantalon), Arrays.asList(zapatos), Arrays.asList(anteojos)))), TipoDeUsuario.PREMIUM);

        facundo.crearEvento("Cumpleaños de juan", fechaCumpleWilly, FrecuenciaEvento.ANUAL,"Casa de Juan");

        entityManager().persist(facundo);

        facundo.getEventos().get(0).generarSugerencias(nuevoMeteorologo);

        assertEquals(entityManager()
                        .createQuery("from Usuario where id LIKE :IdUsuario", Usuario.class)
                        .setParameter("IdUsuario", facundo.getId())
                        .getResultList()
                        .get(0).getEventos().get(0).getSugerenciasObtenidas(),
                facundo.getEventos().get(0).getSugerenciasObtenidas());

    }

    @Test
    public void seRecuperanEventosAPartirDeFecha() throws Exception {

        facundo = new Usuario("Facundo Salerno",new ArrayList<Guardarropas>(Arrays.asList(new GuardarropasPremium("guardarropas casual", Arrays.asList(remera, camisa), Arrays.asList(pantalon), Arrays.asList(zapatos), Arrays.asList(anteojos)))), TipoDeUsuario.PREMIUM);

        facundo.crearEvento("Cumpleaños de juan", fechaCumpleWilly, FrecuenciaEvento.ANUAL,"Casa de Juan");
        facundo.crearEvento("Cumpleaños de Suegra", fechaCumpleSuegra, FrecuenciaEvento.ANUAL,"Casa de suegri");
        facundo.crearEvento("Ultima entrega", fechaUltimaEntrega, FrecuenciaEvento.NO_SE_REPITE,"Lugano");

        entityManager().persist(facundo);

        LocalDateTime fechaBusqueda= LocalDateTime.of(2019,12,11,20,30);

        assertEquals(entityManager()
                        .createQuery("from Evento where fecha BETWEEN :inicio AND :fin", Evento.class)
                        .setParameter("inicio", fechaBusqueda.minusDays(2))
                        .setParameter("fin", fechaBusqueda.plusDays(2))
                        .getResultList()
                        .get(0).getNombre(),
                "Cumpleaños de Suegra");

    }

}
