package ar.uba.fi.jparticulas.view.movil;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import ar.uba.fi.jparticulas.model.movil.Rectangle;

/**
 * Renderiza un rectángulo con un área plana con píxeles del mismo color (sin textura ni
 * transparencias).
 * 
 * @author mmazzei
 */
public class RectangleView extends MovilView {
	public RectangleView(Rectangle rectangle) {
		super(rectangle);
	}

	@Override
	public void print(Graphics2D gr) {
		print(gr, 0, 0);
	}

	@Override
	public void print(Graphics2D gr, double offsetX, double offsetY) {
		Rectangle rect = (Rectangle) movil;
		Rectangle2D draw = new Rectangle2D.Double(rect.getPosition().getX() + offsetX, rect.getPosition().getY()
				+ offsetY, rect.getWidth(), rect.getHeight());
		gr.setColor(this.getColor());
		gr.fill(draw);
	}
}
