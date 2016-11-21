package level;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import util.Random;
import environment.Gate;
import environment.RoomDungeon;
import environment.Wall;
import environment.ContinuousGate;
import environment.Dungeon;
import environment.Grove;
import environment.Room;
import environment.Zone;
import graphics.BackgroundPainting;
import graphics.Fireplace;
import graphics.GUI;
import graphics.Lantern;
import agent.Entity;
import agent.Creature;
import agent.GhostRunner;
import agent.Player;
import animation.Animation;
import animation.AnimationFlicker;
import animation.AnimationMovement;
import animation.AnimationSequence;
import animation.Sound;
import animation.Speech;
import environment.CabinDoor;

public class LevelThree extends Level
{
	Room upperRight, right, bottomRight, bottom,
		bottomLeft, left, upperLeft, top;
	public static Room cabin, interior;
	
	Grove urGrove, brGrove, ulGrove, blGrove,
		tGrove1, tGrove2, bGrove1, bGrove2, lGrove1, lGrove2, rGrove1, rGrove2;
	
	ContinuousGate cabinTop, cabinBottom, cabinLeft, cabinRight;
	CabinDoor doorToCabin;
	Zone cabinStairs;
	
	Lantern lanternUR, lanternUL, lanternBR, lanternBL;
	
	Creature circleOne, circleTwo, circleThree, circleFour, circleFive, circleSix,
						circleSeven, circleEight, circleNine, circleTen, circleEleven;
	
	// nine is a method, not an AnimationSequence
	AnimationSequence sequenceOne, sequenceTwo, sequenceThree,
		sequenceFour, sequenceFive, sequenceSix, sequenceSeven,
		sequenceEight, sequenceEleven, sequenceTwelve;
	
	Animation lightning;
	
	Fireplace fire;

	GhostRunner doug;
	GhostSeth sethIdeal;
		
	// public and static to interact with ghostrunner
	public static Creature seth;
	
	public LevelThree() {
		initializeDungeon(3, 4, "Custom");
	}
	
	public void introAnimation() {		
		Player.setHelpText("");
		
		cabinTop.close(); cabinBottom.close();
		cabinRight.close(); cabinLeft.close();
		doorToCabin.close();
		
		GUI.player.canSummon = false;
		Player.attackMode = false;
		
		cabinStairs.setEvent(new Animation(.01) {
			public void event() {
				seth.setXPos(250 + (GUI.player.getXPos() - 250) / 2);
				cabin.add(seth);
				seth.speak(3, "Get off my lawn! OFF!");
				
				new AnimationMovement(seth, AnimationMovement.DOWN, 50, .002).start();
				new AnimationMovement(GUI.player, AnimationMovement.DOWN, 80, .004).start();
				
				Animation.enterDialogueMode(sequenceTwo);

				cabinStairs.setEvent(null);
				stop();				
			}
		});
	
		Animation.enterDialogueMode(sequenceOne);
	}

	/*******************************************/
	// ARCHITECTURE
	
