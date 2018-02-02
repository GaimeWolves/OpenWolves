package com.gamewolves.planeterra.world.chunkmanager.chunk;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import com.gamewolves.planeterra.model.ModelData;
import com.gamewolves.planeterra.model.ModelDataStorage;
import com.gamewolves.planeterra.render.RendererType;
import com.gamewolves.planeterra.textures.TextureAtlas;
import com.gamewolves.planeterra.world.chunkmanager.BlockDatabase;
import com.gamewolves.planeterra.world.generation.WorldGenerator;

public class MeshBuilder {

	/**
	 * Generates a ChunkMesh from the Blocks in the Chunk
	 * @param blocks Blocks in the Chunk
	 * @param blockAtlas Atlas of all Blocks
	 * @return ChunkMesh
	 */
	public static ModelData generateChunkMesh(int[][][] blocks, TextureAtlas blockAtlas, float indexX, float indexY, float indexZ) {
		List<Integer> indices = new ArrayList<Integer>();
		List<Float> vertices = new ArrayList<Float>();
		List<Float> textureCoords = new ArrayList<Float>();
		List<Float> normals = new ArrayList<Float>();
		
		int numberOfFaces = 0;
		
		// Checks if Faces touch each other and adds Faces that touch a transparent Block
		for (int x = 1; x < Chunk.CHUNK_SIZE + 1; x++) {
			for (int y = 1; y < Chunk.CHUNK_SIZE + 1; y++) {
				for (int z = 1; z < Chunk.CHUNK_SIZE + 1; z++) {
					if (BlockDatabase.getBlock(blocks[x][y][z]).ID != 0 && BlockDatabase.getBlock(blocks[x][y][z]).ID != 5) {
						//Checks if Block on the left is transparent
						if (BlockDatabase.getBlock(blocks[x - 1][y][z]).isTransparent) {
							for(float textureCoord : ModelDataStorage.transformedTextureCoords(blocks[x][y][z], 3))
								textureCoords.add(textureCoord);
							for(int index : ModelDataStorage.transformedIndices(numberOfFaces))
								indices.add(index);
							for(float vertex : ModelDataStorage.transformedVertices(new Vector3f(x, y, z), 3))
								vertices.add(vertex);
							for(float normal : ModelDataStorage.NORMALS_LEFT.clone())
								normals.add(normal);
							numberOfFaces++;
						}
						//Checks if Block on the right is transparent
						if (BlockDatabase.getBlock(blocks[x + 1][y][z]).isTransparent) {
							for(float textureCoord : ModelDataStorage.transformedTextureCoords(blocks[x][y][z], 2))
								textureCoords.add(textureCoord);
							for(int index : ModelDataStorage.transformedIndices(numberOfFaces))
								indices.add(index);
							for(float vertex : ModelDataStorage.transformedVertices(new Vector3f(x, y, z), 2))
								vertices.add(vertex);
							for(float normal : ModelDataStorage.NORMALS_RIGHT.clone())
								normals.add(normal);
							numberOfFaces++;
						}
						//Checks if Block on the top is transparent
						if (BlockDatabase.getBlock(blocks[x][y + 1][z]).isTransparent) {
							for(float textureCoord : ModelDataStorage.transformedTextureCoords(blocks[x][y][z], 4))
								textureCoords.add(textureCoord);
							for(int index : ModelDataStorage.transformedIndices(numberOfFaces))
								indices.add(index);
							for(float vertex : ModelDataStorage.transformedVertices(new Vector3f(x, y, z), 4))
								vertices.add(vertex);
							for(float normal : ModelDataStorage.NORMALS_TOP.clone())
								normals.add(normal);
							numberOfFaces++;
						}
						//Checks if Block on the bottom is transparent
						if (BlockDatabase.getBlock(blocks[x][y - 1][z]).isTransparent) {
							for(float textureCoord : ModelDataStorage.transformedTextureCoords(blocks[x][y][z], 5))
								textureCoords.add(textureCoord);
							for(int index : ModelDataStorage.transformedIndices(numberOfFaces))
								indices.add(index);
							for(float vertex : ModelDataStorage.transformedVertices(new Vector3f(x, y, z), 5))
								vertices.add(vertex);
							for(float normal : ModelDataStorage.NORMALS_BOTTOM.clone())
								normals.add(normal);
							numberOfFaces++;
						}
						//Checks if Block in the front is transparent
						if (BlockDatabase.getBlock(blocks[x][y][z - 1]).isTransparent) {
							for(float textureCoord : ModelDataStorage.transformedTextureCoords(blocks[x][y][z], 0))
								textureCoords.add(textureCoord);
							for(int index : ModelDataStorage.transformedIndices(numberOfFaces))
								indices.add(index);
							for(float vertex : ModelDataStorage.transformedVertices(new Vector3f(x, y, z), 0))
								vertices.add(vertex);
							for(float normal : ModelDataStorage.NORMALS_FRONT.clone())
								normals.add(normal);
							numberOfFaces++;
						}
						//Checks if Block in the back is transparent
						if (BlockDatabase.getBlock(blocks[x][y][z + 1]).isTransparent) {
							for(float textureCoord : ModelDataStorage.transformedTextureCoords(blocks[x][y][z], 1))
								textureCoords.add(textureCoord);
							for(int index : ModelDataStorage.transformedIndices(numberOfFaces))
								indices.add(index);
							for(float vertex : ModelDataStorage.transformedVertices(new Vector3f(x, y, z), 1))
								vertices.add(vertex);
							for(float normal : ModelDataStorage.NORMALS_BACK.clone())
								normals.add(normal);
							numberOfFaces++;
						}
					}
				}
			}
		}
		
		//Converts Lists into Arrays
		float[] verticesArray = new float[vertices.size()];
		float[] normalsArray = new float[normals.size()];
		float[] textureCoordsArray = new float[textureCoords.size()];
		int[] indicesArray = new int[indices.size()];
		
		for (int i = 0; i < normals.size(); i++) {
			normalsArray[i] = normals.get(i);
		}
		
		for (int i = 0; i < textureCoords.size(); i++) {
			textureCoordsArray[i] = textureCoords.get(i);
		}
		
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		
		for (int i = 0; i < vertices.size(); i++) {
			verticesArray[i] = vertices.get(i);
		}
		
		ModelData data = new ModelData(verticesArray, textureCoordsArray, normalsArray, indicesArray, 0, RendererType.ChunkRenderer, indexX, indexY, indexZ);
		
		return data; //Returns the Modeldata
	}
	
