package domain.capaPrenda;

/**
 * El nivel de capa representa que tan arriba o abajo va la prenda en relacion a las demas. Por ejemplo, una remera deberia
 * tener un nivel de capa ABAJO ya que va abajo de por ejemplo una campera, que deberia tener un nivel de capa MEDIO o ARRIBA.
 * Si por ejemplo hubiera buzo, entonces el buzo deberia ser MEDIO y la campera ARRIBA.
 * Unas zapatillas deberian ser MEDIO considerando que ABAJO van las medias.
 * TOPE podria ser por ejemplo una campera de abrigo posta.
 */

public enum NivelDeCapa {
    ABAJO,
    MEDIO,
    ARRIBA,
    TOPE
}
