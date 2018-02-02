package com.gamewolves.planeterra.world.chunkmanager.chunk.block;

import org.joml.Matrix4f;

import com.gamewolves.planeterra.shader.Shader;

public class CubeShader extends Shader {

	private static final String VERTEX_FILE = "src/com/gamewolves/planeterra/world/chunkmanager/chunk/block/vertex.vert";
	private static final String FRAGMENT_FILE = "src/com/gamewolves/planeterra/world/chunkmanager/chunk/block/fragment.frag";
	
	private int location_mvpMatrix;
	
	public CubeShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");
	}
	
	public void loadMVPMatrix(Matrix4f matrix) {
		super.loadMatrix(location_mvpMatrix, matrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "vertex_textureCoords");
		super.bindAttribute(2, "normal");
	}
}
