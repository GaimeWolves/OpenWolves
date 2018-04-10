package com.gamewolves.openwolves.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;

import com.gamewolves.openwolves.core.Renderer;
import com.gamewolves.openwolves.entities.components.Component;

public abstract class Entity
{
	
	private static HashMap<UUID, ? extends Entity> entities = new HashMap<>();
	
	protected UUID ID;
	
	private ArrayList<Component> components;
	
	public Entity()
	{
		ID = UUID.randomUUID();
		addEntity(ID, this);
		components = new ArrayList<>();
	}
	
	/**
	 * Renders the entity
	 */
	public void render()
	{
		Renderer.renderEntity(this);
	}
	
	/**
	 * Deletes the entity
	 */
	public void delete()
	{
		for (int i = 0; i < components.size(); i++)
		{
			Component.deleteComponent(ID, components.get(i).getClass());
		}
		components.clear();
		removeEntity(ID);
	}
	
	public UUID getID()
	{
		return ID;
	}
	
	/**
	 * Adds a component to the ArrayList of components (deletes the old one if it
	 * has one)
	 * 
	 * @param component The component to add
	 */
	public <T extends Component> void addComponent(T component)
	{
		if (getComponent(component.getClass()) != null)
			deleteComponent(component.getClass());
		components.add(component);
	}
	
	/**
	 * Gets a component from the ArrayList of components (returns null if none
	 * found)
	 * 
	 * @param component The component to search
	 * @return The found component
	 */
	public <T extends Component> T getComponent(Class<T> component)
	{
		for (T currentComponent : (ArrayList<T>) components)
		{
			if (currentComponent.getClass() == component)
				return currentComponent;
		}
		return null;
	}
	
	/**
	 * Deletes a component of the entity
	 * 
	 * @param component the component to delete
	 */
	public <T extends Component> void deleteComponent(Class<T> component)
	{
		for (T currentComponent : (ArrayList<T>) components)
		{
			if (currentComponent.getClass() == component)
			{
				currentComponent.delete();
				Component.deleteComponent(ID, component);
				components.remove(currentComponent);
				return;
			}
		}
	}
	
	/**
	 * Adds an entity to the HashMap of all entities
	 * 
	 * @param ID UUID of the entity
	 * @param entity The entity to add
	 */
	public static <T extends Entity> void addEntity(UUID ID, T entity)
	{
		synchronized (entities)
		{
			((HashMap<UUID, T>) entities).put(ID, entity);
		}
	}
	
	/**
	 * Removes an entity from the HashMap
	 * 
	 * @param ID UUID of the entity
	 */
	public static void removeEntity(UUID ID)
	{
		synchronized (entities)
		{
			entities.remove(ID);
		}
	}
	
	/**
	 * Gets the entity with the assigned ID
	 * 
	 * @param ID The ID of the entity
	 * @return The found entity
	 */
	public static <T extends Entity> T getEntity(UUID ID)
	{
		return (T) entities.get(ID);
	}
	
	/**
	 * Returns a list of entities with a specific component
	 * 
	 * @param component The component to search for
	 * @return The list of entities
	 */
	public static <T extends Component> ArrayList<Entity> getEntitiesWithComponent(Class<T> component)
	{
		ArrayList<Entity> entities = new ArrayList<>();
		HashMap<UUID, ? extends Component> entitiesWithComponent = Component.getComponent(component);
		
		for (Entry<UUID, ? extends Component> UUID : entitiesWithComponent.entrySet())
		{
			entities.add(Entity.entities.get(UUID.getKey()));
		}
		
		return entities;
	}
	
	public static HashMap<UUID, ? extends Entity> getEntities()
	{
		return entities;
	}
}