	public void createRooms() {	
		
		// Rooms:
		
		upperRight = dungeon.rooms.get(2).get(2);
		right = dungeon.rooms.get(2).get(1);
		bottomRight = dungeon.rooms.get(2).get(0);
		bottom = dungeon.rooms.get(1).get(0);
		bottomLeft = dungeon.rooms.get(0).get(0);
		left = dungeon.rooms.get(0).get(1);
		upperLeft = dungeon.rooms.get(0).get(2);
		
		cabin = dungeon.rooms.get(1).get(1);
		top = dungeon.rooms.get(1).get(2);
		interior = dungeon.rooms.get(1).get(3);
		
		Dungeon.setLocation(cabin);
		
		GUI.graphics.setBackground(new Color(80, 75, 25));
		Dungeon.spawn(50, 450);
				
		// Characters:
		
		seth = new Creature(225, 285, 20, 20, 0) {
			public void calculateMovements() {}
			public void collideIntoPlayer(Player collidee) {}
		};
		seth.setColor(Color.YELLOW);
		seth.freeze();
						
		sethIdeal = new GhostSeth(460, 430, 20);
				
		doug = new GhostRunner(270, 450, 10);
		doug.freeze();
		doug.setColor(Color.BLUE);
		
		circleOne = new Creature(interior, -25, 130, 20, 20, 0);
		circleTwo = new Creature(interior, -25, 220, 20, 20, 0);
		circleThree = new Creature(interior, -25, 310, 20, 20, 0);
		circleFour = new Creature(interior, 525, 130, 20, 20, 0);
		circleFive = new Creature(interior, 525, 220, 20, 20, 0);
		circleSix = new Creature(interior, 525, 310, 20, 20, 0);
		circleSeven = new Creature(interior, 130, -25, 20, 20, 0);
		circleEight = new Creature(interior, 220, -25, 20, 20, 0);
		circleNine = new Creature(interior, 130, 525, 20, 20, 0);
		circleTen = new Creature(interior, 220, 525, 20, 20, 0);
		circleEleven = new Creature(interior, 310, 525, 20, 20, 0);
		
		circleOne.freeze(); circleTwo.freeze(); circleThree.freeze();
		circleFour.freeze(); circleFive.freeze(); circleSix.freeze();
		circleSeven.freeze(); circleEight.freeze(); circleNine.freeze();
		circleTen.freeze(); circleEleven.freeze();
		
		circleOne.setColor(new Color(Random.roll(100), Random.roll(100), Random.roll(100)));
		circleTwo.setColor(new Color(Random.roll(100), Random.roll(100), Random.roll(100)));
		circleThree.setColor(new Color(Random.roll(100), Random.roll(100), Random.roll(100)));
		circleFour.setColor(new Color(Random.roll(100), Random.roll(100), Random.roll(100)));
		circleFive.setColor(new Color(Random.roll(100), Random.roll(100), Random.roll(100)));
		circleSix.setColor(new Color(Random.roll(100), Random.roll(100), Random.roll(100)));
		circleSeven.setColor(new Color(Random.roll(100), Random.roll(100), Random.roll(100)));
		circleEight.setColor(new Color(Random.roll(100), Random.roll(100), Random.roll(100)));
		circleNine.setColor(new Color(Random.roll(100), Random.roll(100), Random.roll(100)));
		circleTen.setColor(new Color(Random.roll(100), Random.roll(100), Random.roll(100)));
		circleEleven.setColor(new Color(Random.roll(100), Random.roll(100), Random.roll(100)));

		circleOne.collidesWithBarriers = false;
		circleTwo.collidesWithBarriers = false;
		circleThree.collidesWithBarriers = false;
		circleFour.collidesWithBarriers = false;
		circleFive.collidesWithBarriers = false;
		circleSix.collidesWithBarriers = false;
		circleSeven.collidesWithBarriers = false;
		circleEight.collidesWithBarriers = false;
		circleNine.collidesWithBarriers = false;
		circleTen.collidesWithBarriers = false;
		circleEleven.collidesWithBarriers = false;

		
		int width = GUI.graphics.getWidth();
		int height = GUI.graphics.getHeight() - 100;
	
	/***********************************/
	// FOREST AREAS
		
	/******************
	 * UPPER RIGHT
	 */
		
		new Wall(upperRight, -10, 0, 10, height);
		new Wall(upperRight, width, 0, 10, height);
		new Wall(upperRight, 0, height, width, 10);
		new Wall(upperRight, 0, -10, width, 10);
		
		new ContinuousGate(upperRight, 0, height - 1, 50, 10, RoomDungeon.DOWN);
		new ContinuousGate(upperRight, -9, height - 50, 10, 50, RoomDungeon.LEFT);
		
		urGrove = new Grove(upperRight, 0, 0, width + 30, height + 30, 50, 10);
		urGrove.clearArea(0, height - 100, 100, 100);
		
	/******************
	 * RIGHT
	 */
		new Wall(right, width, 0, 10, height);
		new Wall(right, 0, height, width, 10);
		new Wall(right, 0, -10, width, 10);
		
		new ContinuousGate(right, -10, 0, 10, height, RoomDungeon.LEFT);
		new ContinuousGate(right, 0, -9, 100, 10, RoomDungeon.UP);
		new ContinuousGate(right, 0, height - 1, 100, 10, RoomDungeon.DOWN);
		
		rGrove1 = new Grove(right, width / 6, -10, 2 * width / 3, height + 30, 40, 25);
		rGrove2 = new Grove(right, 2 * width / 3, -10, width / 2, height + 30, 50, 10);

	/******************
	 * BOTTOM RIGHT
	 */
		new Wall(bottomRight, -10, 0, 10, height);
		new Wall(bottomRight, width, 0, 10, height);
		new Wall(bottomRight, 0, height, width, 10);
		new Wall(bottomRight, 0, -10, width, 10);
		
		new ContinuousGate(bottomRight, 0, -9, 50, 10, RoomDungeon.UP);
		new ContinuousGate(bottomRight, -9, 0, 10, 50, RoomDungeon.LEFT);
		
		brGrove = new Grove(bottomRight, 0, 0, width + 30, height + 30, 50, 10);
		brGrove.clearArea(0, 0, 100, 100);
		
	/******************
	 * BOTTOM
	 */
		// surrounding walls:
		new Wall(bottom, -10, 0, 10, height);
		new Wall(bottom, width, 0, 10, height);
		new Wall(bottom, 0, height, width, 10);
		
		// exits:
		new ContinuousGate(bottom, 0, -10, width, 10, RoomDungeon.UP);
		new ContinuousGate(bottom, width - 1, 0, 10, 50, RoomDungeon.RIGHT);
		new ContinuousGate(bottom, -9, 0, 10, 50, RoomDungeon.LEFT);
		
		bGrove1 = new Grove(bottom, -10, height / 6, width + 30, 2 * height / 3, 40, 25);
		bGrove2 = new Grove(bottom, -10, 2 * height / 3, width + 30, height / 2, 50, 10);
	
	/******************
	 * BOTTOM LEFT
	 */
		new Wall(bottomLeft, -10, 0, 10, height);
		new Wall(bottomLeft, width, 0, 10, height);
		new Wall(bottomLeft, 0, height, width, 10);
		new Wall(bottomLeft, 0, -10, width, 10);
		
		new ContinuousGate(bottomLeft, width - 50, -9, 50, 10, RoomDungeon.UP);
		new ContinuousGate(bottomLeft, width - 1, 0, 10, 50, RoomDungeon.RIGHT);
	
		blGrove = new Grove(bottomLeft, 0, 0, width + 30, height + 30, 50, 10);
		blGrove.clearArea(width - 100, 0, 100, 100);
		
	/******************
	 * LEFT
	 */
		new Wall(left, -10, 0, 10, height);
		new Wall(left, 0, height, width, 10);
		new Wall(left, 0, -10, width, 10);
		
		new ContinuousGate(left, width, 0, 10, height, RoomDungeon.RIGHT);
		new ContinuousGate(left, width - 50, -9, 50, 10, RoomDungeon.UP);
		new ContinuousGate(left, width - 50, height - 1, 50, 10, RoomDungeon.DOWN);
		
		lGrove1 = new Grove(left, width / 6, -10, 2 * width / 3, height + 30, 40, 25);
		lGrove2 = new Grove(left, -10, -10, width / 2, height + 30, 50, 10);
		
	/******************
	 * UPPER LEFT
	 */
		new Wall(upperLeft, -10, 0, 10, height);
		new Wall(upperLeft, width, 0, 10, height);
		new Wall(upperLeft, 0, height, width, 10);
		new Wall(upperLeft, 0, -10, width, 10);
		
		new ContinuousGate(upperLeft, width - 50, height - 1, 50, 10, RoomDungeon.DOWN);
		new ContinuousGate(upperLeft, width - 1, height - 50, 10, 50, RoomDungeon.RIGHT);
		
		ulGrove = new Grove(upperLeft, 0, 0, width + 30, height + 30, 50, 10);
		ulGrove.clearArea(width - 100, height - 100, 100, 100);
		
	/******************
	 * TOP
	 */
		new Wall(top, -10, 0, 10, height);
		new Wall(top, width, 0, 10, height);
		new Wall(top, 0, -10, width, 10);
		
		new ContinuousGate(top, -9, height - 50, 10, 50, RoomDungeon.LEFT);
		new ContinuousGate(top, width - 1, height - 50, 10, 50, RoomDungeon.RIGHT);
		new ContinuousGate(top, 0, height, width, 10, RoomDungeon.DOWN);
		
		tGrove1 = new Grove(top, -10, height / 6, width + 30, 2 * height / 3, 40, 25);
		tGrove2 = new Grove(top, -10, -10, width + 30, height / 2, 50, 10);
	
	/***********************************/
	// "CIVILIZED" AREAS
		
	/******************
	 * CABIN
	 */
		cabinLeft = new ContinuousGate(cabin, -10, 0, 10, height, RoomDungeon.LEFT);
		cabinRight = new ContinuousGate(cabin, width, 0, 10, height, RoomDungeon.RIGHT);
		cabinBottom = new ContinuousGate(cabin, 0, height, width, 10, RoomDungeon.DOWN);
		cabinTop = new ContinuousGate(cabin, 0, -10, width, 10, RoomDungeon.UP);
			
		// House:
			new Wall(cabin, 175, 125, 150, 150) {
				public void draw(Graphics g) {
					g.setColor(new Color(120, 75, 0));
					g.fillRect(xPos, yPos, xSize, ySize);
					g.setColor(Color.BLACK);
					g.drawLine(175, 125, 325, 275);
					g.drawLine(175, 275, 325, 125);
				}
			};
			// Porch:
			new BackgroundPainting(cabin, 200, 275, 100, 50, 1) {
				public void draw(Graphics g) {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(xPos, yPos, xSize, ySize);
					
					g.setColor(Color.BLACK);
					g.drawLine(200, 300, 300, 300);
					g.drawLine(200, 305, 300, 305);
					g.drawLine(200, 310, 300, 310);
					g.drawLine(200, 315, 300, 315);
					g.drawLine(200, 320, 300, 320);

				}
			};
			doorToCabin = new CabinDoor(cabin, 200, 265, 100, 10, interior) {
				public void draw(Graphics g) {}
				public boolean collisionEvent(Entity collider) {
					if (super.collisionEvent(collider)) return true;
					else {
						GUI.graphics.setBackground(new Color(150, 125, 75));
						return false;
					}
				}
			};
			cabinStairs = new Zone(cabin, 220, 300, 60, 25);
			// to stop ghost runner from getting stuck:
			new Zone(cabin, 175, 125, 150, 200) {
				public boolean collisionEvent(Entity collider) {
					if (collider.getClass().getSimpleName().equals("GhostRunner"))
						return true;
					else return false;
				}
			};
			
			new Wall(cabin, 200, 275, 10, 50).setClosedColor(Color.DARK_GRAY);
			new Wall(cabin, 290, 275, 10, 50).setClosedColor(Color.DARK_GRAY);
		
		
		lanternUL = new Lantern(cabin, 100, 100, 10, 10);
		lanternBL = new Lantern(cabin, 130, 350, 10, 10);
		lanternBR = new Lantern(cabin, 370, 350, 10, 10);
		lanternUR = new Lantern(cabin, 400, 100, 10, 10);
		
	/******************
	 * INTERIOR
	 */
		interior.setSpawnLocation(width / 2, height - 130);
		
		// Walls:
		
		new BackgroundPainting(interior, 0, 0, width, 100, new Color(100, 50, 0), new Color(100, 50, 0), 0) {
			public boolean collisionEvent(Entity collider) {
				return true;
			}
		};
		new BackgroundPainting(interior, 0, 0, 100, height, new Color(100, 50, 0), new Color(100, 50, 0), 0) {
			public boolean collisionEvent(Entity collider) {
				return true;
			}
		};
		new BackgroundPainting(interior, width - 100, 0, 100, height, new Color(100, 50, 0), new Color(100, 50, 0), 0){
			public boolean collisionEvent(Entity collider) {
				return true;
			}
		};
		new BackgroundPainting(interior, 0, height - 100, width, 100, new Color(100, 50, 0), new Color(100, 50, 0), 0) {
			public boolean collisionEvent(Entity collider) {
				return true;
			}
		};	

		new Gate(interior, width / 2 - 20, height - 100, 40, 10, cabin) {
			public boolean collisionEvent(Entity collider) {
				cabin.setSpawnLocation(250, 295);
				if (super.collisionEvent(collider)) return true;
				else {
					GUI.graphics.setBackground(new Color(80, 75, 25));
					return false;
				}
			}
		};
	
		// FURNITURE
		
		// water bucket
		new BackgroundPainting(interior, 107, height - 160, 30, Color.BLUE, new Color(100, 100, 100), 0).setCollidable(true);
		// barrels:
		new BackgroundPainting(interior, width - 150, height - 150, 50, new Color(120, 75, 0), Color.BLACK, 3, 0).setCollidable(true);
		new BackgroundPainting(interior, width - 130, height - 180, 30, new Color(110, 65, 0), Color.BLACK, 3, 0).setCollidable(true);
		new BackgroundPainting(interior, width - 185, height - 135, 35, new Color(120, 75, 0), Color.BLACK, 3, 0).setCollidable(true);
		new BackgroundPainting(interior, width - 170, height - 155, 25, new Color(120, 75, 0), Color.BLACK, 3, 0).setCollidable(true);
		new BackgroundPainting(interior, width - 120, height - 200, 20, new Color(120, 75, 0), Color.BLACK, 3, 0).setCollidable(true);
		new BackgroundPainting(interior, width - 150, height - 170, 25, new Color(120, 75, 0), Color.BLACK, 3, 0).setCollidable(true);
		// table:
		new BackgroundPainting(interior, width / 2 - 25, 100, 50, 30, new Color(80, 75, 25), Color.BLACK, 2, 0).setCollidable(true);
		// cushion:
		new BackgroundPainting(interior, width / 2 - 10, 140, 20, 20, 0) {
			public void draw(Graphics g) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRoundRect(xPos, yPos, xSize, ySize, 10, 10);
			}
		};
		// bed:
		new BackgroundPainting(interior, 335, 115, 50, 80, Color.WHITE, new Color(50, 35, 0), 8, 0);
		// wood stack:
		new BackgroundPainting(interior, 104, height / 2 - 110, 5, 40, new Color(120, 75, 0), Color.BLACK, 1, 0).setCollidable(true);
		new BackgroundPainting(interior, 109, height / 2 - 110, 8, 40, new Color(120, 75, 0), Color.BLACK, 1, 0).setCollidable(true);
		new BackgroundPainting(interior, 117, height / 2 - 110, 11, 40, new Color(120, 75, 0), Color.BLACK, 1, 0).setCollidable(true);
		new BackgroundPainting(interior, 128, height / 2 - 110, 8, 40, new Color(120, 75, 0), Color.BLACK, 1, 0).setCollidable(true);
		new BackgroundPainting(interior, 136, height / 2 - 110, 5, 40, new Color(120, 75, 0), Color.BLACK, 1, 0).setCollidable(true);
		
