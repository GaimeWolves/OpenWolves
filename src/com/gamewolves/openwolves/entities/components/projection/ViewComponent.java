package com.gamewolves.openwolves.entities.components.projection;

import java.util.UUID;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.entities.components.position.TransformComponent;
import com.gamewolves.openwolves.util.math.Maths;

public class ViewComponent extends Component
{
	
	private Matrix4f viewMatrix;
	
	public ViewComponent(UUID entity)
	{
		super(entity);
		viewMatrix = new Matrix4f();
	}
	
	/**
	 * Construct a view matrix form the given transform
	 * 
	 * @param transform
	 */
	public void constructViewMatrix(TransformComponent transform)
	{
		viewMatrix = new Matrix4f();
		viewMatrix.rotateXYZ(new Vector3f(transform.getRotation()).mul(Maths.DEGREES_TO_RADIANS));
		Vector3f negativeCameraPos = new Vector3f(transform.getPosition()).mul(-1);
		viewMatrix.translate(negativeCameraPos);
	}
	
	public Matrix4f getViewMatrix()
	{
		return viewMatrix;
	}
}
