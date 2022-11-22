package graphics;
import environment.Barrier;
import environment.Room;
import java.awt.*;
import javax.imageio.ImageIO;
import agent.Entity;
import agent.Creature;

import java.io.*;

import level.LevelThree;

public class Lantern extends Barrier
{
	Image lowestBrightness, mediumBrightness, highestBrightness;
	
	private static final int HIGH = 3, MEDIUM = 2, LOW = 1;
	private int state;
	
	private Light light;
	private boolean on;
	
	private int incrementCounter;
	private boolean canIncrement;
	
	public Lantern(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);
		
		try {
			highestBrightness = ImageIO.read(getClass().getResource("/resources/highest.png"));
			mediumBrightness = ImageIO.read(getClass().getResource("/resources/medium.png"));
			lowestBrightness = ImageIO.read(getClass().getResource("/resources/lowest.png"));
		} catch (IOException e) {System.out.println("IO Exception");}
		
		room.addToWalls(this);
		
		state = HIGH;
		turnOn();
	}
	
	/**************************************/
	// LIGHT
	
	private class Light extends BackgroundPainting {
		private int damageCounter;
		
		public Light(Room room, int x, int y, int width, int height, int layer) {
			super(room, x, y, width, height, layer);
			collidable = false;
			formID = "Background";
		}
		
		public void draw(Graphics g) {
			if (!on) return;
			switch (state) {
			case HIGH:
				g.drawImage(highestBrightness, xPos, yPos, GUI.graphics);
				break;
			case MEDIUM:
				g.drawImage(mediumBrightness, xPos, yPos, GUI.graphics);
				break;
			case LOW:
				g.drawImage(lowestBrightness, xPos, yPos, GUI.graphics);
				break;
			}
		}
		
		public boolean collisionEvent(final Entity collider) {
			String name = collider.getClass().getSimpleName();
			if ((name.equals("Zombie") || name.equals("LightHunter"))) {
				
				if (!LevelThree.cabin.agents.contains(collider)) return false;
				if (!on) return false;
				
				switch (state) {
				case HIGH:
					if (damageCounter > 100) {
						collider.takeDamage(1);
						damageCounter = 0;
					}
					break;
				case MEDIUM:
					if (damageCounter > 200) {
						collider.takeDamage(1);
						damageCounter = 0;
					}
					break;
				case LOW:
					if (damageCounter > 300) {
						collider.takeDamage(1);
						damageCounter = 0;
					}
					break;
				}
				damageCounter++;
			}
			return false;
		}
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
	public boolean isOn() {
		return on;
	}
	
	public void incrementDown() {
		if (state == LOW) turnOff();
		else state--;
		incrementCounter = 250;
	}
	
	public void incrementUp() {
		if (state == HIGH) return;
		else if (!on) turnOn();
		else state++;
		incrementCounter = 250;
	}
	
	public void turnOff() {
		room.barriers.remove(light);
		light = null;
		on = false;
	}
	
	// image is 250 x 250, so -125 centers it
	public void turnOn() {
		light = new Light(room, xPos - 124, yPos - 124, 250, 250, Barrier.BACKGROUND);
		on = true;
	}
	
	/***********************************/
	// CORE METHODS
	
	public void draw(Graphics g) {		
		g.setColor(new Color(255, 255, 75, 175));
		g.fillRect(xPos, yPos, xSize, ySize);
		
		if (incrementCounter != 0) incrementCounter--;
		
		if (incrementCounter == 0) canIncrement = true;
		else canIncrement = false;
	}

	public boolean collisionEvent(Entity collider) {
		String type = collider.getClass().getSimpleName();
		
		if (canIncrement) {
			if (type.equals("Player")) {
				incrementUp();
			}
			
			else if (Creature.class.isAssignableFrom(collider.getClass())) {
				if (type.equals("GhostRunner") || type.equals("CharacterCircle")) return true;
				if (collider == LevelThree.seth) return true;
				incrementDown();
			}
		}
		return true;
	}
}
