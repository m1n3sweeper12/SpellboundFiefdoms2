package core;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import input.KeyHandler;
import input.MouseHandler;
import states.Game;
import states.STATES;
import states.Settings;
import states.StateHandler;
import utils.Colors;

public class Main implements Runnable {
	
	// window basics
	public static int screen_width, screen_height;
	private String title;
	
	// game loop variables
	private boolean running;
	private Thread thread;
	
	// render variables
	private Window window;
	private BufferStrategy bs;
	private Graphics2D g;
	
	// game state management
	private StateHandler stateHandler;
	
	// input handlers
	private MouseHandler mouseHandler;
	private KeyHandler keyHandler;
	
	// constructor, takes in window width, height, title
	public Main(int width, int height, String title) {
		Main.screen_width = width;
		Main.screen_height = height;
		this.title = title;
		// initialize input handlers,
		// ensures they are initialized before window
		mouseHandler = new MouseHandler();
		keyHandler = new KeyHandler();
	}
	
	// method to initialize the game
	private void init() {
		window = new Window(screen_width, screen_height, title);
		// enable both frame and canvas to track mouse movement and button presses
		window.getFrame().addMouseListener(mouseHandler);
		window.getFrame().addMouseMotionListener(mouseHandler);
		window.getCanvas().addMouseListener(mouseHandler);
		window.getCanvas().addMouseMotionListener(mouseHandler);
		// enable frame to track key presses
		window.getFrame().addKeyListener(keyHandler);
		Colors.initColors();
		stateHandler = new StateHandler(this);
		// TEMP
		StateHandler.state = STATES.Game;
	}
	
	// method to clean up game and
	// update necessary storage files with game info
	public static void exit() {
		Game.save();
		Settings.save();
		System.exit(0);
	}
	
	// method to update variables in main game loop
	private void tick() {
		// update current state
		stateHandler.tick();
	}
	
	// method to render game to screen
	private void render() {
		// get buffer strategy and graphics from window's canvas
		bs = window.getCanvas().getBufferStrategy();
		if(bs == null) {
			window.getCanvas().createBufferStrategy(3);
			return;
		}
		g = (Graphics2D) bs.getDrawGraphics();
		// clears canvas before drawing next frame
		g.clearRect(0, 0, screen_width, screen_height);
		/////////////////
		// RENDER HERE //
		/////////////////
		
		// render current state
		stateHandler.render(g);
		
		/////////////////
		// clean up canvas before showing to screen
		g.dispose();
		bs.show();
	}
	
	// method to start main thread
	public synchronized void start() {
		// check that thread is currently running
		if(running) return;
		// if thread is not running (initializing),
		// instantiate thread and start
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	// method to stop main thread
	public synchronized void stop() {
		// check that thread is not currently running
		if(!running) return;
		// if thread is running,
		// join thread
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// method to run main game loop
	// calls tick 60 times per second,
	// calls render as many times as possible
	public void run() {
		// run initializing method
		init();
		
		// VARIABLES FOR MAIN GAME LOOP //
		// number of times to update per second
		int fps = 60;
		// time between ticks, based on nanoseconds
		double timePerTick = 1000000000/fps;
		// tracks time since last tick
		double delta = 0;
		// time of current tick
		long now;
		// time of last tick
		long lastTime = System.nanoTime();
		// variables to track number of ticks and renders
		long timer = 0;
		int ticks = 0;
		int renders = 0;
		
		// MAIN GAME LOOP //
		while(running) {
			// set time of current tick
			now = System.nanoTime();
			// add difference between current and last tick to delta,
			// divided by time for each tick
			delta += (now - lastTime)/timePerTick;
			// add difference between current and last tick to timer
			timer += now - lastTime;
			// change lastTime to current tick
			lastTime = now;
			
			// tick until delta is less than 0,
			// 60 times per second
			while(delta >= 1) {
				tick();
				ticks++;
				delta--;
			}
			// render as many times as graphics can handle
			render();
			renders++;
			
			// reset tracking variables, add current info to title
			if(timer >= 1000000000) {
				window.getFrame().setTitle("Spellbound Fiefdoms | Greyson Morgan \t\t TICKS: " + ticks + " | FPS: " + renders);
				ticks = 0;
				renders = 0;
				timer = 0;
			}
		}
		// run exit method to clean up
		exit();
		// stop current thread
		stop();
	}
	
	/////////////////////////
	// GETTERS AND SETTERS //
	/////////////////////////
	
	public StateHandler getStateHandler() {
		return stateHandler;
	}
	
	public int getWidth() {
		return screen_width;
	}
	
	public int getHeight() {
		return screen_height;
	}
	
	public String getTitle() {
		return title;
	}
	
}
