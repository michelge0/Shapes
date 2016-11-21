package agent;
import java.awt.*;
import java.awt.geom.Area;

import util.Math2;
import environment.Barrier;
import environment.Dungeon;
import environment.Room;
import graphics.GUI;
import animation.AnimationMovement;

public abstract class Entity extends Thing implements Speaks
{	
	protected Color color;
		
	public boolean movingRight, movingLeft, movingUp, movingDown;
	
	protected int movementCounter = 0;
	protected int movesEvery = 2; // moves every 2 milliseconds
		
	// COLLISION
	public boolean collidesWithPlayer, collidesWithPhantoms,
			collidesWithCreatures, collidesWithBarriers;
	
	protected boolean canMove = true;
	
	Room room;
	
	// DIALOGUE:
	public String currentDialogue = "placeholder";
	public int dialogueTime, dialogueCounter;
	// whether or not the dialogue is timed:
	public static final int INACTIVE = -1, ACTIVE = 0;
	
	public Entity(Room room, int x, int y, int width, int height) {
		xPos = x;
		yPos = y;
		
		this.room = room;
		room.agents.add(this);
		
		this.xSize = width;
		this.ySize = height;
		
		sides = 4;
	}
	
	public Entity(int x, int y, int width, int height) {
		xPos = x;
		yPos = y;
		
		this.xSize = width;
		this.ySize = height;
		
		sides = 4;
	}
		
	public Entity(Room room, int x, int y, int width, int height, int sides) {
		xPos = x;
		yPos = y;
		
		this.room = room;
		room.agents.add(this);
		
		this.xSize = width;
		this.ySize = height;
		
		this.sides = sides;
	}
	
	public Entity(int x, int y, int width, int height, int sides) {
		xPos = x;
		yPos = y;
		
		this.xSize = width;
		this.ySize = height;
		
		this.sides = sides;
	}
		
	/************************************/
	// SETTING AND GETTING
	
	public void takeDamage(int amount) {
		xSize -= amount;
		ySize -= amount;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(xPos, yPos, xSize, ySize);
	}
	
	public void stopMovements() {
		movingUp = false; movingDown = false;
		movingLeft = false; movingRight = false;
	}
	
	public void freeze() {
		stopMovements();
		canMove = false;
	}
	
	public void unfreeze() {
		canMove = true;
	}
	
	/*********************************/
	// COMBAT
	
	protected RecoilAnimation currentRecoil;
	
	private class RecoilAnimation extends AnimationMovement {
		public RecoilAnimation(Entity mover, int direction, int distance) {
			super(mover, direction, distance);
			if (currentRecoil == null) currentRecoil = this;
		}
		
		public RecoilAnimation(Entity mover, int direction, int distance, double movesPerSecond) {
			super(mover, direction, distance, movesPerSecond);
			if (currentRecoil == null) currentRecoil = this;
		}
		
		public void event() {
			if (currentRecoil != this) {
				return;
			}
			else super.event();
		}
		
		public void terminationEvent() {
			currentRecoil = null;
		}
	}

	public void takeRecoil(Entity that) {
		boolean up = that.movingUp;
		boolean down = that.movingDown;
		boolean right = that.movingRight;
		boolean left = that.movingLeft;
		
		if (up && right) new RecoilAnimation(this, 45, 40, .001).start();
		else if (up && left) new RecoilAnimation(this, 135, 40, .001).start();
		else if (down && right) new RecoilAnimation(this, -45, 40, .001).start();
		else if (down && left) new RecoilAnimation(this, -135, 40, .001).start();
		else if (up) new RecoilAnimation(this, RecoilAnimation.UP, 90, .001).start();
		else if (down) new RecoilAnimation(this, RecoilAnimation.DOWN, 90, .001).start();
		else if (right) new RecoilAnimation(this, RecoilAnimation.RIGHT, 90, .001).start();
		else if (left) new RecoilAnimation(this, RecoilAnimation.LEFT, 90, .001).start();
	}
	
