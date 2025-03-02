package main;

public class Settings {
	static int WINDOW_WIDTH = 640;
	static int WINDOW_HEIGHT = 480;
	static int TILE_SIZE = 20;
	// casting as int, prevents obscure spawn food glitch
	static int ROWS = (int) WINDOW_WIDTH / TILE_SIZE;
	static int COLUMNS = (int) WINDOW_HEIGHT / TILE_SIZE;
}
