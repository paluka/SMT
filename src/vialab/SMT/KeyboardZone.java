/**
 * 
 */
package vialab.SMT;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * @author Zach
 * 
 */
public class KeyboardZone extends Zone {

	private int MODIFIERS = 0;

	private Component keyboardComponent = new Component() {
		private static final long serialVersionUID = -3237916182106172342L;
	};

	private class KeyZone extends ButtonZone {
		private Keys key;

		public KeyZone(int x, int y, int width, int height, Keys key) {
			super(x, y, width, height, key.text);
			this.key = key;
		}

		@Override
		public void draw() {
			super.draw();
			// make sure modifiers have the correct setting as they act
			// differently than normal keys and should be unset even without a
			// touchUp event, although really just a hack, as touchUp should be
			// generated whenever a touch is unassigned from a zone
			if (isButtonDown()) {
				modifierDown();
			}
			else {
				modifierUp();
			}

		}

		@Override
		public void touchDown(Touch touch) {
			if (!isButtonDown()) {
				keyDown();
			}
			super.touchDown(touch);
		}

		private void keyDown() {
			char k = key.keyChar;
			// if not undefined char and shift is on, set to upper case
			if (key.keyChar != KeyEvent.CHAR_UNDEFINED && (MODIFIERS >> 6) % 2 == 1) {
				k = Character.toUpperCase(key.keyChar);
			}
			// send key press using KeyEvent to listeners
			for (KeyListener l : keyListeners) {
				l.keyPressed(new KeyEvent(keyboardComponent, KeyEvent.KEY_PRESSED, System
						.currentTimeMillis(), MODIFIERS, key.keyCode, k));
			}

			modifierDown();
		}

		private void modifierDown() {
			switch (key.keyCode) {
			case KeyEvent.VK_SHIFT:
				if ((MODIFIERS >> 6) % 2 == 0) {
					// add modifier only if not down already
					MODIFIERS += KeyEvent.SHIFT_DOWN_MASK;

				}
				break;
			case KeyEvent.VK_CONTROL:
				if ((MODIFIERS >> 7) % 2 == 0) {
					// add modifier only if not down already
					MODIFIERS += KeyEvent.CTRL_DOWN_MASK;

				}
				break;
			case KeyEvent.VK_ALT:
				if ((MODIFIERS >> 8) % 2 == 0) {
					// add modifier only if not down already
					MODIFIERS += KeyEvent.ALT_DOWN_MASK;

				}
				break;
			default:
				break;
			}
		}

		@Override
		public void touchUp(Touch touch) {
			if (isButtonDown()) {
				keyUp();
			}
			super.touchUp(touch);
		}

		private void keyUp() {
			char k = key.keyChar;
			// if not undefined char and shift is on, set to upper case
			if (key.keyChar != KeyEvent.CHAR_UNDEFINED && (MODIFIERS >> 6) % 2 == 1) {
				k = Character.toUpperCase(key.keyChar);
			}
			// send key release and typed using KeyEvent to listeners
			for (KeyListener l : keyListeners) {
				l.keyReleased(new KeyEvent(keyboardComponent, KeyEvent.KEY_RELEASED, System
						.currentTimeMillis(), MODIFIERS, key.keyCode, k));
				if (!key.isModifier) {
					l.keyTyped(new KeyEvent(keyboardComponent, KeyEvent.KEY_TYPED, System
							.currentTimeMillis(), MODIFIERS, KeyEvent.VK_UNDEFINED, k));
				}
			}

			modifierUp();
		}

		private void modifierUp() {
			switch (key.keyCode) {
			case KeyEvent.VK_SHIFT:
				if ((MODIFIERS >> 6) % 2 == 1) {
					// remove modifier only if down already
					MODIFIERS -= KeyEvent.SHIFT_DOWN_MASK;

				}
				break;
			case KeyEvent.VK_CONTROL:
				if ((MODIFIERS >> 7) % 2 == 1) {
					// remove modifier only if down already
					MODIFIERS -= KeyEvent.CTRL_DOWN_MASK;

				}
				break;
			case KeyEvent.VK_ALT:
				if ((MODIFIERS >> 8) % 2 == 1) {
					// remove modifier only if down already
					MODIFIERS -= KeyEvent.ALT_DOWN_MASK;

				}
				break;
			default:
				break;
			}
		}
	}

	private static final int DEFAULT_HEIGHT = 250;
	private static final int DEFAULT_WIDTH = 500;

