package states;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import core.Main;
import input.MouseHandler;
import ui.Button;
import utils.Colors;

public class Menu extends State {
	
	private Button play, settings, quit;
	public static boolean canClick;
	
	// constructor, initializes buttons
	public Menu(Main main) {
		super(main);
	}
	
	@Override
	public void init() {
		canClick = true;
		
		play = new Button(main.getWidth()/2 - 100, 150, 200, 75, "Play",
				Colors.orange, new Font("Monospaced", Font.BOLD, 24));
		play.addAction(Menu::playPressed);
		
		settings = new Button(main.getWidth()/2 - 100, 250, 200, 75, "Settings",
				Colors.orange, new Font("Monospaced", Font.BOLD, 24));
		settings.addAction(Menu::settingsPressed);
		
		quit = new Button(main.getWidth()/2 - 100, 350, 200, 75, "Quit",
				Colors.orange, new Font("Monospaced", Font.BOLD, 24));
		quit.addAction(Menu::quitPressed);
	}

	@Override
	public void tick() {
		// tick buttons
		play.tick();
		settings.tick();
		quit.tick();
		
		// allow button clicks when mouse is released,
		// keeps from automatically clicking after
		// returning from pause menu
		if(!MouseHandler.getButton(MouseEvent.BUTTON1)) {
			canClick = true;
		}
	}

	@Override
	public void render(Graphics2D g) {
		// background color
		g.setColor(Colors.black.brighter().brighter());
		g.fillRect(0, 0, main.getWidth(), main.getHeight());
		
		// menu title
		g.setColor(Colors.white);
		g.setFont(new Font("Serif", Font.BOLD, 36));
		g.drawString("Spellbound Fiefdoms",
				main.getWidth()/2 - g.getFontMetrics().stringWidth("Spellbound Fiefdoms")/2, 100);
		
		// render buttons
		play.render(g);
		settings.render(g);
		quit.render(g);
	}
	
	// method for when play button pressed,
	// changes game state to Game
	private static void playPressed() {
		if(canClick)
			StateHandler.state = STATES.Game;
	}
	
	// method for when settings button pressed,
	// changes game state to Settings
	private static void settingsPressed() {
		if(canClick)
			StateHandler.state = STATES.Settings;
	}
	
	// method for when quit button pressed,
	// exits window
	private static void quitPressed() {
		if(canClick) {
			Main.exit();
			System.exit(0);
		}
	}
	
}