	public void takeRecoil(Entity that, int amount) {
		boolean up = that.movingUp;
		boolean down = that.movingDown;
		boolean right = that.movingRight;
		boolean left = that.movingLeft;
		
		if (up && right) new RecoilAnimation(this, 45, amount, .001).start();
		else if (up && left) new RecoilAnimation(this, 135, amount, .001).start();
		else if (down && right) new RecoilAnimation(this, -45, amount, .001).start();
		else if (down && left) new RecoilAnimation(this, -135, amount, .001).start();
		else if (up) new RecoilAnimation(this, RecoilAnimation.UP, amount * 2, .001).start();
		else if (down) new RecoilAnimation(this, RecoilAnimation.DOWN, amount * 2, .001).start();
		else if (right) new RecoilAnimation(this, RecoilAnimation.RIGHT, amount * 2, .001).start();
		else if (left) new RecoilAnimation(this, RecoilAnimation.LEFT, amount * 2, .001).start();
	}
	
	public void takeRecoil(int x, int y) {
		int angle = (int) Math.toDegrees(Math.atan2((double) y, (double) x));
		new RecoilAnimation(this, angle, 45, .001).start();
	}
	
	public void takeRecoil(int x, int y, int amount) {
		int angle = (int) Math.toDegrees(Math.atan2((double) y, (double) x));
		new RecoilAnimation(this, angle, amount, .001).start();
	}
	
	/*********************************/
	// MOVEMENT
	
	public static void moveAgents() {
		for (int i = 0; i < Dungeon.location.getRoom(GUI.currentDungeon).agents.size(); i++) {
			Entity thisAgent = Dungeon.location.getRoom(GUI.currentDungeon).agents.get(i);
			thisAgent.move();
		}
	}
	
	public void moveHorizontally(int amount) {
		int currentXPos = xPos;
		
		xPos += amount;
		
		if (collideAny()) {
			xPos = currentXPos;
		}
	}
	
	public void moveVertically(int amount) {		
		int currentYPos = yPos;
		
		yPos += amount;
		
		if (collideAny()) {
			yPos = currentYPos;
		}
	}
	
	public void move() {
		if (!canMove) return;
		
		if (xSize < 3 || ySize < 3) die();
		
		if (movementCounter >= movesEvery - 1) {
			if (movingUp) moveVertically(-1);
			if (movingDown) moveVertically(1);
			if (movingLeft) moveHorizontally(-1);
			if (movingRight) moveHorizontally(1);
			
			movementCounter = 0;
		}
		else movementCounter++;
	}
	
	protected void die() {
		room.agents.remove(this);
	}
	
	/*************************************/
	// COLLISION
	
	public boolean collideAny() {
		if (collideBarriers() || collideAgents()
			|| collidePlayer() || collidePhantoms()) {
			return true;
		}
		else return false;
	}
	
	/**
	 * @collideBarriers There will always be a collisionEvent.
	 * If the barrier is open, it will also return true.
	 */
	
	public boolean collideBarriers() {
		if (!collidesWithBarriers) return false;
		Room thisRoom = Dungeon.location.getRoom(GUI.currentDungeon);
		
		boolean goingThroughGate = false;
		for (int i = 0; i < thisRoom.gates.size(); i++) {
			Barrier thisBarrier = thisRoom.gates.get(i);
			if (collision(this, thisBarrier)) {
				if (!thisBarrier.collisionEvent(this)) goingThroughGate = true;
			}
		}
		if (goingThroughGate) return false;
		
		boolean collision = false;
		for (int i = 0; i < thisRoom.walls.size(); i++) {
			Barrier thisBarrier = thisRoom.walls.get(i);
			if (collision(this, thisBarrier)) {
				if (thisBarrier.collisionEvent(this)) collision = true;
			}
		}
		if (collision) return true;
				
		for (int i = 0; i < thisRoom.backgrounds.size(); i++) {
			Barrier thisBarrier = thisRoom.backgrounds.get(i);
			if (collision(this, thisBarrier)) {
				if (thisBarrier.collisionEvent(this)) return true;
			}
		}
		return false;
	}
	
	public boolean collideAgents() {
		if (!collidesWithCreatures) return false;
		Room thisRoom = Dungeon.location.getRoom(GUI.currentDungeon);
		
		for (int i = 0; i < thisRoom.agents.size(); i++) {
			Entity thisCreature = thisRoom.agents.get(i);
			
			if (thisCreature.equals(Entity.this)) continue; // can't run into yourself
			if (collision(this, thisCreature)) {
				collideIntoAgents(thisCreature);
				thisCreature.collisionEvent(this);
				return true;
			}
		}
		return false;
	}
	
