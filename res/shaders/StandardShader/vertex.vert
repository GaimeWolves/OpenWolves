#version 400 core

layout (location = 0) in vec3 position;
layout (location = 2) in vec3 normal;

out vec3 surfaceNormal;
out vec3 worldPosition;

uniform mat4 Model;
uniform mat4 Projection;
uniform mat4 View;
uniform mat4 IT_Model;

void main(void){

	vec4 worldPos = Model * vec4(position, 1.0);
	worldPosition = worldPos.xyz;
	gl_Position = Projection * View * worldPos;

	surfaceNormal = (IT_Model * vec4(normal, 0.0)).xyz;
}
