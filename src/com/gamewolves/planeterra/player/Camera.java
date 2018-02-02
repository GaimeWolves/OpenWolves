package com.gamewolves.planeterra.player;

import org.joml.Vector3f;

public class Camera {

	public Vector3f position, rotation;
	
	/**
	 * Camera. :D
	 */
	public Camera() {
		position = new Vector3f();
		rotation = new Vector3f();
	}
	
	public void rotate(float dx, float dy, float dz) {
		rotation.add(dx, dy, dz);
	}
	
	public void rotate(Vector3f rotation) {
		this.rotation.add(rotation);
	}
	
	public void translate(float dx, float dy, float dz) {
		position.add(dx, dy, dz);
	}
	
	public void translate(Vector3f translation) {
		position.add(translation);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
}
