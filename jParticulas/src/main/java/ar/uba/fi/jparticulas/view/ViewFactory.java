package ar.uba.fi.jparticulas.view;

import ar.uba.fi.jparticulas.model.movil.Movil;
import ar.uba.fi.jparticulas.model.movil.Particle;
import ar.uba.fi.jparticulas.model.movil.Rectangle;
import ar.uba.fi.jparticulas.model.movil.complex.Composite;
import ar.uba.fi.jparticulas.model.movil.complex.Firework;
import ar.uba.fi.jparticulas.view.movil.MovilView;
import ar.uba.fi.jparticulas.view.movil.ParticleView;
import ar.uba.fi.jparticulas.view.movil.RectangleView;
import ar.uba.fi.jparticulas.view.movil.complex.CompositeView;
import ar.uba.fi.jparticulas.view.movil.complex.FireworkView;

/**
 * Utilizado para obtener el {@link MovilView} adecuado a la clase de un determinado objeto del
 * modelo.
 * 
 * @author mmazzei
 */
public class ViewFactory {
	public static MovilView create(Rectangle rectangle) {
		return new RectangleView(rectangle);
	}

	public static MovilView create(Composite composite) {
		return new CompositeView(composite);
	}

	public static MovilView create(Particle particle) {
		return new ParticleView(particle, (short) 2);
	}

	public static MovilView create(Firework firework) {
		return new FireworkView(firework);
	}

	// TODO (mmazzei) - Mejorar o quitar
	public static MovilView create(Movil movil) {
		if (movil.getClass().equals(Rectangle.class)) {
			return create((Rectangle) movil);
		} else if (movil.getClass().equals(Particle.class)) {
			return create((Particle) movil);
		} else if (movil.getClass().equals(Composite.class)) {
			return create((Composite) movil);
		} else if (movil.getClass().equals(Firework.class)) {
			return create((Firework) movil);
		}

		throw new RuntimeException("El tipo de objeto indicado no posee una vista.");
	}
}