		// fireplace:
		fire = new Fireplace(interior, 100, height / 2 - 50, 40, 100, RoomDungeon.RIGHT, 7);
		
	/*****************************************************/
	// ANIMATIONS
			
		sequenceOne = new AnimationSequence(new Object[] {
			new AnimationFlicker(cabin) {
				public void action() {
					Sound.SCARY_NOISE_LOUD.play();
					cabin.add(sethIdeal);
				}
			},
			new AnimationMovement(sethIdeal, AnimationMovement.UP, 50, .015),
			new AnimationMovement(sethIdeal, AnimationMovement.LEFT, 25, .015),
			new AnimationMovement(sethIdeal, AnimationMovement.DOWN, 50, .015),
			new AnimationMovement(sethIdeal, AnimationMovement.RIGHT, 25, .015),
			new AnimationMovement(sethIdeal, 135, 100, .02),
			new AnimationMovement(sethIdeal, 135, 15, .01),
			new AnimationMovement(sethIdeal, -45, 50, .01),
			new Speech(GUI.player, "Umm . . . hello?"),
			new AnimationFlicker(cabin) {
				public void action() {
					cabin.agents.remove(sethIdeal);
				}
			},
			new Speech(GUI.player, ". . ."),
			new Speech(GUI.player, "Hello? Is anybody there?"),
			new Speech(GUI.player, "Maybe I should knock . . .")
		}) {
			public void finishAnimationEvent() {
				Sound.SCARY_NOISE_LOUD.stop();
			}
		};
		
		sequenceTwo = new AnimationSequence(new Object[] {
			new Speech(GUI.player, "Sir, please--"),
			new Speech(seth, "HAHAHA! Did you see yourself just then? You were like--hold on a second--you were--aHAHA!"),
			new Speech(GUI.player, ". . . ?"),
			new Speech(seth, "Ahem. I apologize. It can get boring out here. I have to do these things to stay sane."),
			new Speech(GUI.player, "Okay . . ."),
			new Speech(seth, "So. Let's hear it. State your name and business. What are you doing here?"),
			new Speech(GUI.player, "I was told you could teach me to change."),
			new Speech(seth, "Ah . . . yes . . ."),
			new Speech(GUI.player, "Can you?"),
			new Speech(seth, "Well, you see, young man, you already know how."),
			new Speech(GUI.player, "I'm sorry. I don't think I do."),
			new Speech(seth, "Oh, but you do. You know everything, but you have forgotten much. We teachers, our job is to remind you of it all."),
			new Speech(GUI.player, "That doesn't make any sense."),
			new Speech(seth, "Well you see, young man, there once lived a philosopher named Plato who--"),
			new Speech(GUI.player, ". . ."),
			new Speech(seth, "<i>(Sighs.)"),
			new Speech(seth, "Never mind. Anyway, as I was saying. What was I saying?"),
			new Speech(GUI.player, "You were going to show me how to change."),
			new Speech(seth, "Ah, yes. That. Where to start, where to start."),
			new Speech(GUI.player, ". . ."),
			new Speech(seth, ". . ."),
			new Speech(seth, "Ah! That's it! You see, Ryan, you are not the only thing that looks the way you do."),
			new Speech(GUI.player, "I know."),
			new Speech(seth, "There you go! Plato! Ahem. Anyways. You have . . . an Ideal, Ryan."),
			new Speech(seth, "It is like a soul, or a guardian angel. It is who you are. And if it changes, you change."),
			new Speech(GUI.player, "An . . . Ideal?"),
			new Speech(seth, "Yes, yes. You see, Plato--"),
			new Speech(GUI.player, ". . ."),
			new Speech(seth, "Ahem. Right. Anyway."),
			new Speech(GUI.player, "So how do I make it change?"),
			new Speech(seth, "Well, uh. First you have to get it to appear."),
			new Speech(GUI.player, "And how do I do that?"),
			new Speech(seth, "I'm, uh. Not actually quite sure. Haven't done it in a while . . ."),
			new Speech(seth, "Hmm . . ."),
			new Speech(GUI.player, ". . ."),
			new Speech(seth, "Well, ah, maybe if you try random things it will appear eventually."),
			new Speech(GUI.player, "Random things? Like what?"),
			new Speech(seth, "I don't know. How did you figure out how to walk? You tried what felt right and it worked. You're the one who knows this!"),
			new Animation(.01) {
				public void event() {
					GUI.player.speak("You're supposed to remind me!");
					doug.setXPos(GUI.player.getXPos());
					cabin.add(doug);
					stop();
				}
			}, 
			new Speech(seth, "Ah!"),
			new Animation(.01) {
				public void event() {
					GUI.player.speak("What?");
					cabin.agents.remove(doug);
					stop();
				}
			},
			new Speech(seth, "It was just there! Right there!"),
			new Animation(.01) {
				public void event() {
					GUI.player.speak("What was right here?");
					cabin.add(doug);
					stop();
				}
			},
			new Animation(.01) {
				public void event() {
					seth.speak("Your Ideal, you idiot! AH! There it is again! It's gone! Stop making it disappear!");
					cabin.agents.remove(doug);
					stop();
				}
			},
			new Animation(.01) {
				public void event() {
					GUI.player.speak("What am I doing?");
					cabin.add(doug);
					stop();
				}
			},
			new Animation(.01) {
				public void event() {
					seth.speak("I don't know! Something! Just stop!");
					cabin.agents.remove(doug);
					stop();
				}
			},
			new Animation(.01) {
				public void event() {
					GUI.player.speak("What do you want me to do, stop breathing?");
					cabin.add(doug);
					stop();
				}
			},
			new Animation(.01) {
				public void event() {
					seth.speak("Shut up! It's because you're talking!");
					cabin.agents.remove(doug);
					stop();
				}
			},
			new Animation(.01) {
				public void event() {
					seth.speak("There, do you see it? There it is.");
					cabin.add(doug);
					stop();
				}
			},
			new AnimationMovement(GUI.player, AnimationMovement.DOWN, 10),
			new Animation(.01) {
				public void event() {
					GUI.player.speak("Oh my--");
					cabin.agents.remove(doug);
					stop();
				}
			},
			new Animation(.01) {
				public void event() {
					seth.speak("Gah! You fool!");
					cabin.add(doug);
					stop();
				}
			},
			new Speech(GUI.player, "I see it! I see it! It's really there!"),
			new Speech(seth, "Now stay very still, young man, very still--"),
			new Speech(GUI.player, "--I think I got it--"),
			new Speech(seth, "If it disappears again, young man, I think I am going to cry--"),
			new Speech(GUI.player, "--it's staying--look! It's staying!"),
			new Speech(seth, "Ah. Thank you. Now. Calmly, dismiss it."),
			new Speech(GUI.player, "What?"),
			new Speech(seth, "You heard me."),
			new Speech(GUI.player, "But we just got it to appear . . ."),
			new Speech(seth, "Do as I say! Oh, and when you're done, would you mind brightening that lantern for me?"),
			new Speech(GUI.player, "The one over there?"),
			new Speech(seth, "Yes. They dim sometimes. I don't know why. It's very strange. Anyway, would you mind brightening it? All you have to do is touch it."),
			new Speech(GUI.player, "Okay . . ."),
			new Speech(seth, "Oh, and when you're done with that, meet me inside, would you? I'm going to make some tea."),
			new AnimationMovement(seth, AnimationMovement.UP, 50),
			new Animation(.01) {
				public void event() {
					cabin.agents.remove(seth);
					GUI.currentSpeaker = null;
					stop();
				}
			},
			new Speech(GUI.player, "He clearly has not seen a person for several years . . . his breath smells like onions . . "),
			new Speech(GUI.player, "<i>(Press the spacebar to summon or dismiss your Ideal."),
			new Animation(.01) {
				public void event() {
					cabin.agents.remove(doug);
					GUI.player.canSummon = true;
					
					cabinTop.open(); cabinBottom.open();
					cabinRight.open(); cabinLeft.open();
						doorToCabin.open();
						
					seth.setXPos(160); seth.setYPos(225);
					interior.add(seth);
					
					interior.setEntranceEvent(new Animation(.01) {
						public void event() {
							Animation.enterDialogueMode(sequenceThree);
							interior.setEntranceEvent(null);
							
							GUI.player.removePhantom();
							stop();
						}
					});
					
					stop();
				}
			},
		});
		
