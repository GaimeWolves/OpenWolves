package com.gamewolves.openwolves.materials;

import com.gamewolves.openwolves.materials.colors.Color;
import com.gamewolves.openwolves.materials.shaders.Shader;
import com.gamewolves.openwolves.materials.textures.Texture;

public class Material
{
	
	public static Material baseMaterial;
	
	private Color color;
	private Texture texture;
	private Shader shader;
	private float specularIntensity;
	private float specularPower;
	
	public Material()
	{
		color = new Color();
	}
	
	public void enableShader()
	{
		shader.start();
	}
	
	public void disableShader()
	{
		shader.stop();
	}
	
	public void setShader(String vertexFile, String geometryFile, String fragmentFile)
	{
		shader = new Shader(vertexFile, geometryFile, fragmentFile);
	}
	
	public void setShader(Shader shader)
	{
		this.shader = shader;
	}
	
	public void setTexture(String textureFile)
	{
		texture = new Texture(textureFile);
	}
	
	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}
	
	public Shader getShader()
	{
		return shader;
	}
	
	public Texture getTexture()
	{
		return texture;
	}
	
	public void setColor(float r, float g, float b, float a)
	{
		color.r = r;
		color.g = g;
		color.b = b;
		color.a = a;
	}
	
	public void setColor(Color color)
	{
		this.color.setColor(color);
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public float getSpecularIntensity()
	{
		return specularIntensity;
	}
	
	public void setSpecularIntensity(float specularIntensity)
	{
		this.specularIntensity = specularIntensity;
	}
	
	public float getSpecularPower()
	{
		return specularPower;
	}
	
	public void setSpecularPower(float specularPower)
	{
		this.specularPower = specularPower;
	}
}
