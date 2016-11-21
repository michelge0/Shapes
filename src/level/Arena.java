package level;

import graphics.GUI;
import environment.Dungeon;
import environment.Wall;
import environment.Room;

public class Arena extends Level
{
	Room arena;
	
	public Arena() {
		initializeDungeon(1, 1, "Custom");
	}
	
	public void createRooms() {
		arena = dungeon.rooms.get(0).get(0);
		Dungeon.setLocation(arena);
		
		int width = GUI.graphics.getWidth();
		int height = GUI.graphics.getHeight() - 100;
		
		new Wall(arena, 0, -10, width, 10);
		new Wall(arena, -10, 0, 10, height);
		new Wall(arena, width, 0, 10, height);
		new Wall(arena, 0, height, width, 10);
	}
}
