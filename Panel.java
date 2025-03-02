package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

// make sure you include swing timer, not util timer 
import javax.swing.Timer;

public class Panel extends JPanel implements ActionListener, KeyListener{
	// class members
	int TILE_SIZE = Settings.TILE_SIZE;
	
	Dimension resolution;
	Rect rect;
	Timer loop;
	
	ArrayList<SnakeBodyRect> snake;
	enum Direction{ UP,DOWN,LEFT,RIGHT,NONE }
	Direction current_direction = Direction.NONE;

	Random random;
	int random_x;
	int random_y;
	Rect food;
	
	// class constructor
	Panel(int w, int h){
		// create main panel
		this.resolution = new Dimension(w, h);
		this.setPreferredSize(resolution);
		this.setBackground(Color.black);
		
		// initialize a new rectangle shape object to draw the grid
		this.rect = new Rect(100,100,this.TILE_SIZE - 1,this.TILE_SIZE - 1,Color.DARK_GRAY);
		
		// initializes a new thread on this class, to update and render graphics
		this.loop = new Timer(60, this);
		this.loop.start();
		
		// creating the snake
		this.snake = new ArrayList<SnakeBodyRect>();
		for(int i = 0; i < 3; ++i) {
			SnakeBodyRect body_rect = new SnakeBodyRect(this.TILE_SIZE * (i + 10), this.TILE_SIZE * 10, this.TILE_SIZE, this.TILE_SIZE, Color.green);
			this.snake.add(body_rect);
		}
		
		// methods to handle the keypress
		this.addKeyListener(this);
		this.setFocusable(true);
		
		// creating the food
		this.random = new Random();
		this.shuffleFoodPosition();
		this.food = new Rect(this.random_x, this.random_y, this.TILE_SIZE, this.TILE_SIZE, Color.red);
		
	}
	
	public void shuffleFoodPosition() {
		this.random_x = this.random.nextInt(Settings.ROWS) * this.TILE_SIZE;
		this.random_y = this.random.nextInt(Settings.COLUMNS) * this.TILE_SIZE;
		
	}
	
	// renders the main grid, using the rectangle shape object,
	// you don't need to call this method, but it helps visualizing
	// how the snake moves
	public void renderGrid(Graphics g) {
		for(int i = 0; i < Settings.ROWS; ++i) {
			for(int j = 0; j < Settings.COLUMNS; ++j) {
				this.rect.x_pos = i * Settings.TILE_SIZE;
				this.rect.y_pos = j * Settings.TILE_SIZE;
				this.rect.draw(g);
			}
		}
	}
	
	public void renderSnake(Graphics g) {
		this.snake.forEach((body)-> {body.draw(g);});
	}
	
	// this method moves the snake, since the snake is an array of objects
	// we need to create a new snake body rect, trust me bro, I tried to make the code shorter,
	// but when I tried to get the last element of the list it creates a hard copy of the last object
	// and glitches the snake, this is why we need to create a new object every time this method gets called
	public void updateSnake() {
        int[] head_coordinates = {
        		this.snake.getLast().x_pos, 
        		this.snake.getLast().y_pos
        		};
        
        SnakeBodyRect head = new SnakeBodyRect(head_coordinates[0], head_coordinates[1], this.TILE_SIZE, this.TILE_SIZE, Color.green);
        int current_y = head.getY();
        int current_x = head.getX();
        
        switch(this.current_direction) {
        	case UP:
	    		head.setY(current_y -= this.TILE_SIZE);
	    	    this.snake.add(head);
	    	    this.snake.remove(0);	
	    	break;
	    	
        	case DOWN:
	    	    head.setY(current_y += this.TILE_SIZE);
	    	    this.snake.add(head);
	    	    this.snake.remove(0);
    	    break;
    	    
        	case LEFT:
        		head.setX(current_x -= this.TILE_SIZE);
        		this.snake.add(head);
        		this.snake.remove(0);
        	break;
    	  
        	case RIGHT:
        		head.setX(current_x += this.TILE_SIZE);
        		this.snake.add(head);
        		this.snake.remove(0);
    		break;
		
        	default:
			break;
    	}
	}
	
	// this method checks if the head of the snake collided with the food, if it does, append a new head to the snake array
	// I used the coordinate before the last element  in the list, this will prevent game over glitches
	// when it appends the new head into the body
	public void checkFoodCollision() {
		if (this.snake.getLast().x_pos == this.food.x_pos && this.snake.getLast().y_pos == this.food.y_pos) {
			this.shuffleFoodPosition();
			this.food.x_pos = this.random_x;
			this.food.y_pos = this.random_y;
			
	        int[] head_coordinates = {
	        		this.snake.get(this.snake.size() - 1).x_pos, 
	        		this.snake.get(this.snake.size() - 1).y_pos
	        		};
	        
	        SnakeBodyRect head = new SnakeBodyRect(head_coordinates[0], head_coordinates[1], this.TILE_SIZE, this.TILE_SIZE, Color.green);
	        this.snake.add(head);
		}
	}
	

	
	// check if the snake collides with itself, or if it goes out of bounds, game over
	public void checkGameOver(){
		for(int i = 0; i < this.snake.size() - 1; ++i) {
			if(this.snake.getLast().getX() == this.snake.get(i).getX() && this.snake.getLast().getY() == this.snake.get(i).getY()) {
				this.loop.stop();
			}
		}
		if(this.snake.getLast().getX() >= Settings.WINDOW_WIDTH || this.snake.getLast().getY() == Settings.WINDOW_HEIGHT) {
			this.loop.stop();
		}
		else if(this.snake.getLast().getX() < 0 || this.snake.getLast().getY() < 0) {
			this.loop.stop();
		}
	}
	
	// RENDER
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//this.renderGrid(g);
		this.food.draw(g);
		this.renderSnake(g);
	}
	
	// UPDATE
	@Override
	public void actionPerformed(ActionEvent e) {
        // this line below fixes linux mouse lag hover window
		Toolkit.getDefaultToolkit().sync();
		this.checkFoodCollision();
		this.updateSnake();
		this.checkGameOver();
        repaint();
	}
	
	// CONTROLS
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(this.current_direction != Direction.DOWN) {
				this.current_direction = Direction.UP;
			}
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(this.current_direction != Direction.UP) {
				this.current_direction = Direction.DOWN;
			}
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(this.current_direction != Direction.RIGHT) {
				this.current_direction = Direction.LEFT;
			}
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(this.current_direction != Direction.LEFT) {
				this.current_direction = Direction.RIGHT;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}

