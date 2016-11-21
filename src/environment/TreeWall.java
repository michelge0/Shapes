package environment;
/*
 * Creates a literal wall of trees--lined up like soldiers.
 */

import java.util.ArrayList;
import java.awt.*;

import agent.Entity;

public class TreeWall extends Barrier
{
	ArrayList<Tree> trees = new ArrayList<Tree>();
	public int treeSize;
	public int treeFecundity;
	
	public TreeWall(Room room, int x, int y, int width, int height, int treeSize) {
		super(room, x, y, width, height);
		
		treeFecundity = 3;
		this.treeSize = treeSize;
		
		makeTrees();
	}
	
	public TreeWall(Room room, int x, int y, int width, int height, int treeSize, int fecundity) {
		super(room, x, y, width, height);
		
		treeFecundity = fecundity;
		this.treeSize = treeSize;
		
		makeTrees();
	}
	
	/******************************************/
	// TREE CREATION
	
	/**
	 * @makeTrees
	 * Divides the grove into sections based off of tree size, like a
	 * checkerboard.
	 */
	
	public void makeTrees() {
		room.addToWalls(this);
	
		int columns = (int) (xSize / treeSize);
		int rows = (int) (ySize / treeSize);
				
		for (int currentColumn = 0; currentColumn < columns; currentColumn++) {
			for (int currentRow = 0; currentRow < rows; currentRow++) {
				int treeX = xPos + (currentColumn * treeSize);
				int treeY = yPos + (currentRow * treeSize);
				
				new Tree(room, treeX, treeY, treeSize, treeFecundity);
			}
		}
	}
	
	/******************************************/
	// CORE METHODS
	
	// You can't run into the grove, only the trees within it
	public boolean collisionEvent(Entity collider) {
		return false;
	}
	
	// Likewise, the grove isn't drawn, only the trees
	public void draw(Graphics g) {}
}
