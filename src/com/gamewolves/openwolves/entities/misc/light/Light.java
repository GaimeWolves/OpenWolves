package com.gamewolves.openwolves.entities.misc.light;

import com.gamewolves.openwolves.entities.Entity;
import com.gamewolves.openwolves.entities.components.position.TransformComponent;
import com.gamewolves.openwolves.materials.colors.Color;

public class Light extends Entity {
	
	public TransformComponent transform;
	public Color color;
	public float ambient;

	public Light() {
		super();
		transform = new TransformComponent(ID);
		addComponent(transform);
	}

}
