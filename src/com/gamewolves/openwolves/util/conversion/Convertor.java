package com.gamewolves.openwolves.util.conversion;

import org.joml.Vector3f;

public class Convertor {

	/**
	 * Converts a Vector3f array to a float array
	 * @param vectors The Vector3f array to convert
	 * @return The converted float array
	 */
	public static float[] Vector3fToFloatArray(Vector3f[] vectors) {
		float[] values = new float[vectors.length * 3];
		
		for (int i = 0; i < vectors.length; i++) {
			values[i * 3    ] = vectors[i].x();
			values[i * 3 + 1] = vectors[i].y();
			values[i * 3 + 2] = vectors[i].z();
		}
		
		return values;
	}
}
