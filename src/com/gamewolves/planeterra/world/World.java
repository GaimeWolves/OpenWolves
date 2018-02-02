package com.gamewolves.planeterra.world;

import java.util.Random;

import org.joml.Vector3f;

import com.gamewolves.planeterra.player.Player;
import com.gamewolves.planeterra.player.PlayerCallback;
import com.gamewolves.planeterra.world.chunkmanager.ChunkManager;
import com.gamewolves.planeterra.world.chunkmanager.chunk.Chunk;
import com.gamewolves.planeterra.world.generation.WorldGenerator;

public class World implements PlayerCallback{
	
	private final long SEED = (new Random()).nextLong();
	private final String WORLD_NAME = "debug";
	
	private ChunkManager chunkManager;

	public World() {
		WorldGenerator.init(SEED);
		chunkManager = new ChunkManager(this);
	}
	
	public void update() {
		chunkManager.update();
	}
	
	public void cleanUp() {
		chunkManager.cleanUp();
	}

	@Override
	public void onChunkChanged(Vector3f currentChunk) {
		chunkManager.checkChunks(currentChunk);
	}
	
	public void addPlayer(Player player) {
		player.setCallback(this);
		
		for (float x = player.getCurrentChunk().x - Player.getRenderdistance(); x < player.getCurrentChunk().x + Player.getRenderdistance(); x++) {
			for (float y = player.getCurrentChunk().y - Player.getRenderdistance(); y < player.getCurrentChunk().y + Player.getRenderdistance(); y++) {
				for (float z = player.getCurrentChunk().z - Player.getRenderdistance(); z < player.getCurrentChunk().z + Player.getRenderdistance(); z++) {
					if (y < 0)
						continue;
					if (Math.sqrt(Math.pow(x - player.getCurrentChunk().x, 2) + Math.pow(y - player.getCurrentChunk().y, 2) + Math.pow(z - player.getCurrentChunk().z, 2)) > Player.getRenderdistance())
						continue;
					if (chunkManager.getChunkWithIndex(new Vector3f(x, y, z)) == null) {
						chunkManager.addChunk(new Vector3f(x, y, z));
					}
				}
			}
		}
	}

	public String getWorldName() {
		return WORLD_NAME;
	}

	public ChunkManager getChunkManager() {
		return chunkManager;
	}
}
