#version 400 core

in vec3 surfaceNormal;
in vec3 worldPosition;

out vec4 out_Color;

struct Light
{
	vec3 Light_Position;
	vec3 Light_Color;
	vec3 Light_Attenuation;
	vec3 Light_Direction;
	float Light_AmbientIntensity;
	float Light_DiffuseIntensity;
};

uniform Light Lights[4];
uniform float SpecularIntensity;
uniform float SpecularPower;
uniform vec3 CameraPos;
uniform vec4 MaterialColor;

void main(void){

	vec4 totalDiffuse = vec4(0.0);
	vec4 averageAmbient = vec4(0.0);
	vec4 totalSpecular = vec4(0.0);

	for (int i = 0; i < 4; i++) {
		vec4 ambientLight = vec4(Lights[i].Light_Color, 1.0) * Lights[i].Light_AmbientIntensity;
		averageAmbient = averageAmbient + (ambientLight / 5);

		float diffuseFactor = dot(normalize(surfaceNormal), -Lights[i].Light_Direction);

		vec4 diffuseLight = vec4(0.0);
		vec4 specularLight = vec4(0.0);

		if (diffuseFactor > 0) {
			diffuseLight = vec4(Lights[i].Light_Color * Lights[i].Light_DiffuseIntensity * diffuseFactor, 1.0);
			totalDiffuse = totalDiffuse + diffuseLight;

			vec3 vertexToEye = normalize(CameraPos - worldPosition);
			vec3 lightReflect = normalize(reflect(Lights[i].Light_Direction, surfaceNormal));
			float specularFactor = dot(vertexToEye, lightReflect);
			if (specularFactor > 0) {
				specularFactor = pow(specularFactor, SpecularPower);
				specularLight = vec4(Lights[i].Light_Color * SpecularIntensity * specularFactor, 1.0);
				totalSpecular = totalSpecular + specularLight;
			}
		}
	}

	out_Color = MaterialColor * (averageAmbient + totalDiffuse + totalSpecular);

}
