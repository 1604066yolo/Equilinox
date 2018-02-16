package main.engineTester;

import org.lwjgl.opengl.Display;

import main.models.RawModel;
import main.models.TexturedModel;
import main.renderEngine.DisplayManager;
import main.renderEngine.Loader;
import main.renderEngine.Renderer;
import main.shaders.StaticShader;
import main.textures.ModelTexture;

public class MainGameLoop {

	public static void main(String args[]) {

		DisplayManager.createDisplay();

		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		StaticShader shader = new StaticShader(); 

		float[] vertices = {
				-.5f, .5f, 0,
				-.5f, -.5f, 0,
				.5f, -.5f, 0,
				.5f, .5f, 0
		};
		
		int[] indices = {
				0, 1, 3,
				3, 1, 2
		};
		float[] textureCoords = {
				0, 0,
				0, 1,
				1,1, 
				1, 0
		};
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("images"));
		TexturedModel texturedModel = new TexturedModel(model, texture);

		while (!Display.isCloseRequested()) {
			renderer.prepare();
			shader.start();
			renderer.render(texturedModel);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
