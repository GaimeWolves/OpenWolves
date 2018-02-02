#version 400

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D textureSampler;

void main(void) {
	vec4 surfaceColour = texture(textureSampler, textureCoords);

    if (surfaceColour.a < 0.5) {
    	discard;
    }

	out_Colour = surfaceColour;
}
