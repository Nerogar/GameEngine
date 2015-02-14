package de.nerogar.util;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import de.nerogar.render.Camera;

public class InputHandler {

	private Vector2f mousePos;
	private Vector2f mousePosD;
	private boolean hideMouse;

	private Ray MouseRay;

	public InputHandler() {
		mousePos = new Vector2f();
		mousePosD = new Vector2f();

		MouseRay = new Ray(new Vector3f(), new Vector3f());
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

	public void updateMousePositions(Camera camera, float fov) {
		float height = (float) Display.getHeight();
		float width = (float) Display.getWidth();
		float fovMultY = (float) Math.tan(fov / 2f / 180f * Math.PI);
		float fovMultX = (float) Math.tan(fov / 2f / 180f * Math.PI) / height * width;
		float mouseX = (float) (Mouse.getX() / width * 2 - 1);
		float mouseY = (float) (Mouse.getY() / height * 2 - 1);

		/*float mouseRotSin = (float) Math.sin(mouseX * fov / 180f * Math.PI);
		float mouseRotCos = (float) Math.cos(mouseX * fov / 180f * Math.PI);
		float mouseRotDownSin = (float) Math.sin(mouseY * fov / 180f * Math.PI);
		float mouseRotDownCos = (float) Math.cos(mouseY * fov / 180f * Math.PI);*/

		float camRotSin = (float) Math.sin(camera.yaw / 180f * Math.PI);
		float camRotCos = (float) Math.cos(camera.yaw / 180f * Math.PI);
		float camRotDownSin = (float) Math.sin(camera.pitch / 180f * Math.PI);
		float camRotDownCos = (float) Math.cos(camera.pitch / 180f * Math.PI);

		Vector3f dirLoc = new Vector3f(0, 0, 0);

		//Camera down Rotation:
		dirLoc.setY(-camRotDownSin);
		dirLoc.setZ(-camRotDownCos);

		//Mouse Position Y:
		dirLoc.addY(mouseY * camRotDownCos * fovMultY);
		dirLoc.addZ(-mouseY * camRotDownSin * fovMultY);
		//dirLoc.y += mouseY * camRotDownCos * fovMultY;
		//dirLoc.z -= mouseY * camRotDownSin * fovMultY;

		//Mouse Position X:

		dirLoc.addX(mouseX * fovMultX);
		//dirLoc.x += mouseX * fovMultX;

		//Camera Rotation:

		Vector3f dir = new Vector3f();

		dir.setZ(camRotCos * dirLoc.getZ() + camRotSin * dirLoc.getX());
		dir.setX(camRotCos * dirLoc.getX() - camRotSin * dirLoc.getZ());
		dir.setY(dirLoc.getY());
		//dir.z = camRotCos * dirLoc.z + camRotSin * dirLoc.x;
		//dir.x = camRotCos * dirLoc.x - camRotSin * dirLoc.z;
		//dir.y = dirLoc.y;

		MouseRay.setDirection(dir);
		MouseRay.setStart(new Vector3f(camera.x, camera.y, camera.z));
	}
	
	public Ray getMouseRay(){
		return MouseRay;
	}

	static {
		test = new ArrayList<Character>();
	}
}
