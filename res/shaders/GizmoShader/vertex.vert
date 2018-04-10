#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 color;

out vec3 vertexColor;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){

	vertexColor = color;
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
}
