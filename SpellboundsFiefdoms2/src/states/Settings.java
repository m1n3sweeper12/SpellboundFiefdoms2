package states;

import java.awt.Font;
import java.awt.Graphics2D;

import core.Main;
import ui.Button;
import utils.Colors;

public class Settings extends State{
	
	private Button back;
	
	// constructor, initializes buttons
	public Settings(Main main) {
		super(main);
	}
	
	@Override
	public void init() {
		back = new Button(10, 10, 150, 50, "Back",
				Colors.orange, new Font("Monospaced", Font.BOLD, 24));
		back.addAction(Settings::backPressed);
	}

	@Override
	public void tick() {
		// update buttons
		back.tick();
	}

	@Override
	public void render(Graphics2D g) {
		// draw background
		g.setColor(Colors.black.brighter().brighter());
		g.fillRect(0, 0, main.getWidth(), main.getHeight());
		// draw settings title
		g.setColor(Colors.white);
		g.setFont(new Font("Serif", Font.BOLD, 36));
		g.drawString("Settings", main.getWidth()/2 - g.getFontMetrics().stringWidth("Settings")/2, 50);
		
		// render buttons
		back.render(g);
	}
	
	public static void backPressed() {
		// switch to menu state
		StateHandler.state = STATES.Menu;
		save();
	}
	
	public static void save() {
		
	}
	
}
