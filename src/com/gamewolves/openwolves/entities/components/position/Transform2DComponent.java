package com.gamewolves.openwolves.entities.components.position;

import java.util.UUID;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.util.math.Maths;

public class Transform2DComponent extends Component
{
	
	public Vector2f position, scale;
	public float rotation;
	
	public Transform2DComponent(UUID entity)
	{
		super(entity);
		position = new Vector2f();
		rotation = 0;
		scale = new Vector2f(1, 1);
	}
	
	public void translate(float dx, float dy)
	{
		position.add(dx, dy);
	}
	
	public void translate(Vector2f translation)
	{
		position.add(translation);
	}
	
	public void rotate(float dr)
	{
		rotation += dr;
	}
	
	public void setScale(float x, float y)
	{
		scale.set(x, y);
	}
	
	public Vector2f getPosition()
	{
		return position;
	}
	
	public void setPosition(Vector2f position)
	{
		this.position = position;
	}
	
	public float getRotation()
	{
		return rotation;
	}
	
	public float getRotationInRadians()
	{
		return rotation * Maths.DEGREES_TO_RADIANS;
	}
	
	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}
	
	public Vector2f getScale()
	{
		return scale;
	}
	
	public void setScale(Vector2f scale)
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
		Vector3f _translation = new Vector3f(position.x(), position.y(), 0);
		Vector3f _rotation = new Vector3f(0, 0, getRotationInRadians());
		Vector3f _scale = new Vector3f(scale.x(), scale.y(), 0);
		return Maths.createTransformationMatrix(_translation, _rotation, _scale);
	}
}
