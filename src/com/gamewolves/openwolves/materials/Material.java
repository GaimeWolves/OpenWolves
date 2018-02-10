package com.gamewolves.openwolves.materials;

import com.gamewolves.openwolves.materials.textures.Texture;

public class Material {
	
	public static Material baseMaterial;
	
	private Texture texture;
	private Shader shader;
	
	public void enableShader() {
		shader.start();
	}
	
	public void disableShader() {
		shader.stop();
	}

	public void setShader(String vertexFile, String geometryFile, String fragmentFile, String[] attributes, String[] uniformNames) {
		shader = new Shader(vertexFile, geometryFile, fragmentFile, attributes, uniformNames);
	}
	
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	public void setTexture(String textureFile) {
		texture = new Texture(textureFile);
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Shader getShader() {
		return shader;
	}
	
	public Texture getTexture() {
		return texture;
	}
}
