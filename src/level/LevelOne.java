package level;
import java.awt.Color;

import environment.AutomaticDoor;
import environment.Box;
import environment.DoorWithMessage;
import environment.Gate;
import environment.GateWithMessage;
import environment.SchoolDoor;
import environment.Wall;
import environment.Dungeon;
import environment.Room;
import graphics.BackgroundPainting;
import graphics.GUI;
import graphics.Urinal;
import agent.Entity;
import agent.Creature;
import agent.CreatureStudent;
import agent.Player;
import animation.Animation;
import animation.AnimationMovement;
import animation.AnimationSequence;
import animation.Sound;
import animation.Speech;

public class LevelOne extends Level
{
	Room hallwayOne, hallwayTwo, hallwayThree,
		emptyClassroomLeftOne, emptyClassroomLeftTwo, emptyClassroomRight;
	Room bathroom, classroom, janitorCloset;
	
	BackgroundPainting introspectiveToilet;
	
	Creature joe, tommy, teacher;
	Creature teenager1, teenager2;
	
	AnimationSequence dialogueSequenceOne, dialogueSequenceTwo,
	dialogueSequenceThree, dialogueSequenceFour;
	
	Animation countdownBell;
	
	DoorWithMessage backdoor;
	GateWithMessage doorToClassroom;
	SchoolDoor doorFromClassroom;
	
	SpawnPoint spawn1 = new SpawnPoint() {
		public void respawn() {
			GUI.player.setRadius(18);
			Level.setLevel(new LevelOne());
		}
	};
	
	SpawnPoint spawn2 = new SpawnPoint() {
		public void respawn() {
			GUI.player.setRadius(18);
			
			hallwayOne.agents.clear();
			hallwayTwo.agents.clear();
			hallwayThree.agents.clear();
			
			Dungeon.teleport(bathroom);
			spawnPoint2();
		}
	};
	
	public LevelOne() {		
		initializeDungeon(3, 3, "Custom");
		Level.currentSpawn = spawn1;
	}
	
	/*******************************/
	// ANIMATIONS
	
	public void countdownAnimation() {
		Sound.BELL.play();
		
		GUI.player.setXPos(250);
		GUI.player.setYPos(400);
		
		GUI.player.speak(1, "Was that the bell?");
		
		new Animation(60) {
			public void event() {
				countdownBell.stop();
				Animation.speakBackground(GUI.player, "I'm late . . .");
			}
		}.start();
		
		countdownBell = new Animation(1) {
			int secondsElapsed;
			
			public void event() {
				secondsElapsed++;
				int secondsLeft = 60 - secondsElapsed;

				Animation.speakBackground(1, GUI.player, "Hurry! Class starts in: " + secondsLeft);
			}
		};
		countdownBell.start();
		
		Player.setHelpText("I need to get to class . . .");
	}
	
	public void enterClassroomAnimation() {
		Animation.pause(3);
		Sound.BACKGROUND.loop();
		
		GUI.player.speak("<i>(Press space to advance dialogue.)");
		Animation.enterDialogueMode(dialogueSequenceOne);
	}
	
	public void enterBathroomAnimation() {
		introspectiveToilet.setMessage("");
		
		Animation.enterDialogueMode(dialogueSequenceTwo);
	}
	
	public void spawnFallingStudent(double delay) {
		new Animation(delay) {
			public void event() {
				createFallingStudent();
				
				double delay = Math.random() + 0.4;
				spawnFallingStudent(delay);
				stop();
			}
		}.start();
	}
	
	private void createFallingStudent() {
		Room hallway;
		Room currentRoom = Dungeon.location.getRoom(GUI.currentDungeon);
		
		if (currentRoom.equals(hallwayOne) ||
			currentRoom.equals(hallwayTwo) ||
			currentRoom.equals(hallwayThree)) {
			hallway = currentRoom;
		}
		else hallway = hallwayOne;
		
		CreatureStudent potentialStudent = null;
		
		switch((int) (Math.random() * 4) + 1) { // random number 1-4
		case 1:
			potentialStudent =  new CreatureStudent(hallway, 85, 50, 30, 30);
		case 2:
			potentialStudent = new CreatureStudent(hallway, 85, 190, 30, 30);
		case 3:
			potentialStudent = new CreatureStudent(hallway, 385, 50, 30, 30);
		case 4:
			potentialStudent = new CreatureStudent(hallway, 385, 190, 30, 30);
		}
		
		if (potentialStudent.collideAny()) {
			hallway.remove(potentialStudent);
			potentialStudent = null;
		}
	}
	
