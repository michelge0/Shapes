package environment;
import java.awt.*;
import java.util.ArrayList;

import agent.Entity;

public class Tree extends Barrier
{
	public ArrayList<TreeLeaf> leaves = new ArrayList<TreeLeaf>();
	
	private int diameter;
	private int fecundity; // how many leaves max it can produce
	private Color color;
	private Color originalColor;
	
	public Tree(Room room, int x, int y, int diameter, int fecundity) {
		super(room, x, y, diameter, diameter);
		
		this.fecundity = fecundity;
		this.diameter = diameter;
		color = new Color(120, 75, 0);
		originalColor = color;
		room.addToWalls(this);
		
		spawnLeaves();
	}
	
	/************************************************/
	// LEAF METHODS
	
	/**
	 * @spawnLeaves
	 * The algorithm for newXPos and newYPos works as follows:
	 * the second line calculates how far away from the trunk the
	 * leaf will be, as a function of the radius. 10 + Math.random() ensures
	 * that it won't be too small, and radius - 10 adjusts for the + 10, so that
	 * leaves won't be bigger than the branch.
	 * This result is multiplied by either 1 or -1. This is randomly done by the
	 * third line.
	 * The entire result is added (or "subtracted") from xPos or yPos.
	 */
	
	public void spawnLeaves() {
		for (int i = 0; i < fecundity; i++) {
			// leaf's radius is a fraction of size of tree radius
			int newDiameter = (int) (diameter * .75);
			
			double randomAngle = randomAngle();
			
			int newYPos = (int) ((yPos + diameter / 2) + (diameter / 2 * Math.sin(randomAngle)));
			int newXPos = (int) ((xPos + diameter / 2) + (diameter / 2 * Math.cos(randomAngle)));
						
			new TreeLeaf(this, newXPos, newYPos, newDiameter, fecundity - 1, randomAngle);
		}
	}
	
	public double randomAngle() {
		return (Math.random() * Math.PI * 2);
	}
	
	public void drawLeaves(Graphics g) {
		for (int i = 0; i < leaves.size(); i++) {
			leaves.get(i).draw(g);
		}
	}
	
	public void darken(int amount) {
		color = new Color(color.getRed() - 10 * amount, color.getGreen() - 10 * amount, 0);
		
		for (int i = 0; i < leaves.size(); i++) {
			leaves.get(i).darken(amount);
		}
	}
	
	public void resetColor() {
		color = originalColor;
		
		for (int i = 0; i < leaves.size(); i++) {
			leaves.get(i).resetColor();
		}
	}
	
	/************************************************/
	// CORE METHODS:
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(xPos, yPos, diameter, diameter);
		
		drawLeaves(g);
	}
	
	public boolean collisionEvent(Entity collider) {
		return true;
	}
}
