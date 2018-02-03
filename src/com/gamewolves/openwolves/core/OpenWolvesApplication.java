package com.gamewolves.openwolves.core;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import com.gamewolves.openwolves.Application;
import com.gamewolves.openwolves.entities.Loader;
import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.gl.Display;

public abstract class OpenWolvesApplication {
	
	private static Application m_instance;
	
	public void init(Application instance, int width, int height, boolean resizable, String title) {
		m_instance = instance;
		Display.createDisplay(width, height, resizable, title);
		glfwMakeContextCurrent(Display.getWindow());
		glfwSwapInterval(1);
		GL.createCapabilities();
	}
	
	public void update() {
		while(!GLFW.glfwWindowShouldClose(Display.getWindow())) {
			GLFW.glfwPollEvents();
			Display.updateDeltaTime();
			
			componentsUpdate();
			
			//rendering
			m_instance.update();
			
			componentslateUpdate();
						
			Display.updateDisplay();
		}
	}
	
	/**
	 * Calls the update function of every component
	 */
	public void componentsUpdate() {
		for(Entry<Class, HashMap<UUID, ? extends Component>> hashmap : Component.getComponents().entrySet()) { 
			for(Entry<UUID, ? extends Component> component : hashmap.getValue().entrySet()) {
				component.getValue().update();
			}
		}
	}
	
	/**
	 * Calls the late update function of every component
	 */
	public void componentslateUpdate() {
		for(Entry<Class, HashMap<UUID, ? extends Component>> hashmap : Component.getComponents().entrySet()) { 
			for(Entry<UUID, ? extends Component> component : hashmap.getValue().entrySet()) {
				component.getValue().lateUpdate();
			}
		}
	}
	
	public void delete() {
		m_instance.delete();
		Component.deleteComponents();
		Loader.delete();
		Display.closeDisplay();
	}

}
