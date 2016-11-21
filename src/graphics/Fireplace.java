package graphics;

import java.awt.Color;
import java.awt.Graphics;

import environment.Room;
import environment.RoomDungeon;
import environment.Wall;
import environment.Zone;

public class Fireplace extends Zone
{
	int opening; // use room direction constants
	int margin;
	Fire fire;
	
	public Fireplace(Room room, int x, int y, int width, int height, int opening) {
	    super(room, x, y, width, height);
	    
	    margin = 5;
	    this.opening = opening;
	    initialize();
    }
	
	public Fireplace(Room room, int x, int y, int width, int height, int opening, int margin) {
	    super(room, x, y, width, height);
	    
	    this.margin = margin;
	    this.opening = opening;
	    initialize();
	}
	
	private void initialize() {
	    if (opening == RoomDungeon.UP);
	    else new Wall(room, xPos, yPos, xSize, margin, Color.BLACK);
	    	
	    if (opening == RoomDungeon.DOWN); 
	    else new Wall(room, xPos, yPos + ySize - margin, xSize, margin, Color.BLACK);
	    	
	    if (opening == RoomDungeon.RIGHT);
	  	else new Wall(room, xPos - margin, yPos, margin, ySize, Color.BLACK);
	  		
	  	if (opening == RoomDungeon.LEFT);
	  	else new Wall(room, xPos, yPos, margin, ySize, Color.BLACK);
	  	
	  	fire = new Fire(room, xPos + margin, yPos + margin, xSize - margin * 2 - 10, ySize - margin * 2 - 10);
	}
	
	public void extinguish() {
		fire.extinguish();
	}
	
	/**********************************/
	// CORE METHODS
	
	public void draw(Graphics g) {
		g.setColor(new Color(100, 0, 0));
		g.fillRect(xPos, yPos, xSize, ySize);
	}
}
