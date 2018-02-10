package com.gamewolves.openwolves.entities.components.movement;

import java.util.UUID;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.entities.components.position.TransformComponent;
import com.gamewolves.openwolves.gl.Display;
import com.gamewolves.openwolves.input.InputHandler;
import com.gamewolves.openwolves.util.math.Maths;

public class CameraMovementComponent extends Component {
	
	private float speed;
	private float turnSpeed;
	private Vector3f rotation;

	public CameraMovementComponent(UUID entity) {
		super(entity);
		speed = 15;
		turnSpeed = 10;
		rotation = new Vector3f();
	}

	@Override
	public void update() {

	}

	@Override
	public void lateUpdate() {

	}

	@Override
	public void delete() {

	}
	
	/**
	 * Calculates basic movement for the camera
	 * @param transform The transform to modify
	 */
	public void updateMovement(TransformComponent transform) {
		rotation.mul(Maths.DEGREES_TO_RADIANS);
		
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_W)) {
			transform.translate(
					speed * (float)Math.sin(-rotation.y) * Display.getDeltaTimeInSeconds(), 0 , 
					speed * -(float)Math.cos(-rotation.y) * Display.getDeltaTimeInSeconds()
			);
		}
		
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_A)) {
			transform.translate(
					speed * (float)Math.sin(-rotation.y - (Math.PI / 2)) * Display.getDeltaTimeInSeconds(), 0 , 
					speed * -(float)Math.cos(-rotation.y - (Math.PI / 2)) * Display.getDeltaTimeInSeconds()
			);
		}
		
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_S)) {
			transform.translate(
					speed * -(float)Math.sin(-rotation.y) * Display.getDeltaTimeInSeconds(), 0 , 
					speed * (float)Math.cos(-rotation.y) * Display.getDeltaTimeInSeconds()
			);
		}
		
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_D)) {
			transform.translate(
					speed * (float)Math.sin(-rotation.y + (Math.PI / 2)) * Display.getDeltaTimeInSeconds(), 0 , 
					speed * -(float)Math.cos(-rotation.y + (Math.PI / 2)) * Display.getDeltaTimeInSeconds()
			);
		}
	
		rotation.mul(Maths.RADIANS_TO_DEGREES);
		
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_SPACE)) transform.translate(0, speed * Display.getDeltaTimeInSeconds(), 0);
		else if (InputHandler.isKeyDown(GLFW.GLFW_KEY_C)) transform.translate(0, -speed * Display.getDeltaTimeInSeconds(), 0);
		
		float rotY = (float)InputHandler.getDeltaPosition().x * turnSpeed * Display.getDeltaTimeInSeconds();
		float rotX = (float)InputHandler.getDeltaPosition().y * turnSpeed * Display.getDeltaTimeInSeconds();
		
		transform.rotate(rotX, rotY, 0);
		rotation = new Vector3f(transform.getRotation()).mul(-1);
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getTurnSpeed() {
		return turnSpeed;
	}

	public void setTurnSpeed(float turnSpeed) {
		this.turnSpeed = turnSpeed;
	}	
}
