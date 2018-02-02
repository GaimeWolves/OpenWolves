package com.gamewolves.planeterra.world.chunkmanager.chunk;

/**
 * All the states of the Chunk
 */

public enum ChunkState {
	INSTANTIATED,
	GENERATED,
	CHUNK_MESHDATA_LOADED,
	CHUNK_MESH_LOADED,
	WATER_MESHDATA_LOADED,
	LOADED,
	MODIFIED,
	UNLOADED,
	LOADING
}
