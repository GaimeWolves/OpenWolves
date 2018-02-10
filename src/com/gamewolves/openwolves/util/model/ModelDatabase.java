package com.gamewolves.openwolves.util.model;

import com.gamewolves.openwolves.util.conversion.Convertor;

public class ModelDatabase {
	
	// Holds values for basic shapes 

	public static final int[] CUBE_INDICES = new int[] {
		     0,  1,  2,
		     2,  3,  0,
		     4,  5,  6,
		     6,  7,  4,
		     8,  9, 10,
		    10, 11,  8,
		    12, 13, 14,
		    14, 15, 12,
		    16, 17, 18,
		    18, 19, 16,
		    20, 21, 22,
		    22, 23, 20,
	};
	
	private static final float[] BASE_UV_COORDS = new float[] {
		    0.0f, 0.0f,
		    1.0f, 0.0f,
		    1.0f, 1.0f,
		    0.0f, 1.0f
	};
	
	public static final float[] BASIC_UV_COORDS = Convertor.multiplyArray(BASE_UV_COORDS, 6);
}
