package environment;
import graphics.BackgroundPainting;
import graphics.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import util.Random;
import level.LevelTwo;
import agent.Animal;
import agent.Creature;
import agent.Assassin;
import animation.Animation;
import animation.Sound;

public class RoomForest extends RoomDungeon
{
	public ArrayList<Grove> groves = new ArrayList<Grove>();

	protected Grove topGrove, bottomGrove, leftGrove, rightGrove, centerGrove;
	protected int deepness; // how deep into the forest
	protected int count;
	
	public RoomForest(Dungeon dungeon, int x, int y) {
		super(dungeon, x, y);
		hasEntranceEvent = true;
	}
	
	/***********************************/
	// SPAWNING
	
	public void setSpawn(int direction) {
		Gate entrance;

		switch (direction) {		
		case UP:
			entrance = topWall.entranceGate;
			spawnLocation.setX(entrance.getXPos() + entrance.getWidth() / 2);
			spawnLocation.setY(entrance.getYPos() + 100);
			break;
		case DOWN:
			entrance = bottomWall.entranceGate;
			spawnLocation.setX(entrance.getXPos() + entrance.getWidth() / 2);
			spawnLocation.setY(entrance.getYPos() - 100);
			break;
		case LEFT:
			entrance = leftWall.entranceGate;
			spawnLocation.setX(entrance.getXPos() + 100);
			spawnLocation.setY(entrance.getYPos() + entrance.getHeight() / 2);
			break;
		case RIGHT:
			entrance = rightWall.entranceGate;
			spawnLocation.setX(entrance.getXPos() - 100);
			spawnLocation.setY(entrance.getYPos() + entrance.getHeight() / 2);
			break;
		}
		
		clearSpawnArea();
		spawnCreatures();
	}
	
	private void clearSpawnArea() {
		for (int i = 0; i < groves.size(); i++) {
			groves.get(i).clearArea(spawnLocation.getX() - 25, spawnLocation.getY() - 25, 50, 50);
		}
	}
	
	/***************************************/
	// CREATURES
	
	public void spawnCreatures() {
		int numberCreatures = (int) (Math.random() * deepness / 2) + (deepness / 2);
		calculateSpawnedCreatureDimensions(numberCreatures);
	}
	
	public void calculateSpawnedCreatureDimensions(int numberCreatures) {
		if (this.agents.size() > 0) numberCreatures = numberCreatures / 3;
		
		for (int i = 0; i < numberCreatures; i++) {
			int randomX = (int) (Math.random() * GUI.graphics.getWidth());
			int randomY = (int) (Math.random() * (GUI.graphics.getHeight() - 100));
			int randomWidth = (int) (5 + Math.random() * 40);
			int randomHeight = (int) (5 + Math.random() * 40);
			
			Creature potentialCreature;
			
			if (deepness >= 7) {
				potentialCreature = new Assassin(this, randomX, randomY, randomWidth / 2, randomHeight / 2);
			}
			
			else {
				potentialCreature = new Animal(this, randomX, randomY, randomWidth, randomHeight);
			}
			
			// can't spawn on top of an existing thing:
			if (potentialCreature.collideAny() || potentialCreature.near(GUI.player, 100)) {
				agents.remove(potentialCreature);
				potentialCreature = null;
				i--;
				continue;
			}	
		}
	}
	
	/***************************************/
	// ROOM CREATION
	
