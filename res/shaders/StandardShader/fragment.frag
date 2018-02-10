#version 400 core

in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 out_Color;

uniform vec3 lightColor;
uniform float ambient;

void main(void){

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	float nDotl = dot(unitNormal,unitLightVector);
	float brightness = max(nDotl, ambient);
	vec3 diffuse = brightness * lightColor;

	out_Color = vec4(diffuse.xyz, 1.0);

}
