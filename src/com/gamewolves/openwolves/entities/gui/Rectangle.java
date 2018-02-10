package com.gamewolves.openwolves.entities.gui;

import com.gamewolves.openwolves.entities.Entity;
import com.gamewolves.openwolves.entities.components.drawing.MeshComponent;
import com.gamewolves.openwolves.entities.components.position.Transform2DComponent;
import com.gamewolves.openwolves.util.conversion.Convertor;
import com.gamewolves.openwolves.util.math.Maths;

public class Rectangle extends Entity {
	
	public MeshComponent mesh;
	public Transform2DComponent transform;

	public Rectangle(float width, float height) {
		super();
		transform = new Transform2DComponent(ID);
		mesh = new MeshComponent(ID);
		mesh.loadMesh(Convertor.Vector3fToFloatArray(Maths.calculateRectangleVertices(width, height)), new int[] { 0, 1, 3, 3, 1, 2 });
		addComponent(transform);
		addComponent(mesh);
	}

	public void delete() {
		super.delete();
	}
}