	private enum Keys {
		KEY_1('1', KeyEvent.VK_1, false), KEY_2('2', KeyEvent.VK_2, false), KEY_3('3',
				KeyEvent.VK_3, false), KEY_4('4', KeyEvent.VK_4, false), KEY_5('5', KeyEvent.VK_5,
				false), KEY_6('6', KeyEvent.VK_6, false), KEY_7('7', KeyEvent.VK_7, false), KEY_8(
				'8', KeyEvent.VK_8, false), KEY_9('9', KeyEvent.VK_9, false), KEY_0('0',
				KeyEvent.VK_0, false), KEY_Q('q', KeyEvent.VK_Q, false), KEY_W('w', KeyEvent.VK_W,
				false), KEY_E('e', KeyEvent.VK_E, false), KEY_R('r', KeyEvent.VK_R, false), KEY_T(
				't', KeyEvent.VK_T, false), KEY_Y('y', KeyEvent.VK_Y, false), KEY_U('u',
				KeyEvent.VK_U, false), KEY_I('i', KeyEvent.VK_I, false), KEY_O('o', KeyEvent.VK_O,
				false), KEY_P('p', KeyEvent.VK_P, false), KEY_A('a', KeyEvent.VK_A, false), KEY_S(
				's', KeyEvent.VK_S, false), KEY_D('d', KeyEvent.VK_D, false), KEY_F('f',
				KeyEvent.VK_F, false), KEY_G('g', KeyEvent.VK_G, false), KEY_H('h', KeyEvent.VK_H,
				false), KEY_J('j', KeyEvent.VK_J, false), KEY_K('k', KeyEvent.VK_K, false), KEY_L(
				'l', KeyEvent.VK_L, false), KEY_Z('z', KeyEvent.VK_Z, false), KEY_X('x',
				KeyEvent.VK_X, false), KEY_C('c', KeyEvent.VK_C, false), KEY_V('v', KeyEvent.VK_V,
				false), KEY_B('b', KeyEvent.VK_B, false), KEY_N('n', KeyEvent.VK_N, false), KEY_M(
				'm', KeyEvent.VK_M, false), KEY_SHIFT(KeyEvent.CHAR_UNDEFINED, KeyEvent.VK_SHIFT,
				true, "Shift"), KEY_CTRL(KeyEvent.CHAR_UNDEFINED, KeyEvent.VK_CONTROL, true,
				"Control"), KEY_ALT(KeyEvent.CHAR_UNDEFINED, KeyEvent.VK_ALT, true, "Alt"), KEY_SPACE(
				' ', KeyEvent.VK_SPACE, false, "Space"), KEY_TAB('\t', KeyEvent.VK_TAB, false,
				"Tab");

		private final boolean isModifier;
		private final char keyChar;
		private final int keyCode;
		private final String text;

		Keys(char keyChar, int keyCode, boolean isModifier) {
			this(keyChar, keyCode, isModifier, Character.toString(Character.toUpperCase(keyChar)));
		}

		Keys(char keyChar, int keyCode, boolean isModifier, String text) {
			this.keyChar = keyChar;
			this.keyCode = keyCode;
			this.isModifier = isModifier;
			this.text = text;
		}

		public String toString() {
			return "" + keyChar;
		}
	}

	private ArrayList<KeyListener> keyListeners = new ArrayList<KeyListener>();

	public KeyboardZone() {
		this(null);
	}

	public KeyboardZone(int x, int y) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public KeyboardZone(String name) {
		this(name, 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public KeyboardZone(int x, int y, int width, int height) {
		this(null, x, y, width, height);
	}

	public KeyboardZone(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);

		for (int i = 0; i < 10; i++) {
			this.add(new KeyZone(i * 50, 0, 50, 50, Keys.values()[i]));
		}
		for (int i = 0; i < 10; i++) {
			this.add(new KeyZone(i * 50, 50, 50, 50, Keys.values()[10 + i]));
		}
		for (int i = 0; i < 9; i++) {
			this.add(new KeyZone(i * 50, 100, 50, 50, Keys.values()[20 + i]));
		}
		for (int i = 0; i < 7; i++) {
			this.add(new KeyZone(i * 50, 150, 50, 50, Keys.values()[29 + i]));
		}
		for (int i = 0; i < 5; i++) {
			this.add(new KeyZone(i * 50, 200, 50, 50, Keys.values()[36 + i]));
		}

		for (Zone zone : this.children) {
			zone.setDirect(true);
		}

		// add the processing applet as a KeyListener by default
		this.addKeyListener(applet);
	}

	public void draw() {
		super.beginDraw();
		fill(0);
		rect(0, 0, width, height);
		super.endDraw();
		super.draw();
	}

	public void addKeyListener(KeyListener l) {
		this.keyListeners.add(l);
	}
}
