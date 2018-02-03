package com.gamewolves.openwolves.util.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {
	
	public static final float DEGREES_TO_RADIANS = (float)Math.PI / 180.f;
	public static final float RADIANS_TO_DEGREES = 180.f / (float)Math.PI;

	/**
	 * Returns the vertices of a rectangle for rendering with a given width and height
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @return The rectangles vertices
	 */
	public static Vector3f[] calculateRectangleVertices(float width, float height) {
		Vector3f[] vertices = new Vector3f[4];
		
		vertices[0] = new Vector3f(-1 * width,      height, 0);
		vertices[1] = new Vector3f(-1 * width, -1 * height, 0);
		vertices[2] = new Vector3f(     width, -1 * height, 0);
		vertices[3] = new Vector3f(     width,      height, 0);
		
		return vertices;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
		Matrix4f transformation = new Matrix4f();
		transformation.identity();
		transformation.translate(translation);
		transformation.rotateXYZ(rotation);
		transformation.scale(scale);
		return transformation;
	}
}
