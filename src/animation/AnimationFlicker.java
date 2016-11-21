package animation;
import java.awt.Color;

import environment.Barrier;
import environment.Room;
import agent.Entity;


public abstract class AnimationFlicker extends Animation
{
	private boolean stopNext = false;
	private Room room;
	private Flicker flicker;
	
	public AnimationFlicker(Room room) {
		super(.1);
		this.room = room;
	}
	
	public AnimationFlicker(Room room, int timeInSeconds) {
		super(timeInSeconds);
		this.room = room;
	}
	
	public void event() {
		if (!stopNext) {
			flicker = new Flicker(room);
			action();
		}
		if (stopNext) {
			room.remove(flicker);
			flicker.setWidth(0); flicker.setHeight(0); // just in case
			flicker = null;
			stop();
		}
		stopNext = true; // only runs through twice
	}
	
	private class Flicker extends Barrier {
		public Flicker(Room room) {
			super(room, 0, 0, 500, 500);
			openColor = Color.WHITE;
			setClosedColor(Color.WHITE);
			room.addToGates(this); // gates because it is the last thing painted, so flicker will cover whole screen
		}
		
		public boolean collisionEvent(Entity collider) {
			return false;
		}
	}
	
	/**
	 * @action()
	 * Similar to event()
	 */
	
	public abstract void action();
}
