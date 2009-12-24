package ar.uba.fi.jparticulas.model.movil;

import ar.uba.fi.jparticulas.model.Ambiente;
import ar.uba.fi.jparticulas.model.PhysicsHelper;
import ar.uba.fi.jparticulas.model.Vector;

/**
 * Un móvil es cualquier objeto que pueda ser ubicado en el espacio y al que puedan aplicarse las
 * leyes de la cinemática.
 * 
 * @author mmazzei
 */
public abstract class Movil {
	private double mass;
	private Vector position;
	private Vector velocity = new Vector(0, 0);
	private Vector acceleration = new Vector(0, 0);
	private double elasticity = PhysicsHelper.COEFICIENTE_ELASTICIDAD;
	private boolean isFixed = false;
	private Ambiente ambiente = null;

	// Utilizado en las colisiones, para determinar el plano del impacto
	private Vector previousPosition;

	/**
	 * Construye un móvil en la posición y con la masa indicadas que no se encuentra fijo y tiene
	 * velocidad y aceleración cero.
	 */
	public Movil(double mass, Vector position) {
		this.mass = mass;
		this.position = position;
		this.previousPosition = new Vector(position.getX(), position.getY());
	}

	/*-------------------------------------------------------------------------
	 *							GETTERS Y SETTERS
	 ------------------------------------------------------------------------*/
	/** @return La velocidad actual del móvil. */
	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}

	/** @return La aceleración actual del móvil. */
	public Vector getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector acceleration) {
		this.acceleration = acceleration;
	}

	/** @return La masa del móvil. */
	public double getMass() {
		return mass;
	}

	protected void setMass(double mass) {
		this.mass = mass;
	}

	/** @return La cantidad de momentum que se conserva tras una colisión. */
	public double getElasticity() {
		return elasticity;
	}

	public void setElasticity(double elasticity) {
		this.elasticity = elasticity;
	}

	/** @return La posición actual del móvil. */
	public Vector getPosition() {
		return position;
	}

	/**
	 * @return La posición anterior del móvil (la que tenía antes del último
	 *         {@link #updatePosition(double)}).
	 */
	public Vector getPreviousPosition() {
		return previousPosition;
	}

	/**
	 * Indica si el móvil se encuentra fijo (caso en el cual no es afectado por ninguna fuerza ni se
	 * desplaza).
	 */
	public boolean isFixed() {
		return isFixed;
	}

	public void setFixed(boolean isFixed) {
		this.isFixed = isFixed;
	}

	/** @return El ambiente en el que está inmerso el móvil. */
	public Ambiente getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(Ambiente ambiente) {
		this.ambiente = ambiente;
	}

	/** @return La mayor coordenada en X ocupada. */
	public abstract double getMaxX();

	/** @return La mayor coordenada en Y ocupada. */
	public abstract double getMaxY();

	/*-------------------------------------------------------------------------
	 *								LÓGICA
	 ------------------------------------------------------------------------*/
	/**
	 * Se encarga de detectar una colisión del proyectil contra éste móvil fijo y actuar en
	 * consecuencia.
	 * 
	 * @return true si hubo una colisión.
	 */
	public abstract boolean collide(Movil proyectile);

	/**
	 * Si el móvil ha tenido una colisión, se le notifica para que actúe en consecuencia.
	 * 
	 * @param horizontalCollision
	 *            Indica si se trata de una colisión contra un piso/techo (true) o pared vertical
	 *            (false).
	 */
	// TODO (mmazzei) - El segundo argumento debería ser el ángulo del impacto.
	protected abstract void wasCollide(Movil obstacle, boolean horizontalCollision);

	/**
	 * Actualiza la posición y velocidad del móvil habiendo transcurrido un intervalo de tiempo dt,
	 * en base a la aceleración que posee.
	 * 
	 * @param dt
	 *            Tiempo desde la última actualización en segundos.
	 */
	public void updatePosition(double dt) {
		if (isFixed) {
			return;
		}

		if (this.ambiente != null) {
			ambiente.applyForces(this);
		}

		// Euler mejorado para desplazamientos
		// Este método es de 2° orden (en lugar de 1° orden , como Euler comun)
		// x = x0 + v0 * t + a * (t^2) / 2
		previousPosition.copyFrom(position);
		position.addEquals(velocity.multiply(dt).add(acceleration.multiply(dt * dt / 2)));
		velocity.addEquals(acceleration.multiply(dt));
	}
}
