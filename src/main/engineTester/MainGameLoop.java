package main.engineTester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import main.terrain.Terrain;
import main.textures.ModelTexture;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

public class MainGameLoop {
	
	private static Map<String, TexturedModel> texturedModels = new HashMap<String, TexturedModel>();
	private static Loader loader = new Loader();
	private static List<Entity> entities = new ArrayList<Entity>();
	
	private static Random random = new Random();

	public static void main(String args[]) {

		DisplayManager.createDisplay();

		// -------------------------------------------------------------------------------------

		createModel("tree", "tree", "tree");
		createModel("lowPolyTree", "lowPolyTree", "lpTree");

		TexturedModel grassTexturedModel = createModel("grassModel", "grassTexture", "grass");
		grassTexturedModel.getTexture().setHasTransparency(true);
		grassTexturedModel.getTexture().setUseFakeLighting(true);
		
		
		TexturedModel fernTexturedModel = createModel("fern", "fern", "fern");
		fernTexturedModel.getTexture().setHasTransparency(true);

		// -------------

		for (int i = 0; i < 500; i++) {
			addEntityAtRandomPos("tree", 5);
			addEntityAtRandomPos("lpTree", .5f);
			addEntityAtRandomPos("fern", .5f);
			addEntityAtRandomPos("grass", 1);
		}

		texturedModels.clear();

		// ------------------------------------------------------------------------------------------

		Light sun = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		Camera camera = new Camera(new Vector3f(0, 3, 800));

		MasterRenderer renderer = new MasterRenderer();
		while (!Display.isCloseRequested()) {
			camera.move();

			renderer.processTerrain(terrain);
			for (Entity entity : entities)
				renderer.processEntity(entity);

			renderer.render(sun, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	
	public static TexturedModel createModel(String fileName, String texName, String key) {
		ModelData data = OBJFileLoader.loadOBJ(fileName);
		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		ModelTexture texture = new ModelTexture(loader.loadTexture(texName));
		texture.setReflectivity(1);
		texture.setShineDamper(10);
		TexturedModel texturedModel = new TexturedModel(model, texture);
		texturedModels.put(key, texturedModel);
		return texturedModel;
	}
	
	public static void addEntityAtRandomPos(String key, float scale) {
		float xPos = random.nextFloat() * 800;
		float zPos = random.nextFloat() * 800;
		Vector3f pos = new Vector3f(xPos, 0, zPos);
		entities.add(new Entity(texturedModels.get(key), pos, 0, 0, 0, scale));
	}

}
