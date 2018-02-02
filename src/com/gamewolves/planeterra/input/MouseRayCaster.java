package com.gamewolves.planeterra.input;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.gamewolves.planeterra.player.Camera;
import com.gamewolves.planeterra.render.Display;
import com.gamewolves.planeterra.util.Maths;

public class MouseRayCaster {

	private Vector3f currentRay;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private Camera camera;
	
	public MouseRayCaster(Camera cam, Matrix4f projectionMatrix) {
		this.camera = cam;
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = Maths.createViewMatrix(camera);
	}
	
	/**
	 * Updates the current ray
	 */
	public void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
	}
	
	/**
	 * Calculates the Ray
	 * @return Ray
	 */
	private Vector3f calculateMouseRay() {
		float mouseX = InputHandler.getMousePosition().x;
		float mouseY = InputHandler.getMousePosition().y;
		
		Vector2f normalizedDeviceSpacePosition = calculateNormalizedDeviceSpace(mouseX, mouseY);
		Vector4f homogenousClipSpacePosition = new Vector4f(normalizedDeviceSpacePosition.x, normalizedDeviceSpacePosition.y, -1f, 1f);
		Vector4f eyeSpacePosition = calculateEyeSpace(homogenousClipSpacePosition);
		Vector3f worldRay = calculateWorldSpace(eyeSpacePosition);
		return worldRay;
	}
	
	/**
	 * Transforms the Eye space coordinates to World space coordinates ( inverse View matrix )
	 * @param eyeSpacePosition Eye space Coordinates
	 * @return World space coordinates
	 */
	private Vector3f calculateWorldSpace(Vector4f eyeSpacePosition) {
		Matrix4f viewMat = new Matrix4f(viewMatrix);
		Matrix4f inverseViewMatrix = viewMat.invert();
		Vector4f rayWorld = inverseViewMatrix.transform(eyeSpacePosition);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalize();
		return mouseRay;
	}
	
	/**
	 * Transforms the Screen coordinates to Device coordinates
	 * @param mouseX X-Pos
	 * @param mouseY Y-Pos
	 * @return Normalized Device Space Position
	 */
	private Vector2f calculateNormalizedDeviceSpace(float mouseX, float mouseY) {
		float x = (2f * mouseX) / Display.WIDTH - 1f;
		float y = 1f - (2f * mouseY) / Display.HEIGHT;
		return new Vector2f(x, y);
	}
	
	/**
	 * Transfroms the homogenous clip space Position to Eye space Position ( inverse Projection )
	 * @param homogenousClipSpacePosition homogenous clip space Position
	 * @return Eye space Position
	 */
	private Vector4f calculateEyeSpace(Vector4f homogenousClipSpacePosition) {
		Matrix4f projectionMat = new Matrix4f(projectionMatrix);
		Matrix4f inverseProjectionMatrix = projectionMat.invert();
		Vector4f eyeSpacePosition = inverseProjectionMatrix.transform(homogenousClipSpacePosition);
		return new Vector4f(eyeSpacePosition.x, eyeSpacePosition.y, -1f, 0f);
	}

	public Vector3f getCurrentRay() {
		return currentRay;
	}
}
