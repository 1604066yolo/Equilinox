package main.engineTester;

import org.lwjgl.opengl.Display;

import main.renderEngine.DisplayManager;
import main.renderEngine.Loader;
import main.renderEngine.RawModel;
import main.renderEngine.Renderer;
import main.shaders.StaticShader;

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
		
		RawModel model = loader.loadToVAO(vertices, indices);

		while (!Display.isCloseRequested()) {
			renderer.prepare();
			shader.start();
			renderer.render(model);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
