package environment;
import java.awt.*;

import agent.Entity;
import agent.Thing;

public abstract class Barrier extends Thing
{		
	protected Color openColor = Color.LIGHT_GRAY;
	private Color closedColor = Color.LIGHT_GRAY;
	
	public boolean background;
	public int layer; // more like background layer (layer used iff background true)
		public static final int BACKGROUND = 0;
	public static final int MAX_LAYERS = 1; // layers 0 and 1
	
	public String formID = "";
	public String shapeID = "";
	
	// If open, the barrier can be walked through
	protected boolean open = false;
	
	public Barrier(Room room, int x, int y, int width, int height) {
		this.room = room;
		
		xPos = x;
		yPos = y;
		this.xSize = width;
		this.ySize = height;				
	}
	
	/********************************/
	// SETTING AND GETTING
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public int getWidth() {
		return xSize;
	}
	
	public int getHeight() {
		return ySize;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public Color getClosedColor() {
	    return closedColor;
    }

	public void setClosedColor(Color closedColor) {
	    this.closedColor = closedColor;
    }
	
	/********************************/
	// UTILITIES
	
	public void close() {
		open = false;
	}
	
	public void open() {
		open = true;
	}

	/********************************/
	// CORE METHODS
	
	/**
	 * @collisionEvent
	 * What happens when something else collides
	 * into this barrier. It also returns a value
	 * depending on whether the barrier can be
	 * passed through at this time.
	 */
	
	public abstract boolean collisionEvent(Entity collider);
	
	public void draw(Graphics g) {
		if (open) g.setColor(openColor);
		else g.setColor(getClosedColor());
		
		g.fillRect(xPos, yPos, xSize, ySize);
	}
}