	/**
	 * Generates the Water mesh of the Chunk
	 * @param chunk Chunk to load WaterMesh
	 * @return ModelData for WaterMesh
	 */
	public static ModelData generateWaterMesh(int[][][] blocks, float indexX, float indexY, float indexZ) {
		List<Integer> indices = new ArrayList<Integer>();
		List<Float> vertices = new ArrayList<Float>();
		List<Float> textureCoords = new ArrayList<Float>();
		
		boolean hasWater = false;
		
		int numberOfFaces = 0;
		
		for (int x = 1; x < Chunk.CHUNK_SIZE + 1; x++) {
			for (int z = 1; z < Chunk.CHUNK_SIZE + 1; z++) {
				for (int y = 1; y < Chunk.CHUNK_SIZE + 1; y++) {
					int block = blocks[x][y][z];
					boolean isWaterAbove;
					if (y < Chunk.CHUNK_SIZE + 1)
						isWaterAbove = blocks[x][y + 1][z] == 5;
					else
						isWaterAbove = false;
					if (block == 5 && !isWaterAbove) {
						for(int index : ModelDataStorage.transformedWaterIndices(numberOfFaces))
							indices.add(index);
						for(float vertex : ModelDataStorage.transformedWaterVertices(new Vector3f(x, y, z), WorldGenerator.WATER_HEIGHT_OFFSET))
							vertices.add(vertex);
						for(float textureCoord : ModelDataStorage.transformedWaterTextureCoords(4))
							textureCoords.add(textureCoord);
						numberOfFaces++;
						hasWater = true;
					}
				}
			}
		}
		
		float[] verticesArray = new float[vertices.size()];
		float[] texureCoordsArray = new float[textureCoords.size()];
		int[] indicesArray = new int[indices.size()];
		
		for (int i = 0; i < textureCoords.size(); i++) {
			texureCoordsArray[i] = textureCoords.get(i);
		}
		
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		
		for (int i = 0; i < vertices.size(); i++) {
			verticesArray[i] = vertices.get(i);
		}
		
		ModelData model = new ModelData(verticesArray, texureCoordsArray, null, indicesArray, 0, RendererType.WaterRenderer, indexX, indexY, indexZ);
		
		if (hasWater)
			return model;
		else
			return null;
	}
}
