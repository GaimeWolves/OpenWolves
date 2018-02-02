package com.gamewolves.planeterra.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gamewolves.planeterra.gui.text.fontmeshcreator.FontType;
import com.gamewolves.planeterra.gui.text.fontmeshcreator.GUIText;
import com.gamewolves.planeterra.gui.text.fontrendering.FontRenderer;
import com.gamewolves.planeterra.render.MasterRenderer;

public class GUIRenderer {

	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer fontRenderer;
	
	public static void init() {
		fontRenderer = new FontRenderer();
	}
	
	public static void render() {
		fontRenderer.render(texts);
	}
	
	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		MasterRenderer.loadGUIText(text);
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		MasterRenderer.deleteText(text.getMesh());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}
	
	public static void cleanUp() {
		fontRenderer.cleanUp();
	}
}
