package com.gamewolves.openwolves.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

public class Shader {
	
	private int programmID, vertexID, geometryID, fragmentID;
	private UniformVariable[] uniformVariables;
	
	private static FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(16);

	/**
	 * Creates a shader with the respective shader files. If the shader file is null then it will be skipped
	 * @param vertexFile The location of the vertex shader
	 * @param geometryFile The location of the geometry shader
	 * @param fragmentFile The location of the fragment shader
	 * @param attributes The names of all attributes
	 * @param uniformNames The names of all uniform variables
	 */
	public Shader(String vertexFile, String geometryFile, String fragmentFile, String[] attributes, UniformVariable[] uniformNames) {
		programmID = GL20.glCreateProgram();
		if (vertexFile != null) { vertexID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER); GL20.glAttachShader(programmID, vertexID); }
		if (geometryFile != null) { geometryID = loadShader(geometryFile, GL32.GL_GEOMETRY_SHADER); GL20.glAttachShader(programmID, geometryID); }
		if (fragmentFile != null) { fragmentID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER); GL20.glAttachShader(programmID, fragmentID); }
		bindAttributes(attributes);
		GL20.glLinkProgram(programmID);
		GL20.glValidateProgram(programmID);
		
		uniformVariables = new UniformVariable[uniformNames.length];
		getUniformLocations(uniformNames);
	}
	
	protected void getUniformLocations(UniformVariable[] uniformNames) {
		for (int uniform = 0; uniform < uniformNames.length; uniform++) {
			uniformNames[uniform].location = getUniformLocation(uniformNames[uniform].name);
			uniformVariables[uniform] = uniformNames[uniform];
		}
	}
	
	/**
	 * Returns the ID of an uniform variable in the shaders
	 * @param uniformName The name of the uniform variable
	 * @return The ID of the uniform variable
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programmID, uniformName);
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
	 * Loads a float into an uniform variable
	 * @param location The location of the uniform variable
	 * @param data The float to load
	 */
	public void loadFloat(String uniformName, float data) {
		GL20.glUniform1f(getUniformVariable(uniformName).location, data);
	}
	
	/**
	 * Loads a Vector3f into an uniform variable
	 * @param location The location of the uniform variable
	 * @param data The Vector3f to load
	 */
	public void loadVector3f(String uniformName, Vector3f data) {
		GL20.glUniform3f(getUniformVariable(uniformName).location, data.x, data.y, data.z);
	}
	
	/**
	 * Loads a boolean into an uniform variable
	 * @param location The location of the uniform variable
	 * @param data The boolean to load
	 */
	public void loadBoolean(String uniformName, boolean data) {
		float convertedData = data ? 1 : 0;
		GL20.glUniform1f(getUniformVariable(uniformName).location, convertedData);
	}
	
	/**
	 * Loads a Matrix4f into an uniform variable
	 * @param location The location of the uniform variable
	 * @param data The Matrix4f to load
	 */
	public void loadMatrix4f(String uniformName, Matrix4f data) {
		GL20.glUniformMatrix4fv(getUniformVariable(uniformName).location, false, data.get(matrix4fBuffer));
	}
	
	/**
	 * Gets an uniform variable by name
	 * @param name The name of the uniform variable
	 * @return The found uniform variable
	 */
	private UniformVariable getUniformVariable(String name) {
		for (UniformVariable uniformVariable : uniformVariables) {
			if (uniformVariable.name.equals(name)) return uniformVariable;
		}
		return null;
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
	
	public static class UniformVariable {
		
		public int location;
		public String name;
		
		public UniformVariable(String name) {
			this.name = name;
		}
	}
}
