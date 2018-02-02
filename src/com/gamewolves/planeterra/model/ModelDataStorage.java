package com.gamewolves.planeterra.model;

import org.joml.Vector3f;

import com.gamewolves.planeterra.render.MasterRenderer;
import com.gamewolves.planeterra.render.RendererType;
import com.gamewolves.planeterra.world.chunkmanager.BlockDatabase;

public class ModelDataStorage {
	
	public static final float[] VERTICES_FRONT = {
			-0.5f, -0.5f, -0.5f,
			-0.5f, 0.5f, -0.5f,
			0.5f, 0.5f, -0.5f,
			0.5f, -0.5f, -0.5f
	};
	
	public static final float[] VERTICES_BACK = {
			0.5f, -0.5f, 0.5f,
			0.5f, 0.5f, 0.5f,
			-0.5f, 0.5f, 0.5f,
			-0.5f, -0.5f, 0.5f
	};
	
	public static final float[] VERTICES_RIGHT = {
			0.5f, -0.5f, 0.5f,
			0.5f, -0.5f, -0.5f,
			0.5f, 0.5f, -0.5f,
			0.5f, 0.5f, 0.5f
	};
	
	public static final float[] VERTICES_LEFT = {
			-0.5f, -0.5f, 0.5f,
			-0.5f, 0.5f, 0.5f,
			-0.5f, 0.5f, -0.5f,
			-0.5f, -0.5f, -0.5f
	};
	
	public static final float[] VERTICES_TOP = {
			-0.5f, 0.5f, -0.5f,
			-0.5f, 0.5f, 0.5f,
			0.5f, 0.5f, 0.5f,
			0.5f, 0.5f, -0.5f
	};
	
	public static final float[] VERTICES_BOTTOM = {
			-0.5f, -0.5f, -0.5f,
			0.5f, -0.5f, -0.5f,
			0.5f, -0.5f, 0.5f,
			-0.5f, -0.5f, 0.5f
	};
	
	public static final float[] VERTICES_WATER = {
			-0.5f, 0.5f, -0.5f,
			-0.5f, 0.5f, 0f,
			0f, 0.5f, 0f,
			0f, 0.5f, -0.5f,
			
			0f, 0.5f, -0.5f,
			0f, 0.5f, 0f,
			0.5f, 0.5f, 0f,
			0.5f, 0.5f, -0.5f,
			
			0f, 0.5f, 0f,
			0f, 0.5f, 0.5f,
			0.5f, 0.5f, 0.5f,
			0.5f, 0.5f, 0f,
			
			-0.5f, 0.5f,0f,
			-0.5f, 0.5f, 0.5f,
			0f, 0.5f, 0.5f,
			0f, 0.5f, 0f
	};
	
	public static final int[] WATER_INDICES = {
			0, 1, 3,
			1, 2, 3,
			
			4, 5, 7,
			5, 6, 7,
			
			8, 9, 11,
			9, 10, 11,
			
			12, 13, 15,
			13, 14, 15
	};
	
	public static final float[] WATER_TEXTURECOORDS = {
			0.0f, 1.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.5f, 1.0f,
			
			0.5f, 1.0f,
			0.5f, 0.5f,
			1.0f, 0.5f,
			1.0f, 1.0f,
			
			0.5f, 0.5f,
			0.5f, 0.0f,
			1.0f, 0.0f,
			1.0f, 0.5f,
			
			0.0f, 0.5f,
			0.0f, 0.0f,
			0.5f, 0.0f,
			0.5f, 0.5f
	};
	
	public static final int[] INDICES = {
			0, 1, 3,
			1, 2, 3
	};
	
	public static final float[] TEXTURECOORDINATES = {
			0.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f
	};
	
	public static final float[] NORMALS_FRONT = {
			0, 0, -1,
			0, 0, -1,
			0, 0, -1,
			0, 0, -1,
	};
	
	public static final float[] NORMALS_BACK = {
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
	};
	
	public static final float[] NORMALS_RIGHT = {
			1, 0, 0,
			1, 0, 0,
			1, 0, 0,
			1, 0, 0,
	};
	
