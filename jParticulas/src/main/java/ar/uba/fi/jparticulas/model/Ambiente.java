package ar.uba.fi.jparticulas.model;

import ar.uba.fi.jparticulas.model.movil.Movil;

/**
 * Cada objeto del sistema es afectado por ciertas condiciones impuestas por el ambiente, además de
 * interactuar con otros objetos. Esta clase representa al ambiente en el que se encuentran.
 * 
 * @author mmazzei
 */
// TODO (mmazzei) - ¿Agregar viento?
public class Ambiente {
	private double viscosity;
	private Vector acceleration;

	// TODO (mmazzei) - Rename
	private double umbralPrecision;

	public Ambiente(double viscosity, Vector acceleration, double umbralPrecision) {
		this.viscosity = viscosity;
		this.acceleration = acceleration;
		this.umbralPrecision = umbralPrecision;
	}

	/**
	 * @return La viscosidad del ambiente. Influye en la fuerza necesaria por un objeto para
	 *         avanzar.
	 */
	public double getViscosity() {
		return viscosity;
	}

	/** @return La aceleración aplicada sobre cada objeto en el ambiente. */
	public Vector getAcceleration() {
		return acceleration;
	}

	/**
	 * @return Velocidad más baja que puede tener un objeto. Por debajo de la misma, se lo ignora al
	 *         procesar movimientos.
	 */
	// TODO (mmazzei) - Mover esto a otra parte
	public double getUmbralPrecision() {
		return umbralPrecision;
	}

	/** Aplica sobre un objeto todas las fuerzas correspondientes al ambiente. */
	// TODO (mmazzei) - ¡¡¡MEJORAR!!!
	public void applyForces(Movil movil) {
		// Aquí se aplica demasiado mal la noción de viscosidad. Sólo es un parche.
		if (viscosity != 0) {
			movil.getVelocity().multiplyEquals(viscosity);
		}
		if (this.acceleration != null) {
			movil.setAcceleration(acceleration);
		}
	}
}
