#version 400 core

in vec3 position;
in vec2 uvCoords;

out vec2 pass_uvCoords;

uniform mat4 transformationMatrix;

void main(void){

	gl_Position = transformationMatrix * vec4(position, 1.0);
	pass_uvCoords = uvCoords;
}
