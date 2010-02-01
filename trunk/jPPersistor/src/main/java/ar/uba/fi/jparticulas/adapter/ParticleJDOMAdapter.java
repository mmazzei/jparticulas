package ar.uba.fi.jparticulas.adapter;

import org.jdom.DataConversionException;
import org.jdom.Element;

import ar.uba.fi.jparticulas.model.movil.Movil;
import ar.uba.fi.jparticulas.model.movil.Particle;

/**
 * Se encarga de ampliar la interfaz de la clase {@link Particle} para permitir su persistencia
 * mediante JDOM.
 * 
 * @author mmazzei
 */
public class ParticleJDOMAdapter implements Persistible {
	public static final String TYPE_NAME = "particle";
	private Particle movil;

	public ParticleJDOMAdapter(Particle movil) {
		this.movil = movil;
	}

	public ParticleJDOMAdapter(Element element) throws DataConversionException {
		double mass = element.getAttribute("mass").getDoubleValue();
		double elasticity = element.getAttribute("elasticity").getDoubleValue();
		boolean isFixed = element.getAttribute("isFixed").getBooleanValue();

		VectorPersistibleAdapter position = new VectorPersistibleAdapter(element.getChild("position"));
		this.movil = new Particle(mass, position.getVector());
		this.movil.setElasticity(elasticity);
		this.movil.setFixed(isFixed);

		if (!isFixed) {
			VectorPersistibleAdapter velocity = new VectorPersistibleAdapter(element.getChild("velocity"));
			VectorPersistibleAdapter acceleration = new VectorPersistibleAdapter(element.getChild("acceleration"));
			this.movil.setVelocity(velocity.getVector());
			this.movil.setAcceleration(acceleration.getVector());
		}
	}

	@Override
	public Movil getMovil() {
		return this.movil;
	}

	@Override
	public String getType() {
		return TYPE_NAME;
	}

	@Override
	public Element save(String name) {
		Element element = new Element(name);
		element.setAttribute("mass", this.movil.getMass() + "");
		element.setAttribute("elasticity", this.movil.getElasticity() + "");
		element.setAttribute("isFixed", this.movil.isFixed() + "");

		VectorPersistibleAdapter position = new VectorPersistibleAdapter(this.movil.getPosition());
		element.addContent(position.save("position"));

		// No es necesario almacenar datos de movimiento para partículas fijas.
		if (!movil.isFixed()) {
			VectorPersistibleAdapter velocity = new VectorPersistibleAdapter(this.movil.getVelocity());
			element.addContent(velocity.save("velocity"));
			VectorPersistibleAdapter acceleration = new VectorPersistibleAdapter(this.movil.getAcceleration());
			element.addContent(acceleration.save("acceleration"));
		}

		return element;
	}

}
