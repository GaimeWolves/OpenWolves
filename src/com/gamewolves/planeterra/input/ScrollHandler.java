package com.gamewolves.planeterra.input;

import org.lwjgl.glfw.GLFWScrollCallback;

class ScrollHandler extends GLFWScrollCallback {
	
	private static float nx = 0, ny = 0, dx = 0, dy = 0, lx = 0, ly = 0;

	/**
	 * Gets called if mouse is scrolled
	 */
	@Override
	public void invoke(long window, double xOffset, double yOffset) {
		nx += (float)xOffset;
		ny += (float)yOffset;
	}
	
	/**
	 * Calculates the change of the ScrollWheel
	 */
	static void update() {
		dx = nx - lx;
		dy = ny - ly;
		
		lx = nx;
		ly = ny;
	}

	static float getX() {
		return nx;
	}

	static float getY() {
		return ny;
	}
	
	static float getDeltaX() {
		return dx;
	}
	
	static float getDeltaY() {
		return dy;
	}
}
