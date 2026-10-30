package states;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import core.Main;
import input.KeyHandler;
import ui.Button;
import utils.Colors;

public class Pause extends State{
	
	// boolean to track pauses,
	// removes flickering between Game and Pause states
	public static boolean canPause;
	
	// pause menu buttons
	private Button menu;
	
	// constructor, initializes buttons
	public Pause(Main main) {
		super(main);
	}
	
	@Override
	public void init() {
		canPause = true;
		
		menu = new Button(main.getWidth()/2 - 75, 300, 150, 50, "Main Menu",
				Colors.orange, new Font("Monospaced", Font.BOLD, 24));
		menu.addAction(Pause::menuPressed);
	}
	
	@Override
	public void tick() {
		// handle pause state if space key pressed
		if(KeyHandler.getKey(KeyEvent.VK_SPACE)) {
			if(canPause) {
				StateHandler.state = STATES.Game;
				Game.canPause = false;
			}
		} else {
			canPause = true;
		}
		
		// update buttons
		menu.tick();
	}
	
	@Override
	public void render(Graphics2D g) {
		// draw pause panel
		g.setColor(Colors.purple);
		g.fillRect(100, 100, main.getWidth() - 200, main.getHeight() - 200);
		g.setColor(Colors.purple.darker());
		g.setStroke(new BasicStroke(5));
		g.drawRect(100, 100, main.getWidth() - 200, main.getHeight() - 200);
		g.setStroke(new BasicStroke(1));
		// draw pause title
		g.setColor(Colors.white);
		g.setFont(new Font("Monospaced", Font.BOLD, 24));
		g.drawString("Paused", main.getWidth()/2 - g.getFontMetrics().stringWidth("Paused")/2, 150);
		
		// render buttons
		menu.render(g);
	}
	
	// method to return to main menu
	public static void menuPressed() {
		// run clean up method in main
		Game.save();
		Settings.save();
		Menu.canClick = false;
		// switch to menu state
		StateHandler.state = STATES.Menu;
		
	}
	
}
