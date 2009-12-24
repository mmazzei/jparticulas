package ar.uba.fi.jparticulas.controller.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Abstracción del teclado de una computadora que permite conocer a cada instante las teclas que se
 * encuentran presionadas, así como saber si alguna es presionada en un momento.
 * 
 * @author mmazzei
 */
public final class KeyboardManager implements KeyListener {
	/**
	 * Estado de cada una de las teclas mapeables por esta clase. Por ahora no soporta las
	 * siguientes:
	 * <ul>
	 * <li>VK_F13</li>
	 * <li>VK_F14</li>
	 * <li>VK_F15</li>
	 * <li>VK_F16</li>
	 * <li>VK_F17</li>
	 * <li>VK_F18</li>
	 * <li>VK_F19</li>
	 * <li>VK_F20</li>
	 * <li>VK_F21</li>
	 * <li>VK_F22</li>
	 * <li>VK_F23</li>
	 * <li>VK_F24</li>
	 * <li>VK_AT</li>
	 * <li>VK_COLON</li>
	 * <li>VK_CIRCUMFLEX</li>
	 * <li>VK_DOLLAR</li>
	 * <li>VK_EURO_SIGN</li>
	 * <li>VK_EXCLAMATION_MARK</li>
	 * <li>VK_INVERTED_EXCLAMATION_MARK</li>
	 * <li>VK_LEFT_PARENTHESIS</li>
	 * <li>VK_NUMBER_SIGN</li>
	 * <li>VK_PLUS</li>
	 * <li>VK_RIGHT_PARENTHESIS</li>
	 * <li>VK_UNDERSCORE</li>
	 * <li>VK_WINDOWS</li>
	 * <li>VK_CONTEXT_MENU</li>
	 * <li>VK_FINAL</li>
	 * <li>VK_CONVERT</li>
	 * <li>VK_NONCONVERT</li>
	 * <li>VK_ACCEPT</li>
	 * <li>VK_MODECHANGE</li>
	 * <li>VK_KANA</li>
	 * <li>VK_KANJI</li>
	 * <li>VK_ALPHANUMERIC</li>
	 * <li>VK_KATAKANA</li>
	 * <li>VK_HIRAGANA</li>
	 * <li>VK_FULL_WIDTH</li>
	 * <li>VK_HALF_WIDTH</li>
	 * <li>VK_ROMAN_CHARACTERS</li>
	 * <li>VK_ALL_CANDIDATES</li>
	 * <li>VK_PREVIOUS_CANDIDATE</li>
	 * <li>VK_CODE_INPUT</li>
	 * <li>VK_JAPANESE_KATAKANA</li>
	 * <li>VK_JAPANESE_HIRAGANA</li>
	 * <li>VK_JAPANESE_ROMAN</li>
	 * <li>VK_KANA_LOCK</li>
	 * <li>VK_INPUT_METHOD_ON_OFF</li>
	 * <li>VK_CUT</li>
	 * <li>VK_COPY</li>
	 * <li>VK_PASTE</li>
	 * <li>VK_UNDO</li>
	 * <li>VK_AGAIN</li>
	 * <li>VK_FIND</li>
	 * <li>VK_PROPS</li>
	 * <li>VK_STOP</li>
	 * <li>VK_COMPOSE</li>
	 * <li>VK_ALT_GRAPH</li>
	 * <li>VK_BEGIN</li>
	 * <li>VK_UNDEFINED</li>
	 * </ul>
	 */
	private boolean[] key_state_down = new boolean[256];

	private boolean keyPressed = false;
	private boolean keyReleased = false;

	private static KeyboardManager instance = new KeyboardManager();

	private KeyboardManager() {
	}

	/** @return La única instancia del KeyboardManager */
	public static KeyboardManager getInstance() {
		return instance;
	}

	/**
	 * @param key
	 *            Estado de la tecla consultada.
	 * @return true sólo si la tecla está siendo presionada.
	 */
	public boolean isKeyDown(int key) {
		return key_state_down[key];
	}

	/** @return true sólo si hay alguna tecla presionada */
	public boolean isAnyKeyDown() {
		return keyPressed;
	}

	/**
	 * @return true sólo si hay alguna tecla que acaba de ser soltada desde el último
	 *         {@link #update()}.
	 */
	public boolean isAnyKeyUp() {
		return keyReleased;
	}

	/** Debe invocárselo cuando desee conocerse en algún intervalo de tiempo se soltó alguna tecla. */
	public void reset() {
		keyReleased = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Nada para hacer
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() >= 0 && e.getKeyCode() < 256) {
			key_state_down[e.getKeyCode()] = true;
			keyPressed = true;
			keyReleased = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() >= 0 && e.getKeyCode() < 256) {
			key_state_down[e.getKeyCode()] = false;
			keyPressed = false;
			keyReleased = true;
		}
	}

}