package agent;
import java.awt.*;

import environment.Dungeon;
import environment.Room;
import graphics.GUI;
import animation.AnimationMovement;

public class CreatureStudent extends Creature
{	
	public static final int LANE_ONE = 85;
	public static final int LANE_TWO = 172;
	public static final int LANE_THREE = 260;
	public static final int LANE_FOUR = 347;
	
	private int turningPoint;
	private boolean choosingLane;
	private boolean exiting;
	private int lane;
	
	public CreatureStudent(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);
		
		color = Color.RED;
		
		movingUp = false; movingDown = false;
		movingLeft = false; movingRight = false;
		
		turningPoint = 410;
		
		// find lane x coordinate:
		switch((int) (Math.random() * 4) + 1) { // random number 1-4
		case 1: lane = LANE_ONE; break;
		case 2: lane = LANE_TWO; break;
		case 3: lane = LANE_THREE; break;
		case 4: lane = LANE_FOUR; break;
		default: lane = LANE_TWO;
		}
		choosingLane = true;
	}
	
	/*************************************/
	// MOVEMENT
	
	public void calculateMovements() {
		if (exiting) return;
		
		if (choosingLane) moveToLane();	
		
		if (yPos >= turningPoint) {
			exit();
			exiting = true;
		}
	}
	
	public void moveToLane() {
		if (xPos < lane) {
			new AnimationMovement(this, AnimationMovement.RIGHT, lane - xPos){
				public void terminationEvent() {
					movingDown = true;
				}
			}.start();
		}
		else new AnimationMovement(this, AnimationMovement.LEFT, xPos - lane){
			public void terminationEvent() {
				movingDown = true;
			}
		}.start();
		
		choosingLane = false;
	}
	
	public void exit() {
		if (GUI.currentSpeaker == this)
			GUI.currentSpeaker = null;
		
		if (xPos < 250) {
			System.out.println(250 - xPos);
			new AnimationMovement(this, AnimationMovement.RIGHT, 250 - xPos) {
				public void terminationEvent() {
					Dungeon.location.getRoom(GUI.currentDungeon).agents.remove(CreatureStudent.this);
					System.out.println("end");
				}
			}.start();
		}
		else {
			new AnimationMovement(this, AnimationMovement.LEFT, xPos - 250) {
				public void terminationEvent() {
					Dungeon.location.getRoom(GUI.currentDungeon).remove(CreatureStudent.this);
				}
			}.start();
		}
	}
	
	/*************************************/
	// CORE METHODS
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(xPos, yPos, xSize, ySize);
	}
	
	public void collideIntoPlayer(Player collidee) {
		collidee.takeRecoil(this);

		if (Math.random() > .85) speak("Watch where you're going!");
		else if (Math.random() > .75) speak("Move over, bud.");
		else if (Math.random() > .55) speak("Excuse me! Excuse me!");
		else speak("Hey! Watch it!");
		
		collidee.takeDamage(1);		
	}

	public void displayIcon(Graphics g) {
		g.setColor(color);
		
		int displayRadius = 50;
		g.fillRect(ICON_CENTER_X - displayRadius / 2, ICON_CENTER_Y - displayRadius / 2, displayRadius, displayRadius);
	}
	
	public void speakingHalo(Graphics g) {		
		g.drawRect(xPos, yPos, xSize, ySize);
		g.drawRect(xPos + 1, yPos + 1, xSize - 2, ySize - 2);
	}
}
