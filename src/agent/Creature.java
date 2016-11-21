package agent;
import java.awt.Color;

import util.Random;
import environment.Room;
import graphics.GUI;


public class Creature extends AutonomousAgent
{	
	protected int goalDistance;
	protected int currentDistance;
	protected boolean takingDOT; // damage over time
	
	public Creature(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);
		color = Color.RED;
		movesEvery = 3;
	}
	
	public Creature(int x, int y, int width, int height) {
		super(x, y, width, height);
		color = Color.RED;
		movesEvery = 3;
	}
	
	public Creature(Room room, int x, int y, int width, int height, int sides) {
		super(room, x, y, width, height, sides);
		color = Color.RED;
		movesEvery = 3;
	}
	
	public Creature(int x, int y, int width, int height, int sides) {
		super(x, y, width, height, sides);
		color = Color.RED;
		movesEvery = 3;
	}
	
	/***************************************/
	// SETTING AND GETTING
	
	public void setRoom(Room room) {
		this.room = room;
		room.agents.add(this);
	}
	
	public void setDOT(boolean state) {
		takingDOT = state;
	}
	
	public void setSpeed(int speed) {
		movesEvery = speed;
	}
	
	public boolean isTakingDOT() {
		return takingDOT;
	}
	
	public boolean canMove() {
		return canMove;
	}
	
	public void freeze() {
		canMove = false;
	}
	
	public void unfreeze() {
		canMove = true;
	}
	
	/*******************************/
	// MOVEMENT METHODS

	public void newDirection() {
		
		movingUp = false; movingDown = false;
		movingRight = false; movingLeft = false;
		
		int random = (int) (Math.random() * 9);
		
		/*
		 * Each corresponds to a different direction: north-east-
		 * south-west, northeast, southwest, etc.
		 */
		switch (random) {
		case 0:
			movingUp = true;
			break;
		case 1:
			movingUp = true;
			movingRight = true;
			break;
		case 2:
			movingRight = true;
			break;
		case 3:
			movingRight = true;
			movingDown = true;
			break;
		case 4:
			movingDown = true;
			break;
		case 5:
			movingDown = true;
			movingLeft = true;
			break;
		case 6:
			movingLeft = true;
			break;
		case 7:
			movingLeft = true;
			movingUp = true;
			break;
		case 8:
			break;
		}
			
		goalDistance = (int) (Random.bounded(10, 30)); // 10 < goal < 30
		currentDistance = 0;
	}

	public void calculateMovements() {
		if (near(GUI.player)) {
			track(GUI.player);
			return;
		}
		else if (currentDistance >= goalDistance) {
			newDirection();
		}
		else {
			currentDistance++;
		}
	}

	/******************************************/
	// CORE METHODS
	
	public void collideIntoPlayer(Player collidee) {
		collidee.takeRecoil(this);
		collidee.takeDamage(1);
	}
	
	public void collideIntoPhantom(Phantom collidee) {
		collidee.takeRecoil(this);
	}
	
	public void collideIntoAgents(Entity collidee) {}	
}
