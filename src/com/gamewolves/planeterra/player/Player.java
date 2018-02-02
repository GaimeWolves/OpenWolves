package com.gamewolves.planeterra.player;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.gamewolves.planeterra.input.InputHandler;
import com.gamewolves.planeterra.input.Ray;
import com.gamewolves.planeterra.input.Ray.CollisionType;
import com.gamewolves.planeterra.main.Game;
import com.gamewolves.planeterra.render.Display;
import com.gamewolves.planeterra.util.Maths;
import com.gamewolves.planeterra.world.chunkmanager.chunk.Chunk;
import com.gamewolves.planeterra.world.generation.WorldGenerator;

public class Player {
	
	private static final float RUN_SPEED = 5;
	private static final float TURN_SPEED = 20;
	private static final boolean IS_FLYING = true;
	private static final float RENDERDISTANCE = 10;
	
	private static final float FOV = 90;
	private static final float NEAR_CLIP_PLANE = 0.1f;
	private static final float FAR_CLIP_PLANE = 1000;
	
	private final String ID;

	private Camera camera;
	
	private Vector3f position, rotation;
	private Vector3f lastChunk, currentChunk;
	private PlayerCallback playerCallback;
	private float[] currentBlockPosition;
	private Ray ray;
	
	/**
	 * Creates a Player
	 */
	public Player() {
		camera = new Camera();
		position = new Vector3f(1, WorldGenerator.WATER_LEVEL, 1);
		rotation = new Vector3f(90, 0, 0);
		
		lastChunk = new Vector3f((float)Math.floor(position.x / Chunk.CHUNK_SIZE), (float)Math.floor(position.y / Chunk.CHUNK_SIZE), (float)Math.floor(position.z / Chunk.CHUNK_SIZE));
		currentChunk = new Vector3f(lastChunk);
		currentBlockPosition = new float[8];
		ray = new Ray(4, CollisionType.Block, camera);
		
		ID = String.valueOf((new Random()).nextLong());
	}
	
	public void setCallback(PlayerCallback playerCallback) {
		this.playerCallback = playerCallback;
	}
	
	/**
	 * Moves and rotates the player and it's camera
	 */
	private void handleInput() {
		boolean moved = false;
		
		float speed = RUN_SPEED;	
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) speed *= 2;
		if (IS_FLYING) speed *= 3;
		
		Vector3f rotation = this.rotation;
		rotation.mul(Maths.DEGREES_TO_RADIANS);
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_W)) {
			position.add(
					speed * (float)Math.sin(-rotation.y) * Display.getDeltaTimeInSeconds(), 0 , 
					speed * -(float)Math.cos(-rotation.y) * Display.getDeltaTimeInSeconds()
			);
			moved = true;
		}
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_A)) {
			position.add(
					speed * (float)Math.sin(-rotation.y - (Math.PI / 2)) * Display.getDeltaTimeInSeconds(), 0 , 
					speed * -(float)Math.cos(-rotation.y - (Math.PI / 2)) * Display.getDeltaTimeInSeconds()
			);
			moved = true;
		}
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_S)) {
			position.add(
					speed * -(float)Math.sin(-rotation.y) * Display.getDeltaTimeInSeconds(), 0 , 
					speed * (float)Math.cos(-rotation.y) * Display.getDeltaTimeInSeconds()
			);
			moved = true;
		}
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_D)) {
			position.add(
					speed * (float)Math.sin(-rotation.y + (Math.PI / 2)) * Display.getDeltaTimeInSeconds(), 0 , 
					speed * -(float)Math.cos(-rotation.y + (Math.PI / 2)) * Display.getDeltaTimeInSeconds()
			);
			moved = true;
		}
		rotation.mul(Maths.RADIANS_TO_DEGREES);
		
		////////TEMP////////
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_SPACE)) position.add(0, speed * Display.getDeltaTimeInSeconds(), 0);
		else if (InputHandler.isKeyDown(GLFW.GLFW_KEY_C)) position.add(0, -speed * Display.getDeltaTimeInSeconds(), 0);
		////////TEMP////////
		
		if (moved) {
			int indexX = (int)Math.floor(position.x / Chunk.CHUNK_SIZE);
			int indexY = (int)Math.floor(position.y / Chunk.CHUNK_SIZE);
			int indexZ = (int)Math.floor(position.z / Chunk.CHUNK_SIZE);
			
			currentChunk = new Vector3f(indexX, indexY, indexZ);
			
			if (currentChunk.x != lastChunk.x || currentChunk.y != lastChunk.y || currentChunk.z != lastChunk.z) {
				playerCallback.onChunkChanged(currentChunk);
			}
			
			lastChunk = currentChunk;
		}
		
		float rotY = (float)InputHandler.getDeltaPosition().x * TURN_SPEED * Display.getDeltaTimeInSeconds();
		float rotX = (float)InputHandler.getDeltaPosition().y * TURN_SPEED * Display.getDeltaTimeInSeconds();
		camera.rotate(rotX, rotY, 0);
		this.rotation = new Vector3f(camera.getRotation()).mul(-1);
		camera.setPosition(position);
		
		ray.castRay(InputHandler.getMousePosition());
		currentBlockPosition = ray.getBlockCollision(Game.getWorld());
		
		if (InputHandler.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			if (currentBlockPosition[4] != -1)
				Game.getWorld().getChunkManager().modifyBlock(currentBlockPosition);
		}
	}
	
	public void update() {
		handleInput();
	}

	public Camera getCamera() {
		return camera;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public static float getRenderdistance() {
		return RENDERDISTANCE;
	}

	public String getID() {
		return ID;
	}

	public static float getFov() {
		return FOV;
	}

	public static float getNearClipPlane() {
		return NEAR_CLIP_PLANE;
	}

	public static float getFarClipPlane() {
		return FAR_CLIP_PLANE;
	}

	public Vector3f getCurrentChunk() {
		return currentChunk;
	}

	public float[] getCurrentBlockPosition() {
		return currentBlockPosition;
	}
}
