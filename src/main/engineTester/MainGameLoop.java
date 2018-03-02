package main.engineTester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import main.entities.Camera;
import main.entities.Entity;
import main.entities.Light;
import main.entities.Player;
import main.guis.GuiRenderer;
import main.guis.GuiTexture;
import main.models.RawModel;
import main.models.TexturedModel;
import main.renderEngine.DisplayManager;
import main.renderEngine.Loader;
import main.renderEngine.MasterRenderer;
import main.terrain.Terrain;
import main.textures.ModelTexture;
import main.textures.TerrainTexture;
import main.textures.TerrainTexturePack;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

public class MainGameLoop {
	
	private static Map<String, TexturedModel> texturedModels = new HashMap<String, TexturedModel>();
	private static Loader loader = new Loader();
	private static List<Entity> entities = new ArrayList<Entity>();
	
	private static Random random = new Random();

	public static void main(String args[]) {

		DisplayManager.createDisplay();
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, "heightmap");
		
		//----------------------

		Player player = new Player(createModel("stanfordBunny", "white", "player"), new Vector3f(400, 3, 400), 0, 90, 0, .3f);
		Camera camera = new Camera(player);
		
		// ---------------------

		createModel("tree", "tree", "tree");
		createModel("lowPolyTree", "lowPolyTree", "lpTree");

		TexturedModel grassTexturedModel = createModel("grassModel", "grassTexture", "grass");
		grassTexturedModel.getTexture().setHasTransparency(true);
		grassTexturedModel.getTexture().setUseFakeLighting(true);
		
		TexturedModel flowerTexturedModel = createModel("grassModel", "flower", "flower");
		flowerTexturedModel.getTexture().setHasTransparency(true);
		flowerTexturedModel.getTexture().setUseFakeLighting(true);
		
		
		TexturedModel fernTexturedModel = createModel("fern", "fern", "fern", 2);
		fernTexturedModel.getTexture().setHasTransparency(true);

		// -------------

		entities.add(player);
		for (int i = 0; i < 500; i++) {
			addEntityAtRandomPos("tree", 5, terrain);
			addEntityAtRandomPos("lpTree", .5f, terrain);
			addEntityAtRandomPos("fern", .5f, terrain, random.nextInt(4));
			addEntityAtRandomPos("grass", 1, terrain);
			addEntityAtRandomPos("flower", 1, terrain);
		}
		// -----------------------

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("logo"), new Vector2f(-.87f, -.8f), new Vector2f(.155f / 2f, .25f / 2f));
		guis.add(gui);
		
		//------------------------
		
		Light sun = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));

		MasterRenderer renderer = new MasterRenderer();
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move(terrain);
			renderer.processTerrain(terrain);
			for (Entity entity : entities)
				renderer.processEntity(entity);
			renderer.render(sun, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		guiRenderer.cleanUp();
		loader.cleanUp();
		texturedModels.clear();
		entities.clear();
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
	
	public static TexturedModel createModel(String fileName, String texName, String key, int numberOfRows) {
		ModelData data = OBJFileLoader.loadOBJ(fileName);
		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		ModelTexture texture = new ModelTexture(loader.loadTexture(texName));
		texture.setReflectivity(1);
		texture.setShineDamper(10);
		texture.setNumberOfRows(numberOfRows);
		TexturedModel texturedModel = new TexturedModel(model, texture);
		texturedModels.put(key, texturedModel);
		return texturedModel;
	}
	
	public static void addEntityAtRandomPos(String key, float scale, Terrain terrain) {
		float xPos = random.nextFloat() * 800;
		float zPos = random.nextFloat() * 800;
		Vector3f pos = new Vector3f(xPos, terrain.getHeightOfTerrain(xPos, zPos), zPos);
		entities.add(new Entity(texturedModels.get(key), pos, 0, 0, 0, scale));
	}
	
	public static void addEntityAtRandomPos(String key, float scale, Terrain terrain, int texIndex) {
		float xPos = random.nextFloat() * 800;
		float zPos = random.nextFloat() * 800;
		Vector3f pos = new Vector3f(xPos, terrain.getHeightOfTerrain(xPos, zPos), zPos);
		entities.add(new Entity(texturedModels.get(key), texIndex, pos, 0, 0, 0, scale));
	}

}
