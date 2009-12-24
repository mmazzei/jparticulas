package ar.uba.fi.jparticulas.model.movil.complex;

import ar.uba.fi.jparticulas.model.Vector;
import ar.uba.fi.jparticulas.model.movil.Movil;

/**
 * Representa a un objeto que está compuesto por varios otros.<br />
 * Las posiciones de cada elemento son relativas a la del composite, que no es el centro de masa
 * sino que su punto superior izquierdo.<br />
 * La masa de un composite es la suma de todas las masas de sus componentes.
 * 
 * @author mmazzei
 */
public class Composite extends Movil {
	private Movil[] elements;
	private short count = 0;
	private double width = 0;
	private double height = 0;

	public Composite(Vector position, int elementsSz) {
		super(0, position);
		this.elements = new Movil[elementsSz];
	}

	/*-------------------------------------------------------------------------
	 *							GETTERS Y SETTERS
	 ------------------------------------------------------------------------*/
	/** Fija al compuesto y a todos sus elementos. */
	@Override
	public void setFixed(boolean isFixed) {
		super.setFixed(isFixed);
		for (int i = 0; i < elements.length; i++) {
			elements[i].setFixed(isFixed);
		}
	}

	/** @return Los elementos que componen al composite. */
	public Movil[] getElements() {
		return elements;
	}

	/**
	 * El ancho de un composite se considera como la diferencia entre la posición del mismo y el
	 * mayor MaxX de sus elementos.<br />
	 * Esto significa que, si se trazara un rectángulo desde la posición X,Y del {@link CompositeView}
	 * con el ancho y alto obtenidos de {@link #getWidth()} y {@link #getHeigth()}, se abarcaría a
	 * todas las figuras que lo componen.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * El alto de un composite se considera como la diferencia entre la posición del mismo y el
	 * mayor MaxY de sus elementos.<br />
	 * Esto significa que, si se trazara un rectángulo desde la posición X,Y del {@link CompositeView}
	 * con el ancho y alto obtenidos de {@link #getWidth()} y {@link #getHeigth()}, se abarcaría a
	 * todas las figuras que lo componen.
	 */
	public double getHeight() {
		return height;
	}

	@Override
	public double getMaxX() {
		return this.getPosition().getX() + this.getWidth();
	}

	@Override
	public double getMaxY() {
		return this.getPosition().getY() + this.getHeight();
	}

	/*-------------------------------------------------------------------------
	 *								LÓGICA
	 ------------------------------------------------------------------------*/
	/**
	 * Añade un elemento al CompositeView sólo si no se ha llegado a la cantidad máxima, definida en el
	 * constructor.<br />
	 * La posición del elemento es relativa al punto superior izquierdo del composite (deben ser
	 * ambas coordenadas positivas).<br />
	 */
	public void addElement(Movil element) {
		if (count >= elements.length) {
			throw new RuntimeException("Se superó el límite de elementos de un CompositeView.");
		}

		this.elements[count++] = element;
		this.setMass(this.getMass() + element.getMass());

		// Se compara contra width y height debido a que, la del componente,
		// es una posición relativa.
		if (element.getMaxX() > this.width) {
			this.width = element.getMaxX();
		}
		if (element.getMaxY() > this.height) {
			this.height = element.getMaxY();
		}
	}

	@Override
	public boolean collide(Movil m) {
		if (!this.isFixed()) {
			throw new RuntimeException("El mecanismo de colisiones sólo soporta un móvil contra un fijo.");
		}
		// Sólo si la partícula se encuentra dentro del área del composite evaluaré el impacto con
		// sus componentes
		if ((m.getPosition().getX() < this.getPosition().getX()) || (m.getPosition().getX() > this.getMaxX())
				|| (m.getPosition().getY() < this.getPosition().getY()) || (m.getPosition().getY() > this.getMaxY())) {
			// Si p no se encuentra dentro del rectángulo
			return false;
		}

		// Antes de comenzar le quito el offset del composite a la partícula
		m.getPosition().substractEquals(this.getPosition());
		m.getPreviousPosition().substractEquals(this.getPosition());

		boolean wasCollide = false;
		// TODO (mmazzei) - Descomentar. El flag es utilizado para no ejecutar evaluaciones
		// innecesarias. Se lo comentó debido a que, cuando había dos rectangle juntos, si una
		// partícula colisionaba contra uno de ellos en el límite contra el otro, traspasaba al
		// otro. Corregir eso.
		for (int i = 0; (i < elements.length) /* && (!wasCollide) */; i++) {
			wasCollide = elements[i].collide(m);
		}

		// Le agrego a la partícula el offset que le había quitado
		m.getPosition().addEquals(this.getPosition());
		m.getPreviousPosition().addEquals(this.getPosition());
		return wasCollide;
	}

	@Override
	protected void wasCollide(Movil obstacle, boolean horizontalCollision) {
		throw new RuntimeException("No se ha implementado el mecanismo de CompositeView móviles.");
	}
}
