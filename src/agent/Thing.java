package agent;
import java.awt.*;

import environment.Room;

public abstract class Thing
{
	protected int xPos;
	protected int yPos;
	protected int xSize;
	protected int ySize;
	public Room room;
	
	protected int sides;
		public static final int CIRCLE = 0, RECTANGLE = 4;
	protected Polygon shape;	// only for non-circles / non-rectangles
	
	/****************************************/
	// SETTING AND GETTING
	
	public int getXPos() {
		return xPos;
	}
	
	public void setXPos(int amount) {
		xPos = amount;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public void setYPos(int amount) {
		yPos = amount;
	}
	
	public int getWidth() {
		return xSize;
	}
	
	public void setWidth(int amount) {
		xSize = amount;
	}
	
	public int getHeight() {
		return ySize;
	}
	
	public void setHeight(int amount) {
		ySize = amount;
	}
	
	public int getXCenter() {
		return xPos + xSize / 2;
	}
	
	public int getYCenter() {
		return yPos + ySize / 2;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(xPos, yPos, xSize, ySize);
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	/**************************************/
	// CORE METHODS
	
	public static boolean collision(Thing thing1, Thing thing2) {
		return (thing1.getBounds().intersects(thing2.getBounds()));
	}
	
	public abstract void draw(Graphics g);
}
