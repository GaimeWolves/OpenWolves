package com.gamewolves.openwolves.gl;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

public class Display
{
	
	private static int WIDTH, HEIGHT;
	private static long window;
	private static float ASPECT_RATIO;
	private static long lastFrameTime;
	private static float deltaTime;
	
	/**
	 * Creates the window
	 * 
	 * @param width The width of the window
	 * @param height The height of the window
	 * @param resizable Makes the window resizable
	 * @param title The title of the window
	 */
	public static void createDisplay(int width, int height, boolean resizable, String title)
	{
		WIDTH = width;
		HEIGHT = height;
		ASPECT_RATIO = (float) WIDTH / (float) HEIGHT;
		
		lastFrameTime = getCurrentTime();
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		
		if (resizable)
			glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		else
			glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		glfwWindowHint(GLFW_SAMPLES, 8);
		
		window = glfwCreateWindow(WIDTH, HEIGHT, title, 0, 0);
		if (window == 0)
			throw new RuntimeException("Failed to create the GLFW window");
		
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
		{
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true);
		});
		
		try (MemoryStack stack = stackPush())
		{
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			
			glfwGetWindowSize(window, pWidth, pHeight);
			
			GLFWVidMode videomode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			glfwSetWindowPos(window, (videomode.width() - pWidth.get(0)) / 2,
					(videomode.height() - pHeight.get(0)) / 2);
		}
		
		glfwShowWindow(window);
	}
	
	/**
	 * Updates the Display
	 */
	public static void updateDisplay()
	{
		glfwSwapBuffers(window);
	}
	
	/**
	 * Updates the DeltaTime
	 */
	public static void updateDeltaTime()
	{
		long currentTime = getCurrentTime();
		deltaTime = (currentTime - lastFrameTime) / 1000.f;
		lastFrameTime = currentTime;
	}
	
	/**
	 * Closes the Window
	 */
	public static void closeDisplay()
	{
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	/**
	 * Gets the current time in milliseconds
	 * 
	 * @return Current time in milliseconds
	 */
	public static long getCurrentTime()
	{
		return (long) (System.nanoTime() / 1000000.f);
	}
	
	/**
	 * @return Time between frames
	 */
	public static float getDeltaTimeInSeconds()
	{
		return deltaTime;
	}
	
	/**
	 * @return Aspect Ratio
	 * @return
	 */
	public static float getAspectRatio()
	{
		return ASPECT_RATIO;
	}
	
	/**
	 * @return The GFLW Window
	 */
	public static long getWindow()
	{
		return window;
	}
}
