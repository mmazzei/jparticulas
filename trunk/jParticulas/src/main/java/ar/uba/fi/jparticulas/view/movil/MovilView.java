package ar.uba.fi.jparticulas.view.movil;

import java.awt.Color;

import ar.uba.fi.jparticulas.model.movil.Movil;
import ar.uba.fi.jparticulas.view.Printable;

/**
 * Encabeza la jerarquía de las más simples vistas implementadas para las clases del modelo.
 * 
 * @author mmazzei
 */
public abstract class MovilView implements Printable {
	private Color color = Color.red;
	private boolean drawInfo;
	protected final Movil movil;

	public MovilView(Movil movil) {
		this.movil = movil;
	}

	/** @deprecated Esto debe subir a Printable */
	public Movil getMovil() {
		return movil;
	}

	protected Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
