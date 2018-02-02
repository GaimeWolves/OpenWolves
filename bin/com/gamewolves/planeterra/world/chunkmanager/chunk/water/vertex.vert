#version 400

layout(location = 0) in vec3 position;
layout(location = 2) in vec2 vertex_textureCoords;

out vec2 geometry_textureCoords;

uniform float time;
uniform mat4 transformation;

const float PI = 3.1415926535897932384626433832795;
const float amplitude = 0.05;

float generateHeight(vec4 position){ //Calculates the Height of the Vector based on its position and the current time
	float component1 = sin(2.0 * PI * time + position.z * 8.0) * amplitude;
	float component2 = sin(2.0 * PI * time + position.x * 16.0) * amplitude;
	return component1 + component2;
}

void main(void){

	vec4 worldPosition = transformation * vec4(position, 1.0);
	float height = generateHeight(worldPosition);
	gl_Position = vec4(worldPosition.x, height + worldPosition.y, worldPosition.z, 1.0);

	geometry_textureCoords = vertex_textureCoords;
}
