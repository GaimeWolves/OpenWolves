package com.gamewolves.openwolves.entities.shapes;

import com.gamewolves.openwolves.entities.Entity;
import com.gamewolves.openwolves.entities.components.drawing.MeshComponent;
import com.gamewolves.openwolves.entities.components.position.TransformComponent;
import com.gamewolves.openwolves.util.conversion.Convertor;
import com.gamewolves.openwolves.util.math.Maths;

public class Plane extends Entity
{
	
	public MeshComponent mesh;
	public TransformComponent transform;
	
	public Plane(float width, float depth)
	{
		super();
		transform = new TransformComponent(ID);
		mesh = new MeshComponent(ID);
		mesh.loadMesh(Convertor.Vector3fToFloatArray(Maths.calculatePlaneVertices(width, depth)),
				new int[] { 0, 1, 3, 3, 1, 2 });
		addComponent(transform);
		addComponent(mesh);
	}
	
	/**
	 * public void render() { GL30.glBindVertexArray(vaoID);
	 * GL20.glEnableVertexAttribArray(0);
	 * 
	 * if (usesTexture) loadTexture();
	 * 
	 * GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
	 * GL20.glDisableVertexAttribArray(0);
	 * 
	 * if (usesTexture) unloadTexture();
	 * 
	 * GL30.glBindVertexArray(0); }
	 */
	
	public void delete()
	{
		super.delete();
	}
	
}
