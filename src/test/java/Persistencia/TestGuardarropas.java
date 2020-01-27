package Persistencia;

import domain.guardarropas.Guardarropas;
import domain.guardarropas.GuardarropasLimitado;
import domain.guardarropas.GuardarropasPremium;
import domain.prenda.*;
import org.junit.Before;
import org.junit.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestGuardarropas extends AbstractPersistenceTest implements WithGlobalEntityManager {
    private GuardarropasPremium guardarropasPremiumVerano;
    private GuardarropasLimitado guardarropasLimitadoVerano;
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
    public void SeCreaUnGuardarropasLimitado() {
        guardarropasLimitadoVerano = new GuardarropasLimitado("guardarropas limitado de verano", Arrays.asList(camisaFormalAzul, remeraCanchera), Arrays.asList(pantalonFormal), Arrays.asList(zapatosFormales), Arrays.asList(anteojos));

        entityManager().persist(guardarropasLimitadoVerano);

        assertEquals(entityManager().find(Guardarropas.class,guardarropasLimitadoVerano.getId()),guardarropasLimitadoVerano);

    }


    @Test
    public void SeCreaUnGuardarropasPremium() {
        guardarropasPremiumVerano = new GuardarropasPremium("guardarropas premium de verano", new ArrayList<Prenda>(Arrays.asList( camisaFormalBlanca,remeraDeDia, camisaSalida)), new ArrayList<Prenda>(Arrays.asList(pantalonParaSalida)), new ArrayList<Prenda>(Arrays.asList(zapatosFormales)), new ArrayList<Prenda>(Arrays.asList(anteojos)));

        entityManager().persist(guardarropasPremiumVerano);

        assertEquals(entityManager().find(Guardarropas.class,guardarropasPremiumVerano.getId()),guardarropasPremiumVerano);

    }

    @Test
    public void SeAgregaUnaPrendaAUnGuardarropasPremium() {
        guardarropasInvierno = new GuardarropasPremium("guardarropas de invierno", new ArrayList<Prenda>(Arrays.asList( camisaFormalBlanca,remeraDeDia, camisaSalida, sweaterFormal, busoInformal, camperaParaSalida, camperaMichelin)), new ArrayList<Prenda>(Arrays.asList(pantalonParaSalida)), new ArrayList<Prenda>(Arrays.asList(zapatosFormales)), new ArrayList<Prenda>(Arrays.asList(anteojos)));

        entityManager().persist(guardarropasInvierno);

        guardarropasInvierno.agregarPrenda(camisaFormalAzul);

        assertEquals(entityManager().find(Guardarropas.class,guardarropasInvierno.getId()).getPrendas().contains(camisaFormalAzul),guardarropasInvierno.getPrendas().contains(camisaFormalAzul));

    }
}
