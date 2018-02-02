package com.gamewolves.planeterra.util;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.gamewolves.planeterra.player.Camera;
import com.gamewolves.planeterra.render.Display;
import com.gamewolves.planeterra.render.MasterRenderer;

public class Maths {

	public static final float DEGREES_TO_RADIANS = (float) (java.lang.Math.PI / 180);
	public static final float RADIANS_TO_DEGREES = 1.f / DEGREES_TO_RADIANS;
	public static final Vector3f[] COMPASS = {
		new Vector3f(1, 0, 0),
		new Vector3f(-1, 0, 0),
		new Vector3f(0, 1, 0),
		new Vector3f(0, -1, 0),
		new Vector3f(0, 0, 1),
		new Vector3f(0, 0, -1)
	};
	
	/**
	 * Creates a 3D TransformationMatrix
	 * @param translation Position
	 * @param rotation Rotation
	 * @param scale Scale
	 * @return TransformationMatrix of Position Rotation and Scale
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation,  Vector3f scale) {
		Matrix4f transorfmationMatrix = new Matrix4f();
		transorfmationMatrix.translate(translation);
		transorfmationMatrix.rotateXYZ(rotation);
		transorfmationMatrix.scale(scale);
		return transorfmationMatrix;
	}
	
	/**
	 * Creates a 2D TransformationMatrix
	 * @param translation Position
	 * @param rotation Rotation
	 * @param scale Scale
	 * @return TransformationMatrix of Position Rotation and Scale
	 */
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f rotation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(translation.x, translation.y, 0.0f);
		matrix.rotateXYZ(rotation.x, rotation.y, 0.0f);
		matrix.scale(new Vector3f(scale.x, scale.y, 1f));
		return matrix;
	}
	
	/**
	 * Creates a ViewMatrix of a Camera
	 * @param camera Camera
	 * @return ViewMatrix of Camera
	 */
	public static Matrix4f createViewMatrix(Camera camera){
		  Matrix4f viewMatrix = new Matrix4f();
		  viewMatrix.rotateXYZ(new Vector3f(camera.rotation).mul(DEGREES_TO_RADIANS));
		  Vector3f negativeCameraPos = new Vector3f(camera.getPosition()).mul(-1);
		  viewMatrix.translate(negativeCameraPos);
		  return viewMatrix;
	}
	
	/**
	 * Clamps a number between two numbers (Both inclusive)
	 * @param min Minimum of number
	 * @param max Maximum of number
	 * @param number Number to clamp
	 * @return Clamped number
	 */
	public static float clamp(float min, float max, float number) {
		float returnNumber = number;
		
		if (returnNumber < min) returnNumber = min;
		else if (returnNumber > max) returnNumber = max;
		
		return returnNumber;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	/**
	 * Returns the Hypothenuse of a triangles sides (Vectors individual values)
	 * @param x x Value of Vector
	 * @param y y Value of Vector
	 * @return Hypothenuse of both values
	 */
	public static float euclidianDistance(float x, float y) {
		return (float)(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
	}
	
	/**
	 * Gets the general Direction of a vector
	 * @param vector Vectors
	 * @return Direction of Vector
	 */
	public static Vector3f getGeneralDirectionOfVector(Vector3f vector) {		
	     float maxDot = Float.NEGATIVE_INFINITY;
	     Vector3f ret = new Vector3f();
	     
	     for (Vector3f direction : COMPASS) {
	         float t = vector.dot(direction);
	         if (t > maxDot) {
	             ret = direction;
	             maxDot = t;
	         }
	     }
	     
	     return ret;
	}
	
	public static Vector3f calculateRay(Vector2f screenCoords, Camera camera) {
		Vector2f normalizedDeviceSpacePosition = calculateNormalizedDeviceSpace(screenCoords.x, screenCoords.y);
		Vector4f homogenousClipSpacePosition = new Vector4f(normalizedDeviceSpacePosition.x, normalizedDeviceSpacePosition.y, -1f, 1f);
		Vector4f eyeSpacePosition = calculateEyeSpace(homogenousClipSpacePosition);
		Vector3f worldRay = calculateWorldSpace(eyeSpacePosition, camera);
		return worldRay;
	}
	
	/**
	 * Transforms the Eye space coordinates to World space coordinates ( inverse View matrix )
	 * @param eyeSpacePosition Eye space Coordinates
	 * @return World space coordinates
	 */
	private static Vector3f calculateWorldSpace(Vector4f eyeSpacePosition, Camera camera) {
		Matrix4f viewMat = new Matrix4f(createViewMatrix(camera));
		Matrix4f inverseViewMatrix = viewMat.invert();
		Vector4f rayWorld = inverseViewMatrix.transform(eyeSpacePosition);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalize();
		return mouseRay;
	}
	
	/**
	 * Transforms the Screen coordinates to Device coordinates
	 * @param mouseX X-Pos
	 * @param mouseY Y-Pos
	 * @return Normalized Device Space Position
	 */
	private static Vector2f calculateNormalizedDeviceSpace(float mouseX, float mouseY) {
		float x = (2f * mouseX) / Display.WIDTH - 1f;
		float y = 1f - (2f * mouseY) / Display.HEIGHT;
		return new Vector2f(x, y);
	}
	
	/**
	 * Transfroms the homogenous clip space Position to Eye space Position ( inverse Projection )
	 * @param homogenousClipSpacePosition homogenous clip space Position
	 * @return Eye space Position
	 */
	private static Vector4f calculateEyeSpace(Vector4f homogenousClipSpacePosition) {
		Matrix4f projectionMat = new Matrix4f(MasterRenderer.getProjectionMatrix());
		Matrix4f inverseProjectionMatrix = projectionMat.invert();
		Vector4f eyeSpacePosition = inverseProjectionMatrix.transform(homogenousClipSpacePosition);
		return new Vector4f(eyeSpacePosition.x, eyeSpacePosition.y, -1f, 0f);
	}
}
