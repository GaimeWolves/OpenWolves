package com.gamewolves.planeterra.world.generation;

import java.util.Random;

import com.gamewolves.planeterra.util.Maths;
import com.gamewolves.planeterra.util.OpenSimplexNoise;
import com.gamewolves.planeterra.world.chunkmanager.chunk.Chunk;

public class WorldGenerator {
	
	public static final float HEIGHT_DIFF = 20;
	public static final int WATER_LEVEL = Chunk.WORLDHEIGHT * Chunk.CHUNK_SIZE - (int)(Chunk.WORLDHEIGHT * 0.75f);
	public static final float WATER_HEIGHT_OFFSET = -0.1f;
	public static final float HEIGHT = WATER_LEVEL + 10;
	public static final float STRECH_CONSTANT = 100;
	public static final float TREE_CONSTANT = STRECH_CONSTANT * 0.25f;
	public static final int TREE_RADIUS = 12;
	
	private static final Random random = new Random();
	private static OpenSimplexNoise noise;
	
	public static void init(long seed) {
		noise = new OpenSimplexNoise(seed);
	}

	/**
	 * Generates a Chunk
	 * @param indexX X-Index of Chunk
	 * @param indexZ Y-Index of Chunk
	 * @return Array of Blocks
	 */
	public static int[][][] generateChunk(float indexX, float indexY, float indexZ) {
		int[][][] blocks = new int[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];
		
		float[][] heightMap = new float[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];
		float[][] treeMap = new float[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];
		for (int w = 0; w < Chunk.CHUNK_SIZE; w++) {
			for (int v = 0; v < Chunk.CHUNK_SIZE; v++) {
				heightMap[w][v] = (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + w) / STRECH_CONSTANT, (float)(indexZ * Chunk.CHUNK_SIZE + v) / STRECH_CONSTANT)
						+ 0.5f * (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + w) / (STRECH_CONSTANT * 0.5f), (float)(indexZ * Chunk.CHUNK_SIZE + v) / (STRECH_CONSTANT * 0.5f))
						+ 0.25f * (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + w) / (STRECH_CONSTANT * 0.25f), (float)(indexZ * Chunk.CHUNK_SIZE + v) / (STRECH_CONSTANT * 0.25f));
				heightMap[w][v] *= HEIGHT_DIFF;
				heightMap[w][v] += HEIGHT;
				heightMap[w][v] = Math.round(heightMap[w][v]);
				
