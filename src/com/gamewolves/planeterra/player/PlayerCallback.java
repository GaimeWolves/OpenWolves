package com.gamewolves.planeterra.player;

import org.joml.Vector3f;

public interface PlayerCallback {

	public void onChunkChanged(Vector3f currentChunk);
}
