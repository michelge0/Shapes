package environment;
import graphics.GUI;

import java.util.ArrayList;

public class Dungeon
{			
	public static CoordinateDungeon location = new CoordinateDungeon(1, 1);

	public ArrayList<ArrayList<Room>> rooms = new ArrayList<ArrayList<Room>>();
	
	public Dungeon(int xSize, int ySize, String dungeonType) {
		if (dungeonType.equals("Custom")) {
			rooms = new ArrayList<ArrayList<Room>>();
			
			for(int i = 0; i < xSize; i++) {
				rooms.add(new ArrayList<Room>(ySize));
				
				for(int j = 0; j < ySize; j++)
					rooms.get(i).add(new Room(this, i, j));
			}
		}
		
		else if (dungeonType.equals("Classic")){
			rooms = new ArrayList<ArrayList<Room>>();
			
			for(int i = 0; i < xSize; i++) {
				rooms.add(new ArrayList<Room>(0));
				
				for(int j = 0; j < ySize; j++) {
					rooms.get(i).add(new RoomDungeon(this, i, j));
				}
			}
		}
		
		else if (dungeonType.equals("Forest")){
			if (xSize != ySize) throw new Error("Dungeon must be a square.");
			
			rooms = new ArrayList<ArrayList<Room>>();
			
			for(int i = 0; i < xSize; i++) {
				rooms.add(new ArrayList<Room>(0));
				
				for(int j = 0; j < ySize; j++) {
					RoomForest room = new RoomForest(this, i, j);
					rooms.get(i).add(room);
					
					/*
					 * Gives shallower layers priority over deeper ones,
					 * so that row 1 and column 1 are all deepness 1, row
					 * 2 and column 2 are all deepness 2 except those that
					 * are already deepness 1, etc.
					 */
					
					for (int k = xSize; k >= 1; k--) {
						if (i == k - 1 || j == k - 1) {
							room.deepness = k;
							break;
						}
					}
				}
			}
		}
		
		else {
			System.out.println("Unrecognized dungeon type.");
			System.exit(0);
		}
	}
	
	public boolean isLocationConnected(int direction) {
		if (((RoomDungeon) location.getRoom(this)).isConnected(direction)) return true;
		else return false;
	}

	/**********************************************************/
	// SETTING AND GETTING
	
	public int getXSize() {
		return rooms.size();
	}
	
	public int getYSize() {
		return rooms.get(0).size();
	}
	
	/*****************************************/
	// SHUFFLING ROOMS
	
	private ArrayList<RoomDungeon> visitedRooms = new ArrayList<RoomDungeon>();
	private RoomDungeon focus;
	
	// Only for classic and forest dungeons:
	public void generateMaze() {
		closeAll();
		
		focus = (RoomDungeon) rooms.get(0).get(0);
		chooseDirection();
	}
	
	private boolean allFilled() {
		boolean topFilled, bottomFilled, rightFilled, leftFilled;
		try {
			topFilled = focus.getAdjacent(RoomDungeon.UP).filled;
		} catch (IndexOutOfBoundsException e) { topFilled = true; }
		try {
			bottomFilled = focus.getAdjacent(RoomDungeon.DOWN).filled;
		} catch (IndexOutOfBoundsException e) { bottomFilled = true; }
		try {
			rightFilled = focus.getAdjacent(RoomDungeon.RIGHT).filled;
		} catch (IndexOutOfBoundsException e) { rightFilled = true; }
		try {
			leftFilled = focus.getAdjacent(RoomDungeon.LEFT).filled;
		} catch (IndexOutOfBoundsException e) { leftFilled = true; }	
							
		if (rightFilled && leftFilled && topFilled && bottomFilled)
			return true;
		else return false;
	}
	
	private int getRandom() {
		ArrayList<Integer> possibilities = new ArrayList<Integer>();

		System.out.println("gd");
		
		if (focus.getAdjacent(RoomDungeon.UP) != null && !focus.getAdjacent(RoomDungeon.UP).filled) {
			possibilities.add(RoomDungeon.UP);
		}
		if (focus.getAdjacent(RoomDungeon.DOWN) != null && !focus.getAdjacent(RoomDungeon.DOWN).filled){
			possibilities.add(RoomDungeon.DOWN);
		}
		if (focus.getAdjacent(RoomDungeon.LEFT) != null && !focus.getAdjacent(RoomDungeon.LEFT).filled) {
			possibilities.add(RoomDungeon.LEFT);
		}
		if (focus.getAdjacent(RoomDungeon.RIGHT) != null && !focus.getAdjacent(RoomDungeon.RIGHT).filled) {
			possibilities.add(RoomDungeon.RIGHT);
		}
		
		System.out.println(possibilities);
		
		return possibilities.get(((int) (Math.random()) * possibilities.size()));
	}
	
