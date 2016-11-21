package agent;

import environment.Room;


public abstract class AutonomousAgent extends Entity
{
	public AutonomousAgent(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);			
		
		initialize();
	}
	
	public AutonomousAgent(int x, int y, int width, int height) {
		super(x, y, width, height);	
		
		initialize();
	}
	
	public AutonomousAgent(Room room, int x, int y, int width, int height, int sides) {
		super(room, x, y, width, height, sides);	
		
		initialize();
	}
	
	public AutonomousAgent(int x, int y, int width, int height, int sides) {
		super(x, y, width, height, sides);	
		
		initialize();
	}
	
	private void initialize() {
		collidesWithBarriers = true;
		collidesWithCreatures = true;
		collidesWithPlayer = true;
		collidesWithPhantoms = true;
		
		movesEvery = 3;
	}
	
	/***********************************/
	// MOVEMENT
	
	public boolean near(Thing victim) {
		if (victim == null) return false;
		if (this.xPos + this.xSize > victim.xPos - 150
			&& this.xPos < victim.xPos + victim.getWidth() + 150
			&& this.yPos + this.ySize > victim.yPos - 150
			&& this.yPos < victim.yPos + victim.getHeight() + 150) {
				return true;
			}
		else return false;
	}
	
	public boolean near(Thing victim, int distance) {
		if (victim == null) return false;
		if (this.xPos + this.xSize > victim.xPos - distance
			&& this.xPos < victim.xPos + victim.getWidth() + distance
			&& this.yPos + this.ySize > victim.yPos - distance
			&& this.yPos < victim.yPos + victim.getHeight() + distance) {
				return true;
			}
		else return false;
	}
	
	public boolean near(int x, int y) {
		if (this.xPos + this.xSize > x - 150
			&& this.xPos < x + 150
			&& this.yPos + this.ySize > y - 150
			&& this.yPos < y + 150) {
				return true;
			}	
		else return false;
	}
	
	public boolean near(int x, int y, int distance) {
		if (this.xPos + this.xSize > x - distance
			&& this.xPos < x + distance
			&& this.yPos + this.ySize > y - distance
			&& this.yPos < y + distance) {
				return true;
			}	
		else return false;
	}
	
	public void track(int x, int y) {
		stopMovements();
		
		if (x < this.getXCenter()) movingLeft = true;
		else movingRight = true;
		
		if (y < this.getYCenter()) movingUp = true;
		else movingDown = true;
	}
	
	public void track(Thing victim) {
		stopMovements();
		
		if (victim.getXCenter() < this.getXCenter()) movingLeft = true;
		else movingRight = true;
		
		if (victim.getYCenter() < this.getYCenter()) movingUp = true;
		else movingDown = true;
	}

	public void move() {
		if (!canMove) return;
		super.move();
		calculateMovements();
	}
	
	/**********************************/
	// CORE METHODS
	
	/**
	 * @calculateMovements
	 * Autonomous agents must have a way of calculating
	 * where they go.
	 */
	
	public abstract void calculateMovements();
}
