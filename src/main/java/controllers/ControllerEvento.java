package controllers;

import cron.RepositorioUsuarios;
import domain.atuendo.Atuendo;
import domain.evento.Evento;
import domain.evento.FrecuenciaEvento;
import domain.usuario.Usuario;
import exceptions.UsuarioInexistente;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;

public class ControllerEvento implements WithGlobalEntityManager, TransactionalOps {
    LocalDateTime hoy;





    public ModelAndView creacionEvento(Request req, Response res){
        hoy = LocalDateTime.now();
        return new ModelAndView(this, "crearEvento.hbs");
    }





    public ModelAndView crear(Request req, Response res){
        String nombre= req.cookie("cookie_nombre");
        Usuario usuario;
        try{
            usuario = RepositorioUsuarios.getInstance().buscarUsuario(nombre);
        }catch (UsuarioInexistente e){
            return new ModelAndView(null, "forbidden.hbs");
        }

        usuario.crearEvento(req.queryParams("query_nombre"), LocalDateTime.parse(req.queryParams("query_localDateTime")), FrecuenciaEvento.NO_SE_REPITE, req.queryParams("query_lugar"));
        Evento eventoAPersistir = usuario.buscarEvento(req.queryParams("query_nombre"));

        withTransaction(() -> {
            entityManager().persist(eventoAPersistir);
            entityManager().merge(usuario);

        });


        res.redirect("/calendario");
        return new ModelAndView(null, "calendario.hbs");
    }





    public ModelAndView sugerenciasDelEvento(Request req, Response res){
        String nombre= req.cookie("cookie_nombre");
        Usuario usuario;
        try{
            usuario = RepositorioUsuarios.getInstance().buscarUsuario(nombre);
        }catch (UsuarioInexistente e){
            return new ModelAndView(null, "forbidden.hbs");
        }
        String nombreEvento = req.params(":nombre");
        Evento evento = usuario.buscarEvento(nombreEvento);
        return new ModelAndView(evento, "sugerenciasEvento.hbs");
    }





    public ModelAndView aceptarSugerencia(Request req, Response res){
        String nombre= req.cookie("cookie_nombre");
        Usuario usuario;
        try{
            usuario = RepositorioUsuarios.getInstance().buscarUsuario(nombre);
        }catch (UsuarioInexistente e){
            return new ModelAndView(null, "forbidden.hbs");
        }
        String nombreEvento = req.params(":nombre");
        Evento evento = usuario.buscarEvento(nombreEvento);
        String idSugerencia = req.params(":id");
        Atuendo atuendo = evento.buscarAtuendo(Long.parseLong(idSugerencia));

        usuario.aceptarSugerencia(atuendo);


        evento.getSugerenciasObtenidas().stream().filter(a-> !a.equals(atuendo)).forEach(a->usuario.rechazarSugerencia(a));
        evento.borrarSugerenciasObtenidas();

        withTransaction(() -> {
            entityManager().persist(atuendo);
            entityManager().merge(usuario);

        });


        res.redirect("/sugerencias/aceptadas");
        return new ModelAndView(null, null);
    }





    public ModelAndView verSugerenciasAceptadas(Request req, Response res){
        String nombre= req.cookie("cookie_nombre");
        Usuario usuario;
        try{
            usuario = RepositorioUsuarios.getInstance().buscarUsuario(nombre);
        }catch (UsuarioInexistente e){
            return new ModelAndView(null, "forbidden.hbs");
        }
        return new ModelAndView(usuario, "sugerenciaAceptadas.hbs");
    }




    public ModelAndView calificar(Request req, Response res){
        String nombre= req.cookie("cookie_nombre");
        Usuario usuario;
        try{
            usuario = RepositorioUsuarios.getInstance().buscarUsuario(nombre);
        }catch (UsuarioInexistente e){
            return new ModelAndView(null, "forbidden.hbs");
        }
        String idSugerencia = req.params("id");
        Atuendo atuendo = usuario.buscarAtuendoAceptado(Long.parseLong(idSugerencia));
        int calificacion = Integer.parseInt(req.queryParams("var_button_calificacion"));
        usuario.calificarSugerencia(atuendo, calificacion);

        withTransaction(() -> {
            entityManager().merge(atuendo);
            entityManager().merge(usuario);

        });

        res.redirect("/sugerencias/aceptadas");
        return new ModelAndView(null, "sugerenciaAceptadas.hbs");
    }





    public LocalDateTime getHoy() {
        return hoy;
    }
}
