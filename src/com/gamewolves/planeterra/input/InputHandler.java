package com.gamewolves.planeterra.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.gamewolves.planeterra.player.Camera;
import com.gamewolves.planeterra.render.Display;

public class InputHandler{
	
	public static MouseRayCaster rayCaster;
	
	public static void init() {
		glfwSetKeyCallback(Display.window, new KeyboardHandler());
		glfwSetScrollCallback(Display.window, new ScrollHandler());
	}
	
	public static void initRayCaster(Camera cam, Matrix4f projectionMatrix) {
		rayCaster = new MouseRayCaster(cam, projectionMatrix);
	}
	
	public static void update() {
		ScrollHandler.update();
		MouseHandler.update();
		rayCaster.update();
	}
	
	public static float getDeltaScroll() {
		return ScrollHandler.getDeltaY();
	}

	public static boolean isKeyDown(int keyCode) {
		  return KeyboardHandler.isKeyDown(keyCode);
	}
	
	public static boolean isButtonDown(int keyCode) {
		return glfwGetMouseButton(Display.window, keyCode) == GLFW_PRESS;
	}
	
	public static boolean isButtonUp(int keyCode) {
		return glfwGetMouseButton(Display.window, keyCode) != GLFW_PRESS;
	}
	
	public static Vector2f getDeltaPosition() {
		return new Vector2f(MouseHandler.getDeltaX(), MouseHandler.getDeltaY());
	}
	
	public static Vector2f getMousePosition() {
		return new Vector2f(MouseHandler.getX(), MouseHandler.getY());
	}
	
	public static void lockMouse(Vector2f pos) {
		MouseHandler.lockMouse(pos);
	}
	
	public static void freeMouse() {
		MouseHandler.freeMouse();
	}
}
