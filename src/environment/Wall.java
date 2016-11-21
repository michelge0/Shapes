package environment;
import java.awt.*;

import agent.Entity;

public class Wall extends Barrier
{	
	// optional: a gate that player appears at from another gate
	public Gate entranceGate;
	
	public Wall(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);
		
		room.addToWalls(this);
		setClosedColor(Color.GRAY);
	}
	
	public Wall(Room room, int x, int y, int width, int height, Color color) {
		super(room, x, y, width, height);
		
		room.addToWalls(this);
		setClosedColor(color);
	}
	
	/****************************************/
	// Utilities:
	
	// xPos is how far down the opening should be,
	// length is how tall
	public void createVerticalOpening(int y, int length) {
		// creates a new wall from the opening down
		new Wall(room, xPos, yPos + y + length,
				this.xSize, this.ySize - y - length);
		
		// shrinks this wall to the upper half
		this.ySize = y;
	}
	
	public void createHorizontalOpening(int x, int length) {
		// same as with HorizontalOpening:
		new Wall(room, xPos + x + length, yPos,
				this.xSize - x - length, this.ySize);
		
		this.xSize = x;
	}
	
	public void setVerticalEntrance(int y, int length, int direction) {
		entranceGate = new Gate(room, xPos, yPos + y,
				this.xSize, length, direction);
	}
	
	public void setHorizontalEntrance(int x, int length, int direction) {
		entranceGate = new Gate(room, xPos + x, yPos,
				length, this.ySize, direction);
	}
	
	/****************************************/

	// empty because nothing happens when you run into a wall
	public boolean collisionEvent(Entity collider) {
		return true;
	}
}
