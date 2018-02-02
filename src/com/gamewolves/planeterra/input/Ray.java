package com.gamewolves.planeterra.input;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.gamewolves.planeterra.player.Camera;
import com.gamewolves.planeterra.util.Maths;
import com.gamewolves.planeterra.world.World;
import com.gamewolves.planeterra.world.chunkmanager.chunk.Chunk;

public class Ray {
	
	public static final int ITERATION_SIZE = 100;
	
	public enum CollisionType {
		Block, Entity
	}
	
	Vector3f origin, direction;
	private float range;
	private CollisionType collisionType;
	private Camera camera;
	
	public Ray(float range, CollisionType collisionType, Camera camera) {
		this.range = range;
		this.collisionType = collisionType;
		this.camera = camera;
	}
	
	public Ray(Vector3f origin, Vector3f direction, float range, CollisionType collisionType, Camera camera) {
		this.range = range;
		this.collisionType = collisionType;
		this.origin = origin;
		this.direction = direction;
		this.camera = camera;
	}
	
	public void castRay(Vector3f origin, Vector3f direction) {
		this.origin = origin;
		this.direction = direction;
	}
	
	public Vector3f getCollisionPosition() {
		Vector3f position = new Vector3f();
		
		
		
		return position;
	}
	
	/**
	 * Gets a block Collision
	 * @param world World
	 * @return { chunkIndex.xyz, blockIndex.xyz, blockID }
	 */
	public float[] getBlockCollision(World world) {
		
		///////Possible Chunks///////
		Vector3f currentChunk = new Vector3f(Math.round(origin.x / Chunk.CHUNK_SIZE), Math.round(origin.y / Chunk.CHUNK_SIZE), Math.round(origin.z / Chunk.CHUNK_SIZE));
		List<Chunk> possibleChunks = new ArrayList<Chunk>();
		for (float x = currentChunk.x - 1; x <= currentChunk.x + 1; x++) {
			for (float y = currentChunk.y - 1; y <= currentChunk.y + 1; y++) {
				for (float z = currentChunk.z - 1; z <= currentChunk.z + 1; z++) {
					possibleChunks.add(world.getChunkManager().getChunkWithIndex(new Vector3f((int)x, (int)y, (int)z)));
				}
			}
		}
		
		///////Get Possible Blocks///////
		List<Vector3f> possibleBlocks = new ArrayList<Vector3f>();
		List<Chunk> inChunks = new ArrayList<Chunk>();
		for (int i = 0; i < possibleChunks.size(); i++) {
			Chunk chunk = possibleChunks.get(i);
			if (chunk == null) continue;
			for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
				for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
					for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
						if (chunk.getBlocks() == null) continue;
						if (chunk.getBlocks()[x][y][z] == 0 && chunk.getBlocks()[x][y][z] == 5) continue;
						if (origin.distance(new Vector3f(x + (chunk.getIndex().x * Chunk.CHUNK_SIZE),y + (chunk.getIndex().y * Chunk.CHUNK_SIZE),z + (chunk.getIndex().z * Chunk.CHUNK_SIZE))) <= range + 2) {
							possibleBlocks.add(new Vector3f(x, y, z));
							inChunks.add(chunk);
						}
					}
				}
			}
		}
		
		float[] nearestBlock = new float[] { -1, -1, -1, -1, -1, -1, -1, -1 };
		
		/*
		
		Vector3f currentRayPos = new Vector3f(origin);
		Vector3f rayDirection = new Vector3f(direction);
		rayDirection.mul(range);
		
		int multiplier = 1;
		
		for (int i = 1; i < ITERATION_SIZE; i++) {
			rayDirection.mul(0.5f);
			if (multiplier == 1) {
				currentRayPos.add(rayDirection);
			} else {
				currentRayPos.sub(rayDirection);
			}
			
			for (int j = 0; j < possibleBlocks.size(); j++) {
				Chunk chunk = inChunks.get(j);
				Vector3f block = possibleBlocks.get(j);
				Vector3f blockPos = new Vector3f(block.x + (chunk.getIndex().x * Chunk.CHUNK_SIZE), block.y + (chunk.getIndex().y * Chunk.CHUNK_SIZE), block.z + (chunk.getIndex().z * Chunk.CHUNK_SIZE));
				
				if (currentRayPos.distance(blockPos) <= Math.sqrt(3)) {
					float minX = blockPos.x - 0.5f;
					float maxX = blockPos.x + 0.5f;
					float minY = blockPos.y - 0.5f;
					float maxY = blockPos.y + 0.5f;
					float minZ = blockPos.z - 0.5f;
					float maxZ = blockPos.z + 0.5f;
					float x = currentRayPos.x;
					float y = currentRayPos.y;
					float z = currentRayPos.z;
					
					if (x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ) {
						if (chunk.getBlocks()[(int) block.x][(int) block.y][(int) block.z] == 0 || chunk.getBlocks()[(int) block.x][(int) block.y][(int) block.z] == 5)  {
							multiplier = 1;
						} else {
							multiplier = -1;
							nearestBlock = new float[] { chunk.getIndex().x, chunk.getIndex().y, chunk.getIndex().z, block.x, block.y, block.z, chunk.getBlocks()[(int) block.x][(int) block.y][(int) block.z] };
						}
					}
				}
			}
		}
		
		return nearestBlock;
		
		*/
		
		///////Get Raycasted Block///////
		Vector3f currentRayPos = new Vector3f(origin);
		Vector3f rayStep = new Vector3f(direction);
		rayStep.mul(0.01f);
		while(currentRayPos.distance(origin) <= range) {
			currentRayPos.add(rayStep);
			for (int i = 0; i < possibleBlocks.size(); i++) {
				Chunk chunk = inChunks.get(i);
				Vector3f block = possibleBlocks.get(i);
				Vector3f blockPos = new Vector3f(block.x + (chunk.getIndex().x * Chunk.CHUNK_SIZE + 1), block.y + (chunk.getIndex().y * Chunk.CHUNK_SIZE + 1), block.z + (chunk.getIndex().z * Chunk.CHUNK_SIZE + 1));
				if (currentRayPos.distance(blockPos) <= Math.sqrt(3)) {
					float minX = blockPos.x - 0.5f;
					float maxX = blockPos.x + 0.5f;
					float minY = blockPos.y - 0.5f;
					float maxY = blockPos.y + 0.5f;
					float minZ = blockPos.z - 0.5f;
					float maxZ = blockPos.z + 0.5f;
					float x = currentRayPos.x;
					float y = currentRayPos.y;
					float z = currentRayPos.z;
					
					if (x > minX && x < maxX && y > minY && y < maxY && z > minZ && z < maxZ) {
						
						/*
						if (nearestBlock[4] != -1) {
							Vector3f posOldBlock = new Vector3f(nearestBlock[3] + (nearestBlock[0] * Chunk.CHUNK_SIZE), nearestBlock[4] + (nearestBlock[1] * Chunk.CHUNK_SIZE), nearestBlock[5] + (nearestBlock[2] * Chunk.CHUNK_SIZE));
						
							if (currentRayPos.distance(blockPos) < currentRayPos.distance(posOldBlock)) {
								nearestBlock = new float[] { chunk.getIndex().x, chunk.getIndex().y, chunk.getIndex().z, block.x, block.y, block.z, chunk.getBlocks()[(int) block.x][(int) block.y][(int) block.z], -1 };
							}
						} else {
							nearestBlock = new float[] { chunk.getIndex().x, chunk.getIndex().y, chunk.getIndex().z, block.x, block.y, block.z, chunk.getBlocks()[(int) block.x][(int) block.y][(int) block.z], -1 };
						}
						*/
						
						nearestBlock = new float[] { chunk.getIndex().x, chunk.getIndex().y, chunk.getIndex().z, block.x, block.y, block.z, chunk.getBlocks()[(int) block.x][(int) block.y][(int) block.z], -1 };
						return nearestBlock;
					}
				}
			}
		}
		
		return nearestBlock;
	}
	
	public void castRay(Vector2f screenCoords) {
		origin = camera.position;
		direction = Maths.calculateRay(screenCoords, camera);
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Vector3f getOrigin() {
		return origin;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public void setCollisionType(CollisionType collisionType) {
		this.collisionType = collisionType;
	}
}
