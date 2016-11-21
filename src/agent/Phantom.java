package agent;
import java.awt.*;
import java.awt.event.*;

public class Phantom extends Player implements KeyListener
{
	// what this Phantom is a phantom of
	Player motherPlayer;
	
	public Phantom(int x, int y, int radius, Player player) {
		super(x, y, radius);
		
		color = Color.BLUE;
		motherPlayer = player;
		
		collidesWithPlayer = true;
		collidesWithPhantoms = false;
	}
	
	/********************************/
	// CORE METHODS
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(xPos - (diameter / 2), yPos - (diameter / 2), diameter, diameter);
	}
	
	public void collideIntoAgents(Entity collidee) {
		collidee.takeDamage(1);
		System.out.println("collided");
	}
}