		sequenceThree = new AnimationSequence(new Object[] {
			new AnimationMovement(GUI.player, AnimationMovement.UP, 80),
			new Speech(seth, "Ah. So much warmer in here, wouldn't you say?"),
			new Speech(GUI.player, "Yeah."),
			new Speech(seth, "Well?"),
			new Speech(GUI.player, "Yeah?"),
			new Speech(seth, "Aren't you going to summon it?"),
			new Speech(GUI.player, "Okay . . ."),
			new Animation(.01) { 
				public void event() {
					doug.setXPos(250); doug.setYPos(300);
					interior.add(doug);
					stop();
				}
			},
			new Speech(seth, "Good afternoon, dear fellow! How are you doing? What is your name? I'm Seth. Would you like some tea?"),
			new Speech(doug, "I can't even drink that . . ."),
			new Speech(seth, "Oh, look at it! It talks just like you do!"),
			new Speech(GUI.player, "He does . . ."),
			new Speech(doug, "I'm not 'it.' I'm Doug."),
			new Speech(seth, "Welcome, Doug, welcome, welcome!"),
			new Speech(GUI.player, "Hello, Doug. Are you me?"),
			new Speech(doug, "I'm not you . . . I'm Doug."),
			new Speech(seth, "We understand that, dear Doug. He is asking if you are his Ideal."),
			new Speech(doug, "I don't know what that is."),
			new Speech(seth, "Well, you see, according to Plato's--"),
			new Speech(doug, ". . ."),
			new Speech(GUI.player, ". . ."),
			new Speech(seth, "<i>(Sighs.)"),
			new Speech(seth, "Yes, yes, I know."),
			new Speech(GUI.player, "Doug, I need you to change."),
			new Speech(doug, "Change?"),
			new Speech(GUI.player, "Yes. I need you to be like everyone else is."),
			new Speech(doug, "Like everyone else is?"),
			new Speech(GUI.player, "Yeah. You know."),
			new Speech(doug, "No . . . I don't . . ."),
			new Speech(GUI.player, "It's like . . . they're sharper. More composed. Stable. I don't know how to say it."),
			new Speech(doug, ". . ."),
			new Speech(GUI.player, "Like solid, you know? Like they're comfortable leaning against walls. Like when they sit in class, it's like they fill their space perfectly."),
			new Speech(GUI.player, "And they can walk through doors comfortably. The doors are just right for them. Not airy like they are for me. And the urinals are actually shaped right for them."),
			new Speech(GUI.player, "It's like the toilets, they were built for people like that. The rooms were built for them. The school was. The world was."),
			new Speech(doug, ". . ."),
			new Speech(GUI.player, "I need you to become one of them, Doug."),
			new Speech(doug, "But . . . I like who I am . . ."),
			new Speech(GUI.player, "Please?"),
			new Speech(doug, "I don't get why I have to change . . ."),
			new Speech(GUI.player, "Because you're part of me. And I don't know what school you go to but I bet you don't have to deal with feeling different all the time, do you?"),
			new Speech(doug, "No."),
			new Speech(GUI.player, "So can you just do that one thing?"),
			new Speech(doug, "No."),
			new Speech(seth, "Come now, good old Doug! It's not too bad. You don't even notice."),
			new Speech(doug, "But I will. I'll notice in the mirror every day."),
			new Speech(GUI.player, "Have you even seen a mirror before?"),
			new Speech(doug, ". . ."),
			new Speech(doug, "No."),
			new Speech(GUI.player, "Have you seen anything before?"),
			new Speech(doug, "Not really."),
			new Speech(GUI.player, "See. You don't even know what it's like out there. I'm asking you to change for both our sakes."),
			new Speech(doug, "But I don't want to."),
			new Speech(GUI.player, "Look, quit being so dramatic. Just change okay?"),
			new AnimationMovement(doug, AnimationMovement.DOWN, 30),
			new Speech(doug, "Why do you keep shouting at me?"),
			new Speech(GUI.player, "I'm not shouting."),
			new Speech(doug, "I want to go . . . you're scaring me . . ."),
			new Speech(GUI.player, "I'm sorry. I can't let you go."),
			new Animation(.01) {
				public void event() {
					doug.speak("But . . . why? What have I ever done to you?");
					new AnimationMovement(doug, AnimationMovement.DOWN, 30).start();
					stop();
				}
			},
			new Speech(doug, "Is there something wrong with me? Is that it?"),
			new Speech(GUI.player, ". . ."),
			new Speech(doug, "Because if there is, why don't you just say it? I hate you! I hate both of you!"),
			new Animation(.01) {
				public void event() {
					interior.agents.remove(doug);
					GUI.currentSpeaker = null;
					stop();
				}
			},
			new Speech(GUI.player, "Wait!"),
			new Speech(seth, "After him!"),
		}) {
			public void finishAnimationEvent() {
				GUI.player.removePhantom();
				GUI.player.canSummon = false;
				
				doug.unfreeze();
				Player.setHelpText("I need to catch Doug.");
				
				cabin.setEntranceEvent(new Animation(.01) {
					public void event() {
						cabinTop.close(); cabinBottom.close();
						cabinRight.close(); cabinLeft.close();
						doorToCabin.close();
						
						new AnimationMovement(GUI.player, AnimationMovement.DOWN, 100).start();
						new Animation(.5) {
							public void event() {
								seth.setXPos(225); seth.setYPos(285);
								cabin.add(seth);
								stop();
							}
						}.start();
						new Animation(5) {
							public void event() {
								Animation.enterDialogueMode(sequenceFour);
								GUI.player.speak("What are you doing just standing there? Help me!");
								stop();
							}
						}.start();
						
						cabinTop.close(); cabinBottom.close();
						cabinRight.close(); cabinLeft.close();
						cabin.add(doug);
						doug.unfreeze();
						
						cabin.setEntranceEvent(null);
						stop();
					}
				});
			}
		};
		
