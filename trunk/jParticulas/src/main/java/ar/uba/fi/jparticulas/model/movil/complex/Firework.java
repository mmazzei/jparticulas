package ar.uba.fi.jparticulas.model.movil.complex;

import java.util.Random;

import ar.uba.fi.jparticulas.model.Vector;
import ar.uba.fi.jparticulas.model.movil.Particle;

/**
 * Representa un proyectil que luego de cierto tiempo explota en varios fragmentos.<br />
 * Estos fragmentos son partículas y, actualmente, no respetan colisiones de ningún tipo.<br />
 * Debe tenerse en cuenta que los fragmentos también tienen un tiempo de vida (que, por defecto, es
 * el mismo que el del proyectil) que comienza a contar al momento de la explosión y, tras el cual,
 * debería ignorárselos.
 * 
 * @author mmazzei
 */
public class Firework extends Particle {
	private static final int FUERZA_EXPLOSION = 50;
	double proyectileLifeTime;
	double fragmentsLifeTime;
	private int fragmentCount;
	private boolean isExploded = false;
	private Particle[] fragments;

	/**
	 * @param proyectileLifeTime
	 *            La cantidad de segundos que deben transcurrir hasta que explote.
	 * @param fragments
	 *            La cantidad de fragmentos que resultarán de su explosión.
	 */
	public Firework(double mass, Vector position, double lifeTime, int fragments) {
		super(mass, position);
		this.proyectileLifeTime = lifeTime;
		this.fragmentCount = fragments;
		this.fragmentsLifeTime = lifeTime * 3;
	}

	/*-------------------------------------------------------------------------
	 *							GETTERS Y SETTERS
	 ------------------------------------------------------------------------*/
	@Override
	public double getMaxX() {
		return this.getPosition().getX();
	}

	@Override
	public double getMaxY() {
		return this.getPosition().getY();
	}

	/** El tiempo de vida de los fragmentos (es ignorado si ya explotó el proyectil). */
	public void setFragmentsLifeTime(double lifeTime) {
		if (!this.isExploded) {
			this.fragmentsLifeTime = lifeTime;
		}
	}

	/** El tiempo de vida que le queda a los fragmentos. */
	public double getFragmentsLifeTime() {
		return this.fragmentsLifeTime;

	}

	/** @return Los fragmentos en que explotó el proyectil. */
	public Particle[] getFragments() {
		return this.fragments;
	}

	/** @return true si ya explotó el proyectil. */
	public boolean isExploded() {
		return isExploded;
	}

	/*-------------------------------------------------------------------------
	 *								LÓGICA
	 ------------------------------------------------------------------------*/
	/** Si el objeto no ha explotado, actualiza su posición y tiempo de vida. */
	@Override
	public void updatePosition(double dt) {
		if (!this.isExploded) {
			super.updatePosition(dt);
			this.proyectileLifeTime -= dt;
			this.isExploded = (this.proyectileLifeTime < 0);
		} else {
			this.fragmentsLifeTime -= dt;
			for (int i = 0; i < fragmentCount; i++) {
				fragments[i].updatePosition(dt);
			}

			if (this.fragmentsLifeTime < 0) {
				this.setFixed(true);
			}
		}

		if ((isExploded) && (fragments == null)) {
			this.explode();
		}
	}

	/** @return Los fragmentos del proyectil al explotar (si aún no se los ha solicitado). */
	public Particle[] explode() {
		if (!isExploded) {
			throw new RuntimeException("Aún no ha transcurrido el tiempo necesario.");
		}
		if (fragments != null) {
			throw new RuntimeException("Ya se han creado los proyectiles.");
		}

		// Genero las partículas en la posición del proyectil y con velocidades aleatorias
		// (relativas a la del proyectil). La masa de cada una es una fracción de la total.
		Random rnd = new Random();
		fragments = new Particle[this.fragmentCount];
		for (int i = 0; i < fragments.length; i++) {
			double x = rnd.nextGaussian() * FUERZA_EXPLOSION + this.getVelocity().getX();
			double y = rnd.nextGaussian() * FUERZA_EXPLOSION + this.getVelocity().getY();
			Vector velocidad = new Vector(x, y);
			fragments[i] = new Particle(this.getMass() / fragmentCount, this.getPosition().clone());
			fragments[i].setVelocity(velocidad);
			fragments[i].setAmbiente(this.getAmbiente());
		}

		return fragments;
	}
}
