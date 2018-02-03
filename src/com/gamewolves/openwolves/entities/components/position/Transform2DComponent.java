package com.gamewolves.openwolves.entities.components.position;

import java.util.UUID;

import org.joml.Vector2f;

import com.gamewolves.openwolves.entities.components.Component;

public class Transform2DComponent extends Component {

	public Vector2f position, rotation, scale;

	public Transform2DComponent(UUID entity) {
		Component.addComponent(entity, this);	
		position = new Vector2f();
		rotation = new Vector2f();
		scale = new Vector2f();
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
	
	public void translate(float dx, float dy) {
		position.add(dx, dy);
	}
	
	public void translate(Vector2f translation) {
		position.add(translation);
	}
	
	public void rotate(float dx, float dy) {
		rotation.add(dx, dy);
	}
	
	public void rotate(Vector2f translation) {
		rotation.add(translation);
	}
	
	public void setScale(float x, float y) {
		scale.set(x, y);
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public Vector2f getRotation() {
		return rotation;
	}

	public void setRotation(Vector2f rotation) {
		this.rotation = rotation;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

}
