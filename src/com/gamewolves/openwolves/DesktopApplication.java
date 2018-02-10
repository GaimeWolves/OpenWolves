package com.gamewolves.openwolves;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.gamewolves.openwolves.core.OpenWolvesApplication;
import com.gamewolves.openwolves.entities.components.movement.CameraMovementComponent;
import com.gamewolves.openwolves.entities.misc.Camera;
import com.gamewolves.openwolves.entities.misc.light.Light;
import com.gamewolves.openwolves.entities.shapes.Cube;
import com.gamewolves.openwolves.entities.shapes.Model;
import com.gamewolves.openwolves.entities.shapes.Plane;
import com.gamewolves.openwolves.gl.Display;
import com.gamewolves.openwolves.input.InputHandler;
import com.gamewolves.openwolves.materials.Material;
import com.gamewolves.openwolves.materials.Shader;
import com.gamewolves.openwolves.materials.colors.Color;
import com.gamewolves.openwolves.util.conversion.Convertor;
import com.gamewolves.openwolves.util.model.ModelDatabase;
import com.gamewolves.openwolves.util.model.OBJLoader;
import com.gamewolves.openwolves.util.model.OBJLoader.ModelData;

public class DesktopApplication extends OpenWolvesApplication {
	
	Shader shader3D;
	Shader shader2D;
	Camera camera;
	
	Cube cube;
	Plane plane;
	Material materialCube, materialPlane;
	Model dragon;
	Light light;
	
	public void init(int width, int height, boolean resizable, String title) {
		super.init(this, width, height, resizable, title);
		
		InputHandler.lockMouse(new Vector2f(width / 2, height / 2));
		
		shader3D = new Shader("res/shaders/basicShader/vertex.vert", null, "res/shaders/basicShader/fragment.frag", new String[] { "position", "uvCoords" }, new String[] { "transformationMatrix", "projectionMatrix", "viewMatrix" });
		
		materialCube = new Material();
		materialCube.setTexture("res/cube.png");
		materialCube.setShader(shader3D);
		
		materialPlane = new Material();
		materialPlane.setTexture("res/smiley.png");
		materialPlane.setShader(shader3D);
		
		cube = new Cube(1, 1, 1);
		cube.transform.setPosition(new Vector3f(0, 0, -1));
		cube.mesh.material = materialCube;
		cube.mesh.useTexture = true;
		cube.mesh.loadUV(Convertor.multiplyArray(ModelDatabase.BASIC_UV_COORDS, 6));
		
		plane = new Plane(10, 10);
		plane.transform.setPosition(new Vector3f(0, -10, 0));
		plane.mesh.material = materialPlane;
		plane.mesh.useTexture = true;
		plane.mesh.loadUV(ModelDatabase.BASIC_UV_COORDS);
		//Model model = OBJLoader.loadOBJFile("res/stall.obj");
		//plane.mesh.loadMesh(model.vertices, model.indices, model.uvCoords);
		
		dragon = new Model();
		dragon.transform.setPosition(new Vector3f(0, 0, -10));
		ModelData data = OBJLoader.loadOBJFile("res/dragon.obj");
		dragon.mesh.loadMesh(data.vertices, data.indices);
		dragon.mesh.loadNormals(data.normals);
		dragon.mesh.useLighting = true;
		
		light = new Light();
		light.transform.setPosition(new Vector3f(0, 0, -15));
		light.color = Color.WHITE;
		light.ambient = 0.1f;
		
		camera = new Camera();
		camera.addComponent(new CameraMovementComponent(camera.getID()));
		
		super.update();
		super.delete();
	}

	public void update() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.3f, 0.5f, 0.7f, 1.0f);
		
		camera.update();
		
		cube.mesh.material.enableShader();
		cube.mesh.material.getShader().loadMatrix4f("projectionMatrix", camera.projection.getProjectionMatrix());
		cube.mesh.material.getShader().loadMatrix4f("viewMatrix", camera.view.getViewMatrix());
		cube.mesh.material.getShader().loadMatrix4f("transformationMatrix", cube.transform.getTransformationMatrix());
		cube.mesh.render();
		
		plane.mesh.material.getShader().loadMatrix4f("transformationMatrix", plane.transform.getTransformationMatrix());
		plane.mesh.render();
		plane.mesh.material.disableShader();
		
		dragon.transform.rotate(0, Display.getDeltaTimeInSeconds() * 45, 0);
		
		dragon.mesh.material.enableShader();
		dragon.mesh.material.getShader().loadMatrix4f("projectionMatrix", camera.projection.getProjectionMatrix());
		dragon.mesh.material.getShader().loadMatrix4f("viewMatrix", camera.view.getViewMatrix());
		dragon.mesh.material.getShader().loadMatrix4f("transformationMatrix", dragon.transform.getTransformationMatrix());
		dragon.mesh.material.getShader().loadVector3f("lightPosition", light.transform.getPosition());
		dragon.mesh.material.getShader().loadVector3f("lightColor", light.color.getRGB());
		dragon.mesh.material.getShader().loadFloat("ambient", light.ambient);
		dragon.mesh.render();
		dragon.mesh.material.disableShader();
		
		if (InputHandler.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			InputHandler.freeMouse();
		}
	}

	
	public void delete() {
		
	}
	
}
