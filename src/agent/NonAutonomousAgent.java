package agent;

import environment.Room;

public abstract class NonAutonomousAgent extends Entity
{
	public NonAutonomousAgent(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);				
		initialize();
	}
	
	public NonAutonomousAgent(int x, int y, int width, int height) {
		super(x, y, width, height);	
		initialize();
	}
	
	public NonAutonomousAgent(Room room, int x, int y, int width, int height, int sides) {
		super(room, x, y, width, height, sides);	
		initialize();
	}
	
	public NonAutonomousAgent(int x, int y, int width, int height, int sides) {
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

	/********************************************/
	// CORE METHODS
	
	public void collideIntoPlayer(Player collidee) {}
	public void collideIntoPhantom(Phantom collidee) {}
	public void collideIntoAgents(Entity collidee) {}
}