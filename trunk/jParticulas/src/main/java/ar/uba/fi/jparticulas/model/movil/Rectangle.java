package ar.uba.fi.jparticulas.model.movil;

import ar.uba.fi.jparticulas.model.PhysicsHelper;
import ar.uba.fi.jparticulas.model.Vector;

/**
 * Representa a todos aquellos móviles cuya forma corresponde a un rectángulo.
 * 
 * @author mmazzei
 */
public class Rectangle extends Movil {
	private double width;
	private double height;
	private double maxX;
	private double maxY;

	public Rectangle(double mass, Vector position, double width, double heigth) {
		super(mass, position);
		this.width = width;
		this.height = heigth;
		this.maxX = position.getX() + width;
		this.maxY = position.getY() + heigth;
	}

	/*-------------------------------------------------------------------------
	 *							GETTERS Y SETTERS
	 ------------------------------------------------------------------------*/
	/** @return El ancho del rectángulo. */
	public double getWidth() {
		return width;
	}

	/** @return El alto del rectángulo. */
	public double getHeight() {
		return height;
	}

	@Override
	public double getMaxX() {
		// Notar que esto deja de funcionar si el rectángulo se mueve ¿tiene sentido ahorrarse el
		// cálculo?
		return this.maxX;
	}

	@Override
	public double getMaxY() {
		// Notar que esto deja de funcionar si el rectángulo se mueve ¿tiene sentido ahorrarse el
		// cálculo?
		return this.maxY;
	}

	/*-------------------------------------------------------------------------
	 *								LÓGICA
	 ------------------------------------------------------------------------*/
	// TODO (mmazzei) - Una mejora para que quede mejor visualmente es utilizar la posActual y la
	// proyectada para el siguiente instante de tiempo (porque, de otro modo, se estará actuando
	// demasiado tarde).
	@Override
	public boolean collide(Movil m) {
		// TODO (mmazzei) - Quitar esta condición y que soporte móvil contra móvil
		if (!this.isFixed()) {
			throw new RuntimeException("El mecanismo de colisiones sólo soporta un móvil contra un fijo.");
		}

		// TODO (mmazzei) - Notar que esto sólo funciona contra partículas (se pasa un punto al
		// contains). Implementar soporte para impacto de rectángulo contra rectángulo.

		// Verifico que el móvil haya impactado contra el rectángulo Para eso debo verificar que
		// actualmente se encuentre dentro y, anteriormente, fuera
		if (!PhysicsHelper.contains(m.getPosition(), this.getPosition().getX(), this.getPosition().getY(), width,
				height)) {
			return false;
		}

		if (PhysicsHelper.contains(m.getPreviousPosition(), this.getPosition().getX(), this.getPosition().getY(),
				width, height)) {
			return false;
		}

		// Notar que se supone que el rectángulo nunca podrá estar inclinado, por lo que las
		// colisiones serán contra una cara vertical u horizontal. Gracias a esta información,
		// pueden ahorrarse cálculos y no es necesario reflejar la velocidad del móvil contra la
		// normal del rectángulo.
		// De otra manera, el "boolean impactoHorizontal" debería ser "double collisionAngle".

		// Indica si p impactó a r en una de sus caras horizontales.
		boolean impactoHorizontal = false;
		if (m.getVelocity().getSlope() == 0.0) {
			// Si p se mueve horizontalmente, sólo puede ser un impacto vertical
			impactoHorizontal = false;
		} else {
			// Si la partícula se movía hacia arriba, debo evaluar con la cara inferior del
			// rectángulo y, si se movía hacia abajo, con la superior.
			Vector segmento = new Vector(this.getPosition().getX(), this.getPosition().getY());
			if (m.getVelocity().getY() < 0) {
				segmento.setY(this.getMaxY());
			}

			// Verifico si se intersecta la traza del movimiento de m con el segmento
			// correspondiente del rectángulo. Si no es así, es una colisión vertical.
			impactoHorizontal = PhysicsHelper.intersectWithSegment(m.getPreviousPosition().getX(), m
					.getPreviousPosition().getY(), m.getVelocity().getSlope(), segmento.getX(), segmento.getY(),
					segmento.getX() + this.getWidth(), segmento.getY());
		}

		// Notifico al móvil que impactó
		m.wasCollide(this, impactoHorizontal);
		return true;
	}

	@Override
	protected void wasCollide(Movil obstacle, boolean horizontalCollision) {
		throw new RuntimeException(
				"Aún no se implementó un mecanismo para que un rectángulo impacte contra otro objeto.");
	}
}
