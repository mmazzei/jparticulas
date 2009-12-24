package ar.uba.fi.jparticulas.controller;

import java.util.ArrayList;
import java.util.LinkedList;

import ar.uba.fi.jparticulas.model.Ambiente;
import ar.uba.fi.jparticulas.model.Vector;
import ar.uba.fi.jparticulas.model.movil.Movil;
import ar.uba.fi.jparticulas.view.GameView;
import ar.uba.fi.jparticulas.view.movil.MovilView;

/**
 * Motor del juego: se encarga de ejecutar cada ciclo del mismo, actualizando y dibujando todos los
 * objetos, así como de derivar los eventos a los controladores que correspondan.
 * 
 * @author mmazzei
 */
public class GameEngine {
	// TODO (mmazzei) - Que sea configurable todo lo que aquí está en constantes
	// Tasa de refresco
	public static final int DESIRED_FRAME_RATE = 96;
	public static final int dt = 1000 / DESIRED_FRAME_RATE;

	// TODO (mmazzei) - Considerar si no es preferible que estas colecciones contengan sólo objetos
	// del modelo.
	// PROS: Tiene más sentido que el engine manipule sólo modelos, no vistas.
	// CONTRAS: Se derrocha espacio almacenando dos veces las referencias a cada uno (una desde el
	// engine y otra desde la vista)
	// Elementos del juego
	private ArrayList<MovilView> colliderParticles;
	private ArrayList<MovilView> nonColliderParticles;
	private ArrayList<MovilView> obstacles;
	private LinkedList<MovilView> temporalParticles;
	private Ambiente ambiente;

	// Tiempos para debug
	private long fps;

	/** Indica si la simulación está en pausa. */
	private boolean isPaused;
	/** Indica el instante de tiempo en el que se pausó el juego. */
	private long pausedOnTime;
	/** Cantidad de tiempo transcurrido en pausa. Es reseteado en el primer ciclo luego de la pausa. */
	private long pausedTime;

	// Delegados
	private GameView view;
	private GameController controller;

	/**
	 * Lleva a cabo todas las inicializaciones necesarias para comenzar el juego.
	 * 
	 * @param view
	 *            Objeto que se encargará de la presentación.
	 * @param controller
	 *            Objeto que se encargará de la interacción con el usuario.
	 */
	public GameEngine(GameView view, GameController controller) {
		this.obstacles = new ArrayList<MovilView>();
		this.colliderParticles = new ArrayList<MovilView>();
		this.nonColliderParticles = new ArrayList<MovilView>();
		this.temporalParticles = new LinkedList<MovilView>();
		this.ambiente = new Ambiente(0, new Vector(0, 0), 0);
		this.view = view;
		this.controller = controller;
	}

	/*-------------------------------------------------------------------------
	 *                             GETTERS Y SETTERS
	-------------------------------------------------------------------------*/
	/**
	 * @return Todas aquellas partículas del escenario para las que deben controlarse las colisiones
	 *         contra los obstáculos.
	 */
	public ArrayList<MovilView> getColliderParticles() {
		return colliderParticles;
	}

	public void setColliderParticles(ArrayList<MovilView> colliderParticles) {
		this.colliderParticles = colliderParticles;
	}

	/** @return Las partículas del escenario para las que no se controlan colisiones. */
	public ArrayList<MovilView> getNonColliderParticles() {
		return nonColliderParticles;
	}

	public void setNonColliderParticles(ArrayList<MovilView> nonColliderParticles) {
		this.nonColliderParticles = nonColliderParticles;
	}

	/**
	 * @return Los obstáculos definidos para el escenario. Son partículas para las que no se
	 *         actualiza la posición ni controlan colisiones.
	 */
	public ArrayList<MovilView> getObstacles() {
		return obstacles;
	}

	public void setObstacles(ArrayList<MovilView> obstacles) {
		this.obstacles = obstacles;
	}

	/** @return La configuración de ambiente del escenario. */
	public Ambiente getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(Ambiente ambiente) {
		this.ambiente = ambiente;
	}

	/**
	 * Partículas para las que se controlan colisiones y son eliminadas cuando quedan fijas.
	 * 
	 * @see ar.uba.fi.jparticulas.model.movil.Movil#isFixed()
	 */
	public LinkedList<MovilView> getTemporalParticles() {
		return temporalParticles;
	}

	public void setTemporalParticles(LinkedList<MovilView> temporalParticles) {
		this.temporalParticles = temporalParticles;
	}

	/** @return true si el engine está pausado. */
	public boolean isPaused() {
		return isPaused;
	}

	/*-------------------------------------------------------------------------
	 *                                LÓGICA
	-------------------------------------------------------------------------*/
	/**
	 * Deja en pausa al engine. A partir de que este momento, sólo se invocará a los delegados de
	 * control y vista en el ciclo de ejecución.
	 * 
	 * @see #isPaused()
	 * @see #unpause()
	 */
	public void pause() {
		if (!isPaused) {
			isPaused = true;
			pausedOnTime = System.currentTimeMillis();
		}
	}

