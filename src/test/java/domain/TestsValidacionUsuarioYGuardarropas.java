package domain;

import domain.guardarropas.GuardarropasPremium;
import domain.prenda.*;
import exceptions.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Arrays;

public class TestsValidacionUsuarioYGuardarropas {
    private Prenda zapatos;
    private Prenda remera;
    private Prenda pantalon;
    private Prenda blusa;
    private Prenda shorts;
    private Prenda zapatillas;
    private Prenda pollera;
    private Prenda ojotas;
    private Prenda alpargatas;
    private Prenda camisa;
    private Prenda pulseras;
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
        shorts = armarUnaPrenda("shorts",TipoDePrenda.SHORT, Material.JEAN, verde, rojo, Trama.GASTADO);
        blusa = armarUnaPrenda("blusa",TipoDePrenda.BLUSA, Material.ALGODON, azul, verde, Trama.LISA);
        zapatillas = armarUnaPrenda("zapatillas",TipoDePrenda.ZAPATILLA, Material.GAMUZA, azul, verde, Trama.LISA);
        pollera = armarUnaPrenda("pollera",TipoDePrenda.POLLERA, Material.POLIESTER, rojo, verde, Trama.CUADROS);
        ojotas = armarUnaPrenda("ojotas",TipoDePrenda.OJOTAS, Material.GOMA, azul, rojo, Trama.LISA);
        alpargatas = armarUnaPrenda("alpargatas",TipoDePrenda.ALPARGATAS, Material.ALGODON, azul, rojo, Trama.CUADROS);
        camisa = armarUnaPrenda("camisa",TipoDePrenda.CAMISA, Material.ALGODON, azul, rojo, Trama.CUADROS);
        pulseras=  armarUnaPrenda("pulsera",TipoDePrenda.PULSERA, Material.PLASTICO, azul, rojo, Trama.LISA);
        anteojos = armarUnaPrenda("anteojos",TipoDePrenda.ANTEOJOS, Material.PLASTICO, rojo, verde, Trama.LISA);
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

    //Verificar que se pueda crear correctamente un guardarropas
    @Test
    public void crearUnGuardarropasValido(){
        try{
            GuardarropasPremium guardarropasDeVerano = new GuardarropasPremium("guardarropas de verano", Arrays.asList(remera, blusa), Arrays.asList(shorts, pantalon), Arrays.asList(zapatos, zapatillas),Arrays.asList(pulseras, anteojos));
        }catch (NoPerteneceALaCategoriaException ex){
            Assert.fail();
        }
    }

    // Verificar que no se permita tener un guardarropa vacio

   @Test(expected= NoPermiteGuardarropaIncompletoException.class)
   public void pedirSugerenciaConGuardarropaVacio(){
         GuardarropasPremium guardarropasDeInvierno= new GuardarropasPremium ("guardarropas de invierno", Arrays.asList(),Arrays.asList(),Arrays.asList(),Arrays.asList());
    }

    // Verificar que no de sugerencias si no tiene prenda superior
    @Test(expected= NoPermiteGuardarropaIncompletoException.class)
    public void pedirSugerenciaConGuardarropaIncompleto(){
        GuardarropasPremium guardarropasDeportivo= new GuardarropasPremium ("guardarropas deportivo", Arrays.asList(),Arrays.asList(pantalon),Arrays.asList(zapatillas), Arrays.asList(anteojos));
    }

}

