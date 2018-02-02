package com.gamewolves.openwolves.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.gamewolves.openwolves.entities.components.Component;

public abstract class Entity {
	
	private static HashMap<UUID, ? extends Entity> entities = new HashMap<>();
	
	protected int vaoID;
	protected UUID ID;
	protected ArrayList<Component> components;

	public Entity() {
		ID = UUID.randomUUID();
		components = new ArrayList<>();
	}

	/**
	 * Sets the VAO ID of the entities model
	 * @param vaoID
	 */
	protected void setVAO_ID(int vaoID) {
		this.vaoID = vaoID;
	}
	
	public void delete() {
		for (int i = 0; i < components.size(); i++) {
			Component.deleteComponent(ID, components.get(i).getClass());
		}
		components.clear();
	}
	
	/**
	 * Adds an entity to the HashMap of all entities
	 * @param ID UUID of the entity
	 * @param entity The entity to add
	 */
	public static <T extends Entity> void addEntity(UUID ID, T entity) {
		synchronized (entities) {
			((HashMap<UUID, T>) entities).put(ID, entity);
		}
	}
	
	/**
	 * Removes an entity from the HashMap
	 * @param ID UUID of the entity
	 */
	public static void removeEntity(UUID ID) {
		synchronized (entities) {
			entities.remove(ID);
		}
	}
}
