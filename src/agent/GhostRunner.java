package agent;

import environment.Coordinate;
import graphics.GUI;
import java.awt.Color;
import level.LevelThree;

public class GhostRunner extends Creature
{
	public boolean caughtByPlayer;
	public boolean caughtBySeth;
	
	public boolean attackMode;
	private boolean relocating;
	
	// width and height of screen:
	private static final int WIDTH = 500, HEIGHT = 465,
			CENTERX = 250, CENTERY = 232;
		
	// stores goals as Coordinates
	private Coordinate<Integer> goal = new Coordinate<Integer>(0, 0);
		
	public GhostRunner(int x, int y, int radius) {
		super(x, y, radius, radius, 0);
		this.xSize = radius;
		movesEvery = 2;
		color = Color.BLUE;
	}
	
	/**************************************/
	// MASTER MOVEMENT METHODS
	
	public void calculateMovements() {
		movingUp = false; movingDown = false;
		movingLeft = false; movingRight = false;
	
		if (!attackMode) {
			if (caughtByPlayer) return;
			
			findHidingPlace(GUI.player, LevelThree.seth);
			
			//if (near(GUI.player, 45) && near(LevelThree.seth)) findHidingPlace(GUI.player, LevelThree.seth, xPos - 50, yPos - 50);
			//if (near(GUI.player, 45)) findHidingPlace(GUI.player, null, xPos - 50, yPos - 50);
			//if (near(LevelThree.seth, 45)) findHidingPlace(LevelThree.seth, null, xPos - 50, yPos - 50);
			
			track(goal.getX(), goal.getY());
			System.out.println(goal.getX() + " " + goal.getY());
		}
		
		else {
			if (caughtBySeth) return;
			
			if (relocating) {
				track(goal.getX(), goal.getY());
				if (near(goal.getX(), goal.getY(), 30))
					relocating = false;
				return;
			}
			
			if (near(GUI.player)) LevelThree.seth.speak("Stay away from him!");
			track(GUI.player);
			
			if (near(LevelThree.seth, 100)) {
				LevelThree.seth.speak(1, "Ryan, stay away! I'll take care of this myself.");
				findHidingPlace(GUI.player, LevelThree.seth);
				relocating = true;
			}
		}
	}
	
	/***************************************/
	// PATHFINDING
	
	private void findHidingPlace(Entity bully1, Entity bully2) {
		Coordinate<Integer> bestSpot = new Coordinate<Integer>(0, 0);
		int bestSpotDistance = 0;
		
		for (int i = 50; i <= WIDTH - 50; i += WIDTH - 100) {
			for (int j = 50; j <= HEIGHT - 50; j += HEIGHT - 100) {
				int distance = distance(i, j, bully1);
				if (bully2 != null) distance += distance(i, j, bully2);
				if (distance > bestSpotDistance) {
					bestSpotDistance = distance;
					bestSpot.setX(i); bestSpot.setY(j);
				}
			}
		}
		goal = bestSpot;		
	}
	
	// checks best hiding spot within 100 x 100 square, upper left corner at xPos, yPos
	
	private void findHidingPlace(Entity bully1, Entity bully2, int xPos, int yPos) {
		Coordinate<Integer> bestSpot = new Coordinate<Integer>(0, 0);
		int bestSpotDistance = 0;
		
		for (int i = xPos; i <= xPos + 100; i += 100) {
			for (int j = yPos; j <= yPos + 100; yPos += 100) {
				int distance = distance(i, j, bully1);
				if (bully2 != null) distance += distance(i, j, bully2);
				if (distance > bestSpotDistance) {
					bestSpotDistance = distance;
					bestSpot.setX(i); bestSpot.setY(j);
				}
			}
		}
		goal = bestSpot;
	}
		
	/***************************************/
	// REVISED MOVEMENT UTILITIES
	
	public void runAwayFrom(Entity bully) {
		if (bully == null) return;
		
		movingUp = false; movingDown = false;
		movingLeft = false; movingRight = false;
		
		if (bully.getXPos() < this.getXCenter()) movingRight = true;
		else movingLeft = true;
		
		if (bully.getYPos() < this.getYCenter()) movingDown = true;
		else movingUp = true;
	}

	
	/*****************************************/
	// MATHEMATICAL UTILITIES
	
	// distance formula:
	private int distance(int x, int y, Entity bully) {
		return (int) Math.pow(Math.pow(bully.getXPos() - x, 2)
				+ Math.pow(bully.getYPos() - y, 2), .5);
	}
	
	/*****************************************/
	// CORE METHODS
	
	public void collisionEvent(Entity collidee) {
		if (collidee.getClass().getSimpleName().equals("Player")) {
			caughtByPlayer = true;
			LevelThree.seth.movingUp = false; LevelThree.seth.movingDown = false;
			LevelThree.seth.movingLeft = false; LevelThree.seth.movingRight = false;
		}
		else {
			caughtBySeth = true;
			LevelThree.seth.movingUp = false; LevelThree.seth.movingDown = false;
			LevelThree.seth.movingLeft = false; LevelThree.seth.movingRight = false;
		}
	}
	
	public void collideIntoPlayer(Player collidee) {
		collidee.takeRecoil(this);
		if (attackMode) {
			if (Math.random() > .5) collidee.speak(1, "Oww! Stop!");
			else collidee.speak(1, "Okay, okay! I'll let you go!");
			collidee.takeDamage(1);
		}
		else speak(1, "Get away from me!");
	}
	
	public void collideIntoAgents(Creature collidee) {
		collidee.takeRecoil(this);
		if (!attackMode) {
			if (Math.random() > .5) collidee.speak(1, "Oww! He kicked me!");
			else if (Math.random() > .5) collidee.speak(1, "Ack! He hit me!");
			else collidee.speak(1, "Ah! He bit me!");
		}		
	}
}
