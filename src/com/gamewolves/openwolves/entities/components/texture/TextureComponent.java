package com.gamewolves.openwolves.entities.components.texture;

import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.textures.Texture;

public class TextureComponent extends Component {
	
	Texture texture;

	@Override
	public void update() {
		
	}
	
	@Override
	public void lateUpdate() {

	}

	@Override
	public void delete() {
		texture.delete();
	}

	public void loadTexture(String file) {
		if (texture != null) {
			texture.delete();
		}
		texture = new Texture(file);
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		if (this.texture != null) {
			texture.delete();
		}
		this.texture = texture;
	}
}
