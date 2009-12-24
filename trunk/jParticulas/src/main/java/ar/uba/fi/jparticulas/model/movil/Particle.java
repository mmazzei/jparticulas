package ar.uba.fi.jparticulas.model.movil;

import ar.uba.fi.jparticulas.model.Vector;

/**
 * Una partícula es un móvil sin volumen, que sólo ocupa un punto en el espacio.
 * 
 * @author mmazzei
 */
public class Particle extends Movil {
	/** @see MovilView#Movil(double, Vector) */
	public Particle(double mass, Vector position) {
		super(mass, position);
	}

	@Override
	public double getMaxX() {
		return this.getPosition().getX();
	}

	@Override
	public double getMaxY() {
		return this.getPosition().getY();
	}

	@Override
	public boolean collide(Movil m) {
		throw new RuntimeException("Aún no se implementó el mecanismo de colisión contra una partícula.");
	}

	// TODO (mmazzei) - ¡¡¡MEJORAR!!!
	@Override
	protected void wasCollide(Movil obstacle, boolean horizontalCollision) {
		if (horizontalCollision) {
			// Si fue una colisión contra un piso o techo, me ubico del lado que venía y reflejo la
			// velocidad vertical
			if (this.getPosition().getY() >= this.getPreviousPosition().getY()) {
				this.getPosition().setY(obstacle.getPosition().getY());
			} else {
				this.getPosition().setY(obstacle.getMaxY());
			}

			this.getVelocity().setY(this.getVelocity().getY() * this.getElasticity());
		} else {
			// Pero si la colisión fue contra una pared, me ubico del lado que venía y reflejo la
			// velocidad horizontal
			if (this.getPosition().getX() >= this.getPreviousPosition().getX()) {
				this.getPosition().setX(obstacle.getPosition().getX());
			} else {
				this.getPosition().setX(obstacle.getMaxX());
			}
			this.getVelocity().setX(this.getVelocity().getX() * this.getElasticity());
		}

		// Esto tiene sentido sólo cuando la gravedad es hacia abajo.
		// TODO (mmazzei) - Mejorar urgente
		if (
				// Si fue una colisión contra un piso o techo
				horizontalCollision
				// Y mi velocidad es demasiado baja
				&& (Math.abs(this.getVelocity().getY()) <= this.getAmbiente().getUmbralPrecision())
				// Y, definitivamente es contra un piso
				&& (this.getPreviousPosition().getY() <= this.getPosition().getY())) {
			// Entonces dejo de rebotar
			this.setFixed(true);
		}
	}
}
