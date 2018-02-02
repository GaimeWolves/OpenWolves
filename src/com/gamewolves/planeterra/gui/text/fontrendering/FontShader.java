package com.gamewolves.planeterra.gui.text.fontrendering;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.gamewolves.planeterra.shader.Shader;

public class FontShader extends Shader{

	private static final String VERTEX_FILE = "src/com/gamewolves/planeterra/gui/text/fontrendering/vertex.vert";
	private static final String FRAGMENT_FILE = "src/com/gamewolves/planeterra/gui/text/fontrendering/fragment.frag";
	
	private int location_translation;
	private int location_colour;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_translation = super.getUniformLocation("translation");
		location_colour = super.getUniformLocation("colour");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	protected void loadColour(Vector3f colour) {
		super.loadVector3f(location_colour, colour);
	}
	
	protected void loadTranslation(Vector2f translation) {
		super.loadVector2f(location_translation, translation);
	}
}
