package de.nerogar.util;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class InputHandler {

	private Vector2f mousePos;
	private Vector2f mousePosD;
	private boolean hideMouse;

	public InputHandler() {
		mousePos = new Vector2f();
		mousePosD = new Vector2f();
	}

	public void update() {
		updateMouse();
		updateKeyboard();
	}

	//Mouse
	private void updateMouse() {
		Mouse.poll();

		mousePos.set(0, Mouse.getX());
		mousePos.set(1, Mouse.getY());

		mousePosD.set(0, Mouse.getDX());
		mousePosD.set(1, Mouse.getDY());

		if (hideMouse) {
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
			Mouse.poll();
		}

	}

	public Vector2f getMousePos() {
		return mousePos;
	}

	public Vector2f getMousePosD() {
		return mousePosD;
	}

	public void setHideMouse(boolean flag) {
		hideMouse = flag;
	}

	//Keyboard
	private void updateKeyboard() {
		Keyboard.poll();
	}

	public static void testMousePos() {
		while (Mouse.next()) {
			System.out.println("----------------");
			System.out.println(Mouse.getEventButton());
			System.out.println(Mouse.getEventButtonState());
			System.out.println(Mouse.getEventDWheel());
			System.out.println(Mouse.getEventDX());
			System.out.println(Mouse.getEventDY());
			System.out.println(Mouse.getEventX());
			System.out.println(Mouse.getEventY());
		}
	}

	private static ArrayList<Character> test;

	public static void testKeyboard() {
		/*while (Keyboard.next()) {
			System.out.println("----------------");
			System.out.println(Keyboard.getEventCharacter());
			System.out.println(Keyboard.getEventKey());
			System.out.println(Keyboard.getEventKeyState());
			
		}*/
		Keyboard.enableRepeatEvents(true);
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == 14) {
				if (test.size() > 0 && Keyboard.getEventKeyState()) test.remove(test.size() - 1);
			} else {
				if (Keyboard.getEventKeyState() && Keyboard.getEventCharacter() != 0) {
					test.add(Keyboard.getEventCharacter());
				}
			}
		}

		for (Character c : test) {
			System.out.print(c);
		}
		System.out.println();
	}

	static {
		test = new ArrayList<Character>();
	}
}
