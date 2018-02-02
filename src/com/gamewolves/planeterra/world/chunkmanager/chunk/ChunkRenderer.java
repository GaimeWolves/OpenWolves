package com.gamewolves.planeterra.world.chunkmanager.chunk;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.gamewolves.planeterra.main.Game;
import com.gamewolves.planeterra.model.Model;
import com.gamewolves.planeterra.player.Camera;
import com.gamewolves.planeterra.render.MasterRenderer;
import com.gamewolves.planeterra.util.Maths;
import com.gamewolves.planeterra.world.chunkmanager.chunk.block.CubeShader;
import com.gamewolves.planeterra.world.chunkmanager.chunk.water.WaterShader;

public class ChunkRenderer {

	CubeShader chunkShader;
	WaterShader waterShader;
	
	public ChunkRenderer() {
		chunkShader = new CubeShader();
		waterShader = new WaterShader();
	}
	
	/**
	 * Renders the Chunks
	 * @param projectionMatrix ProjectionMatrix
	 * @param camera Camera of Player
	 * @param textureID ID of Texture
	 */
	public void render(Matrix4f projectionMatrix, Camera camera, int textureID) {
		if (Game.getWorld() == null) return;
			////////CHUNK//////			
			for (Chunk chunk : Game.getWorld().getChunkManager().getRendered_Chunks()) {
				chunkShader.start();
				
				if (chunk.isEmpty()) continue;
				if (chunk.getChunkModel() != null) { 
					Model mesh = chunk.getChunkModel();		
				
					Vector3f position = new Vector3f(chunk.getIndex().x * Chunk.CHUNK_SIZE, chunk.getIndex().y * Chunk.CHUNK_SIZE, chunk.getIndex().z * Chunk.CHUNK_SIZE);
					Matrix4f transformation = Maths.createTransformationMatrix(position, new Vector3f(), new Vector3f(1));
						
					preRender(projectionMatrix, camera, mesh, transformation);
		
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
					GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				
					postRender();
				}
				
				chunkShader.stop();
				
				if (!chunk.hasWater()) continue;
				if (chunk.getWaterModel() == null) continue;
				
				waterShader.start();
				MasterRenderer.disableBackFaceCulling();
				
				Model mesh = chunk.getWaterModel();
				
				
				preWaterRender(camera, mesh, projectionMatrix);
				
				Vector3f position = new Vector3f(chunk.getIndex().x * Chunk.CHUNK_SIZE, chunk.getIndex().y * Chunk.CHUNK_SIZE, chunk.getIndex().z * Chunk.CHUNK_SIZE);
				Matrix4f transformation = Maths.createTransformationMatrix(position, new Vector3f(), new Vector3f(1));
				
				waterShader.loadTransformationMatrix(transformation);
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
				GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				
				postRender();
				
				MasterRenderer.enableBackFaceCulling();
				waterShader.stop();
			}
	}
	
	/**
	 * Enables the Shader and loads some uniform Variables
	 * @param camera Camera of Player
	 * @param mesh Mesh of Chunk
	 * @param projectionMatrix ProjectionMatrix
	 */
	private void preWaterRender(Camera camera, Model mesh, Matrix4f projectionMatrix) {
		GL30.glBindVertexArray(mesh.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		waterShader.loadProjectionMatrix(projectionMatrix);
		waterShader.loadViewMatrix(camera);
		waterShader.loadTime();
		waterShader.loadCameraPosition(camera.getPosition());
	}
	
	private void preRender(Matrix4f projectionMatrix, Camera camera, Model chunkModel, Matrix4f transformation) {
		GL30.glBindVertexArray(chunkModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		Matrix4f MVP;
		Matrix4f modelView = new Matrix4f(projectionMatrix);
		modelView.mul(Maths.createViewMatrix(camera));
		MVP = modelView.mul(transformation);
		
		chunkShader.loadMVPMatrix(MVP);
	}
	
	/**
	 * Disables the Shader
	 */
	private void postRender() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	public void cleanUp() {
		chunkShader.cleanUp();
		waterShader.cleanUp();
	}
}
