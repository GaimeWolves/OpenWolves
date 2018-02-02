package com.gamewolves.planeterra.render;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.gamewolves.planeterra.gui.GUIRenderer;
import com.gamewolves.planeterra.gui.text.fontmeshcreator.FontType;
import com.gamewolves.planeterra.gui.text.fontmeshcreator.GUIText;
import com.gamewolves.planeterra.gui.text.fontmeshcreator.TextMeshData;
import com.gamewolves.planeterra.input.InputHandler;
import com.gamewolves.planeterra.main.Game;
import com.gamewolves.planeterra.model.Model;
import com.gamewolves.planeterra.model.ModelData;
import com.gamewolves.planeterra.player.Player;
import com.gamewolves.planeterra.textures.Texture;
import com.gamewolves.planeterra.textures.TextureAtlas;
import com.gamewolves.planeterra.util.Maths;
import com.gamewolves.planeterra.world.chunkmanager.chunk.ChunkRenderer;

public class MasterRenderer {

	private static Loader loader;
	private static boolean destroyed = false, initialized = false;
	private static Object lock = new Object();

	private static TextureAtlas blockTextures;
	private static Matrix4f projectionMatrix;

	private static Queue<ModelData> loadingQueue;
	private static Queue<Model> deletionQueue;
	private static Queue<GUIText> textQueue;

	private static ChunkRenderer chunkRenderer;

	//////// TEMPORARY VARS/////////
	private static GUIText FPSText, PlayerPos, PlayerChunk, PlayerLookingDirection, PlayerLookingBlock,
			PlayerInventoryBlock, PlayerRotation;

