package level;

import java.awt.Color;

import agent.Creature;
import agent.Entity;
import agent.Player;
import animation.Animation;
import animation.AnimationMovement;
import animation.AnimationSequence;
import animation.Sound;
import animation.Speech;
import environment.AutomaticDoor;
import environment.Door;
import environment.Dungeon;
import environment.Room;
import environment.Wall;
import graphics.BackgroundPainting;
import graphics.GUI;
import graphics.Urinal;

public class LevelFour extends Level
{
	private Creature teenager1, teenager2;
	private Room bathroom;
	BackgroundPainting introspectiveToilet;
	AnimationSequence sequenceOne;
	
	public LevelFour() {
		initializeDungeon(1, 1, "Custom");
	}
	
	public void createRooms() {
		bathroom = dungeon.rooms.get(0).get(0);
		GUI.graphics.setBackground(new Color(240, 240, 240));
		GUI.player.canSummon = false;
		
		GUI.player.setXPos(360); GUI.player.setYPos(335);
		Dungeon.setLocation(bathroom);
		
		int width = GUI.graphics.getWidth();
		int height = GUI.graphics.getHeight() - 100;
		
		teenager1 = new Creature(bathroom, 40, 140, 30, 30);
		teenager2 = new Creature(bathroom, 40, 180, 30, 30);
		
		teenager1.freeze(); teenager2.freeze();
		
		bathroom.setSpawnLocation(40, 175);
		
		// door to hallway one:
		new Door(bathroom, 0, 150, 10, 50) {
			public boolean collisionEvent(Entity collider) {
				if (GUI.gameInProgress) {
					Sound.BACKGROUND.stop();
					Level.credits();
					GUI.gameInProgress = false;
				}
				return false;
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
   
		System.out.println(GUI.player);
		
		sequenceOne = new AnimationSequence(new Object[] {
				new Speech(teenager2, "I asked her. I did ask her. Don't say I didn't ask her."),
				new Speech(teenager1, "Uh-huh."),
				new Speech(teenager2, "Let me tell you something, dude. Us mortals? We were not meant to walk on the same ground as Hally Smith."),
				new Speech(GUI.player, ". . ."),
				new Speech(teenager2, "You had one chance. One chance."),
				new Speech(teenager1, "Oh, give me a break. I told you this would happen didn't I?"),
				new Speech(GUI.player, "<i>This again?"),
				new Speech(teenager2, "You were so, so close. That close!"),
				new Speech(teenager1, "I know! I get it!"),
				new Speech(GUI.player, ". . ."),
				new Speech(GUI.player, "<i>It's strange."),
				new Speech(teenager2, "Ask her again."),
				new Speech(teenager1, "What? Are you crazy?"),
				new Speech(GUI.player, "<i>Sometimes things just feel so . . . small."),
				new Speech(teenager2, "Ask her again! Do it tomorrow."),
				new Speech(teenager1, "She's not going to like, change her mind . . ."),
				new AnimationMovement(GUI.player, AnimationMovement.UP, 230, .005),
				new AnimationMovement(GUI.player, AnimationMovement.LEFT, 260, .005),
				new Speech(GUI.player, "Excuse me."),
				new Speech(teenager2, ". . ."),
				new AnimationMovement(teenager1, AnimationMovement.UP, 20),
				new AnimationMovement(teenager2, AnimationMovement.DOWN, 20),
		});
		
		GUI.player.speak("<i>(A month later . . .)");
		Player.attackMode = false;
		Sound.BACKGROUND.loop();
		Animation.enterDialogueMode(sequenceOne);
	}
}