	public void createWalls(int width, int height) {
		// if walls already created: only needs to check top wall
		if (topWall != null) return;
		
		// Define four walls:
		topWall = new Wall(this, 0, -10, width, 10);
		leftWall = new Wall(this, -10, 0, 10, height);
		rightWall = new Wall(this, width, 0, 10, height);
		bottomWall = new Wall(this, 0, height, width, 10);

		int edgeFactor = deepness * 3;
		 
		topGrove = new Grove(this, 0, 0, width, 100, 35 + deepness, 35 - edgeFactor);
		leftGrove = new Grove(this, 0, 0, 100, height, 35 + deepness, 35 - edgeFactor);
		rightGrove = new Grove(this, width - 75, 0, 100, height, 35 + deepness, 35 - edgeFactor);
		bottomGrove = new Grove(this, 0, height - 75, width, 100, 35 + deepness, 35 - edgeFactor);
		
		int middleFactor = deepness * 5;
		
		centerGrove = new Grove(this, 100, 100, 300, 300, 30 + middleFactor, 70 - middleFactor);
		
		groves.add(topGrove);
		groves.add(bottomGrove);
		groves.add(leftGrove);
		groves.add(rightGrove);
		groves.add(centerGrove);
		
		
		/* 
		 * 50 + Math.random() * (width OR height - 100) is an algorithm
		 * that ensures that the door will not be within 50 or so pixels of
		 * either wall.
		 */
				
		if (dungeon.isLocationConnected(RoomDungeon.UP)) {
			int opening = (int) (Random.bounded(100, width - 100));
			topGrove.createHorizontalOpening(opening - 30, 80);
			topWall.setHorizontalEntrance(opening - 60, 110, UP);
		}
		
		if (dungeon.isLocationConnected(RoomDungeon.DOWN)) {
			int opening = (int) (Random.bounded(100, width - 100));
			bottomGrove.createHorizontalOpening(opening - 30, 80);
			bottomWall.setHorizontalEntrance(opening - 60, 110, DOWN);
		}
		
		if (dungeon.isLocationConnected(RoomDungeon.RIGHT)) {
			int opening = (int) (Random.bounded(100, width - 100));
			rightGrove.createVerticalOpening(opening - 30, 80);
			rightWall.setVerticalEntrance(opening - 60, 110, RIGHT);
		}
		
		if (dungeon.isLocationConnected(RoomDungeon.LEFT)) {
			int opening = (int) (Random.bounded(100, width - 100));
			leftGrove.createVerticalOpening(opening - 20, 60);
			leftWall.setVerticalEntrance(opening - 50, 90, LEFT);
		}
		
		if (deepness >= 6) {
			Sound.FOREST_MEDIUM.stop();
			Sound.FOREST_DEEP.loop();
		}
		else if (deepness >= 4) {
			Sound.FOREST_SHALLOW.stop();
			Sound.FOREST_DEEP.stop();
			Sound.FOREST_MEDIUM.play();
		}
		else {
			Sound.FOREST_DEEP.stop();
			Sound.FOREST_MEDIUM.stop();
			Sound.FOREST_SHALLOW.play();
		}
	}
	
	/*****************************************/
	// UTILITIES
	
	public void darkenAll(int amount) {		
		for (int h = 0; h < groves.size(); h++) {
			groves.get(h).resetColor();
			groves.get(h).darken(amount);
		}
	}
	
	public void entranceEvent() {
		darkenAll(deepness - 1);
		int amount = 10 * (deepness - 1);
		GUI.graphics.setBackground(new Color(150 - amount, 125 - amount, 75 - amount));
	
		if (deepness == 3) {
			GUI.player.speak(5, "The deeper I go, the darker it gets . . .");
		}
		
		// a hint after 20 room movements:
		if (count >= 15) {
			GUI.player.speak(10, "I might have a better chance of finding him deeper in the woods.");
			count = 0;
		}
		
		if (deepness == 8) {
			for (int i = 0; i < groves.size(); i++) {
				groves.get(i).clearArea(400, 0, 100, 100);
			}
			
			for (int i = 0; i < 100; i += 2) {
				new BackgroundPainting(this, 500, 0, 200 - 2*i, 
						new Color(80 + i, 55 + i, 5 + i), new Color(80 + i, 55 + i, 5 + i),
						0, 0) {
					public void draw(Graphics g) {
						g.setColor(strokeColor);
						g.fillOval(xPos - xSize / 2, yPos - ySize / 2, xSize, xSize);
					}
				};
			}
			
			new Zone(this, 450, 0, 50, 50).setEvent(new Animation(.01) {
				public void event() {
					LevelTwo.nextLevel();
					stop();
				}
			});
		}
		
		count++;
	}
}
