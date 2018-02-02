package com.gamewolves.planeterra.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.gamewolves.planeterra.model.Model;
import com.gamewolves.planeterra.model.ModelData;
import com.gamewolves.planeterra.textures.Texture;

public class Loader {

	private Map<Integer, List<Integer>> vaos = new HashMap<Integer, List<Integer>>();     //List of current VAOs [Vertex Array Objects]
	private List<Integer> textures = new ArrayList<Integer>(); //List of current Textures [GL_Texture IDs]
	
	public Model loadToVAO(ModelData data) {
		int vaoID = GL30.glGenVertexArrays();
		vaos.put(vaoID, new ArrayList<Integer>());
		GL30.glBindVertexArray(vaoID);
		
		bindIndicesBuffer(data.getIndices(), vaoID);
		storeDataInAttributeList(0, 3, data.getVertices(), vaoID);
		if (data.getNormals() != null)
			storeDataInAttributeList(1, 3, data.getNormals(), vaoID);
		if (data.getTextureCoords() != null)
			storeDataInAttributeList(2, 2, data.getTextureCoords(), vaoID);
		
		GL30.glBindVertexArray(0);
		
		return new Model(vaoID, data, data.getRenderer(), data.getIndexX(), data.getIndexY(), data.getIndexZ());
	}
	
	public int loadGuiTextVAO(float[] vertices, float[] textureCoords) {
		int vaoID = GL30.glGenVertexArrays();
		vaos.put(vaoID, new ArrayList<Integer>());
		GL30.glBindVertexArray(vaoID);
		
		storeDataInAttributeList(0, 2, vertices, vaoID);
		storeDataInAttributeList(1, 2, textureCoords, vaoID);
		
		GL30.glBindVertexArray(0);
		
		return vaoID;
	}
	
	/**
	 * Loads a Texture into the GL_Texture IDs Array
	 * @param path Path to the Texture
	 * @return The loaded Texture
	 */
	public Texture loadTexture(String path) {
		Texture texture = new Texture(path);
		textures.add(texture.getTextureID());
		return texture;
	}
	
	/**
	 * Clears the list of VAOs, VBOs and GL_Texture IDs
	 */
	public void cleanUp() {
		Iterator<Entry<Integer, List<Integer>>> iterator = vaos.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<Integer, List<Integer>> vao = (Map.Entry<Integer, List<Integer>>) iterator.next();
			Iterator<Integer> vbos = vao.getValue().iterator();
			while(vbos.hasNext()) {
				int vbo = (int) vbos.next();
				GL15.glDeleteBuffers(vbo);
			}
			GL30.glDeleteVertexArrays(vao.getKey());
		}
		vaos.clear();
		textures.forEach((texture) -> { GL11.glDeleteTextures(texture); });
	}
	
	/**
	 * Binds the indices to a VBO
	 * @param indices the indices to bind
	 */
	private void bindIndicesBuffer(int[] indices, int vaoID) {
		int vboID = GL15.glGenBuffers();
		vaos.get(vaoID).add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Creates a VBO and puts the data into it.
	 * 
	 * @param attributeID ID of the VAO
	 * @param coordinateSize Size of one datablock example: Vertex has an x-, y-, z-Coordinate so coordinateSize is 3
	 * @param data the data to put in the VBO
	 */
	private void storeDataInAttributeList(int attributeID, int coordinateSize, float[] data, int vaoID) {
		int vboID = GL15.glGenBuffers();
		vaos.get(vaoID).add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeID, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Creates an IntBuffer from the data
	 * @param data Data to convert to IntBuffer
	 * @return Created IntBuffer
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Creates an FloatBuffer from the data
	 * @param data Data to convert to FloatBuffer
	 * @return Created IntBuffer
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Deletes a VAO
	 * @param vaoID
	 */
	public void deleteVAO(int vaoID) {
		List<Integer> vbos = vaos.get(vaoID);
		if (vbos == null) return;
		for (int i = 0; i < vbos.size(); i++) {
			GL15.glDeleteBuffers(vbos.get(i));
		}
		GL30.glDeleteVertexArrays(vaoID);
		vaos.remove(vaoID);
	}
}
