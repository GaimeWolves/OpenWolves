package com.gamewolves.openwolves.entities.components;

import java.util.HashMap;
import java.util.UUID;

public abstract class Component
{
	
	protected UUID attachedEntity;
	
	public Component(UUID entity)
	{
		Component.addComponent(entity, this);
		attachedEntity = entity;
	}
	
	/**
	 * A map of all Components and their respective Class and Entity
	 */
	private static HashMap<Class, HashMap<UUID, ? extends Component>> components = new HashMap<>();
	
	/**
	 * Update called before rendering
	 */
	public void update()
	{
	}
	
	/**
	 * Called after rendering
	 */
	public void lateUpdate()
	{
	}
	
	/**
	 * Delete called when deleting the Component
	 */
	public void delete()
	{
	}
	
	public UUID getAttachedEntity()
	{
		return attachedEntity;
	}
	
	/**
	 * Adds a component to an entity
	 * 
	 * @param entity The UUID of the entity
	 * @param component The component to add
	 */
	public static <T extends Component> void addComponent(UUID entity, T component)
	{
		synchronized (components)
		{
			HashMap<UUID, ? extends Component> store = components.get(component.getClass());
			if (store == null)
			{
				store = new HashMap<UUID, T>();
				components.put(component.getClass(), store);
			}
			((HashMap<UUID, T>) store).put(entity, component);
		}
	}
	
	/**
	 * Gets a component of an entity
	 * 
	 * @param entity The UUID of the entity
	 * @param component The component to search
	 * @return The found component
	 */
	public static <T extends Component> T getComponent(UUID entity, Class<T> component)
	{
		HashMap<UUID, ? extends Component> store = components.get(component);
		T results = (T) store.get(entity);
		if (results == null)
			throw new IllegalArgumentException(
					"Get Fail: " + entity.toString() + " does not posses Component of Class \n missing: " + component);
		return results;
	}
	
	/**
	 * Gets the HashMap of every entity having this component
	 * 
	 * @param component The component to search
	 * @return HashMap of every entity having this component
	 */
	public static <T extends Component> HashMap<UUID, ? extends Component> getComponent(Class<T> component)
	{
		synchronized (components)
		{
			return components.get(component);
		}
	}
	
	/**
	 * Checks if an entity has a specific component
	 * 
	 * @param entity The UUID of entity to check
	 * @param component The component to search
	 * @return If it has this component
	 */
	public static <T extends Component> boolean hasComponents(UUID entity, Class<T> component)
	{
		HashMap<UUID, ? extends Component> store = components.get(component);
		T results = (T) store.get(entity);
		if (results == null)
			return false;
		return true;
	}
	
	/**
	 * Returns the HashMap of components
	 * 
	 * @return HashMap of components
	 */
	public static HashMap<Class, HashMap<UUID, ? extends Component>> getComponents()
	{
		synchronized (components)
		{
			return components;
		}
	}
	
	/**
	 * Removes a component from the components HasMap
	 * 
	 * @param entity The UUID of the entity holding the component
	 * @param component The component to delete
	 */
	public static <T extends Component> void deleteComponent(UUID entity, Class<T> component)
	{
		synchronized (components)
		{
			HashMap<UUID, ? extends Component> store = components.get(component);
			store.remove(entity);
		}
	}
	
	/**
	 * Clears the HashMap of components
	 */
	public static void deleteComponents()
	{
		components.clear();
	}
}
