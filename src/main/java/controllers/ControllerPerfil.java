package controllers;

import cron.RepositorioUsuarios;
import domain.usuario.Usuario;
import exceptions.UsuarioInexistente;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ControllerPerfil implements WithGlobalEntityManager, TransactionalOps{

    public ModelAndView mostrar(Request req, Response res){
        //Usuario usuario = new Usuario("foo","foo");
        //Map<String,String> model = HashMap<>;
        //SE USA SOLO EL ID, SE RECUPERA DE LA COOKIE Y CON EL MISMO SE BUSCA EN LA BASE DE DATOS PARA OBTENER TODOS LOS OTROS DATOS

        String nombre= req.cookie("cookie_nombre");
        Usuario usuario;
        try{
            usuario = RepositorioUsuarios.getInstance().buscarUsuario(nombre);
        }catch (UsuarioInexistente e){
            return new ModelAndView(null, "forbidden.hbs");
        }
        return new ModelAndView(usuario, "perfil.hbs");
    }


}
