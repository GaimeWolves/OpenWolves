package com.gamewolves.planeterra.world.chunkmanager;

public class BlockData {

	public final int ID;
	public final String name;
	public final int[] textureIndices;
	public final boolean isTransparent;
	
	/**
	 * Datastorage for Blocks
	 * @param ID Id of the Block
	 * @param name Name of the Block
	 * @param textureIndices TextureIndex for each side
	 */
	public BlockData(int ID, String name, int[] textureIndices, boolean isTransparent) {
		this.ID = ID;
		this.name = name;
		this.textureIndices = textureIndices;
		this.isTransparent = isTransparent;
	}
}
