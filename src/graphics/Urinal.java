package graphics;

import java.awt.*;

import environment.Room;

public class Urinal extends BackgroundPainting
{
	public Urinal(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height, 0);
		
		margin = 5;
		background = true;
		message = "This urinal is so big I could fall into it . . .";
	}

	/****************************************/
	// CORE METHODS
	
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(xPos, yPos, xSize, ySize);
		
		g.setColor(Color.GRAY);
		g.fillRect(xPos, yPos, margin, ySize);
		g.fillRect(xPos, yPos + ySize - margin, xSize, margin);
		g.fillRect(xPos + xSize - margin, yPos, margin, ySize);
	}
}
