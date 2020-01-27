package controllers;

import cron.RepositorioUsuarios;
import domain.usuario.Usuario;
import exceptions.UsuarioInexistente;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.Collections;

public class ControllerCalendario implements WithGlobalEntityManager, TransactionalOps{

    public ModelAndView mostrar(Request req, Response res){
        String nombre= req.cookie("cookie_nombre");
        Usuario usuario;
        try{
            usuario = RepositorioUsuarios.getInstance().buscarUsuario(nombre);
        }catch (UsuarioInexistente e){
            return new ModelAndView(null, "forbidden.hbs");
        }
        Collections.sort(usuario.getEventos());
        ControllerMonth controllerMonth = new ControllerMonth(usuario.getEventos());
        return new ModelAndView(controllerMonth, "calendario.hbs");
    }

}
