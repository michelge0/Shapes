package level;
import java.awt.*;

import agent.Animal;
import agent.Player;
import animation.Sound;
import environment.Dungeon;
import environment.Grove;
import environment.RoomForest;
import graphics.GUI;

public class LevelTwo extends Level
{	
	static Grove test;
	
	SpawnPoint spawn2 = new SpawnPoint() {
		public void respawn() {
			GUI.player.setRadius(20);
			Level.setLevel(new LevelTwo());
		}
	};
	
	public LevelTwo() {
		initializeDungeon(8, 8, "Forest");
	}
	
	public static void nextLevel() {
		GUI.player.setRadius(23);
		Level.setLevel(new LevelThree());
	}
	
	public void createRooms() {
		GUI.player.setRadius(20);
		GUI.player.canSummon = false;
		
		Player.setHelpText("I need to go deeper into the woods to find him. The trees are thick, but there are openings where I can get through.");
		
		Sound.BACKGROUND.stop();
		
		currentSpawn = spawn2;
		
		final int width = GUI.graphics.getWidth();
		final int height = GUI.graphics.getHeight() - 100;
				
		GUI.graphics.setBackground(new Color(150, 125, 75));
		
		Dungeon.setLocation(dungeon.rooms.get(0).get(0));
		RoomForest thisRoom = ((RoomForest)Dungeon.location.getRoom(dungeon));
				
		Animal introCreature = new Animal(thisRoom, 330, 130, 30, 30);
		introCreature.setColor(new Color(190, 115, 0));
		introCreature.setSpeed(8);
		
		GUI.player.speak(10, "There are wild animals in this forest . . . I better be careful.");
		
		thisRoom.createWalls(width, height);
		
		for (int i = 0; i < thisRoom.groves.size(); i++) {
			thisRoom.groves.get(i).clearArea(0, 350, 150, 150); // player spawn
			thisRoom.groves.get(i).clearArea(300, 100, 100, 100); // intro creature spawn
		}
		
		Player.attackMode = true;
		Dungeon.spawn(50, 430);
	}
}
