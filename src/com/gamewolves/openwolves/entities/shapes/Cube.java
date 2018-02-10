package com.gamewolves.openwolves.entities.shapes;

import com.gamewolves.openwolves.entities.Entity;
import com.gamewolves.openwolves.entities.components.drawing.MeshComponent;
import com.gamewolves.openwolves.entities.components.position.TransformComponent;
import com.gamewolves.openwolves.util.conversion.Convertor;
import com.gamewolves.openwolves.util.math.Maths;
import com.gamewolves.openwolves.util.model.ModelDatabase;

public class Cube extends Entity {
	
	public TransformComponent transform;
	public MeshComponent mesh;

	public Cube(float width, float height, float depth) {
		super();
		transform = new TransformComponent(ID);
		mesh = new MeshComponent(ID);
		mesh.loadMesh(Convertor.Vector3fToFloatArray(Maths.calculateCubeVertices(width, height, depth)), ModelDatabase.CUBE_INDICES);
		addComponent(transform);
		addComponent(mesh);
	}
	
	public void delete() {
		super.delete();
	}
}
