package com.gamewolves.planeterra.main;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.gamewolves.planeterra.input.InputHandler;
import com.gamewolves.planeterra.player.Player;
import com.gamewolves.planeterra.render.Display;
import com.gamewolves.planeterra.render.MasterRenderer;
import com.gamewolves.planeterra.world.World;
import com.gamewolves.planeterra.world.chunkmanager.BlockDatabase;

public class Game {
	
	private static List<Player> players;
	private static World world;
	private static Object lock = new Object();
	private static boolean isDestroyed = false;

	/**
	 * Initializes the Game and runs the main gameloop
	 * After the Game is closed it will delete everything
	 */
	static void run() {
		Display.createDisplay();
		BlockDatabase.initializeDatabase();
		InputHandler.init();
		InputHandler.lockMouse(new Vector2f(Display.WIDTH / 2, Display.HEIGHT / 2));
		players = new ArrayList<Player>();
		
		new Thread(() -> {
			MasterRenderer.init();
			MasterRenderer.run();
			MasterRenderer.cleanUp();
		}).start();
		
		init();
		update();
		
		synchronized (MasterRenderer.getLock()) {
			MasterRenderer.setDestroyed(true);
			Display.closeDisplay();
		}
	}
	
	/**
	 * Initializes the Game
	 */
	private static void init() {
		while(!MasterRenderer.isInitialized()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		world = new World();
		players.add(new Player());
		world.addPlayer(players.get(0));
		InputHandler.initRayCaster(players.get(0).getCamera(), MasterRenderer.getProjectionMatrix());
	}
	
	/**
	 * Main gameloop
	 */
	private static void update() {	
		new Thread(() -> {
			while (!isDestroyed) {
				synchronized (lock) {
					if (!isDestroyed) {
						world.update();
					}
				}
			}
			
			world.cleanUp();
		}).start();
		
		while(!GLFW.glfwWindowShouldClose(Display.window)) {
			InputHandler.update();

			GLFW.glfwPollEvents();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		synchronized (lock) {
			isDestroyed = true;
		}
	}
	
	public static Player getPlayer(String ID) {
		for (Player player : players) {
			if (player.getID() == ID) {
				return player;
			}
		}
		return null;
	}

	public static List<Player> getPlayers() {
		return players;
	}

	public static World getWorld() {
		return world;
	}
}
