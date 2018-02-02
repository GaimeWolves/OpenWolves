package com.gamewolves.planeterra.world.chunkmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Vector3f;

import com.gamewolves.planeterra.model.Model;
import com.gamewolves.planeterra.player.Player;
import com.gamewolves.planeterra.render.RendererType;
import com.gamewolves.planeterra.world.World;
import com.gamewolves.planeterra.world.chunkmanager.chunk.Chunk;
import com.gamewolves.planeterra.world.chunkmanager.chunk.ChunkState;

public class ChunkManager {

	private List<Vector3f> new_Chunks;
	private List<Chunk> instantiated_Chunks;
	private List<Chunk> generated_Chunks;
	private List<Chunk> chunkMeshDataLoaded_Chunks;
	private List<Chunk> chunkMeshLoaded_Chunks;
	private List<Chunk> waterMeshDataLoaded_Chunks;
	private List<Chunk> loaded_Chunks;
	private List<Chunk> rendered_Chunks;
	private List<Chunk> modified_Chunks;
	private List<Chunk> unloaded_Chunks;

	private List<Chunk> renderedChunksCopy;

	private List<Model> loadedMeshes;

	private World world;

	/**
	 * Initializes the ChunkManager and get a reference of the World
	 * 
	 * @param world
	 *            World
	 */
	public ChunkManager(World world) {
		new_Chunks = new CopyOnWriteArrayList<Vector3f>();
		instantiated_Chunks = new CopyOnWriteArrayList<Chunk>();
		generated_Chunks = new CopyOnWriteArrayList<Chunk>();
		chunkMeshDataLoaded_Chunks = new CopyOnWriteArrayList<Chunk>();
		chunkMeshLoaded_Chunks = new CopyOnWriteArrayList<Chunk>();
		waterMeshDataLoaded_Chunks = new CopyOnWriteArrayList<Chunk>();
		loaded_Chunks = new CopyOnWriteArrayList<Chunk>();
		rendered_Chunks = new CopyOnWriteArrayList<Chunk>();
		modified_Chunks = new CopyOnWriteArrayList<Chunk>();
		unloaded_Chunks = new CopyOnWriteArrayList<Chunk>();

		renderedChunksCopy = new CopyOnWriteArrayList<Chunk>();

		loadedMeshes = new CopyOnWriteArrayList<Model>();

		this.world = world;
	}

	/**
	 * Updates all the Chunks
	 */
	public void update() {
		instantiateChunks();
		generateChunks();
		updateModifiedChunks();
		loadChunkMeshDatas();
		loadChunkMeshes();
		loadWaterMeshDatas();
		loadWaterMeshes();
		calculateRenderableChunks();
		deleteChunks();
	}

	/**
	 * Instantiates the Chunks
	 */
	private synchronized void instantiateChunks() {
		if (new_Chunks.isEmpty())
			return;
		for (Vector3f chunk : new_Chunks) {
			instantiated_Chunks.add(new Chunk(chunk));
			new_Chunks.remove(chunk);
		}
	}

	/**
	 * Generates the Chunks
	 */
	private synchronized void generateChunks() {
		if (instantiated_Chunks.isEmpty())
			return;
		for (Chunk chunk : instantiated_Chunks) {
			if (chunk.getState() == ChunkState.GENERATED) {
				generated_Chunks.add(chunk);
				instantiated_Chunks.remove(chunk);
				continue;
			}
			if (chunk.getState() != ChunkState.LOADING) {
				chunk.generateTerrain(world.getWorldName());
			}
		}
	}

	/**
	 * Loads the Chunks MeshDatas
	 */
	private synchronized void loadChunkMeshDatas() {
		if (generated_Chunks.isEmpty())
			return;
		for (Chunk chunk : generated_Chunks) {
			if (chunk.getState() == ChunkState.LOADED) {
				loaded_Chunks.add(chunk);
				generated_Chunks.remove(chunk);
				continue;
			}
			if (chunk.getState() == ChunkState.CHUNK_MESHDATA_LOADED) {
				chunkMeshDataLoaded_Chunks.add(chunk);
				generated_Chunks.remove(chunk);
				continue;
			}
			if (chunk.getState() != ChunkState.LOADING) {
				chunk.generateChunkMesh(getNeighboringChunks(chunk.getIndex()));
			}
		}
	}

