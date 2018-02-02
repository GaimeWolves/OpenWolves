package com.gamewolves.planeterra.model;

import com.gamewolves.planeterra.render.RendererType;

public class ModelData {
 
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private int[] indices;
    private float furthestPoint;
	private RendererType rendererType;
	private float indexX, indexY, indexZ;
 
	/**
     * Datastorage for Models
     * @param vertices Vertices of Model
     * @param textureCoords Texture Coordinates of Model
     * @param normals Normal Vectors of Model
     * @param indices Indices of Model
     * @param furthestPoint Farthest Point of Model
	 * @param rendererType Renderer to use;
	 * @param x index for chunk (for use in Water-/ChunkRenderer)
	 * @param y index for chunk (for use in Water-/ChunkRenderer)
	 * @param z index for chunk (for use in Water-/ChunkRenderer)
	 */
    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices, float furthestPoint, RendererType rendererType, float x, float y, float z) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
		this.rendererType = rendererType;
		this.indexX = x;
		this.indexY = y;
		this.indexZ = z;
	}
    
    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices, float furthestPoint, RendererType rendererType) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
		this.rendererType = rendererType;
		this.indexX = 0;
		this.indexY = 0;
		this.indexZ = 0;
    }

	public float[] getVertices() {
        return vertices;
    }
 
    public float[] getTextureCoords() {
        return textureCoords;
    }
 
    public float[] getNormals() {
        return normals;
    }
 
    public int[] getIndices() {
        return indices;
    }
 
    public float getFurthestPoint() {
        return furthestPoint;
    }

	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}

	public void setTextureCoords(float[] textureCoords) {
		this.textureCoords = textureCoords;
	}

	public void setNormals(float[] normals) {
		this.normals = normals;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}

	public RendererType getRenderer() {
		return rendererType;
	}

	public void setRenderer(RendererType rendererType) {
		this.rendererType = rendererType;
	}

	public float getIndexX() {
		return indexX;
	}

	public void setIndexX(int indexX) {
		this.indexX = indexX;
	}

	public float getIndexY() {
		return indexY;
	}

	public void setIndexY(int indexY) {
		this.indexY = indexY;
	}

	public void setFurthestPoint(float furthestPoint) {
		this.furthestPoint = furthestPoint;
	}

	public float getIndexZ() {
		return indexZ;
	}

	public void setIndexZ(int indexZ) {
		this.indexZ = indexZ;
	}
}
