package com.gamewolves.openwolves.entities.components.lighting;

import java.util.UUID;

import org.joml.Vector3f;

import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.materials.colors.Color;

public class LightComponent extends Component
{
	
	public Color lightColor = Color.BLACK;
	public float ambientIntensity = 0f;
	public float diffuseIntensity = 0f;
	public Vector3f attenuation = new Vector3f();
	public Vector3f direction = new Vector3f();
	public Vector3f position = new Vector3f();
	
	public LightComponent(UUID enitity)
	{
		super(enitity);
	}
	
}
