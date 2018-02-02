package com.gamewolves.planeterra.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

public abstract class Shader {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	private int geometryShaderID;
	private boolean hasGeometryShader;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * Initializes the Vertex and Fragment Shader
	 * @param vertexFile Path to Vertex Shader
	 * @param fragmentFile Path to Fragment Shader
	 */
	public Shader(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		
		hasGeometryShader = false;
		
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
		bindAttributes();
		
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
		getAllUniformLocations();
	}
	
	/**
	 * Initializes the Vertex, Fragment and Geometry Shader
	 * @param vertexFile Path to Vertex Shader
	 * @param geometryFile Path to Geometry Shader
	 * @param fragmentFile Path to Fragment Shader
	 */
	public Shader(String vertexFile, String fragmentFile, String geometryFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		geometryShaderID = loadShader(geometryFile, GL32.GL_GEOMETRY_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		
		hasGeometryShader = true;
		
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, geometryShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
		bindAttributes();
		
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
		getAllUniformLocations();
	}
	
	/**
	 * Gets the location of all uniform Variables in the Shader
	 */
	protected abstract void getAllUniformLocations();
	
	/**
	 * Gets the location of an Uniform via its name
	 * @param uniformName Name of the Uniform
	 * @return Location of the Uniform
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	/**
	 * Deletes the Shader
	 */
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		
		if (hasGeometryShader) {
			GL20.glDetachShader(programID, geometryShaderID);
			GL20.glDeleteShader(geometryShaderID);
		}
		
		GL20.glDeleteProgram(programID);
	}
	
	/**
	 * Binds all In-Variables of the Vertex Shader
	 */
	protected abstract void bindAttributes();
	
	/**
	 * Binds one Attribute
	 * @param attribute Attribute location
	 * @param variableName Name of the Variable
	 */
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	protected void loadVector3f(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadVector2f(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	protected void loadBoolean(int location, boolean value) {
		GL20.glUniform1f(location, value ? 1 : 0);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix) {
		GL20.glUniformMatrix4fv(location, false, matrix.get(matrixBuffer));
	}
	
	/**
	 * Reads the Shader File and loads it into OpenGL
	 * @param file Path to the file
	 * @param type Type of the Shader
	 * @return ID of the Shader
	 */
	private static int loadShader(String file, int type) {
		
        StringBuilder shaderSource = new StringBuilder();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE) {
        	if(type == GL20.GL_VERTEX_SHADER) {
        		System.out.println("ERROR IN VERTEX SHADER");
        	} else if (type == GL20.GL_FRAGMENT_SHADER){
        		System.out.println("ERROR IN FRAGMENT SHADER");
        	} else {
        		System.out.println("ERROR IN GEOMETRY SHADER");
        	}
        	
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        
        return shaderID;
	}
}