	/**
	 * Continúa con la ejecución del ciclo para los objetos del modelo si el motor está en pausa.
	 * 
	 * @see #pause()
	 */
	public void unpause() {
		if (isPaused) {
			isPaused = false;
			pausedTime = System.currentTimeMillis() - pausedOnTime;
		}
	}

	/**
	 * Ciclo principal del programa, en el que se actualizan los siguientes pasos por cada
	 * iteración:
	 * <ol>
	 * <li>Actualización del movimiento de todos los objetos móviles ({@link #colliderParticles},
	 * {@link #nonColliderParticles}, {@link #temporalParticles}).</li>
	 * <li>Detección de colisiones de cada objeto contra el escenario ({@link #obstacles}).</li>
	 * <li>Solicitud al delegado de presentación para renderizar la vista.</li>
	 * <li>Solicitud al delegado de control para ejecutar órdenes del usuario.</li>
	 * </ol>
	 */
	public void run() throws InterruptedException {
		long lastLoopTime = System.currentTimeMillis() - 1;
		long currentTime;
		double delta;
		pausedTime = 0;
		while (true) {
			if (!isPaused) {
				// Obtengo el tiempo transcurrido desde que se ejecutó el ciclo por última vez
				currentTime = System.currentTimeMillis();
				delta = currentTime - lastLoopTime - pausedTime;
				fps = (long) (1000 / delta);

				// Inicializo los temporizadores de este ciclo
				lastLoopTime = currentTime;
				pausedTime = 0;

				// Actualizo los objetos
				updateAllPositions(delta);
			}

			// Renderizo la presentación
			view.update(this);

			// Interacción con el usuario
			controller.update(this);

			// Se detiene la ejecución para lograr la cantidad adecuada de fps
			// liberando al procesador si sobra tiempo.
			Thread.sleep(dt);
		}
	}

	/**
	 * Actualiza la posición de cada {@link #colliderParticles}, {@link #nonColliderParticles} y
	 * {@link #temporalParticles} (siempre y cuando no estén detenidos). Además se encarga de
	 * controlar las colisiones cotra los {@link #obstacles}.
	 * 
	 * @param delta
	 *            Tiempo transcurrido desde la última actualización (milisegundos).
	 */
	private void updateAllPositions(double delta) {
		// Actualizo la posición de las partículas colisionables
		for (MovilView particle : colliderParticles) {
			Movil movil = particle.getMovil();

			// Ignoro las partículas fijas
			if (movil.isFixed()) {
				continue;
			}

			// Actualizo la posición teniendo en cuenta el tiempo que transcurrió desde la
			// última vez (y pasando todo a segundos.
			movil.updatePosition(delta / 1000);
			controlCollisions(movil);
		}

		// Actualizo la posición de las partículas colisionables
		int tmpSize = temporalParticles.size();
		for (int i = 0; i < tmpSize; i++) {
			MovilView particle = temporalParticles.removeFirst();
			Movil movil = particle.getMovil();

			// Ignoro las partículas fijas, descartándolas de la colección
			if (movil.isFixed()) {
				continue;
			}

			// Actualizo la posición teniendo en cuenta el tiempo que transcurrió desde la
			// última vez (y pasando todo a segundos.
			movil.updatePosition(delta / 1000);
			controlCollisions(movil);
			temporalParticles.addLast(particle);
		}

		// Actualizo la posición de las partículas colisionables
		for (MovilView particle : nonColliderParticles) {
			Movil movil = particle.getMovil();

			// Ignoro las partículas fijas
			if (movil.isFixed()) {
				continue;
			}

			// Actualizo la posición teniendo en cuenta el tiempo que transcurrió desde la
			// última vez (y pasando todo a segundos.
			movil.updatePosition(delta / 1000);
		}
	}

	/**
	 * Detecta si el móvil colisionó contra alguno de los {@link #obstacles} y ejecuta la acción que
	 * corresponda, en caso de que sí:
	 * <ul>
	 * <li>La partícula rebota si colisionó.</li>
	 * <li>Si la velocidad tras el rebote es inferior a {@link #UMBRAL_PRECISION}, se detiene.</li>
	 * </ul>
	 */
	private void controlCollisions(Movil movil) {
		for (MovilView obstacle : obstacles) {
			if (obstacle.getMovil().collide(movil)) {
				// Una partícula sólo puede colisionar con un obstáculo por vez
				// Esto permite ahorrarse tener que procesar las demás colisiones
				// break;
				// Lo comenté porque, si una partícula chocaba contra una esquina formada por la
				// unión de dos rectángulos, traspasaba a uno de ellos
			}
		}
	}
}
