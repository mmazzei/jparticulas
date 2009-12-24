package ar.uba.fi.jparticulas.view.movil;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import ar.uba.fi.jparticulas.model.movil.Particle;

public class ParticleView extends MovilView {
	private final short particleSize;

	/**
	 * @param size
	 *            Tamaño con que se dibujará la partícula.
	 */
	public ParticleView(Particle particle, short size) {
		super(particle);
		particleSize = size;
	}

	@Override
	public void print(Graphics2D gr) {
		Rectangle2D draw = new Rectangle2D.Double(movil.getPosition().getX() - particleSize, movil.getPosition().getY()
				- particleSize, particleSize, particleSize);
		gr.setColor(this.getColor());
		gr.fill(draw);
	}

	public void print(Graphics2D gr, double offsetX, double offsetY) {
		throw new RuntimeException("Not implemented yet");
	}
}
