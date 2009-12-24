package ar.uba.fi.jparticulas.view.movil.complex;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import ar.uba.fi.jparticulas.model.movil.Movil;
import ar.uba.fi.jparticulas.model.movil.complex.Firework;
import ar.uba.fi.jparticulas.view.movil.MovilView;

/**
 * Renderiza el proyectil de un {@link Firework} con un cuadrado de píxeles. Al explotar renderiza
 * cada fragmento como un pixel que se difumina a medida que llega a su límite de tiempo de vida.
 * 
 * @author mmazzei
 */
public class FireworkView extends MovilView {
	private static final short proyectilSize = 2;
	private static final short fragmentSize = 1;
	private final double fragmentLife;

	public FireworkView(Movil movil) {
		super(movil);
		this.fragmentLife = ((Firework) movil).getFragmentsLifeTime();
	}

	@Override
	public void print(Graphics2D gr) {
		Firework fr = (Firework) this.getMovil();

		if (!fr.isExploded()) {
			Rectangle2D draw = new Rectangle2D.Double(movil.getPosition().getX() - proyectilSize, movil.getPosition()
					.getY()
					- proyectilSize, proyectilSize, proyectilSize);
			gr.setColor(this.getColor());
			gr.fill(draw);
		} else if (!fr.isFixed()) {
			// Imprimo los fragmentos del proyectil detonado como píxeles difuminados
			Movil[] fragments = fr.getFragments();
			Composite auxComposite = gr.getComposite();

			// Siendo que el porcentaje de difuminación es proporcional al tiempo que le queda de
			// vida
			gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC,
					(float) (fr.getFragmentsLifeTime() / fragmentLife)));

			gr.setColor(this.getColor());
			for (int i = 0; i < fragments.length; i++) {
				Rectangle2D draw = new Rectangle2D.Double(fragments[i].getPosition().getX() - fragmentSize,
						fragments[i].getPosition().getY() - fragmentSize, fragmentSize, fragmentSize);
				gr.fill(draw);
			}

			// Dejo a gr en su estado original
			gr.setComposite(auxComposite);
		}
	}

	@Override
	public void print(Graphics2D gr, double offsetX, double offsetY) {
		throw new RuntimeException("Not implemented yet");
	}
}
