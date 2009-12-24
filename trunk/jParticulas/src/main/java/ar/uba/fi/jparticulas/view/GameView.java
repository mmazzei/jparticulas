package ar.uba.fi.jparticulas.view;

import ar.uba.fi.jparticulas.controller.GameEngine;

/**
 * Define el comportamiento necesario para presentar el modelo al usuario.
 * 
 * @author mmazzei
 */
public interface GameView {
	/**
	 * En cada uno de los ciclos ejecutados por el {@link GameEngine}, se invoca a este método,
	 * donde se implementa la representación en algún dispositivo de salida de los elementos del
	 * modelo.
	 * 
	 * @param engine
	 *            Para obtener los elementos e información a mostrar.
	 * @see GameEngine#run()
	 */
	public void update(GameEngine engine);
}
