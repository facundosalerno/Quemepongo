package cron;

import domain.guardarropas.Guardarropas;
import domain.guardarropas.GuardarropasPremium;
import domain.prenda.*;
import exceptions.NoExisteGuardarropasException;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RepositorioGuardarropas implements WithGlobalEntityManager {

    private static RepositorioGuardarropas instance = null;

    private List<Guardarropas> listaDeGuardarropas = new ArrayList<>();

    private RepositorioGuardarropas(){}

    public static RepositorioGuardarropas getInstance() {
        if(instance == null)
            return instance = new RepositorioGuardarropas();
        return instance;
    }


    public List<Guardarropas> getListaDeGuardarropas() {
        return listaDeGuardarropas;
    }

    public void agregarGuardarropas(Guardarropas guardarropas){
        /*if(!listaDeGuardarropas.contains(guardarropas))
            listaDeGuardarropas.add(guardarropas);
        */
        if(guardarropas.getId() == null) {  //TODO: verificar que ande y que sea correcto
            entityManager().persist(guardarropas);
        }
    }

    public void eliminarGuardarropas(Guardarropas guardarropas){
        if(listaDeGuardarropas.contains(guardarropas))
            listaDeGuardarropas.remove(guardarropas);
    }

    public Guardarropas buscarGuardarropas(Long id){  //String nombre
        Guardarropas guardarropas = entityManager().find(Guardarropas.class, id);//listaDeGuardarropas.stream().filter(g -> g.getNombre().equals(nombre)).findFirst().orElse(null);
        if(guardarropas == null)
            throw new NoExisteGuardarropasException();
        return guardarropas;
    }


    public static Prenda armarUnaPrenda(String nombre, TipoDePrenda tipoDePrenda, Material material, Color colorPrimario, Color colorSecundario, Trama trama){
        BorradorPrenda borradorPrenda = new BorradorPrenda();
        borradorPrenda.definirNombre(nombre);
        borradorPrenda.definirTipo(tipoDePrenda);
        borradorPrenda.definirMaterial(material);
        borradorPrenda.definirColorPrimario(colorPrimario);
        borradorPrenda.definirColorSecundario(colorSecundario);
        borradorPrenda.definirTrama(trama);
        //borradorPrenda.definirImagen("/public/remera.png");
        return borradorPrenda.crearPrenda();
    }


    public static GuardarropasPremium guardarropasDelAdmin = new GuardarropasPremium(
            "guardarropas principal",
            Arrays.asList(armarUnaPrenda("remera batman", TipoDePrenda.REMERA, Material.ALGODON, new Color(0, 0, 255), new Color(255, 0, 0), Trama.CUADROS), armarUnaPrenda("buso de superman", TipoDePrenda.BUSO, Material.ALGODON, new Color(0, 255, 0), new Color(0, 0, 255), Trama.LISA)),
            Arrays.asList(armarUnaPrenda("pantalon pescador", TipoDePrenda.PANTALON, Material.JEAN, new Color(0, 255, 0), new Color(255, 0, 0), Trama.RAYADA), armarUnaPrenda("short de verano", TipoDePrenda.SHORT, Material.JEAN, new Color(0, 255, 0), new Color(255, 0, 0), Trama.ESCOCESA)),
            Arrays.asList(armarUnaPrenda("zapatos de laburo", TipoDePrenda.ZAPATO, Material.CUERO, new Color(255, 0, 0), new Color(0, 0, 255), Trama.GASTADO), armarUnaPrenda("ojotas tipo crock", TipoDePrenda.OJOTAS, Material.GOMA, new Color(255, 0, 0), new Color(0, 0, 255), Trama.LISA)),
            Arrays.asList(armarUnaPrenda("anteojos de sol", TipoDePrenda.ANTEOJOS, Material.PLASTICO, new Color(0, 255, 0), new Color(255, 0, 0), Trama.LISA), armarUnaPrenda("vacio", TipoDePrenda.SIN_ACCESORIO, Material.NINGUNO, new Color(0, 0, 0), new Color(0, 0, 1), Trama.LISA))
    );

    public static GuardarropasPremium guardarropasDelAdminAuxiliar = new GuardarropasPremium(
            "guardarropas de emergencia",
            Arrays.asList(armarUnaPrenda("remera bensimon", TipoDePrenda.REMERA, Material.ALGODON, new Color(0, 0, 255), new Color(255, 0, 0), Trama.CUADROS), armarUnaPrenda("buso abrigadisimo", TipoDePrenda.BUSO, Material.ALGODON, new Color(0, 255, 0), new Color(0, 0, 255), Trama.LISA)),
            Arrays.asList(armarUnaPrenda("pantalon copado", TipoDePrenda.PANTALON, Material.JEAN, new Color(0, 255, 0), new Color(255, 0, 0), Trama.RAYADA), armarUnaPrenda("short gastado", TipoDePrenda.SHORT, Material.JEAN, new Color(0, 255, 0), new Color(255, 0, 0), Trama.GASTADO)),
            Arrays.asList(armarUnaPrenda("zapatos marrones", TipoDePrenda.ZAPATO, Material.CUERO, new Color(255, 0, 0), new Color(0, 0, 255), Trama.GASTADO), armarUnaPrenda("ojotas hawaianas", TipoDePrenda.OJOTAS, Material.GOMA, new Color(255, 0, 0), new Color(0, 0, 255), Trama.LISA)),
            Arrays.asList(armarUnaPrenda("anteojos de leer", TipoDePrenda.ANTEOJOS, Material.PLASTICO, new Color(0, 255, 0), new Color(255, 0, 0), Trama.LISA), armarUnaPrenda("vacio", TipoDePrenda.SIN_ACCESORIO, Material.NINGUNO, new Color(0, 0, 0), new Color(0, 0, 1), Trama.LISA))
    );
}