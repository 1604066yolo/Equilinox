package main.engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import main.entities.Camera;
import main.entities.Entity;
import main.models.RawModel;
import main.models.TexturedModel;
import main.renderEngine.DisplayManager;
import main.renderEngine.Loader;
import main.renderEngine.OBJLoader;
import main.renderEngine.Renderer;
import main.shaders.StaticShader;
import main.textures.ModelTexture;

public class MainGameLoop {

	public static void main(String args[]) {

		DisplayManager.createDisplay();

		Loader loader = new Loader();		
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		RawModel model = OBJLoader.loadOBJModel("dragon", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel, new Vector3f(0, -5, -30), 0, 0, 0, 1);
		
		Camera camera = new Camera();
		
		while (!Display.isCloseRequested()) {
			entity.increaseRotation(0, 1, 0);
			camera.move();
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
