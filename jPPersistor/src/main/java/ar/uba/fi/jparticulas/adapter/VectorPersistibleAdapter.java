package ar.uba.fi.jparticulas.adapter;

import org.jdom.DataConversionException;
import org.jdom.Element;

import ar.uba.fi.jparticulas.model.Vector;

/**
 * Se encarga de adaptar vectores para que sean persistibles mediante JDOM.
 * 
 * @author mmazzei
 */
public class VectorPersistibleAdapter {
	// Vector adaptado
	private Vector vector;

	/**
	 * @param vector
	 *            Vector a adaptar.
	 */
	public VectorPersistibleAdapter(Vector vector) {
		this.vector = vector;
	}

	/**
	 * @param element
	 *            Elemento de un XML donde se registran los datos del vector a adaptar.
	 * @throws DataConversionException
	 */
	public VectorPersistibleAdapter(Element element) throws DataConversionException {
		double x = element.getAttribute("x").getDoubleValue();
		double y = element.getAttribute("y").getDoubleValue();
		this.vector = new Vector(x, y);
	}

	/**
	 * @param name
	 *            Nombre del atributo representado por el vector.
	 * @return El elemento XML en el que se registran los datos del vector.
	 */
	public Element save(String name) {
		Element element = new Element(name);
		element.setAttribute("x", this.getVector().getX() + "");
		element.setAttribute("y", this.getVector().getY() + "");
		return element;
	}

	/** @return El vector cuya interfaz está siendo modificada. */
	public Vector getVector() {
		return vector;
	}
}
