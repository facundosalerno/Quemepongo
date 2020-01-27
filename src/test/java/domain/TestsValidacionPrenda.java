package domain;

import domain.prenda.*;
import exceptions.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestsValidacionPrenda{
    private BorradorPrenda borradorPrenda;
    private Color rojo;
    private Color verde;
    private Color azul;

    @Before
    public void init(){
        //Instanciaciones previas a los TEST
        borradorPrenda = new BorradorPrenda();
        rojo = new Color(255,0,0);
        verde = new Color(0,255,0);
        azul = new Color(0,0,255);
    }

    //TODO: Hay que ver si esta bien que pueda agarrar tantas exceptions porque no queda claro cual fue el error
    
    //Test para verificar que una prenda se puede crear correctamente
    @Test
    public void verificarCreacionDeLaPrenda(){
        Prenda remeraLisaDeCarlos;
        try{
            borradorPrenda.definirTipo(TipoDePrenda.REMERA);
            borradorPrenda.definirMaterial(Material.ALGODON);
            borradorPrenda.definirColorPrimario(rojo);
            borradorPrenda.definirColorSecundario(verde);
            borradorPrenda.definirTrama(Trama.LISA);
            borradorPrenda.definirNombre("remera lisa navidad");
            remeraLisaDeCarlos = borradorPrenda.crearPrenda();
            Assert.assertEquals(remeraLisaDeCarlos.getTipoPrenda(), TipoDePrenda.REMERA);
        }catch (NullPointerException | TipoDePrendaNoDefinidoExcepcion | NoPermiteMaterialException | NoPermiteSerElMismoColorException ex){
            Assert.fail();
        }
    }

    //Test para verificar consistencia tipo de prenda con material
    @Test
    public void verificar_consistencia_material_con_tipo_prenda (){
        borradorPrenda.definirTipo(TipoDePrenda.REMERA);
        borradorPrenda.definirMaterial(Material.ALGODON);
        borradorPrenda.definirColorPrimario(rojo);
        borradorPrenda.definirColorSecundario(verde);
        borradorPrenda.definirTrama(Trama.LISA);
        borradorPrenda.definirNombre("remera lisa navidad");
        Prenda remeraLisaDeCarlos = borradorPrenda.crearPrenda();

        Assert.assertTrue(remeraLisaDeCarlos.getTipoPrenda().permiteMaterial(Material.ALGODON));
    }

    //Test para verificar que si dos colores son iguales, se lance la excepcion, independientemente de cual color se asigne primero
    @Test (expected = NoPermiteSerElMismoColorException.class)
    public void definir_color_secundario_igual_al_primario(){
        Color rojoHermoso = new Color(255, 0, 0);
        borradorPrenda.definirColorSecundario(rojoHermoso);
        borradorPrenda.definirColorPrimario(rojo);

    }


    //Test para verificar que no se permita agregar un material incorrecto
    @Test(expected= NoPermiteMaterialException.class)
    public void definirMaterialInconsistenteConTipoPrendaDaExcepcion (){
        borradorPrenda.definirTipo(TipoDePrenda.REMERA);
        borradorPrenda.definirMaterial(Material.JEAN);
    }


}
