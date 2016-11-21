package environment;
import graphics.GUI;
import agent.Entity;

public class ContinuousGate extends Zone
{
	public int direction;
	private Room linkedRoom;
	
	// use final variables from Room for direction
	public ContinuousGate(Room room, int xPos, int yPos, int width, int height, int direction) {
		super(room, xPos, yPos, width, height);
		
		int x = room.getXIndex(), y = room.getYIndex();
		
		switch (direction) {
		case RoomDungeon.UP: 
			linkedRoom = GUI.currentDungeon.rooms.get(x).get(y + 1);
			break;
		case RoomDungeon.DOWN:
			linkedRoom = GUI.currentDungeon.rooms.get(x).get(y - 1);
			break;
		case RoomDungeon.RIGHT:
			linkedRoom = GUI.currentDungeon.rooms.get(x + 1).get(y);
			break;
		case RoomDungeon.LEFT:
			linkedRoom = GUI.currentDungeon.rooms.get(x - 1).get(y);
			break;
		}
		
		this.direction = direction;
		open = true;
	}
	
	/*****************************/
	// CORE METHODS
	
	public boolean collisionEvent(Entity collider) {
		if (!open) return true;

		int xSpawn = 0, ySpawn = 0;		
				
		switch (direction) {
		case RoomDungeon.UP:
			xSpawn = collider.getXPos();
			ySpawn = GUI.graphics.getHeight() - 130;
			break;
		case RoomDungeon.DOWN:
			xSpawn = collider.getXPos();
			ySpawn = 30;
			break;
		case RoomDungeon.RIGHT:
			xSpawn = 30;
			ySpawn = collider.getYPos();
			break;
		case RoomDungeon.LEFT:
			xSpawn = GUI.graphics.getWidth() - 30;
			ySpawn = collider.getYPos();
		}

		if (collider.getClass().getSimpleName().equals("Player")) {
			linkedRoom.setSpawnLocation(xSpawn, ySpawn);
			Dungeon.teleport(linkedRoom);
			
			// clears all creatures in the player's way:
			for (int i = 0; i < linkedRoom.agents.size(); i++) {
				if (collider.getBounds().intersects(linkedRoom.agents.get(i).getBounds())) {
					linkedRoom.agents.remove(i);
				}
			}
		}
		return false;
	}
}
