package domain.guardarropas;


import domain.prenda.Categoria;
import domain.prenda.Prenda;
import domain.usuario.TipoDeUsuario;
import exceptions.NoPermiteGuardarropaIncompletoException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value="P")
public class GuardarropasPremium extends Guardarropas{
	
	//Solo para que sea compatible con JPA
    protected GuardarropasPremium() {}

    public GuardarropasPremium(String nombre){
        this.nombre = nombre;
    }

    public GuardarropasPremium(String nombre, List<Prenda> prendasSuperiores, List<Prenda> prendasInferiores, List<Prenda> calzados, List<Prenda> accesorios) {

        if(prendasSuperiores.isEmpty() || prendasInferiores.isEmpty() || calzados.isEmpty() || accesorios.isEmpty())
            throw new NoPermiteGuardarropaIncompletoException();
        prendasCoincidenConCategoria(this.prendasSuperiores, Categoria.PARTE_SUPERIOR);
        prendasCoincidenConCategoria(this.prendasInferiores, Categoria.PARTE_INFERIOR);
        prendasCoincidenConCategoria(this.calzados, Categoria.CALZADO);
        prendasCoincidenConCategoria(this.accesorios, Categoria.ACCESORIOS);
        this.prendasSuperiores = new ArrayList<>(prendasSuperiores);
        this.prendasInferiores = new ArrayList<>(prendasInferiores);
        this.calzados = new ArrayList<>(calzados);
        this.accesorios= new ArrayList<>(accesorios);
        this.nombre=nombre;
        //RepositorioGuardarropas.getInstance().agregarGuardarropas(this);
    }

    @Override
    public TipoDeUsuario tipoDeUsuarioQueAcepta() {
        return TipoDeUsuario.PREMIUM;
    }


}