	public static final float[] NORMALS_LEFT = {
			-1, 0, 0,
			-1, 0, 0,
			-1, 0, 0,
			-1, 0, 0,
	};
	
	public static final float[] NORMALS_TOP = {
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
	};
	
	public static final float[] NORMALS_BOTTOM = {
			0, -1, 0,
			0, -1, 0,
			0, -1, 0,
			0, -1, 0,
	};
	
	/**
	 * Return a CubeModel ( for Prototyping )
	 * @param position Position of Cube
	 * @param blockID ID of Cube
	 * @return Modeldata of Cube
	 */
	public static ModelData getCube(Vector3f position, int blockID) {
		int[] indices = new int[INDICES.length * 6];
		float[] vertices = new float[VERTICES_TOP.length * 6];
		float[] textureCoords = new float[TEXTURECOORDINATES.length * 6];
		float[] normals = new float[NORMALS_TOP.length * 6];
		for (int i = 0; i < 6; i++) {
			int k = -1;
			
			switch (i) {
			case 0:
				k = 3;
				break;
			case 1:
				k = 2;
				break;
			case 2:
				k = 4;
				break;
			case 3:
				k = 5;
				break;
			case 4:
				k = 0;
				break;
			case 5:
				k = 1;
				break;
			default:
				break;
			}
			
			int[] indicesForFace = transformedIndices(k);
			for (int j = 0; j < indicesForFace.length; j++) {
				indices[k * 6 + j] = indicesForFace[j];
			}
			
			float[] transformedVertices = transformedVertices(position, k);
			for (int j = 0; j < transformedVertices.length; j++) {
				vertices[k * 6 + j] = transformedVertices[j];
			}
			
			float[] transformedTextureCoords = transformedTextureCoords(blockID, k);
			for (int j = 0; j < transformedTextureCoords.length; j++) {
				textureCoords[k * 6 + j] = transformedTextureCoords[j];
			}
			
			float[] transformedNormals = NORMALS_FRONT.clone();
			switch(k) {
			case 0:
				transformedNormals = NORMALS_FRONT.clone();
				break;
			case 1:
				transformedNormals = NORMALS_BACK.clone();
				break;
			case 2:
				transformedNormals = NORMALS_RIGHT.clone();
				break;
			case 3:
				transformedNormals = NORMALS_LEFT.clone();
				break;
			case 4:
				transformedNormals = NORMALS_TOP.clone();
				break;
			case 5:
				transformedNormals = NORMALS_BOTTOM.clone();
				break;
			}
			for (int j = 0; j < transformedNormals.length; j++) {
				normals[k * 6 + j] = transformedNormals[j];
			}
		}
		
		return new ModelData(vertices, textureCoords, normals, indices, 0, RendererType.ChunkRenderer, 0, 0, 0);
	}
	
	/**
	 * Return the Indices transformed to fit after numberOfFaces Indices
	 * @param numberOfFaces Number of Faces before this one
	 * @return Indices to put into Array after numberOfFaces
	 */
	public static int[] transformedIndices(int numberOfFaces) {
		int[] indices = INDICES.clone();
		
		for (int i = 0; i < indices.length; i++) {
			indices[i] += numberOfFaces * (VERTICES_FRONT.length / 3);
		}
		
		return indices;
	}
	
	/**
	 * Returns transformed Cube Vertices
	 * @param transformation Position of Cube
	 * @param face Face of Cube
	 * @return Array of Vertices
	 */
	public static float[] transformedVertices(Vector3f transformation, int face) {
		if (face >= 6 || face < 0) throw new IndexOutOfBoundsException("No VertexData for this ModelFace");
		
		float[] vertices = new float[1];
		
		switch (face) {
		case 0:
			vertices = VERTICES_FRONT.clone();
			break;
		case 1:
			vertices = VERTICES_BACK.clone();
			break;
		case 2:
			vertices = VERTICES_RIGHT.clone();
			break;
		case 3:
			vertices = VERTICES_LEFT.clone();
			break;
		case 4:
			vertices = VERTICES_TOP.clone();
			break;
		case 5:
			vertices = VERTICES_BOTTOM.clone();
			break;
		}
		
		for (int i = 0; i < vertices.length / 3; i++) {
			vertices[i * 3] += transformation.x;
			vertices[i * 3 + 1] += transformation.y;
			vertices[i * 3 + 2] += transformation.z;
		}
		
		return vertices;
	}
	
