package com.gamewolves.openwolves.entities.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.gamewolves.openwolves.entities.Entity;
import com.gamewolves.openwolves.entities.Loader;
import com.gamewolves.openwolves.entities.components.position.TransformComponent;
import com.gamewolves.openwolves.util.conversion.Convertor;
import com.gamewolves.openwolves.util.math.Maths;

public class Rectangle extends Entity {
	
	private float[] vertices;
	private int[] indices;
	private int indexCount;
	
	private TransformComponent transform;

	public Rectangle(float width, float height) {
		super();
		vertices = Convertor.Vector3fToFloatArray(Maths.calculateRectangleVertices(width, height));
		indices = new int[] { 0, 1, 3, 3, 1, 2 };
		indexCount = indices.length;
		setVAO_ID(Loader.loadVAO(vertices, indices));
		transform = new TransformComponent(ID);
		components.add(transform);
	}
	
	public void render() {
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	public void delete() {
		super.delete();
	}
}
