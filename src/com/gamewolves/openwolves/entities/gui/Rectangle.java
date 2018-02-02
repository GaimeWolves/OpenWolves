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
	private int vertexCount;
	
	private TransformComponent transform;

	public Rectangle(float width, float height) {
		super();
		vertices = Convertor.Vector3fToFloatArray(Maths.calculateRectangleVertices(width, height));
		vertexCount = vertices.length / 3;
		setVAO_ID(Loader.loadVAO(vertices));
	}
	
	public void render() {
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	public void delete() {
		super.delete();
	}
}