		sequenceFour = new AnimationSequence(new Object[] {
			new Speech(seth, "Well . . . you see . . . I . . ."),
			new Speech(GUI.player, "HELP ME!"),
			new Speech(seth, "You will have to tell me what to do."),
			new Speech(GUI.player, "What do you mean I'll have to--?"),
			new Speech(seth, "Well, as I said, it's been a while . . ."),
			new Speech(GUI.player, "Gah!"),
			new Speech(seth, "<i>(Control Seth with W, A, S, D.)")
		}) {
			public void finishAnimationEvent() {
				GUI.player.sethMoving = true;
				
				Player.setHelpText("I need to catch Doug!");
				
				seth.unfreeze();
				new Animation(5) {
					public void event() {
						seth.speak(5, "Grab him!");
						stop();
					}
				}.start();
				new Animation(60) {
					public void event() {
						seth.speak(3, "Grab him!");
					}
				}.start();
				new Animation(.01) {
					public void event() {
						if (doug.caughtByPlayer){
							GUI.player.speak(1, "Got him!");
							Animation.enterDialogueMode(sequenceFive);
							stop();
						}
					}
				}.start();
			}
		};
		
		sequenceFive = new AnimationSequence(new Object[] {
			new Speech(GUI.player, "Got him!"),
			new Speech(doug, "Stop it! Leave me alone!"),
			new Speech(seth, "Kid, maybe it would be best to--"),
			new Speech(doug, "I hate you, I hate you, I hate you!"),
			new Speech(seth, "Ryan, get away from him!")
		}) {
			public void finishAnimationEvent() {
				doug.attackMode = true;
				doug.caughtBySeth = false;
				
				Player.setHelpText("I need to stay away from Doug while Seth catches him.");
				
				new Animation(.01) {
					public void event() {
						if (doug.caughtBySeth) {
							seth.speak("Alright now, son--");
							Animation.enterDialogueMode(sequenceSix);
							stop();
						}
					}
				}.start();
			}
		};
		
		sequenceSix = new AnimationSequence(new Object[] {
			new Speech(doug, "It's not fair . . ."),
			new Speech(GUI.player, "Doug?"),
			new Speech(doug, "It's not fair! Why are you chasing me?"),
			new Speech(seth, "Now, now, children, why don't we take this inside?"),
			new Speech(GUI.player, "I won't chase you anymore . . ."),
			new Speech(doug, "Change! Change! All you want me to do is change! Well let's see you do it first!"),
			new Speech(GUI.player, "Okay. I get it."),
			new Speech(seth, "Both of you. Stop! Inside! Now!"),
			new Speech(doug, "No. I want to leave."),
			new Speech(GUI.player, "Doug--"),
			new Speech(GUI.player, ". . ."),
			new Speech(GUI.player, "Alright . . . okay . . ."),
			new Speech(seth, "Ryan?"),
			new Animation(.5) {
				public void event() {
					Sound.DISMISS.play();
					cabin.agents.remove(doug);
					stop();
				}
			},
			new Speech(seth, ". . ."),
			new Speech(GUI.player, ". . ."),
			new Speech(seth, "Do you want to come in, Ryan?"),
			new Speech(GUI.player, "No."),
			new Speech(seth, ". . ."),
			new Speech(seth, "I'll be making tea."),
		}) {
			public void finishAnimationEvent() {
				Animation.endAll();
				
				Player.setHelpText("I feel like a jerk . . .");
				
				GUI.player.sethMoving = false;
				GUI.player.canSummon = false;
				// seth going back to house:
				new Animation(.01) {
					public void event() {
						seth.track(350, 400);
						if (seth.near(350, 400, 3)) {
							stop();
							new Animation(.01) {
								public void event() {
									seth.track(250, 285);
									if (seth.near(250, 285, 3)) {
										cabin.agents.remove(seth);
										stop();
									}
								}
							}.start();
						}
						
					}
				}.start();
			
				// start the rain:
				new Animation(15) {
					public void event() {
						GUI.player.speak(3, "It's raining . . .");
						Sound.FOREST_DEEP.stop();
						Sound.OUTSIDE_RAIN.loop();
						darkenGroves(6);
						// random lightning generator:
						lightning = new Animation(30) {
							public void event()
							{
								if (Math.random() > .25)
								{
									if (Math.random() > .5)
										Sound.LIGHTNING1.play();
									else
										Sound.LIGHTNING2.play();
								}
							}
						};
						lightning.start();
						stop();
					}
				}.start();
				new Animation(30) {
					public void event() {
						Sound.SCARY_NOISE_QUIET.loop();
						GUI.player.speak(3, "Where is that noise coming from?");
						cabinTop.open(); cabinBottom.open();
						cabinRight.open(); cabinLeft.open();
						
						sethIdeal.setXPos(260); sethIdeal.setYPos(390);
						top.add(sethIdeal);
						
						Player.setHelpText("I better figure out where that noise is coming from . . .");
						
						stop();
					}
				}.start();
				top.setEntranceEvent(new Animation(.01) {
					public void event() {
						Sound.SCARY_NOISE_QUIET.stop();
						Sound.SCARY_NOISE_LOUD.loop();
						Animation.enterDialogueMode(sequenceSeven);
						top.setEntranceEvent(null);
						stop();
					}
				});
			}
		};
		
		sequenceSeven = new AnimationSequence(new Object[] {
			new Speech(GUI.player, "Ack! My ears . . . they're bursting . . . it's like I'm underwater . . ."),	
			new Speech(GUI.player, "Hey! Hey, you! Who are you?"),
			new Speech(GUI.player, ". . ."),
			new AnimationFlicker(top) {
				public void action() {
					top.agents.remove(sethIdeal);
					Sound.SCARY_NOISE_LOUD.stop();
				}
			},
			new Speech(GUI.player, "Not this again . . ."),
			new Speech(GUI.player, "I better tell Seth . . ."),
		}) {
			public void finishAnimationEvent() {
				Player.setHelpText("I better tell Seth about that creepy guy . . .");
				
				doorToCabin.open();
				seth.freeze();
				seth.setXPos(250); seth.setYPos(175);
				interior.add(seth);
				interior.setEntranceEvent(new Animation(.01) {
					public void event() {
						Sound.OUTSIDE_RAIN.stop();
						Sound.INSIDE_RAIN.loop();
						new AnimationMovement(GUI.player, AnimationMovement.UP, 30).start();
						GUI.player.speak("Seth?");
						fire.extinguish();
						Animation.enterDialogueMode(sequenceEight);
						interior.setEntranceEvent(null);
						stop();
					}
				});
			}
		};
		