	/**
	 * Returns transformed Water Vertices
	 * @param transformation Position of Water
	 * @param offsetY The heightOffset
	 * @return Array of Vertices
	 */
	public static float[] transformedWaterVertices(Vector3f transformation, float offsetY) {
		float[] vertices = VERTICES_WATER.clone();
		
		for (int i = 0; i < vertices.length / 3; i++) {
			vertices[i * 3] += transformation.x;
			vertices[i * 3 + 1] += transformation.y + offsetY;
			vertices[i * 3 + 2] += transformation.z;	
		}
		
		return vertices;
	}
	
	/**
	 * Returns the transformed Indices
	 * @param numberOfFaces Current number of Faces
	 * @return Indices transformed to the current numberOfFaces
	 */
	public static int[] transformedWaterIndices(int numberOfFaces) {
		int[] indices = WATER_INDICES.clone();
		
		for (int i = 0; i < indices.length; i++) {
			indices[i] += numberOfFaces * (VERTICES_WATER.length / 3);
		}
		
		return indices;
	}
	
	/**
	 * Returns the transformed TextureCoordinates for Water
	 * @param textureIndex Index on TextureAtlas
	 * @return floatArray of TextureCoordinates
	 */
	public static float[] transformedWaterTextureCoords(int textureIndex) {
		float[] textureCoords = new float[WATER_TEXTURECOORDS.length];
		int index = textureIndex;
		float offsetX = MasterRenderer.getBlockTextures().getTextureOffset(index).x;
		float offsetY = MasterRenderer.getBlockTextures().getTextureOffset(index).y;
		for (int j = 0; j < textureCoords.length / 2; j++) {
			textureCoords[j * 2] = WATER_TEXTURECOORDS[j * 2];
			textureCoords[j * 2] *= MasterRenderer.getBlockTextures().getTextureSize();
			textureCoords[j * 2] += offsetX;
			
			textureCoords[j * 2 + 1] = WATER_TEXTURECOORDS[j * 2 + 1];
			textureCoords[j * 2 + 1] *= MasterRenderer.getBlockTextures().getTextureSize();
			textureCoords[j * 2 + 1] += offsetY;
		}
		
		return textureCoords;
	}
	
	/**
	 * Transforms TextureCoordinates based on a Block and an ID
	 * @param blockID Block
	 * @param blocks TextureAtlas of all Blocks
	 * @param textureIndex Index of Face of Block
	 * @return Transformed TextureCoordinates
	 */
	public static float[] transformedTextureCoords(int blockID, int textureIndex) {
		float[] textureCoords = new float[TEXTURECOORDINATES.length];
		int index = BlockDatabase.getBlock(blockID).textureIndices[textureIndex];
		float offsetX = MasterRenderer.getBlockTextures().getTextureOffset(index).x;
		float offsetY = MasterRenderer.getBlockTextures().getTextureOffset(index).y;
		for (int j = 0; j < textureCoords.length / 2; j++) {
			textureCoords[j * 2] = TEXTURECOORDINATES[j * 2];
			textureCoords[j * 2] *= MasterRenderer.getBlockTextures().getTextureSize();
			textureCoords[j * 2] += offsetX;
			
			textureCoords[j * 2 + 1] = TEXTURECOORDINATES[j * 2 + 1];
			textureCoords[j * 2 + 1] *= MasterRenderer.getBlockTextures().getTextureSize();
			textureCoords[j * 2 + 1] += offsetY;
		}
		
		if (textureIndex == 2) {
			textureCoords[0] += MasterRenderer.getBlockTextures().getTextureSize();
			textureCoords[1] += 0;
			textureCoords[2] += 0;
			textureCoords[3] += MasterRenderer.getBlockTextures().getTextureSize();
			textureCoords[4] += -MasterRenderer.getBlockTextures().getTextureSize();
			textureCoords[5] += 0;
			textureCoords[6] += 0;
			textureCoords[7] += -MasterRenderer.getBlockTextures().getTextureSize();
		}
		
		return textureCoords;
	}
}
