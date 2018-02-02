package com.gamewolves.openwolves.util.math;

import org.joml.Vector3f;

public class Maths {

	/**
	 * Returns the vertices of a rectangle for rendering with a given width and height
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @return The rectangles vertices
	 */
	public static Vector3f[] calculateRectangleVertices(float width, float height) {
		Vector3f[] vertices = new Vector3f[6];
		
		vertices[0] = new Vector3f(-1 * width, -1 * height, 0);
		vertices[1] = new Vector3f(-1 * width,      height, 0);
		vertices[2] = new Vector3f(     width,      height, 0);
		
		vertices[3] = new Vector3f(-1 * width, -1 * height, 0);
		vertices[4] = new Vector3f(     width,      height, 0);
		vertices[5] = new Vector3f(     width, -1 * height, 0);
		
		return vertices;
	}
}
