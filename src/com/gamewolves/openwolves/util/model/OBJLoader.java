package com.gamewolves.openwolves.util.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class OBJLoader
{
	
	/**
	 * loads a OBJ file and converts it into a model object holding the vertices, UV
	 * coordinates, normals and indices
	 * 
	 * @param filepath The path to the file
	 * @return The model
	 */
	public static ModelData loadOBJFile(String filepath)
	{
		FileReader reader = null;
		try
		{
			reader = new FileReader(filepath);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> uvCoords = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] uvArray = null;
		int[] indicesArray = null;
		
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line;
		
		try
		{
			
			while (true)
			{
				line = bufferedReader.readLine();
				String[] currentLine = line.split(" ");
				
				if (line.startsWith("v "))
				{
					float x = Float.parseFloat(currentLine[1]);
					float y = Float.parseFloat(currentLine[2]);
					float z = Float.parseFloat(currentLine[3]);
					vertices.add(new Vector3f(x, y, z));
				} else if (line.startsWith("vt "))
				{
					float u = Float.parseFloat(currentLine[1]);
					float v = Float.parseFloat(currentLine[2]);
					uvCoords.add(new Vector2f(u, v));
				} else if (line.startsWith("vn "))
				{
					float x = Float.parseFloat(currentLine[1]);
					float y = Float.parseFloat(currentLine[2]);
					float z = Float.parseFloat(currentLine[3]);
					normals.add(new Vector3f(x, y, z));
				} else if (line.startsWith("f "))
				{
					uvArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}
			
			while (line != null)
			{
				if (!line.startsWith("f "))
				{
					line = bufferedReader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indices, uvCoords, normals, uvArray, normalsArray);
				processVertex(vertex2, indices, uvCoords, normals, uvArray, normalsArray);
				processVertex(vertex3, indices, uvCoords, normals, uvArray, normalsArray);
				
				line = bufferedReader.readLine();
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for (Vector3f vertex : vertices)
		{
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for (int i = 0; i < indices.size(); i++)
		{
			indicesArray[i] = indices.get(i);
		}
		
		return new ModelData(verticesArray, uvArray, indicesArray, normalsArray);
	}
	
	/**
	 * Adds the data of a vertex of a face into the arrays
	 * 
	 * @param vertexData The data of the vertex
	 * @param indices The list of indices
	 * @param textures The list of UV coordinates
	 * @param normals The list of normals
	 * @param uvArray The array of UV coordinates
	 * @param normalsArray The array of normals
	 */
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures,
			List<Vector3f> normals, float[] uvArray, float[] normalsArray)
	{
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		uvArray[currentVertexPointer * 2] = currentTex.x;
		uvArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}
	
	/**
	 * Data storage for model data
	 */
	public static class ModelData
	{
		public float[] vertices;
		public float[] uvCoords;
		public int[] indices;
		public float[] normals;
		
		public ModelData(float[] vertices, float[] uvCoords, int[] indices, float[] normals)
		{
			this.vertices = vertices;
			this.uvCoords = uvCoords;
			this.indices = indices;
			this.normals = normals;
		}
	}
}
