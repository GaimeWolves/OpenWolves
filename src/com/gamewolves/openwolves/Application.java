package com.gamewolves.openwolves;

import org.lwjgl.opengl.GL11;

import com.gamewolves.openwolves.core.OpenWolvesApplication;
import com.gamewolves.openwolves.entities.gui.Rectangle;

public class Application extends OpenWolvesApplication {
	
	Rectangle rect;

	public Application() {
		super();
	}
	
	public void init(int width, int height, boolean resizable, String title) {
		super.init(this, width, height, resizable, title);
		
		rect = new Rectangle(0.1f, 0.5f);
		
		//CHANGE THIS SOMEHOW
		super.update();
		super.delete();
	}

	public void update() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.3f, 0.5f, 0.7f, 1.0f);
		
		rect.render();
	}

	
	public void delete() {
		
	}
	
}
