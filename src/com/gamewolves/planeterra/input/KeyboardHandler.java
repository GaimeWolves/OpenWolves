package com.gamewolves.planeterra.input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWKeyCallback;

class KeyboardHandler extends GLFWKeyCallback {

	  public static boolean[] keys = new boolean[65536];

	  @Override
	  public void invoke(long window, int key, int scancode, int action, int mods) {
		  keys[key] = action != GLFW_RELEASE;
	  }

	  static boolean isKeyDown(int keycode) {
		  return keys[keycode];
	  }
}
