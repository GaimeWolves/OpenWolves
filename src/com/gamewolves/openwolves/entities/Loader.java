package com.gamewolves.openwolves.entities;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.gamewolves.openwolves.textures.Texture;

public class Loader {
	
	/**
	 * HashMap of every VAO and its corresponding VBOs
	 */
	private static HashMap<Integer, Integer> models = new HashMap<>();

	/**
	 * Creates a VAO holding the values:
	 * 	1. Vertices
	 * @param vertices Vertices as a float array { x1, y1, z1, x2, y2, ... }
	 * @return the Index of the VAO
	 */
	public static int loadVAO(float[] vertices) {
		int vaoID = createVAO();
		
		storeDataInVBO(0, vertices, 3, vaoID);
		unbindVAO();
		
		return vaoID;
	}
	
	/**
	 * Creates a VAO holding the values:
	 * 	1. Vertices
	 *  2. Indices
	 * @param vertices Vertices as a float array { x1, y1, z1, x2, y2, ... }
	 * @param indices Indices as an integer array
	 * @return the Index of the VAO
	 */
	public static int loadVAO(float[] vertices, int[] indices) {
		int vaoID = createVAO();
		
		storeIndexData(indices, vaoID);
		storeDataInVBO(0, vertices, 3, vaoID);
		unbindVAO();
		
		return vaoID;
	}
	
	/**
	 * Creates a VAO holding the values:
	 * 	1. Vertices
	 *  2. Indices
	 *  3. Texture coordinates
	 * @param vertices Vertices as a float array { x1, y1, z1, x2, y2, ... }
	 * @param indices Indices as an integer array
	 * @return the Index of the VAO
	 */
	public static int loadVAO(float[] vertices, int[] indices, float[] uvCoords) {
		int vaoID = createVAO();
		
		storeIndexData(indices, vaoID);
		storeDataInVBO(0, vertices, 3, vaoID);
		storeDataInVBO(1, uvCoords, 2, vaoID);
		
		unbindVAO();
		
		return vaoID;
	}
	
	/**
	 * Creates a VAO
	 * @return The index of the VAO
	 */
	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	/**
	 * Stores a float array into a VBO
	 * @param attribListID The ID of the VBO
	 * @param data The data to store as a float array
	 * @param coordinateSize The size of each coordinate in the data
	 */
	private static void storeDataInVBO(int attribListID, float[] data, int coordinateSize, int vaoID) {
		int vboID = GL15.glGenBuffers();
		models.put(vaoID, vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = createFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribListID, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Creates a VBO for indices
	 * @param indices The indices for the VBO
	 */
	private static void storeIndexData(int[] indices, int vaoID) {
		int vboID = GL15.glGenBuffers();
		models.put(vaoID, vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = createIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
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
	
	/**
	 * Creates a IntBuffer for the VBO of an integer array
	 * @param data The integer array to convert
	 * @return The created IntBuffer
	 */
	private static IntBuffer createIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static void deleteVAO(int vaoID) {
		for (Entry<Integer, Integer> model : models.entrySet()) {
			if (model.getKey() == vaoID) {
				GL15.glDeleteBuffers(model.getValue());
				models.remove(model.getKey(), model.getValue());
				GL30.glDeleteVertexArrays(model.getKey());
			}
		}
		models.remove(vaoID);
	}
	
	/**
	 * Deletes every VAO and VBO
	 */
	public static void delete() {
		for (Entry<Integer, Integer> model : models.entrySet()) {
			GL15.glDeleteBuffers(model.getValue());
			GL30.glDeleteVertexArrays(model.getKey());
		}
		models.clear();
		for (int texture : Texture.getTextures()) {
			GL11.glDeleteTextures(texture);
		}
	}
}
