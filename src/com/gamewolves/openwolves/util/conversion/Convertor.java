package com.gamewolves.openwolves.util.conversion;

import java.util.ArrayList;

import org.joml.Vector3f;

public class Convertor
{
	
	/**
	 * Converts a Vector3f array to a float array
	 * 
	 * @param vectors The Vector3f array to convert
	 * @return The converted float array
	 */
	public static float[] Vector3fToFloatArray(Vector3f[] vectors)
	{
		float[] values = new float[vectors.length * 3];
		
		for (int i = 0; i < vectors.length; i++)
		{
			values[i * 3] = vectors[i].x();
			values[i * 3 + 1] = vectors[i].y();
			values[i * 3 + 2] = vectors[i].z();
		}
		
		return values;
	}
	
	/**
	 * Combines a list of arrays into one array
	 * 
	 * @param arrays The list of arrays
	 * @return The combined array
	 */
	public static float[] combineFloatArrays(ArrayList<float[]> arrays)
	{
		int size = 0;
		for (int i = 0; i < arrays.size(); i++)
		{
			size += arrays.get(i).length;
		}
		float[] combinedArray = new float[size];
		int currentIndex = 0;
		for (int i = 0; i < arrays.size(); i++)
		{
			for (int j = 0; j < arrays.get(i).length; j++)
			{
				combinedArray[currentIndex++] = arrays.get(i)[j];
			}
		}
		return combinedArray;
	}
	
	/**
	 * Combines multiple instances of one array into a combined array
	 * 
	 * @param array The array to multiply
	 * @param times The amount to multiply
	 * @return The multiplied array
	 */
	public static float[] multiplyArray(float[] array, int times)
	{
		ArrayList<float[]> list = new ArrayList<>();
		for (int i = 0; i < times; i++)
		{
			list.add(array);
		}
		return combineFloatArrays(list);
	}
}
