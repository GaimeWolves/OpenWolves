package com.gamewolves.openwolves.entities.misc.light;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.joml.Vector3f;

import com.gamewolves.openwolves.entities.Entity;
import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.entities.components.drawing.DebugDrawComponent;
import com.gamewolves.openwolves.entities.components.lighting.LightComponent;
import com.gamewolves.openwolves.entities.components.position.TransformComponent;
import com.gamewolves.openwolves.util.math.Maths;
import com.gamewolves.openwolves.util.model.ModelDatabase;

public class Light extends Entity
{
	
	public static int MAX_LIGHTS = 4;
	
	public enum Type
	{
		Directional
	}
	
	public TransformComponent transform;
	public LightComponent lightComponent;
	public DebugDrawComponent debugDrawComponent;
	
	public Light(Type type)
	{
		super();
		transform = new TransformComponent(ID);
		addComponent(transform);
		lightComponent = new LightComponent(ID);
		addComponent(lightComponent);
		debugDrawComponent = new DebugDrawComponent(ID);
		addComponent(debugDrawComponent);
		switch (type)
		{
			case Directional:
				debugDrawComponent.setLines(ModelDatabase.DIRECTIONAL_LIGHT_GIZMO);
				break;
			
			default:
				break;
		}
	}
	
	public void setDirection(Vector3f direction)
	{
		this.lightComponent.direction = direction;
		Vector3f angles = Maths.directionToEulerAngles(direction, new Vector3f(0, 1, 0));
		transform.setRotation(new Vector3f(angles.x, angles.y, angles.z));
	}
	
	public static ArrayList<LightComponent> findNearestLights(Vector3f position)
	{
		ArrayList<LightComponent> lights = new ArrayList<>(MAX_LIGHTS);
		
		ArrayList<LightComponent> allLights = new ArrayList<>();
		
		for (Component entities : Component.getComponent(LightComponent.class).values())
		{
			allLights.add((LightComponent) entities);
		}
		
		Collections.sort(allLights, new Comparator<LightComponent>()
		{
			
			@Override
			public int compare(LightComponent a, LightComponent b)
			{
				return Float.compare(
						new Vector3f(a.position).add(
								Entity.getEntity(a.getAttachedEntity()).getComponent(TransformComponent.class).position)
								.distance(position),
						new Vector3f(b.position).add(
								Entity.getEntity(b.getAttachedEntity()).getComponent(TransformComponent.class).position)
								.distance(position));
			}
		});
		
		for (int i = 0; i < MAX_LIGHTS; i++)
		{
			if (i < allLights.size())
			{
				lights.add(allLights.get(i));
			} else
			{
				lights.add(null);
			}
		}
		
		return lights;
	}
}