		sequenceEight = new AnimationSequence(new Object[] {
			new Speech(seth, "Oh . . ."),
			new Speech(GUI.player, "Seth? Are you okay?"),	
			new Speech(seth, "Oh . . . dear child . . . I feel very sorry for you . . ."),	
			new Speech(GUI.player, ". . ."),	
			new Speech(GUI.player, "It doesn't matter. We can't make Doug change if he doesn't want to."),	
			new Speech(seth, "No, Ryan. Didn't you hear it?"),	
			new Speech(GUI.player, "Hear what?"),	
			new Speech(seth, "The Growling."),	
			new Speech(GUI.player, "Umm . . . what?"),	
			new Speech(seth, "I can't believe I missed it this entire time. I'm going deaf. Yes. That must be it. I'm growing old."),	
			new Speech(GUI.player, "You mean the creepy guy by the woods?"),	
			new Speech(seth, "The--what--did you just say--!"),	
			new Speech(GUI.player, "Yeah. I saw him when I came in, and he was just there again."),	
			new Speech(seth, "God help us."),	
			new Speech(GUI.player, ". . . what's he going to do to us?"),	
			new Speech(seth, "It's not just him that we should worry about, Ryan. It's the others."),	
			new Speech(GUI.player, "The others?"),	
			new Speech(seth, "Yes . . . you see, Ryan . . ."),	
			new Speech(GUI.player, "Yes?"),	
			new Speech(seth, "<i>(Sighs.)"),	
			new Speech(GUI.player, "What is it?"),	
			new Speech(seth, "Let us just say that not every Ideal turns out . . . ideally . . . "),	
			new Speech(GUI.player, ". . ."),	
			new Speech(seth, "AHAHAHAHAHAHAHAHA! AHAHAHAHA!"),	
			new Speech(GUI.player, "Really? You had me all worked up . . ."),	
			new Speech(seth, "<i>And for good reason, too!"),	
			new Speech(seth, "Becuase let me tell you, Ryan. Sometimes, every so often, somebody calls their Ideal . . . and they don't get along."),	
			new Speech(GUI.player, ". . . like me and Doug."),	
			new Speech(seth, "Yes."),	
			new Speech(GUI.player, "How often is every so often?"),	
			new Speech(seth, "Most of the time, actually."),	
			new Speech(GUI.player, "As in?"),	
			new Speech(seth, "Okay. It has only worked twice. But that is beyond the--"),
			new Speech(GUI.player, "Why didn't you tell me this in the first place?"),
			new Speech(seth, "Blame yourself! You were the one going all OH NO I NEED TO CHANGE WHO I AM!!"),
			new Speech(GUI.player, ". . ."),
			new Speech(GUI.player, "So what happens with the Ideals."),
			new Speech(seth, "I don't know. They disappear. They travel somewhere, I suppose. And every so often . . . they come back."),
			new Speech(GUI.player, "Why?"),
			new Speech(seth, "Why are you asking me?"),
			new Speech(GUI.player, "I thought you knew this. Plato right?"),
			new Speech(seth, "Well Plato doesn't count! I don't know and nobody does. But when they come back, they're changed."),
			new Speech(GUI.player, "As in untalkative and socially awkward?"),
			new Speech(seth, "As in bloodthirsty, Ryan. They're after you."),
			new Speech(GUI.player, "After me?"),
			new Speech(seth, "Oh, yes. They know you have an Ideal. A fresh member. And all they have to do to get him for keeps is kill you."),
			new Speech(GUI.player, "Well then."),
			new Speech(seth, "I am sorry, Ryan. Very very sorry."),
			new Speech(GUI.player, "Then we'll fight them off. Or we'll run."),
			new Speech(seth, "The problem is, we can't. We can't touch them."),
			new Speech(GUI.player, "What?"),
			new Speech(seth, "They're not quite physical."),
			new Speech(GUI.player, "I touched Doug just fine."),
			new Speech(seth, "Because he let you. Because even though you were attacking him, he felt sorry for you."),
			new Speech(GUI.player, ". . . Oh."),
			new Speech(seth, "Yes. And now he's gone. Nothing to be done about. Welp! Alright! Good-bye! Have a good night!"),
			new Speech(GUI.player, "Umm . . . what do you mean?"),
			new Speech(seth, "Oh, come on. Do you expect me to be okay with you dying in my house?"),
			new Speech(GUI.player, "Well, uh. I assumed you were going to help me."),
			new Speech(seth, "Help you? How?"),
			new Speech(GUI.player, "There has to be some way."),
			new Speech(seth, "Oh yes. Oh, yes. Doug can touch them. He can hurt them, certainly. But I don't reckon he'll be too glad to help you."),
			new Speech(GUI.player, "Can't you do anything?"),
			new Speech(seth, "Me? What do you think I am, some wise old assistant on your quest? Pah!"),
			new Speech(GUI.player, ". . ."),
			new Speech(GUI.player, "You know, thank you. Thank you for helping me. Thank you real much. I really appreciate it."),
			new Speech(seth, "Yes. Uh-huh. Sure."),
			new Speech(GUI.player, "You have been so, so kind. You know--"),
			new Speech(seth, "Shh!"),
			new Animation(1) {
				public void event() {
					Sound.SCARY_NOISE_LOUD.play();
					stop();
				}
				public void terminationEvent() {
					Sound.SCARY_NOISE_QUIET.loop();
				}
			},
			new Speech(seth, "They're here."),
		}) {
			public void finishAnimationEvent() {
				Level.currentSpawn = spawn2;
				
				Sound.INSIDE_RAIN.stop();
				Sound.OUTSIDE_RAIN.loop();
				
				GUI.player.phantom = null;
				
				Player.setHelpText("They're coming after me! I need Doug to fight them off.");
				
				new Animation(.01) {
					public void event() {
						if (GUI.player.phantomMoving) {
							sequenceNine();
							GUI.player.phantomMoving = false;
							GUI.player.canSummon = false;
							stop();
						}
					}
				}.start();
				new Animation(30) {
					int waveCount = 1;
					public void event() {
						if (waveCount >= 12){
							if (cabin.agents.isEmpty()) {
								stop();
								GUI.player.speak("They're gone.");					
								sequenceTen();
							}
							return;
						};
						spawnCreatures(waveCount);
						waveCount++;
					}
				}.start();
				spawnPoint2();
			}
		};
		
