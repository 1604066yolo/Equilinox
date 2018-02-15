package main.engineTester;

import org.lwjgl.opengl.Display;

import main.renderEngine.DisplayManager;

public class MainGameLoop {
	
	public static void main(String args[]) {
		
		DisplayManager.createDisplay();
		
		while(!Display.isCloseRequested()) {
			DisplayManager.updateDisplay();
		}
		
		DisplayManager.closeDisplay();
		
	}

}
