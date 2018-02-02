package com.gamewolves.openwolves.entities;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Loader {
	
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();

	/**
	 * Creates a VAO holding the values:
	 * 	1. Vertices
	 * @param vertices Vertices as a float array { x1, y1, z1, x2, y2, ... }
	 * @return the Index of the VAO
	 */
	public static int loadVAO(float[] vertices) {
		int vaoID = createVAO();
		
		storeVBO(0, vertices);
		unbindVAO();
		
		return vaoID;
	}
	
	/**
	 * Creates a VAO
	 * @return The index of the VAO
	 */
	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	/**
	 * Stores a float array as 3D vectors into a VBO
	 * @param attribListID The ID of the VBO
	 * @param data The data to store as a float array
	 */
	private static void storeVBO(int attribListID, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = createFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribListID, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Unbinds the VAO
	 */
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Creates a FloatBuffer for the VBO of a float array
	 * @param data The float array to convert
	 * @return The created FloatBuffer
	 */
	private static FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static void delete() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
	}
}
