package environment;

import agent.Entity;
import animation.Sound;

public class CabinDoor extends Gate
{
	// general:
	public CabinDoor(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);			
	}
	
	// with linkedRoom:
	public CabinDoor(Room room, int x, int y, int width, int height, Room linkedRoom) {
		super(room, x, y, width, height, linkedRoom);
	}
	
	/**************************/
	// CORE METHODS
	
	public boolean collisionEvent(Entity collider) {
		if (super.collisionEvent(collider)) {
			return true;
		}
		else {
			Sound.CABIN_DOOR.play();
			return false;
		}
	}
}
