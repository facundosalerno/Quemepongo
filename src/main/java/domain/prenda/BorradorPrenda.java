package domain.prenda;

import exceptions.NoPermiteMaterialException;
import exceptions.NoPermiteSerElMismoColorException;
import exceptions.TipoDePrendaNoDefinidoExcepcion;

import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class BorradorPrenda {
    private String nombre;
    private TipoDePrenda tipoPrenda;
    private Material material;
    private Color colorPrimario;
    private Color colorSecundario;
    private Trama trama = Trama.LISA;
    private Imagen imagen;

    public void definirNombre(String nombre){
        requireNonNull(nombre, "Por ahora no admitimos un nombre null");
        this.nombre = nombre;
    }
    public void definirTipo(TipoDePrenda tipoPrenda) {
        requireNonNull(tipoPrenda, "Por ahora no admitimos un tipo de prenda null");
        this.tipoPrenda = tipoPrenda;
    }

    public void definirMaterial(Material material) {
        requireNonNull(material, "Por ahora no admitimos un material null");
        if(isNull(tipoPrenda))
            throw new TipoDePrendaNoDefinidoExcepcion("Preferimos que defina antes el tipo de prenda");
        if (!tipoPrenda.permiteMaterial(material))
            throw new NoPermiteMaterialException();
        this.material = material;
    }

    public void definirColorPrimario(Color color) {
        requireNonNull(color, "Por ahora no admitimos el color null");
        if (color.equals(this.colorSecundario)) {
            throw new NoPermiteSerElMismoColorException();
        }
        this.colorPrimario = color;
    }

    public void definirColorSecundario(Color color) {
        requireNonNull(color, "Por ahora no admitimos el color null");
        if (color.equals(this.colorPrimario)) {
            throw new NoPermiteSerElMismoColorException();
        }
        this.colorSecundario = color;
    }

    public void definirTrama(Trama trama) {
        if(isNull(trama)){
            this.trama = Trama.LISA;
        } else{
            this.trama = trama;
        }

    }

    public void definirImagen(String pathDeArchivo) throws IOException {
        requireNonNull(pathDeArchivo, "El path del archivo no es valido");
        imagen = new Imagen(pathDeArchivo);
    }

    public Prenda crearPrenda() {
        requireNonNull(nombre, "el nombre es obligatorio");
        requireNonNull(tipoPrenda, "tipo de prenda es obligatorio");
        requireNonNull(material, "material es obligatorio");
        requireNonNull(colorPrimario, "color es obligatorio");
        requireNonNull(trama, "tipo de prenda es obligatorio");
        return new Prenda(nombre,tipoPrenda, colorPrimario, colorSecundario, material, trama, imagen);
    }

}

