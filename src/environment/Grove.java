package environment;
import java.awt.*;
import java.util.ArrayList;
import util.Random;
import agent.Entity;

/*
 * A zone of randomly generated trees, unlike
 * the neatness of TreeWall.
 */

public class Grove extends Barrier
{
	// how far trees are apart:
	public int sparsity;
	public int treeSize;
	public int treeFecundity;
	public ArrayList<Tree> trees = new ArrayList<Tree>();
	
	public Grove(Room room, int x, int y, int width, int height,
			int treeSize, int sparsity) {
		super(room, x, y, width, height);
		
		treeFecundity = 3;
		create(treeSize, sparsity);
	}
	
	public Grove(Room room, int x, int y, int width, int height,
			int treeSize, int sparsity, int fecundity) {
		super(room, x, y, width, height);
		
		treeFecundity = fecundity;
		create(treeSize, sparsity);
	}
	
	/***************************************/
	// TREE CREATION
	
	private void create(int treeSize, int sparsity) {
		this.treeSize = treeSize;
		this.sparsity = sparsity;
		room.addToWalls(this);
		
		makeTrees();
	}
	
	/**
	 * @makeTrees
	 * Creates a similar grid, but adds extra space equal to value of
	 * sparsity. Randomly generates a tree within each grid.
	 */
	
	public void makeTrees() {
		int columns = (int) (xSize / (treeSize + sparsity));
		int rows = (int) (ySize / (treeSize + sparsity));
				
		for (int currentColumn = 0; currentColumn < columns; currentColumn++) {
			for (int currentRow = 0; currentRow < rows; currentRow++) {
				int treeX = xPos + (currentColumn * (treeSize + sparsity)) + (int) (Math.random() * sparsity);
				int treeY = yPos + (currentRow * (treeSize + sparsity)) + (int) (Math.random() * sparsity);
				
				int size = (int) (Random.fluctuation(treeSize, 0.3));
				
				trees.add(new Tree(room, treeX, treeY, size, treeFecundity));
			}
		}
	}
	
	/***********************************/
	// TREE MODIFICATION
	
	public void createHorizontalOpening(int distance, int length) {
		// a rectangle that covers the opening:
		Rectangle opening = new Rectangle(xPos + distance, yPos, length, ySize);
	
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).getBounds().intersects(opening)) {
				room.remove(trees.get(i));
				trees.remove(i);
			}
		}
	}
	
	public void createVerticalOpening(int distance, int length) {
		// a rectangle that covers the opening:
		Rectangle opening = new Rectangle(xPos, yPos + distance, xSize, length);
	
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).getBounds().intersects(opening)) {
				room.remove(trees.get(i));
				trees.remove(i);
			}
		}
	}
	
	// clears area of trees:
	public void clearArea(int x, int y, int width, int height) {
		Rectangle area = new Rectangle(x, y, width, height);
		
		for (int i = 0; i < trees.size(); i++) {
			Tree thisTree = trees.get(i);
			if (thisTree.getBounds().intersects(area)) {
				room.remove(thisTree);
				thisTree = null;
			}
		}
	}
	
	public void darken(int amount) {
		for (int i = 0; i < trees.size(); i++) {
			trees.get(i).resetColor();
			trees.get(i).darken(amount);
		}
	}
	
	public void resetColor() {
		for (int i = 0; i < trees.size(); i++) {
			trees.get(i).resetColor();
		}
	}
	
	/***********************************/
	// CORE METHODS
	
	public boolean collisionEvent(Entity collider) {
		return false;
	}
	
	public void draw(Graphics g) {}
}
