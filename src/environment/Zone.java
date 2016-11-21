package environment;
import java.awt.*;

import agent.Entity;
import animation.Animation;

public class Zone extends Barrier
{
	Animation event;
	boolean hasEvent;
	
	public Zone(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);
		
		room.addToBackground(this);
	}
	
	public void setEvent(Animation newEvent) {
		if (newEvent == null) {
			hasEvent = false;
			return;
		}
		
		hasEvent = true;
		event = newEvent;
	}
	
	/*******************************************/
	// CORE METHODS
	
	public void draw(Graphics g) {}
	
	public boolean collisionEvent(Entity collider) {
		if (hasEvent) {
			event.start();
			hasEvent = false; 
		}
		return false;
	}
}
