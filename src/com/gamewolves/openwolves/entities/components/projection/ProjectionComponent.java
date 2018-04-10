package com.gamewolves.openwolves.entities.components.projection;

import java.util.UUID;

import org.joml.Matrix4f;

import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.gl.Display;
import com.gamewolves.openwolves.util.math.Maths;

public class ProjectionComponent extends Component
{
	
	private Matrix4f projectionMatrix;
	
	private float FOV;
	private float nearPlane;
	private float farPlane;
	
	public ProjectionComponent(UUID entity)
	{
		super(entity);
		FOV = 90;
		nearPlane = 0.1f;
		farPlane = 1000;
		constructProjectionMatrix();
	}
	
	/**
	 * Constructs the projection matrix from the field of view, the aspect ration,
	 * the near clip plane and the far clip plane
	 */
	private void constructProjectionMatrix()
	{
		projectionMatrix = new Matrix4f();
		projectionMatrix.perspective(Maths.DEGREES_TO_RADIANS * FOV, Display.getAspectRatio(), nearPlane, farPlane);
	}
	
	/**
	 * Calculates a new projection matrix from the field of view, the aspect ration,
	 * the near clip plane and the far clip plane
	 * 
	 * @param FOV The FOV of the camera
	 * @param nearPlane The near clip plane of the camera
	 * @param farPlane The far clip plane of the camera
	 */
	public void setProjectionMatrix(float FOV, float nearPlane, float farPlane)
	{
		this.FOV = FOV;
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		constructProjectionMatrix();
	}
	
	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}
	
	public void setFOV(float fOV)
	{
		FOV = fOV;
		constructProjectionMatrix();
	}
	
	public void setNearPlane(float nearPlane)
	{
		this.nearPlane = nearPlane;
		constructProjectionMatrix();
	}
	
	public void setFarPlane(float farPlane)
	{
		this.farPlane = farPlane;
		constructProjectionMatrix();
	}
	
}
