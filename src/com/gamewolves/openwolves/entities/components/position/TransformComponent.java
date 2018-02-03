package com.gamewolves.openwolves.entities.components.position;

import java.util.UUID;

import org.joml.Vector3f;

import com.gamewolves.openwolves.entities.components.Component;

public class TransformComponent extends Component {
	
	public Vector3f position, rotation, scale;

	public TransformComponent(UUID entity) {
		Component.addComponent(entity, this);	
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1, 1, 1);
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
	
	public void translate(float dx, float dy, float dz) {
		position.add(dx, dy, dz);
	}
	
	public void translate(Vector3f translation) {
		position.add(translation);
	}
	
	public void rotate(float dx, float dy, float dz) {
		rotation.add(dx, dy, dz);
	}
	
	public void rotate(Vector3f translation) {
		rotation.add(translation);
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

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
}