	public boolean collidePlayer() {
		if (!collidesWithPlayer) return false;
		
		if (collision(this, GUI.player)) {
			collideIntoPlayer(GUI.player);
			return true;
		}
		
		else return false;
	}
	
	public boolean collidePhantoms() {
		if (!collidesWithPhantoms) return false;
		if (GUI.player.phantom == null) return false;
		
		Phantom thisPhantom = GUI.player.phantom;
		if (collision(this, thisPhantom)) {
			collideIntoPhantom(thisPhantom);
			return true;
		}
		return false;
	}
	
	public boolean collision(Thing that) {
		// if one is a circle/one is a rect or both are rects
		if (this.sides == 4 && that.sides == 4
			|| this.sides == 4 && that.sides == 0
			|| this.sides == 0 && that.sides == 4) {
				return (Entity.this.getBounds().intersects(that.getBounds()));
			}
		// if two circles: intersects if distance between centers < sum of radii
		else if (this.sides == 0 && that.sides == 0) {
			return (Math2.distance(that.getXPos(), Entity.this.getXPos(),
				that.getYPos(), Entity.this.getYPos()) < that.getWidth() + Entity.this.getWidth());
		}
		else {
			Area areaA = new Area(this.shape);
			areaA.intersect(new Area(that.shape));
			return !areaA.isEmpty();
		}
	}

	/**********************************/
	// DIALOGUE
	// displayIcon is from the interface Speaks
	
	public void displayDialogue(Graphics g) {
		if (dialogueCounter <= dialogueTime) {
			displayIcon(g);
			g.setColor(oppositeColor(color));
			speakingHalo(g);
			
			GUI.text.drawText(g, currentDialogue);
		}
		
		else {
			currentDialogue = "";
			GUI.currentSpeaker = null;
			dialogueCounter = 0;
		}
		
		if (dialogueCounter == INACTIVE) return;
		
		// for some reason this is necessary to make
		// timeinSeconds measured actually in seconds:
		dialogueCounter += 2;
	}
	
	public void speak(String text) {
		if (text == "" || text == null) return;
		
		currentDialogue = text;
		
		GUI.currentSpeaker = this;

		dialogueCounter = INACTIVE;
	}
	
	public void speak(int timeInSeconds, String text) {
		speak(text);
		
		dialogueTime = 1000 * timeInSeconds;
		dialogueCounter = ACTIVE;
	}
	
	/**********************************/
	// UTILITIES
	
	public Color oppositeColor(Color color) {
		int red = 255 - color.getRed();
		int green = 255 - color.getGreen();
		int blue = 255 - color.getBlue();
		
		return new Color(red, green, blue);
	}
	
	/**********************************/
	// DRAWING / DISPLAY
	
	public void draw(Graphics g) {
		g.setColor(color);
		
		if (sides == 0) g.fillOval(xPos, yPos, xSize, ySize);
		else if (sides == 4) g.fillRect(xPos, yPos, xSize, ySize);
    }
	
	public void displayIcon(Graphics g) {
		g.setColor(color);
		int displayxSize = 50;

		if (sides == 0) {
			g.fillOval(ICON_CENTER_X - displayxSize / 2, ICON_CENTER_Y - displayxSize / 2, displayxSize, displayxSize);
		}
		
		else if (sides == 4) {
			g.fillRect(ICON_CENTER_X - displayxSize / 2, ICON_CENTER_Y - displayxSize / 2, displayxSize, displayxSize);
		}		
	}

	public void speakingHalo(Graphics g) {
		g.setColor(oppositeColor(color));
		
		if (sides == 0) {
			g.drawOval(xPos, yPos, xSize, ySize);
			g.drawOval(xPos - 1, yPos - 1, xSize + 2, ySize + 2);
		}
		else if (sides == 4) {
			g.drawRect(xPos, yPos, xSize, ySize);
			g.drawRect(xPos + 1, yPos + 1, xSize - 2, ySize - 2);
		}
    }
	
	/**********************************/
	// CORE METHODS (ABSTRACT)
	
	/*
	 * What happens when the agent collides into something?
	 */
	
	public abstract void collideIntoPlayer(Player collidee);
	
	public abstract void collideIntoPhantom(Phantom collidee);
	
	public abstract void collideIntoAgents(Entity collidee);
	
	public void collisionEvent(Entity collidee) {}
}
