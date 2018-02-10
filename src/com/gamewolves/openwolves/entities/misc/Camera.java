package com.gamewolves.openwolves.entities.misc;

import com.gamewolves.openwolves.entities.Entity;
import com.gamewolves.openwolves.entities.components.movement.CameraMovementComponent;
import com.gamewolves.openwolves.entities.components.position.TransformComponent;
import com.gamewolves.openwolves.entities.components.projection.ProjectionComponent;
import com.gamewolves.openwolves.entities.components.projection.ViewComponent;

public class Camera extends Entity {
	
	public TransformComponent transform;
	public ViewComponent view;
	public ProjectionComponent projection;

	public Camera() {
		transform = new TransformComponent(ID);
		projection = new ProjectionComponent(ID);
		view = new ViewComponent(ID);
		view.constructViewMatrix(transform);
		addComponent(transform);
		addComponent(projection);
		addComponent(view);
	}
	
	public void update() {
		if (getComponent(CameraMovementComponent.class) != null) {
			getComponent(CameraMovementComponent.class).updateMovement(transform);
		}
		//transform.setRotation(new Vector3f(transform.getRotation().x(), transform.getRotation().y(), 0));
		view.constructViewMatrix(transform);
	}

	@Override
	public void delete() {
		super.delete();
	}
	
	public void rotate(float dx, float dy) {
		transform.rotate(dx, dy, 0);
	}
}
