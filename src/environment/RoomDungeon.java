package environment;

import agent.Creature;
import util.Random;
import graphics.GUI;

public class RoomDungeon extends Room
{
	public static final boolean OPEN = true, CLOSED = false;
	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
	
	protected Wall topWall, bottomWall, rightWall, leftWall;
	protected boolean[] openings = new boolean[4];
	
	// FOR TESTING:
	public boolean filled = false;
	
	public RoomDungeon(Dungeon dungeon, int x, int y) {
		super(dungeon, x, y);
				
		for(int i = 0; i < 4; i++) {
			setConnection(i);
		}
	}
	
	public void setConnection(int direction) {
		 if(Math.random() > 0)
			this.openings[direction] = OPEN;
	}
	
	/******************************************/
	// UTILITIES:
	
	public boolean isConnected(int direction) {
		try {
			if (this.openings[direction] == OPEN			// if this room is open in direction
					&& this.getAdjacent(direction).		// if the adjacent room in direction
					openings[opposite(direction)] == OPEN)	// is open toward this room
				return true;
			else return false;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	// xIndex and yIndex = this.xIndex, this.yIndex
	public RoomDungeon getAdjacent(int direction) {
		if (direction == UP)
			return (RoomDungeon) dungeon.rooms.get(xIndex).get(yIndex + 1);
		
		else if (direction == DOWN)
			return (RoomDungeon) dungeon.rooms.get(xIndex).get(yIndex - 1);
		
		else if (direction == RIGHT)
			return (RoomDungeon) dungeon.rooms.get(xIndex + 1).get(yIndex);
		
		else if (direction == LEFT)
			return (RoomDungeon) dungeon.rooms.get(xIndex - 1).get(yIndex);
		
		else return null;
	}

	public static int opposite(int direction) {
		switch (direction)
		{
		case UP: return DOWN;
		case DOWN: return UP;
		case LEFT: return RIGHT;
		case RIGHT: return LEFT;
		default: return -1;
		}
	}
	
	/******************************/
	// SPAWNING
	
	public void setSpawn(int direction) {
		Gate entrance;

		switch (direction) {		
		case UP:
			entrance = topWall.entranceGate;
			spawnLocation.setX(entrance.getXPos() + entrance.getWidth() / 2);
			spawnLocation.setY(entrance.getYPos() + 30);
			break;
		case DOWN:
			entrance = bottomWall.entranceGate;
			spawnLocation.setX(entrance.getXPos() + entrance.getWidth() / 2);
			spawnLocation.setY(entrance.getYPos() - 30);
			break;
		case LEFT:
			entrance = leftWall.entranceGate;
			spawnLocation.setX(entrance.getXPos() + 30);
			spawnLocation.setY(entrance.getYPos() + entrance.getHeight() / 2);
			break;
		case RIGHT:
			entrance = rightWall.entranceGate;
			spawnLocation.setX(entrance.getXPos() - 30);
			spawnLocation.setY(entrance.getYPos() + entrance.getHeight() / 2);
			break;
		}
	}
	
	/*****************************/
	// ROOM CREATION
	
	// Use graphics.getHeight() etc. to get width and height
	public void createWalls(int width, int height) {
		// if walls already created: only needs to check top wall
		if (topWall != null) return;
		
		topWall = new Wall(this, 0, 0, width, 10);
		leftWall = new Wall(this, 0, 0, 10, height);
		rightWall = new Wall(this, width - 10, 0, 10, height);
		bottomWall = new Wall(this, 0, height - 10, width, 10);

		/* 
		 * 50 + Math.random() * (width OR height - 100) is an algorithm
		 * that ensures that the door will not be within 50 or so pixels of
		 * either wall.
		 */
				
		if (dungeon.isLocationConnected(RoomDungeon.UP)) {
			int opening = (int) (Random.bounded(100, width - 100));
			topWall.setHorizontalEntrance(opening - 20, 50, UP);
		}
		
		if (dungeon.isLocationConnected(RoomDungeon.DOWN)) {
			int opening = (int) (Random.bounded(100, width - 100));
			bottomWall.setHorizontalEntrance(opening - 20, 50, DOWN);	
		}
		
		if (dungeon.isLocationConnected(RoomDungeon.RIGHT)) {
			int opening = (int) (Random.bounded(100, width - 100));
			rightWall.setVerticalEntrance(opening - 20, 50, RIGHT);
		}
		
		if (dungeon.isLocationConnected(RoomDungeon.LEFT)) {
			int opening = (int) (Random.bounded(100, width - 100));
			leftWall.setVerticalEntrance(opening - 20, 50, LEFT);
		}
		
		spawnCreatures();
	}
	
	/***************************************************/
	// CREATURE SPAWNING
	
	public void spawnCreatures(int numberCreatures) {
		calculateSpawnedCreatureDimensions(numberCreatures);
	}
	
	public void spawnCreatures() {
		int numberCreatures = (int) (Math.random() * 5);
		calculateSpawnedCreatureDimensions(numberCreatures);
	}
	
	// tool of the spawnCreatures method:
	public void calculateSpawnedCreatureDimensions(int numberCreatures) {
		if (this.agents.size() > 0) return;
		
		for (int i = 0; i < numberCreatures; i++) {
			int randomX = (int) (Math.random() * GUI.graphics.getWidth());
			int randomY = (int) (Math.random() * GUI.graphics.getHeight());
			int randomWidth = (int) (5 + Math.random() * 45);
			int randomHeight = (int) (5 + Math.random() * 45);
			
			Creature potentialCreature = new Creature(this, randomX, randomY, randomWidth, randomHeight, 4);
			
			// can't spawn on top of an existing thing:
			if (potentialCreature.collideAny()) {
				agents.remove(potentialCreature);
				potentialCreature = null;
				i--;
				continue;
			}	
		}
	}
	
	public void mixCreatures() {
		int numberCreatures = agents.size();
		agents.clear();
		spawnCreatures(numberCreatures);
	}
}