package com.gamewolves.openwolves.materials.shaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL20;

public class ShaderDecoder
{
	/**
	 * Processes a shader file and its uniform variables and attributes
	 * 
	 * @param shader The shader object of the given shader
	 * @param path The path to the shader
	 * @param programmID the ID of the shader programm
	 */
	public static void processShader(Shader shader, String path, int programmID)
	{
		if (path == null)
			return;
		
		if (path.endsWith(".vert"))
		{
			processAttributes(shader, path);
			GL20.glLinkProgram(programmID);
			GL20.glValidateProgram(programmID);
		}
		processUniforms(shader, path);
	}
	
	/**
	 * Processes the uniforms of the shader
	 * 
	 * @param shader The shader object containing the shader
	 * @param path The path to the shader
	 */
	private static void processUniforms(Shader shader, String path)
	{
		// System.out.println(path);
		ArrayList<String> uniforms = new ArrayList<>();
		
		FileReader reader = null;
		try
		{
			reader = new FileReader(path);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line = bufferedReader.readLine();
			ArrayList<Struct> structs = new ArrayList<>();
			
			while (line != null)
			{
				if (line.startsWith("struct"))
				{
					String[] currentLine = line.split(" ");
					String identifier = currentLine[1].trim().replace("{", "");
					
					Struct struct = new Struct();
					struct.identifier = identifier;
					
					line = bufferedReader.readLine();
					
					while (true)
					{
						if (line.contains("{"))
						{
							line = bufferedReader.readLine();
							continue;
						}
						
						if (line.split(" ").length < 2)
						{
							if (line.contains("}"))
								break;
							line = bufferedReader.readLine();
							continue;
						}
						
						boolean end = line.contains("}");
						
						String[] currentVariable = line.split(" ");
						String name = currentVariable[currentVariable.length - 1].trim().replace(";", "").replace("}",
								"");
						
						struct.variables.add(name);
						
						if (end)
							break;
						
						line = bufferedReader.readLine();
					}
					
					structs.add(struct);
				}
				
				line = bufferedReader.readLine();
			}
			
			try
			{
				reader = new FileReader(path);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			
			bufferedReader = new BufferedReader(reader);
			line = bufferedReader.readLine();
			
			while (line != null)
			{
				if (line.startsWith("uniform"))
				{
					String[] currentLine = line.split(" ");
					String type = currentLine[1];
					String identifier = currentLine[currentLine.length - 1].trim().split(";")[0];
					
					int numberofArray = -1;
					
					if (line.contains("[") && line.contains("]"))
					{
						int startIndex = line.indexOf("[") + 1;
						int endIndex = line.indexOf("]");
						
						numberofArray = Integer.parseInt(line.substring(startIndex, endIndex));
					}
					
					boolean isStruct = false;
					
					for (Struct struct : structs)
					{
						if (type.equals(struct.identifier))
						{
							for (String variable : struct.variables)
							{
								if (numberofArray == -1)
								{
									uniforms.add(identifier + "." + variable);
									// System.out.println(identifier + "." + variable);
								} else
								{
									String tempIdentifier = identifier.substring(0, identifier.indexOf("["));
									for (int i = 0; i < numberofArray; i++)
									{
										uniforms.add(tempIdentifier + "[" + i + "]" + "." + variable);
										// System.out.println(tempIdentifier + "[" + i + "]" + "." + variable);
									}
								}
							}
							isStruct = true;
							break;
						}
					}
					
					if (!isStruct)
					{
						if (numberofArray == -1)
						{
							uniforms.add(identifier);
							// System.out.println(identifier);
						} else
						{
							for (int i = 0; i < numberofArray; i++)
							{
								uniforms.add(identifier + "[" + i + "]");
								// System.out.println(identifier + "[" + i + "]");
							}
						}
					}
				}
				
				line = bufferedReader.readLine();
			}
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		shader.getUniformLocations(uniforms.toArray(new String[0]));
	}
	
	/**
	 * Processes the attributes of the given shader
	 * 
	 * @param shader The shader object containing the shader
	 * @param path The path to the shader
	 */
	private static void processAttributes(Shader shader, String path)
	{
		ArrayList<String> attributes = new ArrayList<String>();
		
		FileReader reader = null;
		try
		{
			reader = new FileReader(path);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line = bufferedReader.readLine();
			
			while (line != null)
			{
				if (line.startsWith("layout"))
				{
					String[] currentLine = line.split(" ");
					
					attributes.add(currentLine[currentLine.length - 1].trim().replace(";", ""));
				}
				
				line = bufferedReader.readLine();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		shader.bindAttributes(attributes.toArray(new String[0]));
	}
	
	private static class Struct
	{
		
		public String identifier;
		public ArrayList<String> variables;
		
		public Struct()
		{
			identifier = "";
			variables = new ArrayList<>();
		}
	}
}
