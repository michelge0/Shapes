package graphics;
import environment.Barrier;
import environment.Room;

import java.awt.*;

import agent.Entity;

public class BackgroundPainting extends Barrier
{
	protected Color fillColor;
	protected Color strokeColor;
	protected int margin;
	
	protected String message;
	protected boolean collidable;
	
	public BackgroundPainting(Room room, int x, int y, int width, int height,
				Color fillColor, Color strokeColor, int layer) {
		super(room, x, y, width, height);
		shapeID = "Rectangle";
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.margin = 5;
		initialize(layer);
	}
	
	public BackgroundPainting(Room room, int x, int y, int radius,
				Color fillColor, Color strokeColor, int layer) {
		super(room, x, y, radius, radius);
		shapeID = "Circle";
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.margin = 5;
		initialize(layer);
	}
	
	public BackgroundPainting(Room room, int x, int y, int width, int height,
			Color fillColor, Color strokeColor, int margin, int layer) {
		super(room, x, y, width, height);
		shapeID = "Rectangle";
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.margin = margin;
		initialize(layer);
	}
	
	public BackgroundPainting(Room room, int x, int y, int radius,
				Color fillColor, Color strokeColor, int margin, int layer) {
		super(room, x, y, radius, radius);
		shapeID = "Circle";
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.margin = margin;
		initialize(layer);
	}
	
	// For custom defined draw methods:
	public BackgroundPainting(Room room, int x, int y, int width, int height, int layer) {
		super(room, x, y, width, height);
		shapeID = "Rectangle";
		initialize(layer);
	}
	
	public BackgroundPainting(Room room, int x, int y, int radius, int layer) {
		super(room, x, y, radius, radius);
		shapeID = "Circle";
		initialize(layer);
	}
	
	private void initialize(int layer) {
		background = true;
		this.layer = layer;
		room.addToBackground(this);
		if (layer > Barrier.MAX_LAYERS) throw new Error("Layer cannot exceed Background.MAX LAYERS");
	}
	
	/***********************************/
	// SETTING AND GETTING
	
	public void setColor(Color color) {
		this.fillColor = color;
	}
	
	public boolean isCollidable() {
	    return collidable;
    }

	public void setCollidable(boolean collidable) {
	    this.collidable = collidable;
    }
	
	public void setMargin(int newMargin) {
		margin = newMargin;
	}
	
	public void setMessage(String newMessage) {
		message = newMessage;
	}

	/**********************************/
	// CORE METHODS
	
	public void draw(Graphics g) {
		g.setColor(fillColor);

		if (shapeID.equals("Rectangle")) {
			g.setColor(fillColor);
			g.fillRect(xPos, yPos, xSize, ySize);
			
			g.setColor(strokeColor);
			g.fillRect(xPos, yPos, margin, ySize);
			g.fillRect(xPos, yPos + ySize - margin, xSize, margin);
			g.fillRect(xPos + xSize - margin, yPos, margin, ySize);
			g.fillRect(xPos, yPos, xSize, margin);
		}
		
		// width as in radius
		else if (shapeID.equals("Circle")) {
			g.setColor(strokeColor);
			g.fillOval(xPos, yPos, xSize, xSize);
			
			g.setColor(fillColor);
			g.fillOval(xPos + margin, yPos + margin, xSize - 2 * margin, xSize - 2 * margin);
		}
	}
	
	public boolean collisionEvent(Entity collider) {
		if (collider.getClass().getSimpleName().equals("Player") && message != null) {
			GUI.player.speak(1, message);
		}
		return isCollidable();
	}
}
