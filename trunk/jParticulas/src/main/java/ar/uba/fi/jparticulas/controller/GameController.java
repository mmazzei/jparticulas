package ar.uba.fi.jparticulas.controller;

import ar.uba.fi.jparticulas.controller.io.KeyboardManager;

/**
 * Interfaz que define el comportamiento necesario para manipular la interacción del usuario con el
 * modelo.
 * 
 * @author mmazzei
 */
public interface GameController {
	/**
	 * En cada uno de los ciclos ejecutados por el {@link GameEngine}, se invoca a este método,
	 * donde se implementa cualquier interacción entre el usuario y el modelo.
	 * 
	 * @param engine
	 *            Para obtener o añadir componentes del modelo y manipularlos.
	 * @see GameEngine#run()
	 * @see KeyboardManager
	 */
	public void update(GameEngine engine);
}
