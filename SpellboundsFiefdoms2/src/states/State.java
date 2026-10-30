package states;

import java.awt.Graphics2D;

import core.Main;

public abstract class State {
	
	protected Main main;
	
	public State(Main main) {
		this.main = main;
	}
	
	public abstract void init();
	public abstract void tick();
	public abstract void render(Graphics2D g);
	
}
