package domain.guardarropas;


import domain.prenda.Categoria;
import domain.prenda.Prenda;
import domain.usuario.TipoDeUsuario;
import exceptions.NoAceptaCantidadPrendasException;
import exceptions.NoPermiteGuardarropaIncompletoException;
import exceptions.NoPerteneceALaCategoriaException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;



@Entity
@DiscriminatorValue(value="L")
public class GuardarropasLimitado extends Guardarropas {

	//Solo para que sea compatible con JPA
    protected GuardarropasLimitado() {};

    public GuardarropasLimitado(String nombre){
        this.nombre = nombre;
    }

    public GuardarropasLimitado (String nombre, List<Prenda> prendasSuperiores, List<Prenda> prendasInferiores, List<Prenda> calzados, List<Prenda> accesorios) {
        if(prendasSuperiores.isEmpty() || prendasInferiores.isEmpty() || calzados.isEmpty() || accesorios.isEmpty())
            throw new NoPermiteGuardarropaIncompletoException();

        if(prendasSuperiores.size() > cantidadPrendasPermitidas() || prendasInferiores.size() > cantidadPrendasPermitidas() || calzados.size() > cantidadPrendasPermitidas() || accesorios.size() > cantidadPrendasPermitidas())
            throw new NoAceptaCantidadPrendasException();
        prendasCoincidenConCategoria(prendasSuperiores, Categoria.PARTE_SUPERIOR);
        prendasCoincidenConCategoria(prendasInferiores, Categoria.PARTE_INFERIOR);
        prendasCoincidenConCategoria(calzados, Categoria.CALZADO);
        prendasCoincidenConCategoria(accesorios, Categoria.ACCESORIOS);
        this.prendasSuperiores = new ArrayList<>(prendasSuperiores);
        this.prendasInferiores = new ArrayList<>(prendasInferiores);
        this.calzados = new ArrayList<>(calzados);
        this.accesorios= new ArrayList<>(accesorios);
        this.nombre=nombre;
        //RepositorioGuardarropas.getInstance().agregarGuardarropas(this);
    }

    private static int cantidadPrendasPermitidas(){
        return 4;
    }



    @Override
    public TipoDeUsuario tipoDeUsuarioQueAcepta() {
        return TipoDeUsuario.GRATIS;
    }

    private void validarQueNoSuperaCantidadPermitida(List<Prenda> prendas){
        if (prendas.size() == cantidadPrendasPermitidas() ){
            throw new NoAceptaCantidadPrendasException();
        }
    }
    @Override
    public void agregarPrendaSuperior(Prenda prendaAgregada) {
        validarQueNoSuperaCantidadPermitida(prendasSuperiores);
        if (!prendaAgregada.esDeCategoria(Categoria.PARTE_SUPERIOR)){
            throw new NoPerteneceALaCategoriaException();
        }

        this.prendasSuperiores.add(prendaAgregada);
    }
    @Override
    public void agregarPrendaInferior(Prenda prendaAgregada) {
        validarQueNoSuperaCantidadPermitida(prendasInferiores);

        if(!prendaAgregada.esDeCategoria(Categoria.PARTE_INFERIOR)){
            throw new NoPerteneceALaCategoriaException();
        }

        this.prendasInferiores.add(prendaAgregada);
    }

    @Override
    public void agregarPrendaCalzado(Prenda prendaAgregada) {
        validarQueNoSuperaCantidadPermitida(calzados);
        if (!prendaAgregada.esDeCategoria(Categoria.CALZADO)){
            throw new NoPerteneceALaCategoriaException();
        }
        this.calzados.add(prendaAgregada);
    }

    @Override
    public void agregarPrendaAccesorio(Prenda prendaAgregada) {
        validarQueNoSuperaCantidadPermitida(accesorios);
        if (!prendaAgregada.esDeCategoria(Categoria.ACCESORIOS)){
            throw new NoPerteneceALaCategoriaException();
        }
        this.accesorios.add(prendaAgregada);
    }
}