	public void enterClassroomAnimationTwo() {
		Animation.enterDialogueMode(dialogueSequenceThree);
	}
	
	public void nextLevel() {
		GUI.player.setRadius(20);
		Level.setLevel(new LevelTwo());
	}
	
	/*******************************************/
	// ARCHITECTURE
	
	public void createRooms() {
		hallwayOne = dungeon.rooms.get(1).get(0);
			emptyClassroomLeftOne = dungeon.rooms.get(0).get(0);
			bathroom = dungeon.rooms.get(2).get(0);
			
		hallwayTwo = dungeon.rooms.get(1).get(1);
			emptyClassroomLeftTwo = dungeon.rooms.get(0).get(1);
			emptyClassroomRight = dungeon.rooms.get(2).get(1);
			
		hallwayThree = dungeon.rooms.get(1).get(2);
			janitorCloset = dungeon.rooms.get(0).get(2);
			classroom = dungeon.rooms.get(2).get(2);
			
		final int height = GUI.graphics.getHeight() - 100;
		final int width = GUI.graphics.getWidth();
	
	/***********************
	 * HALLWAY ONE
	 */
		
		new Wall(hallwayOne, 0, 0, 75, height);
		new Wall(hallwayOne, width - 75, 0, 75, height);
		new Wall(hallwayOne, 0, height - 25, width, 25);
		
		new DoorWithMessage(hallwayOne, width / 2 - 25, height - 25, 50, 10, "", "I can't just leave . . .");
		new DoorWithMessage(hallwayOne, 65, 40, 10, 50, "", "It's locked.");
		new DoorWithMessage(hallwayOne, 65, 180, 10, 50, "", "It's locked.");
		new GateWithMessage(hallwayOne, 65, 320, 10, 50, emptyClassroomLeftOne, "It's empty.", "");
		new DoorWithMessage(hallwayOne, width - 75, 40, 10, 50, "", "It's locked.");
		new DoorWithMessage(hallwayOne, width - 75, 180, 10, 50, "", "It's locked.");
		new SchoolDoor(hallwayOne, width - 75, 320, 10, 50, bathroom);
		
		// exit:
		new Gate(hallwayOne, 0, -10, width, 10, hallwayTwo) {
			public boolean collisionEvent(Entity collider) {
				hallwayTwo.setSpawnLocation(GUI.player.getXPos(), GUI.graphics.getHeight() - 130);
				return super.collisionEvent(collider);
			}
		};
		
	/**************************
	 * EMPTY CLASSROOM LEFT 1
	 */
		
		emptyClassroomLeftOne.setSpawnLocation(width - 50, height / 2);
		
		new Wall(emptyClassroomLeftOne, 0, 0, width, 30);
		new Wall(emptyClassroomLeftOne, 0, 0, 30, height);
		new Wall(emptyClassroomLeftOne, 0, height - 30, width, 30);
		new Wall(emptyClassroomLeftOne, width - 30, 0, 30, height);
		
		// door to hallway one:
		new SchoolDoor(emptyClassroomLeftOne, width - 30, height / 2 - 25, 10, 50, hallwayOne) {
			public boolean collisionEvent(Entity collider) {
				hallwayOne.setSpawnLocation(95, 345);
				return super.collisionEvent(collider);
			}
		};
		
	/*************************
	 * BATHROOM
	 */
		// later will appear in the bathroom:
		teenager1 = new Creature(20, 160, 30, 30);
		teenager2 = new Creature(20, 160, 30, 30);
		
		teenager1.freeze(); teenager2.freeze();
		
		bathroom.setSpawnLocation(40, 175);
		
		// door to hallway one:
		new SchoolDoor(bathroom, 0, 150, 10, 50, hallwayOne) {
			public boolean collisionEvent(Entity collider) {
				hallwayOne.setSpawnLocation(GUI.graphics.getWidth() - 105, 345);
				return super.collisionEvent(collider);
			}
		};
		
		new Wall(bathroom, 0, 0, 30, 150);
		new Wall(bathroom, 0, 200, 30, height / 2);
		new Wall(bathroom, width - 100, 0, 100, height);
		new Wall(bathroom, 0, 0, width, 100);
		new Wall(bathroom, 0, height - 100, width, 100);
		
		// urinals:
		new Urinal(bathroom, 50, 330, 40, 35);
		new Urinal(bathroom, 110, 330, 40, 35);
		new Urinal(bathroom, 170, 330, 40, 35);
		
		// stalls:
		new Wall(bathroom, width - 180, 250, 10, 120);
		new Wall(bathroom, width - 260, 250, 10, 120);
		
		// stall doors:
		new AutomaticDoor(bathroom, width - 170, 250, 70, 10, AutomaticDoor.Orientation.VERTICAL);
		new AutomaticDoor(bathroom, width - 250, 250, 70, 10, AutomaticDoor.Orientation.VERTICAL);
		
		// BackgroundPaintings:
		introspectiveToilet = new BackgroundPainting(bathroom, width - 160, 335, 50, 30,
				Color.WHITE, Color.GRAY, 0);
		new BackgroundPainting(bathroom, width - 240, 335, 50, 30, Color.WHITE, Color.GRAY, 0).setMessage("The toilet seat is cold and uncomfortable.");;
		
		// sinks:
		new BackgroundPainting(bathroom, width - 160, 100, 50, 30, Color.BLUE, Color.GRAY, 0).setCollidable(true);
		new BackgroundPainting(bathroom, width - 240, 100, 50, 30, Color.BLUE, Color.GRAY, 0).setCollidable(true);
		
	/**************************
	 * HALLWAY TWO
	 */
		
		// back to hallway one:
		new Gate(hallwayTwo, 0, height + 10, width, 10, hallwayOne) {
			public boolean collisionEvent(Entity collider) {
				hallwayOne.setSpawnLocation(GUI.player.getXPos(), 30);
				return super.collisionEvent(collider);
			}
		};
				
		new Wall(hallwayTwo, 0, 0, 75, height);
		new Wall(hallwayTwo, width - 75, 0, 75, height); 

		new DoorWithMessage(hallwayTwo, 65, 40, 10, 50, "", "It's locked.");
		new GateWithMessage(hallwayTwo, 65, 180, 10, 50, emptyClassroomLeftTwo, "It's empty.", "");
		new DoorWithMessage(hallwayTwo, 65, 320, 10, 50, "", "It's locked.");
		new GateWithMessage(hallwayTwo, width - 75, 40, 10, 50, emptyClassroomRight, "It's empty.", "");
		new DoorWithMessage(hallwayTwo, width - 75, 180, 10, 50, "", "It's locked.");
		new DoorWithMessage(hallwayTwo, width - 75, 320, 10, 50, "", "It's locked.");
		
		// to hallway three:
		new Gate(hallwayTwo, 0, -10, width, 10, hallwayThree) {
			public boolean collisionEvent(Entity collider) {
				hallwayThree.setSpawnLocation(GUI.player.getXPos(), GUI.graphics.getHeight() - 130);
				return super.collisionEvent(collider);
			}
		};
	
	/*********************
	 * EMPTY CLASSROOM LEFT 2
	 */
		emptyClassroomLeftTwo.setSpawnLocation(width - 50, height / 2);
		
		new Wall(emptyClassroomLeftTwo, 0, 0, width, 30);
		new Wall(emptyClassroomLeftTwo, 0, 0, 30, height);
		new Wall(emptyClassroomLeftTwo, 0, height - 30, width, 30);
		new Wall(emptyClassroomLeftTwo, width - 30, 0, 30, height);
		
		// door to hallway three:
		new SchoolDoor(emptyClassroomLeftTwo, width - 30, height / 2 - 25, 10, 50, hallwayTwo) {
			public boolean collisionEvent(Entity collider) {
				hallwayTwo.setSpawnLocation(95, 205);
				return super.collisionEvent(collider);
			}
		};	
		
	/************************
	 * EMPTY CLASSROOM RIGHT
	 */

		emptyClassroomRight.setSpawnLocation(50, height / 2);
		
		new Wall(emptyClassroomRight, 0, 0, width, 30);
		new Wall(emptyClassroomRight, 0, 0, 30, height);
		new Wall(emptyClassroomRight, 0, height - 30, width, 30);
		new Wall(emptyClassroomRight, width - 30, 0, 30, height);
		
		// door to hallway three:
		new SchoolDoor(emptyClassroomRight, 20, height / 2 - 25, 10, 50, hallwayTwo) {
			public boolean collisionEvent(Entity collider) {
				hallwayTwo.setSpawnLocation(GUI.graphics.getWidth() - 105, 65);
				return super.collisionEvent(collider);
			}
		};
		
	/*************************
	 * HALLWAY THREE
	 */
	
		// back to hallway two:
		new Gate(hallwayThree, 0, height + 10, width, 10, hallwayTwo) {
			public boolean collisionEvent(Entity collider) {
				hallwayTwo.setSpawnLocation(GUI.player.getXPos(), 30);
				return super.collisionEvent(collider);
			}
		};
		
		new Wall(hallwayThree, 0, 0, width, 15);
		new Wall(hallwayThree, 0, 0, 75, height);
		new Wall(hallwayThree, width - 75, 0, 75, height); 
		
		backdoor = new DoorWithMessage(hallwayThree, width / 2 - 25, 5, 50, 10, "", "I can't just leave . . .");
		backdoor.setEvent(new Animation(.01) {
			public void event() {
				if (backdoor.isOpen()) {
					nextLevel();
					backdoor.clearEvent();
					System.out.println("Next level");
				}
				System.out.println("touched");
				stop();
			}
		});
		
		new GateWithMessage(hallwayThree, 65, 40, 10, 50, janitorCloset, "The air is full of dust in here.", "");
		new DoorWithMessage(hallwayThree, 65, 180, 10, 50, "", "It's locked.");
		new DoorWithMessage(hallwayThree, 65, 320, 10, 50, "", "It's locked.");
		new DoorWithMessage(hallwayThree, width - 75, 40, 10, 50, "", "It's locked.");
		doorToClassroom = new GateWithMessage(hallwayThree, width - 75, 180, 10, 50, classroom, "", "") {
			public boolean collisionEvent(Entity collider) {
				Animation.endAll();
				return super.collisionEvent(collider);
			}
		};
		new DoorWithMessage(hallwayThree, width - 75, 320, 10, 50, "", "It's locked.");		
		
	/**********************
	 * JANITOR CLOSET
	 */

		janitorCloset.setSpawnLocation(width - 30, height / 2);
		
		new Wall(janitorCloset, 0, 0, width, 120);
		new Wall(janitorCloset, 0, 0, 200, height);
		new Wall(janitorCloset, 0, height - 120, width, 120);
		new Wall(janitorCloset, width - 10, 0, 10, height);
		
		new Box(janitorCloset, 200, 120, 55, 55);
		new Box(janitorCloset, 255, 120, 35, 25);
		new Box(janitorCloset, 255, 145, 15, 20);
		new Box(janitorCloset, 200, height - 150, 30, 30);
		new Box(janitorCloset, 230, height - 170, 40, 50);
		new Box(janitorCloset, 315, 225, 25, 25);
		
		
		// exit door:
		new SchoolDoor(janitorCloset, width - 10, height / 2 - 25, 10, 50, hallwayThree) {
			public boolean collisionEvent(Entity collider) {
				hallwayThree.setSpawnLocation(100, 65);
				return super.collisionEvent(collider);
			}
		};
		
	/*********************
	 * CLASSROOM
	 */
		
		classroom.setSpawnLocation(60, height / 2);
		
		classroom.setEntranceEvent(new Animation(.01) {
			public void event() {
				classroom.setEntranceEvent(null);
				
				Animation.backgroundSpeaker = null;
				
				// This defines what happens when you try to enter classroom again:
				doorToClassroom.close();
				doorToClassroom.setClosedColor(Color.LIGHT_GRAY);
				doorToClassroom.closedMessage = "I need to go to the bathroom to clear my head.";
				
				enterClassroomAnimation();
				stop();
			}
		});
		
		new Wall(classroom, 0, 0, width, 30);
		new Wall(classroom, 0, 0, 30, height);
		new Wall(classroom, 0, height - 30, width, 30);
		new Wall(classroom, width - 30, 0, 30, height);
		
		// door to hallway three:
		doorFromClassroom = new SchoolDoor(classroom, 20, height / 2 - 25, 10, 50, hallwayThree) {
			public boolean collisionEvent(Entity collider) {
				hallwayThree.setSpawnLocation(GUI.graphics.getWidth() - 105, 205);
				return super.collisionEvent(collider);
			}
		};
		
		new Creature(classroom, (int) (.3 * (width - 60)), (int) (.25 * (width - 60)), 30, 30).freeze();
		new Creature(classroom, (int) (.3 * (width - 60)), (int) (.4 * (width - 60)), 30, 30).freeze();
		new Creature(classroom, (int) (.3 * (width - 60)), (int) (.55 * (width - 60)), 30, 30).freeze();
		new Creature(classroom, (int) (.3 * (width - 60)), (int) (.7 * (width - 60)), 30, 30).freeze();
		new Creature(classroom, (int) (.45 * (width - 60)), (int) (.25 * (width - 60)), 30, 30).freeze();
		joe = new Creature(classroom, (int) (.45 * (width - 60)), (int) (.4 * (width - 60)), 30, 30);
		tommy = new Creature(classroom, (int) (.45 * (width - 60)), (int) (.55 * (width - 60)), 30, 30);
		new Creature(classroom, (int) (.45 * (width - 60)), (int) (.7 * (width - 60)), 30, 30).freeze();
		new Creature(classroom, (int) (.6 * (width - 60)), (int) (.25 * (width - 60)), 30, 30).freeze();
		// This is Ryan's spot.
		new Creature(classroom, (int) (.6 * (width - 60)), (int) (.55 * (width - 60)), 30, 30).freeze();
		new Creature(classroom, (int) (.6 * (width - 60)), (int) (.7 * (width - 60)), 30, 30).freeze();
		
		joe.freeze(); tommy.freeze();
		
		teacher = new Creature(classroom, (int) (.85 * (width - 60)), (int) (.5 * (width - 60)) - 17, 35, 35);
			teacher.freeze();
			teacher.setColor(Color.GREEN);
		
		/*********************************************/
		// ANIMATION SEQUENCES:
			
		dialogueSequenceOne = new AnimationSequence(new Object[] {
				new Speech(teacher, "Oh! Why . . . hello."),
				new Speech(GUI.player, "Hi Miss Adams."),
				new Speech(teacher, "What's your name?"),
				new Speech(GUI.player, "Ryan."),
				new Speech(teacher, "Why don't you, ah . . . take a seat, Ryan?"),
				new AnimationMovement(GUI.player, AnimationMovement.DOWN, 160),
				new AnimationMovement(GUI.player, AnimationMovement.RIGHT, 275),
				new AnimationMovement(GUI.player, AnimationMovement.UP, 200),
				new AnimationMovement(GUI.player, AnimationMovement.LEFT, 65),
				new Speech(teacher, "Now. As I was saying . . ."),
				new Speech(teacher, ". . . will be fifteen percent . . ."),
				new Speech(tommy, "Psst . . . Joe . . ."),
				new Speech(joe, "?"),
				new Speech(tommy, "What's up with him?"),
				new Speech(joe, "Shut up . . . he's right there . . ."),
				new Speech(teacher, ". . . and homework blah . . ."),
				new Speech(teacher, ". . . blah test blah . . ."),
				new Speech(tommy, "Dude . . . but like . . ."),
				new Speech(joe, "Shut up!"),
				new Speech(teacher, "Tommy! Do I need to send your mother a note?"),
				new Speech(joe, "Yeah, Tommy."),
				new Speech(teacher, "Joe!"),
				new Speech(joe, "Yes ma'am, sorry ma'am."),
				new Speech(teacher, "Anyways. Blah blah blah, blah, in some cases, blah, in other cases, blah blah."),
				new Speech(teacher, "Sometimes there will even blah blah if you are especially blah. Blah blah blah?"),
				new Speech(GUI.player, ". . ."),
				new Speech(teacher, "Ryan? Are you alright?"),
				new Speech(GUI.player, "May I please use the restroom?"),
				new Speech(teacher, "Go right ahead."),
		// end of Object[] parameters, beginning of method overriding:
		}) {
			public void finishAnimationEvent() {
				Player.setHelpText("I need to go to the bathroom to clear my head.");

				bathroom.setEntranceEvent(new Animation(.01) {
					public void event() {
						GUI.player.speak("<i>(Sighs.)");
						enterBathroomAnimation();
						bathroom.setEntranceEvent(null); // one time event
						stop();
					}
				});
			}
		};

		dialogueSequenceTwo = new AnimationSequence(new Object[] {
				new AnimationMovement(GUI.player, AnimationMovement.RIGHT, 315),
				new AnimationMovement(GUI.player, AnimationMovement.DOWN, 250),
				new Speech(GUI.player, "Oh well. What were you expecting, anyways? A miracle?"),
				new Speech(GUI.player, "It's just that sometimes . . ."),
				new Animation(.01) {
					public void event() {
						teenager1.setRoom(bathroom);
						stop();
					}
				},
				new AnimationMovement(teenager1, AnimationMovement.RIGHT, 50),
				new Animation(.01) {
					public void event() {
						teenager2.setRoom(bathroom);
						stop();
					}
				},
				
				new Speech(teenager1, "Dude, are you in a class with Hally?"),
				new Speech(teenager2, "Yeah."),
				new Animation(.01){
					public void event() {
						new AnimationMovement(teenager1, AnimationMovement.RIGHT, 193) {
							public void terminationEvent() {
								new AnimationMovement(teenager1, AnimationMovement.UP, 40).start();
							}
						}.start();

						new AnimationMovement(teenager2, AnimationMovement.RIGHT, 35) {
							public void terminationEvent() {
								new AnimationMovement(teenager2, AnimationMovement.DOWN, 150).start();
							}
						}.start();
						stop();
					}
				},
				new Speech(teenager1, "Oh my God, dude, you are stacked."),
				new Speech(teenager2, "She sits right in front of me."),
				new Speech(teenager1, "You're not going back there? Grab her before someone else does."),
				new Speech(teenager2, "Are you kidding me? It's history. Even Hally's not worth history."),
				new Speech(GUI.player, "<i>They're skipping class . . . on the first day of school . . ."),
				new AnimationMovement(teenager1, AnimationMovement.LEFT, 210),
				new Speech(teenager1, "I would do it. I would make that sacrifice."),
				new Speech(teenager2, "Oh, shut up."),
				new AnimationMovement(teenager2, AnimationMovement.UP, 130),
				new Speech(teenager1, "I'm serious. You have to do these kinds of things. Life is short."),
				new Speech(teenager2, "Which is why I'm not going to spend it in history class."),
				new Speech(teenager1, "Oh, stop being a wimp. It's only once. Talk to her. Get her number. Take her out."),
				new Speech(teenager2, "I will."),
				new Speech(teenager1, "Do it now."),
				new Speech(teenager2, "I said I will."),
				new Speech(teenager1, "You're going to put it off, aren't you?"),
				new Speech(teenager2, "No."),
				new Speech(teenager1, "Yeah, you are. You said you were going to ask Christine out last year, and when did that ever happen?"),
				new Speech(teenager2, "I did. We were at a party. I asked her to dance."),
				new Speech(teenager1, "She couldn't even hear you."),
				new Speech(teenager2, "Well I did ask her."),
				new Speech(teenager1, "The party was almost over."),
				new Speech(teenager2, "Asking is asking, okay?"),
				new Speech(teenager1, "Not if the asking never got across."),
				new Speech(teenager2, "And when was the last time you asked someone out?"),
				new Speech(GUI.player, "<i>I need to get back to class . . ."),
				new Speech(teenager1, "I would. If I was in your history class, I would."),
				new Speech(teenager2, "Easily said, my friend. Easily said."),
				new Speech(teenager1, "I would!"),
				new Speech(GUI.player, "<i>I don't think they know I'm here."),
				new Speech(teenager1, "Listen. You're going to put it off and put it off and then what? You know what happens next?"),
				new Speech(teenager2, "I know."),
				new Speech(teenager1, "Bam. End of the year. Hally has a boyfriend, you're single, the opportunity's gone."),
				new Speech(teenager2, "I know."),
				new Speech(teenager1, "This is your childhood, man. Your teenage years. Best time of your life. Don't waste it."),
				new Speech(teenager2, "Alright. Okay. Tomorrow. I'll talk to her tomorrow."),
				new Speech(teenager1, "You know those old guys, those old guys who sit on porches and stuff? You know what I bet they're thinking about all the time?"),
				new Speech(teenager2, "I said I'd do it."),
				new Speech(teenager1, "Girls, man. That’s why there are pedophiles in this world. Some smelly old guy never had the balls to ask out his crush, and it sticks with him forever."),
				new Speech(teenager2, "I get it."),
				new Speech(GUI.player, "<i>When are they going to leave?"),
				new Speech(teenager1, "I'm looking out for you, dude. Don't think I'm just trying to pester you. I'm trying to keep you from being a pedophile."),
				new Speech(teenager2, "Yeah, sure. Who do you like?"),
				new Speech(teenager1, "Huh?"),
				new Speech(teenager2, "Who do you like."),
				new Speech(teenager1, "Oh, people. I have people in mind."),
				new Speech(teenager2, "Who?"),
				new Speech(teenager1, "Don't you worry about me. I'm all set."),
				new Speech(teenager2, "You don't sound like it."),
				new Speech(teenager1, "<i>(Sighs.)"),
				new Speech(teenager1, "Karla M., Brittany, Ashley."),
				new Speech(teenager2, "Ashley?"),
				new Speech(teenager1, "You know. Redhead with the coffee."),
				new Speech(teenager2, "Eww."),
				new Animation(.01) {
					public void event() {
						Sound.BELL.play();
						Sound.HALLWAY_NOISE_QUIET.loop();
						stop();
					}
				},
				new Speech(GUI.player, "<i>Was that the bell? Is class over?"),
				new Speech(teenager1, "There's something about her. I think it's the eyes."),
				new Speech(teenager2, "Yeah, yeah. Think what you want."),
				new Speech(teenager1, "I'm serious, dude!"),
				new Speech(teenager1, "Have you ever seen her talk about books?"),
				new Speech(teenager2, "Books?"),
				new AnimationMovement(teenager2, AnimationMovement.UP, 20) {
					public void terminationEvent() {
						Level.currentSpawn = spawn2;
						new AnimationMovement(teenager2, AnimationMovement.LEFT, 40) {
							public void terminationEvent() {
								bathroom.remove(teenager2);
								teenager2 = null;
								GUI.currentSpeaker = null;
							}
						}.start();
					}
				},
				new AnimationMovement(teenager1, AnimationMovement.DOWN, 30),
				new Speech(teenager1, "They light up, dude. They're like flashlights. Little flashlights in her head. And you're like a deer, seeing the lights, right? . . ."),
				new AnimationMovement(teenager1, AnimationMovement.LEFT, 30) {
					public void terminationEvent() {
						bathroom.remove(teenager1);
						teenager1 = null;
						GUI.currentSpeaker = null;
					}
				},
				new Speech(GUI.player, "I need to talk to Miss Adams . . .")
		// end of Object[] parameters, beginning of method overriding:
		}) {
			public void finishAnimationEvent() {
				doorToClassroom.open();
				
				Player.setHelpText("I need to talk to Mrs. Adams.");
				
				classroom.setEntranceEvent(new Animation(.01) {
					public void event() {
						enterClassroomAnimationTwo();
						classroom.agents.clear();
						
						teacher.setXPos(300);
						teacher.setYPos(100);
						teacher.setColor(Color.GREEN);
						classroom.add(teacher);
						
						Sound.HALLWAY_NOISE_LOUD.stop();
						Sound.HALLWAY_NOISE_QUIET.loop();
						
						GUI.player.speak(1, "Miss Adams--");
						new AnimationMovement(GUI.player, AnimationMovement.RIGHT, 200, .003).start();
						
						classroom.setEntranceEvent(null);
						stop();
					}
				});
				hallwayOne.setEntranceEvent(new Animation(.01) {
					public void event() {
						Sound.HALLWAY_NOISE_QUIET.stop();
						Sound.HALLWAY_NOISE_LOUD.loop();
						// spawn one and they all come:
						spawnFallingStudent(.01);
						hallwayOne.setEntranceEvent(new Animation(.01) {
							public void event() {
								hallwayOne.agents.clear();
								Animation.endAll();
								GUI.currentSpeaker = null;
								spawnFallingStudent(.01);
							}
						});
						stop();
					}
				});
				hallwayTwo.setEntranceEvent(new Animation(.01) {
					public void event() {
						hallwayTwo.agents.clear();
						Animation.endAll();
						GUI.currentSpeaker = null;
						spawnFallingStudent(.01);
					}
				});
				hallwayThree.setEntranceEvent(new Animation(.01) {
					public void event() {
						hallwayThree.agents.clear();
						Animation.endAll();
						GUI.currentSpeaker = null;
						spawnFallingStudent(.01);
					}
				});
			}
		};
		
		dialogueSequenceThree = new AnimationSequence(new Object[] {
				new Speech(teacher, "Ryan, it's okay."),
				new Speech(GUI.player, "I'm sorry."),
				new Speech(teacher, "Come in after school tomorrow. You're not in trouble, but I need to catch you up."),
				new Speech(GUI.player, "Thank you."),
				new Speech(teacher, "Alright, hun. Take care."),
				new Speech(GUI.player, "Miss Adams?"),
				new Speech(teacher, "Mhmm?"),
				new Speech(GUI.player, "Is there something . . . different about me?"),
				new Speech(teacher, ". . ."),
				new Speech(GUI.player, "Sometimes, it just feels . . . never mind."),
				new Speech(teacher, "No, you're right."),
				new Speech(GUI.player, ". . ."),
				new Speech(teacher, "I used to be like you too."),
				new Speech(GUI.player, "You mean . . . ?"),
				new Speech(teacher, "Yes."),
				new Speech(GUI.player, "But you aren't like that now."),
				new Speech(teacher, "No."),
				new Speech(GUI.player, "What happened?"),
				new Speech(teacher, "It doesn't matter."),
				new Speech(GUI.player, "Please?"),
				new Speech(teacher, "Ryan . . ."),
				new Speech(teacher, "<i>(Sighs.)"),
				new Speech(teacher, "I had a mentor, Ryan. He taught me how to change who I was."),
				new Speech(GUI.player, "Who?"),
				new Speech(teacher, "It was a long time ago. He probably isn't around anymore."),
				new Speech(GUI.player, "But--"),
				new Speech(teacher, "Ryan, what I did was okay when I did it. Things are different now."),
				new Speech(GUI.player, "Miss Adams, please."),
				new Speech(teacher, "I'm sorry."),
		}) {
			public void finishAnimationEvent() {
				doorFromClassroom.close();
				doorFromClassroom = new SchoolDoor(classroom, 20, height / 2 - 25, 10, 50, hallwayThree) {
					public boolean collisionEvent(Entity collider) {
						teacher.speak("Ryan.");
						Animation.enterDialogueMode(dialogueSequenceFour);
						hallwayThree.setSpawnLocation(GUI.graphics.getWidth() - 105, 205);
						return true;
					}
				};
			}
		};
		
		dialogueSequenceFour = new AnimationSequence(new Object[] {
				new Speech(GUI.player, "Yes Miss Adams?"),
				new Speech(teacher, "He lived in the woods behind the school.")
		}) {
			public void finishAnimationEvent() {	
				Player.setHelpText("The woods are out the back door.");
				
				hallwayOne.agents.clear();
				hallwayTwo.agents.clear();
				hallwayThree.agents.clear();
				
				hallwayOne.setEntranceEvent(null);
				hallwayTwo.setEntranceEvent(null);
				hallwayThree.setEntranceEvent(null);
				
				hallwayThree.setSpawnLocation(250, 250);
				Dungeon.teleport(hallwayThree);
				
				Sound.HALLWAY_NOISE_QUIET.stop();
				
				backdoor.open();
				
				doorToClassroom.close();
				doorToClassroom.closedMessage = "The closest way to the woods is the back door.";
			}
		};
		
		
		
		GUI.player.canSummon = false;
		Dungeon.setLocation(hallwayOne);
		
		countdownAnimation();
	}
	
