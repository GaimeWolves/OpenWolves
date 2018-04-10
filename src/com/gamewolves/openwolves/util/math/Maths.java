package com.gamewolves.openwolves.util.math;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Maths
{
	
	public static final float DEGREES_TO_RADIANS = (float) Math.PI / 180.f;
	public static final float RADIANS_TO_DEGREES = 180.f / (float) Math.PI;
	
	/**
	 * Returns the vertices of a rectangle for rendering with a given width and
	 * height
	 * 
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @return The rectangles vertices
	 */
	public static Vector3f[] calculateRectangleVertices(float width, float height)
	{
		Vector3f[] vertices = new Vector3f[4];
		
		vertices[0] = new Vector3f(-1 * width, height, 0);
		vertices[1] = new Vector3f(-1 * width, -1 * height, 0);
		vertices[2] = new Vector3f(width, -1 * height, 0);
		vertices[3] = new Vector3f(width, height, 0);
		
		return vertices;
	}
	
	/**
	 * Returns the vertices of a plane for rendering with a given width and depth
	 * 
	 * @param width The width of the plane
	 * @param depth The depth of the plane
	 * @return The rectangles vertices
	 */
	public static Vector3f[] calculatePlaneVertices(float width, float depth)
	{
		Vector3f[] vertices = new Vector3f[4];
		
		vertices[0] = new Vector3f(-1 * width, 0, depth);
		vertices[1] = new Vector3f(-1 * width, 0, -1 * depth);
		vertices[2] = new Vector3f(width, 0, -1 * depth);
		vertices[3] = new Vector3f(width, 0, depth);
		
		return vertices;
	}
	
	/**
	 * Calculates the cube vertices with given width, height and depth
	 * 
	 * @param width The width of the Cube
	 * @param height The height of the Cube
	 * @param depth The depth of the Cube
	 * @return A Vector3f array of vertices
	 */
	public static Vector3f[] calculateCubeVertices(float width, float height, float depth)
	{
		Vector3f[] vertices = new Vector3f[24];
		
		vertices[0] = new Vector3f(-1 * width, -1 * height, depth);
		vertices[1] = new Vector3f(width, -1 * height, depth);
		vertices[2] = new Vector3f(width, height, depth);
		vertices[3] = new Vector3f(-1 * width, height, depth);
		
		vertices[4] = new Vector3f(-1 * width, height, depth);
		vertices[5] = new Vector3f(width, height, depth);
		vertices[6] = new Vector3f(width, height, -1 * depth);
		vertices[7] = new Vector3f(-1 * width, height, -1 * depth);
		
		vertices[8] = new Vector3f(width, -1 * height, -1 * depth);
		vertices[9] = new Vector3f(-1 * width, -1 * height, -1 * depth);
		vertices[10] = new Vector3f(-1 * width, height, -1 * depth);
		vertices[11] = new Vector3f(width, height, -1 * depth);
		
		vertices[12] = new Vector3f(-1 * width, -1 * height, -1 * depth);
		vertices[13] = new Vector3f(width, -1 * height, -1 * depth);
		vertices[14] = new Vector3f(width, -1 * height, depth);
		vertices[15] = new Vector3f(-1 * width, -1 * height, depth);
		
		vertices[16] = new Vector3f(-1 * width, -1 * height, -1 * depth);
		vertices[17] = new Vector3f(-1 * width, -1 * height, depth);
		vertices[18] = new Vector3f(-1 * width, height, depth);
		vertices[19] = new Vector3f(-1 * width, height, -1 * depth);
		
		vertices[20] = new Vector3f(width, -1 * height, depth);
		vertices[21] = new Vector3f(width, -1 * height, -1 * depth);
		vertices[22] = new Vector3f(width, height, -1 * depth);
		vertices[23] = new Vector3f(width, height, depth);
		
		return vertices;
	}
	
	/**
	 * Creates a transformation matrix from a translation, a rotation and a scale
	 * 
	 * @param translation The translation as an 3D vector
	 * @param rotation The rotation as a 3D vector of Euler angles
	 * @param scale The scale as a 3D Vector
	 * @return The 4x4 transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale)
	{
		Matrix4f transformation = new Matrix4f();
		// transformation.identity();
		transformation.translate(translation);
		transformation.rotateXYZ(rotation);
		transformation.scale(scale);
		return transformation;
	}
	
	/**
	 * Calculates the Euler angles of a given vector relative to a given axis
	 * 
	 * @param direction The direction
	 * @param axis The relative axis
	 * @return The Euler angles
	 */
	public static Vector3f directionToEulerAngles(Vector3f direction, Vector3f axis)
	{
		Vector3f normDir = new Vector3f(direction).normalize();
		Quaternionf rotation = new Quaternionf();
		axis.rotationTo(normDir, rotation);
		Vector3f angles = new Vector3f();
		rotation.getEulerAnglesXYZ(angles);
		angles.mul(Maths.RADIANS_TO_DEGREES);
		return angles;
	}
	
	/**
	 * Creates a MVP matrix
	 * 
	 * @param model Model matrix
	 * @param view View matrix
	 * @param projection Projection matrix
	 * @return MVP Matrix
	 */
	public static Matrix4f createMVP(Matrix4f model, Matrix4f view, Matrix4f projection)
	{
		return new Matrix4f(projection).mul(new Matrix4f(view).mul(model));
	}
}
