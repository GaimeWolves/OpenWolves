package com.gamewolves.openwolves.materials.colors;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Color {
	
	public static final Color BLACK  = new Color(0, 0, 0, 1);
	public static final Color WHITE  = new Color(1, 1, 1, 1);
	public static final Color RED    = new Color(1, 0, 0, 1);
	public static final Color GREEN  = new Color(0, 1, 0, 1);
	public static final Color BLUE   = new Color(0, 0, 1, 1);
	public static final Color YELLOW = new Color(1, 1, 0, 1);
	public static final Color CYAN   = new Color(0, 1, 1, 1);
	public static final Color PURPLE = new Color(1, 0, 1, 1);
	
	public float r, g, b, a;

	public Color() {
		
	}

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public void setColor(Color color) {
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.a = color.a;
	}
	
	public Vector3f getRGB() {
		return new Vector3f(r, g, b);
	}
	
	public Vector4f getRGBA() {
		return new Vector4f(r, g, b, a);
	}
}
