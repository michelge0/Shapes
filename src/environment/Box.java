package environment;

import java.awt.Color;
import java.awt.Graphics;

import agent.Entity;
import agent.NonAutonomousAgent;

public class Box extends NonAutonomousAgent
{	
	public Box(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);
	    color = new Color(150, 100, 0);
	}
	
	public Box(int x, int y, int width, int height) {
		super(x, y, width, height);
	    color = new Color(150, 100, 0);
	}
	
	public Box(Room room, int x, int y, int width, int height, int sides) {
		super(room, x, y, width, height);
	    color = new Color(150, 100, 0);
	}
	
	public Box(int x, int y, int width, int height, int sides) {
		super(x, y, width, height);	
	    color = new Color(150, 100, 0);
	}
	
	/***************************************/
	// CORE METHODS
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(xPos, yPos, xSize, ySize);
		g.setColor(Color.BLACK);
		g.drawRect(xPos, yPos, xSize, ySize);
		g.drawRect(xPos + 1, yPos + 1, xSize - 2, ySize - 2);
	}
	
	public void collisionEvent(Entity collider) {
		takeRecoil(collider);
	}

	public void displayIcon(Graphics g) {}
	public void speakingHalo(Graphics g) {}
}
