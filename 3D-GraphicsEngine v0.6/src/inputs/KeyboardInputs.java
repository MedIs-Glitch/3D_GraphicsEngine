package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {
	
	public static class keyInputs{
		public static int vertical;
		public static int horizontal;
		public static int horMovement, forMovement, upDown; // Camera movement inputs
		public static int rotation, rotationX; // Camera rotation input
		public static boolean isShiftPressed, isRPressed, isSPressed, isCtrlPressed, isXPressed;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			keyInputs.vertical = 1;
			keyInputs.upDown = 1;
			keyInputs.rotationX = 1;
			break;
		case KeyEvent.VK_DOWN:
			keyInputs.vertical = -1;
			keyInputs.upDown = -1;
			keyInputs.rotationX = -1;
			break;
		case KeyEvent.VK_LEFT:
			keyInputs.horizontal = -1;
			keyInputs.rotation = 1;
			break;
		case KeyEvent.VK_RIGHT:
			keyInputs.rotation = -1;
			keyInputs.horizontal = 1;
			break;
		case KeyEvent.VK_SHIFT:
			keyInputs.isShiftPressed = true;
			break;
		case KeyEvent.VK_R:
			keyInputs.isRPressed = true;
			break;
		case KeyEvent.VK_S:
			keyInputs.isSPressed = true;
			keyInputs.forMovement = -1;
			break;
		case KeyEvent.VK_Z:
			keyInputs.forMovement = 1;
			break;
		case KeyEvent.VK_Q:
			keyInputs.horMovement = -1;
			break;
		case KeyEvent.VK_D:
			keyInputs.horMovement = 1;
			break;
		case KeyEvent.VK_CONTROL:
			keyInputs.isCtrlPressed = true;
			break;
		case KeyEvent.VK_X:
			keyInputs.isXPressed = true;
			break;
		default:
			break;
		}
		
		//System.out.println("hormovement: " + keyInputs.horMovement);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			keyInputs.vertical = 0;
			keyInputs.upDown = 0;
			keyInputs.rotationX = 0;
			break;
		case KeyEvent.VK_DOWN:
			keyInputs.vertical = 0;
			keyInputs.upDown = 0;
			keyInputs.rotationX = 0;
			break;
		case KeyEvent.VK_LEFT:
			keyInputs.horizontal = 0;
			keyInputs.rotation = 0;
			break;
		case KeyEvent.VK_RIGHT:
			keyInputs.horizontal = 0;
			keyInputs.rotation = 0;
			break;
		case KeyEvent.VK_SHIFT:
			keyInputs.isShiftPressed = false;
			break;
		case KeyEvent.VK_R:
			keyInputs.isRPressed = false;
			break;
		case KeyEvent.VK_S:
			keyInputs.isSPressed = false;
			keyInputs.forMovement = 0;
			break;
		case KeyEvent.VK_Z:
			keyInputs.forMovement = 0;
			break;
		case KeyEvent.VK_Q:
			keyInputs.horMovement = 0;
			break;
		case KeyEvent.VK_D:
			keyInputs.horMovement = 0;
			break;
		case KeyEvent.VK_CONTROL:
			keyInputs.isCtrlPressed = false;
			break;
		case KeyEvent.VK_X:
			keyInputs.isXPressed = false;
			break;
		default:
			break;
		}
	}

}