				treeMap[w][v] = (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + w) / TREE_CONSTANT, (float)(indexZ * Chunk.CHUNK_SIZE + v) / TREE_CONSTANT)
						+ 0.5f * (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + w) / (TREE_CONSTANT * 0.5f), (float)(indexZ * Chunk.CHUNK_SIZE + v) / (TREE_CONSTANT * 0.5f))
						+ 0.25f * (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + w) / (TREE_CONSTANT * 0.25f), (float)(indexZ * Chunk.CHUNK_SIZE + v) / (TREE_CONSTANT * 0.25f));
			}
		}
		
		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
					int worldY = (int) (y + indexY * Chunk.CHUNK_SIZE);
					if (heightMap[x][z] < worldY && worldY <= WATER_LEVEL) {
						blocks[x][y][z] = 5;
					} else { 
						//blocks[Chunk.getIndexfromPosition(x, y, z)] = 0;
					}
					
					int dirtHeight = -random.nextInt(3);
					if (worldY <= heightMap[x][z] - 5 - dirtHeight)  {
						blocks[x][y][z] = 3;
					}
					else if (worldY <= heightMap[x][z] - 1) { 
						blocks[x][y][z] = 2;
						if (hasWaterNear(x, z, indexX, indexZ)) {
							blocks[x][y][z] = 4;
						}
					}
					else if (worldY <= heightMap[x][z]) { 
						blocks[x][y][z] = 1;
						if (hasWaterNear(x, z, indexX, indexZ)) {
							blocks[x][y][z] = 4;
						}
					}
					
					if (worldY == heightMap[x][z] + 1 && x > 3 && x < Chunk.CHUNK_SIZE - 3 && z > 3 && z < Chunk.CHUNK_SIZE - 3 && worldY > WATER_LEVEL + 3) {
						double max = 0;
						for (int zn = z - TREE_RADIUS; zn <= z + TREE_RADIUS; zn++) {
							for (int xn = x - TREE_RADIUS; xn <= x + TREE_RADIUS; xn++) {
								int iX = (int)Maths.clamp(0, Chunk.CHUNK_SIZE - 1, (int)xn);
								int iZ = (int)Maths.clamp(0, Chunk.CHUNK_SIZE - 1, (int)zn);
								double e = treeMap[iX][iZ];
				        		if (e > max) { max = e; }
				      		}
				    	}
				    	if (treeMap[x][z] == max) {
				    		//blocks = placeTree(blocks, x, y, z);
				    	}
					}
				}
			}
		}
		
		return blocks;
	}
	
	public float[][] getHeightMap(int indexX, int indexZ) {
		float[][] heightMap = new float[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];
		for (int w = 0; w < Chunk.CHUNK_SIZE; w++) {
			for (int v = 0; v < Chunk.CHUNK_SIZE; v++) {
				heightMap[w][v] = (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + w) / STRECH_CONSTANT, (float)(indexZ * Chunk.CHUNK_SIZE + v) / STRECH_CONSTANT)
						+ 0.5f * (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + w) / (STRECH_CONSTANT * 0.5f), (float)(indexZ * Chunk.CHUNK_SIZE + v) / (STRECH_CONSTANT * 0.5f))
						+ 0.25f * (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + w) / (STRECH_CONSTANT * 0.25f), (float)(indexZ * Chunk.CHUNK_SIZE + v) / (STRECH_CONSTANT * 0.25f));
				heightMap[w][v] *= HEIGHT_DIFF;
				heightMap[w][v] += HEIGHT;
				heightMap[w][v] = Math.round(heightMap[w][v]);
			}
		}
		return heightMap;
	}
	
	public static int[][][] placeTree(int[][][] blocks, int x, int y, int z) {
		int[][][] newBlocks = blocks.clone();
		int trunkHeight = random.nextInt(3) + 5;
		int radius = random.nextInt(2) + 2;
		
		for (int iX = x - radius; iX <= x + radius; iX++) {
			for (int iY = y; iY <= y + radius + trunkHeight - 1; iY++) {
				for (int iZ = z - radius; iZ <= z + radius; iZ++) {
					if (iY >= y + trunkHeight - 1) {
						if (Math.round(Maths.euclidianDistance((float)iX - x, (float)iZ - z)) <= radius) {
							int jX = (int)Maths.clamp(0, Chunk.CHUNK_SIZE - 1, (int)iX);
							int jZ = (int)Maths.clamp(0, Chunk.CHUNK_SIZE - 1, (int)iZ);
							newBlocks[jX][iY][jZ] = 7;

						}
					}
					if (iY <= y + trunkHeight && iX == x && iZ == z) {
						if (iX >= 0 && iX < Chunk.CHUNK_SIZE && iZ >= 0 && iZ < Chunk.CHUNK_SIZE && iY >= 0 && iY < Chunk.CHUNK_SIZE)
							newBlocks[iX][iY][iZ] = 6;
					}
				}
			}
		}
		
		return newBlocks;
	}
	
	/**
	 * Checks if a Block has Water around it
	 */
	private static boolean hasWaterNear(int x, int z, float indexX, float indexZ) {
		for (int iX = x - (random.nextInt(2) + 1); iX < x + (random.nextInt(2) + 3); iX++) {
			for (int iZ = z - (random.nextInt(2) + 1); iZ < z + (random.nextInt(2) + 3); iZ++) {
				if (getHeightofHeightMap(iX, iZ, indexX, indexZ) < WATER_LEVEL) return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the Height of a Column in a Chunk
	 * @param x X-Position
	 * @param z Z-Position
	 * @param indexX X-Index of Chunk
	 * @param indexZ Y-Index of Chunk
	 * @return Height of Column
	 */
	private static int getHeightofHeightMap(int x, int z, float indexX, float indexZ) {
		float height;
		
		height = (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + x) / STRECH_CONSTANT, (float)(indexZ * Chunk.CHUNK_SIZE + z) / STRECH_CONSTANT)
				+ 0.5f * (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + x) / (STRECH_CONSTANT * 0.5f), (float)(indexZ * Chunk.CHUNK_SIZE + z) / (STRECH_CONSTANT * 0.5f))
				+ 0.25f * (float)noise.eval((float)(indexX * Chunk.CHUNK_SIZE + x) / (STRECH_CONSTANT * 0.25f), (float)(indexZ * Chunk.CHUNK_SIZE + z) / (STRECH_CONSTANT * 0.25f));
		height *= HEIGHT_DIFF;
		height += HEIGHT;
		
		return Math.round(height);
	}
}
