package ar.uba.fi.jparticulas.model;

/**
 * Clase encargada de proveer mecanismos que ayuden en la detección de colisiones.
 * 
 * @author mmazzei
 */
public class PhysicsHelper {
	/** Determina la cantidad de momentum que se conserva tras una colisión. */
	// TODO (mmazzei) - Esto DEBE ser un atributo del objeto.
	public final static double COEFICIENTE_ELASTICIDAD = -0.8;

	/** @return true si el punto está contenido en el interior del rectángulo parametrizado. */
	public static boolean contains(Vector point, double x1, double y1, double width, double height) {
		double relativeX = point.getX() - x1;
		double relativeY = point.getY() - y1;
		return (relativeX > 0) && (relativeX < width) && (relativeY > 0) && (relativeY < height);
	}

	/**
	 * @param x1
	 *            Coordenada x de un punto perteneciente a la función lineal a evaluar.
	 * @param y1
	 *            Coordenada y de un punto perteneciente a la función lineal a evaluar.
	 * @param m1
	 *            Pendiente de la función lineal.
	 * @param x2a
	 *            Coordenada x del primer punto del segmento.
	 * @param y2a
	 *            Coordenada y del primer punto del segmento.
	 * @param x2b
	 *            Coordenada x del segundo punto del segmento.
	 * @param y2b
	 *            Coordenada y del segundo punto del segmento.
	 * @return true si la recta parametrizada por x1, y1 y m1 tiene una intersección con el segmento
	 *         (x2a;y2a) -> (x2b;y2b).
	 */
	public static boolean intersectWithSegment(double x1, double y1, double m1, double x2a, double y2a, double x2b,
			double y2b) {
		if (x2b == x2a) {
			throw new RuntimeException("Pendiente infinita. No se soporta un segmento vertical.");
		}

		double m2 = (y2b - y2a) / (x2b - x2a);
		Vector intersection = getIntersection(x1, y1, m1, x2a, y2a, m2);
		return ((intersection.getX() > x2a) && (intersection.getX() < x2b));
	}

	/**
	 * @return El punto en que se intersectan las rectas cuyas ecuaciones son:
	 *         <ul>
	 *         <li> <code> y = m1 * (x - x1) + y1;</code></li>
	 *         <li> <code>y = m2 * (x - x2) + y2; </code></li>
	 *         </ul>
	 */
	private static Vector getIntersection(double x1, double y1, double m1, double x2, double y2, double m2) {
		// Calculo las coordenadas de la intersección
		double ix, iy;
		if (Double.isInfinite(m1)) {
			// Caso en que la primer recta es vertical
			ix = x1;
			iy = m2 * (ix - x2) + y2;
		} else {
			// Sistema de dos ecuaciones lineales con dos incógnitas
			ix = (m1 * x1 - m2 * x2 + y2 - y1) / (m1 - m2);
			iy = m1 * (ix - x1) + y1;
		}

		return new Vector(ix, iy);
	}
}
