package com.gamewolves.openwolves.entities.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.gamewolves.openwolves.entities.Entity;
import com.gamewolves.openwolves.entities.Loader;
import com.gamewolves.openwolves.entities.components.position.Transform2DComponent;
import com.gamewolves.openwolves.entities.components.texture.TextureComponent;
import com.gamewolves.openwolves.textures.Texture;
import com.gamewolves.openwolves.util.conversion.Convertor;
import com.gamewolves.openwolves.util.math.Maths;

public class Rectangle extends Entity {
	
	private float[] vertices;
	private int[] indices;
	private float[] uvCoords;
	private int indexCount;
	
	private Transform2DComponent transform;

	public Rectangle(float width, float height) {
		super();
		vertices = Convertor.Vector3fToFloatArray(Maths.calculateRectangleVertices(width, height));
		indices = new int[] { 0, 1, 3, 3, 1, 2 };
		uvCoords = new float[] { 0, 0, 0, 1, 1, 1, 1, 0 };
		indexCount = indices.length;
		setVAO_ID(Loader.loadVAO(vertices, indices, uvCoords));
		transform = new Transform2DComponent(ID);
		addComponent(transform);
	}
	
	public void render() {
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getComponent(TextureComponent.class).getTexture().getTextureID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Adds a texture to the rectangle
	 * @param file The path to the texture
	 */
	public void addTexture(String file) {
		TextureComponent texture = new TextureComponent();
		texture.loadTexture(file);
		addComponent(texture);
	}
	
	/**
	 * Adds a texture to the rectangle
	 * @param texture The texture to add
	 */
	public void addTexture(Texture texture) {
		TextureComponent textureComponent = new TextureComponent();
		textureComponent.setTexture(texture);
		addComponent(textureComponent);
	}

	public void delete() {
		super.delete();
	}
}
