#version 400

in vec3 finalColour;
in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D textureSampler;

void main(void){

	vec4 textureColour = texture(textureSampler, textureCoords);

	out_Colour = textureColour;
}
