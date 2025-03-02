package main;

import java.awt.Color;

public class SnakeBodyRect extends Rect {	
	// class constructor
	SnakeBodyRect(int x, int y, int w, int h, Color c) { super(x, y, w, h, c); }
	
	// geters and setters
	public void setX(int x) { this.x_pos = x; }
	public void setY(int y) { this.y_pos = y;}
	public int getX() { return this.x_pos; }
	public int getY() { return this.y_pos; }
}
