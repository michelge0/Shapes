package agent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.*;

import animation.Sound;
import util.Random;
import level.Level;
import level.LevelThree;
import environment.Dungeon;
import graphics.GUI;

// NB: There is only one player-controlled agent, and its values
// are stored here.

public class Player extends Entity implements KeyListener
{	
	protected int diameter;	// marks health
	protected boolean growing;
	protected boolean shrinking;
	protected int growthGoal;

	public Phantom phantom;
	
	public boolean sethMoving;
	public boolean phantomMoving;
	
	public boolean canSummon = true;
	public static boolean attackMode;
	
	private static String helpText;
	
	public Player(int x, int y, int radius)
	{
		super(x, y, radius, radius);
		
		this.diameter = radius;
		color = Color.GREEN;
		
		collidesWithPlayer = false;
		collidesWithPhantoms = true;
		collidesWithCreatures = true;
		collidesWithBarriers = true;
		
		sides = 0;
		
		movesEvery = 2;
		canMove = true;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(xPos - diameter / 2, yPos - diameter / 2, diameter, diameter);
	}
	
	/*********************************/
	// SETTING AND GETTING:
	
	public int getWidth() {
		return diameter;
	}
	
	public int getHeight() {
		return diameter;
	}
	
	public int getXCenter() {
		return xPos;
	}
	
	public int getYCenter() {
		return yPos;
	}
	
	public void incrementGrowth(int amount) {
		growthGoal = diameter + amount;
		if (amount > 0) growing = true;
		else if (amount < 0 ) shrinking = true;
	}
	
	public void grow() {
		if (diameter == growthGoal) {
			growing = false;
			shrinking = false;
		}
		
		if (growing) diameter += 1;
		else if (shrinking) diameter -= 1;
		
		// will only collide if it's growing bigger
		if (collideAny()) {
			diameter -= 1;
			growthGoal = diameter;
		}
	}
	
	public void setRadius(int amount) {
		diameter = amount;
	}
	
	public void takeDamage(int amount) {
		incrementGrowth(-amount);
	}
	
	public void removePhantom() {
		phantom = null;
		phantomMoving = false;
	}
	
	public static void setHelpText(String text) {
		helpText = text;
	}
	
	/************************************/
	// MOVEMENT METHODS
	
	public void setCenter() {
		xPos = (GUI.graphics.getWidth() / 2) - 10;
		yPos = (GUI.graphics.getHeight() / 2) - 10;
	}

	public void move() {	
		if (phantom != null) {
			phantom.move();
		}
		super.move();
		if (diameter <= 3) die();
	}
	
	protected void die() {
		GUI.gameInProgress = false;
		Level.gameOver = true;
		GUI.currentSpeaker = null;
		GUI.graphics.removeKeyListener(this);
	}
	
	/***************************************/
	// CONTROLLER METHODS
	
	public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();  // Which key was pressed?
		        
        if (canMove) {		// if player can move
			if (code == KeyEvent.VK_LEFT) movingLeft = true;
			else if (code == KeyEvent.VK_RIGHT) movingRight = true;
			else if (code == KeyEvent.VK_UP) movingUp = true;
			else if (code == KeyEvent.VK_DOWN) movingDown = true;
        }
        
        if (phantomMoving && phantom != null) {
			if (code == KeyEvent.VK_A) phantom.movingLeft = true;
			else if (code == KeyEvent.VK_D) phantom.movingRight = true;
			else if (code == KeyEvent.VK_W) phantom.movingUp = true;
			else if (code == KeyEvent.VK_S) phantom.movingDown = true;
		}
        
        if (sethMoving && LevelThree.seth != null) {
        	if (code == KeyEvent.VK_A) LevelThree.seth.movingLeft = true;
			else if (code == KeyEvent.VK_D) LevelThree.seth.movingRight = true;
			else if (code == KeyEvent.VK_W) LevelThree.seth.movingUp = true;
			else if (code == KeyEvent.VK_S) LevelThree.seth.movingDown = true;
        }
		
		if (code == KeyEvent.VK_SPACE) {			
			if (!canSummon) return;
			
			// if no phantom yet
			if (!phantomMoving) {
				summonPhantom();
				phantomMoving = true;
				Sound.SUMMON.play();
			}
			// if there is a phantom
			else {
				phantom = null;
				phantomMoving = false;
				Sound.DISMISS.play();
			}
		}
		
		else if (code == KeyEvent.VK_H) {
			speak(3, helpText);
		}
    }
	
	public void summonPhantom() {
		int random1 = (int) Random.bounded(diameter, 40);
		int random2 = (int) Random.bounded(diameter, 40);
		if (Math.random() > .5) random1 *= -1;
		if (Math.random() > .5) random2 *= -1;
		
		phantom = new Phantom(this.xPos + random1, this.yPos + random2, this.diameter * 3 / 5, this);
		
		if (phantom.collideBarriers() || phantom.collideAgents()) {
			phantom = null;
			summonPhantom();
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (canMove) {
			if (code == KeyEvent.VK_LEFT) movingLeft = false;
			else if (code == KeyEvent.VK_RIGHT) movingRight = false;
			else if (code == KeyEvent.VK_UP) movingUp = false;
			else if (code == KeyEvent.VK_DOWN) movingDown = false;
		}
		
		if (phantomMoving && phantom != null && canSummon) {
			if (code == KeyEvent.VK_A) phantom.movingLeft = false;
			else if (code == KeyEvent.VK_D) phantom.movingRight = false;
			else if (code == KeyEvent.VK_W) phantom.movingUp = false;
			else if (code == KeyEvent.VK_S) phantom.movingDown = false;
		}
		
		if (sethMoving && LevelThree.seth != null) {
        	if (code == KeyEvent.VK_A) LevelThree.seth.movingLeft = false;
			else if (code == KeyEvent.VK_D) LevelThree.seth.movingRight = false;
			else if (code == KeyEvent.VK_W) LevelThree.seth.movingUp = false;
			else if (code == KeyEvent.VK_S) LevelThree.seth.movingDown = false;
        }
	}
	
	public void keyTyped(KeyEvent e) {}
	
	/****************************************/
	// COLLISION METHODS
	
	public void collideIntoPlayer(Player collidee) {}
	
	public void collideIntoAgents(Entity collidee) {
		if (attackMode) {
			collidee.takeRecoil(this);
		}
	}
	
	public void collideIntoPhantom(Phantom collidee) {}
	
	/****************************************/
	// CORE METHODS
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(this.xPos - diameter / 2, this.yPos - diameter / 2, this.diameter, this.diameter);
		
		if (phantom != null) phantom.draw(g);
	}
	
	public void displayIcon(Graphics g) {
		g.setColor(color);
		
		int displayRadius = 50;
		g.fillOval(ICON_CENTER_X - displayRadius / 2, ICON_CENTER_Y - displayRadius / 2, displayRadius, displayRadius);
	}
	
	public void speakingHalo(Graphics g) {		
		g.drawOval(xPos - diameter / 2, yPos - diameter / 2, diameter, diameter);
		g.drawOval(xPos - diameter / 2 + 1, yPos - diameter / 2 + 1, diameter - 2, diameter - 2);
	}
}
