package com.gamewolves.planeterra.world.chunkmanager.chunk;

import java.util.List;

import org.joml.Vector3f;

import com.gamewolves.planeterra.model.Model;
import com.gamewolves.planeterra.model.ModelData;
import com.gamewolves.planeterra.render.MasterRenderer;
import com.gamewolves.planeterra.world.generation.WorldGenerator;

public class Chunk {

	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_VOLUME = CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE;
	public static final int WORLDHEIGHT = 64;
	
	private int[][][] blocks;
	private Vector3f index;
	private ChunkState state;
	private ModelData chunkMeshData, waterMeshData;
	private Model chunkModel, waterModel;
	private boolean hasWater;
	private boolean isEmpty;
	
	/**
	 * Instantiates a Chunk
	 * @param index Index of Chunk
	 */
	public Chunk(Vector3f index) {
		this.index = index;
		state = ChunkState.INSTANTIATED;
		hasWater = false;
		isEmpty = true;
	}
	
	/**
	 * Copies a chunk
	 * @param chunk Chunk to copy
	 */
	public Chunk(Chunk chunk) {
		this.blocks = chunk.blocks;
		this.chunkMeshData = chunk.chunkMeshData;
		this.chunkModel = chunk.chunkModel;
		this.hasWater = chunk.hasWater;
		this.state = chunk.state;
		this.waterMeshData = chunk.waterMeshData;
		this.waterModel = chunk.waterModel;
		this.isEmpty = chunk.isEmpty;
	}
	
	/**
	 * Loads the Blocks for the Chunk if Chunkfile doesnt exist it creates one
	 * @param worldName
	 */
	@SuppressWarnings("unused")
	private void loadTerrain(String worldName) {
		if (false) {
			/**
		 	* Load File
		 	*/
		} else {
			//File does not exist: generate Terrain + File
			blocks = WorldGenerator.generateChunk(index.x, index.y, index.z);
		}
	}
	
	/**
	 * Generates the Terrain
	 * @param worldName
	 */
	public void generateTerrain(String worldName) {
		state = ChunkState.LOADING;
		loadTerrain(worldName);
		state = ChunkState.GENERATED;
	}
	
	/**
	 * Generates the Chunk Mesh
	 * @param neighbors The chunks neighbors
	 */
	public void generateChunkMesh(List<Chunk> neighbors) {
		state = ChunkState.LOADING;

		int[][][] block3DArray = getBlocksWithNeighbours(neighbors);
		
		checkChunk();
		
		if (isEmpty) {
			state = ChunkState.LOADED;
			if (chunkModel != null) {
				MasterRenderer.deleteModel(chunkModel);
				chunkModel = null;
			}
			return;
		}
		
		chunkMeshData = MeshBuilder.generateChunkMesh(block3DArray, MasterRenderer.getBlockTextures(), index.x, index.y, index.z); //Generating the chunks Mesh
		MasterRenderer.loadModel(chunkMeshData);
		
		state = ChunkState.CHUNK_MESHDATA_LOADED;
	}
	
	/**
	 * Generates the Water Mesh
	 * @param neighbors The chunks Neighbors
	 */
	public void generateWaterMesh(List<Chunk> neighbors) {
		state = ChunkState.LOADING;
		
		int[][][] blockArray = getBlocksWithNeighbours(neighbors);
		
		waterMeshData = MeshBuilder.generateWaterMesh(blockArray, index.x, index.y, index.z);
		if (waterMeshData == null) {
			state = ChunkState.LOADED;
		} else {
			MasterRenderer.loadModel(waterMeshData);
			hasWater = true;
			state = ChunkState.WATER_MESHDATA_LOADED;
		}
	}
	
	/**
	 * Saves the Chunks Mesh in a variable
	 * @param mesh Mesh
	 */
	public void loadChunkMesh(Model mesh) {
		state = ChunkState.LOADING;
		Model old = null;
		if (chunkModel != null) {
			old = new Model(chunkModel);
		}
		chunkModel = mesh;
		
		if (old != null)
			MasterRenderer.deleteModel(old);
		state = ChunkState.CHUNK_MESH_LOADED;
	}
	
	/**
	 * Saves the Chunks Water mesh in a variable
	 * @param mesh Mesh
	 */
	public void loadWaterMesh(Model mesh) {
		state = ChunkState.LOADING;
		waterModel = mesh;
		state = ChunkState.LOADED;
	}
	
	/**
	 * Deletes both Meshes of the Chunk
	 */
	public void deleteChunk() {
		state = ChunkState.LOADING;
		if (chunkModel != null)
			MasterRenderer.deleteModel(chunkModel);
		if (waterModel != null)
			MasterRenderer.deleteModel(waterModel);
		chunkModel = null;
		waterModel = null;
		state = ChunkState.UNLOADED;
	}
	
