package com.gamewolves.openwolves.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.gamewolves.openwolves.entities.components.Component;

public abstract class Entity {
	
	private static HashMap<UUID, ? extends Entity> entities = new HashMap<>();
	
	protected int vaoID;
	protected UUID ID;
	
	private ArrayList<Component> components;

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
	
	/**
	 * Deletes the entity
	 */
	public void delete() {
		for (int i = 0; i < components.size(); i++) {
			Component.deleteComponent(ID, components.get(i).getClass());
		}
		components.clear();
		removeEntity(ID);
	}
	
	/**
	 * Adds a component to the ArrayList of components (deletes the old one if it has one)
	 * @param component The component to add
	 */
	public <T extends Component> void addComponent(T component) {
		if (getComponent(component.getClass()) != null)
			deleteComponent(component.getClass());
		components.add(component);
	}
	
	/**
	 * Gets a component from the ArrayList of components (returns null if none found)
	 * @param component The component to search
	 * @return The found component
	 */
	public <T extends Component> T getComponent(Class<T> component) {
		for (T currentComponent : (ArrayList<T>)components) {
			if (currentComponent.getClass() == component) return currentComponent; 
		}
		return null;
	}
	
	/**
	 * Deletes a component of the entity
	 * @param component the component to delete
	 */
	public <T extends Component> void deleteComponent(Class<T> component) {
		for (T currentComponent : (ArrayList<T>)components) {
			if (currentComponent.getClass() == component) {
				currentComponent.delete();
				Component.deleteComponent(ID, component);
				components.remove(currentComponent);
				return;
			}
		}
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
