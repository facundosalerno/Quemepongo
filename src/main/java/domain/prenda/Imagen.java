package domain.prenda;

import javax.imageio.ImageIO;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Embeddable
public class Imagen {
	@Transient
    final private int altura=100;
	@Transient
    final private int ancho=100;

	private String url=new String();

	Imagen(){}

    Imagen(String nombreDeArchivo) {
        url = nombreDeArchivo;
    }

    public String getUrl(){return url;}


    public BufferedImage getImagen()throws IOException{
        BufferedImage imagenDeArchivo = ImageIO.read(new File(url));                     //Abre la imagen desde un path

        Image tmp = imagenDeArchivo.getScaledInstance (ancho, altura, Image.SCALE_SMOOTH);         //Crea una instancia de Image con un tamano especifio (100x100)

        BufferedImage imagenNormalizada = new BufferedImage( ancho, altura, BufferedImage.TYPE_INT_ARGB);   //Genera el "lienzo" para poner la imagen normalizada

        Graphics2D g2d = imagenNormalizada.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);        //Dibuja la tmp (imagen con resolucion y tamano normalizado) en imagenNormalizada
        g2d.dispose();                                          //Libera recursos Graphics2D pedidos (g2d)

        return imagenNormalizada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Imagen)) return false;
        Imagen imagen = (Imagen) o;
        return altura == imagen.altura &&
                ancho == imagen.ancho &&
                Objects.equals(url, imagen.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(altura, ancho, url);
    }
}