package environment;

import java.awt.*;
import java.util.ArrayList;
import util.Random;
import agent.Thing;

public class TreeLeaf extends Thing
{	
	public int diameter;
	public Color color;
	public Color originalColor;
	public int fecundity;
	public double angle; // stores direction tree is growing in (angle)
	
	ArrayList<TreeLeaf> branchingLeaves = new ArrayList<TreeLeaf>();
	
	public TreeLeaf(Tree tree, int x, int y, int diameter, int fecundity, double angle) {
		tree.leaves.add(this);
		setValues(x, y, diameter, fecundity, angle);
	}
	
	public TreeLeaf(TreeLeaf branch, int x, int y, int diameter, int fecundity, double angle) {
		branch.branchingLeaves.add(this);
		setValues(x, y, diameter, fecundity, angle);
	}
	
	public void setValues(int x, int y, int diameter, int fecundity, double angle) {
		xPos = x;
		yPos = y;
		this.diameter = diameter;
		this.fecundity = fecundity;
		this.angle = angle;
		
		// G value in RGB is between 75 and 150
		int green = (int) Random.bounded(75, 150);
		color = new Color(0, green, 0);
		originalColor = color;
		
		if (fecundity > 0) {
			spawnLeaves();
		}
	}
	
	/**********************************************/
	// SPAWNING MORE LEAVES
	
	/**
	 * @spawnLeaves
	 * See Tree.spawnLeaves() for documentation.
	 */
	
	public void spawnLeaves() {
		for (int i = 0; i < fecundity; i++) {
			// leaf's diameter is a fraction of size of tree diameter
			int newDiameter = (int) (diameter * (.5 + Math.random() * .5));
			
			double randomAngle = randomAngle();
			
			int newYPos = (int) ((yPos) + (diameter * Math.sin(randomAngle)));
			int newXPos = (int) ((xPos) + (diameter * Math.cos(randomAngle)));
			
			new TreeLeaf(this, newXPos, newYPos, newDiameter, fecundity - 3, randomAngle);
		}
	}
	
	/**
	 * @randomAngle
	 * Generates an angle within pi/2 radians of angle
	 */
	
	public double randomAngle() {
		int random = (int) (Math.random() * Math.PI);
		return (angle - Math.PI / 2) + random;
	}
	
	public void drawLeaves(Graphics g) {
		for (int i = 0; i < branchingLeaves.size(); i++) {
			branchingLeaves.get(i).draw(g);
		}
	}
	
	public void darken(int amount) {
		color = new Color(0, color.getGreen() - 10 * amount, 0);
		
		if (branchingLeaves.isEmpty()) return;
		
		for (int i = 0; i < branchingLeaves.size(); i++) {
			branchingLeaves.get(i).darken(amount);
		}
	}
	
	public void resetColor() {
		color = originalColor;
		
		if (branchingLeaves.isEmpty()) return;
		
		for (int i = 0; i < branchingLeaves.size(); i++) {
			branchingLeaves.get(i).resetColor();
		}
	}
	
	/**********************************************/
	// CORE METHODS
	
	public void draw(Graphics g) {
		drawLeaves(g);
		
		g.setColor(color);
		g.fillOval(xPos - diameter / 2, yPos - diameter / 2, diameter, diameter);
	}
}