	private void chooseDirection() {
		visitedRooms.add(focus);
		
		if (allFilled()) {
			System.out.println("backtracking");
			backtrack();
		}
			
		else try {
			int random = getRandom();
			switch (random) {
			case RoomDungeon.UP:
				focus.openings[RoomDungeon.UP] = true;
				focus.getAdjacent(RoomDungeon.UP).openings[RoomDungeon.DOWN] = true;
				focus.filled = true;
				focus = focus.getAdjacent(RoomDungeon.UP);
				break;
			case RoomDungeon.DOWN:
				focus.openings[RoomDungeon.DOWN] = true;
				focus.getAdjacent(RoomDungeon.DOWN).openings[RoomDungeon.UP] = true;
				focus.filled = true;
				focus = focus.getAdjacent(RoomDungeon.DOWN);
				break;
			case RoomDungeon.RIGHT:
				focus.openings[RoomDungeon.RIGHT] = true;
				focus.getAdjacent(RoomDungeon.RIGHT).openings[RoomDungeon.LEFT] = true;
				focus.filled = true;
				focus = focus.getAdjacent(RoomDungeon.RIGHT);
				break;
			case RoomDungeon.LEFT:
				focus.openings[RoomDungeon.LEFT] = true;
				focus.getAdjacent(RoomDungeon.LEFT).openings[RoomDungeon.RIGHT] = true;
				focus.filled = true;
				focus = focus.getAdjacent(RoomDungeon.LEFT);
				break;
			}
		} catch (IndexOutOfBoundsException e) {
			chooseDirection();
			return;
		}
		
		// chooses direction with new focus:
		chooseDirection();
	}
	
	private void backtrack() {
		if (focus == rooms.get(0).get(0)) return;
		else {
			focus = visitedRooms.get(visitedRooms.size() - 1);
		}
	}
	
	// sets all connections to false
	public void closeAll() {
		for (int i = 0; i < rooms.size(); i++) {
			for (int j = 0; j < rooms.get(i).size(); j++) {
				RoomDungeon thisRoom = (RoomDungeon) rooms.get(i).get(j);
				thisRoom.openings[RoomDungeon.UP] = false;
				thisRoom.openings[RoomDungeon.DOWN] = false;
				thisRoom.openings[RoomDungeon.LEFT] = false;
				thisRoom.openings[RoomDungeon.RIGHT] = false;
			}
		}
	}
	
	/**********************************************************/
	// MOVING ROOMS
	
	public static void setLocation(Room room) {
		location.setX(room.getXIndex());
		location.setY(room.getYIndex());
	}
	
	public static void teleport(Room room) {
		location.setCoordinates(GUI.currentDungeon, room);
		System.out.println(Dungeon.location.getX() + " " + Dungeon.location.getY());
		// spawns in center of room:
		int xPos = location.getRoom(GUI.currentDungeon).getXSpawn();
		int yPos = location.getRoom(GUI.currentDungeon).getYSpawn();
		
		spawn(xPos, yPos);
	}
	
	public static void spawn(int PlayerXPos, int PlayerYPos) {		
		GUI.player.setXPos(PlayerXPos);
		GUI.player.setYPos(PlayerYPos);
				
		if (location.getRoom(GUI.currentDungeon).hasEntranceEvent) {
			location.getRoom(GUI.currentDungeon).entranceEvent();
		}
		
		System.out.println("Coordinates: " + location.getX() + " " + location.getY());
	}
	
	public static void spawn(Coordinate<Integer> spawnLoc) {
		GUI.player.setXPos(spawnLoc.getX());
		GUI.player.setYPos(spawnLoc.getY());
		
		if (location.getRoom(GUI.currentDungeon).hasEntranceEvent) {
			location.getRoom(GUI.currentDungeon).entranceEvent();
		}
		
		System.out.println("Coordinates: " + location.getX() + " " + location.getY());
	}
}