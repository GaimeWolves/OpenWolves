package com.gamewolves.planeterra.model;

import com.gamewolves.planeterra.model.ModelData;
import com.gamewolves.planeterra.render.RendererType;

public class Model {
	
	private int vaoID;
	private ModelData data;
	private RendererType rendererType;
	private float indexX, indexY, indexZ;
	
	/**
	 * A dataholder to hold the VAOs ID
	 */
	public Model(int vaoID, ModelData data, RendererType rendererType, float indexX, float indexY, float indexZ) {
		this.vaoID = vaoID;
		this.data = data;
		this.rendererType = rendererType;
		this.indexX = indexX;
		this.indexY = indexY;
		this.indexZ = indexZ;
	}
	
	public Model(int vaoID, ModelData data) {
		this.vaoID = vaoID;
		this.data = data;
		this.rendererType = data.getRenderer();
		this.indexX = data.getIndexX();
		this.indexY = data.getIndexY();
		this.indexZ = data.getIndexZ();
	}

	public Model(Model model) {
		this.vaoID = model.vaoID;
		this.data = model.data;
		this.rendererType = model.rendererType;
		this.indexX = model.indexX;
		this.indexY = model.indexY;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getIndexCount() {
		return data.getIndices().length;
	}

	public int getVertexCount() {
		return data.getVertices().length;
	}

	public ModelData getData() {
		return data;
	}
	
	/**
	 * Tests if the ModelDatas are the same
	 * @param otherData Other Data
	 * @return Is the same
	 */
	public boolean isEqualTo(ModelData otherData) {
		if (data.getVertices().length != otherData.getVertices().length
				|| data.getTextureCoords().length != otherData.getTextureCoords().length
				|| data.getNormals().length != otherData.getNormals().length
				|| data.getIndices().length != otherData.getIndices().length
				) return false;
		
		for (int i = 0; i < data.getTextureCoords().length; i++) {
			if (data.getTextureCoords()[i] != otherData.getTextureCoords()[i]) return false;
		}
		for (int i = 0; i < data.getVertices().length; i++) {
			if (data.getVertices()[i] != otherData.getVertices()[i]) return false;
		}
		for (int i = 0; i < data.getNormals().length; i++) {
			if (data.getNormals()[i] != otherData.getNormals()[i]) return false;
		}
		for (int i = 0; i < data.getIndices().length; i++) {
			if (data.getIndices()[i] != otherData.getIndices()[i]) return false;
		}
		
		return true;
	}

	public RendererType getRenderer() {
		return rendererType;
	}

	public float getIndexX() {
		return indexX;
	}

	public float getIndexY() {
		return indexY;
	}

	public float getIndexZ() {
		return indexZ;
	}
}
