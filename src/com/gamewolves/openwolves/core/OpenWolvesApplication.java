package com.gamewolves.openwolves.core;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.gamewolves.openwolves.entities.Loader;
import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.gl.Display;
import com.gamewolves.openwolves.input.InputHandler;
import com.gamewolves.openwolves.materials.Material;

public abstract class OpenWolvesApplication
{
	
	private static OpenWolvesApplication m_instance;
	
	/**
	 * Initializes the engine
	 * 
	 * @param width Width of the window
	 * @param height Height of the window
	 * @param resizable Makes the window resizable
	 * @param title The title of the window
	 */
	public void init(int width, int height, boolean resizable, String title)
	{
		m_instance = this;
		Display.createDisplay(width, height, resizable, title);
		glfwMakeContextCurrent(Display.getWindow());
		glfwSwapInterval(1);
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(1f);
		InputHandler.init();
		InputHandler.setMousePosition(new Vector2f(width / 2, height / 2));
		initStandards();
	}
	
	/**
	 * Initializes integrated components and tools
	 */
	private void initStandards()
	{
		Material.baseMaterial = new Material();
		Material.baseMaterial.setShader("res/shaders/StandardShader/vertex.vert", null,
				"res/shaders/StandardShader/fragment.frag");
		Material.baseMaterial.setSpecularIntensity(1);
		Material.baseMaterial.setSpecularPower(32f);
	}
	
	/**
	 * Updates the programm
	 */
	public void update()
	{
		while (!GLFW.glfwWindowShouldClose(Display.getWindow()))
		{
			InputHandler.update();
			GLFW.glfwPollEvents();
			Display.updateDeltaTime();
			
			componentsUpdate();
			
			m_instance.update();
			
			componentslateUpdate();
			
			Display.updateDisplay();
		}
	}
	
	/**
	 * Calls the update function of every component
	 */
	public void componentsUpdate()
	{
		for (Entry<Class, HashMap<UUID, ? extends Component>> hashmap : Component.getComponents().entrySet())
		{
			for (Entry<UUID, ? extends Component> component : hashmap.getValue().entrySet())
			{
				component.getValue().update();
			}
		}
	}
	
	/**
	 * Calls the late update function of every component
	 */
	public void componentslateUpdate()
	{
		for (Entry<Class, HashMap<UUID, ? extends Component>> hashmap : Component.getComponents().entrySet())
		{
			for (Entry<UUID, ? extends Component> component : hashmap.getValue().entrySet())
			{
				component.getValue().lateUpdate();
			}
		}
	}
	
	/**
	 * Deletes everything
	 */
	public void delete()
	{
		m_instance.delete();
		Component.deleteComponents();
		Loader.delete();
		Display.closeDisplay();
	}
}
