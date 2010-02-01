package ar.uba.fi.jparticulas.adapter;

import org.jdom.Element;

import ar.uba.fi.jparticulas.model.movil.Movil;

/**
 * Define la interfaz a brindar por cualquier clase que se encargue de persistir/hidratar un móvil.
 * 
 * @author mmazzei
 */
public interface Persistible {
	/**
	 * @param name
	 *            El nombre de la información que el móvil representa.
	 * @return El elemento de un documento XML que contendrá la información persistida.
	 */
	public Element save(String name);

	/**
	 * @return El móvil cuya interfaz modifica este Persistible.
	 */
	public Movil getMovil();

	/** @return Una cadena que identifica al tipo del móvil. */
	public String getType();
}
