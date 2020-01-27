package controllers;

import cron.RepositorioGuardarropas;
import domain.guardarropas.Guardarropas;
import domain.prenda.*;
import exceptions.NoExisteGuardarropasException;
import exceptions.NoPermiteMaterialException;
import exceptions.NoPermiteSerElMismoColorException;
import exceptions.TipoDePrendaNoDefinidoExcepcion;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.HashMap;

public class ControllerPrendas implements WithGlobalEntityManager, TransactionalOps {

    private String mensajeError;

    private HashMap<String, TipoDePrenda> tiposDePrenda = new HashMap<String, TipoDePrenda>() {{
        put("Sin accesorio", TipoDePrenda.SIN_ACCESORIO);
        put("Zapato", TipoDePrenda.ZAPATO);
        put("Remera", TipoDePrenda.REMERA);
        put("Camisa", TipoDePrenda.CAMISA);
        put("Pantalon", TipoDePrenda.PANTALON);
        put("Short", TipoDePrenda.SHORT);
        put("Blusa", TipoDePrenda.BLUSA);
        put("Zapatillas", TipoDePrenda.ZAPATILLA);
        put("Pollera", TipoDePrenda.POLLERA);
        put("Ojotas", TipoDePrenda.OJOTAS);
        put("Alpargatas", TipoDePrenda.ALPARGATAS);
        put("Pulsera", TipoDePrenda.PULSERA);
        put("Anteojos", TipoDePrenda.ANTEOJOS);
        put("Buso", TipoDePrenda.BUSO);
        put("Sweater", TipoDePrenda.SWEATER);
        put("Campera", TipoDePrenda.CAMPERA);
    }};

    private HashMap<String, Material> materialesDePrenda = new HashMap<String, Material>() {{
        put("Ninguno", Material.NINGUNO);
        put("Algodon", Material.ALGODON);
        put("Seda", Material.SEDA);
        put("Lino", Material.LINO);
        put("Cuero", Material.CUERO);
        put("Lana", Material.LANA);
        put("Licra", Material.LICRA);
        put("Poliester", Material.POLIESTER);
        put("Jean", Material.JEAN);
        put("Gamuza", Material.GAMUZA);
        put("Goma", Material.GOMA);
        put("Plastico", Material.PLASTICO);
        put("Pluma", Material.PLUMA);
        put("Gabardina", Material.GABARDINA);
    }};

    private HashMap<String, Trama> tramasDePrenda = new HashMap<String, Trama>() {{
        put("Lisa", Trama.LISA);
        put("Rayada", Trama.RAYADA);
        put("Cuadros", Trama.CUADROS);
        put("Gastado", Trama.GASTADO);
        put("Escocesa", Trama.ESCOCESA);
    }};


    public ModelAndView mostrarPrendas(Request req, Response res) {
        String idGuardarropas = req.params(":id");

        Guardarropas guardarropas;
        try {
            guardarropas = RepositorioGuardarropas.getInstance().buscarGuardarropas(Long.parseLong(idGuardarropas));

        } catch (NoExisteGuardarropasException e) {
            return new ModelAndView(null, "forbidden.hbs");
        }

        return new ModelAndView(guardarropas, "prendas.hbs");
    }


    public ModelAndView creacionPrenda(Request req, Response res) {
        String idGuardarropas = req.params(":id");
        Guardarropas guardarropas;
        try {
            guardarropas = RepositorioGuardarropas.getInstance().buscarGuardarropas(Long.parseLong(idGuardarropas));

        } catch (NoExisteGuardarropasException e) {
            return new ModelAndView(null, "forbidden.hbs");
        }
        return new ModelAndView(guardarropas, "crearPrenda.hbs");
    }


    public ModelAndView crearPrenda(Request req, Response res) throws IOException {
        String idGuardarropas = req.params(":id");
        Guardarropas guardarropas;
        try {
            guardarropas = RepositorioGuardarropas.getInstance().buscarGuardarropas(Long.parseLong(idGuardarropas));

        } catch (NoExisteGuardarropasException e) {
            return new ModelAndView(null, "forbidden.hbs");
        }

        BorradorPrenda borradorPrenda = new BorradorPrenda();
        borradorPrenda.definirNombre(
                req.queryParams("query_nombre")
        );

        try {
            borradorPrenda.definirTipo(tiposDePrenda.get(req.queryParams("query_tipoPrenda")));
            borradorPrenda.definirMaterial(materialesDePrenda.get(req.queryParams("query_material")));
            borradorPrenda.definirColorPrimario(new Color(Integer.parseInt(req.queryParams("query_colorp_r")), Integer.parseInt(req.queryParams("query_colorp_g")), Integer.parseInt(req.queryParams("query_colorp_b"))));

            if (req.queryParams("query_colors_enabled") != null) {
                borradorPrenda.definirColorSecundario(new Color(Integer.parseInt(req.queryParams("query_colors_r")), Integer.parseInt(req.queryParams("query_colors_g")), Integer.parseInt(req.queryParams("query_colors_b"))));
            }

            borradorPrenda.definirTrama(tramasDePrenda.get(req.queryParams("query_trama")));

            borradorPrenda.definirImagen(req.queryParams("query_imagen"));

            Prenda prenda = borradorPrenda.crearPrenda();

            guardarropas.agregarPrenda(prenda);

            withTransaction(() -> {
                entityManager().persist(prenda);
                entityManager().merge(guardarropas);
                //entityManager().flush();
            });

        }
        catch (NullPointerException e) {
            mensajeError = "Null error: " + e.getMessage();
            return new ModelAndView(this, "fallaCreacionPrenda.hbs");
        } catch (TipoDePrendaNoDefinidoExcepcion e) {
            mensajeError = e.getMessage();
            return new ModelAndView(this, "fallaCreacionPrenda.hbs");
        } catch (NoPermiteMaterialException e) {
            mensajeError = "El material elegido no esta disponible para el tipo de prenda seleccionado.";
            return new ModelAndView(this, "fallaCreacionPrenda.hbs");
        } catch (NoPermiteSerElMismoColorException e) {
            mensajeError = "Los colores primario y secundario no pueden ser iguales.";
            return new ModelAndView(this, "fallaCreacionPrenda.hbs");
        } catch (NumberFormatException e) {
            mensajeError = "Los colores deben ser numericos."; //TODO permitir color secundario que sea nulo ya que con esta solucion lo pone en 0 0 0
        } catch (Exception e) {
            mensajeError = "Error desconocido.";
            return new ModelAndView(this, "fallaCreacionPrenda.hbs");
        }

        //Si no se pone el redirect, igual va a ir a esa uri por que esta en la action de la form. Pero el metodo va a ser post, entonces cada vez que se recargue la pagina se vuelve a agregar la prenda. El redirect es un get de la uri.
        res.redirect("/guardarropas/" + idGuardarropas + "/prendas");


        return new ModelAndView(guardarropas, "prendas.hbs");


    }


    public String getMensajeError() {
        return mensajeError;
    }

}