		sequenceEleven = new AnimationSequence(new Object[] {
			new Speech(seth, "You're alive . . . !"),
			new AnimationMovement(seth, AnimationMovement.DOWN, 50),
			new Speech(GUI.player, "Yes."),
			new Speech(seth, "You killed them all?"),
			new Speech(GUI.player, "Yes."),
			new Speech(doug, ". . ."),
			new Speech(seth, "This is incredible. I never knew something like this could--I always thought--"),
			new Speech(GUI.player, "You left me to die."),
			new Speech(seth, ". . ."),
			new Animation(.01) {
				public void event() {
					seth.speak("Yes. I did. I am sorry, Ryan. You must understand. My Ideal and I . . . we were friends for so long. Years even. I spent my childhood with him.");
					sethIdeal.setXPos(310); sethIdeal.setYPos(-25);
					interior.add(sethIdeal);
					sethIdeal.collidesWithBarriers = false;
					new AnimationMovement(sethIdeal, AnimationMovement.DOWN, 90, .05).start();
					new AnimationMovement(circleSeven, AnimationMovement.DOWN, 90, .1).start();
					new AnimationMovement(circleEight, AnimationMovement.DOWN, 90, .2).start();
					new AnimationMovement(circleOne, AnimationMovement.RIGHT, 100, .2).start();
					new AnimationMovement(circleTwo, AnimationMovement.RIGHT, 100, .15).start();
					new AnimationMovement(circleThree, AnimationMovement.RIGHT, 100, .2).start();
					new AnimationMovement(circleFour, AnimationMovement.LEFT, 120, .25).start();
					new AnimationMovement(circleFive, AnimationMovement.LEFT, 120, .2).start();
					new AnimationMovement(circleSix, AnimationMovement.LEFT, 120, .2).start();
					new AnimationMovement(circleNine, AnimationMovement.UP, 145, .05).start();
					new AnimationMovement(circleTen, AnimationMovement.UP, 145, .1).start();
					new AnimationMovement(circleEleven, AnimationMovement.UP, 145, .07).start();
					stop();
				}
			},
			new Speech(GUI.player, "Okay."),
			new Speech(seth, "It was incredible, Ryan. And then he left."),
			new Speech(GUI.player, "You did what I did?"),
			new Speech(doug, ". . ."),
			new Speech(seth, "No. It did not happen like that. I never demanded for him to change. But one day he just . . . started acting different. And then he left."),
			new Speech(seth, "So many have come to me over the years seeking to change, Ryan. And I told all of them what I told you. To see an Ideal and its owner--"),
			new Speech(doug, "Hmph."),
			new Speech(seth, "--cooperate like that, to see them as friends--even if only for a few moments--it was the only thing that kept me going through all these years."),
			new Speech(GUI.player, "Okay."),
			new Speech(seth, "I know. I have sent most of them to their deaths."),
			new Speech(GUI.player, "I can forgive you. But they can't. They're dead."),
			new Speech(seth, "Yes."),
			new Speech(GUI.player, "How many did you kill?"),
			new Speech(seth, "I did not kill them! I was only trying to help them!"),
			new Speech(GUI.player, "How many?"),
			new Speech(seth, ". . ."),
			new Speech(seth, "I've lost count."),
			new Speech(GUI.player, "And did you ever tell them? Did you ever warn them what was to happen?"),
			new Speech(seth, "A few times. I swear! Bridgette, and Charlie, and--and--"),
			new Speech(seth, "Oh . . . God . . . please, Ryan . . . go home. I can't stand to have anyone look at me anymore."),
			new AnimationMovement(sethIdeal, AnimationMovement.DOWN, 80, .05),
			new Speech(GUI.player, "Seth . . . behind you . . ."),
			new AnimationMovement(seth, AnimationMovement.UP, -10),
			new Speech(seth, "You! Ah! Away! Get it away!"),
			new AnimationMovement(sethIdeal, AnimationMovement.DOWN, 50, .02),
			new Speech(sethIdeal, "I am not 'it.' I am Sid."),
			new Speech(seth, "I know! I know! Please! I'm sorry, whatever I've done, I'm sorry!"),
			new Speech(GUI.player, "Doug . . ."),
			new Speech(doug, "No."),
			new Speech(seth, "Ryan! Doug! Help me!"),
			new Speech(GUI.player, "Doug . . . protect him . . . please . . ."),
			new Speech(doug, "I've already murdered my own once. I won't do it again."),
			new Speech(seth, "Sid . . . Don't you remember those days? Sid? Don't you remember?"),
			new Speech(sethIdeal, ". . ."),
			new Speech(sethIdeal, "No."),
			new Animation(.01) {
				public void event() {
					sethIdeal.movingRight = true;
					new AnimationMovement(circleSeven, AnimationMovement.UP, 90, .01).start();
					new AnimationMovement(circleEight, AnimationMovement.UP, 90, .02).start();
					new AnimationMovement(circleOne, AnimationMovement.LEFT, 100, .025).start();
					new AnimationMovement(circleTwo, AnimationMovement.LEFT, 100, .03).start();
					new AnimationMovement(circleThree, AnimationMovement.LEFT, 100, .02).start();
					new AnimationMovement(circleFour, AnimationMovement.RIGHT, 120, .03).start();
					new AnimationMovement(circleFive, AnimationMovement.RIGHT, 120, .025).start();
					new AnimationMovement(circleSix, AnimationMovement.RIGHT, 120, .02).start();
					new AnimationMovement(circleNine, AnimationMovement.DOWN, 145, .03).start();
					new AnimationMovement(circleTen, AnimationMovement.DOWN, 145, .01).start();
					new AnimationMovement(circleEleven, AnimationMovement.DOWN, 145, .02).start();
					new AnimationMovement(sethIdeal, AnimationMovement.RIGHT, 200, .002).start();
					GUI.player.canSummon = false;
					stop();
				}
			},
			new Speech(GUI.player, ". . ."),
			new AnimationMovement(doug, AnimationMovement.DOWN, 100),
			new Animation(.01) {
				public void event() {
					interior.agents.remove(doug);
					stop();
				}
			},
		}) {
			public void finishAnimationEvent() {
				Sound.SCARY_NOISE_QUIET.stop();
				Sound.OUTSIDE_RAIN.stop();
				GUI.player.canSummon = false;
				doorToCabin.close();
				cabin.setEntranceEvent(new Animation(.01) {
					public void event() {
						doug.setXPos(450); doug.setYPos(400);
						cabin.add(doug);
						enterDialogueMode(sequenceTwelve);
						stop();
					}
				});
			}
		};
		
		sequenceTwelve = new AnimationSequence(new Object[] {
				new Speech(GUI.player, "Doug?"),
				new Speech(doug, "I'm going, Ryan."),
				new Speech(GUI.player, "Okay . . . I'll dismiss you . . ."),
				new AnimationMovement(doug, AnimationMovement.LEFT, 25, .01),
				new Speech(doug, "Why did you make me do it? Why'd I have to kill them?"),
				new Speech(GUI.player, "Look, I'm sorry. They were attacking me. You were the only defense I had."),
				new Speech(doug, "You're not my owner."),
				new Speech(GUI.player, "What?"),
				new Speech(doug, "You're not my owner. I don't 'belong' to you. I'm not your bodyguard."),
				new Speech(GUI.player, "Yes. I know. Doug--please."),
				new Speech(doug, "I can't stand to be around here anymore."),
				new Speech(GUI.player, "Okay . . ."),
				new Speech(doug, "And I want you to know something. I'm not the one who murdered them. Understand? I'm not the murderer. You are."),
				new Animation(.01) {
					public void event() {
						new AnimationMovement(doug, AnimationMovement.UP, 500).start();
						new Animation(3) {
							public void event() {
								cabin.remove(doug);
								stop();
							}
						}.start();
						stop();
					}
				},
				new AnimationMovement(GUI.player, AnimationMovement.DOWN, 130, .01),
				new Speech(GUI.player, ". . ."),
		}) {
			public void finishAnimationEvent() {
				new AnimationFlicker(cabin, 2) {
					public void action() {
						Sound.OUTSIDE_RAIN.stop();
						Sound.INSIDE_RAIN.stop();
						Level.setLevel(new LevelFour());
						lightning.stop();
					}
				}.start();
			}
		};
		
		darkenGroves(5);
		introAnimation();
		currentSpawn = spawn1;
				
