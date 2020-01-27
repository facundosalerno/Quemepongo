package controllers;

import cron.RepositorioGuardarropas;
import cron.RepositorioUsuarios;
import domain.usuario.Usuario;
import exceptions.ContraseñaInvalidaException;
import exceptions.UsuarioInexistente;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;


public class ControllerSesion implements WithGlobalEntityManager, TransactionalOps {

    private String mensajeSesion = "Nunca compartas tu contraseña con nadie.";


    public ModelAndView mostrarLogin(Request req, Response res) {
        return new ModelAndView(new ControllerSesion(), "login.hbs");  //Indica con que se va a renderizar el template (.hbs)
    }


    public ModelAndView iniciarSesion(Request req, Response res) {
        Usuario usuario;

        try {
            usuario = RepositorioUsuarios.getInstance().buscarUsuario(req.queryParams("query_username"));
            usuario.validarContraseña(req.queryParams("query_password"));
        } catch (UsuarioInexistente e) {
            mensajeSesion = "Usuario inexistente o contraseña invalida.";
            return new ModelAndView(this, "login.hbs");
        } catch (ContraseñaInvalidaException e) {
            mensajeSesion = "Usuario inexistente o contraseña invalida.";
            return new ModelAndView(this, "login.hbs");
        }

        res.cookie("cookie_nombre", usuario.getNombre());

        res.redirect("/perfil");
        return new ModelAndView(null, "perfil.hbs");
    }


    public ModelAndView cerrarSesion(Request req, Response res) {
        res.removeCookie("cookie_nombre");
        res.removeCookie("cookie_nombreGuardarropas");
        res.redirect("/login");
        return new ModelAndView(new ControllerSesion(), "login.hbs");
    }


    public String getMensajeSesion() {
        return mensajeSesion;
    }

    public void persistirUsuarioPrueba(){
        withTransaction(() ->{
            RepositorioGuardarropas.getInstance().agregarGuardarropas(RepositorioGuardarropas.guardarropasDelAdmin);
            RepositorioGuardarropas.getInstance().agregarGuardarropas(RepositorioGuardarropas.guardarropasDelAdminAuxiliar);
            RepositorioUsuarios.getInstance().agregarUsuario(RepositorioUsuarios.admin);
        });
    }
}




