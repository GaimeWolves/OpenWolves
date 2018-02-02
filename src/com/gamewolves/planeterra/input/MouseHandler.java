package com.gamewolves.planeterra.input;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

import java.nio.DoubleBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import com.gamewolves.planeterra.render.Display;

class MouseHandler {

	private static float nx = 0, ny = 0, lx = 0, ly = 0, dx = 0, dy = 0;
	
	private static boolean mouseLocked = false;
	private static Vector2f lockedPos = new Vector2f();
	
	/**
	 * Calculates the delta for the position
	 */
	static void update() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
		
		if (!mouseLocked) {
			glfwGetCursorPos(Display.window, x, y);
			x.rewind();
			y.rewind();

			nx = (float) x.get();
			ny = (float) y.get();
        
			dx = nx - lx;
			dy = ny - ly;
		
			lx = nx;
			ly = ny;
		} else {
            glfwGetCursorPos(Display.window, x, y);
            x.rewind();
            y.rewind();

            nx = (float) x.get();
            ny = (float) y.get();
            
            dx = nx - lockedPos.x;
            dy = ny - lockedPos.y;
            
            glfwSetCursorPos(Display.window, lockedPos.x, lockedPos.y);
		}
	}
	
	/**
	 * Locks the Cursor to the specified position
	 * @param pos Position to lock to
	 */
	static void lockMouse(Vector2f pos) {
        mouseLocked = true;
        lockedPos = pos;
        
        lx = pos.x;
        ly = pos.y;
	}
	
	/**
	 * Frees the mouse again
	 */
	static void freeMouse() {
		mouseLocked = false;
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
