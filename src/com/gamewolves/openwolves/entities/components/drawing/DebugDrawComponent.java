package com.gamewolves.openwolves.entities.components.drawing;

import java.util.UUID;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.gamewolves.openwolves.entities.Loader;
import com.gamewolves.openwolves.entities.components.Component;
import com.gamewolves.openwolves.entities.components.position.TransformComponent;
import com.gamewolves.openwolves.entities.misc.Camera;
import com.gamewolves.openwolves.materials.colors.Color;
import com.gamewolves.openwolves.materials.shaders.Shader;
import com.gamewolves.openwolves.util.conversion.Convertor;

public class DebugDrawComponent extends Component
{
	
	private int lineVaoID, triangleVaoID;
	private Line[] lines;
	private Triangle[] triangles;
	public TransformComponent transformReference;
	private static Shader gizmoShader;
	
	public boolean draw;
	
	public DebugDrawComponent(UUID entity)
	{
		super(entity);
		draw = false;
		transformReference = Component.getComponent(entity, TransformComponent.class);
		if (gizmoShader == null)
		{
			gizmoShader = new Shader("res/shaders/GizmoShader/vertex.vert", null,
					"res/shaders/GizmoShader/fragment.frag");
		}
	}
	
	@Override
	public void lateUpdate()
	{
		if (!draw)
			return;
		gizmoShader.start();
		gizmoShader.loadMatrix4f("transformationMatrix", transformReference.getModelMatrix());
		gizmoShader.loadMatrix4f("projectionMatrix", Camera.getMainCam().projection.getProjectionMatrix());
		gizmoShader.loadMatrix4f("viewMatrix", Camera.getMainCam().view.getViewMatrix());
		if (lines != null && lines.length > 0)
		{
			GL30.glBindVertexArray(lineVaoID);
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL11.glDrawArrays(GL11.GL_LINES, 0, lines.length * 2);
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
		}
		if (triangles != null && triangles.length > 0)
		{
			GL30.glBindVertexArray(triangleVaoID);
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, triangles.length * 3);
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
		}
		gizmoShader.stop();
	}
	
	/**
	 * Creates a VAO for the lines in the Gizmo
	 * 
	 * @param lines The lines of the Gizmo
	 */
	public void setLines(Line[] lines)
	{
		this.lines = lines;
		
		float[] colors = new float[lines.length * 6];
		for (int i = 0; i < lines.length; i++)
		{
			colors[i * 6] = lines[i].color.r;
			colors[i * 6 + 1] = lines[i].color.g;
			colors[i * 6 + 2] = lines[i].color.b;
			colors[i * 6 + 3] = lines[i].color.r;
			colors[i * 6 + 4] = lines[i].color.g;
			colors[i * 6 + 5] = lines[i].color.b;
		}
		
		lineVaoID = Loader.loadVAO_V_ANY(Convertor.Vector3fToFloatArray(Line.combineLines(lines)),
				new float[][] { colors }, new int[] { 3 });
	}
	
	/**
	 * Creates a VAO for the triangles in the Gizmo
	 * 
	 * @param triangles The triangles of the Gizmo
	 */
	public void setTriangles(Triangle[] triangles)
	{
		this.triangles = triangles;
		
		float[] colors = new float[triangles.length * 9];
		for (int i = 0; i < triangles.length; i++)
		{
			colors[i * 9] = triangles[i].color.r;
			colors[i * 9 + 1] = triangles[i].color.g;
			colors[i * 9 + 2] = triangles[i].color.b;
			colors[i * 9 + 3] = triangles[i].color.r;
			colors[i * 9 + 4] = triangles[i].color.g;
			colors[i * 9 + 5] = triangles[i].color.b;
			colors[i * 9 + 6] = triangles[i].color.r;
			colors[i * 9 + 7] = triangles[i].color.g;
			colors[i * 9 + 8] = triangles[i].color.b;
		}
		
		triangleVaoID = Loader.loadVAO_V_ANY(Convertor.Vector3fToFloatArray(Triangle.combineTriangles(triangles)),
				new float[][] { colors }, new int[] { 3 });
	}
	
	public static class Line
	{
		public Vector3f a, b;
		public Color color;
		
		public Line(Vector3f a, Vector3f b, Color color)
		{
			super();
			this.a = a;
			this.b = b;
			this.color = color;
		}
		
		public static Vector3f[] combineLines(Line[] lines)
		{
			Vector3f[] vertices = new Vector3f[lines.length * 2];
			for (int i = 0; i < lines.length; i++)
			{
				vertices[i * 2] = lines[i].a;
				vertices[i * 2 + 1] = lines[i].b;
			}
			return vertices;
		}
	}
	
	public static class Triangle
	{
		public Vector3f a, b, c;
		public Color color;
		
		public Triangle(Vector3f a, Vector3f b, Vector3f c, Color color)
		{
			super();
			this.a = a;
			this.b = b;
			this.c = c;
			this.color = color;
		}
		
		public static Vector3f[] combineTriangles(Triangle[] triangles)
		{
			Vector3f[] vertices = new Vector3f[triangles.length * 3];
			for (int i = 0; i < triangles.length; i++)
			{
				vertices[i * 3] = triangles[i].a;
				vertices[i * 3 + 1] = triangles[i].b;
				vertices[i * 3 + 2] = triangles[i].c;
			}
			return vertices;
		}
	}
}
