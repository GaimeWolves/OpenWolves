package com.gamewolves.openwolves;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.gamewolves.openwolves.core.OpenWolvesApplication;
import com.gamewolves.openwolves.entities.components.movement.CameraMovementComponent;
import com.gamewolves.openwolves.entities.misc.Camera;
import com.gamewolves.openwolves.entities.misc.light.Light;
import com.gamewolves.openwolves.entities.misc.light.Light.Type;
import com.gamewolves.openwolves.entities.shapes.Cube;
import com.gamewolves.openwolves.entities.shapes.Model;
import com.gamewolves.openwolves.entities.shapes.Plane;
import com.gamewolves.openwolves.gl.Display;
import com.gamewolves.openwolves.input.InputHandler;
import com.gamewolves.openwolves.materials.Material;
import com.gamewolves.openwolves.materials.colors.Color;
import com.gamewolves.openwolves.materials.shaders.Shader;
import com.gamewolves.openwolves.util.model.OBJLoader;
import com.gamewolves.openwolves.util.model.OBJLoader.ModelData;

public class DesktopApplication extends OpenWolvesApplication
{
	
	private static DesktopApplication desktopApplication;
	
	Shader shader3D;
	Shader shader2D;
	Camera camera;
	
	Cube cube;
	Plane plane;
	Material materialCube, materialPlane;
	Model dragon;
	Light light, light2, light3, light4, light5;
	
	public static void main(String[] args)
	{
		desktopApplication = new DesktopApplication();
		desktopApplication.init(1280, 720, false, "OpenWolvesTest");
	}
	
	public void init(int width, int height, boolean resizable, String title)
	{
		super.init(width, height, resizable, title);
		
		InputHandler.lockMouse(new Vector2f(width / 2, height / 2));
		
		dragon = new Model();
		dragon.transform.setPosition(new Vector3f(0, 0, -10));
		dragon.transform.setScale(new Vector3f(1, 1, 1));
		ModelData data = OBJLoader.loadOBJFile("res/dragon.obj");
		dragon.mesh.loadMesh(data.vertices, data.indices);
		dragon.mesh.loadNormals(data.normals);
		dragon.mesh.useLighting = true;
		dragon.mesh.material.setColor(1.0f, 0.5f, 0, 1.0f);
		
		light = new Light(Type.Directional);
		light.lightComponent.lightColor = Color.WHITE;
		light.lightComponent.ambientIntensity = 0.4f;
		light.lightComponent.diffuseIntensity = 0.5f;
		light.setDirection(new Vector3f(0, 1, 1));
		
		light2 = new Light(Type.Directional);
		light2.transform.position = new Vector3f(1, 1, 1);
		
		light3 = new Light(Type.Directional);
		light3.transform.position = new Vector3f(1, 2, 1);
		
		light4 = new Light(Type.Directional);
		light4.transform.position = new Vector3f(1, 2.2f, 1);
		
		light5 = new Light(Type.Directional);
		light5.transform.position = new Vector3f(1, 2.5f, 1);
		
		camera = new Camera();
		camera.addComponent(new CameraMovementComponent(camera.getID()));
		camera.setMain();
		
		super.update();
		super.delete();
	}
	
	public void update()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.3f, 0.5f, 0.7f, 1.0f);
		
		camera.update();
		
		dragon.transform.rotate(Display.getDeltaTimeInSeconds() * 30, Display.getDeltaTimeInSeconds() * 45,
				Display.getDeltaTimeInSeconds() * 60);
		dragon.render();
		
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_ESCAPE))
		{
			InputHandler.freeMouse();
		}
	}
	
	public void delete()
	{
		
	}
	
}
