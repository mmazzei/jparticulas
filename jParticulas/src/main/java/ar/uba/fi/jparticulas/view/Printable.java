package ar.uba.fi.jparticulas.view;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Interfaz provista por cualquier clase de la aplicación que pueda ser dibujada en Java2D.
 * 
 * @author mmazzei
 */
// TODO (mmazzei) - Añadir getMovil (quitarlo de MovilView)
public interface Printable {
	/** Dibuja el elemento en gr. */
	public void print(Graphics2D gr);

	/** Dibuja el elemento en gr considerando sus coordenadas relativas a las pasadas por parámetro. */
	public void print(Graphics2D gr, double offsetX, double offsetY);

	/** @deprecated Esto no tiene ningún sentido aquí. */
	public void setColor(Color color);
}
