package ar.uba.fi.jparticulas.adapter;

import org.jdom.DataConversionException;
import org.jdom.Element;

import ar.uba.fi.jparticulas.model.movil.Movil;
import ar.uba.fi.jparticulas.model.movil.Particle;
import ar.uba.fi.jparticulas.model.movil.Rectangle;

/**
 * Utilizado para obtener el {@link Persistible} adecuado a la clase de un determinado objeto del
 * modelo.
 * 
 * @author mmazzei
 */
public class PersistibleFactory {
	public static Persistible create(Rectangle rectangle) {
		return new RectangleJDOMAdapter(rectangle);
	}

	public static Persistible create(Particle particle) {
		return new ParticleJDOMAdapter(particle);
	}

	// TODO (mmazzei) - Mejorar o quitar
	public static Persistible create(Movil movil) {
		if (movil.getClass().equals(Rectangle.class)) {
			return create((Rectangle) movil);
		} else if (movil.getClass().equals(Particle.class)) {
			return create((Particle) movil);
		}

		throw new RuntimeException("No se ha implementado la persistencia para el tipo de objeto indicado.");
	}

	// TODO (mmazzei) - Mejorar o quitar
	public static Persistible create(Element element) throws DataConversionException {
		if (element.getName().equals(RectangleJDOMAdapter.TYPE_NAME)) {
			return new RectangleJDOMAdapter(element);
		} else if (element.getName().equals(ParticleJDOMAdapter.TYPE_NAME)) {
			return new ParticleJDOMAdapter(element);
		}

		throw new RuntimeException("No se ha implementado la persistencia para el tipo de objeto indicado.");
	}
}
