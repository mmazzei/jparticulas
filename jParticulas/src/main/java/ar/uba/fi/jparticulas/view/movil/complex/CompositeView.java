package ar.uba.fi.jparticulas.view.movil.complex;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import ar.uba.fi.jparticulas.model.movil.Movil;
import ar.uba.fi.jparticulas.model.movil.complex.Composite;
import ar.uba.fi.jparticulas.view.Printable;
import ar.uba.fi.jparticulas.view.ViewFactory;
import ar.uba.fi.jparticulas.view.movil.MovilView;

/**
 * Utilizado para renderizar un composite como la renderización de todos sus componentes.
 * 
 * @author mmazzei
 */
public class CompositeView extends MovilView {
	private Printable[] elements;

	public CompositeView(Movil movil) {
		super(movil);
		Composite comp = (Composite) movil;
		Movil[] movilElements = comp.getElements();
		this.elements = new Printable[movilElements.length];
		for (short i = 0; i < movilElements.length; i++) {
			this.elements[i] = ViewFactory.create(movilElements[i]);
		}
	}

	@Override
	public void print(Graphics2D gr) {
		this.print(gr, 0, 0);
	}

	@Override
	public void print(Graphics2D gr, double offsetX, double offsetY) {
		// Imprimo el borde (debug)
		Composite comp = (Composite) movil;
		Rectangle2D border = new Rectangle2D.Double(comp.getPosition().getX() + offsetX - 1, comp.getPosition().getY()
				+ offsetY - 1, comp.getWidth() + 1, comp.getHeight() + 1);
		gr.setColor(Color.yellow);
		gr.draw(border);

		// Imprimo todos los componentes
		for (short i = 0; i < elements.length; i++) {
			elements[i].setColor(this.getColor());
			elements[i].print(gr, comp.getPosition().getX() + offsetX, comp.getPosition().getY() + offsetY);
		}
	}
}
