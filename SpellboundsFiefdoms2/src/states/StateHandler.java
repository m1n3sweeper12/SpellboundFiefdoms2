package states;

import java.awt.Graphics2D;

import core.Main;

public class StateHandler {
	
	// enum to track states
	public static STATES state;
	
	// state class instances
	private Game game;
	private Menu menu;
	private Settings settings;
	private Pause pause;
	
	// constructor, initializes each state and sets state
	public StateHandler(Main main) {
		state = STATES.Menu; // default start is menu
		game = new Game(main);
		game.init();
		menu = new Menu(main);
		menu.init();
		settings = new Settings(main);
		settings.init();
		pause = new Pause(main);
		pause.init();
	}
	
	// update current state
	public void tick() {
		switch(state) {
		case Game:
			game.tick();
			break;
		case Menu:
			menu.tick();
			break;
		case Settings:
			settings.tick();
			break;
		case Pause:
			pause.tick();
			break;
		}
	}
	
	// render current state
	public void render(Graphics2D g) {
		switch(state) {
		case Game:
			game.render(g);
			break;
		case Menu:
			menu.render(g);
			break;
		case Settings:
			settings.render(g);
			break;
		case Pause:
			game.render(g);
			pause.render(g);
			break;
		}
	}
	
	// save all states
	public void save() {
		Game.save();
		Settings.save();
	}
	
}
