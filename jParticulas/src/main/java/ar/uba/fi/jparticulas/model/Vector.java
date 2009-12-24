package ar.uba.fi.jparticulas.model;

/**
 * Representa un vector en el espacio bidimensional.
 * 
 * @author mmazzei
 */
public class Vector {
	private double x;
	private double y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/*-------------------------------------------------------------------------
	 *							GETTERS Y SETTERS
	 ------------------------------------------------------------------------*/
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	/*-------------------------------------------------------------------------
	 *								LÓGICA
	 ------------------------------------------------------------------------*/
	/** Copia las coordenadas de v. */
	public void copyFrom(Vector v) {
		this.x = v.x;
		this.y = v.y;
	}

	/** Suma componente a componente. */
	public void addEquals(Vector v) {
		this.x += v.x;
		this.y += v.y;
	}

	/** Resta componente a componente. */
	public void substractEquals(Vector v) {
		this.x -= v.x;
		this.y -= v.y;
	}

	/** Producto por un escalar. */
	public void multiplyEquals(double e) {
		this.x *= e;
		this.y *= e;
	}

	/** @return Un nuevo vector producto de sumar ambos. */
	public Vector add(Vector v) {
		return new Vector(x + v.x, y + v.y);
	}

	/** @return Un nuevo vector producto de restar ambos. */
	public Vector substract(Vector v) {
		return new Vector(x + v.x, y + v.y);
	}

	/** @return Un nuevo vector producto de multiplicar éste por un escalar. */
	public Vector multiply(double e) {
		return new Vector(x * e, y * e);
	}

	/** @return El producto escalar de ambos vectores. */
	public double dotMultiply(Vector v) {
		return this.x * v.x + this.y * v.y;
	}

	/** Rota el vector el ángulo indicado. */
	public void rotate(double angle) {
		double xaux = x;
		this.x = x * Math.cos(angle) + y * Math.sin(angle);
		this.y = -xaux * Math.sin(angle) + y * Math.cos(angle);
	}

	/** @return La norma 2 del vector. */
	public double getNorma() {
		return Math.sqrt(x * x + y * y);
	}

	/** @return La pendiente respecto a (0; 0). */
	public double getSlope() {
		return y / x;
	}

	/** @return Un vector con las mismas coordenadas que éste. */
	public Vector clone() {
		return new Vector(x, y);
	}
}
