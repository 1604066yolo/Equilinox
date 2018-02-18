package main.engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import main.entities.Camera;
import main.entities.Entity;
import main.entities.Light;
import main.models.RawModel;
import main.models.TexturedModel;
import main.renderEngine.DisplayManager;
import main.renderEngine.Loader;
import main.renderEngine.MasterRenderer;
import main.renderEngine.OBJLoader;
import main.textures.ModelTexture;
import terrain.Terrain;

public class MainGameLoop {

	public static void main(String args[]) {

		DisplayManager.createDisplay();

		Loader loader = new Loader();		
		
		RawModel model = OBJLoader.loadOBJModel("dragon", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		texture.setReflectivity(1);
		texture.setShineDamper(10);
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel, new Vector3f(0, -5, -30), 0, 0, 0, 1);
		
		Light sun = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		
		Camera camera = new Camera();
		
		MasterRenderer renderer = new MasterRenderer();
		while (!Display.isCloseRequested()) {
			entity.increaseRotation(0, 1, 0);
			camera.move();
			
			renderer.processTerrain(terrain);
			renderer.processEntity(entity);
			
			renderer.render(sun, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