	/*******************************************/
	// RESPAWNING
	
	private void spawnPoint2() {
		doorToClassroom.open();
		
		classroom.setEntranceEvent(null);
		classroom.setEntranceEvent(new Animation(.01) {
			public void event() {
				enterClassroomAnimationTwo();
				classroom.agents.clear();
				
				teacher.setXPos(300);
				teacher.setYPos(100);
				teacher.setColor(Color.GREEN);
				classroom.add(teacher);
				
				Sound.HALLWAY_NOISE_LOUD.stop();
				Sound.HALLWAY_NOISE_QUIET.loop();
				
				GUI.player.speak(1, "Miss Adams--");
				new AnimationMovement(GUI.player, AnimationMovement.RIGHT, 200, .003).start();
				
				classroom.setEntranceEvent(null);
				stop();
			}
		});
		hallwayOne.setEntranceEvent(new Animation(.01) {
			public void event() {
				Sound.HALLWAY_NOISE_QUIET.stop();
				Sound.HALLWAY_NOISE_LOUD.loop();
				// spawn one and they all come:
				spawnFallingStudent(.01);
				hallwayOne.setEntranceEvent(new Animation(.01) {
					public void event() {
						hallwayOne.agents.clear();
						Animation.endAll();
						GUI.currentSpeaker = null;
						spawnFallingStudent(.01);
					}
				});
				stop();
			}
		});
		hallwayTwo.setEntranceEvent(new Animation(.01) {
			public void event() {
				hallwayTwo.agents.clear();
				Animation.endAll();
				GUI.currentSpeaker = null;
				spawnFallingStudent(.01);
			}
		});
		hallwayThree.setEntranceEvent(new Animation(.01) {
			public void event() {
				hallwayThree.agents.clear();
				Animation.endAll();
				GUI.currentSpeaker = null;
				spawnFallingStudent(.01);
			}
		});
	}
}

