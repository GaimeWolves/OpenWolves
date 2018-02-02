package com.gamewolves.openwolves.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

public class Shader {
	
	private int programmID, vertexID, geometryID, fragmentID;

	/**
	 * Creates a shader with the respective shader files. If the shader file is null then it will be skipped
	 * @param vertexFile The location of the vertex shader
	 * @param geometryFile The location of the geometry shader
	 * @param fragmentFile The location of the fragment shader
	 */
	public Shader(String vertexFile, String geometryFile, String fragmentFile, String[] attributes) {
		programmID = GL20.glCreateProgram();
		if (vertexFile != null) { vertexID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER); GL20.glAttachShader(programmID, vertexID); }
		if (geometryFile != null) { geometryID = loadShader(geometryFile, GL32.GL_GEOMETRY_SHADER); GL20.glAttachShader(programmID, geometryID); }
		if (fragmentFile != null) { fragmentID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER); GL20.glAttachShader(programmID, fragmentID); }
		bindAttributes(attributes);
		GL20.glLinkProgram(programmID);
		GL20.glValidateProgram(programmID);
	}
	
	/**
	 * Binds the Attributes in the string array to the shader
	 * @param attributes List of attributes
	 */
	public void bindAttributes(String[] attributes) {
		for (int attribute = 0; attribute < attributes.length; attribute++) {
			bindAttribute(attribute, attributes[attribute]);
		}
	}
	
	/**
	 * Binds a variable name to an attribute
	 * @param attribute The ID of the attribute
	 * @param variableName The name of the variable
	 */
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programmID, attribute, variableName);
	}
	
	/**
	 * Starts the shader
	 */
	public void start() {
		GL20.glUseProgram(programmID);
	}
	
	/**
	 * Stops the shader
	 */
	public void stop() {
		GL20.glUseProgram(0);
	}

	/**
	 * Loads a shader into OpenGL and returns its ID
	 * @param file The file of the shader
	 * @param type The type of the shader
	 * @return The ID of the shader
	 */
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;
	}

	/**
	 * Deletes the shader and all it's shader components
	 */
	public void delete() {
		stop();
		if (vertexID != 0) { GL20.glDetachShader(programmID, vertexID); GL20.glDeleteShader(vertexID); }
		if (geometryID != 0) { GL20.glDetachShader(programmID, geometryID); GL20.glDeleteShader(geometryID); }
		if (fragmentID != 0) { GL20.glDetachShader(programmID, fragmentID); GL20.glDeleteShader(fragmentID); }
		GL20.glDeleteProgram(programmID);
	}
}
