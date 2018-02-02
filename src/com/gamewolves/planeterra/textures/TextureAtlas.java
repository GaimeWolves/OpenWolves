package com.gamewolves.planeterra.textures;

import org.joml.Vector2f;

public class TextureAtlas {

	private Texture texture;
	private int gridDimensions;
	private float textureSize;

	/**
	 * Saves a Texture as an TextureAtlas with the number of rows and columns [They have to be the same]
	 * @param texture Reference to the Texture
	 * @param gridDimensions Number of Rows and Columns
	 */
	public TextureAtlas(Texture texture, int gridDimensions) {
		this.texture = texture;
		this.gridDimensions = gridDimensions;
		textureSize = 1f / (float)gridDimensions;
	}
	
	/**
	 * Returns the TextureCoordinates for this index
	 * @param index Index of Texture in TextureAtlas
	 * @return TextureCoordinates for the index
	 */
	public Vector2f getTextureOffset(int index) {
		if (index >= (gridDimensions * gridDimensions) || index < 0) throw new IndexOutOfBoundsException("TextureAtlas has no Texture with Index: " + index);
		
		float xOffset = (float)(index % gridDimensions) / (float)gridDimensions;
		float yOffset = (float)(index / gridDimensions) / (float)gridDimensions;
		
		return new Vector2f(xOffset, yOffset);
	}
	
	public Texture getTexure() {
		return texture;
	}

	public int getGridDimensions() {
		return gridDimensions;
	}

	public float getTextureSize() {
		return textureSize;
	}
}