		/*Level.currentSpawn = spawn2;
		new Animation(.01) {
			public void event() {
				if (GUI.player.phantomMoving) {
					sequenceNine();
					GUI.player.phantomMoving = false;
					GUI.player.canSummon = false;
					stop();
				}
			}
		}.start();
		new Animation(30) {
			int waveCount = 1;
			public void event() {
				if (waveCount >= 2){
					if (cabin.agents.isEmpty()) {
						stop();
						GUI.player.speak("They're gone.");					
						sequenceTen();
					}
					System.out.println("checking1");
					return;
				};
				spawnCreatures(waveCount);
				waveCount++;
				System.out.println("checking");
			}
		}.start();
		spawnPoint2();*/
		
	} // end createRooms()
	
	// a method rather than an animationSequence so that
	// the player can move while they talk
	private void sequenceNine() {
		new Animation(3) {
			public void event() {
				GUI.player.speak("Doug? Doug, I need your help!");
				stop();
			}
		}.start();
		new Animation(6) {
			public void event() {
				GUI.player.phantom.speak(". . .");
				stop();
			}
		}.start();
		new Animation(9) {
			public void event() {
				GUI.player.speak("Please . . . I'm going to die . . .");
				stop();
			}
		}.start();
		new Animation(12) {
			public void event() {
				GUI.player.phantom.speak("You're asking me to fight my own kind.");
				stop();
			}
		}.start();
		new Animation(15) {
			public void event() {
				stop();
				GUI.player.speak("They're different! Just look at them! Look how different they are!");
			}
		}.start();
		new Animation(18) {
			public void event() {
				stop();
				GUI.player.phantom.speak("I don't see what you're talking about.");
			}
		}.start();
		new Animation(21) {
			public void event() {
				stop();
				GUI.player.speak("I'm sorry for what I did earlier!");
			}
		}.start();
		new Animation(24) {
			public void event() {
				stop();
				GUI.player.phantom.speak("That doesn't change anything.");
			}
		}.start();
		new Animation(27) {
			public void event() {
				stop();
				GUI.player.speak("Just give me a second chance!");
			}
		}.start();
		new Animation(30) {
			public void event() {
				stop();
				GUI.player.phantom.speak(". . .");
			}
		}.start();
		new Animation(33) {
			public void event() {
				stop();
				GUI.player.phantom.speak("I'm going to regret this . . .");
			}
		}.start();
		new Animation(36) {
			public void event() {
				stop();
				GUI.player.phantom.speak("<i>(Control Doug with W, A, S, D.)");
				GUI.player.canSummon = true;
				GUI.player.phantomMoving = true;
			}
		}.start();
		new Animation(60) {
			public void event() {
				GUI.player.speak("The light seems to hurt them . . .");
				stop();
			}
		}.start();
	}	
	
	private void sequenceTen() {
		GUI.player.canSummon = false;
		if (GUI.player.phantom == null) GUI.player.summonPhantom();
		
		new Animation(3) {
			public void event() {
				GUI.player.speak(". . .");
				stop();
			}
		}.start();
		new Animation(6) {
			public void event() {
				GUI.player.phantom.speak(". . .");
				stop();
			}
		}.start();
		new Animation(9) {
			public void event() {
				GUI.player.speak("Seth . . . SETH!");
				stop();
			}
		}.start();
		new Animation(12) {
			public void event() {
				GUI.player.phantom.speak("He can't hear you.");
				stop();
			}
		}.start();
		new Animation(15) {
			public void event() {
				stop();
				GUI.player.speak("That damn cheater.");
			}
		}.start();
		new Animation(18) {
			public void event() {
				stop();
				GUI.player.phantom.speak("?");
			}
		}.start();
		new Animation(21) {
			public void event() {
				stop();
				GUI.player.speak("Come on. We have business to take care of.");
			}
		}.start();
		new Animation(24) {
			public void event() {
				doorToCabin.open();
				interior.setEntranceEvent(new Animation(.01) {
					public void event() {
						GUI.player.canSummon = false;
						GUI.player.phantom = null;
						seth.setXPos(350); seth.setYPos(150);
						doug.setXPos(200); doug.setYPos(250);
						interior.add(seth);
						interior.add(doug);
						Animation.enterDialogueMode(sequenceEleven);
						stop();
					}
				});
			}
		}.start();
	}
	
	Animation creatureSpawning;
	
	private void spawnCreatures(final int wave) {
		final int numberCreatures = 5 + (int) (wave * 3 * Math.random());
		creatureSpawning = new Animation(.5) {
			int creatureCount;
			public void event() {
				if (creatureCount >= numberCreatures) {
					stop();
					return;
				}
				
				boolean zombie = false;
				if (Math.random() > .5) zombie = true;
				
				Creature potentialCreature = null;
				int randomSide = Random.boundedInt(5, 30);

				switch ((int) (Math.random() * 4 + 1)) {
				case 1:
					if (zombie) potentialCreature = new Zombie(cabin, Random.roll(450), 410, randomSide, randomSide);
					else potentialCreature = new LightHunter(cabin, Random.roll(450), 410, randomSide, randomSide);
					break;
				case 2:
					if (zombie) potentialCreature = new Zombie(cabin, 1, Random.roll(450), randomSide, randomSide);
					else potentialCreature = new LightHunter(cabin, 1, Random.roll(450), randomSide, randomSide);
					break;
				case 3:
					if (zombie) potentialCreature = new Zombie(cabin, Random.roll(450), 1, randomSide, randomSide);
					else potentialCreature = new LightHunter(cabin, Random.roll(450), 1, randomSide, randomSide);
					break;
				case 4:
					if (zombie) potentialCreature = new Zombie(cabin, 460, Random.roll(450), randomSide, randomSide);
					else potentialCreature = new LightHunter(cabin, 460, Random.roll(450), randomSide, randomSide);
					break;
				}
				
				if (potentialCreature.collidePlayer() ||
					potentialCreature.collidePhantoms()) {
					cabin.remove(potentialCreature);
					potentialCreature = null;
				}
	
				creatureCount++;
			}
		};
		
		creatureSpawning.start();
	}

	
	
	private void darkenGroves(int amount) {
		urGrove.darken(amount); brGrove.darken(amount);
		ulGrove.darken(amount); blGrove.darken(amount);
		tGrove1.darken(amount); tGrove2.darken(amount);
		bGrove1.darken(amount); bGrove2.darken(amount);
		lGrove1.darken(amount); lGrove2.darken(amount);
		rGrove1.darken(amount); rGrove2.darken(amount);
	}
	
	/****************************************/
	// RESPAWNING
	
	SpawnPoint spawn1 = new SpawnPoint() {
		public void respawn() {
			GUI.player.setRadius(20);
			GUI.player.setXPos(250); GUI.player.setYPos(400);
			
			doug.setXPos(250); doug.setYPos(50);
			
			Dungeon.teleport(cabin);
						
			GUI.player.removePhantom();
			GUI.player.canSummon = false;		
		}
	};
	
	SpawnPoint spawn2 = new SpawnPoint() {
		public void respawn() {
			GUI.player.speak("<i>(Control Doug with W, A, S, D.)");
			GUI.player.summonPhantom(); GUI.player.phantomMoving = true;
			spawnPoint2();
		}
	};
	
	boolean justRespawned;
	
	public void spawnPoint2() {
		if (justRespawned) return;
		justRespawned = true;
		new Animation(.5) {
			public void event() {
				justRespawned = false;
				stop();
			}
		}.start();
		
		if (creatureSpawning != null) creatureSpawning.stop();
		cabin.agents.clear();
		
		Dungeon.setLocation(cabin);
		GUI.player.setXPos(250); GUI.player.setYPos(300);
		GUI.player.setRadius(20);
			
		GUI.graphics.setBackground(new Color(80, 75, 25));
		GUI.player.canSummon = true;
		Player.attackMode = true;
			
		cabinTop.close(); cabinBottom.close();
		cabinRight.close(); cabinLeft.close();
		doorToCabin.close();
		
		spawnCreatures(5);
	}
	
	/**************************************
	 * PRIVATE CLASSES
	 */
	
	private class GhostSeth extends Creature {
		public GhostSeth(int x, int y, int radius) {
			super(x, y, radius, radius, 0);

			xSize = radius;
			ySize = radius;
			
			color = Color.RED;
			collidesWithCreatures = true;
			canMove = false;
        }
		
		public void calculateMovements() {}
		
		public void collideIntoAgents(Entity collidee) {
			interior.agents.remove(collidee);
			GUI.currentSpeaker = null;
		}
	}
	
	// lights is technically in class LevelThree but shhh
	private static ArrayList<Lantern> lights = new ArrayList<Lantern>();
	
	private class LightHunter extends Creature {
		private Lantern currentLight;
		
		public LightHunter(Room room, int x, int y, int width, int height) {
			super(room, x, y, width, height, 0);
			color = Color.BLACK;
			collidesWithCreatures = false;
			
			lights.add(lanternUL);
			lights.add(lanternBL);
			lights.add(lanternUR);
			lights.add(lanternBR);
			findLantern();
			
			movesEvery = 8;
		}
		
		private void findLantern() {
			for (int i = 0; i < lights.size(); i++) {
				if (lights.get(i).isOn()) {
					currentLight = lights.get(i);
					return;
				}
			}
		}
		
		public void calculateMovements() {
			if (currentLight != null) {
				track(currentLight.getXPos(), currentLight.getYPos());
				if (!currentLight.isOn()) findLantern();
			}
			else track(GUI.player);
		}
	}
	
	private class Zombie extends Creature {		
		public Zombie(Room room, int x, int y, int width, int height) {
			super(room, x, y, width, height, 0);
			color = new Color((int) Random.bounded(0, 100),
					(int) Random.bounded(0, 100), (int) Random.bounded(0, 100));
			collidesWithCreatures = false;
			
			movesEvery = 12;
		}
		
		public void calculateMovements() {
			track(GUI.player);
		}
	}
	
} // end class LevelThree
