package environment;
import graphics.GUI;

import java.awt.*;

import agent.Entity;
import animation.Animation;

public class AutomaticDoor extends Barrier
{
	private Orientation orientation;
	
	private final int originalHeight;
	private final int originalWidth;
	
	private boolean changing;
	
	public enum Orientation { VERTICAL, HORIZONTAL };
	
	public AutomaticDoor(Room room, int x, int y, int width, int height, Orientation orientation) {
		super(room, x, y, width, height);
		
		originalHeight = height;
		originalWidth = width;
		room.addToWalls(this);
		
		this.orientation = orientation;
		setClosedColor(new Color(120, 75, 0)); // brown		
	}
	
	/*******************************************/
	// ANIMATION
	
	public void incrementWidth(int amount) {
		int newWidth = xSize + amount;
		
		if (GUI.player.getBounds().intersects(xPos, yPos, newWidth, ySize)) {
			return;
		}
		else xSize = newWidth;
	}
	
	public void incrementHeight(int amount) {
		int newHeight = ySize + amount;
		
		if (GUI.player.getBounds().intersects(xPos, yPos, xSize, newHeight)) {
			return;
		}
		else ySize = newHeight;
	}
	
	public void open() {
		if (changing) return;
		changing = true;
		
		new Animation(.01) {
			public void event() {
				switch (orientation)	{
				case VERTICAL:
					if (xSize <= 3) {
						// closes after 1 second
						new Animation(1) {
							public void event() {
								close();
								stop();
							}
						}.start();
						// ends this animation:
						stop();
					}
					incrementWidth(-1);
					break;
					
				case HORIZONTAL:
					if (ySize <= 3) {
						// closes after 1 second
						new Animation(1) {
							public void event() {
								close();
								stop();
							}
						}.start();
						// ends this animation:
						stop();
					}
					incrementHeight(-1);
					break;
				}
			}
		}.start();
	}
	
	public void close() {
		
		new Animation(.01) {
			public void event() {
				switch (orientation) {
				case VERTICAL:
					if (xSize >= originalWidth) {
						stop();
						changing = false;
						break;
					}
					incrementWidth(1);
					break;
				case HORIZONTAL:
					if (ySize >= originalHeight) {
						stop();
						changing = false;
						break;
					}
					incrementHeight(1);
					break;
				}
			}
		}.start();
	}
	
	/*******************************************/
	// CORE METHODS
	
	public boolean collisionEvent(Entity collider) {
		if (!changing) open();
		return true;		
	}
}
