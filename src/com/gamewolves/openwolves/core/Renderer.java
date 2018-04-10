package com.gamewolves.openwolves.core;

import java.util.UUID;

import org.joml.Matrix4f;

import com.gamewolves.openwolves.entities.Entity;
import com.gamewolves.openwolves.entities.components.drawing.MeshComponent;
import com.gamewolves.openwolves.entities.components.lighting.LightComponent;
import com.gamewolves.openwolves.entities.components.position.TransformComponent;
import com.gamewolves.openwolves.entities.misc.Camera;
import com.gamewolves.openwolves.entities.misc.light.Light;
import com.gamewolves.openwolves.gl.Display;
import com.gamewolves.openwolves.materials.shaders.Shader;
import com.gamewolves.openwolves.util.math.Maths;

import java.util.ArrayList;
import java.util.Map.Entry;

public class Renderer
{
	
	/**
	 * Renders everything to the scene
	 */
	public static void render()
	{
		Shader currentShader = null;
		boolean started = false;
		for (Entry<UUID, ? extends Entity> entry : Entity.getEntities().entrySet())
		{
			Entity entity = entry.getValue();
			MeshComponent mesh = entity.getComponent(MeshComponent.class);
			TransformComponent transform = entity.getComponent(TransformComponent.class);
			if (mesh == null)
				continue;
			if (currentShader == null)
			{
				currentShader = mesh.material.getShader();
				currentShader.start();
				started = true;
			}
			if (mesh.material.getShader() != currentShader)
			{
				currentShader.stop();
				currentShader = mesh.material.getShader();
				currentShader.start();
			}
			
			if (!started)
			{
				currentShader.start();
				started = true;
			}
			
			loadShader(currentShader, transform, mesh);
			
			entity.getComponent(MeshComponent.class).render();
		}
		
		currentShader.stop();
	}
	
	/**
	 * Renders an entity to the screen
	 * 
	 * @param entity The entity to render
	 */
	public static void renderEntity(Entity entity)
	{
		MeshComponent mesh = entity.getComponent(MeshComponent.class);
		TransformComponent transform = entity.getComponent(TransformComponent.class);
		if (mesh == null)
			return;
		
		Shader shader = mesh.material.getShader();
		if (shader == null)
			return;
		
		shader.start();
		loadShader(shader, transform, mesh);
		mesh.render();
		shader.stop();
	}
	
	/**
	 * Loads the standardized uniform variables
	 * 
	 * @param shader The shader to load
	 * @param transform The transform of the current entity
	 * @param mesh The mesh of the current entity
	 */
	private static void loadShader(Shader shader, TransformComponent transform, MeshComponent mesh)
	{
		Matrix4f MVP = Maths.createMVP(transform.getModelMatrix(), Camera.getMainCam().view.getViewMatrix(),
				Camera.getMainCam().projection.getProjectionMatrix());
		Matrix4f MV = new Matrix4f(transform.getModelMatrix()).mul(Camera.getMainCam().view.getViewMatrix());
		shader.loadMatrix4f("MVP", MVP);
		shader.loadMatrix4f("Model", transform.getModelMatrix());
		shader.loadMatrix4f("View", Camera.getMainCam().view.getViewMatrix());
		shader.loadMatrix4f("IT_MVP", new Matrix4f(MVP).invert().transpose());
		shader.loadMatrix4f("T_MV", new Matrix4f(MV).transpose());
		shader.loadMatrix4f("IT_MV", new Matrix4f(MV).invert().transpose());
		shader.loadMatrix4f("IT_Model", transform.getModelMatrix().invert().transpose());
		
		shader.loadVector3f("CameraPos", Camera.getMainCam().transform.position);
		shader.loadMatrix4f("Projection", Camera.getMainCam().projection.getProjectionMatrix());
		
		shader.loadFloat("Time", Display.getCurrentTime());
		shader.loadFloat("DeltaTime", Display.getDeltaTimeInSeconds());
		
		ArrayList<LightComponent> nearestLights = Light.findNearestLights(transform.position);
		for (int i = 0; i < Light.MAX_LIGHTS; i++)
		{
			if (nearestLights.get(i) != null)
			{
				shader.loadVector3f("Lights" + "[" + i + "]" + ".Light_Position", nearestLights.get(i).position);
				shader.loadVector3f("Lights" + "[" + i + "]" + ".Light_Color",
						nearestLights.get(i).lightColor.getRGB());
				shader.loadVector3f("Lights" + "[" + i + "]" + ".Light_Attenuation", nearestLights.get(i).attenuation);
				shader.loadVector3f("Lights" + "[" + i + "]" + ".Light_Direction", nearestLights.get(i).direction);
				shader.loadFloat("Lights" + "[" + i + "]" + ".Light_DiffuseIntensity",
						nearestLights.get(i).diffuseIntensity);
				shader.loadFloat("Lights" + "[" + i + "]" + ".Light_AmbientIntensity",
						nearestLights.get(i).ambientIntensity);
			}
		}
		
		shader.loadFloat("SpecularPower", mesh.material.getSpecularPower());
		shader.loadFloat("SpecularIntensity", mesh.material.getSpecularIntensity());
		shader.loadVector4f("MaterialColor", mesh.material.getColor().getRGBA());
	}
}
