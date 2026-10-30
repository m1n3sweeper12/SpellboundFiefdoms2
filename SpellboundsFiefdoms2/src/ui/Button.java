package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import input.MouseHandler;

public class Button {
	
	private Rectangle bounds;
	private String text;
	private Color c;
	private Font f;
	private boolean hovered;
	private Runnable action;
	
	public Button(int x, int y, int width, int height, String text, Color c, Font f) {
		this.bounds = new Rectangle(x, y, width, height);
		this.text = text;
		this.c = c;
		this.f = f;
		this.hovered = false;
		this.action = null;
	}
	
	public void tick() {
		// if mouse is on button and pressed, run action
		if(MouseHandler.getBounds().intersects(bounds) && MouseHandler.getButton(MouseEvent.BUTTON1)) {
			if(action != null)
				action.run();
		// if mouse hovered, lighten button colors
		} else if(MouseHandler.getBounds().intersects(bounds)) {
			hovered = true;
		} else {
			hovered = false;
		}
	}
	
	public void render(Graphics2D g) {
		if(hovered) {
			g.setColor(c.brighter());
			g.fill(bounds);
			g.setColor(c.darker());
			g.draw(bounds);
			g.setFont(f);
			g.drawString(text, bounds.x + bounds.width/2 - g.getFontMetrics().stringWidth(text)/2,
					bounds.y + bounds.height/2 + g.getFontMetrics().getAscent() - g.getFontMetrics().getHeight()/2);
		} else {
			g.setColor(c);
			g.fill(bounds);
			g.setColor(c.darker().darker());
			g.draw(bounds);
			g.setFont(f);
			g.drawString(text, bounds.x + bounds.width/2 - g.getFontMetrics().stringWidth(text)/2,
					bounds.y + bounds.height/2 + g.getFontMetrics().getAscent() - g.getFontMetrics().getHeight()/2);
		}
	}
	
	public void addAction(Runnable action) {
		this.action = action;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
}
