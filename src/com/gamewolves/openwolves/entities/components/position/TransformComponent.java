package com.gamewolves.openwolves.entities.components.position;

import java.util.UUID;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.util.math.Maths;

public class TransformComponent extends Component
{
	
	public Vector3f position, rotation, scale;
	
	public TransformComponent(UUID entity)
	{
		super(entity);
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1, 1, 1);
	}
	
	public void translate(float dx, float dy, float dz)
	{
		position.add(dx, dy, dz);
	}
	
	public void translate(Vector3f translation)
	{
		position.add(translation);
	}
	
	public void rotate(float dx, float dy, float dz)
	{
		rotation.add(dx, dy, dz);
	}
	
	public void rotate(Vector3f translation)
	{
		rotation.add(translation);
	}
	
	public Vector3f getPosition()
	{
		return position;
	}
	
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}
	
	public Vector3f getRotation()
	{
		return rotation;
	}
	
	public void setRotation(Vector3f rotation)
	{
		this.rotation = rotation;
	}
	
	public Vector3f getScale()
	{
		return scale;
	}
	
	public void setScale(Vector3f scale)
	{
		this.scale = scale;
	}
	
	/**
	 * Calculates the model matrix
	 * 
	 * @return The model matrix
	 */
	public Matrix4f getModelMatrix()
	{
		Vector3f _rotation = new Vector3f(rotation).mul(Maths.DEGREES_TO_RADIANS);
		return Maths.createTransformationMatrix(position, _rotation, scale);
	}
}