	/**
	 * Initializes the Renderer
	 */
	public static void init() {
		glfwMakeContextCurrent(Display.window);
		glfwSwapInterval(1);
		GL.createCapabilities();
		loader = new Loader();
		Texture blocksTexture = loader.loadTexture("res/blocks/blocks.png");
		blockTextures = new TextureAtlas(blocksTexture, blocksTexture.getWidth() / 16);

		deletionQueue = new ConcurrentLinkedQueue<Model>();
		loadingQueue = new ConcurrentLinkedQueue<ModelData>();
		textQueue = new ConcurrentLinkedQueue<GUIText>();
		createProjectionMatrix();

		chunkRenderer = new ChunkRenderer();
		GUIRenderer.init();
		enableBackFaceCulling();

		FontType font = new FontType(loader.loadTexture("res/fonts/debugFont.png").getTextureID(),
				new File("res/fonts/debugFont.fnt"));
		FPSText = new GUIText("", 1f, font, new Vector2f(0.01f, 0.01f), 1.f, false);
		PlayerPos = new GUIText("", 1f, font, new Vector2f(0.01f, 0.04f), 1.f, false);
		PlayerRotation = new GUIText("", 1f, font, new Vector2f(0.01f, 0.07f), 1.f, false);
		PlayerChunk = new GUIText("", 1f, font, new Vector2f(0.01f, 0.10f), 1.f, false);
		PlayerLookingDirection = new GUIText("", 1f, font, new Vector2f(0.01f, 0.13f), 1.f, false);
		PlayerLookingBlock = new GUIText("", 1f, font, new Vector2f(0.01f, 0.16f), 1.f, false);
		PlayerInventoryBlock = new GUIText("", 1f, font, new Vector2f(0.01f, 0.19f), 1.f, false);

		initialized = true;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * Enables BackFaceCulling
	 */
	public static void enableBackFaceCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	/**
	 * Disables BackFaceCulling
	 */
	public static void disableBackFaceCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	/**
	 * Runs the Main render loops
	 */
	public static void run() {
		while (!destroyed) {
			Display.updateDeltaTime();
			for (Player player : Game.getPlayers()) {
				player.update();
			}
			if (!Game.getPlayers().isEmpty() && InputHandler.rayCaster != null && InputHandler.rayCaster.getCurrentRay() != null) {
				FPSText.changeText("FPS: " + Math.round((1.f / Display.getDeltaTimeInSeconds())) + "   Frame Time: " + Display.getDeltaTimeInSeconds());
				
				PlayerPos.changeText("Player Position: (" + Game.getPlayers().get(0).getPosition().x + " "
						+ Game.getPlayers().get(0).getPosition().y + " " + Game.getPlayers().get(0).getPosition().z
						+ ")");
				
				PlayerRotation.changeText("Player Rotation: (" + Game.getPlayers().get(0).getRotation().x + " "
						+ Game.getPlayers().get(0).getRotation().y + " " + Game.getPlayers().get(0).getRotation().z
						+ ")");
				
				PlayerChunk.changeText("Chunk: (" + Game.getPlayers().get(0).getCurrentChunk() + ")");
				
				PlayerLookingDirection.changeText("Look Direction: (" + InputHandler.rayCaster.getCurrentRay().x + " "
						+ InputHandler.rayCaster.getCurrentRay().y + " " + InputHandler.rayCaster.getCurrentRay().z
						+ ")");
				if (Game.getPlayers() != null && Game.getPlayers().get(0) != null && Game.getPlayers().get(0).getCurrentBlockPosition()[6] != -1) { 
					Vector3f pos = new Vector3f(Game.getPlayers().get(0).getCurrentBlockPosition()[3], Game.getPlayers().get(0).getCurrentBlockPosition()[4], Game.getPlayers().get(0).getCurrentBlockPosition()[5]);
					PlayerLookingBlock.changeText("Block looked at - Position: (" + pos.x + " " + pos.y + " " + pos.z + ")" + " Side: " + Game.getPlayers().get(0).getCurrentBlockPosition()[7]); 
				} else {
					PlayerLookingBlock.changeText("Block looked at - No Block"); 
				}
				 
				PlayerInventoryBlock.changeText("Inventory Block: 3");
			}
			
			processModels();
			preRender();
			render();
			postRender();
		}
	}

	public static void loadGUIText(GUIText text) {
		textQueue.add(text);
	}

	/**
	 * Loads a Model to a VAO
	 * 
	 * @param modelData
	 *            Data for the Model
	 */
	public static void loadModel(ModelData modelData) {
		synchronized (loadingQueue) {
			loadingQueue.add(modelData);
		}
	}

	/**
	 * Deletes the VAO of a Model
	 * 
	 * @param model
	 */
	public static void deleteModel(Model model) {
		synchronized (deletionQueue) {
			Model modelCopy = new Model(model);
			deletionQueue.add(modelCopy);
		}
	}

	public static void deleteText(int vaoID) {
		deletionQueue.add(new Model(vaoID, null));
	}

	/**
	 * Processes the Loading and Deleting of Models
	 */
	private static void processModels() {
		while (!loadingQueue.isEmpty()) {
			ModelData data = loadingQueue.poll();
			Model model = null;
			switch (data.getRenderer()) {
			case ChunkRenderer:
				model = loader.loadToVAO(data);
				Game.getWorld().getChunkManager().addLoadedMesh(model);
				break;
			case WaterRenderer:
				model = loader.loadToVAO(data);
				Game.getWorld().getChunkManager().addLoadedMesh(model);
				break;
			default:

				break;
			}
		}

		while (!deletionQueue.isEmpty()) {
			loader.deleteVAO(deletionQueue.poll().getVaoID());
		}

		while (!textQueue.isEmpty()) {
			GUIText text = textQueue.poll();
			TextMeshData data = text.getFont().loadText(text);
			text.setMeshInfo(loader.loadGuiTextVAO(data.getVertexPositions(), data.getTextureCoords()),
					data.getVertexCount());
		}
	}

	/**
	 * Cleares the Display
	 */
	private static void preRender() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.3f, 0.5f, 0.7f, 1.0f);
		// GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
	}

	/**
	 * Renders the Game
	 */
	private static void render() {
		if (Game.getPlayers() == null) {
			return;
		} else if (Game.getPlayers().isEmpty()) {
			return;
		}
		
		chunkRenderer.render(projectionMatrix, Game.getPlayers().get(0).getCamera(), blockTextures.getTexure().getTextureID());

		GUIRenderer.render();
	}

	/**
	 * Updates the Display
	 */
	private static void postRender() {
		synchronized (lock) {
			if (!destroyed) {
				Display.updateDisplay();
			}
		}
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void cleanUp() {
		chunkRenderer.cleanUp();
	}

	/**
	 * Creates the ProjectionMatrix
	 */
	private static void createProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		projectionMatrix.perspective(Maths.DEGREES_TO_RADIANS * Player.getFov(), Display.ASPECT_RATIO,
				Player.getNearClipPlane(), Player.getFarClipPlane());
	}

	/////// GETTERS AND SETTERS///////

	public static Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public static Loader getLoader() {
		return loader;
	}

	public static boolean isDestroyed() {
		return destroyed;
	}

	public static void setDestroyed(boolean destroyed) {
		MasterRenderer.destroyed = destroyed;
	}

	public static Object getLock() {
		return lock;
	}

	public static TextureAtlas getBlockTextures() {
		return blockTextures;
	}

	public static boolean isInitialized() {
		return initialized;
	}
}
