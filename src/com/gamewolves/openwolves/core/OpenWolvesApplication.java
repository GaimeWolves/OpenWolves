package com.gamewolves.openwolves.core;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;

import java.util.HashMap;
import java.util.Map.Entry;

import java.util.UUID;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import com.gamewolves.openwolves.DesktopApplication;
import com.gamewolves.openwolves.entities.Loader;
import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.gl.Display;
import com.gamewolves.openwolves.input.InputHandler;
import com.gamewolves.openwolves.materials.Material;

public abstract class OpenWolvesApplication {
	
	private static OpenWolvesApplication m_instance;
	
	public void init(DesktopApplication instance, int width, int height, boolean resizable, String title) {
		m_instance = instance;
		Display.createDisplay(width, height, resizable, title);
		glfwMakeContextCurrent(Display.getWindow());
		glfwSwapInterval(1);
		GL.createCapabilities();
		InputHandler.init();
		InputHandler.setMousePosition(new Vector2f(width / 2, height / 2));
		
		Material.baseMaterial = new Material();
		Material.baseMaterial.setShader("res/shaders/StandardShader/vertex.vert", null, "res/shaders/StandardShader/fragment.frag", new String[] { "position", "", "normal" }
		, new String[] { "transformationMatrix", "projectionMatrix", "viewMatrix", "lightPosition", "lightColor", "ambient" });
	}
	
	public void update() {
		while(!GLFW.glfwWindowShouldClose(Display.getWindow())) {
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
	 * Renders everything to the scene
	 
	public void render() {
		Shader currentShader = null;
		boolean started = false;
		for (Entry<UUID, ? extends Entity> entry : Entity.getEntities().entrySet()) {
			Entity entity = entry.getValue();
			if (entity.getComponent(MeshComponent.class) == null) continue;
			if (currentShader == null) {
				currentShader = entity.getComponent(MeshComponent.class).material.getShader();
				currentShader.start();
				started = true;
			}
			if (entity.getComponent(MeshComponent.class).material.getShader() != currentShader) {
				currentShader.stop();
				currentShader = entity.getComponent(MeshComponent.class).material.getShader();
				currentShader.start();
			}
			
			if (!started) { currentShader.start(); started = true; }
			
			currentShader.getUniformVariable("transformationMatrix").setValue(entity.getComponent(TransformComponent.class).getTransformationMatrix());
			currentShader.getUniformVariable("viewMatrix").setValue(mainCamera.view.getViewMatrix());
			currentShader.getUniformVariable("projectionMatrix").setValue(mainCamera.projection.getProjectionMatrix());
			
			entity.getComponent(MeshComponent.class).render();
		}
		
		currentShader.stop();
	}
	*/
	
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
