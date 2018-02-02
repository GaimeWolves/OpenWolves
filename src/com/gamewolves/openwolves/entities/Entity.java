package com.gamewolves.openwolves.entities;

import java.util.HashMap;
import java.util.UUID;

public abstract class Entity {
	
	private static HashMap<UUID, ? extends Entity> entities = new HashMap<>();
	
	protected int vaoID;
	protected UUID ID;

	public Entity() {
		ID = UUID.randomUUID();
		
	}

	protected void setVAO_ID(int vaoID) {
		this.vaoID = vaoID;
	}
	
	public void delete() {
		
	}
	
	public static <T extends Entity> void addEntity(UUID ID, T entity) {
		synchronized (entities) {
			((HashMap<UUID, T>) entities).put(ID, entity);
		}
	}
	
	public static void removeEntity(UUID ID) {
		synchronized (entities) {
			entities.remove(ID);
		}
	}
}
