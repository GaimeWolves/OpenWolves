package com.gamewolves.openwolves.entities.shapes;

import com.gamewolves.openwolves.entities.Entity;
import com.gamewolves.openwolves.entities.components.drawing.MeshComponent;
import com.gamewolves.openwolves.entities.components.position.TransformComponent;

public class Model extends Entity {
	
	public MeshComponent mesh;
	public TransformComponent transform;

	public Model() {
		super();
		transform = new TransformComponent(ID);
		mesh = new MeshComponent(ID);
		addComponent(transform);
		addComponent(mesh);
	}
	
	@Override
	public void delete() {
		
	}
}