	/**
	 * Loads the Chunks Meshes
	 */
	private synchronized void loadChunkMeshes() {
		if (chunkMeshDataLoaded_Chunks.isEmpty())
			return;
		for (Chunk chunk : chunkMeshDataLoaded_Chunks) {
			if (chunk.getState() == ChunkState.CHUNK_MESH_LOADED) {
				chunkMeshLoaded_Chunks.add(chunk);
				chunkMeshDataLoaded_Chunks.remove(chunk);
				continue;
			}
			if (chunk.getState() != ChunkState.LOADING) {
				synchronized (loadedMeshes) {
					for (Model mesh : loadedMeshes) {
						if (mesh.getRenderer() == RendererType.ChunkRenderer && mesh.getIndexX() == chunk.getIndex().x
								&& mesh.getIndexY() == chunk.getIndex().y && mesh.getIndexZ() == chunk.getIndex().z) {
							chunk.loadChunkMesh(mesh);
							loadedMeshes.remove(mesh);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Loads the Chunks Water MeshDatas
	 */
	private synchronized void loadWaterMeshDatas() {
		if (chunkMeshLoaded_Chunks.isEmpty())
			return;
		for (Chunk chunk : chunkMeshLoaded_Chunks) {
			if (chunk.getState() == ChunkState.LOADED) {
				loaded_Chunks.add(chunk);
				chunkMeshLoaded_Chunks.remove(chunk);
				continue;
			}
			if (chunk.getState() == ChunkState.WATER_MESHDATA_LOADED) {
				waterMeshDataLoaded_Chunks.add(chunk);
				chunkMeshLoaded_Chunks.remove(chunk);
				continue;
			}
			if (chunk.getState() != ChunkState.LOADING) {
				chunk.generateWaterMesh(getNeighboringChunks(chunk.getIndex()));
			}
		}
	}

	/**
	 * Loads the Chunks Water Meshes
	 */
	private synchronized void loadWaterMeshes() {
		if (waterMeshDataLoaded_Chunks.isEmpty())
			return;
		for (Chunk chunk : waterMeshDataLoaded_Chunks) {
			if (chunk.getState() == ChunkState.LOADED) {
				loaded_Chunks.add(chunk);
				waterMeshDataLoaded_Chunks.remove(chunk);
				continue;
			}
			if (chunk.getState() != ChunkState.LOADING) {
				synchronized (loadedMeshes) {
					for (Model mesh : loadedMeshes) {
						if (mesh.getRenderer() == RendererType.WaterRenderer && mesh.getIndexX() == chunk.getIndex().x
								&& mesh.getIndexY() == chunk.getIndex().y && mesh.getIndexZ() == chunk.getIndex().z) {
							chunk.loadWaterMesh(mesh);
							loadedMeshes.remove(mesh);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Calculates the Chunks that can possibly be seen by the Player
	 */
	private synchronized void calculateRenderableChunks() {
		if (loaded_Chunks.isEmpty())
			return;
		synchronized (rendered_Chunks) {
			rendered_Chunks = new CopyOnWriteArrayList<Chunk>(getAllChunks());
			
			for (Chunk chunk : rendered_Chunks) {
				if (chunk.getChunkModel() == null) rendered_Chunks.remove(chunk);
			}
		}
		renderedChunksCopy = new CopyOnWriteArrayList<Chunk>(rendered_Chunks);
	}
	
	public void modifyBlock(float[] position) {
		for (Chunk chunk : loaded_Chunks) {
			if (chunk.getIndex().x == position[0] && chunk.getIndex().y == position[1] && chunk.getIndex().z == position[2]) {
				chunk.removeBlock((int)position[3], (int)position[4], (int)position[5]);
				loaded_Chunks.remove(chunk);
				modified_Chunks.add(chunk);
			}
		}
	}

	private void updateModifiedChunks() {
		for (Chunk chunk : modified_Chunks) {
			if (chunk.getState() == ChunkState.MODIFIED) {
				chunk.setState(ChunkState.GENERATED);
				for (Chunk neighbour : getAllChunks()) {
					float cx = chunk.getIndex().x;
					float cy = chunk.getIndex().y;
					float cz = chunk.getIndex().z;
					float nx = neighbour.getIndex().x;
					float ny = neighbour.getIndex().y;
					float nz = neighbour.getIndex().z;
					if (nx == cx - 1 && ny == cy && nz == cz || nx == cx + 1 && ny == cy && nz == cz
							|| nx == cx && ny == cy - 1 && nz == cz || nx == cx && ny == cy + 1 && nz == cz
							|| nx == cx && ny == cy && nz == cz - 1 || nx == cx && ny == cy && nz == cz + 1)
						neighbour.setState(ChunkState.GENERATED);
					
					generated_Chunks.remove(neighbour);
					instantiated_Chunks.remove(neighbour);
					loaded_Chunks.remove(neighbour);
					chunkMeshDataLoaded_Chunks.remove(neighbour);
					generated_Chunks.remove(neighbour);
					waterMeshDataLoaded_Chunks.remove(neighbour);
					chunkMeshLoaded_Chunks.remove(neighbour);
					modified_Chunks.remove(neighbour);
					
					generated_Chunks.add(neighbour);
				}
				modified_Chunks.remove(chunk);
				generated_Chunks.add(chunk);
			} else {
				throw new IllegalStateException("Chunk not in modified State in modified List!");
			}
		}
	}

	/**
	 * Deletes the Chunks
	 */
	private void deleteChunks() {
		for (Chunk chunk : unloaded_Chunks) {
			if (chunk.getState() == ChunkState.UNLOADED) {
				unloaded_Chunks.remove(chunk);
				continue;
			}
			if (chunk.getState() != ChunkState.LOADING) {
				chunk.deleteChunk();
			}
		}
	}

	private CopyOnWriteArrayList<Chunk> getAllChunks() {
		CopyOnWriteArrayList<Chunk> chunks = new CopyOnWriteArrayList<Chunk>();

		chunks.addAll(generated_Chunks);
		chunks.addAll(instantiated_Chunks);
		chunks.addAll(loaded_Chunks);
		chunks.addAll(chunkMeshDataLoaded_Chunks);
		chunks.addAll(chunkMeshLoaded_Chunks);
		chunks.addAll(waterMeshDataLoaded_Chunks);

		return chunks;
	}

	public void checkChunks(Vector3f index) {
		new Thread(() -> {
			CopyOnWriteArrayList<Chunk> chunks = getAllChunks();

			for (Chunk chunk : chunks) {
				if (chunk.getIndex().distance(index) > Player.getRenderdistance() + 1) {
					deleteChunk(chunk.getIndex());
					generated_Chunks.remove(chunk);
					instantiated_Chunks.remove(chunk);
					loaded_Chunks.remove(chunk);
					chunkMeshDataLoaded_Chunks.remove(chunk);
					generated_Chunks.remove(chunk);
					waterMeshDataLoaded_Chunks.remove(chunk);
					chunkMeshLoaded_Chunks.remove(chunk);
				}
			}

			for (float x = index.x - Player.getRenderdistance(); x < index.x + Player.getRenderdistance(); x++) {
				for (float y = index.y - Player.getRenderdistance(); y < index.y + Player.getRenderdistance(); y++) {
					for (float z = index.z - Player.getRenderdistance(); z < index.z + Player.getRenderdistance(); z++) {
						if (y < 0)
							continue;
						if (Math.sqrt(Math.pow(x - index.x, 2) + Math.pow(y - index.y, 2) + Math.pow(z - index.z, 2)) > Player.getRenderdistance())
							continue;
						if (getChunkWithIndex(new Vector3f(x, y, z)) == null) {
							instantiated_Chunks.add(new Chunk(new Vector3f(x, y, z)));
						}
					}
				}
			}
		}).start();
	}

	/**
	 * Gets a chunk with a specific Index
	 * @param index Index
	 * @return Chunk with index
	 */
	public Chunk getChunkWithIndex(Vector3f index) {
		CopyOnWriteArrayList<Chunk> chunks = getAllChunks();
		for (Chunk chunk : chunks) {
			if (index.x == chunk.getIndex().x && index.y == chunk.getIndex().y && index.z == chunk.getIndex().z)
				return chunk;
		}
		return null;
	}

	/**
	 * Deletes one Chunk
	 * 
	 * @param index Index of Chunk
	 */
	public void deleteChunk(Vector3f index) {
		for (Chunk chunk : loaded_Chunks) {
			if (chunk.getIndex().x == index.x && chunk.getIndex().y == index.y && chunk.getIndex().z == index.z) {
				unloaded_Chunks.add(chunk);
				loaded_Chunks.remove(chunk);
				return;
			}
		}
	}

	/**
	 * Deletes all Chunks
	 */
	public void cleanUp() {
		for (Chunk chunk : loaded_Chunks) {
			unloaded_Chunks.add(chunk);
			loaded_Chunks.remove(chunk);
		}
		deleteChunks();
	}

	/**
	 * @return List of Chunks that will be rendered
	 */
	public List<Chunk> getRendered_Chunks() {
		return renderedChunksCopy;
	}

	/**
	 * Gets the neighboring chunks
	 * 
	 * @param index
	 *            Index of Chunk
	 * @return List of neighboring Chunks
	 */
	private List<Chunk> getNeighboringChunks(Vector3f index) {
		List<Chunk> neighbors = new ArrayList<Chunk>();

		Chunk frontChunk = null;
		Chunk rightChunk = null;
		Chunk backChunk = null;
		Chunk leftChunk = null;
		Chunk upChunk = null;
		Chunk downChunk = null;

		float cx = index.x;
		float cy = index.y;
		float cz = index.z;

		synchronized (generated_Chunks) {
			for (Chunk chunk : generated_Chunks) {
				float nx = chunk.getIndex().x;
				float ny = chunk.getIndex().y;
				float nz = chunk.getIndex().z;
				if (nx == cx - 1 && ny == cy && nz == cz)
					leftChunk = chunk;
				else if (nx == cx + 1 && ny == cy && nz == cz)
					rightChunk = chunk;
				else if (nx == cx && ny == cy - 1 && nz == cz)
					downChunk = chunk;
				else if (nx == cx && ny == cy + 1 && nz == cz)
					upChunk = chunk;
				else if (nx == cx && ny == cy && nz == cz - 1)
					frontChunk = chunk;
				else if (nx == cx && ny == cy && nz == cz + 1)
					backChunk = chunk;
			}
		}

		synchronized (chunkMeshDataLoaded_Chunks) {
			for (Chunk chunk : chunkMeshDataLoaded_Chunks) {
				float nx = chunk.getIndex().x;
				float ny = chunk.getIndex().y;
				float nz = chunk.getIndex().z;
				if (nx == cx - 1 && ny == cy && nz == cz)
					leftChunk = chunk;
				else if (nx == cx + 1 && ny == cy && nz == cz)
					rightChunk = chunk;
				else if (nx == cx && ny == cy - 1 && nz == cz)
					downChunk = chunk;
				else if (nx == cx && ny == cy + 1 && nz == cz)
					upChunk = chunk;
				else if (nx == cx && ny == cy && nz == cz - 1)
					frontChunk = chunk;
				else if (nx == cx && ny == cy && nz == cz + 1)
					backChunk = chunk;
			}
		}

		synchronized (chunkMeshLoaded_Chunks) {
			for (Chunk chunk : chunkMeshLoaded_Chunks) {
				float nx = chunk.getIndex().x;
				float ny = chunk.getIndex().y;
				float nz = chunk.getIndex().z;
				if (nx == cx - 1 && ny == cy && nz == cz)
					leftChunk = chunk;
				else if (nx == cx + 1 && ny == cy && nz == cz)
					rightChunk = chunk;
				else if (nx == cx && ny == cy - 1 && nz == cz)
					downChunk = chunk;
				else if (nx == cx && ny == cy + 1 && nz == cz)
					upChunk = chunk;
				else if (nx == cx && ny == cy && nz == cz - 1)
					frontChunk = chunk;
				else if (nx == cx && ny == cy && nz == cz + 1)
					backChunk = chunk;
			}
		}

		synchronized (waterMeshDataLoaded_Chunks) {
			for (Chunk chunk : waterMeshDataLoaded_Chunks) {
				float nx = chunk.getIndex().x;
				float ny = chunk.getIndex().y;
				float nz = chunk.getIndex().z;
				if (nx == cx - 1 && ny == cy && nz == cz)
					leftChunk = chunk;
				else if (nx == cx + 1 && ny == cy && nz == cz)
					rightChunk = chunk;
				else if (nx == cx && ny == cy - 1 && nz == cz)
					downChunk = chunk;
				else if (nx == cx && ny == cy + 1 && nz == cz)
					upChunk = chunk;
				else if (nx == cx && ny == cy && nz == cz - 1)
					frontChunk = chunk;
				else if (nx == cx && ny == cy && nz == cz + 1)
					backChunk = chunk;
			}
		}

		synchronized (loaded_Chunks) {
			for (Chunk chunk : loaded_Chunks) {
				float nx = chunk.getIndex().x;
				float ny = chunk.getIndex().y;
				float nz = chunk.getIndex().z;
				if (nx == cx - 1 && ny == cy && nz == cz)
					leftChunk = chunk;
				else if (nx == cx + 1 && ny == cy && nz == cz)
					rightChunk = chunk;
				else if (nx == cx && ny == cy - 1 && nz == cz)
					downChunk = chunk;
				else if (nx == cx && ny == cy + 1 && nz == cz)
					upChunk = chunk;
				else if (nx == cx && ny == cy && nz == cz - 1)
					frontChunk = chunk;
				else if (nx == cx && ny == cy && nz == cz + 1)
					backChunk = chunk;
			}
		}

		neighbors.add(frontChunk);
		neighbors.add(rightChunk);
		neighbors.add(backChunk);
		neighbors.add(leftChunk);
		neighbors.add(upChunk);
		neighbors.add(downChunk);

		return neighbors;
	}

	/**
	 * Adds a chunk
	 * 
	 * @param index
	 *            Index of Chunk
	 */
	public synchronized void addChunk(Vector3f index) {
		new_Chunks.add(index);
	}

	/**
	 * Adds a loaded Mesh
	 * 
	 * @param model
	 *            Mesh
	 */
	public void addLoadedMesh(Model model) {
		synchronized (loadedMeshes) {
			loadedMeshes.add(model);
		}
	}
}