	/**
	 * Checks if the Chunk is Empty
	 */
	private void checkChunk() {
		isEmpty = true;
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int z = 0; z < CHUNK_SIZE; z++) {
				for (int y = 0; y < CHUNK_SIZE; y++) {
					if(blocks == null) return;
					if (blocks[x][y][z] != 0) {
						isEmpty = false;
					}
				}
			}
		}
	}
	
	/**
	 * Gets the blocks of the Chunk with its neighboring blocks
	 * @param neighbors Neighbors of the chunk
	 * @return Array of blocks
	 */
	private int[][][] getBlocksWithNeighbours(List<Chunk> neighbors) {
		int[][][] block3DArray= new int[CHUNK_SIZE + 2][CHUNK_SIZE + 2][CHUNK_SIZE + 2]; //initializing the Array to be 1 Block wider as a Chunk on each side
		
		if (blocks == null) return block3DArray;
		
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int z = 0; z < CHUNK_SIZE; z++) {
				for (int y = 0; y < CHUNK_SIZE; y++) {
					block3DArray[x + 1][y + 1][z + 1] = blocks[x][y][z]; //Loading in the values from this Chunk//
				}
			}
		}
		for (int s = 0; s < 6; s++) {
			for (int w = 0; w < CHUNK_SIZE; w++) {
				for (int v = 0; v < CHUNK_SIZE; v++) {
					switch(s) {
					case 0: //Loading in edge Blocks of the front [ -z ]
						if (neighbors.get(s) != null && neighbors.get(s).getBlocks() != null)
							block3DArray[w + 1][v + 1][0] = neighbors.get(s).getBlocks()[w][v][CHUNK_SIZE - 1];
						else
							block3DArray[w + 1][v + 1][0] = 0;
						break;
					case 1: //Loading in edge Blocks of the right [ +x ]
						if (neighbors.get(s) != null && neighbors.get(s).getBlocks() != null)
							block3DArray[CHUNK_SIZE + 1][v + 1][w + 1] = neighbors.get(s).getBlocks()[0][v][w];
						else
							block3DArray[CHUNK_SIZE + 1][v + 1][w + 1] = 0;
						break;
					case 2: //Loading in edge Blocks of the back [ +z ]
						if (neighbors.get(s) != null && neighbors.get(s).getBlocks() != null)
							block3DArray[w + 1][v + 1][CHUNK_SIZE + 1] = neighbors.get(s).getBlocks()[w][v][0];
						else
							block3DArray[w + 1][v + 1][CHUNK_SIZE + 1] = 0;
						break;
					case 3: //Loading in edge Blocks of the left [ -x ]
						if (neighbors.get(s) != null && neighbors.get(s).getBlocks() != null)
							block3DArray[0][v + 1][w + 1] = neighbors.get(s).getBlocks()[CHUNK_SIZE - 1][v][w];
						else
							block3DArray[0][v + 1][w + 1] = 0;
						break;
					case 4: //Loading in edge Blocks of the top [ +y ]
						if (neighbors.get(s) != null && neighbors.get(s).getBlocks() != null)
							block3DArray[v + 1][CHUNK_SIZE + 1][w + 1] = neighbors.get(s).getBlocks()[v][0][w];
						else
							block3DArray[v + 1][CHUNK_SIZE + 1][w + 1] = 0;
						break;
					case 5: //Loading in edge Blocks on the bottom [ -y ]
						if (neighbors.get(s) != null && neighbors.get(s).getBlocks() != null)
							block3DArray[v + 1][0][w + 1] = neighbors.get(s).getBlocks()[v][CHUNK_SIZE - 1][w];
						else
							block3DArray[v + 1][0][w + 1] = 0;
						break;
					}
				}
			}
		}
		
		return block3DArray;
	}
	
	public void removeBlock(int x, int y, int z) {
		state = ChunkState.MODIFIED;
		blocks[x][y][z] = 0;
	}

	public ChunkState getState() {
		return state;
	}

	public void setState(ChunkState state) {
		this.state = state;
	}

	public int[][][] getBlocks() {
		return blocks;
	}

	public Vector3f getIndex() {
		return index;
	}

	public ModelData getChunkMeshData() {
		return chunkMeshData;
	}

	public ModelData getWaterMeshData() {
		return waterMeshData;
	}

	public Model getChunkModel() {
		return chunkModel;
	}

	public Model getWaterModel() {
		return waterModel;
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}

	public boolean hasWater() {
		return hasWater;
	}
}
