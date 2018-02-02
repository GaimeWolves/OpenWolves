package com.gamewolves.planeterra.world.chunkmanager.chunk.water;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.gamewolves.planeterra.player.Camera;
import com.gamewolves.planeterra.render.Display;
import com.gamewolves.planeterra.shader.Shader;
import com.gamewolves.planeterra.util.Maths;

public class WaterShader extends Shader {

	private static final String VERTEX_FILE = "src/com/gamewolves/planeterra/world/chunkmanager/chunk/water/vertex.vert";
	private static final String GEOMETRY_FILE = "src/com/gamewolves/planeterra/world/chunkmanager/chunk/water/geometry.geom";
	private static final String FRAGMENT_FILE = "src/com/gamewolves/planeterra/world/chunkmanager/chunk/water/fragment.frag";
	
	private int location_time;
	private int location_cameraPosition;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_transformation;
	
	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, GEOMETRY_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_time = super.getUniformLocation("time");
		location_cameraPosition = super.getUniformLocation("cameraPosition");
		location_transformation = super.getUniformLocation("transformation");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformation, matrix);
	}
	
	public void loadTime() {
		super.loadFloat(location_time, Display.getCurrentTime() / 10000.f);
	}
	
	public void loadCameraPosition(Vector3f postition) {
		super.loadVector3f(location_cameraPosition, postition);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(location_viewMatrix, Maths.createViewMatrix(camera));
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "vertex_textureCoords");
	}
}
