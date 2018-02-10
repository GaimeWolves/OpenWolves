package com.gamewolves.openwolves.entities.components.drawing;

import java.util.UUID;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.gamewolves.openwolves.entities.Loader;
import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.materials.Material;

public class MeshComponent extends Component {
	
	private float[] vertices, uvCoords, normals;
	private int[] indices;
	
	private int vaoID;
	
	public boolean useTexture, useLighting;
	public Material material;

	public MeshComponent(UUID entity) {
		super(entity);
		useTexture = false;
		useLighting = false;
		material = Material.baseMaterial;
	}

	@Override
	public void update() {

	}
	
	/**
	 * Renders an object to the scene
	 */
	public void render() {
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);
		
		if (useTexture && uvCoords != null)
			loadTexture();
		
		if (useLighting = true && normals != null)
			loadNormals();
		
		if (useTexture && uvCoords != null) 
			activateTexture();
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		
		if (useTexture && uvCoords != null)
			unloadTexture();
		
		if (useLighting = true && normals != null) {
			unloadNormals();
		}
		
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Loads the texture
	 */
	private void loadTexture() {
		GL20.glEnableVertexAttribArray(1);
	}
	
	/**
	 * Activates the texture
	 */
	private void activateTexture() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, material.getTexture().getTextureID());
	}
	
	/**
	 * Unloads the texture
	 */
	private void unloadTexture() {
		GL20.glDisableVertexAttribArray(1);
	}
	
	/**
	 * Loads the normals
	 */
	private void loadNormals() {
		GL20.glEnableVertexAttribArray(2);
	}
	
	/**
	 * Unloads the normals
	 */
	private void unloadNormals() {
		GL20.glDisableVertexAttribArray(2);
	}
	
	/**
	 * Creates a VAO with the given values
	 * @param vertices The vertices of the object
	 * @param indices The indices of the object
	 * @param uvCoords The uv coordinates of the object
	 */
	public void loadMesh(float[] vertices, int[] indices, float[] uvCoords) {
		this.vertices = vertices;
		this.indices = indices;
		this.uvCoords = uvCoords;
		Loader.deleteVAO(vaoID);
		vaoID = Loader.loadVAO_VIT(vertices, indices, uvCoords);
	}
	
	/**
	 * Creates a VAO with the given values
	 * @param vertices The vertices of the object
	 * @param indices The indices of the object
	 */
	public void loadMesh(float[] vertices, int[] indices) {
		this.vertices = vertices;
		this.indices = indices;
		Loader.deleteVAO(vaoID);
		vaoID = Loader.loadVAO_VI(vertices, indices);
	}
	
	/**
	 * Recreates the VAO with the new uv coordinates
	 * @param uvCoords The uv coordinates of the object
	 */
	public void loadUV(float[] uvCoords) {
		this.uvCoords = uvCoords;
		if (vertices == null || indices == null) throw new IllegalStateException("Load vertices and indices first");
		Loader.deleteVAO(vaoID);
		if (normals != null)
			vaoID = Loader.loadVAO_VITN(vertices, indices, uvCoords, normals);
		else
			vaoID = Loader.loadVAO_VIT(vertices, indices, uvCoords);
	}
	
	public void loadNormals(float[] normals) {
		this.normals = normals;
		if (vertices == null || indices == null) throw new IllegalStateException("Load vertices and indices first");
		Loader.deleteVAO(vaoID);
		if (uvCoords != null)
			vaoID = Loader.loadVAO_VITN(vertices, indices, uvCoords, normals);
		else
			vaoID = Loader.loadVAO_VIN(vertices, indices, normals);
	}

	@Override
	public void lateUpdate() {

	}

	@Override
	public void delete() {

	}

}
