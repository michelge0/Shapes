package environment;
import java.awt.*;

import agent.Entity;
import animation.Animation;

public class Door extends Barrier
{		
	private Animation event;
	private boolean hasEvent;
	
	public Door(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);
		
		room.addToWalls(this);
		setClosedColor(Color.LIGHT_GRAY);
		
		open = false;
	}
	
	/****************************/
	// SETTING AND GETTING
	
	public void setDoor(boolean state) {
		open = state;
	}
	
	public void setEvent(Animation animation) {
		hasEvent = true;
		event = animation;
	}
	
	public void clearEvent() {
		hasEvent = false;
	}
	
	/****************************/

	/**
	 * @collisionEvent if open, return false (no collision)
	 * else return true
	 */
	
	public boolean collisionEvent(Entity collider) {
		if (hasEvent) event.start();
		return !open;
	}
}
